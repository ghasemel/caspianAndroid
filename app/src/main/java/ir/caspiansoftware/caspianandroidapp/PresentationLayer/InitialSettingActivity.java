package ir.caspiansoftware.caspianandroidapp.PresentationLayer;


import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import info.elyasi.android.elyasilib.Dialogs.DialogResult;
import info.elyasi.android.elyasilib.Dialogs.IDialogCallback;
import info.elyasi.android.elyasilib.Dialogs.IInputDialogProperty;
import info.elyasi.android.elyasilib.UI.FormActionTypes;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import info.elyasi.android.elyasilib.Dialogs.InputDialog;
import info.elyasi.android.elyasilib.Utility.NumberExt;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Setting;

/**
 * Created by Canada on 6/4/2016.
 */
public class InitialSettingActivity extends CaspianActivitySingleFragment implements IDialogCallback<String> {
    private static final String TAG = "InitialSettingActivity";

    public static final String ACTION_API_GENERATOR = "action_api_generator";
    public static final String ACTION_DEVICE_ID_GENERATOR = "action_device_id_generator";

    private static final int REQUEST_CODE_API_GENERATOR = 1;
    private static final int REQUEST_CODE_DEVICE_ID_GENERATOR = 2;

    private IFragmentCallback mFragmentDetailCallback;

    @Override
    public void onCreate(Bundle savedBundleState) {
        super.onCreate(savedBundleState);

        if (getActionBar() != null) {
            getActionBar().setTitle(R.string.app_full_title);
        }

        mFragmentDetailCallback = null;
    }

    @Override
    public Fragment createFragment() {
        return new InitialSettingFragment();
    }

    @Override
    public int getFragmentContainerId() { return R.id.fragmentContainer; }

    @Override
    public int getLayoutId() { return R.layout.single_fragment_activity; }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (mFragmentDetailCallback == null)
            mFragmentDetailCallback = (IFragmentCallback) fragment;
    }

    @Override
    public void onMyFragmentCallBack(String actionName, FormActionTypes actionTypes, Object[] parameters) {
        Log.d(TAG, "onMyFragmentCallBack() called: actionName = " + actionName);

        switch (actionName) {
            case InitialSettingActivity.ACTION_API_GENERATOR:
                generateApiKey();
                break;

            case InitialSettingActivity.ACTION_DEVICE_ID_GENERATOR:
                generateDeviceIdKey();
                break;
        }
    }

    @Override
    public void dialog_callback(DialogResult dialogResult, String result, int requestCode) {
        if (dialogResult == DialogResult.OK) {
            switch (requestCode) {
                case InitialSettingActivity.REQUEST_CODE_API_GENERATOR:
                    mFragmentDetailCallback.onMyActivityCallback(ACTION_API_GENERATOR, result, null);
                    break;

                case InitialSettingActivity.REQUEST_CODE_DEVICE_ID_GENERATOR:
                    mFragmentDetailCallback.onMyActivityCallback(ACTION_DEVICE_ID_GENERATOR, result, null);
                    break;
            }
        }
    }

    public void generateApiKey() {
        InputDialog inputDialog = new InputDialog();
        inputDialog.setDialogCallback(this);
        inputDialog.setTitle(getResources().getString(R.string.initial_company));
        inputDialog.setRequestCode(REQUEST_CODE_API_GENERATOR);
        inputDialog.setInputDialogProperty(new IInputDialogProperty() {
            @Override
            public void setEditTextProperty(EditText editText) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setWidth(600);
                editText.setHeight(80);
                editText.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                editText.setTypeface(Typeface.SERIF);
            }
        });
        //inputDialog.setTypeface(Typeface.SERIF);

        inputDialog.show(getFragmentManager(), "");
    }

    public void generateDeviceIdKey() {
        InputDialog inputDialog = new InputDialog();
        inputDialog.setDialogCallback(this);
        inputDialog.setTitle(getResources().getString(R.string.initial_device));
        inputDialog.setRequestCode(REQUEST_CODE_DEVICE_ID_GENERATOR);
        inputDialog.setInputDialogProperty(new IInputDialogProperty() {
            @Override
            public void setEditTextProperty(EditText editText) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setWidth(600);
                editText.setHeight(80);
                editText.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                editText.setTypeface(Typeface.SERIF);
                editText.setEnabled(false);
                editText.setTextColor(getResources().getColor(android.R.color.black));
                //editText.setText(DeviceUtility.getDeviceUniqueId(getApplicationContext()));
                editText.setText(String.valueOf(
                        NumberExt.getRandomNumber(Setting.DEVICE_ID_LENGTH)
                ));
            }
        });
       // inputDialog.setTypeface(Typeface.SERIF);
        inputDialog.show(getFragmentManager(), "");
        //inputDialog.
//        InputDialog.create(
//                getResources().getString(R.string.initial_user),
//                InitialSettingActivity.this,
//                mFragmentDetailCallback,
//                EXTRA_APPID_GENERATOR).show();
    }


}
