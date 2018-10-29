package jake_simple_hello_world;

public class test {
	private int start_value = 10;
	
	public test(){
		HelloModel model = new HelloModel();
		HelloView view = new HelloView();
		
		model.addObserver(view);
		
		HelloController controller = new HelloController();
		controller.addModel(model);
		controller.addView(view);
		controller.initModel(start_value);
		
		view.addController(controller);
	}
	
	public static void main(String[] args) {
		new test();
	}
}
