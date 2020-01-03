package pkgDarumCtrlJakeMain;

import java.util.ArrayList;

public class MDarum {
	private ArrayList<Observer> list = new ArrayList<>();
	public void registerObserver(Observer o) {
		list.add(o);
	}

	public void notifyObservers() {
		for(Observer o : list) {
			//o.update(data);
		}
	}
}
