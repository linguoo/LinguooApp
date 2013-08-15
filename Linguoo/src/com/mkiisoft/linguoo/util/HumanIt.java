package com.mkiisoft.linguoo.util;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

public class HumanIt {
	
	private final String TAG="HumanIt";
	private MediaPlayer mp;
	private Context ctx;
	
	public HumanIt(Context ctx){
		this.ctx=ctx;
	}

	public void destroyMp(){
		if (mp!=null){
			if (mp.isPlaying()){
            	mp.stop();
            }
			mp.release();
			mp=null;
		}
	}

	public void sayIt(String url){
		AssetFileDescriptor uri = null;
        this.destroyMp();
		mp=new MediaPlayer();
		if (url.equals("")==false){
        	try {
				uri = ctx.getAssets().openFd(url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try{
			if (uri!=null){
				mp.setDataSource(uri.getFileDescriptor(),uri.getStartOffset(),uri.getLength());
				mp.prepare();
				uri.close();
	        	mp.start();
	        	Log.d(TAG,"Preparando el reproductor");
	        }
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void sayIt(int btn) {
		AssetFileDescriptor uri = null;
        this.destroyMp();
		mp=new MediaPlayer();
    	try {

		switch (btn){
	        case Constants.BTN_RSSNOTI:
	        	uri = ctx.getAssets().openFd("noticias.mp3");
	        	break;
	        case Constants.BTN_LNOTI:
	        	uri = ctx.getAssets().openFd("linguoo_noticias.mp3");
	        	break;
	        case Constants.BTN_LIBROS:
	        	uri = ctx.getAssets().openFd("libros.mp3");
	        	break;
	        case Constants.BTN_BLOGS:
	        	uri = ctx.getAssets().openFd("linguoo_libros.mp3");
	        	break;
	        case Constants.BTN_SALIR:
	        	uri = ctx.getAssets().openFd("salir.mp3");
	        	break;
	        case Constants.BTN_CONF:
	        	uri = ctx.getAssets().openFd("configuraciones.mp3");
	        	break;
	        case Constants.BTN_FAV:
	        	uri = ctx.getAssets().openFd("doble_click.mp3");
	        	break;
	        case Constants.BTN_REG:
	        	uri = ctx.getAssets().openFd("regresar.mp3");
	        	Log.d(TAG,"Btn Regresar");
	        	break;
	        case Constants.WELCOME:
	        	uri = ctx.getAssets().openFd("bienvenido.mp3");
	        	break;
	        case Constants.CAT_ACT:
	        	uri = ctx.getAssets().openFd("actualidad.mp3");
	        	break;
	        case Constants.CAT_CUL:
	        	uri = ctx.getAssets().openFd("cultura.mp3");
	        	break;
	        case Constants.CAT_DEP:
	        	uri = ctx.getAssets().openFd("deportes.mp3");
	        	break;
	        case Constants.CAT_ENT:
	        	uri = ctx.getAssets().openFd("entretenimiento.mp3");
	        	break;
	        case Constants.CAT_NEG:
	        	Log.d(TAG,"Btn Negocios");
	        	uri = ctx.getAssets().openFd("negocios.mp3");
	        	break;
	        default:
	        	uri=null;
	        
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
    	try {

	    	if (uri!=null){
				mp.setDataSource(uri.getFileDescriptor(),uri.getStartOffset(),uri.getLength());
				mp.prepare();
				uri.close();
	        	mp.start();
	        	Log.d(TAG,"Preparando el reproductor");
	        }
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	


}
