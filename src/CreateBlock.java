public class CreateBlock {
	Tetromino set[] = new Tetromino[7];
	int idx = 7;
	
	public void newSet() {
		set[0] = new T_O();
		set[1] = new T_I();
		set[2] = new T_S();
		set[3] = new T_Z();
		set[4] = new T_L();
		set[5] = new T_J();
		set[6] = new T_T();
		
		int rand = 0;
		for (int i = 0; i<7; i++) {
			rand = (int)(Math.random() * 7);
			Tetromino temp = set[i];
			set[i] = set[rand];
			set[rand] = temp;
		}
	}
	
	public Tetromino getBlock() {
		Tetromino currBlock;
		
		if (idx == 7) {
			newSet();
			idx = 0;
		}
		
		currBlock = set[idx];
		idx++;
		
		return currBlock;
	}
}
