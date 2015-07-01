package com.kevinhinds.taskblaster.scene;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.util.color.Color;

import android.hardware.SensorManager;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.kevinhinds.taskblaster.GameConfiguation;
import com.kevinhinds.taskblaster.MaxStepPhysicsWorld;
import com.kevinhinds.taskblaster.ResourceManager;
import com.kevinhinds.taskblaster.level.AdversaryManager;
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
	private AdversaryManager adversaryManager;
	public Body fixtureBody;

	@Override
	public void createScene() {
		levelManager = new LevelManager(activity.getAssets());
		adversaryManager = new AdversaryManager(activity.getAssets());
		setBackground();
		createHUD();
		createPhysics();
		addPlayer();
		createControls();
		createLevel(1);
		
		// concept of a current level in progress that the collision manager below will use to look up villians to apply damage, etc
		
		
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
		physicsWorld.setContactListener(contactListener());
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
		final Rectangle roof = new Rectangle(0, GameConfiguation.offscreenJumpHeight, camera.getWidth(), 2, vbom);
		roof.setZIndex(-2);
		roof.setColor(Color.BLACK);

		final Rectangle ground = new Rectangle(0, camera.getHeight() - GameConfiguation.buttonControlsHeight, camera.getWidth(), 2, vbom);
		ground.setZIndex(-2);
		ground.setColor(Color.BLACK);

		final Rectangle left = new Rectangle(0, GameConfiguation.offscreenJumpHeight, 2, camera.getHeight() * 2, vbom);
		left.setZIndex(-2);
		left.setColor(Color.BLACK);

		final Rectangle right = new Rectangle(camera.getWidth() - 2, 0, 2, camera.getHeight() * 2, vbom);
		right.setZIndex(-2);
		right.setColor(Color.BLACK);

		fixtureBody = PhysicsFactory.createBoxBody(physicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		fixtureBody.setUserData("ground");
		attachChild(ground);

		fixtureBody = PhysicsFactory.createBoxBody(physicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		fixtureBody.setUserData("roof");
		attachChild(roof);

		fixtureBody = PhysicsFactory.createBoxBody(physicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		fixtureBody.setUserData("leftWall");
		attachChild(left);

		fixtureBody = PhysicsFactory.createBoxBody(physicsWorld, right, BodyType.StaticBody, wallFixtureDef);
		fixtureBody.setUserData("rightWall");
		attachChild(right);

		levelManager.loadLevel(levelNumber, this, physicsWorld);
		adversaryManager.loadLevel(levelNumber, this, physicsWorld);
	}

	/**
	 * contact listener for the game scene
	 * 
	 * @return
	 */
	private ContactListener contactListener() {
		ContactListener contactListener = new ContactListener() {

			/**
			 * track begin of contact between fixtures
			 */
			public void beginContact(Contact contact) {
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();
				Log.i("beginContact", x1.getBody().getUserData() + " - " + x2.getBody().getUserData());

				if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null) {

					String x1BodyName = (String) x1.getBody().getUserData();
					String x2BodyName = (String) x2.getBody().getUserData();

					/** if player is contact with something (not his own bullet) then he stops jumping */
					if (x1BodyName.equals("player") && (x2BodyName.equals("tile") || x2BodyName.equals("ground"))) {
						player.stopJumping();	
					}
					
					/** if the bullet comes in contact with something that's not the player himself, then the sprite is detached from the scene */
					if (x2BodyName.equals("bullet") && !x1BodyName.equals("player")) {
						player.shootContact();
					}
					
					if (x1BodyName.contains("Character") && x2BodyName.equals("bullet")) {
						// adversaryManager;
						// level.getCharacterByName
						// this is not getting the updated names so its NULL
						// Character shotVillain = ResourceManager.getIntance().adver.getVillainByName(x1BodyName);
						// shotVillain.takeDamage();
					}
				}
			}

			/**
			 * track end of contact between fixtures
			 */
			public void endContact(Contact contact) {
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();
				Log.i("endContact", x1.getBody().getUserData() + " - " + x2.getBody().getUserData());

				if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null) {
					if (x1.getBody().getUserData().equals("player")) {
						if (x2.getBody().getUserData().equals("ground")) {

						}
					}
				}
			}

			public void preSolve(Contact contact, Manifold oldManifold) {

			}

			public void postSolve(Contact contact, ContactImpulse impulse) {

			}
		};
		return contactListener;
	}
}