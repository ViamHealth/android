package com.viamhealth.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.manager.ImageSelector;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.services.ServicesCommon;
import com.viamhealth.android.tasks.InviteUser;
import com.viamhealth.android.tasks.ShareUser;
import com.viamhealth.android.ui.helper.FileLoader;
import com.viamhealth.android.utils.Checker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shawn on 20/5/14.
 */
public class ProfileListActivity extends BaseActivity implements View.OnClickListener {

    Display display;
    int width,height;

    LinearLayout main_layout, bottom_layout, core_layout;
    List<LinearLayout> tiles = new ArrayList<LinearLayout>();
    List<FrameLayout> frames = new ArrayList<FrameLayout>();

    RelativeLayout splashScreen;
    ScrollView scroller;
    ProgressBar bar;
    TextView logoutMessage;

    ViamHealthPrefs appPrefs;
    Global_Application ga;
    int cnt=0,_count=0, selectedViewPosition = 0;
    int w80,w90,h90,w20,h5,w5,w12,h30;
    ArrayList<String> msgArray = new ArrayList<String>();
    List<User> lstFamily = null;
    ProgressDialog dialog;

    UserEP userEndPoint;
    User user, selectedUser;
    private DisplayImageOptions options;

    boolean justRegistered = false;
    boolean isEditMode = false;

    private ActionMode actionMode;

    private final String TAG = "Home";

    private static final int LOGIN_ACTIVITY = 10000;


    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance(this).activityStart(this);  // Add this method.
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance(this).activityStop(this);  // Add this method.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.r2_profile_list_view);

        appPrefs = new ViamHealthPrefs(ProfileListActivity.this);
        ga=((Global_Application)getApplicationContext());
        userEndPoint = new UserEP(this, ga);
        user = ga.getLoggedInUser();

        //for generate square
        scroller = (ScrollView)findViewById(R.id.scroller);
        splashScreen = (RelativeLayout) findViewById(R.id.splash);
        bar = (ProgressBar) splashScreen.findViewById(R.id.progressBar);
        logoutMessage = (TextView) splashScreen.findViewById(R.id.logoutMsg);
        logoutMessage.setVisibility(View.GONE);

        scroller.setVisibility(View.GONE);
        splashScreen.setVisibility(View.VISIBLE);


