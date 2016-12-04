package com.me.kbocharov.bochbench.benchmark;

import com.me.kbocharov.bochbench.benchmark.multiplication.BenchmarkResult;
import com.me.kbocharov.bochbench.benchmark.scorer.Attempt;
import com.me.kbocharov.bochbench.benchmark.tinymembench.LatencyBenchResult;

import java.util.List;

/**
 * Created by k.bocharov on 29.11.2016.
 */

public interface BenchmarkResponder {
    public void respondOnBenchmarkEnd(Attempt attempt);

    public void respondOnProgress(Integer value);
}
