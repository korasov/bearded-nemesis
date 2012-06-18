package DesktopView;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameView {
	
	private JFrame frame = new JFrame();
	private JPanel panel=new JPanel();
	private String mark; 
	private String mark2;
	private boolean turn; //право хода
	private Client_Desktop client;
	
	public GameView(String turnSign, Client_Desktop client) {
	this.client=client;
	
	frame.add(panel);
	System.out.println("Знак игры "+ turnSign);
		if (turnSign == "cross") {
			turn = true;
			mark = "X";
			mark2="O";
		} else {
			turn = false;
			mark = "O";
			mark2="X";
		}

		frame.setSize(170, 170);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		panel.setLayout(new GridLayout(3, 3));

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				addButton(Integer.toString(i)+Integer.toString(j));
			}
		}
	}
	
	
	public void put(String message) {
		for(Component comp: panel.getComponents()){
			System.out.println(comp.getClass().getSimpleName());
			if(comp.getClass().getSimpleName().equals("GButton")){
					if (((GButton)comp).num.equals(message.trim())){
					((GButton)comp).setText(mark2);
					turn = true;}					
			}
		}
	}

	void addButton(String name) {
		final GButton botton = new GButton(name);
		panel.add(botton);

		botton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (turn) {
					if (botton.getText().equals("")){
					botton.setText(mark);
					client.put(botton.num);
					turn = false;}
					else System.out.println("Клетка занята");
				}
				else new AlertMessage(client).weitForTurn();
			}
		});
	}
}


