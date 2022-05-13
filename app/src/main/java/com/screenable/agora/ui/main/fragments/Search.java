package com.screenable.agora.ui.main.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.libraries.maps.CameraUpdate;
import com.google.android.libraries.maps.CameraUpdateFactory;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.OnMapReadyCallback;
import com.google.android.libraries.maps.SupportMapFragment;
import com.google.android.libraries.maps.model.BitmapDescriptorFactory;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.LatLngBounds;
import com.google.android.libraries.maps.model.Marker;
import com.google.android.libraries.maps.model.MarkerOptions;
import com.screenable.agora.MainActivity;
import com.screenable.agora.R;
import com.screenable.agora.adapters.SearchResults;
import com.screenable.agora.apiaccess.Requests;
import com.screenable.agora.config.Config;
import com.screenable.agora.customviews.SpaceItemDecoration;
import com.screenable.agora.helpers.Helpers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class Search extends Fragment {
    static RecyclerView recyclerView;
    static SearchResults searchResults;
    static Context context;
    static ImageView toggle;
    static FrameLayout mapHolder;
    public static boolean ready;
    public static GoogleMap map;
    static View rootView;
    static View body;
    static View loading;
    static View error;
    static View nothing;
    private static SupportMapFragment mapFragment;
    static ArrayList <HashMap<String,String>> results = new ArrayList<HashMap<String, String>>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
//        live audio and video broadcasts
        rootView = inflater.inflate(R.layout.search, container, false);
        body = rootView.findViewById(R.id.s_body);
        loading = rootView.findViewById(R.id.s_loading);
        error = rootView.findViewById(R.id.s_error);
        nothing = rootView.findViewById(R.id.no_res);

        recyclerView = rootView.findViewById(R.id.search_recycler);
        toggle = rootView.findViewById(R.id.mapviewtoggle);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                toggleMap();


            }
        });
        mapHolder = rootView.findViewById(R.id.mapfragmentholder);
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);

        context = this.getActivity();

        return rootView;
    }
    public static void empty_recycler(){

    }

//    private static void pre_indicators(){
//
//    }

    private static void toggleMap(){
        Log.w(Config.APP_IDENT, "toggled");
        if (mapHolder.getVisibility()==View.VISIBLE){
            mapHolder.setVisibility(View.GONE);
        }else{
            mapHolder.setVisibility(View.VISIBLE);
        }
    }


    private static void toHash(JSONArray array, ArrayList<HashMap<String, String>> list) throws Exception{
        for (int i = 0; i < array.length(); i++) {
            list.add(Helpers.Mapify(array.getJSONObject(i)));

        }
    }
    private static void toggle(){


    }
    private static Bitmap returnBitmap(String price){
        LinearLayout tv;
        LayoutInflater inflater = LayoutInflater.from(context);
        tv = (LinearLayout) inflater.inflate(R.layout.mapinfo, null, false);
        TextView pricetip = tv.findViewById(R.id.pricetip);

        pricetip.setText(price);
        tv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

        tv.setDrawingCacheEnabled(true);
        tv.buildDrawingCache();
        Bitmap bm = tv.getDrawingCache();
        return bm;
    }
    private static void populateMap(){
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.clear();
//                TODO fix bitmap conversion....using too much mem

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (HashMap<String, String> result : results) {
                    Double latitude = Double.parseDouble(result.get("lat"));
                    Double longitude = Double.parseDouble(result.get("lng"));
                    LatLng pos = new LatLng(latitude,longitude);

                    String currency = result.get("symbol");
                    Log.w(Config.APP_IDENT, currency);
                    String price = currency + Helpers.priceFormat(result.get("price"));

                    Marker marker = googleMap.addMarker(new MarkerOptions().position(pos));

                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(returnBitmap(price)));

                    builder.include(pos);

                }
                LatLngBounds bounds = builder.build();

                int padding = 0; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                googleMap.animateCamera(cu);
            }
        });


    }
    private static void populate(){
        recyclerView.removeAllViews();

        searchResults = new SearchResults(context,results,"",0);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        recyclerView.setAdapter(searchResults);
        SpaceItemDecoration decoration = new SpaceItemDecoration(16);
        recyclerView.addItemDecoration(decoration);
    }

    private static void pre_indicators(){
        loading.setVisibility(View.VISIBLE);

        body.setVisibility(View.GONE);

        error.setVisibility(View.GONE);
        nothing.setVisibility(View.GONE);
    }
    private static void post_indicators(boolean success, boolean results){
        Log.w(Config.APP_IDENT, success +" "+ results);
        if(!success){
            error.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);



        }else {
            loading.setVisibility(View.GONE);
            if(!results){
                nothing.setVisibility(View.VISIBLE);
            }else {
                body.setVisibility(View.VISIBLE);
            }
        }
    }
    public static class TextSearch extends AsyncTask<String, Integer, String>{
        boolean success;
        boolean res;
        @Override
        protected void onPreExecute() {
            results.clear();
            pre_indicators();



        }

        @Override
        protected String doInBackground(String... strings) {
            String[] params = {"medium","qs"};
            String[] val = {"mobile",strings[0]};
            try {
                JSONObject request = new Requests(context).sendGET(Config.search,params,val);
                Log.w(Config.APP_IDENT, results.toString());
                if (request.getInt("code")==200){
                    success=true;
                    toHash(request.getJSONArray("response"),results);
                }else {
                    success=false;
//                    show something wrong
                }

            }catch (Exception e){
                Log.w(Config.APP_IDENT, e.toString());
                success = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (results.size()>0){
                res=true;
            }else {
                res=false;
            }
            if(success){
                populate();
                if(results.size()>0) {
                    populateMap();
                }
            }
            post_indicators(success, res);
        }
    }

    public static class BarcodeSearch extends AsyncTask<String, Integer, String>{
        private Boolean success;

        @Override
        protected void onPreExecute() {
            MainActivity.mViewPager.setCurrentItem(0);
            results.clear();

        }

        @Override
        protected String doInBackground(String... strings) {
//              first argument is sku
            String[] params = {"medium","barcode"};
            String[] val = {"mobile",strings[0]};
            try {
                JSONObject request = new Requests(context).sendGET(Config.barcode_search,params,val);
                Log.w(Config.APP_IDENT, results.toString());
                if (request.getInt("code")==200){
                    success=true;
                    toHash(request.getJSONArray("response"),results);
                }else {
                    success=false;
//                    show something wrong
                }

            }catch (Exception e){
                Log.w(Config.APP_IDENT, e.toString());
            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {

            if(success){
                populate();
                if(results.size()>0) {
                    populateMap();
                }

            }
        }
    }



}
