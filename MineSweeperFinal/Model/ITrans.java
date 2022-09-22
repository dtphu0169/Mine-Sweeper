package MineSweeperFinal.Model;


public interface ITrans {
	Square[][] getListSquare();
	
	boolean runFind();

	void play(int x, int y);

	void target(int x, int y);

	void restart();
}
