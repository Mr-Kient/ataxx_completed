import static java.lang.System.arraycopy;

/** HW #7, Distribution counting for large numbers.
 *  @author Darren Wang
 */
public class SortInts {

    /** Sort A into ascending order.  Assumes that 0 <= A[i] < n*n for all
     *  i, and that the A[i] are distinct. */
    static void sort(long[] A) {
        if (A == null || A.length < 1) {
            return;
        }
        long max = A[0];
        for (int i = 1; i < A.length; i++) {
            max = Math.max(max, A[i]);
        }
        int num = 0;
        while (max > 0) {
            num += 1;
            max >>= 1;
        }

        for (int i = 0; i < num; i++) {
            sortHelper(A, i);
        }
    }

    private static void sortHelper(long[] a, int digit) {
        int MASK = (1 << (digit + 1)) - 1;
        int[] counts = new int[2];
        long[] output = new long[a.length];
        for (long l : a) {
            long c = (l & MASK) >> digit;
            counts[(int) c]++;
        }
        counts[1] += counts[0];
        for (int i = a.length - 1; i >= 0; i--) {
            long c = (a[i] & MASK) >> digit;
            output[counts[(int) c]-- - 1] = a[i];
        }
        arraycopy(output, 0, a, 0, a.length);
    }

}

