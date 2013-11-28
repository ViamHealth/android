package com.viamhealth.android.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.adapters.FoodAdapter;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.FoodData;
import com.viamhealth.android.model.enums.FoodType;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.ui.RefreshableListView;
import com.viamhealth.android.utils.Checker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by naren on 19/11/13.
 */
public class AddFoodActivity extends BaseFragmentActivity implements SearchView.OnQueryTextListener {

    RefreshableListView listFood;

    ActionBar actionBar;

    ProgressDialog dialog;

    ViamHealthPrefs appPrefs;
    Global_Application ga;
    Typeface tf;

    functionClass dao;

    ArrayList<FoodData> lstResult = new ArrayList<FoodData>();
    String searchQuery;

    User user;

    functionClass obj;
    ProgressDialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_add_food);

        appPrefs = new ViamHealthPrefs(AddFoodActivity.this);
        dao=new functionClass(AddFoodActivity.this);
        ga=((Global_Application)getApplicationContext());
        tf = Typeface.createFromAsset(this.getAssets(), "Roboto-Condensed.ttf");

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        String dietDate = intent.getStringExtra("diet_date");
        FoodType type = (FoodType) intent.getSerializableExtra("type");

        /*** Action Bar Creation starts here ***/
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Add " + getString(type.resId()));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setLogo(R.drawable.ic_action_white_brand);

        Context themedContext = actionBar.getThemedContext();
        obj=new functionClass(AddFoodActivity.this);
        /*** Action bar Creation Ends Here ***/

        listFood = (RefreshableListView) findViewById(R.id.lstfood);

        listFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setQueryHint("Search for food...");
        searchView.setOnQueryTextListener(this);
        //searchView.setOnSuggestionListener(this);

        menu.add("Search")
                .setIcon(com.actionbarsherlock.R.drawable.abs__ic_search)
                .setActionView(searchView)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        //TODO get the food data from the API
        searchQuery = s;
        //hideKeyboard();
        if(Checker.isInternetOn(AddFoodActivity.this)){
            CallSearchFoodTask task = new CallSearchFoodTask();
            task.applicationContext = AddFoodActivity.this;
            task.execute();
        }
        return true;
    }
    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    // async class for calling webservice and get responce message
    public class CallSearchFoodTask extends AsyncTask<String, Void, String> {

        protected Context applicationContext;

        @Override
        protected void onPreExecute() {
            if(dialog!=null){
                dialog.dismiss();
                dialog=null;
            }

            dialog = new ProgressDialog(AddFoodActivity.this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Please Wait....");
            dialog.show();
            Log.i("onPreExecute", "onPreExecute");
        }

        protected void onPostExecute(String result){

            Log.i("onPostExecute", "onPostExecute");
            if(dialog!=null)
            {
                dialog.dismiss();
                dialog=null;
            }

            if(lstResult.size()>0){
                FoodAdapter adapter = new FoodAdapter(AddFoodActivity.this, R.layout.addfoodlist, lstResult);
                listFood.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                listFood.onRefreshComplete();
                hideKeyboard();
            }else{
                if(dialog!=null)
                {
                    dialog.dismiss();
                    dialog=null;
                }
                //Toast.makeText(AddFoodActivity.this, "No Food found...", Toast.LENGTH_SHORT).show();
                //addfood_count.setText("("+lstResult+")");
            }
            //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        }



        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=dao.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            lstResult.clear();

            lstResult.addAll(dao.SearchFoodItem(searchQuery));
            return null;
        }

    }

    public class CallAddFoodTask extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
            dialog1 = new ProgressDialog(AddFoodActivity.this);
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
                Toast.makeText(AddFoodActivity.this, "Food added successfully...",Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected String doInBackground(String... params) {

            String addFood=null;
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //ga.lstResult=obj.manageGoal(appPrefs.getGoalname().toString(), type, goalvalue);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date=new Date();

            return obj.AddFood(ga.getLstFood().get(ga.getFoodPos()).getId(), ga.getFoodType().toUpperCase(), "1",user.getId().toString(),dateFormat.format(date));

        }

    }

}
