package com.screenable.agora.ui.main.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.screenable.agora.MainActivity;
import com.screenable.agora.R;
import com.screenable.agora.adapters.TopBrands;
import com.screenable.agora.adapters.TrendingProducts;
import com.screenable.agora.apiaccess.Requests;
import com.screenable.agora.config.Config;
import com.screenable.agora.helpers.Helpers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Home extends Fragment {
    ArrayList <HashMap<String,String>> brands = new ArrayList<HashMap<String, String>>();
    ArrayList <HashMap<String,String>> products = new ArrayList<HashMap<String, String>>();
    TopBrands topBrands;
    TrendingProducts trendingProducts;
    RecyclerView r_view;
    RecyclerView trendingProductsRecycler;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
//        live audio and video broadcasts

        final View rootView = inflater.inflate(R.layout.home, container, false);

        r_view = rootView.findViewById(R.id.brand_recycler);
        trendingProductsRecycler = rootView.findViewById(R.id.product_recycler);
        ImageView scan = rootView.findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mViewPager.setCurrentItem(2);
            }
        });
        new Load().execute();

        return rootView;

    }

    public class Load extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... strings) {


            try {
                String[] params = {};
                String[] vals = {};
                JSONObject object = new Requests().sendGET(Config.topBrands, params, vals);
                JSONObject trendingProducts = new Requests().sendGET(Config.trendingProducts, params, vals);

                if(object.getInt("code")==200){
//                    proceed
                    Log.w(Config.APP_IDENT, object.toString());
                    toHash(object.getJSONArray("response"),brands);

                }else {

                }
                if(trendingProducts.getInt("code")==200){
//                    proceed
                    Log.w(Config.APP_IDENT, object.toString());
                    toHash(trendingProducts.getJSONArray("response"),products);

                }else {

                }

            }catch (Exception e){
                Log.w(Config.APP_IDENT,e.toString()+"kk");
            }
            return null;
        }
        private void toHash(JSONArray array, ArrayList<HashMap<String, String>> list) throws Exception{
            for (int i = 0; i < array.length(); i++) {
                list.add(Helpers.Mapify(array.getJSONObject(i)));

            }
        }

        @Override
        protected void onPostExecute(String s) {
            topBrands = new TopBrands(getContext(),brands);
            r_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
            r_view.setAdapter(topBrands);

            trendingProducts = new TrendingProducts(getContext(),products);
            trendingProductsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
            trendingProductsRecycler.setAdapter(trendingProducts);
        }
    }

}
