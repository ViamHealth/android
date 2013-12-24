package com.viamhealth.android.provider.handlers;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.net.Uri;

import com.viamhealth.android.sync.SyncHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by naren on 24/12/13.
 */
public class GoalHandler extends SyncHandler {

    public GoalHandler(Context context) {
        super(context);
    }

    @Override
    public ArrayList<ContentProviderOperation> parse(String json, SyncHelper.SyncType type) throws IOException {
        return null;
    }

    @Override
    public String fetch(Date lastSynchedTime) throws IOException {
        return null;
    }

    @Override
    public Uri getContentUri() {
        return null;
    }
}
