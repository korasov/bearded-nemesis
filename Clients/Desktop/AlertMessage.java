package DesktopView;

import java.awt.GridLayout;
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
	JLabel label1 = null;
			
	AlertMessage(final Client_Desktop client) {
		this.client = client;

		frame.setSize(320, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		panel.setLayout(new GridLayout(0,1));
		frame.add(panel);
	}

	// Появляется при отказе оппонета от предложеной игры
	void cancel() {
		label1 = new JLabel(
				"User refused to play. Choose another player.");
		JButton button = new JButton("  Ok  ");

		panel.add(label1);
		panel.add(button);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChoiseView(client, Command.playerList);
				frame.dispose();
			}
		});
	}

	// появляется при завершении партии
	void anotherGame(String gameResult) {
		label1 = new JLabel("You" + gameResult
				+ ". Do you want to play with this player again?");
		JButton button1 = new JButton("  Yes  ");
		JButton button2 = new JButton("  No  ");
JPanel but=new JPanel();

		panel.add(label1);
		but.add(button1);
		but.add(button2);
		panel.add(but);
		but.setLayout(new GridLayout());
		
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			client.sendMessage("OFFERRESPONSE: Yes");
				frame.dispose();
			}
		});

		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.sendMessage("OFFERRESPONSE: No");
				new ChoiseView(client, Command.playerList);
				frame.dispose();
			}
		});
	}

	// появляется при прирывании соеденения с сервером
	void serverFail() {
		label1 = new JLabel(
				"Server currently doesn't response. Try later.");
		JButton button = new JButton("  Ok  ");

		panel.add(label1);
		panel.add(button);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
	}

	// пояляется при попытке хода не в свою очередь
	void weitForTurn() {
		serverFail();
		label1.setText("Wait for your turn!");
		}

	public void tryLeate() {
		serverFail();
		label1.setText("There is no players online. Please, wait.");
		
	}

	public void offergame(String opponent) {
		anotherGame("");
		label1.setText("Do you want to play with "+opponent);
		
	}
	
	
}