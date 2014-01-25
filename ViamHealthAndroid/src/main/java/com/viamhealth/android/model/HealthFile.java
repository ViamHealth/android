package com.viamhealth.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kunal on 25/1/14.
 */
public class HealthFile extends BaseModel implements Parcelable {
    long userId;
    String name,description,download_url, mimeType, updatedByName;

    /*
    Standard basic constructor for non-parcel object creation
     */
    public HealthFile(){
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    @Override
    public String toString() {
        return "HealthFile{" +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", download_url=" + download_url +
                ", mimeType=" + mimeType +
                ", updatedByName=" + updatedByName +
                "} " + super.toString();
    }

    /*
    Purpose of describeContents
    http://stackoverflow.com/questions/4076946/parcelable-where-when-is-describecontents-used/4914799#4914799
    */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     * Btw, Parcel is not a general-purpose serialization mechanism.
     * @param dest 	The Parcel in which the object should be written.
     * @param flags 
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.download_url);
        dest.writeString(this.mimeType);
        dest.writeString(this.updatedByName);
    }

    /**
	 *
	 * Called from the constructor to create this
	 * object from a parcel.
	 *
	 * @param in parcel from which to re-create object
	 */
    public HealthFile(Parcel in){
        super(in);
        this.userId = in.readLong();
        this.name = in.readString();
        this.description = in.readString();
        this.download_url = in.readString();
        this.mimeType = in.readString();
        this.updatedByName = in.readString();
    }



    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays.
     *
     * This also means that you can use use the default
     * constructor to create the object and use another
     * method to hyrdate it as necessary.
     * I just find it easier to use the constructor. -
     * See more at: http://shri.blog.kraya.co.uk/2010/04/26/android-parcel-data-to-pass-between-activities-using-parcelable-classes/#sthash.cmNOQdxr.dpuf
     *
     */
    public static final Parcelable.Creator<HealthFile> CREATOR
            = new Parcelable.Creator<HealthFile>() {
        public HealthFile createFromParcel(Parcel in) {
            return new HealthFile(in);
        }

        public HealthFile[] newArray(int size) {
            return new HealthFile[size];
        }
    };


}
