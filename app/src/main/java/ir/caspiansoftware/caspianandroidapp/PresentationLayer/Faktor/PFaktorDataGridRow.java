package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.UI.AListRowFragment;
import ir.caspiansoftware.caspianandroidapp.Models.SPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 7/22/2016.
 */
public class PFaktorDataGridRow extends AListRowFragment<SPFaktorModel> {
    private static final String TAG = "PFaktorDataGridRow";


    @Override
    protected ArrayList<SPFaktorModel> inBackground(Object... params) throws Exception {
        return null;
    }

    @Override
    protected void onListItemClicked(SPFaktorModel SPFaktorModel, String resultExtraName) {
        Log.d(TAG, "onListItemClicked(): function entered");
    }

    @Override
    protected MyAdapter postExecute(ArrayList<SPFaktorModel> invoiceDetailsList) throws Exception {
        setCloseAfterSelection(false);

        //throw new Exception("empty invoice detail list");
        if (invoiceDetailsList != null) {
            return new PreInvoiceDetailsAdapter(invoiceDetailsList);
        }
        return null;
    }


    public class PreInvoiceDetailsAdapter extends MyAdapter {

        public PreInvoiceDetailsAdapter(ArrayList<SPFaktorModel> list) {
            super(getActivity(), list);
        }

        @Override
        protected List<ImageView> doForEachItem(View convertView, SPFaktorModel spFaktorModel, final int position, boolean isNull) {
            Log.d(TAG, "doForEachItem(): position = " + position);
            TextView radif = (TextView) convertView.findViewById(R.id.cell_radif);
            TextView kala_name = (TextView) convertView.findViewById(R.id.cell_kala_name);
            TextView mcount = (TextView) convertView.findViewById(R.id.cell_mcount);
            TextView scount = (TextView) convertView.findViewById(R.id.cell_scount);
            TextView price = (TextView) convertView.findViewById(R.id.cell_price);
            TextView price_total = (TextView) convertView.findViewById(R.id.cell_total_price);
            ImageView delete = (ImageView) convertView.findViewById(R.id.cell_delete);

//            radif.setText(PersianConvert.ConvertNumbersToPersian(String.valueOf(position + 1)));
//            kala_name.setText(spFaktorModel.getKalaModel().getName());
//            mcount.setText(PersianConvert.ConvertNumbersToPersian(String.valueOf(spFaktorModel.getMCount())));
//            scount.setText(PersianConvert.ConvertNumbersToPersian(String.valueOf(spFaktorModel.getSCount())));
//            price.setText(PersianConvert.ConvertNumbersToPersian(String.valueOf(spFaktorModel.getPrice())));
//            price_total.setText(PersianConvert.ConvertNumbersToPersian(spFaktorModel.getTotalPriceString()));

            radif.setText(String.valueOf(position + 1));
            kala_name.setText(spFaktorModel.getKalaModel().getName());
            mcount.setText(String.valueOf(spFaktorModel.getMCount()));
            scount.setText(String.valueOf(spFaktorModel.getSCount()));
            price.setText(String.valueOf(spFaktorModel.getPrice()));
            price_total.setText(spFaktorModel.getTotalPriceString());

            List<ImageView> list = new ArrayList<>();
            list.add(delete);
            return list;
        }

        @Override
        protected boolean objectIncludeTheFilterConstraints(SPFaktorModel objectModel, String[] constraints) {
            return false;
        }

        @Override
        public int getLayout() {
            return R.layout.fragment_pfaktor_data_grid_row;
        }
    }
}
