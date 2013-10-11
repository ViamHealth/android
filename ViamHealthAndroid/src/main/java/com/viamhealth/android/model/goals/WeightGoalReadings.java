package com.viamhealth.android.model.goals;

/**
 * Created by naren on 11/10/13.
 */
public class WeightGoalReadings extends GoalReadings{

    Double weight;

    public WeightGoalReadings() {
        super();
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "WeightGoalReadings{" +
                "weight=" + weight +
                "} " + super.toString();
    }
}
