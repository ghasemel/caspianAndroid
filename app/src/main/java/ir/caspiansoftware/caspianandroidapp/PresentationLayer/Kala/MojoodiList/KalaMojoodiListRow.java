package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Kala.MojoodiList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.Persian.PersianConvert;
import info.elyasi.android.elyasilib.UI.AListRowFragment;
import info.elyasi.android.elyasilib.Utility.NumberExt;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.KalaBLL;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 7/22/2016.
 */
public class KalaMojoodiListRow extends AListRowFragment<KalaModel> {
    private static final String TAG = "KalaMojoodiRowFragment";


    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);

        setCloseAfterSelection(false);
    }

    @Override
    protected ArrayList<KalaModel> inBackground(Object... params) throws Exception {
        Log.d(TAG, "inBackground(): function entered");

        if (getActivity() != null && getActivity().getApplicationContext() != null && Vars.USER != null) {
            Log.d(TAG, "YearId: " + Vars.YEAR.getId());

            KalaBLL kalaBLL = new KalaBLL(getActivity().getApplicationContext());
            return kalaBLL.getKalaListByYearId(Vars.YEAR.getId());
        }
        return null;
    }

    @Override
    protected KalaAdapter postExecute(ArrayList<KalaModel> kalaList) throws Exception {
        if (kalaList != null)
            return new KalaAdapter(kalaList);
            //throw new Exception("empty person list");

        //mList = personList;

        return null;
    }

    @Override
    protected void onListItemClicked(KalaModel kalaModel, String resultExtraName) throws Exception {
//        Intent intent = new Intent();
//        intent.putExtra(resultExtraName, kalaModel);
//        getActivity().setResult(Activity.RESULT_OK, intent);
    }


    private class KalaAdapter extends MyAdapter {

        public KalaAdapter(ArrayList<KalaModel> list) {
            super(getActivity(), list);
        }

        @Override
        public int getLayout() {
            return R.layout.fragment_kala_mojoodi_list_row;
        }

        @Override
        protected List<ImageView> doForEachItem(View convertView, KalaModel kalaModel, int position, boolean isNull) {
            TextView cell_radif = (TextView) convertView.findViewById(R.id.cell_radif);
            cell_radif.setText(String.valueOf(position + 1));

            TextView cell_code = (TextView) convertView.findViewById(R.id.cell_kala_code);
            cell_code.setText(kalaModel.getCode());

            TextView cell_description = (TextView) convertView.findViewById(R.id.cell_kala_name);
            cell_description.setText(kalaModel.getName());

            TextView cell_mojoodi = (TextView) convertView.findViewById(R.id.cell_mojoodi);
            cell_mojoodi.setText(NumberExt.DigitSeparator(kalaModel.getMojoodi()));

            ImageView cell_gallery = convertView.findViewById(R.id.cell_gallery);
            List<ImageView> imageViews = new ArrayList<>();
            imageViews.add(cell_gallery);

            return imageViews;
        }



        protected boolean objectIncludeTheFilterConstraints(KalaModel kalaModel, String[] constraints) {
            if (constraints == null || constraints.length != 2)
                return false;

            //String code = PersianConvert.ConvertDigitsToPersian(constraints[0]);
            String code = constraints[0];
            String name = PersianConvert.ConvertDigitsToLatin(constraints[1]);

            boolean flag = true;
            if (code.length() > 0) {
                flag = kalaModel.getCode().contains(code);
            }

            if (name.length() > 0) {
                flag = (flag && kalaModel.getName().contains(name));
            }

            return flag;
        }
    }
}