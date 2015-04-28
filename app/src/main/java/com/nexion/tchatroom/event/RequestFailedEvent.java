package com.nexion.tchatroom.event;

import android.content.Context;

import com.nexion.tchatroom.R;

/**
 * Created by DarzuL on 21/03/2015.
 */
public class RequestFailedEvent {

    private final Context mContext;
    private final Integer mStatusCode;

    public RequestFailedEvent(Context context, Integer statusCode) {
        mContext = context;
        mStatusCode = statusCode;
    }

    @Override
    public String toString() {

        if(mStatusCode == null)
            return null;

        switch (mStatusCode) {
            case 404:
                return mContext.getString(R.string.error_404);

            case 403:
                return mContext.getString(R.string.error_403);
        }

        if (mStatusCode >= 500 && mStatusCode < 600)
            return mContext.getString(R.string.error_5xx);

        if (mStatusCode >= 300 && mStatusCode < 400)
            return mContext.getString(R.string.error_3xx);

        return mContext.getString(R.string.error_unknown);
    }
}