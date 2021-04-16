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
        int columns = 0;
        int rows = 0;
        String DM = copyFirst(data);
        rows = countCharTarget(DM, 'c');
        columns = countCharTarget(DM, 'a') + 1;

        return new int[]{rows, columns};
    }

    private static void matrixToString(int[][] matrix) {
        System.out.println(Arrays.deepToString(matrix));
    }

    //-------- create given number of matrices with given number of columns and rows, with the given data in them
    private static void createMatrices(String data, int DMcount, int[] RowsAndCols) {
        Object[] matrices = new Object[DMcount];

        // for (int i = 0; i < DMcount; i++) {
        int[][] matrix = new int[RowsAndCols[0]][RowsAndCols[1]];             //enter first and second entry of countColAndRow-Return-Value --> 0 = number of columns, 1 = number of rows
        for (int j = 0; j < RowsAndCols[0]; j++) {
            for (int k = 0; k < RowsAndCols[1]; k++) {
                matrix[j][k] = 1;
            }
        }
        matrixToString(matrix);
        // matrices[i] = matrix;
        //}


    }


    public static void main(String[] args) {
        String pathMarten = "C:/Users/admin/Downloads/Scoring-Beispiel.rtf";
        String pathEdgar = "/Users/edgar/Documents/4 Semester/Softwareprojekt/Scoring-Beispiel.rtf";
        String pathHenriette = "";
        String pathVincent = "";
        String path = pathMarten;
        String data = readFile(path);
        int DMcount = countCharTarget(data, 'D');
        int[] RowsAndCol = countRowsAndCol(data);
        createMatrices(data, DMcount, RowsAndCol);

//        System.out.println(DMcount);
//        System.out.println(copyFirst(data));
//        System.out.println(cutBeginning(data));
    }


}