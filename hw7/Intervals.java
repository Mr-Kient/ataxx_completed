import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/** HW #7, Sorting ranges.
 *  @author Darren Wang
  */
public class Intervals {
    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals) {
        if (intervals == null) {
            return 0;
        }
        ArrayList<int[]> all = new ArrayList<>();
        for (int[] interval : intervals) {
            all.add(new int[] {interval[0], 1});
            all.add(new int[] {interval[1], -1});
        }
        all.sort((o1, o2) -> {
            if (o1[0] == o2[0]) {
                return o2[1];
            } else {
                return o1[0] - o2[0];
            }
        });
        int start = 0;
        int n = 0;
        int result = 0;
        for (int[] ints : all) {
            if (n == 0) {
                start = ints[0];
            }
            n += ints[1];
            if (n == 0) {
                result += ints[0] - start;
            }
        }
        return result;
    }

    /** Test intervals. */
    static final int[][] INTERVALS = {
        {19, 30},  {8, 15}, {3, 10}, {6, 12}, {4, 5},
    };
    /** Covered length of INTERVALS. */
    static final int CORRECT = 23;

    /** Performs a basic functionality test on the coveredLength method. */
    @Test
    public void basicTest() {
        assertEquals(CORRECT, coveredLength(Arrays.asList(INTERVALS)));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
