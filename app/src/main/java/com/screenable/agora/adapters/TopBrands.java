package com.screenable.agora.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Pair;
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
import com.screenable.agora.ui.main.activities.Product;
import com.screenable.agora.ui.main.activities.Store;

import java.util.ArrayList;
import java.util.HashMap;

public class TopBrands extends RecyclerView.Adapter<TopBrands.ViewHolder> {
    Context context;
    ArrayList <HashMap<String,String>> items;
    public TopBrands(Context context, ArrayList <HashMap<String,String>> items){
        this.context = context;
        this.items = items;

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String businessId = items.get(position).get("businessId");
        String businessName = items.get(position).get("businessName");
        String mainCategory = items.get(position).get("businessId");
        holder.imageView.setImageURI(Config.businessLogo+"?businessId="+businessId);
        holder.data=items.get(position);
        holder.view.setText(businessName);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_text, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView view;
        SimpleDraweeView imageView;
        HashMap<String, String> data;
        public ViewHolder(View itemView){
            super(itemView);
            view = itemView.findViewById(R.id.descr);
            imageView = itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, Store.class);
            intent.putExtra("data", data);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context,
                    Pair.create((View) view, "vendor_name"),
                    Pair.create((View) imageView, "logo"));

            context.startActivity(intent, options.toBundle());
        }
    }
}
