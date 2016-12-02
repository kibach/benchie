package com.me.kbocharov.bochbench.benchmark;

/**
 *
 */
public class BenchmarkResult {
    public long numOps;
    public double duration;
    public int iterations;
    public int mulSize;
    public String benchName;

    public BenchmarkResult(long numOps, double duration, int iterations) {
        this.numOps = numOps;
        this.duration = duration;
        this.iterations = iterations;
    }

    public void setMulSize(int _mulSize) {
        this.mulSize = _mulSize;
    }

    public void setBenchName(String benchName) {
        this.benchName = benchName;
    }

    public double getMFLOPS() {
        return numOps / duration / 1e6;
    }

    public void printResult() {
        System.out.printf("%6.3f GFLOPS (%d iterations in %.1f seconds)%n",
                numOps / duration / 1e9,
                iterations,
                duration);
    }
}
