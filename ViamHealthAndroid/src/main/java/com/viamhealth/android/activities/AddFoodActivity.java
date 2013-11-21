package com.viamhealth.android.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naren on 19/11/13.
 */
public class AddFoodActivity extends BaseFragmentActivity implements SearchView.OnQueryTextListener {

    TextView food_search_name;
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
        /*** Action bar Creation Ends Here ***/

        food_search_name = (TextView) findViewById(R.id.food_search_name);
        listFood = (RefreshableListView) findViewById(R.id.lstfood);
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
        if(Checker.isInternetOn(AddFoodActivity.this)){
            CallSearchFoodTask task = new CallSearchFoodTask();
            task.applicationContext = AddFoodActivity.this;
            task.execute();
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    // async class for calling webservice and get responce message
    public class CallSearchFoodTask extends AsyncTask<String, Void, String> {

        protected Context applicationContext;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(applicationContext, "Calling", "Please wait...", true);
            dialog = new ProgressDialog(AddFoodActivity.this);
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
                //addfood_count.setText("("+lstResult.size()+")");
                food_search_name.setText(searchQuery);
                FoodAdapter adapter = new FoodAdapter(AddFoodActivity.this, R.layout.addfoodlist, lstResult);
                listFood.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                listFood.onRefreshComplete();
            }else{
                if(dialog!=null)
                    dialog.dismiss();
                Toast.makeText(AddFoodActivity.this, "No Food found...", Toast.LENGTH_SHORT).show();
                //addfood_count.setText("("+lstResult+")");
            }

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

}
