package com.example.downloadbar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView txtPercentualDL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        txtPercentualDL = findViewById(R.id.percentualDL);
        new DownloadFileAsync().execute("https://speed.hetzner.de/100MB.bin");
    }


    class DownloadFileAsync extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

        int count;

        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            int fileLength = connection.getContentLength();

            InputStream input = new BufferedInputStream(url.openStream(),8192);

            File downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadDirectory.exists()){
                downloadDirectory.mkdir();
            }

            String fileName = "100MB.bin";

            File outputFile = new File(downloadDirectory,fileName);

            FileOutputStream output = new FileOutputStream(outputFile);

            byte[] data = new byte[1024];
            long total = 0;

            while ((count = input.read(data)) != -1){
                total += count;
                int progress = (int) ((total * 100) / fileLength);
                onProgressUpdate(progress);
                output.write(data,0,count);
            }
            output.flush();

            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ",e.getMessage());
        }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
            txtPercentualDL.setText(String.format(Locale.getDefault(),"%d%%",progress[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(),"Download Completed Sucessfully",Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    }
}