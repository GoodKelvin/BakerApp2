package com.kelvingabe.bakerapp;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class BakeApp extends Application {
    private static final String TAG = "BakeApp";
    private static Context sAppContext;
    private static ConnectivityManager sConnectivityManager;

    public static Context getAppContext() {
        return sAppContext;
    }

    public static boolean isConnectionAvailable() {
        boolean connected = false;
        NetworkInfo networkInfo = null;
        if (sAppContext != null) {
            if (sConnectivityManager != null) {
                networkInfo = sConnectivityManager.getActiveNetworkInfo();
            }
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) connected = true;
            Log.d(TAG, "Connection available? " + connected);
        }
        return connected;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;
        sConnectivityManager = (ConnectivityManager) sAppContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}
