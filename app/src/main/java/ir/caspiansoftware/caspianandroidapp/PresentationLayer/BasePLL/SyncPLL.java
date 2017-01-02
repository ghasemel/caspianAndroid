package ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.List;

import info.elyasi.android.elyasilib.Constant;
import info.elyasi.android.elyasilib.UI.AAsyncTask;
import info.elyasi.android.elyasilib.UI.ActivityFragmentExt;
import info.elyasi.android.elyasilib.Dialogs.DialogResult;
import info.elyasi.android.elyasilib.UI.IActivityCallback;
import info.elyasi.android.elyasilib.Dialogs.IDialogCallback;
import info.elyasi.android.elyasilib.Dialogs.ProgressDialog;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.KalaBLL;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.PersonBLL;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.UserBLL;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Setting;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 6/22/2016.
 */
public class SyncPLL {
    private static final String TAG = "SyncPLL";
    private Context mContext;
    private CaspianFragment mAsyncForm;
    private IActivityCallback mActivityCallback;
    private int mCountKala;
    private int mCountPerson;
    private ProgressDialog mProgressDialog;
    private boolean mCancel = false;


    public SyncPLL(Context context, CaspianFragment fragment, IActivityCallback activityCallback) {
        mContext = context;
        mAsyncForm = fragment;
        mActivityCallback = activityCallback;
    }

