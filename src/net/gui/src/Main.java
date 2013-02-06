package net.gui.src;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;;

public class Main extends JFrame{

	public static void main(String[] a){
		JFrame frame = new JFrame ("MCThunder");
		JButton makebutton = new JButton ("Shutdown");
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(makebutton);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.resize(550, 350);
		}
}

