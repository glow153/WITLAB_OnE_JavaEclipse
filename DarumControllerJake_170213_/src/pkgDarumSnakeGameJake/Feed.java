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

	public void produce() {		//�� ���� ����
		/* ���ؾ� �� ����� �� 3����
		 * 1. ���� �� ��ġ�� ����
		 * 2. ���� �Ӹ� ��ġ�� ����
		 * 3. ���� ��ġ�� �� ���� */
		Random r = new Random();
		int lastFeedRow, lastFeedCol;
		do {	//�ϴ� �ѹ� �����غ���
			lastFeedRow = feedRow;
			lastFeedCol = feedCol;
			feedRow = r.nextInt(20);
			feedCol = r.nextInt(20);
		} while (board[feedRow][feedCol].getType() == cellType.SNAKEHEAD	//������ ��ġ�� �Ӹ��̰ų� 
				|| board[feedRow][feedCol].getType() == cellType.SNAKEBODY	//���̰ų�
				|| (feedRow == lastFeedRow && feedCol == lastFeedCol));	//���� ��ġ�� �� ���������� �ٽ� �����ϱ�
		
		switch (r.nextInt(20)) {	//Ÿ�� ���ϱ�
		case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:case 8:case 9:
		case 10:case 11:case 12:	//20���� 13�� Ȯ����
			board[feedRow][feedCol].setType(cellType.FEED_NORMAL);	//�׳ɿ���
			board[feedRow][feedCol].setDuration(60);
			break;
		case 13:case 14:case 15:	//20���� 3�� Ȯ����
			board[feedRow][feedCol].setType(cellType.FEED_FASTER);	//��������
			board[feedRow][feedCol].setDuration(40);
			break;
		case 16:case 17:	//10���� 1�� Ȯ����
			board[feedRow][feedCol].setType(cellType.FEED_SLOWER);	//��������
			board[feedRow][feedCol].setDuration(40);
			break;
		case 18:case 19:	//10���� 1�� Ȯ����
			board[feedRow][feedCol].setType(cellType.FEED_BIGSCORE);//��ѿ���
			board[feedRow][feedCol].setDuration(20);
			break;
		}
	}
	
}
