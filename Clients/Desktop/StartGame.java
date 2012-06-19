package DesktopView;

import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StartGame {

	private JFrame frame = new JFrame();
	private String username = "";

	private StartGame() {

	
		frame.setSize(150, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		final JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		frame.add(panel);

		final TextField addItem = new TextField(15);
		JLabel label1 = new JLabel("Enter your login");
		JButton button = new JButton("  Submit  ");

		panel.add(label1);
		panel.add(addItem);
		panel.add(button);
		panel.revalidate();
					
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				username = addItem.getText();
				System.out.println(username);
				// добавляемся в список клиентов

				Client_Desktop client = new Client_Desktop();
				client.loggin(username);
				frame.dispose();
			}
		});
	}

	public static void main(String[] args) {
		new StartGame();
	}
}
