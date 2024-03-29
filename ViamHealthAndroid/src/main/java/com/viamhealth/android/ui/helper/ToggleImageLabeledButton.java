package com.viamhealth.android.ui.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.viamhealth.android.R;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by naren on 16/10/13.
 */
public class ToggleImageLabeledButton extends ImageView {

    private int imageOn;
    private int imageOff;
    private AtomicBoolean on = new AtomicBoolean(false);

    public ToggleImageLabeledButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.image_labeled_button, 0, 0);
            imageOn = a.getResourceId(R.styleable.image_labeled_button_icon_resource, 0);
            a = context.obtainStyledAttributes(attrs, R.styleable.toggle_image_labeled_button, 0, 0);
            imageOff = a.getResourceId(R.styleable.toggle_image_labeled_button_icon_resource_off, 0);
            setImageResource(imageOff);
        }

    }

    private void handleNewState(boolean newState) {
        if (newState) {
            setImageResource(imageOn);
        } else {
            setImageResource(imageOff);
        }
    }

    @Override
    public void setOnClickListener(final OnClickListener l) {
        OnClickListener wrappingListener = new OnClickListener() {

            public void onClick(View v) {
                boolean newState = !on.get();
                on.set(newState);
                handleNewState(newState);
                l.onClick(v);
                }

        };

        super.setOnClickListener(wrappingListener);
    }

    public void setState(boolean b) {
        on.set(b);
        handleNewState(b);
    }

}
