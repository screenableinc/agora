package com.screenable.agora.ui.main.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.screenable.agora.R;
import com.screenable.agora.adapters.SearchResults;
import com.screenable.agora.apiaccess.Requests;
import com.screenable.agora.config.Config;
import com.screenable.agora.customviews.SpaceItemDecoration;
import com.screenable.agora.helpers.Helpers;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Store extends AppCompatActivity {
    RecyclerView view;
    String vendorId;
    ArrayList<HashMap<String, String>> results=new ArrayList<>();
    SearchResults adapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        view = findViewById(R.id.products);
        ImageView logo = findViewById(R.id.logo);
        ImageView banner = findViewById(R.id.banner);
        context=getApplicationContext();


        vendorId = (String) getIntent().getExtras().get("vendorId");

        String logoUri = Config.businessLogo+"?businessId="+vendorId;
        String bannerUri = Config.businessbanner+"?businessId="+vendorId;
        Picasso.get().load(logoUri).into(logo);
        Picasso.get().load(bannerUri).into(banner);
        new LoadItemsandShit().execute();
    }

    private static void toHash(JSONArray array, ArrayList<HashMap<String, String>> list) throws Exception{
        for (int i = 0; i < array.length(); i++) {
            list.add(Helpers.Mapify(array.getJSONObject(i)));

        }
    }
    public class LoadItemsandShit extends AsyncTask<String, Integer, String>{
        boolean success=true;
        @Override
        protected String doInBackground(String... strings) {

            String [] parameter = {"businessId"};String [] value = {vendorId};
            try {
                JSONObject response = new Requests(context).sendGET(Config.businessItems, parameter, value);
                Log.w(Config.APP_IDENT,response.toString());
                if(response.getInt("code")==200){
                    toHash(response.getJSONArray("response"),results);
                }else {
//                    fix this later
                    throw new Exception("failed");
                }
            }catch (Exception e){
//                show failed to load
                Log.w(Config.APP_IDENT,e.toString());
                success=false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
           if(success){ adapter = new SearchResults(getApplicationContext(),results,"",0);
            Log.w(Config.APP_IDENT, results.toString());
            view.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            view.setAdapter(adapter);
            SpaceItemDecoration decoration = new SpaceItemDecoration(16);
            view.addItemDecoration(decoration);}else {

           }

        }
    }
}