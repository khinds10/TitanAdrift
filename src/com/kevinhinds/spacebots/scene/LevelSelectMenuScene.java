package com.kevinhinds.spacebots.scene;

import java.util.ArrayList;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.font.Font;

import com.kevinhinds.spacebots.GameConfiguration;
import com.kevinhinds.spacebots.ResourceManager;
import com.kevinhinds.spacebots.status.GameStatus;

/**
 * main menu scene for the game
 * 
 * @author khinds
 * 
 */
public class LevelSelectMenuScene extends BaseScene implements IOnMenuItemClickListener {

	private MenuScene menu;
	private final int MENU_BACK = 0;
	private ArrayList<IMenuItem> levelMenuItems = new ArrayList<IMenuItem>();
	private int levelMenuPadding = 60;
	private int levelRowPadding = 60;
	private int levelColumnPadding = 150;
	private int levelGridXY = 5;

	@Override
	public void createScene() {
		final Sprite spriteBG = new Sprite(0, 0, ResourceManager.getIntance().void_background_region, ResourceManager.getIntance().vbom);
		attachChild(spriteBG);
		createMenu();
	}

	@Override
	public void onBackPressed() {
		SceneManager.getInstance().returnToMenuScene();
	}

	/**
	 * create menu elements from local sprites to display
	 */
	private void createMenu() {
		menu = new MenuScene(camera);
		menu.setPosition(0, 0);

		/** create menu items */
		for (int levelId = 1; levelId <= GameConfiguration.numberLevels; levelId++) {
			String levelName = Integer.toString(levelId);
			if (levelId < 10) {
				levelName = "0" + Integer.toString(levelId);
			}

			/** switch the level select color coding based on level completion status */
			Font selectedFont = null;
			int levelStatus = GameStatus.levelStatusByLevelNumber(levelId);
			if (levelStatus == 0) {
				selectedFont = ResourceManager.getIntance().levelSelectFontNone; 
			}
			if (levelStatus == 1) {
				selectedFont = ResourceManager.getIntance().levelSelectFontPlay;
			}
			if (levelStatus == 2) {
				selectedFont = ResourceManager.getIntance().levelSelectFontOne;
			}
			if (levelStatus == 3) {
				selectedFont = ResourceManager.getIntance().levelSelectFontTwo;
			}
			if (levelStatus == 4) {
				selectedFont = ResourceManager.getIntance().levelSelectFontThree;
			}
			levelMenuItems.add(ResourceManager.getIntance().createTextMenuItem(selectedFont, levelName, levelId, true));
		}
		for (int levelId = 0; levelId < GameConfiguration.numberLevels; levelId++) {
			menu.addMenuItem(levelMenuItems.get(levelId));
		}
		final IMenuItem backButton = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().menuGrayFont, "BACK", MENU_BACK, true);
		menu.addMenuItem(backButton);

		final IMenuItem levelSelectTitle = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFontGray, "SELECT LEVEL...", MENU_BACK, false);
		menu.addMenuItem(levelSelectTitle);

		menu.buildAnimations();
		menu.setBackgroundEnabled(false);

		/** position buttons */
		backButton.setPosition(backButton.getX(), backButton.getY() + 135);
		for (int levelId = 0; levelId < GameConfiguration.numberLevels; levelId++) {
			positionLevelMenuItem(levelId);
		}
		backButton.setPosition(ResourceManager.getIntance().camera.getCenterX() - 75, ResourceManager.getIntance().camera.getHeight() - 60);
		levelSelectTitle.setPosition(10, 10);

		menu.setOnMenuItemClickListener(this);
		setChildScene(menu);
	}

	/**
	 * position level number in a square grid
	 * 
	 * @param levelId
	 */
	private void positionLevelMenuItem(int levelId) {
		int row = levelId / levelGridXY;
		int column = levelId - row * levelGridXY;
		levelMenuItems.get(levelId).setPosition((column * levelColumnPadding) + levelMenuPadding, (row * levelRowPadding) + levelMenuPadding);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene scene, IMenuItem item, float localX, float localY) {
		switch (item.getID()) {
		case MENU_BACK:
			SceneManager.getInstance().returnToMenuScene();
			return true;
		}

		/** head over the scene selected if the status for the level is non-zero (can't play yet) */
		if (GameStatus.levelStatusByLevelNumber(item.getID()) > 0) {
			SceneManager.getInstance().setGameScene(item.getID());	
		}
		return false;
	}

	@Override
	public void setGameLevel(int levelNumber) {

	}

	@Override
	public void disposeScene() {

	}
}