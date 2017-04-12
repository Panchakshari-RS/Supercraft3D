package com.trova.supercraft.Profile;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.trova.supercraft.FontManager;
import com.trova.supercraft.MyTextWatcher;
import com.trova.supercraft.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.MyServiceHandler.sendRequest2Server;
import static com.trova.supercraft.MyTextWatcher.validateConfPassword;
import static com.trova.supercraft.MyTextWatcher.validatePassword;
import static com.trova.supercraft.SuperCraftUtils.checkInternetConenction;
import static com.trova.supercraft.SuperCraftUtils.launchChatNotificationListActivity;
import static com.trova.supercraft.SuperCraftUtils.launchNotificationListActivity;
import static com.trova.supercraft.SuperCraftUtils.logInfo;
import static com.trova.supercraft.SuperCraftUtils.updateUnreadMessagesCount;


/**
 * Created by Trova on 11/2/2016.
 */
public class MyChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private static Context context;
    private static Activity activity;
    private TextView jobCriticalText;
    private static Handler handler = null;
    private static boolean success = false;
    private static ProgressDialog progressBar;
    private String priority;
    private TextInputLayout passOldLyt, passLyt, confPassLyt;
    private EditText etOldPassword, etPassword, etConfPassword;
    private String oldPassword, newPassword;
    private String message = null;
    TextView messageText, loginText;
    LinearLayout lyt_msg;
    Button updateBtn;
    private static Toolbar myToolbar;
    private int isIndex = 0;
    private static TextView notifyText, chatText;
    private static int chatCount = 0, notifyCount = 0;
    public static Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logInfo("MyChangePasswordActivity", "0 .........");
        setContentView(R.layout.change_password_layout);
        context = this;
        activity = this;
        myGlobals.myChangePasswordActivity = this;
        logInfo("MyChangePasswordActivity", "0 .........");

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.getMenu().clear();
        ((TextView)myToolbar.findViewById(R.id.job_id)).setText("Supercraft3D");
        ((TextView)myToolbar.findViewById(R.id.patient_id)).setText("");
        ((LinearLayout)myToolbar.findViewById(R.id.message_back_lyt)).setVisibility(View.GONE);

        etOldPassword = (EditText) findViewById(R.id.input_old_password);
        etPassword = (EditText) findViewById(R.id.input_new_password);
        etConfPassword = (EditText) findViewById(R.id.input_conf_new_password);
        passOldLyt = (TextInputLayout) findViewById(R.id.input_layout_old_password);
        passLyt = (TextInputLayout) findViewById(R.id.input_layout_new_password);
        confPassLyt = (TextInputLayout) findViewById(R.id.input_layout_conf_new_password);
        etOldPassword.addTextChangedListener(new MyTextWatcher(activity, etOldPassword, null, passOldLyt));
        etPassword.addTextChangedListener(new MyTextWatcher(activity, etPassword, null, passLyt));
        etConfPassword.addTextChangedListener(new MyTextWatcher(activity, etConfPassword, etPassword, confPassLyt));
        logInfo("MyChangePasswordActivity", "0 .........");

        messageText = (TextView) findViewById(R.id.message);
        loginText = (TextView) findViewById(R.id.login_button);
        lyt_msg = (LinearLayout)findViewById(R.id.lyt_msg);
        logInfo("MyChangePasswordActivity", "0 .........");

        lyt_msg.setVisibility(View.GONE);
        updateBtn = (Button)findViewById(R.id.update_password);
        updateBtn.setOnClickListener(this);
        logInfo("MyChangePasswordActivity", "0 .........");

    }

    private void updateProfilePassword() {
        progressBar = new ProgressDialog(context);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setMessage("Updating priority ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updatePassword();
            }
        }, 10);

    }

    private void updatePassword() {

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (success) {
                    lyt_msg.setVisibility(View.VISIBLE);
                    messageText.setText(message);
                    updateBtn.setVisibility(View.GONE);
                    //update db and refresh
                    myGlobals.dbHandler.updateUserPassword(String.valueOf(myGlobals.userProfile.getUserId()), newPassword);
                }
                progressBar.dismiss();
                //showSnackBar(cl, message);
            }
        };

        new Thread() {
            public void run() {
                int status = 1;
                priority = jobCriticalText.getText().toString();
                priority = priority.toLowerCase();
                String keyValuePairs = "newpassword=" + newPassword + "&oldpassword=" + oldPassword + "&isFromMobile=" + true + "&userId=" + myGlobals.userProfile.getUserEmail() + "&role=" + myGlobals.loggedinUserType;
                String response = sendRequest2Server("http://54.169.230.139/", "changepassword", keyValuePairs, "POST", null);
                try {
                    if ((response != null) && (!response.isEmpty())) {
                        JSONObject jsonObject = new JSONObject(response);
                        message = jsonObject.getString("message");
                        status = jsonObject.getInt("status");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status == 1)
                    success = true;
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.update_password) {
            if (checkInternetConenction(context)) {
                if (validateForm())
                    updateProfilePassword();
            }
        }
    }

    private boolean validateForm() {
        if (!validatePassword(etPassword, passLyt)) {
            return false;
        }
        if (!validatePassword(etPassword, passLyt)) {
            return false;
        }
        if (!validateConfPassword(etPassword, etConfPassword, confPassLyt)) {
            return false;
        }

        oldPassword = etOldPassword.getText().toString();
        newPassword = etPassword.getText().toString();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        logInfo("MyChangePasswordActivity", "onPause Called .....");
    }

    @Override
    protected  void onDestroy() {
        logInfo("MyChangePasswordActivity", "onDestroy Called .....");
        super.onDestroy();
        myGlobals.myChangePasswordActivity = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        myGlobals.myChangePasswordActivity = this;
        logInfo("MyChangePasswordActivity", "onResume Called .....");
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

        logInfo("MyChangePasswordActivity", "onCreateOptionsMenu");

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
        myGlobals.myChangePasswordActivity = null;
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
