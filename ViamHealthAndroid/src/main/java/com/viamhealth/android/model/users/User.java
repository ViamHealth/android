package com.viamhealth.android.model.users;

import android.os.Parcel;
import android.os.Parcelable;

import com.viamhealth.android.model.BaseModel;

/**
 * Created by naren on 03/10/13.
 */
public class User extends BaseModel implements Parcelable {
    String username;
    String email;
    String mobile;
    String firstName;
    String lastName;

    String name;
    Boolean isLoggedInUser = false;

    Profile profile;
    BMIProfile bmiProfile;


    public BMIProfile getBmiProfile() {
        return bmiProfile;
    }

    public void setBmiProfile(BMIProfile bmiProfile) {
        this.bmiProfile = bmiProfile;
    }

    public Boolean isLoggedInUser() {
        return isLoggedInUser;
    }

    public boolean isProfileCreated() {
        if((firstName==null || firstName.isEmpty()) && (lastName==null || lastName.isEmpty()))
            return false;

        if(profile==null || bmiProfile==null)
            return false;

        if(profile.getDob()==null || bmiProfile.getHeight()==null || bmiProfile.getHeight()==0 || bmiProfile.getWeight()==null
                || bmiProfile.getWeight()==0)
            return false;
        
        return true;
    }

    public void setLoggedInUser(Boolean loggedInUser) {
        isLoggedInUser = loggedInUser;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        if((username==null || username.isEmpty()) && (email!=null && email.isEmpty()))
            return email;

        if((username==null || username.isEmpty()) && (mobile!=null && mobile.isEmpty()))
            return mobile;

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        if(name!=null && !name.isEmpty()){
            return name;
        }

        if((firstName!=null && !firstName.isEmpty()) && (lastName!=null || !lastName.isEmpty()))
            return firstName + " " + lastName;

        if(this.firstName!=null && !this.firstName.isEmpty())
            return firstName;

        if(this.lastName!=null && !this.lastName.isEmpty())
            return lastName;

        if(this.email!=null && !this.email.isEmpty() )
            return email;

        if(username!=null && !username.isEmpty())
            return username;

        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profile=" + profile +
                "} " + super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.username);
        dest.writeString(this.email);
        dest.writeString(this.mobile);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.name);
        dest.writeValue(this.isLoggedInUser);

        this.profile.writeToParcel(dest, flags);
        this.bmiProfile.writeToParcel(dest, flags);
    }

    public User(Parcel in) {
        super(in);
        this.username = in.readString();
        this.email = in.readString();
        this.mobile = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.name = in.readString();
        this.isLoggedInUser = (Boolean) in.readValue(null);
        this.profile = new Profile(in);
        this.bmiProfile = new BMIProfile(in);
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User() {
        this.profile = new Profile();
        this.bmiProfile = new BMIProfile();
    }
}
