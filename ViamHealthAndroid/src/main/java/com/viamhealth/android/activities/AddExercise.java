package com.viamhealth.android.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.adapters.BreakfastAdapter;
import com.viamhealth.android.adapters.DinnerAdapter;
import com.viamhealth.android.adapters.LunchAdapter;
import com.viamhealth.android.adapters.SnacksAdapter;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.users.User;

/**
 * Created by Administrator on 10/14/13.
 */
public class AddExercise extends BaseActivity {
    functionClass obj;
    String weight,time_spent,physical_activity_id,calories_spent="";
    EditText time_val,calories_val;
    Spinner time_check=null,physical_activity_type;
    User user;
    int time_type=0;
    float time_float;
    Button btnSave,btnCancel;
    String date;
    String exercise_list[]=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_exercise);
        obj=new functionClass(AddExercise.this);
        user=getIntent().getParcelableExtra("user");
        time_val=(EditText)findViewById(R.id.txt_time);
        time_check=(Spinner)findViewById(R.id.time_type);
        calories_val=(EditText)findViewById(R.id.txt_calories);
        physical_activity_type=(Spinner)findViewById(R.id.exercise_type);
        physical_activity_type.setOnItemSelectedListener(new CustomOnExerciseSelectedListener());

        date=getIntent().getStringExtra("activity_date");
        //ArrayAdapter<String> yourAdapter=new ArrayAdapter<String>(this, R.layout.custom_spinner_item, R.array.duration_type);


        RetrieveExerciseList fillExercise= new RetrieveExerciseList();
        fillExercise.execute();

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.time_type, R.layout.custom_spinner_item); //change the last argument here to your xml above.
        //typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time_check.setAdapter(typeAdapter);




        //TextView tv=(TextView)time_check.getSelectedView();
        //tv.setTextColor(Color.BLACK);

        time_check.setOnItemSelectedListener(new CustomOnTimeSelectedListener());


        btnSave=(Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time_type==1)
                {
                    time_float=(Float.parseFloat(time_val.getText().toString())*60);
                    time_spent=String.valueOf(time_float);
                    Toast.makeText(getApplicationContext(),"time_type=1 time_spent="+time_spent+time_spent,Toast.LENGTH_LONG).show();
                }
                else
                {
                    time_spent=time_val.getText().toString();
                    Toast.makeText(getApplicationContext(),"time-spent="+time_spent,Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),"time_type="+time_type +" time_spent="+time_spent+time_spent,Toast.LENGTH_LONG).show();
                }

                Toast.makeText(getApplicationContext(),"time-spent="+time_spent,Toast.LENGTH_LONG).show();
                if(!calories_val.getText().toString().equalsIgnoreCase(""))
                {
                    calories_spent=calories_val.getText().toString();
                }

                AddExercisetoserver txt= new AddExercisetoserver();
                txt.execute();


            }
        });


        getSupportActionBar().setTitle("Add Exercise");
        getSupportActionBar().setSubtitle(user.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnCancel=(Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class CustomOnTimeSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            if(pos==1)
            {
                time_type=1;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
            time_type=0;
        }

}


    public class CustomOnExerciseSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            physical_activity_id=String.valueOf(pos+1);
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
            time_type=0;
        }

    }


    public class RetrieveExerciseList extends AsyncTask<String, Void,String>
    {
        protected FragmentActivity activity;
        ProgressDialog mDialog;

        @Override
        protected void onPreExecute()
        {
            mDialog=new ProgressDialog(AddExercise.this);
            mDialog.setMessage("Please Wait...");
            mDialog.show();

        }

        protected void onPostExecute(String result)
        {
            if(mDialog!=null)
            {
                mDialog.dismiss();
                mDialog=null;
            }
            Toast.makeText(getApplicationContext(),"time_spent  = "+time_spent,Toast.LENGTH_LONG).show();

            ArrayAdapter exerciseAdapter = new ArrayAdapter (AddExercise.this, R.layout.custom_spinner_item,exercise_list);
 //change the last argument here to your xml above.
            //typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            physical_activity_type.setAdapter(exerciseAdapter);
            //finish();

        }


        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);

            exercise_list=obj.retrieveExercise();
            return null;
        }

    }



    public class AddExercisetoserver extends AsyncTask<String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            Toast.makeText(getApplicationContext(),"physical activity id="+physical_activity_id,Toast.LENGTH_SHORT).show();

        }

        protected void onPostExecute(String result)
        {

            Toast.makeText(getApplicationContext(),"time_spent  = "+time_spent,Toast.LENGTH_LONG).show();
            finish();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);

            obj.addExercise(user.getBmiProfile().getWeight().toString(),time_spent,physical_activity_id,calories_spent,user.getId().toString(),date);
            return null;
        }

    }


    public class CalculateCalories extends AsyncTask<String, Void,String>
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
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);


            return null;
        }

    }


}
