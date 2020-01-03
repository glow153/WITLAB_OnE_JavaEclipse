package practice_server;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainFrame extends JFrame {
	protected static final long serialVersionUID = 1L;
	protected int width;
	protected int height;
	protected String title;
	protected Container ct;
	
	protected JTextField jtfPort;
	protected JButton jbOpenServer;
	protected JTextArea jtaMsg;
	protected JList<String> jlistUser;
	protected DefaultListModel<String> dlmUser;
	protected JScrollPane jspJtaMsg, jspJlistUser;
	
	protected JTextField jtfMsg = new JTextField();
	protected JButton jbSend = new JButton("보내기");
	
	protected Server server;

	public static void main(String[] args) {
		new MainFrame("Jake's 오픈톡방 v1.2", 640, 700);
	}
	
	public MainFrame(String title, int w, int h) {
		this.title = title;
		this.width = w;
		this.height = h;
		
		initComps();
		addComps();
		addListeners();
		initWnd();
	}
	
	protected void initComps() {
		ct = getContentPane();
		
		jtfPort = new JTextField("7777");
		jbOpenServer = new JButton("서버 열기");
		jtaMsg = new JTextArea();
		jtaMsg.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		dlmUser = new DefaultListModel<>();
		jlistUser = new JList<>(dlmUser);
		jspJtaMsg = new JScrollPane(jtaMsg,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jspJlistUser = new JScrollPane(jlistUser,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	
	protected void addComps() {
		JPanel jpNorth =  new JPanel(new GridLayout(1, 2, 5, 5));
		JPanel jpNorth1 =  new JPanel(new BorderLayout(10, 10));
		JPanel jpNorth2 = new JPanel();
		JLabel jlTop = new JLabel("서버 포트 설정", JLabel.CENTER);
		jpNorth1.add(jlTop, BorderLayout.WEST);
		jpNorth1.add(jtfPort, BorderLayout.CENTER);
		jpNorth1.add(jbOpenServer, BorderLayout.EAST);
		jpNorth.add(jpNorth1);
		jpNorth.add(jpNorth2);
		
		JPanel jpSouth = new JPanel(new BorderLayout(10, 10));
		jpSouth.add(new JLabel("채팅"), BorderLayout.WEST);
		jpSouth.add(jtfMsg, BorderLayout.CENTER);
		jpSouth.add(jbSend, BorderLayout.EAST);
		
		ct.setLayout(new BorderLayout());
		ct.add(jpNorth, BorderLayout.NORTH);
		ct.add(jspJtaMsg, BorderLayout.CENTER);
		ct.add(jpSouth, BorderLayout.SOUTH);
	}
	
	protected void addListeners() {
		jbOpenServer.addActionListener(ae -> {
			if (server == null) {
				server = new Server();
				server.setMessageLogCallback(msg -> writeMsg(msg));
				server.waitForNewClient();
			}
			jbOpenServer.setEnabled(false);
		});
		
		ActionListener al = (ae) -> {
			server.sendMsgAll("서버관리자: " + jtfMsg.getText());
			jtfMsg.setText("");
		};
		
		jbSend.addActionListener(al);
		jtfMsg.addActionListener(al);
	}
	
	protected void writeMsg(String msg) {
		jtaMsg.append(msg);
		jtaMsg.append("\r\n");
		jtaMsg.setCaretPosition(jtaMsg.getText().length());
	}
	
	protected void initWnd() {
		setSize(width, height);
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}