package jake_simple_hello_world;

import java.util.Observable;

public class HelloModel extends Observable {
	private int counter;

	public HelloModel() {
		System.out.println("HelloModel()");
	}

	public void setValue(int value) {
		// controller가 호출, 초기화용
		counter = value;
		System.out.println("Model init : counter = " + counter);
		setChanged();
		notifyObservers(counter);
	}

	public void incrementValue() {
		// 이벤트 발생하면 controller가 이벤트 콜백에서 얘를 호출함
		counter++;
		System.out.println("Model : counter = " + counter);
		setChanged();
		notifyObservers(counter);
	}
}
