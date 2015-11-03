package com.kevinhinds.spacebots.scene;

import java.util.ArrayList;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.Sprite;

import com.kevinhinds.spacebots.GameConfiguration;
import com.kevinhinds.spacebots.ResourceManager;
import com.kevinhinds.spacebots.objects.Piece;
import com.kevinhinds.spacebots.status.GameStatus;
import com.kevinhinds.spacebots.status.StatusListManager;

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
			Piece thisPiece = shipPieces.get(i);
			Integer pieceID = thisPiece.getId();
			/** if the current list of pieces the player aquired contains the piece then show that we have it on the screen, else if it's a piece they don't have to collect then just show it */
			if (StatusListManager.containsValue(shipStatus, pieceID.toString()) || !StatusListManager.containsValue(GameConfiguration.shipPiecesToCollect, pieceID.toString())) {
				thisPiece.setPosition(x + (count * GameConfiguration.pieceMapTileSize), y);
				thisPiece.attach(this);
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

		/** create menu items */
		final IMenuItem mainTitle = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFont, "LEVEL INCOMPLETE", 0, false);
		menu.addMenuItem(mainTitle);

		/** create menu items */
		final IMenuItem rebuildYourShip = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFontTiny, "REBUILD YOUR SHIP", 0, false);
		menu.addMenuItem(rebuildYourShip);

		final IMenuItem backButtonItem = ResourceManager.getIntance().createTextMenuItem(ResourceManager.getIntance().gameFontGray, "MAIN MENU", MENU_BACK, true);
		menu.addMenuItem(backButtonItem);

		menu.buildAnimations();
		menu.setBackgroundEnabled(false);

		/** position the menu items */
		mainTitle.setPosition(10, 20);
		backButtonItem.setPosition(backButtonItem.getX(), backButtonItem.getY() + 160);
		rebuildYourShip.setPosition(600, 100);
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
}