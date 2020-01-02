package pkgIslandJakeMain;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import pkgDefinedPanels.JIntensityPanel;
import pkgDefinedPanels.JPowerPanel;
import pkgDefinedPanels.JSetValuePanel;
import pkgDefinedPanels.JTimePanel;
import pkgIslandSerialComm.Packet;
import pkgIslandSerialComm.ProtocolPackets;
import pkgIslandSerialComm.SerialCommManager;

@SuppressWarnings("serial")
public class IslandJakeWnd extends JFrame implements ActionListener, SerialPortEventListener {
	private final int WNDSIZE_W = 780;
	private final int WNDSIZE_H = 710;
	private final String version = "1.8";
	private final String sTitle = "Ireland Table v" + version + " by Jake";
	private Container ct = getContentPane();//

	private Font fntInfo = new Font("Cambria", Font.BOLD, 16);
	private Font fntCaption = new Font("Georgia", Font.PLAIN, 18);

	private Font fntBorder = new Font("Georgia", Font.PLAIN, 14);
	private Font fntNormal = new Font("한컴 윤고딕 230", Font.PLAIN, 12);

	private Font fntTxt = new Font("Consolas", Font.PLAIN, 13);

	private JPanel jpNorth, jpCenter, jpSouth;

	private JPanel jpPrgInfo;

	private JPanel jpSetting;
	private JLabel jlSetPort, jlsbTime;
	private JTextField jtfPort, jtfsbTime;
	private JButton jbSetPort, jbsbSetTime;

	private JPanel jpCtrlMB, jpCtrlMBSub, jpCtrlMBCaption, jpCtrlMBTop;
	private JLabel jlMBTime;
	private JButton jbFetchMB1, jbFetchMB2, jbFetchMB3;
	private JPanel jpRed, jpBlue, jpFan;
	private JPowerPanel powerpnlRed, powerpnlBlue, powerpnlFan;
	private JIntensityPanel itpnlRed, itpnlBlue, itpnlFan;
	private JSetValuePanel svPWMRed, svPWMBlue, svPWMFan;
	private JSetValuePanel svDutyRed, svDutyBlue, svDutyFan;
	private JTimePanel svOnTimeRed, svOnTimeBlue, svOnTimeFan;
	private JTimePanel svOffTimeRed, svOffTimeBlue, svOffTimeFan;

	private JPanel jpCtrlSB;
	private JButton jbFetchSB;
	private JLabel jlSBTime, jlSBTemp, jlSBHumid, jlSBCrbndx, jlSBLux, jlSBGas;
	private JLabel jlSBTimeV, jlSBTempV, jlSBHumidV, jlSBCrbndxV, jlSBLuxV, jlSBGasV;

	private JButton jbAuto;
	private JPanel jpAuto;

	private JPanel jpConsole;
	private JTextArea jta;
	private JScrollPane jsp;
	private JTextField jtfPkt;
	private JButton jbSendPkt;

	private SerialCommManager scm;
	private ProtocolPackets pp = new ProtocolPackets();

	private volatile boolean bAuto = false;
	private volatile boolean bAutoClicked = false;
	private volatile int pktSendCount = 0;

	public static void main(String[] args) {
		new IslandJakeWnd();
	}

