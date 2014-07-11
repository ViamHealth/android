package com.viamhealth.android;



import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import com.viamhealth.android.dao.db.DataBaseAdapter;
import com.viamhealth.android.model.MedicationData;


import com.viamhealth.android.model.users.User;
import com.viamhealth.android.ui.helper.ExtendedImageDownloader;


import android.app.Application;
import android.graphics.Bitmap;


public class Global_Application extends Application
{
	public Bitmap img;
	public String download;
	//public static String url="http://api.viamhealth.com/";
    public static String url="http://devapi.viamhealth.com/";
	public int inviteuser_flg=0;
	DataBaseAdapter dbAdapter;
	public static String foodType,nextfood,prevfood;
	public static String nextbrekfast,nextlunch,nextsnacks,nextdinner,nextExercise,nextfile,nextmedical,nextmedication,watchupdate,update="0";
	int foodPos;


	public static String selectedfoodid,selectedexerciseid,food_item,food_quantity,meal_type,weight,user_calories,time_spent,exercise_value;
	public static double totalcal=0;
	public String path;
    public static double total_ideal_calories=0;
	public String fileuri=null;
    public String currentUser=null;
    public byte fileByte[]=null;
	public String addvalType;
	public String weightid,cholesterolid,glucoseid,bpid;
	public boolean weightupdate, cholesterolupdate,glucoseupdate,bpupdate;
	public static List<User> lstfamilyglobal;
	public boolean calcelflg;
    public String selected_date;





    public static ArrayList<MedicationData>	listData = new ArrayList<MedicationData>();
    public static ArrayList<MedicationData> otherData = new ArrayList<MedicationData>();

    private User loggedInUser;


	@Override
	public void onCreate() {
		super.onCreate();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.memoryCacheSize(2 * 1024 * 1024) // 2 Mb
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.imageDownloader(new ExtendedImageDownloader(getApplicationContext()))
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.enableLogging() // Not necessary in common
		.build();
	  // Initialize ImageLoader with configuration.
	  ImageLoader.getInstance().init(config);
		/*dbAdapter=new DataBaseAdapter(getApplicationContext());
		dbAdapter.createDatabase();
		dbAdapter.insertDefaulValues();	*/

		// This configuration tuning is custom. You can tune every option, you may tune some of them, 
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
	
	}


    /* Refactor GA integration before next launch */

    public void GA_eventButtonPress(String label){
        EasyTracker.getInstance(this).send(MapBuilder
                .createEvent("ui_action",
                        "button_press",
                        label,
                        null)
                .build());
    }
    public void GA_eventButtonPress(String label, long value){
        EasyTracker.getInstance(this).send(MapBuilder
                .createEvent("ui_action",
                        "button_press",
                        label,
                        value)
                .build());
    }

    public void GA_eventGeneral(String category, String action, String label, long value){
        EasyTracker.getInstance(this).send(MapBuilder
                .createEvent(category,
                        action,
                        label,
                        value)
                .build());
    }
    public void GA_eventGeneral(String category, String action, String label){
        EasyTracker.getInstance(this).send(MapBuilder
                .createEvent(category,
                        action,
                        label,
                        null)
                .build());
    }
    public void GA_eventGeneral(String category, String action){
        EasyTracker.getInstance(this).send(MapBuilder
                .createEvent(category,
                        action,
                        null,
                        null)
                .build());
    }

    /* GA Integration ends */

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        if(this.loggedInUser!=null)
            this.loggedInUser.setLoggedInUser(true);
    }

    public String getDownload() {
        return download;
    }





    /* Unused functions. Need toc ehck before deleting */
    public boolean isCalcelflg() {
		return calcelflg;
	}



	public void setCalcelflg(boolean calcelflg) {
		this.calcelflg = calcelflg;
	}



	public boolean isWeightupdate() {
		return weightupdate;
	}



	public void setWeightupdate(boolean weightupdate) {
		this.weightupdate = weightupdate;
	}



	public boolean isCholesterolupdate() {
		return cholesterolupdate;
	}



	public void setCholesterolupdate(boolean cholesterolupdate) {
		this.cholesterolupdate = cholesterolupdate;
	}



	public boolean isGlucoseupdate() {
		return glucoseupdate;
	}



	public void setGlucoseupdate(boolean glucoseupdate) {
		this.glucoseupdate = glucoseupdate;
	}



	public boolean isBpupdate() {
		return bpupdate;
	}



	public void setBpupdate(boolean bpupdate) {
		this.bpupdate = bpupdate;
	}



	public List<User> getLstfamilyglobal() {
		return lstfamilyglobal;
	}

	public void setLstfamilyglobal(List<User> lstfamilyglobal) {
		this.lstfamilyglobal = lstfamilyglobal;
	}

	public static String getUpdate() {
		return update;
	}

	public static void setUpdate(String update) {
		Global_Application.update = update;
	}

	public static String getWatchupdate() {
		return watchupdate;
	}


	public static void setWatchupdate(String watchupdate) {
		Global_Application.watchupdate = watchupdate;
	}


	public static String getNextmedication() {
		return nextmedication;
	}


	public static void setNextmedication(String nextmedication) {
		Global_Application.nextmedication = nextmedication;
	}


	public static String getNextmedical() {
		return nextmedical;
	}


	public static void setNextmedical(String nextmedical) {
		Global_Application.nextmedical = nextmedical;
	}


	public String getBpid() {
		return bpid;
	}


	public void setBpid(String bpid) {
		this.bpid = bpid;
	}


	public String getGlucoseid() {
		return glucoseid;
	}


	public void setGlucoseid(String glucoseid) {
		this.glucoseid = glucoseid;
	}


	public String getCholesterolid() {
		return cholesterolid;
	}


	public void setCholesterolid(String cholesterolid) {
		this.cholesterolid = cholesterolid;
	}


	public String getWeightid() {
		return weightid;
	}

	public void setWeightid(String weightid) {
		this.weightid = weightid;
	}

	public String getAddvalType() {
		return addvalType;
	}

	public void setAddvalType(String addvalType) {
		this.addvalType = addvalType;
	}

	public String getFileuri() {
		return fileuri;
	}

	public void setFileuri(String fileuri) {
		this.fileuri = fileuri;
	}


    public byte[] getFileByte() {
        return fileByte;
    }

    public void setFileByte(byte [] fileByte) {
        this.fileByte = fileByte;
    }



	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}



	public Bitmap getImg() {
		return img;
	}
	public void setImg(Bitmap img) {
		this.img = img;
	}

	public void setDownload(String download) {
		this.download = download;
	}
	public int getInviteuser_flg() {
		return inviteuser_flg;
	}
	public void setInviteuser_flg(int inviteuser_flg) {
		this.inviteuser_flg = inviteuser_flg;
	}
	
	
	
}