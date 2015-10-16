package com.kevinhinds.spacebots.objects;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;

import com.kevinhinds.spacebots.GameConfiguration;
import com.kevinhinds.spacebots.ResourceManager;
import com.kevinhinds.spacebots.GameConfiguration.State;
import com.kevinhinds.spacebots.scene.BaseScene;

/**
 * explosion on the screen
 * 
 * @author khinds
 */
public class Explosion {

	public AnimatedSprite explosionSprite;
	private long[] animationSpeed;

	/**
	 * create a new explosion based on sprite exploding and what type of explosion it is
	 * 
	 * @param scene
	 * @param actorSprite
	 * @param facing
	 */
	public Explosion(final BaseScene scene, AnimatedSprite actorSprite, int type) {
		float[] objCenterPos = new float[2];
		actorSprite.getSceneCenterCoordinates(objCenterPos);
		this.explode(scene, objCenterPos, type);
	}

	/**
	 * create a new explosion based on sprite exploding and what type of explosion it is
	 * 
	 * @param scene
	 * @param bullet
	 * @param type
	 */
	public Explosion(final BaseScene scene, Bullet bullet, int type) {
		float[] objCenterPos = new float[2];
		bullet.getSceneCenterCoordinates(objCenterPos);
		if (bullet.direction == State.RIGHT) {
			objCenterPos[1] = objCenterPos[1] - 10;
		} else {
			objCenterPos[1] = objCenterPos[1] + 10;
		}
		this.explode(scene, objCenterPos, type);
	}

	/**
	 * explode!
	 * 
	 * @param scene
	 * @param objCenterPos
	 * @param type
	 */
	protected void explode(final BaseScene scene, float[] objCenterPos, int type) {
		animationSpeed = new long[] { GameConfiguration.explosionAnimationSpeed, GameConfiguration.explosionAnimationSpeed, GameConfiguration.explosionAnimationSpeed, GameConfiguration.explosionAnimationSpeed, GameConfiguration.explosionAnimationSpeed, GameConfiguration.explosionAnimationSpeed, GameConfiguration.explosionAnimationSpeed, GameConfiguration.explosionAnimationSpeed, GameConfiguration.explosionAnimationSpeed, GameConfiguration.explosionAnimationSpeed, GameConfiguration.explosionAnimationSpeed, GameConfiguration.explosionAnimationSpeed, GameConfiguration.explosionAnimationSpeed, GameConfiguration.explosionAnimationSpeed, GameConfiguration.explosionAnimationSpeed, GameConfiguration.explosionAnimationSpeed, GameConfiguration.explosionAnimationSpeed };

		/** create a new explosion sprite and detonate it! */
		explosionSprite = scene.createAnimatedSprite(objCenterPos[0], objCenterPos[1] - 12, ResourceManager.getIntance().explosionRegion, scene.vbom);

		/** we'll start counting from 1 but the sprite sheet counts from zero */
		int startTile = (type * GameConfiguration.explosionMapColumns);
		int endTile = startTile + 16;

		/** explosion animates through and then disappears */
		explosionSprite.animate(animationSpeed, startTile, endTile, false, new IAnimationListener() {

			@Override
			public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
				explosionSprite.setVisible(false);
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
		scene.attachChild(explosionSprite);
	}
}