package com.viamhealth.android.activities.cardrows;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viamhealth.android.R;
import com.viamhealth.android.model.cards.PriorityCard;

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
        //we have a don't have a converView so we'll have to create a new one

        if (convertView == null) {
            ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.priority_card_pick_goal, null);

            //use the view holder pattern to save of already looked up subviews
            holder = new ViewHolder(
                    (TextView)viewGroup.findViewById(R.id.card_message),
                    (TextView)viewGroup.findViewById(R.id.goal_1)
            );
            viewGroup.setTag(holder);
            view = viewGroup;
        } else {
            //get the holder back out
            holder = (ViewHolder)convertView.getTag();
            view = convertView;
        }

        //actually setup the view
        //holder.cardMessage.setText(card.getMessage());
        //holder.goal1Text.setText(card.getGoal1Text());

        return view;
    }

    @Override
    public int getViewType() {
        return 0;
    }

    private static class ViewHolder {
        final TextView cardMessage;
        final TextView goal1Text;

        private ViewHolder( TextView cardMessage, TextView goal1Text) {
            this.cardMessage = cardMessage;
            this.goal1Text = goal1Text;
        }
    }
}
