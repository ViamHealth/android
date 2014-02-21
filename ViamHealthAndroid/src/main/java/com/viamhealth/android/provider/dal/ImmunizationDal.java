package com.viamhealth.android.provider.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.viamhealth.android.model.Immunization;
import com.viamhealth.android.provider.DbHelper.ImmunizationDbHelper;
import com.viamhealth.android.provider.contracts.ImmunizationContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunal on 21/2/14.
 */
public class ImmunizationDal extends BaseDal{

    private ImmunizationDbHelper mDbHelper;
    private SQLiteDatabase db;

    public enum SortBy {
        id,
        title,
        recommended_age
    }

    // Define a projection that specifies which columns from the database
    // you will actually use after this query.
    private final String[] PROJECTION = {
            ImmunizationContract.Entry._ID,
            ImmunizationContract.Entry.COLUMN_NAME_TITLE,
            ImmunizationContract.Entry.COLUMN_NAME_RECOMMENDED_AGE,
    };

    // How you want the results sorted in the resulting Cursor
    private final String SORT_ORDER =
            ImmunizationContract.Entry.COLUMN_NAME_ENTRY_ID + " DESC";

    public ImmunizationDal(Context context){
        this.mDbHelper = new ImmunizationDbHelper(context);
        //this.db = mDbHelper.getWritableDatabase();
    }

    public void open() throws SQLException {
        db = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    private Immunization cursorToModel(Cursor cursor) {
        Immunization model = new Immunization();
        model.setId(cursor.getLong(0));
        model.setTitle(cursor.getString(1));
        model.setRecommendedAge(cursor.getLong(2));
        return model;
    }


    public Immunization create(Immunization e){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ImmunizationContract.Entry.COLUMN_NAME_ENTRY_ID, e.getId());
        values.put(ImmunizationContract.Entry.COLUMN_NAME_TITLE, e.getTitle());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                ImmunizationContract.Entry.TABLE_NAME,
                ImmunizationContract.Entry.COLUMN_NAME_ENTRY_ID,
                values);
        //System.out.println(newRowId);
        //Immunization entry = getById(newRowId);
        return e;
    }

    public Immunization getById(Long id){
        String selection = ImmunizationContract.Entry.COLUMN_NAME_ENTRY_ID + " = ";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor c = db.query(
                ImmunizationContract.Entry.TABLE_NAME,  // The table to query
                PROJECTION,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                SORT_ORDER                                 // The sort order
        );
        c.moveToFirst();
        Immunization e = cursorToModel(c);
        return e;
    }

    public Immunization update(Immunization e){
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(ImmunizationContract.Entry.COLUMN_NAME_TITLE, "tit");
        // Which row to update, based on the ID
        String selection = ImmunizationContract.Entry.COLUMN_NAME_ENTRY_ID + " = ";
        String[] selectionArgs = { String.valueOf(e.getId()) };
        int count = db.update(
                ImmunizationContract.Entry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        Immunization entry = getById(e.getId());
        return entry;
    }

    public void delete(Immunization e){
        //String selection = ImmunizationContract.Entry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String selection = ImmunizationContract.Entry.COLUMN_NAME_ENTRY_ID + " = ";
        String[] selectionArgs = { String.valueOf(e.getId()) };
        // Issue SQL statement.
        db.delete(ImmunizationContract.Entry.TABLE_NAME, selection, selectionArgs);
    }


    public List<Immunization> getAll(SortBy sortBy, SortOrder sortOrder ){
        String sortString;
        String sortStringOrder;

        if(sortBy == null ){
            sortString = SORT_ORDER;
        }
        else {
            if(sortOrder == null || sortOrder == SortOrder.DESC ){
                sortStringOrder = " DESC ";
            } else {
                sortStringOrder = " ASC ";
            }
            if( sortBy == SortBy.id){
                sortString = ImmunizationContract.Entry.COLUMN_NAME_ENTRY_ID + sortStringOrder;
            }
            else if( sortBy == SortBy.title){
                sortString = ImmunizationContract.Entry.COLUMN_NAME_TITLE + sortStringOrder;
            }
            else if( sortBy == SortBy.recommended_age){
                sortString = ImmunizationContract.Entry.COLUMN_NAME_RECOMMENDED_AGE + sortStringOrder;
            }else {
                throw new IllegalArgumentException("Illegal sortBy in ImmunizationDal");
            }
        }
        List<Immunization> entries = new ArrayList<Immunization>();
        Cursor cursor = db.query(
                ImmunizationContract.Entry.TABLE_NAME,  // The table to query
                PROJECTION,                               // The columns to return
                null,//selection,                                // The columns for the WHERE clause
                null,//selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortString                                 // The sort order
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Immunization e = cursorToModel(cursor);
            entries.add(e);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return entries;

    }
}

