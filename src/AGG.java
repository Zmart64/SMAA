import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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

    private double[] stringToDoubleArray(String modified) {
        //String[] strArray =
        double[] doubleArray = null;
        return doubleArray;
    }

    private String modifyString(String currentLine) {

            //delete c1 - cn
            String modified = ";" + currentLine.replaceAll("c([0-9]+);", "") + ";"; //semicolons simplify following steps

            //replace missing values with -1
            while (modified.contains(";;")) {
                modified = modified.replaceFirst(";;", ";-1;");
            }

            return modified;
    }

//    private double generateRandom(){
//
//    }
//
//    private void calculateRanking(){
//
//    }


    public static void main(String[] args) {
        String pathVincent = "C:/UNI/04_Semester/ex_missing_values.csv";
        String pathMarten = "C:/Users/admin/Downloads/my-swp-example.csv";

        String path = pathMarten;
        AGG agg = new AGG(path);

    }

}
