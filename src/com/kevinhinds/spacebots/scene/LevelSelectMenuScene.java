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
 */
public class LevelSelectMenuScene extends BaseScene implements IOnMenuItemClickListener {

	private MenuScene menu;
	private final int MENU_BACK = 100;
	private final int MENU_NONE = 101;
	private ArrayList<IMenuItem> levelMenuItems = new ArrayList<IMenuItem>();
	private int levelMenuPadding = 60;
	private int levelRowPadding = 60;
	private int levelColumnPadding = 150;
	private int levelGridXY = 5;

	@Override
	public void createScene() {
		final Sprite spriteBG = new Sprite(0, 0, ResourceManager.getIntance().multicolorBackgroundRegion, ResourceManager.getIntance().vbom);
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

		// create menu items, one for each level
		for (int levelId = 1; levelId <= GameConfiguration.numberLevels; levelId++) {
			String levelName = Integer.toString(levelId);

			// zero pad low numbers
			if (levelId < 10) {
				levelName = "0" + Integer.toString(levelId);
			}

			// last level is END!
			if (levelId == 20) {
				levelName = "END";
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

		final IMenuItem backButton = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFontGray, "MAIN MENU", MENU_BACK, true);
		menu.addMenuItem(backButton);

		final IMenuItem levelSelectTitle = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFontGray, "SELECT AREA...", MENU_NONE, false);
		menu.addMenuItem(levelSelectTitle);

		final IMenuItem oneMetal = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().levelSelectFontOneInfo, "Metal", MENU_NONE, false);
		menu.addMenuItem(oneMetal);

		final IMenuItem twoMetals = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().levelSelectFontTwoInfo, "Metals", MENU_NONE, false);
		menu.addMenuItem(twoMetals);

		final IMenuItem threeMetals = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().levelSelectFontThreeInfo, "Metals", MENU_NONE, false);
		menu.addMenuItem(threeMetals);

		menu.buildAnimations();
		menu.setBackgroundEnabled(false);

		// show the level metals by level color codes
		Sprite playerMetal1 = new Sprite(this.camera.getWidth() - 580, this.camera.getHeight() - 155, ResourceManager.getIntance().playerLevelMetalSmallRegion, this.vbom);
		Sprite playerMetal2 = new Sprite(this.camera.getWidth() - 410, this.camera.getHeight() - 155, ResourceManager.getIntance().playerLevelMetalSmallRegion, this.vbom);
		Sprite playerMetal3 = new Sprite(this.camera.getWidth() - 390, this.camera.getHeight() - 155, ResourceManager.getIntance().playerLevelMetalSmallRegion, this.vbom);
		Sprite playerMetal4 = new Sprite(this.camera.getWidth() - 200, this.camera.getHeight() - 155, ResourceManager.getIntance().playerLevelMetalSmallRegion, this.vbom);
		Sprite playerMetal5 = new Sprite(this.camera.getWidth() - 220, this.camera.getHeight() - 155, ResourceManager.getIntance().playerLevelMetalSmallRegion, this.vbom);
		Sprite playerMetal6 = new Sprite(this.camera.getWidth() - 240, this.camera.getHeight() - 155, ResourceManager.getIntance().playerLevelMetalSmallRegion, this.vbom);
		this.attachChild(playerMetal1);
		this.attachChild(playerMetal2);
		this.attachChild(playerMetal3);
		this.attachChild(playerMetal4);
		this.attachChild(playerMetal5);
		this.attachChild(playerMetal6);

		// position buttons
		for (int levelId = 0; levelId < GameConfiguration.numberLevels; levelId++) {
			positionLevelMenuItem(levelId);
		}
		backButton.setPosition(ResourceManager.getIntance().camera.getCenterX() - 75, ResourceManager.getIntance().camera.getHeight() - 60);
		levelSelectTitle.setPosition(10, 10);

		// position metal color code guide
		oneMetal.setPosition(ResourceManager.getIntance().camera.getCenterX() - 150, ResourceManager.getIntance().camera.getHeight() - 150);
		twoMetals.setPosition(ResourceManager.getIntance().camera.getCenterX() + 35, ResourceManager.getIntance().camera.getHeight() - 150);
		threeMetals.setPosition(ResourceManager.getIntance().camera.getCenterX() + 225, ResourceManager.getIntance().camera.getHeight() - 150);

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
		if (item.getID() < 100) {
			// head over the scene selected if the status for the level is non-zero (can't play yet)
			if (GameStatus.levelStatusByLevelNumber(item.getID()) > 0) {
				SceneManager.getInstance().setGameScene(item.getID());
			}
		}
		return false;
	}
}