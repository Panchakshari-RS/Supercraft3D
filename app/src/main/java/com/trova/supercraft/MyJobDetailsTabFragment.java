package com.trova.supercraft;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import static com.trova.supercraft.MyJobDetailsActivity.updateAllJobs;
import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.SuperCraftUtils.logInfo;

/**
 * Created by Panchakshari on 16/3/2017.
 */

public class MyJobDetailsTabFragment extends Fragment implements View.OnClickListener {
    private static Context context;
    private View contentview = null;
    private CheckBox jobCritical;
    private TextView jobCriticalText;
    private SwitchCompat switchCompat;
    private static Handler handler = null;
    private static boolean success = false;
    private static ProgressDialog progressBar;
    private String priority;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        logInfo("MySTLViewerTabFragment", "0 .........");
        contentview = inflater.inflate(R.layout.details_viewer_tab, container, false);
        context = this.getContext();

        TextView tv = (TextView)contentview.findViewById(R.id.patient_name_label);
        tv.setTypeface(myGlobals.centuryGothic);
        tv = (TextView)contentview.findViewById(R.id.patient_name);
        tv.setTypeface(myGlobals.centuryGothic);
        tv = (TextView)contentview.findViewById(R.id.status_label);
        tv.setTypeface(myGlobals.centuryGothic);
        tv = (TextView)contentview.findViewById(R.id.status);
        tv.setTypeface(myGlobals.centuryGothic);
        tv = (TextView)contentview.findViewById(R.id.priority_label);
        tv.setTypeface(myGlobals.centuryGothic);
        tv = (TextView)contentview.findViewById(R.id.job_critical_text);
        tv.setTypeface(myGlobals.centuryGothic);
        tv = (TextView)contentview.findViewById(R.id.stl_version);
        tv.setTypeface(myGlobals.centuryGothic);
        tv = (TextView)contentview.findViewById(R.id.notes_label);
        tv.setTypeface(myGlobals.centuryGothic);

        tv = (TextView) contentview.findViewById(R.id.patient_name);
        String udata = myGlobals.patientName;
        //SpannableString content = new SpannableString(udata);
        //content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);//where first 0 shows the starting and udata.length() shows the ending span.if you want to span only part of it than you can change these values like 5,8 then it will underline part of it.
        tv.setText(udata);
        tv = (TextView) contentview.findViewById(R.id.status);
        int status = myGlobals.currJobActive.getStatus();
        String strStatus = null;
        switch(status) {
            case 0 :
                strStatus = "New";
                break;
            case 1 :
                strStatus = "Progress";
                break;
            case 2 :
                strStatus = "Completed";
                break;
            case 6 :
                strStatus = "Printing";
                break;
        }
        tv.setText(strStatus);

        tv = null;
        if(myGlobals.stlFileVersionsList != null) {
            LinearLayout llf = (LinearLayout) contentview.findViewById(R.id.stl_lyt);
            ViewGroup.LayoutParams lpf = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            LinearLayout lln = (LinearLayout) contentview.findViewById(R.id.notes_lyt);
            ViewGroup.LayoutParams lpn = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            String fileName, notes;
            for (int i = 0; (i < myGlobals.stlFileVersionsList.size()); i++) {
                StlFileVersions stlFileVersions = myGlobals.stlFileVersionsList.get(i);
                if(stlFileVersions != null) {
                    fileName = stlFileVersions.getFileName();
                    if((fileName != null) && (!fileName.equals("")) && (!fileName.equals("null")) && (fileName.length() > 0)) {
                        tv = new TextView(context);
                        tv.setPadding(25, 5, 5, 5);
                        tv.setText(fileName);
                        tv.setLayoutParams(lpf);
                        tv.setTypeface(myGlobals.centuryGothic);
                        llf.addView(tv);
                    }
                    notes = stlFileVersions.getDoctorNotes();
                    if((notes != null) && (!notes.equals("")) && (!notes.equals("null")) && (notes.length() > 0)) {
                        notes = notes.replace("<br />", "\n");
                        notes = notes.replace("<br/>", "\n");
                        notes = notes.replace("<br >", "\n");
                        notes = notes.replace("<br>", "\n");
                        tv = new TextView(context);
                        tv.setPadding(25, 5, 5, 5);
                        tv.setText(notes);
                        tv.setLayoutParams(lpn);
                        tv.setTypeface(myGlobals.centuryGothic);
                        lln.addView(tv);
                    }
/*
                    notes = stlFileVersions.getEngineerNotes();
                    if((notes != null) && (!notes.equals("")) && (!notes.equals("null")) && (notes.length() > 0)) {
                        notes = notes.replace("<br />", "\n");
                        notes = notes.replace("<br/>", "\n");
                        notes = notes.replace("<br >", "\n");
                        notes = notes.replace("<br>", "\n");
                        tv = new TextView(context);
                        tv.setPadding(25, 5, 5, 5);
                        tv.setText(notes);
                        tv.setLayoutParams(lpn);
                        lln.addView(tv);
                    }
*/
                }
            }
        } else {
            LinearLayout ll = (LinearLayout) contentview.findViewById(R.id.notes_lyt);
            ll.setVisibility(View.GONE);
            ((TextView) contentview.findViewById(R.id.stl_version)).setText("No Stl Files");
        }

