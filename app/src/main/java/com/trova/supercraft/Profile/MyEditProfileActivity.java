package com.trova.supercraft.Profile;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.trova.supercraft.FontManager;
import com.trova.supercraft.R;
import com.trova.supercraft.RoundImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import static com.trova.supercraft.AmazonS3.AmazonS3Utils.uploadResource2AmazonS3;
import static com.trova.supercraft.MessageUtils.bytes2Size;
import static com.trova.supercraft.MessageUtils.compressBitmap;
import static com.trova.supercraft.MessageUtils.getBytes;
import static com.trova.supercraft.MessageUtils.getRealPathFromURI;
import static com.trova.supercraft.MessageUtils.saveBitmap;
import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.SuperCraftUtils.createThumbnailFromPath;
import static com.trova.supercraft.SuperCraftUtils.launchChatNotificationListActivity;
import static com.trova.supercraft.SuperCraftUtils.launchNotificationListActivity;
import static com.trova.supercraft.SuperCraftUtils.logInfo;
import static com.trova.supercraft.SuperCraftUtils.updateUnreadMessagesCount;


/**
 * Created by Trova on 11/2/2016.
 */
public class MyEditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static Context context;
    private static Activity activity;
    private static Handler handler = null;
    private static boolean success = false;
    private static ProgressDialog progressBar;
    final static int PICK_IMAGE_REQUEST = 1;
    final static int CAMERA_REQUEST = 2;
    public static Uri selectedImageUri = null;
    private String filePath = null;
    private String path = null;
    private File file = null;
    private ImageView profileImage;
    private Bitmap thumbnail = null;
    private byte[] compressedImage = null;
    private static Toolbar myToolbar;
    private int isIndex = 0;
    private static TextView notifyText, chatText;
    private static int chatCount = 0, notifyCount = 0;
    public static Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        activity = this;
        myGlobals.myEditProfileActivity = this;

        setContentView(R.layout.edit_profile_layout);
        logInfo("MyEditProfileActivity", "0 .........");
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.getMenu().clear();
        ((TextView)myToolbar.findViewById(R.id.job_id)).setText("Supercraft3D");
        ((TextView)myToolbar.findViewById(R.id.patient_id)).setText("");
        ((LinearLayout)myToolbar.findViewById(R.id.message_back_lyt)).setVisibility(View.GONE);

        ((TextView)findViewById(R.id.label_fname)).setText(myGlobals.userProfile.getUserName());
        ((TextView)findViewById(R.id.label_lname)).setText(myGlobals.userProfile.getUserLName());
        ((TextView)findViewById(R.id.tvcontact_number)).setText(myGlobals.userProfile.getUserPhone());
        ((TextView)findViewById(R.id.tvemail_address)).setText(myGlobals.userProfile.getUserEmail());
        profileImage = (ImageView) findViewById(R.id.profile_image);
        Bitmap bm = myGlobals.bmLoggedinUser;
        if(bm == null)
            bm = BitmapFactory.decodeResource(getResources(),R.drawable.avatar);
        RoundImage roundedImage = new RoundImage(bm);
        profileImage.setImageDrawable(roundedImage);
        ((FloatingActionButton)findViewById(R.id.select)).setOnClickListener(this);
        //((FloatingActionButton)findViewById(R.id.camera)).setOnClickListener(this);
    }

    private void updateProfilePic() {

        progressBar = new ProgressDialog(context);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setMessage("Updating profile image ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateProfile();
            }
        }, 10);

    }

    private void updateProfile() {
        if(selectedImageUri != null) {
            try {
                logInfo("onActivityResult", "Called ...........");
                filePath = getRealPathFromURI(myGlobals.context, selectedImageUri);
                String ext = filePath.substring(filePath.lastIndexOf(".")).substring(1);
                path = filePath.substring(filePath.lastIndexOf("/") + 1).trim();
                int lastIndex = path.lastIndexOf(".");
                String messageId = myGlobals.userProfile.getUserEmail();//path.substring(0, lastIndex);
                logInfo("messageId", messageId);
                try {
                    file = saveBitmap(thumbnail, ext, messageId, myGlobals.context.getString(R.string.profile_pic_folder));
                    //file = saveFile(compressedImage, ext, messageId, myGlobals.context.getString(R.string.profile_pic_folder));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (success) {
                    logInfo("file", Uri.parse(file.getPath()).toString());
                    logInfo("onActivityResult", filePath);
                    logInfo("onActivityResult", path);
                    Bitmap bm = createThumbnailFromPath(file.getPath().toString(), 1);
                    RoundImage roundedImage = new RoundImage(bm);
                    profileImage.setImageDrawable(roundedImage);
                }
                progressBar.dismiss();
            }
        };

        new Thread() {
            public void run() {
                int status = 1;
                final String mediaLink = "profilepic/" + myGlobals.userProfile.getUserEmail()  + ".jpg";
                uploadResource2AmazonS3(file, mediaLink);
                if (status == 1)
                    success = true;
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.select) {
            logInfo("Clicked on", "Browse Image");
            try {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activity.startActivityForResult(Intent.createChooser(i, "Select Image"), PICK_IMAGE_REQUEST);
            } catch (Exception e) {
                Crashlytics.logException(e);
                e.printStackTrace();
            }
        }/* else if(id == R.id.camera) {
            logInfo("Clicked on", "Camera Open");
            try {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                activity.startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } catch (Exception e) {
                Crashlytics.logException(e);
                e.printStackTrace();
            }
        }*/
    }

    //@Override
    public void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        //super.onActivityResult(requestCode,resultCode,data);
        long size = 0;
        int isIndex = 0;
        InputStream iStream = null;
        byte[] imageData = null;
        try {
            logInfo("onActivityResult", "Called ..............");
            if (resultCode == RESULT_OK)/* && (data != null))*/ {
                switch (requestCode) {
                    case PICK_IMAGE_REQUEST:
                        logInfo("onActivityResult", "Called ...........PICK_IMAGE_REQUEST");
                        selectedImageUri = data.getData();
                        logInfo("selectedImageUri", selectedImageUri.toString());
                        try {
                            iStream = context.getContentResolver().openInputStream(selectedImageUri);
                            imageData = getBytes(iStream);
                            logInfo("ImageSize ", String.valueOf(imageData.length));
                        } catch (FileNotFoundException e) {
                            Crashlytics.logException(e);
                            e.printStackTrace();
                        } catch (IOException e) {
                            Crashlytics.logException(e);
                            e.printStackTrace();
                        }
                        thumbnail = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                        compressedImage = compressBitmap(thumbnail, 40);
                        thumbnail.recycle();
                        System.gc();
                        thumbnail = BitmapFactory.decodeByteArray(compressedImage, 0, compressedImage.length);
                        size = bytes2Size(compressedImage.length);
                        if((size > 100) && (isIndex > 0))
                            thumbnail = ThumbnailUtils.extractThumbnail(thumbnail, 300, 300, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

                        updateProfilePic();
                        break;
                    case CAMERA_REQUEST:
                        logInfo("onActivityResult", "Called ...........CAMERA_REQUEST");
                        //selectedImageUri = data.getData();
                        //logInfo("selectedImageUri", selectedImageUri.toString());
                        try {
                            thumbnail = (Bitmap) data.getExtras().get("data");
                            compressedImage = compressBitmap(thumbnail, 40);
                        } catch (Exception e) {
                            Crashlytics.logException(e);
                            e.printStackTrace();
                        }
                        onSelectFromGalleryResult(context, selectedImageUri, null);
/*
                        thumbnail.recycle();
                        System.gc();
                        thumbnail = BitmapFactory.decodeByteArray(compressedImage, 0, compressedImage.length);
                        size = bytes2Size(compressedImage.length);
                        if((size > 100) && (isIndex > 0))
                            thumbnail = ThumbnailUtils.extractThumbnail(thumbnail, 300, 300, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
*/
                        break;
                }
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Context context, Uri selectedImageUri, byte[] data) {
        logInfo("onSelectFromGallery", "Called ..........size " + compressedImage.length);

        thumbnail.recycle();
        System.gc();
        thumbnail = BitmapFactory.decodeByteArray(compressedImage, 0, compressedImage.length);
        long size = bytes2Size(compressedImage.length);
        if((size > 100) && (isIndex > 0))
            thumbnail = ThumbnailUtils.extractThumbnail(thumbnail, 300, 300, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        updateProfilePic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        logInfo("MyEditProfileActivity", "onPause Called .....");
    }

    @Override
    protected  void onDestroy() {
        logInfo("MyEditProfileActivity", "onDestroy Called .....");
        super.onDestroy();
        myGlobals.myEditProfileActivity = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        myGlobals.myEditProfileActivity = this;
        logInfo("MyEditProfileActivity", "onResume Called .....");
        if(myToolbar != null) {
            ((TextView)myToolbar.findViewById(R.id.job_id)).setText("Supercraft3D");
            ((TextView) myToolbar.findViewById(R.id.patient_id)).setText("");
            ((LinearLayout) myToolbar.findViewById(R.id.message_back_lyt)).setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;

        logInfo("MyProfileActivity", "onCreateOptionsMenu");

        MenuItem itemMessages = menu.findItem(R.id.notify);
        RelativeLayout notifyLayout = (RelativeLayout) itemMessages.getActionView();

        notifyText = (TextView) notifyLayout.findViewById(R.id.notify_icon_button);
        notifyText.setText(getResources().getString(R.string.fa_bell));
        FontManager.markAsIconContainer(notifyText, myGlobals.fontAwesomeFont);

        notifyText = (TextView)notifyLayout.findViewById(R.id.notify_textView);
        notifyText.setText(String.valueOf(notifyCount));
        notifyText.setTextColor(getResources().getColor(R.color.white));
        if(notifyCount <= 0)
            notifyText.setVisibility(View.GONE);
        notifyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInfo("Clicked on", "Notify Menu ........");
                launchNotificationListActivity(true);
            }
        });

        itemMessages = menu.findItem(R.id.chat);
        RelativeLayout chatLayout = (RelativeLayout) itemMessages.getActionView();

        chatText = (TextView) chatLayout.findViewById(R.id.chat_icon_button);
        chatText.setText(getResources().getString(R.string.mt_notify_chat));
        FontManager.markAsIconContainer(chatText, myGlobals.materialIconFont);

        chatText = (TextView)chatLayout.findViewById(R.id.chat_textView);
        notifyText.setText(String.valueOf(chatCount));
        chatText.setTextColor(getResources().getColor(R.color.white));
        if(chatCount <= 0)
            chatText.setVisibility(View.GONE);
        chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInfo("Clicked on", "Chat Menu ........");
                launchChatNotificationListActivity(true);
            }
        });

        itemMessages = menu.findItem(R.id.stop);
        itemMessages.setVisible(false);

        itemMessages = menu.findItem(R.id.delete);
        itemMessages.setVisible(false);

        itemMessages = menu.findItem(R.id.archive);
        itemMessages.setVisible(false);

        itemMessages = menu.findItem(R.id.action);
        itemMessages.setVisible(false);

        super.onCreateOptionsMenu(menu);
        updateUnreadMessagesCount();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        chatCount = myGlobals.chatCount;
        notifyCount = myGlobals.notifyCount;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String str = null;
                try {
                    if(chatCount > 0) {
                        str = String.valueOf(chatCount);
                        if(chatCount > 99)
                            str = "99..";
                        chatText.setVisibility(View.VISIBLE);
                        chatText.setText(str);
                        chatText.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        chatText.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(notifyCount > 0) {
                        notifyText.setVisibility(View.VISIBLE);
                        notifyText.setText(String.valueOf(notifyCount));
                        notifyText.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        notifyText.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

/*
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
    }
*/

    public static File saveImage(byte[] data, String fileExt, String messageId, String folderType) throws IOException {
        Context context = myGlobals.context;
        String mBaseFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+context.getPackageName() + context.getString(R.string.trova_base_folder) + folderType;
        if (!new File(mBaseFolderPath).exists()) {
            new File(mBaseFolderPath).mkdirs();
        }
        String fileName = messageId + "." + fileExt;
        String path = mBaseFolderPath+fileName;

        File f = new File(path);
        FileOutputStream fo = null;

        try {
            f.createNewFile();
            logInfo("saveFile FilePath", path);
            logInfo("Bytes", "Data Size " + data.length);
            fo = new FileOutputStream(f);
            fo.write(data);
            fo.flush();
        } catch(Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
            logInfo("Failed to", "Create file " + path);
        } finally {
            if(fo != null){
                try {
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
            }
        }

        return f;
    }

    @Override
    public void onBackPressed() {
        myGlobals.myEditProfileActivity = null;
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
