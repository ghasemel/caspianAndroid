package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.MandeList;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import info.elyasi.android.elyasilib.UI.FormActionType;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 7/22/2016.
 */
public class PersonMandeListActivity extends CaspianActivitySingleFragment {

    @Override
    public void onCreate(Bundle savedBundleState) {
        //showAsPopup(this, 650, getResources().getInteger(R.integer.popup_width));
        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog, R.string.person_list_mande);
        //forceRTLIfSupported();

        super.onCreate(savedBundleState);

        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        return new PersonMandeListFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.single_fragment_activity;
    }

    @Override
    public void onMyFragmentCallBack(String actionName, FormActionType actionType, Object[] parameters) {
        switch (actionName) {
            case Actions.ACTION_TOOLBAR_EXIT:
                this.finish();
                break;
        }
    }
}
