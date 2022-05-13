package com.screenable.agora.apiaccess;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.screenable.agora.config.Config;

import org.json.JSONObject;

public class DoInBackground extends AsyncTask<String, Integer, String> {
    private Context context;
    private String cartCount;
    private final TextView view;
    private Boolean failed=false;

    public DoInBackground(Context context, TextView textView){

        this.context=context;
        this.view=textView;
    }

    @Override
    protected String doInBackground(String... strings){
        try {
            JSONObject request = new Requests(context).sendGET(Config.cartItems, new String[0], new String[0]);
            if (request.getInt("code")==200){
                cartCount=request.getString("response");


            }
        }catch (Exception e){
            failed=true;


        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (failed){

        }else {

            view.setText(cartCount);
        }

    }
}
