package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.DaftarTaf;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import info.elyasi.android.elyasilib.Dialogs.DatePickerDialog;
import info.elyasi.android.elyasilib.Persian.PersianDate;
import info.elyasi.android.elyasilib.UI.FormActionTypes;
import info.elyasi.android.elyasilib.UI.IActivityCallback;
import info.elyasi.android.elyasilib.UI.UIUtility;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Ghasem on 4/22/2017.
 */

public class DaftarTafFragment extends CaspianFragment implements IFragmentCallback {
    private static final String TAG = "DaftarTafFragment";

    private IActivityCallback mActivityCallback;

    private EditText mEditTextFromDate;
    private EditText mEditTextToDate;
    private EditText mEditTextPersonCode;

    private TextView mTextViewPersonName;

    private ImageView mBtnSelectPerson;
    private ImageView mBtnSelectFromDate;
    private ImageView mBtnSelectToDate;
    private Button mBtnOK;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_daf_taf_selection;
    }

    @Override
    protected void mapViews(View parentView) {
        Log.d(TAG, "mapViews(): start...");

        mEditTextFromDate = (EditText) parentView.findViewById(R.id.editText_fromDate);
        mEditTextToDate = (EditText) parentView.findViewById(R.id.editText_toDate);
        mEditTextPersonCode = (EditText) parentView.findViewById(R.id.editText_daf_taf_report_person_code);

        mTextViewPersonName = (TextView) parentView.findViewById(R.id.editText_person_name);

        mBtnSelectFromDate = (ImageView) parentView.findViewById(R.id.btn_fromDate_select);
        mBtnSelectToDate = (ImageView) parentView.findViewById(R.id.btn_toDate_select);
        mBtnSelectPerson = (ImageView) parentView.findViewById(R.id.btn_person_select);
        mBtnOK = (Button) parentView.findViewById(R.id.btn_OK);

        UIUtility.setButtonEffect(mBtnSelectFromDate, this);
        UIUtility.setButtonEffect(mBtnSelectToDate, this);
        UIUtility.setButtonEffect(mBtnSelectPerson, this);
        UIUtility.setButtonEffect(mBtnOK, this);



        mEditTextFromDate.setText(Vars.YEAR.getDoreModel().getStartDore());
        mEditTextToDate.setText(PersianDate.getToday());
    }

    @Override
    protected void afterMapViews(View parentView) {
        Log.d(TAG, "afterMapViews(): start...");
        Intent intent = getActivity().getIntent();

    }

    @Override
    public ProgressBar getProgressBar() {
        return null;
    }

    @Override
    public void activity_callback(String actionName, Object parameter, FormActionTypes formActionTypes) {
        switch (actionName) {
            case DaftarTafActivity.ACTION_SELECT_PERSON_LIST:
                Log.d(TAG, "activity_callback(): " + actionName);

                if (!(parameter instanceof PersonModel)) {
                    showError(R.string.invalid_parameter, null);
                    return;
                }

                PersonModel person = (PersonModel) parameter;
                mEditTextPersonCode.setText(person.getCode());
                mTextViewPersonName.setText(person.getName());
                break;
        }
    }



    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick(): start...");

        if (view.equals(mBtnOK)) {
            btnOK_click();
        } else if (view.equals(mBtnSelectFromDate)) {
            DatePickerDialog.SelectDate(mEditTextFromDate, getActivity().getFragmentManager(), null);
        } else if (view.equals(mBtnSelectPerson)) {
            selectPerson();
        } else if (view.equals(mBtnSelectToDate)) {
            DatePickerDialog.SelectDate(mEditTextToDate, getActivity().getFragmentManager(), null);
        }
    }

    private void btnOK_click() {
        Log.d(TAG, "btnOK_click(): enter");

        if (mEditTextPersonCode.getText().toString().trim().isEmpty()) {
            showToast(getActivity()
                    .getApplicationContext()
                    .getResources()
                    .getString(R.string.no_person_selected));
            return;
        }

        mActivityCallback.fragment_callback(
                DaftarTafActivity.ACTION_SHOW_REPORT, null,
                mTextViewPersonName.getText().toString(),
                mEditTextPersonCode.getText().toString(),
                mEditTextFromDate.getText().toString(),
                mEditTextToDate.getText().toString()
        );
        /*Intent intent = new Intent(getActivity().getApplicationContext(), DaftarTafReportActivity.class);
        intent.putExtra(DaftarTafReportActivity.EXTRA_PERSON_CODE, mEditTextPersonCode.getText().toString());
        intent.putExtra(DaftarTafReportActivity.EXTRA_FROM_DATE, mEditTextFromDate.getText().toString());
        intent.putExtra(DaftarTafReportActivity.EXTRA_TILL_DATE, mEditTextToDate.getText().toString());
        getActivity().getApplicationContext().startActivity(intent);*/
    }

    private void selectPerson() {
        mActivityCallback.fragment_callback(DaftarTafActivity.ACTION_SELECT_PERSON_LIST, null, (Object) null);
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
}
