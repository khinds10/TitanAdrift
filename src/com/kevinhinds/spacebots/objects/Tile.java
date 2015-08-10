package com.kevinhinds.spacebots.objects;

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

	private final String name, type;
	private final int id, tileIndex;
	private final float density, elastic, friction;

	/**
	 * create new tile from a tiledSprite with a specific tile index specified as well as if it's a physical tile a background or foreground tile
	 * 
	 * @param name
	 * @param id
	 * @param tileIndex
	 * @param type
	 * @param x
	 * @param y
	 * @param density
	 * @param elastic
	 * @param friction
	 * @param texture
	 * @param vbom
	 */
	public Tile(String name, int id, int tileIndex, String type, float x, float y, float density, float elastic, float friction, ITiledTextureRegion texture, VertexBufferObjectManager vbom) {
		super(x, y, texture, vbom);
		this.name = name;
		this.id = id;
		this.tileIndex = tileIndex;
		this.density = density;
		this.elastic = elastic;
		this.friction = friction;
		this.type = type;
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
		this.setCurrentTileIndex(tileIndex);

		/** only apply physics to the tiles marked as such */
		if (type.equals("physical") || type.equals("bounce")) {
			String tileData = "tile";
			if (type.equals("bounce")) {
				tileData = "bounce";
			}
			Body body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.StaticBody, tileFixtureDef);
			body.setUserData(tileData);
			physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, true));
		}

		/** set the Z-Index of the tile based on the type */
		if (type.equals("physical") || type.equals("bounce")) {
			this.setZIndex(0);
		}
		if (type.equals("foreground")) {
			this.setZIndex(1);
		}
		if (type.equals("background")) {
			this.setZIndex(-1);
		}
		scene.attachChild(this);
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
	public Tile getInstance(float x, float y, String type) {
		return new Tile(name, id, tileIndex, type, x, y, density, elastic, friction, getTiledTextureRegion(), getVertexBufferObjectManager());
	}
}