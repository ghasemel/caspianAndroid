package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Mali;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Date;

import info.elyasi.android.elyasilib.Controls.ClearableEditText.ClearableEditText;
import info.elyasi.android.elyasilib.Dialogs.DatePickerDialog;
import info.elyasi.android.elyasilib.Dialogs.DialogResult;
import info.elyasi.android.elyasilib.Dialogs.IDialogCallback;
import info.elyasi.android.elyasilib.GPS.MapUtility;
import info.elyasi.android.elyasilib.Persian.PersianDate;
import info.elyasi.android.elyasilib.UI.FormActionType;
import info.elyasi.android.elyasilib.UI.IActivityCallback;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import info.elyasi.android.elyasilib.UI.MoveDirection;
import info.elyasi.android.elyasilib.UI.UIUtility;
import info.elyasi.android.elyasilib.Utility.NumberExt;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianToolbar;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.MaliBLL;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.PFaktorBLL;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.PermissionBLL;
import ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.TimeWebService;
import ir.caspiansoftware.caspianandroidapp.Enum.MaliType;
import ir.caspiansoftware.caspianandroidapp.GPSTracker;
import ir.caspiansoftware.caspianandroidapp.Models.MaliModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.Models.SPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL.MandePLL;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Report.ReportActivity;
import ir.caspiansoftware.caspianandroidapp.Report.pfaktor.PFaktorReport;

/**
 * Created by Canada on 7/14/2016.
 */
public class MaliFragment extends CaspianFragment implements IFragmentCallback {
    private static final String TAG = "MaliFragment";

    public static final String EXTRA_ACTION_NEW = "extra_action_new";

    private IActivityCallback mActivityCallback;


    // region toolbar
    private ImageView mToolbarHome;
    private ImageView mToolbarSave;
    private ImageView mToolbarDelete;
    private ImageView mToolbarNew;
    private ImageView mToolbarLast;
    private ImageView mToolbarFirst;
    private ImageView mToolbarNext;
    private ImageView mToolbarPrevious;
    private ImageView mToolbarSearch;
    private ImageView mToolbarPrint;
    // endregion toolbar

    private ProgressBar mProgressBar;

    private EditText mEditTextNum;

    // bed
    private EditText mEditTextBedCode;
    private TextView mTextViewBedName;
    private ImageView mBtnBedSelect;

    // bes
    private EditText mEditTextBesCode;
    private TextView mTextViewBesName;
    private ImageView mBtnBesSelect;

    // date
    private ImageView mBtnDateSelect;
    private EditText mEditTextDate;


    private EditText mEditTextDescription;

    private TextView mTextViewSyncDate;
    private CheckBox mCheckBoxSynced;
    private ImageView mBtnLocationOnMap;




    private ImageView mBtnMande;

    private PersonModel mPerson = null;

    private MaliModel mMaliModel;
    private MaliBLL mMaliBLL;

    private TextView mLabelFaktorId;

    private boolean mModified = false;



    private EditText mEditTextBank;
    private EditText mEditTextSarresidDate;
    private ImageView mBtnSarresidDateSelect;
    private EditText mEditTextSerial;
    private ClearableEditText mPriceEditText;

