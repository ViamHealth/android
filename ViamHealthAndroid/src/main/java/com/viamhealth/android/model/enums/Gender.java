package com.viamhealth.android.model.enums;

/**
 * Created by naren on 02/10/13.
 */
public enum Gender {
    Male(1, "MALE"),
    Female(2, "FEMALE");

    private final int value;
    private final String key;

    Gender(int value, String key){
        this.value = value;
        this.key = key;
    }

    public int value() {return value;}
    public String key() {return key;}

    public static formEnum(String key){
        switch(key){
            case "MALE":
                return Gender.Male;

            case "FEMALE":
                return Gender.Female;
        }
    }

    public static formEnum(int value){
        switch(value){
            case 1:
                return Gender.Male;

            case 2:
                return Gender.Female;
        }
    }
}
