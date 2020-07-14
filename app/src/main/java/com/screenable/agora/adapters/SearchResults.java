package com.screenable.agora.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.screenable.agora.R;
import com.screenable.agora.config.Config;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchResults extends RecyclerView.Adapter<SearchResults.ViewHolder> {
    Context context;
    ArrayList<HashMap<String,String>> items;
    public SearchResults(Context context, ArrayList <HashMap<String,String>> items){
        this.context = context;
        this.items = items;

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResults.ViewHolder holder, int position) {
        String productId = items.get(position).get("productId");
        String productName = items.get(position).get("productName");
        String price = items.get(position).get("price");
        holder.imageView.setImageURI(Config.productImages+"?productId="+productId);
        holder.price.setText(price);
        holder.name.setText(productName);
    }

    @NonNull
    @Override
    public SearchResults.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_result, parent, false);
        return new SearchResults.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        SimpleDraweeView imageView;
        TextView price;

        public ViewHolder(View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.price);
            name = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.item_image);

        }

        @Override
        public void onClick(View view) {

        }
    }
}