package com.sxshi.android.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sxshi on 2018-2-12.
 * 建立的测试数据
 */

public class News implements Parcelable {
    private String imageUrl;
    private String detialUrl;
    private String newsDetial;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNewsDetial() {
        return newsDetial;
    }

    public void setNewsDetial(String newsDetial) {
        this.newsDetial = newsDetial;
    }

    public String getDetialUrl() {
        return detialUrl;
    }

    public void setDetialUrl(String detialUrl) {
        this.detialUrl = detialUrl;
    }

    @Override
    public String toString() {
        return "News{" +
                "imageUrl='" + imageUrl + '\'' +
                ", detialUrl='" + detialUrl + '\'' +
                ", newsDetial='" + newsDetial + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.detialUrl);
        dest.writeString(this.newsDetial);
    }

    public News() {
    }

    protected News(Parcel in) {
        this.imageUrl = in.readString();
        this.detialUrl = in.readString();
        this.newsDetial = in.readString();
    }

    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel source) {
            return new News(source);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
}
