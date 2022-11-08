package com.screenable.agora.ui.main.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.screenable.agora.R;
import com.screenable.agora.apiaccess.Requests;
import com.screenable.agora.config.Config;
import com.screenable.agora.customviews.CartOption;
import com.screenable.agora.helpers.Helpers;
import com.screenable.agora.ui.main.fragments.Home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Cart extends AppCompatActivity {
    ViewGroup parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        parent = findViewById(R.id.toppest);
//        load cart
        new LoadCart().execute();

    }
    private class LoadCart extends AsyncTask<String, Integer, JSONArray>{
        boolean success=false;

        @Override
        protected JSONArray doInBackground(String... strings) {
            try {
                String [] params = {};
                String [] values = {};
                JSONObject cart = new Requests(getApplicationContext()).sendGET(Config.cart,params,values);
                if(cart.getBoolean("success")){

                    Helpers.Log(cart.toString());

                    success=true;

                    return cart.getJSONArray("response");
                }else {

                }

            }catch (Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray res){
            super.onPostExecute(res);
            try{
                if (success){
                    for (int i = 0; i < res.length(); i++) {
                        JSONObject cartItem = res.getJSONObject(i);
//                    create cart item info holder
                        CartOption cartOption = new CartOption(getApplicationContext(),cartItem);
                        TextView view = new TextView(getApplicationContext());
                        view.setText("okay go");
                        parent.addView(cartOption);
                        Helpers.Log("added");

                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    }
}