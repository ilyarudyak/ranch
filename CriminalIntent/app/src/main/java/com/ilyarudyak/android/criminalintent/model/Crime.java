package com.ilyarudyak.android.criminalintent.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by ilyarudyak on 01/12/14.
 */
public class Crime {

    private UUID mId;
    private Date mDate;
    private boolean mSolved;
    private String mTitle;

    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
