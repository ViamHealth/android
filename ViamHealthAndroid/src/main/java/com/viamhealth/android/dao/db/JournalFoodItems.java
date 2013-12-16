package com.viamhealth.android.dao.db;

/**
 * Created by Administrator on 12/10/13.
 */

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class JournalFoodItems {

    // Database table
    public static final String TABLE_FOOD_ITEMS = "tbl_food_items";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DISPLAY_IMAGE = "display_image";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_QUANTITY_UNIT = "quantity_unit";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_TOTAL_FAT = "total_fat";
    public static final String COLUMN_SATURATED_FAT = "saturated_fat";
    public static final String COLUMN_POLYSATURATED_FAT = "polyunsaturated_fat";
    public static final String COLUMN_MONOSATURATED_FAT = "monounsaturated_fat";
    public static final String COLUMN_TRANS_FAT = "trans_fat";
    public static final String COLUMN_CHOLESTEROL = "cholesterol";
    public static final String COLUMN_SODIUM = "sodium";
    public static final String COLUMN_POTASSIUM = "potassium";
    public static final String COLUMN_TOTAL_CARBOHYDRATES = "total_carbohydrates";
    public static final String COLUMN_DIETARY_FIBER = "dietary_fiber";
    public static final String COLUMN_SUGARS = "sugars";
    public static final String COLUMN_PROTEIN = "protein";
    public static final String COLUMN_VITAMIN_A = "vitamin_a";
    public static final String COLUMN_VITAMIN_C = "vitamin_c";
    public static final String COLUMN_CALCIUM = "calcium";
    public static final String COLUMN_IRON = "iron";
    public static final String COLUMN_CALORIES_UNIT = "calories_unit";
    public static final String COLUMN_TOTAL_FAT_UNIT = "total_fat_unit";
    public static final String COLUMN_SATURATED_FAT_UNIT = "saturated_fat_unit";
    public static final String COLUMN_POLYSATURATED_FAT_UNIT = "polyunsaturated_fat_unit";
    public static final String COLUMN_MONOSATURATED_FAT_UNIT = "monounsaturated_fat_unit";
    public static final String COLUMN_TRANS_FAT_UNIT = "trans_fat_unit";
    public static final String COLUMN_CHOLESTEROL_UNIT = "cholesterol_unit";
    public static final String COLUMN_SODIUM_UNIT = "sodium_unit";
    public static final String COLUMN_POTASSIUM_UNIT = "potassium_unit";
    public static final String COLUMN_TOTAL_CARBOHYDRATES_UNIT = "total_carbohydrates_unit";
    public static final String COLUMN_DIETARY_FIBER_UNIT = "dietary_fiber_unit";
    public static final String COLUMN_SUGARS_UNIT = "sugars_unit";
    public static final String COLUMN_PROTEIN_UNIT = "protein_unit";
    public static final String COLUMN_VITAMIN_A_UNIT = "vitamin_a_unit";
    public static final String COLUMN_VITAMIN_C_UNIT = "vitamin_c_unit";
    public static final String COLUMN_CALCIUM_UNIT = "calcium_unit";
    public static final String COLUMN_IRON_UNIT = "iron_unit";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";
    public static final String COLUMN_UPDATED_BY = "updated_by";
    public static final String COLUMN_STATUS = "status";



    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_FOOD_ITEMS
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null,"
            + COLUMN_DISPLAY_IMAGE + " text not null, "
            + COLUMN_QUANTITY + " double not null,"
            + COLUMN_QUANTITY_UNIT + " text not null,"
            + COLUMN_CALORIES + " double not null,"
            + COLUMN_TOTAL_FAT + " double not null,"
            + COLUMN_SATURATED_FAT + " double not null,"
            + COLUMN_POLYSATURATED_FAT + " double not null,"
            + COLUMN_MONOSATURATED_FAT + " double not null,"
            + COLUMN_TRANS_FAT + " double not null,"
            + COLUMN_CHOLESTEROL + " double not null,"
            + COLUMN_SODIUM + " double not null,"
            + COLUMN_POTASSIUM + " double not null,"
            + COLUMN_TOTAL_CARBOHYDRATES + " double not null,"
            + COLUMN_SUGARS + " double not null,"
            + COLUMN_PROTEIN + " double not null,"
            + COLUMN_VITAMIN_A + " double not null,"
            + COLUMN_VITAMIN_C + " double not null,"
            + COLUMN_CALCIUM + " double not null,"
            + COLUMN_IRON + " double not null,"
            + COLUMN_CALORIES_UNIT + " text not null,"
            + COLUMN_TOTAL_FAT_UNIT + " text not null,"
            + COLUMN_SATURATED_FAT_UNIT + " text not null,"
            + COLUMN_POLYSATURATED_FAT_UNIT + " text not null,"
            + COLUMN_MONOSATURATED_FAT_UNIT + " text not null,"
            + COLUMN_CHOLESTEROL_UNIT + " text not null,"
            + COLUMN_SODIUM_UNIT + " text not null,"
            + COLUMN_POTASSIUM_UNIT + " text not null,"
            + COLUMN_TOTAL_CARBOHYDRATES_UNIT + " text not null,"
            + COLUMN_DIETARY_FIBER_UNIT + " text not null,"
            + COLUMN_SUGARS_UNIT + " text not null,"
            + COLUMN_PROTEIN_UNIT + " text not null,"
            + COLUMN_VITAMIN_A_UNIT + " text not null,"
            + COLUMN_VITAMIN_C_UNIT + " text not null,"
            + COLUMN_CALCIUM_UNIT + " text not null,"
            + COLUMN_IRON_UNIT + " text not null,"
            + COLUMN_CREATED_AT + " text not null,"
            + COLUMN_UPDATED_AT + " text not null,"
            + COLUMN_UPDATED_BY + " int not null,"
            + COLUMN_STATUS + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(JournalDietTracker.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD_ITEMS);
        onCreate(database);
    }
}
