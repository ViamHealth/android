package com.viamhealth.android.model.enums;

/**
 * Created by naren on 02/10/13.
 */
public enum BloodGroup {
    O_Positive(7),
    O_Negative(8),
    A_Positive(1),
    A_Negative(2),
    B_Positive(3),
    B_Negative(4),
    AB_Positive(5),
    AB_Negative(6);

    private final int value;

    BloodGroup(int value){
        this.value = value;
    }

    public int value() {return value;}

    public static BloodGroup formEnum(int value){
        switch(value){
            case 1:
                return A_Positive;

            case 2:
                return A_Negative;

            case 3:
                return B_Positive;

            case 4:
                return B_Negative;

            case 5:
                return AB_Positive;

            case 6:
                return AB_Negative;

            case 7:
                return O_Positive;

            case 8:
                return O_Negative;

        }
    }


}
