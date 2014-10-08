package net.mcthunder;

import net.mcthunder.apis.Config;
import net.mcthunder.handlers.ServerChatHandler;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.ServerLoginHandler;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.data.game.values.setting.Difficulty;
import org.spacehq.mc.protocol.data.game.values.world.WorldType;
import org.spacehq.mc.protocol.data.message.TextMessage;
import org.spacehq.mc.protocol.data.status.PlayerInfo;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol.data.status.VersionInfo;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoBuilder;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerMovementPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.spacehq.mc.protocol.packet.login.client.LoginStartPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.server.ServerAdapter;
import org.spacehq.packetlib.event.server.SessionAddedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.PacketSentEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import java.util.List;

import static net.mcthunder.apis.Utils.tellConsole;


/**
 * Created by Kevin on 8/9/2014.
 */
public class MCThunder {
    private static Config conf;
    private static boolean SPAWN_SERVER = true;
    private static boolean VERIFY_USERS = false;
    private static String HOST;
    private static int PORT;
    private static boolean isRunning;
    private static String USERNAME = "test";
    private static String PASSWORD = "test";
    private static MinecraftProtocol protocol;
    private static List<Session> sessionsList;
    private static ServerChatHandler chatHandler;
    //private static Session[] sessions = null;
    int[] in = null;



    public static void main(String args[]) {

        conf = new Config();
        conf.loadConfig();
        VERIFY_USERS = conf.getOnlineMode();
        HOST = conf.getHost();
        PORT = conf.getPort();
        tellConsole("INFO", "HOST " + HOST);
        if (SPAWN_SERVER) {
            final Server server = new Server(HOST, PORT, MinecraftProtocol.class, new TcpSessionFactory());
            server.setGlobalFlag(ProtocolConstants.VERIFY_USERS_KEY, VERIFY_USERS);
            server.setGlobalFlag(ProtocolConstants.SERVER_COMPRESSION_THRESHOLD, 100);
            server.setGlobalFlag(ProtocolConstants.SERVER_INFO_BUILDER_KEY, new ServerInfoBuilder() {
                @Override
                public ServerStatusInfo buildInfo(Session session) {
                    return new ServerStatusInfo(new VersionInfo(ProtocolConstants.GAME_VERSION, ProtocolConstants.PROTOCOL_VERSION), new PlayerInfo(100, 0, new GameProfile[0]), new TextMessage("Hello world!"), null);
                }
            });

            server.setGlobalFlag(ProtocolConstants.SERVER_LOGIN_HANDLER_KEY, new ServerLoginHandler() {
                @Override
                public void loggedIn(Session session) {

                    session.send(new ServerJoinGamePacket(0, false, GameMode.SURVIVAL, 0, Difficulty.PEACEFUL, 10, WorldType.DEFAULT, false));

                }
            });

            server.addListener(new ServerAdapter() {
                @Override
                public void sessionAdded(SessionAddedEvent event) {

                    //sessionsList.add(event.getSession());







                    event.getSession().addListener(new SessionAdapter() {


                        @Override
                        public void packetReceived(PacketReceivedEvent event) {

                            if (event.getPacket() instanceof LoginStartPacket) {
                                String username = ((LoginStartPacket) event.getPacket()).getUsername().toString();
                                tellConsole("DEBUG", "User " + username + " is trying to log in!");
                            } else if (event.getPacket() instanceof ClientPlayerPositionPacket) {
                                    event.getSession().send(new ServerChunkDataPacket(0, 0));
                                    event.getSession().send(new ServerSpawnPositionPacket(new Position(0, 0, 0)));
                                    event.getSession().send(new ServerPlayerPositionRotationPacket(0, 0, 0, 0, 0));

                            } else if (event.getPacket() instanceof ClientChatPacket) {

                                ClientChatPacket packet = event.getPacket();
                                chatHandler = new ServerChatHandler();
                                chatHandler.handleChat(server, event.getSession(), packet, event, sessionsList);


                                    } else if (event.getPacket() instanceof ClientPlayerMovementPacket) {

                                    } else if (event.getPacket() instanceof ClientKeepAlivePacket) {

                                    } else {
                                        tellConsole("INFO", event.getPacket().toString());
                                    }
                                }


                        @Override
                        public void packetSent(PacketSentEvent event) {
                            if (event.getPacket() instanceof ServerChatPacket) {


                            }

                        }

                    });
                }


                // @Override
                //  public void sessionRemoved(SessionRemovedEvent event) {
                // MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
                // if (protocol.getMode() == ProtocolMode.GAME) {
                //     System.out.println("Closing server.");
                //     event.getServer().close();
                // }
                // }
            });

            server.bind();




    }

    }
}
