package com.me.kbocharov.bochbench.benchmark;

import android.os.AsyncTask;
import android.util.Log;

import com.me.kbocharov.bochbench.benchmark.tinymembench.LatencyBenchResult;
import com.me.kbocharov.bochbench.benchmark.tinymembench.NativeBenchInfo;
import com.me.kbocharov.bochbench.benchmark.tinymembench.Runner;
import com.me.kbocharov.bochbench.benchmark.tinymembench.SpeedBenchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by k.bocharov on 29.11.2016.
 */

public class BenchmarkTask extends AsyncTask<Void, Void, List<BenchmarkResult>> {
    private static Benchmark[] multiplicationBenchmarks = {
        //new JavaDoubleMultiplicationBenchmark(),
        //new JavaFloatMultiplicationBenchmark(),
        new NativeDoubleMultiplicationBenchmark(),
        new NativeFloatMultiplicationBenchmark()
    };

    protected BenchmarkResponder responder;
    protected Runner runner;
    protected LatencyBenchResult[] lbrs;

    @Override
    protected List<BenchmarkResult> doInBackground(Void... voids) {
        int[] multiplicationSizes = {10, 100, 1000};
        ArrayList<BenchmarkResult> list = new ArrayList<>();

        NativeBenchInfo[] nbis = runner.getCSpeedBench();

        /*runner.setUpStage();

        SpeedBenchResult sbr = runner.runSpeedBench(nbis[0]);
        LatencyBenchResult[] lbrs_huge = runner.runLatencyBench(Runner.MADV_HUGEPAGE);
        LatencyBenchResult[] lbrs_nohuge = runner.runLatencyBench(Runner.MADV_NOHUGEPAGE);
        if (lbrs_huge.length == 0 || lbrs_nohuge.length == 0) {
            lbrs = runner.runLatencyBench(Runner.MADV_NONE);
            Log.d("bench", "lbrs size: " + lbrs.length);
        } else {
            lbrs = new LatencyBenchResult[lbrs_huge.length];
            for (int i = 0; i < lbrs_huge.length; ++i) {
                lbrs[i].size = lbrs_huge[i].size;
                lbrs[i].single_read = (lbrs_huge[i].single_read + lbrs_nohuge[i].single_read) / 2;
                lbrs[i].double_read = (lbrs_huge[i].double_read + lbrs_nohuge[i].double_read) / 2;
            }
        }
        runner.turnDownStage();*/

        for (Benchmark b : multiplicationBenchmarks) {
            for (int n : multiplicationSizes) {
                BenchmarkResult result = b.run(n, 5.0);
                result.setMulSize(n);
                result.setBenchName(b.getName());
                list.add(result);
            }
        }

        return list;
    }

    @Override
    protected void onPostExecute(List<BenchmarkResult> resultList) {
        super.onPostExecute(resultList);
        responder.respondOnBenchmarkEnd(
                resultList,
                lbrs
        );
    }

    public void setResponder(BenchmarkResponder responder) {
        this.responder = responder;
    }

    public void setRunner(Runner r) {
        this.runner = r;
    }
}
