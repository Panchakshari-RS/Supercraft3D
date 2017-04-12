package com.trova.supercraft;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.crashlytics.android.Crashlytics;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.MessageActivity.addMessage;
import static com.trova.supercraft.MessageActivity.callerName;
import static com.trova.supercraft.MessageActivity.callerPhone;
import static com.trova.supercraft.MessageActivity.compressedImage;
import static com.trova.supercraft.MessageActivity.productId;
import static com.trova.supercraft.MessageActivity.selectedImageUri;
import static com.trova.supercraft.MessageActivity.thumbnail;
import static com.trova.supercraft.SuperCraftUtils.logInfo;


/**
 * Created by Panchakshari on 27/12/2016.
 */

public class MessageUtils implements ImageViewer.UpdateChatImageActivity {

    private static int isIndex = 0;
    private static TransferState currentState;
    private static TransferUtility transferUtility;

    public static byte[] readFileUriContentToBytes(Context ctx, Uri uri) throws IOException {
        logInfo("readFileUriContent", "Called .............1");
        byte bytes[] = null;

        try {
            InputStream input = ctx.getContentResolver().openInputStream(uri);
            logInfo("readFileUriContent", "Called .............2");
            bytes = IOUtils.toByteArray(input);
            logInfo("readFileUriContent", "Called .............3");
            logInfo("FileSize ", "Size " + bytes.length);
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
            logInfo("Error", e.toString());
            logInfo("Error", e.getMessage());
        }

        return bytes;
    }

    public static String getMimeType(String url)
    {
        String extension = url.substring(url.lastIndexOf("."));
        String mimeTypeMap = MimeTypeMap.getFileExtensionFromUrl(extension);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mimeTypeMap);

