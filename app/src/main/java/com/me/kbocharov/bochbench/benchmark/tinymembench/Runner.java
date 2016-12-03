package com.me.kbocharov.bochbench.benchmark.tinymembench;

import android.util.Log;

/**
 * Created by k.bocharov on 02.12.2016.
 */

public class Runner {
    static {
        System.loadLibrary("tinymembench");
    }

    public static int MADV_HUGEPAGE = 1;
    public static int MADV_NONE = 0;
    public static int MADV_NOHUGEPAGE = -1;

    public native NativeBenchInfo[] getCSpeedBench();
    public native NativeBenchInfo[] getLibCSpeedBench();
    public native NativeBenchInfo[] getAsmSpeedBench();
    public native NativeBenchInfo[] getFrameBufferBench();
    public native void setUpStage();
    public native void turnDownStage();
    public native SpeedBenchResult runSpeedBench(NativeBenchInfo nbi);
    public native LatencyBenchResult[] runLatencyBench(int useHugepage);

    public void doLog(String s) {
        Log.d("NATIVE", s);
    }
}
