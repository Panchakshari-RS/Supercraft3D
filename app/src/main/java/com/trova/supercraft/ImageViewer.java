package com.trova.supercraft;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import static com.trova.supercraft.SuperCraftUtils.logInfo;


/**
 * Created by Panchakshari on 6/10/2016.
 */
public class ImageViewer extends AppCompatActivity implements View.OnClickListener{
    Activity activity;

    interface UpdateChatImageActivity{
        void updateChatImage();
    }

    static UpdateChatImageActivity trovaUpdateChatImage = null;
    public static void registerUpdateChatImageCallback(UpdateChatImageActivity updateChatImage){
        trovaUpdateChatImage = updateChatImage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logInfo("ViewImage", "onCreate .........");
        activity = this;
        setContentView(R.layout.activity_image_view);

        ImageView mImageView = (ImageView)findViewById(R.id.customImageView);
        mImageView.setImageBitmap(MessageActivity.thumbnail);

        ImageButton sendButton = (ImageButton) findViewById(R.id.sendImageButton);
        logInfo("ViewImage", "onCreate .........");
        sendButton.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if(trovaUpdateChatImage != null)
            trovaUpdateChatImage.updateChatImage();
        this.finish();
    }
}
