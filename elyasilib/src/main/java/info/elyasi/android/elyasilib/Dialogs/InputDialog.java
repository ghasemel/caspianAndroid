package info.elyasi.android.elyasilib.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import info.elyasi.android.elyasilib.Font.CustomTFSpan;

/**
 * Created by Canada on 6/17/2016.
 */
public class InputDialog extends ADialogFragment<String> {
    private static final String TAG = "InputDialog";

    private String mTitle;
    private IInputDialogProperty mInputDialogProperty;
    private Typeface mTypeface = null;
    //private AlertDialog mAlertDialog;
    //private EditText mInput;
    //private IFragmentCallback mCallback;
    //private String mCallerNameExtra;

    public InputDialog() {
        super();
        mInputDialogProperty = null;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    protected void mapViews(View parentView) {

    }

    public Typeface getTypeface() {
        return mTypeface;
    }

    public void setTypeface(Typeface typeface) {
        mTypeface = typeface;
    }

    public void setInputDialogProperty(IInputDialogProperty inputDialogProperty) {
        mInputDialogProperty = inputDialogProperty;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mTypeface != null && getDialog() != null && getDialog() instanceof AlertDialog) {
            Log.d(TAG, "onStart()");
            AlertDialog dialog = (AlertDialog) getDialog();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTypeface(mTypeface);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTypeface(mTypeface);
        }
    }

    @Override
    protected Dialog createDialog() {
        // Set up the input
        final EditText input = new EditText(getActivity());

        if (mInputDialogProperty != null)
            mInputDialogProperty.setEditTextProperty(input);

        this.setLayoutView(input);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getTitle())
                .setView(getLayoutView())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //m_Text = input.getText().toString();
                        //mCallback.activity_callback(new String[] { mCallerNameExtra, input.getText().toString() });
                        getDialogCallback().dialog_callback(DialogResult.OK, input.getText().toString(), getRequestCode());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDialogCallback().dialog_callback(DialogResult.Cancel, null, getRequestCode());
                        dialog.cancel();
                    }
                });


        if (mTypeface != null) {
            input.setTypeface(mTypeface);

            CustomTFSpan tfSpan = new CustomTFSpan(mTypeface);
            SpannableString spannableString = new SpannableString(getTitle());
            spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setTitle(spannableString);
        }

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//        input.setInputType(InputType.TYPE_CLASS_TEXT);// | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        input.setWidth(300);
//        input.setHeight(50);





        AlertDialog alertDialog = builder.create();





        // Set up the buttons
        //alertDialog.setButton();
        //alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, );





        return alertDialog;
    }
}
