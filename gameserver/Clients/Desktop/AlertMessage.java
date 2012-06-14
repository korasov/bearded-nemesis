package DesktopView;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AlertMessage {

	private JFrame frame = new JFrame();
	private Client_Desktop client;
	private JPanel panel = new JPanel();

	AlertMessage(final Client_Desktop client) {
		this.client = client;

		frame.setSize(320, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		panel.setLayout(new FlowLayout());
		frame.add(panel);

	}

	void cancel() {
		JLabel label1 = new JLabel(
				"User refused to play. \nChoose another player.");
		JButton button = new JButton("  Ok  ");

		panel.add(label1);
		panel.add(button);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChoiseView(client);
				frame.dispose();

			}
		});
	}

	void anotherGame() {
		JLabel label1 = new JLabel(
				"Do you want to play with this player again?");
		JButton button1 = new JButton("  Yes  ");
		JButton button2 = new JButton("  No  ");

		panel.add(label1);
		panel.add(button1);
		panel.add(button2);

		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new GameView();
				frame.dispose();

			}
		});

		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChoiseView(client);
				frame.dispose();

			}
		});
	}

}