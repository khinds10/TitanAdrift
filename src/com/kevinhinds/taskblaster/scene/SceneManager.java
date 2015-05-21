package com.kevinhinds.taskblaster.scene;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import com.kevinhinds.taskblaster.ResourceManager;

/**
 * set scenes currently being displayed in the game via singleton
 * 
 * @author khinds
 */
public class SceneManager {

	private BaseScene menuScene;
	private BaseScene gameScene;

	private static final SceneManager INSTANCE = new SceneManager();

	private BaseScene currentScene;
	private Engine engine = ResourceManager.getIntance().engine;

	public enum SceneType {
		SCENE_MENU, SCENE_GAME
	}

	/**
	 * set the scene to menu
	 * 
	 * @param cb
	 */
	public void setMenuScene(OnCreateSceneCallback cb) {
		ResourceManager.getIntance().loadMenuResources();
		menuScene = new MainMenuScene();
		setScene(menuScene);
		currentScene.createScene();
		cb.onCreateSceneFinished(menuScene);
	}

	/**
	 * set the scene to the game
	 */
	public void setGameScene() {
		ResourceManager.getIntance().loadGameResources();
		ResourceManager.getIntance().loadTileManager();
		gameScene = new GameScene();
		setScene(gameScene);
		currentScene.createScene();
	}

	/**
	 * dispose current scene and apply the new one specified
	 * 
	 * @param scene
	 */
	public void setScene(BaseScene scene) {
		if (currentScene != null) {
			currentScene.disposeScene();
		}
		engine.setScene(scene);
		currentScene = scene;
	}

	/**
	 * dispose the current scene and apply the new one via enumerated SceneType
	 * 
	 * @param type
	 */
	public void setScene(SceneType type) {
		switch (type) {
		case SCENE_MENU:
			setScene(menuScene);
			break;

		case SCENE_GAME:
			setScene(gameScene);
			break;
		}
	}

	/**
	 * get scene manager via singleton
	 * 
	 * @return
	 */
	public static SceneManager getInstance() {
		return INSTANCE;
	}

	/**
	 * get the current scene the game is playing
	 * 
	 * @return
	 */
	public BaseScene getCurrentScene() {
		return currentScene;
	}
}
