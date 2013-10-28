package com.viamhealth.android.activities.fragments;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
import com.viamhealth.android.activities.Downlaod;
import com.viamhealth.android.adapters.FileDataAdapter;
import com.viamhealth.android.adapters.MultiSelectionAdapter;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.FileData;
import com.viamhealth.android.model.enums.MedicalConditions;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.ui.helper.FileLoader;
import com.viamhealth.android.utils.Checker;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by naren on 27/10/13.
 */
public class FileListFragment extends SherlockListFragment
{

    private MultiSelectionAdapter adapter;
    private ListView list;

    private final List<FileData> files = new ArrayList<FileData>();
    private functionClass obj;
    private Global_Application ga;

    // if ActoinMode is null - assume we are in normal mode
    private ActionMode actionMode;
    private ViamHealthPrefs appPrefs;

    private User selectedUser;

    private static final int LIBRARY_FILE_VIEW = 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_sherlock_list, null);

        selectedUser = getArguments().getParcelable("user");

        obj=new functionClass(getSherlockActivity());
        ga=((Global_Application)getSherlockActivity().getApplicationContext());
        appPrefs = new ViamHealthPrefs(getActivity());

        this.list = (ListView) v.findViewById(android.R.id.list);

        if(Checker.isInternetOn(getSherlockActivity())){
            CallFileNavigationTask task = new CallFileNavigationTask();
            task.activity = getSherlockActivity();
            task.execute();
        }else{
            Toast.makeText(getSherlockActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (this.actionMode != null) {
            this.actionMode.finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initListView()
    {
        if(files.size()==0){
            Toast.makeText(getSherlockActivity(), "No goals found...",Toast.LENGTH_SHORT).show();
            return;
        }
        //goal_count.setText("("+files.size()+")");
        this.adapter = new FileDataAdapter(getSherlockActivity(), R.layout.filelist, files);
        this.list.setAdapter(adapter);
        this.list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                if (actionMode != null) {
                    // if already in action mode - do nothing
                    return false;
                }
                // set checked selected item and enter multi selection mode
                adapter.setChecked(arg2, true);
                getSherlockActivity().startActionMode(new ActionModeCallback());
                actionMode.invalidate();
                return true;
            }
        });
        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                if (actionMode != null) {
                    // if action mode, toggle checked state of item
                    adapter.toggleChecked(position);
                    actionMode.invalidate();
                } else {
                    //open the file
                    final ProgressDialog dialog = new ProgressDialog(getSherlockActivity());
                    dialog.setMessage("downloading the file...");
                    dialog.show();
                    FileLoader loader = new FileLoader(getSherlockActivity());
                    String fileName = files.get(position).getName();
                    final String fileExtension = fileName.lastIndexOf(".")>-1?fileName.substring(fileName.lastIndexOf(".")):null;
                    //need to pass even the fileName to store the file with that name
                    loader.LoadFile(files.get(position).getDownload_url(), new FileLoader.OnFileLoadedListener() {
                        @Override
                        public void OnFileLoaded(File file) {
                            Intent filePickerIntent = new Intent(Intent.ACTION_VIEW);
                            filePickerIntent.setType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension));
                            filePickerIntent.setData(Uri.fromFile(file));
                            startActivityForResult(filePickerIntent, LIBRARY_FILE_VIEW + position);
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
    }


    // all our ActionMode stuff here :)
    private final class ActionModeCallback implements ActionMode.Callback
    {

        // " selected" string resource to update ActionBar text
        private String selected = getActivity().getString(R.string.selected);

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            adapter.enterMultiMode();
            // save global action mode
            actionMode = mode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            // remove previous items
            menu.clear();
            final int checked = adapter.getCheckedItemCount();
            // update title with number of checked items
            mode.setTitle(checked + " " +this.selected);
            switch (checked) {
                case 0:
                    // if nothing checked - exit action mode
                    mode.finish();
                    return true;
                case 1:
                    // all items - rename + delete
                    getSherlockActivity().getSupportMenuInflater().inflate(
                            R.menu.action_mode_files, menu);
                    return true;
                default:
                    getSherlockActivity().getSupportMenuInflater().inflate(
                            R.menu.action_mode_files, menu);
                    return true;
            }
        }

        private void download(String strUrl, String fileName, String extension){
            try {
                String finalFileName = fileName;
                if(fileName.lastIndexOf("." + extension)==-1){
                    finalFileName += "." + extension;
                }

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(strUrl));
                request.addRequestHeader("Authorization","Token " + appPrefs.getToken().toString());
                request.setDescription(strUrl);
                request.setTitle(fileName);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,finalFileName);

                // get download service and enqueue file
                DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("ERROR: ", e.getCause().getMessage());
            }
        }

        private void downloadFiles(ArrayList<FileData> files) {
            int filesCount = files.size();
            DownloadManager downloadManager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            for(int i=0; i<filesCount; i++){
                Uri downloaUri = files.get(i).getUri();
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String extension = mime.getExtensionFromMimeType(files.get(i).getMimeType());
                download(downloaUri.toString(), files.get(i).getName(), extension);
            }
        }


        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Set<Integer> checked = adapter.getCheckedItems();
            final ArrayList<FileData> files = new ArrayList<FileData>();
            final ArrayList<Uri> uris = new ArrayList<Uri>();
            for (Integer ci : checked) {
                FileData file = (FileData) adapter.getItem(ci.intValue());
                files.add(file);
                uris.add(file.getUri());
            }

            switch (item.getItemId()) {
                case R.id.action_mode_download:
                    String url = null;
                    if(adapter.getCheckedItemCount()>0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
                        StringBuilder strBuilder = new StringBuilder("Are you sure to download the below files?\n\n");
                        for(FileData fd : files){
                            strBuilder.append(fd.getName()).append("\n");
                        }
                        builder.setTitle("Downloading files...");
                        builder.setMessage(strBuilder.toString());
                        builder.setPositiveButton("Sure...", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                downloadFiles(files);
                            }
                        });
                        builder.setNegativeButton("Nope...", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();



                    }else{
                        Toast.makeText(getSherlockActivity(), "Please select atlest one file..", Toast.LENGTH_SHORT).show();
                    }

                    // start dialog for renaming
                    /*DialogFragment d = RenameDialog.instantiate(adapter.getFirstCheckedItem().getFile(), LibraryFragment.this);
                    d.show(getActivity().getSupportFragmentManager(), "dialog");*/
                    Toast.makeText(getActivity(), "Download", Toast.LENGTH_LONG).show();
                    return true;

                case R.id.action_mode_delete:
                    if(adapter.getCheckedItemCount()>0){
                        String[] fileIds = new String[checked.size()];
                        int i=0;
                        for (FileData file : files) {
                            fileIds[i++] = file.getId();
                        }
                        if(Checker.isInternetOn(getSherlockActivity())){
                            DeleteFileTask task = new DeleteFileTask();
                            task.activity = getSherlockActivity();
                            task.execute(fileIds);
                            //appPrefs.setReloadgraph("0");
                        }else{
                            Toast.makeText(getSherlockActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getSherlockActivity(), "Please select atlest one file..", Toast.LENGTH_SHORT).show();
                    }

                    /*Set<Integer> checked = adapter.getCheckedItems();
                    // iterate through selected items and delete them
                    for (Integer ci : checked) {
                        adapter.getItem(ci.intValue()).getFile().delete();
                    }
                    updateData();*/
                    Toast.makeText(getActivity(), "Delete", Toast.LENGTH_LONG).show();
                    return true;

                case R.id.action_mode_share:

                    if(files.size()>0){
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                        sharingIntent.setType("*/*");
                        sharingIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                        startActivity(Intent.createChooser(sharingIntent, "Share using"));
                    }

                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode)
        {
            adapter.exitMultiMode();
            // don't forget to remove it, because we are assuming that if it's not null we are in ActionMode
            actionMode = null;
        }

    }

    // async class for calling webservice and get responce message
    public class CallFileNavigationTask extends AsyncTask<String, Void,String>
    {
        protected FragmentActivity activity;
        protected ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
            dialog = new ProgressDialog(getSherlockActivity());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Please Wait....");
            dialog.show();
            files.clear();
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result) {
            Log.i("onPostExecute", "onPostExecute");
            dialog.dismiss();
            Log.e("TAG", "File list size : " + files.size());
            initListView();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            files.addAll(obj.getFile(selectedUser.getId(), null));

            return null;
        }

    }

    // async class for calling webservice and get responce message
    public class DeleteFileTask extends AsyncTask <String, Void,String>
    {
        protected FragmentActivity activity;
        protected ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {

            //dialog = ProgressDialog.show(activity, "Calling", "Please wait...", true);
            dialog = new ProgressDialog(getSherlockActivity());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Please Wait....");
            dialog.show();
            Log.i("onPreExecute", "onPreExecute");

        }

        protected void onPostExecute(String result)
        {

            Log.i("onPostExecute", "onPostExecute");
            dialog.dismiss();
            if(Checker.isInternetOn(getSherlockActivity())){
                CallFileNavigationTask task = new CallFileNavigationTask();
                task.activity = getSherlockActivity();
                task.execute();
                //appPrefs.setReloadgraph("0");
            }else{
                Toast.makeText(getSherlockActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("doInBackground--Object", "doInBackground--Object");
            //String[] temp = delid.toString().substring(0, delid.length()-5).split(",");

            for(int i=0;i<params.length;i++){
                obj.FileDelete(Global_Application.url + "healthfiles/"+params[i]+"/");
            }
            return null;
        }

    }
}