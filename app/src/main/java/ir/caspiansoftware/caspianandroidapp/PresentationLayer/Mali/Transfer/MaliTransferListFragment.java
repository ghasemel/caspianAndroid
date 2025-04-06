package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Mali.Transfer;

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
import ir.caspiansoftware.caspianandroidapp.Models.MaliModel;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 3/8/2016.
 */
public class MaliTransferListFragment extends CaspianDataGridFragment<MaliModel> implements IFragmentCallback {
    private static final String TAG = "MaliConfirmListFragment";

    public static final String REFRESH_LIST = "refresh_list";

    private ProgressBar mProgressBar;
    private LinearLayout mToolbarExit;
    private IActivityCallback mActivityCallback;

    private LinearLayout mToolbarNewMali;
    private LinearLayout mToolbarSyncBtn;

    private List<MaliModel> mSelectedRowsList;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mali_transfer_list;
    }

    @Override
    protected String getFragmentRowTagValue() {
        return getString(R.string.fragment_mali_confirm_row_tag);
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
        mToolbarNewMali = (LinearLayout) parentView.findViewById(R.id.toolbar_new_mali);
        mToolbarSyncBtn = (LinearLayout) parentView.findViewById(R.id.toolbar_sync_selection);
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

        } else if (v.equals(mToolbarNewMali)) {
            Log.d(TAG, Actions.ACTION_NEW_MALI);
            mActivityCallback.onMyFragmentCallBack(Actions.ACTION_NEW_MALI, FormActionType.New);

        } else  if (v.equals(mToolbarSyncBtn)) {
            Log.d(TAG, Actions.ACTION_TRANSFER_MALI);

            if (mSelectedRowsList.isEmpty()) {
                messageBoxOK(R.string.mali_list_title, R.string.no_rows_selected, null);
                return;
            }

            mActivityCallback.onMyFragmentCallBack(Actions.ACTION_TRANSFER_MALI, null, mSelectedRowsList);
        }
    }


    @Override
    public void OnCellClick(MaliModel maliModel, int row, int cellId, View cellView) {
        Log.d(TAG, "OnCellClick() start: row = " + row);
        if (cellId == R.id.cell_synced) {
            if (!maliModel.isSynced()) {
                if (cellView instanceof ImageView) {
                    ImageView img = (ImageView) cellView;
                    if (DrawableExt.equal(img.getDrawable(), android.R.drawable.checkbox_off_background, getContext())) {
                        mSelectedRowsList.add(maliModel);
                        img.setImageResource(android.R.drawable.checkbox_on_background);
                    } else {
                        img.setImageResource(android.R.drawable.checkbox_off_background);
                        mSelectedRowsList.remove(maliModel);
                    }
                }
            } else {
                messageBoxOK(R.string.mali_list_title, R.string.mali_synced_already, null);
            }

            Log.d(TAG, "cell_delete clicked: row " + row);
        }
    }

    @Override
    public void OnItemClick(MaliModel maliModel, int position) {
        Log.d(TAG, "OnItemClick(): position = " + position);
//        if (getRowFragment().getListAdapter() instanceof PFaktorConfirmListRow.PreMaliConfirmListAdapter) {
//            ImageView imageView =
//                    ((PFaktorConfirmListRow.PreMaliConfirmListAdapter) getRowFragment().getListAdapter())
//                            .getButtonCell(5, position);
//            OnCellClick(mpFaktorModel, position, imageView.getId(), imageView);
//        }
    }
}
