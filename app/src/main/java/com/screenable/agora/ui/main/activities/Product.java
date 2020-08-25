package com.screenable.agora.ui.main.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;
import com.facebook.drawee.view.SimpleDraweeView;
import com.screenable.agora.R;
import com.screenable.agora.config.Config;
import com.screenable.agora.customviews.VendorTextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;

public class Product extends AppCompatActivity {

    ImageView image;
    TextView productName;
    TextView description;
    TextView price;
    VendorTextView vendor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
//        getWindow().setSharedElementEnterTransition(DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP,ScalingUtils.ScaleType.FIT_CENTER));
//        getWindow().setSharedElementReturnTransition(DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.FIT_CENTER, ScalingUtils.ScaleType.CENTER_CROP));

        image = findViewById(R.id.product_image);

        productName = findViewById(R.id.product_name);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        vendor = findViewById(R.id.vendor_name);

        HashMap<String,String> data =(HashMap<String, String>) getIntent().getExtras().get("data");
        String productId = data.get("productId");
        String picUrl = Config.productImages+"?productId="+productId;
//        work on mem issues later
        Picasso.get().load(picUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                assert image != null;
                image.setImageBitmap(bitmap);
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@Nullable Palette palette) {

                        Palette.Swatch textSwatch = palette.getDominantSwatch();
                        if(textSwatch == null){
                            Toast.makeText(getApplicationContext(),"Failed to swatch",Toast.LENGTH_LONG).show();
                            return;

                        }
                        findViewById(R.id.image_bg).setBackgroundColor(textSwatch.getRgb());

                    }
                });
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });


        vendor.setVendorId(data.get("vendorId"));
        productName.setText(data.get("productName"));
        description.setText(data.get("description"));
        Log.w(Config.APP_IDENT,data.toString());
        vendor.setText(data.get("businessName"));
    }

}