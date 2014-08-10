package net.mcthunder.packetlib.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.mcthunder.packetlib.*;
import net.mcthunder.packetlib.packet.PacketProtocol;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class TcpSessionFactory implements SessionFactory {

    private Proxy clientProxy;

    public TcpSessionFactory() {
    }

    public TcpSessionFactory(Proxy clientProxy) {
        this.clientProxy = clientProxy;
    }

    @Override
    public Session createClientSession(final Client client) {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = null;
        if (this.clientProxy != null) {
            group = new OioEventLoopGroup();
            bootstrap.channelFactory(new ProxyOioChannelFactory(this.clientProxy));
        } else {
            group = new NioEventLoopGroup();
            bootstrap.channel(NioSocketChannel.class);
        }

        final TcpSession session = new TcpSession(client.getHost(), client.getPort(), client.getPacketProtocol(), group, bootstrap);
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            public void initChannel(Channel ch) throws Exception {
                session.getPacketProtocol().newClientSession(client, session);
                ch.config().setOption(ChannelOption.IP_TOS, 0x18);
                ch.config().setOption(ChannelOption.TCP_NODELAY, false);
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("timer", new ReadTimeoutHandler(30));
                if (session.getPacketProtocol().needsPacketEncryptor()) {
                    pipeline.addLast("encryption", new TcpPacketEncryptor(session));
                }

                if (session.getPacketProtocol().needsPacketSizer()) {
                    pipeline.addLast("sizer", new TcpPacketSizer(session));
                }

                pipeline.addLast("codec", new TcpPacketCodec(session));
                pipeline.addLast("manager", session);
            }
        }).group(group).remoteAddress(client.getHost(), client.getPort());
        return session;
    }

    @Override
    public ConnectionListener createServerListener(final Server server) {
        final EventLoopGroup group = new NioEventLoopGroup();
        return new TcpConnectionListener(server.getHost(), server.getPort(), group, new ServerBootstrap().channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<Channel>() {
            @Override
            public void initChannel(Channel ch) throws Exception {
                InetSocketAddress address = (InetSocketAddress) ch.remoteAddress();
                PacketProtocol protocol = server.createPacketProtocol();
                TcpSession session = new TcpServerSession(address.getHostName(), address.getPort(), protocol, null, null, server);
                session.getPacketProtocol().newServerSession(server, session);
                ch.config().setOption(ChannelOption.IP_TOS, 0x18);
                ch.config().setOption(ChannelOption.TCP_NODELAY, false);
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("timer", new ReadTimeoutHandler(30));
                if (session.getPacketProtocol().needsPacketEncryptor()) {
                    pipeline.addLast("encryption", new TcpPacketEncryptor(session));
                }

                if (session.getPacketProtocol().needsPacketSizer()) {
                    pipeline.addLast("sizer", new TcpPacketSizer(session));
                }

                pipeline.addLast("codec", new TcpPacketCodec(session));
                pipeline.addLast("manager", session);
                server.addSession(session);
            }
        }).group(group).localAddress(server.getHost(), server.getPort()).bind().syncUninterruptibly().channel());
    }

}
