package jake_simple_hello_world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelloController implements ActionListener {
	private HelloModel model;
	private HelloView view;

	public HelloController() {
		System.out.println("HelloController()");
	}

	public void addModel(HelloModel m) {
		System.out.println("Controller : adding Model");
		this.model = m;
	}

	public void addView(HelloView v) {
		System.out.println("Controller : adding View");
		this.view = v;
	}

	public void initModel(int i) {
		model.setValue(i);
	}

	public void actionPerformed(ActionEvent e) {
		/*
		System.out.println("Controller : The" + e.getActionCommand()
				+ " button is clicked at " + new Date(e.getWhen())
				+ " with e.paramString " + e.paramString());
		*/
		System.out.println("Controller: acting on Model");
		model.incrementValue();
	}
}
