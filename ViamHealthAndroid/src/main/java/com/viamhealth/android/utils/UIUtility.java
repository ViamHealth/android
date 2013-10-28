package com.viamhealth.android.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

/**
 * Created by naren on 23/10/13.
 */
public class UIUtility {

    public static String getFileExtension(String name){
        if(name==null){
            throw new NullPointerException("filename passed to getExtension is null");
        }
        String extension = name.lastIndexOf(".")>-1 ? name.substring(name.lastIndexOf(".")+1):null;
        return extension;
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static Calendar getDate(Calendar cal) {
        Calendar dayCal = Calendar.getInstance();
        dayCal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
        dayCal.set(Calendar.MILLISECOND, 0);
        return dayCal;
    }

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

    public static String getFileName(Context context, Uri uri){
        String fileName = null;

        String scheme = uri.getScheme();
        if(scheme==null)
            return null;

        if (scheme.equals("file")) {
            fileName = uri.getLastPathSegment();
        }
        else if (scheme.equals("content")) {
            String[] proj = {MediaStore.Files.FileColumns.TITLE };
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null && cursor.getCount() != 0) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE);
                cursor.moveToFirst();
                fileName = cursor.getString(columnIndex);
            }
        }
        return fileName;
    }
}
