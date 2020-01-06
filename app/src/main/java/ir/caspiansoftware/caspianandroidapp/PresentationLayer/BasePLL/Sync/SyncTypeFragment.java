package ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL.Sync;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import info.elyasi.android.elyasilib.UI.FormActionTypes;
import info.elyasi.android.elyasilib.UI.IActivityCallback;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import info.elyasi.android.elyasilib.UI.UIUtility;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.Enum.SyncType;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Ghasem on 4/22/2017.
 */

public class SyncTypeFragment extends CaspianFragment implements IFragmentCallback {
    private static final String TAG = "DaftarTafFragment";

    private IActivityCallback mActivityCallback;

    private RadioButton rdKala;
    private RadioButton rdPerson;
    private RadioButton rdKalaPhoto;


    private Button mBtnOK;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sync_type_selection;
    }

    @Override
    protected void mapViews(View parentView) {
        Log.d(TAG, "mapViews(): start...");

        rdKala = (RadioButton) parentView.findViewById(R.id.rdSyncKala);
        rdPerson = (RadioButton) parentView.findViewById(R.id.rdSyncPerson);
        rdKalaPhoto = (RadioButton) parentView.findViewById(R.id.rdSyncKalaPhoto);

        rdKala.setOnCheckedChangeListener(this::checkKalaChange);
        rdPerson.setOnCheckedChangeListener(this::checkPersonChange);
        rdKalaPhoto.setOnCheckedChangeListener(this::checkKalaPhotoChange);

        mBtnOK = (Button) parentView.findViewById(R.id.btn_OK);

        UIUtility.setButtonEffect(mBtnOK, this);
    }

    private void checkKalaChange(CompoundButton compoundButton, boolean b) {
        if (b) {
            rdKalaPhoto.setChecked(false);
            rdPerson.setChecked(false);
        }
    }

    private void checkKalaPhotoChange(CompoundButton compoundButton, boolean b) {
        if (b) {
            rdKala.setChecked(false);
            rdPerson.setChecked(false);
        }
    }

    private void checkPersonChange(CompoundButton compoundButton, boolean b) {
        if (b) {
            rdKala.setChecked(false);
            rdKalaPhoto.setChecked(false);
        }
    }

    @Override
    protected void afterMapViews(View parentView) {
        Log.d(TAG, "afterMapViews(): start...");
    }

    @Override
    public ProgressBar getProgressBar() {
        return null;
    }


    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick(): start...");

        if (view.equals(mBtnOK)) {
            btnOK_click();
        }
    }

    @Override
    public void onMyActivityCallback(String actionName, Object parameter, FormActionTypes formActionTypes) {

    }

    private void btnOK_click() {
        Log.d(TAG, "btnOK_click(): enter");

        SyncType syncType = SyncType.KALA;
        if (rdPerson.isChecked())
            syncType = SyncType.PERSON;
        else if (rdKalaPhoto.isChecked())
            syncType = SyncType.KALA_PHOTO;

        mActivityCallback.onMyFragmentCallBack(SyncTypeActivity.ACTION_START_SYNC, null, syncType);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivityCallback = (IActivityCallback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityCallback = null;
    }
}
