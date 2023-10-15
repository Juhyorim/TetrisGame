import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class GamePage_1P extends GamePage {
	private JPanel info = new JPanel();

	private JLabel margin1 = new JLabel(
			"                                                                                                            ");
	private JLabel margin2 = new JLabel(
			"                                                                                                            ");
	private JPanel totalPanel = new JPanel();
	private JPanel setButtons = new JPanel();
	private JButton newGame = new JButton("new game");
	private JButton exit = new JButton("exit");
	JLabel levelLabel = new JLabel("Level: 1");
	JLabel title = new JLabel("��Ʈ���� ����");

	public GamePage_1P(MyFrame f) {
		user1 = new User();
		p1 = new UserItem();
		F = f;
		setLayout(new BorderLayout());
		setBackground(MyColor.DARKGRAY);

		p1.mainBoard.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT: // ���� ����Ű
					if (canMove(user1, Direct.LEFT)) { // ������ �� �ִ��� Ȯ��
						moveBlock(user1, Direct.LEFT); // move
					}
					break;
				case KeyEvent.VK_RIGHT:
					if (canMove(user1, Direct.RIGHT)) {
						moveBlock(user1, Direct.RIGHT);
					}
					break;
				case KeyEvent.VK_Z: // zŰ: ȸ�����
					rotateBlock(user1);
					break;
				case KeyEvent.VK_C:
					holdChange(user1);
					break;
				case KeyEvent.VK_SPACE:
					drop(user1);
					break;
				}
			}
		});

		Font font = new Font("Aharoni ����", Font.BOLD, 15);

		title.setBackground(MyColor.DARKGRAY);
		title.setFont(font);
		title.setForeground(Color.white);
		title.setHorizontalAlignment(JLabel.CENTER);

		p1.score.setFont(font);
		p1.score.setForeground(Color.white);
		p1.score.setBackground(MyColor.DARKGRAY);

		levelLabel.setFont(font);
		levelLabel.setForeground(Color.white);
		levelLabel.setBackground(MyColor.DARKGRAY);

		info.setLayout(new GridLayout(4, 1));
		info.add(setButtons);
		info.add(p1.score);
		info.add(levelLabel);
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

		add(totalPanel, BorderLayout.CENTER);
		add(title, BorderLayout.NORTH);
		add(margin1, BorderLayout.WEST);
		add(margin2, BorderLayout.EAST);

		margin1.setBackground(MyColor.DARKGRAY);
		margin2.setBackground(MyColor.DARKGRAY);

		totalPanel.setLayout(new BorderLayout());
		totalPanel.add(p1.center);
		totalPanel.add(p1.east, BorderLayout.EAST);
		totalPanel.add(p1.west, BorderLayout.WEST);

		title.setBackground(MyColor.DARKGRAY);
		title.setOpaque(true);
		tmpLabel.setForeground(Color.white);
		p1.score.setForeground(Color.white);

		p1.center.add(info, BorderLayout.SOUTH);

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
		
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String btnInfo = e.getActionCommand();
				if (btnInfo.equals("exit")) {

					job.gameOver();
					System.exit(0);
				}
			}
		});

		initGame();
	}

	void updateScore(User user) {
		super.updateScore(user);

		levelLabel.setText("Level: " + user.level);
	}

	void levelUp(User user) {
		super.levelUp(user);

		JOptionPane.showMessageDialog(null, "������!", "�����մϴ�", JOptionPane.WARNING_MESSAGE);
	}

	void gameOver(User user) {
		JOptionPane.showMessageDialog(null, "����: " + Integer.toString(user.score) + "���ڡ�", "���ӿ���",
				JOptionPane.INFORMATION_MESSAGE);
		super.gameOver(user);
	}

}