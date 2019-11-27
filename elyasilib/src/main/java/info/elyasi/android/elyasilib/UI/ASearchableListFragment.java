package info.elyasi.android.elyasilib.UI;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ProgressBar;

import info.elyasi.android.elyasilib.Persian.PersianConvert;
import info.elyasi.android.elyasilib.R;
import info.elyasi.android.elyasilib.Utility.JsonExt;

/**
 * Created by Canada on 7/22/2016.
 */
public abstract class ASearchableListFragment<TListItem> extends AAsyncFragment implements View.OnKeyListener, TextWatcher, IListDataSetChangeCallBack, IListCellClick<TListItem> {
    private static final String TAG = "ASearchableListFragment";

    protected abstract int getLayoutId();
    protected abstract String getFragmentRowTagValue();
    protected abstract int getActionbarTitleTextViewId();
    protected abstract String[] getSearchedTextValueOnTextChange();
    protected abstract EditText[] getSearchEditTexts();


    private AListRowFragment<TListItem> mRowFragment;
    private String mActionBarDefaultTitle;
   // private IListItemClick mListCallback;

    @SuppressWarnings("unchecked")
    @Override
    protected final void afterMapViews(View parentView) {
        Log.d(TAG, "afterMapViews(): function entered");

        for (EditText editText : getSearchEditTexts()) {
            editText.setOnKeyListener(this);
            editText.addTextChangedListener(this);
        }

        Fragment fragment = getChildFragmentManager().findFragmentByTag(getFragmentRowTagValue());
        if (fragment instanceof AListRowFragment) {
            mRowFragment = (AListRowFragment<TListItem>) fragment;
            mRowFragment.setOnListDataSetChange(this);
            mRowFragment.setOnCellClick(this::OnCellClick);
        }

        mActionBarDefaultTitle =
                ActionbarExt.getActionbarTitle((ActivityFragmentExt) getActivity(), getActionbarTitleTextViewId());
        Log.d(TAG, "afterMapViews(): function end");
    }


    @Override
    public ProgressBar getProgressBar() {
        return null;
    }


    @Override
    public final boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            UIUtility.HideKeyboard(getActivity());
            return true;
        }
        return false;
    }

    @Override
    public final void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        Log.d(TAG, "onTextChanged(): function entered");
        Log.d(TAG, "charSequence: " + charSequence + ", start: " + start + ", before: " + before + ", count: " + count);

        Filter filter = mRowFragment.getFilter();

        try {
            filter.filter(
                    JsonExt.toJsonArrayString(
                            getSearchedTextValueOnTextChange()
                    )
            );
        } catch (Exception ex) {
            showError(ex, null);
        }
    }

    @Override
    public final void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
        Log.d(TAG, "beforeTextChanged(): function entered");
        Log.d(TAG, "charSequence: " + charSequence + ", start: " + start + ", before: " + before + ", count: " + count);
    }

    @Override
    public final void afterTextChanged(Editable editable) {
        Log.d(TAG, "afterTextChanged(): function entered");
    }

    @Override
    public final void dataSetChanged(int rowCount) {
        //String c = PersianConvert.ConvertNumbersToPersian(String.valueOf(rowCount));

        ActionbarExt.setActionbarTitle(
                (ActivityFragmentExt) getActivity(),
                mActionBarDefaultTitle +
                        " (" + getResources().getString(R.string.list_count_label) +
                        //" " + PersianConvert.ConvertNumbersToPersian(String.valueOf(rowCount)) + ")",
                        " " + String.valueOf(rowCount) + ")",
                getActionbarTitleTextViewId());
    }

    @Override
    public void OnCellClick(TListItem tListItem, int row, int cellId, View cellView) {

    }
}