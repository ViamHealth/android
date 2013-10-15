package com.viamhealth.android.activities.oldones;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.activities.Home;
import com.viamhealth.android.activities.TabBaseActivity;
import com.viamhealth.android.adapters.GoalDataAdapter;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.dao.db.DataBaseAdapter;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.BPData;
import com.viamhealth.android.model.CholesterolData;
import com.viamhealth.android.model.GlucoseData;
import com.viamhealth.android.model.WeightData;

import com.viamhealth.android.ui.tab.scroller.CirclePageIndicator;
import com.viamhealth.android.ui.tab.scroller.PageIndicator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
public class GoalActivity extends TabBaseActivity implements OnClickListener
{
	private static ProgressDialog dialog;
	
	TextView Add_goal,lbl_profile, goal_count,lbl_goals,heading_name,lbl_invite_user_goal,lbl_invite_user,btn_cancle,btn_invite,lbl_add_goal,btn_goal_cancle,btn_goal_add,lbl_count;
	LinearLayout goal_txt_layout,setting_layout,menu_invite,menu_invite_out;
	FrameLayout main_layout;
	Display display;
	int width,height;  
	int w10,h10,w27,w5,w20,w3,h5,w280,h150,h40,w7,w1,w15,h7,h20,h15,h30,w150;  
	LinearLayout goal_list_layout;
	Typeface tf;
	Dialog sharedetails;
	
	TableLayout tb21;
	ImageView add_val,back,person_icon;
	TableLayout tb,tb11;
	
	
	ViamHealthPrefs appPrefs;
	functionClass obj;
	//ArrayList<GoalData> lstResult = new ArrayList<GoalData>();
	ArrayList<String> lstResultManage = new ArrayList<String>();
	String type,goalvalue;
	DataBaseAdapter dbobj;
	Global_Application ga;
	String goaltype="weight";
	WeightData weightdt = new WeightData();
	CholesterolData cholesteroldt = new CholesterolData();
	GlucoseData glucosedt = new GlucoseData();
	BPData bpdt = new BPData();
	
	String selecteduserid="0";

	// for pagger
	ViewPager mPager;   
	PageIndicator mIndicator;
	    
	
	// for get current date
	 DateFormat fmtDateAndTime=DateFormat.getDateTimeInstance();
     Calendar dateAndTime=Calendar.getInstance();
     int pYear,pMonth,pDay;
     String CurrDate;
     String urlDelete;
     int lastposition;
     
	private DisplayImageOptions options;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    super.onCreate(savedInstanceState);
	
	    //super.getSupportActionBar().setTitle("Sharat Khurana");
	    
		setContentView(R.layout.activity_goal);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		
		
