package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor.Kala;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;

import info.elyasi.android.elyasilib.Controls.ClearableEditText.ClearableEditText;
import info.elyasi.android.elyasilib.Dialogs.DialogResult;
import info.elyasi.android.elyasilib.Dialogs.IDialogCallback;
import info.elyasi.android.elyasilib.UI.FormActionTypes;
import info.elyasi.android.elyasilib.UI.IActivityCallback;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import info.elyasi.android.elyasilib.UI.UIUtility;
import info.elyasi.android.elyasilib.Utility.NumberExt;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.PermissionBLL;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonLastSellInfoModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.Models.SPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL.LastSellPricePLL;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL.MojoodiPLL;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 7/24/2016.
 */
public class FaktorKalaFragment extends CaspianFragment implements IFragmentCallback, TextWatcher {

    private static final String TAG = "FaktorKalaFragment";

    public static final String EXTRA_SP_FAKTOR_MODEL = "extra_sp_faktor_id";
    public static final String EXTRA_RESULT_NAME = "extra_result_name";
    public static final String EXTRA_PERSON_MODEL = "extra_person_model";

    private ProgressBar mProgressBar;

    private ImageView mBtnSelectKala;
    private IActivityCallback mActivityCallback;
    private EditText mKalaCodeEditText;
    private TextView mKalaNameTextView;
    private TextView mVahedATextView;
    private TextView mVahedFTextView;
    private EditText mMCountEditText;
    private EditText mSCountEditText;
    private ClearableEditText mPriceEditText;
    private TextView mTotalPriceTextView;
    private TextView mTitleTextView;
    private Button mBtnOk;
    private Button mBtnCancel;
    private PersonModel mPerson;
    private KalaModel mKala;
    private SPFaktorModel mSPFaktorModel;
    private String mResultExtra;
    private ImageView mBtnMojoodi;
    private ImageView mBtnLastSellPrice;

