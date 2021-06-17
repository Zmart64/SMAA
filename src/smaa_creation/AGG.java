package smaa_creation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Provides methods to create an AGG-table from a given .csv-file
 */
public class AGG {

    private double[][][] dataTable;
    private final ArrayList<int[]> posToRandomizeAt = new ArrayList<>();

    public AGG(String path) {

        String firstDM = copyFirstDmTable(path);
        int[] dimensions = countRowsAndCols(firstDM);

        initAGG(dimensions[0], dimensions[1]);
        csvToAGG(path);

        initPositionsToRandomizeAt();
    }

    public double[][][] getAGG() {
        return dataTable;
    }

    public ArrayList<int[]> getRandPositions() {
        return posToRandomizeAt;
    }


    /**
     * initializes smaa.AGG with the given dimensions and default values
     */
    private void initAGG(int rows, int cols) {
        //inits each cell with [-10,0,1]
        dataTable = new double[rows][cols][3];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dataTable[i][j][0] = -10.0;
                dataTable[i][j][1] = 0.0;
                dataTable[i][j][2] = 1.0;
            }
        }
    }

    /**
     * creates dataTable, defines posToRandomizeAt
     *
     * @param path path to .csv file which shall be analysed
     */
    private void csvToAGG(String path) {
        int currentRow = 0;

        //read all lines into rows, skip DM rows + empty lines
        try {
            File file = new File(path);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {

                String currentLine = reader.nextLine();

                //case: line contains relevant values
                if (currentLine.contains("c") && !currentLine.contains("G")) {
                    double[] doubleArray = stringToDoubleArray(modifyString(currentLine));
                    insertArrayIntoAGG(doubleArray, currentRow);
                    currentRow++;

                    //case:DM /first row of a table
                } else if (currentLine.contains("DM")) {
                    currentRow = 0;
                }
                //else: Gewichte or empty line -> ignore
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * inits class-variable PositionsToRandomizeAt, which saves all indices where values shall be randomized
     * (first Element in Cell is 10 or -10 if there is an interval)
     */
    private void initPositionsToRandomizeAt() {

        int[] index;

        for (int i = 0; i < dataTable.length; i++) {
            for (int j = 0; j < dataTable[0].length; j++) {
                if (dataTable[i][j][0] == -10 || dataTable[i][j][0] == 10) {
                    index = new int[2];
                    index[0] = i;
                    index[1] = j;
                    posToRandomizeAt.add(index);
                }
            }
        }
    }


    /**
     * read through and count all c's for rows and a's for columns
     * necessary to get dimensions for smaa.AGG-dataTable
     */
    private static int[] countRowsAndCols(String data) {
        //minus 1 wegen Wort "Gewichten" ist ein weiteres c drin
        int rows = countCharTarget(data, 'c') - 1;
        int columns = countCharTarget(data, 'a') + 1;
        return new int[]{rows, columns};
    }

    /**
     * counts Occurences of a given Target in a String
     *
     * @param data   String to search Target in
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
     * copies dataTable of first Decision Maker.
     * Method is used to be able to count Rows and Cols
     */
    private static String copyFirstDmTable(String path) {

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
            e.printStackTrace();
        }

        return data.toString();
    }


    /**
     * filters all values out of the given line and replaces missing values with a default value (-1)
     *
     * @return string, format(example): ;1.0;0.5;-1.0;
     */
    private String modifyString(String currentLine) {

        //delete c1 - cn
        //semicolons simplify following steps
        String modified = ";" + currentLine.replaceAll("c([0-9]+);", "") + ";";

        //replace missing values with -1
        while (modified.contains(";;")) {
            modified = modified.replaceFirst(";;", ";-1;");
        }

        //doubles with ., not ,
        modified = modified.replace(",", ".");

        return modified;
    }

    /**
     * converts formatted String to a double Array
     *
     * @param modified String formatted with method modifyString
     * @return 1D double array
     */
    private double[] stringToDoubleArray(String modified) {
        modified = modified.replaceFirst(";", "");

        //values are seperated by ;
        String[] strArray = modified.split(";");

        //parse and store into doubleArray
        double[] doubleArray = new double[strArray.length];
        for (int i = 0; i < doubleArray.length; i++) {
            doubleArray[i] = Double.parseDouble(strArray[i]);
        }

        return doubleArray;
    }

    /**
     * inserts a double array into the smaa.AGG-dataTable
     *
     * @param values   double array
     * @param rowIndex Index to insert at
     */
    private void insertArrayIntoAGG(double[] values, int rowIndex) {
        // -10 = no value, 10 = multiple values, other = one value
        for (int i = 0; i < values.length; i++) {

            double currValue = values[i];

            //-1 = missing value, is ignored
            if (currValue == -1.0)
                continue;

            //case: no value yet
            if (dataTable[rowIndex][i][0] == -10.0) {
                dataTable[rowIndex][i][0] = currValue;
            }
            //case: insert in existing Interval
            else if (dataTable[rowIndex][i][0] == 10.0) {
                if (currValue < dataTable[rowIndex][i][1])
                    dataTable[rowIndex][i][1] = currValue;
                if (currValue > dataTable[rowIndex][i][2])
                    dataTable[rowIndex][i][2] = currValue;
                //case: one value so far -> new Interval if different
            } else {
                if (currValue > dataTable[rowIndex][i][0]) {
                    dataTable[rowIndex][i][1] = dataTable[rowIndex][i][0];
                    dataTable[rowIndex][i][2] = currValue;
                    dataTable[rowIndex][i][0] = 10;
                } else if (currValue < dataTable[rowIndex][i][0]) {
                    dataTable[rowIndex][i][1] = currValue;
                    dataTable[rowIndex][i][2] = dataTable[rowIndex][i][0];
                    dataTable[rowIndex][i][0] = 10;
                }
            }
        }
    }


}
