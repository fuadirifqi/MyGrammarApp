package com.fuadirifqi.android.mygrammarapp;

import java.util.Date;
import java.util.UUID;

/**
 * Created by fuadirifqi on 5/16/16.
 */
public class Grammar {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mValid;

    private String mSuspect;

    public Grammar(){

        this(UUID.randomUUID());

    }

    public Grammar(UUID id){

        mId = id;
        mDate = new Date();

    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isValid() {
        return mValid;
    }

    public void setValid(boolean valid) {
        mValid = valid;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String getPhotoFilename(){
        return "IMG_"+ getId().toString() + ".jpg";
    }
}
