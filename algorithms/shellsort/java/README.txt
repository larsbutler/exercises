This code was written during my attendance of CMSC 451 (Design and Analysis of
Computer Algorithms) as UMUC during the 2017 Spring semester.

A few touch-ups and modifications have been made from the original code.

---

Compile and running the code, with recommended/required stack memory settings:

$ javac *.java
$ java -Xss256m SortMain

Expected output:

Computing n=4096...
Computing n=8092...
Computing n=16384...
Computing n=32768...
Computing n=65536...
Computing n=131072...
Computing n=262144...
Computing n=524288...
Computing n=1048576...
Computing n=2097152...
---------------------------------------------------------------------------------------------------------------------------------------------------
| DataSetSize (n) | Iterative                                                     | Recursive                                                     |
|                 | AvgOpCount    | StdOpCount    | AvgTime(secs) | StdTime(secs) | AvgOpCount    | StdOpCount    | AvgTime(secs) | StdTime(secs) |
---------------------------------------------------------------------------------------------------------------------------------------------------
| 00004096        | 1.354e+05     | 1.546e+04     | 7.850e-04     | 1.220e-03     | 1.354e+05     | 1.546e+04     | 7.246e-04     | 2.933e-04     |
| 00008092        | 1.085e+05     | 4.265e+03     | 8.730e-04     | 9.310e-05     | 1.085e+05     | 4.265e+03     | 1.121e-03     | 1.814e-04     |
| 00016384        | 1.150e+06     | 1.933e+05     | 3.629e-03     | 5.053e-04     | 1.150e+06     | 1.933e+05     | 4.521e-03     | 7.330e-04     |
| 00032768        | 3.146e+06     | 5.271e+05     | 8.662e-03     | 1.089e-03     | 3.146e+06     | 5.271e+05     | 1.090e-02     | 1.581e-03     |
| 00065536        | 8.974e+06     | 1.504e+06     | 2.374e-02     | 2.857e-03     | 8.974e+06     | 1.504e+06     | 2.957e-02     | 3.625e-03     |
| 00131072        | 2.609e+07     | 4.110e+06     | 6.463e-02     | 7.302e-03     | 2.609e+07     | 4.110e+06     | 8.355e-02     | 1.077e-02     |
| 00262144        | 6.950e+07     | 9.398e+06     | 1.633e-01     | 1.937e-02     | 6.950e+07     | 9.398e+06     | 2.150e-01     | 2.977e-02     |
| 00524288        | 2.046e+08     | 2.424e+07     | 4.650e-01     | 4.838e-02     | 2.046e+08     | 2.424e+07     | 6.420e-01     | 9.660e-02     |
| 01048576        | 5.574e+08     | 8.335e+07     | 1.183e+00     | 1.372e-01     | 5.574e+08     | 8.335e+07     | 1.770e+00     | 3.356e-01     |
| 02097152        | 1.603e+09     | 2.188e+08     | 3.490e+00     | 4.293e-01     | 1.603e+09     | 2.188e+08     | 5.768e+00     | 1.010e+00     |
---------------------------------------------------------------------------------------------------------------------------------------------------

Compiling and running unit tests:

$ javac *.java
$ java ShellsortTest

Expected output:

testIsSorted: pass
testMean: pass
testIterativeSort: pass
testSum: pass
testRecursiveSort: pass
testStddev: pass
testGetGapSequence: pass
7/7 tests ran successfully (failed: 0)
