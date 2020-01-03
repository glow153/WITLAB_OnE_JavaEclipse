package pkgDarumCtrlJakeMain;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import pkgDarumSerialCommJake.Packet;
import pkgDarumSerialCommJake.SerialComm;
import pkgDarumSnakeGameJake.SnakeJakeWnd;

public class CDarum implements ActionListener {
	private MDarum model;
	private VDarum view;
	private SerialComm sc;
	private int easterEggCount = 0;
	
	public CDarum(MDarum model, VDarum view) {
		this.model = model;
		this.view = view;
		sc = SerialComm.getInstance();
		this.view.setOnClickListener(this);
		sc.initSerialComm("COM20");
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		String name = ((JButton)ae.getSource()).getName();
		if(name == "jbLogo") {
			easterEggCount++;
			if(easterEggCount == 1) {
				((JButton)ae.getSource()).setBackground(Color.RED);				
			} else if(easterEggCount == 2) {
				((JButton)ae.getSource()).setBackground(Color.GREEN);
			} else if(easterEggCount == 3) {
				((JButton)ae.getSource()).setBackground(Color.BLUE);
			} else if(easterEggCount == 4) {
				view.dispose();
				if (sc.close()) {
					SnakeJakeWnd game = new SnakeJakeWnd();
					easterEggCount = 0;
				} else {
				}
			}
		} else if (name == "off") {
			sc.sendPacket(new Packet(0, 0, 0));
			for(int i=0;i<20;i++)
				for(int j=0;j<20;j++)
					if(view.isCellOn(i,j))
						view.onoff(i, j);
		} else if (name.length() >= 6) {
			int selector = (int)(name.charAt(5)-(int)'0');
			switch(selector) {
			case 0:
				sc.sendPacket(new Packet(0x40, 0x30, 0x00));
				break;
			case 1:
				sc.sendPacket(new Packet(0x55, 0xb0, 0x20));
				break;
			case 2:
				sc.sendPacket(new Packet(0x50, 0x90, 0x26));
				break;
			case 3:
				sc.sendPacket(new Packet(0x60, 0x75, 0x06));
				break;
			}
		} else {
			String[] token = name.split(",");
			int row = Integer.parseInt(token[0]);
			int col = Integer.parseInt(token[1]);
			if(!view.isCellOn(row, col)){
				sc.sendPacket(new Packet(row, col, 255, 255, 255));
			} else {
				sc.sendPacket(new Packet(row, col, 0, 0, 0));
			}
			view.onoff(row, col);
		}
	}
}
