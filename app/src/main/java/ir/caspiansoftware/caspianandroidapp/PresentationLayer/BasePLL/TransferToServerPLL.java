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
import ir.caspiansoftware.caspianandroidapp.BusinessLayer.TransferToServerService;
import ir.caspiansoftware.caspianandroidapp.R;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Canada on 8/3/2016.
 */
public class TransferToServerPLL<T> {
    private static final String TAG = "TransferToServerPLL";
    private final Context mContext;
    private final IAsyncForm mAsyncForm;
    private final IActivityCallback mActivityCallback;
    private ProgressDialog mProgressDialog;
    private boolean mCancel = false;
    private List<T> dataModels;

    private final String actionName;

    private final TransferToServerService<T> transferToServerService;


    public TransferToServerPLL(Context context, IAsyncForm fragment, IActivityCallback activityCallback, TransferToServerService<T> transferToServerService, String actionName) {
        mContext = context;
        mAsyncForm = fragment;
        mActivityCallback = activityCallback;
        this.transferToServerService = transferToServerService;
        this.actionName = actionName;
    }

    public void start(final List<T> maliModels) {
        Log.d(TAG, "start()");
        if (maliModels != null && !maliModels.isEmpty()) {
            if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
                ((ActivityFragmentExt) mAsyncForm.getActivity()).LockScreenRotation();
            }

            this.dataModels = maliModels;
            Log.d(TAG, "maliModels.size(): " + maliModels.size());
            mAsyncForm.startProgress();


            mAsyncForm.messageBoxYesNo(
                    R.string.transfer_to_server_title,
                    String.format(
                            mContext.getString(R.string.transfer_to_server_question),
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
                mActivityCallback.onMyFragmentCallBack(actionName, FormActionType.CANCEL);
                return;
            }

            mProgressDialog = new ProgressDialog();
            mProgressDialog.setTitle(mContext.getString(R.string.transfer_to_server_title));
            mProgressDialog.setMax(dataModels.size());
            mProgressDialog.setDialogCallback(new ProgressDialogCallback());
            mProgressDialog.setAutoClose(false);
            mProgressDialog.show(mAsyncForm.getActivity().getFragmentManager(), "transfer_to_server");

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

                    if (mCancel)
                        return Constant.CANCEL;

                    if (!dataModels.isEmpty()) {
                        Log.d(TAG, "list.size(): " + dataModels.size());
                        try {
                            reportProgress(mContext.getString(R.string.sending_to_server));
                            transferToServerService.sendToServer(dataModels);
                        } catch (Exception ex) {
                            setException(ex);
                            return Constant.FAILED;
                        }
                    } else {
                        setException(new RuntimeException((mContext.getString(R.string.transfer_to_server_fail))));
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
                        mAsyncForm.showError(getException().getMessage(), null);
                        mActivityCallback.onMyFragmentCallBack(actionName, FormActionType.FAILED, (Object) null);
                    } else {
                        if (result.equals(Constant.SUCCESS)) {
                            mActivityCallback.onMyFragmentCallBack(actionName, FormActionType.DONE, (Object) null);
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
