package com.trova.supercraft;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.crashlytics.android.Crashlytics;
import com.trova.supercraft.Notification.ChatNotificationActivity;
import com.trova.supercraft.Notification.MyChatNotificationsRowItem;
import com.trova.supercraft.Notification.MyNotificationsRowItem;
import com.trova.supercraft.Notification.NotificationActivity;
import com.trova.supercraft.Profile.MyChangePasswordActivity;
import com.trova.supercraft.Profile.MyEditProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.trova.supercraft.AmazonS3.AmazonS3Utils.downloadResourceFromAmazonS3;
import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.MyNewJobsTabFragment.launchDetailsActivity;
import static in.trova.android.TrovaEvents.EVENT_AGENT_MESSAGE_UPDATE;
import static in.trova.android.TrovaEvents.EVENT_MESSAGE_READ;
import static in.trova.android.TrovaEvents.EVENT_MESSAGE_SYNC;


/**
 * Created by Panchakshari on 27/2/2017.
 */

public class SuperCraftUtils {
    private static long bytesReceived;
    private static TransferState currentState = TransferState.UNKNOWN;
    private static final float RADIUS_FACTOR = 30.0f;
    private static final int TRIANGLE_WIDTH = 20;
    private static final int TRIANGLE_HEIGHT = 50;
    private static final int TRIANGLE_OFFSET = 80;
    private static Handler handler = null;
    private static boolean success = false;

