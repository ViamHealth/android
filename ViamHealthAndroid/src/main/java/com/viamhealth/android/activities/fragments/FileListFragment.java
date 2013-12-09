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
import com.actionbarsherlock.widget.ShareActionProvider;
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
import com.viamhealth.android.utils.UIUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by naren on 27/10/13.
 */
public class FileListFragment extends BaseListFragment implements FileFragment.OnNewFileUploadedListener {

    private MultiSelectionAdapter adapter;
    private ListView list;

    private final List<FileData> files = new ArrayList<FileData>();
    private functionClass obj;
    private Global_Application ga;

    // if ActoinMode is null - assume we are in normal mode
    private ActionMode actionMode;
    private ShareActionProvider actionProvider = null;

    private ViamHealthPrefs appPrefs;

    private User selectedUser;
    final private Map<String, Uri> mapSelectedUris = new HashMap<String, Uri>();

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
    public void onPause() {
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
        adapter.setOnItemToggledListener(new MultiSelectionAdapter.OnItemToggledListener() {
            @Override
            public void onItemToggled(Object item, boolean isChecked) {
                final FileData data = (FileData) item;
                if(isChecked){
                    FileLoader loader = new FileLoader(getSherlockActivity(), appPrefs.getToken());
                    String fileName = data.getName();
                    final String fileExtension = UIUtility.getFileExtension(fileName);
                    //need to pass even the fileName to store the file with that name
                    loader.LoadFile(data.getDownload_url(), fileName, new FileLoader.OnFileLoadedListener() {
                        @Override
                        public void OnFileLoaded(File file) {
                            mapSelectedUris.put(data.getId(), Uri.fromFile(file));
                            updateShareActionProvider();
                        }
                    });
                }else{
                    mapSelectedUris.remove(data.getId());
                    updateShareActionProvider();
                    if(mapSelectedUris.size()==0)
                        actionMode.finish();
                }
            }
        });

        this.list.setAdapter(adapter);

        this.list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (actionMode != null) {
                    // if already in action mode - do nothing
                    return false;
                }
                // set checked selected item and enter multi selection mode
                adapter.toggleChecked(position);
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
                    actionMode.invalidate();
                    // if action mode, toggle checked state of item
                    adapter.toggleChecked(position);
                }else{
                    FileData data = files.get(position);
                    FileLoader loader = new FileLoader(getSherlockActivity(), appPrefs.getToken());
                    String fileName = data.getName();
                    final String fileExtension = UIUtility.getFileExtension(fileName);
                    //need to pass even the fileName to store the file with that name
                    loader.LoadFile(data.getDownload_url(), fileName, new FileLoader.OnFileLoadedListener() {
                        @Override
                        public void OnFileLoaded(File file) {
                            MimeTypeMap myMime = MimeTypeMap.getSingleton();
                            Intent newIntent = new Intent(android.content.Intent.ACTION_VIEW);
                            String mimeType = myMime.getMimeTypeFromExtension(fileExtension);
                            newIntent.setDataAndType(Uri.fromFile(file), mimeType);
                            newIntent.setFlags(newIntent.FLAG_ACTIVITY_NEW_TASK);
                            try {
                                getSherlockActivity().startActivity(newIntent);
                            } catch (android.content.ActivityNotFoundException e) {
                                Toast.makeText(getSherlockActivity(), "No handler for this type of file.", 4000).show();
                            }
                        }
                    });
                }

            }
        });
    }

    private void updateShareActionProvider() {
        if(actionProvider==null)
            return;
        ArrayList<Uri> uris = new ArrayList<Uri>(mapSelectedUris.values());
        Intent shareIntent = null;
        if(mapSelectedUris.size()>1){
            shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            shareIntent.setType("*/*");
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        }else{
            Uri uri = uris.get(0);
            String extension = UIUtility.getFileExtension(uri.getLastPathSegment());
            String mimeType = MimeTypeMap.getSingleton().hasMimeType(extension) ?
                                MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension):"*/*";

            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType(mimeType);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        actionProvider.setShareIntent(shareIntent);
    }

    // all our ActionMode stuff here :)
    private final class ActionModeCallback implements ActionMode.Callback {

        // " selected" string resource to update ActionBar text
        private String selected = getActivity().getString(R.string.selected);

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            adapter.enterMultiMode();
            // save global action mode
            actionMode = mode;
            return true;
        }


        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // remove previous items
            menu.clear();
            final int checked = adapter.getCheckedItemCount();
            // update title with number of checked items
            mode.setTitle(checked + " " + this.selected);
            switch (checked) {
                case 0:
                    // if nothing checked - exit action mode
                    mode.finish();
                    return true;
                case 1:
                    // all items - rename + delete
                    getSherlockActivity().getSupportMenuInflater().inflate(R.menu.action_mode_files, menu);
                    initShareActionProvider(menu);
                    return true;
                default:
                    getSherlockActivity().getSupportMenuInflater().inflate(R.menu.action_mode_files, menu);
                    initShareActionProvider(menu);
                    return true;
            }
        }

        private void initShareActionProvider(Menu menu){
            // Set file with share history to the provider and set the share intent.
            MenuItem actionItem = menu.findItem(R.id.action_mode_share);
            actionProvider = (ShareActionProvider) actionItem.getActionProvider();
            actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
            // Note that you can set/change the intent any time,
            // say when the user has selected an image.
            if(mapSelectedUris.size()>0){
                updateShareActionProvider();
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

    @Override
    public void onDestroyView() {
        if(actionMode!=null)
            actionMode.finish();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        if(actionMode!=null)
            actionMode.finish();
        super.onDetach();
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

    @Override
    public void onNewFileUploaded(FileData fileData) {
        files.add(0, fileData);
        if(adapter==null){
            initListView();
        }
        adapter.notifyDataSetChanged();
    }
}