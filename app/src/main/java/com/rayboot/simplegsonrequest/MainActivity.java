package com.rayboot.simplegsonrequest;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.rayboot.sgr.GsonRequest;
import com.rayboot.sgr.Sgr;
import com.rayboot.sgr.stateview.StateView;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StateView stateView = (StateView) findViewById(R.id.stateView);
        stateView.setOnRetryListener(new StateView.RetryListener() {
            @Override
            public void onRetry() {
                Toast.makeText(MainActivity.this, "test retry", Toast.LENGTH_SHORT).show();
            }
        });

        Sgr.builder(this, BaseModule.class).stateView(stateView).url("http://timefaceapi.timeface.cn/timefaceapi/v2/time/timelist").finishListener(new GsonRequest.FinishListener<BaseModule>() {
            @Override
            public void onFinishResponse(boolean isSuccess, BaseModule response, VolleyError error) {


            }
        }).post2Queue();
    }

}
