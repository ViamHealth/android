package com.viamhealth.android.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.viamhealth.android.utils.UIUtility;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by naren on 05/11/13.
 */
public class ImageSelector {

    private Activity mActivity;
    private Fragment mFragment;

    private static final int CAMERA_PIC_REQUEST = 1337;
    private static final int LIBRARY_FILE_REQUEST = 1338;

    public enum FileType { Image, All; }

    private Uri uri;
    private Bitmap bitmap;
    private File file;
    private byte[] byteArray;

    public ImageSelector(Activity activity) {
        mActivity = activity;
    }

    public ImageSelector(Fragment fragment) {
        mFragment = fragment;
    }

    public void pickFile(final FileType type) {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Upload from...");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                if (items[item].equals("Take Photo")) {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myImage.jpg");
                    Uri outputFileUri = Uri.fromFile(file);
                    uri=outputFileUri;
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    camera.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    camera.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    /*
                    Camera cam=null;
                    Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                    int cameraCount = Camera.getNumberOfCameras();
                    for ( int camIdx = 0; camIdx < cameraCount; camIdx++ ) {
                        Camera.getCameraInfo( camIdx, cameraInfo );
                        if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT  ) {
                            try {
                                //cam = Camera.open( camIdx );
                            } catch (RuntimeException e) {
                                Log.e("camera open", "Camera failed to open: " + e.getLocalizedMessage());
                            }
                        }
                    }

                    cam = Camera.open( Camera.CameraInfo.CAMERA_FACING_FRONT );
                    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "myImage.jpg");
                            if (file == null) {
                                return;
                            }
                            try {
                                FileOutputStream fos = new FileOutputStream(file);
                                fos.write(data);
                                fos.close();
                            } catch (FileNotFoundException e) {

                            } catch (IOException e) {
                            }
                        }
                    };

                    cam.takePicture(null,null,mPicture);
                    */
                    //camera.
                    if(mActivity!=null)
                        mActivity.startActivityForResult(camera, CAMERA_PIC_REQUEST);
                    else
                        mFragment.startActivityForResult(camera, CAMERA_PIC_REQUEST);


                } else if (items[item].equals("Choose from Library")) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    if(type==FileType.Image)
                        photoPickerIntent.setType("image/*");
                    if(type==FileType.All)
                        photoPickerIntent.setType("*/*");
                    photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);

                    if(mActivity!=null)
                        mActivity.startActivityForResult(photoPickerIntent, LIBRARY_FILE_REQUEST);
                    else
                        mFragment.startActivityForResult(photoPickerIntent, LIBRARY_FILE_REQUEST);

                }
            }
        });
        builder.show();
    }

    public Uri getURI() {
        return uri;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Bitmap getBitmap(int height, int width) {
        //String mimeType = mContext.getContentResolver().getType(uri);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

        if(!options.outMimeType.contains("image"))
            return null;

        options.inSampleSize = calculateInSampleSize(options, width, height);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public File getFile() {
        return file;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        Activity activity = mActivity==null ? mFragment.getActivity() : mActivity;

        if(requestCode==LIBRARY_FILE_REQUEST || requestCode==CAMERA_PIC_REQUEST){
            if(resultCode==activity.RESULT_OK){
                System.gc();
                //uri = data.getData();
               //String filePath = data.getData().getPath();
                String filePath=uri.getPath();
                String fileName = UIUtility.getFileName(mActivity, uri);
                file = new File(getRealPathFromURI(mActivity, uri));
                //Toast.makeText(mContext, "File Name - " + fileName + "\nisHierarchical - " + uri.isHierarchical() + "\n Scheme - " + uri.getScheme(), Toast.LENGTH_LONG).show();
                byteArray = new byte[0];
                try {
                    byteArray = FileUtils.readFileToByteArray(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", "ReadFileToByteArray", e.getCause());
                }

                //uploadFile(fileName, byteArray);
                Toast.makeText(activity, "File Path - " + filePath + "\n File Name - " + fileName, Toast.LENGTH_LONG).show();
                return true;
            }
        }/*else if(requestCode==CAMERA_PIC_REQUEST) {
            if (resultCode == ((Activity)mContext).RESULT_OK) {
                //Log.e("TAG","Path is : " + path);
                Bundle extras = data.getExtras();
                bitmap = (Bitmap)extras.get("data");
                String fileName = "image.png";
                System.gc();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
                return true;
            }
        }
        */

        return false;
    }

    public static String getRealPathFromURI(Context context, Uri contentURI) {
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public interface OnImageSelectedListener {

    }
}
