package pkgDarumSnakeGameJake;

public class StatusVO {
	private final int[] SPEED = { 500, 400, 280, 210, 170, 135, 105, 85, 65, 50 };
	private int snakeLength, level, score, speed;

	private StatusVO() {}
	private static class Singleton_IODHI {
		private static final StatusVO instance = new StatusVO();
	}
	public static StatusVO getInstance() {
		return Singleton_IODHI.instance;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getSnakeLength() {
		return snakeLength;
	}

	public void setSnakeLength(int snakeLength) {
		this.snakeLength = snakeLength;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void addScore(int s) {
		score += s;
	}
	
	public void increaseSpeed(){
		if(speed >= SPEED.length - 1)
			speed = SPEED.length - 1;
		else
			speed++;
	}
	
	public void decreaseSpeed(){
		if(speed <= 0)
			speed = 0;
		else
			speed--;
	}
	
	public int getMilsec(){
		return SPEED[speed];
	}
}
