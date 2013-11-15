package com.viamhealth.android.activities.fragments;

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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.activities.UploadFile;
import com.viamhealth.android.adapters.FileDataAdapter;
import com.viamhealth.android.dao.restclient.core.RestClient;
import com.viamhealth.android.manager.ImageSelector;
import com.viamhealth.android.model.FileData;
import com.viamhealth.android.model.ObjectA;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.utils.UIUtility;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by naren on 27/10/13.
 */
public class FileFragment extends SherlockFragment {

    private TextView filesHeader;

    private Global_Application ga;
    private ViamHealthPrefs appPrefs;

    private User selectedUser;
    private Set<OnNewFileUploadedListener> onNewFileUploadedListener = new HashSet<OnNewFileUploadedListener>();

    private ActionBar actionBar;
    private ImageSelector imageSelector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_file_new, container, false);

        filesHeader = (TextView) view.findViewById(R.id.files_header);

        ga = ((Global_Application)getSherlockActivity().getApplicationContext());
        appPrefs = new ViamHealthPrefs(getSherlockActivity());
        selectedUser = getArguments().getParcelable("user");

        Bundle args = new Bundle();
        args.putParcelable("user", selectedUser);
        FragmentTransaction fm = getSherlockActivity().getSupportFragmentManager().beginTransaction();
        FileListFragment fragment = (FileListFragment) SherlockFragment.instantiate(getSherlockActivity(), FileListFragment.class.getName(), args);
        fm.add(R.id.fileList, fragment, "file-list");
        fm.commit();

        addOnNewFileUploadedListener(fragment);

        actionBar = getSherlockActivity().getSupportActionBar();

        setHasOptionsMenu(true);

        imageSelector = new ImageSelector(getSherlockActivity());

        return view;
    }

    public void addOnNewFileUploadedListener(OnNewFileUploadedListener listener){
        onNewFileUploadedListener.add(listener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG","On  ac result is called");

        imageSelector.onActivityResult(requestCode, resultCode, data);
        Uri uri = imageSelector.getURI();
        String fileName = UIUtility.getFileName(getSherlockActivity(), uri);
        uploadFile(fileName, imageSelector.getByteArray());
    }

    private void uploadFile(final String filename, final byte[] byteArray) {

        final String fileExtension = filename.lastIndexOf(".")>-1?filename.substring(filename.lastIndexOf(".")+1):null;
        String mimeType = fileExtension==null?null:MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        Bitmap imgBitmap = mimeType.contains("image")?BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length):null;

        View dialogView = LayoutInflater.from(getSherlockActivity()).inflate(R.layout.upload_file, null);
        ImageView img_display = (ImageView) dialogView.findViewById(R.id.img_display);
        if(imgBitmap!=null){
            img_display.setImageBitmap(imgBitmap);
        }
        final TextView txtViewFileName = (TextView) dialogView.findViewById(R.id.file_name);
        txtViewFileName.setText(filename.lastIndexOf(".")>-1?filename.substring(0,filename.lastIndexOf(".")):filename);

        AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity(), R.style.Greentheme);
        builder.setView(dialogView);
        builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String fn = txtViewFileName.getText().toString();
                UploadFiletoServer task = new UploadFiletoServer();
                task.fileName = fn + "." + fileExtension;
                task.byteArray = byteArray;
                task.execute();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, R.drawable.ic_action_upload, 1, "Upload")
            .setIcon(R.drawable.ic_action_upload)
