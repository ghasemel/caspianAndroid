package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Restaurant;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.elyasi.android.elyasilib.Dialogs.DialogResult;
import info.elyasi.android.elyasilib.Dialogs.IDialogCallback;
import info.elyasi.android.elyasilib.UI.AAsyncTask;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.RestaurantBLL;
import ir.caspiansoftware.caspianandroidapp.Models.MenuGroupModel;
import ir.caspiansoftware.caspianandroidapp.Models.MenuItemModel;
import ir.caspiansoftware.caspianandroidapp.Models.OrderLineModel;
import ir.caspiansoftware.caspianandroidapp.Models.RestaurantOrderModel;
import ir.caspiansoftware.caspianandroidapp.Models.RestaurantTableModel;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * The ordering screen: groups, dishes and the running bill on ONE screen.
 *
 * The core loop is group -> tap -> tap -> group -> tap, and any navigation
 * between those steps costs seconds on every order, so nothing here opens a
 * new screen. Layout top to bottom:
 *
 *   table name + search      (rarely touched, so furthest from the thumb)
 *   group chips              (horizontal scroll, RTL)
 *   dish grid                (2 columns, tap anywhere on a tile = +1)
 *   cart bar                 (pinned bottom, always visible, thumb-reachable)
 *
 * Tapping a dish adds one immediately -- no dialog, no confirmation. The note
 * and exact-quantity entry live one long-press deeper so the common case stays
 * a single tap.
 */
public class OrderFragment extends CaspianFragment {
    private static final String TAG = "OrderFragment";

    public static final String EXTRA_TABLE = "extra_table";

    private RestaurantTableModel mTable;
    private RestaurantOrderModel mOrder;

    private final List<MenuGroupModel> mGroups = new ArrayList<>();
    private final List<MenuItemModel> mItems = new ArrayList<>();
    private final List<MenuItemModel> mVisibleItems = new ArrayList<>();

    private int mSelectedGroup = -1;

    /**
     * Stable across retries of the same draft: the server uses it to recognise
     * a resend after a dropped response and return the original order instead
     * of duplicating it. A genuinely new round gets a new id.
     */
    private int mAndroidOrderId;

    private TextView mTitleLabel;
    private EditText mSearchBox;
    private LinearLayout mGroupStrip;
    private HorizontalScrollView mGroupScroll;
    private GridView mItemGrid;
    private ProgressBar mProgress;
    private TextView mCartSummary;
    private TextView mCartTotal;
    private Button mSubmitButton;
    private View mCartPanel;
    private ListView mCartList;

