package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 7/18/2016.
 */
public class PersonListActivity extends CaspianActivitySingleFragment {

    @Override
    public void onCreate(Bundle savedBundleState) {
        showAsPopup(this, getResources().getInteger(R.integer.popup_height), getResources().getInteger(R.integer.popup_width));
        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog, R.string.person_list);
        //forceRTLIfSupported();

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
