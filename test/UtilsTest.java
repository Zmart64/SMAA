import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class UtilsTest {

    AGG fixedAgg = new AGG("test/resources/fixed-values-agg.csv");

    @Test
    public void testCalculateRanking() {
        int[] expected = {0, 1, 2, 6, 5, 7, 3, 4};
        Assertions.assertArrayEquals(expected, Utils.calculateRanking(fixedAgg));
    }

    @Test
    public void testCalculateRAI() {
        double[][] expected = {{100.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, {0.0, 100.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, {0.0, 0.0, 100.0, 0.0, 0.0, 0.0, 0.0, 0.0}, {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 100.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 100.0}, {0.0, 0.0, 0.0, 0.0, 100.0, 0.0, 0.0, 0.0}, {0.0, 0.0, 0.0, 100.0, 0.0, 0.0, 0.0, 0.0}, {0.0, 0.0, 0.0, 0.0, 0.0, 100.0, 0.0, 0.0}};
        Assertions.assertArrayEquals(expected, Utils.calculateRAI(fixedAgg, 10000));
    }

    @Test
    public void testCalculateTotalPoints() {
        double[] expected = {800.0, 700.0, 600.0, 200.0, 100.0, 400.0, 500.0, 300.0};
        Assertions.assertArrayEquals(expected, Utils.calculateTotalPoints(Utils.calculateRAI(fixedAgg, 10000)));
    }

    @Test
    public void testGetPercentageDifference() {
        double[] expected = {0.0, 13.0, 25.0, 75.0, 88.0, 50.0, 38.0, 63.0};
        Assertions.assertArrayEquals(expected, Utils.getPercentageDifference(Utils.calculateRAI(fixedAgg, 10000)));
    }

    @Test
    public void testDecideExclusion() {
        List<Integer> expected = Arrays.asList(3, 4, 5, 6, 7, 8);
        Assertions.assertEquals(expected, Utils.decideExclusion(Utils.getPercentageDifference(Utils.calculateRAI(fixedAgg, 10000)), 20));
    }
}
