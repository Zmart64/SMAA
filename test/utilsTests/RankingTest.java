package utilsTests;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import smaa_calculation.Utils;
import utilsTestsTools.AGG_Test_Entity;
import utilsTestsTools.FillParameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class RankingTest {

    private final int[] input;
    private final int[] expected;

    @Parameters(name = "Test for: {0} ranking")
    public static Collection<Object[]> rankingData () throws Exception {
        return new FillParameters().fillParameters("ranking");
    }

    public RankingTest(String name, int[] input, int[] expected){
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void test(){
        Assertions.assertArrayEquals(input, expected);
    }
}
