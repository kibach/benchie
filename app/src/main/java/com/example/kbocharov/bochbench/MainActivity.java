package com.example.kbocharov.bochbench;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kbocharov.bochbench.benchmark.BenchmarkResponder;
import com.example.kbocharov.bochbench.benchmark.BenchmarkResult;
import com.example.kbocharov.bochbench.benchmark.BenchmarkTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements BenchmarkResponder {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    Button btnStart;
    TextView textScore;
    BenchmarkTask benchmarkTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        btnStart = (Button) findViewById(R.id.button2);
        textScore = (TextView) findViewById(R.id.scoreView);
        final MainActivity mainActivity = this;
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (benchmarkTask != null)
                    return;
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                benchmarkTask = new BenchmarkTask();
                benchmarkTask.setResponder(mainActivity);
                benchmarkTask.execute();
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void respondOnBenchmarkEnd(List<BenchmarkResult> resultList) {
        double total = 0;
        for (BenchmarkResult res: resultList) {
            total += res.getMFLOPS();
        }
        total /= resultList.size();
        textScore.setText("" + Math.round(total));
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        benchmarkTask = null;
    }
}
