package com.linguoo.linguooapp.async;


public interface ConnectionListener {
	/**
	 * void ready( int msg, String message )
	 * 
	 * @param msg
	 * @param message
	 * 
	 * msg es un valor entero que se pasó al crear la instancia de AsyncConnection
	 * message es el valor que devuelve AsyncConnection
	 * 
	 */
	public void ready( int msg, String message );

	/**
	 * void cacheReady( int msg, String message )
	 * 
	 * @param msg
	 * @param message
	 * 
	 * msg es un valor entero que se pasó al crear la instancia de AsyncConnection
	 * message es el valor que devuelve AsyncConnection
	 * 
	 */

	public void cacheReady( int msg, String message );
}
