package com.me.kbocharov.bochbench.benchmark;

import org.jblas.DoubleMatrix;
import static org.jblas.DoubleMatrix.*;

/**
 *
 */
class NativeDoubleMultiplicationBenchmark implements Benchmark {

    public String getName() {
        return "native matrix multiplication, double precision";
    }

    private native void prepareStage(int n, double[] A, double[] B, double[] C);
    private native void mmuli();
    private native void cleanupStage();

    public BenchmarkResult run(int size, double seconds) {
        int counter = 0;
        long ops = 0;

        double[] A = randn(size, size).data;
        double[] B = randn(size, size).data;
        double[] C = randn(size, size).data;

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
