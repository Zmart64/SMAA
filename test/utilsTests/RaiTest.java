package utilsTests;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import utilsTestsTools.FillParameters;

import java.util.Collection;

@RunWith(Parameterized.class)
public class RaiTest {

    private final double[][] input;
    private final double[][] expected;

    @Parameters(name = "Test for: {0} rai")
    public static Collection<Object[]> raiData () throws Exception {
        return new FillParameters().fillParameters("rai");
    }


    public RaiTest(String name, double[][] input, double[][] expected){
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void test(){
        Assertions.assertArrayEquals(input, expected);
    }
}
