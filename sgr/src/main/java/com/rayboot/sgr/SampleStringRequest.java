package com.rayboot.sgr;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.timeface.utils.DeviceUtil;
import cn.timeface.utils.DeviceUuidFactory;
import cn.timeface.utils.GzipUtil;
import cn.timeface.utils.NetworkUtil;
import cn.timeface.utils.SharedUtil;
import cn.timeface.utils.TimeFaceUtilInit;

/**
 * @author rayboot
 * @from 14-3-5 15:16
 * @TODO http请求返回字符串
 *
 */
public class SampleStringRequest extends StringRequest
{
    private Map<String, String> postParams;

    public SampleStringRequest(int method, String url,
            Response.Listener<String> listener,
            Response.ErrorListener errorListener, Map<String, String> params)
    {
        super(method, url, listener, errorListener);
        postParams = params == null ? new HashMap<String, String>() : params;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError
    {
        return postParams;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("LOC", DeviceUtil.getLanguageInfo());
        headers.put("userId",
                SharedUtil.getInstance().getUserId());
        headers.put("deviceId",
                new DeviceUuidFactory(TimeFaceUtilInit.getContext()).getDeviceId());
        return headers;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            return NetworkUtil.isGzipSupport(response) ? Response.success(
                    GzipUtil.decompress(response.data),
                    HttpHeaderParser.parseCacheHeaders(response))
                    : super.parseNetworkResponse(response);
        }
        catch (IOException e)
        {
            return Response.error(new ParseError(e));
        }
    }
}
