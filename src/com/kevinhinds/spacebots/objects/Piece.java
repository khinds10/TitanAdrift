package com.kevinhinds.spacebots.objects;

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
import com.kevinhinds.spacebots.ResourceManager;
import com.kevinhinds.spacebots.scene.GameScene;

/**
 * an item sprite extends sprite to easily define and attach to any scene
 * 
 * @author khinds
 */
public class Piece extends TiledSprite {

	private final String name;
	private final int id, tileIndex;
	private final float density, elastic, friction;
	public Body itemBody;
	public Scene scene;

	/**
	 * create new item from a tiledSprite with a specific tile index specified as well as if it's a physical tile a background or foreground tile
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
	public Piece(String name, int id, int tileIndex, float x, float y, float density, float elastic, float friction, ITiledTextureRegion texture, VertexBufferObjectManager vbom) {
		super(x, y, texture, vbom);
		this.name = name;
		this.id = id;
		this.tileIndex = tileIndex;
		this.density = density;
		this.elastic = elastic;
		this.friction = friction;
	}

	/**
	 * attach this current item to the scene in question with the tile index applied
	 * 
	 * @param scene
	 * @param physicsWorld
	 */
	public void createBodyAndAttach(Scene scene, PhysicsWorld physicsWorld) {

		this.scene = scene;
		final FixtureDef tileFixtureDef = PhysicsFactory.createFixtureDef(density, elastic, friction);
		tileFixtureDef.restitution = 0;
		this.setCurrentTileIndex(tileIndex);
		itemBody = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.KinematicBody, tileFixtureDef);
		itemBody.setUserData(this.name);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, itemBody, true, true));
		scene.attachChild(this);
	}

	/**
	 * get name of this item
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * get ID of this item
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * bullet hits object
	 * 
	 * @param player
	 * 
	 * @param scene
	 * 
	 * @param gameScene
	 */
	public void collect(GameScene thisScene) {
		Log.i(this.getName(), "Piece Collected");
		final PhysicsConnector physicsConnector = thisScene.physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(Piece.this);
		ResourceManager.getIntance().engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				if (physicsConnector != null) {
					itemBody.setActive(false);
					itemBody.setUserData("collected");
					scene.detachChild(Piece.this);
				}
			}
		});
	}

	/**
	 * by simple x and y coordinates create a new item
	 * 
	 * @param x
	 * @param y
	 * @param animationSpeed
	 * @param movementSpeed
	 * @return
	 */
	public Piece getInstance(String name, float x, float y, int animationSpeed, int movementSpeed) {
		return new Piece(name, id, tileIndex, x, y, density, elastic, friction, getTiledTextureRegion(), getVertexBufferObjectManager());
	}
}