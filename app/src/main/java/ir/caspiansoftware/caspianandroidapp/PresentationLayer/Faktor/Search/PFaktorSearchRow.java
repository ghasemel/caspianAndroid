package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor.Search;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.Persian.PersianConvert;
import info.elyasi.android.elyasilib.UI.AListRowFragment;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.PFaktorBLL;
import ir.caspiansoftware.caspianandroidapp.Models.MPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 7/22/2016.
 */
public class PFaktorSearchRow extends AListRowFragment<MPFaktorModel> {
    private static final String TAG = "PFaktorSearchRow";



    @Override
    protected ArrayList<MPFaktorModel> inBackground(Object... params) throws Exception {
        Log.d(TAG, "inBackground(): function entered");

        if (getActivity() != null) {
            PFaktorBLL faktorBLL = new PFaktorBLL(getActivity().getApplicationContext());
            return faktorBLL.getMPFaktors();
        }
        return null;
    }

    @Override
    protected void onListItemClicked(MPFaktorModel mpFaktorModel, String resultExtraName) throws Exception {
        Log.d(TAG, "onListItemClicked(): function entered");
        Intent intent = new Intent();
        intent.putExtra(resultExtraName, mpFaktorModel);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    protected MyAdapter postExecute(ArrayList<MPFaktorModel> mpFaktorList) throws Exception {
        setCloseAfterSelection(true);

        //throw new Exception("empty invoice detail list");
        if (mpFaktorList != null) {
            return new PreInvoiceAdapter(mpFaktorList);
        }
        return null;
    }



    static class ViewHolder {
        TextView radif;
        TextView num;
        TextView date;
        TextView customer;
        TextView total_price;
        ImageView synced;
        LinearLayout row;
    }



    public class PreInvoiceAdapter extends MyAdapter {

        public PreInvoiceAdapter(ArrayList<MPFaktorModel> list) {
            super(getActivity(), list);
        }

        @Override
        protected List<ImageView> doForEachItem(View convertView, MPFaktorModel mpFaktorModel, final int position, boolean isNull) {
            ViewHolder viewHolder;

            if (isNull) {
                // well set up the ViewHolder
                viewHolder = new ViewHolder();
                viewHolder.radif = (TextView) convertView.findViewById(R.id.cell_radif);
                viewHolder.num = (TextView) convertView.findViewById(R.id.cell_num);
                viewHolder.date = (TextView) convertView.findViewById(R.id.cell_date);
                viewHolder.customer = (TextView) convertView.findViewById(R.id.cell_customer);
                viewHolder.total_price = (TextView) convertView.findViewById(R.id.cell_total_price);
                viewHolder.synced = (ImageView) convertView.findViewById(R.id.cell_synced);
                viewHolder.row = (LinearLayout) convertView.findViewById(R.id.rowId);

                // store the holder with the view.
                convertView.setTag(viewHolder);
            } else {
                // we've just avoided calling findViewById() on resource everytime
                // just use the viewHolder
                viewHolder = (ViewHolder) convertView.getTag();
            }


            viewHolder.radif.setText(String.valueOf(position + 1));
            viewHolder.num.setText(String.valueOf(mpFaktorModel.getNum()));
            viewHolder.date.setText(String.valueOf(mpFaktorModel.getDate()));

            if (mpFaktorModel.isSynced()) {
                viewHolder.synced.setImageResource(android.R.drawable.checkbox_on_background);
                viewHolder.row.setBackgroundColor(getResources().getColor(R.color.syncedBackColor));
            } else {
                viewHolder.synced.setImageResource(android.R.drawable.checkbox_off_background);
                viewHolder.row.setBackgroundColor(getResources().getColor(android.R.color.white));
            }

            String tmp = mpFaktorModel.getPersonModel().getCode() + " " +
                    mpFaktorModel.getPersonModel().getName();
            viewHolder.customer.setText(tmp);


            viewHolder.total_price.setText(mpFaktorModel.getPriceTotalString());

            return null;
        }

        @Override
        protected boolean objectIncludeTheFilterConstraints(MPFaktorModel mpFaktorModel, String[] constraints) {
            if (constraints == null || constraints.length != 4)
                return false;

//            String num = PersianConvert.ConvertDigitsToPersian(constraints[0]);
//            String date = PersianConvert.ConvertDigitsToPersian(constraints[1]);
//            String customer = PersianConvert.ConvertDigitsToPersian(constraints[2]);
//            String total_price = PersianConvert.ConvertNumbersToLatin(constraints[3]);

            String num = constraints[0];
            String date = constraints[1];
            String customer = PersianConvert.ConvertDigitsToLatin(constraints[2]);
            String total_price = constraints[3];

            boolean flag = true;
            if (num.length() > 0) {
                flag = String.valueOf(mpFaktorModel.getNum()).contains(num);
            }

            if (date.length() > 0) {
                flag = (flag &&
                        String.valueOf(mpFaktorModel.getDate()).contains(date));
            }

            if (customer.length() > 0) {
                flag = (
                        flag &&
                        (
                            mpFaktorModel.getPersonModel().getCode() + " " +
                            mpFaktorModel.getPersonModel().getName()
                        ).contains(customer)
                );
            }

            if (total_price.length() > 0) {
                flag = (flag &&
                        String.valueOf(mpFaktorModel.getPriceTotal()).contains(total_price));
            }

            return flag;
        }

        @Override
        public int getLayout() {
            return R.layout.fragment_faktor_search_row;
        }
    }
}
