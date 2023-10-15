import java.util.LinkedList;
import java.util.Queue;

public class AI {
	public int b_curRot, b_curLoc; // �������� bestȸ������,��ġ
	public int b_nextRot, b_nextLoc;
	public int bestHeight;
	public int irrevocableNum; // ������ ���Ǿ��ִ� ��� ��
	public int tmpBoard[][] = new int[20][10];
	int tempHeight;
	int canErase = 0;
	int moveTime = 800;

	User user;
	UserItem p;

	Idx axis1;
	Idx axis2;

	Tetromino temp1;
	Tetromino temp2;
	boolean irrevocable = false;

	static int mark = 5;

	AI(User user, UserItem p) {
		this.user = user;
		this.p = p;
	}

	void init() { // best�� �ʱ�ȭ
		b_curRot = 0;
		b_curLoc = 0;
		bestHeight = 20; // ū ������ �ʱ�ȭ
		irrevocableNum = 0;
		canErase = 0;
	}

	void copyElement() {
		try {
			temp1 = user.currBlock.copy(); // ���� ��� ����
			temp2 = user.nextBlock_.copy(); // next ��� ����
		} catch (CloneNotSupportedException e) {

		}

		for (int row = 0; row < 20; row++) {
			for (int col = 0; col < 10; col++) {
				tmpBoard[row][col] = user.board[row][col]; // user�� ���� ����
			}
		}
	}

