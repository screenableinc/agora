package com.screenable.agora.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.screenable.agora.R;
import com.screenable.agora.config.Config;

import java.util.ArrayList;

public class VariationOption extends FrameLayout {
    public  Boolean isClicked = false;
    public String Identifier="";
    public ArrayList<String> eligibleIds;

    public VariationOption(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }
    public VariationOption(Context context){
        super(context);
        init(context);

    }

    public void setEligibleIds(ArrayList<String> eligibleIds) {
        this.eligibleIds = eligibleIds;
    }
    public ArrayList<String> getEligibleIds(){
        return this.eligibleIds;
    }

    private void init(Context context){
        inflate(context, R.layout.option,(ViewGroup) this.getRootView());
        setBackground(getResources().getDrawable(R.drawable.attr_bg));



    }

    public void setIdentifier(String value){
        Identifier=value;
    }
    public String getIdentifier(){
        return Identifier;
    }
    public void toggleClick(){
        if(isClicked){
            setIsClicked(false);

        }else{
            setIsClicked(true);

        }
//        setIsClicked(isClicked);
    }
    public void disable(){
//        remove on click listener
        this.setClickable(false);
//        grayout option
        this.setBackground(getResources().getDrawable(R.drawable.disabled));
        ((TextView) this.findViewById(R.id.attr)).setTextColor(getResources().getColor(R.color.colorPrimaryDark));

    }
    public void enable(){
//        set onclick listener in calling function
        this.setClickable(true);

        this.setBackground(getResources().getDrawable(R.drawable.attr_bg));
        ((TextView) this.findViewById(R.id.attr)).setTextColor(getResources().getColor(R.color.colorPrimaryDark));


    }
    public Boolean getIsClicked(){
        return isClicked;
    }

    public void setIsClicked(Boolean bool){
//        might be repetitive, too tired to check
        isClicked = bool;
// add animation here
        if(isClicked){
//            clicked is being set to true, add background


            this.setBackground(getResources().getDrawable(R.drawable.attr_selected_bg));
            ((TextView) this.findViewById(R.id.attr)).setTextColor(getResources().getColor(R.color.gmm_white));
        }else{
            this.setBackground(getResources().getDrawable(R.drawable.attr_bg));
            ((TextView) this.findViewById(R.id.attr)).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }


    }

}
