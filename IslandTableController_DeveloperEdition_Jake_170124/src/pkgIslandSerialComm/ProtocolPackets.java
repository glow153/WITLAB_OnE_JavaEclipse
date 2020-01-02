package pkgIslandSerialComm;

public class ProtocolPackets {
	private final String spktChkSboard = "0202FF53FF00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03";
	private final String spktChkMboard = "0201FF53FF00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03";
	private final String spktChkLED = "0201FF73FF00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03";
	private final String spktChkLEDnfTime = "0201FF52FF00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03";

	public Packet getChkSboardPkt() {
		return new Packet(spktChkSboard);
	}

	public Packet getChkMboardPkt() {
		return new Packet(spktChkMboard);
	}
	
	public Packet getChkLEDPkt(){
		return new Packet(spktChkLED);
	}
	
	public Packet getChkLEDnfTimePkt(){
		return new Packet(spktChkLEDnfTime);
	}
	
	public Packet getSBTimeSetPkt(String sbtime){
		return new Packet("0202FF54FF00FF"
						+ sbtime
						+ "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
	}
	public Packet getMBTimeSetPkt(String sbtime){
		return new Packet("0201FF54FF00FF"
						+ sbtime
						+ "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
	}
	public Packet getPowerCtrlPkt(int ch, boolean onoff){
		return new Packet("0201FF4C"
				+ "FF0"+ch+"FF"+((onoff)?"01":"00")
				+ "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
	}
	public Packet getIntensityPkt(int ch, int val){
		return new Packet("0201FF49"
				+ "FF0"+ch+"FF0"+((val<10)?val:"A")
				+ "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
	}
	public Packet getPWMDutyPkt(int ch, byte[] PWM, byte[] duty){
		return new Packet("0201FF50"
				+ "FF0"+ch+"FF" + Packet.byteArrayToHex(PWM) + Packet.byteArrayToHex(duty)
				+ "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
	}
	public Packet getOnOffTimePkt(int ch, byte[] ontime, byte[] offtime){
		return new Packet("0201FF55"
				+ "FF0"+ch+"FF" + Packet.byteArrayToHex(ontime) + Packet.byteArrayToHex(offtime)
				+ "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF03");
	}
}
