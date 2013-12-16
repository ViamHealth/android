package com.viamhealth.android.dao.db;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.viamhealth.android.dao.db.JournalFoodDatabaseHelper;

import com.viamhealth.android.dao.db.JournalDietTracker;
import com.viamhealth.android.dao.db.JournalFoodItems;


public class JournalContentProvider extends ContentProvider {

    // database
    private JournalFoodDatabaseHelper database;

    // used for the UriMacher
    private static final int TRACKER = 10;
    private static final int TRACKER_ID = 20;

    private static final int ITEMS = 30;
    private static final int ITEMS_ID = 40;

    private static final String AUTHORITY = "com.viamhealth.android.dao.db";

    private static final String BASE_PATH_TRACKER = "tbl_diet_tracker";
    public static final Uri CONTENT_URI_TRACKER = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_TRACKER);

    private static final String BASE_PATH_ITEMS = "tbl_food_items";
    public static final Uri CONTENT_URI_ITEMS = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH_ITEMS);



    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, "tbl_diet_tracker", TRACKER);
        sURIMatcher.addURI(AUTHORITY, "tbl_diet_tracker/#", TRACKER_ID);

        sURIMatcher.addURI(AUTHORITY, "tbl_food_items", ITEMS);
        sURIMatcher.addURI(AUTHORITY, "tbl_food_items/#", ITEMS_ID);
    }

    @Override
    public boolean onCreate() {
        database = new JournalFoodDatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case TRACKER:
            case TRACKER_ID:
                // adding the ID to the original query
                checkColumnsTracker(projection);
                queryBuilder.setTables(JournalDietTracker.TABLE_DIET_TRACKER);
                break;

            case ITEMS:
            case ITEMS_ID:
                // adding the ID to the original query
                checkColumnsItems(projection);
                queryBuilder.setTables(JournalFoodItems.TABLE_FOOD_ITEMS);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        String toParse;
        long id = 0;
        switch (uriType) {
            case TRACKER:
                id = sqlDB.insert(JournalDietTracker.TABLE_DIET_TRACKER, null, values);
                toParse=BASE_PATH_TRACKER + "/" + id;
                break;
            case ITEMS:
                id = sqlDB.insert(JournalFoodItems.TABLE_FOOD_ITEMS, null, values);
                toParse=BASE_PATH_ITEMS + "/" + id;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(toParse);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        String id;
        switch (uriType) {
            case TRACKER:
                rowsDeleted = sqlDB.delete(JournalDietTracker.TABLE_DIET_TRACKER, selection,
                        selectionArgs);
                break;

            case ITEMS:
                rowsDeleted = sqlDB.delete(JournalFoodItems.TABLE_FOOD_ITEMS, selection,
                        selectionArgs);
                break;

            case TRACKER_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(JournalDietTracker.TABLE_DIET_TRACKER,
                            JournalDietTracker.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(JournalDietTracker.TABLE_DIET_TRACKER,
                            JournalDietTracker.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;


            case ITEMS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(JournalFoodItems.TABLE_FOOD_ITEMS,
                            JournalFoodItems.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(JournalFoodItems.TABLE_FOOD_ITEMS,
                            JournalFoodItems.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        String id;
        switch (uriType) {
            case TRACKER:
                rowsUpdated = sqlDB.update(JournalDietTracker.TABLE_DIET_TRACKER,
                        values,
                        selection,
                        selectionArgs);
                break;
            case TRACKER_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(JournalDietTracker.TABLE_DIET_TRACKER,
                            values,
                            JournalDietTracker.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(JournalDietTracker.TABLE_DIET_TRACKER,
                            values,
                            JournalDietTracker.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;

            case ITEMS:
                rowsUpdated = sqlDB.update(JournalFoodItems.TABLE_FOOD_ITEMS,
                        values,
                        selection,
                        selectionArgs);
                break;
            case ITEMS_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(JournalFoodItems.TABLE_FOOD_ITEMS,
                            values,
                            JournalFoodItems.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(JournalFoodItems.TABLE_FOOD_ITEMS,
                            values,
                            JournalFoodItems.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;


            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumnsTracker(String[] projection) {
        String[] available = { JournalDietTracker.USER_ID,
                JournalDietTracker.FOOD_ITEM_ID, JournalDietTracker.FOOD_QUANTITY_MULTIPLIER,
                JournalDietTracker.MEAL_TYPE, JournalDietTracker.DIET_DATE, JournalDietTracker.CREATED_AT, JournalDietTracker.UPDATED_AT, JournalDietTracker.UPDATED_BY};
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

    private void checkColumnsItems(String[] projection) {
        String[] available = { JournalFoodItems.COLUMN_NAME,
                JournalFoodItems.COLUMN_DISPLAY_IMAGE, JournalFoodItems.COLUMN_QUANTITY,
                JournalFoodItems.COLUMN_QUANTITY_UNIT,JournalFoodItems.COLUMN_CALORIES,JournalFoodItems.COLUMN_TOTAL_FAT,JournalFoodItems.COLUMN_SATURATED_FAT,
                JournalFoodItems.COLUMN_POLYSATURATED_FAT,JournalFoodItems.COLUMN_MONOSATURATED_FAT,JournalFoodItems.COLUMN_TRANS_FAT,JournalFoodItems.COLUMN_CHOLESTEROL,JournalFoodItems.COLUMN_SODIUM,
                JournalFoodItems.COLUMN_POTASSIUM,JournalFoodItems.COLUMN_TOTAL_CARBOHYDRATES,JournalFoodItems.COLUMN_DIETARY_FIBER,JournalFoodItems.COLUMN_SUGARS,JournalFoodItems.COLUMN_PROTEIN,JournalFoodItems.COLUMN_VITAMIN_A
        ,JournalFoodItems.COLUMN_VITAMIN_C,JournalFoodItems.COLUMN_CALCIUM,JournalFoodItems.COLUMN_IRON,JournalFoodItems.COLUMN_CALORIES_UNIT,JournalFoodItems.COLUMN_TOTAL_FAT_UNIT,JournalFoodItems.COLUMN_SATURATED_FAT_UNIT
        ,JournalFoodItems.COLUMN_POLYSATURATED_FAT_UNIT,JournalFoodItems.COLUMN_MONOSATURATED_FAT_UNIT,JournalFoodItems.COLUMN_TRANS_FAT_UNIT,JournalFoodItems.COLUMN_CHOLESTEROL_UNIT,JournalFoodItems.COLUMN_SODIUM_UNIT,
                JournalFoodItems.COLUMN_POTASSIUM_UNIT,JournalFoodItems.COLUMN_TOTAL_CARBOHYDRATES_UNIT,JournalFoodItems.COLUMN_DIETARY_FIBER_UNIT,JournalFoodItems.COLUMN_SUGARS_UNIT,JournalFoodItems.COLUMN_PROTEIN_UNIT,
                JournalFoodItems.COLUMN_VITAMIN_A_UNIT,JournalFoodItems.COLUMN_VITAMIN_C_UNIT,JournalFoodItems.COLUMN_CALCIUM_UNIT,JournalFoodItems.COLUMN_IRON_UNIT,JournalFoodItems.COLUMN_CREATED_AT,
                JournalFoodItems.COLUMN_UPDATED_AT,JournalFoodItems.COLUMN_UPDATED_BY,JournalFoodItems.COLUMN_STATUS};
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }



} 