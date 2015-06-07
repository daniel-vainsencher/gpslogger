package com.mendhak.gpslogger.senders.http;

import com.mendhak.gpslogger.common.events.UploadEvents;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import de.greenrobot.event.EventBus;

import org.apache.http.HttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import org.slf4j.LoggerFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HttpJob extends Job {

    private static final org.slf4j.Logger tracer = LoggerFactory.getLogger(HttpJob.class.getSimpleName());

    public static final String postUrl =  "http://www.cyclephilly.org/post/";

    File jsonFile;

    protected HttpJob(File jsonFile) {
        super(new Params(1).requireNetwork().persist());

        this.jsonFile = jsonFile;
    }

    public synchronized static boolean Upload(File jsonFile) {
        boolean result = false;
        List<NameValuePair> nameValuePairs;
        try {
            nameValuePairs = getPostData(jsonFile);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }

        /*
        // For prototype just save the HTTP POST request content to a file so we do not flood the
        // cyclephilly web server with potentially invalid content during the test phase
        try
        {
            File requestFile = new File(jsonFile.getParentFile(), "request-json-" + jsonFile.getName());
            FileWriter writer = new FileWriter(requestFile);
            BufferedWriter output = new BufferedWriter(writer);
            try
            {
                for (NameValuePair nvp : nameValuePairs)
                {
                    output.write("name=" + nvp.getName() + " value=" + nvp.getValue());
                    output.newLine();
                }
            }
            finally
            {
                output.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return result;
        }

        // return now without sending the http request
        if (true)
        return result;
*/
        //Log.debug("PostData=" + nameValuePairs.toString());

        // set connection timeouts for HTTPClient
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        // The default value is zero, that means the timeout is not used.
        int timeoutConnection = 5000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 90000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpClient client = new DefaultHttpClient(httpParameters);


        HttpPost postRequest = new HttpPost(postUrl);

        try {
            postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // For prototype just save the HTTP POST request content to a file so we do not flood the
            // cyclephilly web server with potentially invalid content during the test phase
            if (true)
            {
                logPostRequest(jsonFile, postRequest);
                return true;
            }
            HttpResponse response = client.execute(postRequest);
            String responseString = convertStreamToString(response.getEntity().getContent());
            tracer.debug("httpResponse = " + responseString);
            JSONObject responseData = new JSONObject(responseString);

            ////////////////////////////
            //tracer.debug("server response=" + responseData.toString());
            ///////////////////////////

            if (responseData.getString("status").equals("success")) {                
                tracer.debug("trip updated", "Sent!");
                result = true;
            }else{
                tracer.debug("trip status " + responseData.getString("status"));
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return result;
    }

    private static void logPostRequest(File jsonFile, HttpPost postRequest) {

        try
        {
            File requestFile = new File(jsonFile.getParentFile(), "request-json-" + jsonFile.getName());
            FileWriter writer = new FileWriter(requestFile);
            BufferedWriter output = new BufferedWriter(writer);
            try
            {
                output.write(postRequest.toString());
            }
            finally
            {
                output.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String getDeviceId() {
        String deviceId = "androidDeviceId";
        return deviceId;
    }

    private static JSONObject getCoordsJSON(File jsonFile) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

            FileReader reader = new FileReader(jsonFile);
            BufferedReader br = new BufferedReader(reader);
            // Build JSON objects for each coordinate:
            JSONObject tripDetails = new JSONObject();
            String line;
            while ((line = br.readLine()) != null) {
                JSONObject location = new JSONObject(line);
                tripDetails.put(location.getString("rec"), location);
            }
            return tripDetails;
    }

    private static List<NameValuePair> getPostData(File jsonFile) throws Exception {
        JSONObject coords = getCoordsJSON(jsonFile);
        //JSONObject user = getUserJSON();
        String deviceId = getDeviceId();
        String notes = "";
        String purpose = "commute";
        // TODO where is this data stored?
        String startTime = "";
        String endTime = "";

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("coords", coords.toString()));
        nameValuePairs.add(new BasicNameValuePair("user", ""));
        nameValuePairs.add(new BasicNameValuePair("device", deviceId));
        nameValuePairs.add(new BasicNameValuePair("notes", notes));
        nameValuePairs.add(new BasicNameValuePair("purpose", purpose));
        nameValuePairs.add(new BasicNameValuePair("start", startTime));
        nameValuePairs.add(new BasicNameValuePair("end", endTime));
        nameValuePairs.add(new BasicNameValuePair("version", "2"));

        return nameValuePairs;
    }
    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        // TODO
        if (Upload(jsonFile)) {
           // EventBus.getDefault().post(new UploadEvents.Ftp(true));
        } else {
           // EventBus.getDefault().post(new UploadEvents.Ftp(false));
        }
    }

    @Override
    protected void onCancel() {
       // EventBus.getDefault().post(new UploadEvents.Ftp(false));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        tracer.error("Could not upload file", throwable);
        return false;
    }
}
