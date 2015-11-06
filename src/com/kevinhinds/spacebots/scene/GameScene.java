package com.kevinhinds.spacebots.scene;

import java.util.Random;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
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
import com.kevinhinds.spacebots.ResourceManager;
import com.kevinhinds.spacebots.level.Collision;
import com.kevinhinds.spacebots.level.Level;
import com.kevinhinds.spacebots.level.LevelXMLBuilder;
import com.kevinhinds.spacebots.objects.Actor;
import com.kevinhinds.spacebots.objects.Explosion;
import com.kevinhinds.spacebots.player.Controls;
import com.kevinhinds.spacebots.player.Player;
import com.kevinhinds.spacebots.status.GameStatus;
import com.kevinhinds.spacebots.status.StatusListManager;

/**
 * main game scene which contains multiple levels
 * 
 * @author khinds
 * 
 */
public class GameScene extends BaseScene {

	public Player player;
	public Body fixtureBody;
	public Level level;
	public LevelXMLBuilder levelXMLBuilder;
	public Controls controls;

	/**
	 * construct the gamescene with the level XML builder
	 */
	public GameScene() {
		levelXMLBuilder = new LevelXMLBuilder(activity.getAssets());
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
		controls = new Controls(this, player);
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

					/**
					 * PROCESS GAME SCENE COLLISION
					 */
					new Collision((String) x1.getBody().getUserData(), (String) x2.getBody().getUserData(), level, GameScene.this);
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
					if (x1BodyName.contains("player")) {
						if (x2BodyName.contains("bounce")) {
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

	/**
	 * from the current list of pieces needed to complete a level, if the player has all the pieces then the level is complete!
	 */
	public void checkLevelComplete() {
		int thisLevelID = SceneManager.getInstance().getCurrentScene().levelNumber;
		String[] pieceslist = GameConfiguration.levelPieces[thisLevelID].split(",");
		String collectedPieces = GameStatus.getShipRepairedStatus();
		boolean levelCompleted = true;
		for (String piece : pieceslist) {
			if (!StatusListManager.containsValue(collectedPieces, piece)) {
				levelCompleted = false;
			}
		}

		/** level is complete, set next level to status = 1 (playable) and the current level status as 2, 3 or 4 based on player performance */
		if (levelCompleted) {
			/**
			 * @TODO this should set 2, 3 or 4 based on overall level player performance
			 */
			GameStatus.setLevelStatusByLevelNumber(thisLevelID, "2");
			GameStatus.setLevelStatusByLevelNumber(++thisLevelID, "1");
			SceneManager.getInstance().loadLevelStatusScene();
		}
	}

	/**
	 * random explosion sound from currently available sounds
	 */
	public void randomExplosionSound() {
		Random random = new Random();
		int explosion = random.nextInt(3) + 1;
		if (explosion == 1) {
			ResourceManager.getIntance().explosion1.play();
		}
		if (explosion == 2) {
			ResourceManager.getIntance().explosion2.play();
		}
		if (explosion == 3) {
			ResourceManager.getIntance().explosion3.play();
		}
	}

	/**
	 * for the exploding sprite in question create a random explosion for it (mushroom cloud shape)
	 * 
	 * @param explodingSprite
	 */
	public void randomGameMushroomExplosion(Sprite explodingSprite) {
		Random random = new Random();
		int explosion = random.nextInt(4);
		new Explosion(GameScene.this, explodingSprite, explosion);
	}

	/**
	 * for the exploding sprite in question create a random explosion for it (round cloud shape)
	 * 
	 * @param explodingSprite
	 */
	public void randomGameRoundExplosion(Sprite explodingSprite) {
		Random random = new Random();
		int explosion = random.nextInt(11) + 5;
		new Explosion(GameScene.this, explodingSprite, explosion);
	}
}