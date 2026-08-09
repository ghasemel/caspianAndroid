package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Restaurant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.Models.RestaurantTableModel;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * The ordering screen.
 *
 * Uses the standard action bar rather than a fullscreen theme: the waiter needs
 * a back button to leave a table, and every other screen in the app puts it
 * there. The miz name becomes the action bar title, so the screen does not
 * carry a second header of its own.
 */
public class OrderActivity extends CaspianActivitySingleFragment {

    @Override
    public void onCreate(Bundle savedBundleState) {
        RestaurantTableModel table =
                (RestaurantTableModel) getIntent().getSerializableExtra(OrderFragment.EXTRA_TABLE);

        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog,
                table == null ? "" : table.getTitle());

        super.onCreate(savedBundleState);

        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        OrderFragment fragment = new OrderFragment();

        RestaurantTableModel table =
                (RestaurantTableModel) getIntent().getSerializableExtra(OrderFragment.EXTRA_TABLE);

        Bundle args = new Bundle();
        args.putSerializable(OrderFragment.EXTRA_TABLE, table);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.single_fragment_activity;
    }
}
