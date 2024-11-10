package ir.caspiansoftware.caspianandroidapp;


import android.os.Bundle;
import androidx.fragment.app.Fragment;

import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;

/**
 * Created by Canada on 1/6/2016.
 */
public class FirstActivity extends CaspianActivitySingleFragment {

    @Override
    public int getFragmentContainerId() {
        return R.id.fragmentContainer;
    }

    @Override
    public int getLayoutId() {
        return R.layout.single_fragment_activity;
    }

    @Override
    public Fragment createFragment() {
        return new FirstFragment();
    }

    @Override
    public void onCreate(Bundle savedBundleState) {
        // remove title
        this.removeTitle();

        // fullscreen
        showAsPopup(this, 260, 260);

        super.onCreate(savedBundleState);

//        /ErrorExt.setContext(getApplicationContext());
        //UIUtility.changeLocale(getBaseContext(), "fa");

        Vars.CONTEXT = getApplicationContext();
    }

}
