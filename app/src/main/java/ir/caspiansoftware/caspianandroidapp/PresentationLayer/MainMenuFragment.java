package ir.caspiansoftware.caspianandroidapp.PresentationLayer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Map;

import info.elyasi.android.elyasilib.UI.FormActionTypes;
import info.elyasi.android.elyasilib.UI.IActivityCallback;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.UserBLL;
import ir.caspiansoftware.caspianandroidapp.Models.UserPermissionModel;
import info.elyasi.android.elyasilib.Persian.PersianDate;
import info.elyasi.android.elyasilib.UI.UIUtility;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Vars;


/**
 * Created by Canada on 1/17/2016.
 */
public class MainMenuFragment extends CaspianFragment implements IFragmentCallback {

    private static final String TAG = "MainMenuFragment";


    private LinearLayout mBtnSetting;
    private LinearLayout mBtnPreInvoice;
    private LinearLayout mBtnPersonMande;
    private LinearLayout mBtnKalaMojoodi;
    private LinearLayout mBtnSync;
    private TextView mLabelChangeYear;
    private LinearLayout mFrameChangeYear;
    private LinearLayout mBtnFrame;
    private LinearLayout mBtnConfirmList;

    //private TextView mErrorLabel;
    private ProgressBar mProgressBar;
    // logout button
    private LinearLayout mBtnLogout;

    private TextView mDaftarMali;
    private TextView mYearMali;
    private LinearLayout mAppInfo;
    //private HorizontalScrollView mAppInfoHSV;
    private IActivityCallback mActivityCallback;


