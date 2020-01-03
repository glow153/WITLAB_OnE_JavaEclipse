package pkgDarumSnakeGameJake;

import java.util.Random;

import pkgDarumSnakeGameJake.Cell.cellType;

public class Feed {
	private Cell[][] board;
	int feedRow = 0, feedCol = 0;
	
	public Feed(Cell[][] board){
		this.board = board;
	}
	
	public void decreaseFeedDur(){
		board[feedRow][feedCol].decrement();
	}
	
	public void checkNeedToProduce(){
		if(board[feedRow][feedCol].getType() != cellType.FEED_NORMAL &&
				board[feedRow][feedCol].getType() != cellType.FEED_FASTER &&
				board[feedRow][feedCol].getType() != cellType.FEED_SLOWER &&
				board[feedRow][feedCol].getType() != cellType.FEED_BIGSCORE)
			produce();
	}

	public void produce() {		//뱀 먹이 생성
		/* 피해야 할 경우의 수 3가지
		 * 1. 뱀의 몸 위치에 생성
		 * 2. 뱀의 머리 위치에 생성
		 * 3. 먹은 위치에 또 생성 */
		Random r = new Random();
		int lastFeedRow, lastFeedCol;
		do {	//일단 한번 생성해본다
			lastFeedRow = feedRow;
			lastFeedCol = feedCol;
			feedRow = r.nextInt(20);
			feedCol = r.nextInt(20);
		} while (board[feedRow][feedCol].getType() == cellType.SNAKEHEAD	//생성한 위치가 머리이거나 
				|| board[feedRow][feedCol].getType() == cellType.SNAKEBODY	//몸이거나
				|| (feedRow == lastFeedRow && feedCol == lastFeedCol));	//이전 위치에 또 생성했으면 다시 생성하기
		
		switch (r.nextInt(20)) {	//타입 정하기
		case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:case 8:case 9:
		case 10:case 11:case 12:	//20분의 13의 확률로
			board[feedRow][feedCol].setType(cellType.FEED_NORMAL);	//그냥열매
			board[feedRow][feedCol].setDuration(60);
			break;
		case 13:case 14:case 15:	//20분의 3의 확률로
			board[feedRow][feedCol].setType(cellType.FEED_FASTER);	//빠름열매
			board[feedRow][feedCol].setDuration(40);
			break;
		case 16:case 17:	//10분의 1의 확률로
			board[feedRow][feedCol].setType(cellType.FEED_SLOWER);	//느림열매
			board[feedRow][feedCol].setDuration(40);
			break;
		case 18:case 19:	//10분의 1의 확률로
			board[feedRow][feedCol].setType(cellType.FEED_BIGSCORE);//비싼열매
			board[feedRow][feedCol].setDuration(20);
			break;
		}
	}
	
}
