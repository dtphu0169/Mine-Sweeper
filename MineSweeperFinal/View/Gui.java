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
		int numTarget = board.getNumSquareTarget();//số cờ đã đặt
		controlPanel.updateStatus(numTarget);//update số ô chưa mở
		int numSquareClosed = boardPanel.getNumSquareClosed();//số ô chưa mở
		endGame(numSquareClosed);//kiểm tra xem có win hay lose chưa
	}

	public void target(int x, int y) {
		if (board.isNull()) {
			board = new Board(x, y);
		}
		board.target(x, y);
		boardPanel.updateBoard();
		int numTarget = board.getNumSquareTarget();//số cờ đã đặt
		controlPanel.updateStatus(numTarget);//update số ô chưa mở
	}

	public void restart() {
		board = new Board();
		boardPanel.updateBoard();
		controlPanel.restart();
	}

	//bấm nút "tìm" thì nó tự tìm
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
				JOptionPane.showMessageDialog(this, "Không còn nước đi nào an toàn.");
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
		int numTarget = board.getNumSquareTarget();//số cờ đã đặt
		controlPanel.updateStatus(numTarget);//update số ô chưa mở
		int numSquareClosed = boardPanel.getNumSquareClosed();//số ô chưa mở
		System.out.println("\tso diem: " + ((board.NUM_COLUMNS * board.NUM_ROWS) - numSquareClosed));
		if(endGame(numSquareClosed)) {//nếu đã win hoặc lose rồi thì trả về false để nút biến lại thành "tìm" và stop()
			return false;
		}
		return true;
	}
	
	//neu số ô còn đóng == với số bom được đặt thì hoàn thành trò chơi
	//nếu số ô còn đóng == 0(đã chạm vào ô bom nên hiện hết) thì hiện message là "Bạn đã thất bại !!\n Bạn có muốn chơi lại ?" 
	private boolean endGame(int numSquareClosed) {
		String message = null ;
		if (numSquareClosed == Board.NUM_MINES) {
			message = "Chúc mừng bạn đã hoàn thành trò chơi !!\n Bạn có muốn chơi lại ?";
		} else if (numSquareClosed == 0) {
			message = "Bạn đã thất bại !!\n Bạn có muốn chơi lại ?";
		}
		//nếu message không null thì có option yes and no .Nếu chọn yes thì restart lại .Nếu chọn no thì màn hình message sẽ biến mất và không có gì xảy ra hết
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