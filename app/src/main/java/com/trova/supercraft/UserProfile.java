package com.trova.supercraft;

import java.util.Arrays;

/**
 * Created by Panchakshari on 8/3/2017.
 */

public class UserProfile {
    protected int userId;
    protected String userName;
    protected String userLName;
    protected String userEmail;
    protected String userPhone;
    protected int userType;
    protected String medRegistrationNo;
    protected String hospitalId;
    protected String departmentId;
    protected String passWord;
    protected String agentKey;
    protected byte[] photo;

    public UserProfile() {
    }

    public UserProfile(int userId, String userName, String userLName, String userEmail, String userPhone, int userType, String medRegistrationNo, String hospitalId, String departmentId, String passWord, String agentKey, byte[] photo) {
        this.userId = userId;
        this.userName = userName;
        this.userLName = userLName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userType = userType;
        this.medRegistrationNo = medRegistrationNo;
        this.hospitalId = hospitalId;
        this.departmentId = departmentId;
        this.passWord = passWord;
        this.agentKey = agentKey;
        this.photo = photo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLName() {
        return userLName;
    }

    public void setUserLName(String userLName) {
        this.userLName = userLName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getMedRegistrationNo() {
        return medRegistrationNo;
    }

    public void setMedRegistrationNo(String medRegistrationNo) {
        this.medRegistrationNo = medRegistrationNo;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getAgentKey() {
        return agentKey;
    }

    public void setAgentKey(String agentKey) {
        this.agentKey = agentKey;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userLName='" + userLName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userType=" + userType +
                ", medRegistrationNo='" + medRegistrationNo + '\'' +
                ", hospitalId='" + hospitalId + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", passWord='" + passWord + '\'' +
                ", agentKey='" + agentKey + '\'' +
                ", photo=" + Arrays.toString(photo) +
                '}';
    }
}
