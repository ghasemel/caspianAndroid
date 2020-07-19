package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Year;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 2/23/2016.
 */
public class YearListActivity extends CaspianActivitySingleFragment {

    private static final String TAG = "YearListActivity";

    @Override
    public void onCreate(Bundle savedBundleState) {
        showAsPopup(this, getResources().getInteger(R.integer.popup_height), getResources().getInteger(R.integer.popup_width));
        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog, R.string.year_title);
        //forceRTLIfSupported();
        Log.d(TAG, String.valueOf(getResources().getInteger(R.integer.popup_height)));
        Log.d(TAG, String.valueOf(getResources().getInteger(R.integer.popup_width)));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Log.d(TAG, String.valueOf(height));
        Log.d(TAG, String.valueOf(width));

        float density = getResources().getDisplayMetrics().density;
        Log.d(TAG, String.valueOf(density));
        // (dpi / 160) = 0.75 if it's LDPI
        // (dpi / 160) = 1.0 if it's MDPI
        // (dpi / 160) = 1.5 if it's HDPI
        // (dpi / 160) = 2.0 if it's XHDPI
        // (dpi / 160) = 3.0 if it's XXHDPI
        // (dpi / 160) = 4.0 if it's XXXHDPI


        // Convert dp units to pixel units
        // px = dp * (dpi / 160)
//
//        values-sw720dp          10.1” tablet 1280x800 mdpi
//
//        values-sw600dp          7.0”  tablet 1024x600 mdpi
//
//        values-sw480dp          5.4”  480x854 mdpi
//        values-sw480dp          5.1”  480x800 mdpi
//
//        values-xxhdpi           5.5"  1080x1920 xxhdpi
//        values-xxxhdpi           5.5" 1440x2560 xxxhdpi
//
//        values-xhdpi            4.7”   1280x720 xhdpi
//        values-xhdpi            4.65”  720x1280 xhdpi
//
//        values-hdpi             4.0” 480x800 hdpi
//        values-hdpi             3.7” 480x854 hdpi
//
//        values-mdpi             3.2” 320x480 mdpi
//
//        values-ldpi             3.4” 240x432 ldpi
//        values-ldpi             3.3” 240x400 ldpi
//        values-ldpi             2.7” 240x320 ldpi


        super.onCreate(savedBundleState);

        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        return new YearListFragment();
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.fragmentContainer;
    }

    @Override
    public int getLayoutId() {
        return R.layout.single_fragment_activity;
    }

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    //    getMenuInflater().inflate(R.menu.menu_main, menu);
    //    return true;
    //}
}
