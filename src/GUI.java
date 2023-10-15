import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.LineBorder;


final class Flags {
	public static final int GENERAL = 1;
	public static final int MATCH = 2;
}

final class MyColor {
	public static final Color DARKGRAY = new Color(38, 38, 38);
	public static final Color BLUE = new Color(47, 82, 143);
	public static final Color DEEPBLUE = new Color(30, 54, 96);
}

class MyFrame extends JFrame {
	CardLayout cardLayout = new CardLayout();

	public MyFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Tetris");
		setSize(1200, 800);
		setLayout(cardLayout);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
	}
}

class MainFrame extends MyFrame {
	StartPage startPage;
	ModePage modePage;
	public static GamePage_1P gamePage_1P;
	public static GamePage_2P gamePage_2P;
	public static GamePage_AI gamePage_AI;
	public int modeFlag = 0;

	public MainFrame() {
		startPage = new StartPage(this);
		modePage = new ModePage(this);
		gamePage_1P = new GamePage_1P(this);
		gamePage_2P = new GamePage_2P(this);
		gamePage_AI = new GamePage_AI(this);

		add(startPage, "startP");
		add(modePage, "modeP");
		add(gamePage_1P, "game1P");
		add(gamePage_2P, "game2P");
		add(gamePage_AI, "gameAI");
	}
}

class StartPage extends JPanel {
	private JLabel title = new JLabel("테트리스 게임");
	private JButton startBtn = new JButton("게임시작");
	private MyFrame F;

	public StartPage(MyFrame f) {
		setBackground(MyColor.DARKGRAY);
		setLayout(null);

		F = f;

		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String btnInfo = e.getActionCommand();
				if (btnInfo.equals("게임시작")) {
					F.cardLayout.show(F.getContentPane(), "modeP");
				}
			}
		});

		Font titleFont = new Font("Aharoni 굵게", Font.BOLD, 30);
		title.setFont(titleFont);
		title.setBounds(480, 280, 200, 50);
		title.setForeground(Color.white);

		Font font = new Font("Aharoni 굵게", Font.BOLD, 15);
		startBtn.setBounds(510, 430, 130, 50);
		startBtn.setBackground(MyColor.BLUE);
		startBtn.setForeground(Color.BLACK);
		startBtn.setBorderPainted(false);
		startBtn.setFont(font);

		add(title);
		add(startBtn);

		setVisible(true);
	}
}

class ModePage extends JPanel {
	private JLabel modeInfo = new JLabel("모드 선택");
	private JButton oneBtn = new JButton("1인용 모드");
	private JButton twoBtn = new JButton("2인용 모드");
	private JButton twoCompetitionBtn = new JButton("2인용 대전 모드");
	private JButton aICompetitionBtn = new JButton("인공지능 모드");

	private MyFrame F;

	public ModePage(MyFrame f) {
		setBackground(MyColor.DARKGRAY);
		setLayout(null);

		F = f;

		modeInfo.setBounds(510, 140, 200, 50);
		oneBtn.setBounds(480, 220, 200, 50);
		twoBtn.setBounds(480, 300, 200, 50);
		twoCompetitionBtn.setBounds(480, 380, 200, 50);
		aICompetitionBtn.setBounds(480, 460, 200, 50);

		Font infoFont = new Font("Aharoni 굵게", Font.BOLD, 30);
		modeInfo.setForeground(Color.white);
		modeInfo.setFont(infoFont);

		Font font = new Font("Aharoni 굵게", Font.BOLD, 15);
		oneBtn.setBackground(MyColor.BLUE);
		oneBtn.setForeground(Color.BLACK);
		oneBtn.setBorderPainted(false);
		oneBtn.setFont(font);

		twoBtn.setBackground(MyColor.BLUE);
		twoBtn.setForeground(Color.BLACK);
		twoBtn.setBorderPainted(false);
		twoBtn.setFont(font);

		twoCompetitionBtn.setBackground(MyColor.BLUE);
		twoCompetitionBtn.setForeground(Color.BLACK);
		twoCompetitionBtn.setBorderPainted(false);
		twoCompetitionBtn.setFont(font);

		aICompetitionBtn.setBackground(MyColor.BLUE);
		aICompetitionBtn.setForeground(Color.BLACK);
		aICompetitionBtn.setBorderPainted(false);
		aICompetitionBtn.setFont(font);

		add(modeInfo);
		add(oneBtn);
		add(twoBtn);
		add(twoCompetitionBtn);
		add(aICompetitionBtn);

		oneBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String btnInfo = e.getActionCommand();
				if (btnInfo.equals("1인용 모드")) {
					F.cardLayout.show(F.getContentPane(), "game1P");
					MainFrame.gamePage_1P.initGame();
					MainFrame.gamePage_1P.startClock();
				}
			}
		});

		twoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String btnInfo = e.getActionCommand();
				if (btnInfo.equals("2인용 모드")) {
					F.cardLayout.show(F.getContentPane(), "game2P");
					MainFrame.gamePage_2P.setFlag(Flags.GENERAL);
					MainFrame.gamePage_2P.initGame();
					MainFrame.gamePage_2P.startClock();
				}
			}
		});

		twoCompetitionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String btnInfo = e.getActionCommand();
				if (btnInfo.equals("2인용 대전 모드")) {
					F.cardLayout.show(F.getContentPane(), "game2P");
					MainFrame.gamePage_2P.setFlag(Flags.MATCH);
					MainFrame.gamePage_2P.initGame();
					MainFrame.gamePage_2P.startClock();
				}
			}
		});
		
		aICompetitionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String btnInfo = e.getActionCommand();
				if (btnInfo.equals("인공지능 모드")) {
					F.cardLayout.show(F.getContentPane(), "gameAI");
					MainFrame.gamePage_AI.setFlag(Flags.MATCH);
					MainFrame.gamePage_AI.initGame();
					MainFrame.gamePage_AI.startClock();
					MainFrame.gamePage_AI.startAI();
				}
			}
		});
	}
}

