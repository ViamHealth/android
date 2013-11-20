package com.viamhealth.android.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Session;
import com.facebook.widget.ProfilePictureView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

import com.viamhealth.android.dao.rest.endpoints.UserEP;

import com.viamhealth.android.manager.ImageSelector;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.ui.helper.FileLoader;
import com.viamhealth.android.utils.Checker;
import com.viamhealth.android.utils.UIUtility;
import com.viamhealth.android.utils.Validator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Parcelable;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;

public class Home extends BaseActivity implements OnClickListener{
	Display display;
	int width,height;
	
	LinearLayout main_layout, bottom_layout, core_layout;
	List<LinearLayout> tiles = new ArrayList<LinearLayout>();
	List<FrameLayout> frames = new ArrayList<FrameLayout>();
	
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
		
		setContentView(R.layout.home);

        appPrefs = new ViamHealthPrefs(Home.this);
        ga=((Global_Application)getApplicationContext());
        userEndPoint = new UserEP(this, ga);
        user = ga.getLoggedInUser();

        if(appPrefs.getToken()==null || appPrefs.getToken().isEmpty()){
            Intent loginIntent = new Intent(Home.this, Login.class);
            startActivity(loginIntent);
        }else{
            if(getIntent().getBooleanExtra("logout", false)) {
                logout();
                return;
            }

            justRegistered = getIntent().getBooleanExtra("justRegistered", false);
            lstFamily = getIntent().getParcelableArrayListExtra("family");

            //for generate square
            main_layout = (LinearLayout)findViewById(R.id.main_layout);

            ScreenDimension();

            //next();
            lstFamily = new ArrayList<User>();

            //Monjyoti:commented
            if(Checker.isInternetOn(Home.this)){
                GetFamilyListTask task = new GetFamilyListTask();
                task.applicationContext = Home.this;
                task.execute();
            }else{
                Toast.makeText(Home.this,"Network is not available....",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void next() {
        //if the only logged-in user has not yet created the profile than
        //force for profile creation
        User user = ga.getLoggedInUser();
        boolean getFamilyData = false;
        if(user==null){
            getFamilyData = true;
        }else if(lstFamily==null || lstFamily.size()==0){
            getFamilyData = false;
            lstFamily = new ArrayList<User>();
            lstFamily.add(user);
        }

        if(lstFamily!=null && lstFamily.size()>0)
            generateView();

        if(!getFamilyData && !user.isProfileCreated()){
            // logged In User's profile not yet completed then show this
            Intent addProfileIntent = new Intent(Home.this, NewProfile.class);
            addProfileIntent.putExtra("user", user);
            startActivityForResult(addProfileIntent, 0);
        }

		else if(getFamilyData) {//fetch the data
            lstFamily = new ArrayList<User>();
            if(Checker.isInternetOn(Home.this)){
                GetFamilyListTask task = new GetFamilyListTask();
                task.applicationContext = Home.this;
                task.execute();
            }else{
                Toast.makeText(Home.this,"Network is not available....",Toast.LENGTH_SHORT).show();
            }
        }else {//take the user to the goals screen for the loggedInUser\

            //Monjyoti:commented
            Intent intent = new Intent(Home.this, TabActivity.class);
            intent.putExtra("user", user);
            Parcelable[] users = new Parcelable[lstFamily.size()];
            intent.putExtra("users", lstFamily.toArray(users));
            //startActivity(intent);

        }


    }

    private void logout() {
        if(ga.getLoggedInUser().getProfile()!=null && ga.getLoggedInUser().getProfile().getFbProfileId()!=null
                && !ga.getLoggedInUser().getProfile().getFbProfileId().isEmpty())
            callFacebookLogout();

        ga.setLoggedInUser(null);
        appPrefs.setToken(null);
        Intent i = new Intent(Home.this, Home.class);
        startActivity(i);
        finish();
    }

    public void callFacebookLogout() {
        Session session = Session.getActiveSession();
        if (session != null) {
            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
            }
        } else {
            session = new Session(Home.this);
            Session.setActiveSession(session);

            session.closeAndClearTokenInformation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.home_menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean retVal = super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.menu_logout){
            logout();
            return false;
        }

        if(item.getItemId()==R.id.menu_edit){
            startActionMode(new HomeActionModeCallback());
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
            horizontalLinearLayout = new LinearLayout(Home.this);
            horizontalLinearLayout.setTag("HLL"+horizontalPosition);
            main_layout.addView(horizontalLinearLayout);
        }else{
            horizontalLinearLayout = (LinearLayout) main_layout.findViewWithTag("HLL"+horizontalPosition);
        }


        if(shouldCreateAddNewProfileTile){
            LinearLayout tile = new LinearLayout(Home.this);
            tile.setOrientation(LinearLayout.VERTICAL);
            tile.setLayoutParams(new FrameLayout.LayoutParams(width / 2, width / 2));
            tile.setPadding(2, 2, 2, 2);
            ImageView img1 = new ImageView(Home.this);
            img1.setImageResource(R.drawable.addprofile_new);
            tile.addView(img1);
            tile.setGravity(Gravity.CENTER_VERTICAL);
            tile.setId(position);
            tile.setOnClickListener(Home.this);
            horizontalLinearLayout.addView(tile);
            tiles.add(tile);
            return;
        }

        //create or re-create the tile for the user
        LinearLayout tile = position<tiles.size()?tiles.get(position):null;
        if(lstFamily == null || position>=lstFamily.size())
            throw new Home.ImproperArgumentsPassedException("Either there are no members in the family or the postion is greater than or equal to the family size");

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

            tile = new LinearLayout(Home.this);
            horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            tile.setTag(false);//Set Tag to true if the profile needs to be created
            tile.setOrientation(LinearLayout.VERTICAL);
            tile.setLayoutParams(new FrameLayout.LayoutParams(width / 2, width / 2));
            tile.setPadding(2, 2, 2, 2);

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    width/2, width/2);

            FrameLayout frm = new FrameLayout(Home.this);
            frm.setLayoutParams(lp);
            frm.setId(position);
            frm.setOnClickListener(Home.this);
            frm.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int index = v.getId();
                    selectedViewPosition = index;
                    if(lstFamily.size() > index) {
                        selectedUser = lstFamily.get(index);
                    }
                    startActionMode(new HomeActionModeCallback());
                    return false;
                }
            });

            imgProfile = new ProfilePictureView(Home.this);
            imgProfile.setPresetSize(ProfilePictureView.LARGE);
            imgProfile.setLayoutParams(lp);
            imgProfile.setCropped(true);
            imgProfile.setTag("ppic");
            Log.d(TAG, "GenerateTile::profilePic- default being set to - social_add_person");
            imgProfile.setDefaultProfilePicture(BitmapFactory.decodeResource(null, R.drawable.ic_social_add_person));

            final ProfilePictureView ppv = imgProfile;
            final ImageView iv = new ImageView(Home.this);
            iv.setLayoutParams(lp);
            iv.setTag("ppiciv");

            User u = lstFamily.get(position);
            if(u!=null && u.getProfile()!=null && u.getProfile().getProfilePicURL()!=null &&
                    !u.getProfile().getProfilePicURL().isEmpty()){
                FileLoader loader = new FileLoader(Home.this, null);
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
                iv.setVisibility(View.GONE);
                imgProfile.setVisibility(View.VISIBLE);
            }
            imgView = iv;
            imgProfile.setProfileId(lstFamily.get(position).getProfile().getFbProfileId());

            Animation anim = AnimationUtils.loadAnimation(Home.this, R.anim.fade_in);
            imgProfile.setAnimation(anim);
            imgView.setAnimation(anim);
            anim.start();

            frm.addView(imgProfile);
            frm.addView(imgView);

            LinearLayout lay = new LinearLayout(Home.this);
            lay.setOrientation(LinearLayout.VERTICAL);
            lay.setGravity(Gravity.BOTTOM);

            TextView txtName = new TextView(Home.this);
            txtName.setPadding(w5, h5, w5, h5);
            txtName.setTextColor(Color.WHITE);
            txtName.setBackgroundResource(R.color.textbg);
            txtName.setGravity(Gravity.CENTER);
            txtName.setText(lstFamily.get(position).getName());
            txtName.setTag("pname");
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

            Animation anim = AnimationUtils.loadAnimation(Home.this, R.anim.fade_in);
            imgProfile.setAnimation(anim);
            imgView.setAnimation(anim);
            anim.start();

            final ProfilePictureView ppv = imgProfile;
            final ImageView iv = imgView;
            User u = lstFamily.get(position);
            if(u!=null && u.getProfile()!=null && u.getProfile().getProfilePicURL()!=null &&
                    !u.getProfile().getProfilePicURL().isEmpty()){
                FileLoader loader = new FileLoader(Home.this, null);
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
                Toast.makeText(Home.this, "Not able to load the profiles", Toast.LENGTH_SHORT).show();
            }
        }
        try{
            //do not create a tile if there is only one profile and which is not yet created
            /*if(lstFamily.size()==1 && !lstFamily.get(0).isProfileCreated())
                return;*/
            generateTile(lstFamily.size(), true);
        } catch (ImproperArgumentsPassedException ime) {
            Toast.makeText(Home.this, "Not able to load the profiles", Toast.LENGTH_SHORT).show();
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

                Intent addProfileIntent = new Intent(Home.this, NewProfile.class);
                if(lstFamily.size() > index)
                {
                    addProfileIntent.putExtra("user", selectedUser);
                }
                startActivityForResult(addProfileIntent, index);
            }else{
                Intent intent = new Intent(Home.this, TabActivity.class);
                intent.putExtra("user", selectedUser);
                Parcelable[] users = new Parcelable[lstFamily.size()];
                intent.putExtra("users", lstFamily.toArray(users));
                startActivity(intent);
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
        if(resultCode==RESULT_OK){
            if(requestCode < 100) {
                if(isEditMode && actionMode!=null)
                    actionMode.finish();

                user = (User) data.getParcelableExtra("user");
                if(Checker.isInternetOn(Home.this)){
                    CallAddProfileTask task = new CallAddProfileTask();
                    task.applicationContext = Home.this;
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

        @Override
        protected void onPreExecute()
        {
            dialog = new ProgressDialog(Home.this, R.style.StyledProgressDialog);
            dialog.setMessage("still capturing your profile...");
            dialog.show();
        }

        protected void onPostExecute(String result)
        {
            if(result.toString().equals("0")){
                try{
                    generateTile(lstFamily.size()-1, false);
                    generateTile(lstFamily.size(), true);
                } catch (ImproperArgumentsPassedException ime) {
                    Toast.makeText(Home.this, "Not able to load the profiles", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }else{
                dialog.dismiss();
                Toast.makeText(Home.this, "Not able to add a new profile...", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            UserEP userEP = new UserEP(Home.this, ga);
            boolean isBeingUpdated = (user.getId()>0)? true: false;
            user = userEP.updateUser(user);
            if(isBeingUpdated)
                lstFamily.set(selectedViewPosition, user);
            else
                lstFamily.add(user);
            return "0";
        }
    }

    // async class for calling webservice and get responce message
    public class CallDeleteProfileTask extends AsyncTask<String, Void, String>
    {
        protected Context applicationContext;

        @Override
        protected void onPreExecute(){
            dialog = new ProgressDialog(Home.this, R.style.StyledProgressDialog);
            dialog.setMessage("deleting the profile....");
            dialog.show();
        }

        protected void onPostExecute(String result) {
            if(result.toString().equals("0")){
                dialog.dismiss();
                Intent intent = new Intent(Home.this, Home.class);
                ArrayList<User> families = (ArrayList<User>) lstFamily;
                intent.putParcelableArrayListExtra("family", families);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else{
                dialog.dismiss();
                Toast.makeText(Home.this, "Not able to add a new profile...", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            UserEP userEP = new UserEP(Home.this, ga);
            userEP.deleteUser(selectedUser);
            lstFamily.set(selectedViewPosition, user);
            return "0";
        }
    }

    public class CallShareProfileTask extends AsyncTask<String, Void, String>
    {
        protected Context applicationContext;

        @Override
        protected void onPreExecute(){
            dialog = new ProgressDialog(Home.this, R.style.StyledProgressDialog);
            dialog.setMessage("sharing the profile....");
            dialog.show();
        }

        protected void onPostExecute(String result) {
            if(result.toString().equals("0")){
                dialog.dismiss();
            }else{
                dialog.dismiss();
                Toast.makeText(Home.this, "Not able to add a new profile...", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            UserEP userEP = new UserEP(Home.this, ga);
            userEP.shareUser(selectedUser);
            return "0";
        }
    }
    // async class for calling webservice and get responce message
    public class GetFamilyListTask extends AsyncTask <String, Void,String>
    {
        protected Context applicationContext;

        @Override
        protected void onPreExecute() {
            //dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
            dialog = new ProgressDialog(Home.this, R.style.StyledProgressDialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("loading your family");
            dialog.show();
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result) {
            if(lstFamily.isEmpty()){
                logout();
                return;
            }
            next();
            dialog.dismiss();
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
            View dialogView = LayoutInflater.from(Home.this).inflate(R.layout.delete_confirmation, null);

            TextView message = (TextView) dialogView.findViewById(R.id.confirmMessage);
            message.setText("delete " + selectedUser.getName() + "?");
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setView(dialogView);
            builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    CallDeleteProfileTask task = new CallDeleteProfileTask();
                    task.applicationContext = Home.this;
                    task.execute();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }

        private void shareUser() {
            View dialogView = LayoutInflater.from(Home.this).inflate(R.layout.share_profile, null);

            final EditText shareTo = (EditText) dialogView.findViewById(R.id.shareTo);
            CheckBox chkBox = (CheckBox) dialogView.findViewById(R.id.shareToSelf);
            chkBox.setText("Sharing with " + selectedUser.getName());

            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked && (shareTo.getText().toString()==null || shareTo.getText().toString().isEmpty())) {
                        if(selectedUser.getEmail()!=null && !selectedUser.getEmail().isEmpty())
                            shareTo.setText(selectedUser.getEmail());
                        else if(selectedUser.getProfile()!=null && selectedUser.getProfile().getMobileNumber()!=null
                                    && !selectedUser.getProfile().getMobileNumber().isEmpty())
                            shareTo.setText(selectedUser.getProfile().getMobileNumber());
                    }
                }
            });

            shareTo.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    String un = shareTo.getText().toString();
                    if(un.matches("^[0-9]{1,10}$"))
                        shareTo.setInputType(InputType.TYPE_CLASS_PHONE);
                    else if(Validator.isEmailValid(un))
                        shareTo.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    else
                        shareTo.setError("email of abc@gmail.com or mobile number without country code like 1234512345");

                    return false;
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setView(dialogView);
            builder.setCancelable(true);
            builder.setTitle("Share To...");
            builder.setPositiveButton("share", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    CallShareProfileTask task = new CallShareProfileTask();
                    task.applicationContext = Home.this;
                    task.execute();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }

        private void editUser() {
            Intent addProfileIntent = new Intent(Home.this, NewProfile.class);
            addProfileIntent.putExtra("user", selectedUser);
            addProfileIntent.putExtra("isEditMode", true);
            startActivityForResult(addProfileIntent, selectedViewPosition);
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            isEditMode = false;
        }
    }
}
