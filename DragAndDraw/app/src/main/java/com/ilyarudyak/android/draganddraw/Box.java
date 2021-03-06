package com.ilyarudyak.android.draganddraw;

import android.graphics.PointF;

import java.io.Serializable;

/**
 * Created by ilyarudyak on 2/9/16.
 */
public class Box implements Serializable {
    private PointF mOrigin;
    private PointF mCurrent;

    public Box(PointF origin) {
        mOrigin = origin;
        mCurrent = origin;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public PointF getOrigin() {
        return mOrigin;
    }
}