	private void initPanels() {
		jpNorth = new JPanel();
		jpCenter = new JPanel();
		jpSouth = new JPanel();
		jpPrgInfo = new JPanel();
		jpSetting = new JPanel();
		jpCtrlMBCaption = new JPanel();
		jpCtrlMBTop = new JPanel();
		jpCtrlMB = new JPanel();
		jpCtrlMBSub = new JPanel();
		jpRed = new JPanel();
		jpBlue = new JPanel();
		jpFan = new JPanel();
		jpCtrlSB = new JPanel();
		jpConsole = new JPanel();

		jpCtrlMB.setBorder(
				new TitledBorder(null, " MainBoard ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, fntBorder));
		jpCtrlSB.setBorder(
				new TitledBorder(null, " SensorBoard ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, fntBorder));
		jpConsole.setBorder(new TitledBorder(null, " Console ", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, fntBorder));
	}

	private void initComps_BorderPanels() {
		JPanel jpCenterEast = new JPanel();
		jbAuto = new JButton("Click to On");
		jbAuto.addActionListener(this);
		jpAuto = new JPanel();
		jpAuto.setBorder(
				new TitledBorder(null, " Auto Mode ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, fntBorder));
		jpAuto.add(jbAuto);

		jpCenterEast.setLayout(new BorderLayout(5, 5));
		jpCenter.setBorder(new TitledBorder(null, "  Control View  ", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, fntBorder));
		jpNorth.setLayout(new BorderLayout(5, 5));
		jpCenter.setLayout(new BorderLayout(5, 5));
		jpSouth.setLayout(new BorderLayout(5, 5));
		ct.setLayout(new BorderLayout(5, 5));

		jpNorth.add(jpPrgInfo, BorderLayout.CENTER);
		jpNorth.add(jpSetting, BorderLayout.EAST);

		jpCenter.add(jpCtrlMB, BorderLayout.CENTER);

		jpCenterEast.add(jpCtrlSB, BorderLayout.CENTER);
		jpCenterEast.add(jpAuto, BorderLayout.SOUTH);
		jpCenter.add(jpCenterEast, BorderLayout.EAST);

		jpSouth.add(jpConsole, BorderLayout.CENTER);

		ct.add(jpCenter, BorderLayout.CENTER);
		ct.add(jpNorth, BorderLayout.NORTH);
		ct.add(jpSouth, BorderLayout.SOUTH);
	}

	private void initComps_jpPrgInfo() {
		JLabel jl1 = new JLabel("** Ireland Table Controller **", JLabel.CENTER);
		JLabel jl2 = new JLabel("for Developers", JLabel.CENTER);
		JLabel jl3 = new JLabel("version " + version, JLabel.CENTER);
		JLabel jl4 = new JLabel("made by Jake Park", JLabel.CENTER);

		jl1.setFont(new Font("Cambria", Font.BOLD, 22));
		jl2.setFont(fntInfo);
		jl3.setFont(fntInfo);
		jl4.setFont(fntInfo);

		jpPrgInfo.setLayout(new GridLayout(4, 1, 0, 0));
		jpPrgInfo.setBorder(null);
		jpPrgInfo.add(jl1);
		jpPrgInfo.add(jl2);
		jpPrgInfo.add(jl3);
		jpPrgInfo.add(jl4);
	}

	private void initComps_jpSetting() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String time = sdf.format(new Date());
		jlSetPort = new JLabel("포트 설정", JLabel.CENTER);
		jlsbTime = new JLabel("시간 설정", JLabel.CENTER);
		jtfPort = new JTextField("COM6");
		jtfsbTime = new JTextField(time);
		jbSetPort = new JButton("연결");
		jbsbSetTime = new JButton("설정");

		jlSetPort.setFont(fntNormal);
		jlsbTime.setFont(fntNormal);
		jbSetPort.setFont(fntNormal);
		jbsbSetTime.setFont(fntNormal);

		jpSetting.setLayout(new GridLayout(2, 3, 2, 2));
		jpSetting.setBorder(new TitledBorder(null, "  Setting  ", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, fntBorder));

		jpSetting.add(jlSetPort);
		jpSetting.add(jtfPort);
		jpSetting.add(jbSetPort);

		jpSetting.add(jlsbTime);
		jpSetting.add(jtfsbTime);
		jpSetting.add(jbsbSetTime);
	}

	private void initComps_jpCtrlMBTop() {
		jpCtrlMBTop.setLayout(new BorderLayout(5, 5));
		jbFetchMB1 = new JButton("1");
		jbFetchMB2 = new JButton("2");
		jbFetchMB3 = new JButton("3");

		JPanel jpBtns = new JPanel();
		JPanel jp = new JPanel();
		JLabel jlCptn = new JLabel("MainBoard Time :", JLabel.CENTER);
		jlMBTime = new JLabel("?", JLabel.CENTER);

		jpBtns.setLayout(new GridLayout(1, 3, 0, 0));
		jpBtns.setBorder(
				new TitledBorder(null, "Fetch Status", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, fntBorder));
		jpBtns.add(jbFetchMB1);
		jpBtns.add(jbFetchMB2);
		jpBtns.add(jbFetchMB3);
		jp.setLayout(new GridLayout(1, 2, 2, 2));
		jp.add(jlCptn);
		jp.add(jlMBTime);

		jlCptn.setFont(fntNormal);
		jlMBTime.setFont(fntNormal);
		jbFetchMB1.setFont(fntNormal);
		jbFetchMB2.setFont(fntNormal);
		jbFetchMB3.setFont(fntNormal);

		jpCtrlMBTop.add(jpBtns, BorderLayout.WEST);
		jpCtrlMBTop.add(new JLabel(), BorderLayout.CENTER);
		jpCtrlMBTop.add(jp, BorderLayout.EAST);
	}

	private void initComps_jpCtrlMBCaption() {
		JLabel[] jl = new JLabel[7];
		Font f = new Font("한컴 윤고딕 230", Font.PLAIN, 12);
		jl[0] = new JLabel();
		jl[1] = new JLabel("Power", JLabel.RIGHT);
		jl[2] = new JLabel("Intensity", JLabel.RIGHT);
		jl[3] = new JLabel("PWM", JLabel.RIGHT);
		jl[4] = new JLabel("Duty", JLabel.RIGHT);
		jl[5] = new JLabel("On Time", JLabel.RIGHT);
		jl[6] = new JLabel("Off Time", JLabel.RIGHT);
		jpCtrlMBCaption.setLayout(new GridLayout(7, 1, 2, 2));
		for (int i = 0; i < jl.length; i++) {
			jl[i].setFont(f);
			jpCtrlMBCaption.add(jl[i]);
		}
	}

	private void initComps_jpCtrlMBSub() {
		jpCtrlMBSub.setLayout(new GridLayout(1, 3, 5, 5));
		jpCtrlMBSub.add(jpRed);
		jpCtrlMBSub.add(jpBlue);
		jpCtrlMBSub.add(jpFan);
	}

	private void initComps_jpCtrlMB() {
		jpCtrlMB.setLayout(new BorderLayout(5, 5));
		jpCtrlMB.add(jpCtrlMBCaption, BorderLayout.WEST);
		jpCtrlMB.add(jpCtrlMBSub, BorderLayout.CENTER);
		jpCtrlMB.add(jpCtrlMBTop, BorderLayout.NORTH);
	}

	private void initComps_jpRed() {
		JLabel jlCaption = new JLabel("Red", JLabel.CENTER);
		powerpnlRed = new JPowerPanel(1);
		itpnlRed = new JIntensityPanel(1);
		svPWMRed = new JSetValuePanel(1);
		svDutyRed = new JSetValuePanel(1);
		svOnTimeRed = new JTimePanel(1);
		svOffTimeRed = new JTimePanel(1);

		jlCaption.setFont(fntCaption);

		jpRed.setLayout(new GridLayout(7, 1, 2, 2));
		jpRed.add(jlCaption);
		jpRed.add(powerpnlRed.getSource());
		jpRed.add(itpnlRed.getSource());
		jpRed.add(svPWMRed.getSource());
		jpRed.add(svDutyRed.getSource());
		jpRed.add(svOnTimeRed.getSource());
		jpRed.add(svOffTimeRed.getSource());
	}

	private void initComps_jpBlue() {
		JLabel jlCaption = new JLabel("Blue", JLabel.CENTER);
		powerpnlBlue = new JPowerPanel(2);
		itpnlBlue = new JIntensityPanel(2);
		svPWMBlue = new JSetValuePanel(2);
		svDutyBlue = new JSetValuePanel(2);
		svOnTimeBlue = new JTimePanel(2);
		svOffTimeBlue = new JTimePanel(2);

		jlCaption.setFont(fntCaption);

		jpBlue.setLayout(new GridLayout(7, 1, 2, 2));
		jpBlue.add(jlCaption);
		jpBlue.add(powerpnlBlue.getSource());
		jpBlue.add(itpnlBlue.getSource());
		jpBlue.add(svPWMBlue.getSource());
		jpBlue.add(svDutyBlue.getSource());
		jpBlue.add(svOnTimeBlue.getSource());
		jpBlue.add(svOffTimeBlue.getSource());

	}

	private void initComps_jpFan() {
		JLabel jlCaption = new JLabel("Fan", JLabel.CENTER);
		powerpnlFan = new JPowerPanel(3);
		itpnlFan = new JIntensityPanel(3);
		svPWMFan = new JSetValuePanel(3);
		svDutyFan = new JSetValuePanel(3);
		svOnTimeFan = new JTimePanel(3);
		svOffTimeFan = new JTimePanel(3);

		jlCaption.setFont(fntCaption);

		jpFan.setLayout(new GridLayout(7, 1, 2, 2));
		jpFan.add(jlCaption);
		jpFan.add(powerpnlFan.getSource());
		jpFan.add(itpnlFan.getSource());
		jpFan.add(svPWMFan.getSource());
		jpFan.add(svDutyFan.getSource());
		jpFan.add(svOnTimeFan.getSource());
		jpFan.add(svOffTimeFan.getSource());

	}

	private void initComps_jpCtrlSB() {
		jbFetchSB = new JButton("Fetch Status");
		jlSBTime = new JLabel("시간", JLabel.CENTER);
		jlSBTemp = new JLabel("온도", JLabel.CENTER);
		jlSBHumid = new JLabel("습도", JLabel.CENTER);
		jlSBCrbndx = new JLabel("CO₂", JLabel.CENTER);
		jlSBLux = new JLabel("조도", JLabel.CENTER);
		jlSBGas = new JLabel("가스", JLabel.CENTER);
		jlSBTimeV = new JLabel("?", JLabel.CENTER);
		jlSBTempV = new JLabel("?", JLabel.CENTER);
		jlSBHumidV = new JLabel("?", JLabel.CENTER);
		jlSBCrbndxV = new JLabel("?", JLabel.CENTER);
		jlSBLuxV = new JLabel("?", JLabel.CENTER);
		jlSBGasV = new JLabel("?", JLabel.CENTER);

		jbFetchSB.setFont(fntNormal);
		jlSBTime.setFont(fntNormal);
		jlSBTemp.setFont(fntNormal);
		jlSBHumid.setFont(fntNormal);
		jlSBCrbndx.setFont(fntNormal);
		jlSBLux.setFont(fntNormal);
		jlSBGas.setFont(fntNormal);
		jlSBTimeV.setFont(fntNormal);
		jlSBTempV.setFont(fntNormal);
		jlSBHumidV.setFont(fntNormal);
		jlSBCrbndxV.setFont(fntNormal);
		jlSBLuxV.setFont(fntNormal);
		jlSBGasV.setFont(fntNormal);

		jpCtrlSB.setLayout(new GridLayout(7, 2, 5, 5));
		jpCtrlSB.add(jbFetchSB);
		jpCtrlSB.add(new JLabel());
		jpCtrlSB.add(jlSBTime);
		jpCtrlSB.add(jlSBTimeV);
		jpCtrlSB.add(jlSBTemp);
		jpCtrlSB.add(jlSBTempV);
		jpCtrlSB.add(jlSBHumid);
		jpCtrlSB.add(jlSBHumidV);
		jpCtrlSB.add(jlSBCrbndx);
		jpCtrlSB.add(jlSBCrbndxV);
		jpCtrlSB.add(jlSBLux);
		jpCtrlSB.add(jlSBLuxV);
		jpCtrlSB.add(jlSBGas);
		jpCtrlSB.add(jlSBGasV);

	}

	private void initComps_jpConsole() {
		final String introTxt = "";
		JPanel jpCmd = new JPanel();
		jpConsole.setLayout(new BorderLayout(2, 2));
		jpCmd.setLayout(new BorderLayout(2, 2));

		jta = new JTextArea(introTxt);
		jtfPkt = new JTextField();
		jbSendPkt = new JButton("패킷 보내기");
		jta.setRows(12);
		jta.setColumns(105);
		jsp = new JScrollPane(jta, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		jbSendPkt.setFont(fntNormal);

		jta.setEditable(false);
		jta.setFont(fntTxt);
		jtfPkt.setFont(fntTxt);
		jtfPkt.setText("02 01 FF 4C FF 01 FF 01 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 03");

		jpCmd.add(jtfPkt, BorderLayout.CENTER);
		jpCmd.add(jbSendPkt, BorderLayout.EAST);
		jpConsole.add(jsp, BorderLayout.CENTER);
		jpConsole.add(jpCmd, BorderLayout.SOUTH);
	}

	private void initComps() {
		initComps_BorderPanels();
		initComps_jpPrgInfo();
		initComps_jpSetting();
		initComps_jpCtrlMBCaption();
		initComps_jpCtrlMBSub();
		initComps_jpCtrlMBTop();
		initComps_jpCtrlMB();
		initComps_jpCtrlSB();
		initComps_jpRed();
		initComps_jpBlue();
		initComps_jpFan();
		initComps_jpConsole();
	}

	private void addListeners() {
		jbSetPort.addActionListener(this);
		jbsbSetTime.addActionListener(this);
		jbFetchMB1.addActionListener(this);
		jbFetchMB2.addActionListener(this);
		jbFetchMB3.addActionListener(this);
		jbFetchSB.addActionListener(this);
		jbSendPkt.addActionListener(this);
		jtfPkt.addActionListener(this);

		powerpnlRed.getBtn().addActionListener(this);
		powerpnlBlue.getBtn().addActionListener(this);
		powerpnlFan.getBtn().addActionListener(this);
		itpnlRed.getBtnUp().addActionListener(this);
		itpnlBlue.getBtnUp().addActionListener(this);
		itpnlFan.getBtnUp().addActionListener(this);
		itpnlRed.getBtnDown().addActionListener(this);
		itpnlBlue.getBtnDown().addActionListener(this);
		itpnlFan.getBtnDown().addActionListener(this);
		svPWMRed.getBtn().addActionListener(this);
		svPWMBlue.getBtn().addActionListener(this);
		svPWMFan.getBtn().addActionListener(this);
		svDutyRed.getBtn().addActionListener(this);
		svDutyBlue.getBtn().addActionListener(this);
		svDutyFan.getBtn().addActionListener(this);
		svOnTimeRed.getBtn().addActionListener(this);
		svOnTimeBlue.getBtn().addActionListener(this);
		svOnTimeFan.getBtn().addActionListener(this);
		svOffTimeRed.getBtn().addActionListener(this);
		svOffTimeBlue.getBtn().addActionListener(this);
		svOffTimeFan.getBtn().addActionListener(this);
	}

	private void initWnd() {
		setTitle(sTitle);
		setLocation(50, 50);
		setSize(WNDSIZE_W, WNDSIZE_H);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public IslandJakeWnd() {
		initPanels();
		initComps();
		addListeners();
		initWnd();
	}

	private void writeToConsole(String str) {
		jta.append((jta.getText().length() == 0) ? (str) : ("\r\n" + str));
		jta.setCaretPosition(jta.getText().length());
	}

	private String baTimeToStr(byte[] time) {
		String hxHour = "0" + Integer.toHexString(time[0] & 0xff);
		String hxMinute = "0" + Integer.toHexString(time[1] & 0xff);
		return hxHour.substring(hxHour.length() - 2) + ":" + hxMinute.substring(hxMinute.length() - 2);
	}

	private boolean bConnected = false;
	private SerialPort srlPort = null;
	
	private boolean swap = false;

	public void serialEvent(SerialPortEvent event) {
		if (event.isRXCHAR() && event.getEventValue() > 0) {
			Packet pkt = null;
			try {
				byte[] receivedData = srlPort.readBytes();
				System.out.println("Received response: " + Packet.byteArrayToHex(receivedData));
				for (int i = 0; i < receivedData.length - 30; i++) { // verification
					if (receivedData[i] == (byte) 0x02) {
						byte[] baPkt = new byte[30];
						for (int j = 0; j < 30; j++)
							baPkt[j] = receivedData[i + j];
						pkt = new Packet(baPkt);
						if (pkt.isValidPkt())
							break;
						else
							continue;
					}
				}
				if (pkt != null) {
					byte mode = pkt.byteAt(1);
					byte cmd = pkt.byteAt(3);
					if (mode == (byte) 0x02) {
						if (cmd == (byte) 0x53) {
							jlSBTimeV.setText(baTimeToStr(pkt.subarray(7, 8)));
							jlSBTempV.setText(Character.toString((char) pkt.byteAt(10))
									+ Character.toString((char) pkt.byteAt(11)) + "."
									+ Character.toString((char) pkt.byteAt(12)) + " ℃");
							jlSBHumidV.setText(Character.toString((char) pkt.byteAt(14))
									+ Character.toString((char) pkt.byteAt(15)) + "."
									+ Character.toString((char) pkt.byteAt(16)) + " %");
							jlSBCrbndxV.setText((int) (pkt.byteAt(18) - (byte) 0x30) * 1000
									+ (int) (pkt.byteAt(19) - (byte) 0x30) * 100
									+ (int) (pkt.byteAt(20) - (byte) 0x30) * 10 + (int) (pkt.byteAt(21) - (byte) 0x30)
									+ " ppm");
							jlSBLuxV.setText((int) (pkt.byteAt(23) - (byte) 0x30) * 1000
									+ (int) (pkt.byteAt(24) - (byte) 0x30) * 100
									+ (int) (pkt.byteAt(25) - (byte) 0x30) * 10 + (int) (pkt.byteAt(26) - (byte) 0x30)
									+ " lux");
							jlSBGasV.setText(Integer.toString((int) pkt.byteAt(28)));
						}
						
						//trigger
						String s1 = jlSBTimeV.getText();
						if(s1.length() == 5) {
							if (Integer.parseInt(s1.split(":")[0]) >= 19) {
								new Thread(() -> {
									int r = svDutyRed.getIntValue();
									int b = svDutyBlue.getIntValue();
									
									for (int i = 10; i >= 2; i--) {
										svDutyRed.setIntValue((r/10)*i);
										scm.sendPacket(pp.getPWMDutyPkt(svDutyRed.getChNum(), svPWMRed.getVal(), svDutyRed.getVal()));
										svDutyBlue.setIntValue((b/10)*i);
										scm.sendPacket(pp.getPWMDutyPkt(svDutyBlue.getChNum(), svPWMBlue.getVal(), svDutyBlue.getVal()));
										
										try {
											Thread.sleep(500);
										} catch (InterruptedException e) {
											// TODO: handle exception
										}
									}
								}).start();
							}
						}
						
						String s2 = jlSBCrbndxV.getText();
						if(s2.length() >= 5) {
							if (Integer.parseInt(s2.split(" ")[0]) >= 380) {
								swap = !swap;
								int r1 = svPWMRed.getIntValue();
								int b1 = svPWMBlue.getIntValue();
								int r2 = svDutyRed.getIntValue();
								int b2 = svDutyBlue.getIntValue();
								
								new Thread(() -> {
									svDutyRed.setIntValue(swap ? r1 : r2);
									scm.sendPacket(pp.getPWMDutyPkt(svDutyRed.getChNum(), svPWMRed.getVal(), svDutyRed.getVal()));
									svDutyBlue.setIntValue(swap ? b1 : b2);
									scm.sendPacket(pp.getPWMDutyPkt(svDutyBlue.getChNum(), svPWMBlue.getVal(), svDutyBlue.getVal()));
									
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									svDutyRed.setIntValue(swap ? r1 : r2);
									scm.sendPacket(pp.getPWMDutyPkt(svDutyRed.getChNum(), svPWMRed.getVal(), svDutyRed.getVal()));
									svDutyBlue.setIntValue(swap ? b1 : b2);
									scm.sendPacket(pp.getPWMDutyPkt(svDutyBlue.getChNum(), svPWMBlue.getVal(), svDutyBlue.getVal()));
									
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									svDutyRed.setIntValue(swap ? r1 : r2);
									scm.sendPacket(pp.getPWMDutyPkt(svDutyRed.getChNum(), svPWMRed.getVal(), svDutyRed.getVal()));
									svDutyBlue.setIntValue(swap ? b1 : b2);
									scm.sendPacket(pp.getPWMDutyPkt(svDutyBlue.getChNum(), svPWMBlue.getVal(), svDutyBlue.getVal()));
									
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									svDutyRed.setIntValue(swap ? r1 : r2);
									scm.sendPacket(pp.getPWMDutyPkt(svDutyRed.getChNum(), svPWMRed.getVal(), svDutyRed.getVal()));
									svDutyBlue.setIntValue(swap ? b1 : b2);
									scm.sendPacket(pp.getPWMDutyPkt(svDutyBlue.getChNum(), svPWMBlue.getVal(), svDutyBlue.getVal()));
									
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
								}).start();
							}
						}
						
					} else if (mode == (byte) 0x01) {
						if (cmd == (byte) 0x53) {
							jlMBTime.setText(baTimeToStr(pkt.subarray(5, 6)));
							powerpnlRed.setOnOff((pkt.byteAt(8) == (byte) 0x01) ? true : false);
							powerpnlBlue.setOnOff((pkt.byteAt(9) == (byte) 0x01) ? true : false);
							powerpnlFan.setOnOff((pkt.byteAt(10) == (byte) 0x01) ? true : false);
						} else if (cmd == (byte) 0x73) {
							svDutyRed.setVal(pkt.subarray(5, 7));
							svPWMRed.setVal(pkt.subarray(8, 10));
							svDutyBlue.setVal(pkt.subarray(12, 14));
							svPWMBlue.setVal(pkt.subarray(15, 17));
							svDutyFan.setVal(pkt.subarray(19, 21));
							svPWMFan.setVal(pkt.subarray(22, 24));
							itpnlRed.setVal((int) pkt.byteAt(26));
							itpnlBlue.setVal((int) pkt.byteAt(27));
							itpnlFan.setVal((int) pkt.byteAt(28));
						} else if (cmd == (byte) 0x52) {
							svOnTimeRed.setTime(pkt.subarray(5, 6));
							svOffTimeRed.setTime(pkt.subarray(7, 8));
							svOnTimeBlue.setTime(pkt.subarray(10, 11));
							svOffTimeBlue.setTime(pkt.subarray(12, 13));
							svOnTimeFan.setTime(pkt.subarray(15, 16));
							svOffTimeFan.setTime(pkt.subarray(17, 18));
						}
					}
					writeToConsole("received : " + pkt.toString());
					pktSendCount = 0;
				}
			} catch (SerialPortException ex) {
				System.out.println("Error in receiving string from COM-port: " + ex);
			}
		}
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource().equals(jbSetPort)) {
			scm = new SerialCommManager();
			if (scm.initSerialComm(jtfPort.getText())) {
				writeToConsole("successfully connected to " + jtfPort.getText());
				bConnected = true;
				srlPort = scm.getSerialPort();
				try {
					scm.getSerialPort().addEventListener(this);
				} catch (SerialPortException e) {
					e.printStackTrace();
				}
				jbSetPort.setEnabled(false);

			} else {
				writeToConsole("failed to connect to" + jtfPort.getText());
				bConnected = false;
			}
		} else if (bConnected) {
			Packet p = null;
			if (ae.getSource().equals(jbsbSetTime)) {
				new Thread(() -> {
					Packet p1;
					p1 = pp.getSBTimeSetPkt(jtfsbTime.getText().replaceAll(":", ""));
					scm.sendPacket(p1);
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO: handle exception
					}
					p1 = pp.getMBTimeSetPkt(jtfsbTime.getText().replaceAll(":", ""));
					scm.sendPacket(p1);
				}).start();
				return;
			} else if (ae.getSource().equals(jbFetchMB1)) {
				p = pp.getChkMboardPkt();
			} else if (ae.getSource().equals(jbFetchMB2)) {
				p = pp.getChkLEDPkt();
			} else if (ae.getSource().equals(jbFetchMB3)) {
				p = pp.getChkLEDnfTimePkt();
			} else if (ae.getSource().equals(jbFetchSB)) {
				p = pp.getChkSboardPkt();
			} else if (ae.getSource().equals(jtfPkt) || ae.getSource().equals(jbSendPkt)) {
				p = new Packet(jtfPkt.getText());
				jtfPkt.setText(p.toString());
			} else if (ae.getSource().equals(powerpnlRed.getBtn())) {
				p = pp.getPowerCtrlPkt(powerpnlRed.getChNum(), !powerpnlRed.isOn());
				powerpnlRed.setOnOff(!powerpnlRed.isOn());
			} else if (ae.getSource().equals(powerpnlBlue.getBtn())) {
				p = pp.getPowerCtrlPkt(powerpnlBlue.getChNum(), !powerpnlBlue.isOn());
				powerpnlBlue.setOnOff(!powerpnlBlue.isOn());
			} else if (ae.getSource().equals(powerpnlFan.getBtn())) {
				p = pp.getPowerCtrlPkt(powerpnlFan.getChNum(), !powerpnlFan.isOn());
				powerpnlFan.setOnOff(!powerpnlFan.isOn());
			} else if (ae.getSource().equals(itpnlRed.getBtnUp())) {
				p = pp.getIntensityPkt(itpnlRed.getChNum(), itpnlRed.up());
			} else if (ae.getSource().equals(itpnlBlue.getBtnUp())) {
				p = pp.getIntensityPkt(itpnlBlue.getChNum(), itpnlBlue.up());
			} else if (ae.getSource().equals(itpnlFan.getBtnUp())) {
				p = pp.getIntensityPkt(itpnlFan.getChNum(), itpnlFan.up());
			} else if (ae.getSource().equals(itpnlRed.getBtnDown())) {
				p = pp.getIntensityPkt(itpnlRed.getChNum(), itpnlRed.down());
			} else if (ae.getSource().equals(itpnlBlue.getBtnDown())) {
				p = pp.getIntensityPkt(itpnlBlue.getChNum(), itpnlBlue.down());
			} else if (ae.getSource().equals(itpnlFan.getBtnDown())) {
				p = pp.getIntensityPkt(itpnlFan.getChNum(), itpnlFan.down());
			} else if (ae.getSource().equals(svPWMRed.getBtn()) || ae.getSource().equals(svPWMBlue.getBtn())
					|| ae.getSource().equals(svPWMFan.getBtn()) || ae.getSource().equals(svDutyRed.getBtn())
					|| ae.getSource().equals(svDutyBlue.getBtn()) || ae.getSource().equals(svDutyFan.getBtn())) {
				JSetValuePanel jsvpPWM = null;
				JSetValuePanel jsvpDuty = null;
				if (ae.getSource().equals(svPWMRed.getBtn()) || ae.getSource().equals(svDutyRed.getBtn())) {
					jsvpPWM = svPWMRed;
					jsvpDuty = svDutyRed;
				} else if (ae.getSource().equals(svPWMBlue.getBtn()) || ae.getSource().equals(svDutyBlue.getBtn())) {
					jsvpPWM = svPWMBlue;
					jsvpDuty = svDutyBlue;
				} else if (ae.getSource().equals(svPWMFan.getBtn()) || ae.getSource().equals(svDutyFan.getBtn())) {
					jsvpPWM = svPWMFan;
					jsvpDuty = svDutyFan;
				}
				byte[] baPWM = jsvpPWM.getVal();
				byte[] baDuty = jsvpDuty.getVal();
				p = pp.getPWMDutyPkt(jsvpPWM.getChNum(), baPWM, baDuty);
			} else if (ae.getSource().equals(svOnTimeRed.getBtn()) || ae.getSource().equals(svOnTimeBlue.getBtn())
					|| ae.getSource().equals(svOnTimeFan.getBtn()) || ae.getSource().equals(svOffTimeRed.getBtn())
					|| ae.getSource().equals(svOffTimeBlue.getBtn()) || ae.getSource().equals(svOffTimeFan.getBtn())) {
				JTimePanel jtpOnTime = null;
				JTimePanel jtpOffTime = null;
				if (ae.getSource().equals(svOnTimeRed.getBtn()) || ae.getSource().equals(svOffTimeRed.getBtn())) {
					jtpOnTime = svOnTimeRed;
					jtpOffTime = svOffTimeRed;
				} else if (ae.getSource().equals(svOnTimeBlue.getBtn())
						|| ae.getSource().equals(svOffTimeBlue.getBtn())) {
					jtpOnTime = svOnTimeBlue;
					jtpOffTime = svOffTimeBlue;
				} else if (ae.getSource().equals(svOnTimeFan.getBtn())
						|| ae.getSource().equals(svOffTimeFan.getBtn())) {
					jtpOnTime = svOnTimeFan;
					jtpOffTime = svOffTimeFan;
				}
				byte[] ontime = jtpOnTime.getTime();
				byte[] offtime = jtpOffTime.getTime();
				p = pp.getOnOffTimePkt(jtpOnTime.getChNum(), ontime, offtime);
				
			} else if (ae.getSource().equals(jbAuto)) {
				// set auto mode
				bAuto = !bAuto;
				System.out.println("auto mode " + (bAuto ? "on" : "off"));
				jbAuto.setText("Click to " + (bAuto ? "Off" : "On"));
				if (!bAutoClicked) {
					bAutoClicked = true;
					startAutoMode();
				}
				return;
			}
			
			writeToConsole("send pkt : " + p.toString());
			scm.sendPacket(p);
			pktSendCount++;
		} else {
			writeToConsole("You must connect to any COM Port first!");
			return;
		}
	}

	private void startAutoMode() {
		new Thread(() -> {
			final int interval = 2000;
			long milsec = System.currentTimeMillis();
			int seq = 0;
			boolean pause = false;

			while (true) {
				if (bAuto) {
					if (pktSendCount >= 3) {
						pause = true;
					} else {
						pause = false;
					}
					
					if (!pause && System.currentTimeMillis() - milsec > interval) {
						milsec = System.currentTimeMillis();
						Packet p = null;
						switch (seq) {
						case 0:
							p = pp.getChkMboardPkt();
							break;
						case 1:
							p = pp.getChkLEDPkt();
							break;
						case 2:
							p = pp.getChkLEDnfTimePkt();
							break;
						case 3:
							p = pp.getChkSboardPkt();
							break;
						default:
							break;
						}

						writeToConsole("send pkt : " + p.toString());
						scm.sendPacket(p);
						seq = (seq + 1) % 4;
						pktSendCount++;
						System.out.println("pktSendCount : " + pktSendCount);
					}
				}
			}
		}).start();
	}
}
