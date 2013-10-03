package com.viamhealth.android.model.enums;

/**
 * Created by naren on 02/10/13.
 */
public enum Sex {
    Male(1),
    Female(2);

    private final int value;

    Sex(int value){
        this.value = value;
    }

    private int value() {return value;}

}
