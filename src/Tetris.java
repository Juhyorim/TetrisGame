final class Direct {
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int DOWN = 3; // space
}

class Idx {
	int row;
	int col;

	public Idx(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
}

class ValidBlock {
	Idx axis = new Idx(0, 5);

	void axisInit() {
		axis.row = 0;
		axis.col = 5;
	}
}

