package com.dexa.getideaseed;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Dev on 21/04/18.
 */

public class BaseFragment extends Fragment {

    public boolean isInternetOn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            Log.v("log", "Internet is working");
            // txt_status.setText("Internet is working");
            return true;
        } else {
            Log.v("log", "No internet access");
            Toast.makeText(context,"No internet access",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
