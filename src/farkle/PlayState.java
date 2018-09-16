package farkle;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Represents a state of the game
 * <p>
 * @author Alec Mills and Josh DeMoss
 */
public class PlayState extends GameState {

    //how many dice are we playing the game with
	public static final int NUMOFDICE = 6;

	//dice that have not been selected yet in a given turn
	private final ArrayList<Die> freeDice;
	//dice that have been selected
	private ArrayList<Die> selectedDice;
	//dice that have been confirmed (i.e. selected, validated, and then the remaining free dice rolled again)
	private final ArrayList<Die> capturedDice;

	private final ArrayList<Player> players;
	//tracks player turn (0 or 1)
	private int playerTurn;

	//points a player has accrued so far in this turn only
	private int runningTotal;
	private boolean turnStart = true;
	//should we change the face of the dice during update
	private boolean rolling = false;
	//TODO JOSH
	private boolean gameOver;
	//TODO JOSH
	private int gameOverPlayer;

	//TODO JOSH
    private final Button bRollAgain;
    //TODO JOSH
    private final Button bEndTurn;

    //TODO JOSH
    public PlayState(Renderer render, StateManager controller) {
        super(render, controller);
        players = new ArrayList<>();
        freeDice = new ArrayList<>();
        selectedDice = new ArrayList<>();
        capturedDice = new ArrayList<>();

        //FIXME remove one AI player
        players.add(new Player("Josh", false));
        players.add(new Player("Skynet", true));

        for (int i = 0; i < NUMOFDICE; i++) {
            freeDice.add(new Die(
                    new Vector2(((double) i + 1d) / ((double) NUMOFDICE + 1d) * (Renderer.WindowWidth - GUI.RIGHTPANELSIZE), (Renderer.WindowHeight - GUI.BOTTOMPANELSIZE) / 2),
                    new Rectangle(0, 0, Renderer.WindowWidth - GUI.RIGHTPANELSIZE, Renderer.WindowHeight - GUI.BOTTOMPANELSIZE)));
        }
        bRollAgain = new Button(
                new Rectangle(
                        Renderer.WindowWidth - GUI.RIGHTPANELSIZE - GUI.BUTTONWIDTH - GUI.PANELBORDERSIZE,
                        Renderer.WindowHeight - GUI.BOTTOMPANELSIZE + GUI.PANELBORDERSIZE,
                        GUI.BUTTONWIDTH,
                        (GUI.BOTTOMPANELSIZE - GUI.PANELBORDERSIZE) / 2),
                "Roll Again", Color.BLACK, Color.GRAY, Color.GREEN, 3, 30);
        bEndTurn = new Button(
                new Rectangle(
                        Renderer.WindowWidth - GUI.RIGHTPANELSIZE - GUI.BUTTONWIDTH - GUI.PANELBORDERSIZE ,
                        Renderer.WindowHeight - GUI.BOTTOMPANELSIZE / 2,
                        GUI.BUTTONWIDTH,
                        (GUI.BOTTOMPANELSIZE - GUI.PANELBORDERSIZE) / 2),
                "End Turn", Color.BLACK, Color.GRAY, Color.GREEN, 3, 30);
    }

    /**
     * @param g Graphics
     */
    @Override
    public void draw(Graphics g) {
        GUI.draw(g, this);
        bRollAgain.Draw(g);
        bEndTurn.Draw(g);
    }

    //TODO JOSH
    private void handleKeys() {
        if (Input.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            controller.pop();
        }
    }

    /**
     * moves selected dice from playing board area to captured dice area
     *
     * @param e MouseEvent
     */
    private void selectDicePressed(MouseEvent e) {
        if (checkSelectedDice(e, freeDice, selectedDice)) return;
        checkSelectedDice(e, selectedDice, freeDice);
    }

