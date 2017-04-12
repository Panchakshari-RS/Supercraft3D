package com.trova.supercraft;

/**
 * Created by Panchakshari on 21/2/2017.
 */
public class ChatMessages {
    private int id;
    private String name;
    private String phone_number;
    private String product_id;
    private String message;
    private String date;
    private String time;
    private String mode;
    private String fileExt;
    private String fileName;
    private long fileSize;
    private String mimeType;
    private String thumbPath;
    private String filePath;
    private String durationTime;
    private long sentorrecivedsize;
    private int messageSentOrReceived;
    private int seenstatus;
    private long messageId;
    private String messageDeliveryStatus;
    private int unseenmessageCount;
    private String timeZone;
    private long timemilliseconds;
    private int uploadedResource;
    private String mediaLink;
    private int readMessage;

    public ChatMessages(String message, String date, String time, int status, long messageId, String isDone, String mode, String fileExt, String fileName, long fileSize, String mimeType, String thumbPath, String filePath, String durationTime, int uploadedResource, String mediaLink, int readMessage) {
        this.message = message;
        this.date = date;
        this.time = time;
        this.messageSentOrReceived = status;
        this.messageId = messageId;
        this.messageDeliveryStatus = isDone;
        this.mode = mode;
        this.fileExt = fileExt;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.thumbPath = thumbPath;
        this.filePath = filePath;
        this.durationTime = durationTime;
        this.uploadedResource = uploadedResource;
        this.mediaLink = mediaLink;
        this.readMessage = readMessage;
    }


    public ChatMessages(int id, String name, String phone_number, String product_id, String message, String date, String time, String mode, String fileExt, String fileName, long fileSize, String mimeType, String thumbPath, String filePath, String durationTime, long sentorrecivedsize, int messageSentOrReceived, int seenstatus, long messageId, String messageDeliveryStatus, int unseenmessageCount, String timeZone, long timemilliseconds, int uploadedResource, String mediaLink, int readMessage) {
        this.id = id;
        this.name = name;
        this.phone_number = phone_number;
        this.product_id = product_id;
        this.message = message;
        this.date = date;
        this.time = time;
        this.mode = mode;
        this.fileExt = fileExt;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.thumbPath = thumbPath;
        this.filePath = filePath;
        this.durationTime = durationTime;
        this.sentorrecivedsize = sentorrecivedsize;
        this.messageSentOrReceived = messageSentOrReceived;
        this.seenstatus = seenstatus;
        this.messageId = messageId;
        this.messageDeliveryStatus = messageDeliveryStatus;
        this.unseenmessageCount = unseenmessageCount;
        this.timeZone = timeZone;
        this.timemilliseconds = timemilliseconds;
        this.uploadedResource = uploadedResource;
        this.mediaLink = mediaLink;
        this.readMessage = readMessage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(String durationTime) {
        this.durationTime = durationTime;
    }

    public long getSentorrecivedsize() {
        return sentorrecivedsize;
    }

    public void setSentorrecivedsize(long sentorrecivedsize) {
        this.sentorrecivedsize = sentorrecivedsize;
    }

    public int getMessageSentOrReceived() {
        return messageSentOrReceived;
    }

    public void setMessageSentOrReceived(int messageSentOrReceived) {
        this.messageSentOrReceived = messageSentOrReceived;
    }

    public int getSeenstatus() {
        return seenstatus;
    }

    public void setSeenstatus(int seenstatus) {
        this.seenstatus = seenstatus;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getMessageDeliveryStatus() {
        return messageDeliveryStatus;
    }

    public void setMessageDeliveryStatus(String messageDeliveryStatus) {
        this.messageDeliveryStatus = messageDeliveryStatus;
    }

    public int getUnseenmessageCount() {
        return unseenmessageCount;
    }

    public void setUnseenmessageCount(int unseenmessageCount) {
        this.unseenmessageCount = unseenmessageCount;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public long getTimemilliseconds() {
        return timemilliseconds;
    }

    public void setTimemilliseconds(long timemilliseconds) {
        this.timemilliseconds = timemilliseconds;
    }

    public int getUploadedResource() {
        return uploadedResource;
    }

    public void setUploadedResource(int uploadedResource) {
        this.uploadedResource = uploadedResource;
    }

    public String getMediaLink() {
        return mediaLink;
    }

    public void setMediaLink(String mediaLink) {
        this.mediaLink = mediaLink;
    }

    public int getReadMessage() {
        return readMessage;
    }

    public void setReadMessage(int readMessage) {
        this.readMessage = readMessage;
    }

    @Override
    public String toString() {
        return "ChatMessages{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", product_id='" + product_id + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", mode='" + mode + '\'' +
                ", fileExt='" + fileExt + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", mimeType='" + mimeType + '\'' +
                ", thumbPath='" + thumbPath + '\'' +
                ", filePath='" + filePath + '\'' +
                ", durationTime='" + durationTime + '\'' +
                ", sentorrecivedsize=" + sentorrecivedsize +
                ", messageSentOrReceived=" + messageSentOrReceived +
                ", seenstatus=" + seenstatus +
                ", messageId=" + messageId +
                ", messageDeliveryStatus='" + messageDeliveryStatus + '\'' +
                ", unseenmessageCount=" + unseenmessageCount +
                ", timeZone='" + timeZone + '\'' +
                ", timemilliseconds=" + timemilliseconds +
                ", uploadedResource=" + uploadedResource +
                ", mediaLink='" + mediaLink + '\'' +
                ", readMessage=" + readMessage +
                '}';
    }
}
