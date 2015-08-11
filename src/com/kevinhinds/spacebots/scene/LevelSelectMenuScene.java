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
public class LevelSelectMenuScene extends BaseScene implements IOnMenuItemClickListener {

	private MenuScene menu;
	private final int MENU_BACK = 0;

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

		/** add items to the menu */
		menu.addMenuItem(ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFont, "LEVEL 1", 1, true));
		menu.addMenuItem(ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFont, "LEVEL 2", 2, true));
		menu.addMenuItem(ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFont, "LEVEL 3", 3, true));
		menu.addMenuItem(ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().menuBlueFont, "BACK", MENU_BACK, true));
		menu.buildAnimations();
		menu.setBackgroundEnabled(true);
		menu.setOnMenuItemClickListener(this);
		setChildScene(menu);
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