import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class Utils {

    /**
     * calculates ranking for dataTable
     *
     * @return ranking of alternatives, order: first to last
     */
    private static int[] calculateRanking(AGG agg) {

        double[][][] dataTable = agg.getAGG();

        //number of alternatives
        double[][] ranks = new double[dataTable[1].length - 1][2];
        double score = 0;

        //traverse columns from left to right (starting with 1, because 0 is for weights)
        for (int i = 1; i < dataTable[1].length; i++) {
            //traverse rows "downwards"
            for (int j = 0; j < dataTable.length; j++) {
                score += dataTable[j][0][0] * dataTable[j][i][0];
            }

            //score rank with alternative
            ranks[i - 1][0] = (double) i - 1;
            //round score up to two decimal points
            ranks[i - 1][1] = Math.round(score * 100.00) / 100.00;
            //reset score for next alternative
            score = 0;
        }

        //sort for alternatives with highest score (descending)
        Arrays.sort(ranks, (o1, o2) -> Double.compare(o2[1], o1[1]));

        int[] retRanks = new int[ranks.length];

        for (int i = 0; i < ranks.length; i++) {
            retRanks[i] = (int) ranks[i][0];
        }

        return retRanks;
    }

    /**
     * generates random values in intervals[min, max] specified by indices in posToRandomize
     * and writes generated value into dataTable
     */
    private static void generateRandomValues(AGG agg) {

        ArrayList<int[]> posToRandomize = agg.getRandPositions();
        double[][][] dataTable = agg.getAGG();

        for (int[] index : posToRandomize) {
            double min = dataTable[index[0]][index[1]][1];
            double max = dataTable[index[0]][index[1]][2];

            //generate random Value in [min, max]
            double randValue = min + Math.random() * (max - min);
            randValue = (double) (Math.round(randValue * 10)) / 10;

            assert (randValue >= min && randValue <= max);

            //write random Value into dataTable
            dataTable[index[0]][index[1]][0] = randValue;
        }
    }

    /**
     * @param numIterations How often Ranking shall be performed
     * @return RAI-Table : first dim: alternatives, second dim: ranks
     */
    public static double[][] calculateRAI(AGG agg, int numIterations) {

        ArrayList<int[]> posToRandomize = agg.getRandPositions();
        double[][][] dataTable = agg.getAGG();

        double[][] raiTable = new double[dataTable[1].length - 1][dataTable[1].length - 1];
        // 1.dimension: Alternative, 2.dimension: possible ranks -> save points / percentage per Rank

        //calculate point per alternative per rank
        for (int i = 0; i < numIterations; i++) {
            generateRandomValues(agg);
            int[] rankedAlternatives = calculateRanking(agg);
            for (int rank = 0; rank < rankedAlternatives.length; rank++) {
                raiTable[rankedAlternatives[rank]][rank]++;
            }
        }

        //calculate RAI (in %, 2 decimals after comma)
        for (int a = 0; a < raiTable.length; a++) {
            for (int rank = 0; rank < raiTable.length; rank++) {
                raiTable[a][rank] = Math.round(raiTable[a][rank] / numIterations * 100 * 100) / 100.0;
            }
        }

        return raiTable;
    }

    /**
     * generates new csv file "output.csv" and writes rai values into it
     */
    public static void exportRaiTable(double[][] raiTable, String path) {

        try {
            //creates new file
            FileWriter file = new FileWriter(path+"/RAI_table.csv");
            PrintWriter writer = new PrintWriter(file);

            writer.println("Calculated RAIs:");
            writer.println();

            //first row
            writer.print("Rank;");
            for (int i = 0; i < raiTable[0].length; i++) {
                if (i < raiTable[0].length - 1)
                    writer.print("a" + (i + 1) + ";");
                else
                    writer.print("a" + (i + 1));
            }

            //print all rai values per rank
                for (int rank = 0; rank < raiTable.length; rank++) {
                writer.println();
                writer.print(rank + 1 + ";");
                for (int alt = 0; alt < raiTable[0].length; alt++) {
                    if (alt < raiTable[0].length - 1)
                        writer.print(String.valueOf(raiTable[alt][rank]).replace(".", ",") + ";");
                    else
                        writer.print(String.valueOf(raiTable[alt][rank]).replace(".", ","));
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * calculates totalpoints for each alternative
     * points on rank i is weighted with lastRank-i+1
     * @param raiTable
     * @return array with totalpoints for each alternative, index = number of alternative
     */
    public static double[] calculateTotalPoints(double[][] raiTable){
        double[] totalpoints = new double[raiTable.length];
        for (int alt = 0; alt < totalpoints.length; alt++){
            for(int rank = 0; rank < totalpoints.length;rank++){
                //weight = totalpoints.length - i + 1 = length-rank
                totalpoints[alt] += (totalpoints.length - rank) * raiTable[alt][rank];
            }
        }
        return totalpoints;
    }

    /**
     * calculates percentage difference between total points of each alternative and alternative with maximum totalpoints
     * @param raiTable
     * @return double array with percentual Differences, index = number of the alternative
     */
    public static double[] getPercentageDifference(double[][] raiTable){
        double[] totalpoints = calculateTotalPoints(raiTable);
        System.out.println("totalpoints: "+Arrays.toString(totalpoints));
        double[] tempMax = {0,totalpoints[0]};
        for(int i = 1; i < totalpoints.length;i++){
            if (tempMax[1] < totalpoints[i]) {
                tempMax[1] = totalpoints[i];
                tempMax[0] = i;
            }
        }
        System.out.println("Maximum Alternative: "+tempMax[0]+" points: "+tempMax[1]);

        double max = totalpoints[(int)tempMax[0]];
        double[] percentages = new double[totalpoints.length];
        for (int i = 0; i < percentages.length; i++){
            percentages[i] = Math.round((max - totalpoints[i]) / max * 10000 / 100);
        }
        System.out.println("percentual differences: "+Arrays.toString(percentages));
        return percentages;
    }






    /**
     * only needed for testing atm
     *
     * @param args nothing
     */
    public static void main(String[] args) {
        String pathVincent = "C:/UNI/04_Semester/example.csv";
        String pathMarten = "C:/Users/admin/Downloads/my-swp-example.csv";
        String pathEdgar = "/Users/edgar/Documents/4 Semester/Softwareprojekt/my-swp-example.csv";

        String path = pathVincent;
        AGG agg = new AGG(path);

        double[][] raiTable = calculateRAI(agg, 100000);
        System.out.println("RAI-Table: ");
        System.out.println(Arrays.deepToString(raiTable));

        String outputPath = "C:/UNI/04_Semester";
        exportRaiTable(raiTable, outputPath);

        getPercentageDifference(raiTable);
    }
}

