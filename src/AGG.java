import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class AGG {

    private int dmCount = 0;
    private int criteriaCount = 0;
    private double[][][] agg;
    private ArrayList<int[]> posToRandomizeAt;

    public AGG(String Path) {
        csvToArrayList(Path);

    }

    private ArrayList<String> csvToArrayList(String Path) {
        /*
            - creates ArrayList of Type String from csv-File:
                - each entry represents one line,starting and ending with ";"
                - everything but values gets removed
                - missing values are replaced with -1
                - example format of one line: ;0,4;-1;3;

            - initializes dmCount and criteriaCount
        */


        ArrayList<String> rows = new ArrayList<>();

        //read all lines into rows, skip DM rows + empty lines
        try {
            File file = new File(Path);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String currentLine = reader.nextLine();

                if (currentLine.contains("c") && !currentLine.contains("G")) { //case: line contains relevant values
                    rows.add(currentLine);
                    if (dmCount == 1) {//only count Criteria for first DM
                        criteriaCount++;
                    }
                } else if (currentLine.contains("D")) { //case:DM, ignore
                    dmCount++;
                }
                //else: Gewichte or empty line -> ignore
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found! Check input path.");
            e.printStackTrace();
        }


        //delete cn and replace missing values with -1
        for (int i = 0; i < rows.size(); i++) {

            //delete c1 - cn
            String modified = ";" + rows.get(i).replaceAll("c([0-9]+);", "") + ";"; //semicolons simplify following steps

            //replace missing values with -1
            while (modified.contains(";;")) {
                modified = modified.replaceFirst(";;", ";-1;");
            }

            rows.set(i, modified);

        }


        //for testing: print ArrayList rows
        System.out.println("ArrayList rows: ");
        for (String row : rows)
            System.out.println(row);
        System.out.println("dmCount: " + dmCount + "  criteriaCount: " + criteriaCount);

        return rows;
    }

    public static void main(String[] args) {
        String pathVincent = "C:/UNI/04_Semester/ex_missing_values.csv";

        String path = pathVincent;
        AGG agg = new AGG(path);

    }

}
