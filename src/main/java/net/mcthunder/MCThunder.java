package net.mcthunder;


//MCThunder Code Imports 

import net.mcthunder.apis.Command;
import net.mcthunder.apis.CommandRegistry;
import net.mcthunder.apis.Config;
import net.mcthunder.events.listeners.PlayerChatEventListener;
import net.mcthunder.events.listeners.PlayerCommandEventListener;
import net.mcthunder.events.source.PlayerChatEventSource;
import net.mcthunder.events.source.PlayerCommandEventSource;
import net.mcthunder.handlers.PlayerProfileHandler;
import net.mcthunder.handlers.ServerChatHandler;
import net.mcthunder.handlers.ServerPlayerEntryListHandler;
import net.mcthunder.handlers.ServerTabHandler;
import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.ProtocolMode;
import org.spacehq.mc.protocol.ServerLoginHandler;
import org.spacehq.mc.protocol.data.game.*;
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

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static net.mcthunder.apis.Utils.*;

//MCPacketLib Imports
//Java Imports
//Misc Library Imports
//Static Mist Imports


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
    private static MinecraftProtocol protocol;
    private static List<Session> sessionsList;
    private static ServerChatHandler chatHandler;
    private static ServerTabHandler tabHandler;
    private static ServerPlayerEntryListHandler entryListHandler;
    private static int online = 0;
    private static PlayerProfileHandler playerProfileHandler;
    private static Server server;
    private static PlayerChatEventListener defaultPlayerChatEventListener;
    private static PlayerChatEventSource playerChatEventSource;
    private static PlayerCommandEventSource playerCommandEventSource;
    private static PlayerCommandEventListener defaultPlayerCommandEventListener;


    public static void main(String args[]) {


        conf = new Config();
        conf.loadConfig();


        //Set Server data
        serverName = conf.getServerName();
        VERIFY_USERS = conf.getOnlineMode();
        HOST = getIP();
        PORT = conf.getPort();
        //Done Set Server Data
        tellConsole("INFO", "INTERNAL PORT " + HOST);

        createInitialDirs();
        tellPublicIpAddress();
        //Register Default Commands
        /**
         * Based of of Alphabot/Lukabot code that was created by zack6849
         */
        String pkg = "net.mcthunder.commands.";
        Reflections reflections = new Reflections("net.mcthunder.commands");
        Set<Class<? extends Command>> subTypes = reflections.getSubTypesOf(Command.class);
        for (Class c : subTypes) {
            Command cmd = CommandRegistry.getCommand(c.getSimpleName(), pkg);

            CommandRegistry.register(cmd);
        }
        //Done
        if (SPAWN_SERVER) {
            server = new Server(HOST, PORT, MinecraftProtocol.class, new TcpSessionFactory());
            //Set Handlers
            chatHandler = new ServerChatHandler();
            entryListHandler = new ServerPlayerEntryListHandler();
            tabHandler = new ServerTabHandler();
            playerProfileHandler = new PlayerProfileHandler(server);
            //Done Set Handlers
            //Listeners
            defaultPlayerChatEventListener = new PlayerChatEventListener();
            defaultPlayerCommandEventListener = new PlayerCommandEventListener();
            playerChatEventSource = new PlayerChatEventSource();
            playerCommandEventSource = new PlayerCommandEventSource();
            playerChatEventSource.addEventListener(defaultPlayerChatEventListener);
            playerCommandEventSource.addEventListener(defaultPlayerCommandEventListener);
            //Done Listeners
            


            server.setGlobalFlag(ProtocolConstants.VERIFY_USERS_KEY, VERIFY_USERS);
            server.setGlobalFlag(ProtocolConstants.SERVER_COMPRESSION_THRESHOLD, 100);
            server.setGlobalFlag(ProtocolConstants.SERVER_INFO_BUILDER_KEY, new ServerInfoBuilder() {
                @Override
                public ServerStatusInfo buildInfo(Session session) {
                    sessionsList = server.getSessions();
                    return new ServerStatusInfo(new VersionInfo(ProtocolConstants.GAME_VERSION, ProtocolConstants.PROTOCOL_VERSION), new PlayerInfo(conf.getSlots(), online, new GameProfile[0]), new TextMessage("Hello world!"), null);
                }
            });

            server.setGlobalFlag(ProtocolConstants.SERVER_LOGIN_HANDLER_KEY, new ServerLoginHandler() {
                @Override
                public void loggedIn(Session session) {
                    online++;
                    GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
                    session.send(new ServerJoinGamePacket(0, false, GameMode.CREATIVE, 0, Difficulty.PEACEFUL, 10, WorldType.DEFAULT, false));
                    String username = profile.getName();
                    //Would that be good to keep? V 
                    tellConsole("INFO", "User " + username + " is trying to log in!");
                    entryListHandler.addToPlayerEntryList(server, session);
                    //Send World Data

                    //Done
                    session.send(new ServerPlayerPositionRotationPacket(0, 62, 0, 0, 0));
                    byte[] light = new byte[4096];
                    Arrays.fill(light, (byte) 15);
                    Chunk[] chunks = new Chunk[16];
                    for (int i = 0; i < chunks.length; i++) {
                        NibbleArray3d blocklight = new NibbleArray3d(light);
                        NibbleArray3d skylight = new NibbleArray3d(light);
                        ShortArray3d blocks = new ShortArray3d(4096);
                        for (int cY = 0; cY < 24; cY++) {
                            for (int cZ = 0; cZ < 16; cZ++) {
                                for (int cX = 0; cX < 16; cX++) {
                                    int y = cY - i * 16;
                                    if ((cY < (i + 1) * 16) && (cY > i * 16)) {
                                        if (cY < 1) {
                                            blocks.setBlock(cX, y, cZ, 7);
                                        } else if (cY < 23) {
                                            blocks.setBlock(cX, y, cZ, 3);
                                        } else {
                                            blocks.setBlock(cX, y, cZ, 2);
                                        }
                                    }
                                }
                            }
                        }

                        Chunk chunk = new Chunk(blocks, blocklight, skylight);
                        chunks[i] = chunk;
                    }
                    for (int x = -5; x <= 5; x++) {
                        for (int z = -5; z <= 5; z++) {
                            session.send(new ServerChunkDataPacket(x, z, chunks, new byte[256]));
                        }
                    }

                    session.send(new ServerSpawnPositionPacket(new Position(0, 62, 0)));


                    chatHandler.sendMessage(server, profile.getName() + " has joined " + serverName);
                    playerProfileHandler.checkPlayer(profile);


                }
            });

            server.addListener(new ServerAdapter() {
                @Override
                public void sessionAdded(SessionAddedEvent event) {
                    event.getSession().addListener(new SessionAdapter() {


                        @Override
                        public void packetReceived(PacketReceivedEvent event) {

                            if (event.getPacket() instanceof LoginStartPacket) {


                            } else if (event.getPacket() instanceof ClientPlayerPositionPacket) {
                                sessionsList = server.getSessions();
                                ClientPlayerPositionPacket packet = event.getPacket();
                                GameProfile player = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
                                //int entityID = packet.get
                                UUID uuid = player.getId();
                                double x = packet.getX();
                                double y = packet.getY();
                                double z = packet.getZ();
                                float yaw = (float) packet.getYaw();
                                float pitch = (float) packet.getPitch();
                                int currentItem = 7;
                                EntityMetadata[] metadata;

                                //ServerSpawnPlayerPacket toAllPlayers = new ServerSpawnPlayerPacket();
                                for (Session s : sessionsList) {

                                }


                            } else if (event.getPacket() instanceof ClientChatPacket) {

                                ClientChatPacket packet = event.getPacket();
                                if (packet.getMessage().startsWith("/")) {
                                    String command = StringUtils.lowerCase(packet.getMessage().split(" ")[0].split("/")[1]);

                                    playerCommandEventSource.fireEvent(server, event.getSession(), packet);

                                } else {
                                    playerChatEventSource.fireEvent(server, event.getSession(), packet, server.getSessions());
                                }


                                //chatHandler.handleChat(server, event.getSession(), packet, sessionsList);


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
                            if (event.getPacket() instanceof ServerJoinGamePacket) {





                            }

                        }

                    });
                    }


                @Override
                public void sessionRemoved(SessionRemovedEvent event) {
                    if (((MinecraftProtocol) event.getSession().getPacketProtocol()).getMode() == ProtocolMode.GAME) {
                        GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
                        chatHandler.sendMessage(server, profile.getName() + " has left " + conf.getServerName());
                        online--;
                        entryListHandler.deleteFromPlayerEntryList(server, event.getSession());
                    }


                }
            });

            server.bind();


        }


    }

    public Server getServer() {
        return this.server;
    }

}
