package com.rayboot.sgr;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author rayboot
 * @from 14-5-14 8:53
 * @TODO
 */
public class StateView extends LinearLayout
{
    public static final int TYPE_LOADING = 0;
    public static final int TYPE_404 = 1;
    public static final int TYPE_NO_MSG = 2;
    public static final int TYPE_NO_NETWORK = 3;
    public static final int TYPE_NO_MORE_INFO = 4;
    public static final int TYPE_FINISH = 5;
    public static final int TYPE_NO_MORE_DATA = 6;
    public static final int TYPE_SELECT_ALL_TIME = 7;
    private TextView tvState;
    public ImageView ivState;
    public TextView btnRetry;

    public ViewGroup viewContain;
    public OnClickListener onClickListener;

    AnimationDrawable loadingDrawable;

    public StateView mInstance;


//    public StateView getInstance(Context context, ViewParent viewParent, OnClickListener onClickListener)
//    {
//        return getInstance(context, (ViewGroup) viewParent, onClickListener);
//    }
//    public StateView getInstance(Context context, ViewGroup viewContain, OnClickListener onClickListener)
//    {
//        return getInstance(context, viewContain, TYPE_LOADING, onClickListener);
//    }
//
//    public StateView getInstance(Context context, ViewGroup viewContain,
//            int initType, OnClickListener onClickListener)
//    {
//        if (mInstance == null)
//        {
//            mInstance = new StateView(context);
//        }
//        if (viewContain.equals(mInstance.getViewContain()))
//        {
//            if (viewContain != null)
//            {
//                mInstance.setState(initType);
//            }
//        }
//        else
//        {
//            mInstance.setViewContain(viewContain, initType);
//        }
//        this.onClickListener = onClickListener;
//        return mInstance;
//    }

    public StateView(Context context)
    {
        super(context);
        init();
    }

    public StateView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public StateView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public void setOnRetryClickListener(OnClickListener onClickListener)
    {
        this.onClickListener = onClickListener;
        btnRetry.setOnClickListener(onClickListener);
    }

    private void init()
    {
        inflate(getContext(), R.layout.view_state, this);
        this.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        tvState = (TextView) findViewById(R.id.tvStateTitle);
        ivState = (ImageView) findViewById(R.id.ivState);
        btnRetry = (TextView) findViewById(R.id.retry);

        btnRetry.setOnClickListener(this.onClickListener);
        loadingDrawable = (AnimationDrawable) ivState.getDrawable();
    }

    public TextView getStateViewTextView()
    {
        return tvState;
    }

    public void setTextInfo(String info)
    {
        tvState.setText(info);
    }

    public void setState(int state)
    {
        setVisibility(VISIBLE);
        tvState.setVisibility(VISIBLE);
        ivState.setVisibility(GONE);
        btnRetry.setVisibility(GONE);

        Drawable drawable = null;
        switch (state)
        {
        case TYPE_LOADING:
            loadingDrawable.start();
            ivState.setVisibility(VISIBLE);
            tvState.setCompoundDrawables(null, null, null, null);
            tvState.setText(R.string.state_loading_new);
            break;
        case TYPE_404:
            loadingDrawable.stop();
            drawable = getResources().getDrawable(R.drawable.state_404);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            tvState.setCompoundDrawables(null, drawable, null, null);
            tvState.setText(R.string.state_404);
            break;
        case TYPE_NO_MSG:
            loadingDrawable.stop();
            drawable = getResources().getDrawable(R.drawable.state_no_msg);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            tvState.setCompoundDrawables(null, drawable, null, null);
            tvState.setText(R.string.state_no_msg);
            break;
        case TYPE_NO_NETWORK:
            loadingDrawable.stop();
            drawable = getResources().getDrawable(R.drawable.state_no_network);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            tvState.setCompoundDrawables(null, drawable, null, null);
            tvState.setText(R.string.state_no_network);
            btnRetry.setVisibility(VISIBLE);
            break;
        case TYPE_NO_MORE_INFO:
            loadingDrawable.stop();
            drawable =
                    getResources().getDrawable(R.drawable.state_no_more_info);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            tvState.setCompoundDrawables(null, drawable, null, null);
            tvState.setText(R.string.state_no_more_info);
            break;
        case TYPE_NO_MORE_DATA:
            loadingDrawable.stop();
            drawable =
                    getResources().getDrawable(R.drawable.state_no_more_info);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            tvState.setCompoundDrawables(null, drawable, null, null);
            tvState.setText(R.string.state_no_more_data);
            break;
        case TYPE_FINISH:
            loadingDrawable.stop();
            setVisibility(GONE);
            break;

        case TYPE_SELECT_ALL_TIME:
            loadingDrawable.stop();
            drawable = getResources().getDrawable(R.drawable.state_select_all_time);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvState.setCompoundDrawables(null, drawable, null, null);
            tvState.setText(R.string.state_select_all_time);
            break;
        }
    }
}
