import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AGGTest {

    //5 rows, 7 columns
    String path = "C:/Users/admin/Downloads/scenario_1.csv";
    AGG test = new AGG(path);

    @Test
    public void testCopyFirst(){
        String res = AGG.copyFirst(path);
        Assertions.assertEquals(res, "\uFEFFDM1;;;;;;;;;;;;;;;;;;Gewichte;;a1;a2;a3;a4;a5;a6;a7;a80,1;c1;" +
                "0,5;0,5;0,5;0,5;0,5;0,7;0,5;0,70,2;c2;0,2;0,2;0,2;0,2;0,2;0,1;0,2;0,20,3;c3;0,3;0,3;0,3;0,3;0,3;" +
                "0,1;0,3;0,30,4;c4;0,4;0,4;0,4;0,4;0,4;0,1;0,4;0,40,5;c5;0,5;0,5;0,5;0,1;0,1;0,7;0,5;0,1;;;;;;;;;DM2;;;;;;;;;");
    }

    @Test
    public void testCountColsAndRows(){

    }

}
