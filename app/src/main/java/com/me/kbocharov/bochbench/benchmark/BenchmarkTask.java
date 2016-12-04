package com.me.kbocharov.bochbench.benchmark;

import android.os.AsyncTask;
import android.util.Log;

import com.me.kbocharov.bochbench.benchmark.multiplication.Benchmark;
import com.me.kbocharov.bochbench.benchmark.multiplication.BenchmarkResult;
import com.me.kbocharov.bochbench.benchmark.multiplication.JavaDoubleMultiplicationBenchmark;
import com.me.kbocharov.bochbench.benchmark.multiplication.JavaFloatMultiplicationBenchmark;
import com.me.kbocharov.bochbench.benchmark.multiplication.NativeDoubleMultiplicationBenchmark;
import com.me.kbocharov.bochbench.benchmark.multiplication.NativeFloatMultiplicationBenchmark;
import com.me.kbocharov.bochbench.benchmark.scorer.Attempt;
import com.me.kbocharov.bochbench.benchmark.scorer.ResultStorage;
import com.me.kbocharov.bochbench.benchmark.tinymembench.LatencyBenchResult;
import com.me.kbocharov.bochbench.benchmark.tinymembench.NativeBenchInfo;
import com.me.kbocharov.bochbench.benchmark.tinymembench.Runner;
import com.me.kbocharov.bochbench.benchmark.tinymembench.SpeedBenchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by k.bocharov on 29.11.2016.
 */

public class BenchmarkTask extends AsyncTask<Void, Integer, Attempt> {
    private static Benchmark[] multiplicationBenchmarks = {
        new JavaDoubleMultiplicationBenchmark(),
        new JavaFloatMultiplicationBenchmark(),
        //new NativeDoubleMultiplicationBenchmark(),
        //new NativeFloatMultiplicationBenchmark()
    };

    protected BenchmarkResponder responder;
    protected Runner runner;
    protected LatencyBenchResult[] lbrs;

    @Override
    protected Attempt doInBackground(Void... voids) {
        Attempt attempt = ResultStorage.getInstance().createAttempt();
        int test_count = 0;
        publishProgress(test_count++);

        int[] multiplicationSizes = {10, 100, 1000};
        ArrayList<BenchmarkResult> list = new ArrayList<>();

        NativeBenchInfo[] nbi_c = runner.getCSpeedBench();
        NativeBenchInfo[] nbi_libc = runner.getLibCSpeedBench();
        NativeBenchInfo[] nbi_asm = runner.getAsmSpeedBench();
        NativeBenchInfo[] nbi_fb = runner.getFrameBufferBench();

        NativeBenchInfo[][] nbis = {nbi_c, nbi_libc, nbi_asm, nbi_fb};

        runner.setUpStage();

        for (NativeBenchInfo[] nbi: nbis) {
            for (NativeBenchInfo bench: nbi) {
                SpeedBenchResult sbr = runner.runSpeedBench(bench);
                attempt.addSpeedValue(sbr, bench);
                publishProgress(test_count++);
            }
        }

        /*LatencyBenchResult[] lbrs_huge = runner.runLatencyBench(Runner.MADV_HUGEPAGE);
        publishProgress(test_count++);
        LatencyBenchResult[] lbrs_nohuge = runner.runLatencyBench(Runner.MADV_NOHUGEPAGE);
        publishProgress(test_count++);
        if (lbrs_huge.length == 0 || lbrs_nohuge.length == 0) {
            lbrs = runner.runLatencyBench(Runner.MADV_NONE);
        } else {
            lbrs = new LatencyBenchResult[0];
            /*lbrs = new LatencyBenchResult[lbrs_huge.length];
            for (int i = 0; i < lbrs_huge.length; ++i) {
                lbrs[i].size = lbrs_huge[i].size;
                lbrs[i].single_read = (lbrs_huge[i].single_read + lbrs_nohuge[i].single_read) / 2;
                lbrs[i].double_read = (lbrs_huge[i].double_read + lbrs_nohuge[i].double_read) / 2;
            }
        }

        for (LatencyBenchResult lbr: lbrs) {
            attempt.addLatencyValue(lbr, Runner.MADV_NONE);
        }
        for (LatencyBenchResult lbr: lbrs_huge) {
            attempt.addLatencyValue(lbr, Runner.MADV_HUGEPAGE);
        }
        for (LatencyBenchResult lbr: lbrs_nohuge) {
            attempt.addLatencyValue(lbr, Runner.MADV_NOHUGEPAGE);
        }*/

        runner.turnDownStage();

        /*
        for (Benchmark b : multiplicationBenchmarks) {
            for (int n : multiplicationSizes) {
                BenchmarkResult result = b.run(n, 5.0);
                result.setMulSize(n);
                result.setBenchName(b.getName());
                attempt.addMultiplicationValue(result);
                publishProgress(test_count++);
            }
        }*/

        return attempt;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        responder.respondOnProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Attempt att) {
        super.onPostExecute(att);
        responder.respondOnBenchmarkEnd(att);
    }

    public void setResponder(BenchmarkResponder responder) {
        this.responder = responder;
    }

    public void setRunner(Runner r) {
        this.runner = r;
    }
}
