package pkgDarumSnakeGameJake;

import pkgDarumSerialCommJake.SerialComm;
import pkgDarumSerialCommJake.Packet;

public class Cell {
	SerialComm dscm = SerialComm.getInstance();

	public enum cellType {
		BLANK, SNAKEHEAD, SNAKEBODY, FEED_NORMAL, FEED_FASTER, FEED_SLOWER, FEED_BIGSCORE
	}

	private cellType type;
	private int r;
	private int g;
	private int b;
	private int row;
	private int col;
	private int duration;

	private void setRGB() {
		switch (type) {
		case BLANK:
			r = 0;g = 0;b = 0;
			break;
		case SNAKEHEAD:
			r = 200;g = 0;b = 0;
			break;
		case SNAKEBODY:
			r = 5;g = 128;b = 5;
			break;
		case FEED_NORMAL:
			r = 10;g = 50;b = 20;
			break;
		case FEED_FASTER:
			r = 190;g = 5;b = 5;
			break;
		case FEED_SLOWER:
			r = 5;g = 5;b = 190;
			break;
		case FEED_BIGSCORE:
			r = 170;g = 170;b = 5;
			break;
		}
		dscm.sendPacket(new Packet(this));
	}

	public Cell(cellType type, int row, int col, int duration) {
		this.type = type;
		this.row = row;
		this.col = col;
		this.duration = duration;
		setRGB();
	}

	public Cell(int row, int col, boolean setLight) {
		type = cellType.BLANK;
		r = g = b = 0;
		this.row = row;
		this.col = col;
		duration = 0;
		if (setLight)
			dscm.sendPacket(new Packet(this));
	}

	public String toString() {
		return ("type:" + type + ", R:" + r + ", G:" + g + ", B:" + b
				+ ", row:" + this.row + ", col:" + this.col
				+ ", time:" + this.duration);
	}

	public int getR() {
		return r;
	}

	public int getG() {
		return g;
	}

	public int getB() {
		return b;
	}

	public cellType getType() {
		return type;
	}

	public int getDuration() {
		return duration;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void setType(cellType type) {
		this.type = type;
		setRGB();
	}

	public void setDuration(int dur) {
		this.duration = dur;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public void increment(int i) {
		duration += i;
	}

	public void decrement() {
		duration--;
		if (duration <= 0) {
			type = cellType.BLANK;
			setRGB();
		}
	}

	public void setBlank() {
		type = cellType.BLANK;
		setRGB();
	}

}
