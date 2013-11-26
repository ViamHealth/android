package com.viamhealth.android.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
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
import com.viamhealth.android.activities.fragments.DatePickerFragment;
import com.viamhealth.android.activities.fragments.FBLoginFragment;
import com.viamhealth.android.dao.rest.endpoints.FileUploader;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.manager.ImageSelector;
import com.viamhealth.android.model.users.BMIProfile;
import com.viamhealth.android.model.users.FBUser;
import com.viamhealth.android.model.users.Profile;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.model.enums.BloodGroup;
import com.viamhealth.android.model.enums.Gender;
import com.viamhealth.android.ui.helper.FileLoader;
import com.viamhealth.android.utils.UIUtility;
import com.viamhealth.android.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter;
import antistatic.spinnerwheel.adapters.ArrayWheelAdapter;
import antistatic.spinnerwheel.adapters.NumericWheelAdapter;

public class NewProfile extends SherlockFragmentActivity implements View.OnClickListener,
        EditText.OnFocusChangeListener {

    static final String PENDING_REQUEST_BUNDLE_KEY = "com.facebook.samples.graphapi:PendingRequest";

    ViamHealthPrefs appPrefs;
    Global_Application ga;

    ProgressDialog dialog;

    String selecteduserid;

    EditText firstName, lastName, dob, location, organization, mobileNumber, email, height, weight;
    Button btnSave, btnCancel;
    ImageButton imgMale, imgFemale, imgUpload;
    ProfilePictureView profilePic;
    Spinner bloodGroup, relation;

    User user = null;
    Profile profile = null;
    ImageView imgView = null;

    boolean isImageSelected = false;

    private final String TAG = "NewProfile";

    private enum UserType {
        Manual, FB;
    }

    private enum FBRequestType {
        Profile, Family, FamilyProfile;
    }

    private enum RequestStatus { Not_Started, Pending, Done, Failed; }
    private RequestStatus familyMemberSelected = RequestStatus.Not_Started;
    private RequestStatus profileDataFetched = RequestStatus.Not_Started;

    private ImageSelector imageSelector;

    private UserType userType;

    private boolean isEditMode = false;
    private ActionBar actionBar;
    AbstractWheel feet,inches,cms;
    String[] arr= new String[96];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_new_profile);

        ga=((Global_Application)getApplicationContext());
        if(ga==null)
            throw new NullPointerException("Application in itself is null");

        appPrefs=new ViamHealthPrefs(this);

        Typeface tf = Typeface.createFromAsset(this.getAssets(), "Roboto-Condensed.ttf");



        Intent intent = getIntent();
        int registeredProfileCount = intent.getIntExtra("registeredProfilesCount", 0);
        user = (User) intent.getParcelableExtra("user");
        isEditMode = intent.getBooleanExtra("isEditMode", false);

        int i,j,k=0;

        for(i=1;i<=8;i++)
        {
            for(j=0;j<=11;j++)
            {
                arr[k]=""+i+" ' "+j;
                k++;
            }

        }

        //String str1[]= new String[]{"val1","val2","val3"};

        feet = (AbstractWheel) findViewById(R.id.feet);
        ArrayWheelAdapter<String> adapter =  new ArrayWheelAdapter<String>(this, arr);
        feet.setViewAdapter(adapter);
        feet.setCyclic(true);

        cms = (AbstractWheel) findViewById(R.id.cms);
        cms.setViewAdapter(new NumericWheelAdapter(this, 0, 300));
        cms.setCyclic(true);
        boolean flag=false;



        final OnWheelChangedListener heightListener = new OnWheelChangedListener() {
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                cms.setCurrentItem((int)((feet.getCurrentItem()+12)*2.54));
            }
        };




        feet.addChangingListener(heightListener);


        OnWheelChangedListener cmvalListener = new OnWheelChangedListener() {
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                feet.removeChangingListener(heightListener);
                if((cms.getCurrentItem()*2)/5<11)
                {
                    feet.setCurrentItem(0);
                }
                else
                {
                    feet.setCurrentItem((((cms.getCurrentItem()*2)/5)-12));
                }
                feet.addChangingListener(heightListener);
            }
        };





        cms.addChangingListener(cmvalListener);




        profilePic = (ProfilePictureView) findViewById(R.id.profilepic);
        imgView = (ImageView) findViewById(R.id.profilepiclocal);




        if(user!=null && user.getId()>0)
            isEditMode = true;

        if(user!=null && user.getProfile()!=null && user.getProfile().getFbProfileId()!=null
                && !user.getProfile().getFbProfileId().isEmpty())
            userType = UserType.FB;
        else
            userType = userType.Manual;

        if(user==null)
            user = new User();

        profile = user.getProfile();

        if(profile==null){
            profile = new Profile();
            user.setProfile(profile);
        }

        imgMale = (ImageButton) findViewById(R.id.profile_img_male);
        imgFemale = (ImageButton) findViewById(R.id.profile_img_female);

        imageSelector = new ImageSelector(NewProfile.this);

        imgUpload = (ImageButton) findViewById(R.id.imgBtnUpload);
        if(userType==UserType.Manual){
            imgUpload.setVisibility(View.VISIBLE);
            imgUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "OnCreate::OnClick of ImgUpload:: picking an image");
                    imageSelector.pickFile(ImageSelector.FileType.Image);
                }
            });
        }else{
            imgUpload.setVisibility(View.INVISIBLE);
        }

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

        firstName = (EditText) findViewById(R.id.profile_first_name);
        lastName = (EditText) findViewById(R.id.profile_last_name);
        dob = (EditText) findViewById(R.id.profile_dob);
        dob.setOnFocusChangeListener(this);
        location = (EditText) findViewById(R.id.profile_location);
        organization = (EditText) findViewById(R.id.profile_organization);
        mobileNumber = (EditText) findViewById(R.id.profile_phone);
        bloodGroup = (Spinner) findViewById(R.id.profile_blood_group);
        email = (EditText) findViewById(R.id.profile_email);
        relation = (Spinner) findViewById(R.id.profile_relation);
