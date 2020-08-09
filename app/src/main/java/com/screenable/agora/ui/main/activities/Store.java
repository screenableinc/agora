package com.screenable.agora.ui.main.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import com.screenable.agora.R;
import com.screenable.agora.config.Config;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Store extends AppCompatActivity {
    RecyclerView view;
    String businessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        view = findViewById(R.id.products);
        ImageView logo = findViewById(R.id.logo);
        ImageView banner = findViewById(R.id.banner);


        HashMap<String,String> data =(HashMap<String, String>) getIntent().getExtras().get("data");
        businessId = data.get("businessId");
        String logoUri = Config.businessLogo+"?businessId="+businessId;
        String bannerUri = Config.businessbanner+"?businessId="+businessId;
        Picasso.get().load(logoUri).into(logo);
        Picasso.get().load(bannerUri).into(banner);
    }


    public class Load extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }
}