package com.sp.android_studio_project;

import android.graphics.Bitmap;

import com.google.firebase.firestore.auth.User;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class UserController {
    private String mImageUrl;

    public UserController() {
        //empty constructor needed
    }

    public UserController(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String ImageUrl) {
        this.mImageUrl = ImageUrl;
    }

}
