package com.trova.supercraft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.crashlytics.android.Crashlytics;
import com.trova.supercraft.Notification.MyChatNotificationsRowItem;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.trova.supercraft.MessageUtils.compressBitmap;
import static com.trova.supercraft.MessageUtils.onAudioPickerSuccess;
import static com.trova.supercraft.MessageUtils.onDocumentPickerSuccess;
import static com.trova.supercraft.MessageUtils.onImagePickerSuccess;
import static com.trova.supercraft.MessageUtils.onSelectFromGalleryResult;
import static com.trova.supercraft.MessageUtils.onVideoCaptureSuccess;
import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.MyNewJobsTabFragment.launchDetailsActivity;
import static com.trova.supercraft.SuperCraftUtils.launchChatNotificationListActivity;
import static com.trova.supercraft.SuperCraftUtils.launchNotificationListActivity;
import static com.trova.supercraft.SuperCraftUtils.loadProfileImage;
import static com.trova.supercraft.SuperCraftUtils.logInfo;
import static com.trova.supercraft.SuperCraftUtils.updateUnreadMessagesCount;
import static in.trova.android.TrovaEvents.EVENT_MESSAGE_READ;


/**
 * Created by Panchakshari on 27/2/2017.
 */

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {
    public static Context context;
    public static Activity activity;
    public static String callerName,callerPhone,productId,businessName;
    private EditText msg_edittext;
    ListView msgListView;
    Map<Long, ChatMessages> chatMessages;
    private static MessageAdapter messageViewAdapter;
    public static MessageActivity messageActivity;
    final static int PICK_FILE_REQUEST=1;
    final static int CAMERA_REQUEST=2;
    final static int PICK_IMAGE_REQUEST=3;
    final static int PICK_AUDIO_REQUEST=4;
    final static int PICK_VIDEO_REQUEST=5;
    final static int ACTION_VIEW_GALLERY=6;
    private static PopupWindow showPopup;
    public static Uri selectedImageUri, selectAudioUri;
    public static Bitmap thumbnail;
    public static byte[] compressedImage;
    private int isIndex = 0;
    private static TransferUtility transferUtility;
    private static TransferState currentState = TransferState.UNKNOWN;
    private int count = 10;
    Toolbar myToolbar;
    TextView contactName, contactDescription, otherContactName, otherContactDescription;
    private static TextView notifyText, chatText;
    private static int chatCount = 0, notifyCount = 0, updateChatCount = 0;
    public static Menu menu;
    private Bitmap bm = null, bm1 = null, bm2 = null;
    private ImageView profileImage1, profileImage2;
    private RoundImage roundedImage;
    String cName = null;
    String oName = null;
    String type = null;
    String otype = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        myGlobals.myMessageActivity = this;
        myGlobals.activityInForeGround = true;

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.chat_message);

        myGlobals.bmEngineer = loadProfileImage(myGlobals.currJobActive.getEngineerEmail());
        myGlobals.bmTechnician = loadProfileImage(myGlobals.currJobActive.getTechEmail());
        myGlobals.bmDoctor = loadProfileImage(myGlobals.currJobActive.getDoctorEmail());

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.getMenu().clear();

        Bundle bundle = getIntent().getExtras();
        messageActivity = this;

        callerName = bundle.getString("agentName");
        callerPhone = bundle.getString("callerPhone");
        productId = myGlobals.jobId;

        checkUnSeenMessageCount(callerPhone);

        ((TextView) myToolbar.findViewById(R.id.job_id)).setText(myGlobals.jobId);
        ((TextView) myToolbar.findViewById(R.id.patient_id)).setText(myGlobals.patientId);
        ((TextView) myToolbar.findViewById(R.id.job_id)).setTypeface(myGlobals.centuryGothic);
        ((TextView) myToolbar.findViewById(R.id.patient_id)).setTypeface(myGlobals.centuryGothic);
        ((TextView) myToolbar.findViewById(R.id.job_id)).setTextColor(getResources().getColor(R.color.colorPrimary));
        ((TextView) myToolbar.findViewById(R.id.patient_id)).setTextColor(getResources().getColor(R.color.colorPrimary));

        contactName = (TextView)findViewById(R.id.contact_name);
        contactName.setTypeface(myGlobals.centuryGothic);
        contactDescription = (TextView)findViewById(R.id.contact_Description);
        contactDescription.setTypeface(myGlobals.centuryGothic);
        otherContactName = (TextView)findViewById(R.id.other_contact_name);
        otherContactName.setTypeface(myGlobals.centuryGothic);
        otherContactDescription = (TextView)findViewById(R.id.other_contact_Description);
        otherContactDescription.setTypeface(myGlobals.centuryGothic);

        LinearLayout backButton = (LinearLayout)myToolbar.findViewById(R.id.message_back_lyt);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);

        chatMessages = new LinkedHashMap<Long, ChatMessages>();

        List<ChatMessages> messageList = myGlobals.dbHandler.getMessageLogsByPhoneNProductId(callerPhone, myGlobals.jobId);
        if(messageList != null) {
            logInfo("messageList", "Count " + messageList.size());
            for (ChatMessages chatMessages : messageList) {
                this.chatMessages.put(chatMessages.getMessageId(), new ChatMessages(chatMessages.getMessage(), chatMessages.getDate(), chatMessages.getTime(), chatMessages.getMessageSentOrReceived(), chatMessages.getMessageId(), chatMessages.getMessageDeliveryStatus(), chatMessages.getMode(), chatMessages.getFileExt(), chatMessages.getFileName(), chatMessages.getFileSize(), chatMessages.getMimeType(), chatMessages.getThumbPath(), chatMessages.getFilePath(), chatMessages.getDurationTime(), chatMessages.getUploadedResource(), chatMessages.getMediaLink(), chatMessages.getSeenstatus()));
            }
        }

        profileImage1 = (ImageView) findViewById(R.id.profile_image1);
        if(myGlobals.loggedinUserType == 1) {
            bm = myGlobals.bmDoctor;
            bm1 = myGlobals.bmEngineer;
            bm2 = myGlobals.bmTechnician;
            cName = myGlobals.currJobActive.getTechFname();
            type = "Technician";
        } else if(myGlobals.loggedinUserType == 2) {
            bm = myGlobals.bmTechnician;
            bm1 = myGlobals.bmEngineer;
            bm2 = myGlobals.bmDoctor;
            cName = myGlobals.currJobActive.getDoctorFname();
            type = "Doctor";
        } else if(myGlobals.loggedinUserType == 3) {
            bm = myGlobals.bmEngineer;
            bm1 = myGlobals.bmDoctor;
            bm2 = myGlobals.bmTechnician;
            cName = myGlobals.currJobActive.getTechFname();
            type = "Technician";
        }

        if(bm1 == null)
            bm1 = BitmapFactory.decodeResource(getResources(),R.drawable.avatar);
        roundedImage = new RoundImage(bm1);
        profileImage1.setImageDrawable(roundedImage);

        profileImage2 = (ImageView) findViewById(R.id.profile_image2);
        if((cName != null) && (cName != "") && (!cName.equals("null")) && (cName.length() > 0)) {
            if(bm2 == null)
                bm2 = BitmapFactory.decodeResource(getResources(),R.drawable.avatar);
            roundedImage = new RoundImage(bm2);
            profileImage2.setImageDrawable(roundedImage);
            profileImage2.setOnClickListener(this);
        } else {
            profileImage2.setVisibility(View.GONE);
            otherContactName.setVisibility(View.GONE);
            otherContactDescription.setVisibility(View.GONE);
            profileImage2 = null;
        }

        setCurrentChatUserName();

        msg_edittext = (EditText) findViewById(R.id.messageEditText);
        msg_edittext.setTypeface(myGlobals.centuryGothic);
        Button sendButton = (Button) findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(this);
        ImageView attachment = (ImageView) findViewById(R.id.attachment);
        attachment.setOnClickListener(this);

        msgListView = (ListView) findViewById(R.id.msgListView);
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);
        messageViewAdapter = new MessageAdapter(this, chatMessages, productId);
        messageViewAdapter.setProfileImages(myGlobals.bmLoggedinUser, bm1);

        msgListView.setAdapter(messageViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        myGlobals.chatCount -= updateChatCount;
        if(myGlobals.chatCount < 0)
            myGlobals.chatCount = 0;

        updateChatCount = 0;

        chatCount = myGlobals.chatCount;
        logInfo("myGlobals.chatCount", myGlobals.chatCount);
        logInfo("chatCount", chatCount);

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
                int count = 0;

                List<MyChatNotificationsRowItem> notificationsList = myGlobals.dbHandler.getUnReadMessages();
                if((notificationsList != null) && (notificationsList.size() > 0)) {
                    count = notificationsList.size();
                }
                if(count == 1) {
                    if (myGlobals.prevJobActive != null) {
                        myGlobals.currJobActive = myGlobals.prevJobActive;
                        myGlobals.jobId = myGlobals.prevJobActive.getJobId();
                        myGlobals.patientId = myGlobals.prevJobActive.getPatientId();
                        myGlobals.patientName = myGlobals.prevJobActive.getPatientName();
                        String hospitalId = myGlobals.prevJobActive.getHospitalId();
                        HospitalInfo hospitalInfo = myGlobals.dbHandler.getHospital(hospitalId);
                        if(hospitalInfo != null) {
                            myGlobals.hospitalName = hospitalInfo.getHospitalName();
                            myGlobals.cityName = hospitalInfo.getHospitalCity();
                        }
                    }
                    finish();
                    myGlobals.myMessageActivity = null;
                }
                launchChatNotificationListActivity(true);
            }
        });

        itemMessages = menu.findItem(R.id.stop);
        itemMessages.setVisible(false);

        itemMessages = menu.findItem(R.id.delete);
        itemMessages.setVisible(false);

        itemMessages = menu.findItem(R.id.archive);
        itemMessages.setVisible(false);

        super.onCreateOptionsMenu(menu);
        //updateChatNotifications(true);
        updateUnreadMessagesCount();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final View menuItemView;

        if(id == R.id.action) {
            logInfo("Clicked on ", "Action Button ..............");
            menuItemView = findViewById(R.id.action);
            showMenuPopup(menuItemView);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMenuPopup(View view) {
        String[] menuText = getResources().getStringArray(R.array.menu);
        int[] images = { R.drawable.edit_profile , R.drawable.password};
        Bitmap[] bitmaps = new Bitmap[2];
        GridView menuGrid;

        bitmaps[0] = myGlobals.bmLoggedinUser;
        if(bitmaps[0] == null)
            bitmaps[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.avatar);

        myGlobals.showPopup = PopupHelper.newBasicPopupWindow(getApplicationContext());
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.custom_menu_layout, null);
        menuGrid = (GridView) popupView.findViewById(R.id.grid_menu_layout);
        CustomGrid menuAdapter = new CustomGrid(getApplicationContext(), menuText, images, null, false);
        menuGrid.setAdapter(menuAdapter);
        myGlobals.showPopup.setContentView(popupView);
        myGlobals.showPopup.setWidth(Toolbar.LayoutParams.WRAP_CONTENT);
        myGlobals.showPopup.setHeight(Toolbar.LayoutParams.WRAP_CONTENT);

        myGlobals.showPopup.setAnimationStyle(R.style.Animations_GrowFromTop);
        myGlobals.showPopup.showAsDropDown(view);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (updateChatCount > 0) {
            if(myGlobals.chatCount >= updateChatCount) {
                myGlobals.chatCount -= updateChatCount;
            }
            updateChatCount = 0;
        }
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
        if(myGlobals.myJobDetailsActivity != null) {
            if (myGlobals.myJobDetailsActivity instanceof MyJobDetailsActivity) {
                ((MyJobDetailsActivity) myGlobals.myJobDetailsActivity).onPrepareOptionsMenu(MyJobDetailsActivity.menu);
            }
        }
        if(myGlobals.myJobsActivity != null) {
            if (myGlobals.myJobsActivity instanceof MyJobsActivity) {
                ((MyJobsActivity) myGlobals.myJobsActivity).onPrepareOptionsMenu(MyJobsActivity.menu);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public static void launchDocumentPicker() {
        if(myGlobals.showPopup != null) {
            myGlobals.showPopup.dismiss();
        }
        logInfo("chatdocument","chatdocument");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FILE_REQUEST);
    }

    public static void launchCameraIntent() {
        if(myGlobals.showPopup != null) {
            myGlobals.showPopup.dismiss();
        }

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public static void launchGalleryIntent() {
        if(myGlobals.showPopup != null) {
            myGlobals.showPopup.dismiss();
        }
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(Intent.createChooser(i, "Select Image"), PICK_IMAGE_REQUEST);
    }

    public static void launchAudioIntent() {
        if(myGlobals.showPopup != null) {
            myGlobals.showPopup.dismiss();
        }
        List<Intent> targetedAudioIntents = new ArrayList<Intent>();

        Intent audioRecorder = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        Intent meidaIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        targetedAudioIntents.add(audioRecorder);
        targetedAudioIntents.add(meidaIntent);
        Intent chooserIntent = Intent.createChooser(targetedAudioIntents.remove(0), "");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedAudioIntents.toArray(new Parcelable[targetedAudioIntents.size()]));
        activity.startActivityForResult(chooserIntent, PICK_AUDIO_REQUEST);
    }

    public static void launchVideoIntent() {
        if(myGlobals.showPopup != null) {
            myGlobals.showPopup.dismiss();
        }

        List<Intent> targetedVideoIntents = new ArrayList<Intent>();

        Intent videoRecorder = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Intent mediaIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        targetedVideoIntents.add(mediaIntent);
        targetedVideoIntents.add(videoRecorder);

        Intent videoIntent = Intent.createChooser(targetedVideoIntents.remove(0), "");
        videoIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedVideoIntents.toArray(new Parcelable[targetedVideoIntents.size()]));
        activity.startActivityForResult( videoIntent, PICK_VIDEO_REQUEST);
    }

    public static long getMessageId(ImageView imageView) {
        return messageViewAdapter.getMessageId(imageView);
    }

    public static Uri getImageUri(ImageView imageView) {
        return messageViewAdapter.getImageUri(imageView);
    }

    public static void clearChat() {
        if(myGlobals.showPopup != null) {
            myGlobals.showPopup.dismiss();
        }
        myGlobals.dbHandler.clearMessage(callerPhone, myGlobals.jobId);

        messageViewAdapter.clear();
        messageViewAdapter.notifyDataSetChanged();
    }

    public void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        Uri uri = null;
        logInfo("onActivityResult", "Called ..............");
        if (resultCode == RESULT_OK) {
            switch(requestCode) {
                case PICK_FILE_REQUEST:
                    uri = data.getData();

                    try {
                        onDocumentPickerSuccess(context, uri);
                    } catch (URISyntaxException e) {
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    }
                    break;
                case CAMERA_REQUEST:
                    logInfo("onActivityResult", "CAMERA_REQUEST ..............");

                    selectedImageUri = data.getData();
                    thumbnail = (Bitmap) data.getExtras().get("data");
                    compressedImage = compressBitmap(thumbnail, 40);
                    if(thumbnail != null)
                        logInfo("onActivityResult", "selectedImageUri ..............thumbnail ! NULL");
                    else
                        logInfo("onActivityResult", "selectedImageUri ..............thumbnail = NULL");

                    onSelectFromGalleryResult(context, selectedImageUri, null);
                    break;
                case PICK_IMAGE_REQUEST:
                    selectedImageUri = data.getData();

                    onImagePickerSuccess(context, selectedImageUri);
                    break;
                case PICK_AUDIO_REQUEST:
                    logInfo("PICK_AUDIO_REQUEST", String.valueOf(PICK_AUDIO_REQUEST));

                    selectAudioUri = data.getData();
                    logInfo("PICK_AUDIO_REQUEST", selectAudioUri.toString());
                    try {
                        onAudioPickerSuccess(context, selectAudioUri);
                    } catch (URISyntaxException e) {
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    }
                    break;
                case PICK_VIDEO_REQUEST:
                    uri = data.getData();

                    try {
                        onVideoCaptureSuccess(context, uri);
                    } catch (URISyntaxException e) {
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    }
                    break;
            }
        } else if(resultCode == RESULT_CANCELED) {
            logInfo("onActivityResult", "Cancelled Called .............");
            switch(requestCode) {
                case ACTION_VIEW_GALLERY :
                    break;
            }
        } else {
            logInfo("onActivityResult", "resultCode ............." + resultCode);
            switch (requestCode) {
                case ACTION_VIEW_GALLERY:
                    break;
            }
        }
    }

    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.sendMessageButton){
            sendTextMessage(v);
        } else if(id == R.id.message_back_lyt) {
            onBackPressed();
        } else if(id == R.id.profile_image2) {
            boolean switchChat = false;
            String temp = cName;
            cName = oName;
            oName = temp;

            temp = type;
            type = otype;
            otype = temp;

            if(myGlobals.loggedinUserType == 1) { // Currently Doctor loggedin
                logInfo("Logged in as", "Doctor");
                if(myGlobals.currentChatUserType == 3) {
                    //cName = myGlobals.currJobActive.getTechFname();
                    //if((cName != null) && (cName != "") && (!cName.equals("null")) && (cName.length() > 0)) {
                    if(profileImage2 != null) {
                        //bm1 = myGlobals.bmTechnician;
                        //bm2 = myGlobals.bmEngineer;
                        switchChat = true;
                        logInfo("Switching to", "Technician");
                        myGlobals.currentChatUserType = 2;
                        //type = "Technician";
                        callerName = cName;
                        callerPhone = "+91" + myGlobals.currJobActive.getTechEmail();
                    }
                } else {
                    //cName = myGlobals.currJobActive.getEngineerFname();
                    //if((cName != null) && (cName != "") && (!cName.equals("null")) && (cName.length() > 0)) {
                    if(profileImage2 != null) {
                        //bm1 = myGlobals.bmEngineer;
                        //bm2 = myGlobals.bmTechnician;
                        switchChat = true;
                        logInfo("Switching to", "Engineer");
                        myGlobals.currentChatUserType = 3;
                        //type = "Engineer";
                        callerName = cName;
                        callerPhone = "+91" + myGlobals.currJobActive.getEngineerEmail();
                    }
                }
            } else if(myGlobals.loggedinUserType == 2) { // Currently Technician loggedin
                logInfo("Logged in as", "Technician");
                if(myGlobals.currentChatUserType == 3) {
                    //cName = myGlobals.currJobActive.getDoctorFname();
                    //if((cName != null) && (cName != "") && (!cName.equals("null")) && (cName.length() > 0)) {
                    if(profileImage2 != null) {
                        //bm1 = myGlobals.bmDoctor;
                        //bm2 = myGlobals.bmEngineer;
                        switchChat = true;
                        logInfo("Switching to", "Doctor");
                        myGlobals.currentChatUserType = 1;
                        //type = "Doctor";
                        callerName = cName;
                        callerPhone = "+91" + myGlobals.currJobActive.getDoctorEmail();
                    }
                } else {
                    //cName = myGlobals.currJobActive.getEngineerFname();
                    //if((cName != null) && (cName != "") && (!cName.equals("null")) && (cName.length() > 0)) {
                    if(profileImage2 != null) {
                        //bm1 = myGlobals.bmEngineer;
                        //bm2 = myGlobals.bmDoctor;
                        switchChat = true;
                        logInfo("Switching to", "Engineer");
                        myGlobals.currentChatUserType = 3;
                        //type = "Engineer";
                        callerName = cName;
                        callerPhone = "+91" + myGlobals.currJobActive.getEngineerEmail();
                    }
                }
            } else if(myGlobals.loggedinUserType == 3) { // Currently Engineer loggedin
                logInfo("Logged in as", "Engineer");
                if(myGlobals.currentChatUserType == 1) {
                    //cName = myGlobals.currJobActive.getTechFname();
                    //if((cName != null) && (cName != "") && (!cName.equals("null")) && (cName.length() > 0)) {
                    if(profileImage2 != null) {
                        //bm1 = myGlobals.bmTechnician;
                        //bm2 = myGlobals.bmDoctor;
                        switchChat = true;
                        logInfo("Switching to", "Technician");
                        myGlobals.currentChatUserType = 2;
                        //type = "Technician";
                        callerName = cName;
                        callerPhone = "+91" + myGlobals.currJobActive.getTechEmail();
                    }
                } else {
                    //cName = myGlobals.currJobActive.getDoctorFname();
                    //if((cName != null) && (cName != "") && (!cName.equals("null")) && (cName.length() > 0)) {
                    if(profileImage2 != null) {
                        //bm1 = myGlobals.bmDoctor;
                        //bm2 = myGlobals.bmTechnician;
                        switchChat = true;
                        logInfo("Switching to", "Doctor");
                        myGlobals.currentChatUserType = 1;
                        //type = "Doctor";
                        callerName = cName;
                        callerPhone = "+91" + myGlobals.currJobActive.getDoctorEmail();
                    }
                }
            }

            if(profileImage2 != null) {
                logInfo("Refreshing ", "Messages .......");
                logInfo("cName", cName);
                logInfo("type", type);

/*
                String temp = cName;
                cName = oName;
                oName = temp;

                temp = type;
                type = otype;
                otype = temp;
*/

                Bitmap tmp = bm1;
                bm1 = bm2;
                bm2 = tmp;

                roundedImage = new RoundImage(bm1);
                profileImage1.setImageDrawable(roundedImage);
                //if(profileImage2 != null) {
                    roundedImage = new RoundImage(bm2);
                    profileImage2.setImageDrawable(roundedImage);
                //}

                contactName.setText(cName);
                contactDescription.setText(type);
                //Need to change the profile picture
                otherContactName.setText(oName);
                otherContactDescription.setText(otype);

                checkUnSeenMessageCount(callerPhone);
                //updateChatNotifications(true);
                updateUnreadMessagesCount();

                messageViewAdapter.clear();
                chatMessages = new LinkedHashMap<Long, ChatMessages>();
                List<ChatMessages> messageList = myGlobals.dbHandler.getMessageLogsByPhoneNProductId(callerPhone, myGlobals.jobId);
                if (messageList != null) {
                    logInfo("messageList", "Count " + messageList.size());
                    for (ChatMessages chatMessages : messageList) {
                        messageViewAdapter.add(new ChatMessages(chatMessages.getMessage(), chatMessages.getDate(), chatMessages.getTime(), chatMessages.getMessageSentOrReceived(), chatMessages.getMessageId(), chatMessages.getMessageDeliveryStatus(), chatMessages.getMode(), chatMessages.getFileExt(), chatMessages.getFileName(), chatMessages.getFileSize(), chatMessages.getMimeType(), chatMessages.getThumbPath(), chatMessages.getFilePath(), chatMessages.getDurationTime(), chatMessages.getUploadedResource(), chatMessages.getMediaLink(), chatMessages.getSeenstatus()));
                    }
                }
                messageViewAdapter.setProfileImages(myGlobals.bmLoggedinUser, bm1);
                messageViewAdapter.notifyDataSetChanged();
            }
        } else if(id == R.id.attachment) {
            showAttachmentPopup(v);
        }

    }

    private void updateChatNotifications(boolean self) {
        if (updateChatCount > 0) {
            if(myGlobals.chatCount >= updateChatCount) {
                myGlobals.chatCount -= updateChatCount;
                if(self)
                    this.onPrepareOptionsMenu(this.menu);
            }
            updateChatCount = 0;
        }
    }

    private void checkUnSeenMessageCount(String callerPhone) {
        updateChatCount = myGlobals.dbHandler.getUnseenMessagesCountByProductId(callerPhone, myGlobals.jobId);
        logInfo("myGlobals.chatCount", myGlobals.chatCount);
        logInfo("count", updateChatCount);
        if (updateChatCount > 0) {
            myGlobals.trovaApi.despatchChatEvents(0, "+" + myGlobals.userId, callerPhone, myGlobals.jobId, EVENT_MESSAGE_READ);
        }
    }

    private void setCurrentChatUserName() {
        cName = myGlobals.currJobActive.getDoctorFname();
        oName = myGlobals.currJobActive.getTechFname();
        type = "Doctor";
        otype = "Technician";
        if (myGlobals.loggedinUserType == 1) {
            cName = myGlobals.currJobActive.getEngineerFname();
            oName = myGlobals.currJobActive.getTechFname();
            type = "Engineer";
            otype = "Technician";
            myGlobals.currentChatUserType = 3;
        } else  if (myGlobals.loggedinUserType == 2) {
            cName = myGlobals.currJobActive.getEngineerFname();
            oName = myGlobals.currJobActive.getDoctorFname();
            type = "Engineer";
            otype = "Doctor";
            myGlobals.currentChatUserType = 3;
        }
        contactName.setText(cName);
        contactDescription.setText(type);
        otherContactName.setText(oName);
        otherContactDescription.setText(otype);
    }

    private void showAttachmentPopup(View view) {
        String[] menuText = getResources().getStringArray(R.array.attachment);
        int[] images = { R.drawable.document, R.drawable.camera,
                R.drawable.gallery,R.drawable.video,
                R.drawable.audio };
        GridView menuGrid;
        myGlobals.showPopup = PopupHelper.newBasicPopupWindow(getApplicationContext());
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.custom_grid_layout, null);

        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        parms.setMargins(20, 0, 20, 0);
        popupView.setLayoutParams(parms);

        menuGrid = (GridView) popupView.findViewById(R.id.grid_layout);
        CustomGrid adapter = new CustomGrid(getApplicationContext(), menuText, images, null, false);
        menuGrid.setAdapter(adapter);
        myGlobals.showPopup.setContentView(popupView);

