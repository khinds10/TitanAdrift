package com.kevinhinds.spacebots.status;

import com.kevinhinds.spacebots.GameConfiguration;
import com.kevinhinds.spacebots.MainGameActivity;
import com.kevinhinds.spacebots.ResourceManager;

/**
 * main status of the game for player, persisted to Android SharedPreferences
 * 
 * @author khinds
 */
public class GameStatus {

	/** list of keys used for the SharedPreferences references */
	public static String HighScore = "HighScore";
	public static String MusicPlay = "MusicPlay";
	public static String SoundFXPlay = "SoundFXPlay";
	public static String LevelStatus = "LevelStatus";
	public static String ShipRepairedStatus = "ShipRepairedStatus";

	/**
	 * code that will run if it's the first time we've installed the application
	 */
	public static void setDefaultGameStatus() {

		MainGameActivity activity = ResourceManager.getIntance().activity;
		activity.statusAndPreferencesEditor.putInt(GameStatus.HighScore, 0);
		activity.statusAndPreferencesEditor.putBoolean(GameStatus.MusicPlay, false);
		activity.statusAndPreferencesEditor.putBoolean(GameStatus.SoundFXPlay, false);

		/** create a list of all new level status numbers, marking level 1 as "1" or available to play */
		String levelStats = StatusListManager.createDefaultCSVList(GameConfiguration.numberLevels, "0");
		levelStats = StatusListManager.updateIndexByValue(levelStats, 1, "1");
		activity.statusAndPreferencesEditor.putString(GameStatus.LevelStatus, levelStats);

		/** update FirstRun to say we're no longer running game for the first time and commit default values */
		activity.statusAndPreferencesEditor.putBoolean("FIRSTRUN", false);
		activity.statusAndPreferencesEditor.commit();
	}

	/**
	 * collect ship piece by name by saving it to the game status list for it
	 * 
	 * @param pieceName
	 */
	public static void collectShipPiece(String pieceName) {
		MainGameActivity activity = ResourceManager.getIntance().activity;
		String shipRepairedStatus = activity.statusAndPreferences.getString(GameStatus.ShipRepairedStatus, "");
		if (!StatusListManager.containsValue(shipRepairedStatus, pieceName)) {
			shipRepairedStatus = shipRepairedStatus + pieceName + ",";
			activity.statusAndPreferencesEditor.putString(GameStatus.ShipRepairedStatus, shipRepairedStatus);
			activity.statusAndPreferencesEditor.commit();
		}
	}

	/**
	 * get the current status of ship in repair to show in UI
	 * 
	 * @return
	 */
	public static String getShipRepairedStatus() {
		return ResourceManager.getIntance().activity.statusAndPreferences.getString(GameStatus.ShipRepairedStatus, "");
	}

	/** determine if we're supposed to play music in the game */
	public static boolean playMusic() {
		return ResourceManager.getIntance().activity.statusAndPreferences.getBoolean(GameStatus.MusicPlay, true);
	}

	/** set play music or not in game */
	public static void setPlayMusicPreference(boolean playMusic) {
		ResourceManager.getIntance().activity.statusAndPreferencesEditor.putBoolean(GameStatus.MusicPlay, playMusic);
		ResourceManager.getIntance().activity.statusAndPreferencesEditor.commit();
	}

	/** determine if we're supposed to play sound effects in the game */
	public static boolean playSoundFX() {
		return ResourceManager.getIntance().activity.statusAndPreferences.getBoolean(GameStatus.SoundFXPlay, true);
	}

	/** set play sound effects or not in game */
	public static void setPlaySoundEffectsPreference(boolean playSoundFX) {
		ResourceManager.getIntance().activity.statusAndPreferencesEditor.putBoolean(GameStatus.SoundFXPlay, playSoundFX);
		ResourceManager.getIntance().activity.statusAndPreferencesEditor.commit();
	}

	/**
	 * generate comma separated list of current status code for each level in the game, updating the one specified to a new value
	 * 
	 * @param levelNumber
	 * @param status
	 */
	public static void setLevelStatusByLevelNumber(int levelNumber, String status) {
		MainGameActivity activity = ResourceManager.getIntance().activity;
		String NewLevelStatusList = StatusListManager.updateIndexByValue(activity.statusAndPreferences.getString(GameStatus.LevelStatus, ""), levelNumber, status);
		activity.statusAndPreferencesEditor.putString(GameStatus.LevelStatus, NewLevelStatusList);
		ResourceManager.getIntance().activity.statusAndPreferencesEditor.commit();
	}

	/**
	 * for a given level in the game return the current status as far as an integer
	 * 
	 * @param levelNumber
	 * @return
	 */
	public static int levelStatusByLevelNumber(int levelNumber) {
		MainGameActivity activity = ResourceManager.getIntance().activity;
		String levelStatus = StatusListManager.getValueByIndex(activity.statusAndPreferences.getString(GameStatus.LevelStatus, ""), levelNumber);
		return Integer.parseInt(levelStatus);
	}
}