package com.viamhealth.android.provider.handlers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import com.viamhealth.android.auth.AccountGeneral;
import com.viamhealth.android.model.BaseModel;
import com.viamhealth.android.model.ReminderReadings;
import com.viamhealth.android.model.enums.BloodGroup;
import com.viamhealth.android.model.enums.Gender;
import com.viamhealth.android.model.enums.Relation;
import com.viamhealth.android.model.enums.ReminderTime;
import com.viamhealth.android.model.enums.ReminderType;
import com.viamhealth.android.model.enums.RepeatMode;
import com.viamhealth.android.model.enums.RepeatWeekDay;
import com.viamhealth.android.model.reminder.Action;
import com.viamhealth.android.model.reminder.Reminder;
import com.viamhealth.android.model.reminder.ReminderReading;
import com.viamhealth.android.model.reminder.ReminderTimeData;
import com.viamhealth.android.model.users.BMIProfile;
import com.viamhealth.android.model.users.Profile;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.provider.ScheduleContract;
import com.viamhealth.android.provider.parsers.JsonParser;
import com.viamhealth.android.provider.parsers.ReminderJasonParser;
import com.viamhealth.android.provider.parsers.UserJsonParser;
import com.viamhealth.android.sync.restclient.BaseEndPoint;
import com.viamhealth.android.sync.restclient.ReminderEndPoint;
import com.viamhealth.android.sync.restclient.UserEndPoint;
import com.viamhealth.android.utils.LogUtils;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by monj on 2/1/14.
 */
public class ReminderHandler extends SyncHandler{

    private final ReminderEndPoint reminderEndPoint;

    public ReminderHandler(Context context) {
        super(context);
        reminderEndPoint = new ReminderEndPoint(context);
    }

    @Override
    public ArrayList<ContentProviderOperation> parse(List<BaseModel> items) throws IOException {
        final ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
        int usersCounts = items.size();
        for(int i=0; i<usersCounts; i++)
            parseReminder((Reminder) items.get(i), batch);
        return batch;
    }

    @Override
    public Uri getContentUri() {
        return ScheduleContract.Reminders.CONTENT_REMINDER_URI;
    }

    @Override
    public BaseEndPoint getEndPoint(Context context) {
        return new ReminderEndPoint(context);
    }

    @Override
    public JsonParser newParser() {
        return new ReminderJasonParser();
    }

    @Override
    public BaseModel newModel() {
        return new Reminder();
    }

    @Override
    public List<BaseModel> newList() {
        return new ArrayList<BaseModel>();
    }



    @Override
    protected ArrayList<ContentProviderOperation> changeId(BaseModel b, BaseModel a) {
        Reminder before = (Reminder) b;
        Reminder after = (Reminder) a;

        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        ContentProviderOperation.Builder userBuilder = ContentProviderOperation.newUpdate(ScheduleContract.Reminders.buildReminderUri());
        userBuilder.withValue(ScheduleContract.Users.USER_ID, after.getId());
        batch.add(userBuilder.build());

        ContentProviderOperation.Builder profileBuilder = ContentProviderOperation.newUpdate(ScheduleContract.Reminders.buildReminderReadingsUri());
        profileBuilder.withValue(ScheduleContract.Users.USER_ID, after.getId());
        batch.add(profileBuilder.build());


        return batch;
    }

    @Override
    protected ArrayList<ContentProviderOperation> updateSyncStatus(BaseModel a, ScheduleContract.SyncStatus syncStatus) {
        User after = (User) a;
        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        ContentProviderOperation.Builder userBuilder = ContentProviderOperation.newUpdate(ScheduleContract.Reminders.buildReminderUri());
        userBuilder.withValue(ScheduleContract.Users.SYNC_STATUS, syncStatus.ordinal());
        batch.add(userBuilder.build());

        ContentProviderOperation.Builder profileBuilder = ContentProviderOperation.newUpdate(ScheduleContract.Reminders.buildReminderReadingsUri());
        profileBuilder.withValue(ScheduleContract.Users.SYNC_STATUS, syncStatus.ordinal());
        batch.add(profileBuilder.build());


        return batch;
    }