/*
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int height = Math.round(displayMetrics.heightPixels / displayMetrics.density);
        int width = Math.round(displayMetrics.widthPixels / displayMetrics.density);
*/

        myGlobals.showPopup.setWidth(Toolbar.LayoutParams.MATCH_PARENT);
        myGlobals.showPopup.setHeight(Toolbar.LayoutParams.WRAP_CONTENT);
        myGlobals.showPopup.setAnimationStyle(R.style.Animations_GrowFromTop);
        Rect rect = new Rect();
        view.getLocalVisibleRect(rect);

        logInfo("showAttachmentPopup", rect.width() + "=" + rect.height() + "=" + rect.left + "=" + rect.top + "=" + rect.right + "=" + rect.bottom);

        //myGlobals.showPopup.showAsDropDown(view);
        myGlobals.showPopup.showAsDropDown(view, 10, (rect.height()), Gravity.CENTER);
    }

    public void sendTextMessage(View v) {
        String message = msg_edittext.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {
            CommonDateTimeZone commonDateTimeZone=new CommonDateTimeZone();
            ChatMessages chatMessage = new ChatMessages(message,commonDateTimeZone.currDate,commonDateTimeZone.time,1, commonDateTimeZone.messageId,"false","text", null, null, 0, "text", null, null, null, 1, null, 0);
            msg_edittext.setText("");
            try {
                addMessage(chatMessage);
                myGlobals.dbHandler.addMessageLogs(new ChatMessages(0, callerName, callerPhone, productId, message, commonDateTimeZone.currDate, commonDateTimeZone.time, "text", null, null, 0, "text", null, null, null, 0, 1, 0, commonDateTimeZone.messageId, "false", 0, commonDateTimeZone.timezone, commonDateTimeZone.timemilliseconds, 1, null, 0));
                myGlobals.trovaApi.despatchMessage(message, productId, commonDateTimeZone.messageId, callerPhone);
            }catch (Exception e){
                Crashlytics.logException(e);
                e.printStackTrace();
            }
        }
    }

    public static void addMessage(ChatMessages chatMessage) {
        logInfo("ChatMessages",chatMessage.toString());
        messageViewAdapter.add(chatMessage);
        messageViewAdapter.notifyDataSetChanged();
    }

    public void updateReadMessage() {
        try {
            messageViewAdapter.updateReadMessage();
            messageViewAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    public void updatemessageId(long mesgId) {
        try {
            messageViewAdapter.updatemessageId(mesgId);
            messageViewAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    String getBits(byte b)
    {
        String result = "";
        for(int i = 0; i < 8; i++){
            result += (b & (1 << i)) == 0 ? "0" : "1";
            System.out.println(result);
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        myGlobals.myMessageActivity = this;
        logInfo("MessageActivity", "onResume Called ............");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logInfo("MessageActivity", "onPause Called ............");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logInfo("MessageActivity", "onDestroy Called .......");
        messageActivity =null;
        myGlobals.myMessageActivity = null;
    }

    @Override
    public void onBackPressed() {
        myGlobals.myMessageActivity = null;
        if ((myGlobals.myJobDetailsActivity == null) && (myGlobals.myJobsActivity == null)){
            exitApplication();
        } else {
            if((myGlobals.myJobsActivity != null) && (myGlobals.myJobDetailsActivity == null)) {
                launchDetailsActivity(myGlobals.currJobActive);
            }
            finish();
            if(myGlobals.prevJobActive != null) {
                myGlobals.currJobActive = myGlobals.prevJobActive;
                myGlobals.jobId = myGlobals.prevJobActive.getJobId();
                myGlobals.patientId = myGlobals.prevJobActive.getPatientId();
                myGlobals.patientName = myGlobals.prevJobActive.getPatientName();
                String hospitalId = myGlobals.prevJobActive.getHospitalId();
                HospitalInfo hospitalInfo = myGlobals.dbHandler.getHospital(hospitalId);
                if(hospitalInfo != null) {
                    myGlobals.hospitalName = hospitalInfo.getHospitalName();
                    myGlobals.cityName = hospitalInfo.getHospitalCity();
                }
            }
            if(myGlobals.myJobDetailsActivity != null)
                ((MyJobDetailsActivity) myGlobals.myJobDetailsActivity).resetPreviousTabSelected();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public static void exitApplication() {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.finishAndRemoveTask();
        } else {
            activity.finish();
        }
    }

}
