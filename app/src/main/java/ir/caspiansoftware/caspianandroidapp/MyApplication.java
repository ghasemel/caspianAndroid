package ir.caspiansoftware.caspianandroidapp;

import info.elyasi.android.elyasilib.Font.FontsOverride;

/**
 * Created by Canada on 7/29/2016.
 */
public final class MyApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //FontsOverride.setDefaultFont(this, "DEFAULT", "MyFontAsset.ttf");
        //FontsOverride.setDefaultFont(this, "MONOSPACE", "MyFontAsset2.ttf");
        //FontsOverride.setDefaultFont(this, "SERIF", "fonts/BNazanin.ttf");
        //FontsOverride.setDefaultFont(this, "SANS_SERIF", "MyFontAsset4.TTF");

        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/NiloofarBd.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/NiloofarBd_Num.ttf");
    }
}