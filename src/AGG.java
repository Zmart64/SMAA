import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class AGG {

    private int criteriaCount = 0;
    private double[][][] agg;
    private ArrayList<int[]> posToRandomizeAt=new ArrayList<>();

    public AGG(String Path) {
       // csvToAGG(Path);

        agg = new double[3][3][3];
        agg[0][0][0] = 0.4;
        agg[1][0][0] = 0.5;
        agg[2][0][0] = 0.2;
        agg[0][1][0] = 0.6;
        agg[1][1][0] = 0.3;
        agg[0][2][0] = 0.2;
        agg[2][2][0] = 0.2;
        agg[1][2][0] = 0.8;
        agg[2][1][0] = 0.8;
        agg[0][0][1] = 0.4;
        agg[0][0][2] = 0.7;
        agg[0][1][1] = 0.2;
        agg[0][1][2] = 0.8;
        agg[1][1][1] = 0.3;
        agg[1][1][2] = 0.4;
        agg[1][0][1] = 0.1;
        agg[1][0][2] = 0.2;
        agg[2][1][1] = 0.3;
        agg[2][1][2] = 0.4;
        agg[2][0][1] = 0.1;
        agg[2][0][2] = 0.2;

        System.out.println(Arrays.deepToString(agg));
        System.out.println("agg.length" + " reihen    " + "agg[1].length" + " spalten" );

        calculateRanking(agg);
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

    private void insertArrayInAGG(double[] doubleArray, int currow) { //agg hat überall -1.0 stehen
        int[] g=new int[2];
        for(int i=0;i<doubleArray.length;i++){
            if(doubleArray[i]==-1.0 && agg[currow][i][1]==-1.0 && agg[currow][i][2]==-1.0){       //if agg hasn't any value and DM didn't give a evaluation
                agg[currow][i][0]=-5.0; //-5 for random values between 0 an 1
                agg[currow][i][1]=0.0;
                agg[currow][i][2]=1.0;
                g[0]=currow;
                g[1]=i;
                posToRandomizeAt.add(g);
            }else if(agg[currow][i][1]==-1.0 && agg[currow][i][2]==-1.0){ //if agg hasn't any value and a=-1 and b=-1
                agg[currow][i][1]=doubleArray[i];
                agg[currow][i][2]=doubleArray[i];

            }else if(doubleArray[i]<agg[currow][i][1] || agg[currow][i][1]==-1.0 || (agg[currow][i][0]==-5.0 && agg[currow][i][0]==0)){
                agg[currow][i][1]=doubleArray[i]; //wenn dm bewertung gegeben hat die kleiner ist als vorheriger wert oder falls vorher noch kein wert abgegeben wurde

            }else if(doubleArray[i]>agg[currow][i][2] ||(agg[currow][i][0]==-5.0 && agg[currow][i][0]==1.0) ){
                agg[currow][i][2]=doubleArray[i];
            }
            if(agg[currow][i][1] != agg[currow][i][2]){ //if first and second value are not the same position 0 has the value -10
                agg[currow][i][0]=-10.0;
                g[0]=currow;
                g[1]=i;
                posToRandomizeAt.add(g);
            }
            if(agg[currow][i][1] == agg[currow][i][2]){//if first and second value are the same put the value at position 0
                agg[currow][i][0]=agg[currow][i][1];
            }

        }


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

    /**
     * generateRandomValues:
     * generate rand value in interval[min, max] specified by indices in posToRandomizeAt
     * write generated value into agg
     **/
    private void generateRandomValues(){

        for(int[] index : posToRandomizeAt){
            double min = agg[index[0]][index[1]][1];
            double max = agg[index[0]][index[1]][2];

            //generate random Value in [min, max]
            double randValue = min + Math.random()*(max-min);
            randValue = (double)(Math.round(randValue*10)) / 10;

            assert (randValue >= min && randValue <= max);

            agg[index[0]][index[1]][0] = randValue; //write random Value into agg
        }
    }

    private void calculateRanking(double[][][] agg) {

        double[][] ranks = new double[agg[1].length - 1][2];                       //number of alternatives
        double score = 0;

        for (int i = 1; i < agg[1].length; i++) {                                  //traverse columns from left to right (starting with 1, because 0 is for weights)
            for (int j = 0; j < agg.length; j++) {                                 //traverse rows "downwards"
                score += agg[j][0][0] * agg[j][i][0];
            }

            ranks[i - 1][0] = i;                                                   //score rank with alternative
            ranks[i - 1][1] = Math.round(score * 100.00) / 100.00;
            score = 0;                                                             //reset score for next alternative
        }

        Arrays.sort(ranks, (o1, o2) -> Double.compare(o2[1], o1[1]));

        for (int i = 0; i < ranks.length; i++) {
            System.out.println("Alternative: " + (int) ranks[i][0] + ", Score: " + ranks[i][1]);
        }
    }


    public static void main(String[] args) {
        String pathVincent = "C:/UNI/04_Semester/ex_missing_values.csv";
        String pathMarten = "C:/Users/admin/Downloads/my-swp-example.csv";
        String pathEdgar = "/Users/edgar/Documents/4 Semester/Softwareprojekt/my-swp-example.csv";

        String path = pathMarten;
        AGG agg = new AGG(path);
    }

}
