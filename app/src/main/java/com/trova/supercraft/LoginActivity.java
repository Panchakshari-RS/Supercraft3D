package com.trova.supercraft;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.digits.sdk.android.Digits;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;

import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.MyTextWatcher.validateEmail;
import static com.trova.supercraft.MyTextWatcher.validatePassword;
import static com.trova.supercraft.SuperCraftUtils.checkInternetConenction;
import static com.trova.supercraft.SuperCraftUtils.logInfo;
import static com.trova.supercraft.SuperCraftUtils.showSnackBar;

/**
 * Created by Panchakshari on 13/3/2017.
 */

public class LoginActivity extends Activity implements View.OnClickListener {
    private String userId = "+917026209963";
    Context context;
    public static Activity activity;
    private static final String TWITTER_KEY = "Zrm75QgqP3jPEJuO9YOJIdQiY";
    private static final String TWITTER_SECRET = "m98hkQyrSnENK4DBWMy7b4qLfnBEYfCPmnSCoJfALGT8K974y4";
    private Handler handler;
    private TextInputLayout emailLyt, passLyt;
    private EditText etEmail, etPassword;
    private String email, password;
    private ProgressDialog progressBar;
    private boolean success = false, launchDigitsActivity = false;
    private CoordinatorLayout cl = null;
    private boolean isMD5Done = false, updateUserProfile = false;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        context = this;
        activity = this;

        setContentView(R.layout.login_activity);
        cl = (CoordinatorLayout) findViewById(R.id.loginlayout);

