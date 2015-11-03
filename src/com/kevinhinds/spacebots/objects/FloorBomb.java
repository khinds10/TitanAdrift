package com.kevinhinds.spacebots.objects;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.kevinhinds.spacebots.GameConfiguration;
import com.kevinhinds.spacebots.ResourceManager;
import com.kevinhinds.spacebots.GameConfiguration.State;
import com.kevinhinds.spacebots.scene.BaseScene;

/**
 * floor bomb set by the player
 * 
 * @author khinds
 */
public class FloorBomb {

	public AnimatedSprite floorBombSprite;
	private long[] animationSpeed;
	public Body bombBody;

	/**
	 * create a new floor bomb based on where the player is on the screen
	 * 
	 * @param scene
	 * @param actorSprite
	 * @param facing
	 */
	public FloorBomb(final BaseScene scene, AnimatedSprite actorSprite) {
		float[] objCenterPos = new float[2];
		actorSprite.getSceneCenterCoordinates(objCenterPos);
		this.detonate(scene, objCenterPos);
	}

	/**
	 * create a new floor bomb based on the player and what direction they're facing
	 * 
	 * @param scene
	 * @param playerSprite
	 * @param facing
	 */
	public FloorBomb(final BaseScene scene, AnimatedSprite playerSprite, State facing) {
		float[] objCenterPos = new float[2];
		playerSprite.getSceneCenterCoordinates(objCenterPos);
		objCenterPos[1] = objCenterPos[1] - 30;

		if (facing == State.LEFT) {
			objCenterPos[0] = objCenterPos[0] - 100;
		} else {
			objCenterPos[0] = objCenterPos[0] + 25;
		}
		this.detonate(scene, objCenterPos);
	}

	/**
	 * detonate!
	 * 
	 * @param scene
	 * @param objCenterPos
	 * @param type
	 */
	protected void detonate(final BaseScene scene, float[] objCenterPos) {

		animationSpeed = new long[] { GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed,
				GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed, GameConfiguration.floorBombAnimationSpeed };

		/** create a new floor bomb sprite and detonate it! */
		floorBombSprite = scene.createAnimatedSprite(objCenterPos[0], objCenterPos[1], ResourceManager.getIntance().floorBombRegion, scene.vbom);
		final FixtureDef tileFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0);
		tileFixtureDef.restitution = 0;
		bombBody = PhysicsFactory.createBoxBody(scene.physicsWorld, this.floorBombSprite, BodyType.DynamicBody, tileFixtureDef);
		scene.physicsWorld.registerPhysicsConnector(new PhysicsConnector(this.floorBombSprite, bombBody, true, true));
		bombBody.setUserData("floorbomb");

		/** we'll start counting from 1 but the sprite sheet counts from zero */
		int startTile = 0;
		int endTile = startTile + 34;

		/** explosion animates through and then disappears */
		floorBombSprite.animate(animationSpeed, startTile, endTile, false, new IAnimationListener() {

			@Override
			public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {

				/** remove current bomb from game physics */
				bombBody.setActive(false);
			}

			@Override
			public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {
			}

			@Override
			public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {
			}

			@Override
			public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {
			}
		});
		scene.attachChild(floorBombSprite);
	}
}