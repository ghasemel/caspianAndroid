package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Restaurant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.Models.RestaurantTableModel;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * The ordering screen. Fullscreen (as PFaktorActivity is) so the menu grid gets
 * every available pixel -- the waiter is picking dishes, not reading chrome.
 */
public class OrderActivity extends CaspianActivitySingleFragment {

    // fullscreenTheme is applied in AndroidManifest.xml, matching PFaktorActivity.

    @Override
    public void onCreate(Bundle savedBundleState) {
        super.onCreate(savedBundleState);
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
