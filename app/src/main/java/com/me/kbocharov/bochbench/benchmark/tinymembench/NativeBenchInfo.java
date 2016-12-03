package com.me.kbocharov.bochbench.benchmark.tinymembench;

/**
 * Created by weenn on 03.12.2016.
 */

public class NativeBenchInfo {
    public String description;
    public int use_tmpbuf;
    public int score;
    public byte[] f;

    public NativeBenchInfo(int use_tmpbuf, int score, byte[] f, String description) {
        this.description = description;
        this.f = f;
        this.use_tmpbuf = use_tmpbuf;
        this.score = score;
    }
}
