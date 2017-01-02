package info.elyasi.android.elyasilib.UI;

import android.app.Activity;
import android.os.Bundle;

import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;


import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import info.elyasi.android.elyasilib.Utility.JsonExt;

/**
 * Created by Canada on 3/5/2016.
 */
public abstract class AListRowFragment<TListItem> extends ListFragment { //TResponse
    private static final String TAG = "AListRowFragment";

    public static final String EXTRA_RETURN_NAME = "list_selected_object";
    //private String mExtraReturnSelectedObjct;

    protected abstract ArrayList<TListItem> inBackground(Object... params) throws Exception;
    protected abstract MyAdapter postExecute(ArrayList<TListItem> response) throws Exception;
    protected abstract void onListItemClicked(TListItem item, String resultExtraName) throws Exception;
    private boolean mAutoLoad = true;

    private IListDataSetChangeCallBack mOnListDataSetChange = null;
    private IListItemClick<TListItem> mOnListItemSelection = null;
    private IListCellClick<TListItem> mOnCellClick;

    //private TListItem mSelectedRow;
    private boolean mCloseAfterSelection = true;
    //private boolean mSuperOnListItemClickCalled = false;
    //protected ArrayList<TListItem> mList;
    //protected MyAdapter mAdapter = null;


    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);


        //mSelectedRow = null;

    }

    @Override
    public void onStart() {
        super.onStart();

        if (mAutoLoad)
            LoadDataAsync();
    }

    public void LoadDataAsync(Object... params)
    {
        class RunAsync extends AAsyncTask<Object, Void, ArrayList<TListItem>> {

            @Override
            protected ArrayList<TListItem> doInBackground(Object... params) {
                try {
                    return inBackground(params);
                } catch (Exception ex) {
                    setException(ex);
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<TListItem> response) {
                if (isException()) {
                    showError(getException());
                } else {
                    try {
                        MyAdapter myAdapter = postExecute(response);
                        //if (myAdapter != null)
                            setListAdapter(myAdapter);
                    } catch (Exception ex) {
                        showError(ex);
                    }
                }
            }
        }

        RunAsync runAsync = new RunAsync();
        runAsync.execute(params);
    }

    @SuppressWarnings("unchecked")
    public Filter getFilter() {
        if (getListAdapter() != null) {
            MyAdapter myAdapter = (MyAdapter) getListAdapter();
            return myAdapter.getFilter();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public void clearFilter() {
        if (getListAdapter() != null) {
            MyAdapter myAdapter = (MyAdapter) getListAdapter();
            myAdapter.clearFilter();
        }
    }

    @SuppressWarnings("unchecked")
    public int getCount() {
        if (getListAdapter() != null) {
            MyAdapter myAdapter = (MyAdapter) getListAdapter();
            return myAdapter.getCount();
        }
        return 0;
    }

    protected void showError(String errorMessage) {
        if (getParentFragment() != null) {
            AAsyncFragment asyncFragment = (AAsyncFragment) getParentFragment();
            asyncFragment.showError(errorMessage, null);
        }

        if (getView() != null)
            setListShown(true);
    }

    protected void showError(Exception exception) {
        if (getParentFragment() != null) {
            AAsyncFragment asyncFragment = (AAsyncFragment) getParentFragment();
            asyncFragment.showError(exception, null);
        }

        if (getView() != null)
            setListShown(true);
    }

    public void setOnListDataSetChange(IListDataSetChangeCallBack onListDataSetChange) {
        mOnListDataSetChange = onListDataSetChange;
    }


//    public TListItem getSelectedRow() {
//        return mSelectedRow;
//    }
//
//    protected void setSelectedRow(TListItem selectedRow) {
//        mSelectedRow = selectedRow;
//    }


    public boolean isCloseAfterSelection() {
        return mCloseAfterSelection;
    }

    public void setCloseAfterSelection(boolean closeAfterSelection) {
        mCloseAfterSelection = closeAfterSelection;
    }


    public boolean isAutoLoad() {
        return mAutoLoad;
    }

    public void setAutoLoad(boolean autoLoad) {
        mAutoLoad = autoLoad;
    }

    public void setOnListItemSelection(IListItemClick<TListItem> onListItemSelection) {
        mOnListItemSelection = onListItemSelection;
    }

    protected IListItemClick<TListItem> getOnListItemSelection() {
        return mOnListItemSelection;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void onListItemClick(ListView listView, View v, int position, long id) {
        try {
            Object obj = listView.getAdapter().getItem(position);
            //setSelectedRow((TListItem) obj);

            onListItemClicked((TListItem) obj, getExtraReturnName());

            if (mOnListItemSelection != null)
                mOnListItemSelection.OnItemClick((TListItem) obj, position);

            UIUtility.HideKeyboard(getActivity());

            if (isCloseAfterSelection()) {
                getActivity().finish();
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<TListItem> getOriginalDataList() {
        if (getListAdapter() != null) {
            MyAdapter myAdapter = (MyAdapter) getListAdapter();
            return myAdapter.getOrgDataList();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<TListItem> getCurrentDataList() {
        if (getListAdapter() != null) {
            MyAdapter myAdapter = (MyAdapter) getListAdapter();
            return myAdapter.getCurrentDataList();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public void setDataList(ArrayList<TListItem> list) {
        if (list == null)
            setListAdapter(null);

        if (getListAdapter() != null) {
            setListAdapter(null);
        }


        if (getListAdapter() == null) {
            try {
                MyAdapter myAdapter = postExecute(list);
                setListAdapter(myAdapter);
            } catch (Exception ex) {
                showError(ex);
            }
        } //else {
//            MyAdapter myAdapter = (MyAdapter) getListAdapter();
//            myAdapter.setDataList(list);
//        }
    }

    @SuppressWarnings("unchecked")
    public void notifyDataSetChanged() {
        if (getListAdapter() != null) {
            MyAdapter myAdapter = (MyAdapter) getListAdapter();
            myAdapter.notifyDataSetChanged();
        }
    }

    public void setOnCellClick(IListCellClick<TListItem> onCellClick) {
        mOnCellClick = onCellClick;
    }

    protected IListCellClick<TListItem> getOnCellClick() {
        return mOnCellClick;
    }

    public String getExtraReturnName() throws Exception {
//        if (mAutoLoad && !getActivity().getIntent().hasExtra(EXTRA_RETURN_NAME))
//            throw new Exception("AListRowFragment.EXTRA_RETURN_NAME has not be set. " +
//                    "Its value type should be String and the value is not important!. " +
//                    "It's just for your information to be inform that " +
//                    "the selected row object is available in EXTRA_RETURN_NAME");
        return EXTRA_RETURN_NAME;
    }

    // *******************************************************************
    // **************************** ArrayAdapter *************************
    // *******************************************************************
    protected abstract class MyAdapter extends ArrayAdapter<TListItem> implements Filterable {

        ArrayList<TListItem> mOrgDataList;
        ArrayList<TListItem> mDataList;
//        private List<List<ImageView>> mBtnList;


//        public int getColumnsCount() {
//            getView().findViewById()
//            return 0;
//        }


        public abstract int getLayout();
        protected abstract List<ImageView> doForEachItem(View convertView, TListItem item, int position, boolean isNull);

        public MyAdapter(Activity activity, ArrayList<TListItem> list) {
            super(activity, android.R.layout.simple_list_item_1, list); // this is sample

            mDataList = list;

            mOrgDataList = new ArrayList<>();
            mOrgDataList.addAll(list);

            if (mOnListDataSetChange != null) {
                mOnListDataSetChange.dataSetChanged(getCount());
            }
        }

        private void setBtnList(List<ImageView> btnList, final int row) {
            Log.d(TAG, "setBtnList(): row = " + row);
            if (btnList != null) {
                //int i = 0;
                for (final ImageView imageView: btnList) {
                    //final int index = i;
                    UIUtility.setButtonEffect(imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "onClick(): " + imageView.toString());
                            if (mOnCellClick != null) {
                                mOnCellClick.OnCellClick(
                                        getItem(row),
                                        row,
                                        imageView.getId(),
                                        imageView
                                );
                            }
                        }
                    });
                }

            }
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.d(TAG, "getView(): position = " + position);

            try {
                boolean isNull = false;
                if (convertView == null && getLayout() != 0) {
                    isNull = true;
                    convertView = getActivity().getLayoutInflater().inflate(getLayout(), null);
                }


                Log.d(TAG, "getView(): doForEachItem position = " + position);

                // configure the view for the item at the 'position'
                setBtnList(
                        doForEachItem(convertView, getItem(position), position, isNull),
                        position
                );
            } catch (Exception ex) {
                showError(ex);
            }

            return convertView;
        }

        protected abstract boolean objectIncludeTheFilterConstraints(TListItem objectModel, String[] constraints);

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    Log.d(TAG, "performFiltering(): function entered");

                    if (!JsonExt.isJSONArray(constraint.toString()))
                        return null;

                    try {
                        String[] values = JsonExt.toArray(constraint.toString());
                        if (values == null) {
                            clearFilter();
                        } else {
                            ArrayList<TListItem> list = new ArrayList<>();

                            for (TListItem objectModel : getOrgDataList()) {
                                if (objectIncludeTheFilterConstraints(objectModel, values)) {
                                    list.add(objectModel);
                                }
                            }

                            FilterResults results = new FilterResults();
                            results.count = list.size();
                            results.values = list;
                            Log.d(TAG, "result count: " + list.size());
                            return results;
                        }
                    } catch (JSONException jex) {
                        Log.d(TAG, jex.getMessage());
                    }

                    return null;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    if (filterResults != null && filterResults.values instanceof ArrayList) {
                        setDataList((ArrayList<TListItem>) filterResults.values);
                    }
                }
            };
        }


        public ArrayList<TListItem> getOrgDataList() {
            return mOrgDataList;
        }

        public ArrayList<TListItem> getCurrentDataList() {
            return mDataList;
        }

        public void clearFilter() {
           setDataList(mOrgDataList);
        }


        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();

            if (mOnListDataSetChange != null) {
                mOnListDataSetChange.dataSetChanged(getCount());
            }
        }

        public void setDataList(ArrayList<TListItem> dataList) {
            if (!dataList.equals(mDataList)) {
                mDataList.clear();
                mDataList.addAll(dataList);
            }
            this.notifyDataSetChanged();
        }
    }

}