/*
        height = (EditText) findViewById(R.id.input_height);
        */

        weight = (EditText) findViewById(R.id.input_weight);


        //height.setOnKeyListener(this);
/*
        height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String units = "cms";
                if(!s.toString().contains(units)){
                   // height.setText(s.toString() + " " + units);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String units = "cms";
                //event.getNumber();
                if(!s.toString().contains(units)){
                    //height.setText(s.toString() + " " + units);
                }
            }
        });


*/

        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //if(s.)
                String units = "kgs";
                //event.getNumber();
                if(!s.toString().contains(units))
                    s.append(" " + units);
            }
        });

        //weight.setOnKeyListener(this);

        profileDataFetched = RequestStatus.Not_Started;
        familyMemberSelected = RequestStatus.Not_Started;

        /*if(!user.isLoggedInUser()){
            String fbid = null;
            Log.i(TAG, ga.getLoggedInUser().toString());
            if(ga.getLoggedInUser()!=null && ga.getLoggedInUser().getProfile()!=null)
                fbid = ga.getLoggedInUser().getProfile().getFbProfileId();
            if(fbid!=null && !fbid.isEmpty())
                getDataFromFB(FBRequestType.Family, fbid);
        } else {
            updateViewFromModel(user);
            //email.setText(user.getEmail());
            if(user.isLoggedInUser())
                email.setEnabled(false);
            //getDataFromFB(FBRequestType.Profile, null);
        }*/

        updateViewFromModel(user);

        if(user.isLoggedInUser() && user.getEmail()!=null && !user.getEmail().isEmpty())
            email.setEnabled(false);


        /*** Action Bar Creation starts here ***/
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        String title = isEditMode ? getString(R.string.edit) + " Profile" : getString(R.string.add_family_member);
        actionBar.setTitle(title);
        actionBar.setSubtitle(user.getName());
        actionBar.setHomeButtonEnabled(true);
        actionBar.setLogo(R.drawable.ic_action_white_brand);
        /*** Action bar Creation Ends Here ***/
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_add_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.fbFamilyMember){
            String fbid = ga.getLoggedInUser().getProfile().getFbProfileId();
            getDataFromFB(FBRequestType.Family, fbid);
            return true;
        }

        if(item.getItemId()== android.R.id.home){
            Intent returnIntent = new Intent();
            setResult(RESULT_CANCELED, returnIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getDataFromFB(final FBRequestType type, final String profileId) {
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened() ) {
            imgUpload.setVisibility(View.GONE);
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
        }else{
            imgUpload.setVisibility(View.VISIBLE);
        }
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
                        FBUser fbUser = FBUser.deserialize(jsonResponse);
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
        email.setText(user.getEmail());
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());

        if(user.getProfile()!=null) {
            if(user.getProfile().getDob()!=null) {
                SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
                dob.setText(formater.format(user.getProfile().getDob()));
            }

            updateGender(user.getProfile().getGender());

            if(user.getProfile().getLocation()!=null)
                location.setText(user.getProfile().getLocation().toShortString());

            profilePic.setProfileId(user.getProfile().getFbProfileId());
            mobileNumber.setText(user.getProfile().getMobileNumber());
            organization.setText(user.getProfile().getOrganization());

            if(user.getProfile().getFbProfileId()!=null && !user.getProfile().getFbProfileId().isEmpty()){
                imgUpload.setVisibility(View.GONE);
            }
        }

        if(user!=null && user.getProfile()!=null && user.getProfile().getProfilePicURL()!=null &&
                !user.getProfile().getProfilePicURL().isEmpty()){
            FileLoader loader = new FileLoader(NewProfile.this, appPrefs.getToken());
            loader.LoadFile(user.getProfile().getProfilePicURL(), user.getEmail() + "profilePic", new FileLoader.OnFileLoadedListener() {
                @Override
                public void OnFileLoaded(File file) {
                    int size = UIUtility.dpToPx(NewProfile.this, 120);
                    imgView.setImageBitmap(ImageSelector.getReducedBitmapfromFile(file.getAbsolutePath(), size, size));
                    imgView.setVisibility(View.VISIBLE);
                    profilePic.setVisibility(View.GONE);
                }
            });
        }else{
            profilePic.setDefaultProfilePicture(BitmapFactory.decodeResource(null, R.drawable.ic_social_add_person));
            imgView.setVisibility(View.GONE);
            profilePic.setVisibility(View.VISIBLE);
        }

        if(user.getBmiProfile()!=null){
            BMIProfile bmip = user.getBmiProfile();
            //Toast.makeText(getApplicationContext(),"retrieved height="+bmip.getHeight(),Toast.LENGTH_LONG).show();
            if(bmip.getHeight()>0){
                cms.setCurrentItem(bmip.getHeight());

                if((cms.getCurrentItem()*2)/5<11)
                {
                    feet.setCurrentItem(0);
                }
               else
                {
                    feet.setCurrentItem(((cms.getCurrentItem()*2)/5-12));
                }

            }
            //height.setText(bmip.getHeight().toString());
            if(bmip.getWeight()>0) weight.setText(bmip.getWeight().toString());
        }
    }

    private void updateViewFromFBData(FBUser fbUser){
        user = fbUser.toUser(user);

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
                Log.d(TAG, "onActivityResult::ProfileId selected from FB Family list - " + profileId);
                getDataFromFB(FBRequestType.FamilyProfile, profileId);
            }
        }else{//this is for facebook login pop-up
            if(imageSelector.onActivityResult(requestCode, resultCode, data)){
                Log.d(TAG, "onActivityResult::ImageSelected - " + imageSelector.getURI().toString());
                int size = UIUtility.dpToPx(NewProfile.this, 120);
                Bitmap bitmap = imageSelector.getBitmap(size, size);
                profilePic.setDefaultProfilePicture(bitmap);
                imgView.setImageBitmap(bitmap);
                isImageSelected = true;
            }else{
                Log.d(TAG, "onActivityResult::else");
                super.onActivityResult(requestCode, resultCode, data);
                Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
            }
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


        /* Get the BMI related data*/
        BMIProfile bmi = new BMIProfile();
        String[] strH = String.valueOf(cms.getCurrentItem()).split(" ");
        //height.getText().toString().split(" ");
        //Toast.makeText(getApplicationContext(),"stored height="+strH[0],Toast.LENGTH_LONG).show();

        String[] strW = weight.getText().toString().split(" ");
        bmi.setHeight(Integer.parseInt(strH[0]));
        bmi.setWeight(Double.parseDouble(strW[0]));

        user.setBmiProfile(bmi);

        /* TODO set BP, Diabetes and Cholesterol profiles too */

        return user;
    }

    protected void updateUser(User u) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("user", u);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent returnIntent = new Intent();

        if(v == btnSave){
            Log.d(TAG, "onClick : btnSave clicked - isImageSelected - " + isImageSelected);
            if(validate()){
                if(isImageSelected){
                    UploadProfilePicTask task = new UploadProfilePicTask();
                    task.execute();
                }else{
                    User newUser = generateModelFromView();
                    Log.d(TAG, "onClick : generated user from model " + newUser);
                    updateUser(newUser);
                }
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

        if(dob.getText().length()==0){
            dob.setError(getString(R.string.profile_dob_not_present));
            isValid = false;
        }
/*
        if(height.getText().length()==0){
            height.setError(getString(R.string.profile_height_not_present));
            isValid = false;
        }
*/

        if(weight.getText().length()==0){
            weight.setError(getString(R.string.profile_weight_not_present));
            isValid = false;
        }

        return isValid;

    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText text = (EditText) v;
        int inputType = text.getInputType();
        if(hasFocus){//when focused in
            if(inputType==(InputType.TYPE_CLASS_DATETIME|InputType.TYPE_DATETIME_VARIATION_DATE)){//if the editText is a dateTime filed then showTheDatePicker
                DialogFragment newFragment = new DatePickerFragment(text);
                newFragment.show(this.getSupportFragmentManager(), "datePicker");
            }
        }else{//when focused out

        }
    }

    public class UploadProfilePicTask extends AsyncTask<Void, Void, FileUploader.Response> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(NewProfile.this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("uploading your image..");
            dialog.show();
        }

        @Override
        protected void onPostExecute(FileUploader.Response response) {
            dialog.dismiss();
            Log.d(TAG, "AsyncTask : generating user from model");
            User newUser = generateModelFromView();
            newUser.getProfile().setProfilePicURL(response.getProfilePicUrl());
            Log.d(TAG, "AsyncTask : generated user from model " + newUser);
            updateUser(newUser);
        }

        @Override
        protected FileUploader.Response doInBackground(Void... params) {
            FileUploader uploader = new FileUploader(appPrefs.getToken());
            long userId = user.getId();
            if(userId==0){
                //create user
                Log.i(TAG, "AsyncTask : Creating a new user as userId = 0 ");
                UserEP userEP = new UserEP(NewProfile.this, ga);
                user = userEP.updateUser(user);
                userId = user.getId();
                Log.i(TAG, "AsyncTask : Creating a new user - " + user);
            }
            Log.i(TAG, "AsyncTask : Uploading the file now");
            FileUploader.Response response = uploader.uploadProfilePicture(imageSelector.getURI(),
                                                        NewProfile.this, userId, dialog);
            Log.i(TAG, "AsyncTask : Uploaded the file with response as " + response);
            return response;
        }
    }

}
