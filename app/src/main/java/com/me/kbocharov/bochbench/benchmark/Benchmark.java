package com.me.kbocharov.bochbench.benchmark;

/**
 *
 */
interface Benchmark {

    public String getName();

    public BenchmarkResult run(int size, double seconds);
}
