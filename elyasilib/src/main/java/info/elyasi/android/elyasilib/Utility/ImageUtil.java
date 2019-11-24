package info.elyasi.android.elyasilib.Utility;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {

    public static boolean saveImage(Bitmap image, String path) {
        try (FileOutputStream out = new FileOutputStream(path)) {
            image.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            Log.e("saveImage exception:", e.toString());
            return false;
        }

        return true;
    }
}
