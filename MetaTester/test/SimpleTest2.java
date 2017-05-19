import main.MetaTester;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Oren Afek
 * @since 3/27/2017
 */

@RunWith(MetaTester.class)
public class SimpleTest2 {

    @Test
    public void testF() {
        List<Integer> l = new ArrayList<>();
        assertEquals(0, l.size());
    }

}
