import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadAndParse {

//    //--------cut out everything before important data
//    private static String cutBeginning(String data) {
//        String target = "DM1";
//        String ret = "";
//        int index = data.indexOf("DM1");
//
//        for (int i = index; i < data.length(); ++i) {
//            ret += data.charAt(i);
//        }
//        //same as ret = data.substring(index)
//
//        return ret;
//    }

//    //--------read in the .csv file and store into String
//    private static String readFile(String Path) {
//        String data = "";
//        try {
//            File myObj = new File(Path);
//            Scanner myReader = new Scanner(myObj);
//            while (myReader.hasNextLine()) {
//                data = myReader.nextLine();
//                System.out.println(data);
//            }
////            data = cutBeginning(temp);
//            myReader.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("File not found! Check input path.");
//            e.printStackTrace();
//        }
//        return data;
//    }

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

    //--------read through and count all c's for rows and a's for columns
    private static int[] countRowsAndCols(String data) {
        int rows = countCharTarget(data, 'c') - 1;                            //minus 1 wegen "Gewichte" -> ist ein c drin
        int columns = countCharTarget(data, 'a') + 1;

        return new int[]{rows, columns};
    }

//    //-------- create given number of matrices with given number of columns and rows, with the given data in them
//    private static double[][][] createMatrices(String data, int nrmatrices, int[] RowsAndCols) {
//        int nrrows = RowsAndCols[0];
//        int nrcols = RowsAndCols[1];
//        double[][][] allmatrices = new double[nrmatrices][nrrows][nrcols];                       //matrix, which contains all the matrices of the DM's
//        for (int i = 0; i < nrmatrices; i++) {
//            for (int j = 0; j < nrrows; j++) {
//                for (int k = 0; k < nrcols; k++) {
//                    allmatrices[i][j][k] = i + 1.1;
//                }
//
//            }
//
//        }
//
//        matrixToString(allmatrices);
//
//        return allmatrices;
//    }

    //--------- create agg table, which stores the interval values for the calculation (AGG)
    private static String[][] createAggTable(String path, int rows, int cols) {
        String[][] agg = new String[rows][cols];

        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String t = myReader.nextLine();

                if (t.contains("c") && !t.contains("a") && !t.contains("|")) {                      //get lines with numbers
                    System.out.println(t.replaceAll("(c)([0-9]+)(;)", ""));

//                    for (int i = 0; i < rows; i++) {
//                        StringBuilder tBuilder = new StringBuilder();                              //use StringBuilder to delete single chars certain indices
//                        tBuilder.append(t.replaceAll("(c)([0-9]+)(;)", ""));                        //get rid of criteria-name
//
//                        for (int j = 0; j < cols; j++) {
//
//                            int index = tBuilder.indexOf(";");                                     //identify first ";"
//                            int deletionCount = 0;
//
//                            for (int k = 0; k < index; k++) {
//                                agg[i][j] += tBuilder.charAt(k) + " | ";                           //add everything until first ";" to agg
//                                deletionCount++;
//                                System.err.println(tBuilder);
//                            }
//
//                            for (int k = 0; k <= deletionCount; k++) {
//                                tBuilder.deleteCharAt(k);                                          //delete anything in line until and including first ";"
//                                System.err.println("deleted: " + tBuilder);
//                            }
//                        }
//                    }
                }
            }

            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found! Check input path.");
            e.printStackTrace();
        }


        return agg;
    }


    public static void main(String[] args) {
        String pathMarten = "C:/Users/admin/Downloads/my-swp-example.csv";
        String pathEdgar = "/Users/edgar/Documents/4 Semester/Softwareprojekt/my-swp-example.csv";
        String pathHenriette = "";
        String pathVincent = "C:/UNI/04_Semester/Scoring-Beispiel.rtf";
        String path = pathMarten;                                                                     //nur für uns, je nachdem, wer gerade ändert, muss den Pfad auf das Scoring Beispiel auf seinem PC setzen
        String dataFirst = copyFirst(path);

        int[] rowsAndCols = countRowsAndCols(dataFirst);
        //System.out.println(Arrays.toString(rowsAndCols));

        String[][] test = createAggTable(path, rowsAndCols[0], rowsAndCols[1]);

        for (int i = 0; i < rowsAndCols[0]; i++) {
            for (int j = 0; j < rowsAndCols[1]; j++) {
                System.out.println(test[i][j]);
            }

        }


        //replace everything but numbers
        //  data = data.replaceAll("(DM|a|c)([0-9]+)|//|,|\\\\|}", " ");
        //  data = data.replaceAll("\\s+", " ");

    }
}