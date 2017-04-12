package com.trova.supercraft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.SuperCraftUtils.initTrovaChat;
import static com.trova.supercraft.SuperCraftUtils.launchChatNotificationListActivity;
import static com.trova.supercraft.SuperCraftUtils.logInfo;
import static com.trova.supercraft.TrovaMessageCallback.initMyGlobals;

/**
 * Created by Panchakshari on 9/2/2016.
 */
public class MyBroadCastReceiver extends BroadcastReceiver {
    private String TAG = "BroadCastReceiver --> ";

    @Override
    public void onReceive(Context context, Intent intent) {

        logInfo(TAG, "Called ........");
        if(intent != null) {
            String action = intent.getAction();

            initMyGlobals();

            if (action.equals("OpenChatPage")) {
                Bundle jobInfo = intent.getBundleExtra("jobInfo");
                if(myGlobals != null) {
                    if(!launchChatNotificationListActivity(false)) {
                        if (jobInfo != null) {
                            logInfo("jobInfo", "TRUE");
                            String jobId = jobInfo.getString("productId");
                            MyJobs myJobs = myGlobals.dbHandler.getMyJob(jobId);
                            if (myJobs != null) {
                                logInfo("myJobs", "TRUE");
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

                                logInfo("initTrovaChat", "Called ..........");
                                initTrovaChat(context, agentKey);
                            }
                        }
                    }
                }
            } else if(action.equals("OpenNotificationPage")) {
                Log.i("BroadCastReceiver", "OpenNotificationPage");
            }
        }
    }

}
