package com.screenable.agora.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.screenable.agora.R;
import com.screenable.agora.config.Config;
import com.screenable.agora.helpers.Helpers;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class TrendingProducts extends RecyclerView.Adapter<TrendingProducts.ViewHolder> {
    Context context;
    ArrayList<HashMap<String,String>> items;
    public TrendingProducts(Context context, ArrayList <HashMap<String,String>> items){
        this.context = context;
        this.items = items;

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingProducts.ViewHolder holder, int position) {
        String productId = items.get(position).get("productId");
        String productName = items.get(position).get("productName");
        String vendor = items.get(position).get("businessName");
        String price = items.get(position).get("price");
        holder.imageView.setImageURI(Config.productImages+"?productId="+productId);

        holder.name.setText(productName);
        holder.vendor.setText(vendor);
        holder.price.setText(Helpers.priceFormat(price));
    }

    @NonNull
    @Override
    public TrendingProducts.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ll, parent, false);
        return new TrendingProducts.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        TextView price;
        TextView vendor;
        SimpleDraweeView imageView;
        ImageView add;
        public ViewHolder(View itemView){
            super(itemView);
            vendor = itemView.findViewById(R.id.vendor);
            price = itemView.findViewById(R.id.price);
            name = itemView.findViewById(R.id.product_name);
            imageView = itemView.findViewById(R.id.item_image);
            add = itemView.findViewById(R.id.add_to_cart);


        }

        @Override
        public void onClick(View view) {
            if(view.getId()==add.getId()){

            }
        }
    }
}