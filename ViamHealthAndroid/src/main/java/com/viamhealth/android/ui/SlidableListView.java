package com.viamhealth.android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by naren on 22/10/13.
 */
public class SlidableListView extends RefreshableListView {

    OnSlideEventListener listener;
    float historicX = Float.NaN, historicY = Float.NaN;
    static final int TRIGGER_DELTA = 50;

    public void setOnSlideEventListener(OnSlideEventListener listener) {
        this.listener = listener;
    }

    public SlidableListView(Context context) {
        super(context);
    }

    public SlidableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(listener!=null) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    historicX = e.getX();
                    historicY = e.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    if (e.getX() - historicX > -TRIGGER_DELTA) {
                        listener.onSlideComplete(Direction.LEFT);
                        return true;
                    }
                    else if (e.getX() - historicX > TRIGGER_DELTA)  {
                        listener.onSlideComplete(Direction.RIGHT);
                        return true;
                    } break;
                default:
                    return super.onTouchEvent(e);
            }
        }
        return true;//super.onTouchEvent(e);
    }

    public enum Direction { RIGHT, LEFT; }

    public interface OnSlideEventListener {

        public void onSlideComplete(Direction whichDirection);

    }
}
