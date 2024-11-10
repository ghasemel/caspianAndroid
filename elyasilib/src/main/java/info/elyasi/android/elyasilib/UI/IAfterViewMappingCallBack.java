package info.elyasi.android.elyasilib.UI;

import android.widget.HorizontalScrollView;

public interface IAfterViewMappingCallBack {

    HorizontalScrollView getHorizontalScrollView();

    default void setScrollBarToZero() {
        getHorizontalScrollView().scrollTo(0, 0);
    }
}
