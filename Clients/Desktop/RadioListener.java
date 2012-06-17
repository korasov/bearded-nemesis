package DesktopView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class RadioListener implements ActionListener {
	String playername = null;

	public void actionPerformed(ActionEvent e) {
		playername = e.getActionCommand();
		System.out.println(playername);
	}
}
