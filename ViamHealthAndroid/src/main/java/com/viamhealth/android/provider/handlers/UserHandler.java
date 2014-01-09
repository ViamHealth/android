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
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.BaseModel;
import com.viamhealth.android.model.enums.BloodGroup;
import com.viamhealth.android.model.enums.Gender;
import com.viamhealth.android.model.enums.Relation;
import com.viamhealth.android.model.users.BMIProfile;
import com.viamhealth.android.model.users.Profile;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.provider.ScheduleContract;
import com.viamhealth.android.provider.parsers.JsonParser;
import com.viamhealth.android.provider.parsers.UserJsonParser;
import com.viamhealth.android.sync.SyncHelper;
import com.viamhealth.android.sync.restclient.BaseEndPoint;
import com.viamhealth.android.sync.restclient.UserEndPoint;
import com.viamhealth.android.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by naren on 17/12/13.
 */
public class UserHandler extends SyncHandler {

    private final UserEndPoint userEndPoint;

    public UserHandler(Context context) {
        super(context);
        userEndPoint = new UserEndPoint(context);
    }

    @Override
    public ArrayList<ContentProviderOperation> parse(List<BaseModel> items) throws IOException {
        final ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
        int usersCounts = items.size();
        for(int i=0; i<usersCounts; i++)
            parseUser((User)items.get(i), batch);
        return batch;
    }

    @Override
    public Uri getContentUri() {
        return ScheduleContract.Users.CONTENT_USER_URI;
    }

    @Override
    public BaseEndPoint getEndPoint(Context context) {
        return new UserEndPoint(context);
    }

    @Override
    public JsonParser newParser() {
        return new UserJsonParser();
    }

    @Override
    public BaseModel newModel() {
        return new User();
    }

    @Override
    public List<BaseModel> newList() {
        return new ArrayList<BaseModel>();
    }

    @Override
    protected ArrayList<ContentProviderOperation> changeId(BaseModel b, BaseModel a) {
        User before = (User) b;
        User after = (User) a;

        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        ContentProviderOperation.Builder userBuilder = ContentProviderOperation.newUpdate(ScheduleContract.Users.buildUserUri(before.getId()));
        userBuilder.withValue(ScheduleContract.Users.USER_ID, after.getId());
        batch.add(userBuilder.build());

        ContentProviderOperation.Builder profileBuilder = ContentProviderOperation.newUpdate(ScheduleContract.Users.buildUserProfileUri(before.getId(), false));
        profileBuilder.withValue(ScheduleContract.Users.USER_ID, after.getId());
        batch.add(profileBuilder.build());

        ContentProviderOperation.Builder bmiBuilder = ContentProviderOperation.newUpdate(ScheduleContract.Users.buildUserHealthProfileUri(before.getId(), false));
        bmiBuilder.withValue(ScheduleContract.Users.USER_ID, after.getId());
        batch.add(bmiBuilder.build());

        return batch;
    }

    @Override
    protected ArrayList<ContentProviderOperation> updateSyncStatus(BaseModel a, ScheduleContract.SyncStatus syncStatus) {
        User after = (User) a;
        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        ContentProviderOperation.Builder userBuilder = ContentProviderOperation.newUpdate(ScheduleContract.Users.buildUserUri(after.getId()));
        userBuilder.withValue(ScheduleContract.Users.SYNC_STATUS, syncStatus.ordinal());
        batch.add(userBuilder.build());

        ContentProviderOperation.Builder profileBuilder = ContentProviderOperation.newUpdate(ScheduleContract.Users.buildUserProfileUri(after.getId(), false));
        profileBuilder.withValue(ScheduleContract.Users.SYNC_STATUS, syncStatus.ordinal());
        batch.add(profileBuilder.build());

        ContentProviderOperation.Builder bmiBuilder = ContentProviderOperation.newUpdate(ScheduleContract.Users.buildUserHealthProfileUri(after.getId(), false));
        bmiBuilder.withValue(ScheduleContract.Users.SYNC_STATUS, syncStatus.ordinal());
        batch.add(bmiBuilder.build());

        return batch;
    }

