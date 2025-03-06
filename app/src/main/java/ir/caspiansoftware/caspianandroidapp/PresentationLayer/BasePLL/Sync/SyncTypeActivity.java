package ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL.Sync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;

import info.elyasi.android.elyasilib.UI.FormActionType;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.Enum.SyncType;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Setting;

/**
 * Created by Ghasem on 4/22/2017.
 */

public class SyncTypeActivity extends CaspianActivitySingleFragment {
    public static final String TAG = "DaftarTafActivity";
    public static final String ACTION_START_SYNC = "action_start_sync";

    public static final String EXTRA_SYNC_TYPE = "sync_type";

    private IFragmentCallback mFragmentCallback;

    private SyncType syncType;

    @Override
    public void onCreate(Bundle savedBundleState) {
        Log.d(TAG, "start...");
        Setting.setHeightWidth(this);
        //showAsPopup(this, getResources().getInteger(R.integer.popup_daf_taf_height), getResources().getInteger(R.integer.popup_daf_taf_width));
        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog, R.string.sync_select_sync_type);

        super.onCreate(savedBundleState);

        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        return new SyncTypeFragment();
    }

    @Override
    public void onMyFragmentCallBack(String actionName, FormActionType actionType, Object[] parameters) {
        Log.d(TAG, "onMyFragmentCallBack(): actionName = " + actionName);

        if (ACTION_START_SYNC.equals(actionName)) {
            syncType = (SyncType) parameters[0];
            this.setResult(Activity.RESULT_OK, new Intent().putExtra(EXTRA_SYNC_TYPE, syncType));
            finish();
        }
    }

    public SyncType getSyncType() {
        return syncType;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof IFragmentCallback) {
            mFragmentCallback = (IFragmentCallback) fragment;
        }
    }


}
