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
import org.spacehq.mc.protocol.packet.ingame.client.ClientSettingsPacket;
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
                    session.send(new ServerJoinGamePacket(0, false, GameMode.CREATIVE, 0, Difficulty.PEACEFUL, 10, WorldType.DEFAULT, false));

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
                                //Array[] short
                                //ShortArray3d blocks = new ShortArray3d(16);
                                // NibbleArray3d skylight = new NibbleArray3d(1);
                                // NibbleArray3d blockLight = new NibbleArray3d(1);
                                // Chunk c = new Chunk(blocks,blockLight, skylight );
                                // Chunk[] chunks = null;
                                // chunks[0] = c;
                                String username = ((LoginStartPacket) event.getPacket()).getUsername().toString();
                                tellConsole("DEBUG", "User " + username + " is trying to log in!");
                                event.getSession().send(new ServerChunkDataPacket(0, 0));
                                event.getSession().send(new ServerSpawnPositionPacket(new Position(0, 62, 0)));
                                event.getSession().send(new ServerPlayerPositionRotationPacket(0, 62, 0, 0, 0));
                                GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);

                                chatHandler.sendMessage(server, profile.getName() + " has joined " + serverName);


                            } else if (event.getPacket() instanceof ClientPlayerPositionPacket) {
                                ClientPlayerMovementPacket packet = event.getPacket();
                                double pitch2 = packet.getPitch();
                                float pitch = (float) pitch2;
                                double Yaw2 = packet.getYaw();
                                float yaw = (float) Yaw2;
                                boolean onGround = packet.isOnGround();
                                double x = packet.getX();
                                double y = packet.getY() * 0.2;
                                double z = packet.getZ();
                                ServerPlayerPositionRotationPacket newPacket = new ServerPlayerPositionRotationPacket(0, 64, 0, yaw, pitch);
                                //tellConsole("DEBUG", String.valueOf(x)+ "/"+ String.valueOf(y)+ " " + String.valueOf(z));


                                // event.getSession().send(newPacket);


                                // tellConsole("DEBUG", "NEW PLAYER POSITION");




                            } else if (event.getPacket() instanceof ClientChatPacket) {

                                ClientChatPacket packet = event.getPacket();

                                chatHandler.handleChat(server, event.getSession(), packet, event, sessionsList);


                            } else if (event.getPacket() instanceof ClientPlayerMovementPacket) {

                                ClientPlayerMovementPacket packet = event.getPacket();
                                double pitch2 = packet.getPitch();


                                float pitch = (float) pitch2;
                                double Yaw2 = packet.getYaw();
                                float yaw = (float) Yaw2;
                                boolean onGround = packet.isOnGround();
                                double x = packet.getX();
                                double y = packet.getY() * 0.2;
                                double z = packet.getZ();
                                ServerPlayerPositionRotationPacket newPacket = new ServerPlayerPositionRotationPacket(0, 64, 0, yaw, pitch);
                                String[] debug = {String.valueOf(x), "/", String.valueOf(y), " ", String.valueOf(z)};
                                //ServerPlayerPositionRotationPacket newPacket = new ServerPlayerPositionRotationPacket(0,64,0,yaw,pitch);
                                //tellConsole("DEBUG", String.valueOf(x)+ "/"+ String.valueOf(y)+ " " + String.valueOf(z));

                                //event.getSession().send(newPacket);


                            } else if (event.getPacket() instanceof ClientKeepAlivePacket) {
                                // List<Session> sessions= server.getSessions();
                                //if(sessionsList.contains(null)){
                                //   sessions.remove(null);
                                //  tellConsole("DEBUG", "REMOVED NULL SESSION");
                                //  }
                                //online = sessions.size();
                                // for (Session s : sessionsList){
                                //    tellConsole("DEBUG", "Sessions for " + s.getFlag(ProtocolConstants.PROFILE_KEY));
                                // }
                                GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);

                            } else if (event.getPacket() instanceof ClientSettingsPacket) {
                                ;
                                //ServerBlockChangePacket packet;
                                // Position p;
                                // int x = 0, y = 60, z = 0;
                                //  Position position;
                                //  BlockChangeRecord blockChangeRecord;


                                //       while (x < 16){
                                //            p = new Position(x,y,z);

                                // position = p;
                                ///            blockChangeRecord = new BlockChangeRecord(p,1);
                                //           packet = new ServerBlockChangePacket(blockChangeRecord);
                                //            event.getSession().send(packet);
                                //           ServerBlock

                                ///           z++;
                                //          x++;
                                //           tellConsole("DEBUG", "Created Block " + p.getX());

                                //      }


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


                @Override
                public void sessionRemoved(SessionRemovedEvent event) {


                }
            });

            server.bind();


        }

    }
}
