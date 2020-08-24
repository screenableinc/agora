package com.screenable.agora.customviews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.screenable.agora.R;
import com.screenable.agora.config.Config;

public class AttrFrameLayout extends FrameLayout {
    public AttrFrameLayout(Context context, AttributeSet attrSet){
        super(context, attrSet);
//        look for touch on child
        ViewGroup myView = this;
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup vg = (ViewGroup)myView.getParent();
        try{
            if(view.getId()!=R.id.child){
                vg.removeView(myView);
            }
        }catch (NullPointerException e){

        }
            }
        });


    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//
//        ViewGroup vg = (ViewGroup)this.getParent();
//        try{
//        vg.removeView(this);}catch (NullPointerException e){
//
//        }
//        return false;
//    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
//        begin animation
        ValueAnimator animator = ValueAnimator.ofFloat(0f,1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float atValue = (float) valueAnimator.getAnimatedValue();
                child.setAlpha(atValue);
            }

        });
        animator.setDuration(500);
        animator.start();
        child.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });
    }
}
