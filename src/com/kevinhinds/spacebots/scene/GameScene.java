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
import com.kevinhinds.spacebots.level.Level;
import com.kevinhinds.spacebots.level.LevelXMLBuilder;
import com.kevinhinds.spacebots.objects.Actor;
import com.kevinhinds.spacebots.objects.Bullet;
import com.kevinhinds.spacebots.objects.Explosion;
import com.kevinhinds.spacebots.objects.Flare;
import com.kevinhinds.spacebots.objects.Item;
import com.kevinhinds.spacebots.objects.Piece;
import com.kevinhinds.spacebots.objects.Tile;
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

	private Player player;
	public Body fixtureBody;
	public Level level;
	private LevelXMLBuilder levelXMLBuilder;
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

					/** get the contacted bodies from the physics engine to deal with below */
					String x1BodyName = (String) x1.getBody().getUserData();
					String x2BodyName = (String) x2.getBody().getUserData();

					/** actor gets hit by bullet */
					if (x1BodyName.contains("Actor") && x2BodyName.contains("Bullet")) {
						Actor actorShot = level.getActorByName(x1BodyName);
						actorShot.takeDamage(2, GameScene.this, player);
					}

					/** actor gets hit by bomb */
					if (x1BodyName.contains("Actor") && x2BodyName.contains("bomb")) {
						Actor actorShot = level.getActorByName(x1BodyName);
						actorShot.takeDamage(20, GameScene.this, player);
					}
					
					/** actor gets hit by flare */
					if (x1BodyName.contains("Actor") && x2BodyName.contains("Flare")) {
						Actor actorShot = level.getActorByName(x1BodyName);
						actorShot.takeDamage(20, GameScene.this, player);
					}

					/** clean up the bullets that hit objects */
					if (x2BodyName.contains("Bullet") && !x1BodyName.equals("player")) {
						Bullet bullet = level.getBulletByName(x2BodyName);
						bullet.hitObject(GameScene.this);
					}
					if (x1BodyName.contains("Bullet") && !x2BodyName.equals("player")) {
						Bullet bullet = level.getBulletByName(x1BodyName);
						bullet.hitObject(GameScene.this);
					}
					
					/** clean up the flares that hit objects */
					if (x2BodyName.contains("Flare") && !x1BodyName.equals("player")) {
						Flare flare = level.getFlareByName(x2BodyName);
						flare.hitObject(GameScene.this);
					}
					if (x1BodyName.contains("Flare") && !x2BodyName.equals("player")) {
						Flare flare = level.getFlareByName(x1BodyName);
						flare.hitObject(GameScene.this);
					}

					/** player interacts with an actor on the level */
					if ((x2BodyName.contains("Actor") && x1BodyName.equals("player")) || (x1BodyName.contains("Actor") && x2BodyName.equals("player"))) {
						Log.e("Player Actor Contact", x1BodyName + " : " + x2BodyName);
						player.takeDamage(1);
					}

					/** deal with the player contacting sprites */
					if (x1BodyName.equals("player")) {

						/** player begins to fall when loses contact with a bounce tile (edges of platforms) */
						if (x2BodyName.contains("tile") || x2BodyName.contains("ground")) {
							player.stopFalling();
							player.stopJumping();
						}

						/** player contacts an item */
						if (x2BodyName.contains("Item")) {

							/** update player controls showing the new item has been collected */
							Item itemCollected = level.getItemByName(x2BodyName);
							String itemName = itemCollected.getName();
							Log.i("Collected Item: ", itemName);
							String[] itemNameDetails = itemName.split("-");
							controls.updatePlayerAbilityButtons(Integer.parseInt(itemNameDetails[2]));
							ResourceManager.getIntance().aquireTokenSound.play();
							itemCollected.collect(GameScene.this);
						}

						/** player contacts an piece */
						if (x2BodyName.contains("Piece")) {

							/** save player collected piece and collect it */
							Piece itemCollected = level.getPieceByName(x2BodyName);
							GameStatus.collectShipPiece(itemCollected.getType());
							ResourceManager.getIntance().aquirePieceSound.play();
							itemCollected.collect(GameScene.this);

							/** now that we've collected a new piece, see if we've completed the level yet */
							checkLevelComplete();
						}
					}

					/** actor touches platform or another actor */
					if (x2BodyName.contains("Actor") && !(x1BodyName.contains("ground") || x1BodyName.contains("physical"))) {
						Actor actor = level.getActorByName(x2BodyName);
						actor.changeDirection();
					}

					/** handle bomb drop which destroys tiles */
					if (x1BodyName.contains("bomb") && x2BodyName.contains("tile")) {
						Tile tile = level.getTileByName(x2BodyName);
						tile.breakTile(GameScene.this);
					}
					if (x2BodyName.contains("bomb") && x1BodyName.contains("tile")) {
						Tile tile = level.getTileByName(x1BodyName);
						tile.breakTile(GameScene.this);
					}
					
					/** handle flare drop which destroys tiles */
					if (x1BodyName.contains("Flare") && x2BodyName.contains("tile")) {
						Tile tile = level.getTileByName(x2BodyName);
						tile.breakTile(GameScene.this);
					}
					if (x2BodyName.contains("Flare") && x1BodyName.contains("tile")) {
						Tile tile = level.getTileByName(x1BodyName);
						tile.breakTile(GameScene.this);
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
	private void checkLevelComplete() {
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
			 * @todo this should set 2, 3 or 4 based on overall level player performance
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