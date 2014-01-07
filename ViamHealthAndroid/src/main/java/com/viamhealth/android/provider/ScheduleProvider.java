package com.viamhealth.android.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.net.Uri;

import com.viamhealth.android.dao.db.helper.SelectionBuilder;
import com.viamhealth.android.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by naren on 17/12/13.
 */
public class ScheduleProvider extends ContentProvider {

    private static final String TAG = LogUtils.makeLogTag(ScheduleProvider.class);

    private ScheduleDatabase mOpenHelper;
    private final SQLiteCursorFactory mFactory = new SQLiteCursorFactory(true);

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    /** Matching Uri Codes **/
    private static final int USERS_URI = 100;
    private static final int USERS_ITEM_URI = 101;
    private static final int USERS_ITEM_WN_URI = 102;
    private static final int USERS_ITEM_PROFILE_URI = 103;
    private static final int USERS_ITEM_PROFILE_WN_URI = 104;
    private static final int USERS_ITEM_HEALTH_PROFILE_URI = 105;
    private static final int USERS_ITEM_HEALTH_PROFILE_WN_URI = 106;
    private static final int USERS_LAST_SYNC_TIME_URI = 107;
    private static final int USERS_UPDATABLE_DATA_URI = 108;

    private static final int REMINDERS_URI=109;
    private static final int REMINDERS_WN_URI=110;
    private static final int REMINDERS_READINGS_URI=111;
    private static final int REMINDERS_READINGS_WN_URI=112;
    private static final int REMINDERS_LAST_SYNC_TIME_URI=113;
    private static final int REMINDERS_UPDATABLE_DATA_URI=114;

    private static final int SYNC_URI = 1;

    /** Matching Uri Patterns **/
    private static final String ANY_INTEGER = "#";
    private static final String SYNC_PATTERN = ScheduleContract.PATH_SYNCHRONIZED;
    private static final String USER_PATTERN = ScheduleContract.PATH_USERS;
    private static final String USER_ITEM_PATTERN = ScheduleContract.PATH_USERS + "/"+ ANY_INTEGER;
    private static final String USER_ITEM_WN_PATTERN = ScheduleContract.PATH_USERS + "/-"+ ANY_INTEGER;
    private static final String USER_PROFILE_PATTERN = ScheduleContract.PATH_USERS + "/"+ ANY_INTEGER +"/" + ScheduleContract.PATH_PROFILE;
    private static final String USER_PROFILE_WN_PATTERN = ScheduleContract.PATH_USERS + "/-"+ ANY_INTEGER +"/" + ScheduleContract.PATH_PROFILE;
    private static final String USER_HEALTH_PROFILE_PATTERN = ScheduleContract.PATH_USERS + "/"+ ANY_INTEGER +"/" + ScheduleContract.PATH_HEALTH_PROFILE;
    private static final String USER_HEALTH_PROFILE_WN_PATTERN = ScheduleContract.PATH_USERS + "/-"+ ANY_INTEGER +"/" + ScheduleContract.PATH_HEALTH_PROFILE;
    private static final String USER_LAST_SYNC_TIME_PATTERN = ScheduleContract.PATH_USERS + "/" + ScheduleContract.PATH_LAST_SYNC_TIME;
    private static final String USER_UPDATABLE_DATA_PATTERN = ScheduleContract.PATH_USERS + "/" + ScheduleContract.PATH_DATA_TO_BE_UPDATED;

    private static final String REMINDERS_PATTERN=ScheduleContract.PATH_REMINDERS;
    private static final String REMINDERS_WN_PATTERN=ScheduleContract.PATH_REMINDERS;
    private static final String REMINDERS_READINGS_PATTERN=ScheduleContract.PATH_REMINDER_READINGS;
    private static final String REMINDERS_READINGS_WN_PATTERN=ScheduleContract.PATH_REMINDER_READINGS;
    private static final String REMINDERS_LAST_SYNC_TIME_PATTERN=ScheduleContract.PATH_REMINDERS+"/"+ScheduleContract.PATH_LAST_SYNC_TIME;
    private static final String REMINDERS_UPDATABLE_DATA_PATTERN=ScheduleContract.PATH_REMINDERS+"/"+ScheduleContract.PATH_DATA_TO_BE_UPDATED;;

    /** Min IDS **/
    private static Integer USER_ID_MIN = -1;

