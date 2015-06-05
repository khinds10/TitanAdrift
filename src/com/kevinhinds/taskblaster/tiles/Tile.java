package com.kevinhinds.taskblaster.tiles;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * a tile sprite extends sprite to easily define and attach to any scene
 * 
 * @author khinds
 */
public class Tile extends TiledSprite {

	private final String name;
	private final int id, tileIndex;
	private final float density, elastic, friction;

	/**
	 * create new tile from a tiledSprite with a specific tile index specified
	 * 
	 * @param name
	 * @param id
	 * @param tileIndex
	 * @param x
	 * @param y
	 * @param density
	 * @param elastic
	 * @param friction
	 * @param texture
	 * @param vbom
	 */
	public Tile(String name, int id, int tileIndex, float x, float y, float density, float elastic, float friction, ITiledTextureRegion texture, VertexBufferObjectManager vbom) {
		super(x, y, texture, vbom);
		this.name = name;
		this.id = id;
		this.tileIndex = tileIndex;
		this.density = density;
		this.elastic = elastic;
		this.friction = friction;
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
		Body body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.StaticBody, tileFixtureDef);
		body.setUserData("tile");
		scene.attachChild(this);
		this.setCurrentTileIndex(tileIndex);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, true));
	}

	/**
	 * get name of this tile
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * get ID of this tile
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * by simple x and y coordinates create a new tile
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Tile getInstance(float x, float y) {
		return new Tile(name, id, tileIndex, x, y, density, elastic, friction, getTiledTextureRegion(), getVertexBufferObjectManager());
	}
}