//            .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem menuItem) {
//                    initiateFileUpload();
//                    return false;
//                }
//            })
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        //menu.add("Menu 1b").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.drawable.ic_action_upload){
            pickFile();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    public void pickFile() {
        imageSelector.pickFile(ImageSelector.FileType.All);
    }
    public interface OnNewFileUploadedListener {
        public void onNewFileUploaded(FileData fileData);
    }

    private void onFileUploadedToServer(FileData fileData) {
        for(OnNewFileUploadedListener listener : this.onNewFileUploadedListener){
            listener.onNewFileUploaded(fileData);
        }

    }

    public class UploadFiletoServer extends AsyncTask <String, Void,String>
    {
        protected Context applicationContext;
        protected ProgressDialog dialog;
        protected Integer fileId = 0;
        protected String fileName;
        protected byte[] byteArray;
        protected String downloadUrl;

        @Override
        protected void onPreExecute(){
//            if(this.fileName==null || this.fileName.isEmpty())
//                this.cancel(true);
            dialog = new ProgressDialog(getSherlockActivity());
            dialog.setMessage("uploading the file....");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Log.i("onPreExecute", "onPreExecute");
        }

        protected void onPostExecute(String result)
        {
            Log.i("onPostExecute", "onPostExecute");

            final String fileExtension = this.fileName.lastIndexOf(".")>-1 ?
                        this.fileName.substring(this.fileName.lastIndexOf(".")+1) : null;
            String mimeType = fileExtension==null ? null :
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
            FileData fileData = new FileData(this.fileId.toString(), selectedUser.getId().toString(), this.fileName,
                                                null, downloadUrl, mimeType);

            onFileUploadedToServer(fileData);

            dialog.dismiss();
            int totalProgress = (Window.PROGRESS_END - Window.PROGRESS_START) / 100;
            //getSherlockActivity().setSupportProgress(totalProgress);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            uploadDatatoServer("http://api.viamhealth.com/healthfiles/?user=" + selectedUser.getId().toString());
            return null;
        }

        /*public int uploadFile(String uploadServerUri) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost postRequest = new HttpPost(uploadServerUri);
                MultipartEntity reqEntity = new Multipa(HttpMulti.BROWSER_COMPATIBLE);

                bm = BitmapFactory.decodeFile("/sdcard/test.jpg");
                Bitmap bmpCompressed = Bitmap.createScaledBitmap(bm, 640, 480, true);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmpCompressed.compress(CompressFormat.JPEG, 100, bos);
                byte[] bytes = bos.toByteArray();
                reqEntity.addPart("myImage", new ByteArrayBody(bytes, "temp.jpg"));
                postRequest.setEntity(reqEntity);
                HttpResponse response = httpClient.execute(postRequest,localContext);

                BufferedReader reader = new BufferedReader(new InputStreamReader( response.getEntity().getContent(), "UTF-8"));
                String sResponse = reader.readLine();

            } catch (Exception e) {
                // handle exception here
                Log.v("myApp", "Some error came up");
            }
        }*/
        public int uploadDatatoServer(String upLoadServerUri)
        {
            //String fileName=sourceFileUri;
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "----ViamHealthFileUploadBoundary";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024;
            int multiplier = (Window.PROGRESS_END - Window.PROGRESS_START) / 100;
            multiplier = multiplier / (4000 + 4000 + byteArray.length);

            try {

                // open a URL connection to the Servlet
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Token " + appPrefs.getToken().toString());
                conn.setRequestProperty("Connection", "Keep-Alive");
                //conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                //conn.setRequestProperty("file", this.fileName);
                //getSherlockActivity().setSupportProgress(50*multiplier);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""+ this.fileName + "\"" + lineEnd);
                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                int lengthBeforeContent = dos.size();
                int lengthofContent = this.byteArray.length;

                ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);

                bytesAvailable = bis.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                //getSherlockActivity().setSupportProgress(4000*multiplier);
                int progress = 4000;
                try {
                    while (bufferSize > 0) {
                        try {
                            dos.write(buffer, 0, bufferSize);
                            progress = 4000 + dos.size();
                            //getSherlockActivity().setSupportProgress(progress*multiplier);
                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                            Log.e("TAG", "Upload file to server error: " + e.getMessage(), e);
                            return -1;
                        }
                        bytesAvailable = bis.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = bis.read(buffer, 0, bufferSize);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "Upload file to server error: " + e.getMessage(), e);
                    return -1;
                }

                int lengthAfterContent = dos.size();

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                //getSherlockActivity().setSupportProgress(progress*multiplier);

                int serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                progress += 2000;
                //getSherlockActivity().setSupportProgress(progress*multiplier);
                dos.flush();

                Log.i("TAG", "Upload file to server info: \n Before - " + lengthBeforeContent + "\n Actual - " + lengthofContent + "\n After - " + lengthAfterContent);
                //dialog.dismiss();
                if(serverResponseCode == HttpStatus.SC_CREATED){
                    Log.i("TAG", "HTTP Response is : "
                            + serverResponseMessage + ": " + "uploaded");

                    InputStream is = conn.getInputStream();
                    int ch;
                    StringBuffer b = new StringBuffer();
                    while( ( ch = is.read() ) != -1 ){
                        b.append( (char)ch );
                    }
                    progress+=1000;
                    //getSherlockActivity().setSupportProgress(progress*multiplier);
                    String responseString = b.toString();
                    Log.i("TAG", "File Upload response string - " + responseString);

                    JSONObject object = new JSONObject(responseString);
                    this.fileId = object.getInt("id");
                    this.downloadUrl = object.getString("download_url");
                }
                dos.close();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                Log.e("TAG", "Upload file to server error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                //dialog.dismiss();
                e.printStackTrace();
                Log.e("TAG", "Upload file to server Exception : "
                        + e.getMessage(), e);
            }

            return -1;
        }

    }

}
