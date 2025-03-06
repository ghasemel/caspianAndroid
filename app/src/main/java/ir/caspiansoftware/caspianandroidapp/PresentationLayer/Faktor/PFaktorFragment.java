package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import info.elyasi.android.elyasilib.GPS.MapUtility;
import info.elyasi.android.elyasilib.Persian.PersianDate;
import info.elyasi.android.elyasilib.Controls.datepicker.util.PersianCalendar;
import info.elyasi.android.elyasilib.Dialogs.DatePickerDialog;
import info.elyasi.android.elyasilib.Dialogs.DialogResult;
import info.elyasi.android.elyasilib.Dialogs.IDialogCallback;
import info.elyasi.android.elyasilib.UI.FormActionType;
import info.elyasi.android.elyasilib.UI.IActivityCallback;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import info.elyasi.android.elyasilib.UI.MoveDirection;
import info.elyasi.android.elyasilib.UI.UIUtility;
import info.elyasi.android.elyasilib.Utility.NumberExt;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianDataGridFragment;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianToolbar;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.PFaktorBLL;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.PermissionBLL;
import ir.caspiansoftware.caspianandroidapp.DataLayer.WebService.TimeWebService;
import ir.caspiansoftware.caspianandroidapp.GPSTracker;
import ir.caspiansoftware.caspianandroidapp.Models.MPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.Models.SPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL.MandePLL;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Report.ReportActivity;
import ir.caspiansoftware.caspianandroidapp.Report.pfaktor.PFaktorReport;

/**
 * Created by Canada on 7/14/2016.
 */
public class PFaktorFragment extends CaspianDataGridFragment<SPFaktorModel> implements IFragmentCallback {
    private static final String TAG = "PFaktorFragment";

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

    private LinearLayout mLinearLayoutTop;
    private LinearLayout mLinearLayoutDataGrid;
    private EditText mEditTextNum;
    private EditText mEditTextInvoiceDate;
    private EditText mEditTextCustomerCode;
    private TextView mTextViewCustomerName;
    private EditText mEditTextDescription;
    private TextView mSummeryTotalPrice;

    private TextView mTextViewSyncDate;
    private CheckBox mCheckBoxSynced;
    private ImageView mBtnLocationOnMap;

    private ImageView mBtnCustomerSelect;
    private ImageView mBtnDateSelect;
    private LinearLayout mBtnAddKala;

    private ImageView mBtnMande;

    private PersonModel mPerson = null;
    private ArrayList<SPFaktorModel> mSPFaktorList;
    private int mSPFaktorEditPosition = -1;

    private MPFaktorModel mMPFaktorModel;
    private PFaktorBLL mPFaktorBLL;

    private TextView mLabelFaktorId;

