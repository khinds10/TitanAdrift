package com.kevinhinds.spacebots.scene;

import java.util.ArrayList;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;

import com.kevinhinds.spacebots.GameConfiguration;
import com.kevinhinds.spacebots.ResourceManager;
import com.kevinhinds.spacebots.objects.Piece;
import com.kevinhinds.spacebots.status.GameStatus;
import com.kevinhinds.spacebots.status.StatusListManager;

import android.util.Log;

/**
 * credits scene for the game
 * 
 * @author khinds
 */
public class LevelStatusScene extends BaseScene implements IOnMenuItemClickListener {

	private MenuScene menu;
	private final int MENU_BACK = 1;
	private final int MENU_NEXT = 2;
	private final int MENU_AGAIN = 4;
	private final int MENU_REBUILD = 5;
	private final int MENU_STATUS = 6;
	public ArrayList<Piece> foundPieces = new ArrayList<Piece>();

	@Override
	public void createScene() {
		createMenu();
		showShipStatus();
	}

	@Override
	public void onBackPressed() {
		SceneManager.getInstance().returnToMenuScene();
	}

	/**
	 * show how far the player has gone in rebuilding thier ship
	 */
	private void showShipStatus() {

		int x = (int) (ResourceManager.getIntance().camera.getWidth() / 2);
		int y = 50;
		ArrayList<Piece> shipPieces = ResourceManager.getIntance().getShipPieces();
		String shipStatus = GameStatus.getShipRepairedStatus();

		/** create the display of the ship in its current status being built by what the user has collected */
		int count = 0;
		for (int i = 0; i < GameConfiguration.pieceMapColumns * GameConfiguration.pieceMapRows; i++) {

			// create a new found piece from the complete list of pieces you can find
			Piece thisPiece = shipPieces.get(i);
			Integer pieceID = thisPiece.getId();
			Piece foundPiece = new Piece("Found Piece " + Integer.toString(pieceID), String.valueOf(pieceID), i, i, 0, 0, 0f, 0f, 0f, ResourceManager.getIntance().pieceRegion, vbom);

			// if the current list of pieces the player aquired contains the piece then show that we have it on the screen, else if it's a piece they don't have to collect then just show it
			if (StatusListManager.containsValue(shipStatus, pieceID.toString()) || !StatusListManager.containsValue(GameConfiguration.shipPiecesToCollect, pieceID.toString())) {
				foundPiece.setPosition(x + (count * GameConfiguration.pieceMapTileSize), y);
				try {
					foundPiece.attach(this);
				} catch (Exception e) {
					Log.e("Could not attached ship piece", e.getMessage());
				}
			}
			count++;
			if (count == GameConfiguration.pieceMapColumns) {
				count = 0;
				y = y + GameConfiguration.pieceMapTileSize;
			}
		}
	}

