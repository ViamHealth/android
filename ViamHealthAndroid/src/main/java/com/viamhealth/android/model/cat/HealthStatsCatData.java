package com.viamhealth.android.model.cat;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.model.enums.CatAdapterType;
import com.viamhealth.android.model.enums.CatType;
import com.viamhealth.android.model.healthreadings.BloodPressureReading;
import com.viamhealth.android.model.healthreadings.WeightReading;

import java.util.Date;

/**
 * Created by Kunal on 19/6/14.
 */
public class HealthStatsCatData implements  Parcelable, CatData {
    final Integer catAdapterType = CatAdapterType.HealthStats.value();
    final Integer catType = CatType.HealthStats.value();
    private Date readingDate;
    private WeightReading weightReading;
    private BloodPressureReading bloodPressureReading;

    @Override
    public Integer getCatType() {
        return this.catType;
    }

    @Override
    public Integer getCatAdapterType() {
        return this.catAdapterType;
    }

    @Override
    public Date getStartDate() {
        return this.getReadingDate();
    }

    @Override
    public Date getEndDate() {
        return this.getReadingDate();
    }

    public Boolean isSetWeightReading(){
        if (this.weightReading == null ){
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    public Boolean isSetBloodPressureReading(){
        if (this.bloodPressureReading == null ){
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    public Date getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(Date readingDate) {
        this.readingDate = readingDate;
    }

    public WeightReading getWeightReading() {
        return weightReading;
    }

    public void setWeightReading(WeightReading weightReading) {
        this.weightReading = weightReading;
    }

    public BloodPressureReading getBloodPressureReading() {
        return bloodPressureReading;
    }

    public void setBloodPressureReading(BloodPressureReading bloodPressureReading) {
        this.bloodPressureReading = bloodPressureReading;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeLong(this.getReadingDate().getTime());
        this.weightReading.writeToParcel(dest, i);
        this.bloodPressureReading.writeToParcel(dest, i);
    }

    public HealthStatsCatData(Parcel in) {
        setReadingDate(new Date(in.readLong()));
        this.weightReading = new WeightReading(in);
        this.bloodPressureReading = new BloodPressureReading(in);
    }

    public static final Parcelable.Creator<HealthStatsCatData> CREATOR
            = new Parcelable.Creator<HealthStatsCatData>() {
        public HealthStatsCatData createFromParcel(Parcel in) {
            return new HealthStatsCatData(in);
        }

        public HealthStatsCatData[] newArray(int size) {
            return new HealthStatsCatData[size];
        }
    };

    public HealthStatsCatData() {
        //this.weightReading = new WeightReading();
        //this.bloodPressureReading = new BloodPressureReading();
    }

}
