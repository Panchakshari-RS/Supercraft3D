package com.trova.supercraft;

/**
 * Created by Panchakshari on 6/10/2015.
 */
public class MyJobsRowItem {
    private String jobId;
    private int status;
    private String patientName;
    private String patientId;
    private String content;
    private String strDate;
    private int id;
    private String priority;
    private MyJobs myJobs;

    public MyJobsRowItem(String jobId, int status, String patientName, String patientId, String content, String strDate, int id, String priority, MyJobs myJobs) {
        this.jobId = jobId;
        this.status = status;
        this.patientName = patientName;
        this.patientId = patientId;
        this.content = content;
        this.strDate = strDate;
        this.id = id;
        this.priority = priority;
        this.myJobs = myJobs;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public MyJobs getMyJobs() {
        return myJobs;
    }

    public void setMyJobs(MyJobs myJobs) {
        this.myJobs = myJobs;
    }

    @Override
    public String toString() {
        return "MyJobsRowItem{" +
                "jobId='" + jobId + '\'' +
                ", status=" + status +
                ", patientName='" + patientName + '\'' +
                ", patientId='" + patientId + '\'' +
                ", content='" + content + '\'' +
                ", strDate='" + strDate + '\'' +
                ", id=" + id +
                ", priority='" + priority + '\'' +
                ", myJobs=" + myJobs +
                '}';
    }
}
