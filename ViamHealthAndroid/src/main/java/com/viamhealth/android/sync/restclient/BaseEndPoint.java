package com.viamhealth.android.sync.restclient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.viamhealth.android.Global_Application;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.auth.AccountGeneral;
import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;
import com.viamhealth.android.model.BaseModel;
import com.viamhealth.android.model.goals.Goal;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.provider.parsers.JsonParser;
import com.viamhealth.android.utils.LogUtils;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by naren on 17/12/13.
 */
public abstract class BaseEndPoint {

    protected final String TAG = LogUtils.makeLogTag(getClass());

    protected Context context;

    protected final Uri BASE_URI = Uri.parse("http://api.viamhealth.com");
    protected static boolean hasReauthCalled = false;

    public BaseEndPoint(Context context) {
        this.context = context;
    }

    protected RestClient getRestClient(Params params, String... pathSegments) {
        return getAuthenticatedRestClient(params, true, pathSegments);
    }

    protected final SimpleDateFormat dateTimeFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected final SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");

    protected RestClient getAuthenticatedRestClient(Params params, boolean needAuth, String... pathSegments) {
        Uri.Builder uriBuilder = BASE_URI.buildUpon();

        for(int i=0; i<pathSegments.length; i++)
            uriBuilder = uriBuilder.appendEncodedPath(pathSegments[i]);

        uriBuilder = uriBuilder.appendPath("");

        if(params!=null && !params.isEmpty()){
            for (String key : params.keySet()){
                uriBuilder.appendQueryParameter(key, params.get(key));
            }
        }


        RestClient client = new RestClient(uriBuilder.build().toString());

        LogUtils.LOGE(TAG+" Sync Pull URL", uriBuilder.build().toString());

        if(needAuth){
            AccountManager manager = AccountManager.get(context);
            Account[] accounts = manager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
            if(accounts != null && accounts.length>0){
                try {
                    String authToken = manager.blockingGetAuthToken(accounts[0], AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);
                    client.AddHeader("Authorization","Token " + authToken);
                } catch (OperationCanceledException e) {
                    e.printStackTrace();
                    LogUtils.LOGE(TAG, e.getMessage(), e.getCause());
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtils.LOGE(TAG, e.getMessage(), e.getCause());
                } catch (AuthenticatorException e) {
                    e.printStackTrace();
                    LogUtils.LOGE(TAG, e.getMessage(), e.getCause());
                }
            }
        }

        return client;
    }

    protected class Params extends HashMap<String, String> {

    }

