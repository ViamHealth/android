package com.viamhealth.android.activities;

import java.util.ArrayList;
import java.util.List;

import com.facebook.widget.ProfilePictureView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

import com.viamhealth.android.dao.rest.endpoints.UserEP;

import com.viamhealth.android.model.users.User;
import com.viamhealth.android.utils.UIUtility;

import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
	User user;
    private DisplayImageOptions options;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.home);

        appPrefs = new ViamHealthPrefs(Home.this);
        ga=((Global_Application)getApplicationContext());
        userEndPoint = new UserEP(this, ga);

        if(getIntent().getBooleanExtra("logout", false))
        {
            Intent i = new Intent(Home.this,Login.class);
            appPrefs.setToken(null);
            startActivity(i);
            finish();
            return;
        }

        // for get screen diamention
        ScreenDimension();

        //calculate dynamic height width and padding
        w80=(int)((width*25)/100);
        w90=(int)((width*28.12)/100);
        w20=(int)((width*6.25)/100);
        w5=(int)((width*1.56)/100);
        w12=(int)((width*3.75)/100);

        h90=(int)((height*18.75)/100);
        h5=(int)((height*1.042)/100);
        h30=(int)((height*6.25)/100);


        bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);
        core_layout = (LinearLayout) findViewById(R.id.core_layout);

        //for generate square
        main_layout = (LinearLayout)findViewById(R.id.main_layout);
        main_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomLayout();
            }
        });

        hideBottomLayout();

        Button btnSetGoal = (Button) bottom_layout.findViewById(R.id.btnSetGoal);
        btnSetGoal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, TabActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("action", TabActivity.Actions.SetGoal);
                startActivity(intent);
            }
        });

        Button btnUploadFiles = (Button) bottom_layout.findViewById(R.id.btnUploadFiles);
        btnUploadFiles.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, TabActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("action", TabActivity.Actions.UploadFiles);
                startActivity(intent);
            }
        });


        lstFamily = new ArrayList<User>();

        if(isInternetOn()){
            CalluserMeTask task = new CalluserMeTask();
            task.applicationContext = Home.this;
            task.execute();
        }else{
            Toast.makeText(Home.this,"Network is not available....",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
	}

	public void ScreenDimension()
    {
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
        if(tile!=null){
            imgProfile = (ProfilePictureView)tile.findViewWithTag("ppic");
        }
        if(tile == null || imgProfile == null){ // if the tiel is not yet created then create it
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
                    FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT);

            FrameLayout frm = new FrameLayout(Home.this);
            frm.setLayoutParams(lp);
            frm.setId(position);
            frm.setOnClickListener(Home.this);

            imgProfile = new ProfilePictureView(Home.this);
            imgProfile.setDefaultProfilePicture(BitmapFactory.decodeResource(null, R.drawable.ic_social_add_person));
            imgProfile.setPresetSize(ProfilePictureView.LARGE);
            imgProfile.setLayoutParams(lp);
            imgProfile.setCropped(true);
            imgProfile.setTag("ppic");
            imgProfile.setProfileId(lstFamily.get(position).getProfile().getFbProfileId());

            Animation anim = AnimationUtils.loadAnimation(Home.this, R.anim.fade_in);
            imgProfile.setAnimation(anim);
            anim.start();

            frm.addView(imgProfile);

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
            imgProfile.setProfileId(lstFamily.get(position).getProfile().getFbProfileId());
            Animation anim = AnimationUtils.loadAnimation(Home.this, R.anim.fade_in);
            imgProfile.setAnimation(anim);
            anim.start();

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

        hideBottomLayout();

        Log.e("TAG","id is : " + v.getId());
        int index = v.getId();
        this.selectedViewPosition = index;
        LinearLayout tr1lay=(LinearLayout)tiles.get(index);
        Boolean shouldCreateProfile = index<lstFamily.size() && lstFamily.get(index).isProfileCreated() ?
                                        false : true;

        User selectedUser = null;
        if(lstFamily.size() > index) {
            selectedUser = lstFamily.get(index);
        }
        if(shouldCreateProfile){
            appPrefs.setBtnprofile_hide("1");
            Long userId = null;
            Boolean isLoggedInUser = false;

            Intent addProfileIntent = new Intent(Home.this, NewProfile.class);
            addProfileIntent.putExtra("user", selectedUser);
            startActivityForResult(addProfileIntent, index);
        }else{
            //FrameLayout tr1frm=(FrameLayout) tiles.get(index).findViewWithTag("frame");
            //LinearLayout tr1=(LinearLayout)tr1frm.getChildAt(1);
            //TextView txt = (TextView)tr1.getChildAt(0);
            //appPrefs.setProfileName(selectedUser.getName());
            //appPrefs.setGoalDisable("0");

            Intent intent = new Intent(Home.this, TabActivity.class);
            intent.putExtra("user", selectedUser);
            startActivity(intent);

        }

	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.selectedViewPosition = requestCode;
        if(resultCode==RESULT_OK){
            if(requestCode < 100) {
                user = (User) data.getParcelableExtra("user");
                if(isInternetOn()){
                    CallAddProfileTask task = new CallAddProfileTask();
                    task.applicationContext = Home.this;
                    task.execute();

                }else{

                }
            }else{//it is from tabactivity

            }
        }
    }

    private void showBottomLayout() {
        /*Animation slideUpIn = AnimationUtils.loadAnimation(Home.this, R.anim.slide_up);
        bottom_layout.startAnimation(slideUpIn);*/
        bottom_layout.setVisibility(View.VISIBLE);
        int blHeight = bottom_layout.getHeight();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) core_layout.getLayoutParams();
        params.height = height - blHeight - UIUtility.dpToPx(Home.this, 20);
        core_layout.setLayoutParams(params);
    }

    private void hideBottomLayout() {
        bottom_layout.setVisibility(View.GONE);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) core_layout.getLayoutParams();
        params.height = LinearLayout.LayoutParams.MATCH_PARENT;
        core_layout.setLayoutParams(params);
    }
    // async class for calling webservice and get responce message
    public class CallAddProfileTask extends AsyncTask<String, Void, String>
    {
        protected Context applicationContext;

        @Override
        protected void onPreExecute()
        {
            dialog = new ProgressDialog(Home.this);
            dialog.setMessage("still capturing your profile...");
            dialog.show();
        }

        protected void onPostExecute(String result)
        {
            if(result.toString().equals("0")){
                try{
                    generateTile(lstFamily.size()-1, false);
                    generateTile(lstFamily.size(), true);

                    /* Set the name in the bottom_layer and slide-it-up */
                    TextView name = (TextView) bottom_layout.findViewById(R.id.txtViewName);
                    name.setText(user.getName());

                    showBottomLayout();
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
		public class CalluserMeTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute() {
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				dialog = new ProgressDialog(Home.this);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setMessage("loading your family");
				dialog.show();
				Log.i("onPreExecute", "onPreExecute");
				
			}        
			 
			protected void onPostExecute(String result) {
                generateView();
				dialog.dismiss();
			}

			@Override
			protected String doInBackground(String... params) {
                lstFamily.clear();
				if(ga.getLoggedInUser()==null){
                    userEndPoint.getLoggedInUser();
                }
                lstFamily.add(ga.getLoggedInUser());
                lstFamily.addAll(userEndPoint.GetFamilyMembers());
				return null;
			}
			   
		}     

	// function for check internet is available or not
	public final boolean isInternetOn() {

		  ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		  if ((connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED)
		    || (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING)
		    || (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING)
		    || (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED)) {
		   return true;
		  }

		  else if ((connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED)
		    || (connec.getNetworkInfo(1).getState() ==  NetworkInfo.State.DISCONNECTED)) {
		   return false;
		  }

		  return false;
		 }
	@Override
    public void onBackPressed() 
        {
			moveTaskToBack(true);  
			System.exit(0);
			return;
         
        }


    public class ImproperArgumentsPassedException extends Exception {

        public ImproperArgumentsPassedException(String detailMessage) {
            super(detailMessage);
        }
    }
}
