package com.viamhealth.android.provider.handlers;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.viamhealth.android.model.BaseModel;
import com.viamhealth.android.provider.ScheduleContract;
import com.viamhealth.android.provider.parsers.JsonParser;
import com.viamhealth.android.sync.SyncHelper;
import com.viamhealth.android.sync.restclient.BaseEndPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by naren on 24/12/13.
 */
public class GoalHandler extends SyncHandler {

    public GoalHandler(Context context) {
        super(context);
    }

    @Override
    public Uri getContentUri() {
        return null;
    }

    @Override
    public JsonParser newParser() {
        return null;
    }

    @Override
    public ArrayList<ContentProviderOperation> parse(List<BaseModel> items) throws IOException {
        return null;
    }

    @Override
    public BaseEndPoint getEndPoint(Context context) {
        return null;
    }

    @Override
    public BaseModel newModel() {
        return null;
    }

    @Override
    public List<BaseModel> newList() {
        return null;
    }

    @Override
    protected ArrayList<ContentProviderOperation> save(BaseModel model, boolean shouldDelete) {
        return null;
    }

    @Override
    protected ArrayList<ContentProviderOperation> changeId(BaseModel before, BaseModel after) {
        return null;
    }

    @Override
    protected ArrayList<ContentProviderOperation> updateSyncStatus(BaseModel model, ScheduleContract.SyncStatus syncStatus) {
        return null;
    }

    @Override
    public BaseModel parseCursor(Cursor cursor) {
        return null;
    }
}
