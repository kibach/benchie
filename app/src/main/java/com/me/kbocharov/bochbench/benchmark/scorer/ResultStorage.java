package com.me.kbocharov.bochbench.benchmark.scorer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by weenn on 04.12.2016.
 */
public class ResultStorage {
    private static ResultStorage ourInstance = new ResultStorage();

    public static ResultStorage getInstance() {
        return ourInstance;
    }

    private ResultStorage() {
    }

    private SQLiteDatabase db;

    public void setDB(SQLiteDatabase db) {
        this.db = db;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public Attempt createAttempt() {
        ContentValues cv = new ContentValues();
        cv.put("timepoint", getDateTime());
        cv.put("score", 0);
        long rowId = this.db.insert("attempts", null, cv);
        return new Attempt(rowId, this.db);
    }

    public List<Attempt> getAttempts() {
        Cursor c = db.query("attempts", null, null, null, null, null, null);
        ArrayList<Attempt> ret = new ArrayList<>();
        if (!c.moveToFirst())
            return ret;

        int idColIndex = c.getColumnIndex("id");
        int timepointColIndex = c.getColumnIndex("timepoint");
        int scoreColIndex = c.getColumnIndex("score");

        do {
            Attempt at = new Attempt(c.getLong(idColIndex), this.db);
            at.timepoint = c.getString(timepointColIndex);
            at.score = c.getInt(scoreColIndex);
            ret.add(at);
        } while (c.moveToNext());

        return ret;
    }

    public Attempt getLastAttempt() {
        Cursor c = db.query("attempts", null, null, null, null, null, "timepoint", "1");
        if (!c.moveToFirst())
            return null;

        int idColIndex = c.getColumnIndex("id");
        int timepointColIndex = c.getColumnIndex("timepoint");
        int scoreColIndex = c.getColumnIndex("score");

        Attempt at = new Attempt(c.getLong(idColIndex), this.db);
        at.timepoint = c.getString(timepointColIndex);
        at.score = c.getInt(scoreColIndex);

        return at;
    }

    public void clearAllResults() {
        this.db.delete("speed", null, null);
        this.db.delete("latency", null, null);
        this.db.delete("multiplication", null, null);
        this.db.delete("attempts", null, null);
    }
}
