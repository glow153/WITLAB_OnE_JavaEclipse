package pkgDefinedPanels;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JIntensityPanel {
	private JPanel jp;
	private JButton jbUp;
	private JLabel jl;
	private JButton jbDown;
	private int value;
	private int ch;
	
	public JIntensityPanel(int ch){
		jp = new JPanel();
		jbUp = new JButton("¡ã");
		jl = new JLabel("?", JLabel.CENTER);
		jbDown = new JButton("¡å");
		jl.setFont(new Font("ÇÑÄÄ À±°íµñ 230", Font.PLAIN, 12));
		jp.setLayout(new BorderLayout(0,0));
		jp.add(jbUp, BorderLayout.WEST);
		jp.add(jl, BorderLayout.CENTER);
		jp.add(jbDown, BorderLayout.EAST);
		
		this.ch = ch;
	}
	
	public JPanel getSource(){
		return jp;
	}
	
	public int up(){
		if(value+1 < 10)
			value++;
		else
			value = 10;
		jl.setText(Integer.toString(value));
		return value;
	}
	
	public int down(){
		if(value-1 > 0)
			value--;
		else
			value = 1;
		jl.setText(Integer.toString(value));
		return value;
	}
	
	public boolean setVal(int i){
		if(1 <= i && i <= 10){
			value = i;
			jl.setText(Integer.toString(value));
			return true;
		}
		else{
			value = 1;
			jl.setText(Integer.toString(value));
			return false;
		}
	}
	
	public JButton getBtnUp(){
		return jbUp;
	}
	public JButton getBtnDown(){
		return jbDown;
	}
	public int getChNum(){
		return ch;
	}
}
