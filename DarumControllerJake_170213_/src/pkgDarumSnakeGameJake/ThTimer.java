package pkgDarumSnakeGameJake;

import javax.swing.JLabel;

public class ThTimer extends Thread {
	private long milsec;
	private long minute;
	private long second;
	private long tenthsec;
	private boolean bOff;
	private boolean bStartStop;
	private JLabel jlRef;
	private StatusVO status = StatusVO.getInstance();

	private String int2Str(int num, int len) {
		String tmp = "00" + num;
		return tmp.substring(tmp.length() - len);
	}

	private void _sleep(int ms) {
		try {
			sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public ThTimer(JLabel jl) {
		setName("Timer");
		minute = second = tenthsec = 0;
		bOff = false;
		bStartStop = true;
		jlRef = jl;
	}

	public void toggle() {
		bStartStop = !bStartStop;
	}

	public void off() {
		bOff = true;
	}

	public void run() {
		new Thread(() -> {
			while (!bOff) {
				if (bStartStop) {
					long ms1 = System.currentTimeMillis();
					_sleep(1);
					long ms2 = System.currentTimeMillis();
					milsec += (ms2 - ms1);
					ms1 = ms2;
				} else
					_sleep(1);
			}
		}).start();

		while (!bOff) {
			if (bStartStop) {
				tenthsec = milsec / 100;
				second = milsec / 1000;
				minute = (milsec / 1000) / 60;
				status.setLevel(((int)second / 30) + 1);
				jlRef.setText(int2Str((int) minute, 2) + ":"
						+ int2Str((int) second % 60, 2) + "."
						+ int2Str((int) tenthsec % 10, 1));
			}
			_sleep(50);
		}
	}
}
