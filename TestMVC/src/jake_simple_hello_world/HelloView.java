package jake_simple_hello_world;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HelloView extends JFrame implements Observer {
	private static final long serialVersionUID = 1L;
	private final int WNDSIZE_W = 200;
	private final int WNDSIZE_H = 100;
	private final String S_TITLE = "Hello, MVC!";
	private Container ct;

	private JTextField jtf;
	private JButton jbtn;

	private void initComps() {
		ct = getContentPane();
		jtf = new JTextField();
		jbtn = new JButton("Press Me");
	}

	private void addComps() {
		JPanel jp = new JPanel();
		ct.setLayout(new BorderLayout(5, 5));

		ct.add(new JLabel("Counter", JLabel.CENTER), BorderLayout.NORTH);
		ct.add(jtf, BorderLayout.CENTER);
		ct.add(jp, BorderLayout.SOUTH);
		jp.add(jbtn);		
	}

	private void initWnd() {
		setSize(WNDSIZE_W, WNDSIZE_H);
		setTitle(S_TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public HelloView() {
		System.out.println("HelloView()");
		initComps();
		addComps();
		initWnd();
	}

	@Override
	public void update(Observable o, Object arg) {
		jtf.setText(Integer.toString(((Integer)arg).intValue()));
	}
	
	public void setValue(int v) {
		jtf.setText("" + v);
	}
	
	public void addController(HelloController controller) {
		// controller 할당 : 이벤트 리스너 달기
		System.out.println("View : adding controller");
		jbtn.addActionListener(controller);
	}
}
