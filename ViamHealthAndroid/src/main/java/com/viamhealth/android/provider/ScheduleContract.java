package com.viamhealth.android.provider;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.text.TextUtils;

/**
 * Contract class for interacting with {@link ScheduleProvider}. Unless
 * otherwise noted, all time-based fields are milliseconds since epoch and can
 * be compared against {@link System#currentTimeMillis()}.
 * <p>
 * The backing {@link android.content.ContentProvider} assumes that {@link android.net.Uri}
 * are generated using stronger {@link String} identifiers, instead of
 * {@code int} {@link android.provider.BaseColumns#_ID} values, which are prone to shuffle during
 * sync.
 */

public class ScheduleContract {

    /**
     * Special value for {@link SyncColumns#UPDATED} indicating that an entry
     * has never been updated, or doesn't exist yet.
     */
    public static final long UPDATED_NEVER = -2;

    /**
     * Special value for {@link SyncColumns#UPDATED} indicating that the last
     * update time is unknown, usually when inserted from a local file source.
     */
    public static final long UPDATED_UNKNOWN = -1;

    public enum SyncStatus {
        None,
        PENDING_UPDATE,
        POST_INITIATED,
        PUT_INITIATED,
        DELETE_INITIATED,
        SUCCESS,
        OVERRIDEN;
    }

    public interface SyncColumns {
        /** Last time this entry was updated on the server **/
        String UPDATED = "updated";
        /** Last time this entry was synchronized from the server **/
        String SYNCHRONIZED = "synchronized";
        /** SyncStatus **/
        String SYNC_STATUS = "sync_status";
        /** table name **/
        String SYNC_TABLE_URI = "sync_table";
        /** soft delete **/
        String IS_DELETED = "is_deleted";
    }

    public interface UserColumns {
        String EMAIL = "email";
        String MOBILE = "mobile";
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String IS_LOGGED_IN_USER = "is_logged_in_user";
        String USER_NAME = "username";
    }

    public interface UserForeignKeyColumn {
        String USER_ID = "user_id";
    }

    public interface UserProfileColumns {
        String DOB = "dob";
        String GENDER = "gender";
        String PIC_URL = "pic_url";
        String ORGANIZATION = "organization";
        String MOBILE_NUMBER = "mobile_number";
        String LOCATION = "location";
        String FB_PROFILE_ID = "fb_profile_id";
        String FB_USERNAME = "fb_username";
        String RELATION = "relation";
        String BLOOD_GROUP = "blood_group";
    }

    public interface UserHealthProfileColumns {
        String HEIGHT = "height";
        String WEIGHT = "weight";
        String LIFESTYLE = "lifestyle";
        String BMI_CLASSIFIER = "bmi_classifier";
        String BMR = "bmr";
        String SYSTOLIC_PRESSURE = "systolic_pressure";
        String DIASTOLIC_PRESSURE = "diastolic_pressure";
        String PULSE_RATE = "pulse_rate";
        String RANDOM_SUGAR = "random_sugar";
        String FASTING_SUGAR = "fasting_sugar";
        String HDL = "hdl";
        String LDL = "ldl";
        String TRIGLYCERIDES = "triglycerides";
        String TOTAL_CHOLESTEROL = "total_cholesterol";
    }

    public interface RemindersColumns {
        String TYPE = "type";
        String NAME = "name";
        String DETAILS = "details";
        String MORNING_COUNT = "morning_count";
        String AFTERNOON_COUNT = "afternoon_count";
        String EVENING_COUNT = "evening_count";
        String NIGHT_COUNT = "night_count";
        String START_DATE = "start_date";
        String END_DATE = "end_date";
        String REPEAT_MODE = "repeat_mode";
        String REPEAT_DAY = "repeat_day";
        String REPEAT_HOUR = "repeat_hour";
        String REPEAT_MIN = "repeat_min";
        String REPEAT_WEEKDAY = "repeat_weekday";
        String REPEAT_EVERY_X = "repeat_every_x";
        String REPEAT_I_COUNTER = "repeat_i_counter";
        String CREATED_AT = "created_at";
        String UPDATED_AT = "updated_at";
    }

    public interface ReminderForeignKeyColumn {
        String USER_ID = "user_id";
    }

