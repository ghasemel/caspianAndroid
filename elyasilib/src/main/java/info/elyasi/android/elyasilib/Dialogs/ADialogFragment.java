package info.elyasi.android.elyasilib.Dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Created by Canada on 7/18/2016.
 */
public abstract class ADialogFragment<TResult> extends DialogFragment {

    private View mLayoutView;
    private int mRequestCode;
    //private String mTitle = null;
    private IDialogCallback<TResult> mDialogCallback = null;

    protected abstract void mapViews(View parentView);
    protected abstract int getLayoutId();
    protected abstract Dialog createDialog();


    @NonNull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getLayoutId() != 0) {
            mLayoutView = getActivity().getLayoutInflater()
                    .inflate(getLayoutId(), null);
        }

        mapViews(mLayoutView);
        return createDialog();

        //return getDialog();
    }

    @Override
    public void onStart() {
        super.onStart();

        forceWrapContent(mLayoutView);
    }


//    @Override
//    public void show(FragmentManager manager, String tag) {
//        super.show(manager, tag);
//    }


    public View getLayoutView() {
        return mLayoutView;
    }

    protected void setLayoutView(View layoutView) {
        mLayoutView = layoutView;
    }


    public IDialogCallback<TResult> getDialogCallback() {
        return mDialogCallback;
    }

    public void setDialogCallback(IDialogCallback<TResult> dialogCallback) {
        mDialogCallback = dialogCallback;
    }




    public int getRequestCode() {
        return mRequestCode;
    }

    public void setRequestCode(int requestCode) {
        mRequestCode = requestCode;
    }

    public static void forceWrapContent(View v) {
        // Start with the provided view
        View current = v;

        // Travel up the tree until fail, modifying the LayoutParams
        do {
            // Get the parent
            ViewParent parent = current.getParent();

            // Check if the parent exists
            if (parent != null) {
                // Get the view
                try {
                    current = (View) parent;
                    ViewGroup.LayoutParams layoutParams = current.getLayoutParams();
                    if (layoutParams instanceof FrameLayout.LayoutParams) {
                        ((FrameLayout.LayoutParams) layoutParams).
                                gravity = Gravity.CENTER_HORIZONTAL;
                    } else if (layoutParams instanceof WindowManager.LayoutParams) {
                        ((WindowManager.LayoutParams) layoutParams).
                                gravity = Gravity.CENTER_HORIZONTAL;
                    }
                } catch (ClassCastException e) {
                    // This will happen when at the top view, it cannot be cast to a View
                    break;
                }

                // Modify the layout
                current.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        } while (current.getParent() != null);

        // Request a layout to be re-done
        current.requestLayout();
    }

}
