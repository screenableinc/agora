package com.screenable.agora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.screenable.agora.apiaccess.Requests;
import com.screenable.agora.config.Config;

import org.json.JSONObject;

public class Verification extends AppCompatActivity {
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        context = this.getApplicationContext();
    }

//    fix code in future....new devs problem
    public static class Verify extends AsyncTask<String, Integer, String>{
        Boolean success=false;
        @Override
        protected String doInBackground(String... strings) {
        String[] keys = {"number"};

        String[] values = {strings[0]};
        try {


            JSONObject serverResponse = new Requests(context).sendGET(Config.userVerify, keys, values);
            if(serverResponse.getBoolean("success")){
                success=true;
//                write to SF
            }

        }catch (Exception e){

        }
        return null;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(!success){
            Toast.makeText(context.getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();
        }else{

        }
    }
}
}