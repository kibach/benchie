package com.me.kbocharov.bochbench.benchmark;

import java.util.List;

/**
 * Created by k.bocharov on 29.11.2016.
 */

public interface BenchmarkResponder {
    public void respondOnBenchmarkEnd(List<BenchmarkResult> resultList);
}
