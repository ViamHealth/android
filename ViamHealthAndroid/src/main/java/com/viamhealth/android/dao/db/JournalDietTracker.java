package com.viamhealth.android.dao.db;

/**
 * Created by Administrator on 12/10/13.
 */

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class JournalDietTracker {

    // Database table
    public static final String TABLE_DIET_TRACKER = "tbl_diet_tracker";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_SUMMARY = "summary";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String USER_ID = "user_id";
    public static final String FOOD_ITEM_ID = "food_item_id";
    public static final String FOOD_QUANTITY_MULTIPLIER = "food_quantity_multiplier";
    public static final String MEAL_TYPE = "meal_type";
    public static final String DIET_DATE = "diet_date";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String UPDATED_BY = "updated_by";


    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_DIET_TRACKER
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + USER_ID + " string not null,"
            + FOOD_ITEM_ID + " string not null, "
            + FOOD_QUANTITY_MULTIPLIER + " string not null,"
            + MEAL_TYPE + " text not null,"
            + DIET_DATE + " text not null,"
            + CREATED_AT + " text not null,"
            + UPDATED_AT + " text not null,"
            + UPDATED_BY + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(JournalDietTracker.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_DIET_TRACKER);
        onCreate(database);
    }
}
