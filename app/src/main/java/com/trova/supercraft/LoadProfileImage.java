package com.trova.supercraft;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by Panchakshari on 10/3/2017.
 */

public class LoadProfileImage extends AsyncTask<ProgressBarHelper, Void, RoundImage> {

    ImageView imageView = null;
    ProgressBar pb = null;
    int resourceId = -1;
    Bitmap bm;

    protected RoundImage doInBackground(ProgressBarHelper... pb_and_images) {
        this.imageView = (ImageView)pb_and_images[0].getImg();
        //this.resourceId = pb_and_images[0].getResourceId();
        this.bm = pb_and_images[0].getBitmap();
        this.pb = (ProgressBar)pb_and_images[0].getPb();
        //Bitmap bm = BitmapFactory.decodeResource(MessageActivity.context.getResources(), resourceId);
        //RoundImage roundedImage = new RoundImage(bm);
        RoundImage roundedImage = new RoundImage(this.bm);
        return roundedImage;
    }

    protected void onPostExecute(RoundImage result) {
        imageView.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);  // hide the progressbar after downloading the image.
        imageView.setImageDrawable(result); //set the bitmap to the imageview.
    }

}
