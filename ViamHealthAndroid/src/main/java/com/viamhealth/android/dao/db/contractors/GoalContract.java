package com.viamhealth.android.dao.db.contractors;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.CalendarContract;

/**
 * Created by naren on 13/12/13.
 */
public class GoalContract extends BaseContract {

   /**
    * The content URI for the top-level
    * goals authority.
    */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String TABLE_NAME = "goals";

    public static final class Goal implements CommonColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(GoalContract.CONTENT_URI, "goals");
        public static final Uri CONTENT_BY_USER_ID_AND_GOAL_TYPE_URI = Uri.withAppendedPath(Goal.CONTENT_URI, "/#/#");

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/viamhealth.android.goals.meta";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/viamhealth.android.goals.meta";

        public static final String[] PROJECTION_ALL = {_ID, USER_ID, GOAL_TYPE, DATA};
    }

//    public static final class GoalReadings implements CommonColumns {
//        public static final Uri CONTENT_URI = Uri.withAppendedPath(GoalContract.CONTENT_URI, "readings");
//        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/viamhealth.android.goals.readings";
//        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/viamhealth.android.goals.readings";
//        public static final String[] PROJECTION_ALL = {_ID, };
//    }

    public static interface CommonColumns extends BaseColumns {
        public static final String USER_ID = "_userId";
        public static final String GOAL_TYPE = "_type";
        public static final String DATA = "_data";
    }



}
