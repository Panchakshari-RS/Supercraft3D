package com.trova.supercraft.Notification;

import android.graphics.Bitmap;

/**
 * Created by Panchakshari on 28/3/2017.
 */

public class MyChatNotificationsRowItem {
    String jobId;
    String message;
    String date;
    String time;
    int messageCount;
    Bitmap profileImage;
    int id;

    public MyChatNotificationsRowItem() {
    }

    public MyChatNotificationsRowItem(int id, String jobId, String message, String date, String time, int messageCount, Bitmap profileImage) {
        this.id = id;
        this.jobId = jobId;
        this.message = message;
        this.date = date;
        this.time = time;
        this.messageCount = messageCount;
        this.profileImage = profileImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "MyChatNotificationsRowItem{" +
                "jobId='" + jobId + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", messageCount=" + messageCount +
                ", profileImage=" + profileImage +
                ", id=" + id +
                '}';
    }
}
