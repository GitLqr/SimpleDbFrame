package com.lqr.simpledbframe;

import android.app.Application;

import com.lqr.simpledbframe.customer.db.BaseDaoFactory;

import java.io.File;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BaseDaoFactory.init(new File(getFilesDir(), "lqr_user.db").getAbsolutePath());
    }
}
