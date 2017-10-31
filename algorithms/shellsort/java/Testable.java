
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Extend this class is a simple base for implementing test cases,
 * without having to pull in something big and heavy like JUnit.
 *
 */
public class Testable {

    /**
     * Simple test annotation, to indicate that a method is a test method.
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Test {
    }

    /**
     * Custom exception class which is raised on test failures.
     *
     */
    public class TestItFailure extends RuntimeException {
        public TestItFailure() {
        }

        public TestItFailure(String message) {
            super(message);
        }
    }

    /**
     * Assert that a boolean expression is true.
     *
     * @param expr boolean expression to test
     * @throws TestItFailure if the expression is false
     */
    public void assertThat(boolean expr) throws TestItFailure {
        if (!expr) {
            throw new TestItFailure();
        }
    }

    /**
     * Assert that two objects are equal.
     *
     * @throws TestItFailure if the objects are not equal
     */
    public void assertEqual(Object a, Object b) throws TestItFailure {
        if (!a.equals(b)) {
            throw new TestItFailure(
                    String.format("%s != %s", a.toString(), b.toString()));
        }
    }

    /**
     * Explicitly fail and raise a {@link Testable.TestItFailure}.
     */
    public void fail() {
        assertThat(false);
    }

    /**
     * Default `runTests` method; `verbose` defaults to false.
     * See below.
     */
    public void runTests() {
        runTests(false);
    }

    /**
     * Run all tests in this class and display test results to stdout.
     * If all tests pass, exit 0. If any fail, exit 1.
     *
     * @param verbose: if true, print stack traces of errors to stderr;
     * otherwise, just show "pass" or "fail" for each test.
     */
    public void runTests(boolean verbose) {
        try {
            int failures = doRunTests(verbose);
            if (failures > 0) {
                // If there are any failures, we should return
                // with a proper exit code indicating the failure.
                System.exit(1);
            }
        } catch (Exception e) {
            // Something unexpected happened.
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Override in subclasses to specify custom setup commands for each test
     * method.
     */
    public void setUp() {
    }

    /**
     * Override in subclasses to specify custom teardown commands for each test
     * method.
     */
    public void tearDown() {
    }

    private int doRunTests(
            boolean verbose) throws IllegalAccessException, IllegalArgumentException {

        int testCount = 0;
        int passCount = 0;
        int failCount = 0;

        for (Method meth : this.getClass().getDeclaredMethods()) {
            // Check for `@Test` annotations, in order to find test methods.
            Annotation[] annots = meth
                    .getAnnotationsByType(Testable.Test.class);
            if (annots.length > 0) {
                testCount++;
                boolean pass = true;
                try {
                    this.setUp();
                    meth.invoke(this);
                } catch (InvocationTargetException ex) {
                    if (verbose) {
                        System.err
                                .println("Error in method: " + meth.getName());
                        ex.getTargetException().printStackTrace();
                    }
                    pass = false;
                } finally {
                    // Always teardown, no matter what
                    this.tearDown();
                }

                if (pass) {
                    passCount++;
                } else {
                    failCount++;
                }
                System.out.printf(
                        "%s: %s\n",
                        meth.getName(),
                        pass ? "pass" : "fail");
            }
        }
        System.out.printf(
                "%d/%d tests ran successfully (failed: %d)\n",
                passCount,
                testCount,
                failCount);
        return failCount;
    }

}
