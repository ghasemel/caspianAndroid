package ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.Constant;
import info.elyasi.android.elyasilib.Dialogs.DialogResult;
import info.elyasi.android.elyasilib.Dialogs.IDialogCallback;
import info.elyasi.android.elyasilib.Dialogs.ProgressDialog;
import info.elyasi.android.elyasilib.UI.AAsyncTask;
import info.elyasi.android.elyasilib.UI.ActivityFragmentExt;
import info.elyasi.android.elyasilib.UI.IActivityCallback;
import info.elyasi.android.elyasilib.UI.IAsyncForm;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianFragment;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.PFaktorBLL;
import ir.caspiansoftware.caspianandroidapp.Models.MPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.Models.SPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Setting;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 8/3/2016.
 */
public class SendPreInvoiceListPLL {
    private static final String TAG = "SendPreInvoiceListPLL";
    private Context mContext;
    private IAsyncForm mAsyncForm;
    private IActivityCallback mActivityCallback;
    private ProgressDialog mProgressDialog;
    private boolean mCancel = false;
    private List<MPFaktorModel> mMPFaktorModelList;


    public SendPreInvoiceListPLL(Context context, IAsyncForm fragment, IActivityCallback activityCallback) {
        mContext = context;
        mAsyncForm = fragment;
        mActivityCallback = activityCallback;
    }

    public void start(final List<MPFaktorModel> mpFaktorModelList) {
        Log.d(TAG, "start()");
        if (mpFaktorModelList != null && mpFaktorModelList.size() > 0) {
            if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
                ((ActivityFragmentExt) mAsyncForm.getActivity()).LockScreenRotation();
            }

            mMPFaktorModelList = mpFaktorModelList;
            Log.d(TAG, "mpFaktorModelList.size(): " + mpFaktorModelList.size());
            mAsyncForm.startProgress();


            mAsyncForm.messageBoxYesNo(
                    R.string.pfaktor_send_list_to_server_title,
                    String.format(
                            mContext.getString(R.string.pfaktor_send_list_question),
                            String.valueOf(mpFaktorModelList.size())
                    ),
                    new DoSendingDialogCallBack());

        }
    }


    class DoSendingDialogCallBack implements IDialogCallback<Integer> {
        @Override
        public void dialog_callback(DialogResult dialogResult, Integer result, int requestCode) {
            if (dialogResult != DialogResult.Yes) {
                mAsyncForm.stopProgress();
                return;
            }

            //if (dialogResult == DialogResult.Yes) {
            mProgressDialog = new ProgressDialog();
            mProgressDialog.setTitle(mContext.getString(R.string.pfaktor_send_list_to_server_title));
            mProgressDialog.setMax(mMPFaktorModelList.size());
            mProgressDialog.setDialogCallback(new ProgressDialogCallback());
            mProgressDialog.setAutoClose(false);
            mProgressDialog.show(mAsyncForm.getActivity().getFragmentManager(), "send_PFaktor");


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
                protected String doInBackground(Void... voids) {
                    Log.d(TAG, "doInBackground(): entered the function");

                    PFaktorBLL faktorBLL = new PFaktorBLL(mContext);

                    List<MPFaktorModel> list = new ArrayList<>();
                    for (MPFaktorModel mpFaktor : mMPFaktorModelList) {
                        ArrayList<SPFaktorModel> spList = faktorBLL.getSPfaktorListByMPFaktorId(mpFaktor.getId());
                        if (spList != null) {
                            Log.d(TAG, "spList.size(): " + spList.size());
                            mpFaktor.setSPFaktorList(spList);
                            list.add(mpFaktor);
                            publishProgress("" + mpFaktor.getNum());
                        } else {

                        }

                        if (mCancel)
                            return Constant.CANCEL;
                    }

                    if (list.size() > 0) {
                        Log.d(TAG, "list.size(): " + list.size());
                        try {
                            publishProgress(mContext.getString(R.string.sending_to_server));
                            faktorBLL.sendMPFaktorToServer(list);
                        } catch (Exception ex) {
                            setException(ex);
                        }
                    } else {
                        return Constant.FAILED;
                    }

                    return Constant.SUCCESS;
                }

                @Override
                protected void onPostExecute(String result) {
                    Log.d(TAG, "onPostExecute(): entered the function");
                    mAsyncForm.stopProgress();
                    mProgressDialog.Close();

                    if (isException()) {
                        mProgressDialog.Close();
                        mAsyncForm.showError(getException(), null);
                    } else {
                        if (result.equals(Constant.SUCCESS)) {
                            mActivityCallback.fragment_callback(Actions.ACTION_CONFIRM_PFaktor_DONE, null, (Object) null);
                        } else if (result.equals(Constant.FAILED)) {
                            mAsyncForm.showError(mContext.getString(R.string.pfaktor_is_empty), null);
                        }
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
            //}
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
