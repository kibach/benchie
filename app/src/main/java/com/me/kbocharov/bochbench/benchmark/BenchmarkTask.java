package com.me.kbocharov.bochbench.benchmark;

import android.os.AsyncTask;

import com.me.kbocharov.bochbench.benchmark.tinymembench.NativeBenchInfo;
import com.me.kbocharov.bochbench.benchmark.tinymembench.Runner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by k.bocharov on 29.11.2016.
 */

public class BenchmarkTask extends AsyncTask<Void, Void, List<BenchmarkResult>> {
    private static Benchmark[] multiplicationBenchmarks = {
        new JavaDoubleMultiplicationBenchmark(),
        new JavaFloatMultiplicationBenchmark(),
        //new NativeDoubleMultiplicationBenchmark(),
        //new NativeFloatMultiplicationBenchmark()
    };

    protected BenchmarkResponder responder;
    protected Runner runner;

    @Override
    protected List<BenchmarkResult> doInBackground(Void... voids) {
        int[] multiplicationSizes = {10, 100, 1000};
        ArrayList<BenchmarkResult> list = new ArrayList<>();

        NativeBenchInfo[] nbis = runner.getCSpeedBench();

        runner.setUpStage();
        runner.runSpeedBench(nbis[0]);
        runner.turnDownStage();

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
        responder.respondOnBenchmarkEnd(resultList);
    }

    public void setResponder(BenchmarkResponder responder) {
        this.responder = responder;
    }

    public void setRunner(Runner r) {
        this.runner = r;
    }
}
