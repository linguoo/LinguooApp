package com.mkiisoft.linguoo.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class KeySaver {
	private static final String AWKEY = "Linguoo";
	private static final String AWPREFIX = "li_";
	
	public static String getIMEI( Activity a ) {
		TelephonyManager telephonyManager = (TelephonyManager) a.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}
	
	public static String getDeviceID( Activity a ) {
		return Secure.getString( a.getBaseContext().getContentResolver(), Secure.ANDROID_ID);
	}
	
	public static void saveShare(Activity a, boolean f ) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean( AWPREFIX + "shared", f);
	    editor.commit();
	}
	
	public static void saveShare(Activity a, int f ) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putInt( AWPREFIX + "shared", f);
	    editor.commit();
	}

	public static void saveShare(Activity a, String f ) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString( AWPREFIX + "shared", f);
	    editor.commit();
	}

	
	public static boolean getBoolSavedShare(Activity a) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
		return settings.getBoolean( AWPREFIX + "shared", false);
	}

	public static int getIntSavedShare(Activity a) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
		return settings.getInt( AWPREFIX + "shared", -1);
	}

	public static String getStringSavedShare(Activity a) {
		SharedPreferences settings = a.getSharedPreferences(AWKEY, Context.MODE_PRIVATE);
		return settings.getString( AWPREFIX + "shared", null);
	}
	
}
