import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BenchmarkSorts {

    /**
     * Range of input sizes as powers of two.
     *
     * This should give a nice variety of input set sizes
     * to indicate a distinct trend in the performance of the
     * algorithm as "n" increases.
     */
    public static final int[] DEFAULT_INPUT_SIZES = {
            4096,
            8092,
            16384,
            32768,
            65536,
            131072,
            262144,
            524288,
            1048576,
            2097152,
    };
    /**
     * Samples per input sizes per implementation of algorithm.
     */
    public static final int NUM_SAMPLES = 50;
    public static final String REPORT_HEADER_ROW_1 = ("| DataSetSize (n) | Iterative                                     "
            + "                | Recursive                                                     |");
    public static final String REPORT_STATS_COLUMNS = " AvgOpCount    | StdOpCount    | AvgTime(secs) | StdTime(secs) |";
    public static final String REPORT_HEADER_ROW_2 = "|                 |"
            + REPORT_STATS_COLUMNS + REPORT_STATS_COLUMNS;
    public static final String REPORT_COLUMN_VALUE_FMT = " %6.3e     |";
    public static final String REPORT_LINE_FMT = String.format(
            "| %%08d        |%s%s%s%s%s%s%s%s\n",
            REPORT_COLUMN_VALUE_FMT,
            REPORT_COLUMN_VALUE_FMT,
            REPORT_COLUMN_VALUE_FMT,
            REPORT_COLUMN_VALUE_FMT,
            REPORT_COLUMN_VALUE_FMT,
            REPORT_COLUMN_VALUE_FMT,
            REPORT_COLUMN_VALUE_FMT,
            REPORT_COLUMN_VALUE_FMT);
    public static final int TABLE_WIDTH = 147;
    /**
     * Credit for this string repetition trick goes to:
     * http://stackoverflow.com/questions/1235179/simple-way-to-repeat-a-string-in-java
     */
    public static final String HR = String
            .format("%0" + TABLE_WIDTH + "d", 0)
            .replace("0", "-");

    private int[] sizes;
    private Random random;
    private List<Benchmark> iterBenchmarks;
    private List<Benchmark> recurBenchmarks;

    /**
     * Default input sizes and a random random seed.
     */
    public BenchmarkSorts() {
        this(DEFAULT_INPUT_SIZES);
    }

    /**
     * Default input sizes and a custom random seed.
     * @param randomSeed
     */
    public BenchmarkSorts(long randomSeed) {
        this(DEFAULT_INPUT_SIZES, randomSeed);
    }

    /**
     * Custom input sizes and a random random seed.
     * @param sizes
     */
    public BenchmarkSorts(int[] sizes) {
        this(sizes, new Random().nextLong());
    }

    /**
     * Custom input sizes and custom random seed.
     * @param sizes
     * @param randomSeed
     */
    public BenchmarkSorts(int[] sizes, long randomSeed) {
        this.sizes = sizes;
        this.random = new Random(randomSeed);
    }

    public List<Benchmark> getIterativeBenchmarks() {
        return iterBenchmarks;
    }

    public List<Benchmark> getRecursiveBenchmarks() {
        return recurBenchmarks;
    }

    /**
     * Generate an array of size `n` and populate it with random integer values.
     *
     * The `Random` object is a parameter so that the random generation can be
     * seeded for testing purposes.
     *
     * @param n
     * @param random
     * @return array of random integers
     */
    public static int[] generateInput(int n, Random random) {
        int[] input = new int[n];
        for (int i = 0; i < n; i++) {
            input[i] = random.nextInt();
        }
        return input;
    }

    public void runSorts() {
        Shellsort ss = new Shellsort();

        // Store all benchmarks here:
        iterBenchmarks = new ArrayList<>();
        recurBenchmarks = new ArrayList<>();

        // Loop over sizes:
        for (int size : this.sizes) {
            System.out.printf("Computing n=%d...\n", size);
            // Track benchmark data for a given input size:
            Benchmark iterBenchmark = new Benchmark(size);
            Benchmark recurBenchmark = new Benchmark(size);
            // Loop over samples:
            for (int i = 0; i < NUM_SAMPLES; i++) {
                int[] input = generateInput(size, this.random);
                // WARNING: Make sure the copy the input before sorting!
                int[] iterWorkingCopy = Arrays.copyOf(input, input.length);
                int[] recurWorkingCopy = Arrays.copyOf(input, input.length);

                /**
                 * Iterative case.
                 */
                ss.iterativeSort(iterWorkingCopy);
                iterBenchmark.addCount(ss.getCount());
                iterBenchmark.addTime(ss.getTime());

                /**
                 * Recursive case.
                 */
                ss.recursiveSort(recurWorkingCopy);
                recurBenchmark.addCount(ss.getCount());
                recurBenchmark.addTime(ss.getTime());
            }
            iterBenchmarks.add(iterBenchmark);
            recurBenchmarks.add(recurBenchmark);
        }
    }

    public void displayReport() {
        // Sanity check: make sure iterative/recursive benchmark lists are the
        // same size.
        if (iterBenchmarks.size() != recurBenchmarks.size()) {
            throw new RuntimeException(
                    "Something bad happened: Benchmark lists have non-uniform size!");
        }
        System.out.println(HR);
        System.out.println(REPORT_HEADER_ROW_1);
        System.out.println(REPORT_HEADER_ROW_2);
        System.out.println(HR);

        List<Double> iterAvgOpCounts = new ArrayList<>();
        List<Double> iterStddevOpCounts = new ArrayList<>();
        List<Double> iterAvgTimes = new ArrayList<>();
        List<Double> iterStddevTimes = new ArrayList<>();

        List<Double> recurAvgOpCounts = new ArrayList<>();
        List<Double> recurStddevOpCounts = new ArrayList<>();
        List<Double> recurAvgTimes = new ArrayList<>();
        List<Double> recurStddevTimes = new ArrayList<>();

        for (int i = 0; i < iterBenchmarks.size(); i++) {
            Benchmark ib = iterBenchmarks.get(i);
            Benchmark rb = recurBenchmarks.get(i);
            System.out.printf(
                    REPORT_LINE_FMT,
                    ib.getDataSetSize(),
                    ib.getAverageCriticalOperationCount(),
                    ib.getStddevCriticalOperationCount(),
                    ib.getAverageTime(),
                    ib.getStddevTime(),
                    rb.getAverageCriticalOperationCount(),
                    rb.getStddevCriticalOperationCount(),
                    rb.getAverageTime(),
                    rb.getStddevTime());

            iterAvgOpCounts.add(ib.getAverageCriticalOperationCount());
            iterStddevOpCounts.add(ib.getStddevCriticalOperationCount());
            iterAvgTimes.add(ib.getAverageTime());
            iterStddevTimes.add(ib.getStddevTime());

            recurAvgOpCounts.add(rb.getAverageCriticalOperationCount());
            recurStddevOpCounts.add(rb.getStddevCriticalOperationCount());
            recurAvgTimes.add(rb.getAverageTime());
            recurStddevTimes.add(rb.getStddevTime());
        }
        System.out.println(HR);
    }

}
