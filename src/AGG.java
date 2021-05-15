import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class AGG {

    private double[][][] dataTable;
    private ArrayList<int[]> posToRandomize = new ArrayList<>();

    public AGG(String path) {

        String firstDM = copyFirstDM(path);
        int[] dimensions = countRowsAndCols(firstDM);

        initAGG(dimensions[0], dimensions[1]);
        csvToAGG(path);

        initPositionsToRandomizeAt();

        //only for testing
        System.out.println("AGG_Table: ");
        for (double[][] rows : dataTable) {
            System.out.println(Arrays.deepToString(rows));
        }
        System.out.println("posToRandomize: ");
        System.out.println(Arrays.deepToString(posToRandomize.toArray()));

    }

    public double[][][] getAGG() {
        return dataTable;
    }
    public ArrayList<int[]> getRandPositions() {
        return posToRandomize;
    }



    /**
     * <ul>
     *     <li>read through and count all c's for rows and a's for columns</li>
     *     <li>necessary to get dimensions for AGG-dataTable</li>
     * </ul>
     */
    private static int[] countRowsAndCols(String data) {
        //minus 1 wegen "Gewichte" -> ist ein c drin
        int rows = countCharTarget(data, 'c') - 1;
        int columns = countCharTarget(data, 'a') + 1;

        return new int[]{rows, columns};
    }

    /**
     * copies dataTable of first Decision Maker.
     * Method is used to be able to count Rows and Cols
     */
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
     * initializes AGG with given dimensions and default values
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
     * creates dataTable, defines posToRandomize
     * @param path path to .csv file which shall be analysed
     */
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
        int length = strArray.length;
        double[] doubleArray = new double[length];
        for (int i = 0; i < length; i++) {
            //parse String to double Array
            doubleArray[i] = Double.parseDouble(strArray[i]);
        }
        return doubleArray;
    }

    /**
     * inserts a double array into the AGG-dataTable
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
            } else {
                //case: one value so far -> new Interval if different
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

    /**
     * inits class-variable PositionsToRandomizeAt, which saves all indices where values shall be randoized
     */
    private void initPositionsToRandomizeAt() {

        int[] index;

        for (int i = 0; i < dataTable.length; i++) {
            for (int j = 0; j < dataTable[0].length; j++) {
                //10 only when using new insert method!!!
                if (dataTable[i][j][0] == -10 || dataTable[i][j][0] == 10) {
                    index = new int[2];
                    index[0] = i;
                    index[1] = j;
                    posToRandomize.add(index);
                }
            }
        }
    }



}
