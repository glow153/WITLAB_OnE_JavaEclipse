package practice_server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import practice_server.listeners.OnCalculationReceivedListener;
import practice_server.listeners.OnChangeNameListener;
import practice_server.listeners.OnClientConnectedListener;
import practice_server.listeners.OnClientDisconnectedListener;
import practice_server.listeners.OnReceivedMsgListener;
import practice_server.listeners.OnRequestHelpListener;
import practice_server.listeners.OnRequestPeopleListener;

public class ClientConnection extends Thread {
	private int id;
	private Socket socket;
	private BufferedReader br;
	private BufferedWriter bw;
	private String name, ip;
	
	protected OnClientConnectedListener conlistener;
	protected OnClientDisconnectedListener dconlistener;
	protected OnReceivedMsgListener msglistener;
	protected OnChangeNameListener namelistener;
	protected OnCalculationReceivedListener calclistener;
	protected OnRequestHelpListener helplistener;
	protected OnRequestPeopleListener peoplelistener;
	
	private boolean isConnected;
	
	public ClientConnection(int id, Socket socket) {
		this.id = id;
		this.socket = socket;
		isConnected = true;
		
		name = socket.getInetAddress().toString();
		ip = socket.getInetAddress().toString();
		
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ClientConnection setOnClientConnectedListener(OnClientConnectedListener conlistener) {
		this.conlistener = conlistener;
		return this;
	}
	
	public ClientConnection setOnClientDisconnectedListener(OnClientDisconnectedListener dconlistener) {
		this.dconlistener = dconlistener;
		return this;
	}
	
	public ClientConnection setOnReceivedMsgListener(OnReceivedMsgListener msglistener) {
		this.msglistener = msglistener;
		return this;
	}
	
	public ClientConnection setOnChangeNameListener(OnChangeNameListener namelistener) {
		this.namelistener = namelistener;
		return this;
	}
	
	public ClientConnection setOnCalculationReceivedListener(OnCalculationReceivedListener calclistener) {
		this.calclistener = calclistener;
		return this;
	}
	
	public ClientConnection setOnRequestHelpListener(OnRequestHelpListener helplistener) {
		this.helplistener = helplistener;
		return this;
	}
	
	public ClientConnection setOnRequestPeopleListener(OnRequestPeopleListener peoplelistener) {
		this.peoplelistener = peoplelistener;
		return this;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String msg = null;
		String[] msgSplit = null;
		
		conlistener.onConnected(id, name);
		
		while(isConnected) {
			try {
				msg = br.readLine(); //메시지 (패킷) 올때 까지 기다림
				msg = msg.trim(); //앞뒤공백제거
			} catch (IOException e) {
				System.err.println(id + "번 클라이언트와의 연결이 끊겼습니다. -----");
				
				try {
					socket.close();
					br.close();
					bw.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				dconlistener.onDisconnected(id, name);
				isConnected = false;
				break;
			}
			
			if (msg != null) { // 메시지 (패킷) 오면 메시지 해석
				msgSplit = msg.split(" ");
				
				try {
					// 이름 변경하기
					if (msgSplit[0].equals("/name")) {
						namelistener.changeName(id, name, msg.split(" ")[1]);
						name = msg.split(" ")[1];
					} else if (msgSplit[0].equals("/help")) {
						helplistener.sendHelp(id);
					} else if (msgSplit[0].equals("/people")) {
						peoplelistener.sendPeople(id);
					} else if (msgSplit.length == 3 &&
									(msgSplit[1].equals("+") ||
									msgSplit[1].equals("-") ||
									msgSplit[1].equals("*") ||
									msgSplit[1].equals("/")) ) {
						msglistener.onReceived(id, name, msg, false);
						calclistener.onReceived(msg);
					} else {
						msglistener.onReceived(id, name, msg, false);
					}
				} catch (Exception e) {
					System.err.println(id + "번 클라이언트가 잘못된 입력을 시도했습니다 : " + msg);
					e.printStackTrace();
				}
			}
		}
	}
	
	public void sendMsg(String msg) {
		try {
			bw.write(msg);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			System.err.println("클라이언트" + id + "번으로의 전송 오류! -----" + msg);
			e.printStackTrace();
		}
	}
	
	public String getClientName() {
		return name;
	}
	
	public String getClientIp() {
		return ip;
	}
	
	public int getClientId() {
		return id;
	}
}
