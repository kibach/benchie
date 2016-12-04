package com.me.kbocharov.bochbench.benchmark.multiplication;

import static org.jblas.FloatMatrix.*;

/**
 *
 */
public class NativeFloatMultiplicationBenchmark implements Benchmark {

    public String getName() {
        return "native matrix multiplication, single precision";
    }

    private native void prepareStage(int n, float[] A, float[] B, float[] C);
    private native BenchmarkResult mmuli(double seconds);
    private native void cleanupStage();

    public BenchmarkResult run(int size, double seconds) {
        float[] A = randn(size, size).data;
        float[] B = randn(size, size).data;
        float[] C = randn(size, size).data;

        prepareStage(size, A, B, C);
        BenchmarkResult ret = mmuli(seconds);
        cleanupStage();

        return ret;
    }
}
