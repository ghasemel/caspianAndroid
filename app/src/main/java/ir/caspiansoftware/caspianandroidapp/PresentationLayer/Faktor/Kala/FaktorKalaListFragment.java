package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor.Kala;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import info.elyasi.android.elyasilib.UI.FormActionType;
import info.elyasi.android.elyasilib.UI.IActivityCallback;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import info.elyasi.android.elyasilib.UI.UIUtility;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianSearchableListFragment;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.R;


/**
 * Created by Canada on 7/22/2016.
 */
public class FaktorKalaListFragment extends CaspianSearchableListFragment<KalaModel> implements IFragmentCallback {
    private static final String TAG = "FaktorKalaListFragment";

    private EditText mSearchCode;
    private EditText mSearchName;
    private LinearLayout mBtnExit;
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
        mSearchCode = parentView.findViewById(R.id.search_kala_code);
        mSearchName = parentView.findViewById(R.id.search_kala_name);

        mBtnExit = parentView.findViewById(R.id.toolbar_exit);
        UIUtility.setButtonEffect(mBtnExit);
        mBtnExit.setOnClickListener(this);

        Log.d(TAG, "mapViews(): function end");
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
        return R.layout.fragment_kala_cell_list;
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
        return getResources().getString(R.string.fragment_pfaktor_kala_list_row_tag);
    }

    @Override
    public void onMyActivityCallback(String actionName, Object parameter, FormActionType formActionType) {

    }



    @Override
    public void OnCellClick(KalaModel kalaModel, int row, int cellId, View cellView) {
        Log.d(TAG, "OnCellClick()");

        if (cellId == R.id.cell_gallery) {
            mActivityCallback.onMyFragmentCallBack(Actions.ACTION_OPEN_KALA_GALLERY, FormActionType.VIEW, kalaModel);
        }
    }
}
