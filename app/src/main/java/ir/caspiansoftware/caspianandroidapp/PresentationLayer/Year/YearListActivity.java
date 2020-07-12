package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Year;

import android.support.v4.app.Fragment;
import android.os.Bundle;
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
