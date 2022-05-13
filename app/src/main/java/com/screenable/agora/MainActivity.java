package com.screenable.agora;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.screenable.agora.adapters.Products;
import com.screenable.agora.apiaccess.DoInBackground;
import com.screenable.agora.customviews.ProductHolder;
import com.screenable.agora.ui.main.activities.Mall;
import com.screenable.agora.ui.main.fragments.Home;
import com.screenable.agora.ui.main.fragments.Search;
import com.screenable.agora.ui.main.fragments.Vision;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static ViewPager mViewPager;
    Activity mainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity=this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        Picasso.setSingletonInstance(
                new Picasso.Builder(this)
                        // additional settings
                        .build());
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mViewPager = findViewById(R.id.view_pager);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        ImageView stores = findViewById(R.id.stores);
        final TextView cartCount = findViewById(R.id.count);
        new DoInBackground(getApplicationContext(),cartCount).execute();
        setStoreClickListener(stores);
        EditText search = findViewById(R.id.search);
        DrawerLayout drawerLayout = findViewById(R.id.dra);
        ImageView logo = findViewById(R.id.home_logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {

                    hideKeyboard(mainActivity, search.getText().toString());
                    return true;
                }
                return false;
            }
        });

        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    mViewPager.setCurrentItem(0);
                }
            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ImageView scan = findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mViewPager.setCurrentItem(2);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==3){

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public static void hideKeyboard(Activity activity, String text) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        new Search.TextSearch().execute(text);
    }


    private void setStoreClickListener(View store) {
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Mall.class));
            }
        });
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 1:
                    return new Home();
                case 2:
                    return new Vision();
                case 0:
                    return new Search();


                default:
                    return null;


            }




        }


        @Override
        public int getCount() {
            // Show 1 total pages.
            return 3;
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        // Other 'case' lines to check for other
//        // permissions this app might request.
//    }
    public static void goToSearch(String sku){
        mViewPager.setCurrentItem(0);
    }
    }