    @Override
    public ArrayList<ContentProviderOperation> save(final BaseModel model1, final boolean shouldDelete) {
        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
        Reminder reminder = (Reminder) model1;

        boolean shouldUpdate = false;

        if(reminder.getId()>0) shouldUpdate = true;

        ContentProviderOperation.Builder builder = null, pBuilder = null, bmiBuilder = null, syncBuilder = null;
        //if(shouldUpdate){
            builder = ContentProviderOperation.newUpdate(ScheduleContract.Reminders.buildReminderUri());
            pBuilder = ContentProviderOperation.newUpdate(ScheduleContract.Reminders.buildReminderReadingsUri());
    /*
    }else{
            builder = ContentProviderOperation.newInsert(ScheduleContract.Users.buildUserUri(null));
            pBuilder = ContentProviderOperation.newInsert(ScheduleContract.Users.buildUserProfileUri(user.getId(), true));
            pBuilder.withValueBackReference(ScheduleContract.UserForeignKeyColumn.USER_ID, 0);
            bmiBuilder = ContentProviderOperation.newInsert(ScheduleContract.Users.buildUserHealthProfileUri(user.getId(), true));
            bmiBuilder.withValueBackReference(ScheduleContract.UserForeignKeyColumn.USER_ID, 0);
        }
*/
        builder.withValue(ScheduleContract.Reminders.UPDATED, System.currentTimeMillis());
        builder.withValue(ScheduleContract.Reminders.SYNC_STATUS, ScheduleContract.SyncStatus.PENDING_UPDATE.ordinal());
        if(shouldDelete) builder.withValue(ScheduleContract.Reminders.IS_DELETED, true);
        batch.add(constructReminder(builder, reminder).build());
/*

        pBuilder.withValue(ScheduleContract.Users.UPDATED, System.currentTimeMillis());
        pBuilder.withValue(ScheduleContract.Users.SYNC_STATUS, ScheduleContract.SyncStatus.PENDING_UPDATE.ordinal());
        if(shouldDelete) builder.withValue(ScheduleContract.Reminders.IS_DELETED, true);
        batch.add(constructReminderReadings(pBuilder,reminderReading).build());
*/
        syncBuilder = getSyncBuilderForUpdate();
        batch.add(syncBuilder.build());

        return batch;
    }


    public boolean saveLocally(final BaseModel model, final boolean shouldDelete) {
        ArrayList<ContentProviderOperation> batch = save(model, shouldDelete);

        try {
            ContentProviderResult[] results = mContext.getContentResolver().applyBatch(ScheduleContract.CONTENT_AUTHORITY, batch);
            LogUtils.LOGD(TAG, "updated user - " + results);
        } catch (RemoteException e) {
            e.printStackTrace();
            LogUtils.LOGE(TAG, e.getMessage(), e);
            return false;
        } catch (OperationApplicationException e) {
            e.printStackTrace();
            LogUtils.LOGE(TAG, e.getMessage(), e);
            return false;
        }

        return true;
    }

    private boolean deleteReminder(User user) {
        try {
            LogUtils.LOGD(TAG, "deleting user " + user);
            mContext.getContentResolver().delete(ScheduleContract.Reminders.buildReminderUri(), null, null);
            LogUtils.LOGD(TAG, "deleted");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.LOGE(TAG, e.getMessage(), e);
            return false;
        }
        return true;
    }

