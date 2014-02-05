package com.viamhealth.android.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.viamhealth.android.R;
import com.viamhealth.android.model.reminder.ReminderReading;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.notification.NotificationHandler;
import com.viamhealth.android.notification.NotificationObject;
import com.viamhealth.android.notification.NotificationType;
import com.viamhealth.android.notification.NotifyHandlerFactory;
import com.viamhealth.android.notification.ReminderNO;
import com.viamhealth.android.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class NotifyService extends Service {
    public NotifyService() {
    }

    private NotificationManager mNM;
    private static final String TAG = LogUtils.makeLogTag(NotifyService.class);

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private static int NOTIFICATION = 1;

    public final static String PARAM_USER = "user";
    public final static String PARAM_TYPE = "type";
    public final static String PARAM_DATA = "data";

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.LOGI(TAG, "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        // If this service was started by out AlarmTask intent then we want to show our notification
        byte[] byteArrayExtraUser = intent.getByteArrayExtra(PARAM_USER);
        //User user = intent.getParcelableExtra(PARAM_USER);
        Parcel parcel_user = Parcel.obtain();
        parcel_user.unmarshall(byteArrayExtraUser, 0, byteArrayExtraUser.length);
        parcel_user.setDataPosition(0);
        User user = User.CREATOR.createFromParcel(parcel_user);

        int typeOrdinal = intent.getIntExtra(PARAM_TYPE, -1);
        if(typeOrdinal>=0){
            NotificationType type = NotificationType.values()[typeOrdinal];
            //ArrayList<NotificationObject> objects = intent.getParcelableArrayListExtra(PARAM_DATA);
            byte[] byteArrayExtra = intent.getByteArrayExtra(PARAM_DATA);
            Parcel parcel = Parcel.obtain();
            parcel.unmarshall(byteArrayExtra, 0, byteArrayExtra.length);
            parcel.setDataPosition(0);
            ReminderNO objects_n = ReminderNO.CREATOR.createFromParcel(parcel);
            List<ReminderReading> objects = objects_n.getReadings();

            showNotification(type, user, objects);
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    private void showNotification(NotificationType type, User user, List<ReminderReading> notifyObjects) {

        NotificationHandler handler = NotifyHandlerFactory.getHandler(type, this);
        NotificationCompat.Builder notification = handler.getNotification(user, notifyObjects);


        // Send the notification.
        int hi = 100 + (int)(Math.random() * ((5000 - 1000) + 1));
        Notification not = notification.build();
        System.out.println(notification.toString());
        System.out.println(notification.hashCode());
        mNM.notify(hi, not);

        //stop the service
        stopSelf();
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
