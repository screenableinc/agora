package com.screenable.agora.signupflow.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.screenable.agora.R;
import com.screenable.agora.SignUP;
import com.screenable.agora.Verification;
import com.screenable.agora.apiaccess.Requests;
import com.screenable.agora.config.Config;
import com.screenable.agora.helpers.Helpers;
import com.screenable.agora.signupflow.SignUp;

import org.json.JSONObject;

import java.net.ConnectException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Verify#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Verify extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int count=3;
    ProgressBar progressBar;
    AppCompatButton button;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Verify() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Verify.
     */
    // TODO: Rename and change types and number of parameters
    public static Verify newInstance(String param1, String param2) {
        Verify fragment = new Verify();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify, container, false);
        CountryCodePicker ccp = (CountryCodePicker) view.findViewById(R.id.country);
        EditText editTextCarrierNumber = (EditText) view.findViewById(R.id.number);
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);
        button = view.findViewById(R.id.register);
        progressBar=view.findViewById(R.id.progress);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RequestCode().execute(ccp.getFullNumber());
            }
        });
        return view;
    }
    //    fix code in future....new devs problem
    public class RequestCode extends AsyncTask<String, Integer, String>{
        Boolean success=false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Helpers.toggleButtonProgress(button,progressBar);
        }

        @Override
        protected String doInBackground(String... strings) {
            String[] keys = {"number"};
            String[] values = {strings[0]};

            try {


                JSONObject serverResponse = new Requests(getContext()).sendGET(Config.userVerify, keys, values);
                if (serverResponse.getBoolean("success")){
                    success=true;
                }
            }catch (ConnectException e){
                Log.w(Config.APP_IDENT,e);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    //                    alert developer
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"Check Network Settings", Toast.LENGTH_LONG).show();
                    }
                });

            }catch (Exception e){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    //                    alert developer
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"Fatal App Error: Developer alerted", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;

        }




        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Helpers.toggleButtonProgress(button,progressBar);
            if (success){

//                startActivity(new Intent(SignUP.this, Verification.class));
                SharedPreferences.Editor editor = Objects.requireNonNull(requireActivity()).getSharedPreferences(Config.userdata_SP_N, Context.MODE_PRIVATE).edit();
//                editor.putString("password",input);
//                vcode will be stored on server
                editor.putString("stage", "entercode");
                editor.apply();
                SignUp.SignUpViewPager.setCurrentItem(SignUp.SignUpViewPager.getCurrentItem()+1);
            }else {
//                alert error
                Toast.makeText(getContext(),"Please try again", Toast.LENGTH_LONG).show();
            }
        }
    }
}