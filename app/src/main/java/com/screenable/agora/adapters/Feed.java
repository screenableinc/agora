package com.screenable.agora.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.facebook.drawee.view.SimpleDraweeView;
import com.screenable.agora.R;
import com.screenable.agora.config.Config;
import com.screenable.agora.customviews.ProductHolder;
import com.screenable.agora.customviews.ProductVariations;
import com.screenable.agora.helpers.Helpers;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.screenable.agora.config.Config.APP_IDENT;

public class Feed extends RecyclerView.Adapter<Feed.ViewHolder> {
    Context context;
    ArrayList<HashMap<String,String>> items;
    public Feed(Context context, ArrayList <HashMap<String,String>> items){
        this.context = context;
        this.items = items;

    }

    @Override
    public int getItemCount() {
        Log.w(APP_IDENT, items.size()+" Wise check this");
        return items.size();
    }


    @Override
    public void onBindViewHolder(@NonNull Feed.ViewHolder holder, int position) {
//        oriduct information
        String productId = items.get(position).get("productId");
        String productName = items.get(position).get("productName");
        String vendor = items.get(position).get("businessName");
        String vendorId = items.get(position).get("vendorId");
        String price = items.get(position).get("price");
        Log.w(APP_IDENT, "Wise check this, binded "+position);

//        TODO://change this to hashmap or JSON array to deal with dynamic number of images
        String[] links={Config.productImages+"?productId="+productId,Config.productImages+"?productId="+productId};
        holder.images.setAdapter(new Products(links,context));



        holder.name.setText(productName);
        holder.vendor.setText(vendor);
        holder.price.setText(items.get(position).get("symbol") + Helpers.priceFormat(price));

//        deal with cart issues
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    add cart options to root view

                ViewGroup mine = (ViewGroup) view.getRootView();
                LayoutInflater layoutInflater = LayoutInflater.from(context);


                View cart= layoutInflater.inflate(R.layout.add_cart_options,null,false);
                if(mine.getChildCount()==3) {
                    CircleImageView circleImageView = cart.findViewById(R.id.product_img);
                    View add = cart.findViewById(R.id.addtocart);
                    ((TextView) cart.findViewById(R.id.product_name)).setText(productName);
                    Picasso.get().load(Config.productImages+"?productId="+productId).into(circleImageView);
//                    circleImageView.setImageDrawable(imageView.getDrawable());
                    LinearLayout attr_list_parent = cart.findViewById(R.id.attr_list_parent);

                    attr_list_parent.addView(new ProductVariations(context,productId,vendorId, cart));
                    mine.addView(cart);




                    Log.w(APP_IDENT, mine.toString());
                }
            }
        });
    }

    @NonNull
    @Override
    public Feed.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.product_item,null,false);
        View view = (View) new ProductHolder(context,null,parent);

        return new Feed.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        TextView price;
        TextView vendor;
        ViewPager2 images;
        ImageView add;
        public ViewHolder(View itemV){
            super(itemV);
            View itemView = (ProductHolder) itemV;
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