        if(appPrefs.getToken()==null || appPrefs.getToken().isEmpty()){
            Intent loginIntent = new Intent(ProfileListActivity.this, Login.class);
            startActivity(loginIntent);
        }else{
            if(getIntent().getBooleanExtra("logout", false)) {
                //logout();
                return;
            }

            main_layout = (LinearLayout)findViewById(R.id.main_layout);

            justRegistered = getIntent().getBooleanExtra("justRegistered", false);
            lstFamily = getIntent().getParcelableArrayListExtra("family");

            ScreenDimension();

            next();
        }
    }

    private void postLogin(){

        main_layout = (LinearLayout)findViewById(R.id.main_layout);
        justRegistered = getIntent().getBooleanExtra("justRegistered", false);
        lstFamily = getIntent().getParcelableArrayListExtra("family");
        ScreenDimension();
        next();
    }

    private void next() {
        next(false);
    }

    private void next(boolean moveToTabActivity) {
        //if the only logged-in user has not yet created the profile than
        //force for profile creation
        User user = ga.getLoggedInUser();
        boolean getFamilyData = false;
        if(user==null || lstFamily==null || lstFamily.size()==0){
            getFamilyData = true;
        }

        if(lstFamily!=null && lstFamily.size()>0){
            generateView();
            splashScreen.setVisibility(View.GONE);
            scroller.setVisibility(View.VISIBLE);
        }

        if(!getFamilyData && !user.isProfileCreated()){
            // logged In User's profile not yet completed then show this
            Intent addProfileIntent = new Intent(ProfileListActivity.this, NewProfile.class);
            addProfileIntent.putExtra("user", user);
            startActivityForResult(addProfileIntent, 0);
        } else if(getFamilyData) {//fetch the data
            lstFamily = new ArrayList<User>();
            if(Checker.isInternetOn(ProfileListActivity.this)){
                GetFamilyListTask task = new GetFamilyListTask();
                task.applicationContext = ProfileListActivity.this;
                task.execute();
            }else{
                Toast.makeText(ProfileListActivity.this, R.string.networkNotAvailable, Toast.LENGTH_SHORT).show();
            }
        } else if(moveToTabActivity){//take the user to the goals screen for the loggedInUser\
            splashScreen.setVisibility(View.GONE);
            scroller.setVisibility(View.VISIBLE);

            //Monjyoti:commented
            Intent intent = new Intent(ProfileListActivity.this, TabActivity.class);
            //Intent intent = new Intent(Home.this, SelectGoals.class);
            //MJ:condition if first time,goalfragment else tabactivity
            //Intent intent = new Intent(Home.this, GoalFragment.class);
            intent.putExtra("user", user);
            Parcelable[] users = new Parcelable[lstFamily.size()];
            intent.putExtra("users", lstFamily.toArray(users));
            startActivity(intent);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.home_menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean retVal = super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.menu_logout){
            //logout();
            return false;
        }

        if(item.getItemId()==R.id.menu_edit){
            startActionMode(new HomeActionModeCallback());
            return false;
        }

        if(item.getItemId() == R.id.menu_invite) {
            InviteUser inviteUser = new InviteUser(ProfileListActivity.this, ga);
            inviteUser.show();
        }

        if(item.getItemId() == R.id.menu_refresh) {
            if(Checker.isInternetOn(ProfileListActivity.this)){
                GetFamilyListTask task = new GetFamilyListTask();
                task.applicationContext = ProfileListActivity.this;
                task.execute();
            }else{
                Toast.makeText(ProfileListActivity.this,R.string.networkNotAvailable,Toast.LENGTH_SHORT).show();
            }
        }

        if(item.getItemId()==R.id.menu_change_password){
            View dialogView = LayoutInflater.from(ProfileListActivity.this).inflate(R.layout.dialog_change_password, null);
            final EditText old = (EditText) dialogView.findViewById(R.id.old);
            final EditText newP = (EditText) dialogView.findViewById(R.id.newP);
            final EditText newPAgain = (EditText) dialogView.findViewById(R.id.new_again);

            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileListActivity.this, R.style.AlertDialogGreenTheme);
            builder.setTitle(R.string.changePasswordTitle);
            builder.setView(dialogView);
            builder.setPositiveButton(R.string.change, new DialogInterface.OnClickListener() {
                private boolean isValid(){
                    String oStr = old.getText().toString();
                    String nStr = newP.getText().toString();
                    String naStr = newP.getText().toString();

                    if(oStr==null || oStr.isEmpty()){
                        old.setError(getString(R.string.oldPasswordMandatory));
                        return false;
                    }

                    if(nStr==null || nStr.isEmpty()){
                        newP.setError(getString(R.string.newPasswordMandatory));
                        return false;
                    }

                    if(naStr==null || naStr.isEmpty()){
                        newPAgain.setError(getString(R.string.confirmNewPasswordMandatory));
                        return false;
                    }

                    if(!nStr.equals(naStr)){
                        newP.setError(getString(R.string.newPasswordsDoNotMatch));
                        return false;
                    }

                    return true;
                }

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(Checker.isInternetOn(ProfileListActivity.this)){
                        if(isValid()){
                            ga.GA_eventButtonPress("home_change_password");
                            ChangePasswordTask task = new ChangePasswordTask();
                            task.execute(old.getText().toString(), newP.getText().toString());
                        }
                    }else{
                        Toast.makeText(ProfileListActivity.this, R.string.networkNotAvailable, Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.show();
            return false;
        }

        return retVal;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void ScreenDimension(){
        display = getWindowManager().getDefaultDisplay();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        width = display.getWidth();
        height = display.getHeight();
    }

    private void generateTile(int position, boolean shouldCreateAddNewProfileTile) throws ImproperArgumentsPassedException {
        LinearLayout horizontalLinearLayout;
        int horizontalPosition = position/2;
        if(position%2==0 && main_layout.getChildCount()<=horizontalPosition){
            horizontalLinearLayout = new LinearLayout(ProfileListActivity.this);
            horizontalLinearLayout.setTag("HLL"+horizontalPosition);
            main_layout.addView(horizontalLinearLayout);
        }else{
            horizontalLinearLayout = (LinearLayout) main_layout.findViewWithTag("HLL"+horizontalPosition);
        }


        if(shouldCreateAddNewProfileTile){
            LinearLayout tile = new LinearLayout(ProfileListActivity.this);
            tile.setOrientation(LinearLayout.VERTICAL);
            tile.setLayoutParams(new FrameLayout.LayoutParams(width / 2, width / 2));
            tile.setPadding(2, 2, 2, 2);
            ImageView img1 = new ImageView(ProfileListActivity.this);
            img1.setImageResource(R.drawable.addprofile_new);
            tile.addView(img1);
            tile.setGravity(Gravity.CENTER_VERTICAL);
            tile.setId(position);
            tile.setOnClickListener(ProfileListActivity.this);
            horizontalLinearLayout.addView(tile);
            tiles.add(tile);
            return;
        }

        //create or re-create the tile for the user
        LinearLayout tile = position<tiles.size()?tiles.get(position):null;
        if(lstFamily == null || position>=lstFamily.size())
            throw new ImproperArgumentsPassedException("Either there are no members in the family or the postion is greater than or equal to the family size");

        ProfilePictureView imgProfile = null;
        ImageView imgView = null;
        if(tile!=null){
            imgProfile = (ProfilePictureView)tile.findViewWithTag("ppic");
            imgView = (ImageView)tile.findViewWithTag("ppiciv");
        }
        if(tile == null || imgProfile == null || imgView == null){ // if the tiel is not yet created then create it
            if(tile != null){
                horizontalLinearLayout.removeViewAt(position%2);
                tiles.remove(position);
            }

            tile = new LinearLayout(ProfileListActivity.this);
            horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            tile.setTag(false);//Set Tag to true if the profile needs to be created
            tile.setOrientation(LinearLayout.VERTICAL);
            tile.setLayoutParams(new FrameLayout.LayoutParams(width / 2, width / 2));
            tile.setPadding(2, 2, 2, 2);

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    width/2, width/2);

            FrameLayout frm = new FrameLayout(ProfileListActivity.this);
            frm.setLayoutParams(lp);
            frm.setId(position);
            frm.setOnClickListener(ProfileListActivity.this);
            frm.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ga.GA_eventButtonPress("home_long_click");
                    int index = v.getId();
                    selectedViewPosition = index;
                    if(lstFamily.size() > index) {
                        selectedUser = lstFamily.get(index);
                    }
                    startActionMode(new HomeActionModeCallback());
                    return false;
                }
            });

            imgProfile = new ProfilePictureView(ProfileListActivity.this);
            imgProfile.setPresetSize(ProfilePictureView.LARGE);
            imgProfile.setLayoutParams(lp);
            imgProfile.setCropped(true);
            imgProfile.setTag("ppic");
            Log.d(TAG, "GenerateTile::profilePic- default being set to - social_add_person");
            imgProfile.setDefaultProfilePicture(BitmapFactory.decodeResource(null, R.drawable.ic_social_add_person));

            final ProfilePictureView ppv = imgProfile;
            final ImageView iv = new ImageView(ProfileListActivity.this);
            iv.setLayoutParams(lp);
            iv.setTag("ppiciv");
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

            User u = lstFamily.get(position);
            if(u==null || u.getProfile()==null || //if user or proile is not yet set
                    (u.getProfile().getFbProfileId()!=null && !u.getProfile().getFbProfileId().isEmpty()) // if fbId is valid one then
                    || (u.getProfile().getProfilePicURL()==null || u.getProfile().getProfilePicURL().isEmpty() // if profilePic is default or not set then
                    || u.getProfile().getProfilePicURL().endsWith("default_profile_picture_n.jpg"))){
                iv.setVisibility(View.GONE);
                imgProfile.setVisibility(View.VISIBLE);

                if(u!=null && u.getProfile()!=null)
                    imgProfile.setProfileId(u.getProfile().getFbProfileId());
            }else{
                FileLoader loader = new FileLoader(ProfileListActivity.this, null);
                loader.LoadFile(u.getProfile().getProfilePicURL(), u.getEmail() + "profilePic", new FileLoader.OnFileLoadedListener() {
                    @Override
                    public void OnFileLoaded(File file) {
                        Log.d(TAG, "GenerateTile::profilePic- default being set to - " + file.getAbsolutePath());
                        iv.setImageBitmap(ImageSelector.getReducedBitmapfromFile(file.getAbsolutePath(), width / 2, width / 2));
                        iv.setVisibility(View.VISIBLE);
                        ppv.setVisibility(View.GONE);
                    }
                });
            }

            imgView = iv;

            Animation anim = AnimationUtils.loadAnimation(ProfileListActivity.this, R.anim.fade_in);
            imgProfile.setAnimation(anim);
            imgView.setAnimation(anim);
            anim.start();

            frm.addView(imgProfile);
            frm.addView(imgView);

            LinearLayout lay = new LinearLayout(ProfileListActivity.this);
            lay.setOrientation(LinearLayout.VERTICAL);
            lay.setGravity(Gravity.BOTTOM);

            TextView txtName = new TextView(ProfileListActivity.this);
            txtName.setPadding(w5, h5, w5, h5);
            txtName.setTextColor(Color.WHITE);
            txtName.setBackgroundResource(R.color.textbg);
            txtName.setGravity(Gravity.CENTER);
            txtName.setText(lstFamily.get(position).getName());
            txtName.setTag("pname");
            //txtName.setTypeface();
            lay.addView(txtName);

            frm.addView(lay);
            frm.setTag("frame");
            tile.setId(position);
            tile.addView(frm);
            horizontalLinearLayout.addView(tile);
            tiles.add(tile);
        } else {
            imgProfile = (ProfilePictureView)tile.findViewWithTag("ppic");
            imgView = (ImageView) tile.findViewWithTag("ppiciv");

            Animation anim = AnimationUtils.loadAnimation(ProfileListActivity.this, R.anim.fade_in);
            imgProfile.setAnimation(anim);
            imgView.setAnimation(anim);
            anim.start();

            final ProfilePictureView ppv = imgProfile;
            final ImageView iv = imgView;
            User u = lstFamily.get(position);
            if(u!=null && u.getProfile()!=null && u.getProfile().getProfilePicURL()!=null &&
                    !u.getProfile().getProfilePicURL().isEmpty()){
                FileLoader loader = new FileLoader(ProfileListActivity.this, null);
                loader.LoadFile(u.getProfile().getProfilePicURL(), u.getEmail() + "profilePic", new FileLoader.OnFileLoadedListener() {
                    @Override
                    public void OnFileLoaded(File file) {
                        Log.d(TAG, "GenerateTile::profilePic- default being set to - " + file.getAbsolutePath());
                        iv.setImageBitmap(ImageSelector.getReducedBitmapfromFile(file.getAbsolutePath(), width/2, width/2));
                        iv.setVisibility(View.VISIBLE);
                        ppv.setVisibility(View.GONE);
                    }
                });
            }else{
                imgProfile.setProfileId(u.getProfile().getFbProfileId());
                iv.setVisibility(View.GONE);
                imgProfile.setVisibility(View.VISIBLE);
            }

            TextView txtName = (TextView)tile.findViewWithTag("pname");
            txtName.setText(lstFamily.get(position).getName());
        }
    }
    public void generateView(){
        String[] str = appPrefs.getMenuList().split(",");
        for(int i = 0; i<lstFamily.size(); i++){
            try{
                generateTile(i, false);
            } catch (ImproperArgumentsPassedException ime) {
                Toast.makeText(ProfileListActivity.this, "Not able to load the profiles", Toast.LENGTH_SHORT).show();
            }
        }
        try{
            //do not create a tile if there is only one profile and which is not yet created
            /*if(lstFamily.size()==1 && !lstFamily.get(0).isProfileCreated())
                return;*/
            generateTile(lstFamily.size(), true);
        } catch (ImproperArgumentsPassedException ime) {
            Toast.makeText(ProfileListActivity.this, "Not able to load the profiles", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Log.e("TAG", "id is : " + v.getId());
        int index = v.getId();
        this.selectedViewPosition = index;
        LinearLayout tr1lay=(LinearLayout)tiles.get(index);
        Boolean shouldCreateProfile = index<lstFamily.size() && lstFamily.get(index).isProfileCreated() ?
                false : true;

        if(lstFamily.size() > index) {
            selectedUser = lstFamily.get(index);
        }


        if(isEditMode){
            if(actionMode!=null){
                actionMode.setTitle(selectedUser.getName() + " selected");
            }
        }else{
            if(shouldCreateProfile){
                appPrefs.setBtnprofile_hide("1");
                Long userId = null;
                Boolean isLoggedInUser = false;

                Intent addProfileIntent = new Intent(ProfileListActivity.this, NewProfile.class);
                if(lstFamily.size() > index)
                {
                    addProfileIntent.putExtra("user", selectedUser);
                }
                startActivityForResult(addProfileIntent, index);
            }else{
                ga.GA_eventButtonPress("home_single_click__user");
                Intent addProfileIntent = new Intent(ProfileListActivity.this, NewProfile.class);
                addProfileIntent.putExtra("user", selectedUser);
                addProfileIntent.putExtra("isEditMode", true);
                startActivityForResult(addProfileIntent, selectedViewPosition);

                /*Intent intent = new Intent(ProfileListActivity.this, TabActivity.class);
                //Intent intent = new Intent(Home.this, SelectGoals.class);
                //MJ:condition if first time then goalfragment,otherwise tabactivity
                //Intent intent = new Intent(Home.this, GoalFragment.class);
                intent.putExtra("user", selectedUser);
                Parcelable[] users = new Parcelable[lstFamily.size()];
                intent.putExtra("users", lstFamily.toArray(users));
                startActivity(intent);*/
            }
        }
    }


    @Override
    public void onBackPressed() {
        if(actionMode!=null){
            actionMode.finish();
        }

        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.selectedViewPosition = requestCode;
        if(requestCode == LOGIN_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                postLogin();
            } else{
                finish();
            }
        }
        if(resultCode==RESULT_OK){
            if(requestCode < 100) {
                if(isEditMode && actionMode!=null)
                    actionMode.finish();

                user = (User) data.getParcelableExtra("user");
                if(Checker.isInternetOn(ProfileListActivity.this)){
                    CallAddProfileTask task = new CallAddProfileTask();
                    task.applicationContext = ProfileListActivity.this;
                    task.execute();

                }else{

                }
            }else{//it is from tabactivity

            }
        }
    }


    // async class for calling webservice and get responce message
    public class CallAddProfileTask extends AsyncTask<String, Void, String>
    {
        protected Context applicationContext;
        protected boolean isBeingUpdated;
        protected boolean profilPicBugIsBugUpdated;

        @Override
        protected void onPreExecute()
        {
            dialog = new ProgressDialog(ProfileListActivity.this, R.style.StyledProgressDialog);
            dialog.setMessage("capturing your profile...");
            dialog.show();
        }

        protected void onPostExecute(String result)
        {
            if(result.toString().equals("0")){
                try{
                    if(isBeingUpdated && profilPicBugIsBugUpdated == Boolean.TRUE){
                        generateTile(selectedViewPosition, false);
                    }else{
                        generateTile(lstFamily.size()-1, false);
                        generateTile(lstFamily.size(), true);
                    }

                } catch (ImproperArgumentsPassedException ime) {
                    Toast.makeText(ProfileListActivity.this, "Not able to load the profiles", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                Intent intent = new Intent(ProfileListActivity.this, TabActivity.class);
                intent.putExtra("user", user);
                Parcelable[] users = new Parcelable[lstFamily.size()];
                intent.putExtra("users", lstFamily.toArray(users));
                startActivity(intent);
            }else{
                dialog.dismiss();
                if(result.toString().equals("1")){
                    Toast.makeText(ProfileListActivity.this,
                            "Email/Mobile No. already registered. Edit details for "+user.getFirstName() + ".",
                            Toast.LENGTH_LONG).show();
                    try{

                        if(isBeingUpdated && profilPicBugIsBugUpdated == Boolean.TRUE){
                            lstFamily.set(selectedViewPosition, user);
                        }
                        else
                            lstFamily.add(user);
                        if(isBeingUpdated && profilPicBugIsBugUpdated == Boolean.TRUE){
                            generateTile(selectedViewPosition, false);
                        }else{
                            generateTile(lstFamily.size()-1, false);
                            generateTile(lstFamily.size(), true);
                        }


                    } catch (ImproperArgumentsPassedException ime) {
                        Toast.makeText(ProfileListActivity.this, "Not able to load the profiles", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ProfileListActivity.this, "Not able to add a new profile.", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(ProfileListActivity.this, ProfileListActivity.class);
                ArrayList<User> families = (ArrayList<User>) lstFamily;
                intent.putParcelableArrayListExtra("family", families);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }


        }

        @Override
        protected String doInBackground(String... params) {
            UserEP userEP = new UserEP(ProfileListActivity.this, ga);

            isBeingUpdated = (user.getId()>0)? true: false;
            profilPicBugIsBugUpdated = Boolean.FALSE;
            if(isBeingUpdated == true){
                profilPicBugIsBugUpdated = Boolean.TRUE;
            } else {
                for(User uu : lstFamily){
                    if(uu.getId() == user.getId()){
                        profilPicBugIsBugUpdated = Boolean.TRUE;
                        //isBeingUpdated = false;
                    }
                }
            }
            try{
                user = userEP.updateUser(user);
                if(isBeingUpdated && profilPicBugIsBugUpdated == Boolean.TRUE){
                    lstFamily.set(selectedViewPosition, user);
                }
                else
                    lstFamily.add(user);
                return "0";
            } catch ( IllegalArgumentException e){
                e.printStackTrace();
                return "1";
            }



        }
    }

    // async class for calling webservice and get responce message
    public class CallDeleteProfileTask extends AsyncTask<String, Void, Boolean>
    {
        protected Context applicationContext;

        @Override
        protected void onPreExecute(){
            dialog = new ProgressDialog(ProfileListActivity.this, R.style.StyledProgressDialog);
            dialog.setMessage("deleting the profile....");
            dialog.show();
        }

        protected void onPostExecute(Boolean result) {
            if(result){
                dialog.dismiss();
                lstFamily.remove(selectedViewPosition);
                Intent intent = new Intent(ProfileListActivity.this, ProfileListActivity.class);
                ArrayList<User> families = (ArrayList<User>) lstFamily;
                intent.putParcelableArrayListExtra("family", families);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else{
                dialog.dismiss();
                Toast.makeText(ProfileListActivity.this, "Not able to delete "+selectedUser.getName()+"...", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            UserEP userEP = new UserEP(ProfileListActivity.this, ga);
            boolean isSuccess = userEP.deleteUser(selectedUser);
            //lstFamily.set(selectedViewPosition, user);
            return isSuccess;
        }
    }

    // async class for calling webservice and get responce message
    public class GetFamilyListTask extends AsyncTask <String, Void,String>
    {
        protected Context applicationContext;

        @Override
        protected void onPreExecute() {
            //dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
            dialog = new ProgressDialog(ProfileListActivity.this, R.style.StyledProgressDialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("loading your family");
            //dialog.show();
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result) {
            if(lstFamily.isEmpty()){
                //logout();
                return;
            }
            splashScreen.setVisibility(View.GONE);
            scroller.setVisibility(View.VISIBLE);

            next();
            //dialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            lstFamily.clear();
            if(ga.getLoggedInUser()==null){
                userEndPoint.getLoggedInUser();
            }
            lstFamily.addAll(userEndPoint.GetFamilyMembers());
            return null;
        }

    }

    public class ChangePasswordTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ProfileListActivity.this, R.style.StyledProgressDialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("changing your password...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
            String msg;
            if(aBoolean){
                msg = "Changed your password successfully.";
            }else{
                msg = "Sorry! Couldn't change your password. Please try after some time.";
            }
            Toast.makeText(ProfileListActivity.this, msg, Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return userEndPoint.ChangePassword(params[0], params[1]);
        }
    }

    public class ImproperArgumentsPassedException extends Exception {
        public ImproperArgumentsPassedException(String detailMessage) {
            super(detailMessage);
        }
    }

    private final class HomeActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            isEditMode = true;
            actionMode = mode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            menu.clear();
            actionMode.setTitle(selectedUser.getName() + " selected");
            getSupportMenuInflater().inflate(R.menu.home, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
            switch(menuItem.getItemId()){
                case R.id.action_mode_edit: //edit
                    editUser();
                    return true;

                case R.id.action_mode_share: //share
                    shareUser();
                    return true;

                case R.id.action_mode_delete: //delete
                    deleteUser();
                    return true;
            }
            return false;
        }

        private void deleteUser() {
            ga.GA_eventButtonPress("home_delete_user");
            View dialogView = LayoutInflater.from(ProfileListActivity.this).inflate(R.layout.delete_confirmation, null);

            TextView message = (TextView) dialogView.findViewById(R.id.confirmMessage);
            message.setText("Are you sure?");// + selectedUser.getName() + "?");
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileListActivity.this, R.style.StyledProgressDialog);
            builder.setView(dialogView);
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (Checker.isInternetOn(ProfileListActivity.this)) {
                        ga.GA_eventButtonPress("home_delete_user_confirm");
                        CallDeleteProfileTask task = new CallDeleteProfileTask();
                        task.applicationContext = ProfileListActivity.this;
                        task.execute();
                    }
                }
            });
            builder.show();
            actionMode.finish();
        }

        private void shareUser() {
            ga.GA_eventButtonPress("home_share_user");
            ShareUser share = new ShareUser(ProfileListActivity.this, ga, selectedUser);
            share.show();
            actionMode.finish();
        }

        private void editUser() {
            ga.GA_eventButtonPress("home_share_user");
            Intent addProfileIntent = new Intent(ProfileListActivity.this, NewProfile.class);
            addProfileIntent.putExtra("user", selectedUser);
            addProfileIntent.putExtra("isEditMode", true);
            startActivityForResult(addProfileIntent, selectedViewPosition);
            actionMode.finish();
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            isEditMode = false;
        }
    }

}