        return mimeType;
    }

    private static String getDuration(Context context, Uri uri) {
        String duration;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        //use one of overloaded setDataSource() functions to set your data source
        retriever.setDataSource(context, uri);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMillisec = Long.parseLong(time );
        int hrs = (int) TimeUnit.MILLISECONDS.toHours(timeInMillisec) % 24;
        int min = (int) TimeUnit.MILLISECONDS.toMinutes(timeInMillisec) % 60;
        int sec = (int) TimeUnit.MILLISECONDS.toSeconds(timeInMillisec) % 60;
        if(hrs > 0)
            duration = String.format("%02d:%02d:%02d", hrs, min, sec);
        else
            duration = String.format("%02d:%02d", min, sec);

        logInfo("Format", String.format("%02d:%02d:%02d", hrs, min, sec));
        logInfo("duration", duration);
        return duration;
    }

    public static void openSendDialoger(final Context context, final Uri uri, final String title, final String sourceFilePath, final String mimeType, final Bitmap thumbnail){
        final String fileName = sourceFilePath.substring(sourceFilePath.lastIndexOf("/")).substring(1);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(fileName);
        alertDialogBuilder.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                try {
                    byte data[] = null;
                    File thumbFile = null;
                    String thumbFilePath = null, filePath = null;
                    String dur = null;
                    String s3StoragePath;
                    String packageName = myGlobals.context.getPackageName() + "/";
                    logInfo("openSendDialoger", "Called ............");

                    int totalSize = 0;
                    final String fileExt = (sourceFilePath.substring(sourceFilePath.lastIndexOf("."))).substring(1);
                    final CommonDateTimeZone commonDateTimeZone=new CommonDateTimeZone();

                    data = readFileUriContentToBytes(context, uri);
                    if(data != null)
                        totalSize = data.length;

                    filePath = sourceFilePath;
                    final File file = new File(filePath);

                    if(mimeType.contains("video/")) {
                        thumbFile = saveBitmap(thumbnail, "jpg", String.valueOf(commonDateTimeZone.messageId),context.getString(R.string.trova_base_thumb_folder));
                        thumbFilePath = Uri.fromFile(thumbFile).toString();
                        dur = getDuration(context, uri);
                        s3StoragePath = myGlobals.userId + "/videos/";
                    } else {
                        // Audio / Documents
                        if(mimeType.contains("audio/")) {
                            s3StoragePath = myGlobals.userId + "/audios/";
                            dur = getDuration(context, uri);
                        } else {
                            s3StoragePath = myGlobals.userId + "/documents/";
                            thumbFile = saveFile(data, fileExt, String.valueOf(commonDateTimeZone.messageId),context.getString(R.string.trova_base_document_folder));
                            filePath = Uri.fromFile(thumbFile).toString();
                        }
                    }

                    final String duration = dur;
                    logInfo("openSendDialoger", "Called ............");

                    logInfo("TotalSize ", "Size of bytes " + totalSize);
                    logInfo("MimeType ", mimeType);

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String strDate = sdf.format(c.getTime());
                    String[] dtArr = strDate.split("-");
                    String dateSep = dtArr[2] + "/" + dtArr[1] + "/" + dtArr[0] + "/";
                    final String mediaLink = dateSep + s3StoragePath  + file.getName();

                    myGlobals.trovaApi.despatchAttachment(filePath, productId, commonDateTimeZone.messageId, callerPhone);

                    ChatMessages chatMessage = new ChatMessages(null,commonDateTimeZone.currDate,commonDateTimeZone.time,1, commonDateTimeZone.messageId,"false","stream", fileExt, fileName, totalSize, mimeType, thumbFilePath, filePath, duration, 0, mediaLink, 0);
                    try {
                        addMessage(chatMessage);
                        myGlobals.dbHandler.addMessageLogs(new ChatMessages(0, callerName, callerPhone, productId, null, commonDateTimeZone.currDate, commonDateTimeZone.time, "stream", fileExt, fileName, totalSize, mimeType, thumbFilePath, filePath, duration, 0, 1, 0, commonDateTimeZone.messageId, "false", 0, commonDateTimeZone.timezone,commonDateTimeZone.timemilliseconds, 0, mediaLink, 0));
                    }catch (Exception e){
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }
            }
        });
        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void onImagePickerSuccess(Context context, Uri selectedImageUri) {
        if(selectedImageUri == null)
            logInfo("onImagePickerSuccess", "selectedImageUri ..............NULL");
        else
            logInfo("onImagePickerSuccess", "selectedImageUri ..............TRUE");
        InputStream iStream = null;
        byte[] imageData = null;
        try {
            iStream = context.getContentResolver().openInputStream(selectedImageUri);
            imageData = getBytes(iStream);
            logInfo("ImageSize ", String.valueOf(imageData.length));
        } catch (FileNotFoundException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        } catch (IOException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
        thumbnail = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        compressedImage = compressBitmap(thumbnail, 40);

        onSelectFromGalleryResult(context, selectedImageUri, imageData);
        imageData = null;
    }

    public static void onDocumentPickerSuccess(Context context, Uri uri) throws URISyntaxException {
        logInfo("onActivityResult", "PICK_FILE_REQUEST");
        logInfo("onActivityResult", uri.getPath());
        String sourceFilePath = getRealPathFromURI(context, uri);

        logInfo("sourceFilePath", sourceFilePath);
        String mimeType = getMimeType(sourceFilePath);

        if((mimeType.contains("video/")) || (mimeType.contains("audio/")) || (mimeType.contains("image/"))) {
            Toast.makeText(context, "Please choose a different file.", Toast.LENGTH_LONG).show();
            return;
        }

        openSendDialoger(context, uri, "Send Document", sourceFilePath, mimeType, null);
    }

    public static void onAudioPickerSuccess(Context context, Uri uri) throws URISyntaxException {
        String sourceFilePath = getRealPathFromURI(context, uri);
        logInfo("onAudioPickerSuccess", getRealPathFromURI(context, uri));
        String mimeType = getMimeType(sourceFilePath);
        logInfo("onAudioPickerSuccess", mimeType);

        openSendDialoger(context, uri, "Send Audio", sourceFilePath, mimeType, null);
    }

    public static void onVideoCaptureSuccess(Context context, Uri uri) throws URISyntaxException {
        String sourceFilePath = getRealPathFromURI(context, uri);
        String mimeType = getMimeType(sourceFilePath);
        thumbnail = ThumbnailUtils.createVideoThumbnail(sourceFilePath, MediaStore.Images.Thumbnails.MINI_KIND);

        openSendDialoger(context, uri, "Send Video", sourceFilePath, mimeType, thumbnail);
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @SuppressWarnings("deprecation")
    public static void onSelectFromGalleryResult(Context context, Uri selectedImageUri, byte[] data) {

        ImageViewer.registerUpdateChatImageCallback(new MessageUtils());
        logInfo("onSelectFromGallery", "Called ..........size " + compressedImage.length);

        thumbnail.recycle();
        System.gc();
        thumbnail = BitmapFactory.decodeByteArray(compressedImage, 0, compressedImage.length);
        long size = bytes2Size(compressedImage.length);
        if((size > 100) && (isIndex > 0))
            thumbnail = ThumbnailUtils.extractThumbnail(thumbnail, 300, 300, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        Intent intent = new Intent(context.getApplicationContext(), ImageViewer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static long bytes2Size(long bytes) {
        if(bytes == 0) {
            isIndex = 0;
            return 0;
        }

        logInfo("bytes2Size", String.valueOf(bytes));
        Double d = (Math.floor(Math.log(bytes)) / Math.log(1024));
        int i = d.intValue();

        long val = (Math.round(bytes/Math.pow(1024, i)));
        isIndex = i;

        return val;
    }

    @Override
    public void updateChatImage() {
        final byte[] image = compressedImage;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                int totalSize = image.length;
                File thumbFile = null, f = null;
                String thumbFilePath = null, filePath = null;
                final CommonDateTimeZone commonDateTimeZone=new CommonDateTimeZone();
                logInfo("onSelectFromGallery", "Called ............1" + selectedImageUri.toString());

                String mType = "image/jpeg";
                String ext = "jpg";
                if(selectedImageUri != null) {
                    try {
                        filePath = getRealPathFromURI(myGlobals.context, selectedImageUri);
                    } catch (URISyntaxException e) {
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    }
                    mType = getMimeType(filePath);
                    ext = filePath.substring(filePath.lastIndexOf(".")).substring(1);
                }

                try {
                    f = saveFile(image, ext, String.valueOf(commonDateTimeZone.messageId),myGlobals.context.getString(R.string.trova_base_image_folder));

                    long size = bytes2Size(f.length());
                    logInfo("Saved Filed Size ", String.valueOf(size));
                    filePath = Uri.fromFile(f).getPath();
                    if((size > 100) && (isIndex > 0)) {
                        thumbFile = saveBitmap(thumbnail, ext, String.valueOf(commonDateTimeZone.messageId),myGlobals.context.getString(R.string.trova_base_thumb_folder));
                    } else {
                        thumbFile = saveFile(image, ext, String.valueOf(commonDateTimeZone.messageId),myGlobals.context.getString(R.string.trova_base_thumb_folder));
                    }

                    thumbFilePath = Uri.fromFile(thumbFile).toString();
                } catch (IOException e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }
                String fileName = filePath.substring(filePath.lastIndexOf("/")).substring(1);

                logInfo("filePath", filePath);

                String packageName = myGlobals.context.getPackageName() + "/";
                String s3StoragePath = myGlobals.userId + "/images/";
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String strDate = sdf.format(c.getTime());
                String[] dtArr = strDate.split("-");
                String dateSep = dtArr[2] + "/" + dtArr[1] + "/" + dtArr[0] + "/";
                final String mediaLink = dateSep + s3StoragePath  + f.getName();
                final String fileExt = ext;
                final String mimeType = mType;
                final File file = f;

                myGlobals.trovaApi.despatchAttachment(filePath, productId, commonDateTimeZone.messageId, callerPhone);

                ChatMessages chatMessage = new ChatMessages(null,commonDateTimeZone.currDate,commonDateTimeZone.time,1, commonDateTimeZone.messageId,"false","stream", fileExt, fileName, totalSize, mimeType, thumbFilePath, filePath, null, 0, mediaLink, 0);
                try {
                    addMessage(chatMessage);
                    myGlobals.dbHandler.addMessageLogs(new ChatMessages(0, callerName, callerPhone, productId, null, commonDateTimeZone.currDate, commonDateTimeZone.time, "stream", fileExt, fileName, totalSize, mimeType, thumbFilePath, filePath, null, 0, 1, 0, commonDateTimeZone.messageId, "false", 0, commonDateTimeZone.timezone, commonDateTimeZone.timemilliseconds, 0, mediaLink, 0));
                }catch (Exception e){
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }

            }
        });
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI(Context context, Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;

        if (needToCheckUri && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[] {
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                Crashlytics.logException(e);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static Bitmap createThumbnailFromPath(String filePath, int type) {
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(filePath), 300, 300, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
    }

    public static Bitmap retriveVideoFrameFromVideo(Context context, String videoPath) throws Throwable {
        Bitmap bitmap = null;
        Uri uri = Uri.parse(videoPath);
        MediaMetadataRetriever mediaMetadataRetriever = null;

        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(context, uri);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public static byte[] compressBitmap(Bitmap thumbnail, int quality) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, quality, bytes);

        return bytes.toByteArray();
    }

    public static void launchGalleryViewer(Activity ctx, Uri uri, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri imgUri = Uri.parse("file://" + uri.toString());
        intent.setDataAndType(imgUri, mimeType);

        ctx.startActivityForResult(intent, MessageActivity.ACTION_VIEW_GALLERY);
    }

    public static File saveBitmap(Bitmap bmp, String fileExt, String messageId, String folderType) throws IOException {
        Context context = myGlobals.context;
        String mBaseFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+context.getPackageName() + context.getString(R.string.trova_base_folder) + folderType;
        if (!new File(mBaseFolderPath).exists()) {
            new File(mBaseFolderPath).mkdirs();
        }
        String fileName = messageId + "." + fileExt;
        String path = mBaseFolderPath+fileName;

        File f = new File(path);
        FileOutputStream fo = null;

        try {
            byte[] data = compressBitmap(bmp, 100);
            f.createNewFile();
            logInfo("saveBitmap FilePath", path);
            logInfo("Bytes", "Data Size " + data.length);
            fo = new FileOutputStream(f);
            fo.write(data);
        } catch(Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
            logInfo("Failed to", "Create file " + path);
        } finally {
            if(fo != null){
                try {
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
            }
        }

        return f;
    }

    public static File saveFile(byte[] data, String fileExt, String messageId, String folderType) throws IOException {
        Context context = myGlobals.context;
        String mBaseFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+context.getPackageName() + context.getString(R.string.trova_base_folder) + folderType;
        if (!new File(mBaseFolderPath).exists()) {
            new File(mBaseFolderPath).mkdirs();
        }
        String fileName = messageId + "." + fileExt;
        String path = mBaseFolderPath+fileName;

        File f = new File(path);
        FileOutputStream fo = null;

        try {
            f.createNewFile();
            logInfo("saveFile FilePath", path);
            logInfo("Bytes", "Data Size " + data.length);
            fo = new FileOutputStream(f);
            fo.write(data);
            fo.flush();
        } catch(Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
            logInfo("Failed to", "Create file " + path);
        } finally {
            if(fo != null){
                try {
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
            }
        }

        return f;
    }

    private static class UploadListener implements TransferListener {
        String TAG = "UploadListener";
        // Simply updates the UI list when notified.
        @Override
        public void onError(int id, Exception e) {
            logInfo(TAG, "Error during upload: " + id + " " + e.toString());
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            logInfo(TAG, String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));
        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            logInfo(TAG, "onStateChanged: " + id + ", " + newState);
            currentState = newState;
        }
    }

}
