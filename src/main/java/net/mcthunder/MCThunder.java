package net.mcthunder;

import net.mcthunder.apis.Config;
import net.mcthunder.packetlib.Server;
import net.mcthunder.packetlib.tcp.TcpSessionFactory;
import net.mcthunder.protocol.MinecraftProtocol;

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


    public static void main(String args[]) {
        conf = new Config();
        conf.loadConfig();
        //VERIFY_USERS = conf.getOnlineMode();
        HOST = conf.getHost();
        Server server = new Server(HOST, PORT, MinecraftProtocol.class, new TcpSessionFactory());


    }

}
