package com.viamhealth.android.activities.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
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
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.activities.UploadFile;
import com.viamhealth.android.adapters.FileDataAdapter;
import com.viamhealth.android.model.ObjectA;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.utils.UIUtility;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Created by naren on 27/10/13.
 */
public class FileFragment extends SherlockFragment {

    private TextView filesHeader;

    private static final int CAMERA_PIC_REQUEST = 1337;
    private static final int LIBRARY_FILE_REQUEST = 1338;

    private Global_Application ga;

    private User selectedUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_file_new, container, false);

        filesHeader = (TextView) view.findViewById(R.id.files_header);

        ga=((Global_Application)getSherlockActivity().getApplicationContext());

        selectedUser = getArguments().getParcelable("user");

        Bundle args = new Bundle();
        args.putParcelable("user", selectedUser);
        FragmentTransaction fm = getSherlockActivity().getSupportFragmentManager().beginTransaction();
        FileListFragment fragment = (FileListFragment) SherlockFragment.instantiate(getSherlockActivity(), FileListFragment.class.getName(), args);
        fm.add(R.id.fileList, fragment, "file-list");
        fm.commit();

        setHasOptionsMenu(true);

        return view;
    }

    private void initiateFileUpload() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
        builder.setTitle("Upload from...");
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
                    photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(photoPickerIntent, LIBRARY_FILE_REQUEST);
                }
                dialog.dismiss();
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
                File file = new File(URI.create(filePath));
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

        if (requestCode==CAMERA_PIC_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                //Log.e("TAG","Path is : " + path);
                Bundle extras = data.getExtras();
                Bitmap bitmap=(Bitmap)extras.get("data");
                String fileName = "From Camera of " + selectedUser.getName() + ".png";
                uploadImage(fileName, bitmap);
            }
        }

    }
    private void uploadFile(String fileName, byte[] byteArray) {
        Intent myintent= new Intent(getSherlockActivity(),UploadFile.class);
        myintent.putExtra("user", (Parcelable) selectedUser);
        myintent.putExtra("filename", fileName);
        myintent.putExtra("content", byteArray);
        startActivity(myintent);
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
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
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
}
