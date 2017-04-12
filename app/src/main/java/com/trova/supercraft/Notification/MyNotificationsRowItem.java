package com.trova.supercraft.Notification;

import android.graphics.Bitmap;

/**
 * Created by Panchakshari on 28/3/2017.
 */

public class MyNotificationsRowItem {
    String notifyId;
    int action;
    String jobId;
    String userId;
    String date;
    Bitmap profileImage;
    int status;
    int count;
    int id;

    public MyNotificationsRowItem() {
    }

    public MyNotificationsRowItem(String notifyId, int action, String jobId, String userId, String date, int status, int id, int count, Bitmap profileImage) {
        this.notifyId = notifyId;
        this.action = action;
        this.jobId = jobId;
        this.userId = userId;
        this.date = date;
        this.status = status;
        this.id = id;
        this.count = count;
        this.profileImage = profileImage;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "MyNotificationsRowItem{" +
                "notifyId='" + notifyId + '\'' +
                ", action=" + action +
                ", jobId='" + jobId + '\'' +
                ", userId='" + userId + '\'' +
                ", date='" + date + '\'' +
                ", profileImage=" + profileImage +
                ", status=" + status +
                ", count=" + count +
                ", id=" + id +
                '}';
    }
}
