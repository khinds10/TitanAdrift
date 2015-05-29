package com.kevinhinds.taskblaster.weapons;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.kevinhinds.taskblaster.ResourceManager;
import com.kevinhinds.taskblaster.TaskBlasterActivity;
import com.kevinhinds.taskblaster.scene.BaseScene;

/**
 * laser gun for player
 * 
 * @author khinds
 */
public class Laser {

	public Body laserBody;
	public AnimatedSprite laserSprite;
	public BaseScene scene;

	public Laser(BaseScene scene, AnimatedSprite playerSprite) {

		float[] objCenterPos = new float[2];
		playerSprite.getSceneCenterCoordinates(objCenterPos);
		Log.e(this.toString(), "Coordinates: (" + objCenterPos[0] + "," + objCenterPos[1] + ")");

		final FixtureDef laserFixtureDef = PhysicsFactory.createFixtureDef(0, 0.0f, 0.0f);
		this.scene = scene;
		laserSprite = scene.createAnimatedSprite(objCenterPos[0] + 50, objCenterPos[1] - 50, ResourceManager.getIntance().laser_region, scene.vbom);
		laserBody = PhysicsFactory.createBoxBody(scene.physicsWorld, laserSprite, BodyType.DynamicBody, laserFixtureDef);
		laserBody.setUserData("laser");
		scene.physicsWorld.registerPhysicsConnector(new PhysicsConnector(laserSprite, laserBody, true, true));
		laserSprite.animate(new long[] { 100 }, new int[] { 1 });
		scene.attachChild(laserSprite);
		final Vector2 velocity = Vector2Pool.obtain(50, 0);
		laserBody.setLinearVelocity(velocity);
	}

	/**
	 * laser hits object
	 */
	public void hitObject() {

		final PhysicsConnector physicsConnector = scene.physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(laserSprite);

		ResourceManager.getIntance().engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				if (physicsConnector != null) {
					scene.physicsWorld.unregisterPhysicsConnector(physicsConnector);
					laserBody.setActive(false);
					scene.physicsWorld.destroyBody(laserBody);
					scene.detachChild(laserSprite);
				}
			}
		});

	}
}
