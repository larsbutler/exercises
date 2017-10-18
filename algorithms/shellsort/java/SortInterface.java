/**
 * This is interface was provided by UMUC as part of their
 * CMSC451 course.
 *
 * Credit goes to them, apart from the noted modifcations below.
 */
public interface SortInterface {

    public void recursiveSort(int[] list);

    public void iterativeSort(int[] list);

    // public int getCount();
    /**
     * NOTE(larsbutler): I had to change this interface to return a long
     * because the samples I used and the critical operations I chose
     * yielded operation counts way beyond the max "int" value.
     */
    public long getCount();

    public long getTime();
}
