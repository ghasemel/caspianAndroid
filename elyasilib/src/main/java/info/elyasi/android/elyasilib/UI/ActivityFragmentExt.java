package info.elyasi.android.elyasilib.UI;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import info.elyasi.android.elyasilib.Persian.PersianConvert;
import info.elyasi.android.elyasilib.R;
import info.elyasi.android.elyasilib.Thread.ICallBack;


/**
 * Created by Canada on 1/6/2016.
 */
public abstract class ActivityFragmentExt extends FragmentActivity implements View.OnClickListener, View.OnTouchListener, IActivityCallback {

    private static final String TAG = "ActivityFragmentExt";

    public abstract Fragment createFragment();
    public abstract int getFragmentContainerId();
    public abstract int getFragmentDetailContainerId();
    public abstract int getLayoutId();


    //private Fragment mFragmentDetail = null;
    //private Intent mActivityDetail = null;

    private Fragment mFragment;

    @Override
    public void onCreate(Bundle savedBundleState) {
        super.onCreate(savedBundleState);
        setContentView(getLayoutId());

        FragmentManager fragmentManager = getSupportFragmentManager();
        mFragment = fragmentManager.findFragmentById(getFragmentContainerId());

        if (mFragment == null) {
            mFragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(getFragmentContainerId(), mFragment)
                    .commit();
        }

        //if (getActionBar() != null) {
            //setOnClickTouchEffect(getActionBar().getCustomView());
        //}
    }

    public Fragment getFragmentContainer() {
        return getSupportFragmentManager().findFragmentById(getFragmentContainerId());
    }

    public Fragment getFragmentDetailContainer() {
        return getSupportFragmentManager().findFragmentById(getFragmentDetailContainerId());
    }

    protected void removeFragmentDetail() {
        if (getFragmentDetailContainer() != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(getFragmentDetailContainer())
            .commit();
        }
    }

    protected void startDetailFragment(Fragment newFragment, Intent intentActivity) throws Exception {

        // if device has small screen
        if (findViewById(getFragmentDetailContainerId()) == null) {
            // start a new activity
            if (intentActivity == null)
                throw new ClassNotFoundException("intentActivity is null");

            startActivity(intentActivity);
        } else {
            // if device is a tablet
            if (newFragment == null)
                throw new ClassNotFoundException("setFragmentDetail function is not implemented");

            // add_btn to detail FrameLayout
            FragmentManager fm = getSupportFragmentManager();
            Fragment oldFragment = fm.findFragmentById(getFragmentDetailContainerId());

            FragmentTransaction ft = fm.beginTransaction();
            if (oldFragment != null) {
                ft.remove(oldFragment);
            }

            ft.add(getFragmentDetailContainerId(), newFragment);
            ft.commit();
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    //@Override
    //public void onActivityResult(int requestCode, int resultCode, Intent data) {
    //    super.onActivityResult(requestCode, resultCode, data);

        //Fragment fragment = getSupportFragmentManager().findFragmentById(getFragmentContainerId());
        //fragment.onActivityResult(requestCode, resultCode, data);
    //}

    public static void showAsPopup(Activity activity, int height, int width) {
        //To show activity as dialog and dim the background, you need to declare android:theme="@style/PopupTheme" on for the chosen activity on the manifest
        activity.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.height = height; //fixed height
        params.width = width; //fixed width
        params.alpha = 1.0f;
        params.dimAmount = 0.5f;
        activity.getWindow().setAttributes(params);
    }

    public void fullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    public void removeTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

//    public void showError(String errorMessage) {
//        AlertDialog dialog = new AlertDialog.Builder(this.getApplicationContext())
//                .setTitle(android.R.string.untitled)
//                .setMessage(PersianConvert.ConvertNumbersToPersian(errorMessage))
//                .setPositiveButton(android.R.string.yes, null)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .create();
//
//        dialog.show();
//    }


    @Override
    public boolean onTouch(View sender, MotionEvent motionEvent) {
        return true;
    }


    @Override
    public void onClick(View sender) {

    }


    public void LockScreenRotation() {
        int orientation = getResources().getConfiguration().orientation;
        Log.d(TAG, "orientation:" +  orientation);
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }

    public void UnLockScreenRotation() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }


    public void setActionbarTitle(int title, int actionbarTitleTextViewId) {
        setActionbarTitle(getResources().getString(title), actionbarTitleTextViewId);
    }

    public void setActionbarTitle(String title, int actionbarTitleTextViewId) {
        if (title == null)
            return;

        TextView titleView = (TextView) findViewById(actionbarTitleTextViewId);
        if (titleView != null) {
            titleView.setText(title);
        }
    }

    public void showError(String errorMessage) {
        showError(errorMessage, null);
    }

    public void showError(String errorMessage, final ICallBack callBack) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error_title)
                //.setMessage(PersianConvert.ConvertNumbersToPersian(errorMessage))
                .setMessage(errorMessage)
                .setIcon(R.drawable.error_dialog)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callBack.callback("OnClick", i, null);
                    }
                }).create().show();
    }
}
