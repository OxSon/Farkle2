package dataHandling;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SanitizeData {
    public static void main(String[] args) {
        try {
            File dirtyData = new File("data/farkle/farkle_strategy2.csv");
            Scanner input = new Scanner(dirtyData);

            File cleanData = new File("data/farkle/cleanData2.sql");
            FileWriter write = new FileWriter(cleanData, true);

            while (input.hasNextLine()) {
                String line = input.nextLine();
                System.out.println(line);

                String[] values = line.split(",");
                System.out.println();
                for (int i = 1; i <= 6; i++) {
                    //build correct line / column relationship

                    StringBuilder cell = new StringBuilder();//extract trailing chars from number strings
                    cell.append(values[i]);

                    //save char for later setting of boolean RollAgain value in DB
                    char rollAgain = cell.charAt(cell.length() - 1);
                    cell.deleteCharAt(cell.length() - 1);

                    if (Integer.valueOf(cell.toString()) != 0) {
                        String row = "(" +
                                values[0] +
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
