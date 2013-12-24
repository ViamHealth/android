package com.viamhealth.android.dao;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.viamhealth.android.model.users.User;
import com.viamhealth.android.provider.handlers.UserHandler;

import java.util.ArrayList;

/**
 * Created by naren on 18/12/13.
 */
public class UserDAO {

    public void addUser(User user) {
        Uri newUri;

        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        UserHandler.parseUser(user, batch, null);


    }
}
