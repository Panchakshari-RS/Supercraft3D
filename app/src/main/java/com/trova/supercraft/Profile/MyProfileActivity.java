package com.trova.supercraft.Profile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.trova.supercraft.FontManager;
import com.trova.supercraft.R;

import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.SuperCraftUtils.launchChatNotificationListActivity;
import static com.trova.supercraft.SuperCraftUtils.launchNotificationListActivity;
import static com.trova.supercraft.SuperCraftUtils.logInfo;
import static com.trova.supercraft.SuperCraftUtils.updateUnreadMessagesCount;

/**
 * Created by Panchakshari on 23/9/2016.
 */
public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static Context context;
    public static Activity activity;
    private String LOG_TAG = "MyProfileActivity";
    private static Toolbar myToolbar;
    private static TabLayout tabLayout = null;
    private static ViewPager viewPager = null;
    public static Menu menu;
    private static TextView changePassword, editProfile;
    private static TextView notifyText, chatText;
    private static int chatCount = 0, notifyCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;

        logInfo("MyProfileActivity", "0 .........");
        myGlobals.myProfileActivity = this;

        setContentView(R.layout.job_tab_view);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.getMenu().clear();
        ((TextView)myToolbar.findViewById(R.id.job_id)).setText("Supercraft3D");
        ((TextView)myToolbar.findViewById(R.id.patient_id)).setText("");
        ((LinearLayout)myToolbar.findViewById(R.id.message_back_lyt)).setVisibility(View.GONE);

        tabLayout = (TabLayout) findViewById(R.id.jobs_tab_layout);
        tabLayout.addTab(tabLayout.newTab());
        View view= LayoutInflater.from(this).inflate(R.layout.change_password_tab,tabLayout,false);
        changePassword = (TextView)view.findViewById(R.id.change_password);
        changePassword.setTextColor(getResources().getColor(R.color.colorPrimary));
        tabLayout.getTabAt(0).setCustomView(view);

        tabLayout.addTab(tabLayout.newTab());
        view= LayoutInflater.from(this).inflate(R.layout.edit_profile_tab,tabLayout,false);
        editProfile =(TextView)view.findViewById(R.id.edit_profile);
        tabLayout.getTabAt(1).setCustomView(view);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.jobs_pager);
        logInfo("Tab Count", tabLayout.getTabCount());
        viewPager.setOffscreenPageLimit(1);
        final MyProfileAdapter adapter = new MyProfileAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                logInfo("TabLayoutOnPageChangeListener", "onTabSelected " + tab.getPosition());

                viewPager.setCurrentItem(tab.getPosition());
                changePassword.setTextColor(getResources().getColor(R.color.focus_color));
                editProfile.setTextColor(getResources().getColor(R.color.focus_color));

                switch(tab.getPosition()) {
                    case 0:
                        changePassword.setTextColor(getResources().getColor(R.color.colorPrimary));
                        break;
                    case 1:
                        editProfile.setTextColor(getResources().getColor(R.color.colorPrimary));
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        logInfo("MyProfileActivity", "onPause Called .....");
    }

    @Override
    protected  void onDestroy() {
        logInfo("MyProfileActivity", "onDestroy Called .....");
        super.onDestroy();
        myGlobals.myProfileActivity = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        myGlobals.myProfileActivity = this;
        logInfo("MyProfileActivity", "onResume Called .....");
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

    @Override
    public void onBackPressed() {
        myGlobals.myProfileActivity = null;
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