    private void sync() {
        Log.d(TAG, "startSync called");
        mAsyncForm.startProgress();

        class RunAsync extends AAsyncTask<Void, String, String> {

            public RunAsync(ProgressBar progressBar) {
                super(progressBar);
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);

                mProgressDialog.performStep(values[0]);
            }

            @Override
            protected String doInBackground(Void... objects) {
                Log.d(TAG, "doInBackground(): entered the function");

//                try {
//                    UserBLL userBLL = new UserBLL(mContext);
//                    if (userBLL.checkForLogout(Vars.USER.getUserId()))
//                        return Actions.ACTION_LOGOUT;
//                } catch (Exception ex) {
//                    setException(ex);
//                    return null;
//                }


                if (mCancel) return Constant.CANCEL;
                if (mCountKala > 0) {
                    try {
                        KalaBLL kalaBLL = new KalaBLL(mContext);
                        List<KalaModel> kalaList = kalaBLL.FetchKalaList(Vars.YEAR.getDataBase());

                        kalaBLL.DeleteNotExistInList(kalaList);
                        for (int i = 0; i < kalaList.size(); i++) {
                            if (mCancel) return Constant.CANCEL;
                            kalaBLL.SyncWithDatabase(kalaList.get(i));
                            publishProgress(kalaList.get(i).getName());
                        }

                        //return kalaList.size() + "-3";
                    } catch (Exception ex) {
                        Log.d(TAG, "kala sync exception");
                        setException(ex);
                    }
                }

                if (mCancel) return Constant.CANCEL;

                if (mCountPerson > 0) {
                    try {
                        PersonBLL personBLL = new PersonBLL(mContext);
                        List<PersonModel> personList = personBLL.FetchPersonList(Vars.YEAR.getDataBase());

                        personBLL.DeleteNotExistInList(personList);

                        for (int i = 0; i < personList.size(); i++) {
                            if (mCancel) return Constant.CANCEL;
                            personBLL.SyncWithDatabase(personList.get(i));
                            publishProgress(personList.get(i).getName());
                        }

                        //return kalaList.size() + "-3";
                    } catch (Exception ex) {
                        Log.d(TAG, "person sync exception");
                        setException(ex);
                    }
                }


                Log.d(TAG, "doInBackground(): end of the function");
                return Constant.SUCCESS;
            }

            @Override
            protected void onPostExecute(String response) {
                Log.d(TAG, "onPostExecute(): entered the function");
                if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
                    ((ActivityFragmentExt) mAsyncForm.getActivity()).UnLockScreenRotation();
                }

                mAsyncForm.stopProgress();

                if (isException()) {
                    mAsyncForm.showError(getException(), null);
                } else {
                    Log.d(TAG, "sync - " + response);

//                    if (response.equals(Actions.ACTION_LOGOUT)) {
//                        mActivityCallback.fragment_callback(Actions.ACTION_LOGOUT, null, null);

                    //} else
                    if (response.equals(Constant.SUCCESS)) {
                        mAsyncForm.messageBoxOK(R.string.sync_title,
                                String.format(
                                        mContext.getString(R.string.sync_success_message),
                                        mCountKala,
                                        mCountPerson
                                ), null);
                    }
                }
                Log.d(TAG, "onPostExecute(): end of the function");
            }
        }

        if (Vars.USER != null) {
            RunAsync runAsync = new RunAsync(mAsyncForm.getProgressBar());
            runAsync.execute();
        }
    }


    public void start() {
        Log.d(TAG, "start called");
        mAsyncForm.startProgress();
        if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
            ((ActivityFragmentExt) mAsyncForm.getActivity()).LockScreenRotation();
        }


        class RunAsync extends AAsyncTask<Void, Integer, int[]> {


            @Override
            protected int[] doInBackground(Void... objects) {
                Log.d(TAG, "doInBackground(): entered the function");


                int kala_count = 0;
                try {
                    KalaBLL kalaBLL = new KalaBLL(mContext);
                    kala_count = kalaBLL.FetchKalaListCount(Vars.YEAR.getDataBase());
                } catch (Exception ex) {
                    setException(ex);
                }

                int person_count = 0;
                try {
                    PersonBLL personBLL = new PersonBLL(mContext);
                    person_count = personBLL.FetchPersonListCount(Vars.YEAR.getDataBase());

                } catch (Exception ex) {
                    setException(ex);
                }

                Log.d(TAG, "doInBackground(): end of the function");
                return new int[] { kala_count, person_count };
            }

            @Override
            protected void onPostExecute(int[] response) {
                Log.d(TAG, "onPostExecute(): entered the function");
                mAsyncForm.stopProgress();

                if (isException()) {
                    mAsyncForm.showError(getException(), null);
                } else {
                    Log.d(TAG, "startSync - " + response[0] + "-" + response[1]);
                    if (response.length == 2) {
                        mCountKala = response[0];
                        mCountPerson = response[1];

                        if (mCountKala + mCountPerson > 0) {
                            mAsyncForm.messageBoxYesNo(R.string.sync_title,
                                    String.format(mContext.getString(R.string.sync_question), response[0], response[1]),
                                    new count_dialog_callback());
                        } else {
                            if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
                                ((ActivityFragmentExt) mAsyncForm.getActivity()).UnLockScreenRotation();
                            }

                            mAsyncForm.messageBoxOK(R.string.sync_title,
                                    mContext.getString(R.string.sync_not_exist_message),
                                    null);
                        }
                    }
                }
                Log.d(TAG, "onPostExecute(): end of the function");
            }
        }

        if (Vars.USER != null) {
            RunAsync runAsync = new RunAsync();
            runAsync.execute();
        }



    }

    class count_dialog_callback implements IDialogCallback<Object> {

        @Override
        public void dialog_callback(DialogResult dialogResult, Object result, int requestCode) {
            if (dialogResult == DialogResult.Yes) {
                mProgressDialog = new ProgressDialog();
                mProgressDialog.setTitle(mContext.getString(R.string.sync_title));
                mProgressDialog.setMax(mCountKala + mCountPerson);
                mProgressDialog.setDialogCallback(new ProgressDialogCallback());

                //mContext.getResources().getInteger(R.integer.popup_width),
                //mContext.getResources().getInteger(R.integer.popup_height));

                mProgressDialog.show(mAsyncForm.getActivity().getFragmentManager(), "sync_kal_person");

                sync();
            } else {
                if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
                    ((ActivityFragmentExt) mAsyncForm.getActivity()).UnLockScreenRotation();
                }
            }
        }
    }

    class ProgressDialogCallback implements IDialogCallback<Integer> {
        @Override
        public void dialog_callback(DialogResult dialogResult, Integer result, int requestCode) {
            if (dialogResult == DialogResult.Cancel) {
                mCancel = true;
            }
        }
    }

}
