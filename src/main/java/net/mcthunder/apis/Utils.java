package net.mcthunder.apis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kevin on 8/9/2014.
 * import org.jsoup.nodes.Document;
 */

public class Utils {
    private static Config conf;

    public static String getIP() {

        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


        return ip.getHostAddress();

    }

    public static void tellPublicIpAddress() {
        conf = new Config();
        conf.loadConfig();
        try {
            Document document = Jsoup.connect("http://ipchicken.com").get();
            Elements ip = document.select("b");
            String[] publicIp = ip.toString().split(" ");
            tellConsole("INFO", "People may connect to the server with " + publicIp[1] + ":" + conf.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void tellConsole(String type, String message) {
        String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
        System.out.println(date + " [" + type + "]: " + message);
    }

}
