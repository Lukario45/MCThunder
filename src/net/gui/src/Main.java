package net.gui.src;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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

public class Main extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void guiTime() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
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
	public Main() {
		
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
		
		TextArea tabHomeConsoleOut = new TextArea();
		tabHomeConsoleOut.setBounds(0, 10, 432, 293);
		tabHome.add(tabHomeConsoleOut);
	}
}
