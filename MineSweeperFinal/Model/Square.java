package MineSweeperFinal.Model;


public class Square {
	private boolean isOpen;
	private boolean hasMine;
	private int numMineAround;
	private boolean isTarget;

	public Square() {
		isOpen = false;
		hasMine = false;
		isTarget = false;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	//kiem tra xem o do co min chua
	public boolean isHasMine() {
		return hasMine;
	}
	//dat min
	public void setHasMine(boolean hasMine) {
		this.hasMine = hasMine;
	}
	//co ra so min xung quanh
	public int getNumMineAround() {
		return numMineAround;
	}
	//set so min xung quanh
	public void setNumMineAround(int numMineAround) {
		this.numMineAround = numMineAround;
	}
	//kiem tra xem no co phai muc tieu khong
	public boolean isTarget() {
		return isTarget;
	}
	//
	public void setTarget(boolean isTarget) {
		this.isTarget = isTarget;
	}
}