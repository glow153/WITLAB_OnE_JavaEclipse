package pkgDefinedPanels;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JTimePanel {
	private JPanel jp;
	private JTextField jtf;
	private JButton jb;
	private byte[] baValue = null;
	private int ch;
	
	public JTimePanel(int ch){
		jp = new JPanel();
		jtf = new JTextField("?");
		jb = new JButton("Set");
		jb.setFont(new Font("ÇÑÄÄ À±°íµñ 230", Font.PLAIN, 12));
		jp.setLayout(new GridLayout(1,2,0,0));
		jp.add(jtf);
		jp.add(jb);
		
		this.ch = ch;
	}
	
	private String baTimeToStr(byte[] time){
		String hxHour = "0"+Integer.toHexString(time[0] & 0xff);
		String hxMinute = "0"+Integer.toHexString(time[1] & 0xff);
		return hxHour.substring(hxHour.length()-2) + ":" + hxMinute.substring(hxMinute.length()-2);
	}
	
	private byte[] strToBaTime(String time){
		byte[] baTime = new byte[2];
		String[] saTime = time.split(":");
		baTime[0] = (byte)Integer.parseInt(saTime[0], 16);
		baTime[1] = (byte)Integer.parseInt(saTime[1], 16);
		return baTime;
	}
	
	public byte[] getTime(){
		baValue = strToBaTime(jtf.getText());
		return baValue;
	}
	
	public void setTime(byte[] time){
		baValue = time;
		jtf.setText(baTimeToStr(time));
	}

	public JButton getBtn(){
		return jb;
	}
	
	public int getChNum(){
		return ch;
	}
	
	public JPanel getSource(){
		return jp;
	}
	
}
