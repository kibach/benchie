package com.me.kbocharov.bochbench.benchmark.scorer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.me.kbocharov.bochbench.benchmark.multiplication.BenchmarkResult;
import com.me.kbocharov.bochbench.benchmark.tinymembench.LatencyBenchResult;
import com.me.kbocharov.bochbench.benchmark.tinymembench.NativeBenchInfo;
import com.me.kbocharov.bochbench.benchmark.tinymembench.SpeedBenchResult;

/**
 * Created by weenn on 04.12.2016.
 */

public class Attempt {
    public long attempt_id;
    private SQLiteDatabase db;

    public String timepoint;
    public int score;

    Attempt(long attempt_id, SQLiteDatabase db) {
        this.attempt_id = attempt_id;
        this.db = db;
    }

    public void addMultiplicationValue(BenchmarkResult r) {
        ContentValues cv = new ContentValues();
        cv.put("attempt_id", this.attempt_id);
        cv.put("num_ops", r.numOps);
        cv.put("duration", r.duration);
        cv.put("iterations", r.iterations);
        cv.put("mul_size", r.mulSize);
        cv.put("name", r.benchName);
        this.db.insert("multiplication", null, cv);
    }

    public void addSpeedValue(SpeedBenchResult r, NativeBenchInfo bi) {
        ContentValues cv = new ContentValues();
        cv.put("attempt_id", this.attempt_id);
        cv.put("speed", r.speed);
        cv.put("diversity", r.diversity);
        cv.put("name", bi.description);
        cv.put("score", bi.score);
        this.db.insert("speed", null, cv);
    }

    public void addLatencyValue(LatencyBenchResult r, int hugePage) {
        ContentValues cv = new ContentValues();
        cv.put("attempt_id", this.attempt_id);
        cv.put("size", r.size);
        cv.put("single_read", r.single_read);
        cv.put("double_read", r.double_read);
        cv.put("hugepage", hugePage);
        this.db.insert("latency", null, cv);
    }

    public void updateRecord() {
        ContentValues cv = new ContentValues();
        cv.put("id", this.attempt_id);
        cv.put("timepoint", this.timepoint);
        cv.put("score", this.score);
        this.db.replace("attempts", null, cv);
    }

    public String calculateScore() {
        //SELECT ((speed * (1 + diversity) / 2) * score) as mark FROM speed WHERE attempt_id = 5;
        double avgSpeed = 0;
        double avgMul = 0;
        double avgLatency = 0;

        Cursor c = this.db.query(
                "speed",
                new String[] {"((speed * (1 + diversity) / 2) * score) as mark"},
                "attempt_id = ?",
                new String[] { "" + this.attempt_id },
                "", "", ""
        );

        if (c.moveToFirst()) {
            int markColumnId = c.getColumnIndex("mark");
            int markCount = 0;

            do {
                markCount += 1;
                avgSpeed += c.getDouble(markColumnId);
            } while (c.moveToNext());

            avgSpeed /= markCount;
        }

        c = this.db.query(
                "multiplication",
                new String[] {"(num_ops / duration / 1000000) as mark"},
                "attempt_id = ?",
                new String[] { "" + this.attempt_id },
                "", "", ""
        );

        if (c.moveToFirst()) {
            int markColumnId = c.getColumnIndex("mark");
            int markCount = 0;

            do {
                markCount += 1;
                avgMul += c.getDouble(markColumnId);
            } while (c.moveToNext());

            avgMul /= markCount;
        }

        c = this.db.query(
                "latency",
                new String[] {"MAX(double_read) as mark"},
                "attempt_id = ?",
                new String[] { "" + this.attempt_id },
                "", "", ""
        );

        if (c.moveToFirst()) {
            int markColumnId = c.getColumnIndex("mark");
            int markCount = 0;

            do {
                markCount += 1;
                avgLatency += c.getDouble(markColumnId);
            } while (c.moveToNext());

            avgLatency /= markCount / 2;
        }

        double res = avgSpeed + avgMul - avgLatency;

        /*double total = 0;
        for (BenchmarkResult res: resultList) {
            total += res.getMFLOPS();
        }
        total /= resultList.size();
        return "" + Math.round(total);*/
        this.score = (int) Math.round(res);

        updateRecord();

        return "" + this.score;
    }
}
