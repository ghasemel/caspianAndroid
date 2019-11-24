package info.elyasi.android.elyasilib.Utility;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Canada on 3/2/2016.
 */
public class LogExt {
    private static final String TAG = "LogExt";

    //private String mRootDirName;
    private Context mContext;
    private File mErrorDir;
    private File mTraceDir;

    private static LogExt sLogExt;
    public static LogExt get(String rootDirName, Context context) {
        if (sLogExt == null) {
            sLogExt = new LogExt(rootDirName, context);
        }
        return sLogExt;
    }

    private LogExt(String rootDirName, Context context) {
        mContext = context;

        try {
           File parentDir = null;
           String sdCardState = FileExt.getExternalStorageState();
           if (sdCardState.equals("mounted")) {
                File sdCard = FileExt.getExternalStorage();
                parentDir = FileExt.createDirectory(sdCard, rootDirName);

            } else {
                parentDir = FileExt.getInternalStorage(mContext);
            }

            if (parentDir != null) {
                mErrorDir = FileExt.createDirectory(parentDir, "error");
                mTraceDir = FileExt.createDirectory(parentDir, "trace");
            }
        } catch (IOException io) {
            Log.e(TAG, io.getMessage());
        }
    }

    public void WriteLine(String line) {
        try {
            if (mErrorDir != null && mErrorDir.exists()) {
                File logfile = FileExt.createFile(mErrorDir, DateExt.getDateAsFileName() + ".html");

                FileOutputStream stream = new FileOutputStream(logfile, true);
                PrintWriter writer = new PrintWriter(stream);
                writer.println(DateExt.Now() + " " + line);
                writer.flush();
                writer.close();
                stream.close();
            }
        } catch (IOException io) {
            Log.e(TAG, io.getMessage());
        }
    }
}
