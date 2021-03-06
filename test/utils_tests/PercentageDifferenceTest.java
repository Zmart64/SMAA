package utils_tests;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import utils_tests_tools.FillParameters;

import java.util.Collection;

@RunWith(Parameterized.class)
public class PercentageDifferenceTest {

    private final double[] input;
    private final double[] expected;

    @Parameters(name = "Test for: {0} percentageDifference")
    public static Collection<Object[]> percentageDifferenceData () throws Exception {
        return new FillParameters().fillParameters("percentageDifference");
    }


    public PercentageDifferenceTest(String name, double[] input, double[] expected){
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void test(){
        Assertions.assertArrayEquals(input, expected);
    }
}
