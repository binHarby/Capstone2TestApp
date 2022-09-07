package com.example.capstone2test.controller;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private RequestQueue requestQueue;
    private static VolleySingleton mInstance;
    private static Context mCtx;
    private Cache cache;

    // Set up the network to use HttpURLConnection as the HTTP client.
    private Network network;
    private VolleySingleton(Context context){
        this.mCtx=context;
        requestQueue = getRequestQueue();

    }
    public static synchronized VolleySingleton getInstance(Context context) {

        if(mInstance==null){
            mInstance= new VolleySingleton(context);


        }
        return mInstance;
    }
    public RequestQueue getRequestQueue() {
        if(requestQueue==null){
            requestQueue=Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }
    public <T> void AddToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }
}