package com.trova.supercraft;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.trova.android.TrovaEvents;

import static com.trova.supercraft.MyJobDetailsActivity.updateAllJobs;
import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.SuperCraftUtils.handleMessageData;
import static com.trova.supercraft.SuperCraftUtils.initAmazonS3;
import static com.trova.supercraft.SuperCraftUtils.initTrova;
import static com.trova.supercraft.SuperCraftUtils.logInfo;
import static com.trova.supercraft.SuperCraftUtils.updateUnreadMessagesCount;
import static in.trova.android.TrovaEvents.EVENT_MESSAGE_RECEIVED;
import static in.trova.android.TrovaEvents.EVENT_MESSAGE_SYNC;

/**
 * Created by Panchakshari on 17/10/2016.
 */
public class TrovaMessageCallback {
    private static Context context;

    public TrovaMessageCallback(Context context) {
        this.context = context;
    }

    public static void initMyGlobals() {
        if(myGlobals == null) {
            myGlobals = new MyGlobals(context);
            if(myGlobals != null) {
                if (myGlobals.userProfile != null) {
                    logInfo("UserProfile", "TRUE");
                    if (!myGlobals.activityInForeGround) {
                        initTrova(context, "+91" + myGlobals.userProfile.getUserEmail(), "+91" + myGlobals.userProfile.getUserEmail());
                    }
                    try {
                        int cnt = 0;
                        while (!myGlobals.initSuccess) {
                            cnt++;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Crashlytics.logException(e);
                                e.printStackTrace();
                            }
                            if (cnt > 500) {
                                // post message and return / exit
                                return;
                            }
                        }
                    } catch (Exception e) {
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    }
                }
                // Launch Chat Page
                if (myGlobals.initSuccess) {
                    if (!myGlobals.activityInForeGround) {
                        initAmazonS3(context, myGlobals.amazonCognitoId, myGlobals.amazonAWSBucket);
                    }
                }
            }
        }
    }

    public void messageCallback(TrovaEvents eventId, JSONArray jsonArray, String from) {
        logInfo("TrovaMessageCallback ", "eventId " + eventId);
        initMyGlobals();
        JSONArray agentJsonArrayList = null;
        try {
            agentJsonArrayList = jsonArray.getJSONArray(0);

            logInfo("Total Agent Messages ", agentJsonArrayList.length());
            for (int i = 0; i < agentJsonArrayList.length(); i++) {
                JSONObject agentJsonObject = agentJsonArrayList.getJSONObject(i);
                if (agentJsonObject != null) {
                    logInfo("agentJsonObject", agentJsonObject.toString());
                    String userCallerPhone = agentJsonObject.getString("userCallerPhone");
                    String productId = null;
                    if (agentJsonObject.has("productId"))
                        productId = agentJsonObject.getString("productId");
                    handleMessageData(agentJsonObject, userCallerPhone);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void messageCallback(TrovaEvents eventId, JSONObject jsonObject, String from) {
        logInfo("TrovaMessageCallback ", "eventId " + eventId);

        initMyGlobals();

        logInfo("TrovaMessageCallback ", "eventId " + eventId);

        String message = null;
        if(jsonObject.has("message")) {
            try {
                message = jsonObject.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String agentName = null;
        String callerPhone = null;

        switch(eventId) {
            case EVENT_INIT_SUCCESS :
                myGlobals.initSuccess = true;
                break;
            case EVENT_CHAT_MESSAGES :
                myGlobals.initSuccess = true;
                break;
            case EVENT_USER_INFO :
                try {
                    logInfo("executeReciveUserInfo", jsonObject.toString());
                    myGlobals.userId = jsonObject.getString("userId");
                    myGlobals.userPhone = "+" + jsonObject.getString("userPhone");
                    myGlobals.userName = jsonObject.getString("userName");
                    myGlobals.isFromMobileWidget = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case EVENT_AGENT_INFO :
            case EVENT_INIT_CHAT_SUCCESS :
                /*try {
                    logInfo("executeReciveAgentInfo", jsonObject.toString());
                    if(jsonObject.has("productId"))
                        productId = jsonObject.getString("productId");
                    if(jsonObject.has("agentName"))
                        agentName = jsonObject.getString("agentName");
                    if(jsonObject.has("agentPhone"))
                        agentPhone = "+" + jsonObject.getString("agentPhone");
                    if(jsonObject.has("callMode"))
                        callMode = jsonObject.getString("callMode");
                    if(jsonObject.has("callType"))
                        callType = jsonObject.getString("callType");
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

                Intent chatIntent = new Intent(myGlobals.context, MessageActivity.class);
                callerPhone = "91" + myGlobals.currJobActive.getDoctorEmail();
                agentName = myGlobals.currJobActive.getDoctorFname();
                if(myGlobals.loggedinUserType == 1) {
                    myGlobals.currentChatUserType = 3;
                    callerPhone = "91" + myGlobals.currJobActive.getEngineerEmail();
                    agentName = myGlobals.currJobActive.getEngineerFname();
                } else  if(myGlobals.loggedinUserType == 2) {
                    myGlobals.currentChatUserType = 3;
                    callerPhone = "91" + myGlobals.currJobActive.getEngineerEmail();
                    agentName = myGlobals.currJobActive.getEngineerFname();
                } else  if(myGlobals.loggedinUserType == 3) {
                    myGlobals.currentChatUserType = 1;
                    callerPhone = "91" + myGlobals.currJobActive.getDoctorEmail();
                    agentName = myGlobals.currJobActive.getDoctorFname();
                }
                chatIntent.putExtra("callerPhone", "+" + callerPhone);
                chatIntent.putExtra("agentName", agentName);
                chatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(chatIntent);
                break;
            case EVENT_MESSAGE_DELIVERED :
                try {
                    final long msgId = jsonObject.getLong("messageId");
                    String productId = jsonObject.getString("productId");
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (MessageActivity.messageActivity != null) {
                                MessageActivity.messageActivity.updatemessageId(msgId);
                            }
                        }
                    });
                    myGlobals.dbHandler.updateMessageId(msgId, productId);
                    String callerId = "+";
                    myGlobals.trovaApi.despatchChatEvents(msgId, "+" + myGlobals.userId, callerId, productId, EVENT_MESSAGE_SYNC);
                }catch(Exception e){
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }
                break;
            case EVENT_MESSAGE_READ :
                try {
                    String callerId = jsonObject.getString("callerId");
                    String productId = jsonObject.getString("productId");
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (MessageActivity.messageActivity != null) {
                                MessageActivity.messageActivity.updateReadMessage();
                            }
                        }
                    });
                    myGlobals.dbHandler.updateUnseenMessageStatus(callerId, productId);
                }catch(Exception e){
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }
                break;
            case EVENT_MESSAGE_DESPATCHED :
                String fileExt = null;
                String fileName = null;
                String filePath = null;
                String mimeType = null;
                String duration = null;
                String mediaLink = null;
                String productId = null;
                long messageId = 0;
                long fileSize = 0;
                try {
                    messageId = jsonObject.getLong("messageId");
                    productId =jsonObject.getString("productId");
                    fileExt = jsonObject.getString("fileExt");
                    fileName = jsonObject.getString("fileName");
                    filePath = jsonObject.getString("filePath");
                    fileSize = jsonObject.getLong("fileSize");
                    mimeType = jsonObject.getString("mimeType");
                    duration = jsonObject.getString("duration");
                    mediaLink = jsonObject.getString("mediaLink");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                myGlobals.dbHandler.updateUploadResource(messageId, productId, fileExt, fileName, filePath, fileSize, mimeType, duration, mediaLink);
                break;
            case EVENT_ALREADY_USER_REGISTERED :
                new MyNotification(context, message, from, null);
                break;
            case EVENT_MESSAGE_SYNC :
            case EVENT_MESSAGE_RECEIVED :
                String userCallerPhone = "";
                try {
                    if(eventId == EVENT_MESSAGE_RECEIVED) {
                        if (jsonObject.has("callerPhone"))
                            userCallerPhone = jsonObject.getString("callerPhone");
                    } else {
                        if (jsonObject.has("userCallerPhone"))
                            userCallerPhone = jsonObject.getString("userCallerPhone");
                    }
                    handleMessageData(jsonObject, userCallerPhone);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*String msg = "TestCallbackClass " + message + " From " + from;
                System.out.println(msg);
                new MyNotification(context, message, from);*/
                break;
            case EVENT_SYNC :
                if(jsonObject != null) {
                    logInfo("EVENT_NOTIFICATION", jsonObject.toString());
                    String action = null, syncId = null, appstate = null;
                    String jobId = null;
                    String date = null;
                    String userId = null;
                    String priority = null;
                    JSONObject obj = null;

                    try {
                        syncId = jsonObject.getString("syncId");
                        appstate = jsonObject.getString("appstate");
                        date = jsonObject.getString("generatedTime");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(appstate != null) {
                        try {
                            obj = new JSONObject(appstate);
                            logInfo("Notification", obj.toString());
                            action = obj.getString("action");
                            jobId = obj.getString("jobId");
                            userId = obj.getString("fromId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    int actionId = -1;
                    switch(action) {
                        case "ADDEDJOB" :
                            actionId = 1;
                            break;
                        case "CRITICAL" :
                        case "NONCRITICAL" :
                            actionId = 2;
                            break;
                        case "APPROVED" :
                            actionId = 3;
                            break;
                        case "ADDEDDICOM" :
                            actionId = 4;
                            break;
                        case "ADDEDSTL" :
                            actionId = 5;
                            break;
                        case "MODIFIEDPROFILE" :
                            actionId = 6;
                            break;
                        case "ACCEPTEDJOB" :
                            actionId = 7;
                            break;
                        case "REMOVEACCEPTEDJOB" :
                            actionId = 8;
                            break;
                    }

                    if((actionId == 1) || (actionId == 4) || (actionId == 5)) {
                        getJobFromServer(jobId);
                    } else if(actionId == 2) { // ||(actionId == 3)) {
                        priority = "non critical";
                        if(action.equals("CRITICAL"))
                            priority = "critical";
                        myGlobals.dbHandler.updateJobPriority(jobId, priority);
                    } else if(actionId == 3) {
                        getJobFromServer(jobId);
                        myGlobals.dbHandler.updateJobStatus(jobId, 2);
                    } else if(actionId == 7) {
                        getJobFromServer(jobId);
                        myGlobals.dbHandler.updateJobStatus(jobId, 1);
                    } else if(actionId == 8) {
                        myGlobals.dbHandler.deleteJob(jobId);
                    }

                    logInfo("updateAllJobs", "Updating ............0");
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Thread(new Runnable() {
                        public void run() {
                            logInfo("updateAllJobs", "Updating ............1");
                            if(myGlobals.myJobsActivity != null) {
                                logInfo("updateAllJobs", "Updating ............2");
                                if (myGlobals.myJobsActivity instanceof MyJobsActivity) {
                                    logInfo("updateAllJobs", "Updating ............3");
                                    updateAllJobs();
                                }
                            }
                        }
                    }));
                }
                break;
            case EVENT_NOTIFICATION :
                if(jsonObject != null) {
                    logInfo("EVENT_NOTIFICATION", jsonObject.toString());
                    String action = null, notifyId = null, notification = null;
                    String jobId = null;
                    String date = null;
                    String userId = null;
                    String priority = null;
                    JSONObject obj = null;

                    try {
                        notifyId = jsonObject.getString("notificationId");
                        notification = jsonObject.getString("notification");
                        date = jsonObject.getString("generatedTime");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(notification != null) {
                        try {
                            obj = new JSONObject(notification);
                            logInfo("Notification", obj.toString());
                            action = obj.getString("action");
                            jobId = obj.getString("jobId");
                            userId = obj.getString("fromId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    if(action != null) {
                        String actionStr = null;
                        int actionId = -1;
                        switch(action) {
                            case "ADDEDJOB" :
                                actionId = 1;
                                actionStr = "Created New Job : #00000" + jobId;
                                break;
                            case "CRITICAL" :
                            case "NONCRITICAL" :
                                actionId = 2;
                                actionStr = "Changed priority for Job : #00000" + jobId;
                                break;
                            case "APPROVED" :
                                actionId = 3;
                                actionStr = "Approved Job : #00000" + jobId;
                                break;
                            case "ADDEDDICOM" :
                                actionStr = "New DICOM Available for Job : #00000" + jobId;
                                actionId = 4;
                                break;
                            case "ADDEDSTL" :
                                actionStr = "New 3D Model Available for Job : #00000" + jobId;
                                actionId = 5;
                                break;
                            case "MODIFIEDPROFILE" :
                                actionId = 6;
                                break;
                            case "ACCEPTEDJOB" :
                                actionStr = "Accepted Your Job : #00000" + jobId;
                                actionId = 7;
                                break;
                            case "REMOVEACCEPTEDJOB" :
                                actionId = 8;
                                break;
                        }
                        if(actionId != -1) {
                            myGlobals.dbHandler.addNotificatons(notifyId, actionId, jobId, userId, date);
                            int count = myGlobals.dbHandler.getNotificationCount();
                            if (count > 0) {
                                updateUnreadMessagesCount();
                            }

                            if((actionId == 1) || (actionId == 4) || (actionId == 5)) {
                                getJobFromServer(jobId);
                            } else if(actionId == 2) { // ||(actionId == 3)) {
                                priority = "non critical";
                                if(action.equals("CRITICAL"))
                                    priority = "critical";
                                myGlobals.dbHandler.updateJobPriority(jobId, priority);
                            } else if(actionId == 3) {
                                getJobFromServer(jobId);
                                myGlobals.dbHandler.updateJobStatus(jobId, 2);
                            } else if(actionId == 7) {
                                getJobFromServer(jobId);
                                myGlobals.dbHandler.updateJobStatus(jobId, 1);
                            } else if(actionId == 8) {
                                myGlobals.dbHandler.deleteJob(jobId);
                            }

                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Thread(new Runnable() {
                                public void run() {
                                    if(myGlobals.myJobsActivity != null) {
                                        if (myGlobals.myJobsActivity instanceof MyJobsActivity) {
                                            updateAllJobs();
                                        }
                                    }
                                }
                            }));
                            new MyNotification(myGlobals.context, actionStr, jobId);
                        }
                    }
                }
                break;
        }

    }

    private void getJobFromServer(final String jobId) {
        String keyValuePairs = "jobId=" + jobId + "&isFromMobile=" + true;
        logInfo("keyValuePairs", keyValuePairs);
        String response = MyServiceHandler.sendRequest2Server(myGlobals.serverIp, "getJobById", keyValuePairs, "POST", null);
        try {
            if((response != null) && (!response.isEmpty())) {
                logInfo("getJobById Response", response);
                JSONArray jsonArray = new JSONArray(response);
                if(jsonArray != null) {
                    logInfo("jsonArray.length()", jsonArray.length());
                    for (int i = 0; (i < jsonArray.length()); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject != null) {
                            addJob(jsonObject);
                        }
                    }
                    //fetchFileVersions();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void pullDataFromServer() {
        pullJobsFromServer();
    }

    public static List<MyJobs> pullJobsFromServer() {
        List<MyJobs> myJobsList = null;
        String jobId = "0";//myGlobals.dbHandler.getJobCount();
        String keyValuePairs = "userId=" + myGlobals.userProfile.getUserEmail() + "&jobId=" + jobId + "&role=" + myGlobals.loggedinUserType;
        logInfo("keyValuePairs", keyValuePairs);
        String response = MyServiceHandler.sendRequest2Server(myGlobals.serverIp, "getAllJobs", keyValuePairs, "POST", null);
        try {
            if((response != null) && (!response.isEmpty())) {
                logInfo("getAllJobs Response", response);
                JSONArray jsonArray = new JSONArray(response);
                if(jsonArray != null) {
                    logInfo("jsonArray.length()", jsonArray.length());
                    for (int i = 0; (i < jsonArray.length()); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject != null) {
                            addJob(jsonObject);
                        }
                    }
                    //fetchFileVersions();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return myJobsList;
    }

    private static void addJob(JSONObject jsonObject) {
        try {
            logInfo("jsonObject", jsonObject.toString());
            MyJobs myJobs = new MyJobs();
            myJobs.setJobId(jsonObject.getString("id"));
            myJobs.setPatientName(jsonObject.getString("patientname"));
            myJobs.setPatientId(jsonObject.getString("patientId"));
            myJobs.setHospitalId(jsonObject.getString("hospitalId"));
            myJobs.setDepartmentId(jsonObject.getString("departmentId"));
            myJobs.setDoctorFname(jsonObject.getString("doctorfname"));
            myJobs.setDoctorLname(jsonObject.getString("doctorlname"));
            myJobs.setDoctorEmail(jsonObject.getString("doctoremail"));
            myJobs.setDoctorKey(jsonObject.getString("doctoragentkey"));
            myJobs.setPriority(jsonObject.getString("priority"));
            myJobs.setStatus(jsonObject.getInt("status"));
            myJobs.setCreatedDate(jsonObject.getString("createddate"));
            myJobs.setAcceptedDate(jsonObject.getString("accepteddate"));
            myJobs.setEngineerFname(jsonObject.getString("engineerfname"));
            myJobs.setEngineerLname(jsonObject.getString("engineerlname"));
            myJobs.setEngineerEmail(jsonObject.getString("engineeremail"));
            myJobs.setEngineerKey(jsonObject.getString("engineeragentkey"));
            myJobs.setTechFname(jsonObject.getString("technicianfname"));
            myJobs.setTechLname(jsonObject.getString("technicianlname"));
            myJobs.setTechEmail(jsonObject.getString("technicianemail"));
            myJobs.setTechKey(jsonObject.getString("technicianagentkey"));
            myJobs.setStlFileIds(jsonObject.getString("stluploadFileId"));
            myJobs.setStlFilePath(jsonObject.getString("stlfilepath"));
            myJobs.setStlDoctorFileNotes(jsonObject.getString("doctor_stlfilenotes"));
            myJobs.setStlEngineerFileNotes(jsonObject.getString("engineer_stlfilenotes"));
            myJobs.setMriFile(jsonObject.getString("mridicomfilePath"));
            myJobs.setCtFile(jsonObject.getString("ctdicomfilePath"));
            myJobs.setUsFile(jsonObject.getString("usdicomfilePath"));
            myJobs.setEchoFile(jsonObject.getString("echodicomfilePath"));
            myJobs.setOtherFile(jsonObject.getString("othersdicomfilePath"));
            myJobs.setMriFileId(jsonObject.getString("mridicomuploadFileId"));
            myJobs.setCtFileId(jsonObject.getString("ctdicomuploadFileId"));
            myJobs.setUsFileId(jsonObject.getString("usdicomuploadFileId"));
            myJobs.setEchoFileId(jsonObject.getString("echodicomuploadFileId"));
            myJobs.setOtherFileId(jsonObject.getString("othersdicomuploadFileId"));
            myJobs.setDicomNotes(jsonObject.getString("dicomfilenotes"));
            logInfo("myJobs", myJobs.toString());
            if (myGlobals.dbHandler.addJobs(myJobs)) {
                logInfo("Record Added", "Successfully.........");
            }
        } catch (JSONException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }

    }

    private static void fetchFileVersions() {
        final List<MyJobs> myJobsList = myGlobals.dbHandler.getMyJobs();
        //new Thread() {
            //public void run() {
                if(myJobsList != null) {
                    logInfo("myJobsList count", myJobsList.size());
                    for(int i = 0; (i < myJobsList.size()); i++) {
                        MyJobs myJobs = myJobsList.get(i);
                        String jobId = myJobs.getJobId();
                        String keyValuePairs = "isFromMobile=" + true + "&jobId=" + jobId;
                        String response = MyServiceHandler.sendRequest2Server(myGlobals.serverIp, "jobfileversion", keyValuePairs, "POST", null);
                        try {
                            if ((response != null) && (!response.isEmpty())) {
                                logInfo("rsponse", response);
                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject != null) {
                                    int status = jsonObject.getInt("status");
                                    if (status == 1) {
                                        JSONArray stlFileVersionArray = jsonObject.getJSONArray("stlfileVersionInfo");
                                        if ((stlFileVersionArray != null) && (stlFileVersionArray.length() > 0)) {
                                            StlFileVersions stlFileVersions = new StlFileVersions();
                                            for (int idx = 0; (idx < stlFileVersionArray.length()); idx++) {
                                                JSONObject stlFileObject = stlFileVersionArray.getJSONObject(idx);
                                                if (stlFileObject != null) {
                                                    stlFileVersions.setJobId(jobId);
                                                    stlFileVersions.setFileName(stlFileObject.getString("fileinfo"));
                                                    stlFileVersions.setFileId(stlFileObject.getString("id"));
                                                    stlFileVersions.setDoctorNotes(stlFileObject.getString("doctor_notes"));
                                                    stlFileVersions.setEngineerNotes(stlFileObject.getString("engineer_notes"));
                                                    stlFileVersions.setDate(stlFileObject.getString("date"));
                                                    stlFileVersions.setStatus(stlFileObject.getString("status"));
                                                    myGlobals.dbHandler.addStlFileVersions(stlFileVersions);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            //}
        //}.start();

    }

    public static void fetchJobFileVersions(String jobId) {
        String keyValuePairs = "isFromMobile=" + true + "&jobId=" + jobId;
        String response = MyServiceHandler.sendRequest2Server(myGlobals.serverIp, "jobfileversion", keyValuePairs, "POST", null);
        try {
            if ((response != null) && (!response.isEmpty())) {
                logInfo("rsponse", response);
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject != null) {
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONArray stlFileVersionArray = jsonObject.getJSONArray("stlfileVersionInfo");
                        if ((stlFileVersionArray != null) && (stlFileVersionArray.length() > 0)) {
                            StlFileVersions stlFileVersions = new StlFileVersions();
                            for (int idx = 0; (idx < stlFileVersionArray.length()); idx++) {
                                JSONObject stlFileObject = stlFileVersionArray.getJSONObject(idx);
                                if (stlFileObject != null) {
                                    stlFileVersions.setJobId(jobId);
                                    stlFileVersions.setFileName(stlFileObject.getString("fileinfo"));
                                    stlFileVersions.setFileId(stlFileObject.getString("id"));
                                    stlFileVersions.setDoctorNotes(stlFileObject.getString("doctor_notes"));
                                    stlFileVersions.setEngineerNotes(stlFileObject.getString("engineer_notes"));
                                    stlFileVersions.setDate(stlFileObject.getString("date"));
                                    stlFileVersions.setStatus(stlFileObject.getString("status"));
                                    myGlobals.dbHandler.addStlFileVersions(stlFileVersions);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
