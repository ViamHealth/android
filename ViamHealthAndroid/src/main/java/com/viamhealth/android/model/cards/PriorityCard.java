package com.viamhealth.android.model.cards;

import android.os.Parcelable;

import com.viamhealth.android.model.BaseModel;
import com.viamhealth.android.model.enums.PriorityCardType;

/**
 * Created by kunal on 10/1/14.
 */
public class PriorityCard extends BaseModel implements Parcelable {

    private PriorityCardType type;

    public  PriorityCardType getType(){
        return type;
    }

    public void setType(PriorityCardType type) {
        this.type = type;
    }

}
