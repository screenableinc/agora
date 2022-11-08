package com.screenable.agora.customviews;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.screenable.agora.R;
import com.screenable.agora.apiaccess.Requests;
import com.screenable.agora.config.Config;
import com.screenable.agora.helpers.Helpers;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.jar.JarException;

public class CartOption extends CardView {
    HorinSelect quantity_selector;
    ImageView itemImage;
    TextView variations;
    TextView productName;
    ImageView remove;
    String productId;
    String variationId;
    RadioGroup paymentOptions;
    ProgressBar sending;
    Button pay;
    CardView cardViewMain=this;
//    TODO::these payment options should be made dynamic in future
    String[] acceptable_options={"airtelMoney","mtnMoney"};
//    public CartOption(Context context, AttributeSet attributeSet){
//        super(context, attributeSet);
////        honestly dont know if this can be removed
//
//    }
    public CartOption(Context context, JSONObject cartItem){
        super(context);
        inflate(context, R.layout.cart_option,this);
//        attach quantity counter
        this.quantity_selector = new HorinSelect(context);
//        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        ((ViewGroup) findViewById(R.id.number_selector)).addView(this.quantity_selector);
        pay = findViewById(R.id.pay_btn);
        variations = findViewById(R.id.variations);
        itemImage = findViewById(R.id.product_image);
        remove = findViewById(R.id.remove);
        sending = findViewById(R.id.sendingorder);
        productName = findViewById(R.id.productname);
        paymentOptions = findViewById(R.id.payment_options);
        Helpers.Log("this was added");
        pay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                bring options for accepted payments
                if(!paymentOptions.isSelected()){

//                    send order through
                    new Order().execute();
                }else {
//                    alert should have selected payment
                    Toast.makeText(context, "should have selected",Toast.LENGTH_LONG).show();
                }



            }
        });

        try {
            attachVariations(cartItem.getJSONArray("variations"));
            setUpPaymentOptions();
            attachImage(cartItem.getString("productId"));
            productId = cartItem.getString("productId");
            variationId = cartItem.getString("variationId");
            productName.setText(cartItem.getString("productName"));
        }catch (JSONException e){
            e.printStackTrace();
        }

    }
    private void attachImage(String productId){
        Picasso.get().load(Config.productImages+productId).into(itemImage);
    }
    private void attachVariations(JSONArray theVariations) throws JSONException{
        StringBuilder finalString = new StringBuilder();
        for (int i = 0; i < theVariations.length(); i++) {
            finalString.append(theVariations.getJSONObject(i).getString("value")+" ");

        }
        variations.setText(finalString);
    }
    private void setUpPaymentOptions() throws JSONException {
        for (int i = 0; i < acceptable_options.length; i++) {
           RadioButton option = new RadioButton(getContext());
           option.setText(acceptable_options[i]);
           paymentOptions.addView(option);
        }
//        finally add cash option::might need to be removed later on
        RadioButton cash = new RadioButton(getContext());
        cash.setText("Cash");
        paymentOptions.addView(cash);

    }
    public class Order extends AsyncTask<String, Integer, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cardViewMain.setVisibility(GONE);
            sending.setVisibility(VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
//            the work is here
            try{
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("username", Helpers.getUsername(getContext()));
                parameters.put("variationId", variationId);
                parameters.put("productId", productId);
                parameters.put("paymentOption","1");
                JSONObject response = new Requests(getContext()).sendPost(Config.makeorder+"/", Helpers.JSONify(parameters));
            }catch (Exception e ){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cardViewMain.setVisibility(VISIBLE);
            sending.setVisibility(GONE);
        }
    }
}
