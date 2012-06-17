package DesktopView;

import javax.swing.JButton;

//клетака игрового поля
class GButton extends JButton {
	
	private static final long serialVersionUID = 1L;
	public String num = "";

	public GButton(String num) {
		this.num = num;
	}
}