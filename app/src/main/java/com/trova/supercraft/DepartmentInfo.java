package com.trova.supercraft;

/**
 * Created by Panchakshari on 8/3/2017.
 */

public class DepartmentInfo {
    protected int id;
    protected String departmentId;
    protected String departmentName;

    public DepartmentInfo() {
    }

    public DepartmentInfo(int id, String departmentId, String departmentName) {
        this.id = id;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "DepartmentInfo{" +
                "id=" + id +
                ", departmentId='" + departmentId + '\'' +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
