package com.linguoo.linguooapp.util;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class GraphicsUtils {
	/**
	 * Convierte valores en Píxeles a Dp.
	 * @param px
	 * @param context
	 * @return float
	 */
	public static float pxToDp(float px, Context context)
	{
	    return px / context.getResources().getDisplayMetrics().density;
	}
	
	/**
	 * Convierte valores en Dp a Píxeles
	 * @param dp
	 * @param context
	 * @return float
	 */
	public static float dpTopx(float dp, Context context)
	{
	    return dp * context.getResources().getDisplayMetrics().density;
	}
	
	/**
	 * Obtiene el tamaño de la pantalla y lo retorna en un objeto Point(width,height).
	 * Point p= GraphicsUtil.getWindowsSize(context);
	 * int ancho = p.x;
	 * int alto = p.y;
	 * 
	 * p.x y p.y representarán píxeles.
	 * @param context
	 * @return Point
	 */
	public static Point getWindowSize(Context context){
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		display.getMetrics(metrics);
		return new Point(metrics.widthPixels,metrics.heightPixels);
	}

	/**
	 * Obtiene el tamaño de la pantalla y lo retorna en un objeto Point(width,height).
	 * PointF p= GraphicsUtil.getWindowsSizeDp(context);
	 * float ancho = p.x;
	 * float alto = p.y;
	 * 
	 * p.x y p.y representarán unidades dp.
	 * @param context
	 * @return PointF
	 */

	public static PointF getWindowSizeDp(Context context){
		Point p= getWindowSize(context);
		PointF pf = new PointF(pxToDp(p.x,context),pxToDp(p.y,context));
		return pf;
	}
	
	/**
	 * Obtiene la densidad de píxeles por pulgada de la pantalla del dispositivo.
	 * @param context
	 * @return int
	 */
	public static int getDeviceDip(Context context){
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		display.getMetrics(metrics);
		return metrics.densityDpi;
	}

	
}
