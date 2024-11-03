package info.elyasi.android.elyasilib.UI;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.View;

/**
 * Created by Canada on 7/22/2016.
 */
public abstract class ADataGridListFragment<TListItem> extends AAsyncFragment implements IListDataSetChangeCallBack, IListItemClick<TListItem>, IListCellClick<TListItem> {
    private static final String TAG = "ADataGridListFragment";

    protected abstract String getFragmentRowTagValue();
    protected abstract int getActionbarTitleTextViewId();
    protected abstract void  afterFragmentRowAttached();

    private AListRowFragment<TListItem> mRowFragment;
    private String mActionBarDefaultTitle;

    @SuppressWarnings("unchecked")
    @Override
    protected final void afterMapViews(View parentView) {
        Log.d(TAG, "afterMapViews(): function entered");

        Fragment fragment = getChildFragmentManager().findFragmentByTag(getFragmentRowTagValue());
        if (fragment instanceof AListRowFragment) {
            mRowFragment = (AListRowFragment<TListItem>) fragment;
            mRowFragment.setOnListDataSetChange(this);
            mRowFragment.setOnListItemSelection(this);
            mRowFragment.setOnCellClick(this);
            mRowFragment.setAutoLoad(false);
            afterFragmentRowAttached();
        }

        mActionBarDefaultTitle =
                ActionbarExt.getActionbarTitle((ActivityFragmentExt) getActivity(), getActionbarTitleTextViewId());

        Log.d(TAG, "afterMapViews(): function end");
    }


    public AListRowFragment<TListItem> getRowFragment() {
        return mRowFragment;
    }


    @Override
    public void OnItemClick(TListItem tListItem, int position) {

    }

    @Override
    public void OnCellClick(TListItem tListItem, int row, int cellId, View cellView) {

    }
}
