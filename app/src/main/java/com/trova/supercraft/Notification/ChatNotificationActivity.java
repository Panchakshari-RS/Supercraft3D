package com.trova.supercraft.Notification;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.trova.supercraft.CustomGrid;
import com.trova.supercraft.FontManager;
import com.trova.supercraft.HospitalInfo;
import com.trova.supercraft.MyJobs;
import com.trova.supercraft.MySwipeRefreshLayout;
import com.trova.supercraft.PopupHelper;
import com.trova.supercraft.R;

import java.util.ArrayList;
import java.util.List;

import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.SuperCraftUtils.initTrovaChat;
import static com.trova.supercraft.SuperCraftUtils.launchNotificationListActivity;
import static com.trova.supercraft.SuperCraftUtils.logInfo;
import static com.trova.supercraft.SuperCraftUtils.updateUnreadMessagesCount;

/**
 * Created by Panchakshari on 28/3/2017.
 */

public class ChatNotificationActivity extends AppCompatActivity implements View.OnClickListener {
    private static Context context;
    private static Activity activity;
    private static MySwipeRefreshLayout mySwipeRefreshLayout;
    private static TextView notifyText, chatText;
    private static int chatCount = 0, notifyCount = 0;
    public static Menu menu;
    private Toolbar myToolbar;
    public static RecyclerView mRecyclerView = null;
    private static MyChatNotifyRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        myGlobals.myChatNotificationActivity = this;

        setContentView(R.layout.notification_list_layout);

        mySwipeRefreshLayout = (MySwipeRefreshLayout) findViewById (R.id.my_swipe_refresh_layout);
        mySwipeRefreshLayout.setOnRefreshListener (new MySwipeRefreshLayoutListener ());

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.getMenu().clear();
        ((TextView)myToolbar.findViewById(R.id.job_id)).setText("Chat Notifications");
        ((TextView)myToolbar.findViewById(R.id.job_id)).setTypeface(myGlobals.centuryGothic);
        ((TextView)myToolbar.findViewById(R.id.patient_id)).setText("");
        ((TextView)myToolbar.findViewById(R.id.job_id)).setTextColor(getResources().getColor(R.color.colorPrimary));
        ((LinearLayout)myToolbar.findViewById(R.id.message_back_lyt)).setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyChatNotifyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }
        });

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        ((MyChatNotifyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyChatNotifyRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                MyChatNotifyRecyclerViewAdapter.DataObjectHolder vh = (MyChatNotifyRecyclerViewAdapter.DataObjectHolder) mRecyclerView.findViewHolderForAdapterPosition(position);

                switch (v.getId()) {
                    case R.id.notify_layout:
                        MyChatNotificationsRowItem notification = vh.notification;

                        String jobId = notification.getJobId();
                        MyJobs myJobs = myGlobals.dbHandler.getMyJob(jobId);
                        if(myJobs != null) {
                            if(myGlobals.currJobActive != null) {
                                myGlobals.prevJobActive = myGlobals.currJobActive;
                                myGlobals.prevJobId = myGlobals.currJobActive.getJobId();
                                myGlobals.prevPatientId = myGlobals.currJobActive.getPatientId();
                                myGlobals.prevPatientName = myGlobals.currJobActive.getPatientName();
                            }

                            myGlobals.currJobActive = myJobs;
                            myGlobals.jobId = myJobs.getJobId();
                            myGlobals.patientId = myJobs.getPatientId();
                            myGlobals.patientName = myJobs.getPatientName();
                            String hospitalId = myJobs.getHospitalId();
                            HospitalInfo hospitalInfo = myGlobals.dbHandler.getHospital(hospitalId);
                            if(hospitalInfo != null) {
                                myGlobals.hospitalName = hospitalInfo.getHospitalName();
                                myGlobals.cityName = hospitalInfo.getHospitalCity();
                            }

                            String agentKey = myGlobals.currJobActive.getDoctorKey();
                            if (myGlobals.loggedinUserType == 1)
                                agentKey = myGlobals.currJobActive.getEngineerKey();

                            initTrovaChat(context, agentKey);
/*
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
*/
                            onBackPressed();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    private static ArrayList<MyChatNotificationsRowItem> getDataSet() {
        ArrayList results = new ArrayList<MyChatNotificationsRowItem>();
        List<MyChatNotificationsRowItem> notificationsList = myGlobals.dbHandler.getUnReadMessages();
        if((notificationsList != null) && (notificationsList.size() > 0)) {
            results.addAll(notificationsList);
            logInfo("Total Messages", notificationsList.size());
        }

        return results;
    }

    public static void updateNotifyList() {
        mAdapter.clearDataSet();
        ArrayList results = getDataSet();
        mAdapter.addDataSet(results);
        mAdapter.notifyDataSetChanged();
    }

    private class MySwipeRefreshLayoutListener implements SwipeRefreshLayout.OnRefreshListener
    {
        @Override
        public void onRefresh ()
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateComplaintStatus();
                }
            }, 100);

        }
    }

    private static void updateComplaintStatus() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mySwipeRefreshLayout.setRefreshing(true);
                        mAdapter.notifyDataSetChanged();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        };

        new Thread() {
            public void run() {
                try {
                    //myGlobals.getJobsStatus();
                    mAdapter.clearDataSet();
                    mAdapter.addDataSet(getDataSet());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
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

            }
        });

        itemMessages = menu.findItem(R.id.stop);
        RelativeLayout stopLayout = (RelativeLayout) itemMessages.getActionView();
        stopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInfo("Clicked on", "Stop Menu ........");

            }
        });
        itemMessages.setVisible(false);

        itemMessages = menu.findItem(R.id.delete);
        RelativeLayout deleteLayout = (RelativeLayout) itemMessages.getActionView();
        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInfo("Clicked on", "Delete Menu ........");

            }
        });
        itemMessages.setVisible(false);

        itemMessages = menu.findItem(R.id.archive);
        RelativeLayout archiveLayout = (RelativeLayout) itemMessages.getActionView();
        archiveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInfo("Clicked on", "Archive Menu ........");

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
    protected void onResume() {
        super.onResume();
        logInfo("ChatNotificationActivity", "onResume Called ............");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myGlobals.myChatNotificationActivity = null;
        logInfo("ChatNotificationActivity", "onDestroy Called .......");
    }

    @Override
    public void onBackPressed() {
        myGlobals.myChatNotificationActivity = null;
        if ((myGlobals.myJobDetailsActivity == null) && (myGlobals.myJobsActivity == null) && (myGlobals.myMessageActivity == null)){
            exitApplication();
        } else {
            finish();
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
