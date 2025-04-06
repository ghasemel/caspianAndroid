package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Mali.Transfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.util.List;

import info.elyasi.android.elyasilib.UI.FormActionType;
import info.elyasi.android.elyasilib.UI.IAsyncForm;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.Models.MaliModel;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL.TransferMaliListPLL;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Mali.MaliActivity;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Mali.MaliFragment;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 3/9/2016.
 */
public class MaliTransferListActivity extends CaspianActivitySingleFragment {
    private static final String TAG = "MaliConfirmListFragment";

    private static final int REQUEST_NEW_MALI = 1;

    @Override
    public void onCreate(Bundle savedBundleState) {
        Log.d(TAG, "starting");

        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog, R.string.mali_list_title);

        super.onCreate(savedBundleState);

        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        return new MaliTransferListFragment();
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

            case Actions.ACTION_NEW_MALI:
                showNewMaliForResult(this, REQUEST_NEW_MALI);
                break;

            case Actions.ACTION_TRANSFER_MALI:
                if (parameter != null && parameter[0] instanceof List) {
                    transferMali((List<MaliModel>) parameter[0]);
                }
                break;

            case Actions.ACTION_CONFIRM_MALI_DONE:
                updateMaliTransferList();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_NEW_MALI:
                updateMaliTransferList();
                break;
        }
    }


    private void updateMaliTransferList()
    {
        if (getFragmentContainer() != null && getFragmentContainer() instanceof IFragmentCallback) {
            ((IFragmentCallback) getFragmentContainer())
                    .onMyActivityCallback(MaliTransferListFragment.REFRESH_LIST, null, null);
        }
    }


    private void transferMali(List<MaliModel> selectedMaliList) {
        Log.d(TAG, "transferMali()");
        if (getFragmentContainer() instanceof IAsyncForm) {
            TransferMaliListPLL pll =
                    new TransferMaliListPLL
                            (
                                    getApplicationContext(),
                                    (IAsyncForm) getFragmentContainer(),
                                    this
                            );

            pll.start(selectedMaliList);
        }
    }

    public static void showNewMaliForResult(Activity activity, int requestCode) {
        Intent i = new Intent(activity.getApplicationContext(), MaliActivity.class);
        i.putExtra(MaliFragment.EXTRA_ACTION_NEW, "");
        activity.startActivityForResult(i, requestCode);
    }
}
