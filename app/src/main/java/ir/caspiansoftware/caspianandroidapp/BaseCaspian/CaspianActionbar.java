package ir.caspiansoftware.caspianandroidapp.BaseCaspian;

import android.view.View;
import android.widget.TextView;


import info.elyasi.android.elyasilib.UI.ActionbarExt;
import info.elyasi.android.elyasilib.UI.ActivityFragmentExt;
import info.elyasi.android.elyasilib.UI.UIUtility;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 3/7/2016.
 */
public class CaspianActionbar {

    public static void setActionbarLayout(ActivityFragmentExt activity, int layout, int title) {
        setActionbarLayout(activity, layout, activity.getResources().getString(title));
    }

    public static void setActionbarLayout(final ActivityFragmentExt activity, int layout, String title) {
        ActionbarExt.setActionbar(activity, layout, title, R.id.actionbar_title, R.id.actionbar_back_button);
    }

    public static void setActionbarEvents(ActivityFragmentExt activity) {
        if (activity.getActionBar() != null) {
            View v = activity.getActionBar().getCustomView().findViewById(R.id.actionbar_buttons_frame);
            UIUtility.setIncludedButtonsEffect(v, activity);
        }
    }

    public static void setActionbarTitle(ActivityFragmentExt activity, String title) {
        ActionbarExt.setActionbarTitle(activity, title, R.id.actionbar_title);
    }

    public static void addActionbarTitle(ActivityFragmentExt activity, String title) {
        if (activity.getActionBar() != null) {
            TextView v = (TextView) activity.getActionBar().getCustomView().findViewById(R.id.actionbar_title);
            String tmp = v.getText() + title;
            v.setText(tmp);
        }
    }

    public static String getActionbarTitle(ActivityFragmentExt activity) {
        return ActionbarExt.getActionbarTitle(activity, R.id.actionbar_title);
    }
}