    private ItemAdapter mItemAdapter;
    private CartAdapter mCartAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_restaurant_order;
    }

    @Override
    protected TextView getErrorLabel() { return null; }

    @Override
    protected View getStarterControl() { return null; }

    @Override
    public ProgressBar getProgressBar() { return mProgress; }

    @Override
    protected void mapViews(View parentView) {
        mTitleLabel = parentView.findViewById(R.id.orderTableTitle);
        mSearchBox = parentView.findViewById(R.id.orderSearchBox);
        mGroupStrip = parentView.findViewById(R.id.orderGroupStrip);
        mGroupScroll = parentView.findViewById(R.id.orderGroupScroll);
        mItemGrid = parentView.findViewById(R.id.orderItemGrid);
        mProgress = parentView.findViewById(R.id.orderProgress);
        mCartSummary = parentView.findViewById(R.id.orderCartSummary);
        mCartTotal = parentView.findViewById(R.id.orderCartTotal);
        mSubmitButton = parentView.findViewById(R.id.orderSubmitButton);
        mCartPanel = parentView.findViewById(R.id.orderCartPanel);
        mCartList = parentView.findViewById(R.id.orderCartList);
    }

    @Override
    protected void afterMapViews(View parentView) {
        if (getArguments() != null)
            mTable = (RestaurantTableModel) getArguments().getSerializable(EXTRA_TABLE);

        if (mTable == null) {
            showError(getString(R.string.restaurant_no_table), null);
            return;
        }

        mAndroidOrderId = generateOrderId(mTable.getId());

        mTitleLabel.setText(mTable.getTitle());

        mItemAdapter = new ItemAdapter();
        mItemGrid.setAdapter(mItemAdapter);
        mItemGrid.setOnItemClickListener((p, v, position, id) -> addItem(mVisibleItems.get(position)));
        mItemGrid.setOnItemLongClickListener((p, v, position, id) -> {
            editItem(mVisibleItems.get(position));
            return true;
        });

        mCartAdapter = new CartAdapter();
        mCartList.setAdapter(mCartAdapter);

        View cartBar = parentView.findViewById(R.id.orderCartBar);
        cartBar.setOnClickListener(v -> toggleCart());

        mSubmitButton.setOnClickListener(v -> confirmSubmit());

        mSearchBox.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) { applyFilter(s.toString()); }
            @Override public void afterTextChanged(Editable s) { }
        });

        loadEverything();
    }

    /**
     * Required by AAsyncFragment (which implements OnClickListener). This
     * screen wires its own listeners in afterMapViews, so nothing to route.
     */
    @Override
    public void onClick(View v) {
    }

    /**
     * Table id plus the minute the draft started. Two rounds at the same table
     * get different ids; a retry within the same minute keeps the same one,
     * which is exactly the window a dropped response falls into.
     */
    private static int generateOrderId(int tableId) {
        long minutes = System.currentTimeMillis() / 60000L;
        return (int) ((minutes % 1000000L) * 100 + (tableId % 100));
    }

    // region loading

    private void loadEverything() {
        new LoadTask(mProgress).execute();
    }

    private class LoadTask extends AAsyncTask<Void, Void, LoadResult> {
        LoadTask(ProgressBar bar) { super(bar); }

        @Override
        protected LoadResult doInBackground(Void... voids) {
            try {
                RestaurantBLL bll = new RestaurantBLL(getActivity());
                LoadResult r = new LoadResult();
                r.groups = bll.fetchGroups();
                r.order = bll.fetchOrderForTable(mTable);
                if (!r.groups.isEmpty())
                    r.items = bll.fetchGroupItems(r.groups.get(0).getCode());
                return r;
            } catch (Exception ex) {
                setException(ex);
                return null;
            }
        }

        @Override
        protected void onPostExecute(LoadResult result) {
            stopProgress();

            if (isException()) {
                showError(getException(), null);
                return;
            }

            mOrder = result.order;
            mGroups.clear();
            mGroups.addAll(result.groups);
            buildGroupStrip();

            if (!mGroups.isEmpty()) {
                mSelectedGroup = mGroups.get(0).getCode();
                mItems.clear();
                if (result.items != null)
                    mItems.addAll(result.items);
                applyFilter(mSearchBox.getText().toString());
            }

            refreshCart();
        }
    }

    private static class LoadResult {
        List<MenuGroupModel> groups = new ArrayList<>();
        List<MenuItemModel> items = new ArrayList<>();
        RestaurantOrderModel order;
    }

    private void loadGroup(int grkCode) {
        mSelectedGroup = grkCode;
        highlightSelectedGroup();
        new LoadItemsTask(mProgress, grkCode).execute();
    }

    private class LoadItemsTask extends AAsyncTask<Void, Void, List<MenuItemModel>> {
        private final int mGrk;

        LoadItemsTask(ProgressBar bar, int grk) { super(bar); mGrk = grk; }

        @Override
        protected List<MenuItemModel> doInBackground(Void... voids) {
            try {
                return new RestaurantBLL(getActivity()).fetchGroupItems(mGrk);
            } catch (Exception ex) {
                setException(ex);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<MenuItemModel> result) {
            stopProgress();

            if (isException()) {
                showError(getException(), null);
                return;
            }

            mItems.clear();
            if (result != null)
                mItems.addAll(result);
            applyFilter(mSearchBox.getText().toString());
        }
    }

    // endregion

    // region group strip

    private void buildGroupStrip() {
        mGroupStrip.removeAllViews();

        for (final MenuGroupModel group : mGroups) {
            Button chip = (Button) LayoutInflater.from(getActivity())
                    .inflate(R.layout.cell_group_chip, mGroupStrip, false);
            chip.setText(group.getName());
            chip.setTag(group.getCode());
            chip.setOnClickListener(v -> loadGroup(group.getCode()));
            mGroupStrip.addView(chip);
        }

        highlightSelectedGroup();
    }

    private void highlightSelectedGroup() {
        for (int i = 0; i < mGroupStrip.getChildCount(); i++) {
            View chip = mGroupStrip.getChildAt(i);
            boolean selected = chip.getTag() != null && (Integer) chip.getTag() == mSelectedGroup;
            chip.setBackgroundResource(selected ? R.drawable.group_chip_selected : R.drawable.group_chip);
        }
    }

    // endregion

    // region items

    /**
     * Search spans every group, not just the selected one: with a large menu
     * the waiter usually knows the dish but not which category it sits in.
     */
    private void applyFilter(String query) {
        mVisibleItems.clear();

        String q = query == null ? "" : query.trim();
        for (MenuItemModel item : mItems)
            if (q.isEmpty() || item.getName().contains(q) || item.getCode().contains(q))
                mVisibleItems.add(item);

        mItemAdapter.notifyDataSetChanged();
    }

    private void addItem(MenuItemModel item) {
        mOrder.addItem(item, 1);
        mItemAdapter.notifyDataSetChanged();
        refreshCart();
    }

    private void editItem(final MenuItemModel item) {
        OrderLineDialog.show(this, item, mOrder, () -> {
            mItemAdapter.notifyDataSetChanged();
            refreshCart();
        });
    }

    // endregion

    // region cart

    private void toggleCart() {
        boolean visible = mCartPanel.getVisibility() == View.VISIBLE;
        mCartPanel.setVisibility(visible ? View.GONE : View.VISIBLE);
        if (!visible)
            mCartAdapter.notifyDataSetChanged();
    }

    private void refreshCart() {
        if (mOrder == null)
            return;

        int count = mOrder.getDraftItemCount();
        mCartSummary.setText(getString(R.string.restaurant_cart_items, count));
        mCartTotal.setText(formatPrice(mOrder.getGrandTotal()));
        mSubmitButton.setEnabled(mOrder.hasDraft());
        mCartAdapter.notifyDataSetChanged();
    }

    private void confirmSubmit() {
        if (mOrder == null || !mOrder.hasDraft())
            return;

        messageBoxYesNo(
                R.string.restaurant_submit_title,
                getString(R.string.restaurant_submit_question,
                          mOrder.getDraftItemCount(), mTable.getTitle()),
                new IDialogCallback<Integer>() {
                    @Override
                    public void dialog_callback(DialogResult dialogResult, Integer result, int requestCode) {
                        if (dialogResult == DialogResult.Yes)
                            new SubmitTask(mProgress).execute();
                    }
                });
    }

    private class SubmitTask extends AAsyncTask<Void, Void, RestaurantOrderModel> {
        SubmitTask(ProgressBar bar) { super(bar); }

        @Override
        protected RestaurantOrderModel doInBackground(Void... voids) {
            try {
                return new RestaurantBLL(getActivity()).submitDraft(mOrder, mAndroidOrderId);
            } catch (Exception ex) {
                setException(ex);
                return null;
            }
        }

        @Override
        protected void onPostExecute(RestaurantOrderModel result) {
            stopProgress();

            if (isException()) {
                showError(getException(), null);
                return;
            }

            showToast(getString(R.string.restaurant_submit_done, result.getNum()));

            // A new draft after this point is a genuinely new round, so it
            // needs its own idempotency key.
            mAndroidOrderId = generateOrderId(mTable.getId()) + 1;

            mCartPanel.setVisibility(View.GONE);
            mItemAdapter.notifyDataSetChanged();
            refreshCart();
        }
    }

    // endregion

    static String formatPrice(double value) {
        return String.format(Locale.US, "%,d", Math.round(value));
    }

    // region adapters

    private class ItemAdapter extends BaseAdapter {
        @Override public int getCount() { return mVisibleItems.size(); }
        @Override public Object getItem(int position) { return mVisibleItems.get(position); }
        @Override public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null)
                view = LayoutInflater.from(getActivity()).inflate(R.layout.cell_product_tile, parent, false);

            MenuItemModel item = mVisibleItems.get(position);

            TextView name = view.findViewById(R.id.productTileName);
            TextView price = view.findViewById(R.id.productTilePrice);
            TextView badge = view.findViewById(R.id.productTileBadge);

            name.setText(item.getName());
            price.setText(formatPrice(item.getPrice()));

            double inCart = mOrder == null ? 0 : mOrder.getDraftCount(item.getCode());
            if (inCart > 0) {
                badge.setText(String.valueOf((long) inCart));
                badge.setVisibility(View.VISIBLE);
            } else {
                badge.setVisibility(View.GONE);
            }

            return view;
        }
    }

    private class CartAdapter extends BaseAdapter {
        private List<OrderLineModel> lines() {
            return mOrder == null ? new ArrayList<>() : mOrder.getLines();
        }

        @Override public int getCount() { return lines().size(); }
        @Override public Object getItem(int position) { return lines().get(position); }
        @Override public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null)
                view = LayoutInflater.from(getActivity()).inflate(R.layout.row_cart_line, parent, false);

            final OrderLineModel line = lines().get(position);

            TextView name = view.findViewById(R.id.cartLineName);
            TextView count = view.findViewById(R.id.cartLineCount);
            TextView total = view.findViewById(R.id.cartLineTotal);
            TextView spec = view.findViewById(R.id.cartLineSpec);
            View minus = view.findViewById(R.id.cartLineMinus);
            View plus = view.findViewById(R.id.cartLinePlus);
            TextView sentFlag = view.findViewById(R.id.cartLineSent);

            name.setText(line.getName());
            count.setText(String.valueOf((long) line.getCount()));
            total.setText(formatPrice(line.getTotal()));

            if (line.getSpec().isEmpty()) {
                spec.setVisibility(View.GONE);
            } else {
                spec.setText(line.getSpec());
                spec.setVisibility(View.VISIBLE);
            }

            // Lines the kitchen already has must not be editable here --
            // changing them on the phone would not un-cook the food.
            if (line.isSent()) {
                sentFlag.setVisibility(View.VISIBLE);
                minus.setVisibility(View.INVISIBLE);
                plus.setVisibility(View.INVISIBLE);
            } else {
                sentFlag.setVisibility(View.GONE);
                minus.setVisibility(View.VISIBLE);
                plus.setVisibility(View.VISIBLE);

                minus.setOnClickListener(v -> {
                    if (line.getCount() <= 1)
                        mOrder.removeLine(line);
                    else
                        line.setCount(line.getCount() - 1);
                    mItemAdapter.notifyDataSetChanged();
                    refreshCart();
                });

                plus.setOnClickListener(v -> {
                    line.setCount(line.getCount() + 1);
                    mItemAdapter.notifyDataSetChanged();
                    refreshCart();
                });
            }

            return view;
        }
    }

    // endregion
}
