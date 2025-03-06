package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor.Confirm;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.UI.FormActionType;
import info.elyasi.android.elyasilib.UI.IActivityCallback;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import info.elyasi.android.elyasilib.Utility.DrawableExt;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianDataGridFragment;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianToolbar;
import ir.caspiansoftware.caspianandroidapp.Models.MPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 3/8/2016.
 */
public class PFaktorConfirmListFragment extends CaspianDataGridFragment<MPFaktorModel> implements IFragmentCallback {
    private static final String TAG = "InvoiceConfirmFragment";

    public static final String REFRESH_LIST = "refresh_list";

    private ProgressBar mProgressBar;
    private LinearLayout mToolbarExit;
    private IActivityCallback mActivityCallback;

    private RelativeLayout mToolbarNewInvoice;
    private RelativeLayout mToolbarSyncBtn;

    private List<MPFaktorModel> mSelectedRowsList;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pfaktor_confirm_list;
    }

    @Override
    protected String getFragmentRowTagValue() {
        return getString(R.string.fragment_invoice_confirm_row_tag);
    }

    @Override
    public void dataSetChanged(int rowCount) {

    }

    private void RefreshList() {
        getRowFragment().LoadDataAsync();
        mSelectedRowsList = new ArrayList<>();
    }

    @Override
    protected void afterFragmentRowAttached() {
        //getRowFragment().setDataList(null);
        //getRowFragment().setAutoLoad(true);
        RefreshList();
//        getRowFragment().LoadDataAsync();
//        mSelectedRowsList = new ArrayList<>();
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

    @Override
    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    @Override
    public void mapViews(View parentView) {
        View toolbar = parentView.findViewById(R.id.toolbar);
        CaspianToolbar.setToolbar(this, toolbar);

        mapToolbar(parentView);

        mProgressBar = (ProgressBar) parentView.findViewById(R.id.progressBar);
    }

    private void mapToolbar(View parentView) {
        mToolbarExit = (LinearLayout) parentView.findViewById(R.id.toolbar_exit);
        mToolbarNewInvoice = (RelativeLayout) parentView.findViewById(R.id.toolbar_new_invoice);
        mToolbarSyncBtn = (RelativeLayout) parentView.findViewById(R.id.toolbar_sync_selection);
    }

    @Override
    public void onMyActivityCallback(String actionName, Object parameter, FormActionType formActionType) {
        Log.d(TAG, "onMyFragmentCallBack start");

        switch (actionName) {
            case REFRESH_LIST:
                RefreshList();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick start");

        if (v.equals(mToolbarExit)) {
            //Log.d(TAG, Setting.ACTION_TOOLBAR_EXIT);
            mActivityCallback.onMyFragmentCallBack(Actions.ACTION_TOOLBAR_EXIT, null);

        } else if (v.equals(mToolbarNewInvoice)) {
            Log.d(TAG, Actions.ACTION_PRE_INVOICE);
            mActivityCallback.onMyFragmentCallBack(Actions.ACTION_PRE_INVOICE, FormActionType.New);

        } else  if (v.equals(mToolbarSyncBtn)) {
            Log.d(TAG, Actions.ACTION_CONFIRM_PFaktor);

            if (mSelectedRowsList.size() == 0) {
                messageBoxOK(R.string.preInvoice_list_title, R.string.no_rows_selected, null);
                return;
            }

            mActivityCallback.onMyFragmentCallBack(Actions.ACTION_CONFIRM_PFaktor, null, mSelectedRowsList);
        }
    }


    @Override
    public void OnCellClick(MPFaktorModel mpFaktorModel, int row, int cellId, View cellView) {
        Log.d(TAG, "OnCellClick() start: row = " + row);
        if (cellId == R.id.cell_synced) {
            if (!mpFaktorModel.isSynced()) {
                if (cellView instanceof ImageView) {
                    ImageView img = (ImageView) cellView;
                    if (DrawableExt.equal(img.getDrawable(), android.R.drawable.checkbox_off_background, getContext())) {
                        mSelectedRowsList.add(mpFaktorModel);
                        img.setImageResource(android.R.drawable.checkbox_on_background);
                    } else {
                        img.setImageResource(android.R.drawable.checkbox_off_background);
                        mSelectedRowsList.remove(mpFaktorModel);
                    }
                }
            } else {
                messageBoxOK(R.string.preInvoice_title, R.string.pfaktor_synced_already, null);
            }

            Log.d(TAG, "cell_delete clicked: row " + row);
        }
    }

    @Override
    public void OnItemClick(MPFaktorModel mpFaktorModel, int position) {
        Log.d(TAG, "OnItemClick(): position = " + position);
//        if (getRowFragment().getListAdapter() instanceof PFaktorConfirmListRow.PreInvoiceConfirmListAdapter) {
//            ImageView imageView =
//                    ((PFaktorConfirmListRow.PreInvoiceConfirmListAdapter) getRowFragment().getListAdapter())
//                            .getButtonCell(5, position);
//            OnCellClick(mpFaktorModel, position, imageView.getId(), imageView);
//        }
    }
}
