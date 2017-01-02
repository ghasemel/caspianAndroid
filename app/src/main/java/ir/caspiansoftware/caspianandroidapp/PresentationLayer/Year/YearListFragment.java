package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Year;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.R;


/**
 * Created by Canada on 3/3/2016.
 */
public class YearListFragment extends CaspianFragment {

    public ProgressBar getProgressBar() {
        return null;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_year_list;
    }

    protected void mapViews(View parentView) {
    }

    @Override
    public void onClick(View v) {

    }
}
