package info.elyasi.android.elyasilib.Utility;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * Created by Canada on 7/30/2016.
 */
public class DrawableExt {
    public static boolean equal(Drawable drawable1, Drawable drawable2) {
        return drawable1.getConstantState().equals(drawable2.getConstantState());
    }

    public static boolean equal(Drawable drawable1, int drawable2Id, Context context) {
        Drawable drawable2 = ContextCompat.getDrawable(context, drawable2Id);
        return equal(drawable1, drawable2);
    }
}
