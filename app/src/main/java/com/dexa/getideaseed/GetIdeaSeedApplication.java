package com.dexa.getideaseed;

import android.app.Application;
import android.content.Context;

/**
 * Created by Dev on 04/03/18.
 */

public class GetIdeaSeedApplication extends Application {

    private static GetIdeaSeedApplication application;

    @Override public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static Context getContext(){
        return application;
    }

}
