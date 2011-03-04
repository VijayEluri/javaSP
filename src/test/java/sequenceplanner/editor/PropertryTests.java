package sequenceplanner.editor;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import sequenceplanner.general.SP;
import static org.junit.Assert.*;

/**
 *
 * @author patrik
 */
public class PropertryTests {

    public PropertryTests() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testtest() {
        SP sp = new SP();
        sp.loadFromSOPXFile("resources/filesForTesting/fileForTesting.sopx");
        sp.insertOperation();
        sp.saveToSOPXFile("C:/Users/patrik/Desktop/result.sopx");
    }

    /**
     * test of id 100
     */
    @Test
    public void id100() {

        //Insert property (A) with name Adam
        //Insert property (B) with name Bertil

        //assertTrue(all property names are different);

        //Insert propery (C) with name empty
        //assertTrue(nbr of properties == 2)

        //Insert property (D) with name Ad
        //assertTrue(nbr of properties == 3)

        //Change name of B to empty
        //assertTrue(nbr of properties == 3)
        //assertTrue(A.getName.equals("Adam"))
        //assertTrue(B.getName.equals("Bertil"))
        //assertTrue(C.getName.equals("Ad"))

        //Change name of B to Adam
        //assertTrue(nbr of properties == 3)
        //assertTrue(A.getName.equals("Adam"))
        //assertTrue(B.getName.equals("Bertil"))
        //assertTrue(C.getName.equals("Ad"))

        //Insert value (1) with name ett to A
        //Insert value (2) with name tv� to A
        //assertTrue(A."nbr of values" == 2)

        //Change name of 1 to empty
        //assertTrue(A."nbr of values" == 2)
        //assertTrue(1.getName.equals("ett"))
        //assertTrue(2.getName.equals("tv�"))

        //Change name of 1 to tv�
        //assertTrue(A."nbr of values" == 2)
        //assertTrue(1.getName.equals("ett"))
        //assertTrue(2.getName.equals("tv�"))

        //Insert value (3) with name ett to B
        //assertTrue(B."nbr of values" == 1)
        //assertTrue(3.getName.equals("ett"))
        //assertTrue(A."nbr of values" == 2)
        //assertTrue(1.getName.equals("ett"))
        //assertTrue(2.getName.equals("tv�"))

        //Insert value (4) with name empty to C
        //assertTrue(C."nbr of values" == 0)
    }

}