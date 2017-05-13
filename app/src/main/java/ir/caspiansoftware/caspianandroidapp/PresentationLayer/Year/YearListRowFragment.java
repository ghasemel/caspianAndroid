package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Year;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.UI.AListRowFragment;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.YearMaliBLL;
import ir.caspiansoftware.caspianandroidapp.Models.YearMaliModel;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 2/23/2016.
 */
public class YearListRowFragment extends AListRowFragment<YearMaliModel> { //ArrayList<YearMaliModel>
    private static final String TAG = "YearListRowFragment";

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setCloseAfterSelection(false);
    }

    @Override
    protected void onListItemClicked(YearMaliModel yearMali, String resultExtraName) throws Exception {
        yearMali.setUserId(Vars.USER.getUserId());
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
        }
    }


    @Override
    protected ArrayList<YearMaliModel> inBackground(Object... params) throws Exception {
        if (getActivity() != null && getActivity().getApplicationContext() != null && Vars.USER != null) {
            YearMaliBLL yearMaliBLL = new YearMaliBLL(getActivity().getApplicationContext());
            return yearMaliBLL.FetchYearMali(Vars.USER.getUserId());
        }
        return null;
    }

    @Override
    protected YearMaliAdapter postExecute(ArrayList<YearMaliModel> yearMaliList) throws Exception {
        if (yearMaliList != null) {
            return new YearMaliAdapter(yearMaliList);
            //throw new Exception("empty year mali");
        }
        return null;
        //if (responseWebService.getCode() == Constant.ERROR) {
        //    showError(responseWebService.getData());
        //    return null;
        //} else {
            //ArrayList<YearMaliModel> list = ResponseToModel.getYearMaliList(responseWebService.getData());
        //return new YearMaliAdapter(yearMaliList);
        //}
    }

    private class YearMaliAdapter extends MyAdapter {

        public YearMaliAdapter(ArrayList<YearMaliModel> list) {
            super(getActivity(), list);
        }

        @Override
        public int getLayout() {
            return R.layout.fragment_year_list_row;
        }

        @Override
        protected List<ImageView> doForEachItem(View convertView, YearMaliModel yearMali, int position, boolean isNull) {
            TextView year = (TextView) convertView.findViewById(R.id.year_year);
            //year.setText(PersianConvert.ConvertNumbersToPersian(String.valueOf(yearMali.getYear())));
            year.setText(String.valueOf(yearMali.getYear()));

            TextView daftar = (TextView) convertView.findViewById(R.id.year_daftar);
            //daftar.setText(PersianConvert.ConvertNumbersToPersian(String.valueOf(yearMali.getDaftar())));
            daftar.setText(String.valueOf(yearMali.getDaftar()));

            TextView company = (TextView) convertView.findViewById(R.id.year_company);
            //company.setText(PersianConvert.ConvertNumbersToPersian(yearMali.getCompany()));
            company.setText(yearMali.getCompany());

            return null;
        }

        @Override
        protected boolean objectIncludeTheFilterConstraints(YearMaliModel objectModel, String[] constraints) {
            return false;
        }
    }

}
