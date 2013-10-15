package com.viamhealth.android.model.goals;

/**
 * Created by naren on 11/10/13.
 */
public class WeightGoal extends Goal {
    Double weight;

    public WeightGoal() {
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
        return "WeightGoal{" +
                "weight=" + weight +
                "} " + super.toString();
    }
}
