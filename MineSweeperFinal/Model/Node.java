package MineSweeperFinal.Model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Node {
	private int x, y;
	private ClauseValue hasbom;
	
	public Node() {
		// TODO Auto-generated constructor stub
	}
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getValue() {
		int value = 0;
		if (hasbom != null) {
			value += 100;
		}
		return value;
	}

	public ClauseValue getHasbom() {
		return hasbom;
	}

	public void setHasbom(boolean hasbom) {
		if (this.hasbom == null) {
			this.hasbom = new ClauseValue(false);
		}
		this.hasbom.setValue(hasbom);
	}

	public void setLocate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static Comparator<Node> ValueComparator = new Comparator<Node>() {
		@Override
		public int compare(Node o1, Node o2) {
			return Integer.compare(o2.getValue(), o1.getValue());
		}
	};

	
}
