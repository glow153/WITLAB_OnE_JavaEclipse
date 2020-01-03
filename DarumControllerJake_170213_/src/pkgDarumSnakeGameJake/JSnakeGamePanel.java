package pkgDarumSnakeGameJake;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pkgDarumSerialCommJake.SerialComm;
import pkgDarumSerialCommJake.Packet;

public class JSnakeGamePanel extends JPanel {	//이 클래스 자체가 패널임
	private static final long serialVersionUID = 1L;
	public final int LEVEL = 0;
	public final int LENGTH = 1;
	public final int SPEED = 2;
	public final int TIME = 3;
	public final int SCORE = 4;

	private JButton refBtnStart, refBtnPauseGo;	//버튼활성화 제어를 위한 레퍼런스
	private JPanel jpWest, jpCenter;	//하위패널
	private JLabel[] jlCaption, jlValue;	//하위패널에 붙을 레이블

	private boolean isSnakeRunning;	//뱀이 움직이고있니
	private boolean isSnakeAlive;	//뱀이 살아있니

	private SerialComm dscm = SerialComm.getInstance();
	private StatusVO status = StatusVO.getInstance();	//상태값 객체 singleton
	private Cell[][] board;	//게임보드
	private ThTimer timer;	//타이머
	private Snake snake;	//뱀 객체
	private Feed feed;

	private void initComps_jpWest() {
		final String[] saCaption = { "레벨 : ", "길이 : ", "속도 : ", "시간 : ", "점수 : " };
		Font fntTxt = new Font("한컴 윤고딕 240", Font.PLAIN, 22);
		jpWest = new JPanel();
		jlCaption = new JLabel[saCaption.length];
		
		jpWest.setLayout(new GridLayout(jlCaption.length, 1, 0, 7));
		for (int i = 0; i < jlCaption.length; i++) {
			jlCaption[i] = new JLabel(saCaption[i], JLabel.RIGHT);
			jlCaption[i].setFont(fntTxt);
			jpWest.add(jlCaption[i]);
		}
	}

	private void initComps_jpCenter() {
		Font fntValue = new Font("Consolas", Font.BOLD, 19);
		jpCenter = new JPanel();
		jlValue = new JLabel[jlCaption.length];	//jlCaption이 먼저 초기화되어있어야 함
		
		jpCenter.setLayout(new GridLayout(jlValue.length, 1, 0, 7));
		for (int i = 0; i < jlValue.length; i++) {
			jlValue[i] = new JLabel("       ", JLabel.LEFT);
			jlValue[i].setFont(fntValue);
			jpCenter.add(jlValue[i]);
		}
	}

	private void initComps() {	//컴포넌트들 초기화
		initComps_jpWest();	//jpCenter보다 먼저 초기화되어야함
		initComps_jpCenter();
	}

	private void addComps() {	//메인패널에 하위패널들 붙이기
		setLayout(new BorderLayout(3, 3));
		add(jpWest, BorderLayout.WEST);
		add(jpCenter, BorderLayout.CENTER);
	}
	
	private void initBoard() {	//게임보드 초기화
		board = new Cell[20][20];
		for (int i = 0; i < 20; i++)
			for (int j = 0; j < 20; j++)
				board[i][j] = new Cell(i, j, false);
		dscm.sendPacket(new Packet(0, 0, 0));
	}
	
	private void initGame(){
		initBoard();
		feed = new Feed(board);
		snake = new Snake(board);
		timer = new ThTimer(jlValue[TIME]);
		status.setLevel(1);
		status.setSpeed(2);
		status.setScore(0);
	}
	
	public JSnakeGamePanel(JButton jbStart, JButton jbPauseGo) { //생성자가 여기있지롱
		refBtnStart = jbStart;
		refBtnPauseGo = jbPauseGo;	//버튼활성화 제어를 위한 레퍼런스
		initComps();
		addComps();
		addKeyListener(new JKeyListener());	//키보드입력 리스너
	}

	private class JKeyListener extends KeyAdapter {	//키보드 특수키 입력을 위해
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch (keyCode) {
			case KeyEvent.VK_UP:
				snake.toUp();
				break;
			case KeyEvent.VK_DOWN:
				snake.toDown();
				break;
			case KeyEvent.VK_LEFT:
				snake.toLeft();
				break;
			case KeyEvent.VK_RIGHT:
				snake.toRight();
				break;
			case KeyEvent.VK_ENTER:
				if(refBtnStart.isEnabled())
					startSnake();
				break;
			case KeyEvent.VK_SPACE:
				pauseGoSnake();
				break;
			case KeyEvent.VK_ESCAPE:
				dscm.sendPacket(new Packet(0, 0, 0));
				dscm.close();
				System.exit(0);
				break;
			}
		}
	}
	
	private class SnakeStarter extends Thread{	//뱀 루프 - 여기서 대부분의 퍼포먼스가 벌어짐
		private JSnakeGamePanel refJsgp;	//메시지박스용 레퍼런스
		public SnakeStarter(JSnakeGamePanel p){
			refJsgp = p;
		}
		public void run(){
			timer.start();
			feed.produce();
			while (true) {
				if (isSnakeRunning) {	// 뱀이 달리고 있으면 루프를 돌아라
					if(!snake.isAbleToTurn())	//방향 바꿀 수 없는 상태라면
						snake.makeBeAbleToTurn(true);//방향을 바꿀수 있게 해라 (ableToTurn = true;)
					if (!snake.confirmFront()) {//앞으로 나갈 수 없는 상태라면(앞 cellType 에 따라)
						refBtnStart.setEnabled(true);
						timer.off();
						isSnakeAlive = false;
						JOptionPane.showMessageDialog(refJsgp, "뱀이 죽었어요 ㅠㅠ");
						break;
					}
					snake.stepSnake();
					feed.checkNeedToProduce();
					setTextToValue();
				}
				_sleep(status.getMilsec() - ((status.getLevel() - 1) * 10));
			}
		}
	}

	public void startSnake() {
		SnakeStarter ss;
		initGame();
		refBtnStart.setEnabled(false);
		ss = new SnakeStarter(this);
		ss.start();
		isSnakeRunning = true;
		isSnakeAlive = true;
		requestFocus();
	}

	public void pauseGoSnake() {
		ImageIcon iiPause = new ImageIcon("pause.png");
		ImageIcon iiGo = new ImageIcon("go.png");
		if (isSnakeAlive) {
			isSnakeRunning = !isSnakeRunning;
			refBtnPauseGo.setIcon(isSnakeRunning ? iiPause : iiGo);
			timer.toggle();
		}
		requestFocus();
	}

	private void setTextToValue() {
		jlValue[LEVEL].setText(Integer.toString(status.getLevel()));
		jlValue[LENGTH].setText(Integer.toString(status.getSnakeLength()));
		jlValue[SPEED].setText(Integer.toString(status.getSpeed()));
		jlValue[SCORE].setText(Integer.toString(status.getScore()));
	}

	private void _sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
