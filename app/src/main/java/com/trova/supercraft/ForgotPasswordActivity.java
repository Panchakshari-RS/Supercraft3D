package com.trova.supercraft;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.MyTextWatcher.validateEmail;
import static com.trova.supercraft.SuperCraftUtils.logInfo;
import static com.trova.supercraft.SuperCraftUtils.showSnackBar;

/**
 * Created by Panchakshari on 25/3/2017.
 */

public class ForgotPasswordActivity extends Activity implements View.OnClickListener {
    Context context;
    Activity activity;
    CoordinatorLayout cl;
    TextView messageText, loginText;
    EditText etEmail;
    TextInputLayout emailLyt;
    LinearLayout lyt_msg, lyt_login;
    String email;
    Handler handler;
    ProgressDialog progressBar;
    boolean success = false;
    String message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;

        setContentView(R.layout.forgot_password_activity);
        cl = (CoordinatorLayout) findViewById(R.id.forgotpasswordlayout);
        etEmail = (EditText) findViewById(R.id.input_email);
        emailLyt = (TextInputLayout) findViewById(R.id.input_layout_email);
        etEmail.addTextChangedListener(new MyTextWatcher(activity, etEmail, null, emailLyt));

        messageText = (TextView) findViewById(R.id.message);
        loginText = (TextView) findViewById(R.id.login_button);
        lyt_msg = (LinearLayout)findViewById(R.id.lyt_msg);
        lyt_login = (LinearLayout)findViewById(R.id.lyt_login);

        lyt_msg.setVisibility(View.GONE);
        lyt_login.setVisibility(View.GONE);
        ((Button)findViewById(R.id.forgot_button)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.forgot_button) {
            email = etEmail.getText().toString();
            if(validateEmail(etEmail, emailLyt)) {
                sendForgotPassword();
            } else {
                showSnackBar(cl, "Please fill in proper details.");
            }
        } else if(id == R.id. login_button) {
            this.finish();
        }
    }

    private void sendForgotPassword() {
        progressBar = new ProgressDialog(context);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setMessage("Sending email ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressBar.dismiss();
                if(success) {
                    lyt_msg.setVisibility(View.VISIBLE);
                    lyt_login.setVisibility(View.VISIBLE);
                    messageText.setText(message);
                }
            }
        };

        new Thread() {
            public void run() {
                String keyValuePairs = "userId=" + email;

                String response = MyServiceHandler.sendRequest2Server(myGlobals.serverIp, "forgotpassword", keyValuePairs, "POST", null);
                try {
                    if((response != null) && (!response.isEmpty())) {
                        logInfo("response", response);
                        JSONObject jsonObject = new JSONObject(response);
                        message = jsonObject.getString("message");

                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            success = true;
                        } else {
                            showSnackBar(cl, "Sorry we dont recognize this email.");
                        }
                    } else {
                        showSnackBar(cl, "Please re-enter later.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }.start();

    }

}