        myGlobals = new MyGlobals(this);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build(), new Crashlytics());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getMasterDataFromServer();
            }
        }, 10);

        if(myGlobals.userProfile != null)
            logInfo("myGlobals.userProfile != null", "NOT NULL");
        else
            logInfo("myGlobals.userProfile != null", "NULL");

        if((myGlobals.userProfile != null) && (myGlobals.userProfile.getUserPhone() != null) && (myGlobals.userProfile.getUserPhone().length() > 0)) {
            email = myGlobals.userProfile.getUserEmail();
            password = myGlobals.userProfile.getPassWord();
            isMD5Done = true;
            ((RelativeLayout) findViewById(R.id.splash_lyt)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.login_lyt)).setVisibility(View.GONE);
            logInfo("userPhone", myGlobals.userProfile.getUserPhone());
            myGlobals.agentKey = myGlobals.userProfile.getAgentKey();
            logInfo("agentKey", myGlobals.agentKey);
            //launchDigitsActivity = true;
            login();
        } else {
            loginScreen();
        }
    }

    private void loginScreen() {
        isMD5Done = false;
        ((RelativeLayout)findViewById(R.id.splash_lyt)).setVisibility(View.GONE);
        ((LinearLayout)findViewById(R.id.login_lyt)).setVisibility(View.VISIBLE);

        ((Button)findViewById(R.id.login_button)).setOnClickListener(this);
        ((TextView)findViewById(R.id.signup)).setOnClickListener(this);

        etEmail = (EditText) findViewById(R.id.input_email);
        etPassword = (EditText) findViewById(R.id.input_password);
        emailLyt = (TextInputLayout) findViewById(R.id.input_layout_email);
        passLyt = (TextInputLayout) findViewById(R.id.input_layout_password);
        etEmail.addTextChangedListener(new MyTextWatcher(activity, etEmail, null, emailLyt));
        etPassword.addTextChangedListener(new MyTextWatcher(activity, etPassword, null, passLyt));

/*
        TextView tv = (TextView) findViewById(R.id.signup);
        tv.setTypeface(myGlobals.openSansRegular);
        tv = (TextView) findViewById(R.id.forgot_password);
        tv.setTypeface(myGlobals.openSansRegular);
        tv = (TextView) findViewById(R.id.label_login);
        tv.setTypeface(myGlobals.openSansBold);

        EditText et = (EditText) findViewById(R.id.input_password);
        et.setTypeface(myGlobals.openSansRegular);
        et = (EditText) findViewById(R.id.input_email);
        et.setTypeface(myGlobals.openSansRegular);
*/

        TextView tv = (TextView) findViewById(R.id.signup);
        tv.setTypeface(myGlobals.centuryGothic);
        tv = (TextView) findViewById(R.id.forgot_password);
        tv.setTypeface(myGlobals.centuryGothic);
        tv = (TextView) findViewById(R.id.label_login);
        tv.setTypeface(myGlobals.centuryGothicBold);
        Button btn = (Button) findViewById(R.id.login_button);
        btn.setTypeface(myGlobals.centuryGothicBold);

        EditText et = (EditText) findViewById(R.id.input_password);
        et.setTypeface(myGlobals.centuryGothic);
        et = (EditText) findViewById(R.id.input_email);
        et.setTypeface(myGlobals.centuryGothic);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.login_button) {
            if(checkInternetConenction(context)) {
                if (validateForm()) {
                    login();
                } else {
                    showSnackBar(cl, "Please fill in proper details.");
                }
            } else {
                showSnackBar(cl, "Check internet connection.");
            }
        } else if(id == R.id.signup) {
            Intent intent = new Intent(activity, RegistrationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if(id == R.id.forgot_password) {
            Intent intent = new Intent(activity, ForgotPasswordActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void login() {
        progressBar = new ProgressDialog(context);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setMessage("Logging in ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        success = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cbInitSuccess();
            }
        }, 10);
    }

    private void cbInitSuccess() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressBar.dismiss();
                if(success) {
                    Intent twitterDigits = new Intent(myGlobals.context, TwitterDigitsActivity.class);
                    twitterDigits.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(twitterDigits);
                    finish();
                } else {
                    updateUserProfile = true;
                    //myGlobals.userProfile = null;
                    //myGlobals.dbHandler.clearTableData("profile");
                    loginScreen();
                }
            }
        };

        new Thread() {
            public void run() {
                checkLogin();
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private boolean checkLogin() {
        boolean result = false;
        String keyValuePairs = "userId=" + email + "&password=" + password + "&isFromMobile=" + true + "&isMD5Done=" + isMD5Done;

        String response = MyServiceHandler.sendRequest2Server(myGlobals.serverIp, "checkLogin", keyValuePairs, "POST", null);
        try {
            if((response != null) && (!response.isEmpty())) {
                logInfo("response", response);
                JSONObject jsonObject = new JSONObject(response);
                String message = jsonObject.getString("message");
                if (jsonObject.has("profileInfo")) {
                    JSONObject profileInfo = jsonObject.getJSONObject("profileInfo");
                    if (profileInfo.getInt("status") == 0)
                        myGlobals.activateUser = true;
                    int status = jsonObject.getInt("status");
                    myGlobals.serverRefCode = jsonObject.getString("refCode");
                    if (status == 1) {
                        success = true;
                        result = true;
                        if ((myGlobals.userProfile == null) || (updateUserProfile)) {
                            myGlobals.getChatMessages = true;
                            UserProfile userProfile = new UserProfile();
                            userProfile.setUserId(0);
                            if(updateUserProfile)
                                userProfile.setUserId(myGlobals.userProfile.getUserId());
                            userProfile.setUserName(profileInfo.getString("fname"));
                            userProfile.setUserLName(profileInfo.getString("lname"));
                            userProfile.setUserType(profileInfo.getInt("role"));
                            userProfile.setUserEmail(profileInfo.getString("email"));
                            userProfile.setMedRegistrationNo(profileInfo.getString("registrationNum"));
                            userProfile.setPassWord(profileInfo.getString("password"));
                            userProfile.setUserPhone(profileInfo.getString("mobile"));
                            userProfile.setAgentKey(profileInfo.getString("agentKey"));
                            logInfo("userProfile", userProfile.toString());
                            if (myGlobals.dbHandler.addUserProfile(userProfile))
                                myGlobals.userProfile = myGlobals.dbHandler.getUserProfile();
                            logInfo("userId", myGlobals.userProfile.userId);
                        } /*else {
                                    myGlobals.agentKey = profileInfo.getString("agentKey");
                                }*/
                        myGlobals.agentKey = profileInfo.getString("agentKey");
                    } else {
                        showSnackBar(cl, "Email or Password wrong please re-enter.");
                    }
                }
            } else {
                showSnackBar(cl, "Please re-enter later.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private boolean validateForm() {
        email = etEmail.getText().toString();
        if (!validateEmail(etEmail, emailLyt)) {
            return false;
        }
        password = etPassword.getText().toString();
        if (!validatePassword(etPassword, passLyt)) {
            return false;
        }

        return true;
    }

    private void getMasterDataFromServer(){
        new Thread() {
            public void run() {
                String response = MyServiceHandler.sendRequest2Server(myGlobals.serverIp, "getAllHospital", "", "POST", null);
                try {
                    if((response != null) && (!response.isEmpty())) {
                        logInfo("getAllHospital Response", response);
                        JSONArray jsonArray = new JSONArray(response);
                        if(jsonArray != null) {
                            for (int i = 0; (i < jsonArray.length()); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (jsonObject != null) {
                                    HospitalInfo hospitalInfo = new HospitalInfo();
                                    hospitalInfo.setHospitalId(jsonObject.getString("id"));
                                    hospitalInfo.setHospitalName(jsonObject.getString("hospitalname"));
                                    hospitalInfo.setHospitalAddress(jsonObject.getString("address"));
                                    hospitalInfo.setHospitalCity(jsonObject.getString("city"));
                                    hospitalInfo.setHospitalPin(jsonObject.getString("pin"));
                                    hospitalInfo.setHospitalPhone(jsonObject.getString("phone"));
                                    String logo = jsonObject.getString("hostpitallogo");
                                    if((logo != null) && (!logo.equals("null")) && (!logo.equals("")) && (logo.length() > 0))
                                    hospitalInfo.setHospitalLogo(logo.getBytes());
                                    myGlobals.dbHandler.addHospitalMaster(hospitalInfo);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            public void run() {
                String response = MyServiceHandler.sendRequest2Server(myGlobals.serverIp, "getAllDepartments", "", "POST", null);
                try {
                    if((response != null) && (!response.isEmpty())) {
                        logInfo("getAllDepartments Response", response);
                        JSONArray jsonArray = new JSONArray(response);
                        if(jsonArray != null) {
                            for (int i = 0; (i < jsonArray.length()); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (jsonObject != null) {
                                    String id = jsonObject.getString("id");
                                    String name = jsonObject.getString("deptname");
                                    myGlobals.dbHandler.addDepartmentMaster(id, name);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        if(launchDigitsActivity) {
            Intent twitterDigits = new Intent(myGlobals.context, TwitterDigitsActivity.class);
            twitterDigits.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(twitterDigits);
            finish();
        }
    }
}
