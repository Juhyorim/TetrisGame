import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class GamePage_2P extends GamePage {
	JPanel info = new JPanel();
	JPanel totPanel = new JPanel();
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();

	JLabel margin1 = new JLabel("         ");
	JLabel margin2 = new JLabel("         ");

	JPanel setButtons = new JPanel();
	JButton newGame = new JButton("new game");
	JButton exit = new JButton("exit");
	JLabel title = new JLabel("테트리스 게임");

	int flag;

	void setFlag(int flag) {
		this.flag = flag;
	}

	void makeGamePane(User user, JPanel panel) {
		UserItem p;
		if (user == user1)
			p = p1;
		else
			p = p2;

		panel.setLayout(new BorderLayout());

		panel.add(p.center, BorderLayout.CENTER);
		panel.add(p.east, BorderLayout.EAST);
		panel.add(p.west, BorderLayout.WEST);
	}

	public GamePage_2P(MyFrame f) {
		F = f;

		user1 = new User();
		user2 = new User();
		p1 = new UserItem();
		p2 = new UserItem();

		setLayout(new BorderLayout());

		add(info, BorderLayout.SOUTH);
		add(title, BorderLayout.NORTH);
		add(totPanel, BorderLayout.CENTER);

		totPanel.setLayout(new GridLayout(1, 2, 170, 0));
		totPanel.setBackground(MyColor.DARKGRAY);
		totPanel.add(panel1);
		totPanel.add(panel2);

		makeGamePane(user1, panel1);
		makeGamePane(user2, panel2);

		Font font = new Font("Aharoni 굵게", Font.BOLD, 15);

		title.setBackground(MyColor.DARKGRAY);
		title.setFont(font);
		title.setForeground(Color.white);
		title.setHorizontalAlignment(JLabel.CENTER);

		info.setLayout(new GridLayout(3, 1));
		info.add(setButtons);
		info.add(tmpLabel);
		info.setBackground(MyColor.DARKGRAY);

		setButtons.setLayout(new FlowLayout());
		setButtons.add(newGame, BorderLayout.NORTH);
		setButtons.add(exit, BorderLayout.NORTH);
		setButtons.setBackground(MyColor.DARKGRAY);
		
		exit.setBackground(MyColor.DEEPBLUE);
		exit.setForeground(Color.BLACK);
		exit.setBorderPainted(false);

		newGame.setBackground(MyColor.DEEPBLUE);
		newGame.setForeground(Color.BLACK);
		newGame.setBorderPainted(false);

		title.setBackground(MyColor.DARKGRAY);
		title.setOpaque(true);
		tmpLabel.setForeground(Color.white);

		p1.center.add(p1.score, BorderLayout.SOUTH);
		p2.center.add(p2.score, BorderLayout.SOUTH);

		setVisible(true);

		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String btnInfo = e.getActionCommand();
				if (btnInfo.equals("new game")) {

					job.gameOver();
					F.cardLayout.show(F.getContentPane(), "modeP");
				}
			}
		});

		makeKey();

		initGame();
	}
	
	void makeKey() {
		p1.mainBoard.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT: // 왼쪽 방향키
					if (canMove(user1, Direct.LEFT)) { // 움직일 수 있는지 확인
						moveBlock(user1, Direct.LEFT); // move
					}
					break;
				case KeyEvent.VK_RIGHT:
					if (canMove(user1, Direct.RIGHT)) {
						moveBlock(user1, Direct.RIGHT);
					}
					break;
				case KeyEvent.VK_QUOTE: // .키: 회전기능
					rotateBlock(user1);
					break;
				case KeyEvent.VK_SEMICOLON:
					holdChange(user1);
					break;
				case KeyEvent.VK_ENTER:
					drop(user1);
					break;
				case KeyEvent.VK_A:
					if (canMove(user2, Direct.LEFT)) { // 움직일 수 있는지 확인
						moveBlock(user2, Direct.LEFT); // move
					}
					break;
				case KeyEvent.VK_D:
					if (canMove(user2, Direct.RIGHT)) {
						moveBlock(user2, Direct.RIGHT);
					}
					break;
				case KeyEvent.VK_V: // v키: 회전기능
					rotateBlock(user2);
					break;
				case KeyEvent.VK_G:
					holdChange(user2);
					break;
				case KeyEvent.VK_SPACE:
					drop(user2);
					break;
				}
			}
		});
	}

	void gameOver(User user) {
		String winner;
		if (user == user1)
			winner = "User2";
		else
			winner = "User1";

		JOptionPane.showMessageDialog(null, winner + " Win!★★", "게임오버", JOptionPane.INFORMATION_MESSAGE);

		super.gameOver(user);
	}

	void drop(User user) { // 오버로딩, 2인게임일 때는 대전모드인지 체크 후 attack함수를 호출한다.
		super.drop(user);

		if (flag == Flags.MATCH) {
			int attackLine = (eliminatedNum - 1) * 2;
			if (attackLine > 0) {
				attack(user, attackLine);
			}

		}
	}

	void attack(User user, int line) { // line수만큼 어택
		User victim;
		if (user == user1)
			victim = user2;
		else
			victim = user1;
		
		victim.totHeight += line;

		for (int i = 0; i < line;) {
			int rand = (int) (Math.random() * 3) + 1;
			int trash[] = makeTrashLine();

			for (int j = 0; j < rand; j++) {
				if (i >= line)
					break;
				stackTrash(victim, trash);
				i++;
			}
		}
	}

	void stackTrash(User user, int trash[]) {
		UserItem u;
		if (user == user1)
			u = p1;
		else
			u = p2;

		eraseCurrBlock(user);

		for (int row = 20 - user.totHeight; row < 20; row++) {
			if (user.totHeight == 0)
				break;

			for (int col = 0; col < 10; col++) {
				user.board[row - 1][col] = user.board[row][col];
				u.gameBoard[row - 1][col].setBackground(u.gameBoard[row][col].getBackground());
			}
		}

		for (int col = 0; col < 10; col++) {
			user.board[19][col] = trash[col];
			if (user.board[19][col] == 3)
				u.gameBoard[19][col].setBackground(Color.GRAY);
			else
				u.gameBoard[19][col].setBackground(Color.black);
		}

		if (user.totHeight < 20)
			user.totHeight++; // 전체높이 + 1

		// 원위치 초기화
		user.validBlock.axisInit();

		drawCurrBlock(user);
		user.goDrop = false;
	}

	int[] makeTrashLine() {
		int line[] = new int[10];
		for (int i = 0; i < 10; i++) {
			line[i] = 3;
		}

		int rand = (int) (Math.random() * 7);
		line[rand] = 0;

		return line;
	}
}