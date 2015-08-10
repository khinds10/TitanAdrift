package com.kevinhinds.spacebots.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;

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
	private final int MENU_PLAY = 0;
	private final int MENU_EXIT = 1;
	private final int MENU_LEVELS = 2;

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

		final IMenuItem titleItem = new SpriteMenuItem(MENU_PLAY, ResourceManager.getIntance().title_region, vbom);
		final IMenuItem playItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, ResourceManager.getIntance().play_button_region, vbom), 1.1f, 1);
		final IMenuItem exitItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_EXIT, ResourceManager.getIntance().exit_button_region, vbom), 1.1f, 1);
		final IMenuItem levelSelectItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_LEVELS, ResourceManager.getIntance().level_select_region, vbom), 1.1f, 1);

		/** add items to the menu */
		menu.addMenuItem(titleItem);
		menu.addMenuItem(playItem);
		menu.addMenuItem(levelSelectItem);
		menu.addMenuItem(exitItem);
		menu.buildAnimations();
		menu.setBackgroundEnabled(true);

		/** set menu item positions */
		titleItem.setPosition(titleItem.getX(), titleItem.getY() - 50);
		playItem.setPosition(playItem.getX(), playItem.getY());
		levelSelectItem.setPosition(levelSelectItem.getX(), levelSelectItem.getY() + 25);
		exitItem.setPosition(exitItem.getX(), exitItem.getY() + 65);

		menu.setOnMenuItemClickListener(this);
		setChildScene(menu);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene scene, IMenuItem item, float localX, float localY) {
		switch (item.getID()) {
		case MENU_PLAY:
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