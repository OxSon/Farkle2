package farkle;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Alec Mills and Josh DeMoss
 * <p>
 * handles AI-controlled player decisions for Farkle
 */
public class SkyNet {

    /**
     * handles AI's turn, dice choices, roll choices, etc
     *
     * @param state the game-state at time of turn-start
     */
    public static void takeTurn(PlayState state) {
        ArrayList<Die> freeDice = state.getFreeDice();

        //possible combinations of dice the AI could select
        ArrayList<ArrayList<Die>> options = getOptions(state);
        //combination AI selected
        ArrayList<Die> selection = selectDice(options, state);

        //update GUI
        state.setSelectedDice(selection);
        state.addRunningTotal(state.getCurrentSelectionScore());
        state.captureDice();

        //if AI used all the dice, give it another hand
        if (state.getFreeDice().isEmpty()) {
            state.nextHand();
        }

        //AI uses a different look-up table depending on whether it has a minimum value necessary to bank
        boolean onScoreBoard = state.getActivePlayer().getScore() > 0;
        if (!rollAgain(state.getRunningTotal(), freeDice.size(), onScoreBoard))
            state.endTurn();
        else
            state.shakeDice();
    }

    /**
     * finds all legal combinations of scoring dice given a roll
     *
     * @param state game-state at time of turn start
     * @return a list of legal combinations of dice the AI could choose
     */
    private static ArrayList<ArrayList<Die>> getOptions(PlayState state) {
        ArrayList<Die> freeDice = state.getFreeDice();
        ArrayList<ArrayList<Die>> options = new ArrayList<>();

        //given a set of N objects, there are 2^N combinations; here we ignore the empty set option
        for (int i = 1; i < Math.pow(2, freeDice.size()); i++) {
            ArrayList<Die> set = new ArrayList<>();

            //use a binary string as a bit field
            String binary = String.format("%6s", Integer.toBinaryString(i)).replace(' ', '0');
            //format with trailing zeroes rather than leading
            binary = new StringBuilder(binary).reverse().toString();

            for (int j = 0; j < freeDice.size(); j++) {
                if (binary.charAt(j) == '1') {
                    set.add(freeDice.get(j));
                }
            }

            //if the set is worth points and a legal combination, add it to our options
            if (state.getScore(set) != 0 && state.verifyHand(set)) {
                options.add(set);
            }
        }
        return options;
    }

    /**
     * AI chooses what dice to set aside
     *
     * @param options dice-combinations to consider
     * @return the dice chosen
     */
    private static ArrayList<Die> selectDice(ArrayList<ArrayList<Die>> options, PlayState state) {
        //weights are the 'desireability' of any given choice the AI could make
        int[] weights = new int[options.size()];

        int i = 0;
        for (ArrayList<Die> dice : options) {
            //what AI's running total would be if it made the choice represented by dice
            int score = state.getRunningTotal() + state.getScore(dice);
            //how many freeDice would be left over if the AI made this choice
            int numFreeDice = state.getFreeDice().size() - dice.size();
            //if the choice would consume all the dice, account for the new hand
            if (numFreeDice == 0) {
                numFreeDice = 6;
            }

            //has the player successfully banked a turn of 500 or more
            boolean onScoreBoard = state.getActivePlayer().getScore() > 0;
            weights[i] = Objects.requireNonNull(DataBase.queryStrategyTable(score, numFreeDice, onScoreBoard)).weight;
            i++;
        }

        //index of largest value in weights[]
        int indexChosen = maxValueIndex(weights);
        return options.get(indexChosen);
    }

    /**
     * finds highest value in an int[]
     *
     * @param values the int[] to search
     * @return index of highest value
     */
    private static int maxValueIndex(int[] values) {
        int max = Integer.MIN_VALUE;
        int index = 0;

        for (int i = 0; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
                index = i;
            }
        }

        return index;
    }

    /**
     * AI chooses whether to roll again or bank points
     *
     * @param score       AI's current accrued score
     * @param numFreeDice how many dice will be rolled, if rolled
     * @return boolean
     */
    private static boolean rollAgain(int score, int numFreeDice, boolean fiveHundred) {
        Tuple response = DataBase.queryStrategyTable(score, numFreeDice, fiveHundred);
        return Objects.requireNonNull(response).roll;
    }
}
