
/**
 * Shellsort implementation.
 *
 * Credit for the implementation goes to the pseudocode in the Shellsort
 * article on Wikipedia [1]. Gap sequence generating credit goes to Wikipedia
 * and www.stoimen.com [2].
 *
 * [1] - https://en.wikipedia.org/wiki/Shellsort
 * [2] - http://www.stoimen.com/blog/2012/02/27/computer-algorithms-shell-sort/
 * @author larsbutler
 *
 */
public class Shellsort implements SortInterface {

    /**
     * Operation counter. Keeps track of the number operations required to
     * complete the sort. The number of operations ultimately determines
     * the complexity of the algorithm by itself (as opposed to the runtime,
     * which can vary wildly from machine to machine).
     */
    private long opCount;
    private long runtime;

    /**
     * Generate a gap sequence. For this implementation, we use Donald Shell's
     * gap sequence, defined as N/2^k.
     *
     * Credit: https://en.wikipedia.org/wiki/Shellsort.
     * @param n - input size of the data set
     * @return array of gap values with a length of floor(log2(n)).
     */
    public static Integer[] getGapSequence(int n) {
        // gap array size is floor(log2(n))
        int size = (int) Math.floor(Math.log10(n) / Math.log10(2));
        Integer[] gaps = new Integer[size];
        for (int i = 0; i < size; i++) {
            gaps[i] = (int) (n / Math.pow(2, i + 1));
        }
        return gaps;
    }

    /**
     * This implementation is essentially a direct recursive translation of the
     * iterative version of the algorithm. The iterative version is much
     * more readable as a complete algorithm, but each of the "loop" recursive
     * methods is pretty simple by itself. I guess that's the tradeoff.
     *
     * I wanted to use better variable names than "i" and "j" in the recursive
     * version, but 1) the algorithm definition doesn't really define any more
     * meaningful name, so these variables are just counters and 2) I wanted
     * to be able to show how to draw parallels between the two versions of the
     * algorithm.
     */
    @Override
    public void recursiveSort(int[] list) {
        this.opCount = 0;
        int n = list.length;
        long startTime = System.nanoTime();
        Integer[] gaps = getGapSequence(n);

        // Creating a moveable slice which makes it simpler to iterate
        // over an array through recursion without having to maintain and pass
        // around indices.
        Slice<Integer> gapSlice = new Slice<>(gaps);
        gapLoop(gapSlice, list);
        this.runtime = System.nanoTime() - startTime;
        // Verify that the array is sorted:
        if (!Util.isSorted(list)) {
            throw new UnsortedException();
        }
    }

    /**
     * Loop over the "gap" array.
     *
     * Calls the "outerLoop" on each activation.
     *
     * @param gaps
     * @param list
     */
    private void gapLoop(Slice<Integer> gaps, int[] list) {
        if (gaps.isEmpty()) {
            // We're at the end
            return;
        }
        int gap = gaps.head();
        int i = gap;
        outerLoop(i, gap, list);
        gapLoop(gaps.tail(), list);
    }

    /**
     * Outer loop, counting from gap up to n (the list length).
     *
     * Calls the innerLoop on each activation.
     *
     * @param i
     * @param gap
     * @param list
     */
    private void outerLoop(int i, int gap, int[] list) {
        if (i >= list.length) {
            // Base case
            return;
        }

        int temp = list[i];
        int j = i;
        j = innerLoop(j, gap, temp, list);
        list[j] = temp;
        outerLoop(i + 1, gap, list);
    }

    /**
     * Inner loop of Shellsort. This part of the algorithm actually makes
     * comparisons and rearranges elements. In other words, the actual "sorting"
     * happens here.
     *
     * @param j
     * @param gap
     * @param temp
     * @param list
     * @return the index where the `temp` value needs to be place, after all
     * element shift is done for this part
     */
    private int innerLoop(int j, int gap, int temp, int[] list) {
        // NOTE(larsbutler): The `this.count++` could easily go here as well in
        // order to reflect the comparisons in the "if" below. However, since
        // the recursive algorithm is a direct adaption from the iterative
        // version, I think it makes sense to keep it in the same place (below).
        if (!(j >= gap && list[j - gap] > temp)) {
            // Base case
            return j;
        }
        // Recursive case
        /**
         * Critical operation: element shifting.
         *
         * The advantage this algorithm has over Insertion Sort,
         * which is O(n^2), is that the overall number of element
         * shifting is smaller. At least, that's how I understand it.
         */
        list[j] = list[j - gap];
        this.opCount++;
        return innerLoop(j - gap, gap, temp, list);
    }

    /**
     * Iterative Shellsort. Code adapted directly from pseudocode on Wikipedia.
     * Even the comments below have been shamelessly stolen/borrowed/adapted
     * from Wikipedia; they're pretty helpful to understand the inner workings
     * of the algorithm.
     *
     * Source: https://en.wikipedia.org/wiki/Shellsort.
     */
    @Override
    public void iterativeSort(int[] list) {
        this.opCount = 0;
        int n = list.length;

        // Include gap sequence generation in time measurement, since for any
        // arbitrary n, the gap sequence must be calculated.
        long startTime = System.nanoTime();
        Integer[] gaps = getGapSequence(n);

        // Start with largest gap and work down to a gap of 1.
        // This assumes that the last gap value is _actually_ 1!
        for (Integer gap : gaps) {
            // Do a gapped insertion sort for this gap size.
            // The first gap elements list[0..gap-1] are already in gapped order
            // keep adding one more element until the entire array is gap sorted
            for (int i = gap; i < n; i++) {
                // add list[i] to the elements that have been gap sorted
                // save list[i] in temp and make a hole at position i
                int temp = list[i];
                // shift earlier gap-sorted elements up until the correct
                // location for list[i] is found
                int j;
                for (j = i; j >= gap && list[j - gap] > temp; j -= gap) {
                    /**
                     * Critical operation: element shifting.
                     *
                     * The advantage this algorithm has over Insertion Sort,
                     * which is O(n^2), is that the overall number of element
                     * shifting is smaller. At least, that's how I understand it.
                     */
                    list[j] = list[j - gap];
                    this.opCount++;
                }
                // put temp (the original list[i]) in its correct location
                list[j] = temp;
            }
        }
        this.runtime = System.nanoTime() - startTime;
        // Verify that the array is sorted:
        if (!Util.isSorted(list)) {
            throw new UnsortedException();
        }
    }

    @Override
    public long getCount() {
        if (this.opCount < 0) {
            System.err.printf(
                    "WARNING: possible count rollover - count=%d",
                    this.opCount);
        }
        return this.opCount;
    }

    @Override
    public long getTime() {
        return this.runtime;
    }

}