    /**
     * Build and return a {@link UriMatcher} that catches all {@link Uri}
     * variations supported by this {@link ContentProvider}.
     */
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ScheduleContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, SYNC_PATTERN, SYNC_URI);
        matcher.addURI(authority, USER_PATTERN, USERS_URI);
        matcher.addURI(authority, USER_ITEM_PATTERN, USERS_ITEM_URI);
        matcher.addURI(authority, USER_ITEM_WN_PATTERN, USERS_ITEM_WN_URI);
        matcher.addURI(authority, USER_PROFILE_PATTERN, USERS_ITEM_PROFILE_URI);
        matcher.addURI(authority, USER_PROFILE_WN_PATTERN, USERS_ITEM_PROFILE_WN_URI);
        matcher.addURI(authority, USER_HEALTH_PROFILE_PATTERN, USERS_ITEM_HEALTH_PROFILE_URI);
        matcher.addURI(authority, USER_HEALTH_PROFILE_WN_PATTERN, USERS_ITEM_HEALTH_PROFILE_WN_URI);
        //matcher.addURI(authority, USER_LAST_SYNC_TIME_PATTERN, USERS_LAST_SYNC_TIME_URI);
        matcher.addURI(authority, USER_UPDATABLE_DATA_PATTERN, USERS_UPDATABLE_DATA_URI);

        matcher.addURI(authority, REMINDERS_PATTERN, REMINDERS_URI);
        matcher.addURI(authority, REMINDERS_WN_PATTERN, REMINDERS_WN_URI);
        matcher.addURI(authority, REMINDERS_READINGS_PATTERN, REMINDERS_READINGS_URI);
        matcher.addURI(authority, REMINDERS_READINGS_WN_PATTERN, REMINDERS_READINGS_WN_URI);
        matcher.addURI(authority, REMINDERS_LAST_SYNC_TIME_PATTERN, REMINDERS_LAST_SYNC_TIME_URI);
        matcher.addURI(authority, REMINDERS_UPDATABLE_DATA_PATTERN, REMINDERS_UPDATABLE_DATA_URI);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ScheduleDatabase(getContext(), mFactory);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        LogUtils.LOGV(TAG, "query(uri=" + uri + ", proj=" + Arrays.toString(projection) + ")");
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        final int match = sUriMatcher.match(uri);
        LogUtils.LOGV(TAG, "query: match code " + match);
        SelectionBuilder builder = null;

        Cursor c = null;

        switch(match){
            case USERS_UPDATABLE_DATA_URI://get all the data that needs to be updated
                builder = new SelectionBuilder();
                c = builder.table(ScheduleDatabase.TABLES.USERS_COMPLETE_JOIN).where(ScheduleContract.Users.TABLE_ALIAS + "." +ScheduleContract.Users.SYNC_STATUS + "=?", new String[]{String.valueOf(ScheduleContract.SyncStatus.PENDING_UPDATE.ordinal())})
                        .query(db, projection, null);
                    //select * from users where sync_status=pending_update;
                break;

            case REMINDERS_UPDATABLE_DATA_URI://get all the data that needs to be updated
                builder = new SelectionBuilder();
                c = builder.table(ScheduleDatabase.TABLES.REMINDERS).where(ScheduleContract.Reminders.SYNC_STATUS + "=?", new String[]{String.valueOf(ScheduleContract.SyncStatus.PENDING_UPDATE.ordinal())})
                        .query(db, projection, null);
                //select * from users where sync_status=pending_update;
                break;

            default:
                builder = buildAdvancedSelection(uri, match);
                c = builder.where(selection, selectionArgs).query(db, projection, sortOrder);
                break;
        }

        // Note: Notification URI must be manually set here for loaders to correctly
        // register ContentObservers.
        Context ctx = getContext();
        assert ctx != null;
        c.setNotificationUri(ctx.getContentResolver(), uri);

        return c;
    }

    private SelectionBuilder buildAdvancedSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        Integer userId;
        switch (match) {
            case SYNC_URI:
                builder.table(ScheduleDatabase.TABLES.SYNCHRONIZE);
                return builder;
            case USERS_ITEM_URI:
            case USERS_ITEM_WN_URI:
                userId = ScheduleContract.Users.getUserId(uri);
                builder.where(ScheduleContract.UserForeignKeyColumn.USER_ID + "=?", userId.toString());
            case USERS_URI:
                // Return all known entries.
                builder.where(ScheduleContract.Users.TABLE_ALIAS + "." + ScheduleContract.Users.IS_DELETED + "=0");//get only active ones
                builder.table(ScheduleDatabase.TABLES.USERS_COMPLETE_JOIN);
                return builder;
            case USERS_ITEM_PROFILE_URI:
            case USERS_ITEM_PROFILE_WN_URI:
                userId = ScheduleContract.Users.getUserId(uri);
                builder.where(ScheduleContract.UserForeignKeyColumn.USER_ID + "=?", userId.toString());
                builder.where(ScheduleContract.Users.IS_DELETED + "=0");//get only active ones
                builder.table(ScheduleDatabase.TABLES.PROFILE);
                return builder;
            case USERS_ITEM_HEALTH_PROFILE_URI:
            case USERS_ITEM_HEALTH_PROFILE_WN_URI:
                userId = ScheduleContract.Users.getUserId(uri);
                builder.where(ScheduleContract.UserForeignKeyColumn.USER_ID + "=?", userId.toString());
                builder.where(ScheduleContract.Users.IS_DELETED + "=0");//get only active ones
                builder.table(ScheduleDatabase.TABLES.HEALTH_PROFILE);
                return builder;

            case REMINDERS_URI:
            case REMINDERS_WN_URI:
                builder.where(ScheduleContract.Reminders.IS_DELETED + "=0");//get only active ones
                builder.table(ScheduleDatabase.TABLES.REMINDERS);

            case REMINDERS_READINGS_URI:
            case REMINDERS_READINGS_WN_URI:
                builder.where(ScheduleContract.Reminders.IS_DELETED + "=0");//get only active ones
                builder.table(ScheduleDatabase.TABLES.REMINDER_READINGS);


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
    /**
     * Build an advanced {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually only used by {@link #query}, since it
     * performs table joins useful for {@link Cursor} data.
     */
    private SelectionBuilder buildSimpleSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        Integer userId;
        switch (match) {
            case SYNC_URI:
                builder.table(ScheduleDatabase.TABLES.SYNCHRONIZE);
                return builder;
            case USERS_ITEM_URI:
            case USERS_ITEM_WN_URI:
                userId = ScheduleContract.Users.getUserId(uri);
                builder.where(ScheduleContract.UserForeignKeyColumn.USER_ID + "=?", userId.toString());
            case USERS_URI:
                // Return all known entries.
                builder.table(ScheduleDatabase.TABLES.USERS);
                return builder;
            case USERS_ITEM_PROFILE_URI:
            case USERS_ITEM_PROFILE_WN_URI:
                userId = ScheduleContract.Users.getUserId(uri);
                builder.where(ScheduleContract.UserForeignKeyColumn.USER_ID + "=?", userId.toString());
                builder.table(ScheduleDatabase.TABLES.PROFILE);
                return builder;
            case USERS_ITEM_HEALTH_PROFILE_URI:
            case USERS_ITEM_HEALTH_PROFILE_WN_URI:
                userId = ScheduleContract.Users.getUserId(uri);
                builder.where(ScheduleContract.UserForeignKeyColumn.USER_ID + "=?", userId.toString());
                builder.table(ScheduleDatabase.TABLES.HEALTH_PROFILE);
                return builder;

            case REMINDERS_URI:
            case REMINDERS_WN_URI:
                builder.table(ScheduleDatabase.TABLES.REMINDERS);
                return builder;

            case REMINDERS_READINGS_URI:
            case REMINDERS_READINGS_WN_URI:
                builder.table(ScheduleDatabase.TABLES.REMINDER_READINGS);
                return builder;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }


    @Override
    public String getType(Uri uri) {
        final int type = sUriMatcher.match(uri);
        switch(type){
            case USERS_URI:
                return ScheduleContract.Users.CONTENT_TYPE;
            case USERS_ITEM_URI:
            case USERS_ITEM_WN_URI:
                return ScheduleContract.Users.CONTENT_ITEM_TYPE;

            case REMINDERS_URI:
            case REMINDERS_WN_URI:
            case REMINDERS_READINGS_URI:
            case REMINDERS_READINGS_WN_URI:
                return ScheduleContract.Reminders.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    private Long getMinId(String tableName) {
        long id = 0;
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor c = db.rawQuery("select min("+ScheduleContract.Users.USER_ID+") as minId from "+ScheduleDatabase.TABLES.USERS, null);
        if(c!=null && c.getCount()>0 && c.moveToFirst()){
            id = c.getLong(0);
            if(id>0) return -1L;
        }
        return id-1;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        LogUtils.LOGV(TAG, "insert(uri=" + uri + ", values=" + values.toString() + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        LogUtils.LOGD(TAG, "matched uri-code is "+ match +" for " + uri);
        boolean syncToNetwork = !ScheduleContract.hasCallerIsSyncAdapterParameter(uri);
        switch (match) {
            case SYNC_URI:
                db.insertOrThrow(ScheduleDatabase.TABLES.SYNCHRONIZE, null, values);
                notifyChange(uri, false);
                return uri;

            case USERS_URI:
            case USERS_ITEM_URI:
            case USERS_ITEM_WN_URI:{
                Integer userId = values.getAsInteger(ScheduleContract.Users.USER_ID);
                if(userId==null || userId == 0){
                    userId = getMinId(ScheduleDatabase.TABLES.USERS).intValue();
                    values.remove(ScheduleContract.Users.USER_ID);
                    values.put(ScheduleContract.Users.USER_ID, userId);
                }
                db.insertOrThrow(ScheduleDatabase.TABLES.USERS, null, values);
                notifyChange(uri, syncToNetwork);
                return ScheduleContract.Users.buildUserUri(values.getAsLong(ScheduleContract.Users.USER_ID));
            }
            case USERS_ITEM_PROFILE_URI:
            case USERS_ITEM_PROFILE_WN_URI:{
                Integer userId = ScheduleContract.Users.getUserId(uri);
                if(userId!=null)
                    values.put(ScheduleContract.UserForeignKeyColumn.USER_ID, userId);
                db.insertOrThrow(ScheduleDatabase.TABLES.PROFILE, null, values);
                notifyChange(uri, syncToNetwork);
                return ScheduleContract.Users.buildUserUri(values.getAsLong(ScheduleContract.Users.USER_ID));
            }
            case USERS_ITEM_HEALTH_PROFILE_URI:
            case USERS_ITEM_HEALTH_PROFILE_WN_URI: {
                Integer userId = ScheduleContract.Users.getUserId(uri);
                if(userId!=null)
                    values.put(ScheduleContract.UserForeignKeyColumn.USER_ID, userId);
                db.insertOrThrow(ScheduleDatabase.TABLES.HEALTH_PROFILE, null, values);
                notifyChange(uri, syncToNetwork);
                return ScheduleContract.Users.buildUserUri(values.getAsLong(ScheduleContract.Users.USER_ID));
            }

            case REMINDERS_URI:
            case REMINDERS_WN_URI:{
                db.insertOrThrow(ScheduleDatabase.TABLES.REMINDERS, null, values);
                notifyChange(uri, syncToNetwork);
                return ScheduleContract.Reminders.buildReminderUri();
            }

            case REMINDERS_READINGS_URI:
            case REMINDERS_READINGS_WN_URI:{
                db.insertOrThrow(ScheduleDatabase.TABLES.REMINDER_READINGS, null, values);
                notifyChange(uri, syncToNetwork);
                return ScheduleContract.Reminders.buildReminderReadingsUri();
            }



            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    private void deleteDatabase() {
        // TODO: wait for content provider operations to finish, then tear down
        mOpenHelper.close();
        Context context = getContext();
        ScheduleDatabase.deleteDatabase(context);
        mOpenHelper = new ScheduleDatabase(getContext(), mFactory);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        LogUtils.LOGV(TAG, "delete(uri=" + uri + ")");
        if (uri == ScheduleContract.BASE_CONTENT_URI) {
            // Handle whole database deletes (e.g. when signing out)
            deleteDatabase();
            notifyChange(uri, false);
            return 1;
        }
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        final SelectionBuilder builder = buildSimpleSelection(uri, match);
        int retVal = builder.where(selection, selectionArgs).delete(db);
        notifyChange(uri, !ScheduleContract.hasCallerIsSyncAdapterParameter(uri));
        return retVal;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        LogUtils.LOGV(TAG, "update(uri=" + uri + ", values=" + values.toString() + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        final SelectionBuilder builder = buildSimpleSelection(uri, match);
        int retVal = builder.where(selection, selectionArgs).update(db, values);
        boolean syncToNetwork = !ScheduleContract.hasCallerIsSyncAdapterParameter(uri);
        notifyChange(uri, syncToNetwork);
        return retVal;
    }

    private void notifyChange(Uri uri, boolean syncToNetwork) {
        Context context = getContext();
        context.getContentResolver().notifyChange(uri, null, syncToNetwork);

        // Widgets can't register content observers so we refresh widgets separately.
        //context.sendBroadcast(ScheduleWidgetProvider.getRefreshBroadcastIntent(context, false));
    }

    public class SQLiteCursorFactory implements SQLiteDatabase.CursorFactory {

        private boolean debugQueries = false;

        public SQLiteCursorFactory() {
            super();
            this.debugQueries = false;
        }

        public SQLiteCursorFactory(boolean debugQueries) {
            super();
            this.debugQueries = debugQueries;
        }

        @Override
        public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery,
                                String editTable, SQLiteQuery query) {
            if (debugQueries) {
                LogUtils.LOGD(TAG, "SQL:" + query.toString());
            }
            return new SQLiteCursor(db, masterQuery, editTable, query);
        }
    }

}
