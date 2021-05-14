import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class AGG {

    private double[][][] table;
    private ArrayList<int[]> positionsToRandomizeAt = new ArrayList<>();

    public double[][][] getAGG() {
        return table;
    }

    public AGG(String path) {

        String firstDM = copyFirstDM(path);
        int[] dimensions = countRowsAndCols(firstDM);

        initAGG(dimensions[0], dimensions[1]);
        csvToAGG(path);

        initPositionsToRandomizeAt();

        //only for testing
        System.out.println("AGG_Table: ");
        for (double[][] rows : table) {
            System.out.println(Arrays.deepToString(rows));
        }
        System.out.println("positionsToRandomizeAt: ");
        System.out.println(Arrays.deepToString(positionsToRandomizeAt.toArray()));

    }



    /**
     * read through and count all c's for rows and a's for columns
     * necessary to get dimenions for AGG-table
     * **/
    private static int[] countRowsAndCols(String data) {
        int rows = countCharTarget(data, 'c') - 1;                            //minus 1 wegen "Gewichte" -> ist ein c drin
        int columns = countCharTarget(data, 'a') + 1;

        return new int[]{rows, columns};
    }

    /**
     * copies table of first Decision Maker.
     * Method is used to be able to count Rows and Cols
     * **/
    private static String copyFirstDM(String path) {

        String target = "DM";
        int counter = 0;

        StringBuilder data = new StringBuilder();

        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (counter <= 1 && myReader.hasNextLine()) {
                String t = myReader.nextLine();

                data.append(t);

                if (t.contains(target)) {
                    counter++;
                }
            }

            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found! Check input path.");
            e.printStackTrace();
        }

        return data.toString();
    }

    /**
     * counts Occurences of a given Target in a String
     * @param data String to search Target in
     * @param target What to search for
     * @return Number of Occurences of target in data
     */
    private static int countCharTarget(String data, char target) {
        int counter = 0;

        for (int i = 0; i < data.length(); i++) {
            if (target == data.charAt(i)) {
                counter++;
            }
        }

        return counter;
    }

    /**
     * initializes AGG with given dimensions and default values
     * @param rows
     * @param cols
     */
    private void initAGG(int rows, int cols) {
        //inits each cell with [-10,0,1]
        table = new double[rows][cols][3];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                table[i][j][0] = -10.0;
                table[i][j][1] = 0.0;
                table[i][j][2] = 1.0;

            }
        }
    }

    /**
     * creates table, defines positionsToRandomizeAt
     * @param path path to .csv file which shall be analysed
     **/
    private void csvToAGG(String path) {
        int currow = 0;


        //read all lines into rows, skip DM rows + empty lines
        try {
            File file = new File(path);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String currentLine = reader.nextLine();

                if (currentLine.contains("c") && !currentLine.contains("G") && !currentLine.contains("|")) { //case: line contains relevant values
                    //rows.add(currentLine);

                    String modified = modifyString(currentLine);
                    double[] doubleArray = stringToDoubleArray(modified);
                    //System.out.println(Arrays.toString(doubleArray));
                    //insertArrayInAGG(doubleArray, currow);
                    /**!!!!!!!!!!!**/
                    insertArrayIntoAGG(doubleArray, currow);

                    currow++;

                } else if (currentLine.contains("DM")) { //case:DM, ignore
                    currow = 0;
                }
                //else: Gewichte or empty line -> ignore
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found! Check input path.");
            e.printStackTrace();
        }
    }

    /**
     * filters all values out of the given line and replaces missing values with a default value (-1)
     * @param currentLine
     * @return string, format(example): ;1.0;0.5;-1.0;
     */
    private String modifyString(String currentLine) {

        //delete c1 - cn
        String modified = ";" + currentLine.replaceAll("c([0-9]+);", "") + ";"; //semicolons simplify following steps

        //replace missing values with -1
        while (modified.contains(";;")) {
            modified = modified.replaceFirst(";;", ";-1;");
        }

        modified = modified.replace(",", ".");               //doubles with ., not ,

        return modified;
    }


    /**
     * converts formatted String to a double Array
     * @param modified
     * @return 1D double array
     */
    private double[] stringToDoubleArray(String modified) {
        modified = modified.replaceFirst(";", "");
        String[] strArray = modified.split(";");                            //values are seperated by ;
        int length = strArray.length;
        double[] doubleArray = new double[length];
        for (int i = 0; i < length; i++) {
            doubleArray[i] = Double.parseDouble(strArray[i]);                       //parse String to double Array
        }
        return doubleArray;
    }

    /**
     * inserts a double array into the AGG-table
     * @param values double array
     * @param rowIndex Index to insert at
     **/
    private void insertArrayIntoAGG(double[] values, int rowIndex) {
        // -10 = no value, 10 = multiple values, other = one value
        for (int i = 0; i < values.length; i++) {

            double currValue = values[i];

            //-1 = missing value, is ignored
            if (currValue == -1.0)
                continue;

            //case: no value yet
            if (table[rowIndex][i][0] == -10.0) {
                table[rowIndex][i][0] = currValue;
            }
            //case: insert in existing Interval
            else if (table[rowIndex][i][0] == 10.0) {
                if (currValue < table[rowIndex][i][1])
                    table[rowIndex][i][1] = currValue;
                if (currValue > table[rowIndex][i][2])
                    table[rowIndex][i][2] = currValue;
            } else {
                //case: one value so far -> new Interval if different
                if (currValue > table[rowIndex][i][0]) {
                    table[rowIndex][i][1] = table[rowIndex][i][0];
                    table[rowIndex][i][2] = currValue;
                    table[rowIndex][i][0] = 10;
                } else if (currValue < table[rowIndex][i][0]) {
                    table[rowIndex][i][1] = currValue;
                    table[rowIndex][i][2] = table[rowIndex][i][0];
                    table[rowIndex][i][0] = 10;
                }
            }
        }
    }

    /**
     * inits class-variable PositionsToRandomizeAt, which saves all indices where values shall be randoized
     */
    private void initPositionsToRandomizeAt() {

        int[] index;

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                if (table[i][j][0] == -10 || table[i][j][0] == 10) { //10 only when using new insert method!!!
                    index = new int[2];
                    index[0] = i;
                    index[1] = j;
                    positionsToRandomizeAt.add(index);
                }
            }
        }
    }

    /**
     * @param numIterations How often Ranking shall be performed
     * @return RAI-Table
     */
    public double[][] calculateRAI(int numIterations) {

        double[][] raiTable = new double[table[1].length - 1][table[1].length - 1];
        // 1.dimension: Alternative, 2.dimension: possible ranks -> save points / percentage per Rank

        //calculate point per alternative per rank
        for (int i = 0; i < numIterations; i++) {
            generateRandomValues();
            int[] rankedAlternatives = calculateRanking();
            for (int rank = 0; rank < rankedAlternatives.length; rank++) {
                raiTable[rankedAlternatives[rank]][rank]++;
            }
        }

        //calculate RAI (in %, 2 decimals after comma)
        for (int a = 0; a < raiTable.length; a++) {
            for (int rank = 0; rank < raiTable.length; rank++) {
                raiTable[a][rank] = Math.round(raiTable[a][rank] / numIterations * 100 * 100) / 100.0;
            }
        }

        return raiTable;
    }

    /**
     * calculates ranking for table
     * @return ranking of alternatives, order: first to last
     */
    private int[] calculateRanking() {

        double[][] ranks = new double[table[1].length - 1][2];                                            //number of alternatives
        double score = 0;

        for (int i = 1; i < table[1].length; i++) {                                                       //traverse columns from left to right (starting with 1, because 0 is for weights)
            for (int j = 0; j < table.length; j++) {                                                      //traverse rows "downwards"
                score += table[j][0][0] * table[j][i][0];
            }

            ranks[i - 1][0] = (double) i - 1;                                                           //score rank with alternative
            ranks[i - 1][1] = Math.round(score * 100.00) / 100.00;                                      //round score up to two decimal points
            score = 0;                                                                                  //reset score for next alternative
        }

        Arrays.sort(ranks, (o1, o2) -> Double.compare(o2[1], o1[1]));                                   //sort for alternatives with highest score (descending)

        int[] retRanks = new int[ranks.length];

        for (int i = 0; i < ranks.length; i++) {
            retRanks[i] = (int) ranks[i][0];
        }

        return retRanks;
    }

    /**
     * generates random values in intervals[min, max] specified by indices in positionsToRandomizeAt
     * and writes generated value into table
     **/
    private void generateRandomValues() {

        for (int[] index : positionsToRandomizeAt) {
            double min = table[index[0]][index[1]][1];
            double max = table[index[0]][index[1]][2];

            //generate random Value in [min, max]
            double randValue = min + Math.random() * (max - min);
            randValue = (double) (Math.round(randValue * 10)) / 10;

            assert (randValue >= min && randValue <= max);

            table[index[0]][index[1]][0] = randValue; //write random Value into table
        }
    }


    /**
     * generates new csv file "output.csv" and writes rai values into it
     **/
    public static void raiTableToCSV(double[][] array) {

        try {
            FileWriter file = new FileWriter("output.csv");//creates new file
            PrintWriter writer = new PrintWriter(file);

            writer.println("Calculated RAIs: ");
            writer.println();

            //first row
            writer.print("Rank;");
            for (int i = 0; i < array[0].length; i++) {
                if (i < array[0].length - 1)
                    writer.print("a" + (i + 1) + ";");
                else
                    writer.print("a" + (i + 1));
            }

            //print all rai values per rank
            for (int i = 0; i < array[0].length; i++) {
                writer.println();
                writer.print(i + 1 + ";");
                for (int k = 0; k < array.length; k++) {
                    if (k < array.length - 1)
                        writer.print(String.valueOf(array[i][k]).replace(".", ",") + ";");
                    else
                        writer.print(String.valueOf(array[i][k]).replace(".", ","));
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * only needed for testing atm
     * @param args nothing
     */
    public static void main(String[] args) {
        String pathVincent = "C:/UNI/04_Semester/ex_missing_values.csv";
        String pathMarten = "C:/Users/admin/Downloads/scenario_1.csv";
        String pathEdgar = "/Users/edgar/Documents/4 Semester/Softwareprojekt/my-swp-example.csv";

        String path = pathMarten;
        AGG agg = new AGG(path);

        double[][] raiTable = agg.calculateRAI(100000);
        System.out.println("RAI-Table: ");
        System.out.println(Arrays.deepToString(raiTable));


        raiTableToCSV(raiTable);
    }

}
