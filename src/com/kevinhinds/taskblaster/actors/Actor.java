package com.kevinhinds.taskblaster.actors;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.kevinhinds.taskblaster.GameConfiguation;
import com.kevinhinds.taskblaster.ResourceManager;
import com.kevinhinds.taskblaster.explosion.Explosion;
import com.kevinhinds.taskblaster.player.Player;
import com.kevinhinds.taskblaster.scene.BaseScene;
import com.kevinhinds.taskblaster.scene.GameScene;

/**
 * a tile sprite extends sprite to easily define and attach to any scene
 * 
 * @author khinds
 */
public class Actor extends AnimatedSprite {

	private String facing, name, type, weapon;
	private int id, life, spriteRow, animationSpeed, explosionType;
	private float density, elastic, friction, x, y, movementSpeed;
	private boolean shoots;
	public Body actorBody;
	public Scene scene;

	/**
	 * create new adversary from a tiledSprite with a specific tile index specified as well as if it's a physical tile a background or foreground tile
	 * 
	 * @param name
	 * @param id
	 * @param tileIndex
	 * @param type
	 * @param life
	 * @param weapon
	 * @param facing
	 * @param shoots
	 * @param x
	 * @param y
	 * @param density
	 * @param elastic
	 * @param friction
	 * @param texture
	 * @param vbom
	 * @param animationSpeed 
	 * @param movementSpeed 
	 * @param explosionType 
	 */
	public Actor(String name, int id, int spriteRow, String type, int life, String weapon, String facing, Boolean shoots, int animationSpeed, float movementSpeed, int explosionType, float x, float y, float density, float elastic, float friction, ITiledTextureRegion texture, VertexBufferObjectManager vbom) {
		super(x, y, texture, vbom);
		this.x = x;
		this.y = y;
		this.name = name;
		this.id = id;
		this.spriteRow = spriteRow;
		this.density = density;
		this.elastic = elastic;
		this.friction = friction;
		this.type = type;
		this.weapon = weapon;
		this.facing = facing;
		this.life = life;
		this.shoots = shoots;
		this.animationSpeed = animationSpeed;
		this.movementSpeed = movementSpeed;
		this.explosionType = explosionType;

		Log.e(this.getName(), "Life: " + Integer.toString(this.life));
	}

	/**
	 * attach this current tile to the scene in question with the tile index applied
	 * 
	 * @param scene
	 * @param physicsWorld
	 */
	public void createBodyAndAttach(Scene scene, PhysicsWorld physicsWorld) {
		final FixtureDef tileFixtureDef = PhysicsFactory.createFixtureDef(density, elastic, friction);
		tileFixtureDef.restitution = 0;

		/** get the villian on the sprite row specified, if the sprite is facing left, then the tile is higher on the same tile row to face the other way */
		this.spriteRow = (GameConfiguation.actorMapColumns * this.spriteRow) - (GameConfiguation.actorMapColumns);
		if (this.facing.equals("right")) {
			this.spriteRow = this.spriteRow + 5;
			movementSpeed = -movementSpeed;
		}
		this.setCurrentTileIndex(spriteRow);

		actorBody = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, tileFixtureDef);
		actorBody.setUserData(this.name);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, actorBody, true, false));

		/** set the type of villain behavior of the tile based on the type */
		if (type.equals("flying")) {

		}
		if (type.equals("walking")) {

		}
		this.scene = scene;
		scene.attachChild(this);
		
		/** the actor animates as it comes to life */
		this.animate(new long[] { animationSpeed, animationSpeed, animationSpeed }, spriteRow, spriteRow + 2, true);
		
		/** the actor moves */
		actorBody.setLinearVelocity(movementSpeed, 0.0f);
		
	}

	/**
	 * get name of this villain
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * get ID of this villain
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * villain takes a hit of damage
	 * 
	 * @param gameScene
	 * 
	 * @param gameScene
	 * @param player 
	 */
	public void takeDamage(int hitPoints, GameScene gameScene, final Player player) {
		life = life - hitPoints;
		Log.e(this.getName(), "Life: " + Integer.toString(this.life));
		if (this.life <= 0) {
			die(gameScene, player);
		} else {
			final PhysicsConnector physicsConnector = gameScene.physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(Actor.this);
			ResourceManager.getIntance().engine.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					if (physicsConnector != null) {
						player.bullet.bulletBody.setActive(false);
						player.bullet.bulletBody.setUserData("deleted");
						scene.detachChild(player.bullet.bulletSprite);
						player.isShooting = false;
					}
				}
			});
		}
	}

	/**
	 * bullet hits object
	 * @param player 
	 * 
	 * @param scene
	 * 
	 * @param gameScene
	 */
	public void die(BaseScene thisScene, final Player player) {
		Log.e(this.getName(), "Has Died");
		
		new Explosion(thisScene, this, explosionType);
		
		final PhysicsConnector physicsConnector = thisScene.physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(Actor.this);
		ResourceManager.getIntance().engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				if (physicsConnector != null) {
					actorBody.setActive(false);
					actorBody.setUserData("deleted");
					scene.detachChild(Actor.this);
					player.bullet.bulletBody.setActive(false);
					player.bullet.bulletBody.setUserData("deleted");
					scene.detachChild(player.bullet.bulletSprite);
					player.isShooting = false;
				}
			}
		});
	}

	/**
	 * by simple x and y coordinates create a new villain
	 * 
	 * @param x
	 * @param y
	 * @param life
	 * @param weapon
	 * @param facing
	 * @param shoots
	 * @return
	 */
	public Actor getInstance(float x, float y, String type, int life, String weapon, String facing, Boolean shoots, String name, int animationSpeed, float movementSpeed, int explosionType) {
		return new Actor(name, id, spriteRow, type, life, weapon, facing, shoots, animationSpeed, movementSpeed, explosionType, x, y, density, elastic, friction, getTiledTextureRegion(), getVertexBufferObjectManager());
	}
}