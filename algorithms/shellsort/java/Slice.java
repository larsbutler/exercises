public class Slice<T> {

    private T[] array;
    private int start;
    private int stop;

    /**
     * Slice of `array` defined by the left-bound half-open interval
     * [start, stop).
     *
     * Inspired by Python array slicing semantics and head/tail recursion
     * patterns from functional programming languages like Erlang.
     *
     * @param array
     * @param start
     * @param stop
     */
    public Slice(T[] array, int start, int stop) {
        this.array = array;
        this.start = start;
        this.stop = stop;
        if (start > stop) {
            throw new RuntimeException("start must be <= stop");
        }
    }

    /**
     * Default constructor. Creates a slice over an entire array.
     * @param array
     */
    public Slice(T[] array) {
        this(array, 0, array.length);
    }

    public T head() {
        return this.array[start];
    }

    public Slice<T> tail() {
        // Don't copy the array, just return a new slice.
        // We need to be careful with the contents, since the array is
        // technically mutable.
        return new Slice<>(this.array, this.start + 1, this.stop);
    }

    /**
     * The slice is empty at the end.
     */
    public boolean isEmpty() {
        return this.start == this.stop;
    }
}
