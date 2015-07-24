package com.mendhak.gpslogger; /**
 * Created by danielv on 7/23/15.
 */

import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SeeClickFixReport {
    public static void testSeeClickFix(RequestQueue queue) {
        String url = "http://test.seeclickfix.com/api/v2/issues?page=2&per_page=10";


        Map<String, Object> ans = new HashMap<String, Object>();
        ans.put("summary", "testing");
        ans.put("description", "Please ignore");

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("lat", 40.3513360);
        data.put("lng", -74.6549120);
        data.put("address", "200 Nassau Street, Princeton, NJ");
        data.put("request_type_id", 5633);
        data.put("anonymize_reporter", true);
        data.put("answers", ans);

        JSONObject req = new JSONObject(data);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, req, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SeeClickFixReport", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("SeeClickFix","Got error");

                    }
                });
        queue.add(jsObjRequest);
        queue.start();
    }
}
