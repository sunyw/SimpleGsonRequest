/**
 * Copyright 2013 Ognyan Bankov
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.rayboot.sgr;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rayboot.sgr.errorview.ErrorView;
import com.rayboot.sgr.errorview.HttpStatusCodes;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author rayboot
 * @from 14-3-5 15:16
 * @TODO http请求返回GSON解析后对象
 */
public class GsonRequest<T> extends Request<T> {
    protected Class<T> mClazz;
    protected Listener<T> mSuccessListener;
    protected FinishListener<T> mFinishListener;
    protected Map<String, String> mParams;
    protected Gson mGson;
    View clickView = null;
    ErrorView errorView = null;
    private int curTimeout = 15 * 1000;
    private Context context;

    /**
     * 带有超时时间的request
     * @param method
     * @param url
     * @param clazz
     * @param listener
     * @param errorListener
     * @param params
     * @param gson
     * @param timeout 设置超时时间
     */
    public GsonRequest(Context context, int method, String url, Class<T> clazz,
                       Listener<T> listener, ErrorListener errorListener,
                       Map<String, String> params, Gson gson, int timeout) {
        super(method, url, errorListener);
        this.context = context;
        mClazz = clazz;
        mSuccessListener = listener;
        mParams = params == null ? new HashMap<String, String>() : params;
        mGson = gson;
        this.curTimeout = timeout;
        setRetryPolicy(getRetryPolicy());
    }

    public GsonRequest<T> oneClickView(View clickView) {
        this.clickView = clickView;
        if (clickView != null) {
            clickView.setEnabled(false);
            clickView.setClickable(false);
        }
        return this;
    }

    public GsonRequest<T> finishListener(FinishListener<T> finishListener) {
        this.mFinishListener = finishListener;
        return this;
    }

    public GsonRequest<T> errorView(ErrorView errorView) {
        this.errorView = errorView;
        return this;
    }

    /**
     * (non-Javadoc)
     * 设置超时时间
     *
     * @see com.android.volley.Request#getRetryPolicy()
     */
    @Override
    public RetryPolicy getRetryPolicy() {
        RetryPolicy retryPolicy = new DefaultRetryPolicy(curTimeout, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return retryPolicy;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return NetworkUtil.getHttpHeaders();
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        int errorRes = R.string.state_error_network;
        if (error instanceof NoConnectionError) {
            errorRes = R.string.state_error_no_connect;
        } else if (error instanceof TimeoutError) {
            errorRes = R.string.state_error_timeout;
        } else if (error instanceof ServerError) {
            errorRes = R.string.state_error_server_error;
        } else if (error instanceof ParseError) {
            errorRes = R.string.state_error_parse_error;
        }

        Toast.makeText(this.context, this.context.getText(errorRes), Toast.LENGTH_SHORT).show();
        if (clickView != null) {
            this.clickView.setClickable(true);
            this.clickView.setEnabled(true);
        }
        if (errorView != null) {
            if (error.networkResponse == null) {
                errorView.setState(HttpStatusCodes.NO_CONNECT);
            } else if (error.networkResponse.statusCode > 0) {
                errorView.setState(error.networkResponse.statusCode);
            } else {
                errorView.setState(HttpStatusCodes.NO_CONNECT);
            }
        }
        if (mFinishListener != null) {
            mFinishListener.onFinishResponse(false, null, error);
        }
    }

    @Override
    protected void deliverResponse(T response) {
        if (clickView != null) {
            this.clickView.setClickable(true);
            this.clickView.setEnabled(true);
        }
        if (errorView != null) {
            errorView.setState(HttpStatusCodes.FINISH);
        }
        if (mSuccessListener != null) {
            mSuccessListener.onResponse(response);
        }
        if (mFinishListener != null) {
            mFinishListener.onFinishResponse(true, response, null);
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json =
                    NetworkUtil.isGzipSupport(response) ? GzipUtil.decompress(
                            response.data) : new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(json, mClazz), HttpHeaderParser.parseCacheHeaders(response, false));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    /** Callback interface for delivering errorView responses. */
    public interface FinishListener<T> {
        /**
         * Callback method that an errorView has been occurred with the
         * provided errorView code and optional user-readable message.
         */
        public void onFinishResponse(boolean isSuccess, T response,
                                     VolleyError error);
    }
}