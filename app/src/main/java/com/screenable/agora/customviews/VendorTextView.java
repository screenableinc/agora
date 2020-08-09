package com.screenable.agora.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class VendorTextView extends AppCompatTextView {

    public VendorTextView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
//        send to view window

    }
}
