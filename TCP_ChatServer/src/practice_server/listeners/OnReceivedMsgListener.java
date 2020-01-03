package practice_server.listeners;

public interface OnReceivedMsgListener {
	public void onReceived(int id, String name, String msg, boolean echo);
}
