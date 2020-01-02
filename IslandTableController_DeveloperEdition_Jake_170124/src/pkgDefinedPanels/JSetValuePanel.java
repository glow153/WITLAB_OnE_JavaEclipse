package pkgDefinedPanels;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JSetValuePanel {
	private JPanel jp;
	private JTextField jtf;
	private JButton jb;
	private byte[] baValue = null;
	private int ch;

	public JSetValuePanel(int ch){
		jp = new JPanel();
		jtf = new JTextField("?");
		jb = new JButton("Set");
		jb.setFont(new Font("ÇÑÄÄ À±°íµñ 230", Font.PLAIN, 12));
		jp.setLayout(new GridLayout(1,2,0,0));
		jp.add(jtf);
		jp.add(jb);
		
		this.ch = ch;
	}
	
	public void setVal(byte[] baValueFromPkt){
		int val = 0;
		baValue = baValueFromPkt;
		for(int i=0;i<baValue.length;i++)
			val |= ((baValue[i] & 0xff) << 8*i);
		jtf.setText(Integer.toString(val));
	}
	
	public byte[] getVal(){
		byte[] baRet = new byte[3];
		int val = Integer.parseInt(jtf.getText());
		for(int i=0;i<baRet.length;i++)
			baRet[i] = (byte)((val >> 8*(baRet.length-1-i)) & 0xff);
		baValue = baRet;
		return baValue;
	}
	
	public void setIntValue(int value) {
		jtf.setText(Integer.toString(value));
	}
	
	public int getIntValue() {
		return Integer.parseInt(jtf.getText());
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
