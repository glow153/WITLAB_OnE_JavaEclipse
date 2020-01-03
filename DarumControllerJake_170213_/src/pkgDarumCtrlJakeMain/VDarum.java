package pkgDarumCtrlJakeMain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class VDarum extends JFrame implements Observer {
	private static final long serialVersionUID = 1L;
	private final int WNDSIZE_W = 720;
	private final int WNDSIZE_H = 780;
	private final String S_TITLE = "다룸 LED 스탠드 제어 프로그램";
	private Container ct;
	private JPanel jpNorth, jpCenter, jpWhole, jpIndiv, jpBoard;
	private JButton jbLogo, jbOff, jbLight[], jbCell[][];
	
	private ImageIcon[] ii;
	private boolean[][] boardState;

	public static void main(String[] args) {
		MDarum model = new MDarum();
		VDarum view = new VDarum();
		model.registerObserver(view);
		CDarum controller = new CDarum(model, view);
		view.setVisible(true);
	}
	
	private void initComps(){
		ct = getContentPane();
		ct.setLayout(new BorderLayout(5, 5));
		ct.setBackground(Color.WHITE);
		
		ii = new ImageIcon[4];
		jbLight = new JButton[ii.length];
		for (int i=0; i<ii.length; i++) {
			ii[i] = new ImageIcon("res/light"+i+".png");
			Image img = ii[i].getImage().getScaledInstance(160, 135, Image.SCALE_SMOOTH);
			ii[i] = new ImageIcon(img);
			jbLight[i] = new JButton(ii[i]);
			//jbLight[i].setBorderPainted(false);
			jbLight[i].setContentAreaFilled(false);
			jbLight[i].setName("light"+i);
		}
		
		ImageIcon logo = new ImageIcon("res/logo.png");
		Image img = logo.getImage().getScaledInstance(58, 33, Image.SCALE_SMOOTH);
		logo = new ImageIcon(img);
		jbLogo = new JButton(logo);
		jbLogo.setBorderPainted(false);
		jbLogo.setContentAreaFilled(false);
		jbLogo.setFocusPainted(false);
		jbLogo.setName("jbLogo");
		
		jbOff = new JButton("전체 off");
		jbOff.setName("off");
		
		jpNorth = new JPanel();
		jpNorth.setLayout(new BorderLayout(2, 2));
		jpNorth.add(jbLogo, BorderLayout.WEST);
		jpNorth.setBackground(Color.WHITE);
		
		jpCenter = new JPanel();
		jpCenter.setLayout(new BorderLayout(5, 5));
		jpCenter.setBackground(Color.WHITE);
		
		jpWhole = new JPanel();
		jpWhole.setBorder(new TitledBorder("전체 제어"));
		jpWhole.setLayout(new GridLayout(1, ii.length, 5, 5));
		jpWhole.setBackground(Color.WHITE);
		
		jpIndiv = new JPanel();
		jpIndiv.setBorder(new TitledBorder("개별 제어"));
		jpIndiv.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		jpIndiv.setBackground(Color.WHITE);
		
		jpBoard = new JPanel();
		jpBoard.setLayout(new GridLayout(20, 20, 2, 2));
		jpBoard.setBackground(Color.WHITE);
		jpBoard.setSize(162, 162);
		
		jbCell = new JButton[20][20];
		boardState = new boolean[20][20];
		for(int i=0;i<20;i++) {
			for(int j=0;j<20;j++) {
				jbCell[i][j] = new JButton("      ");
				jbCell[i][j].setSize(6, 6);
				jbCell[i][j].setBackground(Color.WHITE);
				jbCell[i][j].setBorder(new LineBorder(Color.DARK_GRAY, 2));
				jbCell[i][j].setName(i+","+j);
				
				boardState[i][j] = false;
			}
		}
	}

	private void addComps() {
		ct.add(jpNorth, BorderLayout.NORTH);
		ct.add(jpCenter, BorderLayout.CENTER);

		jpNorth.add(new JLabel(new ImageIcon("res/title.png"), JLabel.CENTER), BorderLayout.CENTER);
		jpNorth.add(jbOff, BorderLayout.EAST);

		jpCenter.add(jpWhole, BorderLayout.NORTH);
		jpCenter.add(jpIndiv, BorderLayout.CENTER);
		
		for(int i=0;i<ii.length;i++)
			jpWhole.add(jbLight[i]);
		
		jpIndiv.add(jpBoard);
		for(int i=0;i<20;i++)
			for(int j=0;j<20;j++)
				jpBoard.add(jbCell[i][j]);
	}

	private void initWnd() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - WNDSIZE_W) / 2, (screenSize.height - WNDSIZE_H) / 2);
		setSize(WNDSIZE_W, WNDSIZE_H);
		setTitle(S_TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setVisible(true);
	}

	public VDarum() {
		initComps();
		addComps();
		initWnd();
	}

	public void setOnClickListener(ActionListener listener) {
		jbLogo.addActionListener(listener);
		jbOff.addActionListener(listener);
		for(int i=0;i<ii.length;i++)
			jbLight[i].addActionListener(listener);
		for(int i=0;i<20;i++)
			for(int j=0;j<20;j++)
				jbCell[i][j].addActionListener(listener);
	}
	
	public void onoff(int r, int c) {
		boardState[r][c] = !boardState[r][c];
		if(isCellOn(r, c))
			jbCell[r][c].setBackground(new Color(200, 100, 200));
		else
			jbCell[r][c].setBackground(new Color(255, 255, 255));
	}
	
	public boolean isCellOn(int r, int c) {
		return boardState[r][c];
	}

	@Override
	public void update(int data) {
	}

}
