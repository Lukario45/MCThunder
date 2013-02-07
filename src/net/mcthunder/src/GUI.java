package net.mcthunder.src;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import javax.swing.JTabbedPane;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Panel;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import java.awt.TextArea;
import javax.swing.JTextPane;

public class GUI extends JFrame {

	public static Object tabHomeConsoleOut;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void guiTime() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		
		setTitle("MCThunder");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 667, 542);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(5, 11, 651, 492);
		contentPane.add(tabbedPane);
		
		JPanel tabHome = new JPanel();
		tabbedPane.addTab("Home", null, tabHome, null);
		tabHome.setLayout(null);
		//String appendConsoleMessage;
		
		JTextArea tabHomeConsoleOut = new JTextArea();
		tabHomeConsoleOut.setText("MCThunder");
		tabHomeConsoleOut.setBounds(0, 10, 432, 293);
		tabHome.add(tabHomeConsoleOut);
	}
	public static void appendConsoleOut(String appendConsoleMessage)
	{
		((JTextComponent) tabHomeConsoleOut).setText((String) append(((JTextComponent) tabHomeConsoleOut).getText(), appendConsoleMessage));
	}

	private static Object append(String text, String string) {
		// TODO Auto-generated method stub
		return null;
	}
}
