package com.viamhealth.android.model.users;

import com.viamhealth.android.model.enums.Gender;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by naren on 04/10/13.
 */
public class FBUser {

    String profileId;
    String name;
    String firstName;
    String lastName;
    String profileLink;
    String profileUsername;
    Date birthday;
    String hometown;
    String location;
    String bio;
    String gender;
    String locale;
    String email;
    String mobile;
    String latestEmployer;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLatestEmployer() {
        return latestEmployer;
    }

    public void setLatestEmployer(String latestEmployer) {
        this.latestEmployer = latestEmployer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getProfileLink() {
        return profileLink;
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }

    public String getProfileUsername() {
        return profileUsername;
    }

    public void setProfileUsername(String profileUsername) {
        this.profileUsername = profileUsername;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public static FBUser deserialize(JSONObject jsonProfile){
        FBUser fbUser = new FBUser();

        try{
            fbUser.setProfileId(jsonProfile.getString("id"));
        } catch (JSONException j){
            //jsut eat up the exception
        }

        try{
            fbUser.setFirstName(jsonProfile.getString("first_name"));
        } catch (JSONException j){
            //jsut eat up the exception
        }

        try{
            fbUser.setLastName(jsonProfile.getString("last_name"));
        } catch (JSONException j){
            //jsut eat up the exception
        }

        try{
            fbUser.setName(jsonProfile.getString("name"));
        } catch (JSONException j){
            //jsut eat up the exception
        }

        try{
            fbUser.setProfileUsername(jsonProfile.getString("username"));
        } catch (JSONException j){
            //jsut eat up the exception
        }

        try{
            fbUser.setProfileLink(jsonProfile.getString("link"));
        } catch (JSONException j){
            //jsut eat up the exception
        }

        try{
            SimpleDateFormat formater = new SimpleDateFormat("MM/dd/yyyy");
            fbUser.setBirthday(formater.parse(jsonProfile.getString("birthday")));
        } catch(ParseException p){

        } catch (JSONException j){
            //jsut eat up the exception
        }

        try{
            if(jsonProfile.getJSONObject("hometown")!=null)
                fbUser.setHometown(jsonProfile.getJSONObject("hometown").getString("name"));
        } catch (JSONException j){
            //jsut eat up the exception
        }

        try{
            if(jsonProfile.getJSONObject("location")!=null)
                fbUser.setLocation(jsonProfile.getJSONObject("location").getString("name"));
        } catch (JSONException j){
            //jsut eat up the exception
        }

        try{
            fbUser.setGender(jsonProfile.getString("gender").toUpperCase());
        } catch (JSONException j){
            //jsut eat up the exception
        }

        try{
            fbUser.setEmail(jsonProfile.getString("email"));
        } catch (JSONException j){
            //jsut eat up the exception
        }

        try {
            JSONArray workList = jsonProfile.getJSONArray("work");
            if(workList!=null && workList.length()>0){
                fbUser.setLatestEmployer(workList.getJSONObject(0).getJSONObject("employer").getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        return fbUser;
    }

    /**
     *
     * @param @nullable user
     * @return
     */
    public User toUser(User user) {
        if(user==null)
            user = new User();

        if(email!=null && email.isEmpty())
            user.setEmail(email);

        //get first name and last name
        user.setFirstName(getFirstName());
        user.setLastName(getLastName());

        Profile profile = user.getProfile();
        if(profile==null)
            profile = new Profile();

        //get DOB
        profile.setDob(getBirthday());
        //get gender
        profile.setGender(Gender.get(getGender()));

        //get the id
        profile.setFbProfileId(getProfileId());
        profile.setFbUsername(getProfileUsername());
        profile.setOrganization(getLatestEmployer());

        //get the location
        Profile.Location location = profile.new Location();
        profile.setLocation(location);
        location.setAddress(getLocation());

        return user;
    }

    @Override
    public String toString() {
        return "FBUser{" +
                "profileId='" + profileId + '\'' +
                ", name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profileLink='" + profileLink + '\'' +
                ", profileUsername='" + profileUsername + '\'' +
                ", birthday=" + birthday +
                ", hometown='" + hometown + '\'' +
                ", location='" + location + '\'' +
                ", bio='" + bio + '\'' +
                ", gender='" + gender + '\'' +
                ", locale='" + locale + '\'' +
                ", email='" + email + '\'' +
                "} " + super.toString();
    }
}
