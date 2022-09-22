package MineSweeperFinal.Model;

public class Percent {
	double value;
	int i;
	int j;
	public Percent(double value, int i, int j) {
		super();
		this.value = value;
		this.i = i;
		this.j = j;
	}
	public Percent() {
		
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public int getJ() {
		return j;
	}
	public void setJ(int j) {
		this.j = j;
	}
	
}
