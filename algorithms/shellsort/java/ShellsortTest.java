import java.util.Arrays;

public class ShellsortTest extends Testable {

    @Test
    public void testGetGapSequence() {
        int n;
        Integer[] expectedGaps;
        Integer[] gaps;
        /**
         * This test case comes directly from the description of Shell Sort at
         * http://www.stoimen.com/blog/2012/02/27/computer-algorithms-shell-sort/.
         */
        n = 1000;
        expectedGaps = new Integer[] { 500, 250, 125, 62, 31, 15, 7, 3, 1 };
        gaps = Shellsort.getGapSequence(n);
        assertThat(Arrays.equals(expectedGaps, gaps));

        /**
         * Simpler test case, where n is a power of 2.
         */
        n = 8;
        expectedGaps = new Integer[] { 4, 2, 1 };
        gaps = Shellsort.getGapSequence(n);
        assertThat(Arrays.equals(expectedGaps, gaps));
    }

    @Test
    public void testIsSorted() {
        int[] empty = {};
        int[] one = { 2 };
        int[] twoSorted = { -1, 0 };
        int[] threeSorted = { -1, 0, 1 };
        int[] twoNotSorted = { 0, -1 };
        int[] threeNotSorted = { -1, 1, 0 };

        assertThat(Util.isSorted(empty));
        assertThat(Util.isSorted(one));
        assertThat(Util.isSorted(twoSorted));
        assertThat(Util.isSorted(threeSorted));
        assertThat(!Util.isSorted(twoNotSorted));
        assertThat(!Util.isSorted(threeNotSorted));
    }

    /**
     * Test the correctness of iterative sort.
     */
    @Test
    public void testIterativeSort() {
        Shellsort ss = new Shellsort();
        int[] list;

        list = new int[] { 6, 7, 0, 4, 8, 9, 1, 3, 2, 5 };
        // Sanity check:
        assertThat(!Util.isSorted(list));
        ss.iterativeSort(list);
        assertThat(Util.isSorted(list));
        // Sanity check:
        assertThat(
                Arrays.equals(
                        new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
                        list));

        list = new int[] {
                274,
                475,
                880,
                45,
                59,
                805,
                746,
                490,
                880,
                643,
                544,
                67,
                126,
                334,
                516,
                616,
                448,
                968,
                265,
                787,
                178,
                881,
                646,
                636,
                454,
                375,
                705,
                175,
                294,
                12 };
        ss.iterativeSort(list);
        assertThat(Util.isSorted(list));
    }

    @Test
    public void testRecursiveSort() {
        Shellsort ss = new Shellsort();
        int[] list;

        list = new int[] { 6, 7, 0, 4, 8, 9, 1, 3, 2, 5 };
        // Sanity check:
        assertThat(!Util.isSorted(list));
        ss.recursiveSort(list);
        assertThat(Util.isSorted(list));
        // Sanity check:
        assertThat(
                Arrays.equals(
                        new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
                        list));

        list = new int[] {
                274,
                475,
                880,
                45,
                59,
                805,
                746,
                490,
                880,
                643,
                544,
                67,
                126,
                334,
                516,
                616,
                448,
                968,
                265,
                787,
                178,
                881,
                646,
                636,
                454,
                375,
                705,
                175,
                294,
                12 };
        ss.recursiveSort(list);
        assertThat(Util.isSorted(list));
    }

    @Test
    public void testSum() {
        assertEqual(0.0, Util.sum(new Double[] {}));
        assertEqual(0.0, Util.sum(new Double[] { 0.0 }));
        assertEqual(1.0, Util.sum(new Double[] { 1.0 }));
        assertEqual(0.0, Util.sum(new Double[] { -1.0, 0., 1.0 }));
        assertEqual(6.0, Util.sum(new Double[] { 1.0, 2.0, 3.0 }));
    }

    @Test
    public void testMean() {
        assertEqual(0.0, Util.mean(new Double[] {}));
        assertEqual(0.0, Util.mean(new Double[] { 0.0 }));
        assertEqual(1.0, Util.mean(new Double[] { 1.0 }));
        assertEqual(0.0, Util.mean(new Double[] { -1.0, 0., 1.0 }));
        assertEqual(2.5, Util.mean(new Double[] { 1.5, 2.5, 3.5 }));
    }

    @Test
    public void testStddev() {
        Double[] samples;
        double stddev;
        double expected;

        samples = new Double[] { 1.0, 2.0, 4.0, 8.0, 16.0, 32.0, 64.0, 128.0 };
        stddev = Util.stddev(samples);
        expected = 41.40784195052913;
        assertEqual(expected, stddev);

        samples = new Double[] { 1.0, 2.0, 3.0 };
        stddev = Util.stddev(samples);
        expected = 0.81649658092772603;
        assertEqual(expected, stddev);

        samples = new Double[] { 1.0, 2.0, -3.0 };
        stddev = Util.stddev(samples);
        expected = 2.1602468994692869;
        assertEqual(expected, stddev);

        // This case is borrowed from an example at
        // https://en.wikipedia.org/wiki/Standard_deviation.
        samples = new Double[] { 2., 4., 4., 4., 5., 5., 7., 9. };
        stddev = Util.stddev(samples);
        expected = 2.0;
        assertEqual(expected, stddev);
    }

    public static void main(String[] args) {
        new Project1Test().runTests(true);
    }
}
