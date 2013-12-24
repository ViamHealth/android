package com.viamhealth.android.provider.handlers;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import com.viamhealth.android.provider.ScheduleContract;
import com.viamhealth.android.sync.SyncHelper;
import com.viamhealth.android.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by naren on 17/12/13.
 */
public abstract class SyncHandler {

    protected static Context mContext;
    protected final String TAG = LogUtils.makeLogTag(getClass());

    public SyncHandler(Context context) {
        mContext = context;
    }

    public abstract ArrayList<ContentProviderOperation> parse(String json, SyncHelper.SyncType type) throws IOException;
    public abstract String fetch(Date lastSynchedTime) throws IOException;
    public abstract Uri getContentUri();

    public final void parseAndApply(String json, SyncHelper.SyncType type) throws IOException {
        try {
            final ContentResolver resolver = mContext.getContentResolver();
            ArrayList<ContentProviderOperation> batch = parse(json, type);
            resolver.applyBatch(ScheduleContract.CONTENT_AUTHORITY, batch);
        } catch (RemoteException e) {
            throw new RuntimeException("Problem applying batch operation", e);
        } catch (OperationApplicationException e) {
            throw new RuntimeException("Problem applying batch operation", e);
        }
    }

    public boolean hasSynched(Date from, Date to) {
        Date lastSynchedTime = getLastSynchedTime();

        if(lastSynchedTime!=null && lastSynchedTime.getTime()>=from.getTime() && lastSynchedTime.getTime()<=to.getTime())
            return true;

        return false;
    }

    public ArrayList<ContentProviderOperation> fetchAndParse(SyncHelper.SyncType type) throws IOException {
        Date lastSynchedTime = getLastSynchedTime();
        LogUtils.LOGD(TAG, "lastSynchedTime " + lastSynchedTime);
        ArrayList<ContentProviderOperation> batch = parse(fetch(lastSynchedTime), type);
        if(batch!=null){//add the meta sync information
            ContentProviderOperation.Builder op = getSyncContentBuilder();
            op.withValue(ScheduleContract.Synchronize.SYNCHRONIZED, System.currentTimeMillis());
            batch.add(op.build());
        }
        return batch;
    }

    public ContentProviderOperation.Builder getSyncBuilderForUpdate() {
        Date lastSynchedTime = getLastSynchedTime();
        ContentProviderOperation.Builder op = getSyncContentBuilder();
        op.withValue(ScheduleContract.Synchronize.UPDATED, System.currentTimeMillis());
        op.withValue(ScheduleContract.Synchronize.SYNC_STATUS, ScheduleContract.SyncStatus.PENDING_UPDATE.ordinal());
        return op;
    }

    private ContentProviderOperation.Builder getSyncContentBuilder() {
        String selection = ScheduleContract.Synchronize.SYNC_TABLE_URI + "=?";
        String[] selectionArgs = new String[]{getContentUri().toString()};//ScheduleContract.Users.CONTENT_USER_URI;
        ContentProviderOperation.Builder op = null;
        if(getLastSynchedTime()==null)
            op = ContentProviderOperation.newInsert(ScheduleContract.Synchronize.CONTENT_URI);
        else{
            op = ContentProviderOperation.newUpdate(ScheduleContract.Synchronize.CONTENT_URI);
            op.withSelection(selection, selectionArgs);
        }
        return op;
    }

    public Date getLastSynchedTime() {
        String selection = ScheduleContract.Synchronize.SYNC_TABLE_URI + "=?";
        String[] selectionArgs = new String[]{getContentUri().toString()};//ScheduleContract.Users.CONTENT_USER_URI;
        Cursor c = mContext.getContentResolver().query(ScheduleContract.Synchronize.CONTENT_URI, null, selection, selectionArgs, null);
        if(c==null) return null;
        if(c.getCount() >0 && c.moveToFirst()){
            if(!c.isAfterLast()){
                long time = c.getLong(c.getColumnIndex(ScheduleContract.Synchronize.SYNCHRONIZED));
                if(time>0)
                    return new Date(time);
            }
        }
        return null;
    }

//    public abstract <T> List<T> getLocallyInsertedData();
//    public abstract <T> List<T> getLocallyUpdatedData();
//    public abstract <T> List<T> getLocallyDeletedData();
//
//    public abstract <T> List<T> postData(List<T> items);
//    public abstract <T> List<T> putData(List<T> items);
//    public abstract <T> List<T> deleteData(List<T> items);
//
//    public updatedRemoteData() {
//        //get the elements that has been changed locally
//        //inserted
//
//        //updated
//        //deleted
//    }

}
