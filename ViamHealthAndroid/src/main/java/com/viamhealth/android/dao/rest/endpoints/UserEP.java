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

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naren on 03/10/13.
 */
public class UserEP extends BaseEP {

    private static String TAG = "UserEP";

    public UserEP(Context context, Application app) {
        super(context, app);
    }

    public String inviteUser(String email, String firstname,String lastname){
        String	responsetxt="1";
        return responsetxt;
    }

    public void logout() {

    }

    // function for call signup service
    public User SignUp(String username,String password) {
        String	responsetxt="1";
        String baseurlString = Global_Application.url+"signup/";
        Log.e(TAG, "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);

        client.AddParam("email",username);
        client.AddParam("password",password);

        try
        {
            client.Execute(RequestMethod.POST);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());
        User user = processUserResponse(responseString);
        user.setLoggedInUser(true);
        ga.setLoggedInUser(user);
        return user;
    }

    public User AuthenticateThroughFB(String fbAccessToken){
        String url = Global_Application.url + "account/facebook/login/token/?next=/api-token-auth/?access_token=" + fbAccessToken;
        Log.e(TAG, "url is : " + url);

        /* this will be the first rest call */
        RestClient client = new RestClient(url);
        client.AddParam("access_token", fbAccessToken);
        client.setDisableAutoRedirect(true);

        try{
            client.Execute(RequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        User user = null;
        String responseString = client.getResponse();
        Log.i(TAG, client.toString());
        try {
            JSONObject jObject = new JSONObject(responseString);
            //TODO::implement proper error handling
            String	responsetxt1 = jObject.getString("token");
            if(responsetxt1.length()>0){
                Log.e(TAG,"token is " + responsetxt1);
                appPrefs.setToken(responsetxt1);
            }
            user = getLoggedInUser();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    // function for call login service
    public String Login(String username,String password) {
        String	responsetxt="1";
        String baseurlString = Global_Application.url+"api-token-auth/";

        RestClient client = new RestClient(baseurlString);
        client.AddParam("email",username);
        client.AddParam("password",password);

        try{
            client.Execute(RequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());
        try {
            JSONObject jObject = new JSONObject(responseString);
            //TODO::implement proper error handling
            String	responsetxt1 = jObject.getString("token");
            if(responsetxt1.length()>0){
                Log.e(TAG,"token is " + responsetxt1);
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

        if(ga.getLoggedInUser()!=null)
            return ga.getLoggedInUser();
        User user=null;

        String baseurlString = Global_Application.url+"users/me/";

        RestClient client = new RestClient(baseurlString);
        if(appPrefs.getToken()!=null)
        {
            client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

            try{
                client.Execute(RequestMethod.GET);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String responseString = client.getResponse();
            Log.i(TAG, client.toString());
            user = processUserResponse(responseString);
            user.setLoggedInUser(true);
            user.setBmiProfile(getUserBMIProfile(user.getId()));
            ga.setLoggedInUser(user);
        }
        return user;
    }

    public boolean shareUser(User userToShare, String email, Boolean isSelf) {
        String baseurlString = Global_Application.url+"share/";

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
        client.AddParam("email", email);
        client.AddParam("share_user_id", userToShare.getId());
        client.AddParam("is_self", isSelf.toString());

        try {
            client.Execute(RequestMethod.POST);
        }catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, "shareUser:" + client.toString());
        if(client.getResponseCode()!= HttpStatus.SC_CREATED || client.getResponseCode() != HttpStatus.SC_OK)
            return false;

        return true;
    }

    public boolean deleteUser(User user) {
        String baseurlString = Global_Application.url+"users/"+user.getId().toString();

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

        try {
            client.Execute(RequestMethod.DELETE);
        }catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, "shareUser:" + client.toString());
        if(client.getResponseCode()!= HttpStatus.SC_NO_CONTENT)
            return false;

        return true;
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
        Log.i(TAG, client.toString());
        List<User> users = processUsersResponse(responseString);
        int usersCount = users==null?0:users.size();
        for(int i=0; i<usersCount; i++){
            //TODO investigate why there was an internal server error
            BMIProfile bmiP = getUserBMIProfile(users.get(i).getId());
            if(bmiP==null)
                continue;
            users.get(i).setBmiProfile(bmiP);
        }
        return users;
    }

    public User getCompleteUserProfile(Long userId){
        User user = getUserProfile(userId);
        user.setBmiProfile(getUserBMIProfile(userId));
        return user;
    }
    // function for get family member list
    public User getUserProfile(Long userId) {
        String	responsetxt="1";
        String baseurlString = Global_Application.url+"users/"+userId+"/";
        Log.e(TAG,"url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

        try {
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());
        User user = processUserResponse(responseString);
        if(user.getId() == getLoggedInUser().getId()){
            user.setLoggedInUser(true);
        }
        return user;
    }

    public BMIProfile getUserBMIProfile(Long userId) {
        String baseurlString = Global_Application.url+"users/"+userId+"/bmi-profile/";
        Log.e(TAG,"url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

        try {
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());
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
        Log.i(TAG, client.toString());
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
        client.AddParam("profile_picture_url", profile.getProfilePicURL());

        if(profile.getBloodGroup()!=BloodGroup.None)
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
        Log.i(TAG, client.toString());
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
        Log.i(TAG, client.toString());
        User user = processUserResponse(responseString);
        if(user.getId() == getLoggedInUser().getId()){
            user.setLoggedInUser(true);
        }
        return user;
    }

    private List<User> processUsersResponse(String usersResponse) {

        List<User> users = new ArrayList<User>();

        try{
            JSONArray jsonUsers = new JSONArray(usersResponse);
            for(int i=0; i<jsonUsers.length(); i++){
                User user = processUserResponse(jsonUsers.getJSONObject(i));
                User loggedInUser = getLoggedInUser();
                if(user.getId().equals(loggedInUser.getId())){
                    user.setLoggedInUser(true);
                }
                users.add(user);
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
            user.setMobile(jsonUser.getString("mobile"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
    private User processUserResponse(String userResponse) {

        User user = null;

        try{
            if(userResponse!=null)
            {
                JSONObject jsonUser = new JSONObject(userResponse);
                user = processUserResponse(jsonUser);
            }
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

        /* need to deserialize Profile object */
        try {
            pd.setGender(Gender.get(jsonProfile.getString("gender")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            pd.setDob(formatter.parse(jsonProfile.getString("date_of_birth")));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            pd.setBloodGroup(BloodGroup.get(jsonProfile.getInt("blood_group")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            pd.setFbProfileId(jsonProfile.getString("fb_profile_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            pd.setOrganization(jsonProfile.getString("organization"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            pd.setProfilePicURL(jsonProfile.getString("profile_picture_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            location.setState(jsonProfile.getString("state"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            location.setStreet(jsonProfile.getString("street"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            location.setCountry(jsonProfile.getString("country"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            location.setCity(jsonProfile.getString("city"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            location.setZip(jsonProfile.getString("zip_code"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            location.setLattitude(jsonProfile.getDouble("lattitude"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            location.setLongitude(jsonProfile.getDouble("longitude"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            location.setAddress(jsonProfile.getString("address"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            pd.setLocation(location);
        } catch (Exception e) {
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
        updateBMIProfile(updatedUser.getId(), user.getBmiProfile());
        user.setId(updatedUser.getId());

        User loggedInUser = ga.getLoggedInUser();
        if(loggedInUser.getId()==user.getId())
            ga.setLoggedInUser(user);

        return user;
    }

}
