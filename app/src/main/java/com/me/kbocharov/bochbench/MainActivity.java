package com.me.kbocharov.bochbench;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.me.kbocharov.bochbench.benchmark.BenchmarkResponder;
import com.me.kbocharov.bochbench.benchmark.multiplication.BenchmarkResult;
import com.me.kbocharov.bochbench.benchmark.BenchmarkTask;
import com.me.kbocharov.bochbench.benchmark.scorer.Attempt;
import com.me.kbocharov.bochbench.benchmark.scorer.ResultStorage;
import com.me.kbocharov.bochbench.benchmark.tinymembench.LatencyBenchResult;
import com.me.kbocharov.bochbench.benchmark.tinymembench.Runner;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BenchmarkResponder {
    Button btnStart;
    Button btnClearAll;
    TextView textScore;
    TextView subCaption;
    BenchmarkTask benchmarkTask;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        btnStart = (Button) findViewById(R.id.button2);
        btnClearAll = (Button) findViewById(R.id.clearAll);
        textScore = (TextView) findViewById(R.id.scoreView);
        subCaption = (TextView) findViewById(R.id.subCaption);
        final MainActivity mainActivity = this;
        final Runner r = new Runner();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (benchmarkTask != null)
                    return;
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                benchmarkTask = new BenchmarkTask();
                benchmarkTask.setResponder(mainActivity);
                benchmarkTask.setRunner(r);
                benchmarkTask.execute();
            }
        });

        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResultStorage.getInstance().clearAllResults();
            }
        });

        dbHelper = new DBHelper(this);
        ResultStorage.getInstance().setDB(dbHelper.getWritableDatabase());
        Attempt a = ResultStorage.getInstance().getLastAttempt();
        if (a != null)
            textScore.setText("" + a.score);
        subCaption.setText("Last known score.");
    }

    @Override
    public void respondOnBenchmarkEnd(Attempt attempt) {
        textScore.setText(attempt.calculateScore());
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        benchmarkTask = null;
    }

    @Override
    public void respondOnProgress(Integer value) {
        subCaption.setText(value + " tests ran so far..");
    }

    class DBHelper extends SQLiteOpenHelper {
        static final int DB_VERSION = 1;
        static final String DB_NAME = "benchie";

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE attempts (\n" +
                    "    id        INTEGER  PRIMARY KEY AUTOINCREMENT\n" +
                    "                       UNIQUE,\n" +
                    "    timepoint DATETIME,\n" +
                    "    score     INTEGER\n" +
                    ");\n");
            db.execSQL("CREATE TABLE multiplication (\n" +
                    "    id         INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                    "                       UNIQUE,\n" +
                    "    attempt_id INTEGER REFERENCES attempts (id),\n" +
                    "    num_ops    INTEGER,\n" +
                    "    duration   DOUBLE,\n" +
                    "    iterations INTEGER,\n" +
                    "    mul_size   INTEGER,\n" +
                    "    name       TEXT\n" +
                    ");\n");
            db.execSQL("CREATE TABLE speed (\n" +
                    "    id         INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                    "                       UNIQUE,\n" +
                    "    attempt_id INTEGER REFERENCES attempts (id),\n" +
                    "    speed      DOUBLE,\n" +
                    "    diversity  DOUBLE,\n" +
                    "    name       TEXT,\n" +
                    "    score      INTEGER\n" +
                    ");\n");
            db.execSQL("CREATE TABLE latency (\n" +
                    "    id          INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                    "                        UNIQUE,\n" +
                    "    attempt_id  INTEGER REFERENCES attempts (id),\n" +
                    "    size        INTEGER,\n" +
                    "    single_read DOUBLE,\n" +
                    "    double_read DOUBLE,\n" +
                    "    hugepage    INTEGER" +
                    ");\n");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
