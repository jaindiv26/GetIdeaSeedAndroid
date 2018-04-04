package com.dexa.getideaseed;

import com.android.volley.VolleyError;

/**
 * Created by Dev on 02/03/18.
 */

public interface ApiManagerListener {

    void onSuccess(String response);
    void onError(VolleyError error);
    void statusCode(int statusCode);
}
