package com.kevinhinds.spacebots.scene;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;

import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.color.Color;

import android.hardware.SensorManager;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.kevinhinds.spacebots.GameConfiguration;
import com.kevinhinds.spacebots.level.Level;
import com.kevinhinds.spacebots.level.LevelXMLBuilder;
import com.kevinhinds.spacebots.objects.Actor;
import com.kevinhinds.spacebots.objects.Bullet;
import com.kevinhinds.spacebots.objects.Item;
import com.kevinhinds.spacebots.player.Controls;
import com.kevinhinds.spacebots.player.Player;

/**
 * main game scene which contains multiple levels
 * 
 * @author khinds
 * 
 */
public class GameScene extends BaseScene {

	public HUD gameHUD;
	private Player player;
	public Body fixtureBody;
	public Level level;
	public int levelNumber;
	private LevelXMLBuilder levelXMLBuilder;

	/**
	 * construct the gamescene with the level XML builder
	 */
	public GameScene() {
		levelXMLBuilder = new LevelXMLBuilder(activity.getAssets());
	}

	/**
	 * set the level the game is currently playing
	 * 
	 * @param levelNumber
	 */
	public void setGameLevel(int levelNumber) {
		this.levelNumber = levelNumber;
	}

	@Override
	public void createScene() {

		setBackground();
		createHUD();
		createPhysics();
		addPlayer();
		createControls();
		createLevel(this.levelNumber);
		level = levelXMLBuilder.level;

		/** every 1 seconds update scene timer */
		this.registerUpdateHandler(new TimerHandler(1, true, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				for (Actor a : level.actors) {
					if (a.actorBody != null) {
						if (!a.actorBody.getUserData().equals("deceased")) {
							a.move();
						}
					}
				}
			}
		}));
	}

	@Override
	public void onBackPressed() {
		SceneManager.getInstance().returnToMenuScene();
	}

	@Override
	public void disposeScene() {
		gameHUD = null;
		camera.setHUD(gameHUD);
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
		physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		physicsWorld.setContactListener(contactListener());
		registerUpdateHandler(physicsWorld);
	}

	/**
	 * add new player to this scene
	 */
	private void addPlayer() {
		player = new Player(this, physicsWorld);
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
		final Rectangle roof = new Rectangle(0, GameConfiguration.offscreenJumpHeight, camera.getWidth(), 2, vbom);
		roof.setZIndex(-2);
		roof.setColor(Color.BLACK);

		final Rectangle ground = new Rectangle(0, camera.getHeight() - GameConfiguration.buttonControlsHeight, camera.getWidth(), 2, vbom);
		ground.setZIndex(-2);
		ground.setColor(Color.BLACK);

		final Rectangle left = new Rectangle(0, GameConfiguration.offscreenJumpHeight, 2, camera.getHeight() * 2, vbom);
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

		/** load all levels from XML */
		levelXMLBuilder.createLevelFromXML(levelNumber);
		levelXMLBuilder.loadLevel(GameScene.this, physicsWorld);
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

					/** get the contacted bodies from the physics engine to deal with below */
					String x1BodyName = (String) x1.getBody().getUserData();
					String x2BodyName = (String) x2.getBody().getUserData();

					/** clean up the bullets that hit object very first */
					if (x2BodyName.contains("Bullet") && !x1BodyName.equals("player")) {
						Bullet bullet = level.getBulletByName(x2BodyName);
						bullet.hitObject(GameScene.this);
					}
					if (x1BodyName.contains("Bullet") && !x2BodyName.equals("player")) {
						Bullet bullet = level.getBulletByName(x1BodyName);
						bullet.hitObject(GameScene.this);
					}

					/** actor gets hit by bullet */
					if (x1BodyName.contains("Actor") && x2BodyName.contains("Bullet")) {
						Actor actorShot = level.getActorByName(x1BodyName);
						actorShot.takeDamage(2, GameScene.this, player);
					}

					/** deal with the player contacting sprites */
					if (x1BodyName.equals("player")) {

						/** player begins to fall when loses contact with a bounce tile (edges of platforms) */
						if (x2BodyName.equals("tile") || x2BodyName.equals("ground")) {
							player.stopFalling();
							player.stopJumping();
						}

						/** player contacts an item */
						if (x2BodyName.contains("Item")) {
							Item itemCollected = level.getItemByName(x2BodyName);
							itemCollected.collect(GameScene.this);
						}
					}

					/** actor touches platform or another actor */
					if (x2BodyName.contains("Actor") && !(x1BodyName.equals("ground") || x1BodyName.equals("physical"))) {
						Actor actor = level.getActorByName(x2BodyName);
						actor.changeDirection();
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

				String x1BodyName = (String) x1.getBody().getUserData();
				String x2BodyName = (String) x2.getBody().getUserData();

				if (x1BodyName != null && x2BodyName != null) {
					
					
					/** player begins to fall when loses contact with a bounce tile (edges of platforms) */
					if (x1BodyName.equals("player")) {
						if (x2BodyName.equals("bounce")) {
							player.fall();
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