package com.screenable.agora.customviews;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.telephony.mbms.StreamingServiceInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.screenable.agora.R;
import com.screenable.agora.config.Config;
import com.screenable.agora.ui.main.activities.Store;

public class VendorTextView extends AppCompatTextView{
    String vendorId;
    OnClickListener listener;
    ImageView logo;
    TextView company_name;
    Pair<View,String> transitionPair;
    ActivityOptions optionsForTransition;

    public VendorTextView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.setClickable(true);
        this.setTextColor(getResources().getColor(R.color.colorAccent));
        this.setTypeface(Typeface.DEFAULT_BOLD);


        TypedArray array = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.VendorTextView,0,0);
        try{
            vendorId = array.getString(R.styleable.VendorTextView_vendorId);






        }finally {
            array.recycle();
        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        logo = findViewById(R.id.item_image);
        company_name = findViewById(R.id.vendor_name);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                fire();
            }
        });


    }
    @NonNull
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;


    }

    public void setOptionsForTransition(ActivityOptions optionsForTransition) {
        this.optionsForTransition = optionsForTransition;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(listener != null) listener.onClick(this);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_UP && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if(listener != null) listener.onClick(this);
        }
        return super.dispatchKeyEvent(event);
    }




    public void fire() {
//        send to view window
        Intent intent = new Intent(getContext(), Store.class);
        intent.putExtra("vendorId", vendorId);
        Log.w(Config.APP_IDENT,company_name+"________"+logo);
        if(optionsForTransition!=null) {



            getContext().startActivity(intent, optionsForTransition.toBundle());
        }else {
            getContext().startActivity(intent);
        }
    }
}
