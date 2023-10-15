import java.util.LinkedList;
import java.util.Queue;

public class AI {
	public int b_curRot, b_curLoc; // 현재블록의 best회전상태,위치
	public int b_nextRot, b_nextLoc;
	public int bestHeight;
	public int irrevocableNum; // 완전히 고립되어있는 블록 수
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

	void init() { // best값 초기화
		b_curRot = 0;
		b_curLoc = 0;
		bestHeight = 20; // 큰 값으로 초기화
		irrevocableNum = 0;
		canErase = 0;
	}

	void copyElement() {
		try {
			temp1 = user.currBlock.copy(); // 현재 블록 복제
			temp2 = user.nextBlock_.copy(); // next 블록 복제
		} catch (CloneNotSupportedException e) {

		}

		for (int row = 0; row < 20; row++) {
			for (int col = 0; col < 10; col++) {
				tmpBoard[row][col] = user.board[row][col]; // user의 보드 복제
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

				} catch (ArrayIndexOutOfBoundsException e) { // 벽을 3으로 설정
					if (basis_row + i < 0) // 위라면 3이 아닌 0으로 설정
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
				if (tmpvalidBlock[i][j] == 4) { // 합이 4라면 벽이나 블록과 충돌한 것
					if (axis.row + i < 0) // 위라면 3이 아닌 0으로 설정
						continue;

					return true;
				}
			}
		}

		return false;
	}

	boolean canMoveDown(Idx axis, Tetromino temp) {
		Idx tempAxis = null; // 임시 축(만약 이동했을 때 축)
		tempAxis = new Idx(axis.row + 1, axis.col);

		int tmpvalidBlock[][] = getValidBlockTmp(tempAxis); // 임시축에 대한 유효블록 가져오기

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

		int basis_row = axis.row - 1; // 회전축이 1행 2열이므로, 유효블록의 좌측상단 점(0행 0열)을 기준으로
		int basis_col = axis.col - 2;

		boolean storeTop = true;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {

				try {
					if (temp.get()[i][j] == 1) {
						if (storeTop) { // 높이저장
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
		int basis_row = axis.row - 1; // 회전축이 1행 2열이므로, 유효블록의 좌측상단 점(0행 0열)을 기준으로
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

		for (int loc1 = 0; loc1 < 10; loc1++) { // 열의 수(위치 수)만큼 반복
			for (int rot1 = 0; rot1 < temp1.num; rot1++) { // 회전할 수 있는 경우의 수만큼 반복
				temp1.curState = rot1;
				tempHeight = user.totHeight;
				axis1 = layBlock(temp1, loc1);

				if (axis1 == null) // 위치에 놓을 수 없다면
					break;

				for (int loc2 = 0; loc2 < 10; loc2++) { // 회전할 수 있는 경우의 수만큼 반복
					for (int rot2 = 0; rot2 < temp2.num; rot2++) { // 열의 수(위치 수)만큼 반봇
						temp2.curState = rot2;
						axis2 = layBlock(temp2, loc2);

						if (axis2 == null) // 위치에 놓을 수 없다면
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

		// 고립블록 찾기
		newMark();
		for (int i = 20 - tempHeight; i < 20; i++) {
			if (tempHeight <= 1) // 높이가 1이하라면 고립된 블록이 없다.
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

		// 없앨 수 있는 라인 찾기
		for (int row = 20 - tempHeight; row < 20; row++) {
			if (tempHeight <= 0)
				break;

			int sum = 0;

			for (int col = 0; col < 10; col++) {
				if (tmpBoard[row][col] == 3)
					sum += tmpBoard[row][col];
			}

			if (sum >= 30) { // 라인 삭제
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
					if (nearWall()) // temp가 벽에 더 붙어있다면
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

		queue.offer(new Idx(row, col)); // 자기 자신 담기

		irrevocable = true;

		while (!queue.isEmpty()) {
			Idx start2 = queue.poll(); // pop
			int tmprow;
			int tmpcol;

			for (int i = 0; i < 4; i++) {
				tmprow = start2.row;
				tmpcol = start2.col;

				switch (i) {
				case 0: // 왼쪽
					tmpcol--;
					break;
				case 1: // 오른쪽
					tmpcol++;
					break;
				case 2: // 위
					tmprow--;
					break;
				default: // 아래
					tmprow++;
				}

				try {
					if (tmpBoard[tmprow][tmpcol] == 3 || tmpBoard[tmprow][tmpcol] == mark) // 방문했나
						continue;

					tmpBoard[tmprow][tmpcol] = mark; // 방문표시
					queue.offer(new Idx(tmprow, tmpcol));
					count++;

					if (tmprow == heightRow) // 고립되어있지 않다
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
