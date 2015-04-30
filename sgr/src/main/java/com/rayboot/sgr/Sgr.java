package com.rayboot.sgr;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.rayboot.sgr.stateview.StateView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author rayboot
 * @from 14/10/31 13:58
 * @TODO Sample mGson request
 * 默认使用POST
 * 必须传 url和gsonClass
 * mUrl  请求连接地址
 * mClazz  返回json对象的类
 * mParams  请求参数
 * mErrorListener 异常时回调
 * mSuccessListener  成功时回调
 * mFinishListener   成功或异常都回调
 * <p/>
 * mClickView  控制该view的enable和clickable属性方式机关枪
 * mErrorView  控制状态页面的显示
 * <p/>
 * 例子
 * Sgr.builder(this, BaseResponse.class)
 * .url("http://baidu.com")
 * .requestParams()
 * .finishListener(new GsonRequest.FinishListener<BaseResponse>()
 * {
 * @Override public void onFinishResponse(boolean isSuccess,
 * BaseResponse response, VolleyError errorView)
 * {
 * if (isSuccess)
 * {
 * //do something
 * }
 * }
 * })
 * .post2Queue(getVolleyTag());
 */
public class Sgr<T> {
    int mMethod = Request.Method.POST;
    String mUrl;
    String mTag;
    Class<T> mClazz;
    Response.Listener<T> mSuccessListener;
    Response.ErrorListener mErrorListener;
    View mClickView = null;
    Map<String, String> mParams = new HashMap<String, String>();
    Gson mGson;
    GsonRequest.FinishListener<T> mFinishListener;
    Map<String, String> mHeaders;
    StateView mErrorView = null;
    final int WIFI_TIMEOUT_TIME = 15 * 1000;
    final int MOBILE_TIMEOUT_TIME = 60 * 1000;
    int mCustomTimeOut = -1;

    final boolean DEBUG = true;

    public static <T> Sgr<T> builder(Context context, Class<T> classOfT) {
        return new Sgr<T>().with(context).gsonClass(classOfT);
    }

    public Sgr<T> method(int method) {
        this.mMethod = method;
        return this;
    }

    public Sgr<T> timeout(int timeout) {
        this.mCustomTimeOut = timeout;
        return this;
    }

    public Sgr<T> url(String url) {
        this.mUrl = url;
        return this;
    }

    private Sgr<T> gsonClass(Class<T> mClazz) {
        this.mClazz = mClazz;
        return this;
    }

    public Sgr<T> successListener(Response.Listener<T> listener) {
        this.mSuccessListener = listener;
        return this;
    }

    public Sgr<T> finishListener(GsonRequest.FinishListener<T> listener) {
        this.mFinishListener = listener;
        return this;
    }

    public Sgr<T> errorListener(Response.ErrorListener listener) {
        this.mErrorListener = listener;
        return this;
    }

    public Sgr<T> clickView(View clickView) {
        this.mClickView = clickView;
        return this;
    }

    public Sgr<T> requestParams(Map<String, String> params) {
        this.mParams = params;
        return this;
    }

    public Sgr<T> setHeaders(Map<String, String> mHeaders) {
        this.mHeaders = mHeaders;
        return this;
    }

    public Sgr<T> gson(Gson gson) {
        this.mGson = gson;
        return this;
    }

    public Sgr<T> stateView(StateView view) {
        this.mErrorView = view;
        return this;
    }

    public Sgr<T> tag(String tag) {
        this.mTag = tag;
        return this;
    }

    private Sgr<T> with(Context context) {
        if (context instanceof INetWork) {
            mTag = ((INetWork) context).getVolleyTag();
        }
        return this;
    }

    public void post2Queue() {
        post2Queue(mTag);
    }

    public void post2Queue(String tag) {
        if (TextUtils.isEmpty(mUrl)) {
            throw new IllegalArgumentException("url must not be null.");
        }
        if (mClazz == null) {
            throw new IllegalArgumentException("class must not be null.");
        }
        if (tag == null) {
            throw new IllegalArgumentException(
                    "tag must not be null.you can use .tag() or .with(INetWork context) to add tag");
        }


        GsonRequest<T> gsonRequest = new GsonRequest<T>(this.mMethod, this.mUrl,
                this.mClazz, this.mSuccessListener,
                this.mErrorListener, mParams, mHeaders,
                this.mGson == null ? new Gson() : this.mGson, mCustomTimeOut > 0 ? mCustomTimeOut : NetworkUtil.isWifiConnected(SgrVolley.getMainContext()) ? WIFI_TIMEOUT_TIME : MOBILE_TIMEOUT_TIME);

        gsonRequest.oneClickView(mClickView);
        gsonRequest.finishListener(mFinishListener);
        gsonRequest.errorView(mErrorView);
        gsonRequest.setShouldCache(false);
        if (DEBUG) {
            StringBuilder params = new StringBuilder(10);
            params.append("?");
            try {
                if (gsonRequest.getParams() != null
                        && gsonRequest.getParams().size() > 0) {
                    Iterator iter = gsonRequest.getParams().entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        params.append(entry.getKey());
                        params.append("=");
                        params.append(entry.getValue());
                        params.append("&");
                    }
                }
                params.deleteCharAt(params.length() - 1);
            } catch (AuthFailureError authFailureError) {
                authFailureError.printStackTrace();
            }
            System.out.println("volley req url = " + mUrl + params.toString());
        }
        SgrVolley.getInstance().addToRequestQueue(gsonRequest, tag);
    }
}
