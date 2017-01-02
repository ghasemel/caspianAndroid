package info.elyasi.android.elyasilib.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;

import info.elyasi.android.elyasilib.Controls.datepicker.PersianDatePickerControl;
import info.elyasi.android.elyasilib.Controls.datepicker.util.PersianCalendar;
import info.elyasi.android.elyasilib.R;

/**
 * Created by Canada on 7/15/2016.
 */
public class DatePickerDialog extends ADialogFragment<PersianCalendar> {

    private PersianDatePickerControl mDatePickerControl;
    private PersianCalendar mCurrentDate;

    protected int getLayoutId() {
        return R.layout.dialog_date_persian;
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
