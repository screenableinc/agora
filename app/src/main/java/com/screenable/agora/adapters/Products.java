package com.screenable.agora.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.screenable.agora.R;
import com.screenable.agora.config.Config;
import com.squareup.picasso.Picasso;

public class Products extends RecyclerView.Adapter<Products.VH>{
    String[] links;
    Context context;
    public Products(String [] links, Context context){
        this.links = links;
        this.context = context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        parent = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.product_holder,parent, false);

        return new VH(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
//        change data here
        Picasso.get().load(links[position]).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return links.length;
    }

    protected class VH extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView productImage;
        public VH(View view){
            super(view);
            productImage = view.findViewById(R.id.pager_image);

        }
        @Override
        public void onClick(View view) {

        }
    }
}
//        {
////        String[] links;
////        Activity activity;
////        LayoutInflater mLayoutInflater;
////    public Products(String[]links, Activity activity){
////
////        this.links = links;
////        this.activity = activity;
////        mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////    }
////        @Override
////        public int getCount () {
////        return links.length;
////    }
////        @SuppressLint("NewApi")
////        @Override
////        public void finishUpdate (ViewGroup container){
////        // TODO Auto-generated method stub
////        super.finishUpdate(container);
////
////    }
////
////        @NonNull
////        @Override
////        public Object instantiateItem (@NonNull ViewGroup container,final int position){
////        LayoutInflater inflater = (LayoutInflater) activity
////                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////        Log.w(Config.APP_IDENT, "Should have instantiated");
////        View parentView = mLayoutInflater.inflate(R.layout.product_holder, container, false);
////
////
//////            ImageView image = (ImageView) parentView.findViewById(R.id.pager_image);
////////            load image from server
//////            Picasso.get().load(links[position]).into(image);
//////            add to view
////        ((ViewGroup) container).addView(parentView, 0);
////        Toast.makeText(container.getContext(), "should have instantiated__" + container.getChildCount(), Toast.LENGTH_LONG).show();
////
////        return container;
////    }
////
////
////        @Override
////        public boolean isViewFromObject (@NonNull View view, @NonNull Object object){
////        return view == object;
////    }
////
////        @Override
////        public void destroyItem (@NonNull ViewGroup container,int position, @NonNull Object object){
////        ((ViewPager) container).removeView((View) object);
////    }
//        }