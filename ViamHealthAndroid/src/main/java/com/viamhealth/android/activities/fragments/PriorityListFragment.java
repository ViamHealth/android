package com.viamhealth.android.activities.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.viamhealth.android.Global_Application;
import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;
//import com.viamhealth.android.activities.listrows.OptionsSelectorCard;
import com.viamhealth.android.model.enums.PriorityCardType;
//import com.viamhealth.android.activities.listrows.SimpleListCard;
//import com.viamhealth.android.activities.listrows.TrackerYNCard;
import com.viamhealth.android.activities.cardrows.CardRow;
import com.viamhealth.android.dao.restclient.old.functionClass;
import com.viamhealth.android.model.cards.PriorityCard;
import com.viamhealth.android.model.users.User;
import com.viamhealth.android.utils.Checker;
import com.viamhealth.android.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunal on 3/1/14.
 */
public class PriorityListFragment extends  BaseListFragment {

    private User selectedUser;

    private ActionBar actionBar;

    private ListView list;

    private final String TAG = "PriorityListFragment";
    private functionClass obj;
    private Global_Application ga;
    private ViamHealthPrefs appPrefs;

    private final List<PriorityCard> cards = new ArrayList<PriorityCard>();


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {

        obj=new functionClass(getSherlockActivity());
        ga=((Global_Application)getSherlockActivity().getApplicationContext());
        appPrefs = new ViamHealthPrefs(getActivity());
        selectedUser = getArguments().getParcelable("user");

        View view =  layoutInflater.inflate(R.layout.fragment_sherlock_list, null);

        this.list = (ListView) view.findViewById( android.R.id.list );

        if(Checker.isInternetOn(getSherlockActivity())){
            getCardsTask task = new getCardsTask();
            task.activity = getSherlockActivity();
            task.execute();
        } else {
            Toast.makeText(getSherlockActivity(),"Network is not available....",Toast.LENGTH_SHORT).show();
        }

        this.list.setAdapter(new PriorityCardListAdapter(null));

        return view;
    }

    private void initListView()
    {
        if(cards.size()==0){
            Toast.makeText(getSherlockActivity(), "No more tasks for today ...", Toast.LENGTH_SHORT).show();
            return;
        }

        this.list.setAdapter(new PriorityCardListAdapter(cards));

    }

    private class PriorityCardListAdapter extends BaseAdapter {

        final List<CardRow> rows;

        PriorityCardListAdapter(List<PriorityCard> cards){
            rows = new ArrayList<CardRow>();
            if (cards != null){
            for ( PriorityCard card : cards ){
                if(card.getType() == PriorityCardType.TRACKER_YN_CARD){
                    //rows.add(new TrackerYNCard(LayoutInflater.from(getActivity()),card));
                } else if ( card.getType() == PriorityCardType.OPTIONS_SELECTOR_CARD){
                    //rows.add(new OptionsSelectorCard(LayoutInflater.from(getActivity()),card));
                }
                else {
                    //rows.add(new SimpleListCard(LayoutInflater.from(getActivity()), card));
                }
            }
            }

        }

        @Override
        public int getCount() {
            return rows.size();
        }

        @Override
        public int getViewTypeCount(){
            return PriorityCardType.values().length;
        }

        @Override
        public int getItemViewType(int i){
            return rows.get(i).getViewType();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return rows.get(i).getView(view);
        }
    }

    public class getCardsTask extends AsyncTask<String, Void, String> {

        protected ProgressDialog dialog;
        protected FragmentActivity activity;

        @Override
        protected void onPreExecute()
        {
            dialog = new ProgressDialog(getSherlockActivity());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Please Wait....");
            dialog.show();
            cards.clear();
            Log.i("onPreExecute", "onPreExecute");
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i("doInBackground--Object", "doInBackground--Object");
            cards.addAll(obj.getCard(selectedUser.getId()));
            return null;
        }

        protected void onPostExecute(String result) {
            Log.i("onPostExecute", "onPostExecute");
            dialog.dismiss();
            Log.e("TAG", "File list size : " + cards.size());
            initListView();
        }
    }
}
