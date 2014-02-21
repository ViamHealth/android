package com.viamhealth.android.activities.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.viamhealth.android.R;
import com.viamhealth.android.model.Immunization;
import com.viamhealth.android.provider.dal.ImmunizationDal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by kunal on 19/2/14.
 */
public class BabyGoalFragment extends BaseFragment {

    private WebView mWebview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baby_growth, container,false);

        mWebview = (WebView) view.findViewById(R.id.webViewBabyGrowth);

        init();

        return view;
    }


    private void init() {

        //load HTML File in webview
        mWebview.loadUrl("file:///android_asset/baby_growth.html");

        //add the JavaScriptInterface so that JavaScript is able to use BabyGrowthJavaScriptInterface's methods when calling "LocalStorage"
        mWebview.addJavascriptInterface(new BabyGrowthJavaScriptInterface(getActivity().getApplicationContext()), "LocalStorage");

        WebSettings webSettings = mWebview.getSettings();
        //enable JavaScript in webview
        webSettings.setJavaScriptEnabled(true);

        //Enable and setup JS localStorage
        webSettings.setDomStorageEnabled(true);
        //those two lines seem necessary to keep data that were stored even if the app was killed.
        webSettings.setDatabaseEnabled(true);
        webSettings.setDatabasePath(getActivity().getFilesDir().getParentFile().getPath()+"/databases/");
    }


    /**
     * This class is used as a substitution of the local storage in Android webviews
     *
     * @author Diane
     */
    private class BabyGrowthJavaScriptInterface {
        private Context mContext;
        private ImmunizationDal immunizationDal;
        //private LocalStorage localStorageDBHelper;
        //private SQLiteDatabase database;

        BabyGrowthJavaScriptInterface(Context c) {
            mContext = c;
            immunizationDal = new ImmunizationDal(c);
            immunizationDal.open();
            Immunization model = new Immunization();
            model.setId(1);
            model.setTitle("Hahahha");
            model.setRecommendedAge(28L);

            //immunizationDal.create(model);
            immunizationDal.close();
            //localStorageDBHelper = LocalStorage.getInstance(mContext);
        }

        @JavascriptInterface
        public String getImmunizationData(){
            immunizationDal.open();
            List<Immunization> data = immunizationDal.getAll(null,null);
            immunizationDal.close();
            JSONArray listData = new JSONArray();
            int j = 0;
            for(Immunization i : data){
                JSONObject object = new JSONObject();
                try {
                    object.put("id", i.getId());
                    object.put("title", i.getTitle());
                    object.put("recommendedAge", i.getRecommendedAge());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try{
                    listData.put(j,object);
                    j++;
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            return listData.toString();
        }

    }
}