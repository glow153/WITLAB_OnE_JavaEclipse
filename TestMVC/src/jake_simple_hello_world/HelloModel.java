package jake_simple_hello_world;

import java.util.Observable;

public class HelloModel extends Observable {
	private int counter;

	public HelloModel() {
		System.out.println("HelloModel()");
	}

	public void setValue(int value) {
		// controller�� ȣ��, �ʱ�ȭ��
		counter = value;
		System.out.println("Model init : counter = " + counter);
		setChanged();
		notifyObservers(counter);
	}

	public void incrementValue() {
		// �̺�Ʈ �߻��ϸ� controller�� �̺�Ʈ �ݹ鿡�� �긦 ȣ����
		counter++;
		System.out.println("Model : counter = " + counter);
		setChanged();
		notifyObservers(counter);
	}
}
