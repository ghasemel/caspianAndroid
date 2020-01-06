package ir.caspiansoftware.caspianandroidapp.BaseCaspian;

import android.os.Bundle;

import info.elyasi.android.elyasilib.UI.ActivityFragmentExt;
import info.elyasi.android.elyasilib.UI.FormActionTypes;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 3/8/2016.
 */
public abstract class CaspianActivitySingleFragment extends ActivityFragmentExt {

    @Override
    public void onCreate(Bundle savedBundleState) {
        forceRTLIfSupported();
        super.onCreate(savedBundleState);
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.fragmentContainer;
    }

    @Override
    public int getFragmentDetailContainerId() {
        return -1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.single_fragment_activity;
    }


    @Override
    public void onMyFragmentCallBack(String actionName, FormActionTypes actionTypes, Object[] parameters) {

    }
}
