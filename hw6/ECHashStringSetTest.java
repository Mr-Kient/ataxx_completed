import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Darren Wang
 */
public class ECHashStringSetTest  {

    @Test
    public void testAll() {
        ECHashStringSet echash = new ECHashStringSet();

        echash.put("e");
        assertTrue(echash.contains("e"));

        echash.put("c");
        assertTrue(echash.contains("c"));
        List<String> output1 = new ArrayList<>();
        output1.add("e");
        output1.add("c");
        assertEquals(output1, echash.asList());

        echash.put("h");
        List<String> output2 = new ArrayList<>();
        output2.add("e");
        output2.add("c");
        output2.add("h");
        assertEquals(output2, echash.asList());
    }
}
