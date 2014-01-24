package com.viamhealth.android.model.cards;

import com.viamhealth.android.model.enums.PriorityCardType;

/**
 * Created by kunal on 11/1/14.
 */
public class PickGoalPriorityCard extends PriorityCard {

    private String weightGoalMessage;
    private String bpGoalMessage;

    //Y, N or blank.
    //@TODO: Use enums
    private String weightGoalCheck;
    private String bpGoalCheck;

    @Override
    public PriorityCardType getType() {
        return PriorityCardType.PICK_GOAL;
    }

    public void setWeightGoalMessage(String weightGoalMessage) {
        this.weightGoalMessage = weightGoalMessage;
    }

    public void setBpGoalMessage(String bpGoalMessage) {
        this.bpGoalMessage = bpGoalMessage;
    }

    public void setWeightGoalCheck(String weightGoalCheck) {
        this.weightGoalCheck = weightGoalCheck;
    }

    public String getWeightGoalMessage() {
        return weightGoalMessage;
    }

    public String getBpGoalMessage() {
        return bpGoalMessage;
    }

    public String getWeightGoalCheck() {
        return weightGoalCheck;
    }

    public String getBpGoalCheck() {
        return bpGoalCheck;
    }

    public void setBpGoalCheck(String bpGoalCheck) {
        this.bpGoalCheck = bpGoalCheck;
    }

    public String getCustomData(String type) {
        if (type == "weightGoalMessage") {
            return getWeightGoalMessage();
        } else if (type == "bpGoalMessage") {
            return getBpGoalMessage();
        } else if (type == "weightGoalCheck") {
            return getWeightGoalCheck();
        } else if (type == "bpGoalCheck") {
            return getBpGoalCheck();
        } else {
            throw new IllegalArgumentException("incorrect custom data type");
        }

    }

    public void setCustomData(String type, String value){
        if (type == "weightGoalMessage") {
            setWeightGoalMessage(value);
        } else if (type == "bpGoalMessage") {
            setBpGoalMessage(value);
        } else if (type == "weightGoalCheck") {
            setWeightGoalCheck(value);
        } else if (type == "bpGoalCheck") {
            setBpGoalCheck(value);
        } else {
            throw new IllegalArgumentException("incorrect custom data type");
        }
    }

}
