package farkle;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PlayState extends GameState {

	public static final int NUMOFDICE = 6;

	private final ArrayList<Die> freeDice;
	private final ArrayList<Die> selectedDice;
	private final ArrayList<Die> capturedDice;

	private final ArrayList<Player> players;
	private int playerTurn;

	private int runningTotal;
	private boolean turnStart = true;
	private boolean rolling = false;    //This will say if you should change the face of the dice during it's updates

	public PlayState(Renderer render, StateManager controller) {
		super(render, controller);
		players = new ArrayList<>();
		freeDice = new ArrayList<>();
		selectedDice = new ArrayList<>();
		capturedDice = new ArrayList<>();

		for (int i = 0; i < NUMOFDICE; i++) {
			freeDice.add(new Die(
					new Vector2(((double) i + 1d) / ((double) NUMOFDICE + 1d) * (Renderer.WindowWidth - GUI.RIGHTPANELSIZE), (Renderer.WindowHeight - GUI.BOTTOMPANELSIZE) / 2),
					new Rectangle(0, 0, Renderer.WindowWidth - GUI.RIGHTPANELSIZE, Renderer.WindowHeight - GUI.BOTTOMPANELSIZE)));
		}
		players.add(new Player("Josh"));
		players.add(new Player("Skynet", true));
	}

	@Override
	public void draw(Graphics g) {
		GUI.draw(g, this);
	}

	private void handleKeys() {
		if (Input.isKeyPressed(KeyEvent.VK_ESCAPE)) {
			controller.pop();
		}
	}

	private void selectDicePressed(MouseEvent e) {
		//FIXME code duplication, extract method?
		for (int i = 0; i < freeDice.size(); i++) {
			if (Math.hypot(freeDice.get(i).getPosition().getX() - e.getX(), freeDice.get(i).getPosition().getY() - e.getY()) < Die.HYPOT / 2) {
				selectedDice.add(freeDice.get(i));
				freeDice.remove(i);
				return;
			}
		}
		for (int i = 0; i < selectedDice.size(); i++) {
			if (Math.hypot(selectedDice.get(i).getPosition().getX() - e.getX(), selectedDice.get(i).getPosition().getY() - e.getY()) < Die.HYPOT / 2) {
				freeDice.add(selectedDice.get(i));
				selectedDice.remove(i);
				return;
			}
		}
	}

	private void endTurnPressed() {
		if (players.get(playerTurn).getScore() == 0) {
			if (runningTotal + getScore(selectedDice) < 500) {
				return;
			}
		}
		runningTotal += getScore(selectedDice);
		endTurn();
	}

	private void nextRollPressed() {
		if (turnStart) {        //If this is the first roll of a hand
			turnStart = false;
			shakeDice();
		}
		if (getScore(selectedDice) != 0 && verifyHand(selectedDice)) {
			runningTotal += getScore(selectedDice);
			capturedDice.addAll(selectedDice);
			selectedDice.clear();

			for (int i = 0; i < capturedDice.size(); i++) {
				capturedDice.get(i).setAngle(0);
				capturedDice.get(i).setPosition(new Vector2(100 + i * 150, Renderer.WindowHeight - GUI.BOTTOMPANELSIZE + 100));
			}
			if (freeDice.isEmpty()) {
				nextHand();
				GUI.notify("Extra Hand!");
			} else {
				shakeDice();
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (players.get(playerTurn).isAI()) {    //Don't allow any mouse presses while the AI is taking it's turn
			return;
		}
		if (!rolling) {
			if (e.getButton() == MouseEvent.BUTTON1 && !turnStart) {
				selectDicePressed(e);
			} else if (e.getButton() == MouseEvent.BUTTON2) {		//TODO MAKE THIS NOT RIGHT MOUSE FOR NEXT ROLL
				endTurnPressed();
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				nextRollPressed();
			}
		}
	}

	@Override
	public void update() {
		handleKeys();        //Handle any input from the keyboard
		if (rolling) {        //If there are dice rolling
			for (Die aFreeDice : freeDice) {        //update all the dice
				aFreeDice.update();
			}
			rolling = false;    //Set rolling to false

			for (Die aFreeDice : freeDice) {        //If any dice are still rolling, set rolling back to true
				if (!aFreeDice.isStopped()) {
					rolling = true;
					break;
				}
			}
			if (!rolling && getScore(freeDice) == 0) {    //If it was rolling, and now is not and the hand is worthless WE HAVE A FARKLE LADIES AND GENTLEMAN
				GUI.notify("FARKLE");
				runningTotal = 0;
				endTurn();
			}
		} else if (players.get(playerTurn).isAI()) {
			if (turnStart) {
				turnStart = false;
				shakeDice();
				return;
			}

			//TODO THIS IS WHERE WE INTERFACE WITH AN AI CLASS
			boolean solved = false;
			while (!solved) {
				int num = (int) (Math.random() * freeDice.size()) + 1;
				for (int i = 0; i < num; i++) {
					selectedDice.add(freeDice.remove((int) (Math.random() * freeDice.size())));
				}
				if (!verifyHand(selectedDice)) {
					freeDice.addAll(selectedDice);
					selectedDice.clear();
					continue;
				}
				if (getScore(selectedDice) == 0) {
					freeDice.addAll(selectedDice);
					selectedDice.clear();
				} else {
					runningTotal += getScore(selectedDice);
					capturedDice.addAll(selectedDice);
					selectedDice.clear();
					for (int i = 0; i < capturedDice.size(); i++) {
						capturedDice.get(i).setAngle(0);
						capturedDice.get(i).setPosition(new Vector2(100 + i * 150, Renderer.WindowHeight - GUI.BOTTOMPANELSIZE + 100));
					}
					if (runningTotal < 500) {
						shakeDice();
					} else {
						endTurn();
					}
					solved = true;
				}
			}
		}
	}

	private void shakeDice() {
		rolling = true;
		for (Die aFreeDice : freeDice) {
			aFreeDice.shake(100);
		}
	}

	private void nextHand() {
		turnStart = true;
		freeDice.clear();
		selectedDice.clear();
		capturedDice.clear();
		for (int i = 0; i < NUMOFDICE; i++) {
			freeDice.add(new Die(
					new Vector2(((double) i + 1d) / ((double) NUMOFDICE + 1d) * (Renderer.WindowWidth - GUI.RIGHTPANELSIZE), (Renderer.WindowHeight - GUI.BOTTOMPANELSIZE) / 2),
					new Rectangle(0, 0, Renderer.WindowWidth - GUI.RIGHTPANELSIZE, Renderer.WindowHeight - GUI.BOTTOMPANELSIZE)));
		}
	}

	private void endTurn() {
		System.out.println("Player " + players.get(playerTurn).getName() + " earned " + runningTotal + " Points");
		players.get(playerTurn).finishTurn(runningTotal);        //Give the player the points they earned
		runningTotal = 0;
		nextHand();
		playerTurn = (playerTurn + 1) % players.size();
	}

	//FIXME test
	public static boolean verifyHand(ArrayList<Die> Dice) {
		//build record of how many of each die-value we have
		int[] oc = new int[Die.MAXVALUE];
		for (Die aDice : Dice) {
			oc[aDice.getValue() - 1]++;
		}
		//test failure conditions
		for (int i = 0; i < Die.MAXVALUE; i++) {
			//we don't need to pay attention to non-existent die-values
			if (oc[i] > 0) {
				//ones and twos of values other than 1 or 5 are invalid except in scenarios
				//that involve using all 6 dice and will have been handled separately
				if (oc[i] < 3 && (i != 0 && i != 4)) {
					return false;
				}
			}
		}

		return true;
	}

	public static int getScore(ArrayList<Die> dice) {
		int score = 0;
		int[] oc = new int[Die.MAXVALUE];	//Occurance Count

		for (Die aDice : dice) {
			oc[aDice.getValue() - 1]++;
		}

		for (int i = 0; i < oc.length; i++) {
			if (oc[i] == 4) {
				score += 1000;
				oc[i] -= 4;
				for (int anOc2 : oc) {	//Check for a pair to determine if its a full house
					if (anOc2 == 2) {
						return 1500;	//FULL HOSUSE
					}
				}
			} else if (oc[i] == 5) {
				score += 2000;
				oc[i] -= 5;
			} else if (oc[i] == 6) {
				return 3000;	//Max points
			}
		}
		score += 100 * oc[0];	//Handle Single Ones
		score += 50 * oc[4];	//Handle Single Fives

		//We don't care about Triple Ones, as it adds up to the same points as single ones
		score += 200 * (oc[1] / 3);	//Handle Triple twos
		score += 300 * (oc[2] / 3);	//Handle Triple threes
		score += 400 * (oc[3] / 3);	//Handle Triple fours
		score += 350 * (oc[4] / 3);	//Handle Triple fives	SPECIAL MATH We would already get 150 from the single fives scoring
		score += 600 * (oc[5] / 3);	//Handle Triple sixes
		//STRAIGHT CHECK
		int count = 0;
		for (int anOc3 : oc) {
			if (anOc3 == 1) {
				count++;
			}
		}
		if (count == 6) {
			return 1500;        //Special case, this will use the whole roll, leaving no other points available
		}
		//ONLY PAIRS CHECK
		count = 0;
		for (int anOc2 : oc) {
			if (anOc2 == 2) {
				count++;
			}
		}
		if (count == 3) {        //If we found 3 pairs
			return 1500;        //Special case, this will use the whole roll, leaving no other points available
		}
		//ONLY TRIPLETS CHECK
		count = 0;
		for (int anOc : oc) {
			if (anOc == 3) {
				count++;
			}
		}
		if (count == 2) {    //If we found 2 triplets
			return 2500;        //Special case, this will use the whole roll, leaving no other points available
		}

		return score;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public int getPlayerTurn() {
		return playerTurn;
	}

	public int getRunningTotal() {
		return runningTotal;
	}

	public int getCurrentSelectionScore() {
		return getScore(selectedDice);
	}

	public ArrayList<Die> getFreeDice() {
		return freeDice;
	}

	public ArrayList<Die> getSelectedDice() {
		return selectedDice;
	}

	public ArrayList<Die> getCapturedDice() {
		return capturedDice;
	}
>>>>>>> master
}