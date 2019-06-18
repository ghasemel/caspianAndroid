package info.elyasi.android.elyasilib.UI;

import android.app.AlertDialog;


import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import info.elyasi.android.elyasilib.Dialogs.DialogResult;
import info.elyasi.android.elyasilib.Dialogs.IDialogCallback;
import info.elyasi.android.elyasilib.Persian.PersianConvert;
import info.elyasi.android.elyasilib.R;
import info.elyasi.android.elyasilib.Utility.AErrorHandler;


/**
 * Created by Canada on 2/12/2016.
 */
public abstract class AAsyncFragment extends Fragment implements IAsyncForm {
    private static final String TAG = "AAsyncFragment";

    protected abstract TextView getErrorLabel();
    protected abstract View getStarterControl();
    protected abstract void mapViews(View parentView);
    protected void afterMapViews(View parentView) {}
    protected void afterOnCreate() {}
    protected abstract AErrorHandler getErrorHandler();
    protected abstract int getLayoutId();


    private DialogResult mDialogResult = DialogResult.Cancel;
    private View mLayoutView;

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        afterOnCreate();
        //setRetainInstance(true);
    }


    protected View getLayoutView() {
        return mLayoutView;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceBundle) {
        Log.d(TAG, "onCreateView(): function entered");
        mLayoutView = inflater.inflate(getLayoutId(), parent, false);
        mapViews(mLayoutView);
        afterMapViews(mLayoutView);
        return mLayoutView;
    }

    public void startProgress() {
        clearError();
        if (getStarterControl() != null) {
            getStarterControl().setEnabled(false);
        }
        getProgressBar().setVisibility(View.VISIBLE);
    }

    public void stopProgress() {
        if (getProgressBar() != null) {
            getProgressBar().setVisibility(View.GONE);
        }
        if (getStarterControl() != null) {
            getStarterControl().setEnabled(true);
        }
    }

    /*
    public void showError(String errorMessage) {
        if (getErrorLabel() != null) {
            getErrorLabel().setVisibility(View.VISIBLE);
            getErrorLabel().setText(PersianConvert.ConvertDigitsToPersian(errorMessage));
        } else {

        }
    }

    public void showError(Exception ex) {
        if (getActivity() != null) {
            ErrorExt errorExt = ErrorExt.get(ex);
            showError(errorExt.toString());
        }
    }*/

    public void showToast(String pMessage) {
        if (getActivity() != null && getActivity().getApplicationContext() != null) {
            Toast.makeText(
                    getActivity().getApplicationContext(),
                    pMessage,
                    Toast.LENGTH_LONG)
                    .show();
        }
    }


    public void clearError() {
        if (getErrorLabel() != null) {
            getErrorLabel().setVisibility(View.GONE);
            getErrorLabel().setText("");
        }
    }

    public DialogResult getDialogResult() {
        return mDialogResult;
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        return true;
    }
 /*
    private void setOnClickTouchEffect(View contentView, boolean setForParent) {
        setOnClickTouchEffect2(contentView, -1, setForParent);
    }

    private void setOnClickTouchEffect2(View contentView, int actionbarBackButtonId, boolean setForParent) {
       for (View btn : contentView.getTouchables()) {
            Log.d(TAG, "btn id: " + btn.getErrorCode());

            // set onClick event
            if (btn.getErrorCode() != actionbarBackButtonId)
                btn.setOnClickListener(this);

            // set onTouch event
            btn.setOnTouchListener(this);

            if (setForParent) {
                if (btn.getParent() instanceof LinearLayout) {
                    Log.d(TAG, "parent id: " + ((LinearLayout) btn.getParent()).getErrorCode());
                    ((LinearLayout) btn.getParent()).setOnClickListener(this);
                    ((LinearLayout) btn.getParent()).setOnTouchListener(this);
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if (v.getBackground() instanceof ColorDrawable) {
            return UIUtility.onTouchEffect(v, motionEvent, R.color.touchColor, ((ColorDrawable) v.getBackground()).getColor());
        }
        return false;
    }



    public void setActionBarOnClickTouch2(int actionbarBackButtonId) {
        if (getActivity().getActionBar() != null) {
            View actionbarView = getActivity().getActionBar().getCustomView();
            setOnClickTouchEffect2(actionbarView, actionbarBackButtonId, false);
        }
    }*/

    public void showError(Exception ex, IDialogCallback dialogCallback) {
        getErrorHandler().setException(ex);
        showError(getErrorHandler().getUserMessage(), dialogCallback);
    }

    public void showError(String errorMessage, IDialogCallback dialogCallback) {
        messageBoxOK(R.string.error_title, errorMessage, R.drawable.error_dialog, dialogCallback, true);
    }

    public void showError(int errorMessage, IDialogCallback dialogCallback) {
        messageBoxOK(R.string.error_title, getResources().getString(errorMessage), R.drawable.error_dialog, dialogCallback, true);
    }

    public void messageBoxOK(int title, int message, final IDialogCallback dialogCallback) {
        messageBoxOK(title, getResources().getString(message), R.drawable.info, dialogCallback, false);
    }

    public void messageBoxOK(int title, String message, final IDialogCallback dialogCallback) {
        messageBoxOK(title, message, R.drawable.info, dialogCallback, false);
    }


    public void messageBoxOK(int title, String message, boolean containsPrice, final IDialogCallback dialogCallback) {
        messageBoxOK(title, message, R.drawable.info, containsPrice, dialogCallback, false);
    }

    @Override
    public void messageBoxOK(int title, String message, int icon, IDialogCallback dialogCallback) {
        messageBoxOK(title, message, R.drawable.info, dialogCallback, false);
    }

    public void messageBoxOK(int title, String message, int icon, final IDialogCallback dialogCallback, boolean isLatin) {
        messageBoxOK(title, message, icon, false, dialogCallback, isLatin);
    }


    public void messageBoxOK(int title, String message, int icon, boolean containsPrice, final IDialogCallback dialogCallback, boolean isLatin) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        ScrollView scrollView = new ScrollView(getContext());

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setVerticalScrollBarEnabled(true);

        TextView textView = new TextView(getContext());
        textView.setPadding(20, 10, 20, 10);
        if (isLatin) {
            textView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            textView.setTypeface(Typeface.SERIF);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

            scrollView.setTextDirection(View.TEXT_DIRECTION_LTR);
            scrollView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            scrollView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        } else if (containsPrice) {
            textView.setTextSize(17);
            textView.setTypeface(Typeface.SANS_SERIF);
            textView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        //textView.setMaxLines(Integer.MAX_VALUE);
        //textView.setVerticalScrollBarEnabled(true);
        textView.setText(message);
        linearLayout.addView(textView);
        scrollView.addView(linearLayout);


        alertDialog.setView(scrollView);


        // Setting Icon to Dialog
        alertDialog.setIcon(icon);

        // Setting OK Button
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.ok_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mDialogResult = DialogResult.OK;
                if (dialogCallback != null)
                    dialogCallback.dialog_callback(mDialogResult, null, 0);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void messageBoxYesNo(int title, String message, final IDialogCallback dialogCallback) {
        messageBoxYesNo(title, message, R.drawable.question, dialogCallback);
    }

    public void messageBoxYesNo(int title, int message, final IDialogCallback dialogCallback) {
        messageBoxYesNo(title, getResources().getString(message), R.drawable.question, dialogCallback);
    }

    public void messageBoxYesNo(int title, int message, int icon, final IDialogCallback dialogCallback) {
        messageBoxYesNo(title, getResources().getString(message), icon, dialogCallback);
    }

    public void messageBoxYesNo(int title, String message, int icon, final IDialogCallback dialogCallback) {
        Log.d(TAG, "messageBoxYesNo(): entered the function");

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        //alertDialog.setMessage(PersianConvert.ConvertDigitsToPersian(message));
        alertDialog.setMessage(message);

        // Setting Icon to Dialog
        alertDialog.setIcon(icon);

                // Setting No Button
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.no_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "No clicked");
                mDialogResult = DialogResult.No;

                if (dialogCallback != null)
                    dialogCallback.dialog_callback(mDialogResult, null, 0);
            }
        });


        // Setting Yes Button
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.yes_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Yes clicked");
                mDialogResult = DialogResult.Yes;

                if (dialogCallback != null)
                    dialogCallback.dialog_callback(mDialogResult, null, 0);
            }
        });


        // Showing Alert Message
        alertDialog.show();

        Log.d(TAG, "messageBoxYesNo(): end of the function");
    }
}
