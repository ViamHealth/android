package com.viamhealth.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ViamHealthPrefs {
	public static final String USER_PREFS = "USER_PREFS";
	public SharedPreferences appSharedPrefs;
	public SharedPreferences.Editor prefsEditor;
	
	public String token = "token";
    public String fbtoken = "fbtoken";
	public String menuList = "menuList";
	public String reloadgraph = "reloadgraph";
	public String goalname = "goalname";
	public String goaldesc = "goaldesc";
	public String goalid = "goalid";
	public String sheight = "sheight";
	public String swidth = "swidth";
	public String grpfoodname = "grpfoodname";
	public String addprofiletab="addprofiletab";
	public String goalval="goalval";
	public String edt="edt";
	public String profileName = "profileName";
	public String btngoal_hide="btngoal_hide";
	public String btnprofile_hide="btnprofile_hide";
	public String btnhealth_hide="btnhealth_hide";
	public String goalDisable = "goalDisable";
	public String userid = "userid";
	public String username = "username";
	public String gender="gender"; 
	public String firstname = "firstname";
	public String lastname = "lastname";
	public String bornon = "bornon";
	public String profilepic = "profilepic";
	//public String dateAdded = "dateAdded";

    public int targetCaloriesPerDay;

	public ViamHealthPrefs(Context context) {
		this.appSharedPrefs = context.getSharedPreferences(USER_PREFS, Activity.MODE_PRIVATE);
		this.prefsEditor = appSharedPrefs.edit();
	}

    public String getSheight() {
		return appSharedPrefs.getString(sheight, "0");
	}


	public void setSheight(String _sheight) {
		this.prefsEditor.putString(sheight, _sheight).commit();
	}


	public String getSwidth() {
		return appSharedPrefs.getString(swidth, "0");
	}


	public void setSwidth(String _swidth) {
		this.prefsEditor.putString(swidth, _swidth).commit();
	}


	public String getToken() {
		return appSharedPrefs.getString(token, null);
	}

	public void setToken(String _token) {
		this.prefsEditor.putString(token, _token).commit();
	}


    public void setFBAccessToken(String _token) {
        this.prefsEditor.putString(fbtoken, _token).commit();
    }

	public String getMenuList() {
		return appSharedPrefs.getString(menuList, "");
	}

    public String getUsername() {
        return appSharedPrefs.getString(username, "");
    }

    public void setUsername(String _username) {
        this.prefsEditor.putString(username, _username).commit();
    }
    public void setBtnprofile_hide(String _btnprofile_hide) {
        this.prefsEditor.putString(btnprofile_hide, _btnprofile_hide).commit();
    }

    public String getUserid() {
        return appSharedPrefs.getString(userid, "0");
    }












    /* Unused functions . Need toc heck before deleting*/

    public int getTargetCaloriesPerDay() {
        return appSharedPrefs.getInt("targetCalories", 0);
    }

    public void setTargetCaloriesPerDay(int targetCaloriesPerDay) {
        this.prefsEditor.putInt("targetCalories", targetCaloriesPerDay).commit();
    }

    public String getFBAccessToken() {
        return appSharedPrefs.getString(fbtoken, null);
    }

	public void setMenuList(String _menuList) {
		this.prefsEditor.putString(menuList, _menuList).commit();
	}

    public String getReloadgraph() {
		return appSharedPrefs.getString(reloadgraph, "0");
	}

	public void setReloadgraph(String _reloadgraph) {
		this.prefsEditor.putString(reloadgraph, _reloadgraph).commit();
	}

	public String getGoalname() {
		return appSharedPrefs.getString(goalname, "");
	}

	public void setGoalname(String _goalname) {
		this.prefsEditor.putString(goalname, _goalname).commit();
	}

	public String getGoaldesc() {
		return appSharedPrefs.getString(goaldesc, "");
	}

	public void setGoaldesc(String _goaldesc) {
		this.prefsEditor.putString(goaldesc, _goaldesc).commit();
	}


	public String getGoalid() {
		return appSharedPrefs.getString(goalid, "");
	}


	public void setGoalid(String _goalid) {
		this.prefsEditor.putString(goalid, _goalid).commit();
	}


	public String getGrpfoodname() {
		return appSharedPrefs.getString(grpfoodname, "");
	}


	public void setGrpfoodname(String _grpfoodname) {
		this.prefsEditor.putString(grpfoodname, _grpfoodname).commit();
	}


	public String getAddprofiletab() {
		return appSharedPrefs.getString(addprofiletab, "0");
	}


	public void setAddprofiletab(String _addprofiletab) {
		this.prefsEditor.putString(addprofiletab, _addprofiletab).commit();
	}


	public String getGoalval() {
		return appSharedPrefs.getString(goalval, "0");
	}


	public void setGoalval(String _goalval) {
		this.prefsEditor.putString(goalval, _goalval).commit();
	}


	public String getEdt() {
		return appSharedPrefs.getString(edt, "0");
	}


	public void setEdt(String _edt) {
		this.prefsEditor.putString(edt, _edt).commit();
	}


	public String getProfileName() {
		return appSharedPrefs.getString(profileName, "null");
	}


	public void setProfileName(String _profileName) {
		this.prefsEditor.putString(profileName, _profileName).commit();
	}


	public String getBtngoal_hide() {
		return appSharedPrefs.getString(btngoal_hide, "0");
	}


	public void setBtngoal_hide(String _btngoal_hide) {
		this.prefsEditor.putString(btngoal_hide, _btngoal_hide).commit();
	}


	public String getBtnprofile_hide() {
		return appSharedPrefs.getString(btnprofile_hide, "0");
	}



	public String getBtnhealth_hide() {
		return appSharedPrefs.getString(btnhealth_hide, "0");
	}


	public void setBtnhealth_hide(String _btnhealth_hide) {
		this.prefsEditor.putString(btnhealth_hide, _btnhealth_hide).commit();
	}


	public String getGoalDisable() {
		return appSharedPrefs.getString(goalDisable, "0");
	}


	public void setGoalDisable(String _goalDisable) {
		this.prefsEditor.putString(goalDisable, _goalDisable).commit();
	}





	public void setUserid(String _userid) {
		this.prefsEditor.putString(userid, _userid).commit();
	}


	public String getGender() {
		return appSharedPrefs.getString(gender, "0");
	}


	public void setGender(String _gender) {
		this.prefsEditor.putString(gender, _gender).commit();
	}


	public String getFirstname() {
		return appSharedPrefs.getString(firstname, "0");
	}


	public void setFirstname(String _firstname) {
		this.prefsEditor.putString(firstname, _firstname).commit();
	}


	public String getLastname() {
		return appSharedPrefs.getString(lastname, "0");
	}


	public void setLastname(String _lastname) {
		this.prefsEditor.putString(lastname, _lastname).commit();
	}


	public String getBornon() {
		return appSharedPrefs.getString(bornon, "0");
	}


	public void setBornon(String _bornon) {
		this.prefsEditor.putString(bornon, _bornon).commit();
	}


	public String getProfilepic() {
		return appSharedPrefs.getString(profilepic, "0");
	}


	public void setProfilepic(String _profilepic) {
		this.prefsEditor.putString(profilepic, _profilepic).commit();
	}


/*	public String getDateAdded() {
		return appSharedPrefs.getString(dateAdded, "0");
	}


	public void setDateAdded(String _dateAdded) {
		this.prefsEditor.putString(dateAdded, _dateAdded).commit();
	}  
	*/
}
