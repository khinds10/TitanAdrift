package com.kevinhinds.spacebots.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;

import org.andengine.util.color.Color;
import com.kevinhinds.spacebots.ResourceManager;

/**
 * main menu scene for the game
 * 
 * @author khinds
 * 
 */
public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener {

	private MenuScene menu;

	private final int MENU_TITLE = 0;
	private final int MENU_PLAY = 1;
	private final int MENU_EXIT = 2;
	private final int MENU_LEVELS = 3;
	private final int MENU_CREDITS = 4;

	@Override
	public void createScene() {
		setBackground(new Background(Color.BLACK));
		createMenu();
	}

	@Override
	public void onBackPressed() {
		System.exit(0);
	}

	/**
	 * create menu elements from local sprites to display
	 */
	private void createMenu() {
		menu = new MenuScene(camera);
		menu.setPosition(0, 0);

		final IMenuItem mainTitle = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().titleFont, "SPACEBOTS", MENU_TITLE, false);
		menu.addMenuItem(mainTitle);

		final IMenuItem playItem = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().menuRedFont, "PLAY", MENU_PLAY, true);
		menu.addMenuItem(playItem);

		final IMenuItem levelSelectItem = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().menuGreenFont, "LEVEL SELECT", MENU_LEVELS, true);
		menu.addMenuItem(levelSelectItem);

		final IMenuItem exitItem = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().menuBlueFont, "EXIT", MENU_EXIT, true);
		menu.addMenuItem(exitItem);

		final IMenuItem creditsItem = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFontGray, "CREDITS", MENU_CREDITS, true);
		menu.addMenuItem(creditsItem);

		menu.buildAnimations();
		menu.setBackgroundEnabled(true);
		
		mainTitle.setPosition(mainTitle.getX(), mainTitle.getY() - 60);
		levelSelectItem.setPosition(levelSelectItem.getX(), levelSelectItem.getY() + 15);
		exitItem.setPosition(exitItem.getX(), exitItem.getY() + 35);
		creditsItem.setPosition(creditsItem.getX() + 250, creditsItem.getY() + 70);

		menu.setOnMenuItemClickListener(this);
		setChildScene(menu);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene scene, IMenuItem item, float localX, float localY) {
		switch (item.getID()) {
		case MENU_PLAY:

			/** @todo, this value should be from the last level you've not completed yet */
			SceneManager.getInstance().setGameScene(1);
			return true;
		case MENU_LEVELS:
			SceneManager.getInstance().loadLevelSelectScene();
			return true;
		case MENU_EXIT:
			System.exit(0);
			return true;
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