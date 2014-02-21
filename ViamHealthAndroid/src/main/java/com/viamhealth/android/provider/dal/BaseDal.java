package com.viamhealth.android.provider.dal;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.viamhealth.android.provider.DbHelper.ImmunizationDbHelper;

/**
 * Created by kunal on 21/2/14.
 */
public abstract class BaseDal {

    public enum SortOrder {
        DESC,
        ASC
    }

}
