package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.users.BMIProfile;
import com.viamhealth.android.model.users.Profile;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.model.enums.BloodGroup;
import com.viamhealth.android.model.enums.Gender;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naren on 03/10/13.
 */
public class UserEP extends BaseEP {

    public UserEP(Context context, Application app) {
        super(context, app);
    }

    public String inviteUser(String email, String firstname,String lastname){
        String	responsetxt="1";
        return responsetxt;
    }

    // function for call signup service
    public String SignUp(String username,String password) {
        String	responsetxt="1";
        String baseurlString = Global_Application.url+"signup/";
        Log.e("TAG", "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddParam("username",username);
        client.AddParam("password",password);

        try
        {
            client.Execute(RequestMethod.POST);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();

        try {
            JSONObject jObject = new JSONObject(responseString);
            //TODO:: implement proper error handling
            String usernameResp = jObject.getString("username");
            if(usernameResp.equals(username)){
                responsetxt="0";
            }else{
                responsetxt = usernameResp;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return responsetxt;
    }

    // function for call login service
    public String Login(String username,String password) {
        String	responsetxt="1";
        String baseurlString = Global_Application.url+"api-token-auth/";

        RestClient client = new RestClient(baseurlString);
        client.AddParam("username",username);
        client.AddParam("password",password);

        try{
            client.Execute(RequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();

        try {
            JSONObject jObject = new JSONObject(responseString);
            //TODO::implement proper error handling
            String	responsetxt1 = jObject.getString("token");
            if(responsetxt1.length()>0){
                Log.e("TAG","token is " + responsetxt1);
                appPrefs.setToken(responsetxt1);
                responsetxt="0";
            }
            getLoggedInUser();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return responsetxt;
    }

    public User getLoggedInUser() {
        String baseurlString = Global_Application.url+"users/me";

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

        try{
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        User user = processUserResponse(responseString);
        ga.setLoggedInUser(user);
        return user;
    }

    // function for get family member list
    public List<User> GetFamilyMembers() {

        String baseurlString = Global_Application.url+"users/";

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

        try {
            client.Execute(RequestMethod.GET);
        }catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        List<User> users = processUsersResponse(responseString);
        return users;
    }

    // function for get family member list
    public User getUserProfile(Long userId) {
        String	responsetxt="1";
        String baseurlString = Global_Application.url+"users/"+userId+"/";
        Log.e("TAG","url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

        try {
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();

        return processUserResponse(responseString);
    }

    public BMIProfile getUserBMIProfile(Long userId) {
        String baseurlString = Global_Application.url+"users/"+userId+"/bmi-profile/";
        Log.e("TAG","url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

        try {
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();

        return processBMIProfileResponse(responseString);
    }

    public BMIProfile updateBMIProfile(Long userId, BMIProfile profile) {
        String baseurlString = Global_Application.url+"users/" + userId + "/bmi-profile/";
        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

        client.AddParam("height", profile.getHeight());
        client.AddParam("weight", profile.getWeight());
        //TODO::lifestyle

        try{
            client.Execute(RequestMethod.PUT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        return processBMIProfileResponse(responseString);
    }

    public Profile updateProfile(long userId, Profile profile) {
        String responce="1";
        String baseurlString = Global_Application.url+"users/" + userId + "/profile/";
        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

        client.AddParam("gender", profile.getGender().key());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        client.AddParam("date_of_birth", formatter.format(profile.getDob()));
        client.AddParam("profile_picture", profile.getProfilePicURL());
        client.AddParam("blood_group", profile.getBloodGroup().value());
        client.AddParam("fb_profile_id", profile.getFbProfileId());
        client.AddParam("fb_username", profile.getFbUsername());
        client.AddParam("organization", profile.getOrganization());
        client.AddParam("street", profile.getLocation().getStreet());
        client.AddParam("city", profile.getLocation().getCity());
        client.AddParam("state", profile.getLocation().getState());
        client.AddParam("country", profile.getLocation().getCountry());
        client.AddParam("zip_code", profile.getLocation().getZip());
        client.AddParam("lattitude", profile.getLocation().getLattitude());
        client.AddParam("longitude", profile.getLocation().getLongitude());
        client.AddParam("address", profile.getLocation().getAddress());

        try{
            client.Execute(RequestMethod.PUT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        return processProfileResponse(responseString);
    }

    private User updateUser(Long userId, String firstName, String lastName, String username){
        String responce="1";
        String baseurlString = Global_Application.url+"users/";

        if(userId!=null && userId>0)
            baseurlString += userId + "/";

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

        client.AddParam("first_name", firstName);
        client.AddParam("last_name", lastName);

        try{
            if(userId==null || userId==0){
                client.AddParam("username", username);
                client.Execute(RequestMethod.POST);
            } else
                client.Execute(RequestMethod.PUT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        return processUserResponse(responseString);
    }

    private List<User> processUsersResponse(String usersResponse) {

        List<User> users = new ArrayList<User>();

        try{
            JSONArray jsonUsers = new JSONArray(usersResponse);
            for(int i=0; i<jsonUsers.length(); i++){
                users.add(processUserResponse(jsonUsers.getJSONObject(i)));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return users;
    }

    private User processUserResponse(JSONObject jsonUser) {

        User user = new User();

        try{
            user.setId(jsonUser.getLong("id"));
            user.setEmail(jsonUser.getString("email"));
            user.setUsername(jsonUser.getString("username"));
            user.setFirstName(jsonUser.getString("first_name"));
            user.setLastName(jsonUser.getString("last_name"));
            user.setProfile(processProfileResponse(jsonUser.getJSONObject("profile")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
    private User processUserResponse(String userResponse) {

        User user = null;

        try{
            JSONObject jsonUser = new JSONObject(userResponse);
            user = processUserResponse(jsonUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    private BMIProfile processBMIProfileResponse(String profileResponse){

        BMIProfile pd = null;

        try{
            /* need to deserialize Profile object */
            JSONObject jsonProfile = new JSONObject(profileResponse);
            pd = processBMIProfileResponse(jsonProfile);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pd;
    }

    private BMIProfile processBMIProfileResponse(JSONObject jsonProfile){

        BMIProfile pd = new BMIProfile();
        try{
            /* need to deserialize Profile object */
            pd.setHeight(jsonProfile.getInt("height"));
            pd.setWeight(jsonProfile.getDouble("weight"));
            pd.setBmr(jsonProfile.getInt("bmr"));
            //pd.setBmiClassifier();
            //pd.setLifeStyle();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pd;
    }
    private Profile processProfileResponse(JSONObject jsonProfile){

        Profile pd = new Profile();
        Profile.Location location = pd.new Location();
        pd.setLocation(location);
        try{
            /* need to deserialize Profile object */
            pd.setGender(Gender.get(jsonProfile.getString("gender")));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            pd.setDob(formatter.parse(jsonProfile.getString("date_of_birth")));
            pd.setBloodGroup(BloodGroup.get(jsonProfile.getInt("blood_group")));
            pd.setFbProfileId(jsonProfile.getString("fb_profile_id"));
            pd.setOrganization(jsonProfile.getString("organization"));
            pd.setProfilePicURL(jsonProfile.getString("profile_picture_url"));

            location.setState(jsonProfile.getString("state"));
            location.setStreet(jsonProfile.getString("street"));
            location.setCountry(jsonProfile.getString("country"));
            location.setCity(jsonProfile.getString("city"));
            location.setZip(jsonProfile.getString("zip_code"));
            location.setLattitude(jsonProfile.getDouble("lattitude"));
            location.setLongitude(jsonProfile.getDouble("longitude"));
            location.setAddress(jsonProfile.getString("address"));

            pd.setLocation(location);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return pd;
    }
    private Profile processProfileResponse(String profileResponse){

        Profile pd = null;

        try{
            /* need to deserialize Profile object */
            JSONObject jsonProfile = new JSONObject(profileResponse);
            pd = processProfileResponse(jsonProfile);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pd;
    }

    public User updateUser(User user) {

        // Update the user data
        User updatedUser = updateUser(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername());

        //Update profile data
        updateProfile(updatedUser.getId(), user.getProfile());

        user.setId(updatedUser.getId());

        return user;
    }

}
