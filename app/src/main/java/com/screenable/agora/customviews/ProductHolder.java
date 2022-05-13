package com.screenable.agora.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.screenable.agora.R;
import com.screenable.agora.config.Config;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ProductHolder extends LinearLayout {
    ViewPager2 viewPager2;
    LinearLayout pagination;

    public ProductHolder(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        inflate(context,R.layout.product_item,this);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        init(context, attributeSet);






    }
    public void setAdapter(RecyclerView.Adapter adapter){
        viewPager2.setAdapter(adapter);
    }

    public void rigCounter(int slideCount){
//       paginate with dots
        for (int i = 0; i < slideCount; i++) {
            FrameLayout frameLayout = new FrameLayout(getContext());
            frameLayout.setBackground(getResources().getDrawable(R.drawable.scan_btn));
            LayoutParams params = new LayoutParams(18,18);
            params.rightMargin=20;
            frameLayout.setLayoutParams(params);
            frameLayout.setAlpha(0.4f);
            pagination.addView(frameLayout);
        }
    }
    private void init(Context context, AttributeSet attributeSet){

        viewPager2 = findViewById(R.id.pager);
        pagination = findViewById(R.id.pagination);
        viewPager2.setOffscreenPageLimit(3);


        float pageMargin = getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);

        viewPager2.setPageTransformer((page, position) -> {
            Log.w(Config.APP_IDENT,page+"____"+position);
            float myOffset = position * -(2 * pageOffset + pageMargin);
            if (position < -1) {
                page.setTranslationX(-myOffset);
            } else if (position <= 1) {
                float scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f));
                page.setTranslationX(myOffset);
                page.setScaleY(scaleFactor);
                page.setAlpha(scaleFactor);
            } else {
                page.setAlpha(0);
                page.setTranslationX(myOffset);
            }
        });

    }





}
