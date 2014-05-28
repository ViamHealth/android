package com.viamhealth.android.dao.db;

import java.util.ArrayList;

import com.viamhealth.android.model.ReminderData;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataBaseAdapter {
    public String TAG = "DatabaseAdapter";
    private static final String DATABASE_NAME = "ViamHealth.com.viamhealth.android.dao.db";
    private static final String CREATE_GOAL = "create table if not exists goal(" +
            "id integer primary key AUTOINCREMENT, gn varchar(20), " +
            "gd varchar(100), gv varchar(500), dates varchar(300), val varchar(300),dates1 varchar(100));";
    private static final String CREATE_Reminder = "create table if not exists reminder(" +
            "id integer primary key AUTOINCREMENT, name varchar(100), " +
            "date varchar(100));";
    SQLiteDatabase myDatabase;
    private Context context;

    public DataBaseAdapter(Context con) {
        this.context = con;

    }

    public void createDatabase() {
        try {
            myDatabase = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
            Log.e(TAG, "Step 1");

            myDatabase.execSQL(CREATE_GOAL);
            Log.e(TAG, "Step 2");

            myDatabase.execSQL(CREATE_Reminder);
            Log.e(TAG, "Step 3");

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            myDatabase.close();
        }
    }

    public void insertDefaulValues() {
        try {
            myDatabase = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
            String query = "insert or ignore into goal (gn,gd,gv,dates,val,dates) values ('Weight','Lower','8','2013-7-6,2013-7-10,2013-7-14,2013-7-18,2013-7-22,2013-7-26','8,20,10,35,46','');";
            String query1 = "insert or ignore into goal (gn,gd,gv,dates,val,dates) values ('Cholestrol','Medium','20','2013-7-6,2013-7-10,2013-7-14,2013-7-18,2013-7-22,2013-7-26','20,36,31,40,58','');";
            String reminder1 = "insert or ignore into reminder (name,date) values ('Take a Triglyceride test','Monday 21 July 2013');";
            myDatabase.execSQL(query);
            myDatabase.execSQL(query1);
            myDatabase.execSQL(reminder1);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            myDatabase.close();
        } finally {
            myDatabase.close();
        }
    }


    public void insertReminder(String name, String date) {
        try {
            myDatabase = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
            String query = "insert or ignore into reminder (name,date) values ('" + name + "','" + date + "');";
            myDatabase.execSQL(query);
            myDatabase.close();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            myDatabase.close();
        } finally {
            myDatabase.close();
        }
    }

    public ArrayList<ReminderData> getReminder() {
        ArrayList<ReminderData> lstdata = new ArrayList<ReminderData>();
        try {
            myDatabase = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
            String query2 = "SELECT * from reminder";
            Cursor resultset = myDatabase.rawQuery(query2, null);
            while (resultset.moveToNext()) {
                lstdata.add(new ReminderData(resultset.getString(0), resultset.getString(1), resultset.getString(2)));
            }
            resultset.close();
            myDatabase.close();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            myDatabase.close();
        } finally {
            myDatabase.close();
        }
        return lstdata;
    }

    public void manageReminder(String id) {
        try {
            myDatabase = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
            String query2 = "delete from reminder where id='" + id + "'";
            myDatabase.execSQL(query2);

            myDatabase.close();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            myDatabase.close();
        } finally {
            myDatabase.close();
        }
    }

}
