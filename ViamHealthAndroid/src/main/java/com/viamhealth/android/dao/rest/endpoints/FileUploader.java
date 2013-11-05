package com.viamhealth.android.dao.rest.endpoints;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by naren on 05/11/13.
 */
public class FileUploader {

    private static String upLoadServerUri = "http://api.viamhealth.com/";
    private String formValueName = "file";
    private String uploadURL = "";

    public class Response {
        private int serverResponseCode;
        private String serverResponseMessage;
        private String response;

        private Integer fileId;
        private String downloadURL;

        public int getServerResponseCode() {
            return serverResponseCode;
        }

        public String getServerResponseMessage() {
            return serverResponseMessage;
        }

        public String getResponse() {
            return response;
        }

        public Integer getFileId() {
            if(response==null || response.isEmpty())
                return 0;
            try {
                JSONObject object = new JSONObject(response);
                return object.getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }

        public String getDownloadURL() {
            if(response==null || response.isEmpty())
                return null;
            try {
                JSONObject object = new JSONObject(response);
                return object.getString("download_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public Response uploadProfilePicture(String sourceFileUri, Activity activity, Long userId, Dialog dialog) {
        formValueName = "profile_pictue";
        uploadURL = upLoadServerUri + "users/" + userId + "/profile-picture/";
        return upload(sourceFileUri, activity, dialog);
    }

    public Response uploadFile(String sourceFileUri, Activity activity, Long userId, Dialog dialog) {
        formValueName = "file";
        uploadURL = upLoadServerUri + "healthfiles/?user=" + userId;
        return upload(sourceFileUri, activity, dialog);
    }

    private Response upload(String sourceFileUri, Activity activity, Dialog dialog) {

        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "--ViamHealthFileUploader";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {
            dialog.dismiss();
            Log.e("uploadFile", "Source File not exist :" + uploadFilePath + "" + uploadFileName);
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity, "Source File not exist :"
                            + uploadFilePath + "" + uploadFileName, Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
        else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(uploadURL);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty(formValueName, fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\""+formValueName+"\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                Response response = new Response();
                // Responses from the server (code and message)
                response.serverResponseCode = conn.getResponseCode();
                response.serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    +" http://www.androidexample.com/media/uploads/"
                                    +uploadFileName;

                            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

                    InputStream is = conn.getInputStream();
                    int ch;
                    StringBuffer b = new StringBuffer();
                    while( ( ch = is.read() ) != -1 ){
                        b.append( (char)ch );
                    }
                    progress+=1000;
                    //getSherlockActivity().setSupportProgress(progress*multiplier);
                    response.response = b.toString();

                    is.close();
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
                return response;
            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(UploadToServer.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(UploadToServer.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();

            return null;

        } // End else block
    }
}
