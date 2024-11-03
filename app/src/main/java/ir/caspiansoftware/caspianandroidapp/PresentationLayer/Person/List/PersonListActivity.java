package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.List;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;

import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Setting;

/**
 * Created by Canada on 7/18/2016.
 */
public class PersonListActivity extends CaspianActivitySingleFragment {
    public static final String TAG = "PersonListActivity";

    @Override
    public void onCreate(Bundle savedBundleState) {
        //showAsPopup(this, getResources().getInteger(R.integer.popup_height), getResources().getInteger(R.integer.popup_width));
        Setting.setHeightWidth(this);
        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog, R.string.person_list);
        //forceRTLIfSupported();

        //Log.d(TAG, "height: " + String.valueOf(getResources().getInteger(R.integer.popup_height)));
        //Log.d(TAG, "width: " + String.valueOf(getResources().getInteger(R.integer.popup_width)));

        super.onCreate(savedBundleState);

        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        return new PersonListFragment();
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.fragmentContainer;
    }

    @Override
    public int getLayoutId() {
        return R.layout.single_fragment_activity;
    }
}
