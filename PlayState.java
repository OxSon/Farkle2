package farkle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PlayState extends GameState {

	public static final int NUMOFDICE = 6;

	private ArrayList<Die> FreeDice;
	private ArrayList<Die> SelectedDice;

	private int CapturedDice;

	private ArrayList<Player> Players;

	private boolean Rolling;	//This will say if you should change the face of the dice during it's updates

	public PlayState(Renderer Render, StateManager Controller) {
		super(Render, Controller);
		Players = new ArrayList<>();
		FreeDice = new ArrayList<>();
		SelectedDice = new ArrayList<>();
		for (int i = 0; i < NUMOFDICE; i++) {
			FreeDice.add(new Die(
					new Vector2(((double) i + 1d) / ((double) NUMOFDICE + 1d) * (Renderer.WindowWidth - GUI.RIGHTPANELSIZE), (Renderer.WindowHeight - GUI.BOTTOMPANELSIZE) / 2),
					new Rectangle(0, 0, Renderer.WindowWidth - GUI.RIGHTPANELSIZE, Renderer.WindowHeight - GUI.BOTTOMPANELSIZE)));
		}
		Players.add(new Player("Josh"));
		Players.add(new Player("Skynet"));
	}

	@Override
	public void Draw(Graphics g) {
		GUI.Draw(g, Players);
		for (int i = 0; i < FreeDice.size(); i++) {
			FreeDice.get(i).draw(g);
		}
		for (int i = 0; i < SelectedDice.size(); i++) {
			g.setColor(Color.RED);
			g.fillOval((int) SelectedDice.get(i).getPosition().GetX() - (int) Die.HYPOT / 2, (int) SelectedDice.get(i).getPosition().GetY() - (int) Die.HYPOT / 2, (int) Die.HYPOT, (int) Die.HYPOT);
			SelectedDice.get(i).draw(g);
		}
	}

	private void HandleKeys() {
		if (Input.IsKeyPressed(KeyEvent.VK_ESCAPE)) {
			Controller.Pop();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!Rolling) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				for (int i = 0; i < FreeDice.size(); i++) {
					if (Math.sqrt(Math.pow(FreeDice.get(i).getPosition().GetX() - e.getX(), 2) + Math.pow(FreeDice.get(i).getPosition().GetY() - e.getY(), 2)) < Die.HYPOT / 2) {
						SelectedDice.add(FreeDice.get(i));
						FreeDice.remove(i);
						return;
					}
				}
				for (int i = 0; i < SelectedDice.size(); i++) {
					if (Math.sqrt(Math.pow(SelectedDice.get(i).getPosition().GetX() - e.getX(), 2) + Math.pow(SelectedDice.get(i).getPosition().GetY() - e.getY(), 2)) < Die.HYPOT / 2) {
						FreeDice.add(SelectedDice.get(i));
						SelectedDice.remove(i);
						return;
					}
				}
			}
		}
	}

	@Override
	public void Update() {
		HandleKeys();
		if (Rolling) {
			Rolling = false;
			for (int i = 0; i < FreeDice.size(); i++) {
				if (!FreeDice.get(i).isStopped()) {
					Rolling = true;
				}
			}
		}
	}

	public static int getScore(ArrayList<Die> Dice) {
		int score = 0;
		int[] oc = new int[Die.MAXVALUE];
		for (int i = 0; i < Dice.size(); i++) {
			oc[Dice.get(i).getValue()]++;
		}
		if (oc[0] == 1) {
			score += 100;
		}
		if (oc[4] == 1) {
			score += 50;
		}
		if (oc[0] == 3) {
			score += 300;
		}
		if (oc[1] == 3) {
			score += 200;
		}
		if (oc[2] == 3) {
			score += 300;
		}
		if (oc[3] == 3) {
			score += 400;
		}
		if (oc[4] == 3) {
			score += 500;
		}
		if (oc[5] == 3) {
			score += 600;
		}
		for (int i = 0; i < oc.length; i++){
			if (oc[i] == 4){
				score += 1000;
			}
			if (oc[i] == 5){
				score += 2000;
			}
			if (oc[i] == 6){
				score += 3000;
			}
		}
		for (int i = 0; i < oc.length; i++){
			if (oc[i] != 1){
				break;
			}
			return 1500;		//Special case, this will use the whole roll, leaving no other points avalible
		}
		for (int i = 0; i < oc.length; i++){
			if (oc[i] != 2 || oc[i] != 0){
				break;
			}
			return 1500;		//Special case, this will use the whole roll, leaving no other points avalible
		}
		for (int i = 0; i < oc.length; i++){
			if (oc[i] != 3 || oc[i] != 0){
				break;
			}
			return 2500;		//Special case, this will use the whole roll, leaving no other points avalible
		}
		for (int i = 0; i < oc.length; i++){
			if (oc[i] != 4 || oc[i] != 2){		//VERY IMPORTANT THAT WE RUN 3 PAIRS CHECK FIRST OTHERWISE THIS WOULD BE HIT
				break;							//EVEN THOUGH IT IS THE SAME POINT VALUE, IF WE CUSTOM THE POINTS EARNED, IT WOULD BREAKITY
			}
			return 1500;		//Special case, this will use the whole roll, leaving no other points avalible
		}
		return score;
	}
}
