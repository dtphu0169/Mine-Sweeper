package MineSweeperFinal.View;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import MineSweeperFinal.Model.*;

public class ControlPanel extends JPanel implements ICommon, ActionListener {
	private static final long serialVersionUID = 5219120377989554161L;
	public static final boolean STT_WIN = true;
	public static final boolean STT_LOSE = false;
	public static Mode mode = Mode.PLAYER;
	private JLabel lbNumBomRemain;
	private JLabel lbNotify;
	private JButton btnRestart, btnExcute, btnStop;
	private ITrans listener;
	private JComboBox cbMode;
	String selectedValue = "";

	boolean started = false;
	Timer t = new Timer(300, new ActionListener() {

		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			boolean checkone = listener.runFind();
			if (!checkone) { // nếu chưa chọn ô đầu tiên hoặc đã thua(thắng)
				started = false;
				btnExcute.setText("tìm");
//				listener.restart();
//				lbNumSquareClosed.setText("so o chua mo: " + Board.NUM_ROWS * Board.NUM_COLUMNS);
//				lbNotify.setText("");
				stop();
			}
		}
	});

	public ControlPanel() {
		initComp();
		addComp();
		addEvent();

	}

	public void initComp() {
		setLayout(null);
	}

	public void addComp() {
		Font font = new Font("VNI", Font.PLAIN, 20);

		lbNumBomRemain = new JLabel();
		lbNumBomRemain.setFont(font);
		lbNumBomRemain.setText("bom còn lại: " + Board.NUM_MINES );
		lbNumBomRemain.setBounds(10, 10, 250, 40);
		add(lbNumBomRemain);

		lbNotify = new JLabel();
		lbNotify.setFont(font);
		lbNotify.setBounds(270, 10, 200, 40);
		add(lbNotify);

		btnExcute = new JButton();
		btnExcute.setFont(font);
		btnExcute.setText("tìm ");
		btnExcute.setBounds(220, 10, 140, 40);// 220 là vị trí từ cạnh bên trái qua bên trái, 10 là vị trí từ trên xuống
												// phía dưới, 140 là độ dài, 40 là độ cao

		add(btnExcute);
		btnExcute.addActionListener(this);

		cbMode = new JComboBox();
		cbMode.setModel(new DefaultComboBoxModel(Mode.values()));
		cbMode.setBounds(400, 10, 120, 40);
		add(cbMode);

		btnRestart = new JButton();
		btnRestart.setFont(font);
		btnRestart.setText("chơi lại");
		btnRestart.setBounds(550, 10, 120, 40);
		add(btnRestart);
		btnRestart.addActionListener(this);
	}

	public void addEvent() {
		// lam ben duoi het roi
	}

	public void addListener(ITrans event) {
		listener = event;
	}

	// update số ô chưa mở
	public void updateStatus(int numTarget) {
		lbNumBomRemain.setText("bom còn lại: " + (Board.NUM_MINES - numTarget));
		// if (numSquareClosed == Board.NUM_MINES) {
		// lbNotify.setText("WIN");
		// lbNotify.setForeground(Color.blue);
		// } else if (numSquareClosed == 0) {
		// lbNotify.setText("LOSE");
		// lbNotify.setForeground(Color.red);
		// }
	}

	// action của những cái nút
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btnExcute) {
			selectedValue = cbMode.getSelectedItem().toString();//lấy giá trị trong Jcombobox
			if (selectedValue.equalsIgnoreCase("Percent")) {//nếu là máy 1 thì mới bấm được

				mode = Mode.AI1;
				if (started == false) {
					started = true;
					btnExcute.setText("dừng");
					start();
				} else {
					started = false;
					btnExcute.setText("tìm");
					stop();
				}
			}
			if (selectedValue.equalsIgnoreCase("Logic")) {

				mode = Mode.AI2;
				if (started == false) {
					started = true;
					btnExcute.setText("dừng");
					start();
				} else {
					started = false;
					btnExcute.setText("tìm");
					stop();
				}
			}
			if (selectedValue.equalsIgnoreCase("Player")) {

				mode = Mode.PLAYER;
				listener.runFind();
			}
		}
		if (e.getSource() == btnRestart) {
			started = false;
			btnExcute.setText("tìm");
			listener.restart();//
			lbNumBomRemain.setText("bom còn lại: " + Board.NUM_MINES);
			lbNotify.setText("");
			stop();
		}
	}

	void start() {
		t.start();
	}

	void stop() {
		t.stop();
	}
	
	public void restart() {//hàm đc gọi từ listener nên ko restart listener 
		started = false;
		btnExcute.setText("tìm");
		lbNumBomRemain.setText("bom còn lại: " + Board.NUM_MINES);
		lbNotify.setText("");
		stop();
	}
}
