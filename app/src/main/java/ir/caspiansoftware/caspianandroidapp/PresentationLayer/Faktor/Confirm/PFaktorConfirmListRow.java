package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor.Confirm;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.caspiansoftware.caspianandroidapp.BusinessLayer.PFaktorBLL;
import ir.caspiansoftware.caspianandroidapp.Models.MPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Faktor.Search.PFaktorSearchRow;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 7/30/2016.
 */
public class PFaktorConfirmListRow extends PFaktorSearchRow {
    private static final String TAG = "InvoiceConfirmListRow";

    @Override
    protected void onListItemClicked(MPFaktorModel mpFaktorModel, String resultExtraName) throws Exception {
        //super.onListItemClicked(mpFaktorModel, resultExtraName);
    }

    @Override
    protected ArrayList<MPFaktorModel> inBackground(Object... params) throws Exception {
        Log.d(TAG, "inBackground(): function entered");

        if (getActivity() != null) {
            PFaktorBLL faktorBLL = new PFaktorBLL(getActivity().getApplicationContext());
            return faktorBLL.getMPFaktorsByLast();
        }
        return null;
    }

    @Override
    protected MyAdapter postExecute(ArrayList<MPFaktorModel> mpFaktorList) throws Exception {
        setCloseAfterSelection(false);

        if (mpFaktorList != null) {
            return new PreInvoiceConfirmListAdapter(mpFaktorList);
        }
        return null;
    }








    static class ViewHolder {
        TextView num;
        TextView date;
        TextView customer;
        TextView total_price;
        ImageView synced;
        LinearLayout row;
    }







    public class PreInvoiceConfirmListAdapter extends MyAdapter {
        public PreInvoiceConfirmListAdapter(ArrayList<MPFaktorModel> list) {
            super(getActivity(), list);
        }

        @Override
        public int getLayout() {
            return R.layout.fragment_pfaktor_confirm_list_row;
        }




        @Override
        protected List<ImageView> doForEachItem(View convertView, MPFaktorModel mpFaktorModel, int position, boolean isNull) {
            Log.d(TAG, "doForEachItem()");
            ViewHolder viewHolder;


            if (isNull) {
                // well set up the ViewHolder
                viewHolder = new ViewHolder();
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


            //radif.setText(String.valueOf(position + 1));
            viewHolder.num.setText(String.valueOf(mpFaktorModel.getNum()));
            viewHolder.date.setText(String.valueOf(mpFaktorModel.getDate()));

            //Log.d(TAG, "all - synced = " + mpFaktorModel.isSynced() + ", id = " + mpFaktorModel.getId() + ", position = " + position);

            if (mpFaktorModel.isSynced()) {
                Log.d(TAG, "true- synced = " + mpFaktorModel.isSynced() +
                        ", id = " + mpFaktorModel.getId() +
                        ", position = " + position
                );

                viewHolder.synced.setImageResource(android.R.drawable.checkbox_on_background);
                viewHolder.synced.setEnabled(false);

                viewHolder.row.setBackgroundColor(getResources().getColor(R.color.syncedBackColor));
            } else {
                viewHolder.synced.setImageResource(android.R.drawable.checkbox_off_background);
                viewHolder.synced.setEnabled(true);

                viewHolder.row.setBackgroundColor(getResources().getColor(android.R.color.white));
            }


            String tmp = mpFaktorModel.getPersonModel().getCode() + " " +
                    mpFaktorModel.getPersonModel().getName();
            viewHolder.customer.setText(tmp);

            viewHolder.total_price.setText(mpFaktorModel.getPriceTotalString());


            List<ImageView> list = new ArrayList<>();
            list.add(viewHolder.synced);
            return list;
        }

        @Override
        protected boolean objectIncludeTheFilterConstraints(MPFaktorModel objectModel, String[] constraints) {
            return false;
        }

        //        @Override
//        public int getColumnsCount() {
//            return 6;
//        }
    }
}
