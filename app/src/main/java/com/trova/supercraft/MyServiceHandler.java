package com.trova.supercraft;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import in.trova.android.TrovaService;

/**
 * Created by Panchakshari on 10/2/2017.
 */

public class MyServiceHandler {

    public static String sendRequest2Server(String extUrl, String service, String request, String requestMethod, String contentType) {
        String response = null;
        BufferedReader reader = null;
        int responseCode = 0;
        HttpURLConnection conn = null;
        OutputStreamWriter wr = null;
        int timeout = 10000;
        boolean processRequest = true;

        if(!processRequest) {
            return null;
        }

        String path = null;

        if(requestMethod.equals("POST"))
            path = extUrl + service;
        else
            path = extUrl + service + request;

        Log.i("sendRequest2Server URL", path);
        Log.i("request", request);

        try {
            URL url = new URL(path);

            conn = (HttpURLConnection)url.openConnection();
            if(requestMethod.equals("POST")) {
                if(contentType != null)
                    conn.setRequestProperty("Content-Type", contentType);
                conn.setConnectTimeout(timeout);
                conn.setReadTimeout(timeout);
                conn.setDoOutput(true);
                wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(request);
                wr.flush();
                responseCode = HttpURLConnection.HTTP_OK;
            } else {
                conn.setRequestProperty("Content-length", "0");
                conn.setUseCaches(false);
                conn.setAllowUserInteraction(false);
                conn.setConnectTimeout(timeout);
                conn.setReadTimeout(timeout);
                conn.connect();
                responseCode = conn.getResponseCode();
            }

            switch(responseCode) {
                case HttpURLConnection.HTTP_CREATED :
                case HttpURLConnection.HTTP_OK :
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    if(sb.length() > 0)
                        response = sb.toString();
                    reader.close();
                    break;
            }

        } catch (MalformedURLException ex) {
            TrovaService.logData("MalformedURLException ", ex.toString());
            Crashlytics.logException(ex);
            ex.printStackTrace();
        } catch (IOException ex) {
            TrovaService.logData("IOException ", ex.toString());
            Crashlytics.logException(ex);
            ex.printStackTrace();
        } catch(Exception ex) {
            TrovaService.logData("Exception ", ex.toString());
            Crashlytics.logException(ex);
            ex.printStackTrace();
        } finally {
            if(wr != null) {
                try {
                    wr.close();
                } catch (IOException e) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception ex) {
                    Crashlytics.logException(ex);
                    Log.i("finally Exception ", ex.toString());
                    ex.printStackTrace();
                }
            }
        }

        return response;
    }
}
