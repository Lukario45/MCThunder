package net.minecraft.src;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ServerGUI extends JComponent
{
    /** Reference to the logger. */
    public static Logger logger = Logger.getLogger("Minecraft");

    /** This is set to true after server GUI window has been initialized. */
    private static boolean serverGuiInitialized = false;
    private DedicatedServer serverInstance;

    /**
     * Sets up the server GUI
     */
    public static void initGUI(DedicatedServer par0DedicatedServer)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception var3)
        {
            ;
        }

        ServerGUI var1 = new ServerGUI(par0DedicatedServer);
        serverGuiInitialized = true;
        JFrame var2 = new JFrame("Minecraft server");
        var2.add(var1);
        var2.pack();
        var2.setLocationRelativeTo((Component)null);
        var2.setVisible(true);
        var2.addWindowListener(new ServerWindowAdapter(par0DedicatedServer));
    }

    public ServerGUI(DedicatedServer par1DedicatedServer)
    {
        this.serverInstance = par1DedicatedServer;
        this.setPreferredSize(new Dimension(854, 480));
        this.setLayout(new BorderLayout());

        try
        {
            this.add(this.getLogComponent(), "Center");
            this.add(this.getStatsComponent(), "West");
        }
        catch (Exception var3)
        {
            var3.printStackTrace();
        }
    }

    /**
     * Returns a new JPanel with a new GuiStatsComponent inside.
     */
    private JComponent getStatsComponent()
    {
        JPanel var1 = new JPanel(new BorderLayout());
        var1.add(new GuiStatsComponent(this.serverInstance), "North");
        var1.add(this.getPlayerListComponent(), "Center");
        var1.setBorder(new TitledBorder(new EtchedBorder(), "Stats"));
        return var1;
    }

    /**
     * Returns a new JScrollPane with a new PlayerListBox inside.
     */
    private JComponent getPlayerListComponent()
    {
        PlayerListBox var1 = new PlayerListBox(this.serverInstance);
        JScrollPane var2 = new JScrollPane(var1, 22, 30);
        var2.setBorder(new TitledBorder(new EtchedBorder(), "Players"));
        return var2;
    }

    /**
     * Returns a new JPanel with a new GuiStatsComponent inside.
     */
    private JComponent getLogComponent()
    {
        JPanel var1 = new JPanel(new BorderLayout());
        JTextArea var2 = new JTextArea();
        logger.addHandler(new GuiLogOutputHandler(var2));
        JScrollPane var3 = new JScrollPane(var2, 22, 30);
        var2.setEditable(false);
        JTextField var4 = new JTextField();
        var4.addActionListener(new ServerGuiCommandListener(this, var4));
        var2.addFocusListener(new ServerGuiFocusAdapter(this));
        var1.add(var3, "Center");
        var1.add(var4, "South");
        var1.setBorder(new TitledBorder(new EtchedBorder(), "Log and chat"));
        return var1;
    }

    static DedicatedServer getDedicatedServer(ServerGUI par0ServerGUI)
    {
        return par0ServerGUI.serverInstance;
    }
}
