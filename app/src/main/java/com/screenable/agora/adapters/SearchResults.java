package com.screenable.agora.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.renderscript.Script;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.screenable.agora.R;
import com.screenable.agora.config.Config;
import com.screenable.agora.customviews.VariationOptionsLL;
import com.screenable.agora.helpers.Helpers;
import com.screenable.agora.ui.main.activities.Product;
import com.screenable.agora.ui.main.fragments.Home;
import com.screenable.agora.ui.main.fragments.Search;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.screenable.agora.config.Config.APP_IDENT;

public class SearchResults extends RecyclerView.Adapter<SearchResults.ViewHolder> {
    Context context;
    ArrayList<HashMap<String,String>> items;
    String which;
    ArrayList <Integer> holders = new ArrayList<>();
    JSONArray remainingCombinations = new JSONArray();
    JSONArray attrNames;
//
    public int size;
//    here, cart represents the dialogue to select variatnts
    View cart;
    private ArrayList<HashMap<String,String>> selected;
    public SearchResults(Context context, ArrayList <HashMap<String,String>> items, String which, int size){
//        size refers to the preferred width of the cardview 0 for small one for largee
        this.context = context;
        this.items = items;
        this.which = which;
        this.size = size;

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
        View view;
        Log.w(APP_IDENT,size +";;;;;;;");
        if(size==1) {
            view = LayoutInflater.from(context).inflate(R.layout.product, parent, false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.product_small, parent, false);
        }
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
    private void filter(VariationOptionsLL clicked,JSONArray variations, String attr) throws JSONException{
//        when one attribute is clicked, filter through the data and figure out the unclicked items
        String selectedAttr = clicked.getSelected();



        for (int holder:
             holders) {
            VariationOptionsLL holder_ll = (VariationOptionsLL) cart.findViewById(holder);
           if(holder_ll.getSelected()==null){
//               reload attributes except this time with if clause
               holder_ll.removeAllViews();
               for (int i = 0; i < variations.length(); i++) {
                   if(variations.getJSONObject(i).get(attr)==selectedAttr){
                       remainingCombinations.put(variations.getJSONObject(i));
                   }else {
                       remainingCombinations.remove(i);
                   }
               }
               loadCategoryOptions(holder_ll, remainingCombinations,attr,true);
           }

        }
//        reload



    }


    private void loadCategoryOptions(VariationOptionsLL hr_ll, JSONArray variations, String attr, Boolean hasFilter) throws JSONException {
        //                keep track of attibutes already added
        ArrayList<String> attrlist = new ArrayList<String>();
        if (!hasFilter) {
            for (int i = 0; i < variations.length(); i++) {
                JSONObject variation = variations.getJSONObject(i);
                String text = variation.getString(attr);
                FrameLayout attr_view = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.option, hr_ll, false);
                if (!attrlist.contains(text)) {


                    attr_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                hr_ll.setSelectedView(view);
                                filter(hr_ll, variations, attr);
                                Log.w(APP_IDENT, "clidked");
                            } catch (JSONException e) {
                                Toast.makeText(context, "err", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    ((TextView) attr_view.findViewById(R.id.attr)).setText(text);
                    hr_ll.addView(attr_view);
                    attrlist.add(text);
                }
            }
        } else {
            Log.w(APP_IDENT, variations.toString());
            for (int j = 0; j < attrNames.length(); j++) {

                if(!attr.equals(attrNames.getString(j))) {


                    for (int i = 0; i < variations.length(); i++) {
                        JSONObject variation = variations.getJSONObject(i);
                        String text = variation.getString(attrNames.getString(j));
                        FrameLayout attr_view = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.option, hr_ll, false);


                        if (!attrlist.contains(text)) {



                            ((TextView) attr_view.findViewById(R.id.attr)).setText(text);
                            attr_view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        hr_ll.setSelectedView(view);
                                        filter(hr_ll, remainingCombinations, attr);


                                    } catch (JSONException e) {
                                        Toast.makeText(context, "err", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            hr_ll.addView(attr_view);
                            attrlist.add(text);

                        }
                    }
                }
            }
    }


    }
    private void createAttributeCategories(LinearLayout attr_parent,JSONArray variations) throws JSONException{

        for (int j = 0; j < attrNames.length(); j++) {
            String attr=attrNames.getString(j);
            if(!attr.toLowerCase().equals("qty")){
//                        add to view
                View view = LayoutInflater.from(context).inflate(R.layout.attribute_opts,null,false);
                TextView attr_name = view.findViewById(R.id.attr_name);
                VariationOptionsLL hr_ll = view.findViewById(R.id.attrs);
                hr_ll.setTag(attr);
                int id = hr_ll.getId()+j;
                hr_ll.setId(id);
                attr_name.setText("select "+attr);
                attr_name.setAllCaps(true);
                Log.w(APP_IDENT,hr_ll.getId()+"oo");
                holders.add(id);
//                        add actual attrs

                attr_parent.addView(view);

                loadCategoryOptions(hr_ll,variations,attr,false);



            }


        }
    }

    public void loadAttributes(View attributeView, HashMap<String, String> data){
        ((TextView) attributeView.findViewById(R.id.product_name)).setText(data.get("productName"));
        LinearLayout attr_parent = attributeView.findViewById(R.id.attr_list_parent);
        try {

            JSONObject attributes = new JSONObject(data.get("attributes"));
            JSONArray variations = attributes.getJSONArray("variations");
            attrNames = attributes.getJSONArray("attrs");
            createAttributeCategories(attr_parent,variations);


            Log.w(APP_IDENT, data.toString());
        }catch (Exception e){
            Log.w(APP_IDENT, e.toString());
        }
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

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    add cart options to root view

                    ViewGroup mine = (ViewGroup) view.getRootView();
                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    

                    cart= layoutInflater.inflate(R.layout.add_cart_options,null,false);
                    if(mine.getChildCount()==3) {
                        CircleImageView circleImageView = cart.findViewById(R.id.product_img);
                        circleImageView.setImageDrawable(imageView.getDrawable());

                        mine.addView(cart);


//                    load attributes
                        holders.clear();

                        loadAttributes(cart, data);




                        Log.w(APP_IDENT, mine.toString());
                    }
                }
            });

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
                Log.w(APP_IDENT, "clicked");

            }

        }
    }
}