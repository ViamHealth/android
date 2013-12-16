package com.viamhealth.android.dao.db.providers;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.viamhealth.android.dao.db.contractors.GoalContract;
import com.viamhealth.android.dao.db.helper.SelectionBuilder;

import java.util.List;

public class GoalProvider extends VHContentProvider {

    private GoalDatabase mDatabaseHelper;

    /**
     * Content authority for this provider.
     */
    private static final String AUTHORITY = GoalContract.AUTHORITY;

    // The constants below represent individual URI routes, as IDs. Every URI pattern recognized by
    // this ContentProvider is defined using sUriMatcher.addURI(), and associated with one of these
    // IDs.
    //
    // When a incoming URI is run through sUriMatcher, it will be tested against the defined
    // URI patterns, and the corresponding route ID will be returned.
    /**
     * URI ID for route: /goals
     */
    public static final int ROUTE_ALL = 1;

    /**
     * URI ID for route: /goals/{UserID}/{goalType}
     */
    public static final int ROUTE_SPECIFIC = 2;

    /**
     * UriMatcher, used to decode incoming URIs.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, GoalContract.Goal.CONTENT_URI, ROUTE_ALL);
        sUriMatcher.addURI(AUTHORITY, GoalContract.Goal.CONTENT_BY_USER_ID_AND_GOAL_TYPE_URI, ROUTE_SPECIFIC);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
            case ROUTE_ALL:
                count = builder.table(GoalContract.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_SPECIFIC:
                List<String> segments = uri.getPathSegments();
                int segmentsCount = segments.size();
                String goalType = segments.get(segmentsCount-1);
                String userId = segments.get(segmentsCount-2);
                count = builder.table(GoalContract.TABLE_NAME)
                        .where(GoalContract.Goal.USER_ID + "=?", userId)
                        .where(GoalContract.Goal.GOAL_TYPE + "=?", goalType);
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case ROUTE_ALL:
                return GoalContract.Goal.CONTENT_TYPE;
            case ROUTE_SPECIFIC:
                return GoalContract.Goal.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        assert db != null;
        final int match = sUriMatcher.match(uri);
        Uri result;
        switch (match) {
            case ROUTE_ALL:
                long id = db.insertOrThrow(GoalContract.TABLE_NAME, null, values);
                result = Uri.parse(GoalContract.Goal.CONTENT_URI + "/" + id);
                break;
            case ROUTE_SPECIFIC:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return result;
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new GoalDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        SelectionBuilder builder = new SelectionBuilder();
        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case ROUTE_SPECIFIC:
                List<String> segments = uri.getPathSegments();
                int segmentsCount = segments.size();
                String goalType = segments.get(segmentsCount-1);
                String userId = segments.get(segmentsCount-2);
                builder.where(GoalContract.Goal.USER_ID + "=?", userId)
                        .where(GoalContract.Goal.GOAL_TYPE + "=?", goalType);
            case ROUTE_ALL:
                // Return all known entries.
                builder.table(GoalContract.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor c = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context ctx = getContext();
                assert ctx != null;
                c.setNotificationUri(ctx.getContentResolver(), uri);
                return c;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
            case ROUTE_ALL:
                count = builder.table(GoalContract.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_SPECIFIC:
                List<String> segments = uri.getPathSegments();
                int segmentsCount = segments.size();
                String goalType = segments.get(segmentsCount-1);
                String userId = segments.get(segmentsCount-2);
                count = builder.table(GoalContract.TABLE_NAME)
                        .where(GoalContract.Goal.USER_ID + "=?", userId)
                        .where(GoalContract.Goal.GOAL_TYPE + "=?", goalType);
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    /**
     * SQLite backend for @{link GoalProvider}.
     *
     * Provides access to an disk-backed, SQLite datastore which is utilized by FeedProvider. This
     * database should never be accessed by other parts of the application directly.
     */

    static class GoalDatabase extends VHDatabase {
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
