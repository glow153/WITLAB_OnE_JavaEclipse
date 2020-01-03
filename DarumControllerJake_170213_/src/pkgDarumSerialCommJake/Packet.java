package pkgDarumSerialCommJake;

import pkgDarumSnakeGameJake.Cell;

public class Packet {
	private byte[] packet = null;

	private byte getCheckSum() {
		byte ret = packet[1];
		for (int i = 2; i <= (int) packet[2]; i++)
			ret ^= packet[i];
		return ret;
	}

	public Packet(int r, int g, int b) {
		byte[] ba = {(byte)0x02, (byte)0xC0, (byte)0x05,
				(byte)r, (byte)g, (byte)b, (byte)0x00, (byte)0x03};
		packet = ba;
		packet[6] = getCheckSum();
	}
	
	public Packet(int row, int col, int r, int g, int b) {
		byte[] ba = {(byte)0x02, (byte)0xC2, (byte)0x07, (byte)(19-row), (byte)col,
				(byte)r, (byte)g, (byte)b, (byte)0x00, (byte)0x03};
		packet = ba;
		packet[8] = getCheckSum();
	}
	
	public Packet(Cell c) {
		byte[] ba = {(byte)0x02, (byte)0xC2, (byte)0x07, (byte)(19-c.getRow()), (byte)c.getCol(),
				(byte)c.getR(), (byte)c.getG(), (byte)c.getB(), (byte)0x00, (byte)0x03};
		packet = ba;
		packet[8] = getCheckSum();
	}

	public byte[] getBytes() {
		return packet;
	}

	public boolean isValidPkt() {
		boolean bHead = false;
		boolean bTail = false;
		boolean bChksum = false;
		byte chksumTest = (byte) 0x00;
		if (packet[0] == (byte) 0x02)
			bHead = true;
		if (packet[packet.length - 1] == (byte) 0x03)
			bTail = true;
		for (int i = 0; i < (int) packet[2]; i++)
			chksumTest ^= packet[1 + i];
		if (chksumTest == packet[packet.length - 2])
			bChksum = true;
		return bHead && bTail && bChksum;
	}
}
