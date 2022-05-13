package com.screenable.agora.signupflow.fragments;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

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
import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FullName#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FullName extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int count=2;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AppCompatButton button;
    ProgressBar progressBar;

    public FullName() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FullName.
     */
    // TODO: Rename and change types and number of parameters
    public static FullName newInstance(String param1, String param2) {
        FullName fragment = new FullName();
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
        View view = inflater.inflate(R.layout.fragment_full_name, container, false);
        EditText fullname = view.findViewById(R.id.fullname);
        button = view.findViewById(R.id.fullnamesub);
        progressBar = view.findViewById(R.id.progress);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = fullname.getText().toString();
                if(InputValidation.length(input,4)){
                    SharedPreferences.Editor editor = Objects.requireNonNull(requireActivity()).getSharedPreferences(Config.userdata_SP_N, Context.MODE_PRIVATE).edit();
                    editor.putString("fullname",input);
                    editor.putString("stage", "verify");
                    editor.apply();
                    new createAccountOnServer().execute(input);

                }else{
//                    alert text invalid
                    Toast.makeText(getContext(),"Cannot be less than 4 characters", Toast.LENGTH_LONG).show();
                }
            }

        });





        return view;
    }
//    SIgn in on server happens after either full name is set or skipped
    private class createAccountOnServer extends AsyncTask<String, Integer, String>{
        Boolean success=false;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Helpers.toggleButtonProgress(button,progressBar);
    }

    @Override
        protected String doInBackground(String... strings) {

        try {

            HashMap<String, String> map = new HashMap<>();
            String username = getContext().getSharedPreferences(Config.userdata_SP_N, Context.MODE_PRIVATE).getString("username",null);
            String password = getContext().getSharedPreferences(Config.userdata_SP_N, Context.MODE_PRIVATE).getString("password",null);
            map.put("username", username);
            map.put("password", password);
            map.put("fullName", strings[0]);
            String params = Helpers.JSONify(map);

            JSONObject serverResponse = new Requests(getContext()).sendPost(Config.joinLink,params);
            if (serverResponse.getBoolean("success")){
                SharedPreferences.Editor editor = getContext().getSharedPreferences(Config.userdata_SP_N,Context.MODE_PRIVATE).edit();
                editor.putString("accessToken",serverResponse.getString("accessToken"));
                editor.apply();
                success=true;
            }
        }catch (NetworkErrorException e){
            Helpers.showToast(getContext(), "Check Network");
        }catch (Exception e){
            Helpers.showToast(getContext(), "Fatal error, Developer alerted");
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        Helpers.toggleButtonProgress(button,progressBar);
        if(success){
            SignUp.next();
        }
    }
}
}