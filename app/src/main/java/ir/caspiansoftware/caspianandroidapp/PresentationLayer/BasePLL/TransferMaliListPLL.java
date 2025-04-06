package ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.List;

import info.elyasi.android.elyasilib.Constant;
import info.elyasi.android.elyasilib.Dialogs.DialogResult;
import info.elyasi.android.elyasilib.Dialogs.IDialogCallback;
import info.elyasi.android.elyasilib.Dialogs.ProgressDialog;
import info.elyasi.android.elyasilib.UI.AAsyncTask;
import info.elyasi.android.elyasilib.UI.ActivityFragmentExt;
import info.elyasi.android.elyasilib.UI.FormActionType;
import info.elyasi.android.elyasilib.UI.IActivityCallback;
import info.elyasi.android.elyasilib.UI.IAsyncForm;
import ir.caspiansoftware.caspianandroidapp.Actions;
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.MaliBLL;
import ir.caspiansoftware.caspianandroidapp.Models.MaliModel;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 8/3/2016.
 */
public class TransferMaliListPLL {
    private static final String TAG = "TransferMaliListPLL";
    private Context mContext;
    private IAsyncForm mAsyncForm;
    private IActivityCallback mActivityCallback;
    private ProgressDialog mProgressDialog;
    private boolean mCancel = false;
    private List<MaliModel> maliModels;


    public TransferMaliListPLL(Context context, IAsyncForm fragment, IActivityCallback activityCallback) {
        mContext = context;
        mAsyncForm = fragment;
        mActivityCallback = activityCallback;
    }

    public void start(final List<MaliModel> maliModels) {
        Log.d(TAG, "start()");
        if (maliModels != null && !maliModels.isEmpty()) {
            if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
                ((ActivityFragmentExt) mAsyncForm.getActivity()).LockScreenRotation();
            }

            this.maliModels = maliModels;
            Log.d(TAG, "maliModels.size(): " + maliModels.size());
            mAsyncForm.startProgress();


            mAsyncForm.messageBoxYesNo(
                    R.string.mali_send_list_to_server_title,
                    String.format(
                            mContext.getString(R.string.mali_send_list_question),
                            String.valueOf(maliModels.size())
                    ),
                    new DoSendingDialogCallBack());

        }
    }


    class DoSendingDialogCallBack implements IDialogCallback<Integer> {
        @Override
        public void dialog_callback(DialogResult dialogResult, Integer result, int requestCode) {
            if (dialogResult != DialogResult.Yes) {
                mAsyncForm.stopProgress();
                mActivityCallback.onMyFragmentCallBack(Actions.ACTION_TRANSFER_CANCELED, FormActionType.CANCEL);
                return;
            }

            mProgressDialog = new ProgressDialog();
            mProgressDialog.setTitle(mContext.getString(R.string.mali_send_list_to_server_title));
            mProgressDialog.setMax(maliModels.size());
            mProgressDialog.setDialogCallback(new ProgressDialogCallback());
            mProgressDialog.setAutoClose(false);
            mProgressDialog.show(mAsyncForm.getActivity().getFragmentManager(), "send_Mali");

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

                    MaliBLL maliBLL = new MaliBLL(mContext);


                    if (mCancel)
                        return Constant.CANCEL;

                    if (!maliModels.isEmpty()) {
                        Log.d(TAG, "list.size(): " + maliModels.size());
                        try {
                            reportProgress(mContext.getString(R.string.sending_to_server));
                            maliBLL.sendMaliInfoToServer(maliModels);
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
                            mActivityCallback.onMyFragmentCallBack(Actions.ACTION_TRANSFER_PFaktor_DONE, null, (Object) null);
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
