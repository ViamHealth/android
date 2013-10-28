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
import com.viamhealth.android.model.FileData;
import com.viamhealth.android.model.ObjectA;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.utils.UIUtility;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;
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

    private static final int CAMERA_PIC_REQUEST = 1337;
    private static final int LIBRARY_FILE_REQUEST = 1338;

    private Global_Application ga;
    private ViamHealthPrefs appPrefs;

    private User selectedUser;
    private Set<OnNewFileUploadedListener> onNewFileUploadedListener = new HashSet<OnNewFileUploadedListener>();

    private ActionBar actionBar;

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

        return view;
    }

    public void addOnNewFileUploadedListener(OnNewFileUploadedListener listener){
        onNewFileUploadedListener.add(listener);
    }

    private void initiateFileUpload() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
        builder.setTitle("Upload from...");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                dialog.dismiss();

                if (items[item].equals("Take Photo")) {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    camera.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    startActivityForResult(camera, CAMERA_PIC_REQUEST);
                } else if (items[item].equals("Choose from Library")) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    photoPickerIntent.setType("*/*");
                    photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(photoPickerIntent, LIBRARY_FILE_REQUEST);
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG","On  ac result is called");

        if(requestCode==LIBRARY_FILE_REQUEST){
            if(resultCode==getActivity().RESULT_OK){
                Uri uri = data.getData();
                String filePath = data.getData().getPath();
                String fileName = UIUtility.getFileName(getSherlockActivity(), uri);
                File file = new File(getRealPathFromURI(uri));
                Toast.makeText(getSherlockActivity(), "File Name - " + fileName + "\nisHierarchical - " + uri.isHierarchical() + "\n Scheme - " + uri.getScheme(), Toast.LENGTH_LONG).show();
                byte[] byteArray = new byte[0];
                try {
                    byteArray = FileUtils.readFileToByteArray(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", "ReadFileToByteArray", e.getCause());
                }
                uploadFile(fileName, byteArray);
                Toast.makeText(getSherlockActivity(), "File Path - " + filePath + "\n File Name - " + fileName, Toast.LENGTH_LONG).show();
            }
        }

        if(requestCode==CAMERA_PIC_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                //Log.e("TAG","Path is : " + path);
                Bundle extras = data.getExtras();
                Bitmap bitmap=(Bitmap)extras.get("data");
                String fileName = "From Camera of " + selectedUser.getName() + ".png";
                uploadImage(fileName, bitmap);
            }
        }
    }

    private void uploadFile(final String filename, final byte[] byteArray) {
        /*Intent myintent= new Intent(getSherlockActivity(),UploadFile.class);
        myintent.putExtra("user", (Parcelable) selectedUser);
        myintent.putExtra("filename", fileName);
        myintent.putExtra("content", byteArray);
        startActivityForResult(myintent, UploadFile.getCode());*/
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity(), R.style.GreenTheme);
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

    private void uploadImage(String fileName, Bitmap bitmap) {
        System.gc();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        uploadFile(fileName, byteArray);
    }



    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getSherlockActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float srcWidth = bm.getWidth();
        float srcHeight = bm.getHeight();

        if((srcWidth>=newWidth || srcHeight>=newHeight)==false) {
            return bm;
        }

        float resizeWidth = srcWidth;
        float resizeHeight = srcHeight;
        float aspect = resizeWidth / resizeHeight;

        if (resizeWidth > newWidth) {
            resizeWidth = newWidth;
            resizeHeight= (newWidth * srcHeight)/srcWidth;
        }
        if (resizeHeight > newHeight){
            //  aspect = resizeWidth / resizeHeight;
            resizeHeight = newHeight;
            resizeWidth=(newHeight * srcWidth)/srcHeight;
        }

        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(resizeWidth / width, resizeHeight / height);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return resizedBitmap;
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
            initiateFileUpload();
            return false;
        }
        return super.onOptionsItemSelected(item);
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

        public int uploadDatatoServer(String upLoadServerUri)
        {
            //String fileName=sourceFileUri;
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
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
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                conn.setRequestProperty("file", this.fileName);
                //getSherlockActivity().setSupportProgress(50*multiplier);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""+ this.fileName + "\"" + lineEnd);
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
