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
import com.viamhealth.android.model.trackgrowth.TrackGrowthAdvData;
import com.viamhealth.android.model.trackgrowth.TrackGrowthData;
import com.viamhealth.android.model.trackgrowth.UserTrackGrowthData;
import com.viamhealth.android.model.users.User;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
        View view = inflater.inflate(R.layout.fragment_baby_growth, container, false);
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
        webSettings.setDatabasePath(getActivity().getFilesDir().getParentFile().getPath() + "/databases/");
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
        public void showToast(String message) {
            final String msg = message;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                }
            });
        }

        @JavascriptInterface
        public String getUserAgeInMonths() {
            int age = selectedUser.getProfile().getAgeInMonths();
            if (age == 0)
                Toast.makeText(mContext, "Age not specified", Toast.LENGTH_LONG);
            return String.valueOf(age);
        }

        @JavascriptInterface
        public String getPercentileData(String sdate, String height, String weight) {
            UserTrackGrowthEP ep = new UserTrackGrowthEP(mContext, ((Global_Application) mContext.getApplicationContext()));
            Date ddate;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                ddate = df.parse(sdate);
            } catch (ParseException e) {
                ddate = new Date();
            }
            TrackGrowthAdvData obj = ep.getPercentileData(selectedUser.getProfile().getGender().key(), selectedUser.getProfile().getAgeInDays(ddate));
            Float fheight = Float.valueOf(height);
            Float fweight = Float.valueOf(weight);
            Float pl = 0F;
            Float ph = 0F;
            Float hl = 0F;
            Float hh = 0F;
            Float heightPercentile = 0F;
            Float weightPercentile = 0F;
            JSONObject tgdObject = new JSONObject();
            if (obj == null) {
                try {
                    tgdObject.put("error", "Could not fetch the details.Please try again later.");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (fheight < obj.getHeight_3n()) {
                    ph = obj.getPercentile_3n();
                    pl = 0F;
                    hh = obj.getHeight_3n();
                    hl = 0F;
                } else if (fheight < obj.getHeight_2n()) {
                    ph = obj.getPercentile_2n();
                    pl = obj.getPercentile_3n();
                    hh = obj.getHeight_2n();
                    hl = obj.getHeight_3n();
                } else if (fheight < obj.getHeight_1n()) {
                    ph = obj.getPercentile_1n();
                    pl = obj.getPercentile_2n();
                    hh = obj.getHeight_1n();
                    hl = obj.getHeight_2n();
                } else if (fheight < obj.getHeight_0()) {
                    ph = obj.getPercentile_0();
                    pl = obj.getPercentile_1n();
                    hh = obj.getHeight_0();
                    hl = obj.getHeight_1n();
                } else if (fheight < obj.getHeight_1()) {
                    ph = obj.getPercentile_1();
                    pl = obj.getPercentile_0();
                    hh = obj.getHeight_1();
                    hl = obj.getHeight_0();
                } else if (fheight < obj.getHeight_2()) {
                    ph = obj.getPercentile_2();
                    pl = obj.getPercentile_1();
                    hh = obj.getHeight_2();
                    hl = obj.getHeight_1();
                } else if (fheight < obj.getHeight_3()) {
                    ph = obj.getPercentile_3();
                    pl = obj.getPercentile_2();
                    hh = obj.getHeight_3();
                    hl = obj.getHeight_2();
                } else {
                    ph = 100F;
                    pl = obj.getPercentile_3n();
                    hh = obj.getHeight_3n() + 1F;//Random. as No value available
                    hl = obj.getHeight_3n();
                }


                heightPercentile = ((ph - pl) / (hh - hl)) * (fheight - hl) + pl;

                ph = 0F;
                pl = 0F;
                hh = 0F;
                hl = 0F;

                if (fweight < obj.getWeight_3n()) {
                    ph = obj.getPercentile_3n();
                    pl = 0F;
                    hh = obj.getWeight_3n();
                    hl = 0F;
                } else if (fweight < obj.getWeight_2n()) {
                    ph = obj.getPercentile_2n();
                    pl = obj.getPercentile_3n();
                    hh = obj.getWeight_2n();
                    hl = obj.getWeight_3n();
                } else if (fweight < obj.getWeight_1n()) {
                    ph = obj.getPercentile_1n();
                    pl = obj.getPercentile_2n();
                    hh = obj.getWeight_1n();
                    hl = obj.getWeight_2n();
                } else if (fweight < obj.getWeight_0()) {
                    ph = obj.getPercentile_0();
                    pl = obj.getPercentile_1n();
                    hh = obj.getWeight_0();
                    hl = obj.getWeight_1n();
                } else if (fweight < obj.getWeight_1()) {
                    ph = obj.getPercentile_1();
                    pl = obj.getPercentile_0();
                    hh = obj.getWeight_1();
                    hl = obj.getWeight_0();
                } else if (fweight < obj.getWeight_2()) {
                    ph = obj.getPercentile_2();
                    pl = obj.getPercentile_1();
                    hh = obj.getWeight_2();
                    hl = obj.getWeight_1();
                } else if (fweight < obj.getWeight_3()) {
                    ph = obj.getPercentile_3();
                    pl = obj.getPercentile_2();
                    hh = obj.getWeight_3();
                    hl = obj.getWeight_2();
                } else {
                    ph = 100F;
                    pl = obj.getPercentile_3();
                    hh = obj.getWeight_3() + 1F;//Random. as No value available
                    hl = obj.getWeight_3();
                }


                weightPercentile = ((ph - pl) / (hh - hl)) * (fweight - hl) + pl;
                try {
                    tgdObject.put("error", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (heightPercentile > 100) heightPercentile = 100F;
            else if (heightPercentile < 0) heightPercentile = 0F;
            if (weightPercentile > 100) weightPercentile = 100F;
            else if (weightPercentile < 0) weightPercentile = 0F;


            try {
                tgdObject.put("heightPercentile", String.valueOf(Math.round(heightPercentile * 100.0) / 100.0));
                tgdObject.put("weightPercentile", String.valueOf(Math.round(weightPercentile * 100.0) / 100.0));
                tgdObject.put("weight", weight);
                tgdObject.put("height", height);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return tgdObject.toString();
        }

        @JavascriptInterface
        public void updateUserTrackData(String sdate, String sheight, String sweight) {
            DateTime edate;
            if (sdate == null || sdate.trim().isEmpty()) {
                edate = new DateTime();
            } else {
                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-mm-dd");
                edate = formatter.parseDateTime(sdate);
            }

            UserTrackGrowthData obj = new UserTrackGrowthData();
            obj.setUserId(selectedUser.getId());
            obj.setEntryDate(edate.toDate());
            obj.setWeight(Float.valueOf(sweight));
            obj.setHeight(Float.valueOf(sheight));

            UserTrackGrowthEP ep = new UserTrackGrowthEP(mContext, ((Global_Application) mContext.getApplicationContext()));
            ep.update(obj);

        }

        @JavascriptInterface
        public String getGrowthChartData() {

            if (trackGrowthDataUpdated == true) {
                UserTrackGrowthEP ep = new UserTrackGrowthEP(mContext, ((Global_Application) mContext.getApplicationContext()));
                trackGrowthMap = ep.list(selectedUser.getId());
            }


            JSONObject object = new JSONObject();
            try {
                List<TrackGrowth> trackGrowthList = trackGrowthMap.get("track_growth");
                JSONArray trackGrowthDataJsonList = new JSONArray();
                for (TrackGrowth trackGrowth : trackGrowthList) {
                    TrackGrowthData trackGrowthData = (TrackGrowthData) trackGrowth;
                    JSONObject tgdObject = new JSONObject();
                    tgdObject.put("id", trackGrowthData.getId());
                    tgdObject.put("label", trackGrowthData.getLabel());
                    tgdObject.put("age", trackGrowthData.getAge());
                    tgdObject.put("height", trackGrowthData.getHeight());
                    tgdObject.put("weight", trackGrowthData.getWeight());
                    trackGrowthDataJsonList.put(tgdObject);
                }

                List<TrackGrowth> trackGrowthList1 = trackGrowthMap.get("user_track_growth");
                JSONArray userTrackGrowthDataJsonList = new JSONArray();
                for (TrackGrowth trackGrowth : trackGrowthList1) {
                    UserTrackGrowthData userTrackGrowthData = (UserTrackGrowthData) trackGrowth;
                    JSONObject tgdObject = new JSONObject();
                    tgdObject.put("id", userTrackGrowthData.getId());
                    tgdObject.put("user", userTrackGrowthData.getUserId());
                    tgdObject.put("age", userTrackGrowthData.getAge());
                    tgdObject.put("height", userTrackGrowthData.getHeight());
                    tgdObject.put("weight", userTrackGrowthData.getWeight());
                    tgdObject.put("entry_date", userTrackGrowthData.getEntryDate());
                    userTrackGrowthDataJsonList.put(tgdObject);
                }
                object.put("track_growth", trackGrowthDataJsonList);
                object.put("user_track_growth", userTrackGrowthDataJsonList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //System.out.println(object.toString());
            return object.toString();
        }

        @JavascriptInterface
        public String getImmunizationData() {
            List<Immunization> data = new ArrayList<Immunization>();
            if (immunization_data_updated == false) {
                for (Map.Entry<Long, Immunization> entry : immunizationMap.entrySet()) {
                    data.add(entry.getValue());
                }
            } else {
                ImmunizationEP iep = new ImmunizationEP(mContext, ((Global_Application) mContext.getApplicationContext()));
                data = iep.list(selectedUser.getId());
                for (Immunization entry : data) {
                    immunizationMap.put(entry.getId(), entry);
                }
                immunization_data_updated = false;
            }
            Collections.sort(data, new Comparator<Immunization>() {
                @Override
                public int compare(Immunization fruite1, Immunization fruite2) {
                    return (int) fruite1.getRecommendedAge() - (int) fruite2.getRecommendedAge();
                }
            });
            JSONArray listData = new JSONArray();
            int j = 0;
            for (Immunization i : data) {
                JSONObject object = new JSONObject();
                try {
                    object.put("immunization_id", i.getId());
                    object.put("title", i.getLabel());
                    object.put("recommended_age", i.getRecommendedAge());
                    if (i.scheduleDate(selectedUser.getProfile().getDob()) != null) {
                        object.put("schedule_date_string", i.scheduleDate(selectedUser.getProfile().getDob()));
                        object.put("header_string", i.scheduleTimeFrame(selectedUser.getProfile().getDob()));
                        object.put("list_item_type", i.getListItemType(selectedUser.getProfile().getDob()));
                    } else {
                        object.put("schedule_date_string", "");
                        object.put("header_string", "");
                        object.put("list_item_type", "0");
                    }

                    if (i.getUserImmunization() != null) {
                        object.put("user", i.getUserImmunization().getUserId());
                        object.put("is_completed", i.getUserImmunization().isCompleted());
                        object.put("user_immunization_id", i.getUserImmunization().getId());
                    } else {
                        object.put("user", null);
                        object.put("is_completed", null);
                        object.put("user_immunization_id", null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    listData.put(j, object);
                    j++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return listData.toString();
        }

        @JavascriptInterface
        public boolean updateIsCompleted(String immunization_id_string, String is_completed) {
            Long user_immunization_id = 0L;
            Long immunization_id = Long.parseLong(immunization_id_string);

            ImmunizationEP iep = new ImmunizationEP(mContext, ((Global_Application) mContext.getApplicationContext()));

            Immunization old_immunization_object = immunizationMap.get(immunization_id);

            if (old_immunization_object == null) {
                //Something went wrong, or race condition achieved
                System.out.println("Race condition for updating immunization object ? ");
                return false;
            }

            if (old_immunization_object.getUserImmunization() != null && old_immunization_object.getUserImmunization().getId() != null) {
                user_immunization_id = old_immunization_object.getUserImmunization().getId();
            }

            if (user_immunization_id == 0L) {
                UserImmunization obj = new UserImmunization();
                obj.setCompleted(true);
                obj.setUserId(this.selectedUser.getId());
                obj.setImmunization(immunization_id);
                try {
                    UserImmunization ui = iep.create(obj);
                    immunization_data_updated = true;
                    old_immunization_object.setUserImmunization(ui);
                    immunizationMap.put(immunization_id, old_immunization_object);
                } catch (Exception e) {
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
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }

    }
}