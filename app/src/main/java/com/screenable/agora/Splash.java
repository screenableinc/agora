package com.screenable.agora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.screenable.agora.config.Config;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startActivity(new Intent(Splash.this, MainActivity.class));

//        if(internalAuth()){
//            startActivity(new Intent(Splash.this, MainActivity.class));
//            finish();
//        } else {
//            startActivity(new Intent(Splash.this, Login.class));
//            finish();
//
//        }
//        check if logged in


    }
    private boolean internalAuth(){
        SharedPreferences info = getApplicationContext().getSharedPreferences(Config.userdata_SP_N,MODE_PRIVATE);
        Log.w(Config.APP_IDENT,info.getString("loggedIn","nothing"));

        Boolean loggedIn = Boolean.parseBoolean(info.getString("loggedIn","false"));
        return loggedIn;
    }
}