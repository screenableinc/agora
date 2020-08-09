package com.screenable.agora.features;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.AudioManager;
import android.media.Image;
import android.media.ToneGenerator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.screenable.agora.MainActivity;
import com.screenable.agora.config.Config;
import com.screenable.agora.ui.main.fragments.Search;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class BarcodeScanner {

    BarcodeScannerOptions options;
    com.google.mlkit.vision.barcode.BarcodeScanner scanner;
    Activity context;


    public BarcodeScanner(Activity activity){
        this.context = activity;

        options = new BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_AZTEC, Barcode.FORMAT_EAN_13, Barcode.FORMAT_EAN_8)
        .build();
        scanner= BarcodeScanning.getClient(options);
    }
    public String scan(Bitmap image){
        Log.w(Config.APP_IDENT, "scanning");

        final Task<List<Barcode>> result = scanner.process(InputImage.fromBitmap(image,0))
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {

                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        // Task completed successfully
                        if(barcodes.size() > 0) {
                            analyse(barcodes.get(0));
                        }
//                        imageAnalysis.clearAnalyzer();






                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                        Log.w(Config.APP_IDENT,"nope:::: " + e.toString());
                    }
                });

        Log.w(Config.APP_IDENT, result+" :::");

    return null;
    }
    private void analyse(Barcode barcode){

            Rect bounds = barcode.getBoundingBox();
            Point[] corners = barcode.getCornerPoints();

            String rawValue = barcode.getRawValue();

            int valueType = barcode.getValueType();
            Log.w(Config.APP_IDENT,"value:::: " + valueType);
            if(valueType == Barcode.TYPE_PRODUCT){
                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Search.BarcodeSearch().execute(rawValue);


                    }
                });


            }



    }

}
