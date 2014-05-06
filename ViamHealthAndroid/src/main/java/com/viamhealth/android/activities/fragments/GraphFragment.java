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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.viamhealth.android.R;
import com.viamhealth.android.model.enums.MedicalConditions;
import com.viamhealth.android.model.goals.Goal;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Date;

/**
 * Created by naren on 18/10/13.
 */
public class GraphFragment extends BaseFragment implements GoalFragment.OnGoalDataChangeListener, View.OnLongClickListener {

    private static final String TAG = "GraphFragment";
    View view;
    WebView webView;
    String json = "";
    OnClickAddValueListener onClickAddValueListener;
    OnClickAddGoalListener onClickAddGoalListener;
    MedicalConditions type;
    ActionMode actionMode;
    ActionBar actionBar;
    Date startDate, endDate, currentDate;
    ProgressBar timeLinePB;
    Goal goal;
    private Date selectedDateForEdit = null;
    private OnGoalModifyListener onGoalModifyListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_graph, container, false);

        type = (MedicalConditions) getArguments().getSerializable("type");
        json = getArguments().getString("json");
        goal = getArguments().getParcelable("goal");
        startDate = goal.getStartDate();
        endDate = goal.getTargetDate();
        currentDate = goal.getPresentDate();

        DateTime targetDateTime = new DateTime(endDate.getTime());
        DateTime startDateTime = new DateTime(startDate.getTime());
        DateTime presentDateTime = new DateTime(currentDate.getTime());
        Days noOfDays = Days.daysBetween(targetDateTime, startDateTime);
        int daysToReachTarget = Math.abs(noOfDays.getDays());
        noOfDays = Days.daysBetween(presentDateTime, startDateTime);
        int daysPassedBy = Math.abs(noOfDays.getDays());
        timeLinePB = (ProgressBar) view.findViewById(R.id.timeLineProgressBar);
        timeLinePB.setMax(daysToReachTarget);
        timeLinePB.setProgress(daysPassedBy);

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
        webView.setOnLongClickListener(GraphFragment.this);
        webView.loadUrl("file:///android_asset/" + type.assetName());

        actionBar = getSherlockActivity().getSupportActionBar();

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public boolean onLongClick(View v) {
        //enable the action mode
        //getSherlockActivity().startActionMode(new GraphActionModeCallback());
        return false;
    }

    @Override
    public void onPause() {
        if (actionMode != null)
            actionMode.finish();
        super.onPause();
    }

    @Override
    public void onResume() {
        if (actionMode != null)
            actionMode.finish();
        super.onResume();
    }

    @Override
    public String getScreenName() {
        if (type != null) {
            switch (type) {
                case Obese:
                    return "Weight Graph";
                case BloodPressure:
                    return "Blood Pressure Graph";
                case Diabetes:
                    return "Blood Sugar Graph";
                case Cholesterol:
                    return "Cholesterol Graph";
            }
        }
        return super.getScreenName();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, R.drawable.ic_content_new, 1, "New Value")
                .setIcon(R.drawable.ic_content_new)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.drawable.ic_content_new) {
            if (onClickAddValueListener != null) {
                onClickAddValueListener.onClick(type);
            }
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    public MedicalConditions getType() {
        return type;
    }

    @Override
    public void onChange(String json, Goal goal) {
        this.json = json;
        Log.i(TAG, this.json);

        currentDate = goal.getPresentDate();
        DateTime startDateTime = new DateTime(startDate.getTime());
        DateTime presentDateTime = new DateTime(currentDate.getTime());
        Days noOfDays = Days.daysBetween(presentDateTime, startDateTime);
        int daysPassedBy = Math.abs(noOfDays.getDays());

        timeLinePB.setProgress(daysPassedBy);

        //reload the webView
        webView.loadUrl("javascript:window.location.reload( true )");
    }

    @Override
    public void onAdd(String jsonReading) {
        Log.i(TAG, jsonReading);
        //String url = "javascript:addPoint('" + jsonReading + "')";
        String url = "javascript:addPoint('" + " " + "')";
        Log.i(TAG, url);
        webView.loadUrl(url);
    }

    @JavascriptInterface
    public String getData() {
        return this.json;
    }

    @JavascriptInterface
    public void addValue() {
        if (onClickAddValueListener != null) {
            onClickAddValueListener.onClick(type);
        }
    }

    public void setOnGoalModifyListener(OnGoalModifyListener listener) {
        this.onGoalModifyListener = listener;
    }

    private void editGoal() {
        if (this.onGoalModifyListener != null) {
            this.onGoalModifyListener.OnEdit(goal, type);
        }
        actionMode.finish();
    }

    private void deleteGoal() {
        if (this.onGoalModifyListener != null) {
            this.onGoalModifyListener.OnDelete(goal, type);
        }
        actionMode.finish();
    }

    public void setOnClickAddValueListener(OnClickAddValueListener listener) {
        this.onClickAddValueListener = listener;
    }

    public void setOnClickAddGoalListener(OnClickAddGoalListener listener) {
        this.onClickAddGoalListener = listener;
    }

    public interface OnClickAddValueListener {
        public void onClick(MedicalConditions medicalCondition);
    }

    public interface OnClickAddGoalListener {
        public void onClick();
    }

    public interface OnGoalModifyListener {
        public void OnEdit(Goal goal, MedicalConditions type);

        public void OnDelete(Goal goal, MedicalConditions type);
    }

    // all our ActionMode stuff here :)
    private final class GraphActionModeCallback implements ActionMode.Callback {

        // " selected" string resource to update ActionBar text
        private String selected = getActivity().getString(R.string.selected);

        @Override
        public boolean onCreateActionMode(ActionMode mode, com.actionbarsherlock.view.Menu menu) {
            actionMode = mode;
            return true;
        }


        @Override
        public boolean onPrepareActionMode(ActionMode mode, com.actionbarsherlock.view.Menu menu) {
            // remove previous items
            menu.clear();
            //final int checked = adapter.getCheckedItemCount();
            // update title with number of checked items
            mode.setTitle("Edit " + getString(type.key()) + " Goal");
            getSherlockActivity().getSupportMenuInflater().inflate(R.menu.action_mode_graph, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_mode_edit:
                    editGoal();
                    //Toast.makeText(getActivity(), "Edit graph " , Toast.LENGTH_LONG).show();
                    return true;

                case R.id.action_mode_delete:
                    Toast.makeText(getActivity(), "Delete graph ", Toast.LENGTH_LONG).show();
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
