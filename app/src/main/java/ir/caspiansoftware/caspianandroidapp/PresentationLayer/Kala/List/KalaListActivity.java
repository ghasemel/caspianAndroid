package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Kala.List;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Setting;

/**
 * Created by Canada on 7/22/2016.
 */
public class KalaListActivity extends CaspianActivitySingleFragment {

    @Override
    public void onCreate(Bundle savedBundleState) {
        //Setting.setHeightWidth(this);
        //showAsPopup(this, getResources().getInteger(R.integer.popup_height), getResources().getInteger(R.integer.popup_width));
        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog, R.string.kala_list);
        //forceRTLIfSupported();

        super.onCreate(savedBundleState);

        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        return new KalaListFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.single_fragment_activity;
    }
}
