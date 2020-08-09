package com.screenable.agora.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.screenable.agora.R;
import com.screenable.agora.config.Config;
import com.screenable.agora.helpers.Helpers;
import com.screenable.agora.ui.main.activities.Product;
import com.screenable.agora.ui.main.fragments.Home;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchResults extends RecyclerView.Adapter<SearchResults.ViewHolder> {
    Context context;
    ArrayList<HashMap<String,String>> items;
    String which;
    public SearchResults(Context context, ArrayList <HashMap<String,String>> items, String which){
        this.context = context;
        this.items = items;
        this.which = which;

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
        String timestamp = items.get(position).get("timestamp");
        Picasso.get().load(Config.productImages+"?productId="+productId).into(holder.imageView);
        holder.price.setText( items.get(position).get("symbol") + Helpers.priceFormat(price));
        holder.name.setText(productName);
        holder.data = items.get(position);


//        if(position+1==items.size() && which.equals("discover")){
////            on last position....load more
//            new Home.Additional(timestamp).execute();
//        }
//




    }

    @NonNull
    @Override
    public SearchResults.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product, parent, false);
        return new SearchResults.ViewHolder(view);
    }

    public void animateLike(ViewGroup viewGroup){
        viewGroup.removeViewAt(0);
        LottieAnimationView animationView = (LottieAnimationView) LayoutInflater.from(context).inflate(R.layout.lottie,viewGroup,false);

//        LottieAnimationView animationView = new LottieAnimationView(context);
////        animationView.setAnimation("like.json");
//        animationView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
        animationView.setAnimation(R.raw.like3);
        viewGroup.addView(animationView,0);
        animationView.loop(false);
        animationView.playAnimation();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        ImageView imageView;
        TextView price;
        HashMap<String, String> data;
        LinearLayout parent;
        ImageView add;
        ViewGroup likeParent;

        public ViewHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            price = itemView.findViewById(R.id.price);
            name = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.item_image);
            add = itemView.findViewById(R.id.add_to_cart);
            likeParent=itemView.findViewById(R.id.like_parent);

            likeParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    animateLike(likeParent);
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Product.class);
                    intent.putExtra("data", data);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context,
                            Pair.create((View) name, "productname"),
                            Pair.create((View) imageView, "product_image"));

                    context.startActivity(intent, options.toBundle());
                }
            });
//            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if(view.getId()==add.getId()) {
//                addToCart
            }else {
                Log.w(Config.APP_IDENT, "clicked");

            }

        }
    }
}