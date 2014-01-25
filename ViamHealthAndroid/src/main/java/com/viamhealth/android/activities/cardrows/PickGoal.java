package com.viamhealth.android.activities.cardrows;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.viamhealth.android.R;
import com.viamhealth.android.ViamHealthPrefs;

import com.viamhealth.android.activities.AddGoalActivity;
import com.viamhealth.android.activities.Home;
import com.viamhealth.android.activities.SplashActivity;
import com.viamhealth.android.activities.TabActivity;
import com.viamhealth.android.activities.fragments.GoalFragment;
import com.viamhealth.android.manager.TabManager;
import com.viamhealth.android.model.cards.PriorityCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunal on 11/1/14.
 */
public class PickGoal implements CardRow {

    private final PriorityCard card;
    private final LayoutInflater inflater;
    private ViamHealthPrefs appPrefs;

    public PickGoal(LayoutInflater inflater,PriorityCard card) {
        this.card = card;
        this.inflater = inflater;
        this.appPrefs = new ViamHealthPrefs(this.inflater.getContext());
    }

    @Override
    public View getView(View convertView) {
        ViewHolder holder;
        View view;

        //we have a don't have a convertView so we'll have to create a new one

        if (convertView == null) {
            ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.priority_card_pick_goal, null);

            //use the view holder pattern to save of already looked up subviews
            holder = new ViewHolder(
                    appPrefs,
                    inflater,
                    (LinearLayout)viewGroup.findViewById(R.id.pick_goal_card_selector),
                    (TextView)viewGroup.findViewById(R.id.pick_goal_card_message),
                    (TextView)viewGroup.findViewById(R.id.weight_pick_goal_message),
                    (RadioButton)viewGroup.findViewById(R.id.weight_pick_goal_Y),
                    (RadioButton)viewGroup.findViewById(R.id.weight_pick_goal_N),
                    (RadioGroup)viewGroup.findViewById(R.id.weight_pick_goal_check),
                    (LinearLayout)viewGroup.findViewById(R.id.weight_pick_goal_create_layout),
                    (TextView)viewGroup.findViewById(R.id.bp_pick_goal_message),
                    (RadioButton)viewGroup.findViewById(R.id.bp_pick_goal_Y),
                    (RadioButton)viewGroup.findViewById(R.id.bp_pick_goal_N),
                    (RadioGroup)viewGroup.findViewById(R.id.bp_pick_goal_check),
                    (LinearLayout)viewGroup.findViewById(R.id.bp_pick_goal_create_layout),

                    (TextView)viewGroup.findViewById(R.id.glucose_pick_goal_message),
                    (RadioButton)viewGroup.findViewById(R.id.glucose_pick_goal_Y),
                    (RadioButton)viewGroup.findViewById(R.id.glucose_pick_goal_N),
                    (RadioGroup)viewGroup.findViewById(R.id.glucose_pick_goal_check),
                    (LinearLayout)viewGroup.findViewById(R.id.glucose_pick_goal_create_layout),
                    (TextView)viewGroup.findViewById(R.id.cholesterol_pick_goal_message),
                    (RadioButton)viewGroup.findViewById(R.id.cholesterol_pick_goal_Y),
                    (RadioButton)viewGroup.findViewById(R.id.cholesterol_pick_goal_N),
                    (RadioGroup)viewGroup.findViewById(R.id.cholesterol_pick_goal_check),
                    (LinearLayout)viewGroup.findViewById(R.id.cholesterol_pick_goal_create_layout),

                    (Button)viewGroup.findViewById(R.id.weight_pick_goal_create_button),
                    (Button)viewGroup.findViewById(R.id.cholesterol_pick_goal_create_button),
                    (Button)viewGroup.findViewById(R.id.bp_pick_goal_create_button),
                    (Button)viewGroup.findViewById(R.id.glucose_pick_goal_create_button)
            );

            viewGroup.setTag(holder);
            view = viewGroup;
        } else {
            //get the holder back out
            holder = (ViewHolder)convertView.getTag();
            view = convertView;
        }

        //actually setup the view
        holder.cardMessage.setText(card.getMessage());

        holder.weightGoalMessage.setText(card.getCustomData("weightGoalMessage"));
        if(card.getCustomData("weightGoalCheck") == "Y"){
            //holder.WeightCheckVal = 'Y';
            holder.weightGoalY.setChecked(true);
        }
        else
            holder.weightGoalY.setChecked(false);
        holder.weightCheck.setOnCheckedChangeListener(holder.onPickGoalRadioButtonClicked());

