package com.kevinhinds.taskblaster.tiles;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Tile extends Sprite {

	private final String name;
	private final int id;
	private final float density, elastic, friction;

	public Tile(String name, int id, float x, float y, float density, float elastic, float friction, ITextureRegion texture, VertexBufferObjectManager vbom) {
		super(x, y, texture, vbom);
		this.name = name;
		this.id = id;
		this.density = density;
		this.elastic = elastic;
		this.friction = friction;
	}

	public void createBodyAndAttach(Scene scene, PhysicsWorld physicsWorld) {
		final FixtureDef tileFixtureDef = PhysicsFactory.createFixtureDef(density, elastic, friction);
		tileFixtureDef.restitution = 0;
		Body body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.StaticBody, tileFixtureDef);
		scene.attachChild(this);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, true));
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public Tile getInstance(float x, float y) {
		return new Tile(name, id, x, y, density, elastic, friction, getTextureRegion(), getVertexBufferObjectManager());
	}
}
