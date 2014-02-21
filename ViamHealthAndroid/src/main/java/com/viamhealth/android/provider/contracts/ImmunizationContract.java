package com.viamhealth.android.provider.contracts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.viamhealth.android.provider.DbHelper.ImmunizationDbHelper;

import java.util.Map;

/**
 * Created by kunal on 21/2/14.
 */
public class ImmunizationContract implements BaseContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public ImmunizationContract(){}



    /* Inner class that defines the table contents */
    public static abstract class Entry implements BaseColumns{
        public static final String TABLE_NAME = "immunization";
        public static final String COLUMN_NAME_ENTRY_ID = "immunization_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_RECOMMENDED_AGE = "recommended_age";

    }

    /* Creating table */
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Entry.TABLE_NAME + " (" +
                    Entry._ID + " INTEGER PRIMARY KEY," +
                    Entry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    Entry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    Entry.COLUMN_NAME_RECOMMENDED_AGE + TEXT_TYPE +
            " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Entry.TABLE_NAME;



}
