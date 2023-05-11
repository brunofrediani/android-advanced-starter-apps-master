package com.example.speedtestapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    private TextView downloadSpeedTxt;
    private double lastSpeed = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadSpeedTxt = findViewById(R.id.downloadSpeedTxt);

        DownloadAndMeasureSpeed downloadTask = new DownloadAndMeasureSpeed(this);
        downloadTask.execute("https://speed.hetzner.de/100MB.bin");
    }
    private class DownloadAndMeasureSpeed extends AsyncTask<String,Long,Long> {
        private long startTime;
        private long fileSize;
        private Context context;
        public DownloadAndMeasureSpeed(Context context) {
            this.context = context;
        }

        @Override
        protected Long doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                fileSize = connection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(),8192);
                FileOutputStream output = new FileOutputStream(context.getCacheDir() + "/download.bin");

                byte[]data = new byte[1024];
                int count;
                long total= 0;

                startTime = System.nanoTime();

                while ((count = input.read(data))!= -1){
                    total += count;
                    output.write(data,0,count);

                    long currentTime = System.nanoTime();
                    long elapsedTime = currentTime - startTime;
                    if (elapsedTime > 1000000000) {
                        publishProgress(total);
                        startTime = currentTime;
                    }
                }
                output.flush();
                output.close();
                input.close();

            } catch (IOException e){
                e.printStackTrace();
            }
            return fileSize;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            long downloadedSize = values[0];
            long elapsedTime = System.nanoTime() - startTime;

            double downloadSpeed = downloadedSize * 1000000000 / (elapsedTime * 1.0);
            double downloadSpeedMbps = downloadSpeed / 1000000.0;


            lastSpeed =  downloadSpeedMbps;
            downloadSpeedTxt.setText(String.format("%.2f Mbps", downloadSpeedMbps));
        }

        @Override
        protected void onPostExecute(Long result) {
            downloadSpeedTxt.setText(String.format("Last speed: %.2f Mbps", lastSpeed));
        }
    }

}