package jake_calculator_mvc;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VCalculator extends JFrame implements Observer {
	private static final long serialVersionUID = 1L;
	private final int WNDSIZE_W = 300;
	private final int WNDSIZE_H = 100;
	private final String S_TITLE = "Calculator - using MVC";
	private Container ct;
	
	private JTextField jtfNum1;
	private JLabel jlAdd;
	private JTextField jtfNum2;
	private JButton jbtnCalc;
	private JTextField jtfResult;
	private JPanel jpCalc;
	
	private void initComps(){
		ct = getContentPane();
		jtfNum1 = new JTextField(10);
		jtfNum2 = new JTextField(10);
		jtfResult = new JTextField(10);
		jlAdd = new JLabel("+");
		jbtnCalc = new JButton("°è»ê");
		jpCalc = new JPanel();
	}
	
	private void addComps(){
		jpCalc.setLayout(new FlowLayout(FlowLayout.CENTER));
		ct.setLayout(new BorderLayout(5, 5));
		
		jpCalc.add(jtfNum1);
		jpCalc.add(jlAdd);
		jpCalc.add(jtfNum2);
		jpCalc.add(jbtnCalc);
		jpCalc.add(jtfResult);
		ct.add(jpCalc);
	}

	private void initWnd(){
		setSize(WNDSIZE_W, WNDSIZE_H);
		setTitle(S_TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public VCalculator(){
		initComps();
		addComps();
		initWnd();
	}
	
	public int getNum1() {
		return Integer.parseInt(jtfNum1.getText());
	}
	
	public int getNum2() {
		return Integer.parseInt(jtfNum2.getText());
	}
	
	public void setCalcSolution(int result) {
		jtfResult.setText(Integer.toString(result));
	}
	
	public void setCalculatorListener(ActionListener listener){
		jbtnCalc.addActionListener(listener);
	}

	@Override
	public void update(int data) {
		setCalcSolution(data);
	}
	
}
