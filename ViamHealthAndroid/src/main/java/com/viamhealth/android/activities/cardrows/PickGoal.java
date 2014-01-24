package com.viamhealth.android.activities.cardrows;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.viamhealth.android.R;
import com.viamhealth.android.model.cards.PriorityCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunal on 11/1/14.
 */
public class PickGoal implements CardRow {

    private final PriorityCard card;
    private final LayoutInflater inflater;

    public PickGoal(LayoutInflater inflater,PriorityCard card) {
        this.card = card;
        this.inflater = inflater;
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
                    (LinearLayout)viewGroup.findViewById(R.id.pick_goal_card_selector),
                    (TextView)viewGroup.findViewById(R.id.pick_goal_card_message),
                    (TextView)viewGroup.findViewById(R.id.weight_pick_goal_message),
                    (RadioButton)viewGroup.findViewById(R.id.weight_pick_goal_Y),
                    (RadioButton)viewGroup.findViewById(R.id.weight_pick_goal_N),
                    (RadioGroup)viewGroup.findViewById(R.id.weight_pick_goal_check),
                    (LinearLayout)viewGroup.findViewById(R.id.weight_pick_goal_create_layout)
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


        //holder.onAllOptionsChecked();
        //holder.weightGoalY.setOnClickListener(holder.onPickGoalRadioButtonClicked());
        //holder.weightGoalN.setOnClickListener(holder.onPickGoalRadioButtonClicked());


        return view;
    }



    @Override
    public int getViewType() {
        return 0;
    }


    private static class ViewHolder {
        final LinearLayout pickGoalCardSelector;
        final TextView cardMessage;
        final TextView weightGoalMessage;
        final RadioButton weightGoalY;
        final RadioButton weightGoalN;
        final RadioGroup weightCheck;
        final LinearLayout weightGoalCreate;
        private Integer checkCounter = 0;
        private Character WeightCheckVal = null;

        private ViewHolder( LinearLayout pickGoalCardSelector, TextView cardMessage,
                            TextView weightGoalMessage, RadioButton weightGoalY, RadioButton weightGoalN, RadioGroup weightCheck,
                            LinearLayout weightGoalCreate) {
            this.cardMessage = cardMessage;
            this.weightGoalMessage = weightGoalMessage;
            this.weightGoalY = weightGoalY;
            this.weightGoalN = weightGoalN;
            this.weightCheck = weightCheck;
            this.weightGoalCreate = weightGoalCreate;
            this.pickGoalCardSelector =  pickGoalCardSelector;
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
                        default:
                            break;
                    }
                    checkComplete();
                }
                private void checkComplete(){
                    if(p.WeightCheckVal != null){
                        p.onAllOptionsChecked();
                    }
                }
            };
        }

        private void onAllOptionsChecked() {
            if(WeightCheckVal == null)
                return;

            System.out.println("Weight check complete");
            System.out.println(WeightCheckVal);
            if(  WeightCheckVal =='Y'){
                this.weightGoalCreate.setVisibility(View.VISIBLE);
            } else {
                System.out.println("making invisible");
                this.weightGoalCreate.setVisibility(View.GONE);
            }
            this.pickGoalCardSelector.setVisibility(View.GONE);

        }
    }
}
