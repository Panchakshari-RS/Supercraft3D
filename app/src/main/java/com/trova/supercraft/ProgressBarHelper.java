package com.trova.supercraft;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by Panchakshari on 29/9/2016.
 */
public class ProgressBarHelper {

    private Bitmap bm;
    private ImageView img;
    private ProgressBar pb;
    int resourceId = -1;

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public ProgressBar getPb() {
        return pb;
    }

    public void setPb(ProgressBar pb) {
        this.pb = pb;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public Bitmap getBitmap() {
        return bm;
    }

    public void setBitmap(Bitmap bm) {
        this.bm = bm;
    }
}
