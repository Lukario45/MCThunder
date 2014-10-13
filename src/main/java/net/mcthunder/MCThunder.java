package net.mcthunder;

import net.mcthunder.apis.Config;
import net.mcthunder.handlers.ServerChatHandler;
import net.mcthunder.handlers.ServerTabHandler;
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
import org.spacehq.mc.protocol.packet.ingame.client.ClientSettingsPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
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
import org.spacehq.packetlib.event.server.SessionRemovedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.PacketSentEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import java.util.List;

import static net.mcthunder.apis.Utils.tellConsole;
import static net.mcthunder.apis.Utils.updatePlayerEntryList;


/**
 * Created by Kevin on 8/9/2014.
 */
public class MCThunder {
    private static Config conf;
    private static String serverName;
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
    private static ServerTabHandler tabHandler;
    private static int online = 0;
    //private static Session[] sessions = null;
    int[] in = null;


    public static void main(String args[]) {


        chatHandler = new ServerChatHandler();
        conf = new Config();
        conf.loadConfig();
        serverName = conf.getServerName();
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
                    sessionsList = server.getSessions();
                    return new ServerStatusInfo(new VersionInfo(ProtocolConstants.GAME_VERSION, ProtocolConstants.PROTOCOL_VERSION), new PlayerInfo(100, online, new GameProfile[0]), new TextMessage("Hello world!"), null);
                }
            });

            server.setGlobalFlag(ProtocolConstants.SERVER_LOGIN_HANDLER_KEY, new ServerLoginHandler() {
                @Override
                public void loggedIn(Session session) {
                    online++;
                    GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
                    session.send(new ServerJoinGamePacket(0, false, GameMode.CREATIVE, 0, Difficulty.PEACEFUL, 10, WorldType.DEFAULT, false));
                    String username = profile.getName();
                    tellConsole("DEBUG", "User " + username + " is trying to log in!");
                    ;
                    updatePlayerEntryList(server, session);
                    session.send(new ServerChunkDataPacket(0, 0));
                    session.send(new ServerSpawnPositionPacket(new Position(0, 62, 0)));
                    session.send(new ServerPlayerPositionRotationPacket(0, 62, 0, 0, 0));
                    chatHandler.sendMessage(server, profile.getName() + " has joined " + serverName);


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


                            } else if (event.getPacket() instanceof ClientPlayerPositionPacket) {

                            } else if (event.getPacket() instanceof ClientChatPacket) {

                                ClientChatPacket packet = event.getPacket();

                                chatHandler.handleChat(server, event.getSession(), packet, event, sessionsList);


                            } else if (event.getPacket() instanceof ClientPlayerMovementPacket) {


                            } else if (event.getPacket() instanceof ClientKeepAlivePacket) {


                            } else if (event.getPacket() instanceof ClientSettingsPacket) {


                            } else if (event.getPacket() instanceof ClientTabCompletePacket) {
                                ClientTabCompletePacket packet = event.getPacket();
                                tabHandler.handleTabComplete(server, event.getSession(), packet);
                            } else {
                                tellConsole("DEBUG", event.getPacket().toString());
                            }
                        }


                        @Override
                        public void packetSent(PacketSentEvent event) {
                            if (event.getPacket() instanceof ServerChatPacket) {


                            }

                        }

                    });
                }


                @Override
                public void sessionRemoved(SessionRemovedEvent event) {


                }
            });

            server.bind();


        }

    }
}
