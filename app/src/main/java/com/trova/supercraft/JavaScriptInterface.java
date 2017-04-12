package com.trova.supercraft;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.SuperCraftUtils.checkInternetConenction;

/**
 * Created by Panchakshari on 28/2/2017.
 */

public class JavaScriptInterface {
    private Context context;
    private String userId;

    public JavaScriptInterface(Context context, String userId){
        this.context = context;
        this.userId = userId;
    }

    @JavascriptInterface
    public void makeToast(final String message, boolean lengthLong){
        Toast.makeText(context, message, (lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show();

            /*Handler handler=new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    cwv.loadUrl("javascript:callFromActivity(\""+message+"\")");
                }
            });*/
    }

    @JavascriptInterface
    public boolean authenticateMobileNumber(JSONObject userObj) {
        boolean ret = false;
        UserProfile userProfile = new UserProfile();
        try {
            userProfile.setUserName(userObj.getString("userName"));
            userProfile.setUserEmail(userObj.getString("userEmail"));
            userProfile.setUserType(userObj.getInt("userType"));
            userProfile.setMedRegistrationNo(userObj.getString("regNo"));
            userProfile.setHospitalId(userObj.getString("hospitalId"));
            userProfile.setDepartmentId(userObj.getString("departmentId"));
            userProfile.setPassWord(userObj.getString("userPass"));
            if(myGlobals.dbHandler.addUserProfile(userProfile))
                myGlobals.userProfile = myGlobals.dbHandler.getUserProfile();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(myGlobals.userProfile != null) {
            Intent twitterDigits = new Intent(myGlobals.context, TwitterDigitsActivity.class);
            twitterDigits.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(twitterDigits);
            ret = true;
        }

        return ret;
    }

    @JavascriptInterface
    public void launchTwitterDigitsActivity(final String activity) {
        new Thread() {
            public void run() {
                try {
                    if(activity.equals("signup"))
                        myGlobals.pageId = 1;
                    else if(activity.equals("details"))
                        myGlobals.pageId = 2;
                    else if(activity.equals("chat"))
                        myGlobals.pageId = 3;

                    Intent twitterDigits = new Intent(myGlobals.context, TwitterDigitsActivity.class);
                    twitterDigits.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(twitterDigits);
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @JavascriptInterface
    public void initTrovaChat() {
        if (checkInternetConenction(context)){
            //once implementation done for getting the customer id remove the below line setCustomerKey()
            //String agentKey = "6230a03c1c6fdc3609d3e4ac9ee13f29"; //data.trova.in panchu
            String agentKey = "8d8c2e229c7a55aab3a436efcb5b3079"; // dev.trova.in panchu
            //String agentKey = "42a8b85f8842aa0b0e6d6089fdcda3aa"; //data.trova.in srijith
            //String agentKey = "e81ad81e906d721e48272a4e3edd26c6"; //dev.trova.in srijith
            //String agentKey = "6969d089c4cbf182b5e1bb754a58e322"; //data.trova.in hari
            JSONObject jsonObject = new JSONObject();

            myGlobals.activityInForeGround = true;
            try {
                jsonObject.put("businessId", agentKey);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            myGlobals.trovaApi.initChat(jsonObject);
        } else{
        }
    }

    @JavascriptInterface
    public void approveJob() {

    }

    @JavascriptInterface
    public String getStlFilePath() {
        String stlFilePath = myGlobals.currJobActive.getStlFilePath();

        return stlFilePath;
    }

}
