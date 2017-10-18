public class Util {

    public static Double sum(Number[] nums) {
        Double sum = 0.0;
        for (Number n : nums) {
            sum += n.doubleValue();
        }
        return sum;
    }

    public static Double mean(Number[] nums) {
        if (nums.length == 0) {
            return 0.0;
        }
        return sum(nums) / nums.length;
    }

    /**
     * Calculate the standard deviation of an array of numbers.
     * Formula/algorithm for calculating stddev from:
     * - https://www.mathsisfun.com/data/standard-deviation-formulas.html
     * - https://docs.scipy.org/doc/numpy/reference/generated/numpy.std.html
     *
     * @param nums
     * @return standard deviation of `samples`
     */
    public static Double stddev(Number[] nums) {
        final int n = nums.length;
        final Double mean = sum(nums) / n;

        // Squared deviations: (x - mean)^2 for each x in samples
        Double[] squaredDevs = new Double[n];
        for (int i = 0; i < n; i++) {
            squaredDevs[i] = Math.pow(nums[i].doubleValue() - mean, 2.0);
        }

        Double sumSquaredDevs = sum(squaredDevs);
        Double stddev = Math.sqrt(sumSquaredDevs / n);
        return stddev;
    }

    /**
     * Utility method to check if an array is sorted.
     * @param array
     * @return true if the array is sorted, else false
     */
    public static boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            // if previous value is larger than the current one,
            // array is not sorted
            if (array[i - 1] > array[i]) {
                return false;
            }
        }
        return true;
    }
}
