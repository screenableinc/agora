package com.screenable.agora.signupflow.fragments;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.screenable.agora.R;
import com.screenable.agora.apiaccess.Requests;
import com.screenable.agora.config.Config;
import com.screenable.agora.helpers.Helpers;
import com.screenable.agora.helpers.InputValidation;
import com.screenable.agora.signupflow.SignUp;

import org.json.JSONObject;

import java.net.ConnectException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Username} factory method to
 * create an instance of this fragment.
 */
public class Username extends Fragment {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int count=0;
    private ProgressBar progress;
    AppCompatButton button;
//
//    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
//
    public Username() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment Username.
//     */
//    // TODO: Rename and change types and number of parameters
    public static Username newInstance(String param1, String param2) {
        Username fragment = new Username();
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
        View view = inflater.inflate(R.layout.fragment_username, container, false);
        EditText username = view.findViewById(R.id.username);
        button = view.findViewById(R.id.check_username);
        progress = view.findViewById(R.id.progress);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                call AsyncTask if validation passes
                String input = username.getText().toString();
                if(InputValidation.length(input,4)){

                    new CheckUserNameAvail().execute(input);

                }else {
                    Toast.makeText(getContext(), "Must be more than 4 letters", Toast.LENGTH_LONG).show();
                }


            }
        });
        return view;

        }
    private class CheckUserNameAvail extends AsyncTask<String, Integer, String>{
        Boolean exceptionThrown=false;
        Boolean success=false;
        String username;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            show progressbar and disappear button
            Helpers.toggleButtonProgress(button, progress);
        }

        @Override
        protected String doInBackground(String... strings) {
            username=strings[0];
            String[] values = {username};
            String[] params = {"username"};
            try {
                JSONObject serverResponse = new Requests(getContext()).sendGET(Config.userExists,params,values);
                if(!serverResponse.getBoolean("exists")){
//                    proceed, user does not exist
                    success=true;
//                    store in sp, go to next page
                }

            }catch (NetworkErrorException e){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"Please Check Internet Connection ", Toast.LENGTH_LONG).show();
                    }
                });

            } catch (ConnectException e){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    //                    alert developer
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"Check Network", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e){
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
            Helpers.toggleButtonProgress(button, progress);
            if (success){
                SharedPreferences.Editor editor = Objects.requireNonNull(requireActivity()).getSharedPreferences(Config.userdata_SP_N, Context.MODE_PRIVATE).edit();
                editor.putString("username",username);
                editor.putString("stage", "password");
                editor.apply();
                SignUp.SignUpViewPager.setCurrentItem(SignUp.SignUpViewPager.getCurrentItem()+1);
            }
        }
    }
    }
