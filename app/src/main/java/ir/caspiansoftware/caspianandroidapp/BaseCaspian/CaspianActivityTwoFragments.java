package ir.caspiansoftware.caspianandroidapp.BaseCaspian;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import info.elyasi.android.elyasilib.Persian.PersianConvert;
import info.elyasi.android.elyasilib.UI.ActivityFragmentExt;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 3/8/2016.
 */
public abstract class CaspianActivityTwoFragments extends ActivityFragmentExt {

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
        return R.id.fragmentDetailContainer;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_master_detail;
    }


}
