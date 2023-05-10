package com.example.speedtestapp;

import android.os.AsyncTask;
import android.widget.TextView;

import androidx.loader.content.AsyncTaskLoader;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadAndMeasureSpeed extends AsyncTask<TextView,Void,Double> {

    private TextView textView;


    @Override
    protected Double doInBackground(TextView... textViews) {
    textView = textViews[0];
    try {
        URL url = new URL("https://speed.hetzner.de/100MB.bin");
        URLConnection connection = url.openConnection();
        long startTime = System.currentTimeMillis();
        BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
        FileOutputStream out = new FileOutputStream("100MB.bin");
        byte[]data = new byte[1024];
        int count;
        while ((count = in.read(data,0,1024))!= -1){
            out.write(data,0,count);
        }
        out.close();
        in.close();

        long endTime = System.currentTimeMillis();
        double fileSizeMB = (double) connection.getContentLength()/(1024 * 1024);
        double downloadTimeSeconds = (endTime - startTime) /1000.0;
        double downloadSpeedMpbs = fileSizeMB / downloadTimeSeconds * 8.0;
        return downloadSpeedMpbs;
    } catch (IOException e){
        e.printStackTrace();
    }
        return null;
    }

    @Override
    protected void onPostExecute(Double downloadSpeedMbps) {
        if (downloadSpeedMbps != null){
            textView.setText("Download speed: " + downloadSpeedMbps + " Mbps");
        }else {
            textView.setText("Failed to download file");
        }
    }
}
