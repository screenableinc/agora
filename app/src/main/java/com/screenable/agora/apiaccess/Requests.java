package com.screenable.agora.apiaccess;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import static com.screenable.agora.helpers.Helpers.GenQueryString;

public class Requests {
    public static JSONObject sendPost(String url, String parameters) throws Exception{

        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        // Acts like a browser
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        byte[] out = parameters .getBytes(StandardCharsets.UTF_8);
        int length = out.length;
        conn.setRequestProperty("User-Agent", "Android app");
        conn.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        conn.setConnectTimeout(5000);
//        for (String cookie : this.cookies) {
//            conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
//        }
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Referer", url);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setFixedLengthStreamingMode(length);

        conn.setDoOutput(true);
        conn.setDoInput(true);

        // Send post request
        OutputStream wr = conn.getOutputStream();
        wr.write(out);
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + parameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        // System.out.println(response.toString());

        return new JSONObject(response.toString());

    }
    public JSONObject sendGET(String url, String[] parameters, String[] values) throws Exception {
        HttpsURLConnection conn;
        String queryString = GenQueryString(parameters, values);
        URL obj = new URL(url+"/?"+queryString);

        conn = (HttpsURLConnection) obj.openConnection();


        // Acts like a browser
        conn.setUseCaches(false);
        conn.setRequestMethod("GET");

        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 8.0.0; SM-G960F Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.84 Mobile Safari/537.36");
        conn.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        conn.setConnectTimeout(5000);


//        for (String cookie : this.cookies) {
//            conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
//        }
//        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Referer", url);
        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");



        conn.setDoOutput(false);
        conn.setDoInput(true);



        int responseCode = conn.getResponseCode();


        BufferedReader in =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        conn.disconnect();
        // System.out.println(response.toString());

        return new JSONObject(response.toString());

    }
}
