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

    public boolean isEmpty() {
        if(height==0)
            return true;

        else
            return false;
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
                "} " + super.toString();
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
    }

    public BMIProfile(Parcel in) {
        this.height = in.readInt();
        this.weight = in.readDouble();
        this.bmr = in.readInt();
        //this.bmiClassifier = (BMIClassifier) in.readValue(null);
        //this.lifeStyle = (LifeStyle) in.readValue(null);
    }

    public BMIProfile() {

    }
}
