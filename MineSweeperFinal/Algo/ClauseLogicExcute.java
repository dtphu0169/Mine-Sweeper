package MineSweeperFinal.Algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import MineSweeperFinal.Model.*;

public class ClauseLogicExcute {
	private static HashSet<List<Node>> data;

	public static Board excute(Board initialState) {
		data = new HashSet<List<Node>>();
		List<Node> list = BoardAnal(initialState);
		Collections.sort(list, Node.ValueComparator);
		System.out.println("so o xac dinh : " + list.size());
		for (Node node : list) {
			try {
				if (!node.getHasbom().isValue()) {
					initialState.play(node.getX(), node.getY());
				} else {
					initialState.target(node.getX(), node.getY());
				}
				return initialState;
			} catch (NullPointerException e) {
				continue;
			}
		}

		Node analNode = NodeAnal(initialState);
		if (analNode == null)
			return null;
		if (!analNode.getHasbom().isValue()) {
			initialState.play(analNode.getX(), analNode.getY());
		} else {
			initialState.target(analNode.getX(), analNode.getY());
		}
		return initialState;
	}

	private static List<Node> BoardAnal(Board initialState) {
		List<Node> result = new ArrayList<Node>();

		Square[][] board = initialState.getListSquare();
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			for (int j = 0; j < Board.NUM_COLUMNS; j++) {
				if (board[i][j].isOpen()) {
					int opened = initialState.getNumAroundOpened(i, j);
					int targeted = initialState.getNumAroundTarget(i, j);
					int nummine = board[i][j].getNumMineAround();
					int numsquarearound = initialState.getNumSquareAround(i, j);

					if (targeted == nummine) {
						for (int k = i - 1; k < i + 2; k++) {
							for (int k2 = j - 1; k2 < j + 2; k2++) {
								if (k < 0 || k >= Board.NUM_ROWS || k2 < 0 || k2 >= Board.NUM_COLUMNS) {
									continue;
								}
								if (!board[k][k2].isOpen() && !board[k][k2].isTarget()) {
									Node node = new Node(k, k2);
									node.setHasbom(false);
									result.add(node);
								}
							}
						}
					}

					if ((opened) == (numsquarearound - nummine)) {
						for (int k = i - 1; k < i + 2; k++) {
							for (int k2 = j - 1; k2 < j + 2; k2++) {
								if (k < 0 || k >= Board.NUM_ROWS || k2 < 0 || k2 >= Board.NUM_COLUMNS) {
									continue;
								}
								if (!board[k][k2].isOpen() && !board[k][k2].isTarget()) {
									Node node = new Node(k, k2);
									node.setHasbom(true);
									result.add(node);
								}
							}
						}
					}
					
					if (numsquarearound - (opened + targeted) == 2) {
						List<Node> lst = new ArrayList<Node>();
						for (int k = i - 1; k < i + 2; k++) {
							for (int k2 = j - 1; k2 < j + 2; k2++) {
								if (k < 0 || k >= Board.NUM_ROWS || k2 < 0 || k2 >= Board.NUM_COLUMNS) {
									continue;
								}
								if (!board[k][k2].isOpen() && !board[k][k2].isTarget()) {
									Node node = new Node(k, k2);
									lst.add(node);
								}
							}
						}
						data.add(lst);
						
					}

				}
			}
		}

		return result;
	}

	private static Node NodeAnal(Board initialState) {
		Square[][] board = initialState.getListSquare();
		int offsetvalue = 0;

		for (int i = 0; i < Board.NUM_ROWS; i++) {
			for (int j = 0; j < Board.NUM_COLUMNS; j++) {

				for (List<Node> lst : data) {
					boolean hasoffset = isContainList(lst, i, j);

					if (board[i][j].isOpen() && hasoffset) {
						offsetvalue = 1;
						int opened = initialState.getNumAroundOpened(i, j);
						int targeted = initialState.getNumAroundTarget(i, j) + offsetvalue;
						int nummine = board[i][j].getNumMineAround();
						int numsquarearound = initialState.getNumSquareAround(i, j) - offsetvalue;

						if (targeted == nummine) {
							for (int k = i - 1; k < i + 2; k++) {
								for (int k2 = j - 1; k2 < j + 2; k2++) {
									if (k < 0 || k >= Board.NUM_ROWS || k2 < 0 || k2 >= Board.NUM_COLUMNS) {
										continue;
									} 
									if (!board[k][k2].isOpen() && !board[k][k2].isTarget()) {
										if ((k == lst.get(0).getX() && k2 == lst.get(0).getY())
												|| (k == lst.get(1).getX() && k2 == lst.get(1).getY())) {

										} else {
											Node node = new Node(k, k2);
											node.setHasbom(false);
											return node;
										}

									}
								}
							}
						}

						if ((opened) == (numsquarearound - nummine)) {
							for (int k = i - 1; k < i + 2; k++) {
								for (int k2 = j - 1; k2 < j + 2; k2++) {
									if (k < 0 || k >= Board.NUM_ROWS || k2 < 0 || k2 >= Board.NUM_COLUMNS) {
										continue;
									} 
									if (!board[k][k2].isOpen() && !board[k][k2].isTarget()) {
										if ((k == lst.get(0).getX() && k2 == lst.get(0).getY())
												|| (k == lst.get(1).getX() && k2 == lst.get(1).getY())) {
											continue;
										}
										Node node = new Node(k, k2);
										node.setHasbom(true);
										return node;
									}
								}
							}
						}
					}
				}
			}
		}
		return null;

	}

	private static boolean isContainList(List<Node> lst, int i, int j) {
		int count = 0;
		for (Node datanode : lst) {
			for (int k = i - 1; k < i + 2; k++) {
				for (int k2 = j - 1; k2 < j + 2; k2++) {
					if (k < 0 || k >= Board.NUM_ROWS || k2 < 0 || k2 >= Board.NUM_COLUMNS) {
						continue;
					}
					if (datanode.getX() == k && datanode.getY() == k2) {
						count++;
					}
				}
			}
		}

		if (count == 2) {
			return true;
		}
		return false;
	}

}
