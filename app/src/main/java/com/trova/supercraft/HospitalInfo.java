package com.trova.supercraft;

import java.util.Arrays;

/**
 * Created by Panchakshari on 8/3/2017.
 */

public class HospitalInfo {
    protected int id;
    protected String hospitalId;
    protected String hospitalName;
    protected String hospitalAddress;
    protected String hospitalCity;
    protected String hospitalPin;
    protected String hospitalPhone;
    protected byte[] hospitalLogo;

    public HospitalInfo() {
    }

    public HospitalInfo(int id, String hospitalId, String hospitalName, String hospitalAddress, String hospitalCity, String hospitalPin, String hospitalPhone, byte[] hospitalLogo) {
        this.id = id;
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.hospitalCity = hospitalCity;
        this.hospitalPin = hospitalPin;
        this.hospitalPhone = hospitalPhone;
        this.hospitalLogo = hospitalLogo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public String getHospitalCity() {
        return hospitalCity;
    }

    public void setHospitalCity(String hospitalCity) {
        this.hospitalCity = hospitalCity;
    }

    public String getHospitalPin() {
        return hospitalPin;
    }

    public void setHospitalPin(String hospitalPin) {
        this.hospitalPin = hospitalPin;
    }

    public String getHospitalPhone() {
        return hospitalPhone;
    }

    public void setHospitalPhone(String hospitalPhone) {
        this.hospitalPhone = hospitalPhone;
    }

    public byte[] getHospitalLogo() {
        return hospitalLogo;
    }

    public void setHospitalLogo(byte[] hospitalLogo) {
        this.hospitalLogo = hospitalLogo;
    }

    @Override
    public String toString() {
        return "HospitalInfo{" +
                "id=" + id +
                ", hospitalId='" + hospitalId + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", hospitalAddress='" + hospitalAddress + '\'' +
                ", hospitalCity='" + hospitalCity + '\'' +
                ", hospitalPin='" + hospitalPin + '\'' +
                ", hospitalPhone='" + hospitalPhone + '\'' +
                ", hospitalLogo=" + Arrays.toString(hospitalLogo) +
                '}';
    }
}
