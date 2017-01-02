package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Kala.MojoodiList;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import info.elyasi.android.elyasilib.UI.FormActionTypes;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 7/22/2016.
 */
public class KalaMojoodiListActivity extends CaspianActivitySingleFragment {

    @Override
    public void onCreate(Bundle savedBundleState) {
        //showAsPopup(this, 650, getResources().getInteger(R.integer.popup_width));
        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog, R.string.kala_list_mojoodi);
        //forceRTLIfSupported();

        super.onCreate(savedBundleState);

        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        return new KalaMojoodiListFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.single_fragment_activity;
    }

    @Override
    public void fragment_callback(String actionName, FormActionTypes actionTypes, Object[] parameters) {
        switch (actionName) {
            case Actions.ACTION_TOOLBAR_EXIT:
                this.finish();
                break;
        }
    }
}
