package DesktopView;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class GameView {
	JFrame frame = new JFrame();
	String mark="O"; //берем с сервера
	
	GameView() {

		frame.setSize(170, 170);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLayout(new GridLayout(3, 3));

		for (int i = 0; i < 9; i++) {
			addButton();
		}
	}

	void addButton() {
		final JButton button = new JButton();
		frame.add(button);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//посылаем на сервер запрос можно ли тут поставить крестик, если да, то
				button.setText(mark);
			}
		});
	}
}
