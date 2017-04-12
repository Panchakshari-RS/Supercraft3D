package com.trova.supercraft;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.trova.supercraft.Notification.MyChatNotificationsRowItem;
import com.trova.supercraft.Notification.MyNotificationsRowItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.trova.supercraft.SuperCraftUtils.logInfo;


/**
 * Created by Panchakshari on 28/2/2017.
 */

public class DBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SuperCraft";
    private static final int DATABASE_VERSION = 1;
    private static final String MESSAGELOGS = "messagelogs";
    private static final String USERPROFILE = "profile";
    private static final String HOSPITAL ="hospital";
    private static final String DEPARTMENT ="department";
    private static final String JOBS = "jobs";
    private static final String STLFILEVERSIONS = "stlFileVersions";
    private static final String NOTIFICATIONS = "notifications";

    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_PHONE = "phone";
    private static final String FIELD_PRODUCT_ID = "productid";
    private static final String FIELD_READMESSAGE = "messageread";
    private static final String FIELD_MESSAGE = "message";
    private static final String FIELD_DATE = "date";
    private static final String FIELD_TIME = "time";
    private static final String FIELD_MODE = "mode";
    private static final String FIELD_FILEEXT = "fileext";
    private static final String FIELD_FILENAME = "filename";
    private static final String FIELD_FILESIZE = "filesize";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_THUMBPATH = "thumbpath";
    private static final String FIELD_FILEPATH = "path";
    private static final String FIELD_DURATIONTIME = "durationtime";
    private static final String FIELD_SENTORRECIVEDSIZE = "sentorrecivedsize";
    private static final String FIELD_MESSAGESENTORRECEIVED="messagestatus";
    private static final String FIELD_SEENSTATUS="seenstatus";
    private static final String FIELD_MESSAGEID="sentorrecivedmessageid";
    private static final String FIELD_DELIVERYSTATUS="isprocessdone";
    private static final String FIELD_UNSEENMESSAGECOUNT="unseenmessagecount";
    private static final String FIELD_TIMEZONE = "timezone";
    private static final String FIELD_TIMEMILISECONDS="timemilliseconds";
    private static final String FIELD_UPLOADEDRESOURCE="uploadedresource";
    private static final String FIELD_MEDIALINK="medialink";
    private static final String FIELD_COUNT = "count(*)";

    // Registration interfaces
    private static final String FIELD_USER_NAME = "username";
    private static final String FIELD_USER_LNAME = "userlname";
    private static final String FIELD_USER_EMAIL = "useremail";
    private static final String FIELD_USER_PHONE = "userphone";
    private static final String FIELD_USER_TYPE = "usertype";
    private static final String FIELD_REG_NO = "userregno";
    private static final String FIELD_HOSPITAL_ID = "hospitalid";
    private static final String FIELD_HOSPITAL_NAME = "hospitalname";
    private static final String FIELD_HOSPITAL_ADDRESS = "hospitalAddress";
    private static final String FIELD_HOSPITAL_CITY = "hospitalCity";
    private static final String FIELD_HOSPITAL_PIN = "hospitalPin";
    private static final String FIELD_HOSPITAL_PHONE = "hospitalPhone";
    private static final String FIELD_HOSPITAL_LOGO = "hospitalLogo";
    private static final String FIELD_DEPARTMENT_ID = "departmentid";
    private static final String FIELD_DEPARTMENT_NAME = "departmentname";
    private static final String FIELD_USER_PASSWORD = "userkey";
    private static final String FIELD_USER_PHOTO = "profilePic";
    private static final String FIELD_USER_AGENTKEY = "agentkey";

    //Job Interfaces
    private static final String FIELD_JOB_ID = "jobId";
    private static final String FIELD_PATIENT_NAME = "patientName";
    private static final String FIELD_PATIENT_ID = "patientId";
    private static final String FIELD_DOCTOR_FNAME = "doctorFname";
    private static final String FIELD_DOCTOR_LNAME = "doctorLname";
    private static final String FIELD_DOCTOR_EMAIL = "doctorEmail";
    private static final String FIELD_DOCTOR_KEY = "doctorKey";
    private static final String FIELD_PRIORITY ="priority";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_CREATED_DATE = "createdDate";
    private static final String FIELD_ACCEPTED_DATE = "acceptedDate";
    private static final String FIELD_CREATED_BYID = "createdById";
    private static final String FIELD_ENGINEER_FNAME = "engineerFname";
    private static final String FIELD_ENGINEER_LNAME = "engineerLname";
    private static final String FIELD_ENGINEER_EMAIL = "engineerEmail";
    private static final String FIELD_ENGINEER_KEY = "engineerKey";
    private static final String FIELD_TECH_FNAME = "techFname";
    private static final String FIELD_TECH_LNAME = "techLname";
    private static final String FIELD_TECH_EMAIL = "techEmail";
    private static final String FIELD_TECH_KEY = "techKey";
    private static final String FIELD_STL_FILE_IDS = "stlFileIds";
    private static final String FIELD_STL_FILE_PATH = "stlFilePath";
    private static final String FIELD_STL_DOCTOR_FILE_NOTES = "stlDoctorFileNotes";
    private static final String FIELD_STL_ENGINEER_FILE_NOTES = "stlEngineerFileNotes";
    private static final String FIELD_MRI_FILE = "mriFile";
    private static final String FIELD_CT_FILE = "ctFile";
    private static final String FIELD_US_FILE = "usFile";
    private static final String FIELD_ECHO_FILE = "echoFile";
    private static final String FIELD_OTHER_FILE = "otherFile";
    private static final String FIELD_MRI_FILE_ID = "mriFileId";
    private static final String FIELD_CT_FILE_ID = "ctFileId";
    private static final String FIELD_US_FILE_ID = "usFileId";
    private static final String FIELD_ECHO_FILE_ID = "echoFileId";
    private static final String FIELD_OTHER_FILE_ID = "otherFileId";
    private static final String FIELD_DICOM_NOTES = "dicomNotes";

    // StlFileVersion Interfaces
    private static final String FIELD_STL_FILE = "stlFile";
    private static final String FIELD_STL_FILE_ID = "stlFileId";
    private static final String FIELD_STL_DOCTOR_NOTES = "stlDoctorNotes";
    private static final String FIELD_STL_ENGINEER_NOTES = "stlEngineerNotes";
    private static final String FIELD_STL_DATE = "stlDate";
    private static final String FIELD_STL_STATUS = "stlStatus";

    // Notifications Interfaces
    private static final String FIELD_NOTIFY_ID = "notifyId";
    private static final String FIELD_NOTIFY_ACTION = "notifyAction";
    private static final String FIELD_NOTIFY_JOBID = "notifyJobid";
    private static final String FIELD_NOTIFY_USERID = "notifyUserid";
    private static final String FIELD_NOTIFY_STATUS = "notifyStatus";
    private static final String FIELD_NOTIFY_DATE = "notifyDate";

    private static String CREATE_TABLE1;
    private static String CREATE_TABLE2;
    private static String CREATE_TABLE3;
    private static String CREATE_TABLE4;
    private static String CREATE_TABLE5;
    private static String CREATE_TABLE6;
    private static String CREATE_TABLE7;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.CREATE_TABLE1 = "CREATE TABLE " + MESSAGELOGS + "("
                + FIELD_ID + " INTEGER PRIMARY KEY," + FIELD_NAME + " TEXT,"+ FIELD_PHONE+" TEXT,"+ FIELD_PRODUCT_ID + " TEXT,"
                + FIELD_READMESSAGE + " INTEGER,"+ FIELD_MESSAGE + " TEXT,"+FIELD_DATE+" TEXT,"+ FIELD_TIME + " TEXT,"+ FIELD_MODE + " TEXT,"
                + FIELD_FILEEXT + " TEXT,"+ FIELD_FILENAME + " TEXT,"+ FIELD_FILESIZE + " INTEGER,"+ FIELD_TYPE + " TEXT,"+ FIELD_THUMBPATH + " TEXT,"+ FIELD_FILEPATH + " TEXT,"
                + FIELD_DURATIONTIME + " TEXT,"+ FIELD_SENTORRECIVEDSIZE +" INTEGER,"
                + FIELD_MESSAGESENTORRECEIVED + " INTEGER,"+ FIELD_SEENSTATUS + " INTEGER,"+ FIELD_MESSAGEID +" INTEGER,"
                + FIELD_DELIVERYSTATUS +" INTEGER,"+ FIELD_UNSEENMESSAGECOUNT +" INTEGER,"
                + FIELD_TIMEZONE +" TEXT," +FIELD_TIMEMILISECONDS+" INTEGER," +FIELD_UPLOADEDRESOURCE+" INTEGER," +FIELD_MEDIALINK+" TEXT" +")";
        this.CREATE_TABLE2 = "CREATE TABLE " + USERPROFILE + "("
                + FIELD_ID + " INTEGER PRIMARY KEY,"
                + FIELD_USER_NAME + " TEXT,"
                + FIELD_USER_LNAME + " TEXT,"
                + FIELD_USER_EMAIL + " TEXT,"
                + FIELD_USER_PHONE + " TEXT,"
                + FIELD_USER_TYPE + " TEXT,"
                + FIELD_REG_NO + " TEXT,"
                + FIELD_HOSPITAL_ID + " TEXT,"
                + FIELD_DEPARTMENT_ID + " TEXT,"
                + FIELD_USER_PASSWORD + " TEXT,"
                + FIELD_USER_AGENTKEY + " TEXT"
                +")";
        this.CREATE_TABLE3 = "CREATE TABLE " + HOSPITAL + "("
                + FIELD_ID + " INTEGER PRIMARY KEY,"
                + FIELD_HOSPITAL_ID + " TEXT,"
                + FIELD_HOSPITAL_NAME + " TEXT,"
                + FIELD_HOSPITAL_ADDRESS + " TEXT,"
                + FIELD_HOSPITAL_CITY + " TEXT,"
                + FIELD_HOSPITAL_PIN + " TEXT,"
                + FIELD_HOSPITAL_PHONE + " TEXT,"
                + FIELD_HOSPITAL_LOGO + " BLOB"
                +")";
        this.CREATE_TABLE4 = "CREATE TABLE " + DEPARTMENT + "("
                + FIELD_ID + " INTEGER PRIMARY KEY,"
                + FIELD_DEPARTMENT_ID + " TEXT,"
                + FIELD_DEPARTMENT_NAME + " TEXT"
                +")";
        this.CREATE_TABLE5 = "CREATE TABLE " + JOBS + "("
                + FIELD_ID + " INTEGER PRIMARY KEY,"
                + FIELD_JOB_ID + " INTEGER,"
                + FIELD_PATIENT_NAME + " TEXT,"
                + FIELD_PATIENT_ID + " TEXT,"
                + FIELD_HOSPITAL_ID + " TEXT,"
                + FIELD_DEPARTMENT_ID + " TEXT,"
                + FIELD_DOCTOR_FNAME + " TEXT,"
                + FIELD_DOCTOR_LNAME + " TEXT,"
                + FIELD_DOCTOR_EMAIL + " TEXT,"
                + FIELD_DOCTOR_KEY + " TEXT,"
                + FIELD_PRIORITY + " TEXT,"
                + FIELD_STATUS + " INTEGER,"
                + FIELD_CREATED_DATE + " TEXT,"
                + FIELD_ACCEPTED_DATE + " TEXT,"
                + FIELD_CREATED_BYID + " TEXT,"
                + FIELD_ENGINEER_FNAME + " TEXT,"
                + FIELD_ENGINEER_LNAME + " TEXT,"
                + FIELD_ENGINEER_EMAIL + " TEXT,"
                + FIELD_ENGINEER_KEY + " TEXT,"
                + FIELD_TECH_FNAME + " TEXT,"
                + FIELD_TECH_LNAME + " TEXT,"
                + FIELD_TECH_EMAIL + " TEXT,"
                + FIELD_TECH_KEY + " TEXT,"
                + FIELD_STL_FILE_IDS + " TEXT,"
                + FIELD_STL_FILE_PATH + " TEXT,"
                + FIELD_MRI_FILE + " TEXT,"
                + FIELD_CT_FILE + " TEXT,"
                + FIELD_US_FILE + " TEXT,"
                + FIELD_ECHO_FILE + " TEXT,"
                + FIELD_OTHER_FILE + " TEXT,"
                + FIELD_MRI_FILE_ID + " TEXT,"
                + FIELD_CT_FILE_ID + " TEXT,"
                + FIELD_US_FILE_ID + " TEXT,"
                + FIELD_ECHO_FILE_ID + " TEXT,"
                + FIELD_OTHER_FILE_ID + " TEXT,"
                + FIELD_DICOM_NOTES + " TEXT,"
                + FIELD_STL_DOCTOR_FILE_NOTES + " TEXT,"
                + FIELD_STL_ENGINEER_FILE_NOTES + " TEXT"
                +")";
        this.CREATE_TABLE6 = "CREATE TABLE " + STLFILEVERSIONS + "("
                + FIELD_ID + " INTEGER PRIMARY KEY,"
                + FIELD_JOB_ID + " TEXT,"
                + FIELD_STL_FILE + " TEXT,"
                + FIELD_STL_FILE_ID + " TEXT,"
                + FIELD_STL_DOCTOR_NOTES + " TEXT,"
                + FIELD_STL_ENGINEER_NOTES + " TEXT,"
                + FIELD_STL_DATE + " TEXT,"
                + FIELD_STL_STATUS + " TEXT"
                +")";
        this.CREATE_TABLE7 = "CREATE TABLE " + NOTIFICATIONS + "("
                + FIELD_ID + " INTEGER PRIMARY KEY,"
                + FIELD_NOTIFY_ID + " INTEGER,"
                + FIELD_NOTIFY_ACTION + " INTEGER,"
                + FIELD_NOTIFY_JOBID + " TEXT,"
                + FIELD_NOTIFY_USERID + " TEXT,"
                + FIELD_NOTIFY_STATUS + " INTEGER,"
                + FIELD_NOTIFY_DATE + " TEXT"
                +")";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE1);
        db.execSQL(CREATE_TABLE2);
        db.execSQL(CREATE_TABLE3);
        db.execSQL(CREATE_TABLE4);
        db.execSQL(CREATE_TABLE5);
        db.execSQL(CREATE_TABLE6);
        db.execSQL(CREATE_TABLE7);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables();
        onCreate(db);
    }

    void dropTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGELOGS);
        db.execSQL("DROP TABLE IF EXISTS " + USERPROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + HOSPITAL);
        db.execSQL("DROP TABLE IF EXISTS " + DEPARTMENT);
        db.execSQL("DROP TABLE IF EXISTS " + JOBS);
        db.execSQL("DROP TABLE IF EXISTS " + STLFILEVERSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + NOTIFICATIONS);
    }

    public void clearTableData(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        switch(tableName) {
            case MESSAGELOGS :
                db.execSQL(CREATE_TABLE1);
                break;
            case USERPROFILE :
                db.execSQL(CREATE_TABLE2);
                break;
            case HOSPITAL :
                db.execSQL(CREATE_TABLE3);
                break;
            case DEPARTMENT :
                db.execSQL(CREATE_TABLE4);
                break;
            case JOBS :
                db.execSQL(CREATE_TABLE5);
                break;
            case STLFILEVERSIONS :
                db.execSQL(CREATE_TABLE6);
                break;
            case NOTIFICATIONS :
                db.execSQL(CREATE_TABLE7);
                break;
        }

    }

    // Registration interfaces
    public boolean addUserProfile(UserProfile profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_USER_NAME, profile.getUserName());
        cv.put(FIELD_USER_LNAME, profile.getUserLName());
        cv.put(FIELD_USER_EMAIL, profile.getUserEmail());
        cv.put(FIELD_USER_PHONE, profile.getUserPhone());
        cv.put(FIELD_USER_TYPE, profile.getUserType());
        cv.put(FIELD_REG_NO, profile.getMedRegistrationNo());
        cv.put(FIELD_HOSPITAL_ID, profile.getHospitalId());
        cv.put(FIELD_DEPARTMENT_ID, profile.getDepartmentId());
        cv.put(FIELD_USER_PASSWORD, profile.getPassWord());
        cv.put(FIELD_USER_AGENTKEY, profile.getAgentKey());
        int userId = profile.getUserId();

        if(userId > 0) {
            if(db.update(USERPROFILE, cv, FIELD_ID + " = ?", new String[]{String.valueOf(userId)}) >= 0)
                return true;
        } else {
            if(db.insert(USERPROFILE, null, cv) >= 0)
                return true;
        }

        return false;
    }

    boolean updateUserPhone(String userId, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_USER_PHONE, phone);
        if(db.update(USERPROFILE, cv, FIELD_ID + " = ?", new String[]{userId}) >= 0)
            return true;

        return false;
    }

    boolean updateUserAgentKey(String userId, String agentKey) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_USER_AGENTKEY, agentKey);
        if(db.update(USERPROFILE, cv, FIELD_ID + " = ?", new String[]{userId}) >= 0)
            return true;

        return false;
    }

    public boolean updateUserPassword(String userId, String passWord) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_USER_PASSWORD, passWord);
        if(db.update(USERPROFILE, cv, FIELD_ID + " = ?", new String[]{userId}) >= 0)
            return true;

        return false;
    }

    boolean updateUserProfileImage(String userId, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_USER_PHOTO, image);
        if(db.update(USERPROFILE, cv, FIELD_ID + " = ?", new String[]{userId}) >= 0)
            return true;

        return false;
    }

    public UserProfile getUserProfile() {
        UserProfile profile = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + USERPROFILE;

        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                profile = new UserProfile();
                profile.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FIELD_ID))));
                profile.setUserName(cursor.getString(cursor.getColumnIndex(FIELD_USER_NAME)));
                profile.setUserLName(cursor.getString(cursor.getColumnIndex(FIELD_USER_LNAME)));
                profile.setUserEmail(cursor.getString(cursor.getColumnIndex(FIELD_USER_EMAIL)));
                profile.setUserPhone(cursor.getString(cursor.getColumnIndex(FIELD_USER_PHONE)));
                profile.setUserType(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FIELD_USER_TYPE))));
                profile.setMedRegistrationNo(cursor.getString(cursor.getColumnIndex(FIELD_REG_NO)));
                profile.setHospitalId(cursor.getString(cursor.getColumnIndex(FIELD_HOSPITAL_ID)));
                profile.setDepartmentId(cursor.getString(cursor.getColumnIndex(FIELD_DEPARTMENT_ID)));
                profile.setPassWord(cursor.getString(cursor.getColumnIndex(FIELD_USER_PASSWORD)));
                profile.setAgentKey(cursor.getString(cursor.getColumnIndex(FIELD_USER_AGENTKEY)));
            }
        }

        return profile;
    }

    public boolean addHospitalMaster(HospitalInfo hospitalInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String selectQuery = "SELECT * FROM " + HOSPITAL + " WHERE " + FIELD_HOSPITAL_ID + "='" + hospitalInfo.getHospitalId() + "'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        boolean update = false;

        cv.put(FIELD_HOSPITAL_ID, hospitalInfo.getHospitalId());
        cv.put(FIELD_HOSPITAL_NAME, hospitalInfo.getHospitalName());
        cv.put(FIELD_HOSPITAL_ADDRESS, hospitalInfo.getHospitalAddress());
        cv.put(FIELD_HOSPITAL_CITY, hospitalInfo.getHospitalCity());
        cv.put(FIELD_HOSPITAL_PIN, hospitalInfo.getHospitalPin());
        cv.put(FIELD_HOSPITAL_PHONE, hospitalInfo.getHospitalPhone());
        byte[] logo = hospitalInfo.getHospitalLogo();
        if((logo != null) && (!logo.equals("null")) && (!logo.equals("")) && (logo.length > 0))
            cv.put(FIELD_HOSPITAL_LOGO, hospitalInfo.getHospitalLogo());
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                update = true;
            }
        }

        logInfo("values", cv.toString() + " update " + update);
        long ret = -1;
        if(update)
            ret = db.update(HOSPITAL, cv, FIELD_HOSPITAL_ID + " = ?", new String[]{hospitalInfo.getHospitalId()});
        else
            ret = db.insert(HOSPITAL, null, cv);

        logInfo("addHospitalMaster Result", ret);
        if(ret >= 0)
            return true;

        return false;
    }

    public boolean updateHospitalMaster(String id, String hospitalId, String hospitalName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FIELD_HOSPITAL_ID, hospitalId);
        cv.put(FIELD_HOSPITAL_NAME, hospitalName);
        if(db.update(HOSPITAL, cv, FIELD_ID + " = ?", new String[]{String.valueOf(id)}) >= 0)
            return true;

        return false;
    }

    public List<HospitalInfo> getHospitalMaster() {
        List<HospitalInfo> hospitalInfoList = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + HOSPITAL;

        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                do {
                    HospitalInfo hospitalInfo = new HospitalInfo();
                    hospitalInfo.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FIELD_ID))));
                    hospitalInfo.setHospitalId(cursor.getString(cursor.getColumnIndex(FIELD_HOSPITAL_ID)));
                    hospitalInfo.setHospitalName(cursor.getString(cursor.getColumnIndex(FIELD_HOSPITAL_NAME)));
                    hospitalInfo.setHospitalAddress(cursor.getString(cursor.getColumnIndex(FIELD_HOSPITAL_ADDRESS)));
                    hospitalInfo.setHospitalCity(cursor.getString(cursor.getColumnIndex(FIELD_HOSPITAL_CITY)));
                    hospitalInfo.setHospitalPin(cursor.getString(cursor.getColumnIndex(FIELD_HOSPITAL_PIN)));
                    hospitalInfo.setHospitalPhone(cursor.getString(cursor.getColumnIndex(FIELD_HOSPITAL_PHONE)));
                    hospitalInfo.setHospitalLogo(cursor.getBlob(cursor.getColumnIndex(FIELD_HOSPITAL_LOGO)));
                    hospitalInfoList.add(hospitalInfo);
                } while(cursor.moveToNext());
            }
        }

        return hospitalInfoList;
    }

    public HospitalInfo getHospital(String id) {
        HospitalInfo hospitalInfo = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + HOSPITAL + " Where " + FIELD_HOSPITAL_ID + "='" + id + "'";

        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                hospitalInfo = new HospitalInfo();
                hospitalInfo.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FIELD_ID))));
                hospitalInfo.setHospitalId(cursor.getString(cursor.getColumnIndex(FIELD_HOSPITAL_ID)));
                hospitalInfo.setHospitalName(cursor.getString(cursor.getColumnIndex(FIELD_HOSPITAL_NAME)));
                hospitalInfo.setHospitalAddress(cursor.getString(cursor.getColumnIndex(FIELD_HOSPITAL_ADDRESS)));
                hospitalInfo.setHospitalCity(cursor.getString(cursor.getColumnIndex(FIELD_HOSPITAL_CITY)));
                hospitalInfo.setHospitalPin(cursor.getString(cursor.getColumnIndex(FIELD_HOSPITAL_PIN)));
                hospitalInfo.setHospitalPhone(cursor.getString(cursor.getColumnIndex(FIELD_HOSPITAL_PHONE)));
                hospitalInfo.setHospitalLogo(cursor.getBlob(cursor.getColumnIndex(FIELD_HOSPITAL_LOGO)));
            }
        }

        return hospitalInfo;
    }

    public boolean addDepartmentMaster(String departmentId, String departmentName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String selectQuery = "SELECT * FROM " + DEPARTMENT + " WHERE " + FIELD_DEPARTMENT_ID + "=" + departmentId;
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() <= 0) {
                cv.put(FIELD_DEPARTMENT_ID, departmentId);
                cv.put(FIELD_DEPARTMENT_NAME, departmentName);
                if (db.insert(DEPARTMENT, null, cv) >= 0)
                    return true;
            }
        }

        return false;
    }

    public boolean updateDepartmentMaster(String id, String departmentId, String departmentName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FIELD_DEPARTMENT_ID, departmentId);
        cv.put(FIELD_DEPARTMENT_NAME, departmentName);
        if(db.update(DEPARTMENT, cv, FIELD_ID + " = ?", new String[]{String.valueOf(id)}) >= 0)
            return true;

        return false;
    }

    public List<DepartmentInfo> getDepartentMaster() {
        List<DepartmentInfo> departmentInfoList = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + HOSPITAL;

        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                do {
                    DepartmentInfo departmentInfo = new DepartmentInfo();
                    departmentInfo.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FIELD_ID))));
                    departmentInfo.setDepartmentId(cursor.getString(cursor.getColumnIndex(FIELD_DEPARTMENT_ID)));
                    departmentInfo.setDepartmentName(cursor.getString(cursor.getColumnIndex(FIELD_DEPARTMENT_NAME)));
                    departmentInfoList.add(departmentInfo);
                } while(cursor.moveToNext());
            }
        }

        return departmentInfoList;
    }

    public boolean addNotificatons(String notifyId, int action, String jobId, String userId, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        boolean update = false;

        cv.put(FIELD_NOTIFY_ID, notifyId);
        cv.put(FIELD_NOTIFY_ACTION, action);
        cv.put(FIELD_NOTIFY_JOBID, jobId);
        cv.put(FIELD_NOTIFY_USERID, userId);
        cv.put(FIELD_NOTIFY_STATUS, 0);
        cv.put(FIELD_NOTIFY_DATE, date);

        String selectQuery = "SELECT * FROM " + NOTIFICATIONS + " WHERE " + FIELD_NOTIFY_ID + "=" + notifyId + " AND " + FIELD_NOTIFY_JOBID + "='" + jobId + "'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            logInfo("selectQuery", selectQuery);
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                logInfo("selectQuery", selectQuery);
                update = true;
            }
        }

        logInfo("values", cv.toString() + " update " + update);
        long ret = -1;
        if(update)
            ret = db.update(NOTIFICATIONS, cv, FIELD_NOTIFY_ID + " = ? AND " + FIELD_NOTIFY_JOBID + " = ?", new String[]{notifyId, jobId});
        else
            ret = db.insert(NOTIFICATIONS, null, cv);

        logInfo("addNotifications Result", ret);
        if(ret >= 0)
            return true;

        return false;
    }

    public List<MyNotificationsRowItem> getNotifications() {
        List<MyNotificationsRowItem> notificationList = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT *, " + FIELD_COUNT + " FROM " + NOTIFICATIONS + " WHERE " + FIELD_NOTIFY_STATUS + "=0 GROUP BY " + FIELD_NOTIFY_ACTION + "," + FIELD_NOTIFY_JOBID;
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            logInfo("selectQuery", selectQuery);
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                logInfo("selectQuery ", cursor.getCount());
                notificationList = new ArrayList<MyNotificationsRowItem>();
                do {
                    MyNotificationsRowItem notification = new MyNotificationsRowItem();
                    notification.setId(Integer.parseInt(cursor.getString(0)));
                    notification.setNotifyId(cursor.getString(1));
                    notification.setAction(Integer.parseInt(cursor.getString(2)));
                    notification.setJobId(cursor.getString(3));
                    notification.setUserId(cursor.getString(4));
                    notification.setStatus(Integer.parseInt(cursor.getString(5)));
                    notification.setDate(cursor.getString(6));
                    notification.setCount(Integer.parseInt(cursor.getString(7)));
                    notificationList.add(notification);
                } while (cursor.moveToNext());
            }
        }

        logInfo("Returning From ", "getNotifications");
        return notificationList;
    }

    public boolean updateNotification(String jobId, int action) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FIELD_NOTIFY_STATUS, 1);
        if(db.update(NOTIFICATIONS, cv, FIELD_NOTIFY_JOBID + " = ? AND " + FIELD_NOTIFY_ACTION + " = ?", new String[]{jobId, String.valueOf(action)}) >= 0)
            return true;

        logInfo("Notification Status Change", "FAILED ...................For " + jobId + " action " + action);
        return false;
    }

    public boolean updateNotifications() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FIELD_NOTIFY_STATUS, 1);
        if(db.update(NOTIFICATIONS, cv, null, null) >= 0)
            return true;

        return false;
    }

    public int getNotificationCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + FIELD_COUNT + " FROM " + NOTIFICATIONS + " WHERE " + FIELD_NOTIFY_STATUS + "=0 GROUP BY " + FIELD_NOTIFY_ACTION + "," + FIELD_NOTIFY_JOBID;
        int count = 0;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                count = cursor.getCount();
            }
        }

        return count;
    }

    public boolean addStlFileVersions(StlFileVersions stlFileVersions) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        boolean update = false;

        cv.put(FIELD_JOB_ID, stlFileVersions.getJobId());
        cv.put(FIELD_STL_FILE, stlFileVersions.getFileName());
        cv.put(FIELD_STL_FILE_ID, stlFileVersions.getFileId());
        cv.put(FIELD_STL_DOCTOR_NOTES, stlFileVersions.getDoctorNotes());
        cv.put(FIELD_STL_ENGINEER_NOTES, stlFileVersions.getEngineerNotes());
        cv.put(FIELD_STL_DATE, stlFileVersions.getDate());
        cv.put(FIELD_STL_STATUS, stlFileVersions.getStatus());

        String selectQuery = "SELECT * FROM " + STLFILEVERSIONS + " WHERE " + FIELD_JOB_ID + "=" + stlFileVersions.getJobId() + " AND " + FIELD_STL_FILE_ID + "=" + stlFileVersions.getFileId();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            logInfo("selectQuery", selectQuery);
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                logInfo("selectQuery", selectQuery);
                update = true;
            }
        }

        logInfo("values", cv.toString() + " update " + update);
        long ret = -1;
        if(update)
            ret = db.update(STLFILEVERSIONS, cv, FIELD_JOB_ID + " = ? AND " + FIELD_STL_FILE_ID + " = ?", new String[]{stlFileVersions.getJobId(), stlFileVersions.getFileId()});
        else
            ret = db.insert(STLFILEVERSIONS, null, cv);

        logInfo("addJobs Result", ret);
        if(ret >= 0)
            return true;

        return false;
    }

    public List<StlFileVersions> getStlFileVersions(String jobId) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<StlFileVersions> stlFileVersionsList = null;
        String selectQuery = "SELECT * FROM " + STLFILEVERSIONS + " WHERE " + FIELD_JOB_ID + "=" + jobId;
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            logInfo("selectQuery", selectQuery);
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                logInfo("selectQuery", selectQuery);
                stlFileVersionsList = new ArrayList<StlFileVersions>();
                do {
                    StlFileVersions stlFileVersions = new StlFileVersions();
                    stlFileVersions.setId(cursor.getString(0));
                    stlFileVersions.setJobId(cursor.getString(1));
                    stlFileVersions.setFileName(cursor.getString(2));
                    stlFileVersions.setFileId(cursor.getString(3));
                    stlFileVersions.setDoctorNotes(cursor.getString(4));
                    stlFileVersions.setEngineerNotes(cursor.getString(5));
                    stlFileVersions.setDate(cursor.getString(6));
                    stlFileVersions.setStatus(cursor.getString(7));
                    stlFileVersionsList.add(stlFileVersions);
                } while (cursor.moveToNext());
            }
        }

        return stlFileVersionsList;
    }

    public boolean addJobs(MyJobs jobs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_JOB_ID, jobs.getJobId());
        cv.put(FIELD_PATIENT_NAME, jobs.getPatientName());
        cv.put(FIELD_PATIENT_ID, jobs.getPatientId());
        cv.put(FIELD_HOSPITAL_ID, jobs.getHospitalId());
        cv.put(FIELD_DEPARTMENT_ID, jobs.getDepartmentId());
        cv.put(FIELD_DOCTOR_FNAME, jobs.getDoctorFname());
        cv.put(FIELD_DOCTOR_LNAME, jobs.getDoctorLname());
        cv.put(FIELD_DOCTOR_EMAIL, jobs.getDoctorEmail());
        cv.put(FIELD_DOCTOR_KEY, jobs.getDoctorKey());
        cv.put(FIELD_PRIORITY, jobs.getPriority());
        cv.put(FIELD_STATUS, jobs.getStatus());
        cv.put(FIELD_CREATED_DATE, jobs.getCreatedDate());
        cv.put(FIELD_ACCEPTED_DATE, jobs.getAcceptedDate());
        cv.put(FIELD_CREATED_BYID, jobs.getCreatedById());
        cv.put(FIELD_ENGINEER_FNAME, jobs.getEngineerFname());
        cv.put(FIELD_ENGINEER_LNAME, jobs.getEngineerLname());
        cv.put(FIELD_ENGINEER_EMAIL, jobs.getEngineerEmail());
        cv.put(FIELD_ENGINEER_KEY, jobs.getEngineerKey());
        cv.put(FIELD_TECH_FNAME, jobs.getTechFname());
        cv.put(FIELD_TECH_LNAME, jobs.getTechLname());
        cv.put(FIELD_TECH_EMAIL, jobs.getTechEmail());
        cv.put(FIELD_TECH_KEY, jobs.getTechKey());
        cv.put(FIELD_STL_FILE_IDS, jobs.getStlFileIds());
        cv.put(FIELD_STL_FILE_PATH, jobs.getStlFilePath());
        cv.put(FIELD_MRI_FILE, jobs.getMriFile());
        cv.put(FIELD_CT_FILE, jobs.getCtFile());
        cv.put(FIELD_US_FILE, jobs.getUsFile());
        cv.put(FIELD_ECHO_FILE, jobs.getEchoFile());
        cv.put(FIELD_OTHER_FILE, jobs.getOtherFile());
        cv.put(FIELD_MRI_FILE_ID, jobs.getMriFileId());
        cv.put(FIELD_CT_FILE_ID, jobs.getCtFileId());
        cv.put(FIELD_US_FILE_ID, jobs.getUsFileId());
        cv.put(FIELD_ECHO_FILE_ID, jobs.getEchoFileId());
        cv.put(FIELD_OTHER_FILE_ID, jobs.getOtherFileId());
        cv.put(FIELD_DICOM_NOTES, jobs.getDicomNotes());
        cv.put(FIELD_STL_DOCTOR_FILE_NOTES, jobs.getStlDoctorFileNotes());
        cv.put(FIELD_STL_ENGINEER_FILE_NOTES, jobs.getStlEngineerFileNotes());

        boolean update = false;
        String selectQuery = "SELECT * FROM " + JOBS + " WHERE " + FIELD_JOB_ID + "=" + jobs.getJobId();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            logInfo("selectQuery", selectQuery);
            cursor.moveToFirst();
            logInfo("selectQuery", selectQuery);
            if (cursor.getCount() != 0) {
                update = true;
            }
        }

        long ret = -1;
        if(update)
            ret = db.update(JOBS, cv, FIELD_JOB_ID + " = ?", new String[]{jobs.getJobId()});
        else
            ret = db.insert(JOBS, null, cv);

        logInfo("addJobs Result", ret);
        if(ret >= 0)
            return true;

        return false;
    }

    public int getJobCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int jobId = 0;
        String selectQuery = "SELECT MAX(" + FIELD_JOB_ID + ") AS " + FIELD_JOB_ID + " FROM " + JOBS;// + " where id = (SELECT MAX(" + FIELD_JOB_ID + ") from " + JOBS + ")";
        logInfo("selectQuery", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            logInfo("selectQuery", selectQuery);
            cursor.moveToFirst();
            logInfo("selectQuery", selectQuery);
            if (cursor.getCount() != 0) {
                logInfo("selectQuery", selectQuery);
                String value = cursor.getString(cursor.getColumnIndex(FIELD_JOB_ID));
                if((value != null) && (value != ""))
                    jobId = Integer.parseInt(value);
                logInfo("selectQuery", selectQuery);
            }
            logInfo("selectQuery", selectQuery);
        }
        logInfo("jobId", jobId);

        return jobId;
    }

    private MyJobs fillMyJobs(Cursor cursor) {
        MyJobs jobs = new MyJobs();
        jobs.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FIELD_ID))));
        jobs.setJobId(cursor.getString(cursor.getColumnIndex(FIELD_JOB_ID)));
        jobs.setPatientName(cursor.getString(cursor.getColumnIndex(FIELD_PATIENT_NAME)));
        jobs.setPatientId(cursor.getString(cursor.getColumnIndex(FIELD_PATIENT_ID)));
        jobs.setHospitalId(cursor.getString(cursor.getColumnIndex(FIELD_HOSPITAL_ID)));
        jobs.setDepartmentId(cursor.getString(cursor.getColumnIndex(FIELD_DEPARTMENT_ID)));
        jobs.setDoctorFname(cursor.getString(cursor.getColumnIndex(FIELD_DOCTOR_FNAME)));
        jobs.setDoctorLname(cursor.getString(cursor.getColumnIndex(FIELD_DOCTOR_LNAME)));
        jobs.setDoctorEmail(cursor.getString(cursor.getColumnIndex(FIELD_DOCTOR_EMAIL)));
        jobs.setDoctorKey(cursor.getString(cursor.getColumnIndex(FIELD_DOCTOR_KEY)));
        jobs.setPriority(cursor.getString(cursor.getColumnIndex(FIELD_PRIORITY)));
        jobs.setStatus(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FIELD_STATUS))));
        jobs.setCreatedDate(cursor.getString(cursor.getColumnIndex(FIELD_CREATED_DATE)));
        jobs.setAcceptedDate(cursor.getString(cursor.getColumnIndex(FIELD_ACCEPTED_DATE)));
        jobs.setCreatedById(cursor.getString(cursor.getColumnIndex(FIELD_CREATED_BYID)));
        jobs.setEngineerFname(cursor.getString(cursor.getColumnIndex(FIELD_ENGINEER_FNAME)));
        jobs.setEngineerLname(cursor.getString(cursor.getColumnIndex(FIELD_ENGINEER_LNAME)));
        jobs.setEngineerEmail(cursor.getString(cursor.getColumnIndex(FIELD_ENGINEER_EMAIL)));
        jobs.setEngineerKey(cursor.getString(cursor.getColumnIndex(FIELD_ENGINEER_KEY)));
        jobs.setTechFname(cursor.getString(cursor.getColumnIndex(FIELD_TECH_FNAME)));
        jobs.setTechLname(cursor.getString(cursor.getColumnIndex(FIELD_TECH_LNAME)));
        jobs.setTechEmail(cursor.getString(cursor.getColumnIndex(FIELD_TECH_EMAIL)));
        jobs.setTechKey(cursor.getString(cursor.getColumnIndex(FIELD_TECH_KEY)));
        jobs.setStlFileIds(cursor.getString(cursor.getColumnIndex(FIELD_STL_FILE_IDS)));
        jobs.setStlFilePath(cursor.getString(cursor.getColumnIndex(FIELD_STL_FILE_PATH)));
        jobs.setMriFile(cursor.getString(cursor.getColumnIndex(FIELD_MRI_FILE)));
        jobs.setCtFile(cursor.getString(cursor.getColumnIndex(FIELD_CT_FILE)));
        jobs.setUsFile(cursor.getString(cursor.getColumnIndex(FIELD_US_FILE)));
        jobs.setEchoFile(cursor.getString(cursor.getColumnIndex(FIELD_ECHO_FILE)));
        jobs.setOtherFile(cursor.getString(cursor.getColumnIndex(FIELD_OTHER_FILE)));
        jobs.setMriFileId(cursor.getString(cursor.getColumnIndex(FIELD_MRI_FILE_ID)));
        jobs.setCtFileId(cursor.getString(cursor.getColumnIndex(FIELD_CT_FILE_ID)));
        jobs.setUsFileId(cursor.getString(cursor.getColumnIndex(FIELD_US_FILE_ID)));
        jobs.setEchoFileId(cursor.getString(cursor.getColumnIndex(FIELD_ECHO_FILE_ID)));
        jobs.setOtherFileId(cursor.getString(cursor.getColumnIndex(FIELD_OTHER_FILE_ID)));
        jobs.setDicomNotes(cursor.getString(cursor.getColumnIndex(FIELD_DICOM_NOTES)));
        jobs.setStlDoctorFileNotes(cursor.getString(cursor.getColumnIndex(FIELD_STL_DOCTOR_FILE_NOTES)));
        jobs.setStlEngineerFileNotes(cursor.getString(cursor.getColumnIndex(FIELD_STL_ENGINEER_FILE_NOTES)));

        return jobs;
    }

    public List<MyJobs> getMyJobsByStatus(int status) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<MyJobs> jobsList = null;

        String selectQuery = "SELECT * FROM " + JOBS + " WHERE " + FIELD_STATUS + "=" + status;
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                jobsList = new ArrayList<MyJobs>();
                do {
                    MyJobs jobs = fillMyJobs(cursor);
                    jobsList.add(jobs);
                } while (cursor.moveToNext());
            }
        }

        return jobsList;
    }

    public List<MyJobs> getMyJobsByStatus(int status, String order) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<MyJobs> jobsList = null;

        String selectQuery = "SELECT * FROM " + JOBS + " WHERE " + FIELD_STATUS + "=" + status + " ORDER BY " + FIELD_JOB_ID + " " + order;
        logInfo("selectQuery", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                jobsList = new ArrayList<MyJobs>();
                do {
                    MyJobs jobs = fillMyJobs(cursor);
                    jobsList.add(jobs);
                } while (cursor.moveToNext());
            }
        }

        return jobsList;
    }

    public List<MyJobs> getMyJobsByStatusOrderTime(int status, String order) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<MyJobs> jobsList = null;

        String selectQuery = "SELECT * FROM " + JOBS + " WHERE " + FIELD_STATUS + "=" + status + " ORDER BY " + FIELD_CREATED_DATE + " " + order;
        logInfo("selectQuery", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                jobsList = new ArrayList<MyJobs>();
                do {
                    MyJobs jobs = fillMyJobs(cursor);
                    jobsList.add(jobs);
                } while (cursor.moveToNext());
            }
        }

        return jobsList;
    }

    public List<MyJobs> getMyJobsByPriority(String priority) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<MyJobs> jobsList = null;

        String selectQuery = "SELECT * FROM " + JOBS + " WHERE " + FIELD_PRIORITY + "='" + priority + "'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                jobsList = new ArrayList<MyJobs>();
                do {
                    MyJobs jobs = fillMyJobs(cursor);
                    jobsList.add(jobs);
                } while (cursor.moveToNext());
            }
        }

        return jobsList;
    }

    public List<MyJobs> getMyJobs() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<MyJobs> jobsList = null;

        String selectQuery = "SELECT * FROM " + JOBS;
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                jobsList = new ArrayList<MyJobs>();
                do {
                    MyJobs jobs = fillMyJobs(cursor);
                    jobsList.add(jobs);
                } while (cursor.moveToNext());
            }
        }

        return jobsList;
    }

    public MyJobs getMyJob(String jobId) {
        SQLiteDatabase db = this.getReadableDatabase();
        MyJobs myJobs = null;

        String selectQuery = "SELECT * FROM " + JOBS + " where " + FIELD_JOB_ID + " = '" + jobId + "'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                myJobs = fillMyJobs(cursor);
            }
        }

        return myJobs;
    }

    public void deleteJob(String jobId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(JOBS, FIELD_JOB_ID + " = ?", new String[]{jobId});

        return;
    }

    public void updateJobStatus(String jobId, int jobStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_JOB_ID, jobId);
        cv.put(FIELD_STATUS, jobStatus);
        db.update(JOBS, cv, FIELD_JOB_ID + " = ?", new String[]{jobId});

        return;
    }

    public void updateJobDoctorNotes(String jobId, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_JOB_ID, jobId);
        cv.put(FIELD_STL_DOCTOR_FILE_NOTES, notes);
        db.update(JOBS, cv, FIELD_JOB_ID + " = ?", new String[]{jobId});

        return;
    }

    public void updateJobEngineerNotes(String jobId, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_JOB_ID, jobId);
        cv.put(FIELD_STL_ENGINEER_FILE_NOTES, notes);
        db.update(JOBS, cv, FIELD_JOB_ID + " = ?", new String[]{jobId});

        return;
    }

    public void updateJobPriority(String jobId, String priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_PRIORITY, priority);
        if(db.update(JOBS, cv, FIELD_JOB_ID + " = ?", new String[]{jobId}) > 0)
            logInfo("Job Updated", "Successfully");
        else
            logInfo("Job Updated", "FAILED .......");

        return;
    }

    // Chat Interfaces
    public boolean addMessageLogs(ChatMessages messageLogs) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + MESSAGELOGS+" WHERE "+FIELD_PHONE+"='"+messageLogs.getPhone_number()+"' AND "+FIELD_MESSAGEID+"="+messageLogs.getMessageId() + " AND " + FIELD_PRODUCT_ID + "='" + messageLogs.getProduct_id() + "'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                ContentValues cv = new ContentValues();
                cv.put(FIELD_NAME, messageLogs.getName());
                cv.put(FIELD_PHONE, messageLogs.getPhone_number());
                cv.put(FIELD_PRODUCT_ID, messageLogs.getProduct_id());
                cv.put(FIELD_MESSAGE, messageLogs.getMessage());
                cv.put(FIELD_DATE, messageLogs.getDate());
                cv.put(FIELD_TIME, messageLogs.getTime());
                cv.put(FIELD_MODE, messageLogs.getMode());
                cv.put(FIELD_FILEEXT, messageLogs.getFileExt());
                cv.put(FIELD_FILENAME, messageLogs.getFileName());
                cv.put(FIELD_FILESIZE, messageLogs.getFileSize());
                cv.put(FIELD_TYPE, messageLogs.getMimeType());
                cv.put(FIELD_THUMBPATH, messageLogs.getThumbPath());
                cv.put(FIELD_FILEPATH, messageLogs.getFilePath());
                cv.put(FIELD_DURATIONTIME, messageLogs.getDurationTime());
                cv.put(FIELD_SENTORRECIVEDSIZE, messageLogs.getSentorrecivedsize());
                cv.put(FIELD_MESSAGESENTORRECEIVED, messageLogs.getMessageSentOrReceived());
                cv.put(FIELD_SEENSTATUS, messageLogs.getSeenstatus());
                cv.put(FIELD_READMESSAGE, messageLogs.getReadMessage());
                cv.put(FIELD_MESSAGEID, messageLogs.getMessageId());
                cv.put(FIELD_DELIVERYSTATUS, messageLogs.getMessageDeliveryStatus());
                cv.put(FIELD_UNSEENMESSAGECOUNT, messageLogs.getUnseenmessageCount());
                cv.put(FIELD_TIMEZONE, messageLogs.getTimeZone());
                cv.put(FIELD_TIMEMILISECONDS, messageLogs.getTimemilliseconds());
                cv.put(FIELD_UPLOADEDRESOURCE, messageLogs.getUploadedResource());
                cv.put(FIELD_MEDIALINK, messageLogs.getMediaLink());

                db.insert(MESSAGELOGS, null, cv);
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean updateMessageLogs(long messageId, String productId, String filePath, String thumbFilePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + MESSAGELOGS + " WHERE " + FIELD_MESSAGEID + "=" + messageId + " AND " + FIELD_PRODUCT_ID + "='" + productId + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                String id = cursor.getString(cursor.getColumnIndex(FIELD_ID));
                ContentValues cv = new ContentValues();
                cv.put(FIELD_THUMBPATH, thumbFilePath);
                cv.put(FIELD_FILEPATH, filePath);

                db.update(MESSAGELOGS, cv, FIELD_ID + " = ?", new String[]{id});
            } else {
                return false;
            }
        }
        return true;
    }

    public ChatMessages getMessage(long messageId, String productId) {
        ChatMessages messageLog = null;

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + MESSAGELOGS + " WHERE " + FIELD_MESSAGEID + "='" + messageId + "'" + " AND " + FIELD_PRODUCT_ID + "='" + productId + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                messageLog = fillMessageLogs(cursor);
            }
        }
        return messageLog;
    }

    public int getUnseenMessagesCount(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + FIELD_COUNT + " FROM " + MESSAGELOGS + " WHERE " + FIELD_PHONE + "='" + phone + "' AND " + FIELD_READMESSAGE + "=0";
        int count = 0;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                count = Integer.parseInt(cursor.getString(0));
                if(count > 0) {
                    updateUnseenMessages(phone, null);
                }
            }
        }
        return count;
    }

    public int getUnReadMessagesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + FIELD_COUNT + " FROM " + MESSAGELOGS + " WHERE " + FIELD_READMESSAGE + "=0";
        int count = 0;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                count = Integer.parseInt(cursor.getString(0));
            }
        }

        selectQuery = "SELECT " + FIELD_COUNT + " FROM " + MESSAGELOGS;
        int countAll = 0;
        Cursor cursor1 = db.rawQuery(selectQuery, null);
        if (cursor1 != null) {
            cursor1.moveToFirst();
            if (cursor1.getCount() != 0) {
                countAll = Integer.parseInt(cursor1.getString(0));
            }
        }
        logInfo("countAll", countAll);


        return count;
    }

    public int getUnseenMessagesCountByProductId(String phone, String productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + FIELD_COUNT + " FROM " + MESSAGELOGS + " WHERE " + FIELD_PHONE + "='" + phone + "' AND " + FIELD_READMESSAGE + "=0" + " AND " + FIELD_PRODUCT_ID + "='" + productId + "'";
        logInfo("selectQuery", selectQuery);
        int count = 0;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                count = Integer.parseInt(cursor.getString(0));
                if(count > 0) {
                    updateUnseenMessages(phone, productId);
                }
            }
        }
        return count;
    }

    public void updateUnseenMessages(String phone, String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_SEENSTATUS, 1);
        cv.put(FIELD_READMESSAGE, 1);
        if(productId != null)
            db.update(MESSAGELOGS, cv, FIELD_PHONE + " = ? AND " + FIELD_PRODUCT_ID + " = ?",
                new String[]{phone, productId});
        else
            db.update(MESSAGELOGS, cv, FIELD_PHONE + " = ?",
                new String[]{phone});
    }

    public List<ChatMessages> getMessageLogsByPhone(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM "+MESSAGELOGS+" WHERE "+FIELD_PHONE+"='"+phone+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        List<ChatMessages> messageLogsList = null;

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                messageLogsList = new ArrayList<ChatMessages>();
                do {
                    ChatMessages messageLogs = fillMessageLogs(cursor);
                    messageLogsList.add(messageLogs);
                } while (cursor.moveToNext());
            }
        }

        return messageLogsList;
    }

    public List<MyChatNotificationsRowItem> getUnReadMessages() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT "+ FIELD_ID + "," + FIELD_PRODUCT_ID + "," + FIELD_MESSAGE + "," + FIELD_DATE + "," + FIELD_TIME + ", " + FIELD_COUNT + " FROM " + MESSAGELOGS + " WHERE " + FIELD_READMESSAGE + "=0 GROUP BY " + FIELD_PRODUCT_ID;
        logInfo("selectQuery", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery,null);
        List<MyChatNotificationsRowItem> notificationsList = null;
        int id, unSeenCount;
        String productId, message, date, time;

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                notificationsList = new ArrayList<MyChatNotificationsRowItem>();
                do {
                    id = Integer.parseInt(cursor.getString(0));
                    productId = cursor.getString(1);
                    message = cursor.getString(2);
                    date = cursor.getString(3);
                    time = cursor.getString(4);
                    unSeenCount = Integer.parseInt(cursor.getString(5));

                    if((productId != null) && (!productId.equals("")) && (productId.length() > 0)) {
                        MyChatNotificationsRowItem notification = new MyChatNotificationsRowItem(id, productId, message, date, time, unSeenCount, null);

                        notificationsList.add(notification);
                    }
                } while (cursor.moveToNext());
            }
        }

        return notificationsList;
    }

    public List<ChatMessages> getMessageLogsByPhoneNProductId(String phone, String productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM "+MESSAGELOGS+" WHERE "+FIELD_PHONE+"='"+phone+"' AND " + FIELD_PRODUCT_ID + "='"+productId+"'";
        logInfo("selectQuery", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery,null);
        List<ChatMessages> messageLogsList = null;

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                messageLogsList = new ArrayList<ChatMessages>();
                do {
                    ChatMessages messageLogs = fillMessageLogs(cursor);
                    messageLogsList.add(messageLogs);
                } while (cursor.moveToNext());
            }
        }

        return messageLogsList;
    }

    public List<ChatMessages> getUndeliveredMessageLogs() {
        SQLiteDatabase db = this.getReadableDatabase();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, -7);
        long timemilliseconds=cal.getTimeInMillis();
        List<ChatMessages> messageLogsList = null;

        String selectQuery = "SELECT * FROM "+MESSAGELOGS+" WHERE "+FIELD_TIMEMILISECONDS+">="+timemilliseconds+" AND "+FIELD_DELIVERYSTATUS+"='false' AND "+FIELD_MESSAGESENTORRECEIVED+"=1";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                messageLogsList = new ArrayList<ChatMessages>();
                do {
                    ChatMessages messageLogs = fillMessageLogs(cursor);
                    messageLogsList.add(messageLogs);
                } while (cursor.moveToNext());
            }
        }

        return messageLogsList;
    }

    public int getUndeliveredMessagesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, -7);
        long timemilliseconds=cal.getTimeInMillis();
        int count=0;
        String selectQuery = "SELECT " + FIELD_COUNT + " FROM "+MESSAGELOGS+" WHERE "+FIELD_TIMEMILISECONDS+">="+timemilliseconds+" AND "+FIELD_DELIVERYSTATUS+"='false' AND "+FIELD_MESSAGESENTORRECEIVED+"=1";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            cursor.moveToFirst();
            count = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FIELD_COUNT)));
        }
        return count;
    }

    public void updateMessageId(long messageId, String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_DELIVERYSTATUS, "true");
        db.update(MESSAGELOGS, cv, FIELD_MESSAGEID + " = ? AND " + FIELD_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(messageId), productId});
    }

    public void updateUploadResource(long messageId, String productId, String fileExt, String fileName, String filePath, long fileSize, String mimeType, String duration, String mediaLink) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_UPLOADEDRESOURCE, 1);
        cv.put(FIELD_FILEEXT, fileExt);
        cv.put(FIELD_FILENAME, fileName);
        cv.put(FIELD_FILESIZE, fileSize);
        cv.put(FIELD_TYPE, mimeType);
        cv.put(FIELD_MEDIALINK, mediaLink);
        cv.put(FIELD_FILEPATH, filePath);
        cv.put(FIELD_DURATIONTIME, duration);
        db.update(MESSAGELOGS, cv, FIELD_MESSAGEID + " = ? AND " + FIELD_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(messageId), productId});
    }

    public void updateUnseenMessageStatus(String phone, String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_UNSEENMESSAGECOUNT, 0);
        db.update(MESSAGELOGS, cv, FIELD_PHONE + " = ? AND " + FIELD_PRODUCT_ID + " = ?",
                new String[]{phone, productId});
    }

    public void clearMessage(String phone, String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MESSAGELOGS, FIELD_PHONE + " = ? AND " + FIELD_PRODUCT_ID + " = ?", new String[]{phone, productId});
        ContentValues cv = new ContentValues();
        cv.put(FIELD_MESSAGE, "");
        cv.put(FIELD_PRODUCT_ID, "");
        cv.put(FIELD_DATE, "");
        cv.put(FIELD_TIME, "");
        cv.put(FIELD_MODE, "");
        cv.put(FIELD_FILEEXT, "");
        cv.put(FIELD_FILENAME, "");
        cv.put(FIELD_FILESIZE, 0);
        cv.put(FIELD_TYPE, "");
        cv.put(FIELD_THUMBPATH, "");
        cv.put(FIELD_FILEPATH, "");
        cv.put(FIELD_DURATIONTIME, "");
        cv.put(FIELD_SENTORRECIVEDSIZE, 0);
        cv.put(FIELD_MESSAGESENTORRECEIVED, 0);
        cv.put(FIELD_SEENSTATUS, 0);
        cv.put(FIELD_MESSAGEID, 0);
        cv.put(FIELD_DELIVERYSTATUS, "");
        cv.put(FIELD_UNSEENMESSAGECOUNT, 0);
        cv.put(FIELD_TIMEZONE, "");
        cv.put(FIELD_TIMEMILISECONDS, 0);
        cv.put(FIELD_UPLOADEDRESOURCE, 0);
        cv.put(FIELD_MEDIALINK, "");
        cv.put(FIELD_READMESSAGE, 0);
    }

    private ChatMessages fillMessageLogs(Cursor cursor) {
        ChatMessages messageLogs;
        int id, sentOrReceived, seenStatus, unSeenCount, fileSize, uploadedResource, readMessage;
        String name, phoneNumber, message, date, time, mode, fileExt, fileName, type, durationTime, deliveryStatus;
        String product_id, mediaLink, timeZone, thumbFilePath, filePath;
        long sentRecvSize, messageId, timeMilliSecs;

        id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FIELD_ID)));
        name = cursor.getString(cursor.getColumnIndex(FIELD_NAME));
        phoneNumber = cursor.getString(cursor.getColumnIndex(FIELD_PHONE));
        product_id = cursor.getString(cursor.getColumnIndex(FIELD_PRODUCT_ID));
        message = cursor.getString(cursor.getColumnIndex(FIELD_MESSAGE));
        date = cursor.getString(cursor.getColumnIndex(FIELD_DATE));
        time = cursor.getString(cursor.getColumnIndex(FIELD_TIME));
        mode = cursor.getString(cursor.getColumnIndex(FIELD_MODE));
        fileExt = cursor.getString(cursor.getColumnIndex(FIELD_FILEEXT));
        fileName = cursor.getString(cursor.getColumnIndex(FIELD_FILENAME));
        fileSize = cursor.getInt(cursor.getColumnIndex(FIELD_FILESIZE));
        type = cursor.getString(cursor.getColumnIndex(FIELD_TYPE));
        thumbFilePath = cursor.getString(cursor.getColumnIndex(FIELD_THUMBPATH));
        filePath = cursor.getString(cursor.getColumnIndex(FIELD_FILEPATH));
        durationTime = cursor.getString(cursor.getColumnIndex(FIELD_DURATIONTIME));
        sentRecvSize = Long.parseLong(cursor.getString(cursor.getColumnIndex(FIELD_SENTORRECIVEDSIZE)));
        sentOrReceived = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FIELD_MESSAGESENTORRECEIVED)));
        seenStatus = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FIELD_SEENSTATUS)));
        messageId = Long.parseLong(cursor.getString(cursor.getColumnIndex(FIELD_MESSAGEID)));
        deliveryStatus = cursor.getString(cursor.getColumnIndex(FIELD_DELIVERYSTATUS));
        unSeenCount = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FIELD_UNSEENMESSAGECOUNT)));
        timeZone = cursor.getString(cursor.getColumnIndex(FIELD_TIMEZONE));
        timeMilliSecs = cursor.getLong(cursor.getColumnIndex(FIELD_TIMEMILISECONDS));
        uploadedResource = cursor.getInt(cursor.getColumnIndex(FIELD_UPLOADEDRESOURCE));
        mediaLink = cursor.getString(cursor.getColumnIndex(FIELD_MEDIALINK));
        readMessage = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FIELD_READMESSAGE)));

        messageLogs = new ChatMessages(id,name,phoneNumber, product_id,message,date,time,mode,fileExt,fileName, fileSize,type,
                thumbFilePath, filePath, durationTime,sentRecvSize,sentOrReceived,seenStatus,messageId,deliveryStatus,
                unSeenCount,timeZone,timeMilliSecs,uploadedResource, mediaLink, readMessage);

        return messageLogs;
    }

}
