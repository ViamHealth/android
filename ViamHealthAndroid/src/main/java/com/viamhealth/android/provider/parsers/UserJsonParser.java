package com.viamhealth.android.provider.parsers;

import com.viamhealth.android.model.BaseModel;
import com.viamhealth.android.model.enums.BloodGroup;
import com.viamhealth.android.model.enums.Gender;
import com.viamhealth.android.model.users.BMIProfile;
import com.viamhealth.android.model.users.Profile;
import com.viamhealth.android.model.users.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naren on 17/12/13.
 */
public class UserJsonParser extends JsonParser {

    @Override
    protected BaseModel parse(JSONObject jsonObject) throws JSONException {
        User user = new User();

        try{
            user.setId(jsonObject.getLong("id"));

            if(!jsonObject.isNull("email"))
                user.setEmail(jsonObject.getString("email"));

            user.setUsername(jsonObject.getString("username"));
            user.setFirstName(jsonObject.getString("first_name"));
            user.setLastName(jsonObject.getString("last_name"));
            user.setProfile(processProfileResponse(jsonObject.getJSONObject("profile")));
            user.setMobile(user.getProfile().getMobileNumber());
            //user.setMobile(jsonUser.getString("mobile"));
            user.setBmiProfile(processBMIProfileResponse(jsonObject.getJSONObject("bmi_profile")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
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

@Override
    public List<BaseModel> newList() {
        return new ArrayList<BaseModel>();
    }
}
