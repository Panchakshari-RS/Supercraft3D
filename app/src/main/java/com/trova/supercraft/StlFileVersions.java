package com.trova.supercraft;

/**
 * Created by Panchakshari on 30/3/2017.
 */

public class StlFileVersions {
    private String id;
    private String jobId;
    private String fileName;
    private String fileId;
    private String doctorNotes;
    private String engineerNotes;
    private String date;
    private String status;

    public StlFileVersions() {
    }

    public StlFileVersions(String id, String jobId, String fileName, String fileId, String doctorNotes, String engineerNotes, String date, String status) {
        this.id = id;
        this.jobId = jobId;
        this.fileName = fileName;
        this.fileId = fileId;
        this.doctorNotes = doctorNotes;
        this.engineerNotes = engineerNotes;
        this.date = date;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }

    public String getEngineerNotes() {
        return engineerNotes;
    }

    public void setEngineerNotes(String engineerNotes) {
        this.engineerNotes = engineerNotes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StlFileVersions{" +
                "id='" + id + '\'' +
                ", jobId='" + jobId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileId='" + fileId + '\'' +
                ", doctorNotes='" + doctorNotes + '\'' +
                ", engineerNotes='" + engineerNotes + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
