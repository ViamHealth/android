package com.viamhealth.android.provider.handlers;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import com.viamhealth.android.model.BaseModel;
import com.viamhealth.android.provider.ScheduleContract;
import com.viamhealth.android.provider.parsers.JsonParser;
import com.viamhealth.android.sync.SyncHelper;
import com.viamhealth.android.sync.restclient.BaseEndPoint;
import com.viamhealth.android.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by naren on 17/12/13.
 */
public abstract class SyncHandler {

    protected static Context mContext;
    protected final String TAG = LogUtils.makeLogTag(getClass());

    public SyncHandler(Context context) {
        mContext = context;
    }

    /**
     * parse will take in the List of BaseModel Items obtained from the API and construct the ContentproviderOperation to either insert
     * update or delete records in the local db
     *
     * @param json - raw json obtained from the api call
     * @param type - syncType {PUSH, PULL}
     * @return ArrayList<ContentProviderOperation> to either insert, update or delete records
     * @throws IOException
     */
    public abstract ArrayList<ContentProviderOperation> parse(final List<BaseModel> items) throws IOException;


    /**
     * This method returns new instance of end point extending BaseEndPoint
     * @return BaseEndPoint
     */
    public abstract BaseEndPoint getEndPoint(Context context);

    /**
     * Content Uri
     * @return
     */
    public abstract Uri getContentUri();

    /**
     * This is required to create the new instance of the model
     * @return the new BaseModel
     */
    public abstract BaseModel newModel();

    /**
     * This is required to create the new instance of the parser
     * @return the new JsonParser
     */
    public abstract JsonParser newParser();

    /**
     * This is required to create the new instance of the List
     * @return
     */
    public abstract List<BaseModel> newList();

    /**
     * this method should try to save the data locally
     * @param model
     * @param shouldDelete
     * @return
     */
    protected abstract ArrayList<ContentProviderOperation> save(final BaseModel model, final boolean shouldDelete);

    /**
     * To change the ids from negatives to positives
     * @param before
     * @param after
     * @return
     */
    protected abstract ArrayList<ContentProviderOperation> changeId(final BaseModel before, final BaseModel after);

    /**
     *
     * @param after
     * @param syncStatus
     * @return
     */
    protected abstract ArrayList<ContentProviderOperation> updateSyncStatus(BaseModel model, ScheduleContract.SyncStatus syncStatus);

    /**
     * the query using which we could get the data that has been locally modified
     * @return
     */
    public Uri getUriForLocallyModifiedData() {
        return ScheduleContract.getDataToBeUpdatedUri(getContentUri());
    };

    /**
     * Provided the cursor, create the BaseModel from it
     * Assumption: cursor pointing to the current row of the resultset
     * @param cursor pointing to the current row of model
     * @return
     */
    public abstract BaseModel parseCursor(Cursor cursor);

