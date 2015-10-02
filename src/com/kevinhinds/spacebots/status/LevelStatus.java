package com.kevinhinds.spacebots.status;

import com.kevinhinds.spacebots.ResourceManager;

/**
 * current status of the player in a particular level, persisted to Android SharedPreferences
 * 
 * @author khinds
 */
public class LevelStatus {

	/** list of keys used for the SharedPreferences references */
	public static String HitPoints = "HitPoints";
	public static String ItemsAquired = "ItemsList";

	/**
	 * start new level with default level begin status
	 */
	public static void startLevel() {
		ResourceManager.getIntance().activity.statusAndPreferencesEditor.putInt(LevelStatus.HitPoints, 1);
	}
}