		//Log.e("TAG","APP pregfs disable : "  +appPrefs.getGoalDisable());
		/*if(appPrefs.getGoalDisable().toString().equals("0")){
			appPrefs.setGoalDisable("1");
			
			Intent Profile = new Intent(getParent(),Profile.class);
			TabGroupActivity parentoption = (TabGroupActivity)getParent();
			parentoption.startChildActivity("Profile",Profile);
			
		}else{*/
		  	display();            
		//}
	}
	public void setIndicator()
    {
    	 CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
    	 indicator.setPadding(0, 0, 0, h40);
         mIndicator = indicator;
         indicator.setViewPager(mPager);

         final float density = getResources().getDisplayMetrics().density;
         indicator.setBackgroundColor(0xFFEFEFEF);
         indicator.setRadius(5 * density);
         indicator.setPageColor(0x887E8C8D);
         indicator.setFillColor(0xFF5CACE1);
         indicator.setStrokeColor(0x10000000);
         //indicator.setStrokeWidth(2 * density);
    }
	public void display(){
		tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
		// get screen height and width
		
		appPrefs = new ViamHealthPrefs(this.getParent());
		obj=new functionClass(this.getParent());
		ga=((Global_Application)getApplicationContext());
		dbobj=new DataBaseAdapter(this.getParent());
		
		
		ga.setCalcelflg(false);
		ScreenDimension();
		
		
		
		// calculate padding dynamically accroding to different screen
		w10=(int)((width*3.13)/100);
		w27=(int)((width*8.5)/100);
		w5=(int)((width*1.7)/100);
		w7=(int)((width*2.19)/100);
		w1=(int)((width*0.50)/100);
		w20=(int)((width*6.25)/100);
		w3=(int)((width*0.93)/100);
		w280=(int)((width*87.6)/100);
		w15=(int)((width*4.68)/100);
		w150=(int)((width*46.87)/100);
		
		h150=(int)((height*41.66)/100);
		h10=(int)((height*2.09)/100);
		h40=(int)((height*8.34)/100);
		h5=(int)((height*1.2)/100);
		h7=(int)((height*1.46)/100);
		h15=(int)((height*3.13)/100);
		h20=(int)((height*4.17)/100);
		h30=(int)((height*6.25)/100);
		
		// contol casting, set typeface of textview and edittext and manage onclicck of button
		
	    back=(ImageView)findViewById(R.id.back);
	 	back.setOnClickListener(GoalActivity.this);
	   
	    lbl_invite_user=(TextView)findViewById(R.id.lbl_invite_user_food);
        lbl_invite_user.setTypeface(tf);
        lbl_invite_user.setOnClickListener(this);
        
        lbl_profile=(TextView)findViewById(R.id.lbl_profile);
        lbl_profile.setTypeface(tf);
        lbl_profile.setOnClickListener(this);
     		
     		
        menu_invite= (LinearLayout)findViewById(R.id.menu_invite_food);
        menu_invite.setPadding(w15, 0, w20, 0);
        menu_invite.setOnClickListener(this);
     	
        person_icon = (ImageView)findViewById(R.id.person_icon);
        person_icon.getLayoutParams().width = w20;
        person_icon.getLayoutParams().height = h20;
     
        options = new DisplayImageOptions.Builder().build();
 		
 		imageLoader.displayImage(appPrefs.getProfilepic(), person_icon, options, new SimpleImageLoadingListener() {
 			@Override
 			public void onLoadingComplete(Bitmap loadedImage) {
 				Animation anim = AnimationUtils.loadAnimation(GoalActivity.this, R.anim.fade_in);
 				person_icon.setAnimation(anim);
 				anim.start();
 				
 				
 			}
 		});
        
     	heading_name=(TextView)findViewById(R.id.heding_name_food);
     	heading_name.setText(appPrefs.getProfileName());
   		heading_name.setTypeface(tf);
   		//heding_name_food.setPadding(0, 0, w50, 0);
   		
   		menu_invite_out = (LinearLayout)findViewById(R.id.menu_invite_out_food);
   		menu_invite_out.setOnClickListener(this);
   		menu_invite_out.setPadding(w15, 0, w20, 0);
   		     
   		setting_layout = (LinearLayout)findViewById(R.id.settiglayout_food);
   		setting_layout.setPadding(0, h40, w5, 0);
   		
		lbl_count=(TextView)findViewById(R.id.lbl_count);
		lbl_count.setTypeface(tf);
		
		main_layout = (FrameLayout)findViewById(R.id.main_layout);
		main_layout.setOnClickListener(this);  
		
		goal_txt_layout=(LinearLayout)findViewById(R.id.goal_txt_layout);
		goal_txt_layout.setPadding(w10, h7, w10, h7);  
		  
		lbl_goals = (TextView)findViewById(R.id.lbl_goals);
		lbl_goals.setTypeface(tf,Typeface.BOLD);
		
	    actionmenu();
		Add_goal=(TextView)findViewById(R.id.Add_goal);
		Add_goal.setPressed(true);
		Add_goal.setTypeface(tf,Typeface.BOLD);
		Add_goal.setOnClickListener(this);
		
		
		// for generate date
				 pYear = dateAndTime.get(Calendar.YEAR);
			     pMonth = dateAndTime.get(Calendar.MONTH);
			     pDay = dateAndTime.get(Calendar.DAY_OF_MONTH);
			     String s=(pMonth + 1)+"-"+pDay+"-"+pYear;
			        SimpleDateFormat curFormater = new SimpleDateFormat("MM-dd-yyyy"); 
			         Date dateObj=null;
				   try
				   {
				    dateObj = curFormater.parse(s);
				    
				   }
				   catch (ParseException e) 
				   {
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				   }    
				   
				   SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd"); 
				   String newDateStr = postFormater.format(dateObj);
				   CurrDate = newDateStr;
			   
		//goal_list_layout=(LinearLayout)findViewById(R.id.goal_list_layout);
		    mPager = (ViewPager)findViewById(R.id.pager);
		    mPager.setOnPageChangeListener(new MyPageChangeListener());
		    /*mPager.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(GoalActivity.this,"on page selected is call", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					Toast.makeText(GoalActivity.this,"on page scrolled is call", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(GoalActivity.this,"on page scroll state change is call", Toast.LENGTH_SHORT).show();
				}
			});*/
		   
			if(isInternetOn()){
				lstResultManage.clear();
					 Callweightask task = new Callweightask();
					 task.applicationContext =this.getParent();
					 task.execute();
				
			}else{
				Toast.makeText(GoalActivity.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}
	}
		

	
	public void actionmenu(){
		// for generate menu
		 final List<String> Goal_data;
		 Goal_data =Arrays.asList(appPrefs.getMenuList().toString().split("\\s*,\\s*"));
		 final GoalDataAdapter adapter = new GoalDataAdapter(this,R.layout.listview_item_row, Goal_data);
		        
		final ListView listView1 = (ListView)findViewById(R.id.listView1);
		listView1.setAdapter(adapter);
	
		listView1.setOnItemClickListener(new OnItemClickListener() {
		    	
		    	@Override    
				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long arg3) {
					// TODO Auto-generated method stub
		    		String value = ((TextView)view.findViewById(R.id.txtName)).getText().toString();
		    		/*((ImageView)view.findViewById(R.id.imgIcon)).setImageResource(R.drawable.tick);
					Log.e("TAG","Selected value is " + value);*/
		    	    
		    		Log.e("TAG","Selected value is " + value);
		    		appPrefs.setProfileName(value);
		    		heading_name.setText(appPrefs.getProfileName());
		    		for(int i=0;i<Goal_data.size();i++){
						if(value.toString().equals(appPrefs.getProfileName().toString())){
							Log.e("TAG","visible");
							((ImageView)view.findViewById(R.id.imgIcon)).setVisibility(View.VISIBLE);
						}else{
							Log.e("TAG","Invisible");
							((ImageView)view.findViewById(R.id.imgIcon)).setVisibility(View.INVISIBLE);
						}
				}
		    		Animation anim = AnimationUtils.loadAnimation(GoalActivity.this, R.anim.fade_out);
					setting_layout.startAnimation(anim);
					setting_layout.setVisibility(View.INVISIBLE);
					menu_invite.setVisibility(View.VISIBLE);
					menu_invite_out.setVisibility(View.INVISIBLE);
					
					selecteduserid = Integer.toString(position);
				
					CalluserMeTask task = new CalluserMeTask();
					task.applicationContext =GoalActivity.this.getParent();
					task.execute();
				}
		    	
			
		       });
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
	public void onClick(View v)    
	{
		// TODO Auto-generated method stub
		if(v==back){
			Intent i = new Intent(this.getParent(),Home.class);
			startActivity(i);
			finish();
		}
		if(v==Add_goal){
			if(mPager.getCurrentItem()==0){
			
				Intent i = new Intent(this.getParent(),AddWeightGoal.class);
				startActivity(i);
			}
			if(mPager.getCurrentItem()==1){
				Intent i = new Intent(this.getParent(),AddCholesterolGoal.class);
				startActivity(i);
			}
			if(mPager.getCurrentItem()==2){
				Intent i = new Intent(this.getParent(),AddGlucoseGoal.class);
				startActivity(i);
			}
			if(mPager.getCurrentItem()==3){
				Intent i = new Intent(this.getParent(),AddBPGoal.class);
				startActivity(i);
			}
			
		}
		if(v==lbl_invite_user_goal){
			Log.e("TAG","Selected value is " + "invite user is clicked");
			Animation anim = AnimationUtils.loadAnimation(GoalActivity.this, R.anim.fade_out);
			setting_layout.startAnimation(anim);
			setting_layout.setVisibility(View.INVISIBLE);
			menu_invite.setVisibility(View.VISIBLE);
			menu_invite_out.setVisibility(View.INVISIBLE);
			Intent i = new Intent(this.getParent(),InviteUser.class);
			startActivity(i);
			
		}
		
	/*	if(v.getId()==1){  
			Log.e("TAG","new val name is " + v.getId());
			TextView tr=(TextView)v;
			String tag=tr.getTag().toString();  
			Log.e("TAG","tag name is " + tag);
				tb=(TableLayout)goal_list_layout.getChildAt(Integer.parseInt(tag.substring(0,1)));
				TableRow tr1=(TableRow)tb.getChildAt(0);
				tb11=(TableLayout)tr1.getChildAt(0);
				TableRow tbtr1=(TableRow)tb11.getChildAt(0);   
				TextView tx1 = (TextView) tbtr1.getChildAt(1);
				TextView tx2 = (TextView) tbtr1.getChildAt(2);
				TextView tx3 = (TextView) tbtr1.getChildAt(3);
				Log.e("TAG","goal name is " + tx1.getText().toString());
			
			appPrefs.setGoalname(tx1.getText().toString());
			appPrefs.setGoaldesc(tx2.getText().toString());
			appPrefs.setGoalid(tx3.getText().toString());
			Intent AddNewVal = new Intent(getParent(),AddWeightValue.class);
			TabGroupActivity parentoption = (TabGroupActivity)getParent();
			parentoption.startChildActivity("AddNewVal",AddNewVal);
		}*/
		
	/*	if(v.getId()==3){
			Log.e("TAG","edit goal name is " + v.getId());
			TextView tr=(TextView)v;
			String tag=tr.getTag().toString();
			Log.e("TAG","tag name is " + tag);
				tb=(TableLayout)goal_list_layout.getChildAt(Integer.parseInt(tag.substring(0,1)));
				TableRow tr1=(TableRow)tb.getChildAt(0);
				tb11=(TableLayout)tr1.getChildAt(0);
				TableRow tbtr1=(TableRow)tb11.getChildAt(0);
				TextView tx1 = (TextView) tbtr1.getChildAt(1);
				TextView tx2 = (TextView) tbtr1.getChildAt(2);
				TextView tx3 = (TextView) tbtr1.getChildAt(3);
				Log.e("TAG","goal name is " + tx1.getText().toString());
			
			
			String[] temp=tx1.getTag().toString().split(",");
			goalvalue=temp[temp.length-1];
			appPrefs.setGoalval(goalvalue);
			appPrefs.setGoalid(tx3.getText().toString());
			appPrefs.setEdt("1");
			Intent i = new Intent(this.getParent(),AddWeightGoal.class);
			startActivity(i);
		}*/
	/*	if(v.getId()==4){  
			Log.e("TAG","del val name is " + v.getId());
			TextView tr=(TextView)v;
			String tag=tr.getTag().toString();
			Log.e("TAG","tag name is " + tag);
				tb=(TableLayout)goal_list_layout.getChildAt(Integer.parseInt(tag.substring(0,1)));
				TableRow tr1=(TableRow)tb.getChildAt(0);
				tb11=(TableLayout)tr1.getChildAt(0);
				TableRow tbtr1=(TableRow)tb11.getChildAt(0);
				TextView tx1 = (TextView) tbtr1.getChildAt(1);
				TextView tx2 = (TextView) tbtr1.getChildAt(2);
				TextView tx3 = (TextView) tbtr1.getChildAt(3);
				Log.e("TAG","goal name is " + tx1.getText().toString());
			
			appPrefs.setGoalname(tx1.getText().toString());
			appPrefs.setGoaldesc(tx2.getText().toString());
			appPrefs.setGoalid(tx3.getText().toString());
			type="0";
			String[] temp=tx1.getTag().toString().split(",");
			goalvalue=temp[temp.length-1];
			AlertDialog diaBox = makeAndShowDialogBox();
         	diaBox.show();
			 if(isInternetOn()){
				 CallManageGoalTask task = new CallManageGoalTask();
				 task.applicationContext =this.getParent();
				 task.execute();
				// appPrefs.setReloadgraph("0");
			}else{
				Toast.makeText(GoalActivity.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}     
		}*/
		if(v==lbl_invite_user){
			Log.e("TAG","Selected value is " + "invite user is clicked");
			Animation anim = AnimationUtils.loadAnimation(GoalActivity.this, R.anim.fade_out);
			setting_layout.startAnimation(anim);
			setting_layout.setVisibility(View.INVISIBLE);
			menu_invite.setVisibility(View.VISIBLE);
			menu_invite_out.setVisibility(View.INVISIBLE);
			Log.e("TAG","Clicked");
			Intent i = new Intent(GoalActivity.this,InviteUser.class);
			startActivity(i);
		}
		if(v==lbl_profile){
			Log.e("TAG","Selected value is " + "invite user is clicked");
			Animation anim = AnimationUtils.loadAnimation(GoalActivity.this, R.anim.fade_out);
			setting_layout.startAnimation(anim);
			setting_layout.setVisibility(View.INVISIBLE);
			menu_invite.setVisibility(View.VISIBLE);
			menu_invite_out.setVisibility(View.INVISIBLE);
			Log.e("TAG","Clicked");
			Intent Profile = new Intent(getParent(), com.viamhealth.android.activities.Profile.class);
			TabGroupActivity parentoption = (TabGroupActivity)getParent();
			parentoption.startChildActivity("Profile",Profile);
		}
	
			if(v==menu_invite){
				actionmenu();
				setting_layout.setVisibility(View.VISIBLE);
			menu_invite_out.setVisibility(View.VISIBLE);
			menu_invite.setVisibility(View.INVISIBLE);
			Animation anim = AnimationUtils.loadAnimation(GoalActivity.this, R.anim.fade_in);
			setting_layout.startAnimation(anim);
			
			Log.e("TAG","Clicked");
		}
		if(v==menu_invite_out){
			Animation anim = AnimationUtils.loadAnimation(GoalActivity.this, R.anim.fade_out);
			setting_layout.startAnimation(anim);
			setting_layout.setVisibility(View.INVISIBLE);
			menu_invite.setVisibility(View.VISIBLE);
			menu_invite_out.setVisibility(View.INVISIBLE);
			Log.e("TAG","Clicked");
		}
		
	}
	
/*	public void showlist()
    {
    	
		goal_list_layout=(LinearLayout)findViewById(R.id.goal_list_layout);
    	
		goal_list_layout.removeAllViews();
		for(int i=0;i<ga.lstResult.size();i++)
    	{
    	
    	    tb=new TableLayout(GoalActivity.this);
    		tb.setTag("maintbl");
    		tb.setPadding(0, 0, 0, h10);
    		tb.setGravity(Gravity.CENTER_HORIZONTAL);
    		TableRow tr1=new TableRow(GoalActivity.this);
    		tr1.setTag("tr"+1);
    		//tr1.setPadding(2, 0, 2, 0);
    		 tb11=new TableLayout(GoalActivity.this);
    			tb11.setBackgroundResource(R.drawable.r1); 
    			tb11.setTag("tb" + i);
    			tb11.setGravity(Gravity.CENTER_HORIZONTAL);
    			
    				TableRow tb111=new TableRow(GoalActivity.this);
    				tb111.setGravity(Gravity.CENTER_VERTICAL);
    				tb111.setPadding(w20, h5, 0, h5);
    			
    				ImageView icon1=new ImageView(GoalActivity.this);
    				icon1.setImageResource(R.drawable.icon1);
    				tb111.addView(icon1);  
    				
    				TextView name=new TextView(GoalActivity.this);    //GoalActivity Name
    				name.setPadding(w5, 0, 0, 0);
    				name.setTag(ga.lstResult.get(i).getGn() + "," + ga.lstResult.get(i).getGv());
    				name.setText(ga.lstResult.get(i).getGn().toString());
    				name.setTextColor(Color.BLACK);
    				name.setTextSize(18);
    				name.setTypeface(tf,Typeface.BOLD);
    				tb111.addView(name);
    				
    				TextView desc=new TextView(GoalActivity.this);    //GoalActivity Desc
    				desc.setTextSize(15);
    				desc.setTypeface(tf);
    				desc.setPadding(w5, 0, 0, 0);
    				desc.setText(ga.lstResult.get(i).getGd());  
    				desc.setTextColor(Color.BLACK);
    				tb111.addView(desc);
    				
    				TextView goalid=new TextView(GoalActivity.this);    //GoalActivity Desc
    				goalid.setTextSize(15);
    				goalid.setTypeface(tf);
    				goalid.setPadding(w5, 0, 0, 0);
    				goalid.setText(ga.lstResult.get(i).getId());  
    				goalid.setTextColor(Color.BLACK);
    				tb111.addView(goalid);
    				goalid.setVisibility(View.INVISIBLE);
    				LinearLayout layoutsetting = new LinearLayout(GoalActivity.this);
    				layoutsetting.setGravity(Gravity.RIGHT);
    				layoutsetting.setPadding(w27, 0, 0, 0);
    				ImageView settings = new ImageView(GoalActivity.this);
    				settings.setImageResource(R.drawable.graphsetting);
    				
    				layoutsetting.addView(settings);
    				tb111.addView(layoutsetting);
    				
    				tb11.addView(tb111);  
    			tr1.addView(tb11);  
    		tb.addView(tr1);
    		
    		
    		TableRow tr2=new TableRow(GoalActivity.this);
    		tr2.setTag("tr2");
    		//tr2.setGravity(Gravity.CENTER_HORIZONTAL);
    		//tr2.setPadding(0, 1, 0, 0);
    	 		tb21=new TableLayout(GoalActivity.this);
    	 		//tb21.setPadding(1, 0,0, 0);
    	 		LinearLayout main = new LinearLayout(GoalActivity.this);
    	 		main.setBackgroundResource(R.drawable.r2); 
    	 		main.setPadding(w1, h5, w1, h5);
	    		XYPlot xyplot = new XYPlot(GoalActivity.this, "");
	    		xyplot.setLayoutParams(new LayoutParams(w280, h150));
	    		//showGraph();
	    		//setchart(xyplot,i);
	    		main.addView(xyplot);
	    		tb21.addView(main);
				tr2.addView(tb21);
    		tb.addView(tr2);
    
    		    
    		TableRow tr3=new TableRow(GoalActivity.this);
    		tr3.setTag("tr3");
    	     	TableLayout tb31=new TableLayout(GoalActivity.this);
    				TableRow tr311=new TableRow(GoalActivity.this);
    				TextView add = new TextView(GoalActivity.this);
    				add.setText("+New Value");
    				add.setBackgroundResource(R.drawable.valuebg_toggle);
    				add.setPadding(w7, h7, w7, h7);
    				add.setGravity(Gravity.CENTER);
    				add.setTextColor(Color.rgb(65, 219, 255));
    				add.setTextSize(11);
    				add.setId(1);
    				add.setTag(i+"newval");
    				add.setTypeface(tf);
    				add.setOnClickListener(this);
    				tr311.addView(add);
    				 
    			  
    				TextView editval=new TextView(GoalActivity.this);
    				editval.setTypeface(tf);
    				editval.setBackgroundResource(R.drawable.otherbg_toggle);
    				editval.setPadding(w7, h7, w7, h7);
    				editval.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edt_icon, 0, 0, 0);
    				editval.setText("   Edit Value");
    				editval.setTextColor(Color.rgb(101,102,102));
    				editval.setTextSize(11);
    				editval.setId(2);
    				editval.setTag(i+"editval");
    				editval.setOnClickListener(this);
    				tr311.addView(editval);  
    					
    				TextView editgoal=new TextView(GoalActivity.this);
    				editgoal.setTypeface(tf);
    				editgoal.setBackgroundResource(R.drawable.otherbg_toggle);
    				editgoal.setPadding(w7, h7, w7, h7);
    				editgoal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edt_icon, 0, 0, 0);
    				editgoal.setText("   Edit GoalActivity");
    				editgoal.setTextColor(Color.rgb(101,102,102));
    				editgoal.setTextSize(11);
    				editgoal.setId(3);
    				editgoal.setTag(i+"edtgoal");
    				editgoal.setOnClickListener(this);
    				tr311.addView(editgoal);  
    				
    				TextView deletegoal=new TextView(GoalActivity.this);
    				deletegoal.setTypeface(tf);
    				deletegoal.setBackgroundResource(R.drawable.otherbg_toggle);
    				deletegoal.setPadding(w7, h7, w7, h7);
    				deletegoal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edt_delete, 0, 0, 0);
    				if(width==720){
    					deletegoal.setText("   Delete GoalActivity       ");
    				}else{
    					deletegoal.setText("   Delete GoalActivity");
    				}
    				deletegoal.setTextColor(Color.rgb(101,102,102));
    				deletegoal.setTextSize(11);
    				deletegoal.setId(4);
    				deletegoal.setTag(i+"delgoal");
    				deletegoal.setOnClickListener(this);
    				tr311.addView(deletegoal);  
    				
    				tb31.addView(tr311);
    			tr3.addView(tb31);
    		tb.addView(tr3);
    		
    		goal_list_layout.addView(tb);
    		
    	}
    }  
	*/
		
	// async class for calling webservice and get responce message
	public class Callweightask extends AsyncTask <String, Void,String>
	{
		protected Context applicationContext;

		@Override
		protected void onPreExecute()     
		{
			
			//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
			dialog = new ProgressDialog(getParent());
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("Please Wait....");
			dialog.show();
			Log.i("onPreExecute", "onPreExecute");
			
		}       
		
		protected void onPostExecute(String result)
		{
			
			Log.i("onPostExecute", "onPostExecute");
			 if(isInternetOn()){
				 CallCholesteroltask task = new CallCholesteroltask();
				 task.applicationContext =GoalActivity.this.getParent();
				 task.execute();
				// appPrefs.setReloadgraph("0");
			}else{
				Toast.makeText(GoalActivity.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}     
		}
   
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.i("doInBackground--Object", "doInBackground--Object");
			//ga.lstResult=obj.getGoal();
			//dialog.dismiss();
			weightdt=obj.getWeightGoal();
			return null;
		}
		   
	}     
	// async class for calling webservice and get responce message
		public class CallCholesteroltask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()     
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				Log.i("onPreExecute", "onPreExecute");
				
			}       
			
			protected void onPostExecute(String result)
			{
				
				Log.i("onPostExecute", "onPostExecute");
				 if(isInternetOn()){
					 CallGlucosetask task = new CallGlucosetask();
					 task.applicationContext =GoalActivity.this.getParent();
					 task.execute();
					// appPrefs.setReloadgraph("0");
				}else{
					Toast.makeText(GoalActivity.this,"Network is not available....",Toast.LENGTH_SHORT).show();
				}     
			}     
	   
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
				//ga.lstResult=obj.getGoal();
				cholesteroldt=obj.getCholesterolGoal();
				return null;
			}
			   
		}     
		// async class for calling webservice and get responce message
				public class CallGlucosetask extends AsyncTask <String, Void,String>
				{
					protected Context applicationContext;

					@Override
					protected void onPreExecute()     
					{
						
						//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
						Log.i("onPreExecute", "onPreExecute");
						
					}       
					
					protected void onPostExecute(String result)
					{
						
						Log.i("onPostExecute", "onPostExecute");
						 if(isInternetOn()){
							 CallGoalTask task = new CallGoalTask();
							 task.applicationContext =GoalActivity.this.getParent();
							 task.execute();
							// appPrefs.setReloadgraph("0");
						}else{
							Toast.makeText(GoalActivity.this,"Network is not available....",Toast.LENGTH_SHORT).show();
						}     
					}
			   
					@Override
					protected String doInBackground(String... params) {
						// TODO Auto-generated method stub
						Log.i("doInBackground--Object", "doInBackground--Object");
						//ga.lstResult=obj.getGoal();
						glucosedt=obj.getGlucoseGoal();
						return null;
					}
					   
				}     
	// async class for calling webservice and get responce message
		public class CallGoalTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()     
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				
				Log.i("onPreExecute", "onPreExecute");
				
			}       
			
			protected void onPostExecute(String result)
			{
				
				Log.i("onPostExecute", "onPostExecute");
					
					  mPager.setAdapter(new ImagePagerAdapter(lstResultManage));
					  //  mPager.setOnPageChangeListener(new MyPageChangeListener());
					 /* mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
							
							@Override
							public void onPageSelected(int position) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onPageScrolled(int position, float arg1, int arg2) {
								// TODO Auto-generated method stub
								
							    
							}
							
							@Override
							public void onPageScrollStateChanged(int position) {
								// TODO Auto-generated method stub
								Log.e("TAG","Move to left");
								 if(lastposition>position)
							      {Log.e("TAG","Move to left");}
							    else if(lastposition<position) 
							     {Log.e("TAG","Move to right");}
							    	lastposition=position;
							}
						});*/
					  dialog.dismiss();
						
					if(lstResultManage.size()>0){
						lbl_count.setText("("+lstResultManage.size()+")");
						Log.e("TAG","lst size : " + lstResultManage.size());
							 
						
						//showlist();  	
					}else{     
						Toast.makeText(getParent(), "No goals found...",Toast.LENGTH_SHORT).show();
					//	 mPager.setAdapter(new ImagePagerAdapter(lstResultManage));
						//showlist();  	
					}
				
					 setIndicator();
			}  
	   
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
				//ga.lstResult=obj.getGoal();
				//dialog.dismiss();
				lstResultManage.add("Weight");
				lstResultManage.add("Cholestrol");
				lstResultManage.add("Glucose");
				lstResultManage.add("Bloog Presure");
				bpdt=obj.getBPGoal();
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
						dialog = new ProgressDialog(GoalActivity.this.getParent());
						dialog.setCanceledOnTouchOutside(false);
						dialog.setMessage("Please Wait....");
						dialog.show();
						Log.i("onPreExecute", "onPreExecute");
						
					}        
					 
					protected void onPostExecute(String result)
					{
						
						Log.i("onPostExecute", "onPostExecute");
						//generateView();
						dialog.dismiss();
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
				
				// async class for calling webservice and get responce message
				public class CallDeleteTask extends AsyncTask <String, Void,String>
				{
					protected Context applicationContext;

					@Override
					protected void onPreExecute()     
					{
						
						//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
						dialog = new ProgressDialog(GoalActivity.this.getParent());
						dialog.setCanceledOnTouchOutside(false);
						dialog.setMessage("Please Wait....");
						dialog.show();
						Log.i("onPreExecute", "onPreExecute");
						
					}       
					
					protected void onPostExecute(String result)
					{
						
						Log.i("onPostExecute", "onPostExecute");
							dialog.dismiss();
							if(isInternetOn()){
								lstResultManage.clear();
									 Callweightask task = new Callweightask();
									 task.applicationContext =GoalActivity.this.getParent();
									 task.execute();
								
							}else{
								Toast.makeText(GoalActivity.this,"Network is not available....",Toast.LENGTH_SHORT).show();
							}
							
					}  
			   
					@Override
					protected String doInBackground(String... params) {
						// TODO Auto-generated method stub
						Log.i("doInBackground--Object", "doInBackground--Object");
						//ga.lstResult=obj.getGoal();
						//dialog.dismiss();
						obj.ReadingDelete(urlDelete);
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
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			Log.e("TAG","On resume call : " + ga.isCalcelflg());
		
			/*if(appPrefs.getGoalDisable().toString().equals("0")){
				appPrefs.setGoalDisable("1");
				
				Intent Profile = new Intent(getParent(),Profile.class);
				TabGroupActivity parentoption = (TabGroupActivity)getParent();
				parentoption.startChildActivity("Profile",Profile);
			}else{*/
			if(ga.isCalcelflg() ==true){
				display();
			}
			//}
		}
	   
	 
	 
	   @Override
	   public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	   public class MyPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
			  
		     @Override
		     public void onPageSelected(int position)
		     {
		    	 //Log.e("TAG","position is : " + position);
		    	// Toast.makeText(GoalActivity.this,"selected postion is " + position, Toast.LENGTH_SHORT).show();
		         /*focusedPage = position;
		         
		         ga.setUserPhotoID(ga.getUserPhotoIDGlobal(focusedPage));
		         Toast.makeText(ImagePagerActivity.this, ga.getUserPhotoID()+"", Toast.LENGTH_LONG).show();
		   name.setText(ga.getUserNameArr(focusedPage));  
		   votono.setText(ga.getVoteArr(focusedPage));
		   no_of_tip.setText(ga.getNoOfTipArr(focusedPage));  
		         total_tip.setText(ga.getTipAmtArr(focusedPage));*/
		            
		     }
		 }
	   private class ImagePagerAdapter extends PagerAdapter {

			private ArrayList<String> lstData=new ArrayList<String>();
			private LayoutInflater inflater;

			ImagePagerAdapter(ArrayList<String> lstResult) {
				this.lstData = lstResult;
				inflater = getLayoutInflater();
			}  

			@Override
			public void destroyItem(View container, int position, Object object)
			{
			//	Toast.makeText(ImagePagerActivity.this,"hello" +position+"", Toast.LENGTH_LONG).show();
				((ViewPager) container).removeView((View) object);
				
			}   

			@Override  
			public void finishUpdate(View container) {
			}

			@Override
			public int getCount() {
				return lstData.size();
			}     
			@Override
			public Object instantiateItem(View view, final int position)
			{
				lastposition=position;
				final View imageLayout = inflater.inflate(R.layout.item_pager_image, null);
				final TextView txtTitle=(TextView)imageLayout.findViewById(R.id.txtTitle);
				final TextView bmi=(TextView)imageLayout.findViewById(R.id.bmi);
				final TextView weight=(TextView)imageLayout.findViewById(R.id.weight);
				final LinearLayout chart = (LinearLayout)imageLayout.findViewById(R.id.chart);
				final LinearLayout bewlow = (LinearLayout)imageLayout.findViewById(R.id.below);
				final LinearLayout bewlow1= (LinearLayout)imageLayout.findViewById(R.id.below1);
				final ImageView delete_reading = (ImageView)imageLayout.findViewById(R.id.delete_reading);
				
				GraphicalView mChart = null;
				bewlow.getLayoutParams().height = h30;
				bewlow1.getLayoutParams().height = h30;
				   
				bmi.getLayoutParams().width = w150;
				weight.getLayoutParams().width = w150;
				
				
				// for date array
				 Date[] dtWeight,dtCholesterol,dtglucose,dtbp;
				if(position==0 && weightdt!=null){   
					ArrayList<String> lst = new ArrayList<String>();
					lst.addAll(weightdt.getReadings());
					
					// for chart
					// Creating a dataset to hold each series  
		        	
					XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
					
					// Creating a XYMultipleSeriesRenderer to customize the whole chart
		        	XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		        	    
					if(lst.size()>0){
						Log.e("TAG","targer date : " + weightdt.getTarget_date());
						
						
						String[] todate = weightdt.getTarget_date().split("-");
						
						Calendar date1 = Calendar.getInstance();
				        Calendar date2 = Calendar.getInstance();
				       
				       
			            date2.clear();
			            date2.set(Integer.parseInt(todate[0]), Integer.parseInt(todate[1]), Integer.parseInt(todate[2]));
				        
			            // for make date series
			           dtWeight = new Date[lst.size()];
			            // visit seriers
			        	int[] readingweigt = new int[lst.size()];// = { 2000,2500,2700,2100,2800};
			            for(int i=0;i<lst.size();i++){
			            	Log.e("TAG","weight readings : " + lst.get(i).toString());
						    String[] weightredings = lst.get(i).toString().split(",");
			            	String[] fromdate = weightredings[3].split("-");
			            	GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(fromdate[0]), (Integer.parseInt(fromdate[1]))-1, Integer.parseInt(fromdate[2]));
			            	dtWeight[i] = gc.getTime();
			        		readingweigt[i]=Integer.parseInt(weightredings[2]);
			        		if(lst.get(i).contains(CurrDate)){
			        			delete_reading.setVisibility(View.VISIBLE);
			        		}
			        	}
			         // Creating TimeSeries for Visits
			        	TimeSeries weightseries = new TimeSeries("Weight");    	
			        	
			        	// Creating TimeSeries for Views
			        	TimeSeries weightgoal = new TimeSeries("");   
			        	// Adding data to Visits and Views Series
			        	for(int i=0;i<dtWeight.length;i++){
			        		weightseries.add(dtWeight[i], readingweigt[i]);
			        	}
			        	
			        	GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(todate[0]), Integer.parseInt(todate[1]), Integer.parseInt(todate[2]));
			        	Log.e("TAG","Date is : " + weightdt.getWeight());
			        	//viewsSeries.add(dt[0], 2500);
			        	weightgoal.add(gc.getTime(),Integer.parseInt((weightdt.getWeight())));
			        	
			        	
			        	
			        	// Adding Visits Series to the dataset
			        	dataset.addSeries(weightseries);
			        	
			        	// Adding Visits Series to dataset
			        	dataset.addSeries(weightgoal);    	
			        	
			        	
			        	// Creating XYSeriesRenderer to customize visitsSeries  	
			        	XYSeriesRenderer visitsRenderer = new XYSeriesRenderer();
			        	visitsRenderer.setColor(Color.BLUE);
			        	visitsRenderer.setPointStyle(PointStyle.CIRCLE);
			        	visitsRenderer.setFillPoints(true);
			        	visitsRenderer.setLineWidth(2);
			        //	visitsRenderer.setDisplayChartValues(true);
			        	
			        	    
			        	// Creating XYSeriesRenderer to customize viewsSeries
			        	XYSeriesRenderer viewsRenderer = new XYSeriesRenderer();
			        	viewsRenderer.setColor(Color.BLUE);
			        	viewsRenderer.setPointStyle(PointStyle.CIRCLE);
			        	viewsRenderer.setFillPoints(true);
			        	viewsRenderer.setFillBelowLineColor(Color.BLUE);
			        	viewsRenderer.setFillBelowLine(true);
			        	viewsRenderer.setLineWidth(2);
			        	
			        //	viewsRenderer.setDisplayChartValues(true);
			        	
			        	
			        	
			        	
			        	//multiRenderer.setChartTitle("");
			        	//multiRenderer.setXTitle("");
			        	//multiRenderer.setYTitle("");
			        	multiRenderer.setZoomButtonsVisible(false);    	
			        	
			        	// Adding visitsRenderer and viewsRenderer to multipleRenderer
			        	// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
			        	// should be same
			        	multiRenderer.addSeriesRenderer(visitsRenderer);
			        	multiRenderer.addSeriesRenderer(viewsRenderer);
			        	
			        	mChart = (GraphicalView) ChartFactory.getTimeChartView(getBaseContext(), dataset, multiRenderer,"dd-MMM-yyyy");   		
			       		
			       		//multiRenderer.setClickEnabled(true);
			         	multiRenderer.setSelectableBuffer(10);
			         	multiRenderer.setPanEnabled(false, false);
			         //	multiRenderer.setClickEnabled(false); 
			         	multiRenderer.setShowLegend(true);
			         	//multiRenderer.setMargins(new int[]{10,0,10,0});
			         /*	multiRenderer.setApplyBackgroundColor(true);
			         	multiRenderer.setMarginsColor(Color.WHITE);
			         	multiRenderer.setBackgroundColor(Color.WHITE);*/
			         	multiRenderer.setApplyBackgroundColor(true);
			         	multiRenderer.setBackgroundColor(Color.TRANSPARENT);
			         	multiRenderer.setMarginsColor(Color.TRANSPARENT);
			         	
			         	    
			         	multiRenderer.setXLabelsColor(Color.BLACK);
			         	multiRenderer.setYLabelsColor(0,Color.BLACK);	
			         	  
			         	         
			         	chart.addView(mChart,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			         	
			         	//mChart.setOnClickListener(GoalActivity.this);
			         	//chart.setOnClickListener(GoalActivity.this);
			         	txtTitle.setText("Weight");
			         	
			         //	txtTitle.setOnClickListener(GoalActivity.this);
					}else{
						mChart = (GraphicalView) ChartFactory.getTimeChartView(getBaseContext(), dataset, multiRenderer,"dd-MMM-yyyy");   		
						chart.addView(mChart,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
						//mChart.setOnClickListener(GoalActivity.this);
						//chart.setOnClickListener(GoalActivity.this);
						txtTitle.setText("Weight");
					//	txtTitle.setOnClickListener(GoalActivity.this);
					}
					multiRenderer.setClickEnabled(true);
					chart.setTag("Weight");
					
					//chart.setOnClickListener(GoalActivity.this);
				}
				if(position==1 && cholesteroldt!=null){
    
					ArrayList<String> lst = new ArrayList<String>();
					lst.addAll(cholesteroldt.getReadings());
					
					// for chart
					// Creating a dataset to hold each series
		        	     
					XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
					
					// Creating a XYMultipleSeriesRenderer to customize the whole chart
		        	XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		        	
					if(lst.size()>0){
						Log.e("TAG","targer date : " + cholesteroldt.getTarget_date());
						
						String[] todate = cholesteroldt.getTarget_date().split("-");
						
						Calendar date1 = Calendar.getInstance();
				        Calendar date2 = Calendar.getInstance();
				        
			            date2.clear();
			            date2.set(Integer.parseInt(todate[0]), Integer.parseInt(todate[1]), Integer.parseInt(todate[2]));
				        
			            // for make date series
			            dtCholesterol = new Date[lst.size()];
			            // visit seriers
			        	int[] hdlch = new int[lst.size()];
			        	int[] ldlch = new int[lst.size()];
			        	int[] triglych = new int[lst.size()];
			        	int[] totalch = new int[lst.size()];
			            for(int i=0;i<lst.size();i++){
			            	Log.e("TAG","cholesterol readings : " + lst.get(i).toString());
						    String[] cholesterolredings = lst.get(i).toString().split(",");
			            	String[] fromdate = cholesterolredings[6].split("-");
			            	GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(fromdate[0]),  (Integer.parseInt(fromdate[1]))-1, Integer.parseInt(fromdate[2]));
			            	dtCholesterol[i] = gc.getTime();
			        		hdlch[i]=Integer.parseInt(cholesterolredings[2]);
			        		ldlch[i]=Integer.parseInt(cholesterolredings[3]);
			        		triglych[i]=Integer.parseInt(cholesterolredings[4]);
			        		totalch[i]=Integer.parseInt(cholesterolredings[5]);
			        		if(lst.get(i).contains(CurrDate)){
			        			delete_reading.setVisibility(View.VISIBLE);
			        		}
			        	}
			         // Creating TimeSeries for Visits
			        	TimeSeries hdlseries = new TimeSeries("HDL");    
			        	TimeSeries ldlseries = new TimeSeries("LDL");    	
			        	TimeSeries triglyseries = new TimeSeries("Triglycerides");    	
			        	TimeSeries totalseries = new TimeSeries("Total");    	
			        	
			        	// Creating TimeSeries for Views
			        	TimeSeries hdlgoal = new TimeSeries("");
			        	TimeSeries ldlgoal = new TimeSeries("");
			        	TimeSeries triglygoal = new TimeSeries("");
			        	TimeSeries totalgoal = new TimeSeries("");
			        	
			        	// Adding data to Visits and Views Series
			        	for(int i=0;i<dtCholesterol.length;i++){
			        		hdlseries.add(dtCholesterol[i], hdlch[i]);
			        		ldlseries.add(dtCholesterol[i], ldlch[i]);
			        		triglyseries.add(dtCholesterol[i], triglych[i]);
			        		totalseries.add(dtCholesterol[i], totalch[i]);
			        	}
			        	
			        	GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(todate[0]), Integer.parseInt(todate[1]), Integer.parseInt(todate[2]));
			        	//viewsSeries.add(dt[0], 2500);
			        	hdlgoal.add(gc.getTime(),Integer.parseInt(cholesteroldt.getHdl()));
			        	ldlgoal.add(gc.getTime(),Integer.parseInt(cholesteroldt.getLdl()));
			        	triglygoal.add(gc.getTime(),Integer.parseInt(cholesteroldt.getTriglycerides()));
			        	totalgoal.add(gc.getTime(),Integer.parseInt(cholesteroldt.getTotal_cholesterol()));
			        	
			        	
			        	
			        	// Adding Visits Series to the dataset
			        	dataset.addSeries(hdlseries);
			        	dataset.addSeries(ldlseries);
			        	dataset.addSeries(triglyseries);
			        	dataset.addSeries(totalseries);
			        	
			        	// Adding Visits Series to dataset
			        	dataset.addSeries(hdlgoal);
			        	dataset.addSeries(ldlgoal);
			        	dataset.addSeries(triglygoal);
			        	dataset.addSeries(totalgoal);
			        	
			        	
			        	// Creating XYSeriesRenderer to customize visitsSeries  	
			        	XYSeriesRenderer hdlRenderer = new XYSeriesRenderer();
			        	hdlRenderer.setColor(Color.BLACK);
			        	hdlRenderer.setPointStyle(PointStyle.CIRCLE);
			        	hdlRenderer.setFillPoints(true);
			        	hdlRenderer.setLineWidth(2);
			        	hdlRenderer.setDisplayChartValues(true);
			        	
			        	XYSeriesRenderer ldlRenderer = new XYSeriesRenderer();
			        	ldlRenderer.setColor(Color.RED);
			        	ldlRenderer.setPointStyle(PointStyle.CIRCLE);
			        	ldlRenderer.setFillPoints(true);
			        	ldlRenderer.setLineWidth(2);
			        	ldlRenderer.setDisplayChartValues(true);
			        	
			        	XYSeriesRenderer triglyRenderer = new XYSeriesRenderer();
			        	triglyRenderer.setColor(Color.GREEN);
			        	triglyRenderer.setPointStyle(PointStyle.CIRCLE);
			        	triglyRenderer.setFillPoints(true);
			        	triglyRenderer.setLineWidth(2);
			        	triglyRenderer.setDisplayChartValues(true);
			        	
			        	XYSeriesRenderer totalRenderer = new XYSeriesRenderer();
			        	totalRenderer.setColor(Color.MAGENTA);
			        	totalRenderer.setPointStyle(PointStyle.CIRCLE);
			        	totalRenderer.setFillPoints(true);
			        	totalRenderer.setLineWidth(2);
			        	totalRenderer.setDisplayChartValues(true);
			        	
			        	
			        	
			        	
			        	// Creating XYSeriesRenderer to customize viewsSeries
			        	XYSeriesRenderer hdlgoalRenderer = new XYSeriesRenderer();
			        	hdlgoalRenderer.setColor(Color.BLACK);
			        	hdlgoalRenderer.setPointStyle(PointStyle.CIRCLE);
			        	hdlgoalRenderer.setFillPoints(true);
			        	hdlgoalRenderer.setFillBelowLineColor(Color.BLUE);
			        	hdlgoalRenderer.setFillBelowLine(true);
			        	hdlgoalRenderer.setLineWidth(2);
			        	hdlgoalRenderer.setDisplayChartValues(true);
			        	
			        	XYSeriesRenderer ldlgoalRenderer = new XYSeriesRenderer();
			        	ldlgoalRenderer.setColor(Color.RED);
			        	ldlgoalRenderer.setPointStyle(PointStyle.CIRCLE);
			        	ldlgoalRenderer.setFillPoints(true);
			        	ldlgoalRenderer.setFillBelowLineColor(Color.BLUE);
			        	ldlgoalRenderer.setFillBelowLine(true);
			        	ldlgoalRenderer.setLineWidth(2);
			        	ldlgoalRenderer.setDisplayChartValues(true);
			        	
			        	XYSeriesRenderer triglygoalRenderer = new XYSeriesRenderer();
			        	triglygoalRenderer.setColor(Color.GREEN);
			        	triglygoalRenderer.setPointStyle(PointStyle.CIRCLE);
			        	triglygoalRenderer.setFillPoints(true);
			        	triglygoalRenderer.setFillBelowLineColor(Color.BLUE);
			        	triglygoalRenderer.setFillBelowLine(true);
			        	triglygoalRenderer.setLineWidth(2);
			        	triglygoalRenderer.setDisplayChartValues(true);
			        	
			        	XYSeriesRenderer totalgoalRenderer = new XYSeriesRenderer();
			        	totalgoalRenderer.setColor(Color.MAGENTA);
			        	totalgoalRenderer.setPointStyle(PointStyle.CIRCLE);
			        	totalgoalRenderer.setFillPoints(true);
			        	totalgoalRenderer.setFillBelowLineColor(Color.BLUE);
			        	totalgoalRenderer.setFillBelowLine(true);
			        	totalgoalRenderer.setLineWidth(2);
			        	totalgoalRenderer.setDisplayChartValues(true);
			        	
			        	//multiRenderer.setChartTitle("");
			        	//multiRenderer.setXTitle("");
			        	//multiRenderer.setYTitle("");
			        	multiRenderer.setZoomButtonsVisible(false);    	
			        	
			        	// Adding visitsRenderer and viewsRenderer to multipleRenderer
			        	// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
			        	// should be same
			        	multiRenderer.addSeriesRenderer(hdlRenderer);
			        	multiRenderer.addSeriesRenderer(ldlRenderer);
			        	multiRenderer.addSeriesRenderer(triglyRenderer);
			        	multiRenderer.addSeriesRenderer(totalRenderer);
			        	
			        	multiRenderer.addSeriesRenderer(hdlgoalRenderer);
			        	multiRenderer.addSeriesRenderer(ldlgoalRenderer);
			        	multiRenderer.addSeriesRenderer(triglygoalRenderer);
			        	multiRenderer.addSeriesRenderer(totalgoalRenderer);
			        
			        	mChart = (GraphicalView) ChartFactory.getTimeChartView(getBaseContext(), dataset, multiRenderer,"dd-MMM-yyyy");   		
			       		
			       		//multiRenderer.setClickEnabled(true);
			         	multiRenderer.setSelectableBuffer(10);
			         	multiRenderer.setPanEnabled(false, false);
			         //	multiRenderer.setClickEnabled(false); 
			         	multiRenderer.setShowLegend(true);
			         	multiRenderer.setLabelsColor(Color.BLACK);
			         	//multiRenderer.setMargins(new int[]{10,0,10,0});
			         	multiRenderer.setApplyBackgroundColor(true);
			         	multiRenderer.setBackgroundColor(Color.TRANSPARENT);
			         	multiRenderer.setMarginsColor(Color.TRANSPARENT);
			         	
			         	multiRenderer.setXLabelsColor(Color.BLACK);
			         	multiRenderer.setYLabelsColor(0,Color.BLACK);	
			         	chart.addView(mChart,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			         
			         	//mChart.setOnClickListener(GoalActivity.this);
			         	//chart.setOnClickListener(GoalActivity.this);
			         	txtTitle.setText("Cholestrol");
			         
			         //	txtTitle.setOnClickListener(GoalActivity.this);
					}else{
						mChart = (GraphicalView) ChartFactory.getTimeChartView(getBaseContext(), dataset, multiRenderer,"dd-MMM-yyyy");   		
						chart.addView(mChart,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
						//mChart.setOnClickListener(GoalActivity.this);
						//chart.setOnClickListener(GoalActivity.this);
						txtTitle.setText("Cholestrol");
						//txtTitle.setOnClickListener(GoalActivity.this);
					}
					multiRenderer.setClickEnabled(true);
					chart.setTag("Cholestrol");
					//chart.setOnClickListener(GoalActivity.this);
				
				}
				if(position==2 && glucosedt!=null){
					ArrayList<String> lst = new ArrayList<String>();
					lst.addAll(glucosedt.getReadings());
					
					// for chart
					// Creating a dataset to hold each series
		        	
					XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
					
					// Creating a XYMultipleSeriesRenderer to customize the whole chart
		        	XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		        	
					if(lst.size()>0){
						Log.e("TAG","targer date : " + glucosedt.getTarget_date());
						
						
						String[] todate = glucosedt.getTarget_date().split("-");
						
						Calendar date1 = Calendar.getInstance();
				        Calendar date2 = Calendar.getInstance();
				       
				       
			            date2.clear();
			            date2.set(Integer.parseInt(todate[0]), Integer.parseInt(todate[1]), Integer.parseInt(todate[2]));
				        
			            // for make date series
			            dtglucose = new Date[lst.size()];
			            // visit seriers
			        	int[] fastingreading = new int[lst.size()];
			        	int[] randomreading = new int[lst.size()];
			            for(int i=0;i<lst.size();i++){
			            	Log.e("TAG","glucose readings : " + lst.get(i).toString());
						    String[] glucoseredings = lst.get(i).toString().split(",");
			            	String[] fromdate = glucoseredings[4].split("-");
			            	GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(fromdate[0]),  (Integer.parseInt(fromdate[1]))-1, Integer.parseInt(fromdate[2]));
			            	dtglucose[i] = gc.getTime();
			        		fastingreading[i]=Integer.parseInt(glucoseredings[2]);
			        		randomreading[i]=Integer.parseInt(glucoseredings[3]);
			        		if(lst.get(i).contains(CurrDate)){
			        			delete_reading.setVisibility(View.VISIBLE);
			        		}
			        	}
			         // Creating TimeSeries for Visits
			        	TimeSeries fastingseries = new TimeSeries("Fasting"); 
			        	TimeSeries randomseries = new TimeSeries("Random"); 
			        	
			        	// Creating TimeSeries for Views
			        	TimeSeries fastinggoal = new TimeSeries(""); 
			        	TimeSeries randomgoal = new TimeSeries(""); 
			        	// Adding data to Visits and Views Series
			        	for(int i=0;i<dtglucose.length;i++){
			        		fastingseries.add(dtglucose[i], fastingreading[i]);
			        		randomseries.add(dtglucose[i], randomreading[i]);
			        	}
			        	
			        	GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(todate[0]), Integer.parseInt(todate[1]), Integer.parseInt(todate[2]));
			        	Log.e("TAG","Date is : " + weightdt.getWeight());
			        	//viewsSeries.add(dt[0], 2500);
			        	fastinggoal.add(gc.getTime(),Integer.parseInt((glucosedt.getFasting())));
			        	randomgoal.add(gc.getTime(),Integer.parseInt((glucosedt.getRandom())));
			        	
			        	// Adding Visits Series to the dataset
			        	dataset.addSeries(fastingseries);
			        	dataset.addSeries(randomseries);
			        	
			        	// Adding Visits Series to dataset
			        	dataset.addSeries(fastinggoal);  
			        	dataset.addSeries(randomgoal);  
			        	
			        	
			        	// Creating XYSeriesRenderer to customize visitsSeries  	
			        	XYSeriesRenderer fastingRenderer = new XYSeriesRenderer();
			        	fastingRenderer.setColor(Color.BLACK);
			        	fastingRenderer.setPointStyle(PointStyle.CIRCLE);
			        	fastingRenderer.setFillPoints(true);
			        	fastingRenderer.setLineWidth(2);
			        //	visitsRenderer.setDisplayChartValues(true);
			        	
			        	XYSeriesRenderer randomRenderer = new XYSeriesRenderer();
			        	randomRenderer.setColor(Color.GREEN);
			        	randomRenderer.setPointStyle(PointStyle.CIRCLE);
			        	randomRenderer.setFillPoints(true);
			        	randomRenderer.setLineWidth(2);
			        	
			        	
			        	// Creating XYSeriesRenderer to customize viewsSeries
			        	XYSeriesRenderer fastinggoalRenderer = new XYSeriesRenderer();
			        	fastinggoalRenderer.setColor(Color.BLACK);
			        	fastinggoalRenderer.setPointStyle(PointStyle.CIRCLE);
			        	fastinggoalRenderer.setFillPoints(true);
			        	fastinggoalRenderer.setFillBelowLineColor(Color.BLUE);
			        	fastinggoalRenderer.setFillBelowLine(true);
			        	fastinggoalRenderer.setLineWidth(2);
			        	
			        	XYSeriesRenderer randomgoalRenderer = new XYSeriesRenderer();
			        	randomgoalRenderer.setColor(Color.GREEN);
			        	randomgoalRenderer.setPointStyle(PointStyle.CIRCLE);
			        	randomgoalRenderer.setFillPoints(true);
			        	randomgoalRenderer.setFillBelowLineColor(Color.BLUE);
			        	randomgoalRenderer.setFillBelowLine(true);
			        	randomgoalRenderer.setLineWidth(2);
			        	
			        	//viewsRenderer.setDisplayChartValues(true);
			        	
			         	multiRenderer.setZoomButtonsVisible(false);    	
			        	
			        	// Adding visitsRenderer and viewsRenderer to multipleRenderer
			        	// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
			        	// should be same
			        	multiRenderer.addSeriesRenderer(fastingRenderer);
			        	multiRenderer.addSeriesRenderer(randomRenderer);
			        	
			        	multiRenderer.addSeriesRenderer(fastinggoalRenderer);
			        	multiRenderer.addSeriesRenderer(randomgoalRenderer);
			        	
			        	mChart = (GraphicalView) ChartFactory.getTimeChartView(getBaseContext(), dataset, multiRenderer,"dd-MMM-yyyy");   		
			       		
			       	//	multiRenderer.setClickEnabled(true);
			         	multiRenderer.setSelectableBuffer(10);
			         	multiRenderer.setPanEnabled(false, false);
			        // 	multiRenderer.setClickEnabled(false); 
			         	multiRenderer.setShowLegend(true);
			         	//multiRenderer.setMargins(new int[]{10,0,10,0});
			         	multiRenderer.setApplyBackgroundColor(true);
			         	multiRenderer.setBackgroundColor(Color.TRANSPARENT);
			         	multiRenderer.setMarginsColor(Color.TRANSPARENT);
			         	multiRenderer.setXLabelsColor(Color.BLACK);
			         	multiRenderer.setYLabelsColor(0,Color.BLACK);	
			         	
			         	chart.addView(mChart,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			         	//mChart.setOnClickListener(GoalActivity.this);
			         	//chart.setOnClickListener(GoalActivity.this);
			         	txtTitle.setText("Glucose");
			         	
			         //	txtTitle.setOnClickListener(GoalActivity.this);
					}else{
						mChart = (GraphicalView) ChartFactory.getTimeChartView(getBaseContext(), dataset, multiRenderer,"dd-MMM-yyyy");   		
						chart.addView(mChart,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
						//chart.setOnClickListener(GoalActivity.this);
						txtTitle.setText("Glucose");
						//txtTitle.setOnClickListener(GoalActivity.this);
					}
					multiRenderer.setClickEnabled(true);
					chart.setTag("Glucose");
					//chart.setOnClickListener(GoalActivity.this);
				}
				if(position==3 && bpdt!=null){
					ArrayList<String> lst = new ArrayList<String>();
					lst.addAll(bpdt.getReadings());
					
					// for chart
					// Creating a dataset to hold each series
		        	
					XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
					
					// Creating a XYMultipleSeriesRenderer to customize the whole chart
		        	XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		        	
					if(lst.size()>0){
						Log.e("TAG","targer date : " + bpdt.getTarget_date());
						
						
						String[] todate = bpdt.getTarget_date().split("-");
						
						Calendar date1 = Calendar.getInstance();
				        Calendar date2 = Calendar.getInstance();
				       
				       
			            date2.clear();
			            date2.set(Integer.parseInt(todate[0]), Integer.parseInt(todate[1]), Integer.parseInt(todate[2]));
				        
			            // for make date series
			            dtbp = new Date[lst.size()];
			            // visit seriers
			        	int[] systolicreading = new int[lst.size()];
			        	int[] diastolicreading = new int[lst.size()];
			            for(int i=0;i<lst.size();i++){
			            	Log.e("TAG","bp readings : " + lst.get(i).toString());
						    String[] glucoseredings = lst.get(i).toString().split(",");
			            	String[] fromdate = glucoseredings[5].split("-");
			            	GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(fromdate[0]),  (Integer.parseInt(fromdate[1]))-1, Integer.parseInt(fromdate[2]));
			            	dtbp[i] = gc.getTime();
			        		systolicreading[i]=Integer.parseInt(glucoseredings[2]);
			        		diastolicreading[i]=Integer.parseInt(glucoseredings[3]);
			        		if(lst.get(i).contains(CurrDate)){
			        			delete_reading.setVisibility(View.VISIBLE);
			        		}
			        	}
			         // Creating TimeSeries for Visits
			        	TimeSeries systolicseries = new TimeSeries("Systolic"); 
			        	TimeSeries diastolicseries = new TimeSeries("Diastolic"); 
			        	
			        	// Creating TimeSeries for Views
			        	TimeSeries systolicgoal = new TimeSeries(""); 
			        	TimeSeries diastolicgoal = new TimeSeries(""); 
			        	// Adding data to Visits and Views Series
			        	for(int i=0;i<dtbp.length;i++){
			        		systolicseries.add(dtbp[i], systolicreading[i]);
			        		diastolicseries.add(dtbp[i], diastolicreading[i]);
			        	}
			        	
			        	GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(todate[0]), Integer.parseInt(todate[1]), Integer.parseInt(todate[2]));
			       
			        	systolicgoal.add(gc.getTime(),Integer.parseInt((bpdt.getSystolic())));
			        	diastolicgoal.add(gc.getTime(),Integer.parseInt((bpdt.getDiastolic())));
			        	
			        	// Adding Visits Series to the dataset
			        	dataset.addSeries(systolicseries);
			        	dataset.addSeries(diastolicseries);
			        	
			        	// Adding Visits Series to dataset
			        	dataset.addSeries(systolicgoal);  
			        	dataset.addSeries(diastolicgoal);  
			        	
			        	
			        	// Creating XYSeriesRenderer to customize visitsSeries  	
			        	XYSeriesRenderer systolicRenderer = new XYSeriesRenderer();
			        	systolicRenderer.setColor(Color.BLUE);
			        	systolicRenderer.setPointStyle(PointStyle.CIRCLE);
			        	systolicRenderer.setFillPoints(true);
			        	systolicRenderer.setLineWidth(2);
			        //	visitsRenderer.setDisplayChartValues(true);
			        	
			        	XYSeriesRenderer diastolicRenderer = new XYSeriesRenderer();
			        	diastolicRenderer.setColor(Color.CYAN);
			        	diastolicRenderer.setPointStyle(PointStyle.CIRCLE);
			        	diastolicRenderer.setFillPoints(true);
			        	diastolicRenderer.setLineWidth(2);
			        	
			        	
			        	// Creating XYSeriesRenderer to customize viewsSeries
			        	XYSeriesRenderer systolicgoalRenderer = new XYSeriesRenderer();
			        	systolicgoalRenderer.setColor(Color.BLUE);
			        	systolicgoalRenderer.setPointStyle(PointStyle.CIRCLE);
			        	systolicgoalRenderer.setFillPoints(true);
			        	systolicgoalRenderer.setFillBelowLineColor(Color.BLUE);
			        	systolicgoalRenderer.setFillBelowLine(true);
			        	systolicgoalRenderer.setLineWidth(2);
			        	
			        	XYSeriesRenderer diastolicgoalRenderer = new XYSeriesRenderer();
			        	diastolicgoalRenderer.setColor(Color.CYAN);
			        	diastolicgoalRenderer.setPointStyle(PointStyle.CIRCLE);
			        	diastolicgoalRenderer.setFillPoints(true);
			        	diastolicgoalRenderer.setFillBelowLineColor(Color.BLUE);
			        	diastolicgoalRenderer.setFillBelowLine(true);
			        	diastolicgoalRenderer.setLineWidth(2);
			        	
			        	//viewsRenderer.setDisplayChartValues(true);
			        	
			         	multiRenderer.setZoomButtonsVisible(false);    	
			        	
			        	// Adding visitsRenderer and viewsRenderer to multipleRenderer
			        	// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
			        	// should be same
			        	multiRenderer.addSeriesRenderer(systolicRenderer);
			        	multiRenderer.addSeriesRenderer(diastolicRenderer);
			        	
			        	multiRenderer.addSeriesRenderer(systolicgoalRenderer);
			        	multiRenderer.addSeriesRenderer(diastolicgoalRenderer);
			        	
			        	mChart = (GraphicalView) ChartFactory.getTimeChartView(getBaseContext(), dataset, multiRenderer,"dd-MMM-yyyy");   		
			       		
			       	//	multiRenderer.setClickEnabled(true);
			         	multiRenderer.setSelectableBuffer(10);
			         	multiRenderer.setPanEnabled(false, false);
			         //	multiRenderer.setClickEnabled(false); 
			         	//multiRenderer.setMargins(new int[]{10,0,10,0});
			         	multiRenderer.setApplyBackgroundColor(true);
			         	multiRenderer.setBackgroundColor(Color.TRANSPARENT);
			         	multiRenderer.setMarginsColor(Color.TRANSPARENT);
			         	multiRenderer.setXLabelsColor(Color.BLACK);
			         	multiRenderer.setYLabelsColor(0,Color.BLACK);
			         	multiRenderer.setShowLegend(true);
			         	chart.addView(mChart,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			         	//mChart.setOnClickListener(GoalActivity.this);
			         //	chart.setOnClickListener(GoalActivity.this);
			         	txtTitle.setText("Blood Pressure");
			         	
			         //	txtTitle.setOnClickListener(GoalActivity.this);
					}else{      
						mChart = (GraphicalView) ChartFactory.getTimeChartView(getBaseContext(), dataset, multiRenderer,"dd-MMM-yyyy");   		
						chart.addView(mChart,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
						
						chart.setOnClickListener(GoalActivity.this);
						txtTitle.setText("Blood Pressure");
						//txtTitle.setOnClickListener(GoalActivity.this);
					}
					multiRenderer.setClickEnabled(true);
					chart.setTag("Blood Pressure");
					//chart.setOnClickListener(GoalActivity.this);
				}
				  
	    		
	    		    
				     
			/*	 chart.setOnTouchListener(new View.OnTouchListener() {
						
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							// TODO Auto-generated method stub
							int action = event.getAction();
				            switch (action) {
				            case MotionEvent.ACTION_DOWN:
				                // Disallow ScrollView to intercept touch events.
				                v.getParent().requestDisallowInterceptTouchEvent(true);
				                break;

				            case MotionEvent.ACTION_UP:
				                // Allow ScrollView to intercept touch events.
				                v.getParent().requestDisallowInterceptTouchEvent(false);
				                break;
				            }

				            // Handle ListView touch events.
				            v.onTouchEvent(event);
				            return true;
						}
					});*/
				mChart.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//appPrefs.setGoalname(xyplot.getTag().toString());
						Log.e("TAG","Click is occuere");
						/*Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
		     			
		     			SeriesSelection seriesSelection = mChart.getCurrentSeriesAndPoint();

		     			if (seriesSelection != null) {     	   			
		     				int seriesIndex = seriesSelection.getSeriesIndex();
		            	  	String selectedSeries="Visits";
		            	  	if(seriesIndex==0)
		            	  		selectedSeries = "Visits";
		            	  	else
		            	  		selectedSeries = "Views";
		            	  	
		            	  	// Getting the clicked Date ( x value )
		          			long clickedDateSeconds = (long) seriesSelection.getXValue();
		          			Date clickedDate = new Date(clickedDateSeconds);
		          			String strDate = formatter.format(clickedDate);
		          			
		          			// Getting the y value 
		          			int amount = (int) seriesSelection.getValue();
		          			
		          			// Displaying Toast Message
		          			Toast.makeText(
		                	       getBaseContext(),
		                	       selectedSeries + " on "  + strDate + " : " + amount ,
		                	       Toast.LENGTH_SHORT).show();
		     			}*/
						String val = chart.getTag().toString();
						if(val.equals("Weight")){
							if(weightdt.getId() != null){
								if(weightdt.getReadings().size()>0){
									ga.setWeightupdate(false);
									for(int i = 0;i>weightdt.getReadings().size();i++){
										if(weightdt.getReadings().get(i).contains(CurrDate)){
											ga.setWeightupdate(true);
										}
									}
									ga.setWeightid(weightdt.getId());
									Intent AddNewVal = new Intent(GoalActivity.this,AddWeightValue.class);
									startActivity(AddNewVal);
									/*TabGroupActivity parentoption = (TabGroupActivity)getParent();
									parentoption.startChildActivity("AddNewVal",AddNewVal);*/
								}else{
									ga.setWeightid(weightdt.getId());
									/*Intent AddNewVal = new Intent(getParent(),AddWeightValue.class);
									TabGroupActivity parentoption = (TabGroupActivity)getParent();
									parentoption.startChildActivity("AddNewVal",AddNewVal);*/
									Intent AddNewVal = new Intent(GoalActivity.this,AddWeightValue.class);
									startActivity(AddNewVal);
								}
							}else{
								Toast.makeText(GoalActivity.this,"Add Weight goal first", Toast.LENGTH_SHORT).show();
							}
						}
						if(val.equals("Cholestrol")){
							if(cholesteroldt.getId() != null){
								ga.setCholesterolid(cholesteroldt.getId().toString());
								Intent AddCholesterolValue = new Intent(GoalActivity.this,AddCholesterolValue.class);
								startActivity(AddCholesterolValue);
							
							}else{
								Toast.makeText(GoalActivity.this,"Add cholesterol goal first", Toast.LENGTH_SHORT).show();
							}
						}
						if(val.equals("Glucose")){
							if(glucosedt.getId() != null){
								ga.setGlucoseid(glucosedt.getId().toString());
								Intent AddGlucoseValue = new Intent(GoalActivity.this,AddGlucoseValue.class);
								startActivity(AddGlucoseValue);
								
							}else{
								Toast.makeText(GoalActivity.this,"Add Glucose goal first", Toast.LENGTH_SHORT).show();
							}
						}
						if(val.equals("Blood Pressure")){
							if(bpdt.getId() != null){
								ga.setBpid(bpdt.getId().toString());
								Intent AddBPValue = new Intent(GoalActivity.this,AddBPValue.class);
								startActivity(AddBPValue);
								
							}else{
								Toast.makeText(GoalActivity.this,"Add Blood Pressure goal first", Toast.LENGTH_SHORT).show();
							}
						}
						
					}
				});
				delete_reading.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//appPrefs.setGoalname(xyplot.getTag().toString());
						Log.e("TAG","Click is occuere");
						String val = chart.getTag().toString();
						if(val.equals("Weight")){
							urlDelete = ga.url+"weight-goals/"+ weightdt.getId()+"/destroy-reading/?reading_date="+CurrDate;
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GoalActivity.this.getParent());
							 
							// set title
							alertDialogBuilder.setTitle("Confirm");
				 
							// set dialog message
							alertDialogBuilder
								.setMessage("Are you sure for delete reading?")
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog1, int arg1) {
										// TODO Auto-generated method stub
										 if(isInternetOn()){
											 dialog1.dismiss();
											 CallDeleteTask task = new CallDeleteTask();
											 task.applicationContext =GoalActivity.this.getParent();
											 task.execute();
											// appPrefs.setReloadgraph("0");
										}else{
											Toast.makeText(GoalActivity.this,"Network is not available....",Toast.LENGTH_SHORT).show();
										}     
									}
								})
								.setNegativeButton("No",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog1,int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										//cb.setChecked(false);
										dialog1.cancel();
									}
								});
							
				 
								// create alert dialog
								AlertDialog alertDialog = alertDialogBuilder.create();
				 
								// show it
								alertDialog.show();
				 
						}
						if(val.equals("Cholestrol")){
							urlDelete = ga.url+"cholesterol-goals/"+ cholesteroldt.getId()+"/destroy-reading/?reading_date="+CurrDate;
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GoalActivity.this.getParent());
							 
							// set title
							alertDialogBuilder.setTitle("Confirm");
				 
							// set dialog message
							alertDialogBuilder
								.setMessage("Are you sure for delete reading?")
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog1, int arg1) {
										// TODO Auto-generated method stub
										 if(isInternetOn()){
											 dialog1.dismiss();
											 CallDeleteTask task = new CallDeleteTask();
											 task.applicationContext =GoalActivity.this.getParent();
											 task.execute();
											// appPrefs.setReloadgraph("0");
										}else{
											Toast.makeText(GoalActivity.this,"Network is not available....",Toast.LENGTH_SHORT).show();
										}     
									}
								})
								.setNegativeButton("No",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog1,int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										//cb.setChecked(false);
										dialog1.cancel();
									}
								});
							
				 
								// create alert dialog
								AlertDialog alertDialog = alertDialogBuilder.create();
				 
								// show it
								alertDialog.show();
						}
						if(val.equals("Glucose")){
							urlDelete = ga.url+"glucose-goals/"+ glucosedt.getId()+"/destroy-reading/?reading_date="+CurrDate;
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GoalActivity.this.getParent());
							 
							// set title
							alertDialogBuilder.setTitle("Confirm");
				 
							// set dialog message
							alertDialogBuilder
								.setMessage("Are you sure for delete reading?")
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog1, int arg1) {
										// TODO Auto-generated method stub
										 if(isInternetOn()){
											 dialog1.dismiss();
											 CallDeleteTask task = new CallDeleteTask();
											 task.applicationContext =GoalActivity.this.getParent();
											 task.execute();
											// appPrefs.setReloadgraph("0");
										}else{
											Toast.makeText(GoalActivity.this,"Network is not available....",Toast.LENGTH_SHORT).show();
										}     
									}
								})
								.setNegativeButton("No",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog1,int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										//cb.setChecked(false);
										dialog1.cancel();
									}
								});
							
				 
								// create alert dialog
								AlertDialog alertDialog = alertDialogBuilder.create();
				 
								// show it
								alertDialog.show();
						}
						if(val.equals("Blood Pressure")){
							urlDelete = ga.url+"blood-pressure-goals/"+ bpdt.getId()+"/destroy-reading/?reading_date="+CurrDate;
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GoalActivity.this.getParent());
							 
							// set title
							alertDialogBuilder.setTitle("Confirm");
				 
							// set dialog message
							alertDialogBuilder   
								.setMessage("Are you sure for delete reading?")
								.setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog1, int arg1) {
										// TODO Auto-generated method stub
										 if(isInternetOn()){
											 dialog1.dismiss();
											 CallDeleteTask task = new CallDeleteTask();
											 task.applicationContext =GoalActivity.this.getParent();
											 task.execute();
											// appPrefs.setReloadgraph("0");
										}else{
											Toast.makeText(GoalActivity.this,"Network is not available....",Toast.LENGTH_SHORT).show();
										}     
									}
								})
								.setNegativeButton("No",new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog1,int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										//cb.setChecked(false);
										dialog1.cancel();
									}
								});
							
				 
								// create alert dialog
								AlertDialog alertDialog = alertDialogBuilder.create();
				 
								// show it
								alertDialog.show();
						}
						
					}
				});
				
				mChart.setOnDragListener(new OnDragListener() {
					
					@Override
					public boolean onDrag(View v, DragEvent event) {
						// TODO Auto-generated method stub
						int action = event.getAction();
			            switch (action) {
			            case DragEvent.ACTION_DRAG_ENTERED:
			                // Disallow ScrollView to intercept touch events.
			                v.getParent().requestDisallowInterceptTouchEvent(true);
			                break;

			            case DragEvent.ACTION_DRAG_EXITED:
			                // Allow ScrollView to intercept touch events.
			                v.getParent().requestDisallowInterceptTouchEvent(false);
			                break;
			            }

			            // Handle ListView touch events.
			            v.onDragEvent(event);
			            return true;
					}
				});
				((ViewPager) view).addView(imageLayout, 0);
				
				
				
				return imageLayout;
			}

			
			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view.equals(object);
			}

			@Override
			public void restoreState(Parcelable state, ClassLoader loader) {
			}

			@Override
			public Parcelable saveState() {
				return null;
			}
	  
			@Override
			public void startUpdate(View container) {
			}
		
		}     
	   public Date StringToDate(String strdate){
		   Date date = null;
		  SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");  
		   try {  
		       date = format.parse(strdate);  
		       System.out.println(date);  
		   } catch (ParseException e) {  
		       // TODO Auto-generated catch block  
		       e.printStackTrace();  
		   }
		   return date;
	   }
	
	 
}
