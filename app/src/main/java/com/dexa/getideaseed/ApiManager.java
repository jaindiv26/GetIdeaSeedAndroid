package com.dexa.getideaseed;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dev on 02/03/18.
 */

public class ApiManager {

    ApiManagerListener apiManagerListener;

    public ApiManager(ApiManagerListener apiManagerListener) {
        this.apiManagerListener =apiManagerListener;
    }

    public ApiManager() {
    }

    public void postRequest(Context context, String url, final HashMap<String,String> params){
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (response != null) {
                    apiManagerListener.onSuccess(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                apiManagerListener.onError(error);
            }
        })
        {
            @Override protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int x = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    public void getRequest(Context context, String url){
        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (response != null) {
                    apiManagerListener.onSuccess(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                apiManagerListener.onError(error);
            }
        })
        {
            @Override protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int x = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    public void deleteRequest(Context context,String url){
        final StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (response != null) {
                    apiManagerListener.onSuccess(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                apiManagerListener.onError(error);
            }
        })
        {
            @Override protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int x = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public void putRequest(Context context,String url, final HashMap<String,String> params){
        final StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (response != null) {
                    apiManagerListener.onSuccess(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                apiManagerListener.onError(error);
            }
        })
        {
            @Override protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int x = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }
}
