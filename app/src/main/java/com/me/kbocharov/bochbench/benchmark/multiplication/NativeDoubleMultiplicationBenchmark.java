package com.me.kbocharov.bochbench.benchmark.multiplication;

import static org.jblas.DoubleMatrix.*;

/**
 *
 */
public class NativeDoubleMultiplicationBenchmark implements Benchmark {

    public String getName() {
        return "native matrix multiplication, double precision";
    }

    private native void prepareStage(int n, double[] A, double[] B, double[] C);
    private native BenchmarkResult mmuli(double seconds);
    private native void cleanupStage();

    public BenchmarkResult run(int size, double seconds) {
        double[] A = randn(size, size).data;
        double[] B = randn(size, size).data;
        double[] C = randn(size, size).data;

        prepareStage(size, A, B, C);
        BenchmarkResult ret = mmuli(seconds);
        cleanupStage();

        return ret;
    }
}