    public String login(String key, String password, AuthEndPoint.AuthType type){
        RestClient client = getAuthenticatedRestClient(null, false, Paths.GET_TOKEN);

        switch(type){
            case FB:
                client.AddParam("access_token", key);
                break;

            case Email:
                client.AddParam("email",key);
                client.AddParam("password",password);
                break;

            case Mobile:
                client.AddParam("mobile",key);
                client.AddParam("password",password);
                break;

            case UserName:
                client.AddParam("username",key);
                client.AddParam("password",password);
                break;

        }

        try{
            client.Execute(RequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String responseString = client.getResponse();
        LogUtils.LOGD(TAG, "login:" + client.toString());
        User user = null;
        try {
            JSONObject jObject = new JSONObject(responseString);
            //TODO::implement proper error handling
            String	token = jObject.getString("token");
            if(token.length()>0){
                LogUtils.LOGD(TAG,"token is " + token);
                return token;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.LOGW(TAG, e.getMessage(), e.getCause());
        }

        return null;
    }

    public final boolean reAuthorize() {
        hasReauthCalled = true;
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        if(accounts != null && accounts.length>0){
            String authToken = manager.peekAuthToken(accounts[0], AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
            manager.invalidateAuthToken(AccountGeneral.ACCOUNT_TYPE, authToken);
            ViamHealthPrefs prefs = new ViamHealthPrefs(context);
            prefs.setToken(null);
            return true;
        }
        return false;
    }

    public final List<BaseModel> getData(Date lastUpdatedTime) throws IOException {
        Params params = getQueryParams();
        if(params==null) params = new Params();

        if(lastUpdatedTime!=null)
            params.put("last_sync", dateTimeFormater.format(lastUpdatedTime));

        RestClient client = getRestClient(params, getPathSegments());

        try {
            client.Execute(RequestMethod.GET);
        }catch (Exception e) {
            throw new IOException(e);
        }

        int responseCode = client.getResponseCode();
        LogUtils.LOGD(TAG+"Reminders Sync", client.toString());

        if(responseCode ==HttpStatus.SC_UNAUTHORIZED){
            if(!hasReauthCalled && reAuthorize())
                return getData(lastUpdatedTime);
            else
                throw new IOException(client.getResponseCode() + ": " + client.getResponse());
        }else if(responseCode == HttpStatus.SC_OK){
            return getJsonParser().parseArray(client.getResponse());
        }else{
            throw new IOException(client.getResponseCode() + ": " + client.getResponse());
        }
    }


    /**
     * This method return the new Parser which is extended from JsonParser
     * @return
     */
    protected abstract JsonParser getJsonParser();

    /**
     * This method return the instance of a List
     * @return
     */
    protected abstract List<BaseModel> newList();

    /**
     * This method adds all the params that needs to sent across as part of POST and PUT calls
     * Assumption: there will be same set of params for both POST and PUT requests
     * @param client
     * @param model
     */
    protected abstract void addParams(final RestClient client, final BaseModel model);

    private List<BaseModel> saveAllDataItems(List<BaseModel> modelElements, RequestMethod method) throws IOException {
        Params params = getQueryParams();
        if(params==null) params = new Params();

        int elementsCount = modelElements.size();
        List<BaseModel> respModelElements = newList();
        for(int i=0; i<elementsCount; i++){
            respModelElements.addAll(saveData(modelElements.get(i), method));
        }
        return respModelElements;

    }

    protected List<BaseModel> saveData(BaseModel model, RequestMethod method) throws IOException {
        String[] ps = getPathSegments();
        String[] pathSegments = null;
        if(method==RequestMethod.PUT){
            pathSegments = Arrays.copyOf(ps, ps.length + 1);
            pathSegments[ps.length] = model.getId().toString();
        }else{
            pathSegments = ps;
        }
        RestClient client = getRestClient(getQueryParams(), pathSegments);
        addParams(client, model);
        try {
            client.Execute(method);
        }catch (Exception e) {
            e.printStackTrace();
            LogUtils.LOGW(TAG, "response of save data failed", e.getCause());
        }

        Log.i(TAG, client.toString());
        if(client.getResponseCode() == HttpStatus.SC_CREATED ||
                client.getResponseCode() == HttpStatus.SC_OK) {
            return getJsonParser().parseArray(client.getResponse());
        }

        return null;
    }

    public final List<BaseModel> postData(List<BaseModel> modelElements) throws IOException {
        return saveAllDataItems(modelElements, RequestMethod.POST);
    }

    public final List<BaseModel> putData(List<BaseModel> modelElements) throws IOException {
        return saveAllDataItems(modelElements, RequestMethod.PUT);
    }

    public List<BaseModel> deleteData(List<BaseModel> modelElements) throws IOException {
        return null;
    }

    /**
     * This methode returns all the query params that needs to be added to the request
     * @return
     */
    protected abstract Params getQueryParams();

    /**
     * This method returns all path segments in sequence which will be joined to construct the API Uri
     * @return
     */
    protected abstract String[] getPathSegments();

    protected interface Paths {
        public static String GET_TOKEN = "api-token-auth";
        public static String USERS = "users";
        public static String LOGGED_IN_USER = "me";
        public static String REMINDERS = "reminders";
        public static String REMINDER_READINGS="reminderreadings";
    }
}
