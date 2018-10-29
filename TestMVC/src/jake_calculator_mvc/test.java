package jake_calculator_mvc;

public class test {
	public static void main(String[] args) {
		VCalculator view = new VCalculator();
		MCalculator model = new MCalculator();
		model.registerObserver(view);
		CCalculator controller = new CCalculator(model, view);
		view.setVisible(true);
	}
}
