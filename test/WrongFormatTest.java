import org.junit.Test;
import smaa_creation.AGG;

import java.io.FileNotFoundException;

public class WrongFormatTest {

    //test if exception is thrown when calculation receives wrongly formatted agg
    @Test(expected = Exception.class)
    public void testWrongFormat() throws FileNotFoundException {
        new AGG("test/resources/scenario4_wrong_format.csv");
    }

    @Test(expected = Exception.class)
    public void testEmptyFile() throws FileNotFoundException {
        new AGG("test/resources/empty_file.csv");
    }
}
