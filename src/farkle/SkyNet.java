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
    public static ArrayList<Die> selectDice(ArrayList<ArrayList<Die>> options, PlayState state) {
        int[] weights = new int[options.size()];

        int i = 0;
        for (ArrayList<Die> dice : options) {
            int score = state.getRunningTotal() + state.getScore(dice);
            int numFreeDice = state.getFreeDice().size() - dice.size();

            weights[i] = Objects.requireNonNull(DataBase.queryStrategyTable(score, numFreeDice)).weight;
            i++;
        }

        //FIXME debugging
        System.out.print("Weights: [");
        for (int el : weights) {
            System.out.print(el + " ");
        }
        System.out.println("]");
        int indexChosen = maxValueIndex(weights);
        System.out.println("Index chosen: " + indexChosen);

        ArrayList<Die> choice = options.get(indexChosen);
        System.out.print("Chose: ");
        for (Die die : choice) {
            System.out.print(die.getValue() + " ");
        }
        System.out.println();

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

            ArrayList<Die> freeDice = state.getFreeDice();

            if (freeDice.size() > 0 && state.getScore(freeDice) != 0) {
                ArrayList<ArrayList<Die>> options = getOptions(state);

                System.out.println("Dice: ");
                for (Die die : freeDice) {
                    System.out.println(die.getValue() + " ");
                }
                System.out.println();

                int i = 0;
                for (ArrayList<Die> set : options) {
                    System.out.printf("Option %d:%n", i++);
                    for (Die die : set) {
                        System.out.print(die.getValue() + " ");
                    }
                    System.out.println();
                }

                ArrayList<Die> selection = selectDice(options, state);
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

                state.captureDice();

                if (state.getFreeDice().isEmpty()) {
                    state.nextHand();
                    state.shakeDice();
                    return;
                }

                if (!rollAgain(state.getRunningTotal(), freeDice.size()) && state.getActivePlayer().getScore() != 0 &&
                        state.getRunningTotal() < 500) {
                    state.endTurn();
                    return;
                }

                else
                    state.shakeDice();

            }
    }

    /**
     * finds all legal combinations of scoring dice given a roll
     */
    public static ArrayList<ArrayList<Die>> getOptions(PlayState state) {
        ArrayList<Die> freeDice = state.getFreeDice();
        ArrayList<ArrayList<Die>> options = new ArrayList<>();

        for (int i = 1; i < Math.pow(2, freeDice.size()); i++) {
            ArrayList<Die> set = new ArrayList<>();

            String binary = String.format("%6s", Integer.toBinaryString(i)).replace(' ', '0');
            //FIXME debugging
            System.out.println(binary);
            binary = new StringBuilder(binary).reverse().toString();
            for (int j = 0; j < freeDice.size(); j++) {
                if (binary.charAt(j) == '1') {
                    set.add(freeDice.get(j));
                }
            }
            if (state.getScore(set) != 0 && state.verifyHand(set)) {
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
}