        ImageView rawView = (ImageView) contentview.findViewById(R.id.raw_view);
        rawView.setOnClickListener(this);

        jobCriticalText = (TextView) contentview.findViewById(R.id.job_critical_text);
        switchCompat = (SwitchCompat) contentview.findViewById(R.id.switchButton);

        priority = myGlobals.currJobActive.getPriority();
        if (priority.equals("critical")) {
            switchCompat.setChecked(true);
            jobCriticalText.setText("Critical");
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    jobCriticalText.setText("Critical");
                } else {
                    jobCriticalText.setText("Non Critical");
                }

                updateJobPriority();
            }
        });

        return contentview;
    }

    private void updateJobPriority() {

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
                updatePriority();
            }
        }, 10);

    }

    private void updatePriority() {

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (success) {
                    //update db and refresh
                    myGlobals.dbHandler.updateJobPriority(myGlobals.jobId, priority);
                    updateAllJobs();
                }
                progressBar.dismiss();
            }
        };

        new Thread() {
            public void run() {
                int status = 0;
                priority = jobCriticalText.getText().toString();
                priority = priority.toLowerCase();
                String keyValuePairs = "priority=" + priority + "&isFromMobile=" + true + "&jobId=" + myGlobals.jobId;
                String response = MyServiceHandler.sendRequest2Server(myGlobals.serverIp, "changepriority", keyValuePairs, "POST", null);
                try {
                    if ((response != null) && (!response.isEmpty())) {
                        logInfo("Response", response);
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        status = jsonObject.getInt("status");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status == 1) {
                    success = true;
                    String callerPhone = null;
                    String message = null;
                    JSONObject jsonObject = new JSONObject();
                    try {
                        if(priority.equals("critical"))
                            jsonObject.put("action", "CRITICAL");
                        else
                            jsonObject.put("action", "NONCRITICAL");
                        jsonObject.put("jobId", myGlobals.jobId);
                        jsonObject.put("name", myGlobals.userProfile.getUserName());
                        jsonObject.put("fromId", "91" + myGlobals.userProfile.getUserEmail());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String cName= null, oName = null;
                    message = jsonObject.toString();
                    logInfo("message", message);
                    if(myGlobals.loggedinUserType == 1) {
                        cName = myGlobals.currJobActive.getEngineerEmail();
                        oName = myGlobals.currJobActive.getTechEmail();
                    } else if(myGlobals.loggedinUserType == 2) {
                        cName = myGlobals.currJobActive.getEngineerEmail();
                        oName = myGlobals.currJobActive.getDoctorEmail();
                    } else if(myGlobals.loggedinUserType == 3) {
                        cName = myGlobals.currJobActive.getTechEmail();
                        oName = myGlobals.currJobActive.getDoctorEmail();
                    }
                    if((cName != null) && (cName != "") && (!cName.equals("null")) && (cName.length() > 0)) {
                        callerPhone = "91" + cName;
                        myGlobals.trovaApi.pushNotification(message, callerPhone, "high", "0");
                    }
                    if((oName != null) && (oName != "") && (!oName.equals("null")) && (oName.length() > 0)) {
                        callerPhone = "91" + oName;
                        myGlobals.trovaApi.pushNotification(message, callerPhone, "high", "0");
                    }
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.raw_view) {
            Intent intent = new Intent(context, RawActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

}
