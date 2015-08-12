package com.kevinhinds.spacebots.scene;

import java.util.ArrayList;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.util.color.Color;

import com.kevinhinds.spacebots.GameConfiguation;
import com.kevinhinds.spacebots.ResourceManager;

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
		setBackground(new Background(Color.BLACK));
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
		for (int levelId = 1; levelId <= GameConfiguation.numberLevels; levelId++) {
			levelMenuItems.add(ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFont, Integer.toString(levelId), levelId, true));
		}
		for (int levelId = 0; levelId < GameConfiguation.numberLevels; levelId++) {
			menu.addMenuItem(levelMenuItems.get(levelId));
		}
		final IMenuItem backButton = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().menuBlueFont, "BACK", MENU_BACK, true);
		menu.addMenuItem(backButton);

		final IMenuItem levelSelectTitle = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFontGray, "SELECT LEVEL...", MENU_BACK, false);
		menu.addMenuItem(levelSelectTitle);

		menu.buildAnimations();
		menu.setBackgroundEnabled(true);

		/** position buttons */
		backButton.setPosition(backButton.getX(), backButton.getY() + 135);
		for (int levelId = 0; levelId < GameConfiguation.numberLevels; levelId++) {
			positionLevelMenuItem(levelId);
		}
		backButton.setPosition(ResourceManager.getIntance().camera.getCenterX() - 75, ResourceManager.getIntance().camera.getHeight() - 75);
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

		/** head over the scene selected! */
		SceneManager.getInstance().setGameScene(item.getID());
		return false;
	}

	@Override
	public void setGameLevel(int levelNumber) {

	}

	@Override
	public void disposeScene() {

	}
}