
public class SortMain {

    /**
     * Default random seed to use for testing, in order to
     * reproduce exact test runs with same random samples and
     * results.
     */
    public static final int DEFAULT_RANDOM_SEED = 1337;

    /**
     * PLEASE NOTE: In order to run this benchmarking with the default
     * input sizes, additional stack memory beyond the default JVM setting
     * is required (for the recursive algorithm).
     *
     * Recommended memory settings are "-Xss256m" (256mb).
     *
     * You can specify this on the command line by running:
     *
     *      java -Xss256m SortMain
     */
    public static void main(String[] args) {
        BenchmarkSorts bs = new BenchmarkSorts(
                BenchmarkSorts.DEFAULT_INPUT_SIZES,
                DEFAULT_RANDOM_SEED);
        bs.runSorts();
        bs.displayReport();
    }

}
