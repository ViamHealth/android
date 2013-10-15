package com.viamhealth.android.activities.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.activities.Downlaod;
import com.viamhealth.android.activities.UploadFile;
import com.viamhealth.android.adapters.FileDataAdapter;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.FileData;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.ui.RefreshableListView;

import java.util.ArrayList;

/**
 * Created by naren on 08/10/13.
 */
public class FileFragment extends Fragment implements View.OnClickListener {

    User user;

    private static ProgressDialog dialog;


    Display display;
    int height,width;
    int h40,w5,w15,w20,h10,w10,h1,w1,w9,h9,w8,h5,w35,w3,h3,w80,h20;

    TextView lbl_invite_user_file,lbl_files,heding_name,lbl_upload,lbl_share,lbl_delete,lbl_download,lbl_file_title,
            lbl_label_title,lbl_date_title,goal_count,heding_name_food,lbl_invite_user_food;
    LinearLayout goal_list_layout,filename_lbl_layout,settiglayout_food,menu_invite_food,menu_invite_out_food,search_layout,file_mid_layout,
            download_layout_main,download_layout,lable_file_layout,
            date_file_layout;
    EditText edt_search;
    RefreshableListView listfile;
    ImageView back,person_icon;
    Typeface tf;

    ViamHealthPrefs appPrefs;
    functionClass obj;
    Activity myactivity;
    String delid;
    Global_Application ga;
    String selecteduserid="0";
    ArrayList<FileData> lstResult = new ArrayList<FileData>();
    private DisplayImageOptions options;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_file, container, false);

        user = getArguments().getParcelable("user");

        appPrefs = new ViamHealthPrefs(getActivity());
        obj=new functionClass(getActivity());
        ga=((Global_Application)getActivity().getApplicationContext());
        tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Condensed.ttf");

        // get screen height and width
        ScreenDimension();

        // calculate padding dynamically accroding to different screen
        w5=(int)((width*1.7)/100);
        w20=(int)((width*6.25)/100);
        w15=(int)((width*4.68)/100);
        w10=(int)((width*3.13)/100);
        w35=(int)((width*7.30)/100);
        w1=(int)((width*0.32)/100);
        w9=(int)((width*2.81)/100);
        w8=(int)((width*2.50)/100);
        w3=(int)((width*0.94)/100);
        w80=(int)((width*25)/100);

        h40=(int)((height*8.34)/100);
        h10=(int)((height*2.083)/100);
        h1=(int)((height*0.21)/100);
        h9=(int)((height*1.9)/100);
        h5=(int)((height*1.042)/100);
        h3=(int)((height*0.63)/100);
        h20=(int)((height*4.17)/100);


        edt_search=(EditText)view.findViewById(R.id.edt_search);
        edt_search.setTypeface(tf);
        edt_search.setPadding(w5, 0, 0, 0);
        edt_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(isInternetOn()){
                    CallFileSearchTask task = new CallFileSearchTask();
                    task.activity =getActivity();
                    task.execute();
                }else{
                    Toast.makeText(getActivity(), "Network is not available....", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lbl_upload = (TextView)view.findViewById(R.id.lbl_upload);
        lbl_upload.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_upload, 0, 0, 0);
        lbl_upload.setPadding(w8, h9, w8, h9);
        lbl_upload.setOnClickListener(this);
        lbl_upload.setTypeface(tf);

        lbl_share = (TextView)view.findViewById(R.id.lbl_share);
        lbl_share.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_social_share, 0, 0, 0);
        lbl_share.setPadding(w8, h9, w8, h9);

        lbl_delete = (TextView)view.findViewById(R.id.lbl_delete);
        lbl_delete.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_content_discard, 0, 0, 0);
        lbl_delete.setPadding(w8, h9, w8, h9);
        lbl_delete.setOnClickListener(this);
        lbl_delete.setTypeface(tf);

        lbl_download = (TextView)view.findViewById(R.id.lbl_download);
        lbl_download.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_download, 0, 0, 0);
        lbl_download.setPadding(w8, h9, w8, h9);
        lbl_download.setOnClickListener(this);
        lbl_download.setTypeface(tf);

        search_layout = (LinearLayout)view.findViewById(R.id.search_layout);
        search_layout.setPadding(w10, h10, w10, h10);

        file_mid_layout = (LinearLayout)view.findViewById(R.id.file_mid_layout);


        listfile = (RefreshableListView)view.findViewById(R.id.lstfile);
        //listfile.setPadding(w3, h3, w3, h3);
        listfile.setOnRefreshListener(new RefreshableListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                if(!ga.getNextfile().toString().equals("null")){

                    if(isInternetOn()){
                        CallFileNevigationTask task = new CallFileNevigationTask();
                        task.activity =getActivity();
                        task.execute();
                    }else{
                        Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

//    public static Bitmap getBitmapFromURL(String src) {
//        try {
//            URL url = new URL(src);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public void ScreenDimension()
    {
        display = getActivity().getWindowManager().getDefaultDisplay();
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        width = display.getWidth();
        height = display.getHeight();

    }

    @Override
    public void onClick(View v) {
        if(v==lbl_upload){
            // reditect uplod file screen
            //selectImage();
            Intent displayImage = new Intent(getActivity(),UploadFile.class);
            displayImage.putExtra("user", user);
            startActivityForResult(displayImage, 1);
        }
        if(v==lbl_delete){
            boolean val=false;
            for(int i=0;i<lstResult.size();i++){
                if(lstResult.get(i).isChecked()){
                    delid=lstResult.get(i).getId() + "," + delid;
                    val=true;
                }
            }

            if(val==true){
                Log.e("TAG", delid.toString().substring(0, delid.length() - 5) + " th cb is checked");
                if(isInternetOn()){
                    DeleteFileTask task = new DeleteFileTask();
                    task.activity =getActivity();
                    task.execute();
                    //appPrefs.setReloadgraph("0");
                }else{
                    Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getActivity(), "Please select atlest one file..", Toast.LENGTH_SHORT).show();
            }
        }
        if(v==lbl_download){
            String url = null;
            boolean val=false;
            for(int i=0;i<lstResult.size();i++){
                if(lstResult.get(i).isChecked()){
                    url=lstResult.get(i).getDownload_url() + "," + url;
                    val=true;
                }
            }

            if(val==true){
                Log.e("TAG",url.toString().substring(0, url.length()-5) + " urls");
                ga.setDownload(url.toString().substring(0, url.length()-5));
                Intent i = new Intent(getActivity(),Downlaod.class);
                startActivity(i);
            }else{
                Toast.makeText(getActivity(), "Please select atlest one file..", Toast.LENGTH_SHORT).show();
            }
        }
    }



    // async class for calling webservice and get responce message
    public class CallFileTask extends AsyncTask<String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
            dialog = new ProgressDialog(getActivity());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Please Wait....");
            dialog.show();
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            dialog.dismiss();
            if(lstResult.size()>0){
                goal_count.setText("("+lstResult.size()+")");
                FileDataAdapter adapter = new FileDataAdapter(getActivity(),R.layout.filelist, lstResult);
                //TODO avoid creating objects every time
                listfile.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                listfile.onRefreshComplete();
            }else{
                Toast.makeText(getActivity(), "No goals found...",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            lstResult.clear();
            lstResult=obj.getFile(Global_Application.url+"healthfiles/?user="+appPrefs.getUserid());
            return null;
        }

    }
    // async class for calling webservice and get responce message
    public class CallFileSearchTask extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
            dialog = new ProgressDialog(getActivity());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Please Wait....");
            dialog.show();
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            dialog.dismiss();
            if(lstResult.size()>0){
                goal_count.setText("("+lstResult.size()+")");
                FileDataAdapter adapter = new FileDataAdapter(getActivity(),R.layout.filelist, lstResult);
                listfile.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                listfile.onRefreshComplete();
            }else{
                Toast.makeText(getActivity(), "No goals found...",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            lstResult=obj.getFile(Global_Application.url+"healthfiles/?search="+edt_search.getText().toString());
            return null;
        }

    }

    // async class for calling webservice and get responce message
    public class CallFileNevigationTask extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
            dialog = new ProgressDialog(getActivity());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Please Wait....");
            dialog.show();
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            dialog.dismiss();
            Log.e("TAG","File list size : " + lstResult.size());
            if(lstResult.size()>0){
                goal_count.setText("("+lstResult.size()+")");
                FileDataAdapter adapter = new FileDataAdapter(getActivity(),R.layout.filelist, lstResult);
                listfile.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                listfile.onRefreshComplete();
            }else{
                Toast.makeText(getActivity(), "No goals found...",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            lstResult.addAll(obj.getFile(ga.getNextfile()));

            return null;
        }

    }
    // async class for calling webservice and get responce message
    public class DeleteFileTask extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
            dialog = new ProgressDialog(getActivity());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Please Wait....");
            dialog.show();
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            dialog.dismiss();
            if(isInternetOn()){
                CallFileTask task = new CallFileTask();
                task.activity =getActivity();
                task.execute();
                appPrefs.setReloadgraph("0");
            }else{
                Toast.makeText(getActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            String[] temp = delid.toString().substring(0, delid.length()-5).split(",");
            for(int i=0;i<temp.length;i++){
                obj.FileDelete(Global_Application.url + "healthfiles/"+temp[i]+"/");
            }
            return null;
        }

    }
    // async class for calling webservice and get responce message
    public class CalluserMeTask extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
            dialog = new ProgressDialog(getActivity());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Please Wait....");
            dialog.show();
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            //generateView();
            dialog.dismiss();
						/*	Intent intent = new Intent(GoalActivity.this,MainActivity.class);
							startActivity(intent);*/
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //obj.GetUserProfile(ga.getLstfamilyglobal().get(Integer.parseInt(selecteduserid)).getId());
            return null;
        }

    }
    // function for check internet is available or not
    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if ((connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED)
                || (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING)
                || (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING)
                || (connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        }

        else if ((connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED)
                || (connec.getNetworkInfo(1).getState() ==  NetworkInfo.State.DISCONNECTED)) {
            return false;
        }

        return false;
    }

}
