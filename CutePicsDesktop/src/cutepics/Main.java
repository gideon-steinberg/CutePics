package cutepics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final JFrame frame = new JFrame("Cute Pictures");
		frame.setSize(210, 240);
		JPanel panel = new CutePicPanel();
		frame.add(panel);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.repaint();
		Timer timer = new Timer(15000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.repaint();				
			}
		});
		timer.start();
	}
	
}
