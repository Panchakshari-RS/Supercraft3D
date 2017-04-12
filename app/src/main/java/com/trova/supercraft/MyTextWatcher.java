package com.trova.supercraft;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Created by Panchakshari on 25/3/2017.
 */

public class MyTextWatcher implements TextWatcher {
    private static Activity activity;
    private View view1, view2;
    private TextInputLayout layout;

    public MyTextWatcher(Activity activity, View view1, View view2, TextInputLayout layout) {
        this.activity = activity;
        this.view1 = view1;
        this.view2 = view2;
        this.layout = layout;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void afterTextChanged(Editable editable) {
        int id = view1.getId();

        if(id == R.id.input_name) {
            validateName((EditText)view1, layout);
        } else if(id == R.id.input_lname) {
            validateName((EditText)view1, layout);
        } else if(id == R.id.input_email) {
            validateEmail((EditText)view1, layout);
        } else if(id == R.id.input_medical_regno) {
            validateRegNo((EditText)view1, layout);
        } else if(id == R.id.input_password) {
            validatePassword((EditText)view1, layout);
        } else if(id == R.id.input_conf_password) {
            validateConfPassword((EditText)view2, (EditText)view1, layout);
        } else if(id == R.id.input_old_password) {
            validatePassword((EditText)view1, layout);
        } else if(id == R.id.input_new_password) {
            validatePassword((EditText)view1, layout);
        } else if(id == R.id.input_conf_new_password) {
            validateConfPassword((EditText)view2, (EditText)view1, layout);
        }
    }

    public static boolean validateName(EditText et, TextInputLayout lyt) {
        if (et.getText().toString().trim().isEmpty()) {
            lyt.setError(activity.getResources().getString(R.string.err_Name));
            lyt.setErrorEnabled(true);
            requestFocus(et);
            return false;
        } else {
            lyt.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validateRegNo(EditText etRegNo, TextInputLayout regNoLyt) {
        if (etRegNo.getText().toString().trim().isEmpty()) {
            regNoLyt.setError(activity.getResources().getString(R.string.err_RegNo));
            regNoLyt.setErrorEnabled(true);
            requestFocus(etRegNo);
            return false;
        } else if (etRegNo.getText().length() < 6) {
            regNoLyt.setError(activity.getResources().getString(R.string.err_RegNoLen));
            regNoLyt.setErrorEnabled(true);
            requestFocus(etRegNo);
            return false;
        } else {
            regNoLyt.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validatePassword(EditText etPassword, TextInputLayout passLyt) {
        if (etPassword.getText().toString().trim().isEmpty()) {
            passLyt.setError(activity.getResources().getString(R.string.err_Password));
            passLyt.setErrorEnabled(true);
            requestFocus(etPassword);
            return false;
        } else if (etPassword.getText().length() < 6) {
            passLyt.setError(activity.getResources().getString(R.string.err_PasswordLen));
            passLyt.setErrorEnabled(true);
            requestFocus(etPassword);
            return false;
        } else {
            passLyt.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validateConfPassword(EditText etPassword, EditText etConfPassword, TextInputLayout confPassLyt) {
        String confPass = etConfPassword.getText().toString();
        String pass = etPassword.getText().toString();
        if (confPass.trim().isEmpty()) {
            confPassLyt.setError(activity.getResources().getString(R.string.err_ConfPass));
            confPassLyt.setErrorEnabled(true);
            requestFocus(etConfPassword);
            return false;
        } else if (!confPass.equals(pass)) {
            confPassLyt.setError(activity.getResources().getString(R.string.err_PassConfPassEqual));
            confPassLyt.setErrorEnabled(true);
            requestFocus(etConfPassword);
            return false;
        } else {
            confPassLyt.setErrorEnabled(false);
        }

        return true;
    }

    public static boolean validateEmail(EditText etEmail, TextInputLayout emailLyt) {
        String email = etEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            emailLyt.setError(activity.getResources().getString(R.string.err_Email));
            emailLyt.setErrorEnabled(true);
            requestFocus(etEmail);
            return false;
        } else {
            emailLyt.setErrorEnabled(false);
        }

        return true;
    }

    private static void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
