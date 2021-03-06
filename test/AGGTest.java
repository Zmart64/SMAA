import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import smaa_creation.AGG;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class AGGTest {

    //5 rows, 8 + 1 columns (8 alternatives, 1 for weights)
    private static final String SCENARIO1 = "test/resources/scenario_1.csv";

    //5 rows, 8 + 1 columns
    //two decisionmaker changed weightorder --> no impact on smaa.AGG
    private static final String SCENARIO2 = "test/resources/scenario_2.csv";

    //5 rows, 8 + 1 columns
    //three decisionmaker changed weightorder --> changes smaa.AGG
    private static final String SCENARIO3 = "test/resources/scenario_3.csv";

    //3 rows, 3 + 1 columns
    private static final String SCENARIO4 = "test/resources/scenario_4.csv";

    //3 rows, 3 + 1 columns
    private static final String SCENARIO5 = "test/resources/scenario_5.csv";

    //3 rows, 3 + 1 columns
    //5 decisionmaker changed weightorder --> same smaa.AGG as in scenario_4
    private static final String SCENARIO6 = "test/resources/scenario_6.csv";

    @Test
    public void testCSVToAGGScenario1() throws FileNotFoundException {
        AGG test = new AGG(SCENARIO1);
        Assertions.assertEquals(Arrays.deepToString(test.getAGG()), "[[[10.0, 0.1, 0.5], [0.5, 0.0, 1.0], [0.5, 0.0, 1.0], [10.0, 0.1, 0.7], [10.0, 0.5, 0.7], [10.0, 0.1, 0.5], [0.7, 0.0, 1.0], [0.5, 0.0, 1.0], [10.0, 0.1, 0.7]], " +
                "[[10.0, 0.2, 0.4], [0.2, 0.0, 1.0], [0.2, 0.0, 1.0], [0.2, 0.0, 1.0], [0.2, 0.0, 1.0], [0.2, 0.0, 1.0], [0.1, 0.0, 1.0], [0.2, 0.0, 1.0], [0.2, 0.0, 1.0]], " +
                "[[0.3, 0.0, 1.0], [0.3, 0.0, 1.0], [0.3, 0.0, 1.0], [0.3, 0.0, 1.0], [0.3, 0.0, 1.0], [0.3, 0.0, 1.0], [0.1, 0.0, 1.0], [0.3, 0.0, 1.0], [0.3, 0.0, 1.0]], " +
                "[[10.0, 0.2, 0.4], [10.0, 0.1, 0.4], [0.4, 0.0, 1.0], [10.0, 0.1, 0.4], [0.4, 0.0, 1.0], [10.0, 0.1, 0.4], [0.1, 0.0, 1.0], [10.0, 0.1, 0.4], [10.0, 0.1, 0.4]], " +
                "[[10.0, 0.1, 0.5], [0.5, 0.0, 1.0], [10.0, 0.1, 0.5], [10.0, 0.1, 0.5], [0.1, 0.0, 1.0], [0.1, 0.0, 1.0], [0.7, 0.0, 1.0], [0.5, 0.0, 1.0], [0.1, 0.0, 1.0]]]");
    }

    @Test
    public void testCSVToAGGScenario2() throws FileNotFoundException {
        AGG test = new AGG(SCENARIO2);
        Assertions.assertEquals(Arrays.deepToString(test.getAGG()), Arrays.deepToString(new AGG(SCENARIO1).getAGG()));
    }

    @Test
    public void testCSVToAGGScenario3() throws FileNotFoundException {
        AGG test = new AGG(SCENARIO3);
        Assertions.assertEquals(Arrays.deepToString(test.getAGG()), "[[[0.1, 0.0, 1.0], [0.5, 0.0, 1.0], [0.5, 0.0, 1.0], [10.0, 0.1, 0.7], [10.0, 0.5, 0.7], [10.0, 0.1, 0.5], [0.7, 0.0, 1.0], [0.5, 0.0, 1.0], [10.0, 0.1, 0.7]], " +
                "[[0.2, 0.0, 1.0], [0.2, 0.0, 1.0], [0.2, 0.0, 1.0], [0.2, 0.0, 1.0], [0.2, 0.0, 1.0], [0.2, 0.0, 1.0], [0.1, 0.0, 1.0], [0.2, 0.0, 1.0], [0.2, 0.0, 1.0]], " +
                "[[0.3, 0.0, 1.0], [0.3, 0.0, 1.0], [0.3, 0.0, 1.0], [0.3, 0.0, 1.0], [0.3, 0.0, 1.0], [0.3, 0.0, 1.0], [0.1, 0.0, 1.0], [0.3, 0.0, 1.0], [0.3, 0.0, 1.0]], " +
                "[[0.4, 0.0, 1.0], [10.0, 0.1, 0.4], [0.4, 0.0, 1.0], [10.0, 0.1, 0.4], [0.4, 0.0, 1.0], [10.0, 0.1, 0.4], [0.1, 0.0, 1.0], [10.0, 0.1, 0.4], [10.0, 0.1, 0.4]], " +
                "[[0.5, 0.0, 1.0], [0.5, 0.0, 1.0], [10.0, 0.1, 0.5], [10.0, 0.1, 0.5], [0.1, 0.0, 1.0], [0.1, 0.0, 1.0], [0.7, 0.0, 1.0], [0.5, 0.0, 1.0], [0.1, 0.0, 1.0]]]");
    }

    @Test
    public void testCSVToAGGScenario4() throws FileNotFoundException {
        AGG test = new AGG(SCENARIO4);
        Assertions.assertEquals(Arrays.deepToString(test.getAGG()), "[[[10.0, 0.1, 0.3], [10.0, 0.1, 0.5], [10.0, 0.0, 0.5], [10.0, 0.1, 0.8]], " +
                "[[10.0, 0.1, 0.2], [10.0, 0.1, 0.8], [10.0, 0.2, 0.8], [10.0, 0.2, 0.9]], " +
                "[[10.0, 0.1, 0.3], [10.0, 0.1, 0.7], [10.0, 0.1, 0.7], [10.0, 0.1, 0.3]]]");
    }

    @Test
    public void testCSVToAGGScenario5() throws FileNotFoundException {
        AGG test = new AGG(SCENARIO5);
        Assertions.assertEquals(Arrays.deepToString(test.getAGG()),
                "[[[0.1, 0.0, 1.0], [10.0, 0.1, 0.5], [10.0, 0.0, 0.5], [10.0, 0.1, 0.8]], " +
                        "[[0.2, 0.0, 1.0], [10.0, 0.1, 0.8], [10.0, 0.2, 0.8], [10.0, 0.2, 0.9]], " +
                        "[[0.3, 0.0, 1.0], [10.0, 0.1, 0.7], [10.0, 0.1, 0.7], [10.0, 0.1, 0.3]]]");
    }

    @Test
    public void testCSVToAGGScenario6() throws FileNotFoundException {
        AGG test = new AGG(SCENARIO6);
        Assertions.assertEquals(Arrays.deepToString(test.getAGG()), "[[[10.0, 0.1, 0.3], [10.0, 0.1, 0.5], [10.0, 0.0, 0.5], [10.0, 0.1, 0.8]], " +
                "[[10.0, 0.1, 0.2], [10.0, 0.1, 0.8], [10.0, 0.2, 0.8], [10.0, 0.2, 0.9]], " +
                "[[10.0, 0.1, 0.3], [10.0, 0.1, 0.7], [10.0, 0.1, 0.7], [10.0, 0.1, 0.3]]]");
    }
}
