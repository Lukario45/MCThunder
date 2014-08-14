package net.mcthunder;

import net.mcthunder.apis.Config;
import net.mcthunder.apis.Utils;
import net.mcthunder.auth.GameProfile;
import net.mcthunder.packetlib.Server;
import net.mcthunder.packetlib.Session;
import net.mcthunder.packetlib.event.server.ServerAdapter;
import net.mcthunder.packetlib.event.server.SessionAddedEvent;
import net.mcthunder.packetlib.event.server.SessionRemovedEvent;
import net.mcthunder.packetlib.event.session.PacketReceivedEvent;
import net.mcthunder.packetlib.event.session.SessionAdapter;
import net.mcthunder.packetlib.tcp.TcpSessionFactory;
import net.mcthunder.protocol.MinecraftProtocol;
import net.mcthunder.protocol.ProtocolConstants;
import net.mcthunder.protocol.ProtocolMode;
import net.mcthunder.protocol.ServerLoginHandler;
import net.mcthunder.protocol.data.message.*;
import net.mcthunder.protocol.data.status.PlayerInfo;
import net.mcthunder.protocol.data.status.ServerStatusInfo;
import net.mcthunder.protocol.data.status.VersionInfo;
import net.mcthunder.protocol.data.status.handler.ServerInfoBuilder;
import net.mcthunder.protocol.packet.ingame.client.ClientChatPacket;
import net.mcthunder.protocol.packet.ingame.server.ServerChatPacket;
import net.mcthunder.protocol.packet.ingame.server.ServerJoinGamePacket;


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


    public static void main(String args[]) {
        conf = new Config();
        conf.loadConfig();
        VERIFY_USERS = conf.getOnlineMode();
        HOST = conf.getHost();
        Utils.tellConsole("INFO", "HOST " + HOST);
        if (SPAWN_SERVER) {
            Server server = new Server(HOST, PORT, MinecraftProtocol.class, new TcpSessionFactory());
            server.setGlobalFlag(ProtocolConstants.VERIFY_USERS_KEY, VERIFY_USERS);
            server.setGlobalFlag(ProtocolConstants.SERVER_INFO_BUILDER_KEY, new ServerInfoBuilder() {
                @Override
                public ServerStatusInfo buildInfo(Session session) {
                    return new ServerStatusInfo(new VersionInfo(ProtocolConstants.GAME_VERSION, ProtocolConstants.PROTOCOL_VERSION), new PlayerInfo(100, 0, new GameProfile[0]), new TextMessage("Hello world!"), null);
                }
            });

            server.setGlobalFlag(ProtocolConstants.SERVER_LOGIN_HANDLER_KEY, new ServerLoginHandler() {
                @Override
                public void loggedIn(Session session) {
                    session.send(new ServerJoinGamePacket(0, false, ServerJoinGamePacket.GameMode.SURVIVAL, 0, ServerJoinGamePacket.Difficulty.PEACEFUL, 10, ServerJoinGamePacket.WorldType.DEFAULT));
                }
            });

            server.addListener(new ServerAdapter() {
                @Override
                public void sessionAdded(SessionAddedEvent event) {
                    event.getSession().addListener(new SessionAdapter() {

                        @Override
                        public void packetReceived(PacketReceivedEvent event) {
                            if (event.getPacket() instanceof ClientChatPacket) {
                                ClientChatPacket packet = event.getPacket();
                                GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
                                System.out.println(profile.getName() + ": " + packet.getMessage());
                                Message msg = new TextMessage("Hello, ").setStyle(new MessageStyle().setColor(ChatColor.GREEN));
                                Message name = new TextMessage(profile.getName()).setStyle(new MessageStyle().setColor(ChatColor.AQUA).addFormat(ChatFormat.UNDERLINED));
                                Message end = new TextMessage("!");
                                msg.addExtra(name);
                                msg.addExtra(end);
                                event.getSession().send(new ServerChatPacket(msg));
                            }
                        }
                    });
                }

                @Override
                public void sessionRemoved(SessionRemovedEvent event) {
                    MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
                    if (protocol.getMode() == ProtocolMode.GAME) {
                        System.out.println("Closing server.");
                        event.getServer().close();
                    }
                }
            });

            server.bind();




    }

    }
}
