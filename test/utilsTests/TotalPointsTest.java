package utilsTests;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utilsTestsTools.FillParameters;

import java.util.Collection;

@RunWith(Parameterized.class)
public class TotalPointsTest {

    private final double[] input;
    private final double[] expected;

    @Parameterized.Parameters(name = "Test for: {0} total points")
    public static Collection<Object[]> totalPointsData () throws Exception {
        return new FillParameters().fillParameters("totalPoints");
    }


    public TotalPointsTest(String name, double[] input, double[] expected){
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void test(){
        Assertions.assertArrayEquals(input, expected);
    }
}
