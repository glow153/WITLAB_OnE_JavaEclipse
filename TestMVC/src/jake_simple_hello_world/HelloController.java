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
		// model 객체 할당
		System.out.println("Controller : adding Model");
		this.model = m;
	}

	public void addView(HelloView v) {
		// view 객체 할당
		System.out.println("Controller : adding View");
		this.view = v;
	}

	public void initModel(int i) {
		// model의 setter를 호출하여 model 초기값 설정
		model.setValue(i);
	}

	public void actionPerformed(ActionEvent e) {
		// 이벤트 콜백 : 특정 이벤트가 view에서 발생하면 이거 수행
		/*
		System.out.println("Controller : The" + e.getActionCommand()
				+ " button is clicked at " + new Date(e.getWhen())
				+ " with e.paramString " + e.paramString());
		*/
		System.out.println("Controller: acting on Model");
		model.incrementValue();
	}
}
