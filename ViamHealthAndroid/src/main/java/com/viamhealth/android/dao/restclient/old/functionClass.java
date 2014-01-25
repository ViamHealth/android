package com.viamhealth.android.dao.restclient.old;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.viamhealth.android.dao.db.JournalContentProvider;
import com.viamhealth.android.dao.db.JournalDietTracker;
import com.viamhealth.android.dao.db.JournalFoodItems;
import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.model.BPData;
import com.viamhealth.android.model.CategoryExercise;
import com.viamhealth.android.model.CategoryFood;
import com.viamhealth.android.model.CholesterolData;
import com.viamhealth.android.model.FileData;
import com.viamhealth.android.model.FoodData;
import com.viamhealth.android.model.GlucoseData;
import com.viamhealth.android.model.GoalData;
import com.viamhealth.android.model.MedicalData;
import com.viamhealth.android.model.MedicationData;
import com.viamhealth.android.model.WeightData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.model.cards.PickGoalPriorityCard;
import com.viamhealth.android.model.cards.PriorityCard;
import com.viamhealth.android.model.cards.PriorityCardFactory;
import com.viamhealth.android.model.enums.PriorityCardType;

public class functionClass {
	Context context;
	JSONObject jObject;
	JSONArray jarray;
	String responseString = null;
	 Global_Application ga;
	ViamHealthPrefs appPrefs;
	
	public functionClass(Context context) {
		super();
		this.context = context;
		appPrefs=new ViamHealthPrefs(context);
		
	}

