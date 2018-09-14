package farkle;

import java.util.ArrayList;

/**
 * @author alec mills
 * <p>
 * handles AI-controlled player decisions for Farkle
 */
public class SkyNet {
/**
 * TODO JOBS
 * 1. select dice to save, return arraylist
 * 2. decide whether to roll again or end turn, return boolean
 */

    /**
     * determines how risky the AI plays
     */
    private double riskTolerance;

    /**
     * @param risk risk tolerance factor of AI
     */
    private SkyNet(double risk) {
        riskTolerance = risk;
    }

    /**
     * TODO
     * AI chooses what dice to set aside
     *
     * @param freeDice dice available for choosing
     * @return the dice chosen
     */
    public static ArrayList<Die> selectDice(int score, ArrayList<Die> freeDice) {
        return null;
    }

    /**
     * TODO
     * AI chooses whether to roll again or bank points
     *
     * @param score       AI's current accrued score
     * @param numFreeDice how many dice will be rolled, if rolled
     * @return
     */
    public static boolean willRollAgain(int score, int numFreeDice) {
        return false;
    }

    /**
     * TODO
     * returns neighboring states, i.e. reachable from the current roll
     */
    private ArrayList<State> findNeighbors(int score, ArrayList<Die> freeDice) {
        return null;
    }

    /**
     * represents a game state in terms of what is relevant to the AI's decision making
     */
    private class State {
        int score = 0; //score accumulated in turn
        boolean isLastTurn; //are you playing in response to another player reaching 10,000 points
        ArrayList<Die> selectedDice;
        ArrayList<Die> freeDice;

    }
}
