package ir.caspiansoftware.caspianandroidapp.PresentationLayer;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.R;


/**
 * Created by Canada on 12/17/2015.
 */
public class LoginActivity extends CaspianActivitySingleFragment {
    //Fragment mFragment;

    @Override
    public void onCreate(Bundle savedBundleState) {
        // right to left
        //forceRTLIfSupported();

        // full screen
        //fullScreen();

        super.onCreate(savedBundleState);

        if (getActionBar() != null)
            getActionBar().setTitle(R.string.app_full_title);
    }

    @Override
    public Fragment createFragment() {
        return new LoginFragment();
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
