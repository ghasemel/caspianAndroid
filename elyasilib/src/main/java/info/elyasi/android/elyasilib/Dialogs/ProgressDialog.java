package info.elyasi.android.elyasilib.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import info.elyasi.android.elyasilib.Persian.PersianConvert;
import info.elyasi.android.elyasilib.R;


/**
 * Created by Canada on 7/13/2016.
 */
public class ProgressDialog extends ADialogFragment<Integer> {


    private ProgressBar mProgressBar;
    private TextView mMinTextView;
    private TextView mMaxTextView;
    private TextView mDescriptionTextView;
    private String mTitle;
    private boolean mAutoClose = true;

    private int mMax;


    @Override
    protected Dialog createDialog() {
        return new AlertDialog.Builder(getActivity())
                .setTitle(getTitle())
                .setView(getLayoutView())
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (getDialogCallback() != null) {
                            getDialogCallback().dialog_callback(DialogResult.Cancel, -1, getRequestCode());
                        }
                    }
                })
                .create();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_progress;
    }

    @Override
    protected void mapViews(View parentView) {
        mProgressBar = (ProgressBar) parentView.findViewById(R.id.progressing_progressbar);
        mProgressBar.setMax(mMax);
        mProgressBar.setProgress(0);

        //Drawable drawable = getResources().getDrawable(R.drawable.progressbar);
        //mProgressBar.setProgressDrawable(drawable);

        mMinTextView = (TextView) parentView.findViewById(R.id.progressing_minTextView);
        mMinTextView.setText("0");
        //mMinTextView.setText(PersianConvert.ConvertNumbersToPersian("0"));

        mMaxTextView = (TextView) parentView.findViewById(R.id.progressing_maxTextView);
        mMaxTextView.setText(String.valueOf(mMax));
        //mMaxTextView.setText(PersianConvert.ConvertNumbersToPersian(String.valueOf(mMax)));

        mDescriptionTextView = (TextView) parentView.findViewById(R.id.progressing_descriptionTextView);
    }

    public void performStep(String description) {
        int step = mProgressBar.getProgress() + 1;
        mProgressBar.setProgress(step);
        //mMinTextView.setText(PersianConvert.ConvertDigitsToPersian(String.valueOf(step)));
        mMinTextView.setText(String.valueOf(step));

        mDescriptionTextView.setText(description);

        if (isAutoClose() && step == mMax) {
            dismiss();
        }
    }

    public void Close()
    {
        dismiss();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }


    public int getMax() {
        return mMax;
    }

    public void setMax(int max) {
        mMax = max;
    }


    public boolean isAutoClose() {
        return mAutoClose;
    }

    public void setAutoClose(boolean autoClose) {
        mAutoClose = autoClose;
    }
}
