package com.dexa.getideaseed;

import android.app.Application;
import android.content.Context;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Dev on 04/03/18.
 */

public class GetIdeaSeedApplication extends Application {

    private static GetIdeaSeedApplication application;

    @Override public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        application = this;
    }

    public static Context getContext(){
        return application;
    }

}
