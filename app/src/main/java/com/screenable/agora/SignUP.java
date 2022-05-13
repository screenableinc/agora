package com.screenable.agora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.screenable.agora.apiaccess.Requests;
import com.screenable.agora.config.Config;

import org.json.JSONObject;

public class SignUP extends AppCompatActivity {
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = getApplicationContext();
        CountryCodePicker ccp = (CountryCodePicker) findViewById(R.id.country);
        EditText editTextCarrierNumber = (EditText) findViewById(R.id.number);
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);
        Button button = findViewById(R.id.register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SignIn().execute(ccp.getFullNumber());
            }
        });
    }


    public class SignIn extends AsyncTask<String, Integer, String>{
        Boolean success=false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String[] keys = {"number"};
            String[] values = {strings[0]};

            try {


                JSONObject serverResponse = new Requests(context).sendGET(Config.userVerify, keys, values);
                if (serverResponse.getBoolean("success")){
                    success=true;
                }
            }catch (Exception e){
                Log.w(Config.APP_IDENT,e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"horrible error", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;

        }




        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (success){

//                startActivity(new Intent(SignUP.this, Verification.class));
            }else {
//                alert error
                Toast.makeText(getApplicationContext(),"Please try again", Toast.LENGTH_LONG).show();
            }
        }
    }

}
