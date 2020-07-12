package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.DaftarTaf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;

import info.elyasi.android.elyasilib.UI.AListRowFragment;
import info.elyasi.android.elyasilib.UI.FormActionTypes;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.List.PersonListActivity;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Ghasem on 4/22/2017.
 */

public class DaftarTafActivity extends CaspianActivitySingleFragment {
    public static final String TAG = "DaftarTafActivity";


    public static final int REQUEST_PERSON_LIST = 1;
    public static final String ACTION_SELECT_PERSON_LIST = "action_select_person_list";

    public static final String ACTION_SHOW_REPORT = "action_show_report";

    private IFragmentCallback mFragmentCallback;


    @Override
    public void onCreate(Bundle savedBundleState) {
        Log.d(TAG, "start...");
        showAsPopup(this, getResources().getInteger(R.integer.popup_daf_taf_height), getResources().getInteger(R.integer.popup_daf_taf_width));
        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog, R.string.daf_taf_title);

        Log.d(TAG, String.valueOf(getResources().getInteger(R.integer.popup_daf_taf_height)));
        Log.d(TAG, String.valueOf(getResources().getInteger(R.integer.popup_daf_taf_width)));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int densityDpi = (int)(displayMetrics.density * 160f);

        float density = getResources().getDisplayMetrics().density;

        super.onCreate(savedBundleState);

        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        return new DaftarTafFragment();
    }

    @Override
    public void onMyFragmentCallBack(String actionName, FormActionTypes actionTypes, Object[] parameters) {
        Log.d(TAG, "onMyFragmentCallBack(): actionName = " + actionName);

        switch (actionName) {
            case ACTION_SELECT_PERSON_LIST:
                Intent intent = new Intent(this, PersonListActivity.class);
                intent.putExtra(AListRowFragment.EXTRA_RETURN_NAME, "");
                startActivityForResult(intent, REQUEST_PERSON_LIST);
                break;

            case ACTION_SHOW_REPORT:
                Intent in = new Intent(this, DaftarTafReportActivity.class);
                in.putExtra(DaftarTafReportActivity.EXTRA_PERSON_NAME, parameters[0].toString());
                in.putExtra(DaftarTafReportActivity.EXTRA_PERSON_CODE, parameters[1].toString());
                in.putExtra(DaftarTafReportActivity.EXTRA_FROM_DATE, parameters[2].toString());
                in.putExtra(DaftarTafReportActivity.EXTRA_TILL_DATE, parameters[3].toString());
                startActivityForResult(in, -1);
                break;
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof IFragmentCallback) {
            mFragmentCallback = (IFragmentCallback) fragment;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_PERSON_LIST:
                if (resultCode == Activity.RESULT_OK) {
                    PersonModel person = (PersonModel) data.getSerializableExtra(AListRowFragment.EXTRA_RETURN_NAME);
                    mFragmentCallback.onMyActivityCallback(ACTION_SELECT_PERSON_LIST, person, null);
                }
                break;
        }
    }


}
