package com.rayboot.sgr;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;


/**
 * @author rayboot
 * @from 14/9/29 16:02
 * @TODO
 */
public class MyVolley
{
    public static final String TAG = "MyVolley";
    private static MyVolley myVolley = new MyVolley();
    private Context context;

    public static MyVolley getInstance(Context context) {
        myVolley.context = context;
        return myVolley;
    }

    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue()
    {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(this.context, new OkHttpStack());
        }

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     */
    public <T> void addToRequestQueue(Request<T> req, String tag)
    {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     */
    public <T> void addToRequestQueue(Request<T> req)
    {
        // set the default tag if tag is empty
        addToRequestQueue(req, TAG);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     */
    public void cancelPendingRequests(Object tag)
    {
        if (mRequestQueue != null)
        {
            mRequestQueue.cancelAll(tag);
        }
    }
}
