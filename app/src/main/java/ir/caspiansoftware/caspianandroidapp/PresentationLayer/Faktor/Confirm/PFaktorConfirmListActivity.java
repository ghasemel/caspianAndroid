package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor.Confirm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import info.elyasi.android.elyasilib.UI.FormActionType;
import info.elyasi.android.elyasilib.UI.IAsyncForm;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.Models.MPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL.SendPreInvoiceListPLL;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor.PFaktorActivity;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor.PFaktorFragment;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 3/9/2016.
 */
public class PFaktorConfirmListActivity extends CaspianActivitySingleFragment {
    private static final String TAG = "InvoiceConfirmListAct";

    private static final int REQUEST_NEW_INVOICE = 1;

    @Override
    public void onCreate(Bundle savedBundleState) {
        Log.d(TAG, "starting");

        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog, R.string.preInvoice_list_title);

        super.onCreate(savedBundleState);

        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        return new PFaktorConfirmListFragment();
    }


    @SuppressWarnings("unchecked")
    @Override
    public void onMyFragmentCallBack(String actionName, FormActionType actionType, Object... parameter) {
        // do nothing !
        Log.d(TAG, "onMyFragmentCallBack(): actionName = " + actionName);

        switch (actionName) {
            case Actions.ACTION_TOOLBAR_EXIT:
                this.finish();
                break;

            case Actions.ACTION_PRE_INVOICE:
                showNewInvoiceForResult(this, REQUEST_NEW_INVOICE);
                break;

            case Actions.ACTION_CONFIRM_PFaktor:
                if (parameter != null && parameter[0] instanceof List) {
                    confirmPreInvoice((List<MPFaktorModel>) parameter[0]);
                }
                break;

            case Actions.ACTION_CONFIRM_PFaktor_DONE:
                updatePFaktorConfirmList();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_NEW_INVOICE:
                updatePFaktorConfirmList();
                break;
        }
    }


    private void updatePFaktorConfirmList()
    {
        if (getFragmentContainer() != null && getFragmentContainer() instanceof IFragmentCallback) {
            ((IFragmentCallback) getFragmentContainer())
                    .onMyActivityCallback(PFaktorConfirmListFragment.REFRESH_LIST, null, null);
        }
    }


    private void confirmPreInvoice(List<MPFaktorModel> selectedInvoiceList) {
        Log.d(TAG, "confirmPreInvoice()");
        if (getFragmentContainer() instanceof IAsyncForm) {
            SendPreInvoiceListPLL pll =
                    new SendPreInvoiceListPLL
                            (
                                    getApplicationContext(),
                                    (IAsyncForm) getFragmentContainer(),
                                    this
                            );

            pll.start(selectedInvoiceList);
        }
    }

    public static void showNewInvoice(Context context) {
        Intent i = new Intent(context, PFaktorActivity.class);
        i.putExtra(PFaktorFragment.EXTRA_ACTION_NEW, "");
        context.startActivity(i);
    }

    public static void showNewInvoiceForResult(Activity activity, int requestCode) {
        Intent i = new Intent(activity.getApplicationContext(), PFaktorActivity.class);
        i.putExtra(PFaktorFragment.EXTRA_ACTION_NEW, "");
        activity.startActivityForResult(i, requestCode);
    }
}
