package com.viamhealth.android.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.viamhealth.android.activities.fragments.TabHeaderFragment;
import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.dao.restclient.old.functionClass;

import com.viamhealth.android.model.FoodData;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.viamhealth.android.adapters.FoodAdapter;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.adapters.GoalDataAdapter;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.ui.RefreshableListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddBreakfast extends BaseFragmentActivity implements OnClickListener{
	Display display;
	int height,width,w15,w20,w10,h10,w25,w50,w150,w5,h5,h40;
	
	TextView heding_Addfood_name,lblfoodBack,food_search_name,lbl_invite_user_food,lbl_add_food_name,
			 txt1,txt2,txt3,txt4,txt5,txt6,txt7,txt8,addfood_count;
	LinearLayout menu_invite_addfood,menu_invite_out_addfood,search_add_food_layout,back_addfood_layout,addfood_main_layout,
				 header_layout,food_header,settiglayout_food,add_heding;
	RefreshableListView listfood;
	EditText edt_add_food_search;
	ImageButton img_next,img_prev;
	int cnt_next=0,cnt_prev=0;
	ProgressDialog dialog,dialog1;
	Button btn_prev,btn_next;
	
	ViamHealthPrefs appPrefs;
	functionClass obj;
	ArrayList<FoodData> lstResult = new ArrayList<FoodData>();
	Global_Application ga;
	Typeface tf;
	String seach;
	ImageView back;
	int temp=0;
	String selecteduserid= "0";

    User user;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.add_breakfast);
		
		appPrefs = new ViamHealthPrefs(AddBreakfast.this);
		obj=new functionClass(AddBreakfast.this);
		ga=((Global_Application)getApplicationContext());
		tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
		
		ScreenDimension();
		
		w10=(int)((width*3.13)/100);
		w20=(int)((width*6.25)/100);
		w15=(int)((width*3.75)/100);
	    w25=(int)((width*7.85)/100);
	    w150=(int)((width*46.88)/100);
	    w5=(int)((width*1.56)/100);
	    w50=(int)((width*15.63)/100);
	    
		h10=(int)((height*2.083)/100);
		h5=(int)((height*1.042)/100);
		h40=(int)((height*8.33)/100);

        /* Add the header fragment*/
        //headerlayout;
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

		//control casting and control onclick management
		add_heding = (LinearLayout)findViewById(R.id.add_heding);
		add_heding.setPadding(0, 0, 0, h10);
		 
		food_header=(LinearLayout)findViewById(R.id.food_header);
		
		food_header.setPadding(w5, h5, w5, h5);
		
		food_search_name=(TextView)findViewById(R.id.food_search_name);
		food_search_name.setTypeface(tf);
		food_search_name.setPadding(w10, 0, 0, 0);
		
		
		lbl_add_food_name = (TextView)findViewById(R.id.lbl_add_food_name);
		lbl_add_food_name.setText(ga.getFoodType());
		header_layout=(LinearLayout)findViewById(R.id.header_layout);
		header_layout.setPadding(w5, h10, w5, h10);
		
		edt_add_food_search=(EditText)findViewById(R.id.edt_add_food_search);
		edt_add_food_search.setTypeface(tf);
		edt_add_food_search.getLayoutParams().width=w150;
		edt_add_food_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				seach=edt_add_food_search.getText().toString();
				if(isInternetOn()){
					 CallSearchFoodTask task = new CallSearchFoodTask();
					 task.applicationContext =AddBreakfast.this;
					 task.execute();
				}else{
					Toast.makeText(AddBreakfast.this,"Network is not available....",Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	
		addfood_main_layout = (LinearLayout)findViewById(R.id.addfood_main_layout);
		addfood_main_layout.setPadding(w10, h10, w10, h10);
		
		addfood_count = (TextView)findViewById(R.id.addfood_count);
		
		
		img_next=(ImageButton)findViewById(R.id.img_next);
		img_next.setOnClickListener(this);
		
		img_prev=(ImageButton)findViewById(R.id.img_prev);
		img_prev.setOnClickListener(this);
		
		txt1=(TextView)findViewById(R.id.txt1);
		txt1.setTypeface(tf,Typeface.BOLD);
		txt1.setOnClickListener(this);
		
		txt2=(TextView)findViewById(R.id.txt2);
		txt2.getLayoutParams().width=w15;
		txt2.setTypeface(tf,Typeface.BOLD);
		txt2.setOnClickListener(this);
		
		txt3=(TextView)findViewById(R.id.txt3);
		txt3.getLayoutParams().width=w15;
		txt3.setTypeface(tf,Typeface.BOLD);
		txt3.setOnClickListener(this);
		
		txt4=(TextView)findViewById(R.id.txt4);
		txt4.getLayoutParams().width=w15;
		txt4.setTypeface(tf,Typeface.BOLD);
		txt4.setOnClickListener(this);
		
		txt5=(TextView)findViewById(R.id.txt5);
		txt5.getLayoutParams().width=w15;
		txt5.setTypeface(tf,Typeface.BOLD);
		txt5.setOnClickListener(this);
		
		txt6=(TextView)findViewById(R.id.txt6);
		txt6.getLayoutParams().width=w15;
		txt6.setTypeface(tf,Typeface.BOLD);
		txt6.setOnClickListener(this);
		
		txt7=(TextView)findViewById(R.id.txt7);
		txt7.getLayoutParams().width=w15;
		txt7.setTypeface(tf,Typeface.BOLD);
		txt7.setOnClickListener(this);
		
		txt8=(TextView)findViewById(R.id.txt8);
		txt8.getLayoutParams().width=w15;
		txt8.setTypeface(tf,Typeface.BOLD);
		txt8.setOnClickListener(this);
		
		
		listfood=(RefreshableListView)findViewById(R.id.lstfood);
		  
		
		listfood.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
			
				ga.setLstFood(lstResult);
				ga.setFoodPos(position);
                Toast.makeText(getApplicationContext(),"onItemClick position "+position,Toast.LENGTH_LONG);
				//Intent foodDetail = new Intent(AddBreakfast.this, FoodDetail.class);
				//TabGroupActivity parentoption = (TabGroupActivity)AddBreakfast.this;
				//parentoption.startChildActivity("foodDetail",foodDetail);
                //foodDetail.putExtra("user", user);
                //startActivityForResult(foodDetail, 1);
                CallAddFoodTask tsk1= new CallAddFoodTask();
                tsk1.execute();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("user", user);
                setResult(RESULT_OK, returnIntent);
                finish();
			}
		});
		listfood.setOnRefreshListener(new RefreshableListView.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if(!ga.getNextfood().toString().equals("null")){
					temp=0;
					if(isInternetOn()){
						CallNevigationFoodTask task = new CallNevigationFoodTask();
						task.applicationContext =AddBreakfast.this;
						task.execute();
					}else{
						Toast.makeText(AddBreakfast.this,"Network is not available....",Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(AddBreakfast.this,"No Next Food Available",Toast.LENGTH_SHORT).show();
				}
			}
		   });
		
				
			
	}


    @Override
    protected void onPause() {
        super.onPause();

        if(dialog1!=null)
            dialog1.dismiss();
        dialog1 = null;

        if(dialog!=null)
            dialog.dismiss();
        dialog = null;
    }

    public class CallAddFoodTask extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
            dialog1 = new ProgressDialog(AddBreakfast.this);
            dialog1.setCanceledOnTouchOutside(false);
            dialog1.setMessage("Please Wait....");
            dialog1.show();
            Toast.makeText(getApplicationContext(),"Food Detail position before async task"+ga.getFoodPos(),Toast.LENGTH_LONG);
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            if(dialog1!=null)
                dialog1.dismiss();
            //listfood.removeAllViews();

            if(result.equals("0")){
                Toast.makeText(AddBreakfast.this, "Food added successfully...",Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);

            return obj.AddFood(ga.getLstFood().get(ga.getFoodPos()).getId(), ga.getFoodType().toUpperCase(), "1");
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                Intent returnIntent = new Intent();
                returnIntent.putExtra("user", user);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        }
    }

    public void ScreenDimension()
	{
		display = getWindowManager().getDefaultDisplay(); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		width = display.getWidth();
		height = display.getHeight();
		appPrefs.setSwidth(String.valueOf(width));
		appPrefs.setSheight(String.valueOf(height));
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==txt1){
			txt1.setTextColor(Color.RED);
			txt2.setTextColor(Color.BLACK);
			txt2.getLayoutParams().width=w15;
			txt3.setTextColor(Color.BLACK);
			txt3.getLayoutParams().width=w15;
			txt4.setTextColor(Color.BLACK);
			txt4.getLayoutParams().width=w15;
			txt5.setTextColor(Color.BLACK);
			txt5.getLayoutParams().width=w15;
			txt6.setTextColor(Color.BLACK);
			txt6.getLayoutParams().width=w15;
			txt7.setTextColor(Color.BLACK);
			txt7.getLayoutParams().width=w15;
			txt8.setTextColor(Color.BLACK);
			txt8.getLayoutParams().width=w15;
			seach=txt1.getText().toString();
			if(isInternetOn()){
				 CallSearchFoodTask task = new CallSearchFoodTask();
				 task.applicationContext = AddBreakfast.this;
				 task.execute();
			}else{
				Toast.makeText(AddBreakfast.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}
		}
		if(v==txt2){
			txt1.setTextColor(Color.BLACK);
			txt2.setTextColor(Color.RED);
			txt2.getLayoutParams().width=w15;
			txt3.setTextColor(Color.BLACK);
			txt3.getLayoutParams().width=w15;
			txt4.setTextColor(Color.BLACK);
			txt4.getLayoutParams().width=w15;
			txt5.setTextColor(Color.BLACK);
			txt5.getLayoutParams().width=w15;
			txt6.setTextColor(Color.BLACK);
			txt6.getLayoutParams().width=w15;
			txt7.setTextColor(Color.BLACK);
			txt7.getLayoutParams().width=w15;
			txt8.setTextColor(Color.BLACK);
			txt8.getLayoutParams().width=w15;
			seach=txt2.getText().toString();
			if(isInternetOn()){
				 CallSearchFoodTask task = new CallSearchFoodTask();
				 task.applicationContext =AddBreakfast.this;
				 task.execute();
			}else{
				Toast.makeText(AddBreakfast.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}
		}
		if(v==txt3){
			txt1.setTextColor(Color.BLACK);
			txt2.setTextColor(Color.BLACK);
			txt2.getLayoutParams().width=w15;
			txt3.setTextColor(Color.RED);
			txt3.getLayoutParams().width=w15;
			txt4.setTextColor(Color.BLACK);
			txt4.getLayoutParams().width=w15;
			txt5.setTextColor(Color.BLACK);
			txt5.getLayoutParams().width=w15;
			txt6.setTextColor(Color.BLACK);
			txt6.getLayoutParams().width=w15;
			txt7.setTextColor(Color.BLACK);
			txt7.getLayoutParams().width=w15;
			txt8.setTextColor(Color.BLACK);
			txt8.getLayoutParams().width=w15;
			seach=txt3.getText().toString();
			if(isInternetOn()){
				 CallSearchFoodTask task = new CallSearchFoodTask();
				 task.applicationContext =AddBreakfast.this;
				 task.execute();
			}else{
				Toast.makeText(AddBreakfast.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}
		}
		if(v==txt4){
			txt1.setTextColor(Color.BLACK);
			txt2.setTextColor(Color.BLACK);
			txt2.getLayoutParams().width=w15;
			txt3.setTextColor(Color.BLACK);
			txt3.getLayoutParams().width=w15;
			txt4.setTextColor(Color.RED);
			txt4.getLayoutParams().width=w15;
			txt5.setTextColor(Color.BLACK);
			txt5.getLayoutParams().width=w15;
			txt6.setTextColor(Color.BLACK);
			txt6.getLayoutParams().width=w15;
			txt7.setTextColor(Color.BLACK);
			txt7.getLayoutParams().width=w15;
			txt8.setTextColor(Color.BLACK);
			txt8.getLayoutParams().width=w15;
			seach=txt4.getText().toString();
			if(isInternetOn()){
				 CallSearchFoodTask task = new CallSearchFoodTask();
				 task.applicationContext =AddBreakfast.this;
				 task.execute();
			}else{
				Toast.makeText(AddBreakfast.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}
		}
		if(v==txt5){
			txt1.setTextColor(Color.BLACK);
			txt2.setTextColor(Color.BLACK);
			txt2.getLayoutParams().width=w15;
			txt3.setTextColor(Color.BLACK);
			txt3.getLayoutParams().width=w15;
			txt4.setTextColor(Color.BLACK);
			txt4.getLayoutParams().width=w15;
			txt5.setTextColor(Color.RED);
			txt5.getLayoutParams().width=w15;
			txt6.setTextColor(Color.BLACK);
			txt6.getLayoutParams().width=w15;
			txt7.setTextColor(Color.BLACK);
			txt7.getLayoutParams().width=w15;
			txt8.setTextColor(Color.BLACK);
			txt8.getLayoutParams().width=w15;
			seach=txt5.getText().toString();
			if(isInternetOn()){
				 CallSearchFoodTask task = new CallSearchFoodTask();
				 task.applicationContext =AddBreakfast.this;
				 task.execute();
			}else{
				Toast.makeText(AddBreakfast.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}
		}
		if(v==txt6){
			txt1.setTextColor(Color.BLACK);
			txt2.setTextColor(Color.BLACK);
			txt2.getLayoutParams().width=w15;
			txt3.setTextColor(Color.BLACK);
			txt3.getLayoutParams().width=w15;
			txt4.setTextColor(Color.BLACK);
			txt4.getLayoutParams().width=w15;
			txt5.setTextColor(Color.BLACK);
			txt5.getLayoutParams().width=w15;
			txt6.setTextColor(Color.RED);
			txt6.getLayoutParams().width=w15;
			txt7.setTextColor(Color.BLACK);
			txt7.getLayoutParams().width=w15;
			txt8.setTextColor(Color.BLACK);
			txt8.getLayoutParams().width=w15;
			
			seach=txt6.getText().toString();
			if(isInternetOn()){
				 CallSearchFoodTask task = new CallSearchFoodTask();
				 task.applicationContext =AddBreakfast.this;
				 task.execute();
			}else{
				Toast.makeText(AddBreakfast.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}
		}
		if(v==txt7){
			txt1.setTextColor(Color.BLACK);
			txt2.setTextColor(Color.BLACK);
			txt2.getLayoutParams().width=w15;
			txt3.setTextColor(Color.BLACK);
			txt3.getLayoutParams().width=w15;
			txt4.setTextColor(Color.BLACK);
			txt4.getLayoutParams().width=w15;
			txt5.setTextColor(Color.BLACK);
			txt5.getLayoutParams().width=w15;
			txt6.setTextColor(Color.BLACK);
			txt6.getLayoutParams().width=w15;
			txt7.setTextColor(Color.RED);
			txt7.getLayoutParams().width=w15;
			txt8.setTextColor(Color.BLACK);
			txt8.getLayoutParams().width=w15;
			seach=txt7.getText().toString();
			if(isInternetOn()){
				 CallSearchFoodTask task = new CallSearchFoodTask();
				 task.applicationContext =AddBreakfast.this;
				 task.execute();
			}else{
				Toast.makeText(AddBreakfast.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}
		}
		if(v==txt8){
			txt1.setTextColor(Color.BLACK);
			txt2.setTextColor(Color.BLACK);
			txt2.getLayoutParams().width=w15;
			txt3.setTextColor(Color.BLACK);
			txt3.getLayoutParams().width=w15;
			txt4.setTextColor(Color.BLACK);
			txt4.getLayoutParams().width=w15;
			txt5.setTextColor(Color.BLACK);
			txt5.getLayoutParams().width=w15;
			txt6.setTextColor(Color.BLACK);
			txt6.getLayoutParams().width=w15;
			txt7.setTextColor(Color.BLACK);
			txt7.getLayoutParams().width=w15;
			txt8.setTextColor(Color.RED);
			txt8.getLayoutParams().width=w15;
			seach=txt8.getText().toString();
			if(isInternetOn()){
				 CallSearchFoodTask task = new CallSearchFoodTask();
				 task.applicationContext =AddBreakfast.this;
				 task.execute();
			}else{
				Toast.makeText(AddBreakfast.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}
		}
		if(v==img_next){
			Log.e("TAG","next is clicked");
			cnt_next=cnt_next+1;
			next(cnt_next);
		}
		if(v==img_prev){
			Log.e("TAG","prev is clicked");
			cnt_next=cnt_next-1;
			next(cnt_next);
		}
	}
	public void next(int pos){
		if(pos<5){
		if(pos==1){
			txt1.setText("ALL");
			txt1.setTextColor(Color.BLACK);
			
			txt2.setText("A");
			txt2.setTextColor(Color.BLACK);
			
			txt3.setText("B");
			txt3.setTextColor(Color.BLACK);
			
			txt4.setText("C");
			txt4.setTextColor(Color.BLACK);
			
			txt5.setText("D");
			txt5.setTextColor(Color.BLACK);
			
			txt6.setText("E");
			txt6.setTextColor(Color.BLACK);
			
			txt7.setText("F");
			txt7.setTextColor(Color.BLACK);
			
			txt8.setText("G");
			txt8.setTextColor(Color.BLACK);
		}
		if(pos==2){
			txt1.setText("H");
			txt1.setTextColor(Color.BLACK);
			
			txt2.setText("I");
			txt2.setTextColor(Color.BLACK);
			
			txt3.setText("J");
			txt3.setTextColor(Color.BLACK);
			
			txt4.setText("K");
			txt4.setTextColor(Color.BLACK);
			
			txt5.setText("L");
			txt5.setTextColor(Color.BLACK);
			
			txt6.setText("M");
			txt6.setTextColor(Color.BLACK);
			
			txt7.setText("N");
			txt7.setTextColor(Color.BLACK);
			
			txt8.setText("O");
			txt8.setTextColor(Color.BLACK);
		}
		if(pos==3){
			txt1.setText("P");
			txt1.setTextColor(Color.BLACK);
			
			txt2.setText("Q");
			txt2.setTextColor(Color.BLACK);
			
			txt3.setText("R");
			txt3.setTextColor(Color.BLACK);
			
			txt4.setText("S");
			txt4.setTextColor(Color.BLACK);
			
			txt5.setText("T");
			txt5.setTextColor(Color.BLACK);
			
			txt6.setText("U");
			txt6.setTextColor(Color.BLACK);
			
			txt7.setText("V");
			txt7.setTextColor(Color.BLACK);
			
			txt8.setText("W");
			txt8.setTextColor(Color.BLACK);
		}
		if(pos==4){
			
			txt1.setTextColor(Color.BLACK);
			txt1.setText("X");
			
			txt2.setText("Y");
			txt2.setTextColor(Color.BLACK);
			
			txt3.setText("Z");
			txt3.setTextColor(Color.BLACK);
			
			txt4.setText(" ");
			txt4.setTextColor(Color.BLACK);
			
			txt5.setText(" ");
			txt5.setTextColor(Color.BLACK);
			
			txt6.setText(" ");
			txt6.setTextColor(Color.BLACK);
			
			txt7.setText(" ");
			txt7.setTextColor(Color.BLACK);
			
			txt8.setText(" ");
			txt8.setTextColor(Color.BLACK);
		}
		}
	}
	
	// async class for calling webservice and get responce message
	public class CallSearchFoodTask extends AsyncTask <String, Void,String>
	{
		protected Context applicationContext;

		@Override
		protected void onPreExecute()     
		{
			
			//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
			dialog = new ProgressDialog(AddBreakfast.this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("Please Wait....");
			dialog.show();
			Log.i("onPreExecute", "onPreExecute");
			
		}       
		
		protected void onPostExecute(String result)
		{
			
			Log.i("onPostExecute", "onPostExecute");
                if(dialog!=null)
				dialog.dismiss();
				//listfood.removeAllViews();
				
				if(lstResult.size()>0){   
					addfood_count.setText("("+lstResult.size()+")");
					food_search_name.setText(seach);
					FoodAdapter adapter = new FoodAdapter(AddBreakfast.this,R.layout.addfoodlist, lstResult);
				    listfood.setAdapter(adapter);
				    adapter.notifyDataSetChanged();
				    listfood.onRefreshComplete();
				}else{
                    if(dialog!=null)
					dialog.dismiss();
					Toast.makeText(AddBreakfast.this, "No Food found...",Toast.LENGTH_SHORT).show();
					addfood_count.setText("("+lstResult+")");
					   
					}
				 
		}  
      
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.i("doInBackground--Object", "doInBackground--Object");
			//ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
			lstResult.clear();
			
			lstResult.addAll(obj.SearchFoodItem(seach));
			return null;
		}
		   
	}     
	// async class for calling webservice and get responce message
	public class CallNevigationFoodTask extends AsyncTask <String, Void,String>
	{
		protected Context applicationContext;

		@Override
		protected void onPreExecute()     
		{
			
			//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
			dialog1 = new ProgressDialog(AddBreakfast.this);
			dialog1.setCanceledOnTouchOutside(false);
			dialog1.setMessage("Please Wait....");
			dialog1.show();
			Log.i("onPreExecute", "onPreExecute");
			
		}       
		
		protected void onPostExecute(String result)
		{
			
			Log.i("onPostExecute", "onPostExecute");
                if(dialog1!=null)
				    dialog1.dismiss();
				//listfood.removeAllViews();
				food_search_name.setText(seach);
				if(lstResult.size()>0){   
					addfood_count.setText("("+lstResult.size()+")");
					FoodAdapter adapter = new FoodAdapter(AddBreakfast.this,R.layout.addfoodlist, lstResult);
				    listfood.setAdapter(adapter);
				    adapter.notifyDataSetChanged();
				    listfood.onRefreshComplete();
				}else{
					Toast.makeText(AddBreakfast.this, "No Food found...",Toast.LENGTH_SHORT).show();
					addfood_count.setText("("+lstResult.size()+")");
					   
					}
				 
		}  
      
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.i("doInBackground--Object", "doInBackground--Object");
			//ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
			//lstResult.clear();
			if(temp==0){
				lstResult.addAll(obj.FoodNevigation(ga.getNextfood()));
			}else{
				lstResult.addAll(obj.FoodNevigation(ga.getPrevfood()));
			}

			return null;
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
			dialog1 = new ProgressDialog(AddBreakfast.this);
			dialog1.setCanceledOnTouchOutside(false);
			dialog1.setMessage("Please Wait....");
			dialog1.show();
			Log.i("onPreExecute", "onPreExecute");
			
		}        
		 
		protected void onPostExecute(String result)
		{
			
			Log.i("onPostExecute", "onPostExecute");
			//generateView();
            if(dialog1!=null)
			dialog1.dismiss();
		/*	Intent intent = new Intent(GoalActivity.this,MainActivity.class);
			startActivity(intent);*/
		}  
   
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.i("doInBackground--Object", "doInBackground--Object");
			//UserEP userEP = new UserEP(AddBreakfast.this);
            //userEP.getUserProfile(appPrefs.getLoggedInUser().getId());
			return null;
		}
		   
	}     
	 @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

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
