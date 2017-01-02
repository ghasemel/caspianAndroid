package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor.Kala;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import info.elyasi.android.elyasilib.UI.AListRowFragment;
import info.elyasi.android.elyasilib.UI.FormActionTypes;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Kala.List.KalaListActivity;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 7/24/2016.
 */
public class FaktorKalaActivity extends CaspianActivitySingleFragment {
    private static final String TAG = "FaktorKalaActivity";

    private static final int REQUEST_CODE_KALA_LIST = 1;
    public static final String ACTION_SELECT_KALA_LIST = "action_select_kala_list";


    private IFragmentCallback mFragmentCallback;

    @Override
    public void onCreate(Bundle savedBundleState) {
        Log.d(TAG, "starting");
        showAsPopup(this, 550, getResources().getInteger(R.integer.popup_width));
        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog, R.string.invoice_kala_activity_title);

        super.onCreate(savedBundleState);

        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        return new FaktorKalaFragment();
    }

    @Override
    public void fragment_callback(String actionName, FormActionTypes actionTypes, Object[] parameters) {
        Log.d(TAG, "fragment_callback(): actionName = " + actionName);

        switch (actionName) {
            case ACTION_SELECT_KALA_LIST:
                Intent intent1 = new Intent(this, KalaListActivity.class);
                intent1.putExtra(AListRowFragment.EXTRA_RETURN_NAME, "");
                startActivityForResult(intent1, REQUEST_CODE_KALA_LIST);
                break;
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof IFragmentCallback) {
            mFragmentCallback  = (IFragmentCallback) fragment;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_KALA_LIST:
                if (resultCode == Activity.RESULT_OK) {
                    KalaModel kalaModel = (KalaModel) data.getSerializableExtra(AListRowFragment.EXTRA_RETURN_NAME);
                    mFragmentCallback.activity_callback(ACTION_SELECT_KALA_LIST, kalaModel, null);
                }
                break;
        }
    }
}
