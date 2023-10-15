import java.awt.Color;

abstract class Tetromino implements Cloneable {
	int curState = 0;
	int num = 1;
	int state[][][];
	Color color;
	
	public void rotate() {
		curState = (curState+1)%num;
	}
	
	public int[][] get() {
		return state[curState];
	}
	
	Tetromino copy() throws CloneNotSupportedException { 
		Tetromino copyObj = (Tetromino)this.clone(); 
		int[][][]copyState = (int[][][])state.clone(); 
		copyObj.state = copyState; 
		
		return copyObj; 
	}
}


class T_O extends Tetromino {
	T_O() {
		int temp[][][] = {
			{
				{0,0,0,0},
				{0,1,1,0},
				{0,1,1,0},
				{0,0,0,0}
			}
		};
		
		state = temp;
		num = state.length;
		color = Color.ORANGE;
	}
}

class T_I extends Tetromino {
	T_I() {
		int temp[][][] = {
			{
				{0,0,0,0},
				{1,1,1,1},
				{0,0,0,0},
				{0,0,0,0}
			},
			{
				{0,0,1,0},
				{0,0,1,0},
				{0,0,1,0},
				{0,0,1,0}
			}
		};
		
		state = temp;
		num = state.length;
		color = Color.CYAN;
	}
}

class T_S extends Tetromino {
	T_S() {
		int temp[][][] = {
			{
				{0,0,0,0},
				{0,0,1,1},
				{0,1,1,0},
				{0,0,0,0}
			},
			{
				{0,0,1,0},
				{0,0,1,1},
				{0,0,0,1},
				{0,0,0,0}
			}
		};
		
		state = temp;
		num = state.length;
		color = Color.GREEN;
	}
}

class T_Z extends Tetromino {

	T_Z() {
		int temp[][][] = {
				{
					{0,0,0,0},
					{0,1,1,0},
					{0,0,1,1},
					{0,0,0,0}
				},
				{
					{0,0,0,1},
					{0,0,1,1},
					{0,0,1,0},
					{0,0,0,0}
				}
			};
		
		state = temp;
		num = state.length;
		color = Color.MAGENTA;
	}
}

class T_L extends Tetromino {
	
	T_L() {
		int temp[][][] = {
				{
					{0,0,0,0},
					{0,1,1,1},
					{0,1,0,0},
					{0,0,0,0}
				},
				{
					{0,0,1,0},
					{0,0,1,0},
					{0,0,1,1},
					{0,0,0,0}
				},
				{
					{0,0,0,1},
					{0,1,1,1},
					{0,0,0,0},
					{0,0,0,0}
				},
				{
					{0,1,1,0},
					{0,0,1,0},
					{0,0,1,0},
					{0,0,0,0}
				}
			};
		
		state = temp;
		num = state.length;
		color = Color.yellow;
	}
}

class T_J extends Tetromino {
	T_J() {
		int temp[][][] = {
				{
					{0,0,0,0},
					{0,1,1,1},
					{0,0,0,1},
					{0,0,0,0}
				},
				{
					{0,0,1,1},
					{0,0,1,0},
					{0,0,1,0},
					{0,0,0,0}
				},
				{
					{0,1,0,0},
					{0,1,1,1},
					{0,0,0,0},
					{0,0,0,0}
				},
				{
					{0,0,1,0},
					{0,0,1,0},
					{0,1,1,0},
					{0,0,0,0}
				}
			};
		
		state = temp;
		num = state.length;
		color = Color.BLUE;
	}
}

class T_T extends Tetromino {
	T_T() {
		int temp[][][] = {
				{
					{0,0,0,0},
					{0,1,1,1},
					{0,0,1,0},
					{0,0,0,0}
				},
				{
					{0,0,1,0},
					{0,0,1,1},
					{0,0,1,0},
					{0,0,0,0}
				},
				{
					{0,0,1,0},
					{0,1,1,1},
					{0,0,0,0},
					{0,0,0,0}
				},
				{
					{0,0,1,0},
					{0,1,1,0},
					{0,0,1,0},
					{0,0,0,0}
				}
			};
		
		state = temp;
		num = state.length;
		color = new Color(97, 28, 161);
	}
}

