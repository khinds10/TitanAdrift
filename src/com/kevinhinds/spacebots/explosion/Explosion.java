package com.kevinhinds.spacebots.explosion;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;

import com.kevinhinds.spacebots.GameConfiguation;
import com.kevinhinds.spacebots.ResourceManager;
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
	 * create a new bullet based on the player position and facing direction
	 * 
	 * @param scene
	 * @param actorSprite
	 * @param facing
	 */
	public Explosion(final BaseScene scene, AnimatedSprite actorSprite, int type) {

		/** get the direction of the player facing */
		float[] objCenterPos = new float[2];
		actorSprite.getSceneCenterCoordinates(objCenterPos);
		animationSpeed = new long[] { GameConfiguation.explosionAnimationSpeed, GameConfiguation.explosionAnimationSpeed, GameConfiguation.explosionAnimationSpeed, GameConfiguation.explosionAnimationSpeed, GameConfiguation.explosionAnimationSpeed, GameConfiguation.explosionAnimationSpeed, GameConfiguation.explosionAnimationSpeed, GameConfiguation.explosionAnimationSpeed, GameConfiguation.explosionAnimationSpeed, GameConfiguation.explosionAnimationSpeed, GameConfiguation.explosionAnimationSpeed, GameConfiguation.explosionAnimationSpeed, GameConfiguation.explosionAnimationSpeed, GameConfiguation.explosionAnimationSpeed, GameConfiguation.explosionAnimationSpeed, GameConfiguation.explosionAnimationSpeed, GameConfiguation.explosionAnimationSpeed };

		/** create a new explosion sprite and detonate it! */
		explosionSprite = scene.createAnimatedSprite(objCenterPos[0], objCenterPos[1] - 12, ResourceManager.getIntance().explosion_region, scene.vbom);

		/** we'll start counting from 1 but the sprite sheet counts from zero */
		int startTile = (type * GameConfiguation.explosionMapColumns);
		int endTile = startTile + 16;

		/** explosion animates through and then disappears */
		explosionSprite.animate(animationSpeed, startTile, endTile, false, new IAnimationListener() {

			@Override
			public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
				explosionSprite.setVisible(false);
			}

			@Override
			public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {
				// TODO Auto-generated method stub

			}
		});
		scene.attachChild(explosionSprite);
	}
}