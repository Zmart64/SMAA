import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class AGG {

    private double[][][] agg;
    private ArrayList<int[]> positionsToRandomizeAt = new ArrayList<>();

    public AGG(String Path) {

//        agg = new double[3][3][3];
//        agg[0][0][0] = 0.4;
//        agg[1][0][0] = 0.5;
//        agg[2][0][0] = 0.2;
//        agg[0][1][0] = 0.6;
//        agg[1][1][0] = 0.3;
//        agg[0][2][0] = 0.2;
//        agg[2][2][0] = 0.8;
//        agg[1][2][0] = 0.8;
//        agg[2][1][0] = 0.8;
//        agg[0][0][1] = 0.4;
//        agg[0][0][2] = 0.7;
//        agg[0][1][1] = 0.2;
//        agg[0][1][2] = 0.8;
//        agg[1][1][1] = 0.3;
//        agg[1][1][2] = 0.4;
//        agg[1][0][1] = 0.1;
//        agg[1][0][2] = 0.2;
//        agg[2][1][1] = 0.3;
//        agg[2][1][2] = 0.4;
//        agg[2][0][1] = 0.1;
//        agg[2][0][2] = 0.2;

        String firstDM = copyFirst(Path);
        int[] dimensions = countRowsAndCols(firstDM);
        System.out.println("dimensions: rows: "+ dimensions[0] + "cols: " + dimensions[1]);
        initializeAGG(dimensions[0], dimensions[1]);

        csvToAGG(Path);

        System.out.println("AGG_Table: ");
        System.out.println(Arrays.deepToString(agg));

        initPositionsToRandomizeAt();

        System.out.println("positionsToRandomizeAt: ");
        System.out.println(Arrays.deepToString(positionsToRandomizeAt.toArray()));

    }


    //--------read through and count all c's for rows and a's for columns
    private static int[] countRowsAndCols(String data) {
        int rows = countCharTarget(data, 'c') - 1;                            //minus 1 wegen "Gewichte" -> ist ein c drin
        int columns = countCharTarget(data, 'a') + 1;

        return new int[]{rows, columns};
    }

    //--------Copy first DMEntry, to count Rows and Columns
    private static String copyFirst(String path) {

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

    //--------count occurrence of target
    private static int countCharTarget(String data, char target) {
        int counter = 0;

        for (int i = 0; i < data.length(); i++) {
            if (target == data.charAt(i)) {
                counter++;
            }
        }

        return counter;
    }



    private void initializeAGG(int rows, int cols) {

        agg = new double[rows][cols][3];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < 3; k++) {
                    agg[i][j][k] = -1.0;
                }
            }
        }
    }

    /**
     * csvToArrayList:
     * - creates ArrayList of Type String from csv-File:
     * - each entry represents one line,starting and ending with ";"
     * - everything but values gets removed
     * - missing values are replaced with -1
     * - example format of one line: ;0,4;-1;3;
     * <p>
     * - initializes dmCount and criteriaCount
     **/
    private void csvToAGG(String Path) {
        int currow = 0;


        //read all lines into rows, skip DM rows + empty lines
        try {
            File file = new File(Path);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String currentLine = reader.nextLine();

                if (currentLine.contains("c") && !currentLine.contains("G") && !currentLine.contains("|")) { //case: line contains relevant values
                    //rows.add(currentLine);

                    String modified = modifyString(currentLine);
                    double[] doubleArray = stringToDoubleArray(modified);
                    //System.out.println(Arrays.toString(doubleArray));
                    insertArrayInAGG(doubleArray, currow);

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

    //------------gets String of the values as input and converts them to double Array
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

    private void insertArrayInAGG(double[] doubleArray, int currow) {                                                       //agg hat überall -1.0 stehen

        for (int i = 0; i < doubleArray.length; i++) {
            if (doubleArray[i] == -1.0 && agg[currow][i][1] == -1.0 && agg[currow][i][2] == -1.0) {                         //if agg hasn't any value and DM didn't give a evaluation
                agg[currow][i][0] = -5.0;                                                                                   //-5 for random values between 0 an 1
                agg[currow][i][1] = 0.0;
                agg[currow][i][2] = 1.0;
            } else if (agg[currow][i][1] == -1.0 && agg[currow][i][2] == -1.0) {                                            //if agg hasn't any value and a=-1 and b=-1
                agg[currow][i][1] = doubleArray[i];
                agg[currow][i][2] = doubleArray[i];

            } else if ((doubleArray[i] < agg[currow][i][1] && doubleArray[i] != -1.0) || agg[currow][i][1] == -1.0 || (agg[currow][i][0] == -5.0 && agg[currow][i][0] == 0)) {          //Fehler mit dem oder (fügt -1 ein, obwohl vorher schon was drin stand (vorheriger Wert wird überschrieben) ("behoben")
                agg[currow][i][1] = doubleArray[i];                                                                         //wenn dm bewertung gegeben hat die kleiner ist als vorheriger wert oder falls vorher noch kein wert abgegeben wurde

            } else if (doubleArray[i] > agg[currow][i][2] || (agg[currow][i][0] == -5.0 && agg[currow][i][0] == 1.0)) {
                agg[currow][i][2] = doubleArray[i];
            }
            if (agg[currow][i][1] != agg[currow][i][2]) {                                                                   //if first and second value are not the same position 0 has the value -10
                agg[currow][i][0] = -10.0;
            }
            if (agg[currow][i][1] == agg[currow][i][2]) {                                                                   //if first and second value are the same put the value at position 0
                agg[currow][i][0] = agg[currow][i][1];
            }
        }
    }

    private void initPositionsToRandomizeAt() {

        int[] index;

        for (int i = 0; i < agg.length; i++) {
            for (int j = 0; j < agg[0].length; j++) {
                if (agg[i][j][0] == -10) {
                    index = new int[2];
                    index[0] = i;
                    index[1] = j;
                    positionsToRandomizeAt.add(index);
                }
            }
        }
    }

    private double[][] calculateRAI(int numIterations) {

        double[][] raiTable = new double[agg[1].length - 1][agg[1].length - 1];
        // 1.dimension: Alternative, 2.dimension: possible ranks -> save points / percentage per Rank

        //calculate point per alternative per rank
        for (int i = 0; i < numIterations; i++) {
            generateRandomValues();
            int[] rankedAlternatives = calculateRanking();
            for (int rank = 0; rank < rankedAlternatives.length; rank++) {
                raiTable[rankedAlternatives[rank]][rank]++;
            }
        }

        //calculate RAI
        for (int a = 0; a < raiTable.length; a++) {
            for (int rank = 0; rank < raiTable.length; rank++) {
                raiTable[a][rank] = raiTable[a][rank] / numIterations;
            }
        }

        return raiTable;
    }

    private int[] calculateRanking() {

        double[][] ranks = new double[agg[1].length - 1][2];                                            //number of alternatives
        double score = 0;

        for (int i = 1; i < agg[1].length; i++) {                                                       //traverse columns from left to right (starting with 1, because 0 is for weights)
            for (int j = 0; j < agg.length; j++) {                                                      //traverse rows "downwards"
                score += agg[j][0][0] * agg[j][i][0];
            }

            ranks[i - 1][0] = i - 1;                                                                    //score rank with alternative
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
     * generateRandomValues:
     * generate rand value in interval[min, max] specified by indices in positionsToRandomizeAt
     * write generated value into agg
     **/
    private void generateRandomValues() {

        for (int[] index : positionsToRandomizeAt) {
            double min = agg[index[0]][index[1]][1];
            double max = agg[index[0]][index[1]][2];

            //generate random Value in [min, max]
            double randValue = min + Math.random() * (max - min);
            randValue = (double) (Math.round(randValue * 10)) / 10;

            assert (randValue >= min && randValue <= max);

            agg[index[0]][index[1]][0] = randValue; //write random Value into agg
        }
    }



    public static void main(String[] args) {
        String pathVincent = "C:/UNI/04_Semester/ex_missing_values.csv";
        String pathMarten = "C:/Users/admin/Downloads/ex_missing_values.csv";
        String pathEdgar = "/Users/edgar/Documents/4 Semester/Softwareprojekt/my-swp-example.csv";

        String path = pathMarten;
        AGG agg = new AGG(path);

        double[][] raiTable = agg.calculateRAI(1000);
        System.out.println("RAI-Table: ");
        System.out.println(Arrays.deepToString(raiTable));

    }

}
