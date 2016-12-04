package com.me.kbocharov.bochbench.benchmark.multiplication;

/**
 *
 * @author mikio
 */
public class Timer {
    long startTime;
    long stopTime;

    public Timer() {
        startTime = -1;
        stopTime = -1;
    }

    public void start() {
        startTime = System.nanoTime();
    }

    public long stop() {
        stopTime = System.nanoTime();
        return stopTime - startTime;
    }

    public boolean ranFor(double seconds) {
        return (System.nanoTime() - startTime) / 1e9 >= seconds;
    }

    public double elapsedSeconds() {
        return (stopTime - startTime) / 1e9;
    }
}
