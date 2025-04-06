package info.elyasi.android.elyasilib.UI;

import android.os.AsyncTask;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;

/**
 * Created by Canada on 6/20/2016.
 */
public abstract class AAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private WeakReference<ProgressBar> mBar;
    private Exception mException;

    public AAsyncTask() {
        super();
    }

    public AAsyncTask(ProgressBar progressBar) {
        this();
        mBar = new WeakReference<>(progressBar);
    }

    public ProgressBar getBar() {
        return mBar != null ? mBar.get() : null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mException = null;
    }

    @SafeVarargs
    public final void reportProgress(Progress... values) {
        publishProgress(values);
    }


    public Exception getException() {
        return mException;
    }

    public boolean isException() {
        return mException != null;
    }

    public void setException(Exception exception) {
        mException = exception;
    }
}
