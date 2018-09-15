package dataHandling;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SanitizeData {
    public static void main(String[] args) {
        try {
            File dirtyData = new File("data/farkle/farkle_strategy.csv");
            Scanner input = new Scanner(dirtyData);

            File cleanData = new File("data/farkle/cleanData.sql");
            FileWriter write = new FileWriter(cleanData, true);

            while (input.hasNextLine()) {
                String[] line = input.nextLine().split(",");
                for (int i = 1; i <= 6; i++) {
                    //build correct line / column relationship

                    StringBuilder cell = new StringBuilder();//extract trailing chars from number strings
                    cell.append(line[i]);

                    //save char for later setting of boolean RollAgain value in DB
                    char rollAgain = cell.charAt(cell.length() - 1);
                    cell.deleteCharAt(cell.length() - 1);

                    if (Integer.valueOf(cell.toString()) != 0) {
                        String row = "(" +
                                line[0] +
                                ", " +
                                String.valueOf(Math.abs(7 - i)) +
                                ", " +
                                cell.toString() +
                                ", " +
                                ((rollAgain == 'Y') ? "true" : "false") +
                                "),\n";
                        write.write(row);
                    }
                }
                write.flush();
            }

            write.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
