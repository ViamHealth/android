package com.viamhealth.android.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.activities.AddMedication;
import com.viamhealth.android.activities.AddTest;
import com.viamhealth.android.activities.DeleteMedication;
import com.viamhealth.android.activities.Home;
import com.viamhealth.android.activities.InviteUser;
import com.viamhealth.android.adapters.MedicalDataAdapter;
import com.viamhealth.android.adapters.MedicalDataAdapter1;
import com.viamhealth.android.adapters.SeparatedListAdapter;
import com.viamhealth.android.adapters.TestDataAdapter;
import com.viamhealth.android.adapters.TestDataAdapter1;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.ui.RefreshableListView;

import java.util.ArrayList;

/**
 * Created by naren on 08/10/13.
 */
public class ReminderFragment extends Fragment {

    private User user;
    private ViamHealthPrefs appPrefs;
    private functionClass obj;
    private Global_Application ga;
    private Typeface tf;

    ScrollView reminder_scrl,medicine_scrl,test_scrl;
    
    Display display;
    int width,height;
    int w15,w20,w5,h40,h20,w10,h10,w150,w30;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_reminder, container, false);

        user=getArguments().getParcelable("user");

        //Toast.makeText(getApplicationContext(),"user_id="+user_id,Toast.LENGTH_LONG).show();
        appPrefs = new ViamHealthPrefs(getActivity());
        obj=new functionClass(getActivity());
        ga=((Global_Application)getActivity().getApplicationContext());

        tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Condensed.ttf");
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

        reminder_scrl = (ScrollView)findViewById(R.id.reminder_scrl);
        medicine_scrl = (ScrollView)findViewById(R.id.medicine_scrl);
        test_scrl = (ScrollView)findViewById(R.id.test_scrl);
        reminder_scrl.setVisibility(View.VISIBLE);


        txt_reminder = (TextView)findViewById(R.id.txt_reminder);
        txt_reminder.setOnClickListener(getActivity());
        txt_reminder.setBackgroundResource(R.drawable.tabpressed);

        txt_test = (TextView)findViewById(R.id.txt_test);
        txt_test.setOnClickListener(getActivity());

        txt_medication = (TextView)findViewById(R.id.txt_medication);
        txt_medication.setOnClickListener(getActivity());

        add_medicine = (Button)findViewById(R.id.add_medicine);
        //add_medicine.setTypeface(tf);
        add_medicine.setOnClickListener(getActivity());

        add_test = (Button)findViewById(R.id.add_test);
        //add_medicine.setTypeface(tf);
        add_test.setOnClickListener(getActivity());

        add_medicine_reminder = (Button)findViewById(R.id.add_medicine_reminder);
        //add_medicine.setTypeface(tf);
        add_medicine_reminder.setOnClickListener(getActivity());

        add_test_reminder = (Button)findViewById(R.id.add_test_reminder);
        //add_medicine.setTypeface(tf);
        add_test_reminder.setOnClickListener(getActivity());

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

        lstReminderTest = (RefreshableListView)findViewById(R.id.lstReminderTest);
        mPager1 = (ViewPager)findViewById(R.id.pager1);

        mPager1.setOnPageChangeListener(new MyPageChangeListener());


        return view;
    }

    public void ScreenDimension()
    {
        display = getActivity().getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        appPrefs.setSwidth(String.valueOf(width));
        appPrefs.setSheight(String.valueOf(height));
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if(v==back){
            Intent i=new Intent(getActivity(), Home.class);
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
            TestDataAdapter1 adapter5 = new TestDataAdapter1(getActivity(),R.layout.row_medical_list1, otherData);
            lstReminderTest.setAdapter(adapter5);


            RefreshableListView lstReminderMedicine=(RefreshableListView)findViewById(R.id.lstReminderMedicine);

            //ArrayList<String> lstmedical = new ArrayList<String>();
            //lstmedical.add("ABC taken");
            //lstmedical.add("Fever medicine");
            MedicalDataAdapter1 adapter = new MedicalDataAdapter1(getActivity(),R.layout.row_medical_list1, listData);
            lstReminderMedicine.setAdapter(adapter);

            lstReminderMedicine.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    final ImageView img1=(ImageView)v.findViewById(R.id.img1);
                    final ImageView img2=(ImageView)v.findViewById(R.id.img2);
                    final int pos=position;

                    final TextView name=(TextView)v.findViewById(R.id.txt_name);
                    final TextView txt_morn=(TextView)v.findViewById(R.id.txt_morning);
                    final TextView txt_noon=(TextView)v.findViewById(R.id.txt_noon);
                    final TextView txt_night=(TextView)v.findViewById(R.id.txt_night);

                    img1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(getApplicationContext(),"values =" +txt_morn.getText().toString()+ " " + txt_after.getText().toString(),Toast.LENGTH_LONG).show();

                            edit_med=new Intent(getActivity(),AddMedication.class);
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

                    img2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent edit_med1=new Intent(getActivity(),DeleteMedication.class);
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
		 		 	task.applicationContext =getActivity();
				 	task.execute();

			}else{
				Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
			}*/
        }
        if(v==add_medicine_reminder){
            Intent AddMedication = new Intent(getActivity(),AddMedication.class);
            AddMedication.putExtra("user_id",user_id);
            startActivity(AddMedication);
        }
        if(v==add_test_reminder){
            Intent AddTest = new Intent(getActivity(), com.viamhealth.android.activities.AddTest.class);
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
            MedicalDataAdapter1 adapter4 = new MedicalDataAdapter1(getActivity(),R.layout.row_medical_list1, listData);
            lstReminderMedicine.setAdapter(adapter4);


            RefreshableListView lstReminderTest=(RefreshableListView)findViewById(R.id.lstRemTest);
            TestDataAdapter1 adapter = new TestDataAdapter1(getActivity(),R.layout.row_medical_list1, otherData);
            lstReminderTest.setAdapter(adapter);

            lstReminderTest.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    final ImageView img1=(ImageView)v.findViewById(R.id.img1);
                    final ImageView img2=(ImageView)v.findViewById(R.id.img2);
                    final int pos=position;




                    final TextView name=(TextView)v.findViewById(R.id.txt_name);

                    img1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(getApplicationContext(),"values =" +txt_morn.getText().toString()+ " " + txt_after.getText().toString(),Toast.LENGTH_LONG).show();

                            edit_med=new Intent(getActivity(),AddMedication.class);
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

                    img2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent edit_med1=new Intent(getActivity(),DeleteMedication.class);
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
	 		 	task.applicationContext =getActivity();
			 	task.execute();
	 		}else{
				Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
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
            Intent AddMedication = new Intent(getActivity(),AddMedication.class);
            AddMedication.putExtra("user_id",user_id);
            startActivity(AddMedication);
        }
        if(v==add_test){
            Intent AddTest = new Intent(getActivity(),AddTest.class);
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
						Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getActivity(), "Please select atlest one file..", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getActivity(), "Please select atlest one file..", Toast.LENGTH_SHORT).show();
				}
			}
		}*/
        if(v==lbl_invite_user_food){
            Log.e("TAG", "Selected value is " + "invite user is clicked");
            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            settiglayout_food.startAnimation(anim);
            settiglayout_food.setVisibility(View.INVISIBLE);
            menu_invite_addfood.setVisibility(View.VISIBLE);
            menu_invite_out_addfood.setVisibility(View.INVISIBLE);
            Log.e("TAG","Clicked");
            Intent i = new Intent(getActivity(),InviteUser.class);
            startActivity(i);
        }
        if(v==menu_invite_addfood){
            actionmenu();
            settiglayout_food.setVisibility(View.VISIBLE);
            menu_invite_out_addfood.setVisibility(View.VISIBLE);
            menu_invite_addfood.setVisibility(View.INVISIBLE);
            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            settiglayout_food.startAnimation(anim);

            Log.e("TAG","Clicked");
        }
        if(v==menu_invite_out_addfood){
            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            settiglayout_food.startAnimation(anim);
            settiglayout_food.setVisibility(View.INVISIBLE);
            menu_invite_addfood.setVisibility(View.VISIBLE);
            menu_invite_out_addfood.setVisibility(View.INVISIBLE);
            Log.e("TAG","Clicked");
        }
    }

    public class MyPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageSelected(int position)
        {
            //Log.e("TAG","position is : " + position);
            //Toast.makeText(getActivity(),"selected postion is " + position, Toast.LENGTH_SHORT).show();

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
			    MedicalDataAdapter1 adapter = new MedicalDataAdapter1(getActivity(),R.layout.row_medical_list1, lstmedical);
			    lstReminderMedicine1.setAdapter(adapter);



				((ViewPager) view).addView(imageLayout, 0);
				*/

            MedicalDataAdapter adapter = new MedicalDataAdapter(getActivity(),R.layout.row_medical_list, listData);
            // lstReminderMedicine.setAdapter(adapter);

            //ArrayList<String> lsttest = new ArrayList<String>();
            //lsttest.add("Blood Test");
            //lsttest.add("Xyz");

            RefreshableListView lstReminderMedicine=(RefreshableListView)findViewById(R.id.lstReminderMedicine);

            //ArrayList<String> lstmedical = new ArrayList<String>();
            //lstmedical.add("ABC taken");
            //lstmedical.add("Fever medicine");
            MedicalDataAdapter1 adapter4 = new MedicalDataAdapter1(getActivity(),R.layout.row_medical_list1, listData);
            lstReminderMedicine.setAdapter(adapter4);


            RefreshableListView lstReminderTest=(RefreshableListView)findViewById(R.id.lstRemTest);
            TestDataAdapter1 adapter5 = new TestDataAdapter1(getActivity(),R.layout.row_medical_list1, otherData);
            lstReminderTest.setAdapter(adapter5);

            TestDataAdapter adapter1 = new TestDataAdapter(getActivity(),R.layout.row_test_list, otherData);
            //lstReminderMedicine.setAdapter(adapter1);

            SeparatedListAdapter adapter3 = new SeparatedListAdapter(getActivity());
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

    public class StoreReminders extends AsyncTask<String, Void,String>
    {
        protected FragmentActivity activity;

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
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
            // dialog1 = new ProgressDialog(getActivity());
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
            //task1.activity=getActivity();
            //task1.execute();


/*
            MedicalDataAdapter adapter = new MedicalDataAdapter(getActivity(),R.layout.row_medical_list, listData);
           // lstReminderMedicine.setAdapter(adapter);

            ArrayList<String> lsttest = new ArrayList<String>();
            lsttest.add("Blood Test");
            lsttest.add("Xyz");
            TestDataAdapter adapter1 = new TestDataAdapter(getActivity(),R.layout.row_test_list, lsttest);
            //lstReminderMedicine.setAdapter(adapter1);

            SeparatedListAdapter adapter3 = new SeparatedListAdapter(getActivity());
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
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
            // dialog1 = new ProgressDialog(getActivity());
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
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
            // dialog1 = new ProgressDialog(getActivity());
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
            task2.activity=getActivity();
            task2.execute();

/*
            MedicalDataAdapter adapter = new MedicalDataAdapter(getActivity(),R.layout.row_medical_list, listData);
           // lstReminderMedicine.setAdapter(adapter);

            ArrayList<String> lsttest = new ArrayList<String>();
            lsttest.add("Blood Test");
            lsttest.add("Xyz");
            TestDataAdapter adapter1 = new TestDataAdapter(getActivity(),R.layout.row_test_list, lsttest);
            //lstReminderMedicine.setAdapter(adapter1);

            SeparatedListAdapter adapter3 = new SeparatedListAdapter(getActivity());
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

}
