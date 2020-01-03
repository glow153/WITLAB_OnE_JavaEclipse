package practice_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import practice_server.listeners.MessageLogCallback;

public class Server {
	public static final int PORT = 7777;
	public static final int CLIENT_MAX = 30;
	protected Socket socket;
	protected ServerSocket server_socket;
	protected LinkedList<ClientConnection> clientPool;
	protected volatile int clientCount;
	protected final String helpMsg = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\r\n"
			+ "@@@@\t공주대학교 컴퓨터공학부 소프트웨어전공\t@@@@\r\n"
			+ "@@@@\t자바프로그래밍 II 실습용 채팅 서버입니다 ^_^\t@@@@\r\n"
			+ "@@@@\t학내에서만 사용 가능하며, 불법적인 접속 시도를\t@@@@\r\n"
			+ "@@@@\t금지합니다. (문의: glow153@gmail.com)\t@@@@\r\n"
			+ "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\r\n"
			+ ">> 원격계산기 : \'(숫자) (연산자) (숫자)\'\r\n"
			+ "   단, 연산자는 +,-,*,/만 가능, 숫자는 소수점 가능\r\n"
			+ ">> 도움말 보기 : /help\r\n"
			+ ">> 현재 접속자 보기 : /people\r\n"
			+ ">> 이름 바꾸기 : /name 이름\r\n";
	
	protected MessageLogCallback callback;
	
	public Server() {
		clientPool = new LinkedList<>();
		clientCount = 0;
		try {
			server_socket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.err.println("서버 에러! ----------");
			e.printStackTrace();
		}
	}
	
	public void setMessageLogCallback(MessageLogCallback callback) {
		this.callback = callback;
	}
	
	protected synchronized void connectClient() {
		clientCount += 1;
		ClientConnection client = new ClientConnection(clientCount, socket)
				.setOnClientConnectedListener((id, name) -> {
					sendMsgToOne(id, helpMsg);
					sendMsgAll("공지> " + name + "님이 입장하셨습니다.");
				})
				.setOnClientDisconnectedListener((id, name) -> {
					deleteClient(id);
					sendMsgAll("공지> " + name + "님이 퇴장하셨습니다.");
				})
				.setOnReceivedMsgListener((id, name, msg, echo) -> {
					if(echo)
						sendMsgAll(name + ": " + msg);
					else
						sendMsgAll(name + ": " + msg, id);
				})
				.setOnChangeNameListener((id, oldName, newName) -> {
					sendMsgAll("공지> " + "이름 변경: " + oldName + " -> " + newName);
				})
				.setOnCalculationReceivedListener(formula -> {
					sendMsgAll("서버관리자: " + calculation(formula));
				})
				.setOnRequestHelpListener(id -> {
					sendMsgToOne(id, helpMsg);
				})
				.setOnRequestPeopleListener(id -> {
					sendClientList(id);
				});
		client.start();
		clientPool.add(client);
	}

	public void sendMsgAll(String msg) {
		clientPool.forEach(client -> {
			if(client != null) {
				client.sendMsg(msg);
				System.out.println(client.getClientId() + " : " + msg);
			}
		});
		callback.writeMsg(msg);
	}
	
	public void sendMsgAll(String msg, int exclusiveClientId) {
		clientPool.forEach(client -> {
			if(client != null && client.getClientId() != exclusiveClientId)
				client.sendMsg(msg);
		});
		callback.writeMsg(msg);
	}
	
	protected void deleteClient(int id) {
		clientPool.removeIf(client -> client.getClientId() == id);
	}
	
	public void sendMsgToOne(int id, String msg) {
		for (ClientConnection client : clientPool) {
			if(client.getClientId() == id) {
				client.sendMsg(helpMsg);
				break;
			}
		}
	}
	
	public void sendClientList(int id) {
		StringBuffer sb = new StringBuffer(">> 현재 접속자수 : " + clientPool.size() + "\r\n"
				+ "  <접속자 리스트>\r\n");
		int target = -1;
		for (int i=0; i<clientPool.size();i++) {
			if (clientPool.get(i).getClientId() == id)
				target = i;
			sb.append("  " + clientPool.get(i).getClientName())
			  .append("(" + clientPool.get(i).getClientIp() + ")")
			  .append("\r\n");
		}
		
		clientPool.get(target).sendMsg(sb.toString());
	}
	
	public void waitForNewClient() {
		new Thread(() -> {
			try {
				while(true) {
					// 클라이언트 접속을 기다림
					socket = server_socket.accept();
					// 클라이언트 접속하면 클라이언트 생성 후 클라이언트풀에 추가
					connectClient();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	private String calculation(String formula) {
		String[] tok = formula.split(" ");
		
		if(tok.length % 2 == 0)
			return "잘못된 수식입니다.";
		try {
			double result = Double.parseDouble(tok[0]);
			for (int i = 1; i < tok.length; i += 2) {
				if (tok[i].equals("+")) {
					result += Double.parseDouble(tok[i + 1]);
				} else if (tok[i].equals("-")) {
					result -= Double.parseDouble(tok[i + 1]);
				} else if (tok[i].equals("×") || tok[i].equals("*")) {
					result *= Double.parseDouble(tok[i + 1]);
				} else if (tok[i].equals("÷") || tok[i].equals("/")) {
					double operand = Double.parseDouble(tok[i + 1]);
					if (operand != 0.0)
						result /= operand;
					else
						result = Double.NaN;
				}
			}
			return formula + " = " + String.format("%.2f", result);
		} catch (Exception e) {
			return "잘못된 수식입니다.";
		}
	}
}
