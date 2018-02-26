package com.vachanasaahitya.vachanas;

import android.app.Application;

import com.vachanasaahitya.vachanas.db.DatabaseHelper;

/**
 * Created by narensmac on 26/02/18.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper.copyFile(this);
    }
}
