package com.me.kbocharov.bochbench.benchmark.multiplication;

/**
 *
 */
public interface Benchmark {

    public String getName();

    public BenchmarkResult run(int size, double seconds);
}
