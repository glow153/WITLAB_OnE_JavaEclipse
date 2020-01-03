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

public class JSnakeGamePanel extends JPanel {	//�� Ŭ���� ��ü�� �г���
	private static final long serialVersionUID = 1L;
	public final int LEVEL = 0;
	public final int LENGTH = 1;
	public final int SPEED = 2;
	public final int TIME = 3;
	public final int SCORE = 4;

	private JButton refBtnStart, refBtnPauseGo;	//��ưȰ��ȭ ��� ���� ���۷���
	private JPanel jpWest, jpCenter;	//�����г�
	private JLabel[] jlCaption, jlValue;	//�����гο� ���� ���̺�

	private boolean isSnakeRunning;	//���� �����̰��ִ�
	private boolean isSnakeAlive;	//���� ����ִ�

	private SerialComm dscm = SerialComm.getInstance();
	private StatusVO status = StatusVO.getInstance();	//���°� ��ü singleton
	private Cell[][] board;	//���Ӻ���
	private ThTimer timer;	//Ÿ�̸�
	private Snake snake;	//�� ��ü
	private Feed feed;

	private void initComps_jpWest() {
		final String[] saCaption = { "���� : ", "���� : ", "�ӵ� : ", "�ð� : ", "���� : " };
		Font fntTxt = new Font("���� ����� 240", Font.PLAIN, 22);
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
		jlValue = new JLabel[jlCaption.length];	//jlCaption�� ���� �ʱ�ȭ�Ǿ��־�� ��
		
		jpCenter.setLayout(new GridLayout(jlValue.length, 1, 0, 7));
		for (int i = 0; i < jlValue.length; i++) {
			jlValue[i] = new JLabel("       ", JLabel.LEFT);
			jlValue[i].setFont(fntValue);
			jpCenter.add(jlValue[i]);
		}
	}

	private void initComps() {	//������Ʈ�� �ʱ�ȭ
		initComps_jpWest();	//jpCenter���� ���� �ʱ�ȭ�Ǿ����
		initComps_jpCenter();
	}

	private void addComps() {	//�����гο� �����гε� ���̱�
		setLayout(new BorderLayout(3, 3));
		add(jpWest, BorderLayout.WEST);
		add(jpCenter, BorderLayout.CENTER);
	}
	
	private void initBoard() {	//���Ӻ��� �ʱ�ȭ
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
	
	public JSnakeGamePanel(JButton jbStart, JButton jbPauseGo) { //�����ڰ� ����������
		refBtnStart = jbStart;
		refBtnPauseGo = jbPauseGo;	//��ưȰ��ȭ ��� ���� ���۷���
		initComps();
		addComps();
		addKeyListener(new JKeyListener());	//Ű�����Է� ������
	}

	private class JKeyListener extends KeyAdapter {	//Ű���� Ư��Ű �Է��� ����
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
	
	private class SnakeStarter extends Thread{	//�� ���� - ���⼭ ��κ��� �����ս��� ������
		private JSnakeGamePanel refJsgp;	//�޽����ڽ��� ���۷���
		public SnakeStarter(JSnakeGamePanel p){
			refJsgp = p;
		}
		public void run(){
			timer.start();
			feed.produce();
			while (true) {
				if (isSnakeRunning) {	// ���� �޸��� ������ ������ ���ƶ�
					if(!snake.isAbleToTurn())	//���� �ٲ� �� ���� ���¶��
						snake.makeBeAbleToTurn(true);//������ �ٲܼ� �ְ� �ض� (ableToTurn = true;)
					if (!snake.confirmFront()) {//������ ���� �� ���� ���¶��(�� cellType �� ����)
						refBtnStart.setEnabled(true);
						timer.off();
						isSnakeAlive = false;
						JOptionPane.showMessageDialog(refJsgp, "���� �׾���� �Ф�");
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
