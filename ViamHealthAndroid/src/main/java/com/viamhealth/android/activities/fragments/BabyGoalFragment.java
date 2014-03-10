package com.viamhealth.android.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.dao.rest.endpoints.ImmunizationEP;
import com.viamhealth.android.dao.rest.endpoints.UserTrackGrowthEP;
import com.viamhealth.android.model.immunization.Immunization;
import com.viamhealth.android.model.immunization.UserImmunization;
import com.viamhealth.android.model.trackgrowth.TrackGrowth;
import com.viamhealth.android.model.trackgrowth.TrackGrowthData;
import com.viamhealth.android.model.trackgrowth.UserTrackGrowthData;
import com.viamhealth.android.model.users.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kunal on 19/2/14.
 */
public class BabyGoalFragment extends BaseFragment {

    public WebView mWebview;
    private User selectedUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedUser = getArguments().getParcelable("user");
    }

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
        mWebview.loadUrl("file:///android_asset/www/baby_growth.html");

        //add the JavaScriptInterface so that JavaScript is able to use BabyGrowthJavaScriptInterface's methods when calling "LocalStorage"
        mWebview.addJavascriptInterface(new BabyGrowthJavaScriptInterface(getActivity().getApplicationContext(), selectedUser), "BabyGrowthStorage");

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
        private User selectedUser;

        //Sudo Sync
        private Map<Long, Immunization> immunizationMap = new HashMap<Long, Immunization>();
        private boolean immunization_data_updated = true;
        private Map<String, List<TrackGrowth>> trackGrowthMap = new HashMap<String, List<TrackGrowth>>();
        private boolean trackGrowthDataUpdated = true;

        BabyGrowthJavaScriptInterface(Context c, User selectedUser) {
            mContext = c;
            this.selectedUser = selectedUser;
        }
        @JavascriptInterface
        public void showToast(String message){
            final String msg = message;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext,msg, Toast.LENGTH_LONG).show();
                }
            });
        }
        @JavascriptInterface
        public String getUserAgeInMonths(){
            int age = selectedUser.getProfile().getAgeInMonths();
            if(age == 0)
                Toast.makeText(mContext,"Age not specified",Toast.LENGTH_LONG);
            return String.valueOf(age);
        }

        @JavascriptInterface
        public String getGrowthChartData(){

            if(trackGrowthDataUpdated == true){
                UserTrackGrowthEP ep = new UserTrackGrowthEP(mContext, ((Global_Application) mContext.getApplicationContext()));
                trackGrowthMap = ep.list(selectedUser.getId());
            }


            JSONObject object = new JSONObject();
            try {
                List<TrackGrowth> trackGrowthList = trackGrowthMap.get("track_growth");
                JSONArray trackGrowthDataJsonList = new JSONArray();
                for( TrackGrowth trackGrowth : trackGrowthList){
                    TrackGrowthData trackGrowthData = (TrackGrowthData) trackGrowth;
                    JSONObject tgdObject = new JSONObject();
                    tgdObject.put("id",trackGrowthData.getId());
                    tgdObject.put("label",trackGrowthData.getLabel());
                    tgdObject.put("age",trackGrowthData.getAge());
                    tgdObject.put("height",trackGrowthData.getHeight());
                    tgdObject.put("weight",trackGrowthData.getWeight());
                    trackGrowthDataJsonList.put(tgdObject);
                }

                List<TrackGrowth> trackGrowthList1 = trackGrowthMap.get("user_track_growth");
                JSONArray userTrackGrowthDataJsonList = new JSONArray();
                for( TrackGrowth trackGrowth : trackGrowthList1){
                    UserTrackGrowthData userTrackGrowthData = (UserTrackGrowthData) trackGrowth;
                    JSONObject tgdObject = new JSONObject();
                    tgdObject.put("id",userTrackGrowthData.getId());
                    tgdObject.put("user",userTrackGrowthData.getUserId());
                    tgdObject.put("age",userTrackGrowthData.getAge());
                    tgdObject.put("height",userTrackGrowthData.getHeight());
                    tgdObject.put("weight",userTrackGrowthData.getWeight());
                    tgdObject.put("entry_date",userTrackGrowthData.getEntryDate());
                    userTrackGrowthDataJsonList.put(tgdObject);
                }
                object.put("track_growth", trackGrowthDataJsonList);
                object.put("user_track_growth", userTrackGrowthDataJsonList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(object.toString());
            return object.toString();
        }

        @JavascriptInterface
        public String getImmunizationData(){
            List<Immunization>  data = new ArrayList<Immunization>();
            if(immunization_data_updated == false ){
                for (Map.Entry<Long, Immunization> entry : immunizationMap.entrySet()){
                    data.add(entry.getValue());
                }
            } else {
                ImmunizationEP iep = new ImmunizationEP(mContext, ((Global_Application) mContext.getApplicationContext()));
                data  = iep.list(selectedUser.getId());
                for(Immunization entry : data ){
                    immunizationMap.put(entry.getId(), entry);
                }
                immunization_data_updated = false;
            }

            JSONArray listData = new JSONArray();
            int j = 0;
            for(Immunization i : data){
                JSONObject object = new JSONObject();
                try {
                    object.put("immunization_id", i.getId());
                    object.put("title", i.getLabel());
                    object.put("recommended_age", i.getRecommendedAge());
                    if(i.getUserImmunization() != null){
                        object.put("user", i.getUserImmunization().getUserId());
                        object.put("is_completed", i.getUserImmunization().isCompleted());
                        object.put("user_immunization_id", i.getUserImmunization().getId());
                    }
                    else{
                        object.put("user", null);
                        object.put("is_completed", null);
                        object.put("user_immunization_id", null);
                    }
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

        @JavascriptInterface
        public boolean updateIsCompleted( String immunization_id_string, String is_completed){
            Long user_immunization_id = 0L;
            Long immunization_id = Long.parseLong(immunization_id_string);

            ImmunizationEP iep = new ImmunizationEP(mContext, ((Global_Application) mContext.getApplicationContext()));

            Immunization old_immunization_object = immunizationMap.get(immunization_id);

            if(old_immunization_object == null){
                //Something went wrong, or race condition achieved
                System.out.println("Race condition for updating immunization object ? ");
                return false;
            }

            if(old_immunization_object.getUserImmunization() != null && old_immunization_object.getUserImmunization().getId() != null ){
                user_immunization_id = old_immunization_object.getUserImmunization().getId();
            }

            if(user_immunization_id == 0L){
                UserImmunization obj = new UserImmunization();
                obj.setCompleted(true);
                obj.setUserId(this.selectedUser.getId());
                obj.setImmunization(immunization_id);
                try {
                    UserImmunization ui = iep.create(obj);
                    immunization_data_updated = true;
                    old_immunization_object.setUserImmunization(ui);
                    immunizationMap.put(immunization_id, old_immunization_object);
                } catch(Exception e){
                    e.printStackTrace();
                    return false;
                }
            } else {
                UserImmunization obj = new UserImmunization();
                obj.setId(user_immunization_id);
                obj.setCompleted(Boolean.parseBoolean(is_completed));
                obj.setUserId(this.selectedUser.getId());
                obj.setImmunization(immunization_id);
                try {
                    UserImmunization ui = iep.update(obj);
                    immunization_data_updated = true;
                    old_immunization_object.setUserImmunization(ui);
                    immunizationMap.put(immunization_id, old_immunization_object);
                } catch(Exception e){
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }

    }
}