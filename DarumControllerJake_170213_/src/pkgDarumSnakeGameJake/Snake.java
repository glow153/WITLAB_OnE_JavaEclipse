package pkgDarumSnakeGameJake;

import java.util.LinkedList;

import pkgDarumSnakeGameJake.Cell.cellType;

public class Snake {
	private enum direction {
		UP, DOWN, RIGHT, LEFT
	};

	private Cell[][] board;
	private StatusVO status = StatusVO.getInstance();

	private direction snakeDir;
	private LinkedList<Cell> snakeBody = new LinkedList<Cell>();
	private boolean ableToTurn;

	private int snakeHeadRow, snakeHeadCol;
	private int snakeLength;

	public Snake(Cell[][] board) {
		this.board = board;
		initSnake();
	}

	public void initSnake() { // 뱀을 맨 처음 상태로 만들어줌
		snakeDir = direction.RIGHT;
		snakeHeadRow = 9;
		snakeHeadCol = 9;
		snakeLength = 4;
		status.setSnakeLength(snakeLength);

		board[snakeHeadRow][snakeHeadCol] = new Cell(cellType.SNAKEHEAD,
				snakeHeadRow, snakeHeadCol, snakeLength);
		snakeBody.addFirst(board[snakeHeadRow][snakeHeadCol]);
		for (int i = 1; i < snakeLength; i++) {
			switch (snakeDir) {
			case RIGHT:
				board[snakeHeadRow][snakeHeadCol - i] = new Cell(cellType.SNAKEBODY,
						snakeHeadRow, snakeHeadCol - i, snakeLength - i);
				snakeBody.addLast(board[snakeHeadRow][snakeHeadCol - i]);
				break;
			case DOWN:
				board[snakeHeadRow - i][snakeHeadCol] = new Cell(cellType.SNAKEBODY,
						snakeHeadRow - i, snakeHeadCol, snakeLength - i);
				snakeBody.addLast(board[snakeHeadRow - i][snakeHeadCol]);
				break;
			case LEFT:
				board[snakeHeadRow][snakeHeadCol + i] = new Cell(cellType.SNAKEBODY,
						snakeHeadRow, snakeHeadCol + i, snakeLength - i);
				snakeBody.addLast(board[snakeHeadRow][snakeHeadCol + i]);
				break;
			case UP:
				board[snakeHeadRow + i][snakeHeadCol] = new Cell(cellType.SNAKEBODY,
						snakeHeadRow + i, snakeHeadCol, snakeLength - i);
				snakeBody.addLast(board[snakeHeadRow + i][snakeHeadCol]);
				break;
			}
		}
	}

	public void turn(direction dir) {
		snakeDir = dir;
	}

	public boolean confirmFront() { // 뱀 머리 앞에 있는 Cell의 type에 따라 죽느냐 사느냐 결정
		Cell front;
		int tmpRow = snakeHeadRow;
		int tmpCol = snakeHeadCol;
		switch (snakeDir) {
		case RIGHT:
			tmpCol++;
			break;
		case DOWN:
			tmpRow++;
			break;
		case LEFT:
			tmpCol--;
			break;
		case UP:
			tmpRow--;
			break;
		}
		if (tmpRow < 0 || tmpRow > 19 || tmpCol < 0 || tmpCol > 19)
			return false;
		else {
			front = board[tmpRow][tmpCol];
			switch (front.getType()) {
			case SNAKEBODY:
				return false;
			default:
				return true;
			}
		}
	}

	public synchronized void stepSnake() {
		int score = (status.getLevel() - 1) * 5
					+ (status.getSpeed() - 2) * 10
					+ (status.getSnakeLength()/4 - 1) * 5; //열매먹을 때 점수 계산식
		if (score < 0)
			score = 0;
		board[snakeHeadRow][snakeHeadCol].setType(cellType.SNAKEBODY);
		snakeBody.set(0, board[snakeHeadRow][snakeHeadCol]);
		for (int i = 0; i < snakeBody.size(); i++) {
			snakeBody.get(i).decrement();
			if (snakeBody.get(i).getDuration() <= 0)
				snakeBody.remove(i);
		}
		switch (snakeDir) { // 뱀 머리가 올 위치를 잡고
		case RIGHT:
			snakeHeadCol++;
			break;
		case DOWN:
			snakeHeadRow++;
			break;
		case LEFT:
			snakeHeadCol--;
			break;
		case UP:
			snakeHeadRow--;
			break;
		}
		switch (board[snakeHeadRow][snakeHeadCol].getType()) { // 열매 먹기
		case FEED_NORMAL:
			status.addScore(score + 10);
			growSnake(1);
			break;
		case FEED_FASTER:
			status.addScore(score + 20);
			status.increaseSpeed();
			break;
		case FEED_SLOWER:
			status.addScore(score + 5);
			status.decreaseSpeed();
			break;
		case FEED_BIGSCORE:
			status.addScore(score + 50);
			growSnake(3);
			break;
		default:
			break;
		}
		board[snakeHeadRow][snakeHeadCol].setType(cellType.SNAKEHEAD); // 열매
		board[snakeHeadRow][snakeHeadCol].setDuration(snakeLength);
		snakeBody.addFirst(board[snakeHeadRow][snakeHeadCol]);
	}

	public void growSnake(int amount) {
		for (int i = 0; i < snakeBody.size(); i++)
			snakeBody.get(i).increment(amount);
		snakeLength += amount;
		status.setSnakeLength(snakeLength);
	}

	public boolean isAbleToTurn() {
		return ableToTurn;
	}

	public void makeBeAbleToTurn(boolean canTurn) {
		ableToTurn = canTurn;
	}

	public synchronized void toUp() {
		if (ableToTurn) {
			if (snakeDir == direction.DOWN)
				return;
			else {
				snakeDir = direction.UP;
				ableToTurn = false;
			}
		}
	}

	public synchronized void toDown() {
		if (ableToTurn) {
			if (snakeDir == direction.UP)
				return;
			else {
				snakeDir = direction.DOWN;
				ableToTurn = false;
			}
		}
	}

	public synchronized void toLeft() {
		if (ableToTurn) {
			if (snakeDir == direction.RIGHT)
				return;
			else {
				snakeDir = direction.LEFT;
				ableToTurn = false;
			}
		}
	}

	public synchronized void toRight() {
		if (ableToTurn) {
			if (snakeDir == direction.LEFT)
				return;
			else {
				snakeDir = direction.RIGHT;
				ableToTurn = false;
			}
		}
	}
}
