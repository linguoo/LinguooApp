package com.linguoo.linguooapp.async;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
	
	public static void saveFile( File filename, byte[] data ) throws IOException {
        FileOutputStream fOut = new FileOutputStream( filename );

        fOut.write( data );
        
        fOut.flush();
        fOut.close();
	}
	
	public static byte[] readFile( File filename ) throws IOException {
		FileInputStream fIn = new FileInputStream( filename );
		
		byte[] buffer = new byte[ fIn.available() ];
		fIn.read(buffer);
		
		return buffer;
	}
	
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size=1024;
        try {
            byte[] bytes=new byte[buffer_size];
            for(;;) {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        } catch(Exception ex){}
    }
    
    public static boolean testConection(Context context)
    {
        Commons.info("Testing internet");
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public final static boolean isValidEmail(CharSequence target) {
	    if (target == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}
    public final static boolean isPackageInstalled(Activity activity,String thepackage){
        try{
            ApplicationInfo info = activity.getPackageManager().getApplicationInfo(thepackage, 0 );
           return true;
        } catch( PackageManager.NameNotFoundException e ){
           return false;
        }
    }

}