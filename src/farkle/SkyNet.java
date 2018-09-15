package farkle;

import java.util.ArrayList;
import java.util.Objects;

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

            weights[i] = Objects.requireNonNull(DataBase.queryStrategyTable(score, numFreeDice)).weight;
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

    public static void takeTurn(PlayState state) {
        boolean done = false;
        state.clearSelectedDice();
        state.clearCapturedDice();

        while (!done) {
            ArrayList<Die> freeDice = state.getFreeDice();

            if (freeDice.size() > 0 && PlayState.getScore(freeDice) != 0) {
                ArrayList<ArrayList<Die>> options = getOptions(freeDice);

                ArrayList<Die> selection = selectDice(options);
                state.setSelectedDice(selection);
                state.addRunningTotal(state.getCurrentSelectionScore());
                //FIXME debugging
                System.out.println("Free dice: " + freeDice.size());
                System.out.println("Skynet selected: ");
                for (Die die : selection) {
                    System.out.print(die.getValue() + " ");
                }
                System.out.println();
                System.out.printf("Selection is worth %d points%n", state.getCurrentSelectionScore());

                state.setCapturedDice(selection);

                if (!rollAgain(state.getRunningTotal(), freeDice.size())) {
                    state.endTurn();
                    return;
                }

                state.clearSelectedDice();
                state.clearCapturedDice();
                state.shakeDice();
            }
            //FIXME how should this be handled
            else if (freeDice.size() == 0) {
                state.clearAllDice();
                state.nextHand();
                GUI.notify("EXTRA HAND");
            }
            else {
                GUI.notify("FARKLE");
                state.shakeDice();
                state.endTurn();
                return;
            }
        }

        return;
    }

    /**
     * finds all legal combinations of scoring dice given a roll
     * @param freeDice the dice that were rolled
     * @return an arraylist of arraylists of dice
     */
    public static ArrayList<ArrayList<Die>> getOptions(ArrayList<Die> freeDice) {
        ArrayList<ArrayList<Die>> options = new ArrayList<>();
        for (int i = 1; i < 64; i++) {
            ArrayList<Die> set = new ArrayList<>();
            //FIXME needs to include leading zeros
            String binary = String.format("%6s", Integer.toBinaryString(i)).replace(' ', '0');
            for (int j = 0; j < freeDice.size(); j++) {
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
        return Objects.requireNonNull(response).roll;
    }

    /**
     * testing
     *
     * @param args
     */
    public static void main(String[] args) {
        ArrayList<Die> freeDice = new ArrayList<>();
        freeDice.add(new Die(3));
        freeDice.add(new Die(6));
        freeDice.add(new Die(6));
        freeDice.add(new Die(6));
        freeDice.add(new Die(5));
        freeDice.add(new Die(1));

        ArrayList<ArrayList<Die>> options = getOptions(freeDice);
        for (ArrayList<Die> combo : options) {
            for (Die die : combo) {
                System.out.println(die.getValue());
            }
            System.out.println();
        }
    }
}
