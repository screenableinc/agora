package com.screenable.agora.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.screenable.agora.Login;
import com.screenable.agora.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.security.acl.Permission;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Callable;

public class Helpers {
public static String JSONify(HashMap<String, String> map) throws JSONException {
    JSONObject object= new JSONObject();
    for (String i :
         map.keySet()) {
        object.put(i,map.get(i));
    }
    return object.toString();
}

public static String priceFormat(String price){
    price = String.format("%.2f",Float.parseFloat(price));
    String decimals = price.substring(price.length()-3);
    price = price.substring(0,price.length()-3);
    if(price.length()>3){
        String textLeft = price;
        String _final = "";
        while (textLeft.length()>3){
            _final = "," +textLeft.substring(textLeft.length()-3)+_final;
            textLeft=textLeft.substring(0,textLeft.length()-3);
        }
        return textLeft + _final+decimals;
    }else {
        return price+decimals;
    }

}
    public static boolean checkLogin(Context context){

        Boolean loggedIn = context.getSharedPreferences(Config.userdata_SP_N,Context.MODE_PRIVATE).getBoolean("logggedIn",false);
        return loggedIn;

    }
//public static String Price(String price){
//    price=Float.parseFloat(price);
//
//    String decimals = price.substring(price.length()-3);
//    price=price.substring(price.length() -3);
//    if(price.length() >= 4){
//        String textLeft = price;
//        String _final="";
//        while (textLeft.length()>3){
//
//            _final = "," +textLeft.substring(textLeft.length()-3)+ _final;
//            textLeft = textLeft.substring(0, -3);
//
//
//        }
//        return textLeft+_final+decimals;
//
//    }else {
//        return price+decimals;
//    }
//}
public static HashMap<String, String> Mapify(JSONObject jsonObject) throws JSONException{
    Iterator<String> iter = jsonObject.keys();
    HashMap <String, String> map = new HashMap<>();
    while (iter.hasNext()) {
        String key = iter.next();String value = jsonObject.get(key).toString();
        map.put(key, value);
    }
    return map;
}

    public static String GenQueryString(String[] params, String[] values)
            throws UnsupportedEncodingException {





        // build parameters list
        StringBuilder result = new StringBuilder();
        for (int i = 0; i<params.length;i++) {
            if (result.length() == 0) {
                result.append(params[i] + "=" + URLEncoder.encode(values[i],"UTF-8") );
            } else {
                result.append("&" + params[i]+"="+URLEncoder.encode(values[i],"UTF-8") );
            }
        }

        return result.toString();
    }


public static void writeToSharedPref(Context context, HashMap<String, String> map, String sharedPref){
    SharedPreferences.Editor editor = context.getSharedPreferences(sharedPref,Context.MODE_PRIVATE).edit();

    for (String i :
            map.keySet()) {
        editor.putString(i,map.get(i));
    }
    editor.commit();
    Activity activity;

}

public static void permissions(@NonNull Fragment fragment, String[] permissions, int code){
    int PERMISSION_ALL = code;




    if (!hasPermission(fragment.getActivity(), permissions)) {
        fragment.requestPermissions(permissions, 100);

    }


}
    public static boolean hasPermission(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {

                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