    @Override
    public ArrayList<ContentProviderOperation> save(final BaseModel model, final boolean shouldDelete) {
        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
        User user = (User) model;
        boolean shouldUpdate = false;

        if(user.getId()>0) shouldUpdate = true;

        ContentProviderOperation.Builder builder = null, pBuilder = null, bmiBuilder = null, syncBuilder = null;
        if(shouldUpdate){
            builder = ContentProviderOperation.newUpdate(ScheduleContract.Users.buildUserUri(user.getId()));
            pBuilder = ContentProviderOperation.newUpdate(ScheduleContract.Users.buildUserProfileUri(user.getId(), false));
            bmiBuilder = ContentProviderOperation.newUpdate(ScheduleContract.Users.buildUserHealthProfileUri(user.getId(), false));
        }else{
            builder = ContentProviderOperation.newInsert(ScheduleContract.Users.buildUserUri(null));
            pBuilder = ContentProviderOperation.newInsert(ScheduleContract.Users.buildUserProfileUri(user.getId(), true));
            pBuilder.withValueBackReference(ScheduleContract.UserForeignKeyColumn.USER_ID, 0);
            bmiBuilder = ContentProviderOperation.newInsert(ScheduleContract.Users.buildUserHealthProfileUri(user.getId(), true));
            bmiBuilder.withValueBackReference(ScheduleContract.UserForeignKeyColumn.USER_ID, 0);
        }

        builder.withValue(ScheduleContract.Users.UPDATED, System.currentTimeMillis());
        builder.withValue(ScheduleContract.Users.SYNC_STATUS, ScheduleContract.SyncStatus.PENDING_UPDATE.ordinal());
        if(shouldDelete) builder.withValue(ScheduleContract.Users.IS_DELETED, true);
        batch.add(constructUser(builder, user).build());

        pBuilder.withValue(ScheduleContract.Users.UPDATED, System.currentTimeMillis());
        pBuilder.withValue(ScheduleContract.Users.SYNC_STATUS, ScheduleContract.SyncStatus.PENDING_UPDATE.ordinal());
        if(shouldDelete) builder.withValue(ScheduleContract.Users.IS_DELETED, true);
        batch.add(constructUserProfile(pBuilder, user.getProfile()).build());

        bmiBuilder.withValue(ScheduleContract.Users.UPDATED, System.currentTimeMillis());
        bmiBuilder.withValue(ScheduleContract.Users.SYNC_STATUS, ScheduleContract.SyncStatus.PENDING_UPDATE.ordinal());
        if(shouldDelete) builder.withValue(ScheduleContract.Users.IS_DELETED, true);
        batch.add(constructUserHealthProfile(bmiBuilder, user.getBmiProfile()).build());

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

    private boolean deleteUser(User user) {
        try {
            LogUtils.LOGD(TAG, "deleting user " + user);
            mContext.getContentResolver().delete(ScheduleContract.Users.buildUserUri(user.getId()), null, null);
            LogUtils.LOGD(TAG, "deleted");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.LOGE(TAG, e.getMessage(), e);
            return false;
        }
        return true;
    }

    public static ContentProviderOperation.Builder constructUser(ContentProviderOperation.Builder builder, User user){
        builder.withValue(ScheduleContract.Users.USER_ID, user.getId());
        builder.withValue(ScheduleContract.Users.FIRST_NAME, user.getFirstName());
        builder.withValue(ScheduleContract.Users.LAST_NAME, user.getLastName());
        builder.withValue(ScheduleContract.Users.EMAIL, user.getEmail());
        builder.withValue(ScheduleContract.Users.MOBILE, user.getMobile());
        builder.withValue(ScheduleContract.Users.USER_NAME, user.getUsername());
        builder.withValue(ScheduleContract.Users.IS_DELETED, false);
        AccountManager aManager = AccountManager.get(mContext);
        Account[] accounts = aManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        boolean isLoggedInUser = false;
        if(accounts!=null && accounts.length>0){
            if(accounts[0].name.equals(user.getEmail())){
                isLoggedInUser = true;
            }
        }
        builder.withValue(ScheduleContract.Users.IS_LOGGED_IN_USER, isLoggedInUser);
        return builder;
    }

    public static ContentProviderOperation.Builder constructUserProfile(ContentProviderOperation.Builder profileBuilder,
                                                                        Profile profile){
        profileBuilder.withValue(ScheduleContract.Users.DOB, profile.getDob().getTime());
        profileBuilder.withValue(ScheduleContract.Users.FB_PROFILE_ID, profile.getFbProfileId());
        profileBuilder.withValue(ScheduleContract.Users.FB_USERNAME, profile.getFbUsername());
        profileBuilder.withValue(ScheduleContract.Users.GENDER, profile.getGender().value());
        profileBuilder.withValue(ScheduleContract.Users.MOBILE_NUMBER, profile.getMobileNumber());
        profileBuilder.withValue(ScheduleContract.Users.LOCATION, profile.getLocation().toShortString());
        profileBuilder.withValue(ScheduleContract.Users.RELATION, profile.getRelation().value());
        profileBuilder.withValue(ScheduleContract.Users.ORGANIZATION, profile.getOrganization());
        profileBuilder.withValue(ScheduleContract.Users.BLOOD_GROUP, profile.getBloodGroup().value());
        profileBuilder.withValue(ScheduleContract.Users.PIC_URL, profile.getProfilePicURL());
        profileBuilder.withValue(ScheduleContract.Users.IS_DELETED, false);
        return profileBuilder;
    }

    public static ContentProviderOperation.Builder constructUserHealthProfile(ContentProviderOperation.Builder bmiBuilder,
                                                                              BMIProfile bmiProfile){
        bmiBuilder.withValue(ScheduleContract.Users.HEIGHT, bmiProfile.getHeight());
        bmiBuilder.withValue(ScheduleContract.Users.WEIGHT, bmiProfile.getWeight());
        bmiBuilder.withValue(ScheduleContract.Users.SYSTOLIC_PRESSURE, bmiProfile.getSystolicPressure());
        bmiBuilder.withValue(ScheduleContract.Users.DIASTOLIC_PRESSURE, bmiProfile.getDiastolicPressure());
        bmiBuilder.withValue(ScheduleContract.Users.FASTING_SUGAR, bmiProfile.getFastingSugar());
        bmiBuilder.withValue(ScheduleContract.Users.RANDOM_SUGAR, bmiProfile.getRandomSugar());
        bmiBuilder.withValue(ScheduleContract.Users.PULSE_RATE, bmiProfile.getPulseRate());
        bmiBuilder.withValue(ScheduleContract.Users.HDL, bmiProfile.getHdl());
        bmiBuilder.withValue(ScheduleContract.Users.LDL, bmiProfile.getLdl());
        bmiBuilder.withValue(ScheduleContract.Users.TRIGLYCERIDES, bmiProfile.getTriglycerides());
        bmiBuilder.withValue(ScheduleContract.Users.TOTAL_CHOLESTEROL, bmiProfile.getTotalCholesterol());
        bmiBuilder.withValue(ScheduleContract.Users.IS_DELETED, false);
        return bmiBuilder;
    }

    @Override
    public User parseCursor(Cursor cursor){
        /** Assuming that the cursor is pointing to the user row obtained by the user query **/
        if(cursor==null || cursor.isAfterLast() || cursor.isBeforeFirst() || cursor.isClosed())
            return null;

        //long userId = cursor.getLong(cursor.getColumnIndex(ScheduleContract.Users.TABLE_ALIAS+"."+ScheduleContract.Users.USER_ID));

        long userId = cursor.getLong(cursor.getColumnIndex(ScheduleContract.Users.USER_ID));
        User user = new User();
        user.setId(userId);
        user.setEmail(cursor.getString(cursor.getColumnIndex(ScheduleContract.Users.EMAIL)));
        user.setMobile(cursor.getString(cursor.getColumnIndex(ScheduleContract.Users.MOBILE)));
        user.setFirstName(cursor.getString(cursor.getColumnIndex(ScheduleContract.Users.FIRST_NAME)));
        user.setLastName(cursor.getString(cursor.getColumnIndex(ScheduleContract.Users.LAST_NAME)));
        user.setLoggedInUser(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Users.IS_LOGGED_IN_USER)) > 0);
        user.setUsername(cursor.getString(cursor.getColumnIndex(ScheduleContract.Users.USER_NAME)));
        int syncStatus = cursor.getInt(cursor.getColumnIndex(ScheduleContract.Users.SYNC_STATUS));
        user.setSyncStatus(ScheduleContract.SyncStatus.values()[syncStatus]);
        user.setPulledOn(new Date(cursor.getLong(cursor.getColumnIndex(ScheduleContract.Users.SYNCHRONIZED))));
        user.setPushedOn(new Date(cursor.getLong(cursor.getColumnIndex(ScheduleContract.Users.UPDATED))));

