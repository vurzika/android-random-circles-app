package com.viktorija.randomcircles;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;


//  Objective : This program is drawing random circles. User can
//  control drawing with Start / Stop buttons

public class CirclesDrawingActivity extends AppCompatActivity {

    private static final int MAX_NUMBER_OF_CIRCLES = 500;
    private static final int DRAWING_INTERVAL_MILLIS = 250;

    private CirclesDrawingView circlesDrawingView;
    private Button startButton;
    private Button stopButton;

    private Timer circlesDrawingTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circles_drawing);

        circlesDrawingView = findViewById(R.id.my_view);

        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this::startDrawingCircles);

        stopButton = findViewById(R.id.stop_button);
        stopButton.setOnClickListener(this::stopDrawingCircles);
        stopButton.setEnabled(false);
    }

    public void startDrawingCircles(View v) {
        cancelTimerIfRunning();

        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        circlesDrawingView.clearCircles();

        // create timer object to do drawing in interval
        // on background thread to avoid blocking UI thread
        // between drawing intervals
        circlesDrawingTimer = new Timer();
        circlesDrawingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // each time when timer fires after delay
                // switch to UI thread to draw single circle
                // as drawing is not allowed on background thread
                runOnUiThread(() ->
                {
                    // clear screen if we already have max circles
                    if (circlesDrawingView.getNumberOfCircles()
                            >= MAX_NUMBER_OF_CIRCLES) {
                        circlesDrawingView.clearCircles();
                    }

                    // draw single circle
                    circlesDrawingView.drawCircle();
                });
            }
        }, 0, DRAWING_INTERVAL_MILLIS);
    }

    private void stopDrawingCircles(View v) {
        cancelTimerIfRunning();

        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    private void cancelTimerIfRunning() {
        if (circlesDrawingTimer != null) {
            circlesDrawingTimer.cancel();
        }
        circlesDrawingTimer = null;
    }

    @Override
    protected void onDestroy() {
        // release timer when activity is destroyed
        cancelTimerIfRunning();

        super.onDestroy();
    }
}