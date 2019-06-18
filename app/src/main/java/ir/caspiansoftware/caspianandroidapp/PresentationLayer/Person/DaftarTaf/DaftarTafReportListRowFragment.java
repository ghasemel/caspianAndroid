package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Person.DaftarTaf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.Persian.PersianConvert;
import info.elyasi.android.elyasilib.UI.AListRowFragment;
import info.elyasi.android.elyasilib.Utility.NumberExt;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.PersonBLL;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.YearMaliBLL;
import ir.caspiansoftware.caspianandroidapp.Models.DaftarTafReportModel;
import ir.caspiansoftware.caspianandroidapp.Models.YearMaliModel;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 2/23/2016.
 */
public class DaftarTafReportListRowFragment extends AListRowFragment<DaftarTafReportModel> { //ArrayList<YearMaliModel>
    private static final String TAG = "DaftarTafReportRow";

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setCloseAfterSelection(false);
    }

    @Override
    protected void onListItemClicked(DaftarTafReportModel yearMali, String resultExtraName) throws Exception {
        /*yearMali.setUserId(Vars.USER.getUserId());
        yearMali.setCurrent(true);

        YearMaliBLL yearMaliBLL = new YearMaliBLL(getActivity().getApplicationContext());
        long result = yearMaliBLL.saveYearMali(yearMali);
        Log.d(TAG, "save result: " + result);

        if (result > 0) {
            Vars.YEAR = yearMali;
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else {
            Vars.YEAR = null;
        }*/
    }


    @Override
    protected ArrayList<DaftarTafReportModel> inBackground(Object... params) throws Exception {
        if (getActivity() != null && getActivity().getApplicationContext() != null && Vars.USER != null) {

            Intent intent = getActivity().getIntent();
            if (!intent.hasExtra(DaftarTafReportActivity.EXTRA_PERSON_CODE)) {
                showError(getContext().getResources().getString(R.string.no_person_selected));
                return null;
            }

            if (!intent.hasExtra(DaftarTafReportActivity.EXTRA_FROM_DATE)) {
                showError(getContext().getResources().getString(R.string.date_invalid));
                return null;
            }

            if (!intent.hasExtra(DaftarTafReportActivity.EXTRA_TILL_DATE)) {
                showError(getContext().getResources().getString(R.string.date_invalid));
                return null;
            }
            String code = intent.getStringExtra(DaftarTafReportActivity.EXTRA_PERSON_CODE);
            String fromDate = intent.getStringExtra(DaftarTafReportActivity.EXTRA_FROM_DATE);
            String tillDate = intent.getStringExtra(DaftarTafReportActivity.EXTRA_TILL_DATE);

            PersonBLL personBLL = new PersonBLL(getActivity().getApplicationContext());
            return personBLL.FetchPersonDaftarTaf(code, fromDate, tillDate);
        }
        return null;
    }

    @Override
    protected DaftarTafAdapter postExecute(ArrayList<DaftarTafReportModel> daftarTafReportList) throws Exception {
        if (daftarTafReportList != null) {
            return new DaftarTafAdapter(daftarTafReportList);
            //throw new Exception("empty year mali");
        }
        return null;
    }

    private class DaftarTafAdapter extends MyAdapter {

        //private long mSumMande = 0;

        public DaftarTafAdapter(ArrayList<DaftarTafReportModel> list) {
            super(getActivity(), list);
        }

        @Override
        public int getLayout() {
            return R.layout.fragment_daf_taf_report_list_row;
        }

        @Override
        protected List<ImageView> doForEachItem(View convertView, DaftarTafReportModel daftarTaf, int position, boolean isNull) {
            TextView radif = (TextView) convertView.findViewById(R.id.cell_radif);
            radif.setText(String.valueOf(position + 1));

            TextView tarikh = (TextView) convertView.findViewById(R.id.cell_tarikh);
            tarikh.setText(daftarTaf.getTarikh());

            /*TextView code = (TextView) convertView.findViewById(R.id.cell_code);
            code.setText(daftarTaf.getCode());*/

            TextView des = (TextView) convertView.findViewById(R.id.cell_des);
            des.setText(daftarTaf.getDescription());

            TextView bed = (TextView) convertView.findViewById(R.id.cell_bed);
            bed.setText(NumberExt.DigitSeparator(daftarTaf.getBed()));

            TextView bes = (TextView) convertView.findViewById(R.id.cell_bes);
            bes.setText(NumberExt.DigitSeparator(daftarTaf.getBes()));

            TextView mande = (TextView) convertView.findViewById(R.id.cell_mande);
            //mSumMande += daftarTaf.getMande();
            //Log.d(TAG, "position = " + position + ", mSumMande = " + mSumMande);
            mande.setText(NumberExt.DigitSeparator(daftarTaf.getMande()));

            TextView mandeType = (TextView) convertView.findViewById(R.id.cell_mande_type);
            mandeType.setText(daftarTaf.getType());

            return null;
        }

        @Override
        protected boolean objectIncludeTheFilterConstraints(DaftarTafReportModel daftarTaf, String[] constraints) {
            if (constraints == null || constraints.length != 2)
                return false;

            String tarikh = constraints[0];
            String des = PersianConvert.ConvertDigitsToLatin(constraints[1]);

            boolean flag = true;
            if (tarikh.length() > 0) {
                flag = daftarTaf.getTarikh().contains(tarikh);
            }

            if (des.length() > 0) {
                flag = (flag && daftarTaf.getDescription().contains(des));
            }

            return flag;
        }
    }
}
