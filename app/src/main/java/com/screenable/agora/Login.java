package com.screenable.agora;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.screenable.agora.apiaccess.Requests;
import com.screenable.agora.config.Config;
import com.screenable.agora.helpers.Helpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.screenable.agora.apiaccess.Requests.sendPost;

public class Login extends AppCompatActivity {
    Button loginButton;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar=findViewById(R.id.progressBar);
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }



    private void login(){

        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);

        final String username_text = username.getText().toString();
        final String password_text = password.getText().toString();
        if (username_text.isEmpty() || password_text.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill in fields", Toast.LENGTH_LONG).show();
        } else {
//                convert parameters to JSONified string and pass to Auth
            HashMap<String, String> map = new HashMap<>();
            map.put("identifier", username_text);
            map.put("password", password_text);
            try {
                String params = Helpers.JSONify(map);
                new Auth().execute(params);
            } catch (JSONException e) {
//                    TODO:: show Toast

            }
        }

    }
    public class Auth extends AsyncTask<String, Integer, String>{
        boolean success;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject response = Requests.sendPost(Config.loginLink, strings[0]);
                int code = response.getInt("code");
                if(code==100){
//                    proceed to home
//                    first write to sharedPref
                    Helpers.writeToSharedPref(getApplicationContext(),Helpers.Mapify(response.getJSONObject("response").put("loggedIn",true)),Config.userdata_SP_N);
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                }else if (code==403){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Wrong username or password",Toast.LENGTH_LONG).show();
                        }
                    });

                }else if(code==500){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Server error",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }catch (Exception e){
                Log.w(Config.APP_IDENT,e.toString());

                runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"App error",Toast.LENGTH_LONG).show();
                        }
                    });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }
}