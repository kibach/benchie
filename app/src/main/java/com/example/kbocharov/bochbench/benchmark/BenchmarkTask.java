package com.example.kbocharov.bochbench.benchmark;

import android.os.AsyncTask;

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

    @Override
    protected List<BenchmarkResult> doInBackground(Void... voids) {
        int[] multiplicationSizes = {10, 100, 1000};
        ArrayList<BenchmarkResult> list = new ArrayList<>();

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
}
