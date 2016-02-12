package com.ilyarudyak.android.locatr;

import com.google.gson.annotations.SerializedName;

public class GalleryItem {
    @SerializedName("title")
    private String mCaption;
    @SerializedName("id")
    private String mId;
    @SerializedName("url_s")
    private String mUrl;

    public String getUrl() {
        return mUrl;
    }
    public void setUrl(String url) {
        mUrl = url;
    }

    public String getCaption() {
        return mCaption;
    }
    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getId() {
        return mId;
    }
    public void setId(String id) {
        mId = id;
    }

    @Override
    public String toString() {
        return mCaption;
    }
}