    /**
     * checks if a mouse click happened on a die
     * @param e MouseEvent
     * @param freeDice ArrayList<>
     * @param selectedDice ArrayList<>
     * @return true or false as the click was or wasn't on a die
     */
    private boolean checkSelectedDice(MouseEvent e, ArrayList<Die> freeDice, ArrayList<Die> selectedDice) {
        for (int i = 0; i < freeDice.size(); i++) {
            if (Math.hypot(freeDice.get(i).getPosition().getX() - e.getX(), freeDice.get(i).getPosition().getY() - e.getY()) < Die.HYPOT / 2) {
                selectedDice.add(freeDice.get(i));
                freeDice.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * handles ending of turn and scoring
     */
    private void endTurnPressed() {
        if (players.get(playerTurn).getScore() == 0) {
            if (runningTotal + getScore(selectedDice) < 500) {
                return;
            }
        }
        runningTotal += getScore(selectedDice);
        endTurn();
    }

    /**
     * allows human player to roll again
     */
    private void rollAgainPressed() {
        if (turnStart) {        //If this is the first roll of a hand
            turnStart = false;
            shakeDice();
        }
        if (getScore(selectedDice) != 0 && verifyHand(selectedDice)) {
            runningTotal += getScore(selectedDice);
            captureDice();
            if (freeDice.isEmpty()) {
                nextHand();
                GUI.notify("Extra Hand!");
            } else {
                shakeDice();
            }
        }
    }

    /**
     * Left-click: button only
     * Middle-click: end turn (if allowed)
     * Right-click: roll again (if allowed)
     * @param e MouseEvent
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (players.get(playerTurn).isAI()) {    //Don't allow any mouse presses while the AI is taking it's turn
            return;
        }
        if (!rolling) {
            switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    if (!turnStart)
                        selectDicePressed(e);
                    if (bRollAgain.containsPoint(new Vector2(e.getX(),e.getY()))){
                        rollAgainPressed();
                    }	if (bEndTurn.containsPoint(new Vector2(e.getX(),e.getY()))){
                    endTurnPressed();
                }	break;
                case MouseEvent.BUTTON2:
                    endTurnPressed();
                    break;
                case MouseEvent.BUTTON3:
                    rollAgainPressed();
                    break;
                default:
            }
        }
    }

    //TODO JOSH
    @Override
    public void update() {
        handleKeys();        //Handle any input from the keyboard

        Vector2 mousePosition = new Vector2(MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY());
        bRollAgain.update(mousePosition);
        bEndTurn.update(mousePosition);
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

            SkyNet.takeTurn(this);
        }
    }

    /**
     * move selected dice to captured area
     */
    public void captureDice() {
        capturedDice.addAll(selectedDice);
        selectedDice = new ArrayList<>();

        for (int i = 0; i < capturedDice.size(); i++) {
            capturedDice.get(i).setAngle(0);
            capturedDice.get(i).setPosition(new Vector2(100 + i * 150, Renderer.WindowHeight - GUI.BOTTOMPANELSIZE + 100));
        }
    }

    /**
     * move N dice to selected state
     * @param dice to be selected
     */
    public void setSelectedDice(ArrayList<Die> dice) {
        selectedDice.clear();
        for (Die die : dice) {
            freeDice.remove(die);
            selectedDice.add(die);
        }
    }

    /**
     * increments total score
     * @param score
     */
    public void addRunningTotal(int score) {
        runningTotal += score;
    }

    /**
     * causes the dice to move visually
     */
    public void shakeDice() {
        rolling = true;
        for (Die aFreeDice : freeDice) {
            aFreeDice.shake(100);
        }
    }

    /**
     * gives player another 6 dice if they use all 6 succesfully
     */
    public void nextHand() {
        //get rid of previous dice
        turnStart = true;
        freeDice.clear();
        selectedDice.clear();
        capturedDice.clear();

        //draw new dice
        for (int i = 0; i < NUMOFDICE; i++) {
            freeDice.add(new Die(
                    new Vector2(((double) i + 1d) / ((double) NUMOFDICE + 1d) * (Renderer.WindowWidth - GUI.RIGHTPANELSIZE), (Renderer.WindowHeight - GUI.BOTTOMPANELSIZE) / 2),
                    new Rectangle(0, 0, Renderer.WindowWidth - GUI.RIGHTPANELSIZE, Renderer.WindowHeight - GUI.BOTTOMPANELSIZE)));
        }
    }

    /**
     * handles ending of turn and win conditions
     */
    public void endTurn() {
        System.out.println("Player " + players.get(playerTurn).getName() + " earned " + runningTotal + " Points");
        getActivePlayer().finishTurn(runningTotal);        //Give the player the points they earned
        runningTotal = 0;
        nextHand();
        //FIXME debugging
        if (getActivePlayer().getScore() >= 1000){
            gameOver = true;
            gameOverPlayer = playerTurn;
        }
        playerTurn = (playerTurn + 1) % players.size();
        if (gameOver){
            if (gameOverPlayer == playerTurn){
                Player winningPlayer = players.get(gameOverPlayer);
                for (Player p : players){
                    if (p.getScore() > winningPlayer.getScore()){
                        winningPlayer = p;
                    }
                }
                controller.push(new GameOverState(render, controller, winningPlayer));
            }
        }
    }

    /**
     * validates that a selection of dice is legal and scoring according to farkle rules
     * @param dice the dice to check
     * @return true or false as the hand is or isnt legal
     */
    public boolean verifyHand(ArrayList<Die> dice) {
        //build record of how many of each die-value we have
        int[] oc = new int[Die.MAXVALUE];
        for (Die die : dice) {
            oc[die.getValue() - 1]++;
        }
        if (allDiceScoring(oc) != 0) {
            return true;
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


    //TODO JOSH
    public int allDiceScoring(int[] oc) {
        //Full House Check
        for (int oc2 : oc) {
            if (oc2 == 4) {
                for (int oc3 : oc) {    //Check for a pair to determine if its a full house
                    if (oc3 == 2) {
                        return 1500;    //FULL HOSUSE
                    }
                }
            }
        }
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
        return 0;
    }


    //TODO JOSH
    public int getScore(ArrayList<Die> dice) {
        int score = 0;
        int[] oc = new int[Die.MAXVALUE];    //Occurance Count

        for (Die aDice : dice) {
            oc[aDice.getValue() - 1]++;
        }

        if (allDiceScoring(oc) != 0) {
            return allDiceScoring(oc);
        }
        for (int i = 0; i < oc.length; i++) {
            if (oc[i] == 4) {
                score += 1000;
                oc[i] -= 4;
            }
            else if (oc[i] == 5) {
                score += 2000;
                oc[i] -= 5;
            }
            else if (oc[i] == 6) {
                return 3000;    //Max points
            }
        }
        score += 100 * oc[0];    //Handle Single Ones
        score += 50 * oc[4];    //Handle Single Fives

        //We don't care about Triple Ones, as it adds up to the same points as single ones
        score += 200 * (oc[1] / 3);    //Handle Triple twos
        score += 300 * (oc[2] / 3);    //Handle Triple threes
        score += 400 * (oc[3] / 3);    //Handle Triple fours
        score += 350 * (oc[4] / 3);    //Handle Triple fives	SPECIAL MATH We would already get 150 from the single fives scoring
        score += 600 * (oc[5] / 3);    //Handle Triple sixes

        //FIXME
        return score;
    }

    /**
     * @return ArrayList<Player>
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * @return playerTurn
     */
    public int getPlayerTurn() {
        return playerTurn;
    }

    /**
     * @return runningTotal
     */
    public int getRunningTotal() {
        return runningTotal;
    }

    /**
     * @return score of selected dice
     */
    public int getCurrentSelectionScore() {
        return getScore(selectedDice);
    }

    /**
     * @return freeDice
     */
    public ArrayList<Die> getFreeDice() {
        return freeDice;
    }

    /**
     * @return selectedDice
     */
    public ArrayList<Die> getSelectedDice() {
        return selectedDice;
    }

    /**
     * @return capturedDice
     */
    public ArrayList<Die> getCapturedDice() {
        return capturedDice;
    }

    /**
     * @return active player
     */
    public Player getActivePlayer() {
        return players.get(playerTurn);
    }
}