    public static ContentProviderOperation.Builder constructReminder(ContentProviderOperation.Builder builder, Reminder reminder){
        builder.withValue(ScheduleContract.Reminders.USER_ID, reminder.getId());
        builder.withValue(ScheduleContract.Reminders.TYPE, reminder.getType());
        builder.withValue(ScheduleContract.Reminders.NAME, reminder.getName());
        builder.withValue(ScheduleContract.Reminders.DETAILS, reminder.getDetails());
        builder.withValue(ScheduleContract.Reminders.MORNING_COUNT, reminder.getReminderTimeData(ReminderTime.Morning));
        builder.withValue(ScheduleContract.Reminders.AFTERNOON_COUNT, reminder.getReminderTimeData(ReminderTime.Noon));
        builder.withValue(ScheduleContract.Reminders.NIGHT_COUNT, reminder.getReminderTimeData(ReminderTime.Night));
        builder.withValue(ScheduleContract.Reminders.START_DATE, reminder.getStartDate());
        builder.withValue(ScheduleContract.Reminders.END_DATE, reminder.getEndDate());
        builder.withValue(ScheduleContract.Reminders.REPEAT_MODE, reminder.getRepeatMode());
        builder.withValue(ScheduleContract.Reminders.REPEAT_DAY, reminder.getRepeatDay());
        builder.withValue(ScheduleContract.Reminders.REPEAT_HOUR, reminder.getRepeatHour());
        builder.withValue(ScheduleContract.Reminders.REPEAT_MIN, reminder.getRepeatMin());
        builder.withValue(ScheduleContract.Reminders.REPEAT_WEEKDAY, reminder.getRepeatWeekDay());
        builder.withValue(ScheduleContract.Reminders.REPEAT_EVERY_X, reminder.getRepeatEveryX());
        builder.withValue(ScheduleContract.Reminders.REPEAT_I_COUNTER, reminder.getRepeatICounter());
        builder.withValue(ScheduleContract.Reminders.CREATED_AT,"");
        builder.withValue(ScheduleContract.Reminders.UPDATED_AT,"");
        builder.withValue(ScheduleContract.Reminders.IS_DELETED, false);
        return builder;
    }

    public static ContentProviderOperation.Builder constructReminderReadings(ContentProviderOperation.Builder builder,
                                                                        ReminderReading reminderreading){
        builder.withValue(ScheduleContract.Reminders.REMINDER_ID, reminderreading.getId());
        builder.withValue(ScheduleContract.Reminders.MORNING_CHECK, reminderreading.getAction(ReminderTime.Morning).isCheck());
        builder.withValue(ScheduleContract.Reminders.AFTERNOON_CHECK, reminderreading.getAction(ReminderTime.Noon).isCheck());
        builder.withValue(ScheduleContract.Reminders.EVENING_CHECK, reminderreading.getAction(ReminderTime.Evening).isCheck());
        builder.withValue(ScheduleContract.Reminders.NIGHT_CHECK, reminderreading.getAction(ReminderTime.Night).isCheck());
        builder.withValue(ScheduleContract.Reminders.COMPLETE_CHECK, " ");
        builder.withValue(ScheduleContract.Reminders.UPDATED_BY, " ");
        builder.withValue(ScheduleContract.Reminders.READING_DATE, " ");
        builder.withValue(ScheduleContract.Reminders.IS_DELETED, false);
        return builder;
    }


