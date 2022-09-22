package MineSweeperFinal.View;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import MineSweeperFinal.Model.*;

public class BoardPanel extends JPanel implements ICommon {
	private static final long serialVersionUID = -6403941308246651773L;
	private Label[][] lbSquare;
	private ITrans listener;
	private int numSquareClosed;

	public BoardPanel() {
		initComp();
		addComp();
		addEvent();
	}

	public void initComp() {
		setLayout(new GridLayout(Board.NUM_ROWS, Board.NUM_COLUMNS));
	}

	
	//tạo bảng lần đầu tiên
	public void addComp() {
		Border border = BorderFactory.createLineBorder(Color.darkGray,1);//mau duong ke
		lbSquare = new Label[Board.NUM_ROWS][Board.NUM_COLUMNS];
		for (int i = 0; i < lbSquare.length; i++) {
			for (int j = 0; j < lbSquare[0].length; j++) {
				lbSquare[i][j] = new Label();
				lbSquare[i][j].setOpaque(true);
				lbSquare[i][j].setBackground(new Color(153, 153, 102));//màu của ô lúc đầu tiên hiện lên
				lbSquare[i][j].setBorder(border);
				lbSquare[i][j].setHorizontalAlignment(JLabel.CENTER);
				lbSquare[i][j].setVerticalAlignment(JLabel.CENTER);
				add(lbSquare[i][j]);
			}
		}
	}

	public void addEvent() {
		for (int i = 0; i < lbSquare.length; i++) {
			for (int j = 0; j < lbSquare[0].length; j++) {
				lbSquare[i][j].x = i;
				lbSquare[i][j].y = j;
				lbSquare[i][j].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						Label label = (Label) e.getComponent();
						if (e.getButton() == MouseEvent.BUTTON1) {
							listener.play(label.x, label.y);
						} else if (e.getButton() == MouseEvent.BUTTON3) {
							listener.target(label.x, label.y);
						}
					}
				});
			}
		}
	}

	public void addListener(ITrans event) {
		listener = event;
	}

	// cap nhat hien thi
	public void updateBoard() {
		Font font = new Font("VNI", Font.PLAIN, 20);
		numSquareClosed = 0;
		Square[][] listSquare = listener.getListSquare();
		for (int i = 0; i < listSquare.length; i++) {
			for (int j = 0; j < listSquare[0].length; j++) {
				lbSquare[i][j].setFont(font);
				if (!listSquare[i][j].isOpen()) {
					lbSquare[i][j].setBackground(new Color(153, 153, 102));//màu của ô lúc chơi
					lbSquare[i][j].setForeground(Color.black); //màu của flag
					numSquareClosed++;
					if (!listSquare[i][j].isTarget()) {//nếu nó không phải là target thì hiện ra ô trống
						lbSquare[i][j].setText("");
					} else {//nếu nó là target thì hiện ra hình flag
						lbSquare[i][j].setText("\uD83D\uDEA9"); // ki tu 'flag'
					}
				} else {
					if (listSquare[i][j].isHasMine()) {
						lbSquare[i][j].setText("\uD83D\uDCA3"); // ki tu 'bomb'
					} else {
						int numMineAround = listSquare[i][j].getNumMineAround();
						if (numMineAround == 0) {
							lbSquare[i][j].setText("");
						} else {
							lbSquare[i][j].setText(numMineAround + "");
							// dat mau so cho de nhin
							switch (numMineAround) {
							case 1:
								lbSquare[i][j].setForeground(new Color(77, 148, 255));
								break;
							case 2:
								lbSquare[i][j].setForeground(new Color(0, 179, 0));
								break;
							case 3:
								lbSquare[i][j].setForeground(new Color(179, 0, 0));
								break;
							case 4:
								lbSquare[i][j].setForeground(new Color(0, 0, 179));
								break;
							case 5:
								lbSquare[i][j].setForeground(new Color(77, 0, 0));
								break;
							case 6:
								lbSquare[i][j].setForeground(new Color(0, 230, 184));
								break;
							case 7:
								lbSquare[i][j].setForeground(new Color(230, 92, 0));
								break;
							case 8:
								lbSquare[i][j].setForeground(new Color(172, 52, 172));
								break;
							}
						}
					}
					lbSquare[i][j].setBackground(Color.white);
				}
			}
		}
	}
	
	// tao class con de mo rong thuoc tinh cho lop jlabel
	// giup luu chi so hang, cot cua label do trong griblayout
	// vi khong the truyen ve gia tri i,j trong phuong thuc addMouseListener
	private class Label extends JLabel {
		private static final long serialVersionUID = 6099893043079770073L;
		private int x;
		private int y;
	}

	public int getNumSquareClosed() {
		return numSquareClosed;
	}
}
