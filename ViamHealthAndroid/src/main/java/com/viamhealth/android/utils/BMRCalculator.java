package com.viamhealth.android.utils;

import com.viamhealth.android.model.enums.Gender;
import com.viamhealth.android.model.goals.WeightGoal;
import com.viamhealth.android.model.users.User;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Date;

/**
 * Created by naren on 26/10/13.
 */
public class BMRCalculator {

    public static int calculateBMR(double weight, int height, int age, Gender gender){
        Double p = 10*weight + 6.25*height - 5*age;
        if(gender==Gender.Male)
            p += 5;
        else
            p -= 161;

        return p.intValue();
    }

    public static Date getIdealTargetDate(User user, WeightGoal goal) {
        if(user==null || goal==null)
            return null;


    }

    public static int getCaloriesToBeReducedPerDay(WeightGoal goal) {

        DateTime targetDate = new DateTime(goal.getTargetDate().getTime());
        DateTime now = new DateTime();
        Days noOfDays = Days.daysBetween(now, targetDate);
        int daysToReachTarget = noOfDays.getDays();
        int weightToReduceInKgs = goal.getWeight()

        int caloriesPerKg = 7000;

        int totalCaloriesToBeReducedPerDay = weightToReduceInKgs * caloriesPerKg / daysToReachTarget;

        return totalCaloriesToBeReducedPerDay;

    }

    private void updateTargetCalories(User user, WeightGoal goal) {

    }

}
