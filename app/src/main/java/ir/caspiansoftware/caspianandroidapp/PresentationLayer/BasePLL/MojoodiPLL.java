package ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import info.elyasi.android.elyasilib.Dialogs.DialogResult;
import info.elyasi.android.elyasilib.Dialogs.IDialogCallback;
import info.elyasi.android.elyasilib.UI.AAsyncTask;
import info.elyasi.android.elyasilib.UI.ActivityFragmentExt;
import info.elyasi.android.elyasilib.UI.IAsyncForm;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.KalaBLL;
import ir.caspiansoftware.caspianandroidapp.Models.KalaModel;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 8/19/2016.
 */
public class MojoodiPLL {

    private static final String TAG = "MojoodiPLL";
    private Context mContext;
    private IAsyncForm mAsyncForm;
    private IFragmentCallback mFragmentCallback;
    //private ProgressDialog mProgressDialog;
    private boolean mCancel = false;
    private KalaModel mKala;

    public MojoodiPLL(Context context, IAsyncForm fragment, IFragmentCallback fragmentCallback) {
        mContext = context;
        mAsyncForm = fragment;
        mFragmentCallback = fragmentCallback;
    }

    public void start(KalaModel kala) {
        Log.d(TAG, "start()");
        if (kala != null) {
            if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
                ((ActivityFragmentExt) mAsyncForm.getActivity()).LockScreenRotation();
            }

            mKala = kala;
            Log.d(TAG, "kalaCode: " + kala.getCode());
            mAsyncForm.startProgress();

            mAsyncForm.messageBoxYesNo(
                    R.string.kala_moj_title,
                    String.format(
                            mContext.getString(R.string.kala_moj_question),
                            kala.getName()
                    ),
                    new MojoodiDialogCallBack());

        }
    }

    private class MojoodiDialogCallBack implements IDialogCallback<Integer> {
        @Override
        public void dialog_callback(DialogResult dialogResult, Integer result, int requestCode) {
            if (dialogResult != DialogResult.Yes) {
                mAsyncForm.stopProgress();
                if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
                    ((ActivityFragmentExt) mAsyncForm.getActivity()).UnLockScreenRotation();
                }
                mFragmentCallback.activity_callback(Actions.ACTION_GET_MOJOODI, mKala.getMojoodi(), null);
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

                    KalaBLL kalaBLL = new KalaBLL(mContext);

                    double mande = 0;
                    try {
                        mande = kalaBLL.FetchKalaMojoodi(Vars.YEAR.getDataBase(), mKala.getCode());
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
                        mFragmentCallback.activity_callback(Actions.ACTION_GET_MOJOODI, mande, null);
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
