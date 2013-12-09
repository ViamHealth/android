package com.viamhealth.android.utils;

import com.viamhealth.android.model.enums.Gender;
import com.viamhealth.android.model.goals.WeightGoal;
import com.viamhealth.android.model.users.User;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Calendar;
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

        int idealCaloriesPerDay = user.getProfile().getGender()==Gender.Male?1500:1200;

        int totalCalories = calculateBMR(user.getBmiProfile().getWeight(), user.getBmiProfile().getHeight(),
                user.getProfile().getAge(), user.getProfile().getGender());


        int targetCaloriesPerDay = totalCalories - idealCaloriesPerDay;

        int daysRequired = getDaysToReachTargetWeight(goal.getWeight() - user.getBmiProfile().getWeight(), targetCaloriesPerDay);

        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, daysRequired);

        return now.getTime();
    }

    public static int getDaysToReachTargetWeight(double weightToReduce, int maxCaloriesReduciblePerDay) {
        int caloriesPerKg = 7000;
        Double daysToReachTarget = weightToReduce * caloriesPerKg / maxCaloriesReduciblePerDay;
        return daysToReachTarget.intValue();
    }

    public static int getCaloriesToBeReducedPerDay(double weightToReduce, Date targetDate) {

        DateTime targetDateTime = new DateTime(targetDate.getTime());
        DateTime now = new DateTime();
        Days noOfDays = Days.daysBetween(targetDateTime, now);
        int daysToReachTarget = Math.abs(noOfDays.getDays());

        int caloriesPerKg = 7000;

        Double totalCaloriesToBeReducedPerDay = weightToReduce * caloriesPerKg / daysToReachTarget;

        return totalCaloriesToBeReducedPerDay.intValue();

    }
}
