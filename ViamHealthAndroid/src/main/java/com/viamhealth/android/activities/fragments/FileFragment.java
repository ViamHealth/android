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
import com.viamhealth.android.dao.rest.endpoints.FileUploader;
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
public class FileFragment extends BaseFragment {

    private Global_Application ga;
    private ViamHealthPrefs appPrefs;

    private User selectedUser;
    private Set<OnNewFileUploadedListener> onNewFileUploadedListener = new HashSet<OnNewFileUploadedListener>();

    private ActionBar actionBar;
    private ImageSelector imageSelector;

    private final String TAG = "FileFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_file_new, container, false);

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

        imageSelector = new ImageSelector(this);

        return view;
    }

    public void addOnNewFileUploadedListener(OnNewFileUploadedListener listener){
        onNewFileUploadedListener.add(listener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageSelector.onActivityResult(requestCode, resultCode, data, new ImageSelector.OnImageLoadedListener() {
            @Override
            public void OnLoad(ImageSelector imageSelector) {
                Log.d(TAG, "onActivityResult::imageFile - " + imageSelector.getURI().toString());
                Uri uri = imageSelector.getURI();
                String fileName = imageSelector.getFile().getName();
                uploadFile(fileName, imageSelector.getByteArray());
            }
        });
    }

    private void uploadFile(final String filename, final byte[] byteArray) {
        Log.d(TAG, "uploadFile::filename - " + filename);
        final String fileExtension = UIUtility.getFileExtension(filename);

        int size = UIUtility.dpToPx(getActivity(), 198);
        Bitmap imgBitmap = imageSelector.getBitmap(size, size);

        View dialogView = LayoutInflater.from(getSherlockActivity()).inflate(R.layout.upload_file, null);
        ImageView img_display = (ImageView) dialogView.findViewById(R.id.img_display);
        if(imgBitmap!=null){
            img_display.setImageBitmap(imgBitmap);
        }
        final TextView txtViewFileName = (TextView) dialogView.findViewById(R.id.file_name);
        txtViewFileName.setText(filename);

        AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity(), R.style.StyledProgressDialog);
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
        Log.d(TAG, "pickFile::Selecting an Image now");
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

    public class UploadFiletoServer extends AsyncTask <String, Void, FileUploader.Response>
    {
        protected Context applicationContext;
        protected ProgressDialog dialog;
        protected Integer fileId = 0;
        protected String fileName;
        protected byte[] byteArray;
        protected String downloadUrl;

        @Override
        protected void onPreExecute(){
            dialog = new ProgressDialog(getSherlockActivity());
            dialog.setMessage("uploading the file....");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Log.i(TAG, "UploadAsynTask::onPreExecute");
        }

        protected void onPostExecute(FileUploader.Response result) {
            Log.i(TAG, "UploadAsynTask::onPostExecute");

            FileData fileData = result.getFileDate();
            onFileUploadedToServer(fileData);

            dialog.dismiss();
        }

        @Override
        protected FileUploader.Response doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i(TAG, "UploadAsynTask::execute - uploading file - " + this.fileName);
            FileUploader uploader = new FileUploader(appPrefs.getToken());
            FileUploader.Response response = uploader.uploadFile(imageSelector.getFileName(),
                    getActivity(), selectedUser.getId(), dialog);

            Log.i(TAG, "UploadAsynTask::execute - uploaded file with response - " + response);
            return response;
        }

    }

}
