import java.awt.*;

import javax.swing.*;
import javax.swing.border.LineBorder;


class User {
	Tetromino nextBlock_ = null;
	Tetromino holdBlock_ = null;
	Tetromino currBlock;
	Idx shadowAxis = new Idx(0, 5);
	
	ValidBlock validBlock = new ValidBlock();
	CreateBlock createBlock = new CreateBlock();
	
	int levelUpScore = 0;
	int nextLevelScore = 2000;
	int level = 1;
	
	int score = 0;
	
	int board[][] = new int[20][10];
	boolean goDrop = false;
	int totHeight = 0;
	boolean canHold = true;
	
	public User () {
		currBlock = createBlock.getBlock();
		nextBlock_ = createBlock.getBlock();
	}
	
	public void scoreUp(int n) {
		score += n;
		levelUpScore += n;
	}
	
	public boolean checkLevelUp() {
		if (levelUpScore >= nextLevelScore) {
			level++;
			nextLevelScore = level*2000;
			levelUpScore = 0;
			return true;
		}
		return false;
	}
	
	public void init() {
		validBlock.axisInit();
		goDrop = false;
		canHold = true;
		holdBlock_ = null;
	}
	
}

class UserItem {
	JPanel center = new JPanel();
	JPanel east = new JPanel();
	JPanel west = new JPanel();

	JLabel nextLabel = new JLabel("Next");
	JLabel holdLabel = new JLabel("Hold");

	JPanel mainBoard = new JPanel();
	JPanel holdField = new JPanel();
	JPanel nextField = new JPanel();

	JButton gameBoard[][] = new JButton[20][10];
	JButton nextBlock[][] = new JButton[4][4];
	JButton holdBlock[][] = new JButton[4][4];

	JLabel score = new JLabel("score: 0000000000");

	UserItem() {
		mainBoard.setLayout(new MyGridLayout(20, 10));
		mainBoard.setBorder(new LineBorder(MyColor.DEEPBLUE));
		mainBoard.setBackground(MyColor.DEEPBLUE);

		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 10; j++) {
				gameBoard[i][j] = new JButton();
				gameBoard[i][j].setEnabled(false);
				mainBoard.add(gameBoard[i][j]);
			}
		}

		holdField.setBackground(MyColor.DARKGRAY);
		nextField.setBackground(MyColor.DARKGRAY);
		holdField.setLayout(new MyGridLayout(4, 4));
		nextField.setLayout(new MyGridLayout(4, 4));
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				holdBlock[i][j] = new JButton();
				holdBlock[i][j].setEnabled(false);
				holdBlock[i][j].setPreferredSize(new Dimension(25, 25));
				holdField.add(holdBlock[i][j]);

				nextBlock[i][j] = new JButton();
				nextBlock[i][j].setEnabled(false);
				nextBlock[i][j].setPreferredSize(new Dimension(25, 25));
				nextField.add(nextBlock[i][j]);
			}
		}

		Font font = new Font("Aharoni ±½°Ô", Font.BOLD, 15);

		nextLabel.setFont(font);
		nextLabel.setForeground(Color.white);
		nextLabel.setBackground(MyColor.DARKGRAY);
		holdLabel.setFont(font);
		holdLabel.setForeground(Color.white);
		holdLabel.setBackground(MyColor.DARKGRAY);

		score.setFont(font);
		score.setForeground(Color.white);
		score.setBackground(MyColor.DARKGRAY);

		east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
		west.setLayout(new BoxLayout(west, BoxLayout.Y_AXIS));
		east.setBackground(MyColor.DARKGRAY);
		west.setBackground(MyColor.DARKGRAY);

		east.add(nextLabel);
		east.add(nextField);
		west.add(holdLabel);
		west.add(holdField);

		center.setLayout(new BorderLayout());
		center.add(mainBoard);
		center.setBackground(MyColor.DARKGRAY);
	}

}