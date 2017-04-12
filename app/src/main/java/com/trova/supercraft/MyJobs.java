package com.trova.supercraft;

/**
 * Created by Panchakshari on 27/9/2016.
 */
public class MyJobs {
    private String jobId;
    private String patientName;
    private String patientId;
    private String hospitalId;
    private String departmentId;
    private String doctorFname;
    private String doctorLname;
    private String doctorEmail;
    private String doctorKey;
    private String priority;
    private int status;
    private String createdDate;
    private String acceptedDate;
    private String createdById;
    private String engineerFname;
    private String engineerLname;
    private String engineerEmail;
    private String engineerKey;
    private String techFname;
    private String techLname;
    private String techEmail;
    private String techKey;
    private String content;
    private String stlFileIds;
    private String stlFilePath;
    private String stlDoctorFileNotes;
    private String stlEngineerFileNotes;
    private String mriFile;
    private String ctFile;
    private String usFile;
    private String echoFile;
    private String otherFile;
    private String mriFileId;
    private String ctFileId;
    private String usFileId;
    private String echoFileId;
    private String otherFileId;
    private String dicomNotes;
    private int id;

    public MyJobs() {
    }

    public MyJobs(String jobId, String patientName, String patientId, String hospitalId, String departmentId, String doctorFname, String doctorLname, String doctorEmail, String doctorKey, String priority, int status, String createdDate, String acceptedDate, String createdById, String engineerFname, String engineerLname, String engineerEmail, String engineerKey, String techFname, String techLname, String techEmail, String techKey, String content, String stlFileIds, String stlFilePath, String stlDoctorFileNotes, String stlEngineerFileNotes, String mriFile, String ctFile, String usFile, String echoFile, String otherFile, String mriFileId, String ctFileId, String usFileId, String echoFileId, String otherFileId, String dicomNotes, int id) {
        this.jobId = jobId;
        this.patientName = patientName;
        this.patientId = patientId;
        this.hospitalId = hospitalId;
        this.departmentId = departmentId;
        this.doctorFname = doctorFname;
        this.doctorLname = doctorLname;
        this.doctorEmail = doctorEmail;
        this.doctorKey = doctorKey;
        this.priority = priority;
        this.status = status;
        this.createdDate = createdDate;
        this.acceptedDate = acceptedDate;
        this.createdById = createdById;
        this.engineerFname = engineerFname;
        this.engineerLname = engineerLname;
        this.engineerEmail = engineerEmail;
        this.engineerKey = engineerKey;
        this.techFname = techFname;
        this.techLname = techLname;
        this.techEmail = techEmail;
        this.techKey = techKey;
        this.content = content;
        this.stlFileIds = stlFileIds;
        this.stlFilePath = stlFilePath;
        this.stlDoctorFileNotes = stlDoctorFileNotes;
        this.stlEngineerFileNotes = stlEngineerFileNotes;
        this.mriFile = mriFile;
        this.ctFile = ctFile;
        this.usFile = usFile;
        this.echoFile = echoFile;
        this.otherFile = otherFile;
        this.mriFileId = mriFileId;
        this.ctFileId = ctFileId;
        this.usFileId = usFileId;
        this.echoFileId = echoFileId;
        this.otherFileId = otherFileId;
        this.dicomNotes = dicomNotes;
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
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

    public String getDoctorFname() {
        return doctorFname;
    }

    public void setDoctorFname(String doctorFname) {
        this.doctorFname = doctorFname;
    }

    public String getDoctorLname() {
        return doctorLname;
    }

    public void setDoctorLname(String doctorLname) {
        this.doctorLname = doctorLname;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getDoctorKey() {
        return doctorKey;
    }

    public void setDoctorKey(String doctorKey) {
        this.doctorKey = doctorKey;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getAcceptedDate() {
        return acceptedDate;
    }

    public void setAcceptedDate(String acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public String getEngineerFname() {
        return engineerFname;
    }

    public void setEngineerFname(String engineerFname) {
        this.engineerFname = engineerFname;
    }

    public String getEngineerLname() {
        return engineerLname;
    }

    public void setEngineerLname(String engineerLname) {
        this.engineerLname = engineerLname;
    }

    public String getEngineerEmail() {
        return engineerEmail;
    }

    public void setEngineerEmail(String engineerEmail) {
        this.engineerEmail = engineerEmail;
    }

    public String getEngineerKey() {
        return engineerKey;
    }

    public void setEngineerKey(String engineerKey) {
        this.engineerKey = engineerKey;
    }

    public String getTechFname() {
        return techFname;
    }

    public void setTechFname(String techFname) {
        this.techFname = techFname;
    }

    public String getTechLname() {
        return techLname;
    }

    public void setTechLname(String techLname) {
        this.techLname = techLname;
    }

    public String getTechEmail() {
        return techEmail;
    }

    public void setTechEmail(String techEmail) {
        this.techEmail = techEmail;
    }

    public String getTechKey() {
        return techKey;
    }

    public void setTechKey(String techKey) {
        this.techKey = techKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStlFileIds() {
        return stlFileIds;
    }

    public void setStlFileIds(String stlFileIds) {
        this.stlFileIds = stlFileIds;
    }

    public String getStlFilePath() {
        return stlFilePath;
    }

    public void setStlFilePath(String stlFilePath) {
        this.stlFilePath = stlFilePath;
    }

    public String getStlDoctorFileNotes() {
        return stlDoctorFileNotes;
    }

    public void setStlDoctorFileNotes(String stlDoctorFileNotes) {
        this.stlDoctorFileNotes = stlDoctorFileNotes;
    }

    public String getStlEngineerFileNotes() {
        return stlEngineerFileNotes;
    }

    public void setStlEngineerFileNotes(String stlEngineerFileNotes) {
        this.stlEngineerFileNotes = stlEngineerFileNotes;
    }

    public String getMriFile() {
        return mriFile;
    }

    public void setMriFile(String mriFile) {
        this.mriFile = mriFile;
    }

    public String getCtFile() {
        return ctFile;
    }

    public void setCtFile(String ctFile) {
        this.ctFile = ctFile;
    }

    public String getUsFile() {
        return usFile;
    }

    public void setUsFile(String usFile) {
        this.usFile = usFile;
    }

    public String getEchoFile() {
        return echoFile;
    }

    public void setEchoFile(String echoFile) {
        this.echoFile = echoFile;
    }

    public String getOtherFile() {
        return otherFile;
    }

    public void setOtherFile(String otherFile) {
        this.otherFile = otherFile;
    }

    public String getMriFileId() {
        return mriFileId;
    }

    public void setMriFileId(String mriFileId) {
        this.mriFileId = mriFileId;
    }

    public String getCtFileId() {
        return ctFileId;
    }

    public void setCtFileId(String ctFileId) {
        this.ctFileId = ctFileId;
    }

    public String getUsFileId() {
        return usFileId;
    }

    public void setUsFileId(String usFileId) {
        this.usFileId = usFileId;
    }

    public String getEchoFileId() {
        return echoFileId;
    }

    public void setEchoFileId(String echoFileId) {
        this.echoFileId = echoFileId;
    }

    public String getOtherFileId() {
        return otherFileId;
    }

    public void setOtherFileId(String otherFileId) {
        this.otherFileId = otherFileId;
    }

    public String getDicomNotes() {
        return dicomNotes;
    }

    public void setDicomNotes(String dicomNotes) {
        this.dicomNotes = dicomNotes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MyJobs{" +
                "jobId='" + jobId + '\'' +
                ", patientName='" + patientName + '\'' +
                ", patientId='" + patientId + '\'' +
                ", hospitalId='" + hospitalId + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", doctorFname='" + doctorFname + '\'' +
                ", doctorLname='" + doctorLname + '\'' +
                ", doctorEmail='" + doctorEmail + '\'' +
                ", doctorKey='" + doctorKey + '\'' +
                ", priority='" + priority + '\'' +
                ", status=" + status +
                ", createdDate='" + createdDate + '\'' +
                ", acceptedDate='" + acceptedDate + '\'' +
                ", createdById='" + createdById + '\'' +
                ", engineerFname='" + engineerFname + '\'' +
                ", engineerLname='" + engineerLname + '\'' +
                ", engineerEmail='" + engineerEmail + '\'' +
                ", engineerKey='" + engineerKey + '\'' +
                ", techFname='" + techFname + '\'' +
                ", techLname='" + techLname + '\'' +
                ", techEmail='" + techEmail + '\'' +
                ", techKey='" + techKey + '\'' +
                ", content='" + content + '\'' +
                ", stlFileIds='" + stlFileIds + '\'' +
                ", stlFilePath='" + stlFilePath + '\'' +
                ", stlDoctorFileNotes='" + stlDoctorFileNotes + '\'' +
                ", stlEngineerFileNotes='" + stlEngineerFileNotes + '\'' +
                ", mriFile='" + mriFile + '\'' +
                ", ctFile='" + ctFile + '\'' +
                ", usFile='" + usFile + '\'' +
                ", echoFile='" + echoFile + '\'' +
                ", otherFile='" + otherFile + '\'' +
                ", mriFileId='" + mriFileId + '\'' +
                ", ctFileId='" + ctFileId + '\'' +
                ", usFileId='" + usFileId + '\'' +
                ", echoFileId='" + echoFileId + '\'' +
                ", otherFileId='" + otherFileId + '\'' +
                ", dicomNotes='" + dicomNotes + '\'' +
                ", id=" + id +
                '}';
    }
}
