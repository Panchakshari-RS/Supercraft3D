package com.trova.supercraft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.SuperCraftUtils.getProfileImage;
import static com.trova.supercraft.SuperCraftUtils.initAmazonS3;
import static com.trova.supercraft.SuperCraftUtils.initTrova;
import static com.trova.supercraft.SuperCraftUtils.loadProfileImage;
import static com.trova.supercraft.SuperCraftUtils.logInfo;
import static com.trova.supercraft.TrovaMessageCallback.pullDataFromServer;

/**
 * Created by Panchakshari on 8/3/2017.
 */

public class TwitterDigitsActivity extends Activity {
    private static Context context;
    static Activity activity;
    DigitsAuthButton digitsButton = null;
    private Handler handler = null;
    private boolean success = false, getSyncMessages = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;

        setContentView(R.layout.twitterdigits_activity);
        logInfo("TwitterDigitsActivity", "Called .............");
        digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setAuthTheme(R.style.CustomDigitsTheme);
        digitsButton.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postDigitsClick();
            }
        }, 10);

        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                if ((phoneNumber != null) && (!phoneNumber.isEmpty()) && (phoneNumber.length() > 0)) {
                    try {
                        logInfo("If Number", phoneNumber);
                        logInfo("userId", myGlobals.userProfile.userId);
                        //getCountryCode(phoneNumber, myGlobals.countryList, 2);
                        myGlobals.userNumber = phoneNumber;
                        myGlobals.userId = phoneNumber;
                        if(myGlobals.userProfile != null) {
                            myGlobals.userProfile.setUserPhone(myGlobals.userNumber);
                            myGlobals.dbHandler.updateUserPhone(String.valueOf(myGlobals.userProfile.userId), myGlobals.userNumber);
                        }
                    } catch (Exception e) {
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    }
                } else {
                    logInfo("else Number", "phoneNumber");
                    logInfo("else Number", phoneNumber);
                }
                logInfo("Launching", "MyJobsActivity ............");

                if(myGlobals.userProfile != null)
                    myGlobals.loggedinUserType = myGlobals.userProfile.getUserType();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activateUser();
                    }
                }, 10);
            }

            @Override
            public void failure(DigitsException exception) {
                logInfo("Digits", "Sign in with Digits failure " + exception);
            }
        });
    }

    private void activateUser() {
        if(myGlobals.trovaApi != null) {
            logInfo("Initing Trova", "called .............");
            initTrova(context, "+91" + myGlobals.userProfile.getUserEmail(), "+91" + myGlobals.userProfile.getUserEmail());
            logInfo("Initing Trova", "called .............");
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if(success) {
                    myGlobals.bmLoggedinUser = loadProfileImage(myGlobals.userProfile.getUserEmail());

                    if(getSyncMessages) {
                        myGlobals.trovaApi.getUnsyncedMessages();
                    }
                    myGlobals.trovaApi.getAllNotification();
                    myGlobals.trovaApi.getAllSyncAppState();

                    Intent twitterDigits = new Intent(myGlobals.context, MyJobsActivity.class);
                    twitterDigits.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(twitterDigits);
                    finish();
                }
            }
        };

        new Thread() {
            public void run() {
                getProfileImage(myGlobals.userProfile.getUserEmail());

                int status = 0;
                if((myGlobals.activateUser) && (myGlobals.serverRefCode != null)) {
                    String keyValuePairs = "userId=" + myGlobals.userProfile.getUserEmail() + "&mobile=" + myGlobals.userProfile.getUserPhone() + "&isFromMobile=" + true + "&role=" + myGlobals.loggedinUserType;
                    String response = MyServiceHandler.sendRequest2Server(myGlobals.serverIp, "updatephonenumber", keyValuePairs, "POST", null);
                    try {
                        if ((response != null) && (!response.isEmpty())) {
                            logInfo("response", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            status = jsonObject.getInt("status");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (status == 1) {
                        status = 0;
                        keyValuePairs = "/" + myGlobals.userProfile.getUserEmail() + "/" + myGlobals.serverRefCode + "/" + true;
                        response = MyServiceHandler.sendRequest2Server(myGlobals.serverIp, "activateuser", keyValuePairs, "GET", null);
                        try {
                            if ((response != null) && (!response.isEmpty())) {
                                logInfo("response", response);
                                JSONObject jsonObject = new JSONObject(response);
                                String message = jsonObject.getString("message");
                                status = jsonObject.getInt("status");
                                myGlobals.agentKey = jsonObject.getString("agentKey");
                                myGlobals.dbHandler.updateUserAgentKey(String.valueOf(myGlobals.userProfile.userId), myGlobals.agentKey);
                                if (status == 1)
                                    success = true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    success = true;
                }

                List<MyJobs> myJobsList = myGlobals.dbHandler.getMyJobs();
                if((myJobsList == null) || ((myJobsList != null) && (myJobsList.size() <= 0)))
                    pullDataFromServer();
                initAmazonS3(context, myGlobals.amazonCognitoId, myGlobals.amazonAWSBucket);

                myWaitSleep(500);

                if(myGlobals.getChatMessages) {
                    myGlobals.getChatMessages = false;
                    getSyncMessages = false;
                    myGlobals.trovaApi.getAllChatMessages(myGlobals.userId);
                    myGlobals.initSuccess = false;
                    myWaitSleep(0);
                }

                handler.sendEmptyMessage(0);
            }
        }.start();

    }

    private void myWaitSleep(int maxRetry) {
        try {
            int cnt = 0;
            if(maxRetry <= 0)
                maxRetry = Integer.MAX_VALUE;
            while(!myGlobals.initSuccess) {
                cnt++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(cnt > maxRetry) {
                    // post message and return / exit
                    return;
                }
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    private void postDigitsClick() {
        digitsButton.callOnClick();
    }

    @Override
    public void onResume() {
        super.onResume();
        logInfo("TwitterDigitsActivity", "onResume Called .....");
    }

    @Override
    public void onPause() {
        super.onPause();
        logInfo("TwitterDigitsActivity", "onPause Called .....");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logInfo("TwitterDigitsActivity", "onDestroy Called .....");
    }

}
