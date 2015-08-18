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
	private Level level;
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

		/** every 2 seconds update scene timer */
		this.registerUpdateHandler(new TimerHandler(1, true, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				// @todo the scene will clean up stray bullets and other sprites marked as "deleted" (user data)
				player.isShooting = false;
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

					String x1BodyName = (String) x1.getBody().getUserData();
					String x2BodyName = (String) x2.getBody().getUserData();

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

					/** actor gets hit by bullet */
					if (x1BodyName.contains("Actor") && x2BodyName.equals("bullet")) {
						Actor actorShot = level.getActorByName(x1BodyName);
						// @todo this should be based on the player's "bullet" strength
						actorShot.takeDamage(2, GameScene.this, player);
					}

					/** actor touches platform or another actor */
					if ((x1BodyName.equals("bounce") || x1BodyName.equals("edge") || x1BodyName.equals("rightWall") || x1BodyName.equals("leftWall") || x1BodyName.contains("Actor") || x2BodyName.contains("Item")) && x2BodyName.contains("Actor")) {
						Actor actor = level.getActorByName(x2BodyName);
						actor.changeDirection();
						// @todo if the actor is touching another actor, apply a random force so they don't stack on top of each other
					}

					/** if the bullet comes in contact with something that's not the player himself or an actor taking damage, then the sprite is detached from the scene */
					if (x2BodyName.equals("bullet") && !x1BodyName.equals("player")) {
						player.shootContact();
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