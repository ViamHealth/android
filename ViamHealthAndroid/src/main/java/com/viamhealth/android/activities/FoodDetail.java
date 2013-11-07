package com.viamhealth.android.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.activities.fragments.TabHeaderFragment;
import com.viamhealth.android.adapters.GoalDataAdapter;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.dao.restclient.old.functionClass;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.viamhealth.android.model.users.User;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FoodDetail extends BaseFragmentActivity implements OnClickListener{

	Display display;
	int height,width;
	int h10,w10,h5,h20,w15,w20,h40,w5,w120;
	
	TextView calories,fat,carbs,proteins,sugar,cholestoral,sodieum,potassium,saturated,polyunsaturated,diatry_fiber;
	TextView monounsaturated,trans,vitaminA,calcium,vitaminC,iron,heding_Addfood_name,lbl_invite_user_food,titleTxt;
	LinearLayout menu_invite_addfood,menu_invite_out_addfood,settiglayout_food;
	ImageView back,person_icon;
	ProgressDialog dialog1;
	Button addBtn;
	ViamHealthPrefs appPrefs;
	Global_Application ga;
	Typeface tf;
	functionClass obj;
	private DisplayImageOptions options;
	String selecteduserid="0";

    User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.food_detail);
		
		//for get screen height width
        ScreenDimension();
        appPrefs = new ViamHealthPrefs(FoodDetail.this);
		obj=new functionClass(FoodDetail.this);
		ga=((Global_Application)getApplicationContext());
		tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
        w10=(int)((width*3.13)/100);
        h10=(int)((height*2.08)/100);
        
        h5=(int)((height*1.042)/100);
        
        h20=(int)((height*4.17)/100);
        w15=(int)((width*4.68)/100);
        w20=(int)((width*6.25)/100);
        w5=(int)((width*1.56)/100);
        w120=(int)((width*37.5)/100);
        h40=(int)((height*8.33)/100);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        TabHeaderFragment headerFragment = new TabHeaderFragment();
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        user = intent.getParcelableExtra("user");
        bundle.putParcelable("user", user);
        headerFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.headerlayout, headerFragment);
        fragmentTransaction.commit();

        LinearLayout main=(LinearLayout)findViewById(R.id.main);
        main.setPadding(0, h10, 0, h10);
        
        TableLayout tbl=(TableLayout)findViewById(R.id.tbl1);
        tbl.setPadding(w10, h10, 0, h10);
        
        LinearLayout l1=(LinearLayout)findViewById(R.id.l1);
        l1.setPadding(0, h5, 0, 0);
        
        addBtn= (Button)findViewById(R.id.addBtn);
        addBtn.setOnClickListener(FoodDetail.this);
        addBtn.setText("DONE");
        
        titleTxt = (TextView)findViewById(R.id.titleTxt);
        titleTxt.getLayoutParams().width = w120;
        titleTxt.setText(ga.getLstFood().get(ga.getFoodPos()).getName());
        
        calories=(TextView)findViewById(R.id.calciumVal);
        calories.setText(ga.getLstFood().get(ga.getFoodPos()).getCalories());
        
        fat=(TextView)findViewById(R.id.fatVal);
        fat.setText(ga.getLstFood().get(ga.getFoodPos()).getTotal_fat() +" g");
        
        carbs=(TextView)findViewById(R.id.carbsVal);
        carbs.setText(ga.getLstFood().get(ga.getFoodPos()).getTotal_carbohydrates());
        
        proteins=(TextView)findViewById(R.id.proteinVal);
        proteins.setText(ga.getLstFood().get(ga.getFoodPos()).getProtein() + "g");
        
        sugar=(TextView)findViewById(R.id.sugarVal);
        sugar.setText(ga.getLstFood().get(ga.getFoodPos()).getSugars() + " g");
        
        cholestoral=(TextView)findViewById(R.id.cholestorelVal);
        cholestoral.setText(ga.getLstFood().get(ga.getFoodPos()).getCholesterol() + " mg");
        
        sodieum=(TextView)findViewById(R.id.sodiumVal);
        sodieum.setText(ga.getLstFood().get(ga.getFoodPos()).getSodium() + " mg");
        
        potassium=(TextView)findViewById(R.id.potassiumVal);
        potassium.setText(ga.getLstFood().get(ga.getFoodPos()).getPotassium() + " mg");
        
        saturated=(TextView)findViewById(R.id.saturatedVal);
        saturated.setText(ga.getLstFood().get(ga.getFoodPos()).getSaturated_fat() + " g");
        
        polyunsaturated=(TextView)findViewById(R.id.polyunsaturatedVal);
        polyunsaturated.setText(ga.getLstFood().get(ga.getFoodPos()).getPolyunsaturated_fat() + " g");
        
        diatry_fiber=(TextView)findViewById(R.id.fiberVal);
        diatry_fiber.setText(ga.getLstFood().get(ga.getFoodPos()).getDietary_fiber() + " g");
        
        monounsaturated=(TextView)findViewById(R.id.monounsaturtedVal);
        monounsaturated.setText(ga.getLstFood().get(ga.getFoodPos()).getMonounsaturated_fat() + " g");
        
        trans=(TextView)findViewById(R.id.transVal);
        trans.setText(ga.getLstFood().get(ga.getFoodPos()).getTrans_fat() + " g");
        
        vitaminA=(TextView)findViewById(R.id.vitaminaVal);
        vitaminA.setText(ga.getLstFood().get(ga.getFoodPos()).getVitamin_a() + " %");
        
        calcium=(TextView)findViewById(R.id.calciumVal);
        calcium.setText(ga.getLstFood().get(ga.getFoodPos()).getCalcium() + " %");
        
        vitaminC=(TextView)findViewById(R.id.vitamincVal);
        vitaminC.setText(ga.getLstFood().get(ga.getFoodPos()).getVitamin_c() + " %");
        
        iron=(TextView)findViewById(R.id.ironVal);
        iron.setText(ga.getLstFood().get(ga.getFoodPos()).getIron() + " %");
	}
	 public static Bitmap getBitmapFromURL(String src) {
	     try {
	         URL url = new URL(src);
	         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	         connection.setDoInput(true);
	         connection.connect();
	         InputStream input = connection.getInputStream();
	         Bitmap myBitmap = BitmapFactory.decodeStream(input);
	         return myBitmap;
	     } catch (IOException e) {
	         e.printStackTrace();
	         return null;
	     }
	 }
	public void ScreenDimension()
	{
		display = getWindowManager().getDefaultDisplay(); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		width = display.getWidth();
		height = display.getHeight();

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==addBtn){
            finish();
            /*
			if(isInternetOn()){
				CallAddFoodTask task = new CallAddFoodTask();
				 task.activity =FoodDetail.this;
				 task.execute();
			}else{
				Toast.makeText(FoodDetail.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}*/
		}
	}
	// async class for calling webservice and get responce message
		public class CallAddFoodTask extends AsyncTask <String, Void,String>
		{
			protected FragmentActivity activity;

			@Override
			protected void onPreExecute()     
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				dialog1 = new ProgressDialog(FoodDetail.this);
				dialog1.setCanceledOnTouchOutside(false);
				dialog1.setMessage("Please Wait....");
				dialog1.show();
                Toast.makeText(getApplicationContext(),"Food Detail position before async task"+ga.getFoodPos(),Toast.LENGTH_LONG);
				Log.i("onPreExecute", "onPreExecute");
				
			}       
			
			protected void onPostExecute(String result)
			{
				
				Log.i("onPostExecute", "onPostExecute");
					dialog1.dismiss();
					//listfood.removeAllViews();
					
					if(result.equals("0")){
						Toast.makeText(FoodDetail.this, "Food added successfully...",Toast.LENGTH_SHORT).show();
					}
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK, returnIntent);
                    finish();
			}
	      
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
				//ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
			
				return obj.AddFood(ga.getLstFood().get(ga.getFoodPos()).getId(), ga.getFoodType().toUpperCase(), "1","user_id","");
			}
			   
		}     
		// async class for calling webservice and get responce message
		public class CalluserMeTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()        
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				dialog1 = new ProgressDialog(FoodDetail.this);
				dialog1.setCanceledOnTouchOutside(false);
				dialog1.setMessage("Please Wait....");
				dialog1.show();
				Log.i("onPreExecute", "onPreExecute");
				
			}        
			 
			protected void onPostExecute(String result)
			{
				
				Log.i("onPostExecute", "onPostExecute");
				//generateView();
				dialog1.dismiss();
			/*	Intent intent = new Intent(GoalActivity.this,MainActivity.class);
				startActivity(intent);*/
			}  
	   
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
				//obj.GetUserProfile(ga.getLstfamilyglobal().get(Integer.parseInt(selecteduserid)).getId());
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

		
}
