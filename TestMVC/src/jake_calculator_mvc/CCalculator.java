package jake_calculator_mvc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CCalculator implements ActionListener {
	private MCalculator model;
	private VCalculator view;
	
	public CCalculator(MCalculator model, VCalculator view) {
		this.model = model;
		this.view = view;
		this.view.setCalculatorListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			int num1 = view.getNum1();
			int num2 = view.getNum2();
			model.add(num1,  num2);
			
		} catch (RuntimeException e) {
			
		}
	}
}
