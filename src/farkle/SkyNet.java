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
    public static ArrayList<Die> selectDice(ArrayList<ArrayList<Die>> options) {
        int[] weights = new int[options.size()];

        int i = 0;
        for (ArrayList<Die> dice : options) {
            int score = PlayState.getScore(dice);
            int numFreeDice = 6 - dice.size();

            weights[i] = DataBase.queryStrategyTable(score, numFreeDice).weight;
            i++;
        }

        return options.get(maxValueIndex(weights));
    }

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
     *
     * @param score
     * @param freeDice
     * @return
     */
    private static ArrayList<ArrayList<Die>> getOptions(int score, ArrayList<Die> freeDice) {
        ArrayList<ArrayList<Die>> options = new ArrayList<>();
        for (int i = 1; i < 64; i++) {
            ArrayList<Die> set = new ArrayList<>();
            String binary = Integer.toString(i, 2);
            for (int j = 0; j < 6; j++) {
                if (binary.charAt(j) == '1') {
                    set.add(freeDice.get(j));
                }
            }
            if (PlayState.verifyHand(set)) {
                options.add(set);
            }
        }
        return options;
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
