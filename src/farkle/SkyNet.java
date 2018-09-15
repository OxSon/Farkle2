package farkle;

import java.util.ArrayList;

/**
 * @author alec mills
 * <p>
 * handles AI-controlled player decisions for Farkle
 */
public class SkyNet {

    /**
     * AI chooses what dice to set aside
     *
     * @param options dice-combinations to consider
     * @return the dice chosen
     */
    public static ArrayList<Die> selectDice(ArrayList<Die[]> options) {
        return null;
    }


    /**
     * TODO what is best DS for this? I need a grow-able list of objects which are themselves lists/collections
     * returns optional choices, i.e. reachable from the current roll
     */
    private static ArrayList<Die[]> findNeighbors(int score, ArrayList<Die> freeDice) {
        return null;
    }


    /**
     * AI chooses whether to roll again or bank points
     *
     * @param score       AI's current accrued score
     * @param numFreeDice how many dice will be rolled, if rolled
     * @return boolean
     */
    public static boolean rollAgain(int score, int numFreeDice) {
        Tuple response = DataBase.queryStrategyTable(score, numFreeDice);
        return response.roll;
    }
}
