package com.viamhealth.android.activities.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.viamhealth.android.model.ObjectA;

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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    String delid,shareid;
    Global_Application ga;
    String selecteduserid="0";
    ArrayList<FileData> lstResult = new ArrayList<FileData>();
    private DisplayImageOptions options;
    private static final int CAMERA_PIC_REQUEST = 1337;
    Bitmap mBitmap,b1;
    byte[] byteArray;
    String Base64str=null;
    static int serverResponseCode = 0;
    Uri shareUri=null;


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

        goal_count=(TextView)view.findViewById(R.id.goal_count);

        lbl_share = (TextView)view.findViewById(R.id.lbl_share);
        lbl_share.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_social_share, 0, 0, 0);
        lbl_share.setPadding(w8, h9, w8, h9);
        lbl_share.setOnClickListener(this);
        lbl_share.setTypeface(tf);

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

    @Override
    public void onResume()
    {
        super.onResume();
        if(isInternetOn()){
            CallFileSearchTask task = new CallFileSearchTask();
            task.activity =getActivity();
            task.execute();
        }else{
            Toast.makeText(getActivity(), "Network is not available....", Toast.LENGTH_SHORT).show();
        }
    }

    public void ScreenDimension()
    {
        display = getActivity().getWindowManager().getDefaultDisplay();
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        width = display.getWidth();
        height = display.getHeight();

    }

    public void uploadImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    camera.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    startActivityForResult(camera, CAMERA_PIC_REQUEST);

                } else if (items[item].equals("Choose from Library")) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    photoPickerIntent.setType("*/*");
                    startActivityForResult(photoPickerIntent, 1);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /*
    public String getRealPathFromURI(Uri contentUri)
    {
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION}, MediaStore.Images.Media.DATE_ADDED, null, "date_added ASC");
        if(cursor != null && cursor.moveToFirst())
        {
            do {
                Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                ga.setFileuri(uri.toString());
            }while(cursor.moveToNext());
            cursor.close();
        }
        return null;
    }

*/

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {





        int width = bm.getWidth();

        int height = bm.getHeight();


        float srcWidth = bm.getWidth();
        float srcHeight = bm.getHeight();



        if((srcWidth>=newWidth || srcHeight>=newHeight)==false)
        {
            return bm;
        }


        float resizeWidth = srcWidth;
        float resizeHeight = srcHeight;

        float aspect = resizeWidth / resizeHeight;

        if (resizeWidth > newWidth)
        {
            resizeWidth = newWidth;
            resizeHeight= (newWidth * srcHeight)/srcWidth;

            //  resizeHeight = resizeWidth / aspect;
        }
        if (resizeHeight > newHeight)
        {
            //  aspect = resizeWidth / resizeHeight;
            resizeHeight = newHeight;

            resizeWidth=(newHeight * srcWidth)/srcHeight;

            // resizeWidth = resizeHeight * aspect;
        }

        Matrix matrix = new Matrix();

// resize the bit map

        matrix.postScale(resizeWidth / width, resizeHeight / height);

// recreate the new Bitmap

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return resizedBitmap;


    }


    public int uploadDatatoServer(byte []file,String sourceFileUri,String upLoadServerUri)
    {

        String fileName=sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        try {

            // open a URL connection to the Servlet
            URL url = new URL(upLoadServerUri);

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization","Token "+appPrefs.getToken().toString());
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("file", fileName);
            conn.setRequestProperty("user", ga.getLoggedInUser().getId().toString());
            conn.setRequestProperty("description", "description");
            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=file;filename="+ fileName + "" + lineEnd);

            dos.writeBytes(lineEnd);

            dos.write(file,0, 1 * 1024 * 1024);

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : "+ serverResponseMessage + ": " + serverResponseCode);
            dialog.dismiss();
            if(serverResponseCode == 200){
                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + "uploaded");

            }

            //close the streams //
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {

            ex.printStackTrace();



            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {

            dialog.dismiss();
            e.printStackTrace();


            Log.e("Upload file to server Exception", "Exception : "
                    + e.getMessage(), e);
        }

        return serverResponseCode;



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG","On  ac result is called");
        String path="";

        if (requestCode==1)
        {
            if (resultCode == -1)
            {

                Uri chosenImageUri = data.getData();
                shareUri=chosenImageUri;
                try
                {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize=4;
                    InputStream fi=getActivity().getContentResolver().openInputStream(chosenImageUri);

                    //File file=new File(chosenImageUri.toString());
                    //FileInputStream input = new FileInputStream(file);
                    FileInputStream input=(FileInputStream)fi;

                    byte[] buf=new byte[4096];
                    mBitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(chosenImageUri),null,options);
                    options.inPurgeable = true;
                    System.runFinalization();
                    Runtime.getRuntime().gc();
                    System.gc();
                    String type="";


                    String chosenstring=chosenImageUri+"";
                    Log.e("TAG","choosen String : " + chosenstring);
                    int count=0;
                    if(chosenstring.contains("content://"))
                    {
                        ContentResolver cR = getActivity().getContentResolver();
                        MimeTypeMap mime = MimeTypeMap.getSingleton();
                        type = mime.getExtensionFromMimeType(cR.getType(chosenImageUri));
                        String contentType=cR.getType(chosenImageUri);
                        Toast.makeText(getActivity(),"Mime type=,Content type="+type + " "+contentType,Toast.LENGTH_LONG).show();
                        String[] splitval=chosenstring.split("//");
                        path=splitval[1];
                    }
                    else if (chosenstring.contains("file:///"))
                    {
                        String[] splitval=chosenstring.split("//");
                        path=splitval[1];
                    }
                    ga.setFileuri(path);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    if(type.equalsIgnoreCase("jpeg") || type.equalsIgnoreCase("png") )
                    {
                        b1=getResizedBitmap(mBitmap,300,300);
                        b1.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        //img_display.setImageBitmap(b1);
                        ga.setImg(b1);
                    }
                    else
                    {
                        try {
                            for (int readNum; (readNum = input.read(buf)) != -1;) {
                                count+=readNum;
                                stream.write(buf, 0, readNum); //no doubt here is
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }

                    byteArray = stream.toByteArray();

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("*/*");



                    //sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "This is the text that will be shared.");
                    ObjectA obj=new ObjectA();
                    obj.setByteValue(byteArray);
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, obj);
                    startActivity(Intent.createChooser(sharingIntent, "Share using"));

                    Toast.makeText(getActivity(),"read count="+count,Toast.LENGTH_LONG).show();
                    ga.setFileByte(byteArray);

                    Intent myintent= new Intent(getActivity(),UploadFile.class);
                    startActivity(myintent);

                    stream.flush();
                    //Base64str = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    //Log.e("TAG","FROM FILE : "  + Base64str);
									/* if(mBitmap!=null){
									    	mBitmap.recycle();
									    	mBitmap=null;
										}*/

                }
                catch (FileNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        if (requestCode==CAMERA_PIC_REQUEST)
        {
            if (resultCode == -1)
            {
                //Log.e("TAG","Path is : " + path);
                Bundle extras = data.getExtras();
                mBitmap=(Bitmap)extras.get("data");

                System.gc();
                ga.setFileuri("fileuri");
                b1=getResizedBitmap(mBitmap,300,300);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                b1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
                ga.setFileByte(byteArray);
                Intent myintent= new Intent(getActivity(),UploadFile.class);
                startActivity(myintent);

                Base64str = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.e("TAG","Camera FILE : "  + Base64str);
                ga.setImg(b1);
                Uri chosenImageUri = data.getData();
                getRealPathFromURI(chosenImageUri);
							 	/*

			    		 		ga.setFileuri(getRealPathFromURI(chosenImageUri));
			    		 		String chosenstring=chosenImageUri+"";
			    		 		if(chosenstring.contains("content://"))
							 	{
							 		path=getRealPathFromURI(chosenImageUri);
				        	 	}
							 	else if (chosenstring.contains("file:///"))
							 	{
							 		String[] splitval=chosenstring.split("//");
							 		path=splitval[1];
							 	}      */
							    /*if(mBitmap!=null){
							    	mBitmap.recycle();
							    	mBitmap=null;
								}  */
							 /*	process_dialog = new ProgressDialog(this.getParent());
								process_dialog.setMessage("Please Wait....");
							    process_dialog.show();
							    new AddPhotoTask().execute();*/

            }
        }
    }






    @Override
    public void onClick(View v) {
        if(v==lbl_upload){
            // reditect uplod file screen
            uploadImage();
            //Intent displayImage = new Intent(getActivity(),UploadFile.class);
            //displayImage.putExtra("user", user);
            //startActivityForResult(displayImage, 1);
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
                    Toast.makeText(getActivity(), "Download URL.."+url, Toast.LENGTH_SHORT).show();
                    val=true;
                }
            }

            if(val==true){
                Log.e("TAG",url.toString().substring(0, url.length()-5) + " urls");
                String url1=(url.toString().substring(0, url.length()-5)).replace("/download/","/");

                ga.setDownload(url.toString().substring(0, url.length()-5));
                Toast.makeText(getActivity(),"user_id "+ga.getLoggedInUser().getId().toString(),Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(),Downlaod.class);
                //ga.currentUser=user.getId();
                i.putExtra("user_id",ga.getLoggedInUser().getId().toString());
                startActivity(i);
            }else{
                Toast.makeText(getActivity(), "Please select atlest one file..", Toast.LENGTH_SHORT).show();
            }
        }
        if(v==lbl_share)
        {
            boolean val=false;
            for(int i=0;i<lstResult.size();i++){
                if(lstResult.get(i).isChecked()){
                    shareid=lstResult.get(i).getDownload_url() ;//+ "," + shareid;
                    val=true;
                }
            }
            if(val==true)
            {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("*/*");

                //sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "This is the text that will be shared.");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, shareid);
                startActivity(Intent.createChooser(sharingIntent,"Share using"));
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
                Toast.makeText(getActivity(), "number of files == ..." + lstResult.size(),Toast.LENGTH_SHORT).show();
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
