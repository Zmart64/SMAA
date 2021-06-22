package smaa_calculation;

import smaa_creation.AGG;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides several methods to perform SMAA based on an AGG-table
 */
public final class Utils {

    private Utils() {
    }

    /**
     * calculates ranking for dataTable
     *
     * @return ranking of alternatives (specified by a number), order: first to last
     */
    public static int[] calculateRanking(AGG agg) {

        double[][][] dataTable = agg.getAGG();

        //ranks stores 1.Number of the Alternative, 2.Score
        double[][] ranks = new double[dataTable[1].length - 1][2];

        double tempScore = 0;

        //traverse columns from left to right (starting with 1, because first column is for weights)
        for (int i = 1; i < dataTable[1].length; i++) {
            //traverse rows "downwards"
            for (double[][] doubles : dataTable) {
                tempScore += doubles[0][0] * doubles[i][0];
            }

            ranks[i - 1][0] = (double) i - 1;
            ranks[i - 1][1] = Math.round(tempScore * 100.00) / 100.00;
            tempScore = 0;
        }

        //sort for alternatives with highest score (descending)
        Arrays.sort(ranks, (o1, o2) -> Double.compare(o2[1], o1[1]));

        int[] ranking = new int[ranks.length];

        for (int i = 0; i < ranks.length; i++) {
            ranking[i] = (int) ranks[i][0];
        }

        return ranking;
    }

    /**
     * generates random values in intervals[min, max] specified by indices in posToRandomize
     * and writes generated value into dataTable
     */
    private static void generateRandomValues(AGG agg) {

        List<int[]> posToRandomize = agg.getRandPositions();
        double[][][] dataTable = agg.getAGG();

        for (int[] index : posToRandomize) {
            double min = dataTable[index[0]][index[1]][1];
            double max = dataTable[index[0]][index[1]][2];

            //generate random Value in [min, max]
            double randValue = min + Math.random() * (max - min);
            randValue = (double) (Math.round(randValue * 10)) / 10;

            //write random Value into dataTable
            dataTable[index[0]][index[1]][0] = randValue;
        }
    }

    /**
     * @param numIterations How often Ranking shall be performed
     * @return RAI-Table : first dim: alternatives, second dim: ranks
     */
    public static double[][] calculateRAI(AGG agg, int numIterations) {

        double[][][] dataTable = agg.getAGG();

        double[][] raiTable = new double[dataTable[1].length - 1][dataTable[1].length - 1];
        // 1.dimension: Alternative, 2.dimension: possible ranks -> save points / percentage per Rank


        for (int i = 0; i < numIterations; i++) {
            generateRandomValues(agg);
            int[] rankedAlternatives = calculateRanking(agg);
            //increment points at corresponding alternative / rank
            for (int rank = 0; rank < rankedAlternatives.length; rank++) {
                raiTable[rankedAlternatives[rank]][rank]++;
            }
        }

        //calculate RAI (in %, 2 decimals after comma)
        for (int alt = 0; alt < raiTable.length; alt++) {
            for (int rank = 0; rank < raiTable.length; rank++) {
                raiTable[alt][rank] = Math.round(raiTable[alt][rank] / numIterations * 100 * 100) / 100.0;
            }
        }

        return raiTable;
    }


    /**
     * calculates totalpoints for each alternative, only ranks 1 to 3 are relevant
     * lower ranks are not important enough
     * points(actually a percentage) on rank i is weighted with lastRank-i+1
     *
     * @param raiTable
     * @return array with totalpoints for each alternative, index = number of alternative
     */
    public static double[] calculateTotalPoints(double[][] raiTable) {
        double[] totalpoints = new double[raiTable.length];
        for (int alt = 0; alt < totalpoints.length; alt++) {
            for (int rank = 0; rank < totalpoints.length && rank < 3; rank++) {
                //weight = length-(rank-1)
                totalpoints[alt] += (totalpoints.length - rank) * raiTable[alt][rank];
            }
        }
        return totalpoints;
    }

    /**
     * calculates percentage difference between total points of each alternative and alternative with maximum totalpoints
     *
     * @param raiTable
     * @return double array with percentual Differences, index = number of the alternative
     */
    public static double[] getPercentageDifferences(double[][] raiTable) {
        double[] totalpoints = calculateTotalPoints(raiTable);

        //search maximum
        double max = totalpoints[0];
        for (int i = 1; i < totalpoints.length; i++) {
            if (max < totalpoints[i]) {
                max = totalpoints[i];
            }
        }

        //calculate percentageDifferences and round them
        double[] percentages = new double[totalpoints.length];
        for (int i = 0; i < percentages.length; i++) {
            percentages[i] = Math.round((max - totalpoints[i]) / max * 10000 / 100);
        }

        return percentages;
    }

    /**
     * decides about which alternatives should be discarded;
     * the minimum percentage difference to the highest ranked alternative is customizable (second parameter)
     *
     * @param percentageDifferences
     * @return returns a list of integers which contains the alternatives that should be discarded (starting with 1)
     */
    public static List<Integer> decideExclusion(double[] percentageDifferences, double minDifference) {
        List<Integer> discards = new ArrayList<>();

        for (int i = 1; i <= percentageDifferences.length; i++) {
            if (percentageDifferences[i - 1] >= minDifference) {
                discards.add(i);
            }
        }

        return discards;
    }


    /**
     * generates new csv file and writes rai values into it
     */
    public static void exportCsv(double[][] raiTable, String pathToFile) throws IOException {
        //creates new file
        FileWriter file = new FileWriter(pathToFile);
        PrintWriter writer = new PrintWriter(file);

        writer.println("Results of the Stochastic Multicriteria Acceptability Analysis:");
        writer.println();

        printRecommendation(raiTable, writer);
        printPercentageDifferences(raiTable, writer);
        printRaiTable(raiTable, writer);

        writer.close();

    }

    /**
     * prints Recommendation .csv-file
     * minimal percentual difference is customizable
     **/
    private static void printRecommendation(double[][] raiTable, PrintWriter writer) {
        List<Integer> discards = decideExclusion(getPercentageDifferences(raiTable), 10);

        if (discards.isEmpty()) {
            writer.println("Due to very similar scores for all alternatives, no alternatives can be excluded.\n" +
                    "If possible, please re-evaluate your decisions and run the SMAA once again.");
        } else {
            writer.print("The following alternatives should be discarded: ");
            writer.println();
            for (Integer discard : discards) {
                writer.println(discard);
            }
        }
        writer.println();
    }

    /**
     * prints Percentage Differences as a table into .csv-file
     */
    private static void printPercentageDifferences(double[][] raiTable, PrintWriter writer) {
        double[] percentages = getPercentageDifferences(raiTable);

        writer.println("Percentage Differences (referring to the best alternative): ");

        for (int i = 0; i < percentages.length; i++) {
            writer.print("a" + (i + 1));
            if (i != percentages.length - 1)
                writer.print(";");
        }
        writer.println();
        for (int i = 0; i < percentages.length; i++) {
            writer.print(percentages[i]);
            if (i != percentages.length - 1)
                writer.print(";");
        }
        writer.println();

    }

    /**
     * prints RaiTable into .csv-file
     **/
    private static void printRaiTable(double[][] raiTable, PrintWriter writer) {
        writer.println();
        writer.println("Calculated RAIs:");

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
    }


}

