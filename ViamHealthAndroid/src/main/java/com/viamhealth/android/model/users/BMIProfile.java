package com.viamhealth.android.model.users;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.model.enums.BMIClassifier;
import com.viamhealth.android.model.enums.Gender;
import com.viamhealth.android.model.enums.LifeStyle;

/**
 * Created by naren on 11/10/13.
 */
public class BMIProfile implements Parcelable {

    int height;
    double weight;
    LifeStyle lifeStyle;
    BMIClassifier bmiClassifier;
    int bmr;

    int systolicPressure;
    int diastolicPressure;
    int pulseRate;
    int randomSugar;
    int fastingSugar;
    int hdl;
    int ldl;
    int triglycerides;
    int totalCholesterol;

    public boolean isEmpty() {
        if(height==0)
            return true;

        else
            return false;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getSystolicPressure() {
        return systolicPressure;
    }

    public void setSystolicPressure(int systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public int getDiastolicPressure() {
        return diastolicPressure;
    }

    public void setDiastolicPressure(int diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public int getPulseRate() {
        return pulseRate;
    }

    public void setPulseRate(int pulseRate) {
        this.pulseRate = pulseRate;
    }

    public int getRandomSugar() {
        return randomSugar;
    }

    public void setRandomSugar(int randomSugar) {
        this.randomSugar = randomSugar;
    }

    public int getFastingSugar() {
        return fastingSugar;
    }

    public void setFastingSugar(int fastingSugar) {
        this.fastingSugar = fastingSugar;
    }

    public int getHdl() {
        return hdl;
    }

    public void setHdl(int hdl) {
        this.hdl = hdl;
    }

    public int getLdl() {
        return ldl;
    }

    public void setLdl(int ldl) {
        this.ldl = ldl;
    }

    public int getTriglycerides() {
        return triglycerides;
    }

    public void setTriglycerides(int triglycerides) {
        this.triglycerides = triglycerides;
    }

    public int getTotalCholesterol() {
        return totalCholesterol;
    }

    public void setTotalCholesterol(int totalCholesterol) {
        this.totalCholesterol = totalCholesterol;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }


    public LifeStyle getLifeStyle() {
        return lifeStyle;
    }

    public void setLifeStyle(LifeStyle lifeStyle) {
        this.lifeStyle = lifeStyle;
    }

    public BMIClassifier getBmiClassifier() {
        return bmiClassifier;
    }

    public void setBmiClassifier(BMIClassifier bmiClassifier) {
        this.bmiClassifier = bmiClassifier;
    }

    public int getBmr() {
        return bmr;
    }

    public void setBmr(int bmr) {
        this.bmr = bmr;
    }

    @Override
    public String toString() {
        return "BMIProfile{" +
                "height=" + height +
                ", weight=" + weight +
                ", lifeStyle=" + lifeStyle +
                ", bmiClassifier=" + bmiClassifier +
                ", bmr=" + bmr +
                ", systolicPressure=" + systolicPressure +
                ", diastolicPressure=" + diastolicPressure +
                ", pulseRate=" + pulseRate +
                ", randomSugar=" + randomSugar +
                ", fastingSugar=" + fastingSugar +
                ", hdl=" + hdl +
                ", ldl=" + ldl +
                ", triglycerides=" + triglycerides +
                ", totalCholesterol=" + totalCholesterol +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.height);
        dest.writeDouble(this.weight);
        dest.writeInt(this.bmr);
        //dest.writeValue(this.bmiClassifier);
        //dest.writeValue(this.lifeStyle);
        dest.writeInt(systolicPressure);
        dest.writeInt(diastolicPressure);
        dest.writeInt(pulseRate);
        dest.writeInt(fastingSugar);
        dest.writeInt(randomSugar);
        dest.writeInt(hdl);
        dest.writeInt(ldl);
        dest.writeInt(triglycerides);
        dest.writeInt(totalCholesterol);
    }

    public BMIProfile(Parcel in) {
        this.height = in.readInt();
        this.weight = in.readDouble();
        this.bmr = in.readInt();
        //this.bmiClassifier = (BMIClassifier) in.readValue(null);
        //this.lifeStyle = (LifeStyle) in.readValue(null);
        systolicPressure = in.readInt();
        diastolicPressure = in.readInt();
        pulseRate = in.readInt();
        fastingSugar = in.readInt();
        randomSugar = in.readInt();
        hdl = in.readInt();
        ldl = in.readInt();
        triglycerides = in.readInt();
        totalCholesterol = in.readInt();
    }

    public BMIProfile() {

    }
}
