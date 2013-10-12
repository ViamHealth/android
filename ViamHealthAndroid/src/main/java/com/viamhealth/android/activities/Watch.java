package com.viamhealth.android.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.adapters.GoalDataAdapter;
import com.viamhealth.android.adapters.MedicalDataAdapter;
import com.viamhealth.android.R;
import com.viamhealth.android.adapters.MedicalDataAdapter1;
import com.viamhealth.android.adapters.SeparatedListAdapter;
import com.viamhealth.android.adapters.TestDataAdapter;
import com.viamhealth.android.ViamHealthPrefs;

import com.viamhealth.android.dao.rest.endpoints.UserEP;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.adapters.TestDataAdapter1;
import com.viamhealth.android.adapters.MedicalDataAdapter1;
import com.viamhealth.android.model.MedicalData;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.viamhealth.android.model.MedicationData;
import com.viamhealth.android.model.ReminderReadings;
import com.viamhealth.android.ui.RefreshableListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class Watch extends BaseActivity implements OnClickListener{
	Display display;
	int width,height;
	int w15,w20,w5,h40,h20,w10,h10,w150,w30;
	ProgressDialog dialog;
    ProgressDialog dialog1;
	
	TextView mSelected,lbl_add,lbl_delete,txt_test,txt_medication,txt_reminder,txt_name,txt_time,txt_mode;;
	ImageView back,person_icon;
	Button add_medicine,add_test,add_medicine_reminder,add_test_reminder;
	TextView lbl_invite_user_food,heding_Addfood_name;
	LinearLayout menu_invite_addfood,menu_invite_out_addfood,settiglayout_food,search_layout,watch_below_layout,lst_data;
	RefreshableListView lstReminderMedicine,lstReminderTest,lstdata;
	ScrollView reminder_scrl,medicine_scrl,test_scrl;
	TextView lbl_name,lbl_morning,txt1,lbl_noon,txt2,lbl_night;
	
	ViewPager mPager,mPager1;
	
	ArrayList<MedicalData> lstResult = new ArrayList<MedicalData>();
	int selection=0;  
	String delid;
	
	Typeface tf;
	ViamHealthPrefs appPrefs;
    String user_id,med_id;
	functionClass obj;
	Global_Application ga;
	String selecteduserid="0";
	private DisplayImageOptions options;
    ArrayList<MedicationData>	allData = new ArrayList<MedicationData>();
    ArrayList<MedicationData>	listData = new ArrayList<MedicationData>();
    ArrayList<MedicationData> otherData = new ArrayList<MedicationData>();
    MedicationData med_edit=new MedicationData();
    Intent edit_med=null;
    int edit_pos=0;
    ArrayList<ReminderReadings> rem_read=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    super.onCreate(savedInstanceState);
	    //super.getSupportActionBar().setTitle("Sharat Khurana");
	    
		setContentView(R.layout.watch);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        user_id=getIntent().getStringExtra("user_id");
        //Toast.makeText(getApplicationContext(),"user_id="+user_id,Toast.LENGTH_LONG).show();
		appPrefs = new ViamHealthPrefs(this.getParent());
		obj=new functionClass(this.getParent());
		ga=((Global_Application)getApplicationContext());
		
		tf = Typeface.createFromAsset(this.getAssets(),"Roboto-Condensed.ttf");
		// get screen height and width
		ScreenDimension();
	    
		w15=(int)((width*4.68)/100);
		w20=(int)((width*6.25)/100);
		w5=(int)((width*1.56)/100);
		w10=(int)((width*3.13)/100);
		w150=(int)((width*37.5)/100);
	    w30=(int)((width*9.37)/100);
		
		h40=(int)((height*8.33)/100);
		h20=(int)((height*4.17)/100);
		h10=(int)((height*2.09)/100);
		
		back=(ImageView)findViewById(R.id.back);
	 	back.setOnClickListener(Watch.this);	
		
	    lbl_invite_user_food=(TextView)findViewById(R.id.lbl_invite_user_food);
	    lbl_invite_user_food.setTypeface(tf);
	    lbl_invite_user_food.setOnClickListener(this);
	     		
	    
	    menu_invite_addfood= (LinearLayout)findViewById(R.id.menu_invite_food);
	    menu_invite_addfood.setPadding(w15, 0, w20, 0);
	    menu_invite_addfood.setOnClickListener(this);
	     		
	    heding_Addfood_name=(TextView)findViewById(R.id.heding_name_food);
	    heding_Addfood_name.setText(appPrefs.getProfileName());
	    heding_Addfood_name.setTypeface(tf);
	   		//heding_name_food.setPadding(0, 0, w50, 0);
	   		
	   	menu_invite_out_addfood = (LinearLayout)findViewById(R.id.menu_invite_out_food);
	   	menu_invite_out_addfood.setOnClickListener(this);
	   	menu_invite_out_addfood.setPadding(w15, 0, w20, 0);
	   		
	   	settiglayout_food = (LinearLayout)findViewById(R.id.settiglayout_food);
	   	settiglayout_food.setPadding(0, h40, w5, 0);
	   	
	    person_icon = (ImageView)findViewById(R.id.person_icon);
        person_icon.getLayoutParams().width = w20;
        person_icon.getLayoutParams().height = h20;
        
        options = new DisplayImageOptions.Builder()
      		.build();
      		
      		imageLoader.displayImage(appPrefs.getProfilepic(), person_icon, options, new SimpleImageLoadingListener() {
      			@Override
      			public void onLoadingComplete(Bitmap loadedImage) {
      				Animation anim = AnimationUtils.loadAnimation(Watch.this, R.anim.fade_in);
      				person_icon.setAnimation(anim);
      				anim.start();
      				
      				
      			}
      		});
	   	actionmenu();
	    
	   	reminder_scrl = (ScrollView)findViewById(R.id.reminder_scrl);
	   	medicine_scrl = (ScrollView)findViewById(R.id.medicine_scrl);
	 	test_scrl = (ScrollView)findViewById(R.id.test_scrl);
	   	reminder_scrl.setVisibility(View.VISIBLE);
	 	
	 	
	 	txt_reminder = (TextView)findViewById(R.id.txt_reminder);
	   	txt_reminder.setOnClickListener(Watch.this);
	   	txt_reminder.setBackgroundResource(R.drawable.tabpressed);
	   	
	   	txt_test = (TextView)findViewById(R.id.txt_test);
	   	txt_test.setOnClickListener(Watch.this);
	   	
	   	txt_medication = (TextView)findViewById(R.id.txt_medication);
	   	txt_medication.setOnClickListener(Watch.this);
	   	
	   	add_medicine = (Button)findViewById(R.id.add_medicine);
	   	//add_medicine.setTypeface(tf);
	   	add_medicine.setOnClickListener(Watch.this);
	   	
		add_test = (Button)findViewById(R.id.add_test);
	   	//add_medicine.setTypeface(tf);
		add_test.setOnClickListener(Watch.this);
	   	
		add_medicine_reminder = (Button)findViewById(R.id.add_medicine_reminder);
	   	//add_medicine.setTypeface(tf);
	   	add_medicine_reminder.setOnClickListener(Watch.this);
	   	
		add_test_reminder = (Button)findViewById(R.id.add_test_reminder);
	   	//add_medicine.setTypeface(tf);
		add_test_reminder.setOnClickListener(Watch.this);
		
		// for list heading
		 lbl_name = (TextView)findViewById(R.id.lbl_name);
		 lbl_name.getLayoutParams().width = w150;
       
         lbl_morning = (TextView)findViewById(R.id.lbl_morning);
        // lbl_morning.getLayoutParams().width = w30;
         
         lbl_noon = (TextView)findViewById(R.id.lbl_noon);
        // lbl_noon.getLayoutParams().width = w30;
         
         lbl_night = (TextView)findViewById(R.id.lbl_night);
         //lbl_night.getLayoutParams().width = w30;
         
         txt1 = (TextView)findViewById(R.id.txt1);
         txt1.setPadding(w5, 0, 0, 0);
         
         txt2 = (TextView)findViewById(R.id.txt2);
         txt2.setPadding(w10, 0, 0, 0);
		
		
		  
	   	//lstReminderMedicine = (RefreshableListView)findViewById(R.id.lstReminderMedicine);
		/*
	   	ArrayList<String> lstmedical = new ArrayList<String>();
	   	lstmedical.add("ABC taken");
	   	lstmedical.add("Fever medicine");   
	    MedicalDataAdapter adapter = new MedicalDataAdapter(this.getParent(),R.layout.row_medical_list, lstmedical);
	    lstReminderMedicine.setAdapter(adapter);
	      */
	      
		lstReminderTest = (RefreshableListView)findViewById(R.id.lstReminderTest);
		/*
	   	ArrayList<String> lsttest = new ArrayList<String>();
	   	lsttest.add("Blood Test");
	   	lsttest.add("Xyz");
	    TestDataAdapter adapter1 = new TestDataAdapter(this.getParent(),R.layout.row_test_list, lsttest);
	    lstReminderTest.setAdapter(adapter1);   
	    */
		
	    //mPager = (ViewPager)findViewById(R.id.pager);
        mPager1 = (ViewPager)findViewById(R.id.pager1);
	   // mPager.setOnPageChangeListener(new MyPageChangeListener());
        mPager1.setOnPageChangeListener(new MyPageChangeListener());
	   /*	search_layout = (LinearLayout)findViewById(R.id.search_layout);
	   	search_layout.setPadding(w10, h10, w10, h10);
	   	
	   	lst_data = (LinearLayout)findViewById(R.id.lst_data);
	   	lst_data.setPadding(w10, h10, w10, 0);
	   	
	   	watch_below_layout = (LinearLayout)findViewById(R.id.watch_below_layout);
	   	watch_below_layout.setPadding(w10, 0, w10, h20);
	   	
	  
	   	
	   	lbl_add = (TextView)findViewById(R.id.lbl_add);
	   	lbl_add.setOnClickListener(Watch.this);
	   	
		lbl_delete = (TextView)findViewById(R.id.lbl_delete);
		lbl_delete.setOnClickListener(Watch.this);
	   	
	   	txt_name = (TextView)findViewById(R.id.lbl_name);
	   	txt_name.setPadding(w20, 0, 0, 0);
	   	
	   	txt_time = (TextView)findViewById(R.id.lbl_time);
	   	txt_time.setPadding(w20, 0, 0, 0);
	   	
	   	lstdata = (RefreshableListView)findViewById(R.id.lstdata);
	   	lstdata.setOnRefreshListener(new RefreshableListView.OnRefreshListener() {
			
		
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if(!ga.getNextmedical().toString().equals("null")){
					
					if(isInternetOn()){
						 CallNevigationTask task = new CallNevigationTask();
						 task.applicationContext =Watch.this.getParent();
						 task.execute();
					}else{
						Toast.makeText(Watch.this,"Network is not available....",Toast.LENGTH_SHORT).show();
					}
				}
			}
		   });
	  
	   	lstdata.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Log.e("TAG","long clicked is done");
				if(selection==0){
					ga.setWatchupdate(lstResult.get(position)+"");
					ga.setUpdate("1");
					Intent AddMedicaletst = new Intent(getParent(),AddMedical.class);
					TabGroupActivity parentoption = (TabGroupActivity)getParent();
					parentoption.startChildActivity("AddMedicaletst",AddMedicaletst);
				}else{
					ga.setUpdate("1");
					ga.setWatchupdate(lstResult.get(position)+"");
					Intent AddMedication = new Intent(getParent(),AddMedication.class);
					TabGroupActivity parentoption = (TabGroupActivity)getParent();
					parentoption.startChildActivity("AddMedication",AddMedication);
				}   
				
			}
		});
	   	lstdata.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.e("TAG","item Clicked");
				return false;
			}
		});
	   */
	  
	}

    @Override
    protected void onPause()
    {
        super.onPause();

    }
    @Override
    protected void onResume() {

        super.onResume();
        /*
        ArrayList<String> lstmedical = new ArrayList<String>();
        lstmedical.add("Sugar");
        lstmedical.add("Glucose");
        MedicalDataAdapter adapter = new MedicalDataAdapter(this.getParent(),R.layout.row_medical_list, lstmedical);
        lstReminderMedicine.setAdapter(adapter);



        ArrayList<String> lsttest = new ArrayList<String>();
        lsttest.add("Blood Test");
        lsttest.add("Xyz");
        TestDataAdapter adapter1 = new TestDataAdapter(this.getParent(),R.layout.row_test_list, lsttest);
        lstReminderTest.setAdapter(adapter1);
        */

        RetrieveMedicalData task=new RetrieveMedicalData();
        task.applicationContext=Watch.this;
        task.execute();






    }


    public class StoreReminders extends AsyncTask <String, Void,String>
    {
        protected Context applicationContext;

        @Override
        protected void onPreExecute()
        {



        }

        protected void onPostExecute(String result)
        {




        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            allData = obj.getReminderInfo(user_id, "MEDICATION");
            return null;
        }

    }


    public class RetrieveMedicalData extends AsyncTask <String, Void,String>
    {
        protected Context applicationContext;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
           // dialog1 = new ProgressDialog(Watch.this);
            //dialog1.setMessage("Please Wait....");
            //dialog1.show();
            //Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            ArrayList<String> lst = new ArrayList<String>();
            lst.add("1-sep-2013");
            lst.add("2-sep-2013");
            lst.add("3-sep-2013");
            lst.add("4-sep-2013");
            lst.add("5-sep-2013");
            int i=0,j=0,k=0;
            listData.clear();
            otherData.clear();
            for (i=0;i<allData.size();i++)
            {
                if((allData.get(i).getType().equalsIgnoreCase("2")))
                {
                    listData.add(j,allData.get(i));
                    j++;
                }
                else
                {
                    otherData.add(k,allData.get(i));
                    k++;
                }
            }
            mPager1.setAdapter(new ImagePagerAdapter(lst));
            //RetrieveOtherData task1= new RetrieveOtherData();
            //task1.applicationContext=Watch.this;
            //task1.execute();


/*
            MedicalDataAdapter adapter = new MedicalDataAdapter(Watch.this.getParent(),R.layout.row_medical_list, listData);
           // lstReminderMedicine.setAdapter(adapter);

            ArrayList<String> lsttest = new ArrayList<String>();
            lsttest.add("Blood Test");
            lsttest.add("Xyz");
            TestDataAdapter adapter1 = new TestDataAdapter(Watch.this.getParent(),R.layout.row_test_list, lsttest);
            //lstReminderMedicine.setAdapter(adapter1);

            SeparatedListAdapter adapter3 = new SeparatedListAdapter(Watch.this);
            adapter3.addSection("Medication",adapter);
            adapter3.addSection("Rest All",adapter1);
            //lstReminderMedicine.setAdapter(adapter3);
*/
            //dialog1.dismiss();
            //listfood.removeAllViews();
            //Log.e("TAG","size : " + lstData.size());
            //if(lstData.size()>0){
                //finish();
          //  }else{
                //Toast.makeText(getParent(), "Try again lalter...",Toast.LENGTH_SHORT).show();
               // finish();
           // }

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            allData = obj.getReminderInfo(user_id, "2");
            return null;
        }

    }

    public class RetrieveMedicalDataById extends AsyncTask <String, Void,String>
    {
        protected Context applicationContext;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
            // dialog1 = new ProgressDialog(Watch.this);
            //dialog1.setMessage("Please Wait....");
            //dialog1.show();
            //Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {
            edit_med.putExtra("start_date",med_edit.getStart_date());
            startActivity(edit_med);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            med_edit = obj.getMedicationByID(med_id);
            return null;
        }

    }

    public class RetrieveOtherData extends AsyncTask <String, Void,String>
    {
        protected Context applicationContext;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
            // dialog1 = new ProgressDialog(Watch.this);
            //dialog1.setMessage("Please Wait....");
            //dialog1.show();
            //Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            ArrayList<String> lst = new ArrayList<String>();
            lst.add("1-sep-2013");
            lst.add("2-sep-2013");
            lst.add("3-sep-2013");
            lst.add("4-sep-2013");
            lst.add("5-sep-2013");
            mPager1.setAdapter(new ImagePagerAdapter(lst));
            RetrieveMedicalDataById task2=new RetrieveMedicalDataById();
            task2.applicationContext=Watch.this;
            task2.execute();

/*
            MedicalDataAdapter adapter = new MedicalDataAdapter(Watch.this.getParent(),R.layout.row_medical_list, listData);
           // lstReminderMedicine.setAdapter(adapter);

            ArrayList<String> lsttest = new ArrayList<String>();
            lsttest.add("Blood Test");
            lsttest.add("Xyz");
            TestDataAdapter adapter1 = new TestDataAdapter(Watch.this.getParent(),R.layout.row_test_list, lsttest);
            //lstReminderMedicine.setAdapter(adapter1);

            SeparatedListAdapter adapter3 = new SeparatedListAdapter(Watch.this);
            adapter3.addSection("Medication",adapter);
            adapter3.addSection("Rest All",adapter1);
            //lstReminderMedicine.setAdapter(adapter3);
*/
            //dialog1.dismiss();
            //listfood.removeAllViews();
            //Log.e("TAG","size : " + lstData.size());
            //if(lstData.size()>0){
            //finish();
            //  }else{
            //Toast.makeText(getParent(), "Try again lalter...",Toast.LENGTH_SHORT).show();
            // finish();
            // }

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            otherData = obj.getReminderInfo(user_id,"OTHER");
            return null;
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
			    		heding_Addfood_name.setText(appPrefs.getProfileName());
			    		for(int i=0;i<Goal_data.size();i++){
							if(value.toString().equals(appPrefs.getProfileName().toString())){
								Log.e("TAG","visible");
								((ImageView)view.findViewById(R.id.imgIcon)).setVisibility(View.VISIBLE);
							}else{
								Log.e("TAG","Invisible");
								((ImageView)view.findViewById(R.id.imgIcon)).setVisibility(View.INVISIBLE);
							}
					}
			    		Animation anim = AnimationUtils.loadAnimation(Watch.this, R.anim.fade_out);
						settiglayout_food.startAnimation(anim);
						settiglayout_food.setVisibility(View.INVISIBLE);
						menu_invite_addfood.setVisibility(View.VISIBLE);
						menu_invite_out_addfood.setVisibility(View.INVISIBLE);
						
						selecteduserid = Integer.toString(position);
						
						CalluserMeTask task = new CalluserMeTask();
						task.applicationContext =Watch.this.getParent();
						task.execute();
					}
				
			       });
		}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==back){
			Intent i=new Intent(Watch.this, Home.class);
			startActivity(i);
			finish();
		}
		
		if(v==txt_medication){
			lstResult.clear();
			selection=1;
			txt_test.setBackgroundResource(R.drawable.tabnormal);
			txt_medication.setBackgroundResource(R.drawable.tabpressed);
			txt_reminder.setBackgroundResource(R.drawable.tabnormal);
			reminder_scrl.setVisibility(View.GONE);
			medicine_scrl.setVisibility(View.VISIBLE);
			test_scrl.setVisibility(View.GONE);
			ArrayList<String> lst = new ArrayList<String>();
			lst.add("1-sep-2013");
			lst.add("2-sep-2013");
			lst.add("3-sep-2013");
			lst.add("4-sep-2013");
			lst.add("5-sep-2013");
			//mPager.setAdapter(new ImagePagerAdapter(lst));




            RefreshableListView lstReminderTest=(RefreshableListView)findViewById(R.id.lstRemTest);
            TestDataAdapter1 adapter5 = new TestDataAdapter1(Watch.this.getParent(),R.layout.row_medical_list1, otherData);
            lstReminderTest.setAdapter(adapter5);


            RefreshableListView lstReminderMedicine=(RefreshableListView)findViewById(R.id.lstReminderMedicine);

            //ArrayList<String> lstmedical = new ArrayList<String>();
            //lstmedical.add("ABC taken");
            //lstmedical.add("Fever medicine");
            MedicalDataAdapter1 adapter = new MedicalDataAdapter1(Watch.this.getParent(),R.layout.row_medical_list1, listData);
            lstReminderMedicine.setAdapter(adapter);

            lstReminderMedicine.setOnItemClickListener(new OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    final ImageView img1=(ImageView)v.findViewById(R.id.img1);
                    final ImageView img2=(ImageView)v.findViewById(R.id.img2);
                    final int pos=position;

                    final TextView name=(TextView)v.findViewById(R.id.txt_name);
                    final TextView txt_morn=(TextView)v.findViewById(R.id.txt_morning);
                    final TextView txt_noon=(TextView)v.findViewById(R.id.txt_noon);
                    final TextView txt_night=(TextView)v.findViewById(R.id.txt_night);

                    img1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(getApplicationContext(),"values =" +txt_morn.getText().toString()+ " " + txt_after.getText().toString(),Toast.LENGTH_LONG).show();

                            edit_med=new Intent(Watch.this,AddMedication.class);
                            edit_pos=pos;
                            edit_med.putExtra("iseditMed",true);
                            edit_med.putExtra("user_id",user_id);
                            med_id=listData.get(pos).getId();
                            edit_med.putExtra("id",med_id);
                            edit_med.putExtra("start_date",listData.get(pos).getStart_date());
                            edit_med.putExtra("name",name.getText().toString());
                            edit_med.putExtra("morning",txt_morn.getText().toString());
                            edit_med.putExtra("noon",txt_noon.getText().toString());
                            edit_med.putExtra("night",txt_night.getText().toString());
                            //RetrieveMedicalDataById task_med=new RetrieveMedicalDataById();
                            //task_med.execute();
                            //Start the async task here
                            startActivity(edit_med);


                        }
                    });

                    img2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent edit_med1=new Intent(Watch.this,DeleteMedication.class);
                            edit_med1.putExtra("user_id",user_id);
                            edit_med1.putExtra("id",listData.get(pos).getId());
                            startActivity(edit_med1);
                        }
                    });

            }
            });

			/*if(isInternetOn()){
		 			lstResult.clear();
		 			CallMedicationTask task = new CallMedicationTask();
		 		 	task.applicationContext =Watch.this.getParent();
				 	task.execute();
		 		
			}else{
				Toast.makeText(Watch.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}*/
		}
		if(v==add_medicine_reminder){
			Intent AddMedication = new Intent(Watch.this,AddMedication.class);
            AddMedication.putExtra("user_id",user_id);
			startActivity(AddMedication);
		}
		if(v==add_test_reminder){
			Intent AddTest = new Intent(Watch.this,AddTest.class);
			startActivity(AddTest);
		}
		if(v==txt_test){
			selection = 0;
			txt_test.setBackgroundResource(R.drawable.tabpressed);
			txt_medication.setBackgroundResource(R.drawable.tabnormal);
			txt_reminder.setBackgroundResource(R.drawable.tabnormal);
			reminder_scrl.setVisibility(View.GONE);
			medicine_scrl.setVisibility(View.GONE);       
			test_scrl.setVisibility(View.VISIBLE);

            RefreshableListView lstReminderMedicine=(RefreshableListView)findViewById(R.id.lstReminderMedicine);
            MedicalDataAdapter1 adapter4 = new MedicalDataAdapter1(Watch.this.getParent(),R.layout.row_medical_list1, listData);
            lstReminderMedicine.setAdapter(adapter4);


            RefreshableListView lstReminderTest=(RefreshableListView)findViewById(R.id.lstRemTest);
            TestDataAdapter1 adapter = new TestDataAdapter1(Watch.this.getParent(),R.layout.row_medical_list1, otherData);
            lstReminderTest.setAdapter(adapter);

            lstReminderTest.setOnItemClickListener(new OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    final ImageView img1=(ImageView)v.findViewById(R.id.img1);
                    final ImageView img2=(ImageView)v.findViewById(R.id.img2);
                    final int pos=position;




                    final TextView name=(TextView)v.findViewById(R.id.txt_name);

                    img1.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(getApplicationContext(),"values =" +txt_morn.getText().toString()+ " " + txt_after.getText().toString(),Toast.LENGTH_LONG).show();

                            edit_med=new Intent(Watch.this,AddMedication.class);
                            edit_pos=pos;
                            edit_med.putExtra("iseditOthers",true);
                            edit_med.putExtra("user_id",user_id);
                            med_id=otherData.get(pos).getId();
                            edit_med.putExtra("id",med_id);
                            edit_med.putExtra("start_date",listData.get(pos).getStart_date());
                            edit_med.putExtra("name",name.getText().toString());
                            //RetrieveMedicalDataById task_med=new RetrieveMedicalDataById();
                            //task_med.execute();
                            //Start the async task here
                            startActivity(edit_med);


                        }
                    });

                    img2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent edit_med1=new Intent(Watch.this,DeleteMedication.class);
                            edit_med1.putExtra("user_id",user_id);
                            edit_med1.putExtra("id",otherData.get(pos).getId());
                            startActivity(edit_med1);
                        }
                    });

                }
            });

			/*if(isInternetOn()){
				lstResult.clear();
	 		    CallMedicalTask task = new CallMedicalTask();
	 		 	task.applicationContext =Watch.this.getParent();
			 	task.execute();
	 		}else{
				Toast.makeText(Watch.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}*/
		}
		if(v==txt_reminder){
			txt_test.setBackgroundResource(R.drawable.tabnormal);
			txt_medication.setBackgroundResource(R.drawable.tabnormal);
			txt_reminder.setBackgroundResource(R.drawable.tabpressed);
			reminder_scrl.setVisibility(View.VISIBLE);
			medicine_scrl.setVisibility(View.GONE);
			test_scrl.setVisibility(View.GONE);
            ArrayList<String> lst = new ArrayList<String>();
            lst.add("1-sep-2013");
            lst.add("2-sep-2013");
            lst.add("3-sep-2013");
            lst.add("4-sep-2013");
            lst.add("5-sep-2013");
            //mPager.setAdapter(new ImagePagerAdapter(lst));
		}   
		if(v==add_medicine){
			Intent AddMedication = new Intent(Watch.this,AddMedication.class);
            AddMedication.putExtra("user_id",user_id);
			startActivity(AddMedication);
		}    
		if(v==add_test){
			Intent AddTest = new Intent(Watch.this,AddTest.class);
			startActivity(AddTest);
		}
		/*if(v==lbl_add){
			if(selection==0){
				Intent AddMedicaletst = new Intent(getParent(),AddMedical.class);
				TabGroupActivity parentoption = (TabGroupActivity)getParent();
				parentoption.startChildActivity("AddMedicaletst",AddMedicaletst);
			}else{
				Intent AddMedication = new Intent(getParent(),AddMedication.class);
				TabGroupActivity parentoption = (TabGroupActivity)getParent();
				parentoption.startChildActivity("AddMedication",AddMedication);
			}
		}
		if(v==lbl_delete){
			if(selection==1){
				boolean val=false;
				for(int i=0;i<lstResult.size();i++){
					if(lstResult.get(i).isChecked()){
						delid=lstResult.get(i).getId() + "," + delid;
						val=true;
					}
				}

				if(val==true){
					Log.e("TAG",delid.toString().substring(0, delid.length()-5) + " th cb is checked");
					if(isInternetOn()){
						 DeleteMedicationTask task = new DeleteMedicationTask();
						 task.applicationContext =this.getParent();
						 task.execute();
						 //appPrefs.setReloadgraph("0");
					}else{
						Toast.makeText(Watch.this,"Network is not available....",Toast.LENGTH_SHORT).show();
					}     
				}else{
					Toast.makeText(Watch.this, "Please select atlest one file..", Toast.LENGTH_SHORT).show();
				}
			}else{
				boolean val=false;
				for(int i=0;i<lstResult.size();i++){
					if(lstResult.get(i).isChecked()){
						delid=lstResult.get(i).getId() + "," + delid;
						val=true;
					}
				}

				if(val==true){
					Log.e("TAG",delid.toString().substring(0, delid.length()-5) + " th cb is checked");
					if(isInternetOn()){
						DeleteMedicalTask task = new DeleteMedicalTask();
						 task.applicationContext =this.getParent();
						 task.execute();
						 //appPrefs.setReloadgraph("0");
					}else{
						Toast.makeText(Watch.this,"Network is not available....",Toast.LENGTH_SHORT).show();
					}     
				}else{
					Toast.makeText(Watch.this, "Please select atlest one file..", Toast.LENGTH_SHORT).show();
				}
			}
		}*/
		if(v==lbl_invite_user_food){
			Log.e("TAG","Selected value is " + "invite user is clicked");
			Animation anim = AnimationUtils.loadAnimation(Watch.this, R.anim.fade_out);
			settiglayout_food.startAnimation(anim);
			settiglayout_food.setVisibility(View.INVISIBLE);
			menu_invite_addfood.setVisibility(View.VISIBLE);
			menu_invite_out_addfood.setVisibility(View.INVISIBLE);
			Log.e("TAG","Clicked");
			Intent i = new Intent(Watch.this,InviteUser.class);
			startActivity(i);
		}
		if(v==menu_invite_addfood){
			actionmenu();
			settiglayout_food.setVisibility(View.VISIBLE);
			menu_invite_out_addfood.setVisibility(View.VISIBLE);
			menu_invite_addfood.setVisibility(View.INVISIBLE);
			Animation anim = AnimationUtils.loadAnimation(Watch.this, R.anim.fade_in);
			settiglayout_food.startAnimation(anim);
			
			Log.e("TAG","Clicked");
		}
		if(v==menu_invite_out_addfood){
			Animation anim = AnimationUtils.loadAnimation(Watch.this, R.anim.fade_out);
			settiglayout_food.startAnimation(anim);
			settiglayout_food.setVisibility(View.INVISIBLE);
			menu_invite_addfood.setVisibility(View.VISIBLE);
			menu_invite_out_addfood.setVisibility(View.INVISIBLE);
			Log.e("TAG","Clicked");
		}
	}
	/*// async class for calling webservice and get responce message
	public class CallMedicalTask extends AsyncTask <String, Void,String>
	{
		protected Context applicationContext;

		@Override
		protected void onPreExecute()     
		{
			
			//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
			dialog = new ProgressDialog(getParent());
			dialog.setMessage("Please Wait....");
			dialog.show();
			Log.i("onPreExecute", "onPreExecute");
			
		}       
		
		protected void onPostExecute(String result)
		{
			
			Log.i("onPostExecute", "onPostExecute");
				dialog.dismiss();
				if(lstResult.size()>0){
					 MedicalDataAdapter adapter = new MedicalDataAdapter(Watch.this.getParent(),R.layout.row_medical_list, lstResult);
				     lstdata.setAdapter(adapter);	
				     adapter.notifyDataSetChanged();
				     lstdata.onRefreshComplete();
				}else{
					 MedicalDataAdapter adapter = new MedicalDataAdapter(Watch.this.getParent(),R.layout.row_medical_list, lstResult);
				     lstdata.setAdapter(adapter);	
				     adapter.notifyDataSetChanged();
				     lstdata.onRefreshComplete();
					 Toast.makeText(getParent(), "No Medical found...",Toast.LENGTH_SHORT).show();
				}
		}  
   
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.i("doInBackground--Object", "doInBackground--Object");
			lstResult.clear();
			lstResult=obj.getMedical();
			return null;
		}
		   
	}     
	// async class for calling webservice and get responce message
	public class CallNevigationTask extends AsyncTask <String, Void,String>
	{
		protected Context applicationContext;

		@Override
		protected void onPreExecute()     
		{
			
			//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
			dialog = new ProgressDialog(getParent());
			dialog.setMessage("Please Wait....");
			dialog.show();
			Log.i("onPreExecute", "onPreExecute");
			
		}         
		
		protected void onPostExecute(String result)
		{
			
			Log.i("onPostExecute", "onPostExecute");
				dialog.dismiss();
				Log.e("TAG","File list size : " + lstResult.size());
				if(lstResult.size()>0){
					 MedicalDataAdapter adapter = new MedicalDataAdapter(Watch.this.getParent(),R.layout.row_medical_list, lstResult);
				     lstdata.setAdapter(adapter);	
				     adapter.notifyDataSetChanged();
				     lstdata.onRefreshComplete();
				}else{
					Toast.makeText(getParent(), "No More Medical found...",Toast.LENGTH_SHORT).show();
				}
		}  
   
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.i("doInBackground--Object", "doInBackground--Object");
			lstResult.addAll(obj.getMedical(ga.getNextmedical()));
			
			return null;
		}
		   
	}     
	// async class for calling webservice and get responce message
		public class CallMedicationTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()     
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				dialog = new ProgressDialog(getParent());
				dialog.setMessage("Please Wait....");
				dialog.show();
				Log.i("onPreExecute", "onPreExecute");
				
			}       
			
			protected void onPostExecute(String result)
			{
				
				Log.i("onPostExecute", "onPostExecute");
					dialog.dismiss();
					if(lstResult.size()>0){
						 MedicalDataAdapter adapter = new MedicalDataAdapter(Watch.this.getParent(),R.layout.row_medical_list, lstResult);
					     lstdata.setAdapter(adapter);	
					     adapter.notifyDataSetChanged();
					     lstdata.onRefreshComplete();
					}else{
						 MedicalDataAdapter adapter = new MedicalDataAdapter(Watch.this.getParent(),R.layout.row_medical_list, lstResult);
					     lstdata.setAdapter(adapter);	
					     adapter.notifyDataSetChanged();
					     lstdata.onRefreshComplete();
						Toast.makeText(getParent(), "No Medication found...",Toast.LENGTH_SHORT).show();
					}
			}  
	   
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
				lstResult.clear();
				lstResult=obj.getMedication();
				return null;
			}
			   
		}     
		// async class for calling webservice and get responce message
		public class CallNevigationMedicationTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()     
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				dialog = new ProgressDialog(getParent());
				dialog.setMessage("Please Wait....");
				dialog.show();
				Log.i("onPreExecute", "onPreExecute");
				
			}         
			
			protected void onPostExecute(String result)
			{
				
				Log.i("onPostExecute", "onPostExecute");
					dialog.dismiss();
					Log.e("TAG","File list size : " + lstResult.size());
					if(lstResult.size()>0){
						 MedicalDataAdapter adapter = new MedicalDataAdapter(Watch.this.getParent(),R.layout.row_medical_list, lstResult);
					     lstdata.setAdapter(adapter);	
					     adapter.notifyDataSetChanged();
					     lstdata.onRefreshComplete();
					}else{
						Toast.makeText(getParent(), "No More Medication found...",Toast.LENGTH_SHORT).show();
					}
			}  
	   
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
				lstResult.addAll(obj.getMedication(ga.getNextmedication()));
				
				return null;
			}
			   
		}     
		
		// async class for calling webservice and get responce message
		public class DeleteMedicalTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()     
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				dialog = new ProgressDialog(getParent());
				dialog.setMessage("Please Wait....");
				dialog.show();
				Log.i("onPreExecute", "onPreExecute");
				
			}       
			
			protected void onPostExecute(String result)
			{
				
				Log.i("onPostExecute", "onPostExecute");
					dialog.dismiss();
					if(isInternetOn()){
						 CallMedicalTask task = new CallMedicalTask();
						 task.applicationContext =Watch.this.getParent();
						 task.execute();
						
					}else{
						Toast.makeText(Watch.this,"Network is not available....",Toast.LENGTH_SHORT).show();
					}     
			}  
	   
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
				String[] temp = delid.toString().substring(0, delid.length()-5).split(",");
				for(int i=0;i<temp.length;i++){
					obj.DeleteMedical(Global_Application.url + "medicaltests/"+temp[i]+"/");
				}
				return null;
			}
			   
		}     
		// async class for calling webservice and get responce message
		public class DeleteMedicationTask extends AsyncTask <String, Void,String>
		{
			protected Context applicationContext;

			@Override
			protected void onPreExecute()     
			{
				
				//dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
				dialog = new ProgressDialog(getParent());
				dialog.setMessage("Please Wait....");
				dialog.show();
				Log.i("onPreExecute", "onPreExecute");
				
			}       
			
			protected void onPostExecute(String result)
			{
				
				Log.i("onPostExecute", "onPostExecute");
					dialog.dismiss();
					if(isInternetOn()){
						 CallMedicalTask task = new CallMedicalTask();
						 task.applicationContext =Watch.this.getParent();
						 task.execute();
						
					}else{
						Toast.makeText(Watch.this,"Network is not available....",Toast.LENGTH_SHORT).show();
					}     
			}  
	   
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("doInBackground--Object", "doInBackground--Object");
				String[] temp = delid.toString().substring(0, delid.length()-5).split(",");
				for(int i=0;i<temp.length;i++){
					obj.DeleteMedication(Global_Application.url + "medications/"+temp[i]+"/");
				}
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
		 	if(isInternetOn()){
		 		 if(selection==0){
		 			lstResult.clear();
		 		    CallMedicalTask task = new CallMedicalTask();
		 		 	task.applicationContext =Watch.this.getParent();
				 	task.execute();
		 		 }else{
		 			lstResult.clear();
		 			CallMedicationTask task = new CallMedicationTask();
		 		 	task.applicationContext =Watch.this.getParent();
				 	task.execute();
		 		 }
			}else{
				Toast.makeText(Watch.this,"Network is not available....",Toast.LENGTH_SHORT).show();
			}
		}
*/
	   public class MyPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
			  
		     @Override
		     public void onPageSelected(int position)
		     {
		    	 //Log.e("TAG","position is : " + position);
		    	 //Toast.makeText(Watch.this,"selected postion is " + position, Toast.LENGTH_SHORT).show();
		        
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
				dialog = new ProgressDialog(Watch.this.getParent());
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
                UserEP userEP = new UserEP(Watch.this, ga);
                userEP.getUserProfile(ga.getLoggedInUser().getId());
				return null;
			}
			   
		}     
	   private class ImagePagerAdapter extends PagerAdapter {
		   ArrayList<String> lstData = new ArrayList<String>();
			private LayoutInflater inflater;

			ImagePagerAdapter(ArrayList<String> lstData) {
				this.lstData = lstData;
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
				View imageLayout = inflater.inflate(R.layout.item_pager_medicine, null);
				TextView todayDate=(TextView)imageLayout.findViewById(R.id.todayDate);

                RefreshableListView lstReminderMedicine1=(RefreshableListView)imageLayout.findViewById(R.id.lstReminderMedicine1);

				     
				todayDate.setText(lstData.get(position));
                /*
				
				ArrayList<String> lstmedical = new ArrayList<String>();
			   	lstmedical.add("ABC taken");
			   	lstmedical.add("Fever medicine");   
			    MedicalDataAdapter1 adapter = new MedicalDataAdapter1(Watch.this.getParent(),R.layout.row_medical_list1, lstmedical);
			    lstReminderMedicine1.setAdapter(adapter);
			      
				
			
				((ViewPager) view).addView(imageLayout, 0);
				*/

                MedicalDataAdapter adapter = new MedicalDataAdapter(Watch.this.getParent(),R.layout.row_medical_list, listData);
                // lstReminderMedicine.setAdapter(adapter);

                //ArrayList<String> lsttest = new ArrayList<String>();
                //lsttest.add("Blood Test");
                //lsttest.add("Xyz");

                RefreshableListView lstReminderMedicine=(RefreshableListView)findViewById(R.id.lstReminderMedicine);

                //ArrayList<String> lstmedical = new ArrayList<String>();
                //lstmedical.add("ABC taken");
                //lstmedical.add("Fever medicine");
                MedicalDataAdapter1 adapter4 = new MedicalDataAdapter1(Watch.this.getParent(),R.layout.row_medical_list1, listData);
                lstReminderMedicine.setAdapter(adapter4);


                RefreshableListView lstReminderTest=(RefreshableListView)findViewById(R.id.lstRemTest);
                TestDataAdapter1 adapter5 = new TestDataAdapter1(Watch.this.getParent(),R.layout.row_medical_list1, otherData);
                lstReminderTest.setAdapter(adapter5);

                TestDataAdapter adapter1 = new TestDataAdapter(Watch.this.getParent(),R.layout.row_test_list, otherData);
                //lstReminderMedicine.setAdapter(adapter1);

                SeparatedListAdapter adapter3 = new SeparatedListAdapter(Watch.this);
                adapter3.addSection("Medication",adapter);
                adapter3.addSection("Rest All",adapter1);
                ((ViewPager) view).addView(imageLayout, 0);
                lstReminderMedicine1.setAdapter(adapter3);

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
@Override
    public void onBackPressed() 
        {
          
         
        }

	  
	}