    private RadioButton mSandoghRadio;
    private RadioButton mPayRadio;
    private RadioButton mVCheckRadio;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mali;
    }


    private boolean checkForSave(final boolean onExit) {
        if (mModified) {
            Log.d(TAG, "checkForSave(): need save");
            messageBoxYesNo(R.string.mali_save_title, R.string.ask_to_save_mali, new IDialogCallback() {
                @Override
                public void dialog_callback(DialogResult dialogResult, Object result, int requestCode) {
                    if (dialogResult == DialogResult.Yes) {
                        saveAsync();
                    } else {
                        setModified(false);
                        if (onExit)
                            getActivity().finish();
                    }
                }
            });
            return true;
        }
        Log.d(TAG, "checkForSave(): don't need save");
        return false;
    }

    public void setModified(boolean modified) {
        mModified = modified;
    }

    private void loadLast() {
        try {
            if (!checkForSave(false)) {
                MaliModel maliModel = mMaliBLL.getByMove(MoveDirection.Last, -1);
                setMaliModel(maliModel, false);
            }
        } catch (Exception ex) {
            showError(ex, null);
        }
    }

    private void loadFirst() {
        try {
            if (!checkForSave(false)) {
                MaliModel maliModel = mMaliBLL.getByMove(MoveDirection.First, -1);
                setMaliModel(maliModel, false);
            }
        } catch (Exception ex) {
            showError(ex, null);
        }
    }

    private void loadNext() {
        try {
            if (!checkForSave(false)) {
                if (mMaliModel != null && mMaliModel.getId() > 0) {
                    Log.d(TAG, "loadNext()");
                    MaliModel maliModel = mMaliBLL.getByMove(MoveDirection.Next, mMaliModel.getId());
                    if (maliModel != null)
                        setMaliModel(maliModel, false);
                }
            }
        } catch (Exception ex) {
            showError(ex, null);
        }
    }

    private void loadPrevious() {
        try {
            if (!checkForSave(false)) {
                if (mMaliModel != null && mMaliModel.getId() > 0) {
                    Log.d(TAG, "loadPrevious()");
                    MaliModel maliModel = mMaliBLL.getByMove(MoveDirection.Previous, mMaliModel.getId());
                    if (maliModel != null)
                        setMaliModel(maliModel, false);
                } else {
                    loadLast();
                }
            }
        } catch (Exception ex) {
            showError(ex, null);
        }
    }

    private void newMaliEntry() {
        if (!checkForSave(false)) {
            setMaliModel(null, false);
        }
    }

    public void setMaliModel(MaliModel maliModel, boolean justUpdate) {
        Log.d(TAG, "setMaliModel()");

        mModified = false;

//        mLinearLayoutTop.setEnabled(true);
//        mLinearLayoutDataGrid.setEnabled(true);
        mTextViewSyncDate.setText("");
        mCheckBoxSynced.setChecked(false);

        mMaliModel = maliModel;
        if (mMaliModel != null) {
            Log.d(TAG, "setMaliModel(): mMPFaktorModel not null");
            mLabelFaktorId.setText(String.format(
                    getContext().getString(R.string.faktor_id),
                    maliModel.getId())
            );

            if (!justUpdate) {
                mEditTextNum.setText(String.valueOf(mMaliModel.getNum()));
                mEditTextDate.setText(mMaliModel.getMaliDate());
                mEditTextDescription.setText(mMaliModel.getDescription());
                setPerson(mMaliModel.getPersonBedModel(), FormActionType.SELECT_BED);
                setPerson(mMaliModel.getPersonBedModel(), FormActionType.SELECT_BES);
                //mMPFaktorModel.setSynced(true);
            } else {
                mMaliModel.setId(maliModel.getId());
                mMaliModel.setPersonBedModel(maliModel.getPersonBedModel());
            }

            if (maliModel.isSynced()) {
                mTextViewSyncDate.setText(maliModel.getSyncDate());
                mCheckBoxSynced.setChecked(true);
//                mLinearLayoutTop.setEnabled(false);
//                mLinearLayoutDataGrid.setEnabled(false);
            }
        } else {
            mEditTextNum.setText(String.valueOf(mMaliBLL.getNewNum()));
            mEditTextBedCode.setText("");
            mTextViewBedName.setText("");

            mEditTextBesCode.setText("");
            mTextViewBesName.setText("");

            mEditTextDate.setText(PersianDate.getToday());
            mEditTextDescription.setText("");
            mLabelFaktorId.setText("");
            mSandoghRadio.setChecked(true);
        }
    }


    @Override
    protected void mapViews(View parentView) {
        View toolbar = parentView.findViewById(R.id.toolbar);
        CaspianToolbar.setToolbar(this, toolbar);

        mapToolbar(parentView);

        mEditTextNum = (EditText) parentView.findViewById(R.id.editText_num);

        mEditTextBedCode = (EditText) parentView.findViewById(R.id.editText_bedCode);
        mEditTextBedCode.requestFocus();
        mTextViewBedName = (TextView) parentView.findViewById(R.id.textView_bedName);
        mBtnBedSelect = (ImageView) parentView.findViewById(R.id.btn_bedSelect);
        UIUtility.setButtonEffect(mBtnBedSelect, this);

        mEditTextBesCode = (EditText) parentView.findViewById(R.id.editText_besCode);
        mEditTextBesCode.requestFocus();
        mTextViewBesName = (TextView) parentView.findViewById(R.id.textView_besName);
        mBtnBesSelect = (ImageView) parentView.findViewById(R.id.btn_besSelect);
        UIUtility.setButtonEffect(mBtnBesSelect, this);

        mEditTextDate = (EditText) parentView.findViewById(R.id.editText_maliDate);
        mEditTextDate.setOnTouchListener(this);
        mEditTextDate.setText(PersianDate.getToday());
        mBtnDateSelect = (ImageView) parentView.findViewById(R.id.btn_date_select);
        UIUtility.setButtonEffect(mBtnDateSelect, this);

        mEditTextDescription = (EditText) parentView.findViewById(R.id.editText_description);

        // VCheck start
        mEditTextBank = (EditText) parentView.findViewById(R.id.editText_vcheckBank);

        mEditTextSarresidDate = (EditText) parentView.findViewById(R.id.editText_vcheckDate);
        mEditTextSarresidDate.setOnTouchListener(this);
        mEditTextSarresidDate.setText(PersianDate.getToday());
        mBtnSarresidDateSelect = (ImageView) parentView.findViewById(R.id.btn_vcheckDate);
        UIUtility.setButtonEffect(mBtnSarresidDateSelect, this);

        mEditTextSerial = (EditText) parentView.findViewById(R.id.editText_vcheckSerial);
        // VCheck end

        mPriceEditText = (ClearableEditText) parentView.findViewById(R.id.editText_amountPrice);

        mTextViewSyncDate = (TextView) parentView.findViewById(R.id.label_synced_date);
        mCheckBoxSynced = (CheckBox) parentView.findViewById(R.id.checkbox_synced);

        mBtnLocationOnMap = (ImageView) parentView.findViewById(R.id.btn_mapLocation);
        UIUtility.setButtonEffect(mBtnLocationOnMap, this);
        if (!PermissionBLL.seeLocation()) {
            mBtnLocationOnMap.setVisibility(View.INVISIBLE);
            TextView label = (TextView) parentView.findViewById(R.id.label_location);
            label.setVisibility(View.INVISIBLE);
        }

        mBtnMande = (ImageView) parentView.findViewById(R.id.btn_mande);
        UIUtility.setButtonEffect(mBtnMande, this);
        if (!PermissionBLL.mandeAccess()) {
            mBtnMande.setVisibility(View.INVISIBLE);
            TextView label = (TextView) parentView.findViewById(R.id.label_mande);
            label.setVisibility(View.INVISIBLE);
        }

        mProgressBar = (ProgressBar) parentView.findViewById(R.id.progressBar);

        mLabelFaktorId = parentView.findViewById(R.id.labelFaktorId);

        mSandoghRadio = parentView.findViewById(R.id.radio_sandoogh);
        mPayRadio = parentView.findViewById(R.id.radio_pay);
        mVCheckRadio = parentView.findViewById(R.id.radio_vcheck);

        GPSTracker.requestForGps(getActivity());

        newMaliEntry();
    }

    private void mapToolbar(View parentView) {
        mToolbarHome = parentView.findViewById(R.id.home_toolbar);
        mToolbarHome.setOnClickListener(this);

        mToolbarSave = parentView.findViewById(R.id.save_object);
        mToolbarSave.setOnClickListener(this);

        mToolbarDelete = parentView.findViewById(R.id.delete_object);
        mToolbarDelete.setOnClickListener(this);

        mToolbarNew = parentView.findViewById(R.id.new_object);
        mToolbarNew.setOnClickListener(this);

        mToolbarLast = parentView.findViewById(R.id.last_object);
        mToolbarLast.setOnClickListener(this);

        mToolbarFirst = parentView.findViewById(R.id.first_object);
        mToolbarFirst.setOnClickListener(this);

        mToolbarNext = parentView.findViewById(R.id.next_object);
        mToolbarNext.setOnClickListener(this);

        mToolbarPrevious = parentView.findViewById(R.id.previous_object);
        mToolbarPrevious.setOnClickListener(this);

        mToolbarSearch = parentView.findViewById(R.id.search_object);
        mToolbarSearch.setOnClickListener(this);

        mToolbarPrint = parentView.findViewById(R.id.print_object);
        mToolbarPrint.setOnClickListener(this);
    }

    @Override
    protected void afterOnCreate() {
        mMaliBLL = new MaliBLL(getContext());
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

    private boolean checkForSync() {
        if (mMaliModel != null && mMaliModel.isSynced()) {
            messageBoxOK(R.string.preInvoice_title, R.string.pfaktor_readonly_for_sync, null);
            return true;
        }
        return false;
    }


    private void search() {
        if (checkForSave(false))
            return;

        mActivityCallback.onMyFragmentCallBack(MaliActivity.ACTION_INVOICE_SEARCH, null, (Object) null);
    }

    private void delete() {
        try {
            if (mMaliBLL.delete(mMaliModel) > 0) {
                messageBoxOK(R.string.pfaktor_delete_title, R.string.success_delete, new IDialogCallback() {
                    @Override
                    public void dialog_callback(DialogResult dialogResult, Object result, int requestCode) {
                        loadLast();
                    }
                });
            }
        } catch (Exception ex) {
            showError(ex, null);
        }
    }

    private void checkForDelete() {
        if (mMaliModel == null || mMaliModel.getId() <= 0)
            return;

        if (mMaliModel.isSynced()) {
            messageBoxOK(R.string.pfaktor_delete_title, R.string.pfaktor_readonly_for_sync_question, null);
            return;
        }

        messageBoxYesNo(R.string.pfaktor_delete_title, R.string.pfaktor_delete_confirm, new IDialogCallback() {
            @Override
            public void dialog_callback(DialogResult dialogResult, Object result, int requestCode) {
                if (dialogResult == DialogResult.Yes) {
                    delete();
                }
            }
        });
    }

    private void saveAsync() {
        class AsyncRequest extends AsyncTask<Void, Void, Date> {

            @Override
            protected Date doInBackground(Void... params) {
                Date dateTime = null;
                TimeWebService timeWebService = new TimeWebService();
                try {
                    dateTime = timeWebService.getCurrentDateTime();
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
                return dateTime;
            }


            @Override
            protected void onPostExecute(Date dateTime) {
                if (dateTime == null) {
                    showError(R.string.internet_dateTime_not_reachable, null);
                    return;
                }
                save(dateTime);
            }
        }

        AsyncRequest asyncRequest = new AsyncRequest();
        asyncRequest.execute();
    }

    private MaliType getMaliType() {
        if (mVCheckRadio.isChecked())
            return MaliType.VCHECK;

        if (mPayRadio.isChecked())
            return MaliType.PAY;

        return MaliType.SANDOGH;
    }

    private void save(Date dateTime) {
        if (checkForSync())
            return;

        try {
            MaliModel maliModel = mMaliBLL.Save(
                    mMaliModel != null ? mMaliModel.getId() : -1,
                    Integer.parseInt(mEditTextNum.getText().toString()),
                    getMaliType(),
                    mEditTextBedCode.getText().toString(),
                    mEditTextBesCode.getText().toString(),
                    mEditTextDate.getText().toString(),
                    mEditTextDescription.getText().toString(),
                    // TODO Add missing attributes 3 related vcheck and amount
                    getActivity(),
                    dateTime
            );

            setMaliModel(maliModel, true);
            mModified = false;
            messageBoxOK(R.string.pfaktor_save_title, R.string.success_save, null);

        } catch (Exception ex) {
            showError(ex, null);
        }
    }

    private void selectDate(EditText editText) {
        if (checkForSync())
            return;

        DatePickerDialog.SelectDate(editText, getActivity().getFragmentManager(), (dialogResult, result, requestCode) -> {
            if (dialogResult == DialogResult.OK) {
                mModified = true;
            }
        });
    }

    private void getMande(PersonModel person) {
        if (person != null) {
            try {
                MandePLL mandePLL = new MandePLL(getContext(), this, this);
                mandePLL.start(person);
            } catch (Exception ex) {
                showError(ex, null);
            }
        }
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick(): function entered");

        if (view.equals(mBtnDateSelect)) { // mBtnDateSelect
            Log.d(TAG, "mBtnDateSelect");
            selectDate(mEditTextDate);

        } else if (view.equals(mBtnSarresidDateSelect)) { // mBtnSarresidDateSelect
            Log.d(TAG, "mBtnSarresidDateSelect");
            selectDate(mEditTextSarresidDate);

        } else if (view.equals(mBtnBedSelect)) { // mBtnCustomerSelect
            Log.d(TAG, "mBtnBedSelect");
            if (checkForSync())
                return;
            mActivityCallback.onMyFragmentCallBack(MaliActivity.ACTION_SELECT_PERSON_LIST, FormActionType.SELECT_BED, (Object) null);

        } else if (view.equals(mBtnBesSelect)) { // mBtnCustomerSelect
            Log.d(TAG, "mBtnBesSelect");
            if (checkForSync())
                return;
            mActivityCallback.onMyFragmentCallBack(MaliActivity.ACTION_SELECT_PERSON_LIST, FormActionType.SELECT_BES, (Object) null);


        } else if (view.equals(mBtnLocationOnMap)) { // mBtnLocationOnMap
            LocationManager locationManager = (LocationManager) getContext()
                    .getSystemService(Context.LOCATION_SERVICE);

            try {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                Log.d(TAG, "onLocationChanged(): lat: " + location.getLatitude() + ", lon: " + location.getLongitude());
                            }

                            @Override
                            public void onStatusChanged(String s, int i, Bundle bundle) {
                                Log.d(TAG, "onStatusChanged()");
                            }

                            @Override
                            public void onProviderEnabled(String s) {
                                Log.d(TAG, "onProviderEnabled()");
                            }

                            @Override
                            public void onProviderDisabled(String s) {
                                Log.d(TAG, "onProviderDisabled()");
                            }
                        });
                Log.d("GPS Enabled", "GPS Enabled");
                if (locationManager != null) {
                    Location location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        Log.d(TAG, "222 lat: " + location.getLatitude() + ", lon: " + location.getLongitude());
                    }
                }
            } catch (SecurityException ex) {

            } catch (Exception ex) {
                Log.d(TAG, ex.getMessage());
                showError(ex, null);
            }


            // it's saved
            if (mMaliModel != null && mMaliModel.getId() > 0 && mMaliModel.getLat() > 0 && mMaliModel.getLon() > 0)
                // it was a correct location
                MapUtility.ShowLocationOnGoogleMap(mMaliModel.getLat(), mMaliModel.getLon(), getActivity());
            else if (mMaliModel != null)
                messageBoxOK(R.string.pfaktor_save_location_title, R.string.pfaktor_save_location_is_not_available, null);


        } else if (view.equals(mBtnMande) && mBtnMande.getVisibility() == View.VISIBLE) { // mBtnMande
            // get mande info from server
            getMande(mPerson);

        } else if (view.equals(mToolbarHome)) { // mToolbarHome
            if (checkForSave(true))
                return;

            Log.d(TAG, "mToolbarHome clicked");
            getActivity().finish();

        } else if (view.equals(mToolbarSave)) {
            Log.d(TAG, "mToolbarSave clicked");
            saveAsync();

        } else if (view.equals(mToolbarDelete)) {
            Log.d(TAG, "mToolbarDelete clicked");
            checkForDelete();

        } else if (view.equals(mToolbarNew)) {
            Log.d(TAG, "mToolbarNew clicked");
            newMaliEntry();

        } else if (view.equals(mToolbarLast)) {
            Log.d(TAG, "mToolbarLast clicked");
            loadLast();

        } else if (view.equals(mToolbarFirst)) {
            Log.d(TAG, "mToolbarFirst clicked");
            loadFirst();

        } else if (view.equals(mToolbarNext)) {
            Log.d(TAG, "mToolbarNext clicked");
            loadNext();

        } else if (view.equals(mToolbarPrevious)) {
            Log.d(TAG, "mToolbarPrevious clicked");
            loadPrevious();

        } else if (view.equals(mToolbarSearch)) {
            Log.d(TAG, "mToolbarSearch clicked");
            search();

        } else if (view.equals(mToolbarPrint)) {
            Log.d(TAG, "mToolbarPrint clicked");
            if (mMaliModel == null)
                return;

            PFaktorReport pFaktorReport = new PFaktorReport(getContext());
            String reportFile = pFaktorReport.generateReport(mMaliModel.getId());
            Intent intent = ReportActivity.newIntent(getContext(), reportFile);
            startActivity(intent);
            /*if (ReportHelper.alreadyGenerated(mMPFaktorModel.getNum(), getContext()))
                ReportHelper.shareOrHandlePDF(mMPFaktorModel.getNum(), getContext());
            else {
                ReportHelper.generatePDF(mMPFaktorModel.getNum(), getContext());
                ReportHelper.shareOrHandlePDF(mMPFaktorModel.getNum(), getContext());
            }*/

        }
    }



    @Override
    public void onMyActivityCallback(String actionName, Object parameter, FormActionType formActionType) {
       // getRowFragment().notifyDataSetChanged();

        switch (actionName) {
            case MaliActivity.ACTION_SELECT_PERSON_LIST:
                Log.d(TAG, "onMyActivityCallback(): PersonModel");

                if (!(parameter instanceof PersonModel)) {
                    showError(R.string.invalid_parameter, null);
                    return;
                }

                mModified = true;
                setPerson((PersonModel) parameter, formActionType);
                break;


            case MaliActivity.ACTION_INVOICE_KALA:
                Log.d(TAG, "onMyActivityCallback(): SPFaktorModel");
                if (!(parameter instanceof SPFaktorModel)) {
                    showError(R.string.invalid_parameter, null);
                    return;
                }

                mModified = true;
//                switch (formActionTypes) {
//                    case New:
//                        addKala((SPFaktorModel) parameter);
//                        break;
//
//                    case Edit:
//                        try {
//                            updateSPFaktorRow(mSPFaktorEditPosition, (SPFaktorModel) parameter);
//                        } finally {
//                            mSPFaktorEditPosition = -1;
//                        }
//                        break;
//                }
                break;

            case MaliActivity.ACTION_INVOICE_SEARCH:
                Log.d(TAG, "onMyActivityCallback(): Search");

                if (!(parameter instanceof MaliModel)) {
                    showError(R.string.invalid_parameter, null);
                    return;
                }
                MaliModel maliModel = (MaliModel) parameter;
                mModified = false;
                setMaliModel(maliModel, false);
                break;


            case Actions.ACTION_GET_MANDE:
                if (parameter != null && parameter instanceof Double) {
                    messageBoxOK(R.string.person_mande_title,
                            String.format(
                                    getString(R.string.person_mande_result),
                                    mPerson.getName(),
                                    NumberExt.DigitSeparator((double) parameter)),
                            true,
                            null);
                }
                break;
        }
    }

    @Override
    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    @Override
    public void startProgress() {
        super.startProgress();
    }

    @Override
    public void stopProgress() {
        super.stopProgress();
    }

    public void setPerson(PersonModel personModel, FormActionType formActionType) {
        if (personModel != null) {
            mPerson = personModel;
            switch (formActionType) {
                case SELECT_BED:
                    mEditTextBedCode.setText(personModel.getCode());
                    mTextViewBedName.setText(personModel.getName());
                    break;

                case SELECT_BES:
                    mEditTextBesCode.setText(personModel.getCode());
                    mTextViewBesName.setText(personModel.getName());
                    break;
            }
            UIUtility.HideKeyboard(getActivity());
        }
    }
}
