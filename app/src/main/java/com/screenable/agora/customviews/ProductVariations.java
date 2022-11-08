package com.screenable.agora.customviews;

import android.animation.Animator;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.screenable.agora.R;
import com.screenable.agora.apiaccess.Requests;
import com.screenable.agora.config.Config;
import com.screenable.agora.helpers.Helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class ProductVariations extends LinearLayout {
    private Context context;
    JSONObject reOrganizedVariationsObject = new JSONObject();
    ArrayList<String> variationNames = new ArrayList<>();
    ArrayList<String> variationIds=new ArrayList<>();
    JSONArray arr = new JSONArray();
    ArrayList<VariationOption> allOptions = new ArrayList<VariationOption>();
    View addToCart;
    String productId;
    View parent;
    String vendorId;
    private boolean allowedClick=false;

    public ProductVariations(Context context, String productId, String vendorId,View parent){
        super(context);
        this.context=context;this.parent = parent;
        this.addToCart = parent.findViewById(R.id.addtocart);
        this.vendorId = vendorId;
        inflate(context, R.layout.productvariations, this);
        this.productId = productId;
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);
        init(context);
        addToCart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allowedClick){
//                    link to perform add to cart
                    new AddToCart().execute();
                }else {
                    Toast.makeText(context,"please select all options",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public ProductVariations(Context context, AttributeSet attributeSet){
        super(context, attributeSet);








    }

    private void init(Context context){
//        initiate Async task to get variations from DB
        new GetVariations().execute(productId);

    }

    private void reOrganize(JSONArray variations) throws JSONException {
        //    this function reorganizes the variations gotten from the database in
//    a way that makes the variation names keys e.g color,network for easy traversal


        for (int i = 0; i < variations.length(); i++) {
//            get nth object
            JSONObject nthObject = variations.getJSONObject(i);
            String variantName = nthObject.getString("variantName");
            String variantValue = nthObject.getString("value");
            String variationId = nthObject.getString("variationId");
//            keep master list of all varition IDs

            if(!variationIds.contains(variationId)){
                variationIds.add(variationId);
            }

//            maintain list of attribute names to show
            if(!variationNames.contains(variantName)){
                variationNames.add(variantName);
            }


//            if key with variant name doesnt exist, create it
            if (!reOrganizedVariationsObject.has(variantName)) {

                reOrganizedVariationsObject.put(variantName, new JSONObject());
//                go ahead and put value in advance


                reOrganizedVariationsObject.getJSONObject(variantName).put(variantValue,new JSONArray().put(variationId));

            }else {
//                exists so add....note since it exists, then obiously a JSON array was created and has at least one value

                if(!reOrganizedVariationsObject.getJSONObject(variantName).has(variantValue)){
//                    go ahead and create
                    reOrganizedVariationsObject.getJSONObject(variantName).put(variantValue,new JSONArray().put(variationId));
                }else{
                    reOrganizedVariationsObject.getJSONObject(variantName).getJSONArray(variantValue).put(variationId);
                }


            }

        }

        Log.w(Config.APP_IDENT, reOrganizedVariationsObject.toString());

    }
    private void rMasterList(ArrayList<String> childList){
//this function updates the master variation IDs list when an option is clicked
//        the idea is to compare the child list and master list and update the master list in such a way that what wasnt initially in the masterlist is removed
//        remove from master list what isnt in child list

        variationIds.retainAll(childList);



    }
    private void aMasterList(ArrayList<String> childList){
        for (String element:
             childList) {
            if (!variationIds.contains(element)){
                variationIds.add(element);
            }
        }
    }
    private void toggleClickable(){
//        loop through all options
//this method might have a larger footprint
//        Helpers.Log(this.findViewById(R.id.ok).findViewWithTag("size").toString());
        boolean allClicked = true;
        for (String variationName:variationNames
             ) {



//            if (!(boolean) (this.findViewWithTag(variationName)).getTag()){
//                allClicked=false;
//            }

//            for (int i = 0; i < ((ViewGroup) this.findViewWithTag(variationName)).getChildCount(); i++) {
                for (int i = 0; i < allOptions.size(); i++) {

                VariationOption option = allOptions.get(i);
//                VariationOption option = (VariationOption) ((ViewGroup) this.findViewWithTag(variationName)).getChildAt(i);
                Helpers.Log(((View) option.getParent()).getTag()+"__");
//            loop through ids of all loops but only if id size is greater than 1

//                    check if parent has clicked option
                    boolean parentClicked =(Boolean) ((View) option.getParent()).getTag();
                    if(!parentClicked){
                        allClicked=false;
                    }
                    if (option.getIsClicked()){
//                dont know why continue or return is not working in this SE
                    }else {


                        option.disable();
                        boolean foundMatch = false;

                        if(option.getEligibleIds().size()>1){

                            for (int j = 0; j < option.getEligibleIds().size(); j++) {
                                if(variationIds.contains(option.getEligibleIds().get(j))){

                                    foundMatch=true;


                                }
                            }

                        }else {
                            if (variationIds.contains(option.getEligibleIds().get(0))){
                                foundMatch=true;
                            }
                        }
                        if (foundMatch){
                            Helpers.Log("called here");
                            option.enable();
                        }else {
                            Helpers.Log("Call but here");
                            option.disable();
                        }
                    }


            }
        }


        if (allClicked){
            allowedClick=true;
        }else{
            allowedClick = false;
        }

    }
    

    private void addOptionsToUI(ViewGroup holder, String variationName){
//        TODO: might need to move this to async class
        holder.setTag(false);


        try {
            Iterator attribute_options = reOrganizedVariationsObject.getJSONObject(variationName).keys();

            while (attribute_options.hasNext()){
                String option = (String) attribute_options.next();

                VariationOption variationoption = new VariationOption(context);
                TextView optionName = variationoption.findViewById(R.id.attr);

                variationoption.setIdentifier(option);

                try {
                   ArrayList<String> eligibleIds=Helpers.Listify(reOrganizedVariationsObject.getJSONObject(variationName).getJSONArray(option));

                    variationoption.setEligibleIds(eligibleIds);
                }catch (Exception e){

                    e.printStackTrace();
                }

                variationoption.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        disable all others that do not have same ID but first start with the one inn the same category

//

                        try {


                            variationoption.toggleClick();


                            if (variationoption.getIsClicked()){
                                ((View) view.getParent()).setTag(true);
//                                holder.setTag(true);
//                                holder.getTag(0);

                                rMasterList(((VariationOption) view).getEligibleIds());
                            }else {
//                                holder.setTag(0,false);
                                ((View) view.getParent()).setTag(false);
                                aMasterList(((VariationOption) view).getEligibleIds());
                            }

                            toggleClickable();

                    }catch (Exception e){
//                            TODO take care of these
                            e.printStackTrace();


                        }

                    }
                });
                optionName.setText(option);


                arr.put(variationoption);
                allOptions.add(variationoption);




                holder.addView(variationoption);



            }
            Helpers.Log(arr+";;");

        } catch (JSONException e){
            Toast.makeText(context, "failed bro",Toast.LENGTH_LONG).show();
        }
        }
    private void addVariationsToUI(){


//        create horizontal layout with inner horizontal scrollview to house attributes
        for (int i = 0; i < variationNames.size(); i++) {
            String variationName = variationNames.get(i);
            ViewGroup variationsoption = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.variationoptions,this,false);
//            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            variationsoption.setLayoutParams(params);
            //            Add options to UI, but before that, set ID to variable name
//            variationsoption.setTag(variationName);
            LinearLayout options =  variationsoption.findViewById(R.id.options_holder);



//            call function to set options passing in view to hold options as parameter

            addOptionsToUI(options,variationName);


            TextView textView = variationsoption.findViewById(R.id.variation_name);
            textView.setText(variationNames.get(i)+":");


            this.addView(variationsoption);


        }


    }
    private class AddToCart extends AsyncTask<String,Integer,String>{
        boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            remove button and add loader
            parent.findViewById(R.id.addtocart).setVisibility(GONE);
            parent.findViewById(R.id.loading).setVisibility(VISIBLE);

        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                HashMap <String, String> map = new HashMap<>();
                map.put("vendorId",vendorId);
                map.put("variationId",variationIds.get(0));
                map.put("productId",productId);
                String params = Helpers.JSONify(map);
                JSONObject response = new Requests(context).sendPost(Config.add_cart,params);
                if(response.getBoolean("success")) {

                    success = true;
                }
            }catch (Exception e){
                e.printStackTrace();



            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            parent.findViewById(R.id.loading).setVisibility(GONE);
            if(success){
//                show UI positive

                LottieAnimationView animationView =parent.findViewById(R.id.success);
                animationView.setVisibility(VISIBLE);
                animationView.playAnimation();
                animationView.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
//                        close
                        ((ViewGroup) parent.getParent()).removeView(parent);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }else{
                parent.findViewById(R.id.addtocart).setVisibility(VISIBLE);
//                ask user to try again if network, if not, alert developer
            }
        }
    }

    private class GetVariations extends AsyncTask<String, Integer, String> {
        Boolean success = false;
        @Override
        protected String doInBackground(String... strings) {
            try{
                String[] params = {"productId"};
                String[] values = {strings[0]};
                JSONObject variations = new Requests(context).sendGET(Config.variationsLink,params, values);

                reOrganize(variations.getJSONArray("response"));
                success = true;
            }catch (Exception e){
                e.printStackTrace();

                Log.w(Config.APP_IDENT, "this failed "+e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //        call function to add variation names to UI
            if(!success){
                Toast.makeText(context, "failed somewhere, inspect", Toast.LENGTH_LONG).show();
            }else {
                addVariationsToUI();
            }

        }
    }

}
//                        for (int i = 0; i < holder.getChildCount(); i++) {
////                            if it is not the option selected, disable the rest
//                            //                        also, every time an attribute (variation) is clicked/enabled remove from the master list those that arent, elsem add them back
//                            VariationOption currentTarget = ((VariationOption)holder.getChildAt(i));
//
//                                if (!currentTarget.getIdentifier().equals(option) && variationoption.isClicked) {
////                                    only disable those that doont share the same variation ID
//
//                                    currentTarget.disable();
//                                }else if (!currentTarget.getIdentifier().equals(option) && !variationoption.isClicked){
//                                    currentTarget.enable();
//
//                                }
//                            }
//                        call function to check other attributes and disable or enable those that have a matching variation ID