    public static boolean checkInternetConenction(Context context) {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if(connec != null) {
            if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
                return true;
            } else if (
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                            connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
                return false;
            }
        }
        return false;
    }

    public static void jsonValuePut(JSONObject jsonObject, String key, String value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    public static void jsonValuePut(JSONObject jsonObject, String key, String[] value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    public static void jsonValuePut(JSONObject jsonObject, String key, long value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    public static void jsonValuePut(JSONObject jsonObject, String key, boolean value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }
    public static void jsonValuePut(JSONObject jsonObject, String key, JSONObject value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    public static void jsonValuePut(JSONObject jsonObject, String key, int value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    public static String getCountryCode(String phone, String[] codes, int length) {
        List<String> l = new ArrayList<String>();
        String match = phone.substring(0, length);
        String code = null;
        for (int i = 0; i < codes.length; i++) {
            if (codes[i].startsWith(match)) {
                l.add(codes[i]);
            }
        }
        String[] stringArray = Arrays.copyOf(l.toArray(), l.toArray().length, String[].class);
        if (l.size() != 0) {
            if (l.size() > 1) {
                code = getCountryCode(phone, stringArray, length + 1);
            } else {
                code = l.get(0);
            }
        }
        return code;
    }

    public static void handleMessageData(final JSONObject jsonObject, String userCallerPhone) {
        CommonDateTimeZone commonDateTimeZone=new CommonDateTimeZone();
        try {
            String callerPhone = "+" + jsonObject.getString("callerPhone");
            String callerName = jsonObject.getString("callerName");
            String productId = "";
            int historyData = 0;

            if(jsonObject.has("historyData"))
                historyData = jsonObject.getInt("historyData");

            if(jsonObject.has("productId"))
                productId = jsonObject.getString("productId");

            if(productId.equals("") || productId.equals("0"))
                return;

            long fileSize = 0;
            String mode = jsonObject.getString("mode");
            String type = jsonObject.getString("mimetype");
            String message = null, mediaLink = null;
            long messageId = jsonObject.getLong("messageId");
            String fileExt = null, fileName = null, thumbFilePath = null, filePath = null, duration = null;
            Bitmap bmp = null;
            File thumbFile = null;
            boolean done = true;

            logInfo("mode", mode);
            logInfo("mimetype", type);
            if (mode.equals("text")) {
                message = jsonObject.getString("data");
                message = message.replace("<br />", "\n");
                message = message.replace("<br/>", "\n");

                logInfo("Mode", mode);
                logInfo("data", message);
            } else if (mode.equals("stream")) {
                fileExt = jsonObject.getString("fileExt");
                if(jsonObject.has("fileName"))
                    fileName = jsonObject.getString("fileName");
                if(jsonObject.has("thumbFilePath"))
                    thumbFilePath = jsonObject.getString("thumbFilePath");
                if(jsonObject.has("filePath"))
                    filePath = jsonObject.getString("filePath");
                if(jsonObject.has("duration"))
                    duration = jsonObject.getString("duration");
                if(jsonObject.has("fileSize"))
                    fileSize = jsonObject.getLong("fileSize");
                if(jsonObject.has("mediaLink"))
                    mediaLink = jsonObject.getString("mediaLink");
            }

            int readMessage = 0;
            if(jsonObject.has("readMessage")) {
                readMessage = jsonObject.getInt("readMessage");
                logInfo("readMessage", "=================");
            }

            int status = 1; //received message flag = 1, sent message flag = 0
            if(jsonObject.has("status")) {
                status = jsonObject.getInt("status");
                logInfo("status", "=================");
            }
            int seenStatus = 1; //viewed message flag = 0, not viewed message flag = 1
            if(jsonObject.has("seenStatus")) {
                seenStatus = jsonObject.getInt("seenStatus");
                logInfo("seenStatus", "=================");
            }

            int sentOrReceived = 2;
            logInfo("status", status + " seenStatus " + seenStatus);
            logInfo("sentOrReceived", sentOrReceived + " callerPhone " + callerPhone + " userCallerPhone " + userCallerPhone);
            if(jsonObject.has("sentOrReceived")) {
                sentOrReceived = jsonObject.getInt("sentOrReceived");
                logInfo("sentOrReceived", "=================");
            }

            logInfo("sentOrReceived", sentOrReceived + " callerPhone " + callerPhone + " userCallerPhone " + userCallerPhone);

            if(MessageActivity.messageActivity != null){
                logInfo("messageViewActivity","messageViewActivity");
                if(status == 0) {
                    refreshReceivedMessage(seenStatus, readMessage, sentOrReceived, callerName, callerPhone, message, commonDateTimeZone, mode, fileExt, fileName, fileSize, type, thumbFilePath, filePath, duration, messageId, mediaLink, productId);
                } else {
                    if ((MessageActivity.messageActivity.callerPhone.equals(callerPhone)) && (myGlobals.jobId.equals(productId))) {
                        refreshReceivedMessage(seenStatus, readMessage, sentOrReceived, callerName, callerPhone, message, commonDateTimeZone, mode, fileExt, fileName, fileSize, type, thumbFilePath, filePath, duration, messageId, mediaLink, productId);
                    } else {
                        saveMessageAndPost(historyData, seenStatus, readMessage, sentOrReceived, callerName, callerPhone, message, commonDateTimeZone, mode, fileExt, fileName, fileSize, type, thumbFilePath, filePath, duration, messageId, mediaLink, productId);
                    }
                }
            }else{
                saveMessageAndPost(historyData, seenStatus, readMessage, sentOrReceived, callerName, callerPhone, message, commonDateTimeZone,mode, fileExt, fileName, fileSize, type, thumbFilePath, filePath, duration, messageId,mediaLink, productId);
            }

            String userId = myGlobals.userId;
            userId = callerPhone;
            if(sentOrReceived == 2) {
                logInfo("Despatching Event", "Update callerPhone " + callerPhone + " userId " + myGlobals.userId);
                myGlobals.trovaApi.despatchChatEvents(messageId, callerPhone, userId, productId, EVENT_AGENT_MESSAGE_UPDATE);
            }

            if(((MessageActivity.messageActivity != null) && (MessageActivity.messageActivity.callerPhone.equals(callerPhone)) && (myGlobals.jobId.equals(productId))) &&
                    (((readMessage == 1) && (status == 0)) || ((readMessage == 0) && (status == 1)))) {
                logInfo("SuperCraft", "Posting ReadMessage ............");
                logInfo("Despatching Event", "Read callerPhone " + callerPhone + " userId " + myGlobals.userId);
                myGlobals.trovaApi.despatchChatEvents(messageId, callerPhone, userId, productId, EVENT_MESSAGE_READ);
            }
            logInfo("Despatching Event", "Sync callerPhone " + callerPhone + " userId " + myGlobals.userId);
            myGlobals.trovaApi.despatchChatEvents(messageId, "+" + myGlobals.userId, callerPhone, productId, EVENT_MESSAGE_SYNC);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private static void refreshReceivedMessage(int seenStatus, int readMessage, int sentOrReceived, String callerName, String callerPhone, String message, CommonDateTimeZone commonDateTimeZone, String mode, String fileExt, String fileName, long fileSize, String type, String thumbFilePath, String filePath, String duration, long messageId, String mediaLink, String productId) {
        logInfo("refreshReceivedMessage", "Called .............");
        String deliveryStatus = "false";
        if(sentOrReceived == 1)
            deliveryStatus = "true";
        final ChatMessages chatMessage = new ChatMessages(message, commonDateTimeZone.currDate, commonDateTimeZone.time, sentOrReceived, messageId, deliveryStatus, mode, fileExt, fileName, fileSize, type, thumbFilePath, filePath, duration, 0, mediaLink, readMessage);
        if ((MessageActivity.messageActivity.callerPhone.equals(callerPhone)) && (myGlobals.jobId.equals(productId))) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Thread(new Runnable() {
                public void run() {
                    try {
                        MessageActivity.addMessage(chatMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
        }

        logInfo("refreshReceivedMessage callerPhone", callerPhone + " productId " + productId);
        myGlobals.dbHandler.addMessageLogs(new ChatMessages(0, callerName, callerPhone, productId, message, commonDateTimeZone.currDate, commonDateTimeZone.time, mode, fileExt, fileName, fileSize, type, thumbFilePath, filePath, duration, 0, sentOrReceived, 0, messageId, deliveryStatus, seenStatus, commonDateTimeZone.timezone, commonDateTimeZone.timemilliseconds, 0, mediaLink, readMessage));
    }

    private static void saveMessageAndPost(int historyData, int seenStatus, int readMessage, int sentOrReceived, String callerName, String callerPhone, String message, CommonDateTimeZone commonDateTimeZone, String mode, String fileExt, String fileName, long fileSize, String type, String thumbFilePath, String filePath, String duration, long messageId, String mediaLink, String productId) {
        String deliveryStatus = "false";
        if(sentOrReceived == 1) {
            deliveryStatus = "true";
        }

        logInfo("saveMessageAndPost callerPhone", callerPhone + " productId " + productId);
        if (myGlobals.dbHandler.addMessageLogs(new ChatMessages(0,callerName, callerPhone, productId, message, commonDateTimeZone.currDate, commonDateTimeZone.time, mode, fileExt, fileName, fileSize, type, thumbFilePath, filePath, duration, 0, sentOrReceived, seenStatus, messageId, deliveryStatus, seenStatus, commonDateTimeZone.timezone, commonDateTimeZone.timemilliseconds, 0, mediaLink, readMessage))) {
            if ((sentOrReceived == 2) && (historyData == 0)) {
                new MyNotification(myGlobals.context, callerName, callerPhone, productId);
            }
        }
        if (myGlobals.activityInForeGround) {
            updateUnreadMessagesCount();
            String className = getTopActivity();

            if(className != null) {
                if((className.equals("com.trova.supercraft.Notification.ChatNotificationActivity")) && (myGlobals.myChatNotificationActivity != null)) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            launchChatNotificationListActivity(false);
                        }
                    });
                }
            }
        }
    }

    public static void updateUnreadMessagesCount() {
        myGlobals.chatCount = myGlobals.dbHandler.getUnReadMessagesCount();
        myGlobals.notifyCount = myGlobals.dbHandler.getNotificationCount();

        logInfo("updateUnreadMessagesCount", "updateUnreadMessagesCount");
        logInfo("Total Count", myGlobals.chatCount + "---" + myGlobals.notifyCount);

        if(myGlobals.myJobsActivity != null) {
            if (myGlobals.myJobsActivity instanceof MyJobsActivity) {
                ((MyJobsActivity) myGlobals.myJobsActivity).onPrepareOptionsMenu(MyJobsActivity.menu);
            }
        }
        if(myGlobals.myJobDetailsActivity != null) {
            if (myGlobals.myJobDetailsActivity instanceof MyJobDetailsActivity) {
                ((MyJobDetailsActivity) myGlobals.myJobDetailsActivity).onPrepareOptionsMenu(MyJobDetailsActivity.menu);
            }
        }
        if(myGlobals.myMessageActivity != null) {
            if (myGlobals.myMessageActivity instanceof MessageActivity) {
                ((MessageActivity) myGlobals.myMessageActivity).onPrepareOptionsMenu(MessageActivity.menu);
            }
        }
        if(myGlobals.myChatNotificationActivity != null) {
            if (myGlobals.myChatNotificationActivity instanceof ChatNotificationActivity) {
                ((ChatNotificationActivity) myGlobals.myChatNotificationActivity).onPrepareOptionsMenu(ChatNotificationActivity.menu);
            }
        }
        if(myGlobals.myNotificationActivity != null) {
            if (myGlobals.myNotificationActivity instanceof NotificationActivity) {
                ((NotificationActivity) myGlobals.myNotificationActivity).onPrepareOptionsMenu(NotificationActivity.menu);
            }
        }
        if(myGlobals.myRawActivity != null) {
            if (myGlobals.myRawActivity instanceof RawActivity) {
                ((RawActivity) myGlobals.myRawActivity).onPrepareOptionsMenu(RawActivity.menu);
            }
        }
        if(myGlobals.myEditProfileActivity != null) {
            if (myGlobals.myEditProfileActivity instanceof MyEditProfileActivity) {
                ((MyEditProfileActivity) myGlobals.myEditProfileActivity).onPrepareOptionsMenu(MyEditProfileActivity.menu);
            }
        }
        if(myGlobals.myChangePasswordActivity != null) {
            if (myGlobals.myChangePasswordActivity instanceof MyChangePasswordActivity) {
                ((MyChangePasswordActivity) myGlobals.myChangePasswordActivity).onPrepareOptionsMenu(MyChangePasswordActivity.menu);
            }
        }
    }

    public static String getTopActivity() {
        String className = null;
        String TAG = "getTopActivity";
        logInfo("getTopActivity", "Called .............");

        ActivityManager am = (ActivityManager) myGlobals.context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> alltasks = am.getRunningTasks(1);
        if(alltasks != null) {
            className = alltasks.get(0).topActivity.getClassName();
            logInfo("topActivity", "CURRENT Activity ::" + className);
        }

        return className;
    }

    public static boolean launchChatNotificationListActivity(boolean launchChatActivity) {
        int count = 0;
        boolean result = false;

        List<MyChatNotificationsRowItem> notificationsList = myGlobals.dbHandler.getUnReadMessages();
        if((notificationsList != null) && (notificationsList.size() > 0)) {
            count = notificationsList.size();
        }

        if(count > 1) {
            result = true;
            if(myGlobals.myChatNotificationActivity == null) {
                Intent notifyIntent = new Intent(myGlobals.context, ChatNotificationActivity.class);
                notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myGlobals.context.startActivity(notifyIntent);
            } else {
                if (myGlobals.myChatNotificationActivity instanceof ChatNotificationActivity) {
                    ((ChatNotificationActivity) myGlobals.myChatNotificationActivity).updateNotifyList();
                }
            }
        } else if((launchChatActivity) && (count == 1)) {
            MyChatNotificationsRowItem notification = notificationsList.get(0);
            String jobId = notification.getJobId();
            MyJobs myJobs = myGlobals.dbHandler.getMyJob(jobId);
            if (myJobs != null) {
                logInfo("myJobs", "TRUE");
                if(myGlobals.currJobActive != null) {
                    myGlobals.prevJobActive = myGlobals.currJobActive;
                    myGlobals.prevJobId = myGlobals.currJobActive.getJobId();
                    myGlobals.prevPatientId = myGlobals.currJobActive.getPatientId();
                    myGlobals.prevPatientName = myGlobals.currJobActive.getPatientName();
                }

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
                initTrovaChat(myGlobals.context, agentKey);
            }
        }

        return result;
    }

    public static boolean launchNotificationListActivity(boolean launchChatActivity) {
        int count = 0;
        boolean result = false;

        List<MyNotificationsRowItem> notificationsList = myGlobals.dbHandler.getNotifications();
        if((notificationsList != null) && (notificationsList.size() > 0)) {
            count = notificationsList.size();
        }

        logInfo("Notification Count", count);
        if(count > 0) {
            result = true;
            if(myGlobals.myNotificationActivity == null) {
                Intent notifyIntent = new Intent(myGlobals.context, NotificationActivity.class);
                notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myGlobals.context.startActivity(notifyIntent);
            } else {
                if (myGlobals.myNotificationActivity instanceof NotificationActivity) {
                    ((NotificationActivity) myGlobals.myNotificationActivity).updateNotifyList();
                }
            }
        } else if((launchChatActivity) && (count == 1)) {
            String topClass = getTopActivity();
            if(topClass != null) {
                if((topClass.equals("com.trova.supercraft.MyJobDetailsActivity")) || (myGlobals.myJobDetailsActivity != null)) {
                    ((MyJobDetailsActivity) myGlobals.myJobDetailsActivity).finish();
                }
            }
            MyNotificationsRowItem notification = notificationsList.get(0);
            String jobId = notification.getJobId();
            int action = notification.getAction();
            myGlobals.dbHandler.updateNotification(jobId, action);

            MyJobs myJobs = myGlobals.dbHandler.getMyJob(jobId);
            myGlobals.isCompletedJob = false;
            if(myJobs.getStatus() == 2)
                myGlobals.isCompletedJob = true;
            launchDetailsActivity(myJobs);
        }

        return result;
    }

    public static Bitmap drawCallout(Bitmap bitmap, float angle, int flipAxis) {
        return processImage(bitmap, angle, flipAxis);
    }

    private static Bitmap processImage(Bitmap bitmap, float angle, int flipAxis) {
        Bitmap bmp;

        bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);

        float radius = RADIUS_FACTOR;//Math.min(bitmap.getWidth(), bitmap.getHeight()) / RADIUS_FACTOR;
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        RectF rect = new RectF(TRIANGLE_WIDTH, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawRoundRect(rect, radius, radius, paint);

        Path triangle = new Path();
        triangle.moveTo(0, TRIANGLE_OFFSET);
        triangle.lineTo(TRIANGLE_WIDTH, TRIANGLE_OFFSET - (TRIANGLE_HEIGHT / 2));
        triangle.lineTo(TRIANGLE_WIDTH, TRIANGLE_OFFSET + (TRIANGLE_HEIGHT / 2));
        triangle.close();
        canvas.drawPath(triangle, paint);


        return RotateBitmap(bmp, angle, flipAxis);
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle, int flip)
    {
        Matrix matrix = new Matrix();
        if(angle > 0.0f)
            matrix.postRotate(angle); //Rotate around an angle
        else if(flip == 1)
            matrix.postScale(-1, 1, 0, 0); //Flip Horizontally
        else
            matrix.postScale(1, -1, 0, 0); //Flip Vertically

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static void initTrova(Context context, String userId, String userPhone) {
        String userName = "Trova.SuperCraft"; // used to display the name when user clicks for call on the call button
        //String businessName = "Supercraft3D"; // used internally by the Trova, This should be same as what he has registered to signup with Trova
        //String businessKey = "2efe5a50dcb53806717bf87428fd31a6"; //Provided by Trova when Blickx signed up for a business
        // data.trova.in panchu
        //String businessKey = "2efe5a50dcb53806717bf87428fd31a6"; // dev.trova.in panchu
        //String businessKey = "da1f2e97f7e11bd9eb074a891d28fa7e"; //data.trova.in srijith
        //String businessKey = "7dcd5608b2a0dee03d2af41c0177c7a0"; //dev.trova.in srijith
        //String businessKey = "f07e841e2fa2a77cb329e2cd66ac329f"; // data.trova.in hari
        //String businessKey = "af03d814e4fc7714bb8ef19d76a833db"; // dev.trova.in supercraft (supercraft)

        if(myGlobals == null)
            myGlobals = new MyGlobals(context);

        JSONObject jsonObject = new JSONObject();
        try{
            userId = userId.substring(1);
            userPhone = userPhone.substring(1);
            logInfo("userId ", userId);
            jsonObject.put("userId", userId);
            jsonObject.put("userPhone", userPhone);
            jsonObject.put("userName", userName);
            jsonObject.put("businessName", myGlobals.businessName);
            jsonObject.put("businessKey", myGlobals.businessKey);
            logInfo("Package Name ", context.getPackageName());

            myGlobals.trovaApi.trovaInit(context, jsonObject, context.getPackageName(), "TrovaMessageCallback", "messageCallback", false);
            //new TrovaInit(context, jsonObject, context.getPackageName(), "TrovaMessageCallback", "messageCallback", false);
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void initTrovaChat(Context context, String agentKey) {
        if (checkInternetConenction(context)){
            //once implementation done for getting the customer id remove the below line setCustomerKey()
            //String agentKey = "6230a03c1c6fdc3609d3e4ac9ee13f29"; //data.trova.in panchu
            //String agentKey = "8d8c2e229c7a55aab3a436efcb5b3079"; // dev.trova.in panchu
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

    public static void initAmazonS3(Context context, String cognitoPoolId, String bucketName) {
        myGlobals.trovaApi.amazonS3Init(context, cognitoPoolId, bucketName);
    }

    public static String Md5String(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            Crashlytics.logException(e);
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            Crashlytics.logException(e);
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }

        return hex.toString();
    }

    public static void logInfo(String tag, String info) {
        Log.i(tag, info);
    }

    public static void logInfo(String tag, int info) {
        Log.i(tag, String.valueOf(info));
    }

    public static void logInfo(String tag, double info) {
        Log.i(tag, String.valueOf(info));
    }

    public static void logInfo(String tag, boolean info) {
        Log.i(tag, String.valueOf(info));
    }

    public static void showSnackBar(View v, String message) {
        Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_LONG);
        ViewGroup view = (ViewGroup)snackbar.getView();
        view.setBackgroundColor(myGlobals.context.getResources().getColor(R.color.colorPrimary));
        snackbar.show();

/*
        Snackbar bar = Snackbar.make(v, "Weclome to SwA", Snackbar.LENGTH_LONG)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv.setText("You pressed Dismiss!!");
                    }
                });
*/

/*
        bar.setActionTextColor(Color.RED);

        TextView tv = (TextView) bar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.CYAN);
        bar.show();
*/

    }

    public static void downloadProfileImage(final String userEmail) {
        if((userEmail != null) && (userEmail != "") && (!userEmail.equals("null")) && (userEmail.length() > 0)) {
            new Thread() {
                public void run() {
                    String mBaseFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + myGlobals.context.getPackageName() + myGlobals.context.getString(in.trova.android.R.string.trova_base_folder) + "/Profile";
                    if (!new File(mBaseFolderPath).exists()) {
                        new File(mBaseFolderPath).mkdirs();
                    }
                    String fileName = userEmail + ".jpg";
                    String filePath = mBaseFolderPath + "/" + fileName;
                    String mediaLink = "profilepic/" + userEmail + ".jpg";

                    logInfo("filePath", filePath);
                    logInfo("mediaLink", mediaLink);

                    //if (!new File(filePath).exists()) {
                        logInfo("Downloading profile", filePath);
                        downloadResourceFromAmazonS3(myGlobals.context, filePath, mediaLink);
                    //}
                }
            }.start();
        }
    }

    public static void getProfileImage(String userEmail) {

        String mBaseFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + myGlobals.context.getPackageName() + myGlobals.context.getString(in.trova.android.R.string.trova_base_folder) + "/Profile";
        if (!new File(mBaseFolderPath).exists()) {
            new File(mBaseFolderPath).mkdirs();
        }
        String fileName = userEmail + ".jpg";
        String filePath = mBaseFolderPath + "/" + fileName;
        String mediaLink = "profilepic/" + userEmail + ".jpg";

        logInfo("filePath", filePath);
        logInfo("mediaLink", mediaLink);

        //if(!new File(filePath).exists()) {
            logInfo("Downloading profile", filePath);
            downloadResourceFromAmazonS3(myGlobals.context, filePath, mediaLink);
        //}

        return;
    }

    private static void downloadprofileImage(final Context context, final String filePath, final String mediaLink) {
        Bitmap bm = null;

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(success) {
                    myGlobals.bmLoggedinUser = loadProfileImage(myGlobals.userProfile.getUserEmail());
                }
            }
        };

        new Thread() {
            public void run() {
                success = downloadResourceFromAmazonS3(context, filePath, mediaLink);
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    public static Bitmap loadProfileImage(String userEmail) {
        Bitmap bm = null;
        String mBaseFolderPath, filePath, fileName;

        mBaseFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + myGlobals.context.getPackageName() + myGlobals.context.getString(in.trova.android.R.string.trova_base_folder) + "/Profile";
        if (!new File(mBaseFolderPath).exists()) {
            new File(mBaseFolderPath).mkdirs();
        }
        fileName = userEmail + ".jpg";
        filePath = mBaseFolderPath + "/" + fileName;

        if(new File(filePath).exists()) {
            bm = createThumbnailFromPath(filePath, 1);
        }

        return bm;
    }

    public static Bitmap createThumbnailFromPath(String filePath, int type) {
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(filePath), 300, 300, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
    }

}
