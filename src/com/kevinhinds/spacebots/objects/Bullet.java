package com.kevinhinds.spacebots.objects;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.kevinhinds.spacebots.ResourceManager;
import com.kevinhinds.spacebots.GameConfiguration.State;
import com.kevinhinds.spacebots.scene.BaseScene;

/**
 * bullet from player
 * 
 * @author khinds
 */
public class Bullet {

	public Body bulletBody;
	public AnimatedSprite bulletSprite;
	public BaseScene scene;

	/**
	 * create a new bullet based on the player position and facing direction
	 * 
	 * @param scene
	 * @param playerSprite
	 * @param facing
	 */
	public Bullet(BaseScene scene, AnimatedSprite playerSprite, State facing) {

		/** get the direction of the player facing */
		float[] objCenterPos = new float[2];
		playerSprite.getSceneCenterCoordinates(objCenterPos);
		final FixtureDef bulletFixtureDef = PhysicsFactory.createFixtureDef(0, 0.0f, 0.0f);
		this.scene = scene;

		/** apply left or right facing x and acceleration values */
		int direction = 1;
		if (facing == State.LEFT) {
			direction = -1;
		}

		/** create a new bullet sprite and fire it! */
		bulletSprite = scene.createAnimatedSprite(objCenterPos[0] + (direction * 5), objCenterPos[1] - 5, ResourceManager.getIntance().bullet_region, scene.vbom);
		bulletBody = PhysicsFactory.createBoxBody(scene.physicsWorld, bulletSprite, BodyType.DynamicBody, bulletFixtureDef);
		bulletBody.setUserData("bullet");
		bulletBody.setGravityScale(0);
		scene.physicsWorld.registerPhysicsConnector(new PhysicsConnector(bulletSprite, bulletBody, true, true));
		bulletSprite.animate(new long[] { 100, 100, 100, 100 }, new int[] { 0, 1, 2, 3 });

		scene.attachChild(bulletSprite);
		final Vector2 velocity = Vector2Pool.obtain((direction * 50), 0);
		bulletBody.setLinearVelocity(velocity);
	}

	/**
	 * bullet hits object
	 */
	public void hitObject() {

		final PhysicsConnector physicsConnector = scene.physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(bulletSprite);

		ResourceManager.getIntance().engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				if (physicsConnector != null) {
					bulletBody.setActive(false);
					bulletBody.setUserData("deleted");
					scene.detachChild(bulletSprite);
				}
			}
		});
	}
}