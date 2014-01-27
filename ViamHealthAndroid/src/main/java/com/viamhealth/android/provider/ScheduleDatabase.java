package com.viamhealth.android.provider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.provider.BaseColumns;

import com.viamhealth.android.provider.ScheduleContract.UserColumns;
import com.viamhealth.android.provider.ScheduleContract.UserHealthProfileColumns;
import com.viamhealth.android.provider.ScheduleContract.UserProfileColumns;
import com.viamhealth.android.provider.ScheduleContract.UserForeignKeyColumn;
import com.viamhealth.android.provider.ScheduleContract.SyncColumns;
import com.viamhealth.android.provider.ScheduleContract.RemindersColumns;
import com.viamhealth.android.provider.ScheduleContract.ReminderReadingsColumns;
import com.viamhealth.android.provider.ScheduleContract.ReminderForeignKeyColumn;
import com.viamhealth.android.utils.LogUtils;

/**
 * Created by naren on 17/12/13.
 */
public class ScheduleDatabase extends SQLiteOpenHelper {

    private static final String TAG = LogUtils.makeLogTag(ScheduleDatabase.class);

    private static final String DATABASE_NAME = "vh.db";

    // NOTE: carefully update onUpgrade() when bumping database versions to make
    // sure user data is saved.

    private static final int VER_2013_ALPHA_LAUNCH = 8;  // 1.0
    private static final int DATABASE_VERSION = VER_2013_ALPHA_LAUNCH;

    private final Context mContext;

    interface TABLES {
        String SYNCHRONIZE = "synchronize";
        String USERS = "users";
        String PROFILE = "user_profile";
        String HEALTH_PROFILE = "user_health_profile";

        String USERS_COMPLETE_JOIN = USERS + " " + ScheduleContract.Users.TABLE_ALIAS +" LEFT OUTER JOIN "+ PROFILE +" ON "+ScheduleContract.Users.TABLE_ALIAS+"."+ScheduleContract.Users.USER_ID+" = "+PROFILE+"."+ScheduleContract.Users.USER_ID+
                                    " LEFT OUTER JOIN "+ HEALTH_PROFILE +" ON "+ScheduleContract.Users.TABLE_ALIAS+"."+ScheduleContract.Users.USER_ID+" = "+HEALTH_PROFILE+"."+ScheduleContract.Users.USER_ID;
        String REMINDERS="reminders";
        String REMINDER_READINGS="reminder_readings";

    }

    interface TRIGGERS {
        String USER_PROFILE_DELETE = "user_profile_delete";
        String USER_HEALTH_PROFILE_DELETE = "user_health_profile_delete";
        String REMINDERS_DELETE = "reminders_delete";
        String REMINDER_READINGS_DELETE = "reminder_readings_delete";

    }

    interface FOREIGN_KEYS {
        String USER_ID_FOREIGN_KEY = new StringBuilder("FOREIGN KEY (").append(UserForeignKeyColumn.USER_ID)
                            .append(") REFERENCES ").append(TABLES.USERS).append("(")
                            .append(UserForeignKeyColumn.USER_ID).append(")").toString();

        String REMINDER_ID_FOREIGN_KEY = new StringBuilder("FOREIGN KEY (").append(ReminderForeignKeyColumn.REMINDER_ID)
                .append(") REFERENCES ").append(TABLES.REMINDERS).append("(")
                .append(UserForeignKeyColumn.USER_ID).append(")").toString();

    }

    interface CREATE_TABLES {
        String CREATE_TABLE = "CREATE TABLE ";

