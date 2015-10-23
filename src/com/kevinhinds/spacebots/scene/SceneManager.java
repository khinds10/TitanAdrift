package com.kevinhinds.spacebots.scene;

import org.andengine.audio.music.Music;
import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;
import com.kevinhinds.spacebots.ResourceManager;

/**
 * set scenes currently being displayed in the game via singleton
 * 
 * @author khinds
 */
public class SceneManager {

	private BaseScene menuScene;
	private BaseScene gameScene;
	private BaseScene levelScene;
	private static final SceneManager INSTANCE = new SceneManager();
	private BaseScene currentScene;
	private Engine engine = ResourceManager.getIntance().engine;

	/**
	 * set the scene to menu
	 * 
	 * @param cb
	 */
	public void startGame(OnCreateSceneCallback cb) {

		/** load all the resources for the game to start */
		ResourceManager.getIntance().loadMenuResources();
		ResourceManager.getIntance().loadGameResources();
		ResourceManager.getIntance().loadMusic();
		ResourceManager.getIntance().loadSoundEffects();
		ResourceManager.getIntance().loadFonts();
		returnToMenuScene();
		cb.onCreateSceneFinished(menuScene);
	}

	/**
	 * return to menu scene
	 */
	public void returnToMenuScene() {
		menuScene = new MainMenuScene();
		setScene(menuScene, ResourceManager.getIntance().titleMusic);
		currentScene.createScene();
	}

	/**
	 * return to menu scene
	 */
	public void loadCreditsScene() {
		levelScene = new CreditsMenuScene();
		setScene(levelScene, ResourceManager.getIntance().creditsMusic);
		currentScene.createScene();
	}

	/**
	 * return to menu scene
	 */
	public void loadLevelSelectScene() {
		levelScene = new LevelSelectMenuScene();
		setScene(levelScene, null);
		currentScene.createScene();
	}

	/**
	 * set the scene to the game
	 */
	public void setGameScene(int levelNumber) {
		gameScene = new GameScene();
		gameScene.setGameLevel(levelNumber);
		setScene(gameScene, null);
		currentScene.createScene();
	}

	/**
	 * return to menu scene
	 */
	public void loadLevelStatusScene() {
		levelScene = new LevelStatusScene();
		setScene(levelScene, ResourceManager.getIntance().deadMusic);
		currentScene.createScene();
	}

	/**
	 * dispose current scene and apply the new one specified
	 * 
	 * @param scene
	 */
	public void setScene(BaseScene scene, Music music) {

		/** stop any music and play new scene music if it's present */
		ResourceManager.getIntance().stopAllMusic();
		if (music != null) {
			music.seekTo(0);
			music.setVolume(0.2f);
			music.resume();
		}

		/** dispose and load new scene */
		if (currentScene != null) {
			currentScene.disposeScene();
		}
		engine.setScene(scene);
		currentScene = scene;
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