package pkgDefinedPanels;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JPowerPanel {
	private JPanel jp;
	private JLabel jl;
	private JButton jb;
	private boolean bOn;
	private int ch;
	public JPowerPanel(int ch){
		jp = new JPanel();
		jl = new JLabel("?", JLabel.CENTER);
		jb = new JButton("?");
		jl.setFont(new Font("ÇÑÄÄ À±°íµñ 230", Font.PLAIN, 12));
		jb.setFont(new Font("ÇÑÄÄ À±°íµñ 230", Font.PLAIN, 12));
		jp.setLayout(new GridLayout(1,2,2,2));
		jp.add(jl);
		jp.add(jb);
		this.ch = ch;
	}
	public boolean isOn(){
		return bOn;
	}
	public void setOnOff(boolean b){
		bOn = b;
		jl.setText(bOn?"On":"Off");
		jb.setText(bOn?"²ô±â":"ÄÑ±â");
	}
	public JPanel getSource(){
		return jp;
	}
	public JButton getBtn(){
		return jb;
	}
	public int getChNum(){
		return ch;
	}
}
