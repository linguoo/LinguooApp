package com.mkiisoft.linguoo.util;


public interface ConnectionListener {
	public void ready( int msg, String message );
	public void cacheReady( int msg, String message );
}
