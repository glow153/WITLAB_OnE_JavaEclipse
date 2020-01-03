package pkgDarumSnakeGameJake;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pkgDarumSerialCommJake.SerialComm;
import pkgDarumSerialCommJake.Packet;

//윈도우 클래스
public class SnakeJakeWnd extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final String version = "v1.6.2";
	private final String S_TITLE = "Darum Controller Jake "+version;
	private final int WNDSIZE_W = 450;
	private final int WNDSIZE_H = 260;

	private Container ct;
	private JButton jbGameStart, jbPauseGo;
	private JPanel jpEast, jpWest, jpNorth;
	private ImageIcon iiStart, iiPause;

	private JSnakeGamePanel sg;
	private SerialComm dscm; // 시리얼통신 객체(singleton)
	
	private boolean bVersionUpgrd = false;
	
	public static void main(String[] args){
		new SnakeJakeWnd();
	}

	private boolean initInstances() { // 각종 객체들 초기화
		String[] btnCaption = { "연결했음!", "조명이 없어요." };
		ct = getContentPane();
		dscm = SerialComm.getInstance();
		while (!dscm.initSerialComm("COM6")) { // 조명 연결 루프
			int ret = JOptionPane.showOptionDialog(this,
						"다룸 조명이 연결되지 않았네요ㅎ\n다룸 조명을 연결하신 후\n'"
						+ btnCaption[0] + "' 버튼을 눌러주세요!", "조명이 연결되지 않았습니다.",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE,
						null, btnCaption, btnCaption[0]);
			if (ret == JOptionPane.YES_OPTION)
				continue;
			else if (ret == JOptionPane.NO_OPTION) {
				JOptionPane.showMessageDialog(this, "다룸 조명이 없으면 게임을 할 수가 없어요.\n"
						+ "조명 사와요! 안녕~");
				return false;
			}
		}
		return true;
	}

	private void initComps_jpNorth() { // 상단패널에 관련된 컴포넌트들 초기화
		JLabel jlTitle1 = new JLabel("Darum Contoller - SnakeGame", JLabel.CENTER);
		JLabel jlTitle2 = new JLabel("made by Jake Park", JLabel.CENTER);
		Font fnt = new Font("DomCasual BT", Font.PLAIN, 24);
		jpNorth = new JPanel();
		jpNorth.setLayout(new GridLayout(2, 1, 0, 0));
		jlTitle1.setFont(fnt);
		jlTitle2.setFont(fnt);
		jpNorth.add(jlTitle1);
		jpNorth.add(jlTitle2);
	}

	private void initComps_jpWest() { // 좌측패널에 관련된 컴포넌트들 초기화
		String[] saTxt = { "< 키 설명 >",
				"→↑←↓ : 방향조절",
				"Enter : 시작/재시작",
				"SpaceBar : 정지/출발",
				"ESC : 종료" };
		JLabel[] jlTxt = new JLabel[saTxt.length];
		Font fntInfo = new Font("나눔바른펜", Font.PLAIN, 13);
		jpWest = new JPanel();
		jpWest.setLayout(new GridLayout(jlTxt.length + 2, 1, 2, 2));

		jlTxt[0] = new JLabel(saTxt[0], JLabel.CENTER);
		jlTxt[0].setFont(new Font("나눔바른펜", Font.BOLD, 20));
		jpWest.add(jlTxt[0]);

		for (int i = 1; i < jlTxt.length; i++) {
			jlTxt[i] = new JLabel(saTxt[i], JLabel.CENTER);
			jlTxt[i].setFont(fntInfo);
			jpWest.add(jlTxt[i]);
		}
		jpWest.add(new JLabel());
		jpWest.add(new JLabel());
	}

	private void initComps_jpEast() { // 우측패널에 관련된 컴포넌트들 초기화
		jpEast = new JPanel();
		iiStart = new ImageIcon("start.png");
		iiPause = new ImageIcon("pause.png");
		jbGameStart = new JButton(iiStart);
		jbPauseGo = new JButton(iiPause);
		
		jbGameStart.setContentAreaFilled(false);
		jbPauseGo.setContentAreaFilled(false);

		jpEast.setLayout(new GridLayout(2, 1, 10, 10));
		jpEast.add(jbGameStart);
		jpEast.add(jbPauseGo);
	}

	private void initComps() { // 좌우상중패널 컨테이너에 붙이기
		initComps_jpEast();
		initComps_jpWest();
		initComps_jpNorth();
		sg = new JSnakeGamePanel(jbGameStart, jbPauseGo);
		ct.setLayout(new BorderLayout(10, 10));
		if(bVersionUpgrd){
			ct.setBackground(new Color(255,235,0));
		}
		ct.add(sg, BorderLayout.CENTER);
		ct.add(jpEast, BorderLayout.EAST);
		ct.add(jpWest, BorderLayout.WEST);
		ct.add(jpNorth, BorderLayout.NORTH);
	}

	private void addListeners() { // 리스너 달기
		jbGameStart.addActionListener(this);
		jbPauseGo.addActionListener(this);
	}

	private void showWnd() { // 윈도우 세팅하고 띄우기
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(WNDSIZE_W, WNDSIZE_H);
		setLocation((screenSize.width - WNDSIZE_W) / 2,
				(screenSize.height - WNDSIZE_H) / 2 - 20);
		setTitle(S_TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void _sleep(int ms) { // 슬립 (try-catch 지저분해서 만듬)
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void intro() { // 모든 초기 세팅 완료 후 띄울 인트로 화면
		int[][] iaaIntro = {
				{ 0,1,1,0,0,1,1,0,0,1,1,0,0,0,0,0,0,0,0,0 },
				{ 0,1,1,0,0,1,1,0,0,1,1,0,1,1,0,0,1,1,0,0 },
				{ 0,1,1,0,0,1,1,0,0,1,1,0,1,1,0,1,1,1,1,0 },
				{ 0,1,1,0,0,1,1,0,0,1,1,0,0,0,0,1,1,1,1,0 },
				{ 0,1,1,0,0,1,1,0,0,1,1,0,1,1,0,0,1,1,0,0 },
				{ 0,0,1,1,0,1,1,0,1,1,0,0,1,1,0,0,1,1,0,0 },
				{ 0,0,1,1,1,1,1,1,1,1,0,0,1,1,0,0,1,1,0,0 },
				{ 0,0,1,1,1,1,1,1,1,1,0,0,1,1,0,0,1,1,0,0 },
				{ 0,0,0,1,1,0,0,1,1,0,0,0,1,1,0,0,1,1,1,0 },
				{ 0,0,0,1,1,0,0,1,1,0,0,0,1,1,0,0,0,1,1,0 },
				{ 0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 },
				{ 0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 },
				{ 0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0 },
				{ 0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0 },
				{ 0,1,1,0,0,0,0,0,0,1,1,1,0,0,0,1,1,0,0,0 },
				{ 0,1,1,0,0,0,0,0,1,1,1,1,1,0,0,1,1,1,1,0 },
				{ 0,1,1,0,0,0,0,0,1,1,0,1,1,0,0,1,1,0,1,1 },
				{ 0,1,1,0,0,0,0,0,1,1,0,1,1,0,0,1,1,0,1,1 },
				{ 0,1,1,1,1,1,1,0,1,1,1,1,1,1,0,1,1,0,1,1 },
				{ 0,1,1,1,1,1,1,0,0,1,1,1,1,1,0,1,1,1,1,0 },
		};
		dscm.sendPacket(new Packet(0, 0, 0));
		jbGameStart.setEnabled(false);
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				if (iaaIntro[i][j] == 1)
					dscm.sendPacket(new Packet(i, j, 30, 30, 30));
			}
		}
		_sleep(1200);
		dscm.sendPacket(new Packet(0, 0, 0));
		jbGameStart.setEnabled(true);
		sg.requestFocus();
	}

	public SnakeJakeWnd() { // 생성자 - private void 어쩌구()가 거의다 여기에 올라와있음
		if(!initInstances()) {
			dispose();
		}
		initComps();
		addListeners();
		showWnd();
		intro();
	}

	public void actionPerformed(ActionEvent ae) { // 버튼 이벤트 처리
		if (ae.getSource().equals(jbGameStart)){
			sg.startSnake();
		} else if (ae.getSource().equals(jbPauseGo)) {
			sg.pauseGoSnake();
		}
	}
}
