package net.mcthunder.protocol;

import net.mcthunder.auth.GameProfile;
import net.mcthunder.auth.SessionService;
import net.mcthunder.auth.exception.AuthenticationException;
import net.mcthunder.auth.exception.AuthenticationUnavailableException;
import net.mcthunder.auth.exception.InvalidCredentialsException;
import net.mcthunder.packetlib.event.session.ConnectedEvent;
import net.mcthunder.packetlib.event.session.PacketReceivedEvent;
import net.mcthunder.packetlib.event.session.PacketSentEvent;
import net.mcthunder.packetlib.event.session.SessionAdapter;
import net.mcthunder.protocol.data.status.ServerStatusInfo;
import net.mcthunder.protocol.data.status.handler.ServerInfoHandler;
import net.mcthunder.protocol.data.status.handler.ServerPingTimeHandler;
import net.mcthunder.protocol.packet.handshake.client.HandshakePacket;
import net.mcthunder.protocol.packet.ingame.client.ClientKeepAlivePacket;
import net.mcthunder.protocol.packet.ingame.server.ServerDisconnectPacket;
import net.mcthunder.protocol.packet.ingame.server.ServerKeepAlivePacket;
import net.mcthunder.protocol.packet.login.client.EncryptionResponsePacket;
import net.mcthunder.protocol.packet.login.client.LoginStartPacket;
import net.mcthunder.protocol.packet.login.server.EncryptionRequestPacket;
import net.mcthunder.protocol.packet.login.server.LoginDisconnectPacket;
import net.mcthunder.protocol.packet.login.server.LoginSuccessPacket;
import net.mcthunder.protocol.packet.status.client.StatusPingPacket;
import net.mcthunder.protocol.packet.status.client.StatusQueryPacket;
import net.mcthunder.protocol.packet.status.server.StatusPongPacket;
import net.mcthunder.protocol.packet.status.server.StatusResponsePacket;
import net.mcthunder.protocol.util.CryptUtil;

import javax.crypto.SecretKey;
import java.math.BigInteger;

public class ClientListener extends SessionAdapter {

    private SecretKey key;


    @Override
    public void packetReceived(PacketReceivedEvent event) {
        MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
        if (protocol.getMode() == ProtocolMode.LOGIN) {
            if (event.getPacket() instanceof EncryptionRequestPacket) {
                EncryptionRequestPacket packet = event.getPacket();
                this.key = CryptUtil.generateSharedKey();

                GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
                String serverHash = new BigInteger(CryptUtil.getServerIdHash(packet.getServerId(), packet.getPublicKey(), this.key)).toString(16);
                String accessToken = event.getSession().getFlag(ProtocolConstants.ACCESS_TOKEN_KEY);
                try {
                    new SessionService().joinServer(profile, accessToken, serverHash);
                } catch (AuthenticationUnavailableException e) {
                    event.getSession().disconnect("Login failed: Authentication service unavailable.");
                    return;
                } catch (InvalidCredentialsException e) {
                    event.getSession().disconnect("Login failed: Invalid login session.");
                    return;
                } catch (AuthenticationException e) {
                    event.getSession().disconnect("Login failed: Authentication error: " + e.getMessage());
                    return;
                }

                event.getSession().send(new EncryptionResponsePacket(this.key, packet.getPublicKey(), packet.getVerifyToken()));
            } else if (event.getPacket() instanceof LoginSuccessPacket) {
                LoginSuccessPacket packet = event.getPacket();
                event.getSession().setFlag(ProtocolConstants.PROFILE_KEY, packet.getProfile());
                protocol.setMode(ProtocolMode.GAME, true, event.getSession());
            } else if (event.getPacket() instanceof LoginDisconnectPacket) {
                LoginDisconnectPacket packet = event.getPacket();
                event.getSession().disconnect(packet.getReason().getFullText());
            }
        }

        if (protocol.getMode() == ProtocolMode.STATUS) {
            if (event.getPacket() instanceof StatusResponsePacket) {
                ServerStatusInfo info = event.<StatusResponsePacket>getPacket().getInfo();
                ServerInfoHandler handler = event.getSession().getFlag(ProtocolConstants.SERVER_INFO_HANDLER_KEY);
                if (handler != null) {
                    handler.handle(event.getSession(), info);
                }

                event.getSession().send(new StatusPingPacket(System.nanoTime() / 1000000));
            } else if (event.getPacket() instanceof StatusPongPacket) {
                long time = System.nanoTime() / 1000000 - event.<StatusPongPacket>getPacket().getPingTime();
                ServerPingTimeHandler handler = event.getSession().getFlag(ProtocolConstants.SERVER_PING_TIME_HANDLER_KEY);
                if (handler != null) {
                    handler.handle(event.getSession(), time);
                }

                event.getSession().disconnect("Finished");
            }
        }

        if (protocol.getMode() == ProtocolMode.GAME) {
            if (event.getPacket() instanceof ServerKeepAlivePacket) {
                event.getSession().send(new ClientKeepAlivePacket(event.<ServerKeepAlivePacket>getPacket().getPingId()));
            } else if (event.getPacket() instanceof ServerDisconnectPacket) {
                event.getSession().disconnect(event.<ServerDisconnectPacket>getPacket().getReason().getFullText());
            }
        }
    }

    @Override
    public void packetSent(PacketSentEvent event) {
        MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
        if (protocol.getMode() == ProtocolMode.LOGIN && event.getPacket() instanceof EncryptionResponsePacket) {
            protocol.enableEncryption(this.key);
        }
    }

    @Override
    public void connected(ConnectedEvent event) {
        MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
        if (protocol.getMode() == ProtocolMode.LOGIN) {
            GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
            protocol.setMode(ProtocolMode.HANDSHAKE, true, event.getSession());
            event.getSession().send(new HandshakePacket(ProtocolConstants.PROTOCOL_VERSION, event.getSession().getHost(), event.getSession().getPort(), 2));
            protocol.setMode(ProtocolMode.LOGIN, true, event.getSession());
            event.getSession().send(new LoginStartPacket(profile != null ? profile.getName() : ""));
        } else if (protocol.getMode() == ProtocolMode.STATUS) {
            protocol.setMode(ProtocolMode.HANDSHAKE, true, event.getSession());
            event.getSession().send(new HandshakePacket(ProtocolConstants.PROTOCOL_VERSION, event.getSession().getHost(), event.getSession().getPort(), 1));
            protocol.setMode(ProtocolMode.STATUS, true, event.getSession());
            event.getSession().send(new StatusQueryPacket());
        }
    }

}
