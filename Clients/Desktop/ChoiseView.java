package DesktopView;

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
	private JFrame frame = new JFrame();
	private JPanel ChoisePanel = new JPanel();
	private JPanel panel = new JPanel();
	
	Client_Desktop client=null;
	List<String> name=null;

	public ChoiseView(final Client_Desktop client, final List<String> name) {
		this.client=client;
		this.name=name;
	
		frame.setSize(170, 170);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		panel.setLayout(new GridLayout(0,1));
		frame.add(panel);

		final JLabel label = new JLabel("No players online. Try leater");
		JButton ok = new JButton("  OK  ");
		
		panel.add(label);
		if(name!=null){ label.setText("Choose player");
		ChoisePanel.setLayout(new GridLayout(name.size(), 0));
		panel.add(ChoisePanel);}
		panel.add(ok);

		ButtonGroup group = new ButtonGroup();
		final RadioListener myListener = new RadioListener();

		for (String s : name) {
			JRadioButton radiobutton = new JRadioButton(s);
			radiobutton.setActionCommand(s);
			group.add(radiobutton);
			ChoisePanel.add(radiobutton);
			radiobutton.addActionListener(myListener);
		}

		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(name!=null)
					client.gameReqest(myListener.playername);
					
				frame.dispose();
					
					
			}
		});
	}
}
