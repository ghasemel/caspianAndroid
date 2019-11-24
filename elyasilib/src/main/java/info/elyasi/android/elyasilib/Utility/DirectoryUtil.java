package info.elyasi.android.elyasilib.Utility;

import android.content.Context;

import java.io.File;

public class DirectoryUtil {

    public static String getAppDir(Context context) {
        return context.getApplicationInfo().dataDir;
    }

    public static boolean makeDirInAppFolder(Context context, String name) {
        String dir = getAppDir(context) + File.separator + name;

        File projDir = new File(dir);
        if (!projDir.exists())
            return projDir.mkdirs();

        return true;
    }
}