    public final void parseAndApply(String json, SyncHelper.SyncType type) throws IOException {
        try {
            final ContentResolver resolver = mContext.getContentResolver();
            ArrayList<ContentProviderOperation> batch = parse(newParser().parseArray(json));
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

    /**
     *
     * @param items
     * @return
     */
    protected ArrayList<ContentProviderOperation> afterPOST(List<BaseModel> itemsBefore, List<BaseModel> itemsAfter) {
        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
        int size = itemsBefore.size();
        for(int i=0; i<size; i++){
            BaseModel before = itemsBefore.get(i);
            BaseModel after = itemsAfter.get(i);
            batch.addAll(changeId(before, after));
            batch.addAll(save(after, false));
            batch.addAll(updateSyncStatus(after, ScheduleContract.SyncStatus.SUCCESS));
        }
        return batch;
    }

    /**
     *
     * @param items
     * @return
     */
    protected ArrayList<ContentProviderOperation> afterPUT(List<BaseModel> itemsBefore, List<BaseModel> itemsAfter) {
        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
        int size = itemsBefore.size();
        for(int i=0; i<size; i++){
            BaseModel before = itemsBefore.get(i);
            BaseModel after = itemsAfter.get(i);
            batch.addAll(save(after, false));
            batch.addAll(updateSyncStatus(after, ScheduleContract.SyncStatus.SUCCESS));
        }
        return batch;
    }

    /**
     *
     * @param items
     * @return
     */
    protected ArrayList<ContentProviderOperation> afterDELETE(List<BaseModel> itemsBefore, List<BaseModel> itemsAfter) {
        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
        int size = itemsBefore.size();
        for(int i=0; i<size; i++){
            BaseModel before = itemsBefore.get(i);
            batch.addAll(save(before, true));
        }
        return batch;
    }

    public ArrayList<ContentProviderOperation> push() throws IOException {
        Cursor c = mContext.getContentResolver().query(getUriForLocallyModifiedData(), null, null, null, null);
        List<BaseModel> itemsToPost = newList();
        List<BaseModel> itemsToPut = newList();
        List<BaseModel> itemsToDelete = newList();
        if(c!=null && c.getCount()>0 && c.moveToFirst()){
            for(; !c.isAfterLast(); c.moveToNext()){
                BaseModel modelElement = parseCursor(c);
                ScheduleContract.SyncStatus syncStatus = modelElement.getSyncStatus();
                if(modelElement.getId()<0) {
                    itemsToPost.add(modelElement);
                    updateSyncStatus(modelElement, ScheduleContract.SyncStatus.POST_INITIATED);
                }
                else if(modelElement.isDeleted()) {
                    itemsToDelete.add(modelElement);
                    updateSyncStatus(modelElement, ScheduleContract.SyncStatus.DELETE_INITIATED);
                }
                else {
                    itemsToPut.add(modelElement);
                    updateSyncStatus(modelElement, ScheduleContract.SyncStatus.PUT_INITIATED);
                }
            }
        }

        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
        BaseEndPoint endPoint = getEndPoint(mContext);
        try {
            List<BaseModel> itemsAferPost = endPoint.postData(itemsToPost);
            batch.addAll(afterPOST(itemsToPost, itemsAferPost));
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.LOGD(TAG, "Error while posting during syncing -> push ", e.getCause());
        }

        try {
            List<BaseModel> itemsAferPut = endPoint.putData(itemsToPut);
            batch.addAll(afterPUT(itemsToPut, itemsAferPut));
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.LOGD(TAG, "Error while putting during syncing -> push ", e.getCause());
        }

        try {
            List<BaseModel> itemsAfterDelete  = endPoint.deleteData(itemsToDelete);
            batch.addAll(afterDELETE(itemsToDelete, itemsAfterDelete));
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.LOGD(TAG, "Error while deleting during syncing -> push ", e.getCause());
        }

        return batch;
    }

    public ArrayList<ContentProviderOperation> fetchAndParse(SyncHelper.SyncType type) throws IOException {
        Date lastSynchedTime = getLastSynchedTime();
        LogUtils.LOGD(TAG, "lastSynchedTime " + lastSynchedTime);
        List<BaseModel> items = getEndPoint(mContext).getData(lastSynchedTime);
        ArrayList<ContentProviderOperation> batch = parse(items);
        if(batch!=null){//add the meta sync information
            ContentProviderOperation.Builder op = getSyncContentBuilder();
            op.withValue(ScheduleContract.Synchronize.SYNCHRONIZED, System.currentTimeMillis());
            batch.add(op.build());
        }
        return batch;
    }

    protected ContentProviderOperation.Builder getSyncBuilderForUpdate() {
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
        if(getLastSynchedTime()==null) {
            op = ContentProviderOperation.newInsert(ScheduleContract.Synchronize.CONTENT_URI);
            op.withValue(ScheduleContract.Synchronize.SYNC_TABLE_URI, selectionArgs[0]);
        }else{
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
}
