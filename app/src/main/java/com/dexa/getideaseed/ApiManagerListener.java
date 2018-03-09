package com.dexa.getideaseed;

import com.android.volley.VolleyError;

/**
 * Created by Dev on 02/03/18.
 */

public interface ApiManagerListener {

    public void onSuccess(String response);
    public  void onError(VolleyError error);
    public  void statusCode(int statusCode);
}
