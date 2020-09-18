package com.screenable.agora.customviews;

import android.content.Context;
import android.graphics.ColorSpace;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.screenable.agora.R;

public class VariationOptionsLL extends LinearLayout {
    String selected=null;
    String name;
    View currentlySelected;

    public VariationOptionsLL(Context context, AttributeSet attrSet){
        super(context,attrSet);
        init(context,attrSet);
    }
    private void init(Context context, AttributeSet attrSet){
        inflate(context, R.layout.option,this);

        initComponents();
    }

    @Override
    public void setTag(Object tag) {
        name=tag.toString();
    }

    private void initComponents(){

    }
    private void removeBG(View view){

        view.setBackgroundResource(R.drawable.attr_bg);
        ViewGroup parent = (ViewGroup) view;

        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }
    private void addBG(View view){
        view.setBackgroundResource(R.drawable.attr_selected_bg);
        ViewGroup parent = (ViewGroup) view;
        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.gmm_white));

    }
    public void setSelectedView(View view){
//        remove background from current selected
        if(currentlySelected!=null){
            removeBG(currentlySelected);
        }
        ViewGroup parent = (ViewGroup) view;

        selected = ((TextView) parent.getChildAt(0)).getText().toString();

//        selected = ((TextView) view).getText().toString();
//        set currently selected to new view and add bg
        currentlySelected=view;
        addBG(view);
    }

    public String getSelected() {
        return this.selected;
    }




}