    // region fragment

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_menu;
    }

    @Override
    protected void afterMapViews(View parentView) {
        if (Vars.USER != null) {
            TextView mainUserName = (TextView) parentView.findViewById(R.id.main_userName);
            mainUserName.setText(Vars.USER.getName());

            TextView mainUserLastLogin = (TextView) parentView.findViewById(R.id.main_lastLogin);
            //mainUserLastLogin.setText(PersianConvert.ConvertNumbersToPersian(PersianDate.getFullDate(Setting.USER.getLastLoginDate())));
            mainUserLastLogin.setText(PersianDate.getFullDate(Vars.USER.getLastLoginDate()));

            // check for unselected year mali
            if (Vars.YEAR == null) {
                //showYearMaliForm();
                mActivityCallback.fragment_callback(Actions.ACTION_YEAR_MALI, null, (Object) null);
            }

            setYearDaftar();

            setUserPermission(getActivity().getApplicationContext(), parentView);
        } else {
            //GoToForm.goToLoginPage(getActivity());
            mActivityCallback.fragment_callback(Actions.ACTION_LOGOUT, null, (Object) null);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivityCallback = (IActivityCallback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityCallback = null;
    }

    @Override
    public void activity_callback(String actionName, Object parameter, FormActionTypes formActionTypes) {
        switch (actionName) {
            case Actions.ACTION_YEAR_MALI:
                setYearDaftar();
                break;
        }
    }

    @Override
    protected void mapViews(View v) {

        mProgressBar = (ProgressBar) v.findViewById(R.id.main_progressBar);

        // today
        TextView mainToday = (TextView) v.findViewById(R.id.main_today);
        mainToday.setText(PersianDate.getTodayWithNames());

        mDaftarMali = (TextView) v.findViewById(R.id.main_daftarMali);
        mYearMali = (TextView) v.findViewById(R.id.main_yearMali);

        // setting button
        //mBtnSetting = (LinearLayout) v.findViewById(R.id.main_button_setting);

        // pre-invoice button
        mBtnPreInvoice = (LinearLayout) v.findViewById(R.id.main_button_pre_invoice);

        // customer report button
        mBtnPersonMande = (LinearLayout) v.findViewById(R.id.main_button_customer_report);

        // stuff report button
        mBtnKalaMojoodi = (LinearLayout) v.findViewById(R.id.main_button_stuff_report);

        // sync button
        mBtnSync = (LinearLayout) v.findViewById(R.id.main_button_sync);

        // change year button
        mFrameChangeYear = (LinearLayout) v.findViewById(R.id.main_frame_change_year);
        mLabelChangeYear = (TextView) v.findViewById(R.id.main_button_change_year_label);

        // logout button
        mBtnLogout = (LinearLayout) v.findViewById(R.id.main_button_logout);

        mBtnFrame = (LinearLayout) v.findViewById(R.id.main_menu_list);
        UIUtility.setIncludedButtonsEffect(mBtnFrame, this);

        mAppInfo = (LinearLayout) v.findViewById(R.id.main_footer);
        //mAppInfoHSV = (HorizontalScrollView) v.findViewById(R.id.main_footer_scrollbar);

        mBtnConfirmList = (LinearLayout) v.findViewById(R.id.main_button_confirm_list);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick " + v.toString());

        // check for unselected year mali
        if ((Vars.YEAR == null && !v.equals(mBtnLogout)) || v.equals(mFrameChangeYear)) {
            // check for logout from server side
            //shouldLogoutPLL.shouldLogout(getActivity().getApplicationContext(), this, mActivityCallback);
            mActivityCallback.fragment_callback(Actions.ACTION_YEAR_MALI, null, (Object) null);

        } else if (v.equals(mBtnLogout)) {
            mActivityCallback.fragment_callback(Actions.ACTION_LOGOUT, null, (Object) null);

        } else if (v.equals(mBtnPreInvoice)) {
            Log.d(TAG, Actions.ACTION_PRE_INVOICE + " called");
            mActivityCallback.fragment_callback(Actions.ACTION_PRE_INVOICE, null, (Object) null);

        } else if (v.equals(mBtnSync)) {
            Log.d(TAG, Actions.ACTION_SYNC + " called");
            mActivityCallback.fragment_callback(Actions.ACTION_SYNC, null, (Object) null);

        } else if (v.equals(mBtnConfirmList)) {
            Log.d(TAG, Actions.ACTION_CONFIRM_LIST + " called");
            mActivityCallback.fragment_callback(Actions.ACTION_CONFIRM_LIST, null, (Object) null);

        } else if (v.equals(mBtnKalaMojoodi)) {
            Log.d(TAG, Actions.ACTION_KALA_MOJOODI_LIST + " called");
            mActivityCallback.fragment_callback(Actions.ACTION_KALA_MOJOODI_LIST, null, (Object) null);

        } else if (v.equals(mBtnPersonMande)) {
            Log.d(TAG, Actions.ACTION_PERSON_MANDE_LIST + " called");
            mActivityCallback.fragment_callback(Actions.ACTION_PERSON_MANDE_LIST, null, (Object) null);

        }
    }

    //endregion fragment

    //region permission
    private static void setUserPermission(Context context, View parent) {
        UserBLL userBLL = new UserBLL(context);
        Vars.PERMISSIONS = userBLL.getUserPermissions(Vars.USER.getUserId());
        for (Map.Entry<String, UserPermissionModel> entry : Vars.PERMISSIONS.entrySet()) {
            UserPermissionModel p = entry.getValue();
            View v = parent.findViewWithTag(p.getPart());
            if (v != null) {
                Log.d(TAG, "part: " + p.getPart() + " - access: " + p.isAccess() + " - write: " + p.isWrite());
                //v.setEnabled(p.isAccess());
                v.setVisibility(p.isAccess() ? View.VISIBLE : View.GONE);
                Log.d(TAG,  "visible: " + v.getVisibility() + ", tag: " + v.getTag());

                /*
                if (!v.isEnabled()) {
                    v.setAlpha(0.2f);
                    View captionTextView = UIUtility.getNextView(v);
                    if (captionTextView != null) {
                        captionTextView.setAlpha(0.2f);
                    }
                }*/
            }
        }
    }
    //endregion permission

    private void setYearDaftar() {

        mDaftarMali.setText(
//                PersianConvert.ConvertNumbersToPersian(
//                        String.valueOf(Setting.YEAR != null ? Setting.YEAR.getDaftar() : "")
//                )
                String.valueOf(Vars.YEAR != null ? Vars.YEAR.getDaftar() : "")
        );

        mYearMali.setText(
//                PersianConvert.ConvertNumbersToPersian(
//                        String.valueOf(Setting.YEAR != null ? Setting.YEAR.getYear() : 0)
//                )
                String.valueOf(Vars.YEAR != null ? Vars.YEAR.getYear() : 0)
        );

        mLabelChangeYear.setText(mYearMali.getText());
    }

    // region IAsyncForm

    //private int mAppInfoScrollX = 0;
    @Override
    public void startProgress() {
        //Log.d(TAG, "AppInfo Scroll: " + mAppInfoHSV.getScrollX() + " - " + mAppInfoHSV.getScrollY());
        //mAppInfoScrollX = mAppInfoHSV.getScrollX();

        mAppInfo.setVisibility(View.GONE);
        UIUtility.setEnableForAll(mBtnFrame, false);
        super.startProgress();
    }

    @Override
    public void stopProgress() {
        mAppInfo.setVisibility(View.VISIBLE);

        UIUtility.setEnableForAll(mBtnFrame, true);
        super.stopProgress();

        //mAppInfoHSV.scrollTo(mAppInfoScrollX, 0);
        //Log.d(TAG, "AppInfo Scroll: " +  mAppInfoHSV.getScrollX() + " - " + mAppInfoHSV.getScrollY());
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }



}
