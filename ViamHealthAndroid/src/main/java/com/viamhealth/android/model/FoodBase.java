package com.viamhealth.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.model.enums.FoodType;

import java.util.Date;

/**
 * Created by naren on 31/10/13.
 */
public class FoodBase implements Parcelable {

    private long id;
    private long foodItemId;
    private long userId;
    private int foodQuantityMultiplier;
    private FoodType type = FoodType.None;
    private Date foodTakenOn;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(long foodItemId) {
        this.foodItemId = foodItemId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getFoodQuantityMultiplier() {
        return foodQuantityMultiplier;
    }

    public void setFoodQuantityMultiplier(int foodQuantityMultiplier) {
        this.foodQuantityMultiplier = foodQuantityMultiplier;
    }

    public FoodType getType() {
        return type;
    }

    public void setType(FoodType type) {
        this.type = type;
    }

    public Date getFoodTakenOn() {
        return foodTakenOn;
    }

    public void setFoodTakenOn(Date foodTakenOn) {
        this.foodTakenOn = foodTakenOn;
    }

    @Override
    public String toString() {
        return "FoodBase{" +
                "id=" + id +
                ", foodItemId=" + foodItemId +
                ", userId=" + userId +
                ", foodQuantityMultiplier=" + foodQuantityMultiplier +
                ", type=" + type +
                ", foodTakenOn=" + foodTakenOn +
                "} " + super.toString();
    }

    public FoodBase() {
    }

    public FoodBase(Parcel in) {
        id = in.readLong();
        foodItemId = in.readLong();
        userId = in.readLong();
        foodQuantityMultiplier = in.readInt();
        type = (FoodType)in.readValue(null);
        foodTakenOn = (Date)in.readValue(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(foodItemId);
        dest.writeLong(userId);
        dest.writeInt(foodQuantityMultiplier);
        dest.writeValue(type);
        dest.writeValue(foodTakenOn);
    }
}
