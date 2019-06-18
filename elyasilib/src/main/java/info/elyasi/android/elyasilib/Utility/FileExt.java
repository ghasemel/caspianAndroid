package info.elyasi.android.elyasilib.Utility;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Canada on 3/2/2016.
 */
public class FileExt {


    public static File createFile(File dir, String fileName) throws IOException {
        if (!dir.canWrite())
            throw new IOException(dir.getAbsolutePath() + " is not writable");

        File file = new File(dir, fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    public static String getExternalStorageState() {
        return Environment.getExternalStorageState();
    }

    public static File getExternalStorage() {
        return Environment.getExternalStorageDirectory();
    }

    public static File createDirectory(File parentDir, String dirName) throws IOException {
        if (!parentDir.canWrite())
            throw new IOException(parentDir.getAbsolutePath() + " is not writable");

        File dir = new File(parentDir, dirName);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }

    public static File getInternalStorage(Context context) {
        return context.getFilesDir();
    }
}
