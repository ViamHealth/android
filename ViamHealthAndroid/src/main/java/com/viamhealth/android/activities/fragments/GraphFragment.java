package com.viamhealth.android.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.viamhealth.android.R;
import com.viamhealth.android.model.enums.MedicalConditions;

import java.util.Date;

/**
 * Created by naren on 18/10/13.
 */
public class GraphFragment extends SherlockFragment implements GoalFragment.OnGoalDataChangeListener {

    private static final String TAG = "GraphFragment";
    private Date selectedDateForEdit = null;

    public interface OnClickAddValueListener {
        public void onClick(MedicalConditions medicalCondition);
    }

    public interface OnClickAddGoalListener {
        public void onClick();
    }

    View view;
    WebView webView;

    String json = "";
    OnClickAddValueListener onClickAddValueListener;
    OnClickAddGoalListener onClickAddGoalListener;

    MedicalConditions type;
    ActionMode actionMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_graph, container, false);

        type = (MedicalConditions) getArguments().getSerializable("type");
        json = getArguments().getString("json");

        webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.addJavascriptInterface(this, "goals");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.i("GraphWebView", consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }
        });
        webView.loadUrl("file:///android_asset/" + type.assetName());

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, R.drawable.ic_content_new, 1, "New Value")
                .setIcon(R.drawable.ic_content_new)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.drawable.ic_content_new){
            if(onClickAddValueListener != null){
                onClickAddValueListener.onClick(type);
            }
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    /*    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, R.drawable.ic_content_new, 1, "New Value")
                .setIcon(R.drawable.ic_content_new)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add(Menu.NONE, R.drawable.ic_action_goal, 10, "New Goal")
                .setIcon(R.drawable.ic_action_goal)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.drawable.ic_content_new){
            if(onClickAddValueListener != null){
                onClickAddValueListener.onClick(type);
            }
            return false;
        }
        if(item.getItemId()==R.drawable.ic_action_goal){
            if(onClickAddGoalListener != null){
                onClickAddGoalListener.onClick();
            }
            return false;
        }
        return super.onOptionsItemSelected(item);
    }*/

    public MedicalConditions getType() {
        return type;
    }

    @Override
    public void onChange(String json) {
        this.json = json;
        Log.i(TAG, this.json);
        //reload the webView
        webView.loadUrl( "javascript:window.location.reload( true )" );
    }

    @Override
    public void onAdd(String jsonReading) {
        Log.i(TAG, jsonReading);
        String url = "javascript:addPoint('" + jsonReading + "')";
        Log.i(TAG, url);
        webView.loadUrl(url);
    }

    @JavascriptInterface
    public String getData() {
        return this.json;
    }

    @JavascriptInterface
    public void onItemClick(long date) {
        if(actionMode == null)
            getSherlockActivity().startActionMode(new ActionModeCallback());
        selectedDateForEdit = new Date(date);
    }

    public void setOnClickAddValueListener(OnClickAddValueListener listener) {
        this.onClickAddValueListener = listener;
    }

    public void setOnClickAddGoalListener(OnClickAddGoalListener listener) {
        this.onClickAddGoalListener = listener;
    }

    // all our ActionMode stuff here :)
    private final class ActionModeCallback implements ActionMode.Callback {

        // " selected" string resource to update ActionBar text
        private String selected = getActivity().getString(R.string.selected);

        @Override
        public boolean onCreateActionMode(ActionMode mode, com.actionbarsherlock.view.Menu menu) {
            actionMode = mode;
            return true;
        }


        @Override
        public boolean onPrepareActionMode(ActionMode mode, com.actionbarsherlock.view.Menu menu)
        {
            // remove previous items
            menu.clear();
            //final int checked = adapter.getCheckedItemCount();
            // update title with number of checked items
            mode.setTitle("Edit graph data");
            getSherlockActivity().getSupportMenuInflater().inflate(R.menu.action_mode_graph, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_mode_edit:
                    Toast.makeText(getActivity(), "Edit graph data for the date " + selectedDateForEdit, Toast.LENGTH_LONG).show();
                    return true;

                case R.id.action_mode_delete:
                    Toast.makeText(getActivity(), "Delete graph data for the date " + selectedDateForEdit, Toast.LENGTH_LONG).show();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // don't forget to remove it, because we are assuming that if it's not null we are in ActionMode
            actionMode = null;
        }

    }
}
