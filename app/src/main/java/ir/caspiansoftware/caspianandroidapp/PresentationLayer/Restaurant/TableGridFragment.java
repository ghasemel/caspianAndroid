package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.UI.AAsyncTask;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.RestaurantBLL;
import ir.caspiansoftware.caspianandroidapp.Models.RestaurantTableModel;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * The table grid.
 *
 * Big two-column tiles: a waiter reads this at arm's length while walking, so
 * occupancy is shown by the whole tile colour plus text, never colour alone.
 * Refreshes on resume so returning from an order shows the new state.
 */
public class TableGridFragment extends CaspianFragment {
    private static final String TAG = "TableGridFragment";

    private GridView mGrid;
    private TextView mEmptyLabel;
    private ProgressBar mProgress;

    private final List<RestaurantTableModel> mTables = new ArrayList<>();
    private TableAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_table_grid;
    }

    @Override
    protected TextView getErrorLabel() {
        return null;
    }

    @Override
    protected View getStarterControl() {
        return null;
    }

    @Override
    public ProgressBar getProgressBar() {
        return mProgress;
    }

    @Override
    protected void mapViews(View parentView) {
        mGrid = parentView.findViewById(R.id.tableGrid);
        mEmptyLabel = parentView.findViewById(R.id.tableEmptyLabel);
        mProgress = parentView.findViewById(R.id.tableProgress);
    }

    @Override
    protected void afterMapViews(View parentView) {
        mAdapter = new TableAdapter();
        mGrid.setAdapter(mAdapter);

        mGrid.setOnItemClickListener((parent, view, position, id) -> openTable(mTables.get(position)));

        // The refresh action lives on the activity's action bar, not in this
        // fragment's layout, so it is looked up through the activity.
        if (getActivity() != null) {
            View refresh = getActivity().findViewById(R.id.refresh_btn);
            if (refresh != null)
                refresh.setOnClickListener(v -> loadTables());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTables();
    }

    /**
     * Required by AAsyncFragment (which implements OnClickListener). This
     * screen wires its own listeners in afterMapViews, so nothing to route.
     */
    @Override
    public void onClick(View v) {
    }

    private void openTable(RestaurantTableModel table) {
        Intent intent = new Intent(getActivity(), OrderActivity.class);
        intent.putExtra(OrderFragment.EXTRA_TABLE, table);
        startActivity(intent);
    }

    private void loadTables() {
        new LoadTablesTask(mProgress).execute();
    }

    private class LoadTablesTask extends AAsyncTask<Void, Void, List<RestaurantTableModel>> {
        LoadTablesTask(ProgressBar bar) { super(bar); }

        @Override
        protected List<RestaurantTableModel> doInBackground(Void... voids) {
            try {
                return new RestaurantBLL(getActivity()).fetchTables();
            } catch (Exception ex) {
                setException(ex);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<RestaurantTableModel> result) {
            stopProgress();

            if (isException()) {
                showError(getException(), null);
                return;
            }

            mTables.clear();
            if (result != null)
                mTables.addAll(result);

            mAdapter.notifyDataSetChanged();
            mEmptyLabel.setVisibility(mTables.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Recycling adapter over GridView -- the same convertView pattern the rest
     * of the app uses, so no new list dependency is needed.
     */
    private class TableAdapter extends BaseAdapter {
        @Override public int getCount() { return mTables.size(); }
        @Override public Object getItem(int position) { return mTables.get(position); }
        @Override public long getItemId(int position) { return mTables.get(position).getId(); }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null)
                view = LayoutInflater.from(getActivity()).inflate(R.layout.cell_table_tile, parent, false);

            RestaurantTableModel table = mTables.get(position);

            TextView title = view.findViewById(R.id.tableTileTitle);
            TextView status = view.findViewById(R.id.tableTileStatus);
            TextView total = view.findViewById(R.id.tableTileTotal);

            title.setText(table.getTitle());

            if (table.isOccupied()) {
                view.setBackgroundResource(R.drawable.table_tile_occupied);
                status.setText(getString(R.string.restaurant_table_busy, table.getItemCount()));
                total.setText(formatPrice(table.getOpenTotal()));
                total.setVisibility(View.VISIBLE);
            } else {
                view.setBackgroundResource(R.drawable.table_tile_free);
                status.setText(R.string.restaurant_table_free);
                total.setText("");
                total.setVisibility(View.GONE);
            }

            return view;
        }
    }

    static String formatPrice(double value) {
        return String.format(java.util.Locale.US, "%,d", Math.round(value));
    }
}
