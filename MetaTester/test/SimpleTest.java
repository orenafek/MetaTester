import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;
/**
 * Created by oren.afek on 3/26/2017.
 */

@RunWith(MetaTester.class)
public class SimpleTest {

    @Test
    public void testF(){
        System.out.println("");
        assertEquals(1,1);
        assertEquals(1,2);
        System.out.println("");
        assertTrue(true);
    }

}
