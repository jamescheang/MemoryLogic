package com.example.memorylogic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyAsyncTask extends AsyncTask<String, Integer, Bitmap> {
    private ICallback callback = null;

    public MyAsyncTask(ICallback callback) {
        this.callback = callback;
    }

    @Override
    protected Bitmap doInBackground( String... params) {

        long imageLen = 0;
        long totalSoFar = 0;
        int readLen = 0;
        Bitmap bitmap = null;

        try {

            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            imageLen = conn.getContentLength();
            byte[] data = new byte[1024];

            InputStream in = url.openStream();
            BufferedInputStream bufIn = new BufferedInputStream(in, 2048);
            OutputStream out = new FileOutputStream(params[1]);

            while ((readLen = bufIn.read(data)) != -1) {
                totalSoFar += readLen;
                out.write(data, 0, readLen);

                publishProgress((int)((totalSoFar * 100)/imageLen));
            }

            File file = new File(params[1]);
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (callback != null)
            callback.onAsyncTaskProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap == null)
            return;

        if (callback != null)
            callback.onAsyncTaskDone(bitmap);
    }

    public interface ICallback {
        void onAsyncTaskDone(Bitmap bitmap);
        void onAsyncTaskProgress(int progress);
    }
}
