package utilsTests;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import utilsTestsTools.FillParameters;

import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class ExclusionTest {

    private final List<Integer> input;
    private final List<Integer> expected;

    @Parameters(name = "Test for: {0} exclusion")
    public static Collection<Object[]> exclusionTest () throws Exception {
        return new FillParameters().fillParameters("exclusion");
    }


    public ExclusionTest(String name, List<Integer> input, List<Integer> expected){
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void test(){
        Assertions.assertEquals(input, expected);
    }
}
