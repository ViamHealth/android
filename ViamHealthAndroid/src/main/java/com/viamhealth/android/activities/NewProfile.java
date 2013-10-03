package com.viamhealth.android.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
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

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.Profile;
import com.viamhealth.android.model.enums.Gender;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewProfile extends BaseActivity implements View.OnClickListener{

    ViamHealthPrefs appPrefs;
    Global_Application ga;

    ProgressDialog dialog;

    String selecteduserid;

    functionClass obj;

    EditText firstName, lastName, dob, location, organization, mobileNumber;
    DateFormat fmtDateAndTime=DateFormat.getDateTimeInstance();
    Calendar dateAndTime=Calendar.getInstance();
    int pYear,pMonth,pDay;
    private DisplayImageOptions options;

    ImageView profile_image;

    Button btnSave, btnCancel;
    ImageButton imgMale, imgFemale;

    ProfilePictureView profilePic;

    Profile profile = new com.viamhealth.android.model.Profile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_new_profile);

        appPrefs=new ViamHealthPrefs(this);
        obj = new functionClass(this);

        Typeface tf = Typeface.createFromAsset(this.getAssets(), "Roboto-Condensed.ttf");

        //for get screen height width
        ScreenDimension();

        profilePic = (ProfilePictureView) findViewById(R.id.profilepic);
        profilePic.setDefaultProfilePicture(BitmapFactory.decodeResource(null, R.drawable.ic_social_add_person));
        options = new DisplayImageOptions.Builder().build();

        Intent intent = getIntent();
        int registeredProfileCount = intent.getIntExtra("registeredProfilesCount", 0);
        if(registeredProfileCount==0)
            profile.setLoggedInUser(true);
        else
            profile.setLoggedInUser(false);

        imageLoader.displayImage(appPrefs.getProfilepic(), profile_image, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(Bitmap loadedImage) {
                Animation anim = AnimationUtils.loadAnimation(NewProfile.this, R.anim.fade_in);
                profile_image.setAnimation(anim);
                anim.start();
            }
        });


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

        // start Facebook Login
        Session.openActiveSession(this, true, new Session.StatusCallback() {

            // callback when session changes state
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {

                    // make request to the /me API
                    Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

                        // callback after Graph API response with user object
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            if (user != null && profile.isLoggedInUser()) {
                                updateProfileFromFBData(user);
                                appPrefs.setLoggedInUser(profile);
                            }
                        }
                    });
                }
            }
        });
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

    private void updateProfileFromFBData(GraphUser user){
        //get first name and last name
        profile.setFirstName(user.getFirstName());
        firstName.setText(profile.getFirstName());
        profile.setLastName(user.getLastName());
        lastName.setText(profile.getLastName());

        Log.i("VH", "Data from FB - DOB = " + user.getBirthday());
        //get DOB
        try{
            SimpleDateFormat dateFormater = new SimpleDateFormat("MM/dd/yyyy");
            Date date = dateFormater.parse(user.getBirthday());
            profile.setDob(date);

            dateFormater.applyPattern("dd/MM/yyyy");
            dob.setText(dateFormater.format(date));
        } catch(ParseException e){
            //Log.e("VH", "DOB reading exception", e.getCause());
        }

        //get gender

        //get the id
        profilePic.setProfileId(user.getId());
        profile.setFbProfileId(user.getId());

        //get the location
        profile.setLocation(user.getLocation());
        location.setText(profile.getLocation().toShortString());


        Log.i("VH", "Data from FB - Json = " + user.getInnerJSONObject());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {

    }
}
