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
        //draw the frame of the GUI
        GUI.draw(g, this);

        //draw the dice that haven't been selected
        for (Die aFreeDice : freeDice) {
            aFreeDice.draw(g);
        }

        //draw a circle around the dice that have been selected
        for (Die aSelectedDice : selectedDice) {
            g.setColor(Color.RED);
            g.fillOval(
                    (int) aSelectedDice.getPosition().getX() - (int) Die.HYPOT / 2,
                    (int) aSelectedDice.getPosition().getY() - (int) Die.HYPOT / 2,
                    (int) Die.HYPOT,
                    (int) Die.HYPOT);
            aSelectedDice.draw(g);    //draw the dice on top of the selection circle
        }
    }

    private void handleKeys() {
        if (Input.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            controller.pop();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (players.get(playerTurn).isAI()) {    //Don't allow any mouse presses while the AI is taking it's turn
            return;
        }
        if (!rolling) {
            if (e.getButton() == MouseEvent.BUTTON1 && !turnStart) {
                //FIXME code duplication, extract method?
                for (int i = 0; i < freeDice.size(); i++) {
                    if (Math.sqrt(Math.pow(freeDice.get(i).getPosition().getX() - e.getX(), 2) + Math.pow(freeDice.get(i).getPosition().getY() - e.getY(), 2)) < Die.HYPOT / 2) {
                        selectedDice.add(freeDice.get(i));
                        freeDice.remove(i);
                        return;
                    }
                }
                for (int i = 0; i < selectedDice.size(); i++) {
                    if (Math.sqrt(Math.pow(selectedDice.get(i).getPosition().getX() - e.getX(), 2) + Math.pow(selectedDice.get(i).getPosition().getY() - e.getY(), 2)) < Die.HYPOT / 2) {
                        freeDice.add(selectedDice.get(i));
                        selectedDice.remove(i);
                        return;
                    }
                }
            }
            else if (e.getButton() == MouseEvent.BUTTON2) {
                if (players.get(playerTurn).getScore() == 0) {
                    //if (runningTotal + getScore(selectedDice) < 500) {
                    //	return;
                    //}
                }
                runningTotal += getScore(selectedDice);
                endTurn();
            }
            else if (e.getButton() == MouseEvent.BUTTON3) {
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
                    }
                    else {
                        shakeDice();
                    }
                }
            }
        }
    }

    @Override
    public void update() {
        handleKeys();        //Handle any input from the keyboard
        if (rolling) {        //If there are dice rolling
            for (int i = 0; i < freeDice.size(); i++) {        //update all the dice
                freeDice.get(i).update();
            }
            rolling = false;    //Set rolling to false

            for (int i = 0; i < freeDice.size(); i++) {        //If any dice are still rolling, set rolling back to true
                if (!freeDice.get(i).isStopped()) {
                    rolling = true;
                    break;
                }
            }
            if (!rolling && getScore(freeDice) == 0) {    //If it was rolling, and now is not and the hand is worthless WE HAVE A FARKLE LADIES AND GENTLEMAN
                GUI.notify("FARKLE");
                runningTotal = 0;
                endTurn();
            }
        }
        else if (players.get(playerTurn).isAI()) {
            if (turnStart) {
                turnStart = false;
                shakeDice();
                return;
            }

            //TODO THIS IS WHERE WE INTERFACE WITH AN AI CLASS
            boolean solved = false;
            System.out.println(capturedDice.size() + " " + selectedDice + " " + freeDice);
            while (!solved) {
                System.out.println(capturedDice.size() + " " + selectedDice + " " + freeDice);
                int num = (int) (Math.random() * freeDice.size()) + 1;
                for (int i = 0; i < num; i++) {
                    selectedDice.add(freeDice.remove((int) (Math.random() * freeDice.size())));
                }
                if (!verifyHand(selectedDice)) {

                    freeDice.addAll(selectedDice);
                    selectedDice.clear();
                }
                if (getScore(selectedDice) == 0) {
                    freeDice.addAll(selectedDice);
                    selectedDice.clear();
                }
                else {
                    System.out.println(capturedDice.size() + " " + selectedDice + " " + freeDice);
                    runningTotal += getScore(selectedDice);
                    capturedDice.addAll(selectedDice);
                    selectedDice.clear();
                    for (int i = 0; i < capturedDice.size(); i++) {
                        capturedDice.get(i).setAngle(0);
                        capturedDice.get(i).setPosition(new Vector2(100 + i * 150, Renderer.WindowHeight - GUI.BOTTOMPANELSIZE + 100));
                    }
                    if (runningTotal < 500) {
                        shakeDice();
                    }
                    else {
                        endTurn();
                    }
                    solved = true;
                }
            }
        }
    }

    private void shakeDice() {
        rolling = true;
        for (int i = 0; i < freeDice.size(); i++) {
            freeDice.get(i).shake(100);
        }
    }

    public void resetHand() {
        freeDice.clear();
        selectedDice.clear();
        capturedDice.clear();
        for (int i = 0; i < NUMOFDICE; i++) {
            freeDice.add(new Die(
                    new Vector2(((double) i + 1d) / ((double) NUMOFDICE + 1d) * (Renderer.WindowWidth - GUI.RIGHTPANELSIZE), (Renderer.WindowHeight - GUI.BOTTOMPANELSIZE) / 2),
                    new Rectangle(0, 0, Renderer.WindowWidth - GUI.RIGHTPANELSIZE, Renderer.WindowHeight - GUI.BOTTOMPANELSIZE)));
        }
    }

    private void nextHand() {
        System.out.println("Player " + players.get(playerTurn).getName() + " earned " + runningTotal + " Points");
        players.get(playerTurn).finishTurn(runningTotal);        //Give the player the points they earned
        runningTotal = 0;
        turnStart = true;
        resetHand();
    }

    private void endTurn() {
        nextHand();
        playerTurn = (playerTurn + 1) % players.size();
    }

    //FIXME test
    public static boolean verifyHand(ArrayList<Die> Dice) {
        //build record of how many of each die-value we have
        int[] oc = new int[Die.MAXVALUE];
        for (int i = 0; i < Dice.size(); i++) {
            oc[Dice.get(i).getValue() - 1]++;
        }
        //test failure conditions
        for (int i = 0; i < Dice.size(); i++) {
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
        int[] oc = new int[Die.MAXVALUE];

        for (int i = 0; i < dice.size(); i++) {
            oc[dice.get(i).getValue() - 1]++;
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
        for (int i = 0; i < oc.length; i++) {
            if (oc[i] == 4) {
                score += 1000;
            }
            if (oc[i] == 5) {
                score += 2000;
            }
            if (oc[i] == 6) {
                score += 3000;
            }
        }
        //STRAIGHT CHECK
        int count = 0;
        for (int i = 0; i < oc.length; i++) {
            if (oc[i] == 1) {
                count++;
            }
        }
        if (count == 6) {
            return 1500;        //Special case, this will use the whole roll, leaving no other points available
        }
        //ONLY PAIRS CHECK
        count = 0;
        for (int i = 0; i < oc.length; i++) {
            if (oc[i] == 2) {
                count++;
            }
        }
        if (count == 3) {        //If we found 3 pairs
            return 1500;        //Special case, this will use the whole roll, leaving no other points available
        }
        //FULL HOUSE CHECK
        count = 0;
        for (int i = 0; i < oc.length; i++) {
            if (oc[i] == 4) {
                count += 3;
            }
            if (oc[i] == 2) {
                count++;
            }
            if (count == 4) {
                return 1500;        //Special case, this will use the whole roll, leaving no other points available
            }
        }
        //ONLY TRIPLETS CHECK
        count = 0;
        for (int i = 0; i < oc.length; i++) {
            if (oc[i] == 3) {
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

    public ArrayList<Die> getCapturedDice() {
        return capturedDice;
    }
}
