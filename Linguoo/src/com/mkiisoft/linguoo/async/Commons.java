package com.mkiisoft.linguoo.async;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Commons {
	public static final String TAG = "Linguoo";
	public static final String cacheDir = "Linguoo";
	
	public static void info( String msg ) {
		Log.i(Commons.TAG, msg);
	}
	
	public static void error( String msg ) {
		Log.e(Commons.TAG, msg);
	}
	
	public static File getDirectory() {
		File cacheDir = null;
		//Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(), Commons.TAG);
        else
            return null;
        
        if(!cacheDir.exists())
            cacheDir.mkdirs();
        
        return cacheDir;
	}
	
	public static void fullDialog( Activity context,String title, String msg ) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setMessage(msg);
		builder.setTitle(title);
		
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public static void fullDialog( Activity context,int title, int msg ) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public static void dialog( Activity context, int msg ) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setMessage(msg);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public static void dialog( Activity context, String msg ) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setMessage(msg);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public static void dialogFinish( final Activity context, String msg ) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setMessage(msg);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				context.finish();
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	
	/**
	 * Solo para depuraci—n.
	 * 
	 * Permite imprimir en el log todos los parametros que vienen cargados en un Intent,
	 * mediante su bundle.
	 * 
	 * @param a Intent que contiene el Bundle cargado de datos.
	 */
	public static void printExtras( Bundle bundle ) {
		/*
    	 * Only for test
    	 * 
    	 * Print all extras
    	 * 
    	 */
    	try {
    		Iterator<String> keys = bundle.keySet().iterator();
    		
    		for( ; keys.hasNext() ; ) {
    			String key = keys.next();
    			Log.i( Commons.TAG, " -> " + key + ": " + bundle.get(key) );
    		}
    	} catch( Exception e ) {
    		Log.e(Commons.TAG, "Error reading tags: " + e.toString() );
    	}
	}
	
	/**
	 * Solo para depuraci—n.
	 * 
	 * Permite imprimir en el log todos los parametros que vienen cargados en un Intent,
	 * mediante su bundle.
	 * 
	 * @param a Activity con el Intent que contiene el Bundle cargado de datos.
	 */
	public static void printExtras( Activity a ) {
		/*
    	 * Only for test
    	 * 
    	 * Print all extras
    	 * 
    	 */
    	try {
    		Bundle bundle = a.getIntent().getExtras();
    		Commons.printExtras(bundle);
    	} catch( Exception e ) {
    		Log.e(Commons.TAG, "Error reading tags: " + e.toString() );
    	}
	}
	
	/**
	 * Solo para depuraci—n.
	 * 
	 * Permite imprimir en el log todos los parametros que vienen cargados en un Intent,
	 * mediante su bundle.
	 * 
	 * @param a Activity con el Intent que contiene el Bundle cargado de datos.
	 */
	public static void printExtras( Intent a ) {
		/*
    	 * Only for test
    	 * 
    	 * Print all extras
    	 * 
    	 */
    	try {
    		Bundle bundle = a.getExtras();
    		Commons.printExtras(bundle);
    	} catch( Exception e ) {
    		Log.e(Commons.TAG, "Error reading tags: " + e.toString() );
    	}
	}
	
	
	/**
	 * Permite eliminar toda la informaci—n del un Bundle.
	 * Util cuando necesitamos saber si la informaci—n se pisa.
	 * 
	 * @param a
	 */
	public static void removeExtras( Activity a ) {
		/*
    	 * Only for test
    	 * 
    	 * Print all extras
    	 * 
    	 */
    	try {
    		Log.i(Commons.TAG, "Removing all extras of " + a.getLocalClassName() );
    		Bundle bundle = a.getIntent().getExtras();
    		Iterator<String> keys = bundle.keySet().iterator();
    		
    		for( ; keys.hasNext() ; ) {
    			String key = keys.next();
    			a.getIntent().removeExtra( key );
    			Log.i( Commons.TAG, " -> " + key + ": REMOVED");
    		}
    	} catch( Exception e ) {
    		Log.e(Commons.TAG, "Error removing tags: " + e.toString() );
    	}
	}
	
	/**
	 * Permite eliminar toda la informaci—n del un Bundle.
	 * Util cuando necesitamos saber si la informaci—n se pisa.
	 * 
	 * @param a
	 */
	public static void removeExtras( Intent a ) {
		/*
    	 * Only for test
    	 * 
    	 * Print all extras
    	 * 
    	 */
    	try {
    		Log.i(Commons.TAG, "Removing all extras" );
    		Bundle bundle = a.getExtras();
    		Iterator<String> keys = bundle.keySet().iterator();
    		
    		for( ; keys.hasNext() ; ) {
    			String key = keys.next();
    			a.removeExtra( key );
    			Log.i( Commons.TAG, " -> " + key + ": REMOVED");
    		}
    	} catch( Exception e ) {
    		Log.e(Commons.TAG, "Error removing tags: " + e.toString() );
    	}
	}
	
	public static String readFileAsString(String filePath) throws java.io.IOException
	{
	    BufferedReader reader = new BufferedReader(new FileReader(filePath));
	    String line, results = "";
	    while( ( line = reader.readLine() ) != null)
	    {
	        results += line;
	    }
	    reader.close();
	    return results;
	}
}
