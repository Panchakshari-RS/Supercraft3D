package com.trova.supercraft;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.SuperCraftUtils.initTrovaChat;
import static com.trova.supercraft.SuperCraftUtils.launchChatNotificationListActivity;
import static com.trova.supercraft.SuperCraftUtils.launchNotificationListActivity;
import static com.trova.supercraft.SuperCraftUtils.logInfo;
import static com.trova.supercraft.SuperCraftUtils.updateUnreadMessagesCount;

/**
 * Created by Panchakshari on 15/3/2017.
 */

public class MyJobDetailsActivity extends AppCompatActivity implements CordovaInterface,View.OnClickListener {
    private Context context;
    private static Activity activity;
    int count = 1;
    Toolbar myToolbar;
    private static CustomViewPager viewPager = null;
    private static int previousTabSelected = -1;
    private static TextView notifyText, chatText;
    private static int chatCount = 0, notifyCount = 0;
    public static Menu menu;
    private TextView stlView, detailsView, notesView, chatView, approveView;
    private TextView stlViewText, detailsViewText, notesViewText, chatViewText, approveViewText;
    Dialog dialog = null;
    private static Handler handler = null;
    private static boolean success = false;
    private static ProgressDialog progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        myGlobals.myJobDetailsActivity = this;

        checkChatEnabled();

        logInfo("MyJobDetailsActivity", "onCreate Called .........");
        setContentView(R.layout.job_details_view);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.getMenu().clear();

        ((TextView)myToolbar.findViewById(R.id.job_id)).setText(myGlobals.jobId);
        ((TextView)myToolbar.findViewById(R.id.patient_id)).setText(myGlobals.patientId);
        ((TextView) myToolbar.findViewById(R.id.job_id)).setTypeface(myGlobals.centuryGothic);
        ((TextView) myToolbar.findViewById(R.id.patient_id)).setTypeface(myGlobals.centuryGothic);
        ((TextView)myToolbar.findViewById(R.id.job_id)).setTextColor(getResources().getColor(R.color.colorPrimary));
        ((TextView)myToolbar.findViewById(R.id.patient_id)).setTextColor(getResources().getColor(R.color.colorPrimary));
        //((TextView)myToolbar.findViewById(R.id.patient_name)).setText(patientName);

        LinearLayout backButton = (LinearLayout)myToolbar.findViewById(R.id.message_back_lyt);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);

        logInfo("MyJobDetailsActivity", "onCreate Called .........");

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.jobs_tab_layout);

        tabLayout.addTab(tabLayout.newTab());
        View view = LayoutInflater.from(this).inflate(R.layout.stl_viewer_tab_button,tabLayout,false);
        detailsView = (TextView)view.findViewById(R.id.stl_view);
        detailsViewText = (TextView)view.findViewById(R.id.stl_view_text);
        detailsViewText.setText("Details");
        detailsViewText.setTypeface(myGlobals.centuryGothic);
        detailsView.setText(context.getResources().getString(R.string.mt_details));
        detailsView.setTextColor(getResources().getColor(R.color.colorPrimary));
        detailsViewText.setTextColor(getResources().getColor(R.color.colorPrimary));
        FontManager.markAsIconContainer(view.findViewById(R.id.stl_view), myGlobals.materialIconFont);
        tabLayout.getTabAt(0).setCustomView(view);

