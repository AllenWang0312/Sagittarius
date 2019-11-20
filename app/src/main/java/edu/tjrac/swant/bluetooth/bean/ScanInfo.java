package edu.tjrac.swant.bluetooth.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wpc on 2018/2/2 0002.
 */

public class ScanInfo implements Parcelable {


    public ScanInfo(long tagTime, int dbm, long lastTagTime) {
        this.tagTime = tagTime;
        this.dbm = dbm;
        this.lastTagTime = lastTagTime;
        advTime = tagTime - lastTagTime;
    }

    long tagTime;
    int dbm;
    long lastTagTime;
    long advTime;


    protected ScanInfo(Parcel in) {
        tagTime = in.readLong();
        dbm = in.readInt();
        lastTagTime = in.readLong();
    }

    public long getAdvTime() {
        return advTime;
    }

    public void setAdvTime(long advTime) {
        this.advTime = advTime;
    }

    public static final Creator<ScanInfo> CREATOR = new Creator<ScanInfo>() {
        @Override
        public ScanInfo createFromParcel(Parcel in) {
            return new ScanInfo(in);
        }

        @Override
        public ScanInfo[] newArray(int size) {
            return new ScanInfo[size];
        }
    };

    public long getTagTime() {
        return tagTime;
    }

    public void setTagTime(long tagTime) {
        this.tagTime = tagTime;
    }

    public int getDbm() {
        return dbm;
    }

    public void setDbm(int dbm) {
        this.dbm = dbm;
    }

    public long getLastTagTime() {
        return lastTagTime;
    }

    public void setLastTagTime(long lastTagTime) {
        this.lastTagTime = lastTagTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(tagTime);
        dest.writeInt(dbm);
        dest.writeLong(lastTagTime);
    }
}
