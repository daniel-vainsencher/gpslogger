package com.mendhak.gpslogger; /**
 * Created by danielv on 7/23/15.
 */

import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONObject;

public class SeeClickFixReport {
    public static void testSeeClickFix(RequestQueue queue) {
        String url = "http://test.seeclickfix.com/api/v2/issues?page=2&per_page=10";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

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

