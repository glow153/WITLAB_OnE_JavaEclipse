package pkgIslandSerialComm;

public class Packet {
	private byte[] packet = null;
	private int lastIdx = -1;
	public Packet(){
		packet = new byte[30];
	}
	
	public Packet(byte[] baPkt){
		packet = baPkt;
	}
	
	public Packet(byte[] head, byte[] body){
		packet = new byte[head.length + body.length];
		for(int i=0;i<packet.length;i++){
			if(i<head.length)
				packet[i] = head[i];
			else
				packet[i] = body[i-head.length];
		}
	}
	
	public Packet(String sPkt){
		if(sPkt.length() == 60)
			packet = hexToByteArray(sPkt);
		else if(sPkt.length() > 60)
			packet = hexToByteArray(sPkt.replaceAll(" ", ""));
		else
			packet = null;
	}
	
	public String toString(){
		return byteArrayToHex(packet);
	}
	
	public byte byteAt(int i){
		return packet[i];
	}
	
	public byte[] getBytes(){
		return packet;
	}
	
	public void append(byte b){
		if(lastIdx < 29)
			packet[++lastIdx] = b;
	}
	
	public byte[] subarray(int beginIdx, int endIdx){
		byte[] baRet = new byte[endIdx-beginIdx+1];
		for(int i=beginIdx; i<=endIdx; i++)
			baRet[i-beginIdx] = packet[i];
		return baRet;
	}
	
	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}
		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}

	public static String byteArrayToHex(byte[] ba) {
		if (ba == null || ba.length == 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer(ba.length * 2);
		String hexNumber;
		for (int x = 0; x < ba.length; x++) {
			hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
			sb.append(hexNumber.substring(hexNumber.length() - 2).toUpperCase() + " ");
		}
		return sb.toString();
	}
	
	public boolean isValidPkt(){
		boolean bHead = false;
		boolean bTail = false;
		if(packet[1] == 2 || packet[1] == 1)
			bHead = true;
		if(packet[29] == 3)
			bTail = true;
		return bHead && bTail;
	}
}
