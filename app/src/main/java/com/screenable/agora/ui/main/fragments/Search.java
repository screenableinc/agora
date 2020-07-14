package com.screenable.agora.ui.main.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.screenable.agora.R;
import com.screenable.agora.adapters.SearchResults;
import com.screenable.agora.apiaccess.Requests;
import com.screenable.agora.config.Config;
import com.screenable.agora.helpers.Helpers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Search extends Fragment {
    static RecyclerView recyclerView;
    static SearchResults searchResults;
    static ArrayList <HashMap<String,String>> results = new ArrayList<HashMap<String, String>>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
//        live audio and video broadcasts

        final View rootView = inflater.inflate(R.layout.search, container, false);
        recyclerView = rootView.findViewById(R.id.search_recycler);


        return rootView;
    }
    public static void empty_recycler(){

    }
    public static class BarcodeSearch extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... strings) {
//              first argument is sku
            String[] params = {"qs"};
            String[] val = {strings[0]};
            try {
                JSONObject request = new Requests().sendGET(Config.search,params,val);
                if (request.getInt("code")==200){
                    toHash(request.getJSONArray("response"),results);
                }else {
//                    show something wrong
                }

            }catch (Exception e){
                Log.w(Config.APP_IDENT, e.toString());
            }


            return null;
        }
        private void toHash(JSONArray array, ArrayList<HashMap<String, String>> list) throws Exception{
            for (int i = 0; i < array.length(); i++) {
                list.add(Helpers.Mapify(array.getJSONObject(i)));

            }
        }
    }



}