        String SYNCHRONIZE = new StringBuilder(CREATE_TABLE).append(TABLES.SYNCHRONIZE).append("(")
                    .append(BaseColumns._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                    .append(SyncColumns.SYNC_TABLE_URI).append(" TEXT,")
                    .append(SyncColumns.UPDATED).append(" NUMERIC ,")
                    .append(SyncColumns.SYNCHRONIZED).append(" NUMERIC ,")
                    .append(SyncColumns.SYNC_STATUS).append(" INTEGER)").toString();

        String USER = new StringBuilder(CREATE_TABLE).append(TABLES.USERS).append("(")
                    .append(UserForeignKeyColumn.USER_ID).append(" INTEGER PRIMARY KEY,")
                    .append(SyncColumns.UPDATED).append(" NUMERIC ,")
                    .append(SyncColumns.SYNCHRONIZED).append(" NUMERIC ,")
                    .append(SyncColumns.SYNC_STATUS).append(" INTEGER ,")
                    .append(SyncColumns.IS_DELETED).append(" INTEGER,")
                    .append(UserColumns.EMAIL).append(" TEXT,")
                    .append(UserColumns.MOBILE).append(" TEXT,")
                    .append(UserColumns.USER_NAME).append(" TEXT,")
                    .append(UserColumns.FIRST_NAME).append(" TEXT,")
                    .append(UserColumns.LAST_NAME).append(" TEXT,")
                    .append(UserColumns.IS_LOGGED_IN_USER).append(" INTEGER NOT NULL DEFAULT 0 )").toString();

        String PROFILE = new StringBuilder(CREATE_TABLE).append(TABLES.PROFILE).append("(")
                    .append(BaseColumns._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                    .append(SyncColumns.UPDATED).append(" NUMERIC ,")
                    .append(SyncColumns.SYNCHRONIZED).append(" NUMERIC ,")
                    .append(SyncColumns.SYNC_STATUS).append(" INTEGER ,")
                    .append(SyncColumns.IS_DELETED).append(" INTEGER,")
                    .append(UserForeignKeyColumn.USER_ID).append(" INTEGER,")
                    .append(UserProfileColumns.DOB).append(" NUMERIC NOT NULL,")
                    .append(UserProfileColumns.FB_PROFILE_ID).append(" TEXT,")
                    .append(UserProfileColumns.FB_USERNAME).append(" TEXT,")
                    .append(UserProfileColumns.GENDER).append(" INTEGER NOT NULL,")
                    .append(UserProfileColumns.MOBILE_NUMBER).append(" TEXT,")
                    .append(UserProfileColumns.PIC_URL).append(" TEXT,")
                    .append(UserProfileColumns.RELATION).append(" INTEGER,")
                    .append(UserProfileColumns.BLOOD_GROUP).append(" INTEGER,")
                    .append(UserProfileColumns.ORGANIZATION).append(" TEXT,")
                    .append(UserProfileColumns.LOCATION).append(" TEXT,")
                    .append(FOREIGN_KEYS.USER_ID_FOREIGN_KEY).append(")").toString();

        String HEALTH_PROFILE = new StringBuilder(CREATE_TABLE).append(TABLES.HEALTH_PROFILE).append("(")
                .append(BaseColumns._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(SyncColumns.UPDATED).append(" NUMERIC ,")
                .append(SyncColumns.SYNCHRONIZED).append(" NUMERIC ,")
                .append(SyncColumns.SYNC_STATUS).append(" INTEGER ,")
                .append(SyncColumns.IS_DELETED).append(" INTEGER,")
                .append(UserForeignKeyColumn.USER_ID).append(" INTEGER,")
                .append(UserHealthProfileColumns.HEIGHT).append(" INTEGER,")
                .append(UserHealthProfileColumns.WEIGHT).append(" REAL,")
                .append(UserHealthProfileColumns.SYSTOLIC_PRESSURE).append(" INTEGER,")
                .append(UserHealthProfileColumns.DIASTOLIC_PRESSURE).append(" INTEGER,")
                .append(UserHealthProfileColumns.FASTING_SUGAR).append(" INTEGER,")
                .append(UserHealthProfileColumns.RANDOM_SUGAR).append(" INTEGER,")
                .append(UserHealthProfileColumns.HDL).append(" INTEGER,")
                .append(UserHealthProfileColumns.LDL).append(" INTEGER,")
                .append(UserHealthProfileColumns.TRIGLYCERIDES).append(" INTEGER,")
                .append(UserHealthProfileColumns.TOTAL_CHOLESTEROL).append(" INTEGER,")
                .append(UserHealthProfileColumns.PULSE_RATE).append(" INTEGER,")
                .append(UserHealthProfileColumns.BMR).append(" INTEGER,")
                .append(UserHealthProfileColumns.BMI_CLASSIFIER).append(" INTEGER,")
                .append(FOREIGN_KEYS.USER_ID_FOREIGN_KEY).append(")").toString();

        String REMINDERS = new StringBuilder(CREATE_TABLE).append(TABLES.REMINDERS).append("(")
                .append(BaseColumns._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(SyncColumns.UPDATED).append(" NUMERIC ,")
                .append(SyncColumns.SYNCHRONIZED).append(" NUMERIC ,")
                .append(SyncColumns.SYNC_STATUS).append(" INTEGER ,")
                .append(SyncColumns.IS_DELETED).append(" INTEGER,")
                .append(ReminderForeignKeyColumn.REMINDER_ID).append(" INTEGER,")
                .append(RemindersColumns.USER_ID).append(" INTEGER,")
                .append(RemindersColumns.TYPE).append(" INTEGER,")
                .append(RemindersColumns.NAME).append(" TEXT,")
                .append(RemindersColumns.DETAILS).append(" TEXT,")
                .append(RemindersColumns.MORNING_COUNT).append(" INTEGER,")
                .append(RemindersColumns.AFTERNOON_COUNT).append(" INTEGER,")
                .append(RemindersColumns.EVENING_COUNT).append(" INTEGER,")
                .append(RemindersColumns.NIGHT_COUNT).append(" INTEGER,")
                .append(RemindersColumns.START_DATE).append(" TEXT,")
                .append(RemindersColumns.END_DATE).append(" TEXT,")
                .append(RemindersColumns.REPEAT_MODE).append(" INTEGER,")
                .append(RemindersColumns.REPEAT_DAY).append(" INTEGER,")
                .append(RemindersColumns.REPEAT_HOUR).append(" INTEGER,")
                .append(RemindersColumns.REPEAT_MIN).append(" INTEGER,")
                .append(RemindersColumns.REPEAT_WEEKDAY).append(" INTEGER,")
                .append(RemindersColumns.REPEAT_EVERY_X).append(" INTEGER,")
                .append(RemindersColumns.REPEAT_I_COUNTER).append(" INTEGER,")
                .append(RemindersColumns.CREATED_AT).append(" NUMERIC,")
                .append(RemindersColumns.UPDATED_AT).append(" NUMERIC,")
                .append(FOREIGN_KEYS.REMINDER_ID_FOREIGN_KEY).append(")").toString();

        String REMINDER_READINGS = new StringBuilder(CREATE_TABLE).append(TABLES.REMINDER_READINGS).append("(")
                .append(BaseColumns._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(SyncColumns.UPDATED).append(" NUMERIC ,")
                .append(SyncColumns.SYNCHRONIZED).append(" NUMERIC ,")
                .append(SyncColumns.SYNC_STATUS).append(" INTEGER ,")
                .append(SyncColumns.IS_DELETED).append(" INTEGER,")
                .append(RemindersColumns.USER_ID).append(" INTEGER,")
                .append(ReminderForeignKeyColumn.REMINDER_ID).append(" INTEGER,")
                .append(ReminderReadingsColumns.READING_ID).append(" REAL,")
                .append(ReminderReadingsColumns.MORNING_CHECK).append(" REAL,")
                .append(ReminderReadingsColumns.AFTERNOON_CHECK).append(" INTEGER,")
                .append(ReminderReadingsColumns.EVENING_CHECK).append(" INTEGER,")
                .append(ReminderReadingsColumns.NIGHT_CHECK).append(" INTEGER,")
                .append(ReminderReadingsColumns.COMPLETE_CHECK).append(" INTEGER,")
                .append(ReminderReadingsColumns.UPDATED_BY).append(" INTEGER,")
                .append(ReminderReadingsColumns.READING_DATE).append(" INTEGER,")
                .append(FOREIGN_KEYS.REMINDER_ID_FOREIGN_KEY).append(")").toString();

    }

    interface CREATE_TRIGGERS {
        String CREATE_TRIGGER = "CREATE TRIGGER ";
        String USER_PROFILE_DELETE = new StringBuilder(CREATE_TRIGGER).append(TRIGGERS.USER_PROFILE_DELETE).append(" AFTER DELETE ON ")
                 .append(TABLES.USERS).append(" BEGIN DELETE FROM ").append(TABLES.PROFILE).append(" WHERE ")
                 .append(TABLES.PROFILE).append(".").append(UserForeignKeyColumn.USER_ID).append(" = old.")
                 .append(TABLES.USERS).append(".").append(UserForeignKeyColumn.USER_ID).append(";").append("END").toString();

        String USER_HEALTH_DELETE = new StringBuilder(CREATE_TRIGGER).append(TRIGGERS.USER_HEALTH_PROFILE_DELETE).append(" AFTER DELETE ON ")
                .append(TABLES.USERS).append(" BEGIN DELETE FROM ").append(TABLES.HEALTH_PROFILE).append(" WHERE ")
                .append(TABLES.HEALTH_PROFILE).append(".").append(UserForeignKeyColumn.USER_ID).append(" = old.")
                .append(TABLES.USERS).append(".").append(UserForeignKeyColumn.USER_ID).append(";").append("END").toString();
/*
        String REMINDERS_DELETE = new StringBuilder(CREATE_TRIGGER).append(TRIGGERS.REMINDERS_DELETE).append(" AFTER DELETE ON ")
                .append(TABLES.REMINDERS).append(" BEGIN DELETE FROM ").append(TABLES.HEALTH_PROFILE).append(" WHERE ")
                .append(TABLES.HEALTH_PROFILE).append(".").append(UserForeignKeyColumn.USER_ID).append(" = old.")
                .append(TABLES.USERS).append(".").append(UserForeignKeyColumn.USER_ID).append(";").append("END").toString();
*/

    }

    public ScheduleDatabase(Context context, ScheduleProvider.SQLiteCursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON");
        /** Version 0.1 **/
        LogUtils.LOGD(TAG, "creating tables...");
        /** Create Tables **/
        LogUtils.LOGD(TAG, CREATE_TABLES.SYNCHRONIZE);
        db.execSQL(CREATE_TABLES.SYNCHRONIZE);
        LogUtils.LOGD(TAG, CREATE_TABLES.USER);
        db.execSQL(CREATE_TABLES.USER);
        LogUtils.LOGD(TAG, CREATE_TABLES.PROFILE);
        db.execSQL(CREATE_TABLES.PROFILE);
        LogUtils.LOGD(TAG, CREATE_TABLES.HEALTH_PROFILE);
        db.execSQL(CREATE_TABLES.HEALTH_PROFILE);

        LogUtils.LOGD(TAG, CREATE_TABLES.REMINDERS);
        db.execSQL(CREATE_TABLES.REMINDERS);

        LogUtils.LOGD(TAG, CREATE_TABLES.REMINDER_READINGS);
        db.execSQL(CREATE_TABLES.REMINDER_READINGS);

        LogUtils.LOGD(TAG, "creating triggers...");
        /** Create Triggers **/
        LogUtils.LOGD(TAG, CREATE_TRIGGERS.USER_HEALTH_DELETE);
        db.execSQL(CREATE_TRIGGERS.USER_HEALTH_DELETE);
        LogUtils.LOGD(TAG, CREATE_TRIGGERS.USER_PROFILE_DELETE);
        db.execSQL(CREATE_TRIGGERS.USER_PROFILE_DELETE);

        LogUtils.LOGD(TAG, "done with all db creations...");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO will worry for next release
        db.execSQL("DROP TABLE IF EXISTS " + TABLES.SYNCHRONIZE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLES.USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLES.PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLES.HEALTH_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLES.REMINDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLES.REMINDER_READINGS);
        onCreate(db);
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

}
