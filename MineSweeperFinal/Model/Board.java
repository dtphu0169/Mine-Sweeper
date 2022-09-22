package MineSweeperFinal.Model;

import java.util.Random;

public class Board {
	public static final int NUM_ROWS = 20;
	public static final int NUM_COLUMNS = 30;
	public static final int NUM_MINES = 90;

	private Square[][] square;

	int[] num = new int[2];
	double[][] percent;

	public Board(Square[][] square) {
		this.square = square;
	}

	public Board() {
		square = new Square[NUM_ROWS][NUM_COLUMNS];
		for (int i = 0; i < square.length; i++) {
			for (int j = 0; j < square[0].length; j++) {
				square[i][j] = new Square();
			}
		}
	}

	boolean inRange(int x,int y,int x1,int x2) {
		if ((x == x1-1 || x == x1 || x == x1+1) && (y == x2-1 || y == x2 || y == x2+1)) {
			return true;
		}
		return false;
	}
	
	public boolean isNull() {
		for (int i = 0; i < num.length; i++) {
			for (int j = 0; j < square[0].length; j++) {
				if (square[i][j].isHasMine()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public Board(int x1, int x2) {
		square = new Square[NUM_ROWS][NUM_COLUMNS];
		for (int i = 0; i < square.length; i++) {
			for (int j = 0; j < square[0].length; j++) {
				square[i][j] = new Square();
			}
		}

		// dat min vao cac o ngau nhien
		for (int i = 0; i < NUM_MINES; i++) {
			int x = genRan(NUM_ROWS);
			int y = genRan(NUM_COLUMNS);
			// neu co min roi thi dat ngau nhien vao o khac
			while (square[x][y].isHasMine() || inRange(x, y, x1, x2)) {
				x = genRan(NUM_ROWS);
				y = genRan(NUM_COLUMNS);
			}
			square[x][y].setHasMine(true);
		}

		// ghi so luong min xung quanh vao o
		for (int i = 0; i < square.length; i++) {
			for (int j = 0; j < square[0].length; j++) {
				int count = 0;
				for (int m = -1; m <= 1; m++) {
					if (i + m < 0) {// neu vuot qua cai khung tro choi về bên trái thi cong them m
						m++;
					}
					if (i + m > NUM_ROWS - 1) {// nếu vượt qua cái khung trò chơi về bên phải (i+m lớn hơn số cột thì
												// break)
						break;
					}
					for (int n = -1; n <= 1; n++) {
						if (j + n < 0) {
							n++;
						}
						if (j + n > NUM_COLUMNS - 1) {
							break;
						}
						if (!(m == 0 && n == 0) && square[i + m][j + n].isHasMine()) {
							count++;
						}
					}
				}
				square[i][j].setNumMineAround(count);
			}
		}
	}

	// lay so random trong khoang tu 0 den range
	public int genRan(int range) {
		Random rd = new Random();
		return rd.nextInt(range);
	}

	public Square[][] getListSquare() {
		return square;
	}

	// bam vao man hinh de choi
	public boolean play(int x, int y) {
		if (!square[x][y].isOpen()) {
			square[x][y].setOpen(true);
			if (square[x][y].isHasMine()) {
				return false;
			}
			if (square[x][y].getNumMineAround() == 0) {
				for (int m = -1; m <= 1; m++) {
					if (x + m < 0) {
						m++;
					}
					if (x + m > NUM_ROWS - 1) {
						break;
					}
					for (int n = -1; n <= 1; n++) {
						if (y + n < 0) {
							n++;
						}
						if (y + n > NUM_COLUMNS - 1) {
							break;
						}
						play(x + m, y + n);
					}
				}
			}
		}
		return true;
	}

	public boolean checkwin() {
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLUMNS; j++) {
				if (square[i][j].isHasMine() && square[i][j].isOpen()) {
					return false;
				}
			}
		}
		return true;
	}

	public void target(int x, int y) {
		if (!square[x][y].isOpen()) {
			if (!square[x][y].isTarget()) {
				square[x][y].setTarget(true);
			} else {
				square[x][y].setTarget(false);
			}
		}
	}

	// so o duoc mo xung quanh(ô mở trong 8 ô xung quanh)
	public int getNumAroundOpened(int x, int y) {
		int count = 0;
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i < 0 || i >= NUM_ROWS || j < 0 || j >= NUM_COLUMNS) {
					continue;
				}
				if (square[i][j].isOpen())
					count++;
			}
		}
		if (square[x][y].isOpen())
			count--;// tru di chinh no(cai chinh giua)
		return count;
	}

	// so target xung quanh
	public int getNumAroundTarget(int x, int y) {
		int count = 0;
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i < 0 || i >= NUM_ROWS || j < 0 || j >= NUM_COLUMNS) {
					continue;
				}
				if (square[i][j].isTarget())
					count++;
			}
		}
		if (square[x][y].isTarget())
			count--;// tru di chinh no(cai chinh giua)
		return count;
	}

	public int getNumSquareAround(int x, int y) {
		int count = -1;// cach khac cua viec tru di chinh no
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i < 0 || i >= NUM_ROWS || j < 0 || j >= NUM_COLUMNS) {
					continue;
				}
				count++;
			}
		}
		return count;
	}

	// mo het tat ca cac o
	public void showAllSquares() {
		for (int i = 0; i < square.length; i++) {
			for (int j = 0; j < square[0].length; j++) {
				square[i][j].setOpen(true);
			}
		}
	}

	public int getNumSquareOpened() {// số ô đã mở ~~ điểm số
		int result = 0;
		for (int i = 0; i < square.length; i++) {
			for (int j = 0; j < square[0].length; j++) {
				if (square[i][j].isOpen())
					result++;
			}
		}
		return result;
	}

	public int getNumSquareTarget() {// số cờ đã gắn ~~ số bom đã xác định được
		int result = 0;
		for (int i = 0; i < square.length; i++) {
			for (int j = 0; j < square[0].length; j++) {
				if (square[i][j].isTarget()) {
					result++;
				}
			}
		}
		return result;
	}

	public boolean isTarget(int x, int y) {
		return square[x][y].isTarget();
	}
}
