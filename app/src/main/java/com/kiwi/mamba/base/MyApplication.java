package com.kiwi.mamba.base;

import android.app.Application;

import com.codelang.api.Mamba;
import com.kiwi.mamba.loader.CostTimeLoader;
import com.kiwi.mamba.loader.TrackLoader;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Mamba.init(new CostTimeLoader());
//        Mamba.init(new TrackLoader());

    }
}
