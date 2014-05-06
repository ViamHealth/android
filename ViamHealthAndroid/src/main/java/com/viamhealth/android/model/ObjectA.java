package com.viamhealth.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * public class ObjectA implements Parcelable {
 * public byte[] bytearray;
 * /** * Standard basic constructor for non-parcel * object creation
 */
public class ObjectA implements Parcelable {
    public final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {

                public ObjectA createFromParcel(Parcel in) {
                    return new ObjectA(in);
                }

                public ObjectA[] newArray(int size) {
                    return new ObjectA[size];
                }
            };
    public byte[] bytearray;
    public byte[] readByteArray;

    ;

    /**
     * Standard basic constructor for non-parcel * object creation
     */
    public ObjectA() {
        ;
    }

    /**
     * * Constructor to use when re-constructing object * from a parcel
     * * * @param in a parcel from which to read this object
     */

    public ObjectA(Parcel in) {
        readFromParcel(in);
    }

    public byte[] getBytearray() {
        return bytearray;
    }

    public void setByteValue(byte[] bytearray) {
        this.bytearray = bytearray;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bytearray.length);
        dest.writeByteArray(bytearray);
    }

    private void readFromParcel(Parcel in) {
        // We just need to read back each // field in the order that it was // written to the parcel
        readByteArray = new byte[in.readInt()];
        in.readByteArray(readByteArray);

        //in.readByteArray(readByteArray,ObjectA.class.getClassLoader());
    }
}