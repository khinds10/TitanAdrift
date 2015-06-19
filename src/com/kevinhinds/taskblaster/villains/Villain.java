package com.kevinhinds.taskblaster.villains;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.TiledSprite;
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

/**
 * a tile sprite extends sprite to easily define and attach to any scene
 * 
 * @author khinds
 */
public class Villain extends TiledSprite {

	private String facing, name, type, weapon;
	private int id, life, spriteRow;
	private float density, elastic, friction, x, y;
	private boolean shoots;

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
	 */
	public Villain(String name, int id, int spriteRow, String type, int life, String weapon, String facing, Boolean shoots, float x, float y, float density, float elastic, float friction, ITiledTextureRegion texture, VertexBufferObjectManager vbom) {
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
		this.spriteRow = (GameConfiguation.villainMapColumns * this.spriteRow) - (GameConfiguation.villainMapColumns - 1);
		if (this.facing.equals("left")) {
			this.spriteRow = this.spriteRow + 5;
		}
		this.setCurrentTileIndex(spriteRow);

		Body body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, tileFixtureDef);
		
		
		
		
		this.name = "Villain: " + Float.toString(this.x) + "-" + Float.toString(this.y) + "-" + Integer.toString(this.spriteRow);
		body.setUserData(this.name);
		
		
		
		
		
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));

		/** set the type of villain behavior of the tile based on the type */
		if (type.equals("flying")) {

		}
		if (type.equals("stationary")) {

		}
		if (type.equals("walking")) {

		}
		scene.attachChild(this);
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
	 */
	public void takeDamage() {
		life = life - 1;
		Log.e(this.getName(), "Life: " + Integer.toString(this.life));
		if (this.life < 0) {
			die();
		}
	}

	/**
	 * bullet hits object
	 */
	public void die() {

		Log.e(this.getName(), "Died!");

		// ResourceManager.getIntance().engine.runOnUpdateThread(new Runnable() {
		// @Override
		// public void run() {
		// // this.setActive(false);
		// // this.setUserData("deleted");
		// // scene.detachChild(bulletSprite);
		// }
		// });
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
	public Villain getInstance(float x, float y, String type, int life, String weapon, String facing, Boolean shoots) {
		return new Villain(name, id, spriteRow, type, life, weapon, facing, shoots, x, y, density, elastic, friction, getTiledTextureRegion(), getVertexBufferObjectManager());
	}
}