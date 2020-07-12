package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor;

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
import ir.caspiansoftware.caspianandroidapp.GPSTracker;
import ir.caspiansoftware.caspianandroidapp.Models.MPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.Models.SPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor.Kala.FaktorKalaActivity;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor.Kala.FaktorKalaFragment;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor.Search.PFaktorSearchActivity;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.List.PersonListActivity;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 7/14/2016.
 */
public class PFaktorActivity extends CaspianActivitySingleFragment {
    private static final String TAG = "PFaktorActivity";

    private static final int REQUEST_PERSON_LIST = 1;
    public static final String ACTION_SELECT_PERSON_LIST = "action_select_person_list";

    private static final int REQUEST_INVOICE_ADD_KALA = 2;
    private static final int REQUEST_INVOICE_EDIT_KALA = 3;
    public static final String ACTION_INVOICE_KALA = "action_invoice_kala";

    private static final int REQUEST_INVOICE_SEARCH = 4;
    public static final String ACTION_INVOICE_SEARCH = "action_invoice_search";

    private static final int REQUEST_MANDE = 4;


    private IFragmentCallback mFragmentCallback;

    @Override
    public void onCreate(Bundle savedBundleState) {
        Log.d(TAG, "onCreate(): function entered");
        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog, R.string.preInvoice_title);

        super.onCreate(savedBundleState);

        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        return new PFaktorFragment();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof  IFragmentCallback)
            mFragmentCallback = (IFragmentCallback) fragment;
    }

    @Override
    public void fragment_callback(String actionName, FormActionTypes actionTypes, Object... parameter) {
        Log.d(TAG, "fragment_callback(): actionName= " + actionName);

        //String actionName = ActionName.toString();
        switch (actionName) {
            case ACTION_SELECT_PERSON_LIST:
                Intent intent = new Intent(this, PersonListActivity.class);
                intent.putExtra(AListRowFragment.EXTRA_RETURN_NAME, "");
                startActivityForResult(intent, REQUEST_PERSON_LIST);
                break;

            case ACTION_INVOICE_KALA:
                Intent intent_invoice_kala = new Intent(this, FaktorKalaActivity.class);
                intent_invoice_kala.putExtra(actionTypes.name(), actionTypes);
                intent_invoice_kala.putExtra(FaktorKalaFragment.EXTRA_RESULT_NAME, ACTION_INVOICE_KALA);

                switch (actionTypes) {
                    case Edit:
                        if (parameter != null && parameter[0] instanceof SPFaktorModel && parameter[1] instanceof PersonModel) {
                            intent_invoice_kala.putExtra(FaktorKalaFragment.EXTRA_SP_FAKTOR_MODEL, (SPFaktorModel) parameter[0]);
                            intent_invoice_kala.putExtra(FaktorKalaFragment.EXTRA_PERSON_MODEL, (PersonModel) parameter[1]);
                            startActivityForResult(intent_invoice_kala, REQUEST_INVOICE_EDIT_KALA);
                        }
                        break;

                    case New:
                        if (parameter != null && parameter[0] instanceof PersonModel) {
                            intent_invoice_kala.putExtra(FaktorKalaFragment.EXTRA_PERSON_MODEL, (PersonModel) parameter[0]);
                            startActivityForResult(intent_invoice_kala, REQUEST_INVOICE_ADD_KALA);
                        }
                        break;
                }
                break;

            case ACTION_INVOICE_SEARCH:
                Intent intent_search = new Intent(this, PFaktorSearchActivity.class);
                intent_search.putExtra(AListRowFragment.EXTRA_RETURN_NAME, "");
                startActivityForResult(intent_search, REQUEST_INVOICE_SEARCH);
                break;

        }
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch(requestCode) {
            case GPSTracker.REQUEST_GPS_ACCESS:

                break;
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult(): requestCode = " + requestCode + ", resultCode = " + resultCode);

        switch (requestCode) {
            case REQUEST_PERSON_LIST:
                if (resultCode == Activity.RESULT_OK) {
                    PersonModel personModel = (PersonModel) data.getSerializableExtra(AListRowFragment.EXTRA_RETURN_NAME);
                    mFragmentCallback.activity_callback(ACTION_SELECT_PERSON_LIST, personModel, null);
                }
                break;


            case REQUEST_INVOICE_ADD_KALA:
            case REQUEST_INVOICE_EDIT_KALA:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, ACTION_INVOICE_KALA);

                    if (data != null && data.hasExtra(ACTION_INVOICE_KALA)) {
                        if (data.getSerializableExtra(ACTION_INVOICE_KALA) instanceof SPFaktorModel) {
                            if (requestCode == REQUEST_INVOICE_ADD_KALA)
                                mFragmentCallback.activity_callback(ACTION_INVOICE_KALA, data.getSerializableExtra(ACTION_INVOICE_KALA), FormActionTypes.New);
                            else
                                mFragmentCallback.activity_callback(ACTION_INVOICE_KALA, data.getSerializableExtra(ACTION_INVOICE_KALA), FormActionTypes.Edit);
                        }
                    }
                }
                break;


            case REQUEST_INVOICE_SEARCH:
                if (resultCode == Activity.RESULT_OK) {
                    MPFaktorModel mpFaktorModel = (MPFaktorModel) data.getSerializableExtra(AListRowFragment.EXTRA_RETURN_NAME);
                    mFragmentCallback.activity_callback(ACTION_INVOICE_SEARCH, mpFaktorModel, null);
                }
                break;
        }
    }
}
