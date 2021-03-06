package farkle;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 * FIXME
 * <p>
 * generify and create a class that inherits from this to be the strategy table specifically?
 */
public class DataBase {

    public static Tuple queryStrategyTable(int score, int numDice, boolean fiveHundred) {
        try (Connection Driver = DriverManager.getConnection("jdbc:derby:FarkleDB;create=true");
             Statement s = Driver.createStatement()) {
            ResultSet rs;
            if (fiveHundred) {
                rs = s.executeQuery("SELECT Weight, RollAgain FROM " +
                        "Endgame" + " WHERE Score=" + score + " AND N=" + numDice);
            }
            else {
                rs = s.executeQuery("SELECT Weight, RollAgain FROM " +
                        "Strategy" + " WHERE Score=" + score + " AND N=" + numDice);
            }
            ResultSetMetaData rsmd = rs.getMetaData();

            ArrayList<String> response = new ArrayList<>();
            while (rs.next()) {
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    response.add(rs.getString(i));
                }
            }

            if (response.size() > 0) {
                int weight = Integer.valueOf(response.get(0));
                boolean roll = Boolean.valueOf(response.get(1));
                return new Tuple(weight, roll);
            }
            else {
                //FIXME debugging
                System.out.println("Score: " + score + " numDice: " + numDice + "fiveHundred?: " + fiveHundred);
//                throw new IllegalArgumentException("Score/N Dice pairing is unreachable " +
//                        "under valid Farkle play, please check values");
                return new Tuple(0, false);
            }

        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean createTable(String name, String... columns) {
        try (Connection Driver = DriverManager.getConnection("jdbc:derby:FarkleDB;create=true");
             Statement s = Driver.createStatement()) {

            //build our statement
            StringBuilder statement = new StringBuilder();
            statement.append("CREATE TABLE ");
            statement.append(name);
            statement.append(" (");
            //accommodate varargs
            for (String column : columns) {
                statement.append(column);
                statement.append(", ");
            }

            //hacky way to fix the unnecessary ", " from the for-each loop
            statement.replace(statement.length() - 2, statement.length(), ")");
            s.execute(statement.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean execStatement(String... statements) {
        try (Connection Driver = DriverManager.getConnection("jdbc:derby:FarkleDB;create=true");
             Statement s = Driver.createStatement()) {

            for (String sql : statements) {
                s.execute(sql);
                System.out.println("Executed statement");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
	
	public static boolean doesStrategyExist(){
		return queryStrategyTable(50, 5, false) != null;
	}
	public static boolean doesEndgameExist(){
		return queryStrategyTable(50, 5, true) != null;
	}

    public static boolean createStrategyTable() {
        return createTable("Strategy", "Score INT", "N INT", "Weight INT", "RollAgain BOOLEAN");
    }

    public static boolean createEndgameTable() {
        return createTable("Endgame", "Score INT", "N INT", "Weight INT", "RollAgain BOOLEAN");
    }

    public static boolean fillEndgameTable() {
        StringBuilder sb = new StringBuilder();
        String fillTable;

        try {
            File sql = new File("data/farkle/cleanData2.sql");
            Scanner input = new Scanner(sql);
            while (input.hasNextLine()) {
                sb.append(input.nextLine());
            }

            fillTable = sb.toString();
            input.close();

        } catch (IOException e) {
            fillTable = "NULL";
            e.printStackTrace();
        }

        return execStatement(fillTable);
    }

    public static boolean fillStrategyTable() {
        StringBuilder sb = new StringBuilder();
        String fillTable;

        try {
            File sql = new File("data/farkle/cleanData.sql");
            Scanner input = new Scanner(sql);
            while (input.hasNextLine()) {
                sb.append(input.nextLine());
            }

            fillTable = sb.toString();
            input.close();

        } catch (IOException e) {
            fillTable = "NULL";
            e.printStackTrace();
        }

        return execStatement(fillTable);
    }

    public static boolean dropTable(String table) {
        try (Connection Driver = DriverManager.getConnection("jdbc:derby:FarkleDB;create=true");
             Statement s = Driver.createStatement()) {
            s.execute("Drop Table " + table);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean dropEndgameTable() {
        return dropTable("Endgame");
    }

    public static boolean dropStrategyTable() {
        return dropTable("Strategy");
    }

    /**
     * testing
     */
    public static void main(String[] args) {
        dropStrategyTable();
        createStrategyTable();
        fillStrategyTable();

        Tuple response = queryStrategyTable(200, 4, false);
        System.out.println(Objects.requireNonNull(response).weight);
        System.out.println(response.roll);
    }
}
