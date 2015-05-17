package com.nexion.tchatroom.api;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nexion.tchatroom.R;

/**
 * Created by DarzuL on 08/05/2015.
 * <p/>
 * Class to manage Api errors
 */
public class ErrorHandler {
    public static void toastError(Context context, VolleyError error) {
        Log.e("API error", error.toString());
        Integer statusCode = error.networkResponse == null ? null : error.networkResponse.statusCode;

        String msg;
        if (statusCode == null)
            return;

        if (statusCode >= 500 && statusCode < 600) {
            msg = context.getString(R.string.error_5xx);
        } else if (statusCode >= 300 && statusCode < 400) {
            msg = context.getString(R.string.error_3xx);
        } else {
            msg = context.getString(R.string.error_unknown);
        }

        switch (statusCode) {
            case 403:
                msg = context.getString(R.string.error_403);
                break;

            case 404:
                msg = context.getString(R.string.error_404);
                break;

            case 498:
                msg = context.getString(R.string.error_498);
                break;
        }

        if (msg != null) {
            Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public static Integer getStatusCode(VolleyError error) {
        return error.networkResponse == null ? null : error.networkResponse.statusCode;
    }
}
