package info.elyasi.android.elyasilib.Dialogs;

/**
 * Created by Canada on 6/22/2016.
 */
public interface IDialogCallback<T> {
    void dialog_callback(DialogResult dialogResult, T result, int requestCode);
}