        Profile profile = new Profile();
        profile.setMobileNumber(cursor.getString(cursor.getColumnIndex(ScheduleContract.Users.MOBILE_NUMBER)));
        profile.setFbUsername(cursor.getString(cursor.getColumnIndex(ScheduleContract.Users.FB_USERNAME)));
        profile.setFbProfileId(cursor.getString(cursor.getColumnIndex(ScheduleContract.Users.FB_PROFILE_ID)));
        profile.setProfilePicURL(cursor.getString(cursor.getColumnIndex(ScheduleContract.Users.PIC_URL)));
        profile.setOrganization(cursor.getString(cursor.getColumnIndex(ScheduleContract.Users.ORGANIZATION)));
        profile.setLocation(cursor.getString(cursor.getColumnIndex(ScheduleContract.Users.LOCATION)));
        //syncStatus = cursor.getInt(cursor.getColumnIndex(ScheduleContract.Users.SYNC_STATUS));
        //profile.setSyncStatus(ScheduleContract.SyncStatus.values()[syncStatus]);
        //profile.setPulledOn(new Date(cursor.getLong(cursor.getColumnIndex(ScheduleContract.Users.SYNCHRONIZED))));
        //profile.setPushedOn(new Date(cursor.getLong(cursor.getColumnIndex(ScheduleContract.Users.UPDATED))));

