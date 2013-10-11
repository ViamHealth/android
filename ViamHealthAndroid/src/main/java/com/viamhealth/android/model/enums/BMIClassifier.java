package com.viamhealth.android.model.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naren on 11/10/13.
 */
public enum BMIClassifier {
    VSUnderWeight(1, "Very Severy Underweight", 0.0, 14.99),
    SUnderweight(2, "Severy Underweight", 15.0, 15.99),
    Underweight(3, "Underweight", 16.0, 18.49),
    Normal(4, "Normal", 18.5, 24.99),
    Overweight(5, "Overweight", 25.0, 29.99),
    ModeratelyObese(6, "Moderately Obese", 30.0, 34.99),
    SeverelyObese(7, "Severyl Obese", 35.0, 39.99),
    VSeverelyObese(8, "Very Severely Obese", 40.0, 100);

    private final int value;
    private final String key;
    private final double min;
    private final double max;

    BMIClassifier(int value, String key, double min, double max){
        this.value = value;
        this.key = key;
        this.min = min;
        this.max = max;
    }

    public int value() {return value;}
    public String key() {return key;}
    public double min() {return min;}
    public double max() {return max;}

    // Lookup table
    private static final Map<Integer, BMIClassifier> valuelookup = new HashMap<Integer, BMIClassifier>();
    private static final Map<String, BMIClassifier> keylookup = new HashMap<String, BMIClassifier>();

    // Populate the lookup table on loading time
    static {
        for (BMIClassifier bmi : EnumSet.allOf(BMIClassifier.class)){
            valuelookup.put(bmi.value(), bmi);
            keylookup.put(bmi.key(), bmi);
        }
    }

    // This method can be used for reverse lookup purpose
    public static BMIClassifier get(int value) {
        return valuelookup.get(value);
    }

    public static BMIClassifier get(String key) {
        return keylookup.get(key);
    }

}
