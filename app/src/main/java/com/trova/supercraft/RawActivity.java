package com.trova.supercraft;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.SuperCraftUtils.launchChatNotificationListActivity;
import static com.trova.supercraft.SuperCraftUtils.launchNotificationListActivity;
import static com.trova.supercraft.SuperCraftUtils.logInfo;
import static com.trova.supercraft.SuperCraftUtils.updateUnreadMessagesCount;

/**
 * Created by Panchakshari on 30/3/2017.
 */

public class RawActivity extends AppCompatActivity implements View.OnClickListener {
    public static Context context;
    public static Activity activity;
    Toolbar myToolbar;
    private static TextView notifyText, chatText;
    private static int chatCount = 0, notifyCount = 0;
    public static Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        setContentView(R.layout.raw_view_layout);
        myGlobals.myRawActivity = this;

        TextView patientName = (TextView) findViewById(R.id.patient_name);
        patientName.setText(myGlobals.patientName);
        patientName.setTypeface(myGlobals.centuryGothic);
        if((myGlobals.hospitalName != null) && (!myGlobals.hospitalName.equals("null") && (!myGlobals.hospitalName.equals("")) && (myGlobals.hospitalName.length() > 0))) {
            ((TextView) findViewById(R.id.hospital_name)).setText(myGlobals.hospitalName);
            ((TextView) findViewById(R.id.hospital_name)).setTypeface(myGlobals.centuryGothic);
        }
        if((myGlobals.cityName != null) && (!myGlobals.cityName.equals("null") && (!myGlobals.cityName.equals("")) && (myGlobals.cityName.length() > 0))) {
            ((TextView) findViewById(R.id.city_name)).setText(myGlobals.cityName);
            ((TextView) findViewById(R.id.city_name)).setTypeface(myGlobals.centuryGothic);
        }

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.getMenu().clear();
        ((TextView) myToolbar.findViewById(R.id.job_id)).setText(myGlobals.jobId);
        ((TextView) myToolbar.findViewById(R.id.patient_id)).setText(myGlobals.patientId);
        ((TextView) myToolbar.findViewById(R.id.job_id)).setTypeface(myGlobals.centuryGothic);
        ((TextView) myToolbar.findViewById(R.id.patient_id)).setTypeface(myGlobals.centuryGothic);
        ((TextView) myToolbar.findViewById(R.id.job_id)).setTextColor(getResources().getColor(R.color.colorPrimary));
        ((TextView) myToolbar.findViewById(R.id.patient_id)).setTextColor(getResources().getColor(R.color.colorPrimary));
        LinearLayout backButton = (LinearLayout)myToolbar.findViewById(R.id.message_back_lyt);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);

        TextView tv = null;
        LinearLayout ll = (LinearLayout) findViewById(R.id.dicom_lyt);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        String mriFile = myGlobals.currJobActive.getMriFile();
        String ctFile = myGlobals.currJobActive.getCtFile();
        String usFile = myGlobals.currJobActive.getUsFile();
        String echoFile = myGlobals.currJobActive.getEchoFile();
        String otherFile = myGlobals.currJobActive.getOtherFile();

        if((mriFile != null) && (!mriFile.equals("")) && (!mriFile.equals("null")) && (mriFile.length() > 0)) {
            mriFile = mriFile.substring(mriFile.lastIndexOf("/") + 1).trim();
            tv = new TextView(context);
            tv.setPadding(25, 5, 5, 5);
            tv.setText("MRI: " + mriFile);
            tv.setLayoutParams(lp);
            tv.setTypeface(myGlobals.centuryGothic);
            ll.addView(tv);
        }

        if((ctFile != null) && (!ctFile.equals("")) && (!ctFile.equals("null")) && (ctFile.length() > 0)) {
            ctFile = ctFile.substring(ctFile.lastIndexOf("/") + 1).trim();
            tv = new TextView(context);
            tv.setPadding(25, 5, 5, 5);
            tv.setText("CT: " + ctFile);
            tv.setLayoutParams(lp);
            tv.setTypeface(myGlobals.centuryGothic);
            ll.addView(tv);
        }

        if((usFile != null) && (!usFile.equals("")) && (!usFile.equals("null")) && (usFile.length() > 0)) {
            usFile = usFile.substring(usFile.lastIndexOf("/") + 1).trim();
            tv = new TextView(context);
            tv.setPadding(25, 5, 5, 5);
            tv.setText("US: " + usFile);
            tv.setLayoutParams(lp);
            tv.setTypeface(myGlobals.centuryGothic);
            ll.addView(tv);
        }

        if((echoFile != null) && (!echoFile.equals("")) && (!echoFile.equals("null")) && (echoFile.length() > 0)) {
            echoFile = echoFile.substring(echoFile.lastIndexOf("/") + 1).trim();
            tv = new TextView(context);
            tv.setPadding(25, 5, 5, 5);
            tv.setText("ECHO: " + echoFile);
            tv.setLayoutParams(lp);
            tv.setTypeface(myGlobals.centuryGothic);
            ll.addView(tv);
        }

        if((otherFile != null) && (!otherFile.equals("")) && (!otherFile.equals("null")) && (otherFile.length() > 0)) {
            otherFile = otherFile.substring(otherFile.lastIndexOf("/") + 1).trim();
            tv = new TextView(context);
            tv.setPadding(25, 5, 5, 5);
            tv.setText("OTHERS: " + otherFile);
            tv.setLayoutParams(lp);
            tv.setTypeface(myGlobals.centuryGothic);
            ll.addView(tv);
        }

        String dicomNotes = myGlobals.currJobActive.getDicomNotes();
        EditText etDicomNotes = (EditText) findViewById(R.id.dicom_notes);
        if((dicomNotes != null) && (!dicomNotes.equals("")) && (!dicomNotes.equals("null")) && (dicomNotes.length() > 0)) {
            dicomNotes = dicomNotes.replace("<br />", "\n");
            dicomNotes = dicomNotes.replace("<br/>", "\n");
            dicomNotes = dicomNotes.replace("<br >", "\n");
            dicomNotes = dicomNotes.replace("<br>", "\n");
            dicomNotes = dicomNotes + "Test\nTest\nTest\nTest\nTest\nTest\nTest\nTest\nTest";
            etDicomNotes.setText(dicomNotes);
            etDicomNotes.setTypeface(myGlobals.centuryGothic);
        }
        etDicomNotes.setKeyListener(null);

        ((TextView)findViewById(R.id.priority)).setTypeface(myGlobals.centuryGothic);
        if(myGlobals.currJobActive.getPriority().equals("critical"))
            ((TextView)findViewById(R.id.priority)).setText("Critical");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;

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
        chatText.setText(String.valueOf(chatCount));
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

        super.onCreateOptionsMenu(menu);
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
        chatCount = myGlobals.chatCount;
        notifyCount = myGlobals.notifyCount;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(chatCount > 0) {
                        chatText.setVisibility(View.VISIBLE);
                        chatText.setText(String.valueOf(chatCount));
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

    @Override
    protected  void onDestroy() {
        logInfo("MyRawActivity", "onDestroy Called .....");
        super.onDestroy();
        myGlobals.myRawActivity = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        myGlobals.myRawActivity = this;
        logInfo("MyRawActivity", "onResume Called .....");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.message_back_lyt) {
            myGlobals.myRawActivity = null;
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        myGlobals.myRawActivity = null;
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
