package com.trova.supercraft;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;


/**
 * Created by Panchakshari on 29/9/2016.
 */
public class LoadImageTask extends AsyncTask<ProgressBarHelper, Void, Uri> {

    ImageView imageView = null;
    ProgressBar pb = null;

    protected Uri doInBackground(ProgressBarHelper... pb_and_images) {
        this.imageView = (ImageView)pb_and_images[0].getImg();
        //long id = MessageActivity.getMessageId(imageView);
        Uri thumbUri = MessageActivity.getImageUri(imageView);
        this.pb = (ProgressBar)pb_and_images[0].getPb();
        //return getBitmapDownloaded(thumbUri);
        return thumbUri;
    }

    protected void onPostExecute(Uri result) {
        //System.out.println("Downloaded " + imageView.getId());
        imageView.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);  // hide the progressbar after downloading the image.
        imageView.setImageURI(result); //set the bitmap to the imageview.
    }

    /*protected void onPostExecute(Bitmap result) {
        System.out.println("Downloaded " + imageView.getId());
        imageView.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);  // hide the progressbar after downloading the image.
        imageView.setImageBitmap(result); //set the bitmap to the imageview.
    }*/

    /** This function downloads the image and returns the Bitmap **/
    private Bitmap getBitmapDownloaded(long id) {
        //System.out.println("String Record Id " + String.valueOf(id));
        Bitmap bitmap = null;
        try {
            //byte[] imgData = myGlobals.dbHandler.getThumbNailContent(id);
            //bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);

            return bitmap;
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
        return bitmap;
    }

    /** decodes image and scales it to reduce memory consumption **/
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
}
