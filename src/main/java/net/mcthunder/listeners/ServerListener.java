package net.mcthunder.listeners;

import net.mcthunder.apis.MinecraftProtocol;
import net.mcthunder.apis.ProtocolMode;
import net.mcthunder.apis.ServerInfoBuilder;
import net.mcthunder.auth.GameProfile;
import net.mcthunder.auth.expection.AuthenticationUnavailableException;
import net.mcthunder.events.session.DisconnectingEvent;
import net.mcthunder.events.session.PacketReceivedEvent;
import net.mcthunder.events.session.SessionAdapter;
import net.mcthunder.game.essentials.ServerStatusInfo;
import net.mcthunder.handlers.ServerLoginHandler;
import net.mcthunder.packet.HandshakePacket;
import net.mcthunder.packet.essentials.CryptUtil;
import net.mcthunder.packet.essentials.Session;
import net.mcthunder.packet.ingame.client.ClientKeepAlivePacket;
import net.mcthunder.packet.ingame.server.ServerDisconnectPacket;
import net.mcthunder.packet.ingame.server.ServerKeepAlivePacket;
import net.mcthunder.packet.login.client.EncryptionResponsePacket;
import net.mcthunder.packet.login.client.LoginStartPacket;
import net.mcthunder.packet.login.server.EncryptionRequestPacket;
import net.mcthunder.packet.login.server.LoginDisconnectPacket;
import net.mcthunder.packet.login.server.LoginSuccessPacket;
import net.mcthunder.packet.status.client.StatusPingPacket;
import net.mcthunder.packet.status.client.StatusQueryPacket;
import net.mcthunder.packet.status.server.StatusPongPacket;
import net.mcthunder.packet.status.server.StatusResponsePacket;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class ServerListener extends SessionAdapter {
    private static KeyPair pair = CryptUtil.generateKeyPair();

    private byte[] verifyToken = new byte[4];
    private String serverId = "";
    private String username = "";

    private long lastPingTime = 0L;
    private int lastPingId = 0;

    public ServerListener() {
        new Random().nextBytes(this.verifyToken);
    }

    public void packetReceived(PacketReceivedEvent event) {
        MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
        if ((protocol.getMode() == ProtocolMode.HANDSHAKE) &&
                ((event.getPacket() instanceof HandshakePacket))) {
            HandshakePacket packet = (HandshakePacket) event.getPacket();
            switch (packet.getIntent()) {
                case 1:
                    protocol.setMode(ProtocolMode.STATUS, false, event.getSession());
                    break;
                case 2:
                    protocol.setMode(ProtocolMode.LOGIN, false, event.getSession());
                    if (packet.getProtocolVersion() > 5) {
                        event.getSession().disconnect("Outdated server! I'm still on 1.7.7.");
                    } else {
                        if (packet.getProtocolVersion() >= 5) break;
                        event.getSession().disconnect("Outdated client! Please use 1.7.7.");
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Invalid client intent: " + packet.getIntent());
            }

        }

        if (protocol.getMode() == ProtocolMode.LOGIN) {
            if ((event.getPacket() instanceof LoginStartPacket)) {
                this.username = ((LoginStartPacket) event.getPacket()).getUsername();
                boolean verify = event.getSession().hasFlag("verify-users") ? ((Boolean) event.getSession().getFlag("verify-users")).booleanValue() : true;
                if (verify) {
                    event.getSession().send(new EncryptionRequestPacket(this.serverId, pair.getPublic(), this.verifyToken));
                } else {
                    GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + this.username).getBytes()), this.username);
                    event.getSession().send(new LoginSuccessPacket(profile));
                    event.getSession().setFlag("profile", profile);
                    protocol.setMode(ProtocolMode.GAME, false, event.getSession());
                    ServerLoginHandler handler = (ServerLoginHandler) event.getSession().getFlag("login-handler");
                    if (handler != null) {
                        handler.loggedIn(event.getSession());
                    }

                    new KeepAliveThread(event.getSession()).start();
                }
            } else if ((event.getPacket() instanceof EncryptionResponsePacket)) {
                EncryptionResponsePacket packet = (EncryptionResponsePacket) event.getPacket();
                PrivateKey privateKey = pair.getPrivate();
                if (!Arrays.equals(this.verifyToken, packet.getVerifyToken(privateKey))) {
                    throw new IllegalStateException("Invalid nonce!");
                }
                SecretKey key = packet.getSecretKey(privateKey);
                protocol.enableEncryption(key);
                new UserAuthThread(event.getSession(), key).start();
            }

        }

        if (protocol.getMode() == ProtocolMode.STATUS) {
            if ((event.getPacket() instanceof StatusQueryPacket)) {
                ServerInfoBuilder builder = (ServerInfoBuilder) event.getSession().getFlag("info-builder");
                if (builder == null) {
                    event.getSession().disconnect("No server info builder set.");
                }

                ServerStatusInfo info = builder.buildInfo(event.getSession());
                event.getSession().send(new StatusResponsePacket(info));
            } else if ((event.getPacket() instanceof StatusPingPacket)) {
                event.getSession().send(new StatusPongPacket(((StatusPingPacket) event.getPacket()).getPingTime()));
            }
        }

        if ((protocol.getMode() == ProtocolMode.GAME) &&
                ((event.getPacket() instanceof ClientKeepAlivePacket))) {
            ClientKeepAlivePacket packet = (ClientKeepAlivePacket) event.getPacket();
            if (packet.getPingId() == this.lastPingId) {
                long time = System.nanoTime() / 1000000L - this.lastPingTime;
                event.getSession().setFlag("ping", Long.valueOf(time));
            }
        }
    }

    public void disconnecting(DisconnectingEvent event) {
        MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
        if (protocol.getMode() == ProtocolMode.LOGIN)
            event.getSession().send(new LoginDisconnectPacket(event.getReason()));
        else if (protocol.getMode() == ProtocolMode.GAME)
            event.getSession().send(new ServerDisconnectPacket(event.getReason()));
    }

    private class KeepAliveThread extends Thread {
        private Session session;

        public KeepAliveThread(Session session) {
            this.session = session;
        }

        public void run() {
            ServerListener((ServerListener.this, System.nanoTime() / 1000000L);
            while (this.session.isConnected()) {
                long curr = System.nanoTime() / 1000000L;
                long time = curr - ServerListener.this.lastPingTime;
                if (time > 2000L) {
                    ServerListener.access$302(ServerListener.this, curr);
                    ServerListener.access$402(ServerListener.this, (int) curr);
                    this.session.send(new ServerKeepAlivePacket(ServerListener.this.lastPingId));
                }
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class UserAuthThread extends Thread {
        private Session session;
        private SecretKey key;

        public UserAuthThread(Session session, SecretKey key) {
            this.key = key;
            this.session = session;
        }

        public void run() {
            MinecraftProtocol protocol = (MinecraftProtocol) this.session.getPacketProtocol();
            try {
                String serverHash = new BigInteger(CryptUtil.getServerIdHash(ServerListener.this.serverId, ServerListener.pair.getPublic(), this.key)).toString(16);
                SessionService service = new SessionService();
                GameProfile profile = service.hasJoinedServer(new GameProfile((UUID) null, ServerListener.this.username), serverHash);
                if (profile != null) {
                    this.session.send(new LoginSuccessPacket(profile));
                    this.session.setFlag("profile", profile);
                    protocol.setMode(ProtocolMode.GAME, false, this.session);
                    ServerLoginHandler handler = (ServerLoginHandler) this.session.getFlag("login-handler");
                    if (handler != null) {
                        handler.loggedIn(this.session);
                    }

                    new ServerListener.KeepAliveThread(ServerListener.this, this.session).start();
                } else {
                    this.session.disconnect("Failed to verify username!");
                }
            } catch (AuthenticationUnavailableException e) {
                this.session.disconnect("Authentication servers are down. Please try again later, sorry!");
            }
        }
    }
}