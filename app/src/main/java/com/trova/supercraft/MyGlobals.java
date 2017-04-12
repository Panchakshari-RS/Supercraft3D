package com.trova.supercraft;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.widget.PopupWindow;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.crashlytics.android.Crashlytics;
import com.digits.sdk.android.Digits;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.List;

import in.trova.android.Trova;
import io.fabric.sdk.android.Fabric;

import static com.trova.supercraft.AmazonS3.AmazonS3Utils.initSupercraftAmazonS3;

/**
 * Created by Panchakshari on 28/2/2017.
 */

public class MyGlobals {
    public DBHandler dbHandler;
    public String userId, userPhone, userName, countryList[], userNumber, serverRefCode, agentKey;
    public Context context;
    public boolean isFromMobileWidget, activateUser = false;
    public UserProfile userProfile = null;
    public Trova trovaApi;
    private static final String TWITTER_KEY = "Zrm75QgqP3jPEJuO9YOJIdQiY";
    private static final String TWITTER_SECRET = "m98hkQyrSnENK4DBWMy7b4qLfnBEYfCPmnSCoJfALGT8K974y4";
    public boolean initSuccess = false;
    public Typeface materialIconFont = null, materialRegularIconFont = null, centuryGothic = null, centuryGothicBold = null;
    public Typeface fontAwesomeFont = null, openSansLight = null, openSansRegular = null, openSansBold = null;
    public String jobId, patientId, patientName, hospitalName, cityName;
    public String prevJobId, prevPatientId, prevPatientName;
    public boolean isCompletedJob = false, getChatMessages = false, isChatEnabled = false;
    public MyJobs currJobActive;
    public MyJobs prevJobActive;
    public int loggedinUserType = 1, currentChatUserType = 3;
    public String businessName = "Supercraft3D";
    public String businessKey = "af03d814e4fc7714bb8ef19d76a833db";
    public boolean checked = false, activityInForeGround = false;
    public int checkedTabId = -1, chatCount, notifyCount;
    public Context myJobsActivity = null;
    public Context myJobDetailsActivity = null;
    public Context myMessageActivity = null;
    public Context myChatNotificationActivity = null;
    public Context myNotificationActivity = null;
    public Context myProfileActivity = null;
    public Context myRawActivity = null;
    public Context myEditProfileActivity = null;
    public Context myChangePasswordActivity = null;
    public List<StlFileVersions> stlFileVersionsList;
    public TransferUtility transferUtility;
    public Bitmap bmLoggedinUser, bmDoctor, bmEngineer, bmTechnician;
    public String serverIp = null;
    public PopupWindow showPopup;
    public String amazonCognitoId = null, amazonAWSBucket = null;

    // Temp Remove Later
    public int pageId = 0;

    public MyGlobals(Context context) {
        this.context = context;

        this.serverIp = "http://54.169.41.27/";   // Live Ip
        //this.serverIp = "http://54.169.230.139/";   // Testing/Staging Ip
        this.amazonCognitoId = "us-east-1:38af91c2-34ba-4cae-a9f7-3cfbeeb1169e";
        this.amazonAWSBucket = "supercraft-media";  // Live bucket
        //this.amazonAWSBucket = "supercraft-staging";  // Testing/Staging bucket

        this.dbHandler = new DBHandler(context);
        this.countryList = context.getResources().getStringArray(R.array.country_codes);
        this.userProfile = this.dbHandler.getUserProfile();
        this.trovaApi = new Trova(context);

        this.myJobsActivity = null;
        this.myJobDetailsActivity = null;
        this.myMessageActivity = null;
        this.myChatNotificationActivity = null;
        this.myNotificationActivity = null;
        this.myProfileActivity = null;
        this.myRawActivity = null;

        if(this.userProfile != null)
            this.loggedinUserType = this.userProfile.getUserType();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new TwitterCore(authConfig), new Digits.Builder().build(), new Crashlytics());

        this.transferUtility = initSupercraftAmazonS3(context, this.amazonCognitoId, this.amazonAWSBucket);

        this.materialIconFont = FontManager.getTypeface(context, FontManager.MATERIALIZE);
        this.materialRegularIconFont = FontManager.getTypeface(context, FontManager.MATERIALIZEREGULAR);
        this.fontAwesomeFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);
        this.openSansLight = FontManager.getTypeface(context, FontManager.OPENSANSLIGHT);
        this.openSansRegular = FontManager.getTypeface(context, FontManager.OPENSANSREGULAR);
        this.openSansBold = FontManager.getTypeface(context, FontManager.OPENSANSBOLD);
        this.centuryGothic = FontManager.getTypeface(context, FontManager.CENTURYGOTHIC);
        this.centuryGothicBold = FontManager.getTypeface(context, FontManager.CENTURYGOTHIC);
    }

}
