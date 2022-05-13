package com.screenable.agora.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.facebook.drawee.view.SimpleDraweeView;
import com.screenable.agora.R;
import com.screenable.agora.config.Config;
import com.screenable.agora.customviews.ProductHolder;
import com.screenable.agora.helpers.Helpers;

import java.util.ArrayList;
import java.util.HashMap;

public class Feed extends RecyclerView.Adapter<Feed.ViewHolder> {
    Context context;
    ArrayList<HashMap<String,String>> items;
    public Feed(Context context, ArrayList <HashMap<String,String>> items){
        this.context = context;
        this.items = items;

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull Feed.ViewHolder holder, int position) {
        String productId = items.get(position).get("productId");
        String productName = items.get(position).get("productName");
        String vendor = items.get(position).get("businessName");
        String price = items.get(position).get("price");

//        change this to hashmap or JSON array
        String[] links={Config.productImages+"?productId="+productId};
        holder.images.setAdapter(new Products(links,context));


        holder.name.setText(productName);
        holder.vendor.setText(vendor);
        holder.price.setText(Helpers.priceFormat(price));
    }

    @NonNull
    @Override
    public Feed.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new Feed.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        TextView price;
        TextView vendor;
        ViewPager2 images;
        ImageView add;
        public ViewHolder(View itemView){
            super(itemView);
            itemView = (ProductHolder) itemView;
            vendor = itemView.findViewById(R.id.the_vendor);
            price = itemView.findViewById(R.id.product_price);
            name = itemView.findViewById(R.id.productName);
            images = itemView.findViewById(R.id.pager);
            add = itemView.findViewById(R.id.add_to_cart);


        }

        @Override
        public void onClick(View view) {
            if(view.getId()==add.getId()){

            }
        }
    }
}