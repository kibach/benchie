package com.me.kbocharov.bochbench.benchmark;

import com.me.kbocharov.bochbench.benchmark.tinymembench.LatencyBenchResult;

import java.util.List;

/**
 * Created by k.bocharov on 29.11.2016.
 */

public interface BenchmarkResponder {
    public void respondOnBenchmarkEnd(List<BenchmarkResult> resultList, LatencyBenchResult[] latencyBenchResults);
}
