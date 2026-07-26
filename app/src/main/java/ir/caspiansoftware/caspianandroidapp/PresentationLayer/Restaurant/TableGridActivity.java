package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Restaurant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActionbar;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianActivitySingleFragment;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * First screen of the restaurant flow: the grid of tables.
 */
public class TableGridActivity extends CaspianActivitySingleFragment {

    @Override
    public void onCreate(Bundle savedBundleState) {
        CaspianActionbar.setActionbarLayout(this, R.layout.actionbar_dialog, R.string.restaurant_tables_title);
        super.onCreate(savedBundleState);
        CaspianActionbar.setActionbarEvents(this);
    }

    @Override
    public Fragment createFragment() {
        return new TableGridFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.single_fragment_activity;
    }
}
