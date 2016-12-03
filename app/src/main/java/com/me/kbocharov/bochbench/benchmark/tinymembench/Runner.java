package com.me.kbocharov.bochbench.benchmark.tinymembench;

/**
 * Created by k.bocharov on 02.12.2016.
 */

public class Runner {


    static {
        System.loadLibrary("tmblib");
    }

    public native String stringFromJNI();
}
