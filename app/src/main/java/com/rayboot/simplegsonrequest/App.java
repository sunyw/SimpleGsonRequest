package com.rayboot.simplegsonrequest;

import android.app.Application;

import com.rayboot.sgr.SgrVolley;

/**
 * Created by rayboot on 15/4/30.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SgrVolley.Init(this);
    }
}
