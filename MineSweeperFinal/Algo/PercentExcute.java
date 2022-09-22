package MineSweeperFinal.Algo;

import MineSweeperFinal.Model.*;

public class PercentExcute {

	static double[][] percent;
	static int[] num = new int[2];

	// percent
	public static void makeValue() {
		percent = new double[Board.NUM_ROWS][Board.NUM_COLUMNS];
		for (int i = 0; i < percent.length; i++) {
			for (int j = 0; j < percent[0].length; j++) {
				percent[i][j] = Double.MIN_VALUE;
			}
		}
	}

	//
	public static void makePercent(Board initialState) {
		Square[][] square = initialState.getListSquare();
		for (int x = 0; x < square.length; x++) {
			for (int y = 0; y < square[0].length; y++) {
				if (square[x][y].isOpen() && square[x][y].getNumMineAround() != 0) {
//					percent =makePercent();

					double result;
					double notOpened = (double) (getNumSquareNotOpened(x, y, initialState)
							- initialState.getNumAroundTarget(x, y));
					double numMinAround = square[x][y].getNumMineAround() - initialState.getNumAroundTarget(x, y);

					result = numMinAround / notOpened * 100;

					if (numMinAround == 0.0) {
						result = 0.0;
//						System.out.println(result +"o vi tri ["+ x +"," +y +"]");
					}
					//
					for (int i = x - 1; i <= x + 1; i++) {
						for (int j = y - 1; j <= y + 1; j++) {
							if (i < 0 || i >= Board.NUM_ROWS || j < 0 || j >= Board.NUM_COLUMNS) {
								continue;
							}

							if (!square[i][j].isOpen() && !square[i][j].isTarget())
								if (result == 0.0) {// neu result bang 0 thì các ô chưa mở xung quanh ô đó = 0.0
									percent[i][j] = 0.0;
//									System.out.println(result + " o vi tri [" + i + "," + j + "]");
								} else if (percent[i][j] < result && percent[i][j] != 0) {
									percent[i][j] = result;
//									System.out.println(result + " o vi tri [" + i + "," + j + "]");
								}
						}
					}
					//
				}
			}
		}
	}

	// tìm số ô chưa mở xung quanh ô chỉ định
	private static int getNumSquareNotOpened(int x, int y, Board initialState) {
		return initialState.getNumSquareAround(x, y) - initialState.getNumAroundOpened(x, y);
	}

	// kiem tra xem ti le phan tram no la 100 hay mot hay mot so khac
	public static int choose(double[][] percent) {

		for (int i = 0; i < percent.length; i++) {
			for (int j = 0; j < percent[0].length; j++) {
				if (percent[i][j] == 100.0) {
					num[0] = i;
					num[1] = j;
					return 100;
				} else if (percent[i][j] == 0.0) {
					num[0] = i;
					num[1] = j;
					return 0;
				}
			}
		}
		return -1;
	}

	// play ben Board
	public static boolean play(int x, int y, Square[][] square) {
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
					if (x + m > Board.NUM_ROWS - 1) {
						break;
					}
					for (int n = -1; n <= 1; n++) {
						if (y + n < 0) {
							n++;
						}
						if (y + n > Board.NUM_COLUMNS - 1) {
							break;
						}
						play(x + m, y + n, square);
					}
				}
			}
		}
		return true;
	}

	// smallest value in double[][] except Min_value
	public static double smallestValue(double[][] p) {
		double result = Double.MAX_VALUE;

		for (int i = 0; i < p.length; i++) {
			for (int j = 0; j < p[0].length; j++) {
				if (p[i][j] != Double.MIN_VALUE && p[i][j] < result)
					result = p[i][j];
			}
		}

		for (int i = 0; i < p.length; i++) {
			for (int j = 0; j < p[0].length; j++) {
				if (p[i][j] == result) {
					num[0] = i;
					num[1] = j;
				}
			}
		}
		// neu no khac so max value thi moi hien ra so nho nhat
		if (result != Double.MAX_VALUE) {
			System.out.println("so phan tram nho nhat: " + result +"%");
		}
		return result;
	}

	public static Board excute(Board initialState) {
		Square[][] square = initialState.getListSquare();
		makeValue();
		makePercent(initialState);
		//
		int choose = choose(percent);
		// neu bang 100 tuc co bom => dat co
		if (choose == 100) {
			square[num[0]][num[1]].setTarget(true);
			System.out.println("bom o :[" + num[0] + "," + num[1] + "]");

		} else if (choose == 0) { // neu bang 0 tuc la an toan => mo ra
			play(num[0], num[1], square);
			System.out.println("an toan o :[" + num[0] + "," + num[1] + "]");

		} else if (choose == -1) { // neu bang -1 tuc la so khong xac dinh duoc => chon ti le phan tram nho nhat de
									// chon
			double smallV = smallestValue(percent);
			if (smallV == Double.MAX_VALUE) {// cái này để lúc đầu chưa có ô nào thì nó sẽ return null
				return null;
			} else {
				play(num[0], num[1], square);
				System.out.println("phan tram nho nhat o :[" + num[0] + "," + num[1] + "] => chon");
			}
		}
		return initialState;
	}

}
