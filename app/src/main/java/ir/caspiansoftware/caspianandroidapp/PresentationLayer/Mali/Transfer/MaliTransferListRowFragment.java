package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Mali.Transfer;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.caspiansoftware.caspianandroidapp.BusinessLayer.MaliBLL;
import ir.caspiansoftware.caspianandroidapp.Models.MaliModel;
import ir.caspiansoftware.caspianandroidapp.PresentationLayer.Mali.Search.MaliSearchRowFragment;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 7/30/2016.
 */
public class MaliTransferListRowFragment extends MaliSearchRowFragment {
    private static final String TAG = "MaliConfirmListRow";

    @Override
    protected void onListItemClicked(MaliModel maliModel, String resultExtraName) throws Exception {
        //super.onListItemClicked(maliModel, resultExtraName);
    }

    @Override
    protected ArrayList<MaliModel> inBackground(Object... params) throws Exception {
        Log.d(TAG, "inBackground(): function entered");

        if (getActivity() != null) {
            MaliBLL maliBLL = new MaliBLL(getActivity().getApplicationContext());
            return maliBLL.getMaliModelsDescending();
        }
        return null;
    }

    @Override
    protected MyAdapter postExecute(ArrayList<MaliModel> maliModels) throws Exception {
        setCloseAfterSelection(false);

        if (maliModels != null) {
            return new MaliTransferListAdapter(maliModels);
        }
        return null;
    }








    static class ViewHolder {
        TextView num;
        TextView date;
        TextView bes;
        TextView amount;
        ImageView synced;
        LinearLayout row;
    }







    public class MaliTransferListAdapter extends MyAdapter {
        public MaliTransferListAdapter(ArrayList<MaliModel> list) {
            super(getActivity(), list);
        }

        @Override
        public int getLayout() {
            return R.layout.fragment_mali_transfer_list_row;
        }




        @Override
        protected List<ImageView> doForEachItem(View convertView, MaliModel maliModel, int position, boolean initiateView) {
            Log.d(TAG, "doForEachItem()");
            ViewHolder viewHolder;


            if (initiateView) {
                // well set up the ViewHolder
                viewHolder = new ViewHolder();
                viewHolder.num = (TextView) convertView.findViewById(R.id.cell_num);
                viewHolder.date = (TextView) convertView.findViewById(R.id.cell_date);
                viewHolder.bes = (TextView) convertView.findViewById(R.id.cell_bes);
                viewHolder.amount = (TextView) convertView.findViewById(R.id.cell_amount);
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
            viewHolder.num.setText(String.valueOf(maliModel.getNum()));
            viewHolder.date.setText(String.valueOf(maliModel.getMaliDate()));

            //Log.d(TAG, "all - synced = " + maliModel.isSynced() + ", id = " + maliModel.getId() + ", position = " + position);

            if (maliModel.isSynced()) {
                Log.d(TAG, "true- synced = " + maliModel.isSynced() +
                        ", id = " + maliModel.getId() +
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


            String tmp = maliModel.getPersonBesModel().getCode() + " " +
                    maliModel.getPersonBesModel().getName();
            viewHolder.bes.setText(tmp);

            viewHolder.amount.setText(maliModel.getAmountString());


            List<ImageView> list = new ArrayList<>();
            list.add(viewHolder.synced);
            return list;
        }

        @Override
        protected boolean objectIncludeTheFilterConstraints(MaliModel objectModel, String[] constraints) {
            return false;
        }

        //        @Override
//        public int getColumnsCount() {
//            return 6;
//        }
    }
}