abstract class GamePage extends JPanel {
	MyFrame F;

	ScheduledJob job;

	JLabel tmpLabel = new JLabel("    ");

	UserItem p1 = null;
	UserItem p2 = null;
	User user1 = null;
	User user2 = null;

	int eliminatedNum = 0;

	void initGame() {
		user1.init();

		if (user2 != null) // 2인용 모드라면
			user2.init();

		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 10; j++) {
				user1.board[i][j] = 0;
				p1.gameBoard[i][j].setBackground(Color.BLACK);
				p1.gameBoard[i][j].setBorder(new LineBorder(Color.DARK_GRAY));

				if (user2 != null) {
					user2.board[i][j] = 0;
					p2.gameBoard[i][j].setBackground(Color.BLACK);
					p2.gameBoard[i][j].setBorder(new LineBorder(Color.DARK_GRAY));
				}
			}
		}

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				p1.holdBlock[i][j].setBackground(Color.BLACK);
				p1.nextBlock[i][j].setBackground(Color.BLACK);
				p1.holdBlock[i][j].setBorder(new LineBorder(Color.DARK_GRAY));
				p1.nextBlock[i][j].setBorder(new LineBorder(Color.DARK_GRAY));

				if (user2 != null) {
					p2.holdBlock[i][j].setBackground(Color.BLACK);
					p2.nextBlock[i][j].setBackground(Color.BLACK);
					p2.holdBlock[i][j].setBorder(new LineBorder(Color.DARK_GRAY));
					p2.nextBlock[i][j].setBorder(new LineBorder(Color.DARK_GRAY));
				}
			}
		}

		drawCurrBlock(user1);
		drawNextBlock(user1);
		drawShadow(user1);

		if (user2 != null) {
			drawNextBlock(user2);
			drawCurrBlock(user2);
			drawShadow(user2);
		}
	}

	boolean bump(User user, int tmpvalidBlock[][]) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				tmpvalidBlock[i][j] += user.currBlock.get()[i][j];
				if (tmpvalidBlock[i][j] > 3) { // 합이 3을 넘는다면 벽이나 블록과 충돌한 것
					if (user.validBlock.axis.row + i < 0) // 위라면 3이 아닌 0으로 설정
						continue;

					return true;
				}
			}
		}

		return false;
	}

	void rotateBlock(User user) {
		eraseCurrBlock(user);
		user.currBlock.rotate();

		int tmpvalidBlock[][] = getValidBlock(user, user.validBlock.axis);

		while (bump(user, tmpvalidBlock)) {
			if (user.validBlock.axis.col < 4)
				user.validBlock.axis.col++;
			else
				user.validBlock.axis.col--;

			tmpvalidBlock = getValidBlock(user, user.validBlock.axis);
		}

		drawCurrBlock(user);
	}

	int[][] getValidBlock(User user, Idx p) {
		int valid[][] = new int[4][4];
		int basis_row = p.row - 1;
		int basis_col = p.col - 2;

		// -2 -1 0 1
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				try {
					valid[i][j] = user.board[basis_row + i][basis_col + j];

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

	boolean canMove(User user, int direct) {
		Idx tempAxis = null; // 임시 축(만약 이동했을 때 축)

		if (direct == Direct.LEFT) {
			if (0 <= user.validBlock.axis.col - 1)
				tempAxis = new Idx(user.validBlock.axis.row, user.validBlock.axis.col - 1);
		} else if (direct == Direct.RIGHT) {
			if (user.validBlock.axis.col + 1 < 10)
				tempAxis = new Idx(user.validBlock.axis.row, user.validBlock.axis.col + 1);
		} else {
			tempAxis = new Idx(user.validBlock.axis.row + 1, user.validBlock.axis.col);
		}

		if (tempAxis == null)
			return false;

		int tmpvalidBlock[][] = getValidBlock(user, tempAxis); // 임시축에 대한 유효블록 가져오기
		
		if (bump(user, tmpvalidBlock))
			return false;

		return true;
	}

	void moveBlock(User user, int direct) {
		eraseCurrBlock(user);

		if (direct == Direct.LEFT) {
			user.validBlock.axis.col--;
		} else if (direct == Direct.RIGHT) {
			user.validBlock.axis.col++;
		} else { // down
			user.validBlock.axis.row = user.validBlock.axis.row + 1;
		}

		drawCurrBlock(user);
	}

	void drawCurrBlock(User user) {
		UserItem u;
		if (user == user1)
			u = p1;
		else
			u = p2;

		int basis_row;
		int basis_col;
		int tmpvalidBlock[][] = getValidBlock(user, user.validBlock.axis);

		while (bump(user, tmpvalidBlock)) { // 새로 생성 시 원래 블록과 겹치지 않도록 확인작업
			user.validBlock.axis.row--;
			tmpvalidBlock = getValidBlock(user, user.validBlock.axis);
		}

		basis_row = user.validBlock.axis.row - 1; // 회전축이 1행 2열이므로, 유효블록의 좌측상단 점(0행 0열)을 기준으로
		basis_col = user.validBlock.axis.col - 2;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				try {
					if (user.currBlock.get()[i][j] == 1) {
						user.board[basis_row + i][basis_col + j] = 1;
						u.gameBoard[basis_row + i][basis_col + j].setBackground(user.currBlock.color);
					}

				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}

		drawShadow(user);
	}

	void drawNextBlock(User user) {
		UserItem u;
		if (user == user1)
			u = p1;
		else
			u = p2;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				try {
					if (user.nextBlock_.get()[i][j] == 1) {
						u.nextBlock[i][j].setBackground(user.nextBlock_.color);
					} else {
						u.nextBlock[i][j].setBackground(Color.BLACK);
					}

				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
	}

	void drawHoldBlock(User user) {
		UserItem u;
		if (user == user1)
			u = p1;
		else
			u = p2;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				try {
					if (user.holdBlock_.get()[i][j] == 1) {
						u.holdBlock[i][j].setBackground(user.holdBlock_.color);
					} else {
						u.holdBlock[i][j].setBackground(Color.BLACK);
					}

				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
	}

	void eraseShadow(User user) {
		UserItem u;
		if (user == user1)
			u = p1;
		else
			u = p2;

		int basis_row = user.shadowAxis.row - 1; // 회전축이 1행 2열이므로, 유효블록의 좌측상단 점(0행 0열)을 기준으로
		int basis_col = user.shadowAxis.col - 2;

		// -2 -1 0 1
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				try {
					if (user.currBlock.get()[i][j] == 1) {
						u.gameBoard[basis_row + i][basis_col + j].setBackground(Color.BLACK);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
	}

	boolean canMoveSdw(User user) {
		Idx tempAxis = new Idx(user.shadowAxis.row + 1, user.shadowAxis.col);

		int tmpvalidBlock[][] = getValidBlock(user, tempAxis); // 임시축에 대한 유효블록 가져오기
		
		if (bump(user, tmpvalidBlock))
			return false;

		return true;
	}

	void drawShadow(User user) {
		UserItem u;
		if (user == user1)
			u = p1;
		else
			u = p2;

		user.shadowAxis.row = user.validBlock.axis.row;
		user.shadowAxis.col = user.validBlock.axis.col;

		// 임시 축
		while (canMoveSdw(user)) {
			user.shadowAxis.row++;
		}

		if (user.shadowAxis.row == user.validBlock.axis.row) { // 그림자와 블록이 같은 위치라면 그릴 필요 없음
			return;
		}

		int basis_row = user.shadowAxis.row - 1; // 회전축이 1행 2열이므로, 유효블록의 좌측상단 점(0행 0열)을 기준으로
		int basis_col = user.shadowAxis.col - 2;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				try {
					if (user.currBlock.get()[i][j] == 1) {
						if (user.board[basis_row + i][basis_col + j] == 0) {
							u.gameBoard[basis_row + i][basis_col + j].setBackground(new Color(61, 61, 61));
						}

					}

				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}

	}

	void eraseCurrBlock(User user) {
		UserItem u;
		if (user == user1)
			u = p1;
		else
			u = p2;

		int basis_row = user.validBlock.axis.row - 1; // 회전축이 1행 2열이므로, 유효블록의 좌측상단 점(0행 0열)을 기준으로
		int basis_col = user.validBlock.axis.col - 2;

		// -2 -1 0 1
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				try {
					if (user.currBlock.get()[i][j] == 1) {
						u.gameBoard[basis_row + i][basis_col + j].setBackground(Color.BLACK);
						user.board[basis_row + i][basis_col + j] = 0;

					}

				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}

		eraseShadow(user);
	}

	void blockDown(User user) {
		if (canMove(user, Direct.DOWN)) {
			moveBlock(user, Direct.DOWN);
		} else {
			if (user.goDrop) {
				drop(user);
			} else
				user.goDrop = true;
		}
	}

	void drop(User user) {
		boolean isFinish = false;

		while (canMove(user, Direct.DOWN)) {
			moveBlock(user, Direct.DOWN);
		}

		int basis_row = user.validBlock.axis.row - 1; // 회전축이 1행 2열이므로, 유효블록의 좌측상단 점(0행 0열)을 기준으로
		int basis_col = user.validBlock.axis.col - 2;

		boolean storeTop = true;

		// -2 -1 0 1
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (user.currBlock.get()[i][j] == 1) {
					if (storeTop) { // 높이저장
						if (user.totHeight < 20 - (basis_row + i))
							user.totHeight = 20 - (basis_row + i);
						storeTop = false;
					}
					try {
						user.board[basis_row + i][basis_col + j] = 3;
					} catch (ArrayIndexOutOfBoundsException e) {
						if (basis_row + i < 0) {
							isFinish = true; // 게임오버!
							continue;
						}
					}
				}
			}

		}

		if (isFinish) {
			gameOver(user);
		}

		checkEliminate(user);

		user.scoreUp(30); // 하나 쌓으면 30점
		if (user.checkLevelUp()) {
			levelUp(user);
		}
		updateScore(user);
		
		// 새로운 블록 만들기
		user.validBlock.axisInit();
		user.currBlock = user.nextBlock_;
		user.nextBlock_ = user.createBlock.getBlock();

		drawCurrBlock(user);
		drawNextBlock(user);
		user.goDrop = false;
		user.canHold = true;
	}
	
	void checkEliminate(User user) {
		eliminatedNum = 0;
		
		// 없어진 줄 있는지 확인
		for (int row = 20 - user.totHeight; row < 20; row++) {
			if (row < 0)
				break;

			int sum = 0;

			for (int col = 0; col < 10; col++) {
				sum += user.board[row][col];
			}

			if (sum >= 30) { // 라인 삭제
				eliminateLine(user, row);
				eliminatedNum++;
			}

		}
		if (eliminatedNum >= 1)
			user.scoreUp(eliminatedNum * 200 - 100);
	}

	void eliminateLine(User user, int rowNum) { // 해당 행 없애기
		UserItem u;
		if (user == user1)
			u = p1;
		else
			u = p2;

		if (user.totHeight > 0)
			user.totHeight--; // 전체높이 - 1

		for (int row = rowNum; row >= 20 - user.totHeight - 1; row--) {
			for (int col = 0; col < 10; col++) {

				user.board[row][col] = user.board[row - 1][col];
				u.gameBoard[row][col].setBackground(u.gameBoard[row - 1][col].getBackground());
			}
		}
	}

	void gameOver(User user) {
		job.gameOver();
		F.cardLayout.show(F.getContentPane(), "modeP");
	}

	void holdChange(User user) {
		if (user.canHold) { // hold이력이 있으면 동작하지 않음.
			eraseCurrBlock(user);

			user.validBlock.axisInit(); // 축을 위로
			if (user.holdBlock_ == null) { // 이미 홀드해놓은 블록이 없다면
				user.holdBlock_ = user.currBlock;

				// 새로운 블록 만들기
				user.currBlock = user.nextBlock_;
				user.nextBlock_ = user.createBlock.getBlock();

				drawCurrBlock(user);
				drawNextBlock(user);
				drawHoldBlock(user);

			} else {
				// 기존 블록과 바꾸기
				Tetromino temp = user.holdBlock_;
				user.holdBlock_ = user.currBlock;
				user.currBlock = temp;

				drawCurrBlock(user);
				drawHoldBlock(user);
			}

			user.goDrop = false;
			user.canHold = false;
		}
	}

	void startClock() {
		// 시간 체크
		job = new ScheduledJob();
		Timer timeScheduler = new Timer();
		timeScheduler.scheduleAtFixedRate(job, 1000, job.Interval); // 1초씩

		p1.mainBoard.setFocusable(true);
		p1.mainBoard.requestFocus();
	}

	void levelUp(User user) {
		job.stopClock();

		job = new ScheduledJob();
		job.Interval -= 50 * (user.level - 1);
		Timer timeScheduler = new Timer();
		timeScheduler.scheduleAtFixedRate(job, 1000, job.Interval);

		p1.mainBoard.setFocusable(true);
		p1.mainBoard.requestFocus();
	}

	void updateScore(User user) {
		UserItem u;
		if (user == user1)
			u = p1;
		else
			u = p2;

		String userScore = String.format("%010d", user.score);
		u.score.setText("score: " + userScore);
	}

	class ScheduledJob extends TimerTask {
		int Interval = 1000;

		public void gameOver() {
			this.cancel();
		}

		public void stopClock() {
			this.cancel();
		}

		public void run() {
			blockDown(user1);
			if (user2 != null)
				blockDown(user2);
		}
	}
}

class MyGridLayout extends GridLayout {
	public MyGridLayout(int rows, int cols) {
		super(rows, cols);
	}

	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();
			int ncomponents = parent.getComponentCount();
			int nrows = getRows();
			int ncols = getColumns();
			boolean ltr = parent.getComponentOrientation().isLeftToRight();

			if (ncomponents == 0) {
				return;
			}
			if (nrows > 0) {
				ncols = (ncomponents + nrows - 1) / nrows;
			} else {
				nrows = (ncomponents + ncols - 1) / ncols;
			}
			int totalGapsWidth = (ncols - 1) * getHgap();
			int widthWOInsets = parent.getWidth() - (insets.left + insets.right);
			int widthOnComponent = (widthWOInsets - totalGapsWidth) / ncols;
			int extraWidthAvailable = (widthWOInsets - (widthOnComponent * ncols + totalGapsWidth)) / 2;

			int totalGapsHeight = (nrows - 1) * getVgap();
			int heightWOInsets = parent.getHeight() - (insets.top + insets.bottom);
			int heightOnComponent = (heightWOInsets - totalGapsHeight) / nrows;
			int extraHeightAvailable = (heightWOInsets - (heightOnComponent * nrows + totalGapsHeight)) / 2;

			int size = Math.min(widthOnComponent, heightOnComponent);
			widthOnComponent = size;
			heightOnComponent = size;
			if (ltr) {
				for (int c = 0,
						x = insets.left + extraWidthAvailable; c < ncols; c++, x += widthOnComponent + getHgap()) {
					for (int r = 0,
							y = insets.top + extraHeightAvailable; r < nrows; r++, y += heightOnComponent + getVgap()) {
						int i = r * ncols + c;
						if (i < ncomponents) {
							parent.getComponent(i).setBounds(x, y, widthOnComponent, heightOnComponent);
						}
					}
				}
			} else {
				for (int c = 0, x = (parent.getWidth() - insets.right - widthOnComponent)
						- extraWidthAvailable; c < ncols; c++, x -= widthOnComponent + getHgap()) {
					for (int r = 0,
							y = insets.top + extraHeightAvailable; r < nrows; r++, y += heightOnComponent + getVgap()) {
						int i = r * ncols + c;
						if (i < ncomponents) {
							parent.getComponent(i).setBounds(x, y, widthOnComponent, heightOnComponent);
						}
					}
				}
			}
		}
	}
}