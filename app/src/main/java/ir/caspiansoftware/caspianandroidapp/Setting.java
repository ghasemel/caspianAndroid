package ir.caspiansoftware.caspianandroidapp;


import android.app.Activity;
import android.util.DisplayMetrics;

import info.elyasi.android.elyasilib.UI.ActivityFragmentExt;

/**
 * Created by Canada on 12/18/2015.
 */
public class Setting {
    public static final String APP_NAME = "caspian";

    public static final int APP_DB_VERSION = 7;
    public static final boolean RECREATE_DATABASE = false;
    public static final String CASPIAN_DB = "caspian_db.sqlite";

    public static final long FirstActivityDelay = 2; // in seconds


    public static final int DEVICE_ID_LENGTH = 7;
    //public static final int DB_VERSION = 1;

    //public static final long LOCATION_REFRESH_TIME = 5, LOCATION_REFRESH_DISTANCE

    static DisplayMetrics displayMetrics = new DisplayMetrics();
    //static int height = 0;
    //static int width = 0;


    public static void setHeightWidth(ActivityFragmentExt activity) {
        //if (height <= 0 || width < 0) {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        //}

        ActivityFragmentExt.showAsPopup(activity, height - 100, width - 200);
    }
}
