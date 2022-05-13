package com.screenable.agora.signupflow.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.screenable.agora.R;
import com.screenable.agora.config.Config;
import com.screenable.agora.helpers.InputValidation;
import com.screenable.agora.signupflow.SignUp;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Password#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Password extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int count=1;
    AppCompatButton button;
    EditText password;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Password() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Password.
     */
    // TODO: Rename and change types and number of parameters
    public static Password newInstance(String param1, String param2) {
        Password fragment = new Password();
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
        View view =  inflater.inflate(R.layout.fragment_password, container, false);
        password = view.findViewById(R.id.password);
        button = view.findViewById(R.id.passwordset);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = password.getText().toString();
                if(InputValidation.length(input,6)){
//                    store password in sharedPref
                    SharedPreferences.Editor editor = Objects.requireNonNull(requireActivity()).getSharedPreferences(Config.userdata_SP_N, Context.MODE_PRIVATE).edit();
                    editor.putString("password",input);
                    editor.putString("stage", "fullname");
                    editor.apply();
//                    go to next page
                    SignUp.SignUpViewPager.setCurrentItem(SignUp.SignUpViewPager.getCurrentItem()+1);
                }else{
//                    alert text invalid
                    Toast.makeText(getContext(),"Cannot be less than 6 characters", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}