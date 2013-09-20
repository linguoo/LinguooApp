package com.linguoo.linguooapp.player;

public interface LinguooUIManagerInterface {
	public static final int UI_SHOW_MAIN_VIEW = 0;
	public static final int UI_ADD_TO_PLAY_LIST = 1;
	public static final int UI_REMOVE_FROM_PLAY_LIST = 2;
	public static final int UI_ITEM_SELECTED = 3;
	public static final int UI_CONFIG = 4;
	public static final int UI_AUTO_PLAY_ON = 5;
	public static final int UI_AUTO_PLAY_OFF = 6;
	public static final int UI_USER = 7;
	public static final int UI_ADD_CATEGORY = 8;
	public static final int UI_PLAY = 9;
	public static final int UI_PAUSE = 10;
	public static final int UI_MOVE_FORWARD = 11;
	public static final int UI_FACEBOOK_SHARE = 12;
	public static final int UI_GOOGLE_SHARE = 13;
	public static final int UI_TWITTER_SHARE = 14;
	
	public void UIStatusHandler(int status, int value);
}
