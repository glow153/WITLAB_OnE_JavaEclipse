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
			+ "@@@@\t���ִ��б� ��ǻ�Ͱ��к� ����Ʈ��������\t@@@@\r\n"
			+ "@@@@\t�ڹ����α׷��� II �ǽ��� ä�� �����Դϴ� ^_^\t@@@@\r\n"
			+ "@@@@\t�г������� ��� �����ϸ�, �ҹ����� ���� �õ���\t@@@@\r\n"
			+ "@@@@\t�����մϴ�. (����: glow153@gmail.com)\t@@@@\r\n"
			+ "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\r\n"
			+ ">> ���ݰ��� : \'(����) (������) (����)\'\r\n"
			+ "   ��, �����ڴ� +,-,*,/�� ����, ���ڴ� �Ҽ��� ����\r\n"
			+ ">> ���� ���� : /help\r\n"
			+ ">> ���� ������ ���� : /people\r\n"
			+ ">> �̸� �ٲٱ� : /name �̸�\r\n";
	
	protected MessageLogCallback callback;
	
	public Server() {
		clientPool = new LinkedList<>();
		clientCount = 0;
		try {
			server_socket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.err.println("���� ����! ----------");
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
					sendMsgAll("����> " + name + "���� �����ϼ̽��ϴ�.");
				})
				.setOnClientDisconnectedListener((id, name) -> {
					deleteClient(id);
					sendMsgAll("����> " + name + "���� �����ϼ̽��ϴ�.");
				})
				.setOnReceivedMsgListener((id, name, msg, echo) -> {
					if(echo)
						sendMsgAll(name + ": " + msg);
					else
						sendMsgAll(name + ": " + msg, id);
				})
				.setOnChangeNameListener((id, oldName, newName) -> {
					sendMsgAll("����> " + "�̸� ����: " + oldName + " -> " + newName);
				})
				.setOnCalculationReceivedListener(formula -> {
					sendMsgAll("����������: " + calculation(formula));
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
		StringBuffer sb = new StringBuffer(">> ���� �����ڼ� : " + clientPool.size() + "\r\n"
				+ "  <������ ����Ʈ>\r\n");
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
					// Ŭ���̾�Ʈ ������ ��ٸ�
					socket = server_socket.accept();
					// Ŭ���̾�Ʈ �����ϸ� Ŭ���̾�Ʈ ���� �� Ŭ���̾�ƮǮ�� �߰�
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
			return "�߸��� �����Դϴ�.";
		try {
			double result = Double.parseDouble(tok[0]);
			for (int i = 1; i < tok.length; i += 2) {
				if (tok[i].equals("+")) {
					result += Double.parseDouble(tok[i + 1]);
				} else if (tok[i].equals("-")) {
					result -= Double.parseDouble(tok[i + 1]);
				} else if (tok[i].equals("��") || tok[i].equals("*")) {
					result *= Double.parseDouble(tok[i + 1]);
				} else if (tok[i].equals("��") || tok[i].equals("/")) {
					double operand = Double.parseDouble(tok[i + 1]);
					if (operand != 0.0)
						result /= operand;
					else
						result = Double.NaN;
				}
			}
			return formula + " = " + String.format("%.2f", result);
		} catch (Exception e) {
			return "�߸��� �����Դϴ�.";
		}
	}
}
