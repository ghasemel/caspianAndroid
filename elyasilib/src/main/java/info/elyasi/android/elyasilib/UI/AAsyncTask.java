package info.elyasi.android.elyasilib.UI;

import android.os.AsyncTask;
import android.widget.ProgressBar;

/**
 * Created by Canada on 6/20/2016.
 */
public abstract class AAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private ProgressBar mBar;
    private Exception mException;

    public AAsyncTask() {
        super();
    }

    public AAsyncTask(ProgressBar progressBar) {
        this();
        mBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mException = null;
    }


    public ProgressBar getBar() {
        return mBar;
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