	// seach food item
	
	
		public ArrayList<FoodData> SearchFoodItem(String searchKey, int pageNumber){
			ga=new Global_Application();
			ArrayList<FoodData> lstResult = new ArrayList<FoodData>();
			String responsetxt;
			String val="";
			if(searchKey.toString().equals("ALL")){
				val="";
			}else{
				val=searchKey;
			}
            try {
                val = URLEncoder.encode(val, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String baseurlString = Global_Application.url+"food-items/?page_size=1000&page="+pageNumber+"&search="+val;
			Log.e("FoodEP","url is : " + baseurlString);
			
			RestClient client = new RestClient(baseurlString.trim());   
			client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
		  
			try
			{
				client.Execute(RequestMethod.GET);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}

            Log.i("FoodEP", client.toString());
			responseString = client.getResponse();
			
				try {
				 jObject = new JSONObject(responseString);
				 Log.e("TAG","res : " + responseString);
				 ga.setNextfood(jObject.getString("next").toString());
				 ga.setPrevfood(jObject.getString("previous").toString());
				 Log.e("TAG",jObject.getString("next"));
				 jarray = jObject.getJSONArray("results");
					 for (int i = 0; i < jarray.length(); i++) {
						 JSONObject c = jarray.getJSONObject(i);
						 lstResult.add(new FoodData(c.getString("id").toString(),
								 c.getString("name").toString(),
								 c.getString("quantity").toString(),
								 c.getString("quantity_unit").toString(),
								 c.getString("calories").toString(), 
								 c.getString("total_fat").toString(), 
								 c.getString("saturated_fat").toString(),
								 c.getString("polyunsaturated_fat").toString(), 
								 c.getString("monounsaturated_fat").toString(), 
								 c.getString("trans_fat").toString(), 
								 c.getString("cholesterol").toString(), 
								 c.getString("sodium").toString(), 
								 c.getString("potassium").toString(), 
								 c.getString("total_carbohydrates").toString(), 
								 c.getString("dietary_fiber").toString(), 
								 c.getString("sugars").toString(), 
								 c.getString("protein").toString(), 
								 c.getString("vitamin_a").toString(), 
								 c.getString("vitamin_c").toString(), 
								 c.getString("calcium").toString(), 
								 c.getString("iron").toString(), 
								 c.getString("calories_unit").toString(), 
								 c.getString("total_fat_unit").toString(), 
								 c.getString("saturated_fat_unit").toString(), 
								 c.getString("polyunsaturated_fat_unit").toString(),
								 c.getString("monounsaturated_fat_unit").toString(), 
								 c.getString("trans_fat_unit").toString(), 
								 c.getString("cholesterol_unit").toString(),
								 c.getString("sodium_unit").toString(), 
								 c.getString("potassium_unit").toString(),
								 c.getString("total_carbohydrates_unit").toString(),
								 c.getString("dietary_fiber_unit").toString(), 
								 c.getString("sugars_unit").toString(), 
								 c.getString("protein_unit").toString(), 
								 c.getString("vitamin_a_unit").toString(), 
								 c.getString("vitamin_c_unit").toString(), 
								 c.getString("calcium_unit").toString(), c.getString("iron_unit").toString()));
					 }
					 Log.e("TAG","Data is " + appPrefs.getMenuList().toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return lstResult;
		}
		public ArrayList<FoodData> FoodNevigation(String baseurlString){
			ga=new Global_Application();
			ArrayList<FoodData> lstResult = new ArrayList<FoodData>();
			
			RestClient client = new RestClient(baseurlString);   
			client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
		  
			try
			{
				client.Execute(RequestMethod.GET);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}

			responseString = client.getResponse();
			
				try {
				 jObject = new JSONObject(responseString);
				 Log.e("TAG","res : " + responseString);
				 Global_Application.nextfood=jObject.getString("next").toString();
				 Global_Application.prevfood =jObject.getString("previous").toString();
				 Log.e("TAG",jObject.getString("next"));
				 jarray = jObject.getJSONArray("results");
					 for (int i = 0; i < jarray.length(); i++) {
						 JSONObject c = jarray.getJSONObject(i);
						 lstResult.add(new FoodData(c.getString("id").toString(),
                                 c.getString("name").toString(),
                                 c.getString("quantity").toString(),
                                 c.getString("quantity_unit").toString(),
                                 c.getString("calories").toString(),
                                 c.getString("total_fat").toString(),
                                 c.getString("saturated_fat").toString(),
                                 c.getString("polyunsaturated_fat").toString(),
                                 c.getString("monounsaturated_fat").toString(),
                                 c.getString("trans_fat").toString(),
                                 c.getString("cholesterol").toString(),
                                 c.getString("sodium").toString(),
                                 c.getString("potassium").toString(),
                                 c.getString("total_carbohydrates").toString(),
                                 c.getString("dietary_fiber").toString(),
                                 c.getString("sugars").toString(),
                                 c.getString("protein").toString(),
                                 c.getString("vitamin_a").toString(),
                                 c.getString("vitamin_c").toString(),
                                 c.getString("calcium").toString(),
                                 c.getString("iron").toString(),
                                 c.getString("calories_unit").toString(),
                                 c.getString("total_fat_unit").toString(),
                                 c.getString("saturated_fat_unit").toString(),
                                 c.getString("polyunsaturated_fat_unit").toString(),
                                 c.getString("monounsaturated_fat_unit").toString(),
                                 c.getString("trans_fat_unit").toString(),
                                 c.getString("cholesterol_unit").toString(),
                                 c.getString("sodium_unit").toString(),
                                 c.getString("potassium_unit").toString(),
                                 c.getString("total_carbohydrates_unit").toString(),
                                 c.getString("dietary_fiber_unit").toString(),
                                 c.getString("sugars_unit").toString(),
                                 c.getString("protein_unit").toString(),
                                 c.getString("vitamin_a_unit").toString(),
                                 c.getString("vitamin_c_unit").toString(),
                                 c.getString("calcium_unit").toString(), c.getString("iron_unit").toString()));
					 }
					 Log.e("TAG","Data is " + appPrefs.getMenuList().toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return lstResult;
		}


        public ArrayList<CategoryExercise> getExercise(String user,String date)
        {
            ArrayList<CategoryExercise>	lstData = new ArrayList<CategoryExercise>();
            String baseurlString = Global_Application.url+"user-physical-activity/?user="+user+"&activity_date="+date;
            Log.e("TAG"," get Exercise url is : " + baseurlString);

            RestClient client = new RestClient(baseurlString);
            client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

            try
            {
                client.Execute(RequestMethod.GET);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            responseString = client.getResponse();
            Log.e("TAG","Response string " + responseString);

            try {
                jObject = new JSONObject(responseString);
                Log.e("TAG","res : " + responseString);

                jarray = jObject.getJSONArray("results");
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject c = jarray.getJSONObject(i);
                    lstData.add(new CategoryExercise(c.getString("id"),c.getJSONObject("physical_activity").getString("label"),c.getString("time_spent"),c.getString("calories_spent"),c.getJSONObject("physical_activity").getString("id")));
                }
                Log.e("TAG","Data is " + appPrefs.getMenuList().toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return lstData;

        }


    
		public ArrayList<CategoryFood> FoodListing(String baseurlString,String date,String user){
			ArrayList<CategoryFood> lstResult = new ArrayList<CategoryFood>();
            baseurlString+="&user="+user+"&diet_date="+date;
			RestClient client = new RestClient(baseurlString);
			client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
            Log.e("TAG","baseurlString food listing: " + baseurlString);
            //client.AddParam("diet_date",date);
		  
			try
			{
				client.Execute(RequestMethod.GET);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}

			responseString = client.getResponse();
            Log.e("TAG","Food Listing responseString : " + responseString);
			
				try {
				 jObject = new JSONObject(responseString);
				 Log.e("TAG","res : " + responseString);
				 Log.e("TAG",jObject.getString("next"));
				 if(baseurlString.contains("BREAKFAST")){
					 Global_Application.nextbrekfast = jObject.getString("next").toString();
				 }else if(baseurlString.contains("LUNCH")){
					 Global_Application.nextlunch = jObject.getString("next").toString();
				 }else if(baseurlString.contains("SNACKS")){
					 Global_Application.nextsnacks = jObject.getString("next").toString();
				 }else if(baseurlString.contains("DINNER")){
					 Global_Application.nextdinner = jObject.getString("next").toString();
				 }
				 jarray = jObject.getJSONArray("results");
					 for (int i = 0; i < jarray.length(); i++) {
						 JSONObject c = jarray.getJSONObject(i);
						 Log.e("TAG","Multiplier : " + c.getString("food_quantity_multiplier"));
                         float multiplier=Float.valueOf(c.getString("food_quantity_multiplier"));
						 String baseurlString1 = Global_Application.url+"food-items/"+c.getString("food_item")+"/";  
						 Log.e("TAG","inner url : " + baseurlString1);
							RestClient client1 = new RestClient(baseurlString1);   
							client1.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
						  
							try
							{
								client1.Execute(RequestMethod.GET);
							} 
							catch (Exception e) {
								e.printStackTrace();
							}
   
							String responseString1 = client1.getResponse();
							
								try {
								JSONObject jObject1 = new JSONObject(responseString1);
								 Log.e("TAG","res : " + responseString1);
								 Log.e("TAG","Calories : "  + jObject1.getString("calories"));
								 Global_Application.totalcal+=jObject1.getDouble("calories")*multiplier;
                                 Log.e("TAG","total_calories : " + Global_Application.totalcal);
										 lstResult.add(new CategoryFood(c.getString("id"),c.getString("food_item"),jObject1.getString("name"), jObject1.getString("calories"), c.getString("food_quantity_multiplier"),jObject1.getString("quantity"),jObject.getString("count"),jObject1.getString("total_fat"),jObject1.getString("cholesterol"),jObject1.getString("sugars"),jObject1.getString("quantity_unit")));
								
								}catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					 }
					Log.e("TAG","Data is " + appPrefs.getMenuList().toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			return lstResult;
		}


    public String EditFood(String id,String food_item,String food_quantities,String meal_type,String user){
        String baseurlString = Global_Application.url+"diet-tracker/"+id+"/?user="+user;
        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
        Log.e("TAG","Edit Food : " + baseurlString);
        client.AddParam("food_item", food_item);
        client.AddParam("meal_type", meal_type);
        client.AddParam("food_quantity_multiplier", food_quantities);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        client.AddParam("diet_date",sdf.format(date));


        try
        {
            client.Execute(RequestMethod.PUT);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.e("TAG","Edit Food Response : " + responseString);

        return responseString;
    }


    public String EditExercise(String id,String weight,String user_calories,String time_spent,String user,String value){
        String baseurlString = Global_Application.url+"user-physical-activity/"+id+"/?user="+user;
        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
        Log.e("TAG","Edit Exercise : " + baseurlString);
        client.AddParam("weight", weight);
        client.AddParam("time_spent", time_spent);
        client.AddParam("physical_activity", value);
        client.AddParam("user_calories_spent", user_calories);

        try
        {
            client.Execute(RequestMethod.PUT);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.e("TAG","Edit Exercise Response : " + responseString);

        return responseString;
    }





		//delete food
				public String DeleteFood(String sub_url,String id,String user){
					String baseurlString = Global_Application.url+sub_url+id+"/?user="+user;
					RestClient client = new RestClient(baseurlString);   
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
                    Log.e("TAG","Delete Food : " + baseurlString);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    client.AddParam("diet_date",sdf.format(date));
				  
					try
					{
						client.Execute(RequestMethod.DELETE);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
                    Log.e("TAG","Delete Food Response : " + responseString);
					
					return responseString;
				}
	// function for add food

    public String uploadFile(String description,String file){
        String responce="1";
        String baseurlString = Global_Application.url+"healthfiles/";
        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
        client.AddParam("description",description);
        client.AddParam("file", file);

        try
        {
            client.Execute(RequestMethod.POST);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.e("TAG","Response : " + responseString);
        return responce;
    }


        public String AddFoodItem(String id,String mealtype,String food_quantity_multiplier,String userid,String date)
        {
            String responce="1";
            String baseurlString = Global_Application.url+"diet-tracker/"+"?user="+userid;
            RestClient client = new RestClient(baseurlString);
            client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
            client.AddParam("food_item", id);
            client.AddParam("meal_type", mealtype);
            client.AddParam("food_quantity_multiplier", food_quantity_multiplier);
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //Date date = new Date();
            client.AddParam("diet_date",date);

            Log.e("TAG","url for add food : " + baseurlString);

            try
            {
                client.Execute(RequestMethod.POST);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            responseString = client.getResponse();
            Log.e("TAG","Response for add food : " + responseString);
            try {
                jObject = new JSONObject(responseString);
                if(responseString.length()>0){
                    responce="0";
                }
                Log.e("TAG","Data is " + appPrefs.getMenuList().toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ContentValues values = new ContentValues();
            values.put(JournalDietTracker.USER_ID,userid);
            values.put(JournalDietTracker.FOOD_ITEM_ID,id);
            values.put(JournalDietTracker.FOOD_QUANTITY_MULTIPLIER,food_quantity_multiplier);
            values.put(JournalDietTracker.MEAL_TYPE,mealtype);
            values.put(JournalDietTracker.DIET_DATE,date);
            values.put(JournalDietTracker.CREATED_AT,"");
            values.put(JournalDietTracker.UPDATED_AT,"");
            values.put(JournalDietTracker.UPDATED_BY,"");

            Uri addFood=context.getContentResolver().insert(JournalContentProvider.CONTENT_URI_TRACKER,values);

            //String[] projection = { TodoTable.COLUMN_SUMMARY,
                   // TodoTable.COLUMN_DESCRIPTION, TodoTable.COLUMN_CATEGORY };

            Cursor cursor = context.getContentResolver().query(JournalContentProvider.CONTENT_URI_ITEMS,null,JournalFoodItems.COLUMN_ID+"="+id,null,null);
            //check if the food item s there in the items table
            if(cursor!=null)
            {
                String baseurlString1 = Global_Application.url+"food-items/"+id+"/";
                Log.e("TAG","inner url add food item : " + baseurlString1);
                RestClient client1 = new RestClient(baseurlString1);
                client1.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

                try
                {
                    client1.Execute(RequestMethod.GET);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                String responseString1 = client1.getResponse();

                try {
                    JSONObject jObject1 = new JSONObject(responseString1);
                    ContentValues values_items = new ContentValues();
                    values_items.put(JournalFoodItems.COLUMN_NAME,jObject1.getString(JournalFoodItems.COLUMN_NAME));
                    values_items.put(JournalFoodItems.COLUMN_DISPLAY_IMAGE,jObject1.getString(JournalFoodItems.COLUMN_DISPLAY_IMAGE));
                    values_items.put(JournalFoodItems.COLUMN_QUANTITY,jObject1.getString(JournalFoodItems.COLUMN_QUANTITY));
                    values_items.put(JournalFoodItems.COLUMN_QUANTITY_UNIT,jObject1.getString(JournalFoodItems.COLUMN_QUANTITY_UNIT));
                    values_items.put(JournalFoodItems.COLUMN_CALORIES,jObject1.getString(JournalFoodItems.COLUMN_CALORIES));
                    values_items.put(JournalFoodItems.COLUMN_TOTAL_FAT,jObject1.getString(JournalFoodItems.COLUMN_TOTAL_FAT));
                    values_items.put(JournalFoodItems.COLUMN_SATURATED_FAT,jObject1.getString(JournalFoodItems.COLUMN_SATURATED_FAT));
                    values_items.put(JournalFoodItems.COLUMN_POLYSATURATED_FAT,jObject1.getString(JournalFoodItems.COLUMN_POLYSATURATED_FAT));
                    values_items.put(JournalFoodItems.COLUMN_MONOSATURATED_FAT,jObject1.getString(JournalFoodItems.COLUMN_MONOSATURATED_FAT));
                    values_items.put(JournalFoodItems.COLUMN_TRANS_FAT,jObject1.getString(JournalFoodItems.COLUMN_TRANS_FAT));
                    values_items.put(JournalFoodItems.COLUMN_CHOLESTEROL,jObject1.getString(JournalFoodItems.COLUMN_CHOLESTEROL));
                    values_items.put(JournalFoodItems.COLUMN_SODIUM,jObject1.getString(JournalFoodItems.COLUMN_SODIUM));
                    values_items.put(JournalFoodItems.COLUMN_POTASSIUM,jObject1.getString(JournalFoodItems.COLUMN_POTASSIUM));
                    values_items.put(JournalFoodItems.COLUMN_TOTAL_CARBOHYDRATES,jObject1.getString(JournalFoodItems.COLUMN_TOTAL_CARBOHYDRATES));
                    values_items.put(JournalFoodItems.COLUMN_DIETARY_FIBER,jObject1.getString(JournalFoodItems.COLUMN_DIETARY_FIBER));
                    values_items.put(JournalFoodItems.COLUMN_SUGARS,jObject1.getString(JournalFoodItems.COLUMN_SUGARS));
                    values_items.put(JournalFoodItems.COLUMN_PROTEIN,jObject1.getString(JournalFoodItems.COLUMN_PROTEIN));
                    values_items.put(JournalFoodItems.COLUMN_VITAMIN_A,jObject1.getString(JournalFoodItems.COLUMN_VITAMIN_A));
                    values_items.put(JournalFoodItems.COLUMN_VITAMIN_C,jObject1.getString(JournalFoodItems.COLUMN_VITAMIN_C));
                    values_items.put(JournalFoodItems.COLUMN_CALCIUM,jObject1.getString(JournalFoodItems.COLUMN_CALCIUM));
                    values_items.put(JournalFoodItems.COLUMN_IRON,jObject1.getString(JournalFoodItems.COLUMN_IRON));
                    values_items.put(JournalFoodItems.COLUMN_CALORIES_UNIT,jObject1.getString(JournalFoodItems.COLUMN_CALORIES_UNIT));
                    values_items.put(JournalFoodItems.COLUMN_TOTAL_FAT_UNIT,jObject1.getString(JournalFoodItems.COLUMN_TOTAL_FAT_UNIT));
                    values_items.put(JournalFoodItems.COLUMN_SATURATED_FAT_UNIT,jObject1.getString(JournalFoodItems.COLUMN_SATURATED_FAT_UNIT));
                    values_items.put(JournalFoodItems.COLUMN_POLYSATURATED_FAT_UNIT,jObject1.getString(JournalFoodItems.COLUMN_POLYSATURATED_FAT_UNIT));
                    values_items.put(JournalFoodItems.COLUMN_MONOSATURATED_FAT_UNIT,jObject1.getString(JournalFoodItems.COLUMN_MONOSATURATED_FAT_UNIT));
                    values_items.put(JournalFoodItems.COLUMN_TRANS_FAT_UNIT,jObject1.getString(JournalFoodItems.COLUMN_TRANS_FAT_UNIT));
                    values_items.put(JournalFoodItems.COLUMN_CHOLESTEROL_UNIT,jObject1.getString(JournalFoodItems.COLUMN_CHOLESTEROL_UNIT));
                    values_items.put(JournalFoodItems.COLUMN_SODIUM_UNIT,jObject1.getString(JournalFoodItems.COLUMN_SODIUM_UNIT));
                    values_items.put(JournalFoodItems.COLUMN_POTASSIUM_UNIT,jObject1.getString(JournalFoodItems.COLUMN_POTASSIUM_UNIT));
                    values_items.put(JournalFoodItems.COLUMN_TOTAL_CARBOHYDRATES_UNIT,jObject1.getString(JournalFoodItems.COLUMN_TOTAL_CARBOHYDRATES_UNIT));
                    values_items.put(JournalFoodItems.COLUMN_DIETARY_FIBER_UNIT,jObject1.getString(JournalFoodItems.COLUMN_DIETARY_FIBER_UNIT));
                    values_items.put(JournalFoodItems.COLUMN_SUGARS_UNIT,jObject1.getString(JournalFoodItems.COLUMN_SUGARS_UNIT));
                    values_items.put(JournalFoodItems.COLUMN_PROTEIN_UNIT,jObject1.getString(JournalFoodItems.COLUMN_PROTEIN_UNIT));
                    values_items.put(JournalFoodItems.COLUMN_VITAMIN_A_UNIT,jObject1.getString(JournalFoodItems.COLUMN_VITAMIN_A_UNIT));
                    values_items.put(JournalFoodItems.COLUMN_VITAMIN_C_UNIT,jObject1.getString(JournalFoodItems.COLUMN_VITAMIN_C_UNIT));
                    values_items.put(JournalFoodItems.COLUMN_CALCIUM_UNIT,jObject1.getString(JournalFoodItems.COLUMN_CALCIUM_UNIT));
                    values_items.put(JournalFoodItems.COLUMN_IRON_UNIT,jObject1.getString(JournalFoodItems.COLUMN_IRON_UNIT));
                    values_items.put(JournalFoodItems.COLUMN_CREATED_AT,jObject1.getString(JournalFoodItems.COLUMN_CREATED_AT));
                    values_items.put(JournalFoodItems.COLUMN_UPDATED_AT,jObject1.getString(JournalFoodItems.COLUMN_UPDATED_AT));
                    values_items.put(JournalFoodItems.COLUMN_UPDATED_BY,jObject1.getString(JournalFoodItems.COLUMN_UPDATED_BY));
                    values_items.put(JournalFoodItems.COLUMN_STATUS,jObject1.getString(JournalFoodItems.COLUMN_STATUS));

                    Uri addFood1=context.getContentResolver().insert(JournalContentProvider.CONTENT_URI_ITEMS,values_items);

                }catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return responce;
        }

		public String AddFood(String id,String mealtype,String food_quantity_multiplier,String userid,String date){
        String responce="1";
        String baseurlString = Global_Application.url+"diet-tracker/"+"?user="+userid;
        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
        client.AddParam("food_item", id);
        client.AddParam("meal_type", mealtype);
        client.AddParam("food_quantity_multiplier", food_quantity_multiplier);
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //Date date = new Date();
        client.AddParam("diet_date",date);

        Log.e("TAG","url for add food : " + baseurlString);

        try
        {
            client.Execute(RequestMethod.POST);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.e("TAG","Responce for add food : " + responseString);
        try {
            jObject = new JSONObject(responseString);
            if(responseString.length()>0){
                responce="0";
            }
            Log.e("TAG","Data is " + appPrefs.getMenuList().toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return responce;
    }




	// function for get goal data
	public WeightData getWeightGoal(){
		WeightData weight = new WeightData();
		String baseurlString = Global_Application.url+"weight-goals/?user="+appPrefs.getUserid();    
		Log.e("TAG","url is : " + baseurlString);
		
		RestClient client = new RestClient(baseurlString);   
		client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
	   
		try
		{
			client.Execute(RequestMethod.GET);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		responseString = client.getResponse();
		Log.e("TAG","Response string " + responseString);
		
			try {
				 jObject = new JSONObject(responseString);
				 jarray = jObject.getJSONArray("results");
				 JSONObject c = jarray.getJSONObject(jarray.length()-1);
				 weight.setId(c.getString("id"));
				 weight.setUser(c.getString("user"));
				 //for get readings
				 JSONArray array  = c.getJSONArray("readings");
				 ArrayList<String> lstReadings = new ArrayList<String>();
				 for(int i = 0;i<array.length();i++){   
					 JSONObject c1 = array.getJSONObject(i);
					 lstReadings.add(c1.getString("id") + "," + c1.getString("user_weight_goal")+"," + c1.getString("weight") +","+ c1.getString("reading_date"));
				 }
				 weight.setReadings(lstReadings);
				 weight.setWeight(c.getString("weight"));
				// weight.setWeight(c.getString("weight_measure"));
				 weight.setTarget_date(c.getString("target_date"));
				 weight.setInterval_num(c.getString("interval_num"));
				 weight.setInterval_unit(c.getString("interval_unit"));
				 
		   } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return weight;
	}
	
	// function for get goal data
		public CholesterolData getCholesterolGoal(){
			CholesterolData cholesterol = new CholesterolData();
			String baseurlString = Global_Application.url+"cholesterol-goals/?user="+appPrefs.getUserid();    
			Log.e("TAG","url is : " + baseurlString);
			
			RestClient client = new RestClient(baseurlString);   
			client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
		   
			try
			{
				client.Execute(RequestMethod.GET);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}

			responseString = client.getResponse();
			Log.e("TAG","Response string " + responseString);
			
				try {
					 jObject = new JSONObject(responseString);
					 jarray = jObject.getJSONArray("results");
					 JSONObject c = jarray.getJSONObject(jarray.length()-1);
					 cholesterol.setId(c.getString("id"));
					 cholesterol.setUser(c.getString("user"));
					 cholesterol.setHdl(c.getString("hdl"));
					 cholesterol.setLdl(c.getString("ldl"));
					 cholesterol.setTriglycerides(c.getString("triglycerides"));
					 cholesterol.setTotal_cholesterol(c.getString("total_cholesterol"));
					 //for get readings
					 JSONArray array  = c.getJSONArray("readings");
					 ArrayList<String> lstReadings = new ArrayList<String>();
					 for(int i = 0;i<array.length();i++){
						 JSONObject c1 = array.getJSONObject(i);
						 lstReadings.add(c1.getString("id") + "," + c1.getString("user_cholesterol_goal")+"," + c1.getString("hdl") +"," + c1.getString("ldl") +"," + c1.getString("triglycerides") +"," + c1.getString("total_cholesterol") +","+  c1.getString("reading_date"));
					 }
					 for(int i = 0;i<lstReadings.size();i++){
						 Log.e("TAG","lst reading : " +i + " ::: " + lstReadings.get(i));
					 }
					 cholesterol.setReadings(lstReadings);
					 cholesterol.setTarget_date(c.getString("target_date"));
					 cholesterol.setInterval_num(c.getString("interval_num"));
					 cholesterol.setInterval_unit(c.getString("interval_unit"));
					 
			   } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return cholesterol;
		}
		
		// function for get goal data
	public GlucoseData getGlucoseGoal(){
		GlucoseData gluscose = new GlucoseData();
		String baseurlString = Global_Application.url+"glucose-goals/?user="+appPrefs.getUserid();    
		Log.e("TAG","url is : " + baseurlString);
					
		RestClient client = new RestClient(baseurlString);   
		client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
				   
		try
			{
				client.Execute(RequestMethod.GET);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}

			responseString = client.getResponse();
			Log.e("TAG","Response string " + responseString);
					
			try {
				jObject = new JSONObject(responseString);
				jarray = jObject.getJSONArray("results");
				JSONObject c = jarray.getJSONObject(jarray.length()-1);
				gluscose.setId(c.getString("id"));
				gluscose.setUser(c.getString("user"));
				
				//for get readings
				JSONArray array  = c.getJSONArray("readings");
				ArrayList<String> lstReadings = new ArrayList<String>();
				for(int i = 0;i<array.length();i++){
					JSONObject c1 = array.getJSONObject(i);
						lstReadings.add(c1.getString("id") + "," + c1.getString("user_glucose_goal")+"," + c1.getString("fasting") +"," + c1.getString("random")  +","+  c1.getString("reading_date"));
				}
				gluscose.setReadings(lstReadings);
				gluscose.setFasting(c.getString("fasting"));
				gluscose.setRandom(c.getString("random"));
				gluscose.setTarget_date(c.getString("target_date"));
				gluscose.setInterval_num(c.getString("interval_num"));
				gluscose.setInterval_unit(c.getString("interval_unit"));
							 
			} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
		return gluscose;
	}
	public BPData getBPGoal(){
		BPData bloodpresure = new BPData();
		String baseurlString = Global_Application.url+"blood-pressure-goals/?user="+appPrefs.getUserid();    
		Log.e("TAG","url is : " + baseurlString);
					
		RestClient client = new RestClient(baseurlString);   
		client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
				   
		try
			{
				client.Execute(RequestMethod.GET);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}

			responseString = client.getResponse();
			Log.e("TAG","Response string " + responseString);
					
			try {
				jObject = new JSONObject(responseString);
				jarray = jObject.getJSONArray("results");
				JSONObject c = jarray.getJSONObject(jarray.length()-1);
				bloodpresure.setId(c.getString("id"));
				bloodpresure.setUser(c.getString("user"));
				bloodpresure.setSystolic(c.getString("systolic_pressure"));
				bloodpresure.setDiastolic(c.getString("diastolic_pressure"));
				bloodpresure.setPulserate(c.getString("pulse_rate"));
				
				//for get readings
				JSONArray array  = c.getJSONArray("readings");
				ArrayList<String> lstReadings = new ArrayList<String>();
				for(int i = 0;i<array.length();i++){
					JSONObject c1 = array.getJSONObject(i);
					lstReadings.add(c1.getString("id") + "," + c1.getString("user_blood_pressure_goal")+"," + c1.getString("systolic_pressure") +"," + c1.getString("diastolic_pressure")  +","+ c1.getString("pulse_rate") +","+  c1.getString("reading_date"));
				}
				bloodpresure.setReadings(lstReadings);
				bloodpresure.setTarget_date(c.getString("target_date"));
				bloodpresure.setInterval_num(c.getString("interval_num"));
				bloodpresure.setInterval_unit(c.getString("interval_unit"));
							 
			} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
		return bloodpresure;
	}
				

		
	// function for add goal data
		public String addWeightGoal(String weight,String measure, String targetdate,String intervalnum,String intervalunit){
			String resstr="1";
			ArrayList<GoalData>	lstData = new ArrayList<GoalData>();
			String baseurlString = Global_Application.url+"weight-goals/?user="+appPrefs.getUserid();    
			Log.e("TAG","url is : " + baseurlString);
			
			RestClient client = new RestClient(baseurlString); 
			Log.e("TAG","token " + appPrefs.getToken().toString());
			client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
			client.AddParam("weight",weight.toString());
			client.AddParam("weight_measure",measure.toString());
			if(targetdate.length()>0){
				client.AddParam("target_date",targetdate.toString());
			}else{
				client.AddParam("interval_num",intervalnum.toString());
				client.AddParam("interval_unit",intervalunit.toString());
			}
			Log.e("TAG",weight.toString() +" : " + measure.toString() + " : " + intervalnum.toString() + " : " + intervalunit.toString());
			try
			{
				client.Execute(RequestMethod.POST);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}

			responseString = client.getResponse();
			Log.e("TAG","res : " + responseString);
				try {
					 jObject = new JSONObject(responseString);
					 jarray = jObject.getJSONArray("results");
					 if(jarray.length()>0){
						 resstr = "0";
					 }
					 resstr = "0";
					
			   } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return resstr;
		}
		
		
		// function for add goal data
		public String addWeightReading(String weight,String measure, String readingdate,String id){
			Log.e("TAG","Weight Reading : " + id);
			String resstr="1";
		    String baseurlString = Global_Application.url+"weight-goals/"+id+"/set-reading/?user="+appPrefs.getUserid();   
			Log.e("TAG","url is : " + baseurlString);
			
			RestClient client = new RestClient(baseurlString); 
			Log.e("TAG","token " + appPrefs.getToken().toString());
			client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
			client.AddParam("weight",weight.toString());
			client.AddParam("weight_measure",measure.toString());
			client.AddParam("reading_date",readingdate);
			Log.e("TAG","data : " + weight + ", " + measure + ", " +readingdate );
			try
			{
				client.Execute(RequestMethod.POST);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}

			responseString = client.getResponse();
			Log.e("TAG","res : " + responseString);
				try {
					 jObject = new JSONObject(responseString);
					 jarray = jObject.getJSONArray("results");
					 if(jarray.length()>0){
						 resstr = "0";
					 }
					 resstr = "0";
					
			   } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return resstr;
		}
		// function for add goal data
				public String addGlucoseReading(String random,String fasting, String readingdate,String id){
					String resstr="1";
				    String baseurlString = Global_Application.url+"glucose-goals/"+id+"/set-reading/?user="+appPrefs.getUserid();   
					Log.e("TAG","url is : " + baseurlString);
					
					RestClient client = new RestClient(baseurlString); 
					Log.e("TAG","token " + appPrefs.getToken().toString());
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
					client.AddParam("random",fasting.toString());
					client.AddParam("fasting",random.toString());
					client.AddParam("reading_date",readingdate);
				
					try
					{
						client.Execute(RequestMethod.POST);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
					Log.e("TAG","res : " + responseString);
						try {
							 jObject = new JSONObject(responseString);
							 jarray = jObject.getJSONArray("results");
							 if(jarray.length()>0){
								 resstr = "0";
							 }
						  	
							 resstr = "0";
					   } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					return resstr;
				}
				// function for add goal data
				public String addCholesterolReading(String hdl,String ldl,String triglycerides,String total_cholesterol, String readingdate,String id){
					String resstr="1";
				    String baseurlString = Global_Application.url+"cholesterol-goals/"+id+"/set-reading/?user="+appPrefs.getUserid();   
					Log.e("TAG","url is : " + baseurlString);
					
					RestClient client = new RestClient(baseurlString); 
					Log.e("TAG","token " + appPrefs.getToken().toString());
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
					client.AddParam("hdl",hdl.toString());
					client.AddParam("ldl",ldl.toString());
					client.AddParam("triglycerides",triglycerides.toString());
					client.AddParam("total_cholesterol",total_cholesterol.toString());
					client.AddParam("reading_date",readingdate);
				
					try
					{
						client.Execute(RequestMethod.POST);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
					Log.e("TAG","res : " + responseString);
						try {
							 jObject = new JSONObject(responseString);
							 jarray = jObject.getJSONArray("results");
							 if(jarray.length()>0){
								 resstr = "0";
							 }
							 resstr = "0";
							
					   } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					return resstr;
				}
				
				// function for add goal data
				public String addBPReading(String systolic,String diastolic,String plusrate, String readingdate,String id){
					String resstr="1";
				    String baseurlString = Global_Application.url+"blood-pressure-goals/"+id+"/set-reading/?user="+appPrefs.getUserid();   
					Log.e("TAG","url is : " + baseurlString);
					
					RestClient client = new RestClient(baseurlString); 
					Log.e("TAG","token " + appPrefs.getToken().toString());
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
					client.AddParam("systolic_pressure",systolic.toString());
					client.AddParam("diastolic_pressure",diastolic.toString());
					client.AddParam("pulse_rate",plusrate.toString());
					client.AddParam("reading_date",readingdate);
				
					try
					{
						client.Execute(RequestMethod.POST);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
					Log.e("TAG","res : " + responseString);
						try {
							 jObject = new JSONObject(responseString);
							 jarray = jObject.getJSONArray("results");
							 if(jarray.length()>0){
								 resstr = "0";
							 }
							 resstr = "0";
							
					   } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					return resstr;
				}
		// function for add goal data
		public String addGlucoseGoal(String random,String fasting,String targetdate,String intervalnum,String intervalunit){
			String resstr="1";
			ArrayList<GoalData>	lstData = new ArrayList<GoalData>();
			String baseurlString = Global_Application.url+"glucose-goals/?user="+appPrefs.getUserid();    
			Log.e("TAG","url is : " + baseurlString);
			
			RestClient client = new RestClient(baseurlString); 
			client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
			client.AddParam("random",random.toString());
			client.AddParam("fasting",fasting.toString());
			if(targetdate.length()>0){
				client.AddParam("target_date",targetdate.toString());
			}else{
				client.AddParam("interval_num",intervalnum.toString());
				client.AddParam("interval_unit",intervalunit.toString());
			}
			try
			{
				client.Execute(RequestMethod.POST);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}

			responseString = client.getResponse();
			Log.e("TAG","res : " + responseString);
				try {  
					 jObject = new JSONObject(responseString);
					 jarray = jObject.getJSONArray("results");
					 if(jarray.length()>0){
						 resstr = "0";
					 }
				  	
					 resstr = "0";
			   } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return resstr;
		}
		
		// function for add goal data
				public String addBPGoal(String systolic,String diastolic, String plusrate,String targetdate,String intervalnum,String intervalunit){
					String resstr="0";
					String baseurlString = Global_Application.url+"blood-pressure-goals/?user="+appPrefs.getUserid();    
					Log.e("TAG","url is : " + baseurlString);
					
					RestClient client = new RestClient(baseurlString); 
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
					client.AddParam("systolic_pressure",systolic.toString());
					client.AddParam("diastolic_pressure",diastolic.toString());
					client.AddParam("pulse_rate",plusrate.toString());
					if(targetdate.length()>0){
						client.AddParam("target_date",targetdate.toString());
					}else{
						client.AddParam("interval_num",intervalnum.toString());
						client.AddParam("interval_unit",intervalunit.toString());
					}
					try
					{
						client.Execute(RequestMethod.POST);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
					Log.e("TAG","res : " + responseString);
						try {
							 jObject = new JSONObject(responseString);
							 jarray = jObject.getJSONArray("results");
							 if(jarray.length()>0){
								 resstr = "0";
							 }
							 resstr = "0";
					   } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					return resstr;
				}
				// function for add goal data
				public String addCholesterolGoal(String hdl,String ldl, String triglycerides,String totalcolesterol,String targerdate,String intervalnum,String intervalunit){
					String resstr="0";
					ArrayList<GoalData>	lstData = new ArrayList<GoalData>();
					String baseurlString = Global_Application.url+"cholesterol-goals/?user="+appPrefs.getUserid();    
					Log.e("TAG","url is : " + baseurlString);
					
					RestClient client = new RestClient(baseurlString); 
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
					client.AddParam("hdl",hdl.toString());
					client.AddParam("ldl",ldl.toString());
					client.AddParam("triglycerides",triglycerides.toString());
					client.AddParam("total_cholesterol",totalcolesterol.toString());
					if(targerdate.length()>0){
						client.AddParam("target_date",targerdate.toString());
					}else{
						client.AddParam("interval_num",intervalnum.toString());
						client.AddParam("interval_unit",intervalunit.toString());
					}
					try
					{
						client.Execute(RequestMethod.POST);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
					Log.e("TAG","res : " + responseString);
						try {
							 jObject = new JSONObject(responseString);
							 jarray = jObject.getJSONArray("results");
							 if(jarray.length()>0){
								 resstr = "0";
							 }
							 resstr = "0";
							
					   } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					return resstr;
				}
							
				// function for get file data
				public String ReadingDelete(String baseurlString){
					Log.e("TAG","url is : " + baseurlString);
					
					RestClient client = new RestClient(baseurlString);   
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
					
			    
					try
					{
						client.Execute(RequestMethod.DELETE);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
					
					return "0";
				}
		// function for get file data
				public ArrayList<FileData> getFile(Long userId, String searchString){
                    String baseurlString = Global_Application.url + "healthfiles/?page_size=100&user=" + userId;
                    if(searchString!=null && !searchString.isEmpty())
                        baseurlString += "&search=" + searchString;
					ArrayList<FileData>	lstData = new ArrayList<FileData>();
					Log.e("TAG","url is : " + baseurlString);
					
					RestClient client = new RestClient(baseurlString);   
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
					
			    
					try
					{
						client.Execute(RequestMethod.GET);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

                    if(client.getResponseCode()>400){
                        return null;
                    }
					responseString = client.getResponse();
					Log.e("TAG","Response string " + responseString);
					
							try {
								 jObject = new JSONObject(responseString);
								 Log.e("TAG","res : " + responseString);
								 Log.e("TAG",jObject.getString("next"));
								 Global_Application.nextfile = jObject.getString("next");
								 jarray = jObject.getJSONArray("results");
									 for (int i = 0; i < jarray.length(); i++) {
										 JSONObject c = jarray.getJSONObject(i);

                                      FileData data = new FileData(c.getString("id").toString(), c.getString("user"),
                                              c.getString("name"), c.getString("description"),
                                              c.getString("download_url"), c.getString("mime_type"));
                                      data.setUpdatedBy(c.getLong("updated_by"));
                                      data.setUpdatedByName(c.getString("updated_by_name"));
                                      data.setUpdatedOn(c.getLong("updated_at")*1000);
				                      lstData.add(data);
				  				 }

							 Log.e("TAG","lstdata count is " + lstData.size());
							 
					   } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					return lstData;
				}
				
				// function for get file data
				public String FileDelete(String baseurlString){
					Log.e("TAG","url is : " + baseurlString);
					
					RestClient client = new RestClient(baseurlString);   
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
					
			    
					try
					{
						client.Execute(RequestMethod.DELETE);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
                    Log.e("TAG","response string delete  : " + baseurlString);
					
					return "0";
				}
				
				// function for add medicates data
				public ArrayList<MedicalData> addMedical(String name,String detail,String start_timestamp,
														 String repeat_hour,String repeat_day,String repeat_mode,String repeat_min,
														 String repeat_weekday,String repeat_day_interval){
					ArrayList<MedicalData>	lstData = new ArrayList<MedicalData>();
					String baseurlString = Global_Application.url+"medicaltests/?user="+appPrefs.getUserid();  
					Log.e("TAG","url is : " + baseurlString);
					
					RestClient client = new RestClient(baseurlString);   
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
					client.AddParam("name", name.toString());
					client.AddParam("details", detail.toString());
					client.AddParam("start_timestamp", start_timestamp.toString());
					client.AddParam("repeat_hour", repeat_hour.toString());
					client.AddParam("repeat_day", repeat_day.toString());
					client.AddParam("repeat_mode", repeat_mode.toString());
					client.AddParam("repeat_min", repeat_min.toString());
					client.AddParam("repeat_weekday", repeat_weekday.toString());
					client.AddParam("repeat_day_interval", repeat_day_interval.toString());
					
					try
					{
						client.Execute(RequestMethod.POST);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
					Log.e("TAG","Response string " + responseString);
					
							try {
								 jObject = new JSONObject(responseString);
								 Log.e("TAG","res : " + responseString);
								 Log.e("TAG",jObject.getString("next"));
								 jarray = jObject.getJSONArray("results");
									 for (int i = 0; i < jarray.length(); i++) {
										 JSONObject c = jarray.getJSONObject(i);
										 lstData.add(new MedicalData(c.getString("id").toString(), 
												 					 c.getString("name").toString(), 
												 					 c.getString("details").toString(), 
												 					 c.getString("start_timestamp").toString(), 
												 				 	 c.getString("repeat_hour").toString(), 
												 					 c.getString("repeat_day").toString(), 
												 					 c.getString("repeat_mode").toString(), 
												 					 c.getString("repeat_min").toString(), 
												 					 c.getString("repeat_weekday").toString(), 
												 					 c.getString("repeat_day_interval").toString()));
				                     
				  				 }
				              
							 Log.e("TAG","lstdata count is " + lstData.size());
							 
					   } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					return lstData;
				}

    public String[] retrieveExercise() {

        String[] exercise_list=null;
        String baseurlString = Global_Application.url+"physical-activity/?page_size=900";
        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
        try
        {
            client.Execute(RequestMethod.GET);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        responseString = client.getResponse();
        Log.e("TAG","get exercise list Response string " + responseString);
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(responseString);

        JSONArray jarray= null;
            jarray = jObject.getJSONArray("results");

        exercise_list=new String[jarray.length()];
        for (int i = 0; i < jarray.length(); i++)
        {
            exercise_list[i]=jarray.getJSONObject(i).getString("label");
        }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return exercise_list;
    }




                public void addExercise(String weight,String time_spent,String physical_activity_id,String calories_spent,String user,String date)
                {
                      ArrayList<CategoryExercise> lst= new ArrayList<CategoryExercise>();
                      String baseurlString = Global_Application.url+"user-physical-activity/?user="+user;
                      Log.e("TAG","add exercise url is : " + baseurlString);
                      RestClient client = new RestClient(baseurlString);
                      client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
                      client.AddParam("weight", weight);
                      client.AddParam("time_spent",time_spent);
                      client.AddParam("physical_activity",physical_activity_id);
                      client.AddParam("user_calories_spent", calories_spent);
                      client.AddParam("activity_date",date);
                      try
                      {
                        client.Execute(RequestMethod.POST);
                      }
                      catch (Exception e) {
                        e.printStackTrace();
                      }

                      responseString = client.getResponse();
                      Log.e("TAG","Response string " + responseString);

                }


				// function for add medicates data
				public ArrayList<MedicalData> getMedical(){
					ArrayList<MedicalData>	lstData = new ArrayList<MedicalData>();
					String baseurlString = Global_Application.url+"medicaltests/?user="+appPrefs.getUserid();  
					Log.e("TAG","url is : " + baseurlString);
					
					RestClient client = new RestClient(baseurlString);   
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
				
					try
					{
						client.Execute(RequestMethod.GET);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
					Log.e("TAG","Response string " + responseString);
					
							try {
								 jObject = new JSONObject(responseString);
								 Log.e("TAG","res : " + responseString);
								 Log.e("TAG",jObject.getString("next"));
								 Global_Application.nextmedical = jObject.getString("next");
								 jarray = jObject.getJSONArray("results");
								 Log.e("TAG","json array size : " + jarray.length());
									 for (int i = 0; i < jarray.length(); i++) {
										 Log.e("TAG","inside for loop");
										 JSONObject c = jarray.getJSONObject(i);
										 Log.e("TAG","name is : " + c.getString("name"));
										 lstData.add(new MedicalData(c.getString("id").toString(), 
												 					 c.getString("name").toString(), 
												 					 c.getString("details").toString(), 
												 					 c.getString("start_timestamp").toString(), 
												 				 	 c.getString("repeat_hour").toString(), 
												 					 c.getString("repeat_day").toString(), 
												 					 c.getString("repeat_mode").toString(), 
												 					 c.getString("repeat_min").toString(), 
												 					 c.getString("repeat_weekday").toString(), 
												 					 c.getString("repeat_day_interval").toString()));
				                     
				  				 }
				              
							 Log.e("TAG","lstdata count is " + lstData.size());
							 
					   } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
							Log.e("TAG","SIZE : " + lstData.size());
					return lstData;
				}
				
				// function for add medicates data
				public ArrayList<MedicalData> getMedical(String baseurlString){
					ArrayList<MedicalData>	lstData = new ArrayList<MedicalData>();
					Log.e("TAG","url is : " + baseurlString);
					
					RestClient client = new RestClient(baseurlString);   
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
				
					try
					{
						client.Execute(RequestMethod.GET);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
					Log.e("TAG","Response string " + responseString);
					
							try {
								 jObject = new JSONObject(responseString);
								 Log.e("TAG","res : " + responseString);
								 Log.e("TAG",jObject.getString("next"));
								 Global_Application.nextmedical = jObject.getString("next");
								 jarray = jObject.getJSONArray("results");
									 for (int i = 0; i < jarray.length(); i++) {
										 JSONObject c = jarray.getJSONObject(i);
										 lstData.add(new MedicalData(c.getString("id").toString(), 
												 					 c.getString("name").toString(), 
												 					 c.getString("detail").toString(), 
												 					 c.getString("start_timestamp").toString(), 
												 				 	 c.getString("repeat_hour").toString(), 
												 					 c.getString("repeat_day").toString(), 
												 					 c.getString("repeat_mode").toString(), 
												 					 c.getString("repeat_min").toString(), 
												 					 c.getString("repeat_weekday").toString(), 
												 					 c.getString("repeat_day_interval").toString()));
				                     
				  				 }
				              
							 Log.e("TAG","lstdata count is " + lstData.size());
							 
					   } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					return lstData;
				}
				
				// function for add medicates data
				public ArrayList<MedicalData> addMedication(String name,String id,String type,String detail,String morning,String afternoon,
															String evening,String night,String start_timestamp,
														    String repeat_hour,String repeat_day,String repeat_mode,String repeat_min,
														    String repeat_weekday,String repeat_day_interval,String repeat_every_x,String start_date,String end_date){
					ArrayList<MedicalData>	lstData = new ArrayList<MedicalData>();
					//String baseurlString = Global_Application.url+"medications/?user="+appPrefs.getUserid();
                    String baseurlString = Global_Application.url+"reminders/"+"?user="+id.toString(); //MJ:api add
                    Log.e("TAG","url is : " + baseurlString);
					
					RestClient client = new RestClient(baseurlString);   
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
                    client.AddParam("type", type);//MJ
                    client.AddParam("user", id.toString());//MJ
					client.AddParam("name", name.toString());
					client.AddParam("details", detail.toString());
                    if(type.equalsIgnoreCase("2"))
                    {
					    client.AddParam("morning_count", morning.toString());
					    client.AddParam("afternoon_count", afternoon.toString());
					    client.AddParam("evening_count", evening.toString());
					    client.AddParam("night_count", night.toString());
                    }
					client.AddParam("start_timestamp", start_timestamp.toString());
					client.AddParam("repeat_hour", repeat_hour.toString());
					client.AddParam("repeat_day", repeat_day.toString());
					client.AddParam("repeat_mode", repeat_mode.toString());
					client.AddParam("repeat_min", repeat_min.toString());
					client.AddParam("repeat_weekday", repeat_weekday.toString());
					client.AddParam("repeat_day_interval",repeat_day_interval.toString());
                    client.AddParam("repeat_every_x",repeat_every_x.toString());
                    client.AddParam("start_date",start_date.toString());
                    client.AddParam("end_date",end_date.toString());
					
					try
					{
						client.Execute(RequestMethod.POST);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
					Log.e("TAG","Response string " + responseString);
					
							try {
								 jObject = new JSONObject(responseString);
								 Log.e("TAG","res : " + responseString);
          						 if((jarray = jObject.getJSONArray("results"))!= null)  //MJ
								 {
									 for (int i = 0; i < jarray.length(); i++) {
										 JSONObject c = jarray.getJSONObject(i);
										 lstData.add(new MedicalData(c.getString("id").toString(), 
												 					 c.getString("name").toString(), 
												 					 c.getString("details").toString(), 
												 					 c.getString("start_timestamp").toString(), 
												 				 	 c.getString("repeat_hour").toString(), 
												 					 c.getString("repeat_day").toString(), 
												 					 c.getString("repeat_mode").toString(), 
												 					 c.getString("repeat_min").toString(), 
												 					 c.getString("repeat_weekday").toString(), 
												 					 c.getString("repeat_day_interval").toString()));
				                     
                                     }
                                     }
				              
							 Log.e("TAG","lstdata count is " + lstData.size());
							 
					   } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					return lstData;
				}

                public void storeReminderReading(String reminder_id,String morning_check,String evening_check,String afternoon_check,String night_check,String complete_check,String user_id)
                {
                    ArrayList<MedicalData>	lstData = new ArrayList<MedicalData>();
                    String baseurlString = Global_Application.url+"reminderreadings/"+"?user="+user_id+"&"+"type=2"+"&reading_date=2013-10-21";
                    //String baseurlString = Global_Application.url+"reminderreadings/"+reminder_id + "/?user="+user_id;
                    Log.e("TAG","url is : " + baseurlString);

                    RestClient client = new RestClient(baseurlString);
                    client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
                    //client.AddParam("morning_check", morning_check);//MJ
                    //client.AddParam("afternoon_check", afternoon_check);//MJ
                    //client.AddParam("evening_check",evening_check);
                    //client.AddParam("night_check", night_check);
                    //client.AddParam("complete_check",complete_check);

                    try
                    {
                        client.Execute(RequestMethod.GET);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    responseString = client.getResponse();
                    Log.e("TAG","Response string " + responseString);

                }


    public ArrayList<MedicationData> getReminderInfo(String user_id,String remindertype)
    {
        ArrayList<MedicationData>	lstData = new ArrayList<MedicationData>();
        //String baseurlString = Global_Application.url+"reminders/?user="+appPrefs.getUserid()+"&"+"type=MEDICATION";
        String baseurlString = Global_Application.url+"reminders/?user="+user_id+"&"+"type="+remindertype;

        Log.e("TAG","url is : " + baseurlString);

        RestClient client = new RestClient(baseurlString);
        client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

        try
        {
            client.Execute(RequestMethod.GET);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        responseString = client.getResponse();
        Log.e("TAG","Response string " + responseString);

        try {
            jObject = new JSONObject(responseString);
            Log.e("TAG","res : " + responseString);
            Log.e("TAG",jObject.getString("next"));
            jarray = jObject.getJSONArray("results");
            for (int i = 0; i < jarray.length(); i++)
            {
                JSONObject c = jarray.getJSONObject(i);
                lstData.add(new MedicationData(c.getString("id").toString(),
                        "id",
                        c.getString("name").toString(),
                        c.getString("type").toString(),
                        "null",//c.getString("detail").toString()
                        c.getString("morning_count").toString(),
                        c.getString("afternoon_count").toString(),
                        c.getString("evening_count").toString(),
                        c.getString("night_count").toString(),
                        c.getString("user").toString(),
                        "null",
                        "null",
                        "null",
                        c.getString("repeat_mode").toString(),
                        "null",
                        "null",
                        "null",c.getString("start_date").toString(),c.getString("end_date").toString(),false,false,false,false,false));

            }
            Log.e("TAG","lstdata count is " + lstData.size());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return lstData;

    }


    /*
                public ArrayList<MedicationData> getReminderInfo(String user_id,String remindertype,String reading_date)
                {
                    ArrayList<MedicationData>	lstData = new ArrayList<MedicationData>();
                    //String baseurlString = Global_Application.url+"reminders/?user="+appPrefs.getUserid()+"&"+"type=MEDICATION";
                    String baseurlString = Global_Application.url+"reminderreadings/?user="+user_id+"&"+"type="+remindertype+"&reading_date=2013-10-21";

                    Log.e("TAG","url is : " + baseurlString);

                    RestClient client = new RestClient(baseurlString);
                    client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());

                    try
                    {
                        client.Execute(RequestMethod.GET);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    responseString = client.getResponse();
                    Log.e("TAG","Response string " + responseString);

                    try {
                        jObject = new JSONObject(responseString);
                        Log.e("TAG","res : " + responseString);
                        Log.e("TAG",jObject.getString("next"));
                        jarray = jObject.getJSONArray("results");
                        for (int i = 0; i < jarray.length(); i++)
                        {
                            JSONObject c = jarray.getJSONObject(i);
                            lstData.add(new MedicationData(c.getJSONObject("reminder").getString("id").toString(),
                            c.getJSONObject("reminder").getString("id").toString(),
                            c.getJSONObject("reminder").getString("name").toString(),
                            c.getJSONObject("reminder").getString("type").toString(),
                            "null",//c.getString("detail").toString()
                            c.getJSONObject("reminder").getString("morning_count").toString(),
                            c.getJSONObject("reminder").getString("afternoon_count").toString(),
                            c.getJSONObject("reminder").getString("evening_count").toString(),
                            c.getJSONObject("reminder").getString("night_count").toString(),
                            c.getString("user").toString(),
                            "null",
                            "null",
                            "null",
                             c.getJSONObject("reminder").getString("repeat_mode").toString(),
                            "null",
                            "null",
                            "null",c.getJSONObject("reminder").getString("start_date").toString(),c.getJSONObject("reminder").getString("end_date").toString(),Boolean.parseBoolean("morning_check"),Boolean.parseBoolean("noon_check"),Boolean.parseBoolean("evening_check"),Boolean.parseBoolean("night_check"),Boolean.parseBoolean("complete_check")));
                        }
                        Log.e("TAG","lstdata count is " + lstData.size());

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    return lstData;

                }
*/
				// function for add medicates data
				public ArrayList<MedicalData> getMedication(){
					ArrayList<MedicalData>	lstData = new ArrayList<MedicalData>();
					String baseurlString = Global_Application.url+"medications/?user="+appPrefs.getUserid();  
					Log.e("TAG","url is : " + baseurlString);
					
					RestClient client = new RestClient(baseurlString);   
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
				
					try
					{
						client.Execute(RequestMethod.GET);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
					Log.e("TAG","Response string " + responseString);
					
							try {
								 jObject = new JSONObject(responseString);
								 Log.e("TAG","res : " + responseString);
								 Log.e("TAG",jObject.getString("next"));
								 Global_Application.nextmedication = jObject.getString("next");
								 jarray = jObject.getJSONArray("results");
								 Log.e("TAG","json array size : " + jarray.length());
									 for (int i = 0; i < jarray.length(); i++) {
										 Log.e("TAG","inside for loop");
										 JSONObject c = jarray.getJSONObject(i);
										 Log.e("TAG","name is : " + c.getString("name"));
										 lstData.add(new MedicalData(c.getString("id").toString(), 
												 					 c.getString("name").toString(), 
												 					 c.getString("details").toString(), 
												 					 c.getString("start_timestamp").toString(), 
												 				 	 c.getString("repeat_hour").toString(), 
												 					 c.getString("repeat_day").toString(), 
												 					 c.getString("repeat_mode").toString(), 
												 					 c.getString("repeat_min").toString(), 
												 					 c.getString("repeat_weekday").toString(), 
												 					 c.getString("repeat_day_interval").toString()));
				                     
				  				 }
				              
							 Log.e("TAG","lstdata count is " + lstData.size());
							 
					   } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
							Log.e("TAG","SIZE : " + lstData.size());
					return lstData;
				}
				
				// function for add medicates data
				public ArrayList<MedicalData> getMedication(String baseurlString){
					ArrayList<MedicalData>	lstData = new ArrayList<MedicalData>();
					Log.e("TAG","url is : " + baseurlString);
					
					RestClient client = new RestClient(baseurlString);   
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
				
					try
					{
						client.Execute(RequestMethod.GET);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
					Log.e("TAG","Response string " + responseString);
					
							try {
								 jObject = new JSONObject(responseString);
								 Log.e("TAG","res : " + responseString);
								 Log.e("TAG",jObject.getString("next"));
								 Global_Application.nextmedication = jObject.getString("next");
								 jarray = jObject.getJSONArray("results");
									 for (int i = 0; i < jarray.length(); i++) {
										 JSONObject c = jarray.getJSONObject(i);
										 lstData.add(new MedicalData(c.getString("id").toString(), 
												 					 c.getString("name").toString(), 
												 					 c.getString("detail").toString(), 
												 					 c.getString("start_timestamp").toString(), 
												 				 	 c.getString("repeat_hour").toString(), 
												 					 c.getString("repeat_day").toString(), 
												 					 c.getString("repeat_mode").toString(), 
												 					 c.getString("repeat_min").toString(), 
												 					 c.getString("repeat_weekday").toString(), 
												 					 c.getString("repeat_day_interval").toString()));
				                     
				  				 }
				              
							 Log.e("TAG","lstdata count is " + lstData.size());
							 
					   } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					return lstData;
				}
				//delete medical
				public String DeleteMedical(String baseurlString){
					Log.e("TAG","url for delete" + baseurlString);
					RestClient client = new RestClient(baseurlString);   
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
				  
					try
					{
						client.Execute(RequestMethod.DELETE);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
					
					return responseString;
				}
				//delete medication
				public String DeleteMedication(String baseurlString){
					Log.e("TAG","url for delete" + baseurlString);
					RestClient client = new RestClient(baseurlString);   
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
				  
					try
					{
						client.Execute(RequestMethod.DELETE);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
                    Log.e("TAG","Response string " + responseString);
					
					return responseString;
				}
				
				
				// function for add medicates data
				public MedicationData getMedicationByID(String id){
					String baseurlString = Global_Application.url + "medications/"+id;
					MedicationData medicationdt = new MedicationData();
					Log.e("TAG","url is : " + baseurlString);
					
					RestClient client = new RestClient(baseurlString);   
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
				
					try
					{
						client.Execute(RequestMethod.GET);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
					Log.e("TAG","Response string " + responseString);
					
							try {
								 jObject = new JSONObject(responseString);
								 Log.e("TAG","res : " + responseString);
								 medicationdt.setId(jObject.getString("id").toString());
								 medicationdt.setName(jObject.getString("name").toString());
								 medicationdt.setDetails(jObject.getString("details").toString());
								 medicationdt.setMorning_count(jObject.getString("morning_count").toString());
								 medicationdt.setAfternoon_count(jObject.getString("afternoon_count").toString());
								 medicationdt.setEvening_count(jObject.getString("evening_count").toString());
								 medicationdt.setNight_count(jObject.getString("night_count").toString());
								 medicationdt.setUser(jObject.getString("user").toString());
								 medicationdt.setStart_timestamp(jObject.getString("start_timestamp").toString());
								 medicationdt.setRepeat_mode(jObject.getString("repeat_mode").toString());
								 medicationdt.setRepeat_day(jObject.getString("repeat_day").toString());
								 medicationdt.setRepeat_hour(jObject.getString("repeat_hour").toString());
								 medicationdt.setRepeat_min(jObject.getString("repeat_min").toString());
								 medicationdt.setRepeat_weekday(jObject.getString("repeat_weekday").toString());
								 medicationdt.setRepeat_day_interval(jObject.getString("repeat_day_interval").toString());
                                 medicationdt.setStart_date(jObject.getString("start_date").toString());
				  				
				              
						
							 
					   } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					return medicationdt;
				}
				
				
				// function for add medicates data
				public ArrayList<MedicalData> UpdateMedication(String id,String user_id,String type,String name,String detail,String morning,String afternoon,
															String evening,String night,String start_timestamp,
														    String repeat_hour,String repeat_day,String repeat_mode,String repeat_min,
														    String repeat_weekday,String repeat_day_interval,String start_date,String end_date){
					ArrayList<MedicalData>	lstData = new ArrayList<MedicalData>();
					String baseurlString = Global_Application.url+"reminders/"+id+"/"+"?type="+type;  //?user="+user_id;
					Log.e("TAG","url is : " + baseurlString);
					
					RestClient client = new RestClient(baseurlString);   
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
                    client.AddParam("user", user_id.toString());
					client.AddParam("name", name.toString());
					client.AddParam("details", detail.toString());
                    client.AddParam("type", type);
                    if(type.equalsIgnoreCase("2"))
                    {
					    client.AddParam("morning_count", morning.toString());
					    client.AddParam("afternoon_count", afternoon.toString());
					    client.AddParam("evening_count", evening.toString());
					    client.AddParam("night_count", night.toString());
                    }
					client.AddParam("start_timestamp", start_timestamp.toString());
					client.AddParam("repeat_hour", repeat_hour.toString());
					client.AddParam("repeat_day", repeat_day.toString());
					client.AddParam("repeat_mode", repeat_mode.toString());
					client.AddParam("repeat_min", repeat_min.toString());
					client.AddParam("repeat_weekday", repeat_weekday.toString());
					client.AddParam("repeat_day_interval", repeat_day_interval.toString());
                    client.AddParam("start_date",start_date);
                    client.AddParam("end_date",end_date);
					
					try
					{
						client.Execute(RequestMethod.PUT);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
					Log.e("TAG","Response string " + responseString);
					
							try {
								 jObject = new JSONObject(responseString);
								 Log.e("TAG","res : " + responseString);
								 Log.e("TAG",jObject.getString("next"));
								 jarray = jObject.getJSONArray("results");
									 for (int i = 0; i < jarray.length(); i++) {
										 JSONObject c = jarray.getJSONObject(i);
										 lstData.add(new MedicalData(c.getString("id").toString(), 
												 					 c.getString("name").toString(), 
												 					 c.getString("details").toString(), 
												 					 c.getString("start_timestamp").toString(), 
												 				 	 c.getString("repeat_hour").toString(), 
												 					 c.getString("repeat_day").toString(), 
												 					 c.getString("repeat_mode").toString(), 
												 					 c.getString("repeat_min").toString(), 
												 					 c.getString("repeat_weekday").toString(), 
												 					 c.getString("repeat_day_interval").toString()));
				                     
				  				 }
				              
							 Log.e("TAG","lstdata count is " + lstData.size());
							 
					   } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					return lstData;
				}			
				
				
				// function for add medicates data
				public ArrayList<MedicalData> UpdateMedical(String id,String name,String detail,String start_timestamp,
														    String repeat_hour,String repeat_day,String repeat_mode,String repeat_min,
														    String repeat_weekday,String repeat_day_interval){
					ArrayList<MedicalData>	lstData = new ArrayList<MedicalData>();
					String baseurlString = Global_Application.url+"medicaltests/"+id+"/?user="+appPrefs.getUserid();  
					Log.e("TAG","url is : " + baseurlString);
					
					RestClient client = new RestClient(baseurlString);   
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
					client.AddParam("name", name.toString());
					client.AddParam("details", detail.toString());
					client.AddParam("start_timestamp", start_timestamp.toString());
					client.AddParam("repeat_hour", repeat_hour.toString());
					client.AddParam("repeat_day", repeat_day.toString());
					client.AddParam("repeat_mode", repeat_mode.toString());
					client.AddParam("repeat_min", repeat_min.toString());
					client.AddParam("repeat_weekday", repeat_weekday.toString());
					client.AddParam("repeat_day_interval", repeat_day_interval.toString());
					
					try
					{
						client.Execute(RequestMethod.PUT);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
					Log.e("TAG","Response string " + responseString);
					
							try {
								 jObject = new JSONObject(responseString);
								 Log.e("TAG","res : " + responseString);
								 Log.e("TAG",jObject.getString("next"));
								 jarray = jObject.getJSONArray("results");
									 for (int i = 0; i < jarray.length(); i++) {
										 JSONObject c = jarray.getJSONObject(i);
										 lstData.add(new MedicalData(c.getString("id").toString(), 
												 					 c.getString("name").toString(), 
												 					 c.getString("details").toString(), 
												 					 c.getString("start_timestamp").toString(), 
												 				 	 c.getString("repeat_hour").toString(), 
												 					 c.getString("repeat_day").toString(), 
												 					 c.getString("repeat_mode").toString(), 
												 					 c.getString("repeat_min").toString(), 
												 					 c.getString("repeat_weekday").toString(), 
												 					 c.getString("repeat_day_interval").toString()));
				                     
				  				 }
				              
							 Log.e("TAG","lstdata count is " + lstData.size());
							 
					   } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					return lstData;
				}
				
				
				// function for add medicates data
				public MedicalData getMedicalByID(String id){
					String baseurlString = Global_Application.url + "medicaltests/"+id;
					MedicalData medicationdt = new MedicalData();
					Log.e("TAG","url is : " + baseurlString);
					
					RestClient client = new RestClient(baseurlString);   
					client.AddHeader("Authorization","Token "+appPrefs.getToken().toString());
				
					try
					{
						client.Execute(RequestMethod.GET);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}

					responseString = client.getResponse();
					Log.e("TAG","Response string " + responseString);
					
							try {
								 jObject = new JSONObject(responseString);
								 Log.e("TAG","res : " + responseString);
								 medicationdt.setId(jObject.getString("id").toString());
								 medicationdt.setName(jObject.getString("name").toString());
								 medicationdt.setDetail(jObject.getString("details").toString());
								 medicationdt.setStart_timestamp(jObject.getString("start_timestamp").toString());
								 medicationdt.setRepeat_mode(jObject.getString("repeat_mode").toString());
								 medicationdt.setRepeat_day(jObject.getString("repeat_day").toString());
								 medicationdt.setRepeat_hour(jObject.getString("repeat_hour").toString());
								 medicationdt.setRepeat_min(jObject.getString("repeat_min").toString());
								 medicationdt.setRepeat_weekday(jObject.getString("repeat_weekday").toString());
								 medicationdt.setRepeat_day_interval(jObject.getString("repeat_day_interval").toString());
				  				
				              
						
							 
					   } catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					return medicationdt;
				}

				public ArrayList<PriorityCard> getCard(Long userId){
                    ArrayList<PriorityCard> lstData = new ArrayList<PriorityCard>();
                    PriorityCard pc;
                    Date lastSyncDate = new Date(appPrefs.getSSPCSyncedDate());

                    if ( lastSyncDate == new Date() ){
                        pc = new PickGoalPriorityCard();
                        pc.setId(appPrefs.getSSPCId());
                        pc.setMessage(appPrefs.getSSPCMessage());
                        pc.setCustomData("weightGoalMessage",appPrefs.getSSPCWGM());
                        pc.setCustomData("weightGoalCheck",appPrefs.getSSPCWGC());
                        pc.setCustomData("bpGoalMessage",appPrefs.getSSPCBGM());
                        pc.setCustomData("bpGoalCheck",appPrefs.getSSPCBGC());
                        pc.setCustomData("glucoseGoalMessage",appPrefs.getSSPCGGM());
                        pc.setCustomData("glucoseGoalCheck",appPrefs.getSSPCGGC());
                        pc.setCustomData("cholesterolGoalMessage",appPrefs.getSSPCCGM());
                        pc.setCustomData("cholesterolGoalCheck",appPrefs.getSSPCCGC());
                        lstData.add(pc);

                    } else {


                        String responseString = "{\"count\": 1, \"next\": null, \"previous\": null, \"results\": " +
                                "[{\"id\": 1, \"type\": \"1\",\"message\": \"card mesasge\"," +
                                " \"weight_goal_check\": \"Y\", \"weight_goal_message\": \"his is weighgt goal message\"," +
                                " \"cholesterol_goal_check\": \"N\", \"cholesterol_goal_message\": \"his is cholesterol goal message\"," +
                                " \"glucose_goal_check\": \"Y\", \"glucose_goal_message\": \"his is glucose goal message\"," +
                                " \"bp_goal_check\": \"\", \"bp_goal_message\": \"This is BP wqala message\"  }]}";
                    try {
                        System.out.println("here1010");
                        jObject = new JSONObject(responseString);
                        jarray = jObject.getJSONArray("results");
                        System.out.println(jarray.toString());
                        System.out.println(jarray.length());

                        for (int i = 0; i < jarray.length(); i++) {
                            System.out.println("here11");
                            JSONObject c = jarray.getJSONObject(i);
                            System.out.println("a"+c.getString("type")+"b");

                            //if(c.getString("type") == "1"){
                                System.out.println("here22");
                                pc = new PickGoalPriorityCard();


                                pc.setId(c.getString("id"));
                                pc.setMessage(c.getString("message"));
                                pc.setCustomData("weightGoalMessage",c.getString("weight_goal_message").toString());
                                pc.setCustomData("weightGoalCheck",c.getString("weight_goal_check").toString());
                                pc.setCustomData("bpGoalMessage",c.getString("bp_goal_message").toString());
                                pc.setCustomData("bpGoalCheck",c.getString("bp_goal_check").toString());
                                pc.setCustomData("glucoseGoalMessage",c.getString("glucose_goal_message").toString());
                                pc.setCustomData("glucoseGoalCheck",c.getString("glucose_goal_check").toString());
                                pc.setCustomData("cholesterolGoalMessage",c.getString("cholesterol_goal_message").toString());
                                pc.setCustomData("cholesterolGoalCheck",c.getString("cholesterol_goal_check").toString());
                                lstData.add(pc);
                            appPrefs.setSSPCMessage(c.getString("message"));
                            appPrefs.setSSPCId(c.getString("id"));
                            appPrefs.setSSPCWGM(c.getString("weight_goal_message"));
                            appPrefs.setSSPCWGC(c.getString("weight_goal_check"));
                            appPrefs.setSSPCBGM(c.getString("bp_goal_message"));
                            appPrefs.setSSPCBGC(c.getString("bp_goal_check"));
                            appPrefs.setSSPCGGM(c.getString("glucose_goal_message"));
                            appPrefs.setSSPCGGC(c.getString("glucose_goal_check"));
                            appPrefs.setSSPCCGC(c.getString("cholesterol_goal_check"));
                            appPrefs.setSSPCCGM(c.getString("cholesterol_goal_message"));

                            Date currentDate=new Date();
                            appPrefs.setSSPCSyncedDate(currentDate.getTime());

                            //}

                        }


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    }

                    return lstData;
                }
				
}