    public ReminderReading parseCursorReminderReading(Cursor cursor){
        /** Assuming that the cursor is pointing to the r eminder row obtained by the user query **/
        if(cursor==null || cursor.isAfterLast() || cursor.isBeforeFirst() || cursor.isClosed())
            return null;

        long userId = cursor.getLong(cursor.getColumnIndex(ScheduleContract.Reminders.TABLE_ALIAS+"."+ScheduleContract.Reminders.USER_ID));

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        ReminderReading reminderReading=new ReminderReading();

        reminderReading.setId(cursor.getLong(cursor.getColumnIndex(ScheduleContract.Reminders.REMINDER_ID)));
        Action morningAction = new Action();
        morningAction.setCheck(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(ScheduleContract.Reminders.MORNING_CHECK))));
        reminderReading.putAction(ReminderTime.Morning, morningAction);
        try {
        Action noonAction = new Action();
        noonAction.setCheck(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(ScheduleContract.Reminders.AFTERNOON_CHECK))));
        reminderReading.putAction(ReminderTime.Noon, noonAction);

        Action eveningCheck = new Action();
        eveningCheck.setCheck(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(ScheduleContract.Reminders.EVENING_CHECK))));
        reminderReading.putAction(ReminderTime.Evening, eveningCheck);

        Action nightCheck = new Action();
        eveningCheck.setCheck(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(ScheduleContract.Reminders.NIGHT_CHECK))));
        reminderReading.putAction(ReminderTime.Night, nightCheck);


        Action completeCheck = new Action();
        eveningCheck.setCheck(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(ScheduleContract.Reminders.COMPLETE_CHECK))));
        reminderReading.putAction(ReminderTime.Night, completeCheck);

        reminderReading.setUpdatedBy(cursor.getString(cursor.getColumnIndex(ScheduleContract.Reminders.UPDATED_BY)));
        reminderReading.setReadingDate(formater.parse(cursor.getString(cursor.getColumnIndex(ScheduleContract.Reminders.READING_DATE))));
    } catch (ParseException eh) {
        eh.printStackTrace();
        }
        return reminderReading;

    }

    @Override
    public Reminder parseCursor(Cursor cursor){
        /** Assuming that the cursor is pointing to the r eminder row obtained by the user query **/
        if(cursor==null || cursor.isAfterLast() || cursor.isBeforeFirst() || cursor.isClosed())
            return null;

        long userId = cursor.getLong(cursor.getColumnIndex(ScheduleContract.Reminders.TABLE_ALIAS+"."+ScheduleContract.Reminders.USER_ID));

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Reminder reminder = new Reminder();
        reminder.setId(userId);
        reminder.setType(ReminderType.values()[cursor.getInt(cursor.getColumnIndex(ScheduleContract.Reminders.TYPE))]);
        reminder.setName(cursor.getString(cursor.getColumnIndex(ScheduleContract.Reminders.NAME)));
        reminder.setDetails(cursor.getString(cursor.getColumnIndex(ScheduleContract.Reminders.DETAILS)));

        ReminderTimeData morningData = new ReminderTimeData();
        morningData.setCount(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Reminders.MORNING_COUNT)));
        reminder.putReminderTimeData(ReminderTime.Morning,morningData);

        ReminderTimeData noonData = new ReminderTimeData();
        morningData.setCount(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Reminders.AFTERNOON_COUNT)));
        reminder.putReminderTimeData(ReminderTime.Noon,noonData);

        ReminderTimeData eveningData = new ReminderTimeData();
        eveningData.setCount(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Reminders.EVENING_COUNT)));
        reminder.putReminderTimeData(ReminderTime.Evening,eveningData);

        ReminderTimeData nightData = new ReminderTimeData();
        morningData.setCount(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Reminders.NIGHT_COUNT)));
        reminder.putReminderTimeData(ReminderTime.Night,nightData);



        try {
            reminder.setStartDate(formater.parse(cursor.getString(cursor.getColumnIndex(ScheduleContract.Reminders.START_DATE))));
            reminder.setEndDate(formater.parse(cursor.getString(cursor.getColumnIndex(ScheduleContract.Reminders.END_DATE))));


            reminder.setRepeatMode(RepeatMode.values()[cursor.getColumnIndex(ScheduleContract.Reminders.REPEAT_MODE)]);
            reminder.setRepeatDay(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Reminders.REPEAT_DAY)));
            reminder.setRepeatHour(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Reminders.REPEAT_HOUR)));
            reminder.setRepeatMin(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Reminders.REPEAT_MIN)));
            reminder.setRepeatWeekDay(RepeatWeekDay.values()[cursor.getInt(cursor.getColumnIndex(ScheduleContract.Reminders.REPEAT_WEEKDAY))]);
            reminder.setRepeatEveryX(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Reminders.REPEAT_EVERY_X)));
            reminder.setRepeatICounter(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Reminders.REPEAT_EVERY_X)));

            reminder.setCreated(formater.parse(cursor.getString(cursor.getColumnIndex(ScheduleContract.Reminders.CREATED_AT))));
            reminder.setUpdated(formater.parse(cursor.getString(cursor.getColumnIndex(ScheduleContract.Reminders.UPDATED_AT))));


            } catch (ParseException e) {
                e.printStackTrace();


        }
        return reminder;
    }



    /** takes care of inserting a new value or updating an existing record **/

    public static void parseReminderReadings(ReminderReading reminderReading, ArrayList<ContentProviderOperation> batch){
        /** Add user meta data **/
        Uri uri = null;

        if(reminderReading==null)
            return;

        boolean isUpdate = false;


        uri = ScheduleContract.Reminders.buildReminderUri();
        uri = ScheduleContract.addCallerIsSyncAdapterParameter(uri);

        String syncTimeUpdateColumn = ScheduleContract.SyncColumns.SYNCHRONIZED;

        int index = batch.size();
        //construct the user
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(uri);
        if(syncTimeUpdateColumn!=null)
            builder.withValue(syncTimeUpdateColumn, System.currentTimeMillis());

        builder.withValue(ScheduleContract.Reminders.SYNC_STATUS, ScheduleContract.SyncStatus.SUCCESS.ordinal());
        batch.add(constructReminderReadings(builder, reminderReading).build());

        //construct the profile
        if(reminderReading!=null){
            ContentProviderOperation.Builder reminderReadingBuilder = null;
            Uri reminderReadingUri = ScheduleContract.Reminders.buildReminderReadingsUri();
            if(syncTimeUpdateColumn!=null)
                reminderReadingUri = ScheduleContract.addCallerIsSyncAdapterParameter(reminderReadingUri);

            if(isUpdate){
                reminderReadingBuilder = ContentProviderOperation.newUpdate(reminderReadingUri);
            }else{
                reminderReadingBuilder = ContentProviderOperation.newInsert(reminderReadingUri);
                reminderReadingBuilder.withValueBackReference(ScheduleContract.ReminderForeignKeyColumn.USER_ID, index);
            }

            if(syncTimeUpdateColumn!=null)
                reminderReadingBuilder.withValue(syncTimeUpdateColumn, System.currentTimeMillis());

            reminderReadingBuilder.withValue(ScheduleContract.Users.SYNC_STATUS, ScheduleContract.SyncStatus.SUCCESS.ordinal());
            reminderReadingBuilder = constructReminderReadings(reminderReadingBuilder, reminderReading);

            batch.add(reminderReadingBuilder.build());
        }

    }


    public static void parseReminder(Reminder reminder, ArrayList<ContentProviderOperation> batch){
        /** Add user meta data **/
        Uri uri = null;

        if(reminder==null)
            return;

        boolean isUpdate = false;
        if(reminder.getId()>0){
            isUpdate = false;
        }else{
            isUpdate = true;
        }

        uri = ScheduleContract.Reminders.buildReminderUri();
        uri = ScheduleContract.addCallerIsSyncAdapterParameter(uri);

        String syncTimeUpdateColumn = ScheduleContract.SyncColumns.SYNCHRONIZED;

        int index = batch.size();
        //construct the user
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(uri);
        if(syncTimeUpdateColumn!=null)
            builder.withValue(syncTimeUpdateColumn, System.currentTimeMillis());

        builder.withValue(ScheduleContract.Reminders.SYNC_STATUS, ScheduleContract.SyncStatus.SUCCESS.ordinal());
        batch.add(constructReminder(builder, reminder).build());

        //construct the profile
        if(reminder!=null){
            ContentProviderOperation.Builder reminderBuilder = null;
            Uri reminderUri = ScheduleContract.Reminders.buildReminderUri();
            if(syncTimeUpdateColumn!=null)
                reminderUri = ScheduleContract.addCallerIsSyncAdapterParameter(reminderUri);

            if(isUpdate){
                reminderBuilder = ContentProviderOperation.newUpdate(reminderUri);
            }else{
                reminderBuilder = ContentProviderOperation.newInsert(reminderUri);
                reminderBuilder.withValueBackReference(ScheduleContract.UserForeignKeyColumn.USER_ID, index);
            }

            if(syncTimeUpdateColumn!=null)
                reminderBuilder.withValue(syncTimeUpdateColumn, System.currentTimeMillis());

            reminderBuilder.withValue(ScheduleContract.Reminders.SYNC_STATUS, ScheduleContract.SyncStatus.SUCCESS.ordinal());
            reminderBuilder = constructReminder(reminderBuilder, reminder);

            batch.add(reminderBuilder.build());
        }

        //construct the health profile
    }

}
