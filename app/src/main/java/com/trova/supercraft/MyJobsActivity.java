package com.trova.supercraft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.trova.supercraft.Profile.MyChangePasswordActivity;
import com.trova.supercraft.Profile.MyEditProfileActivity;

import static com.trova.supercraft.SuperCraftUtils.launchChatNotificationListActivity;
import static com.trova.supercraft.SuperCraftUtils.launchNotificationListActivity;
import static com.trova.supercraft.SuperCraftUtils.logInfo;
import static com.trova.supercraft.SuperCraftUtils.updateUnreadMessagesCount;

/**
 * Created by Panchakshari on 23/9/2016.
 */
public class MyJobsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    static boolean exit = false;

    private static Context context;
    private static Activity activity;
    private boolean done = false;
    private String LOG_TAG = "MyJobsActivity";
    private static Toolbar myToolbar;
    private static TabLayout tabLayout = null;
    private static ViewPager viewPager = null;
    public static Menu menu;
    private static TextView newCountImg, progressCountImg, completedCountImg, criticalCountImg;
    private static TextView newCount, progressCount, completedCount, criticalCount;
    private static TextView newText, progressText, completedText, criticalText;
    private static TextView notifyText, chatText;
    private static int chatCount = 0, notifyCount = 0;
    public static MyGlobals myGlobals = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;

        logInfo("MyJobsActivity", "0 .........");
        if(myGlobals == null)
            myGlobals = new MyGlobals(this);

        myGlobals.myJobsActivity = this;

        setContentView(R.layout.job_tab_view);
        logInfo("MyJobsActivity", "1 .........");

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.getMenu().clear();
        ((TextView)myToolbar.findViewById(R.id.job_id)).setText("Supercraft3D");
        ((TextView)myToolbar.findViewById(R.id.patient_id)).setText("");
        ((LinearLayout)myToolbar.findViewById(R.id.message_back_lyt)).setVisibility(View.GONE);
        ((ImageView)myToolbar.findViewById(R.id.supercraft_logo)).setVisibility(View.VISIBLE);

        tabLayout = (TabLayout) findViewById(R.id.jobs_tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        View view= LayoutInflater.from(this).inflate(R.layout.jobs_new_tab,tabLayout,false);
        newCountImg = (TextView)view.findViewById(R.id.new_job);
        newText = (TextView)view.findViewById(R.id.new_job_text);
        newText.setTypeface(myGlobals.centuryGothic);
        newCountImg.setText(context.getResources().getString(R.string.fa_new));
        FontManager.markAsIconContainer(newCountImg, myGlobals.fontAwesomeFont);
        newCount = (TextView)view.findViewById(R.id.new_job_count);
        newCountImg.setTextColor(getResources().getColor(R.color.colorPrimary));
        newCount.setTextColor(getResources().getColor(R.color.colorPrimary));
        newText.setTextColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.getTabAt(0).setCustomView(view);
        logInfo("MyJobsActivity", "2 .........");

        tabLayout.addTab(tabLayout.newTab());
        view= LayoutInflater.from(this).inflate(R.layout.jobs_inprogress_tab,tabLayout,false);
        progressCountImg =(TextView)view.findViewById(R.id.inprogress_job);
        progressText =(TextView)view.findViewById(R.id.inprogress_job_text);
        progressText.setTypeface(myGlobals.centuryGothic);
        progressCountImg.setText(context.getResources().getString(R.string.fa_inprogress));
        FontManager.markAsIconContainer(progressCountImg, myGlobals.fontAwesomeFont);
        progressCount =(TextView)view.findViewById(R.id.inprogress_job_count);
        tabLayout.getTabAt(1).setCustomView(view);

        tabLayout.addTab(tabLayout.newTab());
        view= LayoutInflater.from(this).inflate(R.layout.jobs_completed_tab,tabLayout,false);
        completedCountImg = (TextView)view.findViewById(R.id.completed_job);
        completedText = (TextView)view.findViewById(R.id.completed_job_text);
        completedText.setTypeface(myGlobals.centuryGothic);
        completedCountImg.setText(context.getResources().getString(R.string.fa_completed));
        FontManager.markAsIconContainer(completedCountImg, myGlobals.fontAwesomeFont);
        completedCount = (TextView)view.findViewById(R.id.completed_job_count);
        tabLayout.getTabAt(2).setCustomView(view);

        tabLayout.addTab(tabLayout.newTab());
        view= LayoutInflater.from(this).inflate(R.layout.jobs_critical_tab,tabLayout,false);
        criticalCountImg = (TextView)view.findViewById(R.id.critical_job);
        criticalText = (TextView)view.findViewById(R.id.critical_job_text);
        criticalText.setTypeface(myGlobals.centuryGothic);
        criticalCountImg.setText(context.getResources().getString(R.string.fa_critical));
        FontManager.markAsIconContainer(criticalCountImg, myGlobals.fontAwesomeFont);
        criticalCount = (TextView)view.findViewById(R.id.critical_job_count);
        tabLayout.getTabAt(3).setCustomView(view);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.jobs_pager);
        logInfo("Tab Count", tabLayout.getTabCount());
        viewPager.setOffscreenPageLimit(3);
        final MyJobsPagerAdapter adapter = new MyJobsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        //viewPager.setPagingEnabled(true);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                logInfo("TabLayoutOnPageChangeListener", "onTabSelected " + tab.getPosition());
                if(myGlobals.checked) {
                    resetAllCheckedJobs();
                }
                //logInfo("")
                viewPager.setCurrentItem(tab.getPosition());
                newCountImg.setTextColor(getResources().getColor(R.color.focus_color));
                newCount.setTextColor(getResources().getColor(R.color.focus_color));
                newText.setTextColor(getResources().getColor(R.color.focus_color));
                progressCountImg.setTextColor(getResources().getColor(R.color.focus_color));
                progressCount.setTextColor(getResources().getColor(R.color.focus_color));
                progressText.setTextColor(getResources().getColor(R.color.focus_color));
                completedCountImg.setTextColor(getResources().getColor(R.color.focus_color));
                completedCount.setTextColor(getResources().getColor(R.color.focus_color));
                completedText.setTextColor(getResources().getColor(R.color.focus_color));
                criticalCount.setTextColor(getResources().getColor(R.color.focus_color));
                criticalCountImg.setTextColor(getResources().getColor(R.color.focus_color));
                criticalText.setTextColor(getResources().getColor(R.color.focus_color));

                switch(tab.getPosition()) {
                    case 0:
                        newCountImg.setTextColor(getResources().getColor(R.color.colorPrimary));
                        newCount.setTextColor(getResources().getColor(R.color.colorPrimary));
                        newText.setTextColor(getResources().getColor(R.color.colorPrimary));
                        break;
                    case 1:
                        progressCountImg.setTextColor(getResources().getColor(R.color.colorPrimary));
                        progressCount.setTextColor(getResources().getColor(R.color.colorPrimary));
                        progressText.setTextColor(getResources().getColor(R.color.colorPrimary));
                        break;
                    case 2:
                        completedCountImg.setTextColor(getResources().getColor(R.color.colorPrimary));
                        completedCount.setTextColor(getResources().getColor(R.color.colorPrimary));
                        completedText.setTextColor(getResources().getColor(R.color.colorPrimary));
                        break;
                    case 3:
                        criticalCountImg.setTextColor(getResources().getColor(R.color.colorPrimary));
                        criticalCount.setTextColor(getResources().getColor(R.color.colorPrimary));
                        criticalText.setTextColor(getResources().getColor(R.color.colorPrimary));
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    private void resetAllCheckedJobs() {
        if(myGlobals.checkedTabId == 0) {
            MyNewJobsTabFragment.resetViewer();
        } else if(myGlobals.checkedTabId == 1) {
            MyInprogressJobsTabFragment.resetViewer();
        } else if(myGlobals.checkedTabId == 2) {
            MyCompletedJobsTabFragment.resetViewer();
        } else if(myGlobals.checkedTabId == 3) {
            MyCriticalJobsTabFragment.resetViewer();
        }
        myGlobals.checked = false;
        myGlobals.checkedTabId = -1;
        disableMenuItems();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        logInfo("MyJobsActivity", "onPause Called .....");
    }

    @Override
    protected  void onDestroy() {
        logInfo("MyJobsActivity", "onDestroy Called .....");
        super.onDestroy();
        done = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        myGlobals.activityInForeGround = true;
        logInfo("MyJobsActivity", "onResume Called .....");
        if(myToolbar != null) {
            ((TextView)myToolbar.findViewById(R.id.job_id)).setText("Supercraft3D");
            ((TextView) myToolbar.findViewById(R.id.patient_id)).setText("");
            ((LinearLayout) myToolbar.findViewById(R.id.message_back_lyt)).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        String method = null;
        int id = v.getId();
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
        RelativeLayout stopLayout = (RelativeLayout) itemMessages.getActionView();
        stopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInfo("Clicked on", "Stop Menu ........");
                updateJobStatus2Server(1);
            }
        });
        itemMessages.setVisible(false);

        itemMessages = menu.findItem(R.id.delete);
        RelativeLayout deleteLayout = (RelativeLayout) itemMessages.getActionView();
        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInfo("Clicked on", "Delete Menu ........");
                updateJobStatus2Server(2);
            }
        });
        itemMessages.setVisible(false);

        itemMessages = menu.findItem(R.id.archive);
        RelativeLayout archiveLayout = (RelativeLayout) itemMessages.getActionView();
        archiveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInfo("Clicked on", "Archive Menu ........");
                updateJobStatus2Server(3);
            }
        });
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

    public static void editProfile() {
        if(myGlobals.showPopup != null) {
            myGlobals.showPopup.dismiss();
            myGlobals.showPopup = null;
        }

        Intent intent = new Intent(context, MyEditProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void changePassword() {
        if(myGlobals.showPopup != null) {
            myGlobals.showPopup.dismiss();
            myGlobals.showPopup = null;
        }

        Intent intent = new Intent(context, MyChangePasswordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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

    public static void disableMenuItems() {
        //((TextView)myToolbar.findViewById(R.id.job_id)).setText("Supercraft3D");
        ((TextView)myToolbar.findViewById(R.id.job_id)).setVisibility(View.GONE);
        ((ImageView)myToolbar.findViewById(R.id.supercraft_logo)).setVisibility(View.VISIBLE);
        MenuItem itemMessages = menu.findItem(R.id.stop);
        itemMessages.setVisible(false);

        itemMessages = menu.findItem(R.id.delete);
        itemMessages.setVisible(false);

        itemMessages = menu.findItem(R.id.archive);
        itemMessages.setVisible(false);
    }

    public static void enableMenuItems() {
        if(menu != null) {
            //((TextView)myToolbar.findViewById(R.id.job_id)).setText("");
            ((TextView)myToolbar.findViewById(R.id.job_id)).setVisibility(View.GONE);
            ((ImageView)myToolbar.findViewById(R.id.supercraft_logo)).setVisibility(View.GONE);

            MenuItem itemMessages = menu.findItem(R.id.stop);
            itemMessages.setVisible(true);
            RelativeLayout notifyLayout = (RelativeLayout) itemMessages.getActionView();
            TextView tv = (TextView) notifyLayout.findViewById(R.id.action_icon_button);
            tv.setText(context.getResources().getString(R.string.fa_stop));
            FontManager.markAsIconContainer(tv, myGlobals.fontAwesomeFont);

            itemMessages = menu.findItem(R.id.delete);
            itemMessages.setVisible(true);
            notifyLayout = (RelativeLayout) itemMessages.getActionView();
            tv = (TextView) notifyLayout.findViewById(R.id.action_icon_button);
            tv.setText(context.getResources().getString(R.string.fa_trash));
            FontManager.markAsIconContainer(tv, myGlobals.fontAwesomeFont);

            itemMessages = menu.findItem(R.id.archive);
            itemMessages.setVisible(true);
            notifyLayout = (RelativeLayout) itemMessages.getActionView();
            tv = (TextView) notifyLayout.findViewById(R.id.action_icon_button);
            tv.setText(context.getResources().getString(R.string.fa_archive));
            FontManager.markAsIconContainer(tv, myGlobals.fontAwesomeFont);
        }
    }

    public static void updateTabTitle(final int tabId, final int count) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Thread(new Runnable() {
            public void run() {
                String str;
                if(count > 99)
                    str = "99..";
                else if(count > 0)
                    str = "" + String.valueOf(count)+ "";
                else
                    str = "";

                if(tabId == 0){
                    newCount.setText(str);
                } else if(tabId == 1){
                    progressCount.setText(str);
                } else if(tabId == 2){
                    completedCount.setText(str);
                } else if(tabId == 3){
                    criticalCount.setText(str);
                }
            }
        }));
    }

    private void updateJobStatus2Server(int updateMode) {
        String mode = null;

        switch(updateMode) {
            case 1 :
                mode = "stop";
                break;
            case 2 :
                mode = "delete";
                break;
            case 3 :
                mode = "archive";
                break;
        }

        if(mode != null) {
            switch(myGlobals.checkedTabId) {
            case 0 :
                MyNewJobsTabFragment.updateJobStatus(mode);
                break;
            case 1 :
                MyInprogressJobsTabFragment.updateJobStatus(mode);
                break;
            case 2 :
                MyCompletedJobsTabFragment.updateJobStatus(mode);
                break;
            case 3 :
                MyCriticalJobsTabFragment.updateJobStatus(mode);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        done = true;
        exitApplication();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApplication();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public static void exitApplication() {
        if (exit) {
            if (Build.VERSION.SDK_INT >= 21) {
                activity.finishAndRemoveTask();
            } else {
                activity.finish();
            }
        } else {
            Toast.makeText(activity, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

}
