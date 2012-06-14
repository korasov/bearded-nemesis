package DesktopView;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


public class ChoiseView {
	JFrame frame = new JFrame();
	
	List<String> name=null;
	Client_Desktop client;
	
	ChoiseView(final Client_Desktop client){
		this.client=client;
		
		name=client.getPlayerList();
			System.out.println("playerlist "+ name.size());	
	frame.setSize(170, 170);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
	
	JPanel panel = new JPanel();
	panel.setLayout(new FlowLayout());
	frame.add(panel);
	
	JLabel label = new JLabel("Choose player");
	JButton button2 = new JButton("  Submit  ");
	
	panel.add(label);
		
	final JPanel ChoisePanel = new JPanel();
	ChoisePanel.setLayout(new GridLayout(name.size(),0));
	panel.add(ChoisePanel);
	panel.add(button2);
	
	ButtonGroup group = new ButtonGroup();
	final RadioListener myListener = new RadioListener();
	
	for (String s: name){
		JRadioButton radiobutton = new JRadioButton(s);
		radiobutton.setActionCommand(s);
	    group.add(radiobutton);
	    ChoisePanel.add(radiobutton);
	    radiobutton.addActionListener(myListener);
	  }
		
	button2.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			//сервер посылет запрос об играе оппоненту
			//новая игра создается на клиенте в случае положительного ответа
			
			//client.sendMessage("Opponent:"+myListener.playername);
			//client.gameReqest(); вместо 2строки ниже
			
			
			//new AlertMessage(client).cancel();
			//new AlertMessage(client).anotherGame();
			
			new GameView();
			
			frame.dispose(); 
		}
	});
	}
	
}

class RadioListener implements ActionListener {
String playername = null;
public void actionPerformed(ActionEvent e) {
	playername=e.getActionCommand();
	System.out.println(playername);
}
}

