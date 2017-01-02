package info.elyasi.android.elyasilib.UI;

import android.app.ActionBar;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by Canada on 3/4/2016.
 */
public final class ActionbarExt {

    public static String getActionbarTitle(ActivityFragmentExt activity, int titleTextViewId) {
        if (activity.getActionBar() != null) {
            TextView v = (TextView) activity.getActionBar().getCustomView().findViewById(titleTextViewId);
            return v.getText().toString();
        }
        return null;
    }

    public static void setActionbarTitle(ActivityFragmentExt activity, String title, int titleTextViewId) {
        if (activity.getActionBar() != null) {
            TextView v = (TextView) activity.getActionBar().getCustomView().findViewById(titleTextViewId);
            v.setText(title);
        }
    }


    public static void setActionbar(final Activity activity, int layout, String title, int actionbarTitleId, int actionbarBackButtonId) {
        // Inflate your custom layout
        final ViewGroup actionBarLayout = (ViewGroup) activity.getLayoutInflater()
                .inflate(layout, null);

        TextView textViewTitle = (TextView) actionBarLayout.findViewById(actionbarTitleId);
        if (textViewTitle != null) {
            textViewTitle.setText(title);
        }

        View btnBack = actionBarLayout.findViewById(actionbarBackButtonId);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.setResult(Activity.RESULT_CANCELED);
                    activity.finish();
                }
            });
        }

        /*
        for (View btn : actionBarLayout.getTouchables()) {
            btn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    UIUtility.onTouchEffect(view, motionEvent);
                    return false;
                }
            });
        }*/

        // Set up your ActionBar
        final ActionBar actionBar = activity.getActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
            //actionBar.setIcon(R.drawable.ic_logo);
            actionBar.setCustomView(actionBarLayout);
            //setActionbarControls();
        }
    }
}
