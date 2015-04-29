package com.rayboot.sgr;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.NetworkResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rayboot
 * @from 2014-1-24上午10:52:54
 * @TODO 网络相关的公用方法
 */
public class NetworkUtil {
    public static Map<String, String> getHttpHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("VERSION", "1");
        return headers;
    }

    /**
     * 判断返回数据是否经过 gzip 压缩
     *
     * @param req
     * @return boolean 值
     */
    public static boolean isGzipSupport(NetworkResponse req) {
        for (Map.Entry<String, String> entry : req.headers.entrySet()) {
            if (entry.getKey().equals("Data-Type")) {
                return entry.getValue().equals("gzip");
            }
        }
        return false;
    }


    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