    private boolean mModified = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pfaktor;
    }


    @Override
    public void dataSetChanged(int rowCount) {
        Log.d(TAG, "dataSetChanged(): rowCount = " + rowCount);

        mSummeryTotalPrice.setText("0");
        if (mSPFaktorList != null) {
            BigDecimal sum = new BigDecimal(0);
            for (SPFaktorModel spFaktorModel : mSPFaktorList) {
                sum = sum.add(spFaktorModel.getTotalPrice());
            }
            mSummeryTotalPrice.setText(NumberExt.DigitSeparator(sum));
        }
    }

    private boolean checkForSave(final boolean onExit) {
        if (mModified || (mMPFaktorModel == null && mSPFaktorList != null && mSPFaktorList.size() > 0)) {
            Log.d(TAG, "checkForSave(): need save");
            messageBoxYesNo(R.string.pfaktor_save_title, R.string.ask_to_save, new IDialogCallback() {
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
                MPFaktorModel mpFaktorModel = mPFaktorBLL.getByMove(MoveDirection.Last, -1);
                setMPFaktorModel(mpFaktorModel, false);
            }
        } catch (Exception ex) {
            showError(ex, null);
        }
    }

    private void loadFirst() {
        try {
            if (!checkForSave(false)) {
                MPFaktorModel mpFaktorModel = mPFaktorBLL.getByMove(MoveDirection.First, -1);
                setMPFaktorModel(mpFaktorModel, false);
            }
        } catch (Exception ex) {
            showError(ex, null);
        }
    }

    private void loadNext() {
        try {
            if (!checkForSave(false)) {
                if (mMPFaktorModel != null && mMPFaktorModel.getId() > 0) {
                    Log.d(TAG, "loadNext()");
                    MPFaktorModel mpFaktorModel = mPFaktorBLL.getByMove(MoveDirection.Next, mMPFaktorModel.getId());
                    if (mpFaktorModel != null)
                        setMPFaktorModel(mpFaktorModel, false);
                }
            }
        } catch (Exception ex) {
            showError(ex, null);
        }
    }

    private void loadPrevious() {
        try {
            if (!checkForSave(false)) {
                if (mMPFaktorModel != null && mMPFaktorModel.getId() > 0) {
                    Log.d(TAG, "loadPrevious()");
                    MPFaktorModel mpFaktorModel = mPFaktorBLL.getByMove(MoveDirection.Previous, mMPFaktorModel.getId());
                    if (mpFaktorModel != null)
                        setMPFaktorModel(mpFaktorModel, false);
                } else {
                    loadLast();
                }
            }
        } catch (Exception ex) {
            showError(ex, null);
        }
    }

    private void newFaktor() {
        if (!checkForSave(false)) {
            setMPFaktorModel(null, false);
        }
    }

    public void setMPFaktorModel(MPFaktorModel mpFaktorModel, boolean justUpdate) {
        Log.d(TAG, "setMPFaktorModel()");

        mModified = false;

//        mLinearLayoutTop.setEnabled(true);
//        mLinearLayoutDataGrid.setEnabled(true);
        mTextViewSyncDate.setText("");
        mCheckBoxSynced.setChecked(false);
        mBtnAddKala.setEnabled(true);

        mMPFaktorModel = mpFaktorModel;
        if (mMPFaktorModel != null) {
            Log.d(TAG, "setMPFaktorModel(): mMPFaktorModel not null");
            mLabelFaktorId.setText(String.format(
                    getContext().getString(R.string.faktor_id),
                    mpFaktorModel.getId())
            );

            if (!justUpdate) {
                mEditTextNum.setText(String.valueOf(mMPFaktorModel.getNum()));
                mEditTextInvoiceDate.setText(mMPFaktorModel.getDate());
                mEditTextDescription.setText(mMPFaktorModel.getDescription());
                setPerson(mMPFaktorModel.getPersonModel());
                setSPFaktorList(mMPFaktorModel.getSPFaktorList());
                //mMPFaktorModel.setSynced(true);
            } else {
                mMPFaktorModel.setId(mpFaktorModel.getId());
                mMPFaktorModel.setPersonModel(mpFaktorModel.getPersonModel());
                if (mpFaktorModel.getSPFaktorList() != null) {
                    for (int i = 0; i < mpFaktorModel.getSPFaktorList().size(); i++) {
                        mSPFaktorList.get(i).setId(mpFaktorModel.getSPFaktorList().get(i).getId());
                        mSPFaktorList.get(i).setKalaModel(mpFaktorModel.getSPFaktorList().get(i).getKalaModel());
                    }
                }
            }

            if (mpFaktorModel.isSynced()) {
                mTextViewSyncDate.setText(mpFaktorModel.getSyncDate());
                mCheckBoxSynced.setChecked(true);
//                mLinearLayoutTop.setEnabled(false);
//                mLinearLayoutDataGrid.setEnabled(false);
                mBtnAddKala.setEnabled(false);
            }
        } else {
            mEditTextNum.setText(String.valueOf(mPFaktorBLL.getNewNum()));
            mEditTextCustomerCode.setText("");
            mEditTextInvoiceDate.setText(PersianDate.getToday());
            mTextViewCustomerName.setText("");
            mEditTextDescription.setText("");
            mLabelFaktorId.setText("");
            setSPFaktorList(new ArrayList<SPFaktorModel>());
        }
    }

    public void setSPFaktorList(ArrayList<SPFaktorModel> SPFaktorList) {
        if (SPFaktorList == null)
            SPFaktorList = new ArrayList<>();

        mSPFaktorList = SPFaktorList;
        getRowFragment().setDataList(mSPFaktorList);
    }

    @Override
    protected String getFragmentRowTagValue() {
        return getResources().getString(R.string.pre_invoice_data_grid_fragment_dag);
    }

    @Override
    protected void afterFragmentRowAttached() {
        //setMPFaktorModel(null);
        mPFaktorBLL = new PFaktorBLL(getContext());


        if (getActivity().getIntent().hasExtra(EXTRA_ACTION_NEW)) {
            newFaktor();
        } else {
            loadLast();
        }
    }


    @Override
    protected void mapViews(View parentView) {
        View toolbar = parentView.findViewById(R.id.toolbar);
        CaspianToolbar.setToolbar(this, toolbar);

        View dataGrid_toolbar = parentView.findViewById(R.id.dataGrid_summery);
        CaspianToolbar.setToolbar(this, dataGrid_toolbar);

        mapToolbar(parentView);

//        mLinearLayoutTop = (LinearLayout) parentView.findViewById(R.id.linearLayout_faktorTop);
//        mLinearLayoutDataGrid = (LinearLayout) parentView.findViewById(R.id.dataGrid_frame);

        mBtnCustomerSelect = (ImageView) parentView.findViewById(R.id.btn_customer_select);
        UIUtility.setButtonEffect(mBtnCustomerSelect, this);

        mEditTextNum = (EditText) parentView.findViewById(R.id.editText_invoiceNum);
        //mEditTextNum.setTypeface(Sett);

        mEditTextInvoiceDate = (EditText) parentView.findViewById(R.id.editText_invoiceDate);
        mEditTextInvoiceDate.setOnTouchListener(this);
        mEditTextInvoiceDate.setText(PersianDate.getToday());

        mEditTextCustomerCode = (EditText) parentView.findViewById(R.id.editText_invoiceCustomerCode);
        mEditTextCustomerCode.requestFocus();

        mTextViewCustomerName = (TextView) parentView.findViewById(R.id.editText_CustomerName);
        mEditTextDescription = (EditText) parentView.findViewById(R.id.editText_invoiceDescription);

        mTextViewSyncDate = (TextView) parentView.findViewById(R.id.label_synced_date);
        mCheckBoxSynced = (CheckBox) parentView.findViewById(R.id.checkbox_synced);

        mBtnLocationOnMap = (ImageView) parentView.findViewById(R.id.btn_mapLocation);
        UIUtility.setButtonEffect(mBtnLocationOnMap, this);
        if (!PermissionBLL.seeLocation()) {
            mBtnLocationOnMap.setVisibility(View.INVISIBLE);
            TextView label = (TextView) parentView.findViewById(R.id.label_location);
            label.setVisibility(View.INVISIBLE);
        }

        mBtnDateSelect = (ImageView) parentView.findViewById(R.id.btn_date_select);
        UIUtility.setButtonEffect(mBtnDateSelect, this);

        mBtnMande = (ImageView) parentView.findViewById(R.id.btn_mande);
        UIUtility.setButtonEffect(mBtnMande, this);
        if (!PermissionBLL.mandeAccess()) {
            mBtnMande.setVisibility(View.INVISIBLE);
            TextView label = (TextView) parentView.findViewById(R.id.label_mande);
            label.setVisibility(View.INVISIBLE);
        }

        mProgressBar = (ProgressBar) parentView.findViewById(R.id.progressBar);

        mSummeryTotalPrice = (TextView) parentView.findViewById(R.id.summery_total_price);

        mLabelFaktorId = parentView.findViewById(R.id.labelFaktorId);

        GPSTracker.requestForGps(getActivity());
    }

    private void mapToolbar(View parentView) {
        mToolbarHome = (ImageView) parentView.findViewById(R.id.home_toolbar);
        mToolbarHome.setOnClickListener(this);

        //mToolbarNewInvoice = (RelativeLayout) parentView.findViewById(R.id.toolbar_new_invoice);

        mBtnAddKala = parentView.findViewById(R.id.btn_add_kala);
        mBtnAddKala.setOnClickListener(this);

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
    public void OnItemClick(SPFaktorModel spFaktorModel, int position) {
        Log.d(TAG, "OnItemClick(): function entered");
        if (checkForSync())
            return;

        mSPFaktorEditPosition = position;
        mActivityCallback.onMyFragmentCallBack(PFaktorActivity.ACTION_INVOICE_KALA, FormActionType.Edit, spFaktorModel, mPerson);
    }

    private boolean checkForSync() {
        if (mMPFaktorModel != null && mMPFaktorModel.isSynced()) {
            messageBoxOK(R.string.preInvoice_title, R.string.pfaktor_readonly_for_sync, null);
            return true;
        }
        return false;
    }

    @Override
    public void OnCellClick(SPFaktorModel spFaktorModel, final int row, int cellId, View cellView) {
        if (cellId == R.id.cell_delete) {

            if (checkForSync())
                return;

            Log.d(TAG, "cell_delete clicked: row " + row);
            messageBoxYesNo(R.string.delete_invoice_kala_title, R.string.delete_invoice_kala, new IDialogCallback() {
                @Override
                public void dialog_callback(DialogResult dialogResult, Object result, int requestCode) {
                    if (dialogResult == DialogResult.Yes) {
                        mSPFaktorList.remove(row);
                        getRowFragment().notifyDataSetChanged();
                    }
                }
            });

        }
    }


    private void search() {
        if (checkForSave(false))
            return;

        mActivityCallback.onMyFragmentCallBack(PFaktorActivity.ACTION_INVOICE_SEARCH, null, (Object) null);
    }

    private void delete() {
        try {
            if (mPFaktorBLL.delete(mMPFaktorModel) > 0) {
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
        if (mMPFaktorModel == null || mMPFaktorModel.getId() <= 0)
            return;

        if (mMPFaktorModel.isSynced()) {
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

    private void save(Date dateTime) {
        if (checkForSync())
            return;

        try {
            MPFaktorModel mpFaktorModel = mPFaktorBLL.Save(
                    mMPFaktorModel != null ? mMPFaktorModel.getId() : -1,
                    Integer.parseInt(mEditTextNum.getText().toString()),
                    mEditTextInvoiceDate.getText().toString(),
                    mEditTextCustomerCode.getText().toString(),
                    mEditTextDescription.getText().toString(),
                    mSPFaktorList,
                    getActivity(),
                    dateTime
            );

            setMPFaktorModel(mpFaktorModel, true);
            mModified = false;
            messageBoxOK(R.string.pfaktor_save_title, R.string.success_save, null);

        } catch (Exception ex) {
            showError(ex, null);
        }
    }

    private void selectDate() {
        if (checkForSync())
            return;

        DatePickerDialog.SelectDate(mEditTextInvoiceDate, getActivity().getFragmentManager(), new IDialogCallback<PersianCalendar>() {
            @Override
            public void dialog_callback(DialogResult dialogResult, PersianCalendar result, int requestCode) {
                if (dialogResult == DialogResult.OK) {
                    mModified = true;
                }
            }
        });

        /*DatePickerDialog dialog = new DatePickerDialog();
        dialog.setDialogCallback(new IDialogCallback<PersianCalendar>() {
            @Override
            public void dialog_callback(DialogResult dialogResult, PersianCalendar result, int requestCode) {

            }
        });
        dialog.setCurrentDate(mEditTextInvoiceDate.getText().toString());
        dialog.show(getActivity().getFragmentManager(), "DATE");*/
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
            selectDate();


        } else if (view.equals(mBtnCustomerSelect)) { // mBtnCustomerSelect
            Log.d(TAG, "mBtnCustomerSelect");
            if (checkForSync())
                return;
            mActivityCallback.onMyFragmentCallBack(PFaktorActivity.ACTION_SELECT_PERSON_LIST, null, (Object) null);


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
            if (mMPFaktorModel != null && mMPFaktorModel.getId() > 0 && mMPFaktorModel.getLat() > 0 && mMPFaktorModel.getLon() > 0)
                // it was a correct location
                MapUtility.ShowLocationOnGoogleMap(mMPFaktorModel.getLat(), mMPFaktorModel.getLon(), getActivity());
            else if (mMPFaktorModel != null)
                messageBoxOK(R.string.pfaktor_save_location_title, R.string.pfaktor_save_location_is_not_available, null);


        } else if (view.equals(mBtnMande) && mBtnMande.getVisibility() == View.VISIBLE) { // mBtnMande
            // get mande info from server
            getMande(mPerson);

        } else if (view.equals(mToolbarHome)) { // mToolbarHome
            if (checkForSave(true))
                return;

            Log.d(TAG, "mToolbarHome clicked");
            getActivity().finish();

        } else if (view.equals(mBtnAddKala)) {
            Log.d(TAG, "mBtnAddKala clicked");

            if (checkForSync())
                return;

            mActivityCallback.onMyFragmentCallBack(PFaktorActivity.ACTION_INVOICE_KALA, FormActionType.New, mPerson);

            // getRowFragment().notifyDataSetChanged();
        } else if (view.equals(mToolbarSave)) {
            Log.d(TAG, "mToolbarSave clicked");
            saveAsync();

        } else if (view.equals(mToolbarDelete)) {
            Log.d(TAG, "mToolbarDelete clicked");
            checkForDelete();

        } else if (view.equals(mToolbarNew)) {
            Log.d(TAG, "mToolbarNew clicked");
            newFaktor();

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
            if (mMPFaktorModel == null)
                return;

            PFaktorReport pFaktorReport = new PFaktorReport(getContext());
            String reportFile = pFaktorReport.generateReport(mMPFaktorModel.getId());
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
            case PFaktorActivity.ACTION_SELECT_PERSON_LIST:
                Log.d(TAG, "onMyActivityCallback(): PersonModel");

                if (!(parameter instanceof PersonModel)) {
                    showError(R.string.invalid_parameter, null);
                    return;
                }

                mModified = true;
                setPerson((PersonModel) parameter);
                break;


            case PFaktorActivity.ACTION_INVOICE_KALA:
                Log.d(TAG, "onMyActivityCallback(): SPFaktorModel");
                if (!(parameter instanceof SPFaktorModel)) {
                    showError(R.string.invalid_parameter, null);
                    return;
                }

                mModified = true;
                switch (formActionType) {
                    case New:
                        addKala((SPFaktorModel) parameter);
                        break;

                    case Edit:
                        try {
                            updateSPFaktorRow(mSPFaktorEditPosition, (SPFaktorModel) parameter);
                        } finally {
                            mSPFaktorEditPosition = -1;
                        }
                        break;
                }
                break;

            case PFaktorActivity.ACTION_INVOICE_SEARCH:
                Log.d(TAG, "onMyActivityCallback(): Search");

                if (!(parameter instanceof MPFaktorModel)) {
                    showError(R.string.invalid_parameter, null);
                    return;
                }
                MPFaktorModel mpFaktorModel = (MPFaktorModel) parameter;
                mpFaktorModel.setSPFaktorList(
                        mPFaktorBLL.getSPfaktorListByMPFaktorId(mpFaktorModel.getId())
                );
                mModified = false;
                setMPFaktorModel(mpFaktorModel, false);
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

    private void updateSPFaktorRow(int position, SPFaktorModel spFaktorModel) {
        if (position > -1) {
            mSPFaktorList.get(position).setMCount(spFaktorModel.getMCount());
            mSPFaktorList.get(position).setSCount(spFaktorModel.getSCount());
            mSPFaktorList.get(position).setPrice(spFaktorModel.getPrice());
            mSPFaktorList.get(position).setKalaModel(spFaktorModel.getKalaModel());
            getRowFragment().notifyDataSetChanged();
        }
    }

    private void addKala(SPFaktorModel spFaktorModel) {
        if (spFaktorModel != null) {
            mSPFaktorList.add(spFaktorModel);
            getRowFragment().notifyDataSetChanged();
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

    public void setPerson(PersonModel personModel) {
        if (personModel != null) {
            mPerson = personModel;
            mEditTextCustomerCode.setText(personModel.getCode());
            mTextViewCustomerName.setText(personModel.getName());
            UIUtility.HideKeyboard(getActivity());
        }
    }
}
