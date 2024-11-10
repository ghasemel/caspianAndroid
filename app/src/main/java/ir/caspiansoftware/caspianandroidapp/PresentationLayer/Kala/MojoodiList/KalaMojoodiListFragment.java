package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Kala.MojoodiList;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import info.elyasi.android.elyasilib.UI.FormActionTypes;
import info.elyasi.android.elyasilib.UI.IActivityCallback;
import info.elyasi.android.elyasilib.UI.IAfterViewMappingCallBack;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import info.elyasi.android.elyasilib.UI.UIUtility;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianSearchableListFragment;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.R;


/**
 * Created by Canada on 7/22/2016.
 */
public class KalaMojoodiListFragment extends CaspianSearchableListFragment<KalaModel> implements IFragmentCallback, IAfterViewMappingCallBack {
    private static final String TAG = "KalaMojoodiListFragment";

    private EditText mSearchCode;
    private EditText mSearchName;
    private LinearLayout mBtnExit;

    private HorizontalScrollView horizontalScrollView;

    private IActivityCallback mActivityCallback;

    @Override
    public void onClick(View view) {
        if (view.equals(mBtnExit)) {
            mActivityCallback.onMyFragmentCallBack(Actions.ACTION_TOOLBAR_EXIT, null);
        }
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
    protected void mapViews(View parentView) {
        mSearchCode = (EditText) parentView.findViewById(R.id.search_kala_code);
        mSearchName = (EditText) parentView.findViewById(R.id.search_kala_name);

        mBtnExit = (LinearLayout) parentView.findViewById(R.id.toolbar_exit);
        UIUtility.setButtonEffect(mBtnExit);
        mBtnExit.setOnClickListener(this);

        horizontalScrollView = parentView.findViewById(R.id.horizontal_scroll);
        horizontalScrollView.post(this::setScrollBarToZero);

        Log.d(TAG, "mapViews(): function end");
    }

    @Override
    protected void onAfterMapViews() {
        mSearchCode.requestFocus();
    }

    @Override
    public HorizontalScrollView getHorizontalScrollView() {
        return horizontalScrollView;
    }

    @Override
    protected EditText[] getSearchEditTexts() {
        return new EditText[] {
                mSearchCode,
                mSearchName
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_kala_mojoodi_list;
    }

    @Override
    protected String[] getSearchedTextValueOnTextChange() {
        return new String[] {
                mSearchCode.getText().toString(),
                mSearchName.getText().toString()
        };
    }

    @Override
    protected String getFragmentRowTagValue() {
        return getResources().getString(R.string.fragment_kala_list_mojoodi_row_tag);
    }

    @Override
    public void onMyActivityCallback(String actionName, Object parameter, FormActionTypes formActionTypes) {

    }



    @Override
    public void OnCellClick(KalaModel kalaModel, int row, int cellId, View cellView) {
        Log.d(TAG, "OnCellClick()");

        if (cellId == R.id.cell_gallery) {
            mActivityCallback.onMyFragmentCallBack(Actions.ACTION_OPEN_KALA_GALLERY, FormActionTypes.VIEW, kalaModel);
        }
    }
}
