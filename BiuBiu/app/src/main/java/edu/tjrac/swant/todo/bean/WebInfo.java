package edu.tjrac.swant.todo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by wpc on 2018/5/15.
 */

public class WebInfo implements Parcelable{
    String url;
    String host_icon_url;
    String content_image_url;
    String title;
    String describution;

    boolean cached,cache;

    public WebInfo(String url, String title) {
        this.url = url;
        this.title = title;
    }

    protected WebInfo(Parcel in) {
        url = in.readString();
        host_icon_url = in.readString();
        content_image_url = in.readString();
        title = in.readString();
        describution = in.readString();
        cached = in.readByte() != 0;
        cache = in.readByte() != 0;
    }

    public static final Creator<WebInfo> CREATOR = new Creator<WebInfo>() {
        @Override
        public WebInfo createFromParcel(Parcel in) {
            return new WebInfo(in);
        }

        @Override
        public WebInfo[] newArray(int size) {
            return new WebInfo[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost_icon_url() {
        return host_icon_url;
    }

    public void setHost_icon_url(String host_icon_url) {
        this.host_icon_url = host_icon_url;
    }

    public String getContent_image_url() {
        return content_image_url;
    }

    public void setContent_image_url(String content_image_url) {
        this.content_image_url = content_image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribution() {
        return describution;
    }

    public void setDescribution(String describution) {
        this.describution = describution;
    }

    public boolean isCached() {
        return cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(host_icon_url);
        dest.writeString(content_image_url);
        dest.writeString(title);
        dest.writeString(describution);
        dest.writeByte((byte) (cached ? 1 : 0));
        dest.writeByte((byte) (cache ? 1 : 0));
    }
}
