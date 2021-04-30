import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class AGG {

    int dmCount = 0;
    int criteriaCount = 0;

    public AGG(String Path){
        FileToArrayList(Path);

    }

    private ArrayList<String> FileToArrayList(String Path){
        /*
            - creates ArrayList of Type String from csv-File:
                - each entry represents one line
                - everything but values gets removed
                - missing values are replaced with -1
                - example format of one line: 0.4;-1;3;

            - initializes dmCount and criteriaCount
        */


        ArrayList<String> rows = new ArrayList<>();

        //read all lines into rows, skip DM row + empty lines
        try {
            File file = new File(Path);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String currentLine = reader.nextLine();
                if ( ! (currentLine.contains("D") || currentLine.contains("G")) ){
                    rows.add(currentLine);
                    if (dmCount == 1) //only count Criteria for first DM
                        criteriaCount++;
                }
                //case:DM skip next line and delete previous line(should both be empty)
                else if (currentLine.contains("D")) {
                    dmCount++;
                    if(rows.size() > 0)
                        rows.remove(rows.size() - 1);
                    reader.nextLine();
                }
                //else: Gewichte -> ignore
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found! Check input path.");
            e.printStackTrace();
        }

        criteriaCount--;//because of empty line following DM-table

        //delete cn and replace missing values with -1
        for(int i = 0; i < rows.size();i++){

            //delete c1 - cn
            String modified = rows.get(i).replaceAll("c([0-9]+);", "") + ";"; //semicolon at the end simplify following steps

            //replace missing values with -1
            if(Character.toString(modified.charAt(0)).equals(";"))
                modified = "-1;"+modified.substring(1);
            while(modified.contains(";;")) {
                modified = modified.replaceFirst(";;", ";-1;");
            }

            rows.set(i, modified);

        }


        //for testing: print ArrayList rows
        System.out.println("ArrayList rows: ");
        for (String row : rows)
            System.out.println(row);

        System.out.println("dmCount: "+dmCount+"  criteriaCount: "+criteriaCount);

        return rows;
    }

    public static void main(String[] args) {
        String pathVincent = "C:/UNI/04_Semester/example_one_missing_value.csv";

        AGG agg = new AGG(pathVincent);

    }
    
}