	/**
	 * create menu elements from local sprites to display
	 */
	private void createMenu() {
		menu = new MenuScene(camera);
		menu.setPosition(0, 0);

		// based on level status show success or fail title
		IMenuItem mainTitle = null;
		if (levelStatus.equals("dead")) {
			mainTitle = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameRedFont, "AREA INCOMPLETE", MENU_STATUS, false);
		} else {
			mainTitle = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameGreenFont, "AREA SUCCESS!", MENU_STATUS, false);
		}
		menu.addMenuItem(mainTitle);

		// create statistics menu items
		final IMenuItem timeToComplete = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFontGray, "Time to finish", MENU_REBUILD, false);
		menu.addMenuItem(timeToComplete);

		// show how long the level was played for in seconds
		long levelPlayedSecondsCount = GameStatus.getCurrentLevelPlayedTime();
		final IMenuItem timeToCompleteTime = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFontGray, Long.toString(levelPlayedSecondsCount) + " seconds", MENU_REBUILD, false);
		menu.addMenuItem(timeToCompleteTime);

		final IMenuItem marksmanship = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFontGray, "Marksmanship", MENU_REBUILD, false);
		menu.addMenuItem(marksmanship);

		// calculate marksmanship
		int levelHits = GameStatus.getGameLevelStats(GameStatus.levelStatsType.HITS);
		int levelShots = GameStatus.getGameLevelStats(GameStatus.levelStatsType.SHOTS);
		float marksmanshipRating = 0;
		if (levelShots > 0) {
			marksmanshipRating = (levelHits * 100 / levelShots);
		}

		final IMenuItem marksmanshipPercent = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFontGray, Float.toString(marksmanshipRating) + " % (" + Integer.toString(GameStatus.getGameLevelStats(GameStatus.levelStatsType.HITS)) + " Hits)", MENU_REBUILD, false);
		menu.addMenuItem(marksmanshipPercent);

		final IMenuItem shotsFired = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFontGray, "Shots Fired", MENU_REBUILD, false);
		menu.addMenuItem(shotsFired);

		final IMenuItem shotsFiredCount = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFontGray, Integer.toString(GameStatus.getGameLevelStats(GameStatus.levelStatsType.SHOTS)), MENU_REBUILD, false);
		menu.addMenuItem(shotsFiredCount);

		final IMenuItem kills = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFontGray, "Aliens Killed", MENU_REBUILD, false);
		menu.addMenuItem(kills);

		final IMenuItem killsCount = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFontGray, Integer.toString(GameStatus.getGameLevelStats(GameStatus.levelStatsType.KILLS)), MENU_REBUILD, false);
		menu.addMenuItem(killsCount);

		// create control game menu items
		final IMenuItem rebuildYourShip = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFontTiny, "REBUILD YOUR SHIP", MENU_REBUILD, false);
		menu.addMenuItem(rebuildYourShip);

		final IMenuItem menuButtonItem = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameBlueFont, "MENU", MENU_BACK, true);
		menu.addMenuItem(menuButtonItem);

		final IMenuItem nextAreaItem = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameGreenFont, "NEXT", MENU_NEXT, true);
		menu.addMenuItem(nextAreaItem);

		final IMenuItem againAreaItem = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameRedFont, "AGAIN", MENU_AGAIN, true);
		menu.addMenuItem(againAreaItem);

		// build menu with animations
		menu.buildAnimations();
		menu.setBackgroundEnabled(false);
		mainTitle.setPosition(10, 20);

		// control game menu positions
		rebuildYourShip.setPosition(550, 50);
		menuButtonItem.setPosition(650, 100);
		againAreaItem.setPosition(650, 210);
		nextAreaItem.setPosition(650, 320);

		// level stats menu positions
		marksmanship.setPosition(100, 60);
		marksmanshipPercent.setPosition(200, 100);
		shotsFired.setPosition(100, 160);
		shotsFiredCount.setPosition(250, 200);
		kills.setPosition(100, 260);
		killsCount.setPosition(250, 300);
		timeToComplete.setPosition(100, 360);
		timeToCompleteTime.setPosition(250, 400);
		
		// only show the time to complete if they've finished the level 
		if (levelStatus.equals("dead")) {
			timeToComplete.setVisible(false);
			timeToCompleteTime.setVisible(false);
		}
		
		// only show the next level button if the level after the one most recently played is available to play
		if (!SceneManager.getInstance().hasNextLevelAvailable()) {
			nextAreaItem.setVisible(false);
		}

		// for the level just completed calculate a new status as far as number of stars based on marksmanship and time to complete
		if (!levelStatus.equals("dead")) {
			
			// achieve a 3 star rating
			if (marksmanshipRating > 90 && levelPlayedSecondsCount < 45) {
				GameStatus.setLevelStatusByLevelNumber(GameStatus.getMostRecentLevel(), "4");
			}
			
			// achive a 2 star rating
			if (marksmanshipRating > 95 && levelPlayedSecondsCount < 60) {
				GameStatus.setLevelStatusByLevelNumber(GameStatus.getMostRecentLevel(), "3");
			}
		}
		
		menu.setOnMenuItemClickListener(this);
		setChildScene(menu);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene scene, IMenuItem item, float localX, float localY) {
		switch (item.getID()) {

		case MENU_BACK:
			SceneManager.getInstance().returnToMenuScene();
			return true;

		case MENU_AGAIN:
			SceneManager.getInstance().playLevelAgain();
			return true;

		case MENU_NEXT:
			if (SceneManager.getInstance().hasNextLevelAvailable()) {
				SceneManager.getInstance().playNextLevel();
			}
			return true;
		}
		return false;
	}
}