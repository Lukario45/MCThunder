
import com.sun.corba.se.spi.activation.Server;
import java.io.File;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Chris
 */
public class ThunderCore{
    
     public static final Logger logger = Logger.getLogger("Minecraft");
     public static final File ConfigDir = new File("config");
     private static final File configFile = new File(ConfigDir, "MCThunder.yml");
     
     public void init() {
         
     }
    
}
