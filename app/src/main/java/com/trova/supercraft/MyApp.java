package com.trova.supercraft;

import android.app.Application;
import android.util.Log;

/**
 * Created by Panchakshari on 9/4/2017.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("Application", "onCreate Called ...............");
        //FontsOverride.setDefaultFont(this, "DEFAULT", "raleway_regular.ttf");
        //TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Century-Gothic.ttf");
        //  This FontsOverride comes from the example I posted above
/*
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/Century-Gothic.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Century-Gothic.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/Century-Gothic.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/Century-Gothic.ttf");
        FontsOverride.setDefaultFont(this, "SANS", "fonts/Century-Gothic.ttf");
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Century-Gothic.ttf");
        TypefaceUtil.overrideFont(getApplicationContext(), "SANS", "fonts/Century-Gothic.ttf");
*/
    }
}
