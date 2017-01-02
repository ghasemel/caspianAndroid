package ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import info.elyasi.android.elyasilib.Dialogs.DialogResult;
import info.elyasi.android.elyasilib.Dialogs.IDialogCallback;
import info.elyasi.android.elyasilib.Dialogs.ProgressDialog;
import info.elyasi.android.elyasilib.UI.AAsyncTask;
import info.elyasi.android.elyasilib.UI.ActivityFragmentExt;
import info.elyasi.android.elyasilib.UI.IAsyncForm;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.PersonBLL;
import ir.caspiansoftware.caspianandroidapp.Models.PersonModel;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 8/19/2016.
 */
public class MandePLL {

    private static final String TAG = "MandePLL";
    private Context mContext;
    private IAsyncForm mAsyncForm;
    private IFragmentCallback mFragmentCallback;
    //private ProgressDialog mProgressDialog;
    private boolean mCancel = false;
    private PersonModel mPerson;

    public MandePLL(Context context, IAsyncForm fragment, IFragmentCallback fragmentCallback) {
        mContext = context;
        mAsyncForm = fragment;
        mFragmentCallback = fragmentCallback;
    }

    public void start(PersonModel person) {
        Log.d(TAG, "start()");
        if (person != null) {
            if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
                ((ActivityFragmentExt) mAsyncForm.getActivity()).LockScreenRotation();
            }

            mPerson = person;
            Log.d(TAG, "personCode: " + person.getCode());
            mAsyncForm.startProgress();

            mAsyncForm.messageBoxYesNo(
                    R.string.person_mande_title,
                    String.format(
                            mContext.getString(R.string.person_mande_question),
                            person.getName()
                    ),
                    new MandeDialogCallBack());

        }
    }

    private class MandeDialogCallBack implements IDialogCallback<Integer> {
        @Override
        public void dialog_callback(DialogResult dialogResult, Integer result, int requestCode) {
            if (dialogResult != DialogResult.Yes) {
                mAsyncForm.stopProgress();
                if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
                    ((ActivityFragmentExt) mAsyncForm.getActivity()).UnLockScreenRotation();
                }
                mFragmentCallback.activity_callback(Actions.ACTION_GET_MANDE, mPerson.getMande(), null);
                return;
            }

//            mProgressDialog = new ProgressDialog();
//            mProgressDialog.setTitle(mContext.getString(R.string.person_mande_title));
//            mProgressDialog.setMax(1);
//            mProgressDialog.setDialogCallback(new ProgressDialogCallback());
//            //mProgressDialog.setAutoClose(false);
//            mProgressDialog.show(mAsyncForm.getActivity().getFragmentManager(), "send_PFaktor");


            class RunAsync extends AAsyncTask<Void, String, Double> {

                public RunAsync(ProgressBar progressBar) {
                    super(progressBar);
                }

                @Override
                protected void onProgressUpdate(String... values) {
                    super.onProgressUpdate(values);

//                    mProgressDialog.performStep(values[0]);
                }

                @Override
                protected Double doInBackground(Void... voids) {
                    Log.d(TAG, "doInBackground(): entered the function");

                    PersonBLL personBLL = new PersonBLL(mContext);

                    double mande = 0;
                    try {
                        mande = personBLL.FetchPersonMande(Vars.YEAR.getDataBase(), mPerson.getCode());
                        publishProgress(String.valueOf(mande));
                    } catch (Exception ex) {
                        setException(ex);
                    }

                    return mande;
                }

                @Override
                protected void onPostExecute(Double mande) {
                    Log.d(TAG, "onPostExecute(): entered the function");
                    mAsyncForm.stopProgress();
//                    mProgressDialog.Close();
//
                    if (isException()) {
//                        mProgressDialog.Close();
                        mAsyncForm.showError(getException(), null);
                    } else {
                        mFragmentCallback.activity_callback(Actions.ACTION_GET_MANDE, mande, null);
                    }


                    if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
                        ((ActivityFragmentExt) mAsyncForm.getActivity()).UnLockScreenRotation();
                    }
                }
            }


            if (Vars.USER != null) {
                RunAsync runAsync = new RunAsync(mAsyncForm.getProgressBar());
                runAsync.execute();
            }
        }
    }

}
