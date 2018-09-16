package dataHandling;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SanitizeData {
    /**
     * application entry-point
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            //data to format
            File dirtyData = new File("data/farkle/farkle_strategy2.csv");
            Scanner input = new Scanner(dirtyData);

            //where to store formatted data
            File cleanData = new File("data/farkle/cleanData2.sql");
            FileWriter write = new FileWriter(cleanData, true);

            while (input.hasNextLine()) {
                String line = input.nextLine();
                System.out.println(line);

                String[] values = line.split(",");
                System.out.println();

                //build correct line / column relationship
                for (int i = 1; i <= 6; i++) {
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
