package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Mali.Search;

import android.view.View;
import android.widget.EditText;

import ir.caspiansoftware.caspianandroidapp.BaseCaspian.CaspianSearchableListFragment;
import ir.caspiansoftware.caspianandroidapp.Models.MPFaktorModel;
import ir.caspiansoftware.caspianandroidapp.R;

/**
 * Created by Canada on 3/8/2016.
 */
public class MaliSearchFragment extends CaspianSearchableListFragment<MPFaktorModel> {
    private static final String TAG = "MaliSearchFragment";

    private EditText mNum;
    private EditText mDate;
    private EditText mBes;
    private EditText mTotalPrice;


    @Override
    protected String[] getSearchedTextValueOnTextChange() {
        return new String[] {
                mNum.getText().toString(),
                mDate.getText().toString(),
                mBes.getText().toString(),
                mTotalPrice.getText().toString(),
        };
    }

    @Override
    protected String getFragmentRowTagValue() {
        return getString(R.string.fragment_invoice_search_row_tag);
    }

    @Override
    protected EditText[] getSearchEditTexts() {
        return new EditText[] {
                mNum,
                mDate,
                mBes,
                mTotalPrice
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mali_search;
    }


    @Override
    public void mapViews(View parentView) {
        mNum = (EditText) parentView.findViewById(R.id.search_num);
        mDate = (EditText) parentView.findViewById(R.id.search_date);
        mBes = (EditText) parentView.findViewById(R.id.search_customer);
        mTotalPrice = (EditText) parentView.findViewById(R.id.search_total_price);
    }

    @Override
    public void onClick(View view) {

    }

}
