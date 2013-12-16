package com.viamhealth.android.dao.db.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.viamhealth.android.dao.db.contractors.GoalContract;

/**
 * Created by naren on 13/12/13.
 */
public class UserProvider extends VHContentProvider {

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * SQLite backend for @{link UserProvider}.
     *
     * Provides access to an disk-backed, SQLite datastore which is utilized by FeedProvider. This
     * database should never be accessed by other parts of the application directly.
     */

    static class UserDatabase extends VHDatabase {
        /** SQL statement to create "entry" table. */
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + GoalContract.TABLE_NAME + " (" +
                        GoalContract.Goal._ID + " INTEGER PRIMARY KEY," +
                        GoalContract.Goal.USER_ID + TYPE_INTEGER + COMMA_SEP +
                        GoalContract.Goal.GOAL_TYPE + TYPE_INTEGER + COMMA_SEP +
                        GoalContract.Goal.DATA + TYPE_TEXT + COMMA_SEP + ")";

        /** SQL statement to drop "entry" table. */
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + GoalContract.TABLE_NAME;

        public GoalDatabase(Context context) {
            super(context);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
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
