package MineSweeperFinal.View;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import MineSweeperFinal.Algo.*;
import MineSweeperFinal.Model.*;

public class Gui extends JFrame implements ICommon, ITrans {
	private static final long serialVersionUID = -5479701518838741039L;
	private static final String TITLE = "MineSweeper";
	public static final int FRAME_WIDTH = 730;
	public static final int FRAME_HEIGHT = 600;
	private BoardPanel boardPanel;
	private ControlPanel controlPanel;
	private Board board;

	public Gui() {
		board = new Board();
		initComp();
		addComp();
		addEvent(); 
	}

	public Gui(Board b) {
		board = b;
		initComp();
		addComp();
		addEvent(); 
	}
	
	public void initComp() {
		setTitle(TITLE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLayout(null);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void addComp() {
		boardPanel = new BoardPanel();
		boardPanel.setBounds(10, 60, 700, 500);
		add(boardPanel);
		boardPanel.addListener(this);

		controlPanel = new ControlPanel();
		controlPanel.setBounds(10, 0, 700, 60);
		add(controlPanel);
		controlPanel.addListener(this);
	}

	public void addEvent() {
		WindowListener wd = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int kq = JOptionPane.showConfirmDialog(Gui.this, "DO YOU WANT TO QUIT?", "NOTE",
						JOptionPane.YES_NO_OPTION);
				if (kq == JOptionPane.YES_OPTION) {
					dispose();
				}
			}
		};
		addWindowListener(wd);
	}

	public Square[][] getListSquare() {
		return board.getListSquare();
	}

	//nhan de choi
	public void play(int x, int y) {
		if (board.isNull()) {
			board = new Board(x, y);
		}
		if (board.isTarget(x,y)) {
			return;
		}
		boolean check = board.play(x, y);
		if (!check) {
			board.showAllSquares();
		}
		boardPanel.updateBoard();
		// cap nhat so o chua mo controlPanel
		int numTarget = board.getNumSquareTarget();//s??? c??? ???? ?????t
		controlPanel.updateStatus(numTarget);//update s??? ?? ch??a m???
		int numSquareClosed = boardPanel.getNumSquareClosed();//s??? ?? ch??a m???
		endGame(numSquareClosed);//ki???m tra xem c?? win hay lose ch??a
	}

	public void target(int x, int y) {
		if (board.isNull()) {
			board = new Board(x, y);
		}
		board.target(x, y);
		boardPanel.updateBoard();
		int numTarget = board.getNumSquareTarget();//s??? c??? ???? ?????t
		controlPanel.updateStatus(numTarget);//update s??? ?? ch??a m???
	}

	public void restart() {
		board = new Board();
		boardPanel.updateBoard();
		controlPanel.restart();
	}

	//b???m n??t "t??m" th?? n?? t??? t??m
	public boolean runFind() {
		if (board.isNull()) {
			int x = board.genRan(Board.NUM_ROWS);
			int y = board.genRan(Board.NUM_COLUMNS);
			board = new Board(x, y);
			board.play(x, y);
			boardPanel.updateBoard();
			return true;
		}
		Board temp;
		switch (controlPanel.mode) {
		case AI1:
			temp = PercentExcute.excute(board);
			break;
		case AI2:
		case PLAYER:
			temp = ClauseLogicExcute.excute(board);
			if (temp == null) {
				JOptionPane.showMessageDialog(this, "Kh??ng c??n n?????c ??i n??o an to??n.");
				return false;
			}
			break;
		default:
			return false;
		}
		
		if (temp == null) {
			return false;
		}
		
		this.board = temp;
		if (!board.checkwin()) {
			board.showAllSquares();
		}
		boardPanel.updateBoard();
		int numTarget = board.getNumSquareTarget();//s??? c??? ???? ?????t
		controlPanel.updateStatus(numTarget);//update s??? ?? ch??a m???
		int numSquareClosed = boardPanel.getNumSquareClosed();//s??? ?? ch??a m???
		System.out.println("\tso diem: " + ((board.NUM_COLUMNS * board.NUM_ROWS) - numSquareClosed));
		if(endGame(numSquareClosed)) {//n???u ???? win ho???c lose r???i th?? tr??? v??? false ????? n??t bi???n l???i th??nh "t??m" v?? stop()
			return false;
		}
		return true;
	}
	
	//neu s??? ?? c??n ????ng == v???i s??? bom ???????c ?????t th?? ho??n th??nh tr?? ch??i
	//n???u s??? ?? c??n ????ng == 0(???? ch???m v??o ?? bom n??n hi???n h???t) th?? hi???n message l?? "B???n ???? th???t b???i !!\n B???n c?? mu???n ch??i l???i ?" 
	private boolean endGame(int numSquareClosed) {
		String message = null ;
		if (numSquareClosed == Board.NUM_MINES) {
			message = "Ch??c m???ng b???n ???? ho??n th??nh tr?? ch??i !!\n B???n c?? mu???n ch??i l???i ?";
		} else if (numSquareClosed == 0) {
			message = "B???n ???? th???t b???i !!\n B???n c?? mu???n ch??i l???i ?";
		}
		//n???u message kh??ng null th?? c?? option yes and no .N???u ch???n yes th?? restart l???i .N???u ch???n no th?? m??n h??nh message s??? bi???n m???t v?? kh??ng c?? g?? x???y ra h???t
		if (message != null) {
			int answer = JOptionPane.showConfirmDialog(this, message,"", JOptionPane.YES_NO_OPTION);
			if (answer == JOptionPane.YES_OPTION) {
				restart();
				return true;
			}
		}
		return false;
	}
}