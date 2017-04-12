package com.trova.supercraft.AmazonS3;

/**
 * Created by Panchakshari on 14/12/2016.
 */

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import static com.trova.supercraft.MyJobsActivity.myGlobals;
import static com.trova.supercraft.SuperCraftUtils.logInfo;

public class AmazonS3Utils {

    public static String COGNITO_POOL_ID = null;
    public static String BUCKET_NAME = null;
    public static String AWS_ACCESS_KEY_ID = null;
    public static String AWS_SECRET_ACCESS_ID = null;

    private static AmazonS3Client sS3Client;
    private static CognitoCachingCredentialsProvider sCredProvider;
    private static TransferUtility sTransferUtility;
    private static TransferManager sTransferManager;
    private static TransferState currentState = TransferState.UNKNOWN;
    private static long bytesReceived;
    private static boolean done = false;

    private static CognitoCachingCredentialsProvider getCredProvider(Context context) {
        if (sCredProvider == null) {
            sCredProvider = new CognitoCachingCredentialsProvider(
                    context.getApplicationContext(),
                    COGNITO_POOL_ID,
                    Regions.US_EAST_1);
        }
        return sCredProvider;
    }

    public static AmazonS3Client getS3Client(Context context) {
        if (sS3Client == null) {
            sS3Client = new AmazonS3Client(getCredProvider(context.getApplicationContext()));
        }
        if(sS3Client == null)
            Log.i("sS3Client", "NULL");
        else
            Log.i("sS3Client", "TRUE");
        return sS3Client;
    }

    public static TransferUtility getAmazonS3TransferUtility(Context context) {
        if (sTransferUtility == null) {
            sTransferUtility = new TransferUtility(getS3Client(context.getApplicationContext()),
                    context.getApplicationContext());
        }

        return sTransferUtility;
    }

    public static TransferManager getTransferManager(Context context) {
        if(sTransferManager == null)
            sTransferManager = new TransferManager(getCredProvider(context.getApplicationContext()));

        return sTransferManager;
    }

    public static AmazonS3Client getS3ClientWithSecureAccess(Context context) {
        if(sS3Client == null) {
            AWSCredentials creden = new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_ID);
            sS3Client = new AmazonS3Client(creden);
            sS3Client.setRegion(Region.getRegion(Regions.US_EAST_1));
        }
        return sS3Client;
    }

    public static String getBytesString(long bytes) {
        String[] quantifiers = new String[] {
                "KB", "MB", "GB", "TB"
        };
        double speedNum = bytes;
        for (int i = 0;; i++) {
            if (i >= quantifiers.length) {
                return "";
            }
            speedNum /= 1024;
            if (speedNum < 512) {
                return String.format("%.2f", speedNum) + " " + quantifiers[i];
            }
        }
    }

    public static TransferUtility initSupercraftAmazonS3(Context context, String cognitoPoolId, String bucketName) {
        COGNITO_POOL_ID = cognitoPoolId;
        BUCKET_NAME = bucketName;
        Log.i("cognitoPoolId", cognitoPoolId);
        Log.i("bucketName", bucketName);
        return getAmazonS3TransferUtility(context);
    }

    public static TransferUtility initSupercraftAmazonS3(Context context, String cognitoPoolId, String bucketName, String accessKey, String secretKey) {
        COGNITO_POOL_ID = cognitoPoolId;
        BUCKET_NAME = bucketName;
        AWS_ACCESS_KEY_ID = accessKey;
        AWS_SECRET_ACCESS_ID = secretKey;
        return getAmazonS3TransferUtility(context);
    }

    public static File copyContentUriToFile(Context context, Uri uri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(uri);
        File copiedData = new File(context.getDir("SampleImagesDir", Context.MODE_PRIVATE), UUID
                .randomUUID().toString());
        copiedData.createNewFile();

        FileOutputStream fos = new FileOutputStream(copiedData);
        byte[] buf = new byte[2046];
        int read = -1;
        while ((read = is.read(buf)) != -1) {
            fos.write(buf, 0, read);
        }

        fos.flush();
        fos.close();

        return copiedData;
    }

    public static void fillMap(Map<String, Object> map, TransferObserver observer, boolean isChecked) {
        int progress = (int) ((double) observer.getBytesTransferred() * 100 / observer
                .getBytesTotal());
        map.put("id", observer.getId());
        map.put("checked", isChecked);
        map.put("fileName", observer.getAbsoluteFilePath());
        map.put("progress", progress);
        map.put("bytes",
                getBytesString(observer.getBytesTransferred()) + "/"
                        + getBytesString(observer.getBytesTotal()));
        map.put("state", observer.getState());
        map.put("percentage", progress + "%");
    }

    public static boolean downloadResourceFromAmazonS3(final Context context, final String filePath, final String mediaLink) {
        logInfo("filePath", filePath);
        logInfo("mediaLink", mediaLink);

        done = false;
        int cnt = 0;
        File file = new File(filePath);
        currentState = TransferState.UNKNOWN;
        TransferUtility transferUtility = myGlobals.transferUtility;
        if(transferUtility != null) {
            TransferObserver observer = transferUtility.download(AmazonS3Utils.BUCKET_NAME, mediaLink, file);
            if(observer != null) {
                observer.refresh();
                logInfo("observer", observer.toString());
                logInfo("AmazonS3Utils.BUCKET_NAME", AmazonS3Utils.BUCKET_NAME);
                TransferListener listener = new DownloadListener();
                observer.setTransferListener(listener);

                while (!done) {
                    if (currentState == TransferState.COMPLETED) {
                        done = true;
                        break;
                    }
                    if (currentState == TransferState.FAILED)
                        break;
                    if(((currentState == TransferState.WAITING_FOR_NETWORK) || (currentState == TransferState.UNKNOWN)) && (cnt >= 5))
                        break;
                    logInfo("downloadResourceFromS3", "Sleeping ..........." + currentState);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    }
                    cnt++;
                }
                logInfo("downloadResourceFromS3", "done ..........." + currentState);
            }
        }

        return done;
    }

    private static class DownloadListener implements TransferListener {
        String TAG = "DownloadListener";
        @Override
        public void onError(int id, Exception e) {
            logInfo(TAG, "Error during download: " + id + " " + e.toString());
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            logInfo(TAG, String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));
            bytesReceived = bytesCurrent;
        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            logInfo(TAG, "onStateChanged: " + id + ", " + newState);
            currentState = newState;
        }
    }

    public static boolean uploadResource2AmazonS3(final File file, final String mediaLink) {
        boolean done = false;
        int cnt = 0;
        logInfo("uploadResource2S3", "Called ...................");
        currentState = TransferState.UNKNOWN;
        logInfo("mediaLink", mediaLink);
        TransferUtility transferUtility = myGlobals.transferUtility;
        if (transferUtility != null) {
            TransferObserver observer = transferUtility.upload(AmazonS3Utils.BUCKET_NAME, mediaLink, file);
            if (observer != null) {
                observer.refresh();
                logInfo("observer", observer.toString());
                TransferListener listener = new UploadListener();
                observer.setTransferListener(listener);

                while (!done) {
                    if (currentState == TransferState.COMPLETED) {
                        done = true;
                        break;
                    }
                    if (currentState == TransferState.FAILED)
                        break;
                    if ((currentState == TransferState.WAITING_FOR_NETWORK) && (cnt >= 50))
                        break;
                    logInfo("uploadResource2S3", "Sleeping ..........." + currentState);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    }
                    cnt++;
                }
            }
        }

        if (done) {
            logInfo("uploadResource2S3", "Transfered Resource Successful ...........");

        }

        return done;
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
