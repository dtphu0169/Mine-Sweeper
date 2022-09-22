package MineSweeperFinal.Model;


public enum Mode {
	PLAYER,AI1,AI2;

	@Override
	public String toString() {
		switch (this) {
		case PLAYER:
			return "Player";
		case AI1:
			return "Percent";
		case AI2:
			return "Logic";
		}
		return super.toString();
		
	}	
}
