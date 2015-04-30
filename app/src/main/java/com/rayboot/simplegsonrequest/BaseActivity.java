package com.rayboot.simplegsonrequest;

import android.app.Activity;
import android.os.Bundle;

import com.rayboot.sgr.INetWork;
import com.rayboot.sgr.SgrVolley;

/**
 * Created by rayboot on 15/4/30.
 */
public class BaseActivity extends Activity implements INetWork {
    protected String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = ((Object) this).getClass().getSimpleName();
    }

    @Override
    protected void onDestroy() {
        SgrVolley.getInstance().cancelPendingRequests(getVolleyTag());
        super.onDestroy();
    }

    @Override
    public String getVolleyTag() {
        return TAG +  this.hashCode();
    }
}
