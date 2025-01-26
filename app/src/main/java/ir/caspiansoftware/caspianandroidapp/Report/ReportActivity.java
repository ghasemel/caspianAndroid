package ir.caspiansoftware.caspianandroidapp.Report;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.R;

public class ReportActivity extends CaspianActivitySingleFragment {
    private static final String TAG = "ReportActivity";
    public static final String EXTRA_FILE_NAME = "REPORT_FILE_NAME";

    private IFragmentCallback mFragmentCallback;

    public static Intent newIntent(Context context, String fileName) {
        Intent intent = new Intent(context, ReportActivity.class);
        intent.putExtra(EXTRA_FILE_NAME, fileName);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedBundleState) {
        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_report, R.string.report_view_title);
        super.onCreate(savedBundleState);
        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        return new ReportFragment();
    }
}
