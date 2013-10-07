package com.viamhealth.android.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.facebook.widget.ProfilePictureView;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.activities.fragments.FBLoginFragment;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.FBUser;
import com.viamhealth.android.model.Profile;
import com.viamhealth.android.model.User;
import com.viamhealth.android.model.enums.BloodGroup;
import com.viamhealth.android.model.enums.Gender;
import com.viamhealth.android.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class NewProfile extends BaseFragmentActivity implements View.OnClickListener, FBLoginFragment.OnSessionStateChangeListener {

    static final String PENDING_REQUEST_BUNDLE_KEY = "com.facebook.samples.graphapi:PendingRequest";

    ViamHealthPrefs appPrefs;
    Global_Application ga;

    ProgressDialog dialog;

    String selecteduserid;

    EditText firstName, lastName, dob, location, organization, mobileNumber, email;
    ImageView profile_image;
    Button btnSave, btnCancel;
    ImageButton imgMale, imgFemale;
    ProfilePictureView profilePic;
    Spinner bloodGroup;

    User user = null;
    Profile profile = null;

    private FBLoginFragment fbLoginFragment;

    private enum FBRequestType {
        Profile, Family, FamilyProfile;
    }

    private enum RequestStatus { Not_Started, Pending, Done, Failed; }
    private RequestStatus familyMemberSelected = RequestStatus.Not_Started;
    private RequestStatus profileDataFetched = RequestStatus.Not_Started;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            fbLoginFragment = new FBLoginFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fbLoginFragment, fbLoginFragment)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            fbLoginFragment = (FBLoginFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fbLoginFragment);
        }

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_new_profile);

        ga=((Global_Application)getApplicationContext());
        appPrefs=new ViamHealthPrefs(this);

        Typeface tf = Typeface.createFromAsset(this.getAssets(), "Roboto-Condensed.ttf");

        //for get screen height width
        ScreenDimension();

        profilePic = (ProfilePictureView) findViewById(R.id.profilepic);
        profilePic.setDefaultProfilePicture(BitmapFactory.decodeResource(null, R.drawable.ic_social_add_person));

        Animation anim = AnimationUtils.loadAnimation(NewProfile.this, R.anim.fade_in);
        profilePic.setAnimation(anim);
        anim.start();

        Intent intent = getIntent();
        int registeredProfileCount = intent.getIntExtra("registeredProfilesCount", 0);
        user = (User) intent.getParcelableExtra("user");

        if(user==null)
            user = new User();

        profile = user.getProfile();

        if(profile==null){
            profile = new Profile();
            user.setProfile(profile);
        }

        imgMale = (ImageButton) findViewById(R.id.profile_img_male);
        imgFemale = (ImageButton) findViewById(R.id.profile_img_female);

        //by default set the gender as Male
        updateGender(Gender.Male);

        imgMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGender(Gender.Male);
            }
        });

        imgFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGender(Gender.Female);
            }
        });

        btnSave = (Button)findViewById(R.id.btnSave_profile);
        btnSave.setTypeface(tf);
        btnSave.setOnClickListener(this);

        btnCancel=(Button)findViewById(R.id.btnCancel_profile);
        btnCancel.setTypeface(tf);
        btnCancel.setOnClickListener(this);

        firstName = (EditText) findViewById(R.id.profile_first_name);
        lastName = (EditText) findViewById(R.id.profile_last_name);
        dob = (EditText) findViewById(R.id.profile_dob);
        location = (EditText) findViewById(R.id.profile_location);
        organization = (EditText) findViewById(R.id.profile_organization);
        mobileNumber = (EditText) findViewById(R.id.profile_phone);
        bloodGroup = (Spinner) findViewById(R.id.profile_blood_group);
        email = (EditText) findViewById(R.id.profile_email);

        profile.setBloodGroup(BloodGroup.O_Positive);

        profileDataFetched = RequestStatus.Not_Started;
        familyMemberSelected = RequestStatus.Not_Started;

        if(!user.isLoggedInUser()){
            String fbid = null;
            if(ga.getLoggedInUser().getProfile()!=null)
                fbid = ga.getLoggedInUser().getProfile().getFbProfileId();
            getDataFromFB(FBRequestType.Family, fbid);
        } else {
            email.setText(user.getEmail());
            email.setEnabled(false);
            getDataFromFB(FBRequestType.Profile, null);
        }
    }

    @Override
    public void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if(state.isOpened()){
            if(!user.isLoggedInUser()){
                String fbid = null;
                if(ga.getLoggedInUser().getProfile()!=null)
                    fbid = ga.getLoggedInUser().getProfile().getFbProfileId();
                getDataFromFB(FBRequestType.Family, fbid);
            } else {
                getDataFromFB(FBRequestType.Profile, null);
            }
        }
    }

    private void getDataFromFB(final FBRequestType type, final String profileId) {

        Session session = Session.getActiveSession();
        if (session != null && session.isOpened() ) {
            if(type==FBRequestType.Family){
                if(familyMemberSelected==RequestStatus.Pending || familyMemberSelected==RequestStatus.Done)
                    return;
                Intent intent = new Intent(NewProfile.this, FBFamilyListActivity.class);
                startActivityForResult(intent, 1);
                familyMemberSelected = RequestStatus.Pending;
            } else {
                if(profileDataFetched==RequestStatus.Done || profileDataFetched==RequestStatus.Pending)
                    return;
                getProfileDataFromFB(session, profileId);
                profileDataFetched = RequestStatus.Pending;
                dialog = new ProgressDialog(NewProfile.this);
                String whose = type==FBRequestType.Profile?"your":"your family members";
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("we are getting "+whose+" data...");
                dialog.show();
            }
        }
//
//        // start Facebook Login
//        Session.openActiveSession(this, true, new Session.StatusCallback() {
//            // callback when session changes state
//            @Override
//            public void call(Session session, SessionState state, Exception exception) {
//                if (session.isOpened()) {
//                }
//            }
//        });

    }

    private void getProfileDataFromFB(Session session, String profileId){
        String api = "me";
        if(profileId!=null && !profileId.isEmpty())
            api = profileId;
        Request request = Request.newGraphPathRequest(session, api, new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                GraphObject graphObject = response.getGraphObject();
                FacebookRequestError error = response.getError();
                if (graphObject != null) {
                    JSONObject jsonResponse = graphObject.getInnerJSONObject();
                    if (jsonResponse!=null) {
                        FBUser fbUser = deserialize(jsonResponse);
                        updateViewFromFBData(fbUser);
                        profileDataFetched = RequestStatus.Done;
                        dialog.dismiss();
                    }
                }

                if(error!=null)
                    profileDataFetched = RequestStatus.Failed;
            }
        });

        request.executeAsync();
        //Request.executeBatchAsync(request);
    }

    private FBUser deserialize(JSONObject jsonProfile){
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

        return fbUser;
    }

    private void updateGender(Gender gender) {
        int mresId, fresId;

        if(gender == Gender.Male){
            mresId = R.drawable.ic_men_enabled;
            fresId = R.drawable.ic_woman_disabled;
        }else{
            mresId = R.drawable.ic_man_disabled;
            fresId = R.drawable.ic_woman_enabled;
        }

        imgMale.setImageResource(mresId);
        imgFemale.setImageResource(fresId);
        profile.setGender(gender);
    }

    private void updateViewFromModel(User user) {
        if(!user.isLoggedInUser())
            email.setText(user.getEmail());
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());

        if(user.getProfile()!=null) {
            SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
            dob.setText(formater.format(user.getProfile().getDob()));

            updateGender(user.getProfile().getGender());

            if(user.getProfile().getLocation()!=null)
                location.setText(user.getProfile().getLocation().toShortString());

            profilePic.setProfileId(user.getProfile().getFbProfileId());
            mobileNumber.setText(user.getProfile().getMobileNumber());
            organization.setText(user.getProfile().getOrganization());

        }
    }

    private void updateViewFromFBData(FBUser fbUser){
        //get first name and last name
        user.setFirstName(fbUser.getFirstName());
        user.setLastName(fbUser.getLastName());

        //get DOB
        profile.setDob(fbUser.getBirthday());
        //get gender
        profile.setGender(Gender.get(fbUser.getGender()));

        //get the id
        profile.setFbProfileId(fbUser.getProfileId());
        profile.setFbUsername(fbUser.getProfileUsername());

        //get the location
        Profile.Location location = profile.new Location();
        profile.setLocation(location);
        location.setAddress(fbUser.getLocation());

        //get the email
        //user.setEmail(fbUser.getEmail());

        updateViewFromModel(user);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                familyMemberSelected = RequestStatus.Done;
                String profileId = data.getStringExtra("profileId");
                getDataFromFB(FBRequestType.FamilyProfile, profileId);
            }
        }else{//this is for facebook login pop-up
            super.onActivityResult(requestCode, resultCode, data);
            Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        }
    }

    private User generateModelFromView() {
        user.setEmail(email.getText().toString());
        user.setUsername(email.getText().toString());
        user.setFirstName(firstName.getText().toString());
        user.setLastName(lastName.getText().toString());

        profile.getLocation().setAddress(location.getText().toString());
        //Gender is updated in updateGender profile.setGender();
        String strDOB = dob.getText().toString();
        try{
            SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
            profile.setDob(formater.parse(strDOB));
        }catch(ParseException e){
            //just eat it up
            Log.e("NewProfileActivity", "Parse error of date in generateModelFromView");
        }

        //TODO::set Blood Group
        //profile.setBloodGroup(bloodGroup.);

        profile.setOrganization(organization.getText().toString());

        profile.setMobileNumber(mobileNumber.getText().toString());

        user.setProfile(profile);

        return user;
    }

    @Override
    public void onClick(View v) {
        Intent returnIntent = new Intent();

        if(v == btnSave){
            if(validate()){
                User user = generateModelFromView();
                returnIntent.putExtra("user", user);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        }else if(v == btnCancel) {
            setResult(RESULT_CANCELED, returnIntent);
            finish();
        }
    }

    private boolean validate() {
        boolean isValid = true;
        if(email.getText().length()==0){
            email.setError(getString(R.string.profile_email_not_present));
            isValid=false;
        } else if(!Validator.isEmailValid(email.getText().toString())){
            email.setError(getString(R.string.profile_email_not_valid));
            isValid=false;
        }
        if(firstName.getText().length()==0 && lastName.getText().length()==0){
            firstName.setError(getString(R.string.profile_first_name_not_present));
            isValid=false;
        }


        return isValid;

    }


}
