package com.viamhealth.android.notifications.objects.reminders;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.R;
import com.viamhealth.android.model.enums.NotificationTime;
import com.viamhealth.android.model.enums.ReminderTime;
import com.viamhealth.android.model.reminder.Reminder;
import com.viamhealth.android.model.reminder.ReminderReading;
import com.viamhealth.android.notifications.ReminderConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kunal on 30/1/14.
 */
public class Medicine implements Parcelable{

    private int notificationId;

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    private int smallIcon = R.drawable.ic_navigation_logo;;
    private String contentTitle;
    private String contentText;
    private String bigText;
    private int iconTakenAction;
    private String stringTakenAction;
    private int iconRemindAction;
    private String stringRemindAction;
    public static final int snoozeRemindTime = 10*1000;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setStringRemindAction(String stringRemindAction) {
        this.stringRemindAction = stringRemindAction;
    }

    public void setIconRemindAction(int iconRemindAction) {
        this.iconRemindAction = iconRemindAction;
    }

    public void setStringTakenAction(String stringTakenAction) {
        this.stringTakenAction = stringTakenAction;
    }

    public void setIconTakenAction(int iconTakenAction) {
        this.iconTakenAction = iconTakenAction;
    }

    public void setBigText(String bigText) {
        this.bigText = bigText;
    }



    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public void setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
    }

    public int getSmallIcon(){
        return R.drawable.ic_navigation_logo;
    }
    public String getContentTitle(){
        return contentTitle;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getBigText(){
        return bigText;
    }
    public int getIconTakenAction(){
        return iconTakenAction;
    }
    public String getStringTakenAction(){
        return stringTakenAction;
    }
    public int getIconRemindAction(){
        return iconRemindAction;
    }
    public String getStringRemindAction(){
        return stringRemindAction;
    }

    public Medicine(Date date, List<Reminder> reminders, NotificationTime nt){
        ReminderTime rt;
        int nId;
        if(nt == NotificationTime.Morning){
            rt = ReminderTime.Morning;
            nId = ReminderConstants.NOTIFICATION_ID_MEDICINE_MORNING;
        }
        else if(nt == NotificationTime.Noon){
            rt = ReminderTime.Noon;
            nId = ReminderConstants.NOTIFICATION_ID_MEDICINE_NOON;
        }
        else if(nt == NotificationTime.Night){
            rt = ReminderTime.Night;
            nId = ReminderConstants.NOTIFICATION_ID_MEDICINE_NIGHT;
        }

        else
            throw new IllegalArgumentException();

        int smallIcon = R.drawable.ic_navigation_logo;
        String contentTitle = "Medicine Time!";//+ reading.getReminder().getName();

        //List<String> contentText = new ArrayList<String>();
        String tempContextText = " cot -- ";
        String tempBigText = " big text -- ";
        StringBuilder builder = new StringBuilder();

        for(Reminder reminder : reminders){
            tempBigText = tempBigText + "Medicine : "+reminder.getName()+" - Dosage : "+reminder.getReminderTimeData(rt).getCount() + " ";
            if(reminder.getDetails() != null && reminder.getDetails().length() != 0)
                tempContextText = tempContextText + reminder.getName() + " : " + reminder.getDetails() + " ";
        }

        String bigText = tempBigText;
        int iconTakenAction = R.drawable.btn_check_holo_light;
        String stringTakenAction = "Took Medicine ?";
        int iconRemindAction = R.drawable.ic_action_reminders;
        String stringRemindAction = "Remind later";
        //int remindSnoozeTime = //15 minutes


        setNotificationId(nId);
        setSmallIcon(smallIcon);
        setContentTitle(contentTitle);
        setContentText(tempContextText);
        setBigText(bigText);
        setIconTakenAction(iconTakenAction);
        setStringTakenAction(stringTakenAction);
        setIconRemindAction(iconRemindAction);
        setStringRemindAction(stringRemindAction);
        //setSnoozeRemindTime(remindSnoozeTime);
        setDate(date);

    }

   /* public Medicine(ReminderReading reading){
        int smallIcon = R.drawable.ic_navigation_logo;
        String contentTitle = "Take medicine "+ reading.getReminder().getName();

        Map<ReminderTime,String> contentText = new HashMap<ReminderTime, String>();
        contentText.put(ReminderTime.Morning, reading.getReminder().getReminderTimeData(ReminderTime.Morning).getCount()
                + " in morning, ");
        contentText.put(ReminderTime.Noon, reading.getReminder().getReminderTimeData(ReminderTime.Noon).getCount()
                + " in afternoon, ");
        contentText.put(ReminderTime.Night,reading.getReminder().getReminderTimeData(ReminderTime.Night).getCount()
                + " in night.");
        String bigText = reading.getReminder().getDetails();
        int iconTakenAction = R.drawable.btn_check_holo_light;
        String stringTakenAction = "Took Medicine ?";
        int iconRemindAction = R.drawable.ic_action_reminders;
        String stringRemindAction = "Remind later";
        //int remindSnoozeTime = //15 minutes
        Date date = reading.getReadingDate();

        setNotificationId(ReminderConstants.NOTIFICATION_ID_MEDICINE);
        setSmallIcon(smallIcon);
        setContentTitle(contentTitle);
        setContentText(contentText);
        setBigText(bigText);
        setIconTakenAction(iconTakenAction);
        setStringTakenAction(stringTakenAction);
        setIconRemindAction(iconRemindAction);
        setStringRemindAction(stringRemindAction);
        //setSnoozeRemindTime(remindSnoozeTime);
        setDate(date);

    }*/

    //Parcelable properties

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getNotificationId());
        parcel.writeInt(getSmallIcon());
        parcel.writeString(getContentTitle());
        parcel.writeString(getContentText());
        parcel.writeString(getBigText());
        parcel.writeInt(getIconTakenAction());
        parcel.writeString(getStringTakenAction());
        parcel.writeInt(getIconRemindAction());
        parcel.writeString(getStringRemindAction());
        //parcel.writeInt(getSnoozeRemindTime());
        parcel.writeValue(date);
    }

    public static final Creator<Medicine> CREATOR
            = new Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel parcel) {
            return new Medicine(parcel);
        }

        @Override
        public Medicine[] newArray(int i) {
            return new Medicine[0];
        }
    };

    private Medicine(Parcel in){
        setNotificationId(in.readInt());
        setSmallIcon(in.readInt());
        setContentTitle(in.readString());
        setContentText(in.readString());
        setBigText(in.readString());
        setIconTakenAction(in.readInt());
        setStringTakenAction(in.readString());
        setIconRemindAction(in.readInt());
        setStringRemindAction(in.readString());
        //setSnoozeRemindTime(in.readInt());
        setDate((Date) in.readValue(Date.class.getClassLoader()));
    }
}