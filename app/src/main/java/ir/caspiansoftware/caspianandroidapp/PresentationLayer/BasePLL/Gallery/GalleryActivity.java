package ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL.Gallery;

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
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.DaftarTaf.DaftarTafReportActivity;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.List.PersonListActivity;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Ghasem on 4/22/2017.
 */

public class GalleryActivity extends CaspianActivitySingleFragment {
    public static final String TAG = "DaftarTafActivity";


    public static final int REQUEST_PERSON_LIST = 1;
    public static final String EXTRA_KALA = "extra_kala";

    private IFragmentCallback mFragmentCallback;


    @Override
    public void onCreate(Bundle savedBundleState) {
        Log.d(TAG, "start...");
        //showAsPopup(this, getResources().getInteger(R.integer.popup_daf_taf_height), getResources().getInteger(R.integer.popup_daf_taf_width));
        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog, R.string.gallery_activity_title);

        super.onCreate(savedBundleState);

        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        Intent intent = this.getIntent();
        Object kala = intent.getExtras().get(EXTRA_KALA);

        GalleryFragment fragment = new GalleryFragment();
        fragment.setKalaModel((KalaModel) kala);
        return fragment;
    }

    @Override
    public void onMyFragmentCallBack(String actionName, FormActionTypes actionTypes, Object[] parameters) {
        Log.d(TAG, "onMyFragmentCallBack(): actionName = " + actionName);

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
                }
                break;
        }
    }


}
