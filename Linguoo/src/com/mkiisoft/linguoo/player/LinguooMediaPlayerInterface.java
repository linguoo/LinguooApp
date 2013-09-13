package com.mkiisoft.linguoo.player;

public interface LinguooMediaPlayerInterface {
	public static final int ON_PLAYER_STARTED = 0;
	public static final int ON_PLAYER_ERROR = 1;
	public static final int ON_PLAYER_LOADING = 2;
	public static final int ON_PLAYER_READY = 3;
	public static final int ON_PLAYER_PLAYING = 4;
	public static final int ON_PLAYER_STOP = 5;
	public static final int ON_PLAYER_PAUSE = 6;
	public static final int ON_PLAYER_RESUME = 7;
	public static final int ON_PLAYER_RELEASE = 8;
	public static final int ON_PLAYER_COMPLETE = 9;
	public static final int ON_PLAYER_MOVEFORWARD = 10;
	public static final int ON_INVALID_URL = 11;
	
	public void playerStatusHandler(int status);
	public void playerProgressHandler(int value);
	public void playerTitleHandler(String title, String image);
}