        int relationIndex = cursor.getColumnIndex(ScheduleContract.Users.RELATION);
        if(!cursor.isNull(relationIndex)) profile.setRelation(Relation.get(cursor.getInt(relationIndex)));

        int genderIndex = cursor.getColumnIndex(ScheduleContract.Users.GENDER);
        if(!cursor.isNull(genderIndex)) profile.setGender(Gender.get(cursor.getInt(genderIndex)));

        int bgIndex = cursor.getColumnIndex(ScheduleContract.Users.BLOOD_GROUP);
        if(!cursor.isNull(bgIndex)) profile.setBloodGroup(BloodGroup.get(cursor.getInt(bgIndex)));

        profile.setDob(new Date(cursor.getLong(cursor.getColumnIndex(ScheduleContract.Users.DOB))));
        user.setProfile(profile);

        BMIProfile bmiProfile = new BMIProfile();
        bmiProfile.setHeight(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Users.HEIGHT)));
        bmiProfile.setWeight(cursor.getDouble(cursor.getColumnIndex(ScheduleContract.Users.WEIGHT)));
        bmiProfile.setSystolicPressure(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Users.SYSTOLIC_PRESSURE)));
        bmiProfile.setDiastolicPressure(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Users.DIASTOLIC_PRESSURE)));
        bmiProfile.setPulseRate(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Users.PULSE_RATE)));
        bmiProfile.setFastingSugar(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Users.FASTING_SUGAR)));
        bmiProfile.setRandomSugar(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Users.RANDOM_SUGAR)));
        bmiProfile.setHdl(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Users.HDL)));
        bmiProfile.setLdl(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Users.LDL)));
        bmiProfile.setTriglycerides(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Users.TRIGLYCERIDES)));
        bmiProfile.setTotalCholesterol(cursor.getInt(cursor.getColumnIndex(ScheduleContract.Users.TOTAL_CHOLESTEROL)));
        //syncStatus = cursor.getInt(cursor.getColumnIndex(ScheduleContract.Users.SYNC_STATUS));
        //bmiProfile.setSyncStatus(ScheduleContract.SyncStatus.values()[syncStatus]);
        //bmiProfile.setPulledOn(new Date(cursor.getLong(cursor.getColumnIndex(ScheduleContract.Users.SYNCHRONIZED))));
        //bmiProfile.setPushedOn(new Date(cursor.getLong(cursor.getColumnIndex(ScheduleContract.Users.UPDATED))));
        user.setBmiProfile(bmiProfile);

        return user;

    }
    /** takes care of inserting a new value or updating an existing record **/
    public static void parseUser(User user, ArrayList<ContentProviderOperation> batch){
        /** Add user meta data **/
        Uri uri = null;

        if(user==null)
            return;

        boolean isUpdate = false;
        if(user.getId()>0){
            isUpdate = false;
        }else{
            isUpdate = true;
        }

        uri = ScheduleContract.Users.buildUserUri(user.getId());
        uri = ScheduleContract.addCallerIsSyncAdapterParameter(uri);

        String syncTimeUpdateColumn = ScheduleContract.SyncColumns.SYNCHRONIZED;

        int index = batch.size();
        //construct the user
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(uri);
        if(syncTimeUpdateColumn!=null)
            builder.withValue(syncTimeUpdateColumn, System.currentTimeMillis());

        builder.withValue(ScheduleContract.Users.SYNC_STATUS, ScheduleContract.SyncStatus.SUCCESS.ordinal());
        batch.add(constructUser(builder, user).build());

        //construct the profile
        Profile profile = user.getProfile();
        if(profile!=null){
            ContentProviderOperation.Builder profileBuilder = null;
            Uri profileUri = ScheduleContract.Users.buildUserProfileUri(user.getId(), false);
            if(syncTimeUpdateColumn!=null)
                profileUri = ScheduleContract.addCallerIsSyncAdapterParameter(profileUri);

            if(isUpdate){
                profileBuilder = ContentProviderOperation.newUpdate(profileUri);
            }else{
                profileBuilder = ContentProviderOperation.newInsert(profileUri);
                profileBuilder.withValueBackReference(ScheduleContract.UserForeignKeyColumn.USER_ID, index);
            }

            if(syncTimeUpdateColumn!=null)
                profileBuilder.withValue(syncTimeUpdateColumn, System.currentTimeMillis());

            profileBuilder.withValue(ScheduleContract.Users.SYNC_STATUS, ScheduleContract.SyncStatus.SUCCESS.ordinal());
            profileBuilder = constructUserProfile(profileBuilder, profile);

            batch.add(profileBuilder.build());
        }

        //construct the health profile
        BMIProfile bmiProfile = user.getBmiProfile();
        if(bmiProfile!=null){
            ContentProviderOperation.Builder bmiBuilder = null;

            Uri bmiProfileUri = ScheduleContract.Users.buildUserHealthProfileUri(user.getId(), false);
            if(syncTimeUpdateColumn!=null)
                bmiProfileUri = ScheduleContract.addCallerIsSyncAdapterParameter(bmiProfileUri);

            if(isUpdate){
                bmiBuilder = ContentProviderOperation.newUpdate(bmiProfileUri);
            }else{
                bmiBuilder = ContentProviderOperation.newInsert(bmiProfileUri);
                bmiBuilder.withValueBackReference(ScheduleContract.UserForeignKeyColumn.USER_ID, index);
            }

            if(syncTimeUpdateColumn!=null)
                bmiBuilder.withValue(syncTimeUpdateColumn, System.currentTimeMillis());

            bmiBuilder.withValue(ScheduleContract.Users.SYNC_STATUS, ScheduleContract.SyncStatus.SUCCESS.ordinal());
            bmiBuilder = constructUserHealthProfile(bmiBuilder, bmiProfile);

            batch.add(bmiBuilder.build());
        }
    }

}
