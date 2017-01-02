package ir.caspiansoftware.caspianandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import info.elyasi.android.elyasilib.Dialogs.DialogResult;
import info.elyasi.android.elyasilib.Dialogs.IDialogCallback;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianErrors;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.ErrorExt;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.GoToForm;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.InitialSettingBLL;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.UserBLL;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.YearMaliBLL;
import ir.caspiansoftware.caspianandroidapp.DataLayer.DataBase.CaspianDataBaseHelper;
import ir.caspiansoftware.caspianandroidapp.Models.UserModel;
import info.elyasi.android.elyasilib.UI.IPauseUI;
import info.elyasi.android.elyasilib.UI.UIUtility;

/**
 * Created by Canada on 1/6/2016.
 */
public class FirstFragment extends CaspianFragment implements IPauseUI {
    private static final String TAG = "FirstFragment";

    private static final String EXTRA_TO_FOREGROUND = "to_foreground";
    public static final String EXTRA_EXIT = "exit";
    private boolean mOnCreate = false;


    @Override
    protected void afterOnCreate() {
        mOnCreate = true;
        UIUtility.waitForSeconds(Setting.FirstActivityDelay, this);

        // recreate database
        if (Setting.RECREATE_DATABASE) {
            CaspianDataBaseHelper.DropDataBase(getActivity().getApplicationContext());
            Vars.USER = null;
            Vars.YEAR = null;
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_first;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mOnCreate)
            checkOnExecute();

        mOnCreate = false;
    }


    public static void backToForegroundIsValid(Activity activity) {
        Intent i = new Intent(activity.getApplicationContext(), FirstActivity.class);
        i.putExtra(EXTRA_TO_FOREGROUND, true);
        activity.startActivityForResult(i, 0);
    }

    public void Run() {
        checkOnExecute();
        mOnCreate = false;
    }

    public void checkOnExecute() {
        boolean alreadyLogin = false;

        try {
            ErrorExt.create(getActivity().getApplicationContext());

            Intent currentActivity = getActivity().getIntent();
            if (currentActivity.hasExtra(EXTRA_EXIT)) {
                // close app
                getActivity().finish();
                return;
            }


            // create InitialSettingBLL object
            InitialSettingBLL.create(getActivity().getApplicationContext());

            // load Initial settings
            if (InitialSettingBLL.load() == null) {
                // go to initial setting form
                GoToForm.goToInitialSettingPage(getActivity());
                return;
            }



//            // check for database version and app database version
//            if (!InitialSettingBLL.isDBVersionCorrect(Setting.APP_DB_VERSION)) {
//                Setting.RECREATE_DATABASE = true;
//            }
//
//            // recreate database
//            if (Setting.RECREATE_DATABASE) {
//                CaspianDataBaseHelper.DropDataBase(getActivity().getApplicationContext());
//                return;
//            }

            UserBLL userBLL = new UserBLL(getActivity().getApplicationContext());
            UserModel userModel = userBLL.getLogonUser();
            if (userModel != null) {
                alreadyLogin = true;
                Vars.USER = userModel;

                YearMaliBLL yearMaliBLL = new YearMaliBLL(getActivity().getApplicationContext());
                Vars.YEAR = yearMaliBLL.getCurrentYearMali(userModel.getUserId());


            }


            // check for userTbl already login
            if (alreadyLogin) {
                // check with server for userTbl validity
                boolean active = userModel.isActive();

                // check validity
                if (active) {
                    if (currentActivity.hasExtra(EXTRA_TO_FOREGROUND)) {
                        // request for coming to foreground from an activity
                        // this will be a call from onRestart() method of the activity
                        getActivity().finish();
                    } else {
                        // it is new running request
                        GoToForm.goToMainPage(getActivity());
                    }
                } else {
                    GoToForm.goToLoginPage(getActivity());
                }
            } else {
                // go to login page
                GoToForm.goToLoginPage(getActivity());
            }
        } catch (final Exception ex) {
            //showError(ex.getMessage());
            Log.d(TAG, ex.getMessage());
            showError(ex, new IDialogCallback() {
                @Override
                public void dialog_callback(DialogResult dialogResult, Object result, int requestCode) {
                    if (getActivity() != null)
                        getActivity().finish();
                }
            });
            //throw ex;
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public ProgressBar getProgressBar() {
        return null;
    }

    @Override
    public void mapViews(View parentView) {

    }
}
