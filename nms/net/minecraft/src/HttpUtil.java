package net.minecraft.src;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpUtil
{
    /**
     * Builds an encoded HTTP POST content string from a string map
     */
    public static String buildPostString(Map par0Map)
    {
        StringBuilder var1 = new StringBuilder();
        Iterator var2 = par0Map.entrySet().iterator();

        while (var2.hasNext())
        {
            Entry var3 = (Entry)var2.next();

            if (var1.length() > 0)
            {
                var1.append('&');
            }

            try
            {
                var1.append(URLEncoder.encode((String)var3.getKey(), "UTF-8"));
            }
            catch (UnsupportedEncodingException var6)
            {
                var6.printStackTrace();
            }

            if (var3.getValue() != null)
            {
                var1.append('=');

                try
                {
                    var1.append(URLEncoder.encode(var3.getValue().toString(), "UTF-8"));
                }
                catch (UnsupportedEncodingException var5)
                {
                    var5.printStackTrace();
                }
            }
        }

        return var1.toString();
    }

    /**
     * Sends a HTTP POST request to the given URL with data from a map
     */
    public static String sendPost(URL par0URL, Map par1Map, boolean par2)
    {
        return sendPost(par0URL, buildPostString(par1Map), par2);
    }

    /**
     * Sends a HTTP POST request to the given URL with data from a string
     */
    public static String sendPost(URL par0URL, String par1Str, boolean par2)
    {
        try
        {
            HttpURLConnection var3 = (HttpURLConnection)par0URL.openConnection();
            var3.setRequestMethod("POST");
            var3.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            var3.setRequestProperty("Content-Length", "" + par1Str.getBytes().length);
            var3.setRequestProperty("Content-Language", "en-US");
            var3.setUseCaches(false);
            var3.setDoInput(true);
            var3.setDoOutput(true);
            DataOutputStream var4 = new DataOutputStream(var3.getOutputStream());
            var4.writeBytes(par1Str);
            var4.flush();
            var4.close();
            BufferedReader var5 = new BufferedReader(new InputStreamReader(var3.getInputStream()));
            StringBuffer var7 = new StringBuffer();
            String var6;

            while ((var6 = var5.readLine()) != null)
            {
                var7.append(var6);
                var7.append('\r');
            }

            var5.close();
            return var7.toString();
        }
        catch (Exception var8)
        {
            if (!par2)
            {
                Logger.getLogger("Minecraft").log(Level.SEVERE, "Could not post to " + par0URL, var8);
            }

            return "";
        }
    }
}
