package com.me.kbocharov.bochbench.benchmark.tinymembench;

import android.util.Log;

/**
 * Created by weenn on 03.12.2016.
 */

public class NativeBenchInfo {
    public String description;
    public int use_tmpbuf;
    public int score;
    public int f;

    public NativeBenchInfo(int use_tmpbuf, int score, int f, String description) {
        this.description = description;
        this.f = f;
        this.use_tmpbuf = use_tmpbuf;
        this.score = score;
    }

    public String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }
}
