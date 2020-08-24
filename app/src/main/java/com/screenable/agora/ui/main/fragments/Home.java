package com.screenable.agora.ui.main.fragments;

import android.accounts.NetworkErrorException;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AutoCompleteTextView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.screenable.agora.MainActivity;
import com.screenable.agora.R;
import com.screenable.agora.adapters.SearchResults;
import com.screenable.agora.adapters.TopBrands;
import com.screenable.agora.adapters.TrendingProducts;
import com.screenable.agora.apiaccess.Requests;
import com.screenable.agora.config.Config;
import com.screenable.agora.helpers.Helpers;
import com.screenable.agora.ui.main.wrappers.LinearLayoutManagerWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Home extends Fragment {
    static ArrayList <HashMap<String,String>> brands = new ArrayList<HashMap<String, String>>();
    ArrayList <HashMap<String,String>> products = new ArrayList<HashMap<String, String>>();
    static ArrayList <HashMap<String,String>> discovered = new ArrayList<HashMap<String, String>>();
    TopBrands topBrands;
    SearchResults trendingProducts;
    static SearchResults discover_adapter;
    RecyclerView r_view;
    RecyclerView trendingProductsRecycler;
    RecyclerView discover;
    static SharedPreferences preferences;
    NestedScrollView body;
    ProgressBar loading;
    View error;
    static View indicator;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
//        live audio and video broadcasts

        final View rootView = inflater.inflate(R.layout.home, container, false);

        r_view = rootView.findViewById(R.id.brand_recycler);
        indicator = rootView.findViewById(R.id.indicator);
        trendingProductsRecycler = rootView.findViewById(R.id.product_recycler);
        discover = rootView.findViewById(R.id.discover);
        body = rootView.findViewById(R.id.body);
        loading = rootView.findViewById(R.id.loading);
        error = rootView.findViewById(R.id.error);
        discover.setNestedScrollingEnabled(false);
//        discover.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));


        preferences = getActivity().getSharedPreferences(Config.API_access_SP_N, Context.MODE_PRIVATE);
        new Load().execute();

        return rootView;

    }
    private static void toHash(JSONArray array, ArrayList<HashMap<String, String>> list) throws Exception{
        for (int i = 0; i < array.length(); i++) {
            list.add(Helpers.Mapify(array.getJSONObject(i)));

        }
    }

    public static class Additional extends AsyncTask<String, Integer, String>{
        String timestamp;
        boolean success;

        boolean change=false;
        int oldcount;
        int newcount;


        public Additional(@NonNull String timestamp,int oldcount){
            this.timestamp = timestamp;
            this.oldcount=oldcount;

        }
        @Override
        protected void onPreExecute() {
            indicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                String [] d_params = {"last_timestamp"};


                String[] d_vals = {preferences.getString("last_timestamp",timestamp)};

                JSONObject discoverProducts = new Requests().sendGET(Config.discoverProducts, d_params, d_vals);

                if(discoverProducts.getInt("code")==200){
//                    proceed
                    Log.w(Config.APP_IDENT, discoverProducts.toString());
                    if(discoverProducts.getJSONArray("response").length()>0){
                        newcount = discoverProducts.getJSONArray("response").length();
                        change=true;
                    }
                    toHash(discoverProducts.getJSONArray("response"),discovered);
                    success=true;

                }else {
//                    TODO
                }
            }catch (Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(change) {
//                discover_adapter.notifyItemRangeRemoved(0,oldcount);
                discover_adapter.notifyDataSetChanged();
            }
            if (!success){

            }

        }
    }
    public class Load extends AsyncTask<String, Integer, String>{
        boolean success=false;

        @Override
        protected void onPreExecute() {
            loading.setVisibility(View.VISIBLE);
            loading.animate();
//            make everything invisible
            body.setVisibility(View.GONE);
            error.setVisibility(View.GONE);


        }

        @Override
        protected String doInBackground(String... strings) {


            try {
//                todo...put these in separate try blocks
                String[] params = {};
                String[] vals = {};
                JSONObject object = new Requests().sendGET(Config.topBrands, params, vals);
                JSONObject trendingProducts = new Requests().sendGET(Config.trendingProducts, params, vals);

                String [] d_params = {"last_timestamp"};


                String[] d_vals = {preferences.getString("last_timestamp","00")};

                JSONObject discoverProducts = new Requests().sendGET(Config.discoverProducts, d_params, d_vals);

                if(object.getInt("code")==200){
//                    proceed
                    Log.w(Config.APP_IDENT, object.toString());
                    toHash(object.getJSONArray("response"),brands);

                }else {
//                    TODO
                }
                if(trendingProducts.getInt("code")==200){
//                    proceed
                    Log.w(Config.APP_IDENT, object.toString());
                    toHash(trendingProducts.getJSONArray("response"),products);

                }else {
//                  TODO
                }
                if(discoverProducts.getInt("code")==200){
                    toHash(discoverProducts.getJSONArray("response"),discovered);
                }
                success=true;

            }catch (NetworkErrorException e){
                Log.w(Config.APP_IDENT,e.toString()+"kk");
                success = false;
//
            }catch (Exception e){
                Log.w(Config.APP_IDENT,e.toString());
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            if(!success){
                error.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                error.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Load().execute();
                    }
                });


            }else {
                loading.setVisibility(View.GONE);
                body.setVisibility(View.VISIBLE);
                topBrands = new TopBrands(getContext(), brands);
                r_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                r_view.setAdapter(topBrands);

                trendingProducts = new SearchResults(getContext(), products,"");
                trendingProductsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                trendingProductsRecycler.setAdapter(trendingProducts);

                discover_adapter = new SearchResults(getContext(), discovered,"discover");


                discover.setLayoutManager(new LinearLayoutManagerWrapper(getContext(), LinearLayoutManager.VERTICAL, false));
                discover.setAdapter(discover_adapter);
                body.getViewTreeObserver()
                        .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                            @Override
                            public void onScrollChanged() {
                                if (body.getChildAt(0).getBottom()
                                        <= (body.getHeight() + body.getScrollY())) {
                                    try {
                                        new Additional(discovered.get(discovered.size()-1).get("timestamp"),discovered.size()).execute();

                                    }catch (IndexOutOfBoundsException e){

                                    }
                                    Log.w(Config.APP_IDENT, "happu");
                                    //scroll view is at bottom
                                } else {
                                    //scroll view is not at bottom
                                }
                            }
                        });

//
            }
        }
    }

}
