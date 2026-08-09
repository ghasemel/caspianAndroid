package ir.caspiansoftware.caspianandroidapp.PresentationLayer.Restaurant;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * A ScrollView that never grows past a maximum height, scrolling instead.
 *
 * Android has no android:maxHeight for ScrollView, and setting the height from
 * code after the fact does not work reliably here: the strip is populated
 * before it is attached, so params assigned then are replaced on the first
 * layout pass and the view grows to fit all of its content.
 *
 * Capping during onMeasure is the only point where the decision actually
 * sticks, which is why this exists rather than more layout-params juggling.
 */
public class MaxHeightScrollView extends ScrollView {

    private int mMaxHeightPx = 0;   // 0 = no cap

    public MaxHeightScrollView(Context context) {
        super(context);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /** @param maxHeightPx cap in pixels, or 0 to size freely to content. */
    public void setMaxHeight(int maxHeightPx) {
        if (mMaxHeightPx != maxHeightPx) {
            mMaxHeightPx = maxHeightPx;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxHeightPx > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeightPx, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
