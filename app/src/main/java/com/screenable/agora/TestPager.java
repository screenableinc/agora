package com.screenable.agora;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.screenable.agora.adapters.Products;
import com.screenable.agora.customviews.ProductHolder;

public class TestPager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_pager);
        String [] links={"as","asd","kl"};
        LinearLayoutCompat parent = findViewById(R.id.parent_layout);
//        ProductHolder holder = new ProductHolder(getApplicationContext(), null);
//        holder.setAdapter(new Products(links, getApplicationContext()));
//        holder.rigCounter(links.length);
//        parent.addView(holder);
        

    }
}