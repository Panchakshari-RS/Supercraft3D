package com.trova.supercraft;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by Panchakshari on 22/3/2017.
 */

public class FontManager {
    public static final String ROOT = "fonts/";
    public static String MATERIALIZE = ROOT + "materialdesignicons-webfont.ttf";
    public static String MATERIALIZEREGULAR = ROOT + "MaterialIcons-Regular.ttf";
    public static String FONTAWESOME = ROOT + "fontawesome-webfont.ttf";
    public static String OPENSANSLIGHT = ROOT + "OpenSans-Light.ttf";
    public static String OPENSANSREGULAR = ROOT + "OpenSans-Regular.ttf";
    public static String OPENSANSBOLD = ROOT + "OpenSans-Bold.ttf";
    public static String CENTURYGOTHIC = ROOT + "Century-Gothic.ttf";
    public static String CENTURYGOTHICBOLD = ROOT + "Century-Gothic-Bold.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

    public static void markAsIconContainer(View v, Typeface typeface) {
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                markAsIconContainer(child, typeface);
            }
        } else if (v instanceof TextView) {
            ((TextView) v).setTypeface(typeface);
        } else if (v instanceof Button) {
            ((Button) v).setTypeface(typeface);
        }
    }

    /**
     * Using reflection to override default typeface
     * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE OVERRIDDEN
     * @param context to work with assets
     * @param defaultFontNameToOverride for example "monospace"
     * @param customFontFileNameInAssets file name of the font from assets
     */
    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
            e.printStackTrace();
            //Log.e("Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride);
        }
    }
}
