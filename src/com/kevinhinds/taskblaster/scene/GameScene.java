package com.kevinhinds.taskblaster.scene;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.util.color.Color;
import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.kevinhinds.taskblaster.MaxStepPhysicsWorld;
import com.kevinhinds.taskblaster.ResourceManager;
import com.kevinhinds.taskblaster.level.LevelManager;
import com.kevinhinds.taskblaster.player.Controls;
import com.kevinhinds.taskblaster.player.Player;

/**
 * main game scene which contains multiple levels
 * 
 * @author khinds
 * 
 */
public class GameScene extends BaseScene {

	public HUD gameHUD;
	private Player player;
	private LevelManager levelManager;

	@Override
	public void createScene() {
		levelManager = new LevelManager(activity.getAssets());
		setBackground();
		createHUD();
		createPhysics();
		addPlayer();
		createControls();
		createLevel(1);
	}

	@Override
	public void onBackPressed() {
		SceneManager.getInstance().setScene(SceneManager.SceneType.SCENE_MENU);
	}

	@Override
	public void disposeScene() {
		ResourceManager.getIntance().unloadGameResources();
	}

	/**
	 * set the background for the gamescene
	 */
	private void setBackground() {
		setBackground(new Background(Color.BLACK));
	}

	/**
	 * create the heads up display (game controls) that move with the player through the level
	 */
	private void createHUD() {
		gameHUD = new HUD();
		camera.setHUD(gameHUD);
	}

	/**
	 * apply physics to the gamescene we'll use the new improved MaxStepPhysicsWorld which extends the regular PhysicsWorld
	 */
	private void createPhysics() {
		physicsWorld = new MaxStepPhysicsWorld(60, new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		registerUpdateHandler(physicsWorld);
	}

	/**
	 * add new player to this scene
	 */
	private void addPlayer() {
		player = new Player(this);
	}

	/**
	 * create controls for the game for the player's character
	 */
	private void createControls() {
		new Controls(this, player);
	}

	/**
	 * create level for this game scene
	 * 
	 * @param levelNumber
	 */
	private void createLevel(int levelNumber) {

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.0f, 0.0f);
		final Rectangle ground = new Rectangle(0, camera.getHeight() - 2, camera.getWidth(), 2, vbom);
		final Rectangle roof = new Rectangle(0, 0, camera.getWidth(), 2, vbom);
		final Rectangle left = new Rectangle(0, 0, 2, camera.getHeight(), vbom);
		final Rectangle right = new Rectangle(camera.getWidth() - 2, 0, 2, camera.getHeight(), vbom);

		PhysicsFactory.createBoxBody(physicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(physicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(physicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(physicsWorld, right, BodyType.StaticBody, wallFixtureDef);

		attachChild(ground);
		attachChild(roof);
		attachChild(left);
		attachChild(right);

		levelManager.loadLevel(levelNumber, this, physicsWorld);
	}
}
