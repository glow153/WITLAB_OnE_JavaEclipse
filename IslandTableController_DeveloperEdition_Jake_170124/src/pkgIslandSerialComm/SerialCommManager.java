package pkgIslandSerialComm;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class SerialCommManager {
	private SerialPort srlPort = null;

	public boolean closeSerialPort(){
		boolean bRet = false;
		try {
			bRet = srlPort.closePort();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		return bRet;
	}
	
	public void printSerialPorts(){
		String[] srlPorts = SerialPortList.getPortNames();
		for(int i=0;i<srlPorts.length;i++)
			System.out.println(srlPorts[i]);
	}
	
	public SerialPort getSerialPort(){
		return srlPort;
	}
	
	public boolean initSerialComm(String srlPortName){
		boolean bRet;
		srlPort = new SerialPort(srlPortName);
		try {
			if(srlPort.openPort()){
				bRet = true;
			}
			else{
				bRet = false;
				return bRet;
			}
			srlPort.setParams(SerialPort.BAUDRATE_19200,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
		} catch (SerialPortException e) {
			bRet = false;
			return bRet;
		}
		return bRet;
	}
	
	public void sendPacket(Packet pkt){
		if(!pkt.isValidPkt()){
			System.out.println("error - tried to send invalid packet : " + pkt.toString());
			return;
		}
		try {
			srlPort.writeBytes(pkt.getBytes());
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}
}
