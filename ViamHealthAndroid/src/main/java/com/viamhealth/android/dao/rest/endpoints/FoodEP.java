package com.viamhealth.android.dao.rest.endpoints;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.FoodBase;
import com.viamhealth.android.model.enums.FoodType;
import com.viamhealth.android.model.goals.Goal;

import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by naren on 31/10/13.
 */
public class FoodEP {//extends BaseEP {

/*    final Context mContext;
    final Global_Application mApplication;

    public FoodEP(Context context, Application app) {
        super(context, app);
        mContext = context;
        mApplication = app;
    }

    private RestClient getRestClient(Long userId, FoodType type) {
        String baseurlString = Global_Application.url+ "
        diet-tracker" + "/?user=" + userId + "&page_size=1000";

        if(type!=null && type!=FoodType.None){
            baseurlString += "&meal_type=" + type.key();
        }

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+ appPrefs.getToken());

        return client;
    }


    private Map<FoodType, List<FoodBase>> getAllFoodItemsForADay(Long userId, Date date) {
        RestClient client = getRestClient(userId, FoodType.None);

        try {
            client.Execute(RequestMethod.GET);
        }catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        Log.i(TAG, client.toString());

        List<FoodBase> foodItems = null;
        Map<FoodType, List<FoodBase>> foodItemsMap = new HashMap<FoodType, List<FoodBase>>();
        List<FoodBase> breakfast = null;
        List<FoodBase> lunch = null;
        List<FoodBase> snacks = null;
        List<FoodBase> dinner = null;

        if(client.getResponseCode()== HttpStatus.SC_OK)
            foodItems = processGoalsResponse(responseString);

        if(foodItems!=null){
            int fic = foodItems.size();
            for(int i=0; i<)
        }
        return foodItemsMap;

    }
*/

}
