package com.screenable.agora.signupflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.screenable.agora.MainActivity;
import com.screenable.agora.R;
import com.screenable.agora.config.Config;
import com.screenable.agora.signupflow.fragments.EnterCode;
import com.screenable.agora.signupflow.fragments.FullName;
import com.screenable.agora.signupflow.fragments.Password;
import com.screenable.agora.signupflow.fragments.Username;
import com.screenable.agora.signupflow.fragments.Verify;
import com.screenable.agora.ui.main.SectionsPagerAdapter;
import com.screenable.agora.ui.main.fragments.Search;
import com.screenable.agora.ui.main.fragments.Vision;

import java.util.Objects;

public class SignUp extends AppCompatActivity {
    private SignUpSectionsPagerAdapter mSectionsPagerAdapter;
    public static ViewPager SignUpViewPager;
    private TextView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_flow);
        mSectionsPagerAdapter = new SignUpSectionsPagerAdapter(getSupportFragmentManager());
        SignUpViewPager = findViewById(R.id.sign_up_view_pager);
        Log.w(Config.APP_IDENT, SignUpViewPager.toString());
        skip= findViewById(R.id.skip);
        SignUpViewPager.setAdapter(mSectionsPagerAdapter);

//        if skip is pressed on the verification part,skip enter vcode as well

//      check if current page should be skippable
        if (SignUpViewPager.getCurrentItem()==2 || SignUpViewPager.getCurrentItem()==3){
            setSkipable();
        }else {
            unsetSkipable();
        }

//        disable fling
        SignUpViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }

        });

        SignUpViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==2){
                    setSkipable();
                }else {
                    unsetSkipable();
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //        determine current page

        determineCurrentPage();
//SignUpViewPager.setCurrentItem(1);

    }
    public static void next(){
        SignUpViewPager.setCurrentItem(SignUp.SignUpViewPager.getCurrentItem()+1);
    }
    private void determineCurrentPage(){
        SharedPreferences sharedPreferences = Objects.requireNonNull(getApplicationContext()).getSharedPreferences(Config.userdata_SP_N, Context.MODE_PRIVATE);
        String stage = sharedPreferences.getString("stage",null);
        if(stage==null){
            return;
        }
        Log.w(Config.APP_IDENT, stage+ SignUpViewPager.getChildCount() +"__what the "+Password.count);


        switch (Objects.requireNonNull(stage)){
            case "password":
                SignUpViewPager.setCurrentItem(1);
                break;
            case "fullname":
                SignUpViewPager.setCurrentItem(FullName.count);
                break;
            case "verify":
                SignUpViewPager.setCurrentItem(Verify.count);
                break;
            case "entercode":
                SignUpViewPager.setCurrentItem(EnterCode.count);
                break;
            default:
                SignUpViewPager.setCurrentItem(0);
                break;
        }

    }
    private void setSkipable() {
        skip.setAlpha(1);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SignUpViewPager.getCurrentItem()+1 != SignUpViewPager.getChildCount()){
                    SignUpViewPager.setCurrentItem(SignUpViewPager.getCurrentItem()+1);
                }
            }
        });
    }
    private void unsetSkipable() {
        skip.setAlpha(0.1f);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    public class SignUpSectionsPagerAdapter extends FragmentPagerAdapter {

        public SignUpSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    return new Username();
                case 1:
                    return new Password();
                case 2:
                    return new FullName();
                case 3:
                    return new Verify();
                case 4:
                    return new EnterCode();



                default:
                    return null;


            }




        }


        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }
    }
}