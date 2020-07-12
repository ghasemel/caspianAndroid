package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.MandeList;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import info.elyasi.android.elyasilib.UI.FormActionTypes;
import info.elyasi.android.elyasilib.UI.IActivityCallback;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import info.elyasi.android.elyasilib.UI.UIUtility;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianSearchableListFragment;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.R;


/**
 * Created by Canada on 7/22/2016.
 */
public class PersonMandeListFragment extends CaspianSearchableListFragment<PersonModel> implements IFragmentCallback {
    private static final String TAG = "PersonMandeListFragment";

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
        mSearchCode = (EditText) parentView.findViewById(R.id.search_person_code);
        mSearchName = (EditText) parentView.findViewById(R.id.search_person_name);

        mBtnExit = (LinearLayout) parentView.findViewById(R.id.toolbar_exit);
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
        return R.layout.fragment_person_mande_list;
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
        return getResources().getString(R.string.fragment_person_mande_list_row_tag);
    }

    @Override
    public void onMyActivityCallback(String actionName, Object parameter, FormActionTypes formActionTypes) {

    }
}
