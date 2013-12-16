package com.viamhealth.android.dao.db.providers;

import android.content.ContentProvider;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.viamhealth.android.dao.db.contractors.GoalContract;
import com.viamhealth.android.dao.db.contractors.UserContract;

/**
 * Created by naren on 13/12/13.
 */

public abstract class VHContentProvider extends ContentProvider {

    protected static class VHDatabase extends SQLiteOpenHelper {
        /** Schema version. */
        public static final int DATABASE_VERSION = 1;
        /** Filename for SQLite file. */
        public static final String DATABASE_NAME = "vh.db";

        protected static final String TYPE_TEXT = " TEXT";
        protected static final String TYPE_INTEGER = " INTEGER";
        protected static final String COMMA_SEP = ",";

        public VHDatabase(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /** SQL statement to create tables. */
        private static final String SQL_CREATE_USER_TABLE =
                "CREATE TABLE " + UserContract.TABLE_NAME + " (" +
                        UserContract.User._ID + " INTEGER PRIMARY KEY," +
                        UserContract.User.EMAIL + TYPE_TEXT + COMMA_SEP +
                        UserContract.User.MOBILE + TYPE_TEXT + COMMA_SEP +
                        UserContract.User.FIRST_NAME + TYPE_TEXT + COMMA_SEP +
                        UserContract.User.LAST_NAME + TYPE_TEXT + COMMA_SEP +
                        UserContract.User.IS_LOGGED_IN_USER + TYPE_TEXT
                        GoalContract.Goal.USER_ID + TYPE_INTEGER + COMMA_SEP +
                        GoalContract.Goal.GOAL_TYPE + TYPE_INTEGER + COMMA_SEP +
                        GoalContract.Goal.DATA + TYPE_TEXT + COMMA_SEP + ")";

        private static final String SQL_CREATE_GOAL_TABLE =
                "CREATE TABLE " + GoalContract.TABLE_NAME + " (" +
                        GoalContract.Goal._ID + " INTEGER PRIMARY KEY," +
                        GoalContract.Goal.USER_ID + TYPE_INTEGER + COMMA_SEP +
                        GoalContract.Goal.GOAL_TYPE + TYPE_INTEGER + COMMA_SEP +
                        GoalContract.Goal.DATA + TYPE_TEXT + COMMA_SEP + ")";


        /** SQL statement to drop "entry" table. */
        private static final String SQL_DELETE_GOAL_TABLE =
                "DROP TABLE IF EXISTS " + GoalContract.TABLE_NAME;

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("PRAGMA foreign_keys=ON;");
            db.execSQL(SQL_CREATE_GOAL_TABLE);
            db.execSQL(SQL_CREATE_USER_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }


    }

}
