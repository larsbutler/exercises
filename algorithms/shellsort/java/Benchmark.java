import java.util.ArrayList;
import java.util.List;

/**
 * Storage for benchmark information
 * @author larsbutler
 *
 */
public class Benchmark {

    public static final double NANOS_PER_SECOND = 1E9;

    private final int dataSetSize;
    // Operation counts
    private final List<Long> counts;
    // Sorting runtimes, in seconds
    private final List<Double> times;

    public Benchmark(int dataSetSize) {
        this.dataSetSize = dataSetSize;
        this.counts = new ArrayList<>();
        this.times = new ArrayList<>();
    }

    /**
     * Add a runtime operation count.
     * @param count
     */
    public void addCount(long count) {
        counts.add(count);
    }

    /**
     * Add a time in nano seconds, store it in seconds.
     *
     * This conversion may cause a bit of precision loss, but
     * it makes the values more human-readable.
     * @param nanoTime
     */
    public void addTime(long nanoTime) {
        times.add(nanoTime / NANOS_PER_SECOND);
    }

    public List<Long> getCounts() {
        return counts;
    }

    public List<Double> getTimes() {
        return times;
    }

    public int getDataSetSize() {
        return dataSetSize;
    }

    public double getAverageCriticalOperationCount() {
        return Util.mean(this.counts.toArray(new Long[this.counts.size()]));
    }

    public double getStddevCriticalOperationCount() {
        return Util.stddev(this.counts.toArray(new Long[this.counts.size()]));
    }

    public double getAverageTime() {
        return Util.mean(this.times.toArray(new Double[this.times.size()]));
    }

    public double getStddevTime() {
        return Util.stddev(this.times.toArray(new Double[this.times.size()]));
    }

}