        holder.bpGoalMessage.setText(card.getCustomData("bpGoalMessage"));
        if(card.getCustomData("bpGoalCheck") == "Y"){
            holder.bpGoalY.setChecked(true);
        }
        else
            holder.bpGoalY.setChecked(false);
        holder.bpCheck.setOnCheckedChangeListener(holder.onPickGoalRadioButtonClicked());

        holder.glucoseGoalMessage.setText(card.getCustomData("glucoseGoalMessage"));
        if(card.getCustomData("glucoseGoalCheck") == "Y"){
            holder.glucoseGoalY.setChecked(true);
        }
        else
            holder.glucoseGoalY.setChecked(false);
        holder.glucoseCheck.setOnCheckedChangeListener(holder.onPickGoalRadioButtonClicked());

        holder.cholesterolGoalMessage.setText(card.getCustomData("cholesterolGoalMessage"));
        if(card.getCustomData("cholesterolGoalCheck") == "Y"){
            holder.cholesterolGoalY.setChecked(true);
        }
        else
            holder.cholesterolGoalY.setChecked(false);
        holder.cholesterolCheck.setOnCheckedChangeListener(holder.onPickGoalRadioButtonClicked());


        Button glucose_pick_goal_create_button = (Button)view.findViewById(R.id.glucose_pick_goal_create_button);
        View.OnClickListener create_goal_listerner = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context c = view.getContext();
                FragmentTransaction ft = ((Activity) c).get .getSupportFragmentManager().beginTransaction();
                ft.add(R.layout.priority_fragment, new GoalFragment());

            }
        };
        glucose_pick_goal_create_button.setOnClickListener(create_goal_listerner);

        //holder.weightPickGoalCreateButton.setOnClickListener(create_goal_listerner);
        return view;
    }



    @Override
    public int getViewType() {
        return 0;
    }


    private static class ViewHolder {
        private final LayoutInflater inflater;
        final LinearLayout pickGoalCardSelector;
        final TextView cardMessage;
        final TextView weightGoalMessage;
        final RadioButton weightGoalY;
        final RadioButton weightGoalN;
        final RadioGroup weightCheck;
        final LinearLayout weightGoalCreate;
        private Character WeightCheckVal = null;
        final TextView bpGoalMessage;
        final RadioButton bpGoalY;
        final RadioButton bpGoalN;
        final RadioGroup bpCheck;
        final LinearLayout bpGoalCreate;
        private Character bpCheckVal = null;

        final TextView glucoseGoalMessage;
        final RadioButton glucoseGoalY;
        final RadioButton glucoseGoalN;
        final RadioGroup glucoseCheck;
        final LinearLayout glucoseGoalCreate;
        private Character glucoseCheckVal = null;
        final TextView cholesterolGoalMessage;
        final RadioButton cholesterolGoalY;
        final RadioButton cholesterolGoalN;
        final RadioGroup cholesterolCheck;
        final LinearLayout cholesterolGoalCreate;
        private Character cholesterolCheckVal = null;

        private final Button weightPickGoalCreateButton;
        private final Button bpPickGoalCreateButton;
        private final Button glucosePickGoalCreateButton;
        private final Button cholesterolPickGoalCreateButton;

        private ViamHealthPrefs appPrefs;

        private ViewHolder( ViamHealthPrefs appPrefs,LayoutInflater inflater,
                LinearLayout pickGoalCardSelector, TextView cardMessage,
                            TextView weightGoalMessage, RadioButton weightGoalY, RadioButton weightGoalN, RadioGroup weightCheck,
                            LinearLayout weightGoalCreate,
                            TextView bpGoalMessage, RadioButton bpGoalY, RadioButton bpGoalN, RadioGroup bpCheck,
                            LinearLayout bpGoalCreate,
                            TextView glucoseGoalMessage, RadioButton glucoseGoalY, RadioButton glucoseGoalN, RadioGroup glucoseCheck,
                            LinearLayout glucoseGoalCreate,
                            TextView cholesterolGoalMessage, RadioButton cholesterolGoalY, RadioButton cholesterolGoalN, RadioGroup cholesterolCheck,
                            LinearLayout cholesterolGoalCreate,
                            Button weight_pick_goal_create_button, Button cholesterol_pick_goal_create_button, Button bp_pick_goal_create_button, Button glucose_pick_goal_create_button
                            ) {
            this.inflater = inflater;
            this.appPrefs = appPrefs;
            this.cardMessage = cardMessage;
            this.weightGoalMessage = weightGoalMessage;
            this.weightGoalY = weightGoalY;
            this.weightGoalN = weightGoalN;
            this.weightCheck = weightCheck;
            this.weightGoalCreate = weightGoalCreate;
            this.bpGoalMessage = bpGoalMessage;
            this.bpGoalY = bpGoalY;
            this.bpGoalN = bpGoalN;
            this.bpCheck = bpCheck;
            this.bpGoalCreate = bpGoalCreate;

            this.glucoseGoalMessage = glucoseGoalMessage;
            this.glucoseGoalY = glucoseGoalY;
            this.glucoseGoalN = glucoseGoalN;
            this.glucoseCheck = glucoseCheck;
            this.glucoseGoalCreate = glucoseGoalCreate;
            this.cholesterolGoalMessage = cholesterolGoalMessage;
            this.cholesterolGoalY = cholesterolGoalY;
            this.cholesterolGoalN = cholesterolGoalN;
            this.cholesterolCheck = cholesterolCheck;
            this.cholesterolGoalCreate = cholesterolGoalCreate;
            this.pickGoalCardSelector =  pickGoalCardSelector;
            this.weightPickGoalCreateButton = weight_pick_goal_create_button;
            this.bpPickGoalCreateButton = bp_pick_goal_create_button;
            this.cholesterolPickGoalCreateButton = cholesterol_pick_goal_create_button;
            this.glucosePickGoalCreateButton = glucose_pick_goal_create_button;

        }

        public RadioGroup.OnCheckedChangeListener onPickGoalRadioButtonClicked(){
            final ViewHolder p = this;
            return new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch(i){
                        case R.id.weight_pick_goal_Y:
                            p.WeightCheckVal = 'Y';
                            break;
                        case R.id.weight_pick_goal_N:
                            p.WeightCheckVal = 'N';
                            break;
                        case R.id.bp_pick_goal_Y:
                            p.bpCheckVal = 'Y';
                            break;
                        case R.id.bp_pick_goal_N:
                            p.bpCheckVal = 'N';
                            break;
                        case R.id.glucose_pick_goal_Y:
                            p.glucoseCheckVal = 'Y';
                            break;
                        case R.id.glucose_pick_goal_N:
                            p.glucoseCheckVal = 'N';
                            break;
                        case R.id.cholesterol_pick_goal_Y:
                            p.cholesterolCheckVal = 'Y';
                            break;
                        case R.id.cholesterol_pick_goal_N:
                            p.cholesterolCheckVal = 'N';
                            break;
                        default:
                            break;
                    }

                    checkComplete();
                }
                private void checkComplete(){
                    //if(p.WeightCheckVal != null && p.bpCheckVal != null){
                        p.onAllOptionsChecked();
                    //}
                }
            };
        }

        private void onAllOptionsChecked() {
            if(WeightCheckVal == null || cholesterolCheckVal == null || glucoseCheckVal == null|| bpCheckVal == null)
                return;

            System.out.println("Weight check complete");
            System.out.println(WeightCheckVal);
            if(  WeightCheckVal =='Y' ){
                this.appPrefs.setSSPCWGC("Y");
                this.weightGoalCreate.setVisibility(View.VISIBLE);
            }
            else {
                this.appPrefs.setSSPCWGC("N");
            }
            if(  cholesterolCheckVal =='Y' ){
                this.appPrefs.setSSPCCGC("Y");
                this.cholesterolGoalCreate.setVisibility(View.VISIBLE);
            }
            else {
                this.appPrefs.setSSPCCGC("N");
            }
            if(  glucoseCheckVal =='Y' ){
                this.appPrefs.setSSPCGGC("Y");
                this.glucoseGoalCreate.setVisibility(View.VISIBLE);
            }
            else {
                this.appPrefs.setSSPCGGC("N");
            }
            if(  bpCheckVal =='Y' ){
                this.appPrefs.setSSPCBGC("Y");
                this.bpGoalCreate.setVisibility(View.VISIBLE);
            }
            else {
                this.appPrefs.setSSPCBGC("N");
            }

            this.pickGoalCardSelector.setVisibility(View.GONE);

                //System.out.println("making invisible");
                //this.weightGoalCreate.setVisibility(View.GONE);
                //this.bpGoalCreate.setVisibility(View.GONE);



        }
    }
}