    @Override
    public void onResume() {
        super.onResume();

        Intent intent  = getActivity().getIntent();
        if (!intent.hasExtra(EXTRA_RESULT_NAME) || intent.getStringExtra(EXTRA_RESULT_NAME).equals("")) {
            showError("EXTRA_RESULT_NAME has not been set", new cancel_dialog_callback());
        } else {
            mResultExtra = intent.getStringExtra(EXTRA_RESULT_NAME);
        }

        if (!intent.hasExtra(EXTRA_PERSON_MODEL) || intent.getSerializableExtra(EXTRA_PERSON_MODEL) == null) {
            showError("any person not specified by EXTRA_PERSON_MODEL", new cancel_dialog_callback());
        } else {
            mPerson = (PersonModel) intent.getSerializableExtra(EXTRA_PERSON_MODEL);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_faktor_kala;
    }

    @Override
    protected void afterMapViews(View parentView) {
        Log.d(TAG, "afterMapViews()");
        Intent intent  = getActivity().getIntent();
        if (intent.hasExtra(EXTRA_SP_FAKTOR_MODEL)) {
            mTitleTextView.setText(R.string.invoice_kala_title_edit);

            try {
                setSPFaktorModel((SPFaktorModel) intent.getSerializableExtra(EXTRA_SP_FAKTOR_MODEL));
                if (mSPFaktorModel == null) {
                    showError(R.string.invoice_spfaktor_id_not_found, new cancel_dialog_callback());
                }
            } catch (Exception ex) {
                showError(ex, new cancel_dialog_callback());
            }

        } else {
            mTitleTextView.setText(R.string.invoice_kala_title_new);
            mSPFaktorModel = new SPFaktorModel();
        }
    }

    @Override
    protected void mapViews(View parentView) {
        Log.d(TAG, "mapViews()");

        mTitleTextView = (TextView) parentView.findViewById(R.id.label_invoiceKalaTitle);

        mBtnSelectKala = (ImageView) parentView.findViewById(R.id.btn_kala_select);
        UIUtility.setButtonEffect(mBtnSelectKala, this);
        //mBtnSelectKala.setOnClickListener(this);

        mKalaCodeEditText = (EditText) parentView.findViewById(R.id.editText_invoiceKalaCode);
        mKalaNameTextView = (TextView) parentView.findViewById(R.id.label_invoiceKalaName);

        mVahedATextView = (TextView) parentView.findViewById(R.id.label_invoiceVahedA);
        mVahedFTextView = (TextView) parentView.findViewById(R.id.label_invoiceVahedF);

        mBtnOk = (Button) parentView.findViewById(R.id.btn_invoiceKalaOK);
        mBtnOk.setOnClickListener(this);
        mBtnOk.setOnTouchListener(this);

        mBtnCancel = (Button) parentView.findViewById(R.id.btn_invoiceKalaCancel);
        mBtnCancel.setOnClickListener(this);
        mBtnCancel.setOnTouchListener(this);


        mSCountEditText = (EditText) parentView.findViewById(R.id.editText_invoiceSCount);
        mSCountEditText.addTextChangedListener(this);

        mPriceEditText = (ClearableEditText) parentView.findViewById(R.id.editText_invoicePrice);
        mPriceEditText.addTextChangedListener(this);
        //mPriceEditText.setIconLocation(ClearableEditText.Location.LEFT);

        mTotalPriceTextView = (TextView) parentView.findViewById(R.id.label_invoiceTotalPrice);

        mMCountEditText = (EditText) parentView.findViewById(R.id.editText_invoiceMCount);
        mMCountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mKala == null)
                    return;

                if (!editable.toString().equals("") && !editable.toString().equals("0")) {
                    mSCountEditText.setEnabled(false);
                    mMCountEditText.setNextFocusDownId(R.id.editText_invoicePrice);

                    double mcount = Double.parseDouble(mMCountEditText.getText().toString());
                    mSCountEditText.setText(String.valueOf(mcount * mKala.getMohtavi()));
                } else {
                    mSCountEditText.setEnabled(true);
                    mMCountEditText.setNextFocusDownId(R.id.editText_invoiceSCount);
                }
            }
        });


        mBtnMojoodi = (ImageView) parentView.findViewById(R.id.btn_mojoodi);
        UIUtility.setButtonEffect(mBtnMojoodi, this);
        if (!PermissionBLL.mojoodiAccess()) {
            mBtnMojoodi.setVisibility(View.INVISIBLE);
            TextView label = (TextView) parentView.findViewById(R.id.label_mojoodi);
            label.setVisibility(View.INVISIBLE);
        }

        mBtnLastSellPrice = (ImageView) parentView.findViewById(R.id.btn_last_sell_price);
        UIUtility.setButtonEffect(mBtnLastSellPrice, this);
        if (!PermissionBLL.lastSellPriceAccess()) {
            mBtnLastSellPrice.setVisibility(View.INVISIBLE);
            TextView label = (TextView) parentView.findViewById(R.id.label_last_sell_price);
            label.setVisibility(View.INVISIBLE);
        }

        mProgressBar = (ProgressBar) parentView.findViewById(R.id.progressBar);

        mSCountEditText.setEnabled(false);
        mPriceEditText.setEnabled(false);
        mMCountEditText.setEnabled(false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mActivityCallback = (IActivityCallback) activity;
    }

    @Override
    public boolean onTouch(View sender, MotionEvent motionEvent) {
        UIUtility.onTouchEffect(sender, motionEvent);
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(mBtnSelectKala)) {
            mActivityCallback.fragment_callback(FaktorKalaActivity.ACTION_SELECT_KALA_LIST, null, (Object) null);

        } else if (view.equals(mBtnOk)) {
            btnOk_click();

        } else if (view.equals(mBtnCancel)) {
            btnCancel_click();

        } else if (view.equals(mBtnMojoodi) && mBtnMojoodi.getVisibility() == View.VISIBLE) { // mBtnMojoodi
            // get mojoodi info from server
            getMojoodi(mKala);

        } else if (view.equals(mBtnLastSellPrice) && mBtnLastSellPrice.getVisibility() == View.VISIBLE) {
            getLastPrice(mKala, mPerson);
        }
    }


    private void getMojoodi(KalaModel kala) {
        if (kala != null) {
            try {
                MojoodiPLL mojoodiPLL = new MojoodiPLL(getContext(), this, this);
                mojoodiPLL.start(kala);
            } catch (Exception ex) {
                showError(ex, null);
            }
        }
    }

    private void getLastPrice(KalaModel kala, PersonModel person) {
        if (kala != null && person != null) {
            try {
                LastSellPricePLL sellPricePLL = new LastSellPricePLL(getContext(), this, this);
                sellPricePLL.start(person, kala);
            } catch (Exception ex) {
                showError(ex, null);
            }
        }
    }


    public void btnOk_click() {
        try {
            if (mKala == null || mKalaCodeEditText.getText().toString().trim().equals("")) {
                messageBoxOK(R.string.required_info_title, R.string.invoice_kala_kala_not_select, null);
                return;
            }

            if ((mMCountEditText.getText().toString().trim().equals("") || mMCountEditText.getText().toString().trim().equals("0")) &&
                    mSCountEditText.getText().toString().trim().equals("") || mSCountEditText.getText().toString().trim().equals("0")) {

                messageBoxOK(R.string.required_info_title, R.string.invoice_kala_scount_mcount_not_entered, null);
                return;
            }

            if (mPriceEditText.getText().toString().trim().equals("") || mPriceEditText.getText().toString().trim().equals("0")) {
                messageBoxOK(R.string.error_title, R.string.invoice_kala_price_not_entered, null);
                return;
            }

            mSPFaktorModel.setKalaId_FK(mKala.getID());
            if (!mMCountEditText.getText().toString().equals(""))
                mSPFaktorModel.setMCount(Double.parseDouble(mMCountEditText.getText().toString()));
            else
                mSPFaktorModel.setMCount(0);

            if (!mSCountEditText.getText().toString().equals(""))
                mSPFaktorModel.setSCount(Double.parseDouble(mSCountEditText.getText().toString()));
            else
                mSPFaktorModel.setSCount(0);

            mSPFaktorModel.setPrice(Long.parseLong(mPriceEditText.getText().toString()));
            mSPFaktorModel.setKalaModel(mKala);

            Intent intent = new Intent();
            intent.putExtra(mResultExtra, mSPFaktorModel);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();

        } catch (Exception ex) {
            messageBoxOK(R.string.error_title, ex.getMessage(), null);
        }
    }

    public void btnCancel_click() {
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    @Override
    public ProgressBar getProgressBar() {
        return mProgressBar;
    }


    @Override
    public void activity_callback(String actionName, Object parameter, FormActionTypes formActionTypes) {

        switch (actionName) {
            case FaktorKalaActivity.ACTION_SELECT_KALA_LIST:
                if (parameter instanceof KalaModel) {
                    setKala((KalaModel) parameter);
                }
                break;


            case Actions.ACTION_GET_MOJOODI:
                if (parameter != null && parameter instanceof Double) {
                    messageBoxOK(R.string.kala_moj_title,
                            String.format(
                                    getString(R.string.kala_moj_result),
                                    mKala.getName(),
                                    NumberExt.DigitSeparator((double) parameter),
                                    mKala.getVahedA()),
                            true,
                            null);
                }
                break;

            case Actions.ACTION_GET_LAST_SELL_PRICE:
                if (parameter != null && parameter instanceof PersonLastSellInfoModel) {
                    PersonLastSellInfoModel lastSellInfoModel = (PersonLastSellInfoModel) parameter;
                    messageBoxOK(R.string.person_last_sell_price_title,
                            String.format(getString(R.string.person_last_sell_price_result),
                                    NumberExt.DigitSeparator(lastSellInfoModel.getSellPrice()),
                                    lastSellInfoModel.getLastDate(),
                                    String.valueOf(lastSellInfoModel.getFaktorNum()),
                                    NumberExt.DigitSeparator(lastSellInfoModel.getAvancePresent()),
                                    NumberExt.DigitSeparator(lastSellInfoModel.getPriceAfterAvance())
                            ),
                            true,
                            null);
                }
                break;
        }

    }

    private void setSPFaktorModel(SPFaktorModel spFaktorModel) {
        mSPFaktorModel = spFaktorModel;
        if (spFaktorModel != null) {
            mMCountEditText.setText(String.valueOf(spFaktorModel.getMCount()));
            mSCountEditText.setText(String.valueOf(spFaktorModel.getSCount()));
            mPriceEditText.setText(String.valueOf(spFaktorModel.getPrice()));

            setKala(spFaktorModel);
            //setKala(spFaktorModel.getKalaModel());
        }
    }


    private void setKala(SPFaktorModel spFaktorModel) {
        if (spFaktorModel != null) {
            setKala(spFaktorModel.getKalaModel());

            mPriceEditText.setEnabled(false);
            mPriceEditText.setText(
                    String.valueOf(spFaktorModel.getPrice())
            );
            mPriceEditText.setEnabled(true);
        }
    }


    private void setKala(KalaModel kala) {
        mKala = kala;

        if (mKala != null) {
            mKalaCodeEditText.setText(kala.getCode());
            mKalaNameTextView.setText(kala.getName());
            mVahedATextView.setText(kala.getVahedA());
            mVahedFTextView.setText(kala.getVahedF());
            mPriceEditText.setText(String.valueOf(
                    kala.getActivePrice(Vars.USER.getKalaPriceType())
            ));

            if (mKala.getMohtavi() == 0 || mKala.getVahedF().equals("")) {
                mMCountEditText.setEnabled(false);
                mKalaCodeEditText.setNextFocusDownId(R.id.editText_invoiceSCount);
                mSCountEditText.requestFocus();
                UIUtility.ShowKeyboard(getActivity());
            } else {
                mMCountEditText.setEnabled(true);
                mMCountEditText.requestFocus();
                UIUtility.ShowKeyboard(getActivity());
            }

            mSCountEditText.setEnabled(true);
            mPriceEditText.setEnabled(true);

        }
    }



    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String value = editable.toString();
        Log.d(TAG, value);

        if (value.trim().equals("")) {
            mTotalPriceTextView.setText("");
            return;
        }

        try {
            if (!mSCountEditText.getText().toString().trim().equals("") &&
                    !mPriceEditText.getText().toString().trim().equals("")) {

                double scount = Double.parseDouble(mSCountEditText.getText().toString());
                long price = (long) Double.parseDouble(mPriceEditText.getText().toString());
                BigDecimal d = new BigDecimal(scount * price);
                mTotalPriceTextView.setText(NumberExt.DigitSeparator(d));
            } else {
                mTotalPriceTextView.setText("0");
            }
        } catch (Exception ex) {
            messageBoxOK(R.string.error_title, ex.getMessage(), null);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }


    class cancel_dialog_callback implements IDialogCallback<Object> {
        @Override
        public void dialog_callback(DialogResult dialogResult, Object result, int requestCode) {
            btnCancel_click();
        }
    }
}
