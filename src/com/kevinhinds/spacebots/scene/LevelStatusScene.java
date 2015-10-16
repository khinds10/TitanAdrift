package com.kevinhinds.spacebots.scene;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.Sprite;

import com.kevinhinds.spacebots.ResourceManager;

/**
 * credits scene for the game
 * 
 * @author khinds
 */
public class LevelStatusScene extends BaseScene implements IOnMenuItemClickListener {

	private MenuScene menu;
	private final int MENU_BACK = 1;

	@Override
	public void createScene() {
		final Sprite spriteBG = new Sprite(0, 0, ResourceManager.getIntance().voidBackgroundRegion, ResourceManager.getIntance().vbom);
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
		final IMenuItem mainTitle = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFont, "LEVEL INCOMPLETE", 0, false);
		menu.addMenuItem(mainTitle);

		final IMenuItem backButtonItem = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFontGray, "MAIN MENU", MENU_BACK, true);
		menu.addMenuItem(backButtonItem);

		menu.buildAnimations();
		menu.setBackgroundEnabled(false);

		/** position the menu items */
		mainTitle.setPosition(10, 20);
		backButtonItem.setPosition(backButtonItem.getX(), backButtonItem.getY() + 35);
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
		return false;
	}

	@Override
	public void setGameLevel(int levelNumber) {

	}

	@Override
	public void disposeScene() {

	}
}