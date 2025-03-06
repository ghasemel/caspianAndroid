package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Mali;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import info.elyasi.android.elyasilib.UI.AListRowFragment;
import info.elyasi.android.elyasilib.UI.FormActionTypes;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.Models.MPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor.Search.PFaktorSearchActivity;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.List.PersonListActivity;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 7/14/2016.
 */
public class MaliActivity extends CaspianActivitySingleFragment {
    private static final String TAG = "MaliActivity";

    private static final int REQUEST_PERSON_LIST = 1;
    public static final String ACTION_SELECT_PERSON_LIST = "action_select_person_list";


    private static final int REQUEST_INVOICE_ADD_KALA = 2;
    private static final int REQUEST_INVOICE_EDIT_KALA = 3;
    public static final String ACTION_INVOICE_KALA = "action_invoice_kala";

    private static final int REQUEST_INVOICE_SEARCH = 4;
    public static final String ACTION_INVOICE_SEARCH = "action_invoice_search";

    private static final int REQUEST_MANDE = 4;

    private IFragmentCallback myFragment;

    private ActivityResultLauncher<Intent> personListLauncher;
    private ActivityResultLauncher<Intent> invoiceSearchLauncher;



    @Override
    public void onCreate(Bundle savedBundleState) {
        Log.d(TAG, "onCreate(): function entered");
        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog, R.string.preInvoice_title);

        super.onCreate(savedBundleState);

        CaspianActionbar.setActionbarEvents(this);

        registerActivityResultLaunchers();
    }

    @Override
    public Fragment createFragment() {
        var maliFragment = new MaliFragment();
        myFragment = maliFragment;
        return maliFragment;
    }

    private void registerActivityResultLaunchers() {
        personListLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        PersonModel personModel = (PersonModel) result.getData().getSerializableExtra(AListRowFragment.EXTRA_SELECTED_OBJECT);
                        myFragment.onMyActivityCallback(ACTION_SELECT_PERSON_LIST, personModel, null);
                    }
                });

        invoiceSearchLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        MPFaktorModel mpFaktorModel = (MPFaktorModel) result.getData().getSerializableExtra(AListRowFragment.EXTRA_SELECTED_OBJECT);
                        myFragment.onMyActivityCallback(ACTION_INVOICE_SEARCH, mpFaktorModel, null);
                    }
                });
    }

    @Override
    public void onMyFragmentCallBack(String actionName, FormActionTypes actionTypes, Object... parameter) {
        Log.d(TAG, "onMyFragmentCallBack(): actionName= " + actionName);

        switch (actionName) {
            case ACTION_SELECT_PERSON_LIST:
                Intent intent = new Intent(this, PersonListActivity.class);
                personListLauncher.launch(intent);
                break;


            case ACTION_INVOICE_SEARCH:
                Intent intent_search = new Intent(this, PFaktorSearchActivity.class);
                invoiceSearchLauncher.launch(intent_search);
                break;
        }
    }


    public static void showNewMaliForResult(Activity activity, int requestCode) {
        Intent i = new Intent(activity.getApplicationContext(), MaliActivity.class);
        i.putExtra(MaliFragment.EXTRA_ACTION_NEW, "");
        activity.startActivityForResult(i, requestCode);
    }


}
