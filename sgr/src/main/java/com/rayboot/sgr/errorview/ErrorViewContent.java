package com.rayboot.sgr.errorview;

import android.text.TextUtils;

/**
 * Created by rayboot on 15/4/29.
 */
public class ErrorViewContent {
    private int imgRes;
    private String title;
    private String subTitle;
    private String btnTitle;
    private int btnRes;

    public static ErrorViewContent getContentObj(int state) {
        ErrorViewContent content ;
        switch (state) {
            case HttpStatusCodes.FINISH:
                return null;
            case HttpStatusCodes.NO_CONNECT:
                return new ErrorViewContent()

        }
    }

    public ErrorViewContent(int imgRes, String title, String subTitle, String btnTitle, int btnRes) {
        this.title = title;
        this.imgRes = imgRes;
        this.subTitle = subTitle;
        this.btnTitle = btnTitle;
        this.btnRes = btnRes;
    }

    public boolean haveTitle() {
        return !TextUtils.isEmpty(title);
    }

    public boolean haveSubTitle() {
        return !TextUtils.isEmpty(subTitle);
    }

    public boolean haveImg() {
        return imgRes > 0;
    }

    public boolean haveButton() {
        return !TextUtils.isEmpty(btnTitle) || btnRes > 0;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getBtnTitle() {
        return btnTitle;
    }

    public void setBtnTitle(String btnTitle) {
        this.btnTitle = btnTitle;
    }

    public int getBtnRes() {
        return btnRes;
    }

    public void setBtnRes(int btnRes) {
        this.btnRes = btnRes;
    }
}
