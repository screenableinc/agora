package com.screenable.agora;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;


public class Agora extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }

}
