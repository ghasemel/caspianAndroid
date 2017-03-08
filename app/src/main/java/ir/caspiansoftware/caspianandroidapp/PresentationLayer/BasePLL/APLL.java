package ir.caspiansoftware.caspianandroidapp.PresentationLayer.BasePLL;

import android.content.Context;
import android.widget.ProgressBar;

import info.elyasi.android.elyasilib.Dialogs.DialogResult;
import info.elyasi.android.elyasilib.Dialogs.IDialogCallback;
import info.elyasi.android.elyasilib.UI.AAsyncTask;
import info.elyasi.android.elyasilib.UI.ActivityFragmentExt;
import info.elyasi.android.elyasilib.UI.IAsyncForm;
import info.elyasi.android.elyasilib.UI.IFragmentCallback;
import ir.caspiansoftware.caspianandroidapp.Vars;

/**
 * Created by Ghasem on 3/8/2017.
 */

public abstract class APLL<TModel, TResult> {
    private static final String TAG = "APLL";

    protected abstract void onCancelClick();
    protected abstract TResult doInBackgroundThread(RunAsync runAsync) throws Exception;
    protected abstract void onBackgroundComplete(TResult result);
    protected abstract int getMessageBoxTitle();
    protected abstract String getMessageBoxText();

    private Context mContext;
    private IAsyncForm mAsyncForm;
    private IFragmentCallback mFragmentCallback;
    private TModel mModel;

    APLL(Context context, IAsyncForm asyncForm, IFragmentCallback fragmentCallback) {
        mContext = context;
        mAsyncForm = asyncForm;
        mFragmentCallback = fragmentCallback;
    }

    private void LockScreen() {
        if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
            ((ActivityFragmentExt) mAsyncForm.getActivity()).LockScreenRotation();
        }
    }

    private void UnlockScreen() {
        if (mAsyncForm.getActivity() instanceof ActivityFragmentExt) {
            ((ActivityFragmentExt) mAsyncForm.getActivity()).UnLockScreenRotation();
        }
    }

    public void start(TModel model) {
        if (model != null) {
            LockScreen();

            mModel = model;
            mAsyncForm.startProgress();

            mAsyncForm.messageBoxYesNo(
                    getMessageBoxTitle(),
                    getMessageBoxText(),
                    new DialogCallBack()
            );
        }
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public IAsyncForm getAsyncForm() {
        return mAsyncForm;
    }

    public void setAsyncForm(IAsyncForm asyncForm) {
        mAsyncForm = asyncForm;
    }

    public IFragmentCallback getFragmentCallback() {
        return mFragmentCallback;
    }

    public void setFragmentCallback(IFragmentCallback fragmentCallback) {
        mFragmentCallback = fragmentCallback;
    }

    public TModel getModel() {
        return mModel;
    }

    public void setModel(TModel model) {
        mModel = model;
    }

    private class DialogCallBack implements IDialogCallback<Integer> {
        @Override
        public void dialog_callback(DialogResult dialogResult, Integer result, int requestCode) {
            if (dialogResult != DialogResult.Yes) {
                mAsyncForm.stopProgress();
                UnlockScreen();
                onCancelClick();
                return;
            }

            if (Vars.USER != null) {
                RunAsync runAsync = new RunAsync(mAsyncForm.getProgressBar());
                runAsync.execute();
            }
        }
    }


     class RunAsync extends AAsyncTask<Void, String, TResult> {
        private RunAsync(ProgressBar progressBar) {
            super(progressBar);
        }

        public void updateProgressbar(String value) {
            publishProgress(value);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected TResult doInBackground(Void... voids) {
            try {
                return doInBackgroundThread(this);
            }
            catch (Exception ex) {
                setException(ex);
                return null;
            }
        }

        @Override
        protected void onPostExecute(TResult tResult) {
            mAsyncForm.stopProgress();

            if (isException()) {
                mAsyncForm.showError(getException(), null);
            } else {
                onBackgroundComplete(tResult);
            }

            UnlockScreen();
        }
    }
}
