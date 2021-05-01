import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class AGG {

    private int criteriaCount = 0;
    private double[][][] agg;
    private ArrayList<int[]> posToRandomizeAt;

    public AGG(String Path) {
        csvToAGG(Path);

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
        int dmCount = 0;
        int currow = 0;


        //read all lines into rows, skip DM rows + empty lines
        try {
            File file = new File(Path);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String currentLine = reader.nextLine();

                if (currentLine.contains("c") && !currentLine.contains("G") && !currentLine.contains("|")) { //case: line contains relevant values
                    //rows.add(currentLine);
                    currow++;

                    String modified = modifyString(currentLine);
                    double[] doubleArray = stringToDoubleArray(modified);
                    System.out.println(Arrays.toString(doubleArray));
                    insertArrayInAGG(doubleArray, currow);

                    if (dmCount == 1) {//only count Criteria for first DM
                        criteriaCount++;
                    }
                } else if (currentLine.contains("DM")) { //case:DM, ignore
                    dmCount++;
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

    private void insertArrayInAGG(double[] doubleArray, int currow) {


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

//    private double generateRandom(){
//
//    }

    private void calculateRanking(double[][][] agg) {

        double[][] ranks = new double[agg[1].length - 1][2];                       //number of alternatives
        double score = 0;

        for (int i = 1; i < agg[1].length; i++) {                                  //traverse columns from left to right (starting with 1, because 0 is for weights)
            for (int j = 0; j < agg[0].length; j++) {                              //traverse rows "downwards"
                score += agg[j][0][0] * agg[j][i][0];
            }

            ranks[i-1][0] = i-1;                                                   //score rank with alternative
            ranks[i-1][1] = score;
            score = 0;                                                             //reset score for next alternative
        }

        Arrays.sort(ranks, Comparator.comparingDouble(o -> o[0]));

        for (int i = 0; i < ranks[0].length; i++) {
            System.out.println("Alternative: " + ranks[i][0] + ", Score: " +  ranks[i][1]);
        }

    }


    public static void main(String[] args) {
        String pathVincent = "C:/UNI/04_Semester/ex_missing_values.csv";
        String pathMarten = "C:/Users/admin/Downloads/my-swp-example.csv";
        String pathEdgar = "/Users/edgar/Documents/4 Semester/Softwareprojekt/my-swp-example.csv";

        String path = pathEdgar;
        AGG agg = new AGG(path);

    }

}
