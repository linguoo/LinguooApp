package com.mkiisoft.linguoo.player;

public interface LinguooMediaPlayerInterface {
	public static final int ON_PLAYER_ERROR = 0;
	public static final int ON_PLAYER_LOADING = 1;
	public static final int ON_PLAYER_READY = 2;
	public static final int ON_PLAYER_PLAYING = 3;
	public static final int ON_PLAYER_STOP = 4;
	public static final int ON_PLAYER_PAUSE = 5;
	public static final int ON_PLAYER_RESUME = 6;
	public static final int ON_PLAYER_RELEASE = 7;
	public static final int ON_PLAYER_COMPLETE = 8;
	public static final int ON_INVALID_URL = 9;
	
	public void playerStatusHandler(int status);
	public void playerProgressHandler(int value);
	public void playerTitleHandler(String title, String image);
}
