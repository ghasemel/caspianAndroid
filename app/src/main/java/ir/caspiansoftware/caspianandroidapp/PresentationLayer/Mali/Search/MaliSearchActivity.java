package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Mali.Search;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 3/9/2016.
 */
public class MaliSearchActivity extends CaspianActivitySingleFragment {
    private static final String TAG = "MaliSearchActivity";

    @Override
    public void onCreate(Bundle savedBundleState) {
        Log.d(TAG, "onCreate(): starting");

        //showAsPopup(this, getResources().getInteger(R.integer.popup_height), getResources().getInteger(R.integer.popup_width));
        //Setting.setHeightWidth(this);
        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog, R.string.mali_search_title);
        super.onCreate(savedBundleState);
        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        return new MaliSearchFragment();
    }



}
