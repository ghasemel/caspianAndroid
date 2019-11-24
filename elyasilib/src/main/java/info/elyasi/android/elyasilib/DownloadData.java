package info.elyasi.android.elyasilib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadData {
    public static Bitmap DownloadImageFromPath(String path) {
        Bitmap bmp = null;
        try {

            URL url = new URL(path);//"http://192.xx.xx.xx/mypath/img1.jpg
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.connect();
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //download
                InputStream in = con.getInputStream();
                bmp = BitmapFactory.decodeStream(in);
                in.close();
            }

        } catch (Exception ex) {
            Log.e("Exception", ex.toString());
        }

        return bmp;
    }
}
