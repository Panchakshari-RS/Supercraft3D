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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.trova.search.SearchableSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import static com.trova.supercraft.LoginActivity.activity;
import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.MyTextWatcher.validateConfPassword;
import static com.trova.supercraft.MyTextWatcher.validateEmail;
import static com.trova.supercraft.MyTextWatcher.validateName;
import static com.trova.supercraft.MyTextWatcher.validatePassword;
import static com.trova.supercraft.MyTextWatcher.validateRegNo;
import static com.trova.supercraft.SuperCraftUtils.checkInternetConenction;
import static com.trova.supercraft.SuperCraftUtils.logInfo;
import static com.trova.supercraft.SuperCraftUtils.showSnackBar;

public class RegistrationActivity extends Activity implements View.OnClickListener, SearchableSpinner.OnSelectionChangeListener {
    Context context;
    private TextInputLayout fNameLyt, lNameLyt, emailLyt, passLyt, confPassLyt, regNoLyt;
    private EditText etFirstName, etLastName, etEmail, etPassword, etConfPassword, etRegNo;
    private String fName, lName, email, password, regNo;
    private RadioGroup radioGroup;
    private int userType = 1;
    private ProgressDialog progressBar;
    private boolean success = false;
    private Handler handler = null;
    private CoordinatorLayout cl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.registration_layout);
        cl = (CoordinatorLayout) findViewById(R.id.registrationlayout);

        etFirstName = (EditText) findViewById(R.id.input_name);
        etLastName = (EditText) findViewById(R.id.input_lname);
        etEmail = (EditText) findViewById(R.id.input_email);
        etRegNo = (EditText) findViewById(R.id.input_medical_regno);
        etPassword = (EditText) findViewById(R.id.input_password);
        etConfPassword = (EditText) findViewById(R.id.input_conf_password);

        fNameLyt = (TextInputLayout) findViewById(R.id.input_layout_name);
        lNameLyt = (TextInputLayout) findViewById(R.id.input_layout_lname);
        emailLyt = (TextInputLayout) findViewById(R.id.input_layout_email);
        passLyt = (TextInputLayout) findViewById(R.id.input_layout_password);
        confPassLyt = (TextInputLayout) findViewById(R.id.input_layout_conf_password);
        regNoLyt = (TextInputLayout) findViewById(R.id.input_layout_medical_regno);

        etFirstName.addTextChangedListener(new MyTextWatcher(activity, etFirstName, null, fNameLyt));
        etLastName.addTextChangedListener(new MyTextWatcher(activity, etLastName, null, lNameLyt));
        etEmail.addTextChangedListener(new MyTextWatcher(activity, etEmail, null, emailLyt));
        etPassword.addTextChangedListener(new MyTextWatcher(activity, etPassword, null, passLyt));
        etConfPassword.addTextChangedListener(new MyTextWatcher(activity, etConfPassword, etPassword, confPassLyt));
        etRegNo.addTextChangedListener(new MyTextWatcher(activity, etRegNo, null, regNoLyt));

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    if(rb.getText().toString().equals("Doctor"))
                        userType = 1;
                    else if(rb.getText().toString().equals("Technician"))
                        userType = 2;
                    else
                        userType = 3;
                }

            }
        });

/*
        SearchableSpinner spinner_hospital = (SearchableSpinner) findViewById(R.id.search_hospital);
        spinner_hospital.setList(getResources().getStringArray(R.array.hospitals));

        SearchableSpinner spinner_department = (SearchableSpinner) findViewById(R.id.search_department);
        spinner_department.setList(getResources().getStringArray(R.array.departments));
*/

        ((Button)findViewById(R.id.signup)).setOnClickListener(this);
        ((TextView)findViewById(R.id.login_button)).setOnClickListener(this);
    }

    @Override
    public void onSelectionChanged(String selection) {
        Toast.makeText(this, selection + " selected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.signup) {
            if(checkInternetConenction(context)) {
                if (validateForm()) {
                    progressBar = new ProgressDialog(v.getContext());
                    progressBar.setCancelable(false);
                    progressBar.setCanceledOnTouchOutside(false);
                    progressBar.setMessage("Please wait ...");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.setProgress(0);
                    progressBar.setMax(100);
                    progressBar.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            addUser();
                        }
                    }, 10);
                } else {
                    showSnackBar(cl, "Please fill in all the details and the proceed.");
                }
            } else {
                showSnackBar(cl, "Check internet connection.");
            }
        } else if(id == R.id.login_button) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }
    }

    private void addUser() {
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
                }
            }
        };

        new Thread() {
            public void run() {
                String keyValuePairs = "role=" + userType + "&fname=" + fName +
                        "&lname=" + lName + "&email=" + email + "&registrationNum=" + regNo
                        + "&password=" + password + "&mobile=" + "" + "&isFromMobile=" + true;

                String response = MyServiceHandler.sendRequest2Server(myGlobals.serverIp, "adduser", keyValuePairs, "POST", null);

                try {
                    if((response != null) && (!response.isEmpty())) {
                        logInfo("response", response);
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            myGlobals.serverRefCode = jsonObject.getString("refCode");;
                            UserProfile userProfile = new UserProfile();
                            userProfile.setUserId(0);
                            userProfile.setUserName(fName);
                            userProfile.setUserLName(lName);
                            userProfile.setUserType(userType);
                            userProfile.setUserEmail(email);
                            userProfile.setMedRegistrationNo(regNo);
                            userProfile.setPassWord(password);
                            if (myGlobals.dbHandler.addUserProfile(userProfile))
                                myGlobals.userProfile = myGlobals.dbHandler.getUserProfile();
                            myGlobals.activateUser = true;
                            success = true;
                            if(myGlobals.userProfile != null)
                                myGlobals.loggedinUserType = myGlobals.userProfile.getUserType();
                        } else {
                            if(message.equals("email already exists"))
                                showSnackBar(cl, "Email already in use.");
                            else
                                showSnackBar(cl, "Please re-enter later.");
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

    private boolean validateForm() {
        fName = etFirstName.getText().toString();
        if (!validateName(etFirstName, fNameLyt)) {
            return false;
        }
        lName = etLastName.getText().toString();
        if (!validateName(etLastName, lNameLyt)) {
            return false;
        }
        email = etEmail.getText().toString();
        if (!validateEmail(etEmail, emailLyt)) {
            return false;
        }
        regNo = etRegNo.getText().toString();
        if (!validateRegNo(etRegNo, regNoLyt)) {
            return false;
        }
        password = etPassword.getText().toString();
        if (!validatePassword(etPassword, passLyt)) {
            return false;
        }
        if (!validateConfPassword(etPassword, etConfPassword, confPassLyt)) {
            return false;
        }

        return true;
    }

}
