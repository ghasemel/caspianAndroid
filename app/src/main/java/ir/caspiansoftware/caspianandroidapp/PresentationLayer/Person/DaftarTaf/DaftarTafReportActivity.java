package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.DaftarTaf;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;

import info.elyasi.android.elyasilib.Thread.ICallBack;
import info.elyasi.android.elyasilib.UI.UIUtility;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Year.YearListFragment;
import ir.caspiansoftware.caspianandroidapp.R;

import static java.security.AccessController.getContext;

/**
 * Created by Canada on 2/23/2016.
 */
public class DaftarTafReportActivity extends CaspianActivitySingleFragment implements ICallBack {
    private static final String TAG = "DaftarTafReportActivity";

    public static final String EXTRA_PERSON_NAME = "extra_person_name";
    public static final String EXTRA_PERSON_CODE = "extra_person_code";
    public static final String EXTRA_FROM_DATE = "extra_from_date";
    public static final String EXTRA_TILL_DATE = "extra_till_date";

    @Override
    public void onCreate(Bundle savedBundleState) {
        Log.d(TAG, "onCreate(): function entered");


        Intent intent = getIntent();
        if (!intent.hasExtra(DaftarTafReportActivity.EXTRA_PERSON_NAME)) {
            super.onCreate(savedBundleState);
            showError(getResources().getString(R.string.no_person_selected), this);
            return;
        }

        if (!intent.hasExtra(DaftarTafReportActivity.EXTRA_TILL_DATE)) {
            super.onCreate(savedBundleState);
            showError(getResources().getString(R.string.date_invalid), this);
            return;
        }

        String name = intent.getStringExtra(DaftarTafReportActivity.EXTRA_PERSON_NAME);
        String tillDate = intent.getStringExtra(DaftarTafReportActivity.EXTRA_TILL_DATE);


        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog,
                String.format(
                        getResources().getString(R.string.daf_taf_report_title),
                        name,
                        tillDate
                )
        );

        super.onCreate(savedBundleState);

        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        return new DaftarTafReportFragment();
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.fragmentContainer;
    }

    @Override
    public int getLayoutId() {
        return R.layout.single_fragment_activity;
    }



    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    //    getMenuInflater().inflate(R.menu.menu_main, menu);
    //    return true;
    //}

    @Override
    public void callback(String caller, int pResponseCode, String pResponseData) {
        this.finish();
    }

    @Override
    public void onError(String caller, int pErrorCode, String pErrorMessage) {

    }

    @Override
    public void onSuccess(String caller, int pResponseCode, String pResponseData) {

    }
}