	int[][] getValidBlockTmp(Idx p) {
		int valid[][] = new int[4][4];
		int basis_row = p.row - 1;
		int basis_col = p.col - 2;

		// -2 -1 0 1
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				try {
					valid[i][j] = tmpBoard[basis_row + i][basis_col + j];

				} catch (ArrayIndexOutOfBoundsException e) { // ���� 3���� ����
					if (basis_row + i < 0) // ����� 3�� �ƴ� 0���� ����
						valid[i][j] = 0;
					else
						valid[i][j] = 3;
				}

			}
		}

		return valid;
	}

	boolean bump(int tmpvalidBlock[][], Idx axis, Tetromino temp) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				tmpvalidBlock[i][j] += temp.get()[i][j];
				if (tmpvalidBlock[i][j] == 4) { // ���� 4��� ���̳� ��ϰ� �浹�� ��
					if (axis.row + i < 0) // ����� 3�� �ƴ� 0���� ����
						continue;

					return true;
				}
			}
		}

		return false;
	}

	boolean canMoveDown(Idx axis, Tetromino temp) {
		Idx tempAxis = null; // �ӽ� ��(���� �̵����� �� ��)
		tempAxis = new Idx(axis.row + 1, axis.col);

		int tmpvalidBlock[][] = getValidBlockTmp(tempAxis); // �ӽ��࿡ ���� ��ȿ��� ��������

		if (bump(tmpvalidBlock, axis, temp))
			return false;

		return true;
	}

	Idx layBlock(Tetromino temp, int col) {
		Idx axis = new Idx(0, col);
		int tmpvalidBlock[][] = getValidBlockTmp(axis);

		if (bump(tmpvalidBlock, axis, temp))
			return null;

		while (canMoveDown(axis, temp)) {
			axis.row = axis.row + 1; // moveDown
		}

		int basis_row = axis.row - 1; // ȸ������ 1�� 2���̹Ƿ�, ��ȿ����� ������� ��(0�� 0��)�� ��������
		int basis_col = axis.col - 2;

		boolean storeTop = true;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {

				try {
					if (temp.get()[i][j] == 1) {
						if (storeTop) { // ��������
							if (tempHeight < 20 - (basis_row + i))
								tempHeight = 20 - (basis_row + i);
						}
						tmpBoard[basis_row + i][basis_col + j] = 3;
					}

				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}

		return axis;
	}

	void eraseBlock(Idx axis, Tetromino temp) {
		int basis_row = axis.row - 1; // ȸ������ 1�� 2���̹Ƿ�, ��ȿ����� ������� ��(0�� 0��)�� ��������
		int basis_col = axis.col - 2;

		// -2 -1 0 1
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				try {
					if (temp.get()[i][j] == 1) {
						tmpBoard[basis_row + i][basis_col + j] = 0;

						boolean isLow = true;
						if (tempHeight > 20 - (basis_row + i) - 1) {
							for (int col = 0; col < 10; col++) {
								if (tmpBoard[basis_row + i][col] == 3) {
									isLow = false;
									break;
								}
							}

							if (isLow)
								tempHeight = 20 - (basis_row + i) - 1;
						}

					}

				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
	}

	void check() {
		init();
		copyElement();

		for (int loc1 = 0; loc1 < 10; loc1++) { // ���� ��(��ġ ��)��ŭ �ݺ�
			for (int rot1 = 0; rot1 < temp1.num; rot1++) { // ȸ���� �� �ִ� ����� ����ŭ �ݺ�
				temp1.curState = rot1;
				tempHeight = user.totHeight;
				axis1 = layBlock(temp1, loc1);

				if (axis1 == null) // ��ġ�� ���� �� ���ٸ�
					break;

				for (int loc2 = 0; loc2 < 10; loc2++) { // ȸ���� �� �ִ� ����� ����ŭ �ݺ�
					for (int rot2 = 0; rot2 < temp2.num; rot2++) { // ���� ��(��ġ ��)��ŭ �ݺ�
						temp2.curState = rot2;
						axis2 = layBlock(temp2, loc2);

						if (axis2 == null) // ��ġ�� ���� �� ���ٸ�
							break;
						scan();

						eraseBlock(axis2, temp2);
					}
				}

				eraseBlock(axis1, temp1);
			}
		}
	}

	void scan() {
		int irrevocableNumTemp = 0;
		irrevocable = false;
		int canEraseTmp = 0;

		// ����� ã��
		newMark();
		for (int i = 20 - tempHeight; i < 20; i++) {
			if (tempHeight <= 1) // ���̰� 1���϶�� ���� ����� ����.
				break;

			for (int j = 0; j < 10; j++) {
				if (tmpBoard[i][j] == 3 || tmpBoard[i][j] == mark) // visited
					continue;

				int count = bfs(i, j, 20 - tempHeight);

				if (irrevocable) {
					irrevocableNumTemp += count;
					irrevocable = false;
				}
			}
		}

		// ���� �� �ִ� ���� ã��
		for (int row = 20 - tempHeight; row < 20; row++) {
			if (tempHeight <= 0)
				break;

			int sum = 0;

			for (int col = 0; col < 10; col++) {
				if (tmpBoard[row][col] == 3)
					sum += tmpBoard[row][col];
			}

			if (sum >= 30) { // ���� ����
				canEraseTmp++;
			}
		}

		if (canEraseTmp > canErase) {
			renew(canEraseTmp, tempHeight, irrevocableNumTemp);
		} 
		else if (canEraseTmp == canErase) 
		{
			if (tempHeight < bestHeight) 
			{
				renew(canEraseTmp, tempHeight, irrevocableNumTemp);
			} 
			else if (tempHeight == bestHeight) 
			{
				if (irrevocableNumTemp < irrevocableNum) 
				{
					renew(canEraseTmp, tempHeight, irrevocableNumTemp);
				} 
				else 
				{
					if (nearWall()) // temp�� ���� �� �پ��ִٸ�
						renew(canEraseTmp, tempHeight, irrevocableNumTemp);
				}

			} 
		}
	}

	void newMark() {
		if (mark + 1 > 9)
			mark = 5;
		else
			mark++;

	}

	int bfs(int row, int col, int heightRow) {
		int count = 0;
		Queue<Idx> queue = new LinkedList<>();
		tmpBoard[row][col] = mark;

		queue.offer(new Idx(row, col)); // �ڱ� �ڽ� ���

		irrevocable = true;

		while (!queue.isEmpty()) {
			Idx start2 = queue.poll(); // pop
			int tmprow;
			int tmpcol;

			for (int i = 0; i < 4; i++) {
				tmprow = start2.row;
				tmpcol = start2.col;

				switch (i) {
				case 0: // ����
					tmpcol--;
					break;
				case 1: // ������
					tmpcol++;
					break;
				case 2: // ��
					tmprow--;
					break;
				default: // �Ʒ�
					tmprow++;
				}

				try {
					if (tmpBoard[tmprow][tmpcol] == 3 || tmpBoard[tmprow][tmpcol] == mark) // �湮�߳�
						continue;

					tmpBoard[tmprow][tmpcol] = mark; // �湮ǥ��
					queue.offer(new Idx(tmprow, tmpcol));
					count++;

					if (tmprow == heightRow) // ���Ǿ����� �ʴ�
						irrevocable = false;
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}

		return count;
	}

	void renew(int canEraseTmp, int tempHeight, int irrevocableNum) {
		this.bestHeight = tempHeight;
		this.irrevocableNum = irrevocableNum;
		this.canErase = canEraseTmp;

		this.b_curLoc = axis1.col;
		this.b_curRot = temp1.curState;
		this.b_nextLoc = axis2.col;
		this.b_nextRot = temp2.curState;
	}

	boolean nearWall() {
		if (b_curLoc < 5) {
			if (axis1.col < b_curLoc)
				return true;
		} else {
			if (axis1.col > b_curLoc)
				return true;
		}

		return false;
	}

}