/*
        tabLayout.addTab(tabLayout.newTab());
        view = LayoutInflater.from(this).inflate(R.layout.stl_viewer_tab_button,tabLayout,false);
        notesView = (TextView)view.findViewById(R.id.stl_view);
        notesViewText = (TextView)view.findViewById(R.id.stl_view_text);
        notesViewText.setText("Notes");
        notesView.setText(context.getResources().getString(R.string.mt_notes));
        FontManager.markAsIconContainer(view.findViewById(R.id.stl_view), myGlobals.materialIconFont);
        tabLayout.getTabAt(1).setCustomView(view);
*/

        tabLayout.addTab(tabLayout.newTab());
        view = LayoutInflater.from(this).inflate(R.layout.stl_viewer_tab_button,tabLayout,false);
        chatView = (TextView)view.findViewById(R.id.stl_view);
        chatViewText = (TextView)view.findViewById(R.id.stl_view_text);
        chatViewText.setText("Chat");
        chatViewText.setTypeface(myGlobals.centuryGothic);
        chatView.setText(context.getResources().getString(R.string.mt_chat));
        if(!myGlobals.isChatEnabled)
            approveView.setTextColor(getResources().getColor(R.color.focus_color));
        FontManager.markAsIconContainer(view.findViewById(R.id.stl_view), myGlobals.materialIconFont);
        tabLayout.getTabAt(1).setCustomView(view);

        tabLayout.addTab(tabLayout.newTab());
        view = LayoutInflater.from(this).inflate(R.layout.stl_viewer_tab_button,tabLayout,false);
        stlView = (TextView)view.findViewById(R.id.stl_view);
        stlViewText = (TextView)view.findViewById(R.id.stl_view_text);
        stlViewText.setText("3D View");
        stlViewText.setTypeface(myGlobals.centuryGothic);
        stlView.setText(context.getResources().getString(R.string.mt_3dview));
        FontManager.markAsIconContainer(view.findViewById(R.id.stl_view), myGlobals.materialIconFont);
        tabLayout.getTabAt(2).setCustomView(view);

        tabLayout.addTab(tabLayout.newTab());
        view = LayoutInflater.from(this).inflate(R.layout.stl_viewer_tab_button,tabLayout,false);
        approveView = (TextView)view.findViewById(R.id.stl_view);
        approveViewText = (TextView)view.findViewById(R.id.stl_view_text);
        approveViewText.setText("Approve");
        approveViewText.setTypeface(myGlobals.centuryGothic);
        approveView.setText(context.getResources().getString(R.string.mt_o_circle_check));
        if(myGlobals.isCompletedJob)
            approveView.setTextColor(getResources().getColor(R.color.focus_color));
        FontManager.markAsIconContainer(view.findViewById(R.id.stl_view), myGlobals.materialIconFont);
        tabLayout.getTabAt(3).setCustomView(view);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (CustomViewPager) findViewById(R.id.jobs_pager);
        logInfo("Tab Count", tabLayout.getTabCount());
        viewPager.setPagingEnabled(false);
        viewPager.setOffscreenPageLimit(3);

        previousTabSelected = 0;

        final MyJobsDetailsAdapter adapter = new MyJobsDetailsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                logInfo("MyJobDetailsActivity", "Selected ..........." + tab.getPosition() + " previousTabSelected " + previousTabSelected);
                onTabReselected(tab);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(previousTabSelected != tab.getPosition()) {
                    logInfo("onTabReselected", tab.getPosition());
                    previousTabSelected = tab.getPosition();
                    if((tab.getPosition() != 1) && (tab.getPosition() != 3))
                        viewPager.setCurrentItem(tab.getPosition());

                    stlView.setTextColor(getResources().getColor(R.color.focus_color));
                    stlViewText.setTextColor(getResources().getColor(R.color.focus_color));
                    detailsView.setTextColor(getResources().getColor(R.color.focus_color));
                    detailsViewText.setTextColor(getResources().getColor(R.color.focus_color));
                    //notesView.setTextColor(getResources().getColor(R.color.focus_color));
                    //notesViewText.setTextColor(getResources().getColor(R.color.focus_color));
                    chatView.setTextColor(getResources().getColor(R.color.focus_color));
                    chatViewText.setTextColor(getResources().getColor(R.color.focus_color));
                    approveView.setTextColor(getResources().getColor(R.color.focus_color));
                    approveViewText.setTextColor(getResources().getColor(R.color.focus_color));
                    switch (tab.getPosition()) {
                        case 2:
                            logInfo("Case", 2);
                            stlView.setTextColor(getResources().getColor(R.color.colorPrimary));
                            stlViewText.setTextColor(getResources().getColor(R.color.colorPrimary));
                            break;
                        case 0:
                            logInfo("Case", 0);
                            detailsView.setTextColor(getResources().getColor(R.color.colorPrimary));
                            detailsViewText.setTextColor(getResources().getColor(R.color.colorPrimary));
                            break;
/*
                        case 1:
                            notesView.setTextColor(getResources().getColor(R.color.colorPrimary));
                            notesViewText.setTextColor(getResources().getColor(R.color.colorPrimary));
                            break;
*/
                        case 1:
                            logInfo("Case", 1);
                            if(myGlobals.isChatEnabled) {
                                chatView.setTextColor(getResources().getColor(R.color.colorPrimary));
                                chatViewText.setTextColor(getResources().getColor(R.color.colorPrimary));
                                String agentKey = myGlobals.currJobActive.getDoctorKey();
                                if (myGlobals.loggedinUserType == 1)
                                    agentKey = myGlobals.currJobActive.getEngineerKey();

                                initTrovaChat(context, agentKey);
                            }
                            break;
                        case 3:
                            if(!myGlobals.isCompletedJob) {
                                approveView.setTextColor(getResources().getColor(R.color.colorPrimary));
                                approveViewText.setTextColor(getResources().getColor(R.color.colorPrimary));
                                confirmPostReport();
                            }
                            break;
                    }
                }
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.getMenu().clear();

    }

    private void checkChatEnabled() {
        myGlobals.isChatEnabled = false;
        String cName = null, oName = null;
        if(myGlobals.loggedinUserType == 1) {
            cName = "91" + myGlobals.currJobActive.getEngineerEmail();
            oName = "91" + myGlobals.currJobActive.getTechEmail();
        } else if(myGlobals.loggedinUserType == 2) {
            cName = "91" + myGlobals.currJobActive.getEngineerEmail();
            oName = "91" + myGlobals.currJobActive.getDoctorEmail();
        } else if(myGlobals.loggedinUserType == 3) {
            cName = "91" + myGlobals.currJobActive.getTechEmail();
            oName = "91" + myGlobals.currJobActive.getDoctorEmail();
        }

        if(((cName != null) && (!cName.equals("")) && (!cName.equals("null")) && (cName.length() > 0)) || ((oName != null) && (!oName.equals("")) && (!oName.equals("null")) && (oName.length() > 0))) {
            myGlobals.isChatEnabled = true;
        }

    }

    public static void resetPreviousTabSelected(){
        previousTabSelected  = -1;
    }

    private void confirmPostReport() {
        dialog = new Dialog(context);

        Rect displayRectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.activity_custom_dialog, null);
        layout.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.9f));

        ((TextView) layout.findViewById(R.id.tick)).setText("Job Approval");
        ((TextView) layout.findViewById(R.id.tick)).setTypeface(myGlobals.centuryGothic);
        ((TextView) layout.findViewById(R.id.thankyou)).setTypeface(myGlobals.centuryGothic);
        ((TextView) layout.findViewById(R.id.message)).setTypeface(myGlobals.centuryGothic);
        ((TextView) layout.findViewById(R.id.iam_done)).setTypeface(myGlobals.centuryGothic);
        FontManager.markAsIconContainer(layout.findViewById(R.id.tick), myGlobals.materialIconFont);

        dialog.setContentView(layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ((Button) dialog.findViewById(R.id.iam_done)).setOnClickListener(this);
        ((ImageView) dialog.findViewById(R.id.cancel)).setOnClickListener(this);

        dialog.show();
    }

    public static void setTabFocus2Position(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    protected void onPause() {
        super.onPause();
        logInfo("MyJobDetailsActivity", "onPause Called .....");
    }

    @Override
    protected  void onDestroy() {
        logInfo("MyJobDetailsActivity", "onDestroy Called .....");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        logInfo("MyJobDetailsActivity", "onResume Called .....");
    }

    @Override
    public void onBackPressed() {
        myGlobals.myJobDetailsActivity = null;
        if(myGlobals.myJobsActivity == null) {
            Intent intent = new Intent(this, MyJobsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }

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

    @Override
    public void startActivityForResult(CordovaPlugin cordovaPlugin, Intent intent, int i) {

    }

    @Override
    public void setActivityResultCallback(CordovaPlugin cordovaPlugin) {

    }

    @Override
    public Activity getActivity() {
        return null;
    }

    @Override
    public Object onMessage(String s, Object o) {
        return null;
    }

    @Override
    public ExecutorService getThreadPool() {
        return null;
    }

    private final ExecutorService threadPool = Executors.newCachedThreadPool();

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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.message_back_lyt) {
            this.finish();
        } else if(id == R.id.cancel) {
            previousTabSelected = -1;
            dialog.dismiss();
        } else if(id == R.id.iam_done) {
            previousTabSelected = -1;
            logInfo("Resetting Approve", "Tab ");

            progressBar = new ProgressDialog(context);
            progressBar.setCancelable(false);
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.setMessage("Approving ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    approveJob();
                }
            }, 10);
        }
    }

    private void approveJob() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if(success) {
                    //update db and refresh
                    myGlobals.dbHandler.updateJobStatus(myGlobals.jobId, 2);
                    updateAllJobs();
                }
                progressBar.dismiss();
                dialog.dismiss();
            }
        };

        new Thread() {
            public void run() {
                int status = 0;
                String notes = MyJobNotesTabFragment.getNotes();
                String stlFileId = myGlobals.currJobActive.getStlFileIds();
                if((stlFileId != null) && (!stlFileId.equals("")) && (stlFileId.length() > 0)) {
                    if(stlFileId.contains(","))
                        stlFileId = stlFileId.substring(stlFileId.lastIndexOf(',') + 1).trim();
                    String keyValuePairs = "notes=" + notes + "&isFromMobile=" + true + "&jobId=" + myGlobals.jobId + "&stlFileId=" + stlFileId;
                    String response = MyServiceHandler.sendRequest2Server(myGlobals.serverIp, "aprovejob", keyValuePairs, "POST", null);
                    try {
                        if ((response != null) && (!response.isEmpty())) {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            status = jsonObject.getInt("status");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (status == 1) {
                    success = true;
                    String callerPhone = null;
                    String message = null;
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("action", "APPROVED");
                        jsonObject.put("jobId", myGlobals.jobId);
                        jsonObject.put("name", myGlobals.userProfile.getUserName());
                        jsonObject.put("fromId", "91" + myGlobals.userProfile.getUserEmail());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String cName= null, oName = null;
                    message = jsonObject.toString();
                    logInfo("message", message);
                    if(myGlobals.loggedinUserType == 1) {
                        cName = myGlobals.currJobActive.getEngineerEmail();
                        oName = myGlobals.currJobActive.getTechEmail();
                    } else if(myGlobals.loggedinUserType == 2) {
                        cName = myGlobals.currJobActive.getEngineerEmail();
                        oName = myGlobals.currJobActive.getDoctorEmail();
                    } else if(myGlobals.loggedinUserType == 3) {
                        cName = myGlobals.currJobActive.getTechEmail();
                        oName = myGlobals.currJobActive.getDoctorEmail();
                    }
                    if((cName != null) && (cName != "") && (!cName.equals("null")) && (cName.length() > 0)) {
                        callerPhone = "91" + cName;
                        myGlobals.trovaApi.pushNotification(message, callerPhone, "high", "0");
                    }
                    if((oName != null) && (oName != "") && (!oName.equals("null")) && (oName.length() > 0)) {
                        callerPhone = "91" + oName;
                        myGlobals.trovaApi.pushNotification(message, callerPhone, "high", "0");
                    }

/*
                    message = jsonObject.toString();
                    logInfo("message", message);
                    if(myGlobals.loggedinUserType == 1) {
                        callerPhone = "91" + myGlobals.currJobActive.getEngineerEmail();
                        myGlobals.trovaApi.pushNotification(message, callerPhone, "high", "0");
                        callerPhone = "91" + myGlobals.currJobActive.getTechEmail();
                        myGlobals.trovaApi.pushNotification(message, callerPhone, "high", "0");
                    } else if(myGlobals.loggedinUserType == 2) {
                        callerPhone = "91" + myGlobals.currJobActive.getEngineerEmail();
                        myGlobals.trovaApi.pushNotification(message, callerPhone, "high", "0");
                        callerPhone = "91" + myGlobals.currJobActive.getDoctorEmail();
                        myGlobals.trovaApi.pushNotification(message, callerPhone, "high", "0");
                    } else if(myGlobals.loggedinUserType == 3) {
                        callerPhone = "91" + myGlobals.currJobActive.getTechEmail();
                        myGlobals.trovaApi.pushNotification(message, callerPhone, "high", "0");
                        callerPhone = "91" + myGlobals.currJobActive.getDoctorEmail();
                        myGlobals.trovaApi.pushNotification(message, callerPhone, "high", "0");
                    }
*/
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    public static void updateAllJobs() {
        logInfo("updateAllJobs", "Updating ............");
        MyNewJobsTabFragment.updateDataSet();
        MyInprogressJobsTabFragment.updateDataSet();
        MyCompletedJobsTabFragment.updateDataSet();
        MyCriticalJobsTabFragment.updateDataSet();
    }

}
