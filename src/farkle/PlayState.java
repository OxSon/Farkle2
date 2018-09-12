package farkle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PlayState extends GameState {

    public static final int NUMOFDICE = 6;

    private final ArrayList<Die> FreeDice;
    private final ArrayList<Die> SelectedDice;
    private final ArrayList<Die> CapturedDice;

    private final ArrayList<Player> Players;
    private int PlayerTurn;

    private int RunningTotal;
    private boolean TurnStart = true;
    private boolean Rolling = false;    //This will say if you should change the face of the dice during it's updates


    public PlayState(Renderer Render, StateManager Controller) {
        super(Render, Controller);
        Players = new ArrayList<>();
        FreeDice = new ArrayList<>();
        SelectedDice = new ArrayList<>();
        CapturedDice = new ArrayList<>();
        for (int i = 0; i < NUMOFDICE; i++) {
            FreeDice.add(new Die(
                    new Vector2(((double) i + 1d) / ((double) NUMOFDICE + 1d) * (Renderer.WindowWidth - GUI.RIGHTPANELSIZE), (Renderer.WindowHeight - GUI.BOTTOMPANELSIZE) / 2),
                    new Rectangle(0, 0, Renderer.WindowWidth - GUI.RIGHTPANELSIZE, Renderer.WindowHeight - GUI.BOTTOMPANELSIZE)));
        }
        Players.add(new Player("Josh"));
        Players.add(new Player("Skynet", true));
    }

    @Override
    public void Draw(Graphics g) {
        GUI.Draw(g, this);        //Draw the GUI
        for (Die aFreeDice : FreeDice) {    //Draw the free dice
            aFreeDice.draw(g);
        }
        for (int i = 0; i < SelectedDice.size(); i++) {
            g.setColor(Color.RED);
            g.fillOval( //Make a circle around the dice, to indicate it has been selected
                    (int) SelectedDice.get(i).getPosition().GetX() - (int) Die.HYPOT / 2,
                    (int) SelectedDice.get(i).getPosition().GetY() - (int) Die.HYPOT / 2,
                    (int) Die.HYPOT,
                    (int) Die.HYPOT);
            SelectedDice.get(i).draw(g);    //Draw the dice on top of the selection circle
        }
    }

    private void HandleKeys() {
        if (Input.IsKeyPressed(KeyEvent.VK_ESCAPE)) {
            Controller.Pop();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (Players.get(PlayerTurn).isAI()) {    //Don't allow any mouse presses while the AI is taking it's turn
            return;
        }
        if (!Rolling) {
            if (e.getButton() == MouseEvent.BUTTON1 && !TurnStart) {
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
            else if (e.getButton() == MouseEvent.BUTTON2) {
                if (Players.get(PlayerTurn).getScore() == 0) {
                    //if (RunningTotal + getScore(SelectedDice) < 500) {
                    //	return;
                    //}
                }
                RunningTotal += getScore(SelectedDice);
                endTurn();
            }
            else if (e.getButton() == MouseEvent.BUTTON3) {
                if (TurnStart) {        //If this is the first roll of a hand
                    TurnStart = false;
                    ShakeDice();
                }
                if (getScore(SelectedDice) != 0 && verifyHand(SelectedDice)) {
                    RunningTotal += getScore(SelectedDice);
                    CapturedDice.addAll(SelectedDice);
                    SelectedDice.clear();
                    for (int i = 0; i < CapturedDice.size(); i++) {
                        CapturedDice.get(i).setAngle(0);
                        CapturedDice.get(i).setPosition(new Vector2(100 + i * 150, Renderer.WindowHeight - GUI.BOTTOMPANELSIZE + 100));
                    }
                    if (FreeDice.isEmpty()) {
                        NextHand();
                        GUI.notify("Extra Hand!");
                    }
                    else {
                        ShakeDice();
                    }
                }
            }
        }
    }

    @Override
    public void Update() {
        HandleKeys();        //Handle any input from the keyboard
        if (Rolling) {        //If there are dice rolling
            for (int i = 0; i < FreeDice.size(); i++) {        //Update all the dice
                FreeDice.get(i).update();
            }
            Rolling = false;    //Set rolling to false
            for (int i = 0; i < FreeDice.size(); i++) {        //If any dice are still rolling, set rolling back to true
                if (!FreeDice.get(i).isStopped()) {
                    Rolling = true;
                    break;
                }
            }
            if (!Rolling && getScore(FreeDice) == 0) {    //If it was Rolling, and now is not and the hand is worthless WE HAVE A FARKLE LADIES AND GENTLEMAN
                GUI.notify("FARKLE");
                RunningTotal = 0;
                endTurn();
            }
        }
        else if (Players.get(PlayerTurn).isAI()) {
            if (TurnStart) {
                TurnStart = false;
                ShakeDice();
                return;
            }
            //THIS IS WHERE WE INTERFACE WITH AN AI CLASS TODO IMPLEMENT
            boolean solved = false;
            System.out.println(CapturedDice.size() + " " + SelectedDice + " " + FreeDice);
            while (!solved) {
                System.out.println(CapturedDice.size() + " " + SelectedDice + " " + FreeDice);
                int num = (int) (Math.random() * FreeDice.size()) + 1;
                for (int i = 0; i < num; i++) {
                    SelectedDice.add(FreeDice.remove((int) (Math.random() * FreeDice.size())));
                }
                if (!verifyHand(SelectedDice)) {
                    //FIXME debugging
                    System.out.println("verifyHand check failed");

                    FreeDice.addAll(SelectedDice);
                    SelectedDice.clear();
                }
                if (getScore(SelectedDice) == 0) {
                    FreeDice.addAll(SelectedDice);
                    SelectedDice.clear();
                }
                else {
                    System.out.println(CapturedDice.size() + " " + SelectedDice + " " + FreeDice);
                    RunningTotal += getScore(SelectedDice);
                    CapturedDice.addAll(SelectedDice);
                    SelectedDice.clear();
                    for (int i = 0; i < CapturedDice.size(); i++) {
                        CapturedDice.get(i).setAngle(0);
                        CapturedDice.get(i).setPosition(new Vector2(100 + i * 150, Renderer.WindowHeight - GUI.BOTTOMPANELSIZE + 100));
                    }
                    if (RunningTotal < 500) {
                        ShakeDice();
                    }
                    else {
                        endTurn();
                    }
                    solved = true;
                }
            }
        }
    }

    private void ShakeDice() {
        Rolling = true;
        for (int i = 0; i < FreeDice.size(); i++) {
            FreeDice.get(i).shake(100);
        }
    }

    public void resetHand() {
        FreeDice.clear();
        SelectedDice.clear();
        CapturedDice.clear();
        for (int i = 0; i < NUMOFDICE; i++) {
            FreeDice.add(new Die(
                    new Vector2(((double) i + 1d) / ((double) NUMOFDICE + 1d) * (Renderer.WindowWidth - GUI.RIGHTPANELSIZE), (Renderer.WindowHeight - GUI.BOTTOMPANELSIZE) / 2),
                    new Rectangle(0, 0, Renderer.WindowWidth - GUI.RIGHTPANELSIZE, Renderer.WindowHeight - GUI.BOTTOMPANELSIZE)));
        }
    }

    private void NextHand() {
        System.out.println("Player " + Players.get(PlayerTurn).getName() + " earned " + RunningTotal + " Points");
        Players.get(PlayerTurn).FinishTurn(RunningTotal);        //Give the player the points they earned
        RunningTotal = 0;
        TurnStart = true;
        resetHand();
    }

    private void endTurn() {
        NextHand();
        PlayerTurn = (PlayerTurn + 1) % Players.size();
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
                //that involve using all 6 dice
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
            return 1500;        //Special case, this will use the whole roll, leaving no other points avalible
        }
        //ONLY PAIRS CHECK
        count = 0;
        for (int i = 0; i < oc.length; i++) {
            if (oc[i] == 2) {
                count++;
            }
        }
        if (count == 3) {        //If we found 3 pairs
            return 1500;        //Special case, this will use the whole roll, leaving no other points avalible
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
                return 1500;        //Special case, this will use the whole roll, leaving no other points avalible
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
            return 2500;        //Special case, this will use the whole roll, leaving no other points avalible
        }

        return score;
    }

    public ArrayList<Player> getPlayers() {
        return Players;
    }

    public int getPlayerTurn() {
        return PlayerTurn;
    }

    public int getRunningTotal() {
        return RunningTotal;
    }

    public int getCurrentSelectionScore() {
        return getScore(SelectedDice);
    }

    public ArrayList<Die> getCapturedDice() {
        return CapturedDice;
    }
}
