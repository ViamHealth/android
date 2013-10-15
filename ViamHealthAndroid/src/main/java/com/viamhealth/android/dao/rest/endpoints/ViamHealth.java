package com.viamhealth.android.dao.rest.endpoints;

import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.dao.restclient.old.RequestMethod;

/**
 * Created by naren on 03/10/13.
 */
public class ViamHealth {

    private String token;
    private String baseUrl;

    public ViamHealth(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String register() {
        String url = baseUrl + "signup/";

        RestClient client = new RestClient(url);
        //client.AddParam("username",username);
        //client.AddParam("password",password);

/*        try{
            client.Execute(RequestMethod.POST);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        response = client.getResponse();
*/

        return null;
    }



}
