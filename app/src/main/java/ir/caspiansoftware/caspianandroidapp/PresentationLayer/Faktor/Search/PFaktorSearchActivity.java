package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor.Search;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;

import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Setting;

/**
 * Created by Canada on 3/9/2016.
 */
public class PFaktorSearchActivity extends CaspianActivitySingleFragment {
    private static final String TAG = "PFaktorSearchActivity";

    @Override
    public void onCreate(Bundle savedBundleState) {
        Log.d(TAG, "onCreate(): starting");

        //showAsPopup(this, getResources().getInteger(R.integer.popup_height), getResources().getInteger(R.integer.popup_width));
        Setting.setHeightWidth(this);
        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog, R.string.pfaktor_search_title);
        super.onCreate(savedBundleState);
        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        return new PFaktorSearchFragment();
    }



}
