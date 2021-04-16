import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class ReadAndParse {

    //--------cut out everything before important data
    private static String cutBeginning(String data) {
        String target = "DM1";
        String ret = "";
        int index = data.indexOf("DM1");

        for (int i = index; i < data.length(); ++i) {
            ret += data.charAt(i);
        }
        //same as ret = data.substring(index)

        return ret;
    }

    //--------read in the .csv file and store into String
    private static String readFile(String Path) {
        String temp = "";
        String data = "";
        try {
            File myObj = new File(Path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                temp += myReader.nextLine();
            }
            data = cutBeginning(temp);
            // System.out.println(data);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found! Check input path.");
            e.printStackTrace();
        }
        return data;
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

    //--------Copy first DMEntry, to count Rows and Columns
    private static String copyFirst(String data) {
        char target = 'D';
        int counter = 0;
        int i = 0;
        String copy = "";

        while (counter <= 1) {
            if (target == data.charAt(i)) {
                counter++;
            }

            copy += data.charAt(i);

            i++;
        }

        return copy;
    }

    //--------read through and count all c's for rows and a's for columns
    private static int[] countRowsAndCol(String data) {
        String DM = copyFirst(data);
        int rows = countCharTarget(DM, 'c');
        int columns = countCharTarget(DM, 'a') + 1;

        return new int[]{rows, columns};
    }

    private static void matrixToString(double[][][] matrix) {

        System.out.println(Arrays.deepToString(matrix));
    }

    //-------- create given number of matrices with given number of columns and rows, with the given data in them
    private static double[][][] createMatrices(String data, int nrmatrices, int[] RowsAndCols) {
        int nrrows = RowsAndCols[0];
        int nrcols = RowsAndCols[1];
        double [][][] allmatrices = new double[nrmatrices][nrrows][nrcols];           //matrix, which contains all the matrices of the DM's
        for (int i = 0; i < nrmatrices; i++) {
            for (int j = 0; j < nrrows; j++) {
                for (int k = 0; k < nrcols; k++){
                    allmatrices[i][j][k] = i+1.1;
                }

            }

        }

        matrixToString(allmatrices);

        return allmatrices;
    }


    public static void main(String[] args) {
        String pathMarten = "C:/Users/admin/Downloads/Scoring-Beispiel.rtf";
        String pathEdgar = "/Users/edgar/Documents/4 Semester/Softwareprojekt/Scoring-Beispiel.rtf";
        String pathHenriette = "";
        String pathVincent = "C:/UNI/04_Semester/Scoring-Beispiel.rtf";
        String path = pathVincent;                                //nur für uns, je nachdem, wer gerade ändert, muss den Pfad auf das Scoring Beispiel auf seinem PC setzen
        String data = readFile(path);
        int DMcount = countCharTarget(data, 'D');
        int[] RowsAndCol = countRowsAndCol(data);
        double allmatrices[][][] = createMatrices(data, DMcount, RowsAndCol);

        System.out.println(data);

        //replace everything but numbers
        data = data.replaceAll("(DM|a|c)([0-9]+)|//|,|\\\\|}"," ");
        data = data.replaceAll("\\s+"," ");
        System.out.println(data);

//        System.out.println(DMcount);
//        System.out.println(copyFirst(data));
//        System.out.println(cutBeginning(data));
    }


}