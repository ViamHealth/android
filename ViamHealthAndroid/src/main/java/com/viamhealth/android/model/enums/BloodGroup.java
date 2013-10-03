package com.viamhealth.android.model.enums;

/**
 * Created by naren on 02/10/13.
 */
public enum BloodGroup {
    O_Positive(1),
    O_Negative(2),
    A_Positive(3),
    A_Negative(4),
    B_Positive(5),
    B_Negative(6),
    AB_Positive(7),
    AB_Negative(8);

    private final int value;

    BloodGroup(int value){
        this.value = value;
    }

    private int value() {return value;}

}
