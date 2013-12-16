package com.viamhealth.android.dao.db.contractors;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.viamhealth.android.model.enums.BMIClassifier;
import com.viamhealth.android.model.enums.BloodGroup;
import com.viamhealth.android.model.enums.Gender;
import com.viamhealth.android.model.enums.LifeStyle;
import com.viamhealth.android.model.enums.Relation;
import com.viamhealth.android.model.users.BMIProfile;
import com.viamhealth.android.model.users.Profile;

import java.util.Date;

/**
 * Created by naren on 13/12/13.
 */
public class UserContract extends BaseContract {

    /**
     * The content URI for the top-level
     * goals authority.
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String TABLE_NAME = "users";

    public static final class User implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(UserContract.CONTENT_URI, "users");
        public static final Uri CONTENT_USER_ITEM_URI = Uri.withAppendedPath(User.CONTENT_URI, "/#");

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/viamhealth.android.users.meta";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/viamhealth.android.users.entry";

        public static final String EMAIL = "email";
        public static final String MOBILE = "mobile";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String IS_LOGGED_IN_USER = "is_logged_in_user";

        public static final String[] PROJECTION_ALL = {_ID, EMAIL, MOBILE, FIRST_NAME, LAST_NAME, IS_LOGGED_IN_USER};
    }

    public static final class Profile implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(UserContract.CONTENT_URI, "users/#/profile");
        //public static final Uri CONTENT_USER_ITEM_URI = Uri.withAppendedPath(User.CONTENT_URI, "/#");

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/viamhealth.android.users.profile.meta";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/viamhealth.android.users.profile.entry";

        public static final String USER_ID = "user_id";
        public static final String DOB = "dob";
        public static final String GENDER = "gender";
        public static final String PIC_URL = "pic_url";
        public static final String ORGANIZATION = "organization";
        public static final String MOBILE_NUMBER = "mobile_number";
        public static final String LOCATION = "location";
        public static final String FB_PROFILE_ID = "fb_profile_id";
        public static final String FB_USERNAME = "fb_username";
        public static final String RELATION = "relation";

        public static final String[] PROJECTION_ALL = {_ID, USER_ID, DOB, GENDER, PIC_URL, ORGANIZATION, MOBILE_NUMBER,
                                                       LOCATION, FB_PROFILE_ID, FB_USERNAME, RELATION};
    }

    public static final class BMIProfile implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(UserContract.CONTENT_URI, "users/#/bmiprofile");

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/viamhealth.android.users.bmiprofile.meta";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/viamhealth.android.users.bmiprofile.entry";

        public static final String USER_ID = "user_id";
        public static final String HEIGHT = "height";
        public static final String WEIGHT = "weight";
        public static final String LIFESTYLE = "lifestyle";
        public static final String BMI_CLASSIFIER = "bmi_classifier";
        public static final String BMR = "bmr";
        public static final String SYSTOLIC_PRESSURE = "systolic_pressure";
        public static final String DIASTOLIC_PRESSURE = "diastolic_pressure";
        public static final String PULSE_RATE = "pulse_rate";
        public static final String RANDOM_SUGAR = "random_sugar";
        public static final String FASTING_SUGAR = "fasting_sugar";
        public static final String HDL = "hdl";
        public static final String LDL = "ldl";
        public static final String TRIGLYCERIDES = "triglycerides";
        public static final String TOTAL_CHOLESTEROL = "total_cholesterol";

        public static final String[] PROJECTION_ALL = {_ID, USER_ID, HEIGHT, WEIGHT, LIFESTYLE, BMI_CLASSIFIER, BMR,
                                                        SYSTOLIC_PRESSURE, DIASTOLIC_PRESSURE, PULSE_RATE, RANDOM_SUGAR,
                                                        FASTING_SUGAR, HDL, LDL, TRIGLYCERIDES, TOTAL_CHOLESTEROL};
    }

}
