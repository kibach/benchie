package com.me.kbocharov.bochbench.benchmark;

import org.jblas.FloatMatrix;
import static org.jblas.FloatMatrix.*;

/**
 *
 */
class NativeFloatMultiplicationBenchmark implements Benchmark {

    public String getName() {
        return "native matrix multiplication, single precision";
    }

    private native void prepareStage(int n, float[] A, float[] B, float[] C);
    private native void mmuli();
    private native void cleanupStage();

    public BenchmarkResult run(int size, double seconds) {
        int counter = 0;
        long ops = 0;

        float[] A = randn(size, size).data;
        float[] B = randn(size, size).data;
        float[] C = randn(size, size).data;

        prepareStage(size, A, B, C);
        Timer t = new Timer();
        t.start();
        while (!t.ranFor(seconds)) {
            mmuli();
            counter++;
            ops += 2L * size * size * size;
        }
        t.stop();
        cleanupStage();

        return new BenchmarkResult(ops, t.elapsedSeconds(), counter);
    }
}
