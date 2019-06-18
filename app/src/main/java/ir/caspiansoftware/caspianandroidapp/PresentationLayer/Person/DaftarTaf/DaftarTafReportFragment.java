package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.DaftarTaf;


import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import info.elyasi.android.elyasilib.UI.UIUtility;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianSearchableListFragment;
import ir.caspiansoftware.caspianandroidapp.Models.DaftarTafReportModel;
import ir.caspiansoftware.caspianandroidapp.R;


/**
 * Created by Canada on 3/3/2016.
 */
public class DaftarTafReportFragment extends CaspianSearchableListFragment<DaftarTafReportModel> {
    private static final String TAG = "DaftarTafReportFragment";

    private EditText mSearchTarikh;
    private EditText mSearchDescription;


    public ProgressBar getProgressBar() {
        return null;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_daf_taf_report_list;
    }

    protected void mapViews(View parentView) {
        mSearchTarikh = (EditText) parentView.findViewById(R.id.search_tarikh);
        mSearchDescription = (EditText) parentView.findViewById(R.id.search_description);

        Log.d(TAG, "mapViews(): function end");
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected EditText[] getSearchEditTexts() {
        return new EditText[] {
                mSearchTarikh,
                mSearchDescription
        };
    }

    @Override
    protected String[] getSearchedTextValueOnTextChange() {
        return new String[] {
                mSearchTarikh.getText().toString(),
                mSearchDescription.getText().toString()
        };
    }

    @Override
    protected String getFragmentRowTagValue() {
        return getResources().getString(R.string.fragment_daf_taf_row_tag);
    }
}
