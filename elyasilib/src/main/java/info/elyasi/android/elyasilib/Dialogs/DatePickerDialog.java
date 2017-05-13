package info.elyasi.android.elyasilib.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import info.elyasi.android.elyasilib.Controls.datepicker.PersianDatePickerControl;
import info.elyasi.android.elyasilib.Controls.datepicker.util.PersianCalendar;
import info.elyasi.android.elyasilib.R;
import info.elyasi.android.elyasilib.Thread.ICallBack;
import info.elyasi.android.elyasilib.Thread.IUICallBack;

/**
 * Created by Canada on 7/15/2016.
 */
public class DatePickerDialog extends ADialogFragment<PersianCalendar> {
    private static final String TAG = "DatePickerDialog";


    private PersianDatePickerControl mDatePickerControl;
    private PersianCalendar mCurrentDate;

    protected int getLayoutId() {
        return R.layout.dialog_date_persian;
    }


    public static void SelectDate(final EditText editTextDate, FragmentManager fragmentManager, final IDialogCallback<PersianCalendar> callBack) {
        DatePickerDialog dialog = new DatePickerDialog();
        dialog.setDialogCallback(new IDialogCallback<PersianCalendar>() {
            @Override
            public void dialog_callback(DialogResult dialogResult, PersianCalendar result, int requestCode) {
                Log.d(TAG, "PersianDate: " + result.getPersianShortDate());
                editTextDate.setText(result.getPersianShortDate());

                if (callBack != null) {
                    callBack.dialog_callback(dialogResult, result, requestCode);
                }
            }
        });
        dialog.setCurrentDate(editTextDate.getText().toString());
        dialog.show(fragmentManager, "DATE");
    }

    public void setCurrentDate(String persianDate) {
        String[] split = persianDate.split("/");
        if (split.length == 3) {
            mCurrentDate = new PersianCalendar();
            mCurrentDate.setPersianDate(
                    Integer.parseInt(split[0]),
                    Integer.parseInt(split[1]),
                    Integer.parseInt(split[2])
            );
        } else {
            mCurrentDate = null;
        }
    }

    @Override
    protected void mapViews(View parentView) {
        mDatePickerControl = (PersianDatePickerControl) getLayoutView().findViewById(R.id.date_picker_control);
        if (mCurrentDate != null) {
            mDatePickerControl.setDisplayPersianDate(mCurrentDate);
        }
    }

    @Override
    protected Dialog createDialog() {
        return new AlertDialog.Builder(getActivity())
                .setView(getLayoutView())
                .setTitle(R.string.select_date_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (getDialogCallback() != null) {
                            getDialogCallback().dialog_callback(DialogResult.OK, mDatePickerControl.getDisplayPersianDate(), getRequestCode());
                        }
                    }
                })
                .create();
    }


}