    public interface ReminderReadingsColumns {
        String REMINDER_ID= "reminder_id";
        String MORNING_CHECK = "morning_check";
        String AFTERNOON_CHECK = "afternoon_check";
        String EVENING_CHECK = "evening_check";
        String NIGHT_CHECK = "night_check";
        String COMPLETE_CHECK = "complete_check";
        String UPDATED_BY = "updated_by";
        String READING_DATE = "reading_date";
    }




    public static final String CONTENT_AUTHORITY = "com.viamhealth.android.schedule";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_LAST_SYNC_TIME = "lastSyncTime";
    public static final String PATH_DATA_TO_BE_UPDATED = "dataToBeUpdated";

    public static final String PATH_SYNCHRONIZED = "synchronized";
    public static final String PATH_USERS = "user";
    public static final String PATH_PROFILE = "profile";
    public static final String PATH_HEALTH_PROFILE = "healthprofile";

    public static final String PATH_REMINDER = "reminder";
    public static final String PATH_REMINDERS = "reminders";
    public static final String PATH_REMINDER_READINGS = "reminderreadings";

    public static class Synchronize implements BaseColumns, SyncColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SYNCHRONIZED).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.viamhealth.sync";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.viamhealth.sync";
    }


    public static class Reminders implements RemindersColumns, ReminderReadingsColumns, SyncColumns,
            BaseColumns,ReminderForeignKeyColumn {
        public static final Uri CONTENT_REMINDER_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REMINDER).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.viamhealth.reminders";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.viamhealth.reminders";

        public static final String TABLE_ALIAS = "r";

        public static Uri buildReminderUri() {

            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_REMINDER).build();
        }

        public static Uri buildReminderReadingsUri() {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_REMINDER_READINGS).build();
        }

        public static Integer getUserId(Uri uri) {
            String str = uri.getPathSegments().get(1);
            if(str.contains("#"))
                return null;
            return Integer.parseInt(str);
        }

    }



    public static class Users implements UserColumns, UserProfileColumns, UserHealthProfileColumns, UserForeignKeyColumn, SyncColumns,BaseColumns {
        public static final Uri CONTENT_USER_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USERS).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.viamhealth.user";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.viamhealth.user";

        public static final String TABLE_ALIAS = "u";

        public static Uri buildUserUri(Long userId) {
            if(userId==null || userId==0)
                return CONTENT_USER_URI;

            return CONTENT_USER_URI.buildUpon().appendPath(userId.toString()).build();
        }

        public static Uri buildUserProfileUri(Long userId, boolean isNegative) {
            String str = "#";
            if(isNegative)
                str = "-" + str;
            if(userId==0)
                return CONTENT_USER_URI.buildUpon().appendPath(str).appendPath(PATH_PROFILE).build();
            return buildUserUri(userId).buildUpon().appendPath(PATH_PROFILE).build();
        }

        public static Uri buildUserHealthProfileUri(Long userId, boolean isNegative) {
            String str = "#";
            if(isNegative)
                str = "-" + str;
            if(userId==0)
                return CONTENT_USER_URI.buildUpon().appendPath(str).appendPath(PATH_HEALTH_PROFILE).build();
            return buildUserUri(userId).buildUpon().appendPath(PATH_HEALTH_PROFILE).build();
        }

        public static Integer getUserId(Uri uri) {
            String str = uri.getPathSegments().get(1);
            if(str.contains("#"))
                return null;
            return Integer.parseInt(str);
        }

    }

    public static boolean hasCallerIsSyncAdapterParameter(Uri uri) {
        return TextUtils.equals("true",
                uri.getQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER));
    }

    public static Uri addCallerIsSyncAdapterParameter(Uri uri) {
        return uri.buildUpon().appendQueryParameter(
                ContactsContract.CALLER_IS_SYNCADAPTER, "true").build();
    }

    public static Uri getLastSyncedTimeUri(Uri uri) {
        return uri.buildUpon().appendPath(PATH_LAST_SYNC_TIME).build();
    }

    public static Uri getDataToBeUpdatedUri(Uri uri) {
        return uri.buildUpon().appendPath(PATH_DATA_TO_BE_UPDATED).build();
    }

    private ScheduleContract() {
    }
}
