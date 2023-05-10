package com.example.speedtestapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView downloadTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadTxt = findViewById(R.id.downloadSpeedTxt);

        DownloadAndMeasureSpeed downloadTask = new DownloadAndMeasureSpeed();
        downloadTask.execute(downloadTxt);
    }
}