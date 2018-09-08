package farkle;

public class Player {

	private String name;
	private int Score;
	private int Farkles;

	public Player(String name) {
		this.name = name;
	}

	public void FinishTurn(int PointsScored) {
		if (PointsScored == 0) {
			Farkles++;
		} else {
			Score += PointsScored;
		}
	}
	
	public int getScore(){
		return Score;
	}
	public int getFarkels(){
		return Farkles;
	}
	public String getName(){
		return name;
	}
}
