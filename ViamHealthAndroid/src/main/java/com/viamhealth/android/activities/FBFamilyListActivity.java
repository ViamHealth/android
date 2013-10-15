package com.viamhealth.android.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.*;
import com.facebook.Settings;
import com.viamhealth.android.R;

import com.facebook.model.GraphObject;
import com.viamhealth.android.adapters.FamilyListAdapter;
import com.viamhealth.android.model.users.FbFamily;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FBFamilyListActivity extends BaseActivity {

    private List<FbFamily> families = new ArrayList<FbFamily>();
    FamilyListAdapter adapter = null;

    ProgressDialog dialog;
    private static final int REAUTH_ACTIVITY_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fb_family_list);

        Session session = createSession();
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        if(session.isOpened())
            getFamilyMembersDatafromFB(session);
        else
            finish();
    }

    private void getFamilyMembersDatafromFB(Session session){
        dialog = new ProgressDialog(FBFamilyListActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("we are searching for your loved ones...");
        dialog.show();

        Request request = Request.newGraphPathRequest(session, "me/family", new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                GraphObject graphObject = response.getGraphObject();
                FacebookRequestError error = response.getError();
                if (graphObject != null) {
                    try{
                        JSONObject jsonResponse = graphObject.getInnerJSONObject();
                        if (jsonResponse!=null) {
                            JSONArray jsonData = jsonResponse.getJSONArray("data");
                            for(int i=0; i<jsonData.length(); i++){
                                FbFamily familyMember = new FbFamily();
                                familyMember.setName(jsonData.getJSONObject(i).getString("name"));
                                familyMember.setRelationship(jsonData.getJSONObject(i).getString("relationship"));
                                familyMember.setId(jsonData.getJSONObject(i).getString("id"));
                                families.add(familyMember);
                            }
                            if(families.isEmpty())
                                finish();

                            constructView();
                            dialog.dismiss();
                        }
                    } catch (JSONException e) {
                        onBackPressed();
                    }
                }
            }
        });

        request.executeAsync();
    }

    private void constructView() {
        final ListView listView = (ListView) findViewById(R.id.fb_family_listview);
        adapter = new FamilyListAdapter(this, this.families);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String profileId = (String)view.getTag();
                Intent intent = new Intent();
                intent.putExtra("profileId", profileId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fbfamily_list, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    private Session createSession() {
        final String usersRelationshipPermission = "user_relationships";
        Session activeSession = Session.getActiveSession();
        if (activeSession == null || activeSession.getState().isClosed()) {
            activeSession = new Session.Builder(this).setApplicationId(getString(R.string.app_id)).build();
            Session.setActiveSession(activeSession);
        }
        List<String> permissions = activeSession.getPermissions();
        if (!permissions.contains(usersRelationshipPermission)) {
            Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, usersRelationshipPermission)
                    .setRequestCode(REAUTH_ACTIVITY_CODE);
            activeSession.requestNewReadPermissions(newPermissionsRequest);
        }
        return activeSession;
    }
}
