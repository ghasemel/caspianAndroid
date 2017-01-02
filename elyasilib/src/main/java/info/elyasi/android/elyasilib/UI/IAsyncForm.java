package info.elyasi.android.elyasilib.UI;


import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;

import info.elyasi.android.elyasilib.Dialogs.IDialogCallback;

/**
 * Created by Canada on 2/12/2016.
 */
public interface IAsyncForm extends View.OnClickListener, View.OnTouchListener {

    void startProgress();
    void stopProgress();
    Activity getActivity();

    void showError(Exception ex, IDialogCallback dialogCallback);
    void showError(String errorMessage, IDialogCallback dialogCallback);

    void messageBoxOK(int title, String message, int icon, final IDialogCallback dialogCallback, boolean isLatin);
    void messageBoxOK(int title, String message, int icon, final IDialogCallback dialogCallback);
    void messageBoxOK(int title, String message, final IDialogCallback dialogCallback);
    void messageBoxYesNo(int title, String message, int icon, final IDialogCallback dialogCallback);
    void messageBoxYesNo(int title, String message, final IDialogCallback dialogCallback);
    void messageBoxYesNo(int title, int message, final IDialogCallback dialogCallback);

    void clearError();

    ProgressBar getProgressBar();
    //ProgressBar getProgressBar();
}
