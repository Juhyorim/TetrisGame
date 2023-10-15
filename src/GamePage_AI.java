import java.awt.event.*;


class GamePage_AI extends GamePage_2P {
	
	AI computer;
	AIThread aiThread;

	void goBest() {
			while (computer.user.currBlock.curState != computer.b_curRot) {
				rotateBlock(user2);
				try {
					Thread.sleep(computer.moveTime);
				} catch (InterruptedException e) {
				}
			}
			while (computer.user.validBlock.axis.col != computer.b_curLoc) {
				if (computer.user.validBlock.axis.col < computer.b_curLoc)
					moveBlock(user2, Direct.RIGHT);
				else
					moveBlock(user2, Direct.LEFT);
				try {
					Thread.sleep(computer.moveTime);
				} catch (InterruptedException e) {
				}
			}
			drop(user2);
			try {
				Thread.sleep(computer.moveTime);
			} catch (InterruptedException e) {
			}
		}

	class AIThread extends Thread {
		AI ai;

		AIThread(AI ai) {
			this.ai = ai;
		}

		@Override
		public void run() {
			while (true) {
				ai.init();
				ai.check();

				goBest();
			}
		}
	}

	public GamePage_AI(MyFrame f) {
		super(f);

		computer = new AI(user2, p1);
	}
	
	void makeKey() {	// �����ε�
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
	}

	void gameOver(User user) {
		super.gameOver(user);

		aiThread.interrupt();
	}
	
	void attack(User user, int line) { // line����ŭ ����
		super.attack(user, line);
		
		User victim;
		if (user == user1)
			victim = user2;
		else
			victim = user1;

		if (victim == user2) {	//ai�� ���ݴ��ߴ�
			aiThread.interrupt();
			aiThread = new AIThread(computer);
			aiThread.start();
		}
	}

	void startAI() {
		aiThread = new AIThread(computer);
		aiThread.start();
	}

}