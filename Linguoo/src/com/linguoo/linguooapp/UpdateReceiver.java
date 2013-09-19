package com.linguoo.linguooapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class UpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
          ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE );
          NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
          boolean isConnected = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();   
          if (isConnected)       
              Log.i("NET", "Conectado a Internet - " +isConnected);   
          else Log.i("NET", "Sin conexi�n a Internet - " +isConnected);
        }
    }