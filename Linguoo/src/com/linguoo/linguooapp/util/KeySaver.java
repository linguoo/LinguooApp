package com.linguoo.linguooapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class KeySaver {
	private static final String AWKEY = "Linguoo";
	private static final String AWPREFIX = "l1ng_";

	public static String getIMEI( Activity a ) {
		TelephonyManager telephonyManager = (TelephonyManager) a.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	public static String getDeviceID( Activity a ) {
		return Secure.getString( a.getBaseContext().getContentResolver(), Secure.ANDROID_ID);
	}

	public static void saveShare(Activity a,String keyname ,boolean f ) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean( AWPREFIX + keyname, f);
	    editor.commit();
	}

	public static void saveShare(Activity a,String keyname ,int f ) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putInt( AWPREFIX + keyname, f);
	    editor.commit();
	}

	public static void saveShare(Activity a,String keyname ,String f ) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString( AWPREFIX + keyname, f);
	    editor.commit();
	}


	public static boolean getBoolSavedShare(Activity a, String keyname) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
		return settings.getBoolean( AWPREFIX + keyname, false);
	}

	public static int getIntSavedShare(Activity a, String keyname) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
		return settings.getInt( AWPREFIX + keyname, -1);
	}

	public static String getStringSavedShare(Activity a, String keyname) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
		return settings.getString( AWPREFIX + keyname, null);
	}

}