import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Darren Wang
 */
public class BSTStringSetTest  {

    @Test
    public void testAll() {
        BSTStringSet s = new BSTStringSet();
        s.put("Cat");
        List<String> sList = new ArrayList<>();
        sList.add("Cat");
        assertTrue(s.contains("Cat"));
        assertEquals(sList.size(), s.asList().size());
        assertEquals(sList, s.asList());
        s.put("Cob");
        s.put("Boo");
        sList.add(0, "Boo");
        sList.add("Cob");
        assertEquals("Cat", s.RootString());
        assertEquals("Boo", s.Left(s.Root()));
        assertEquals("Cob", s.Right(s.Root()));
        assertTrue(s.contains("Cob"));
        assertTrue(s.contains("Boo"));
        assertEquals(sList.size(), s.asList().size());
        assertEquals(sList, s.asList());
    }
}
