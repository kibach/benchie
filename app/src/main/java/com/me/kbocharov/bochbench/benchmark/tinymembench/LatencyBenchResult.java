package com.me.kbocharov.bochbench.benchmark.tinymembench;

/**
 * Created by weenn on 03.12.2016.
 */

public class LatencyBenchResult {
    public int size;
    public double single_read;
    public double double_read;

    public LatencyBenchResult(int size, double single_read, double double_read) {
        this.size = size;
        this.single_read = single_read;
        this.double_read = double_read;
    }
}
