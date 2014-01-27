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

    public Boolean InviteUser(String email){
        String baseurlString = Global_Application.url+"invite/";
        Log.e(TAG, "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
        client.AddParam("email",email);

        try{
            client.Execute(RequestMethod.POST);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());

        if(client.getResponseCode()==HttpStatus.SC_OK ||
                client.getResponseCode()==HttpStatus.SC_CREATED ||
                client.getResponseCode()==HttpStatus.SC_NO_CONTENT)
            return true;

        return false;
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

    public boolean ChangePassword(String oldPassword, String newPassword){
        String baseurlString = Global_Application.url+"users/change-password/";
        Log.e(TAG, "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
        client.AddParam("old_password", oldPassword);
        client.AddParam("password", newPassword);

        try{
            client.Execute(RequestMethod.POST);
        }catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());

        if(client.getResponseCode()==HttpStatus.SC_OK ||
                client.getResponseCode()==HttpStatus.SC_CREATED ||
                client.getResponseCode()==HttpStatus.SC_NO_CONTENT)
            return true;

        return false;
    }
    public boolean ForgotPassword(String email){
        String baseurlString = Global_Application.url+"forgot-password-email/";
        Log.e(TAG, "url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);

        client.AddParam("email",email);

        try
        {
            client.Execute(RequestMethod.POST);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());

        if(client.getResponseCode()==HttpStatus.SC_OK ||
                client.getResponseCode()==HttpStatus.SC_CREATED ||
                client.getResponseCode()==HttpStatus.SC_NO_CONTENT)
            return true;

        return false;
    }

    public boolean Logout(){
        String url = Global_Application.url + "logout/";
        Log.e(TAG, "url is : " + url);

        if(appPrefs.getToken()==null)
            return false;

        /* this will be the first rest call */
        RestClient client = new RestClient(url);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

        try{
            client.Execute(RequestMethod.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());

        if(client.getResponseCode()==HttpStatus.SC_OK ||
                client.getResponseCode()==HttpStatus.SC_NO_CONTENT)
            return true;

        return false;
    }

    public User AuthenticateThroughFB(String fbAccessToken){
        return _login(fbAccessToken, null, LoginType.FB);
    }

    private enum LoginType {
        Email,
        Mobile,
        FB,
        UserName;
    }

    private User _login(String key, String password, LoginType type){
        String baseurlString = Global_Application.url+"api-token-auth/";

        RestClient client = new RestClient(baseurlString);

        switch(type){
            case FB:
                client.AddParam("access_token", key);
                break;

            case Email:
                client.AddParam("email",key);
                client.AddParam("password",password);
                break;

            case Mobile:
                client.AddParam("mobile",key);
                client.AddParam("password",password);
                break;

            case UserName:
                client.AddParam("username",key);
                client.AddParam("password",password);
                break;

        }

        try{
            client.Execute(RequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, "login:" + client.toString());
        User user = null;
        try {
            JSONObject jObject = new JSONObject(responseString);
            //TODO::implement proper error handling
            String	responsetxt1 = jObject.getString("token");
            if(responsetxt1.length()>0){
                Log.i(TAG,"token is " + responsetxt1);
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
        _login(username, password, LoginType.Email);
        return responsetxt;
    }

    public User getLoggedInUser() {

        if(ga.getLoggedInUser()!=null)
            return ga.getLoggedInUser();
        User user=null;

        String baseurlString = Global_Application.url+"users/me/";

        RestClient client = new RestClient(baseurlString);
        if(appPrefs.getToken()!=null){
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
        if(client.getResponseCode()== HttpStatus.SC_CREATED
                || client.getResponseCode() == HttpStatus.SC_OK
                || client.getResponseCode() == HttpStatus.SC_NO_CONTENT)
            return true;

        return false;
    }

    public boolean deleteUser(User user) {
        String baseurlString = Global_Application.url+"users/"+user.getId().toString()+"/";

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

        try {
            client.Execute(RequestMethod.DELETE);
        }catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, "deleteUser:" + client.toString());
        if(client.getResponseCode() == HttpStatus.SC_NO_CONTENT)
            return true;

        return false;
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

        if(profile.getSystolicPressure()>0)
            client.AddParam("systolic_pressure", profile.getSystolicPressure());

        if(profile.getDiastolicPressure()>0)
            client.AddParam("diastolic_pressure", profile.getDiastolicPressure());

        if(profile.getPulseRate()>0)
            client.AddParam("pulse_rate", profile.getPulseRate());

        if(profile.getFastingSugar()>0)
            client.AddParam("fasting", profile.getFastingSugar());

        if(profile.getRandomSugar()>0)
            client.AddParam("random", profile.getRandomSugar());

        if(profile.getHdl()>0)
            client.AddParam("hdl", profile.getHdl());

        if(profile.getLdl()>0)
            client.AddParam("ldl", profile.getLdl());

        if(profile.getTriglycerides()>0)
            client.AddParam("triglycerides", profile.getTriglycerides());

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

        if(profile.getDob()!=null && profile.getDob().getTime()>0)
            client.AddParam("date_of_birth", formatter.format(profile.getDob()));

        if(profile.getProfilePicURL()!=null && !profile.getProfilePicURL().isEmpty())
            client.AddParam("profile_picture_url", profile.getProfilePicURL());

        if(profile.getMobileNumber()!=null && !profile.getMobileNumber().isEmpty())
            client.AddParam("mobile", profile.getMobileNumber());

        if(profile.getBloodGroup()!=BloodGroup.None)
            client.AddParam("blood_group", profile.getBloodGroup().value());

//        client.AddParam("fb_profile_id", profile.getFbProfileId());
//        client.AddParam("fb_username", profile.getFbUsername());
        if(profile.getOrganization()!=null && !profile.getOrganization().isEmpty())
            client.AddParam("organization", profile.getOrganization());

        if(profile.getLocation().getStreet()!=null && !profile.getLocation().getStreet().isEmpty())
            client.AddParam("street", profile.getLocation().getStreet());

        if(profile.getLocation().getCity()!=null && !profile.getLocation().getCity().isEmpty())
            client.AddParam("city", profile.getLocation().getCity());

        if(profile.getLocation().getState()!=null && !profile.getLocation().getState().isEmpty())
            client.AddParam("state", profile.getLocation().getState());

        if(profile.getLocation().getCountry()!=null && !profile.getLocation().getCountry().isEmpty())
            client.AddParam("country", profile.getLocation().getCountry());

        if(profile.getLocation().getZip()!=null && !profile.getLocation().getZip().isEmpty())
            client.AddParam("zip_code", profile.getLocation().getZip());

        if(profile.getLocation().getLattitude()>0)
            client.AddParam("lattitude", profile.getLocation().getLattitude());

        if(profile.getLocation().getLongitude()>0)
            client.AddParam("longitude", profile.getLocation().getLongitude());

        if(profile.getLocation().getAddress()!=null && !profile.getLocation().getAddress().isEmpty())
            client.AddParam("address", profile.getLocation().getAddress());

        if(profile.getFbProfileId()!=null && !profile.getFbProfileId().isEmpty())
            client.AddParam("fb_profile_id", profile.getFbProfileId());

        if(profile.getFbUsername()!=null && !profile.getFbUsername().isEmpty())
            client.AddParam("fb_username", profile.getFbUsername());

        try{
            client.Execute(RequestMethod.PUT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());
        return processProfileResponse(responseString);
    }

    private User updateUser(Long userId, String firstName, String lastName, String email){
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
                if(email!=null && !email.isEmpty())
                    client.AddParam("email", email);

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

            if(!jsonUser.isNull("email"))
                user.setEmail(jsonUser.getString("email"));

            user.setUsername(jsonUser.getString("username"));
            user.setFirstName(jsonUser.getString("first_name"));
            user.setLastName(jsonUser.getString("last_name"));
            user.setProfile(processProfileResponse(jsonUser.getJSONObject("profile")));
            user.setMobile(user.getProfile().getMobileNumber());
            //user.setMobile(jsonUser.getString("mobile"));
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

        /* need to deserialize Profile object */
        try {
            pd.setHeight(jsonProfile.getInt("height"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            pd.setWeight(jsonProfile.getDouble("weight"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            pd.setBmr(jsonProfile.getInt("bmr"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //pd.setBmiClassifier();
        //pd.setLifeStyle();
        try {
            pd.setSystolicPressure(jsonProfile.getInt("systolic_pressure"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            pd.setDiastolicPressure(jsonProfile.getInt("diastolic_pressure"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            pd.setPulseRate(jsonProfile.getInt("pulse_rate"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            pd.setFastingSugar(jsonProfile.getInt("fasting"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            pd.setRandomSugar(jsonProfile.getInt("random"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            pd.setHdl(jsonProfile.getInt("hdl"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            pd.setLdl(jsonProfile.getInt("ldl"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            pd.setTriglycerides(jsonProfile.getInt("triglycerides"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            pd.setTotalCholesterol(jsonProfile.getInt("total_cholesterol"));
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
            if(!jsonProfile.isNull("date_of_birth"))
                pd.setDob(formatter.parse(jsonProfile.getString("date_of_birth")));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            pd.setBloodGroup(BloodGroup.get(jsonProfile.getInt("blood_group")));
        } catch (JSONException e) {
            pd.setBloodGroup(BloodGroup.None);
            e.printStackTrace();
        }

        try {
            if(!jsonProfile.isNull("fb_profile_id"))
                pd.setFbProfileId(jsonProfile.getString("fb_profile_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(!jsonProfile.isNull("fb_username"))
                pd.setFbUsername(jsonProfile.getString("fb_username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(!jsonProfile.isNull("organization"))
                pd.setOrganization(jsonProfile.getString("organization"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(!jsonProfile.isNull("profile_picture_url"))
                pd.setProfilePicURL(jsonProfile.getString("profile_picture_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(!jsonProfile.isNull("mobile"))
                pd.setMobileNumber(jsonProfile.getString("mobile"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(!jsonProfile.isNull("state"))
                location.setState(jsonProfile.getString("state"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(!jsonProfile.isNull("street"))
                location.setStreet(jsonProfile.getString("street"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(!jsonProfile.isNull("country"))
                location.setCountry(jsonProfile.getString("country"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(!jsonProfile.isNull("city"))
                location.setCity(jsonProfile.getString("city"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(!jsonProfile.isNull("zip_code"))
                location.setZip(jsonProfile.getString("zip_code"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            location.setLattitude(jsonProfile.getDouble("longitude"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            location.setLongitude(jsonProfile.getDouble("longitude"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(!jsonProfile.isNull("address"))
                location.setAddress(jsonProfile.getString("address"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        pd.setLocation(location);

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
        User updatedUser = updateUser(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());

        if(updatedUser==null)
            return null;

        //Update profile data
        user.setId(updatedUser.getId());
        user.setProfile(updateProfile(updatedUser.getId(), user.getProfile()));
        user.setBmiProfile(updateBMIProfile(updatedUser.getId(), user.getBmiProfile()));

        User loggedInUser = ga.getLoggedInUser();
        if(loggedInUser.getId()==user.getId())
            ga.setLoggedInUser(user);

        return user;
    }

}
