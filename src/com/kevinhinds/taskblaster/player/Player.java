package com.kevinhinds.taskblaster.player;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.kevinhinds.taskblaster.GameConfiguation;
import com.kevinhinds.taskblaster.GameConfiguation.State;
import com.kevinhinds.taskblaster.ResourceManager;
import com.kevinhinds.taskblaster.scene.BaseScene;
import com.kevinhinds.taskblaster.weapons.Bullet;

/**
 * main character for the game
 * 
 * @author khinds
 */
public class Player {

	public Body playerBody;
	public AnimatedSprite playerSprite;
	public Bullet bullet;
	public boolean isShooting;

	public boolean isJumping;
	protected State moving;
	protected State facing;

	/**
	 * create player on the scene in question
	 * 
	 * @param scene
	 */
	public Player(BaseScene scene) {
		final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(1, 0.0f, 0.0f);
		playerSprite = scene.createAnimatedSprite(ResourceManager.getIntance().camera.getWidth() / 2, ResourceManager.getIntance().camera.getHeight() - 150, ResourceManager.getIntance().player_region, scene.vbom);
		playerBody = PhysicsFactory.createBoxBody(scene.physicsWorld, playerSprite, BodyType.DynamicBody, playerFixtureDef);
		playerBody.setUserData("player");
		scene.physicsWorld.registerPhysicsConnector(new PhysicsConnector(playerSprite, playerBody, true, false));
		playerSprite.animate(new long[] { GameConfiguation.playerAnimationSpeed }, new int[] { GameConfiguation.playerStartLevelFrame });
		moving = State.STOP;
		facing = State.RIGHT;
		isJumping = false;
		scene.attachChild(playerSprite);
	}

	/**
	 * player moves right w/animation
	 */
	public void moveRight() {
		moving = State.RIGHT;
		facing = State.RIGHT;
		if (!isJumping) {
			playerSprite.animate(new long[] { GameConfiguation.playerAnimationSpeed, GameConfiguation.playerAnimationSpeed, GameConfiguation.playerAnimationSpeed, GameConfiguation.playerAnimationSpeed, GameConfiguation.playerAnimationSpeed, GameConfiguation.playerAnimationSpeed, GameConfiguation.playerAnimationSpeed, GameConfiguation.playerAnimationSpeed }, GameConfiguation.walkRightBeginFrame, GameConfiguation.walkRightEndFrame, true);
			playerBody.setLinearVelocity(GameConfiguation.playerWalkingVelocity, 0.0f);
		} else {
			float minJumpVelocity = GameConfiguation.playerFallingChangeDirectionVelocity;
			if (moving == State.RIGHT) {
				minJumpVelocity = GameConfiguation.playerFallingContinueDirectionVelocity;
			}
			if (facing == State.RIGHT) {
				playerSprite.animate(new long[] { GameConfiguation.playerAnimationSpeed }, new int[] { GameConfiguation.playerJumpRightFrame });
			} else {
				playerSprite.animate(new long[] { GameConfiguation.playerAnimationSpeed }, new int[] { GameConfiguation.playerJumpLeftFrame });
			}
			playerBody.setLinearVelocity(minJumpVelocity, GameConfiguation.playerStopWhileFallingVelocity);
		}
	}

	/**
	 * player moves left w/animation
	 */
	public void moveLeft() {
		moving = State.LEFT;
		facing = State.LEFT;
		if (!isJumping) {
			playerSprite.animate(new long[] { GameConfiguation.playerAnimationSpeed, GameConfiguation.playerAnimationSpeed, GameConfiguation.playerAnimationSpeed, GameConfiguation.playerAnimationSpeed, GameConfiguation.playerAnimationSpeed, GameConfiguation.playerAnimationSpeed, GameConfiguation.playerAnimationSpeed, GameConfiguation.playerAnimationSpeed }, GameConfiguation.walkLeftBeginFrame, GameConfiguation.walkLeftEndFrame, true);
			playerBody.setLinearVelocity(-GameConfiguation.playerWalkingVelocity, 0.0f);
		} else {
			float minJumpVelocity = -GameConfiguation.playerFallingChangeDirectionVelocity;
			if (moving == State.LEFT) {
				minJumpVelocity = -GameConfiguation.playerFallingContinueDirectionVelocity;
			}
			if (facing == State.RIGHT) {
				playerSprite.animate(new long[] { GameConfiguation.playerAnimationSpeed }, new int[] { GameConfiguation.playerJumpRightFrame });
			} else {
				playerSprite.animate(new long[] { GameConfiguation.playerAnimationSpeed }, new int[] { GameConfiguation.playerJumpLeftFrame });
			}
			playerBody.setLinearVelocity(minJumpVelocity, GameConfiguation.playerStopWhileFallingVelocity);
		}
	}

	/**
	 * player stops
	 */
	public void stop() {
		float jumpmotion = 0;
		if (!isJumping) {
			if (facing == State.RIGHT) {
				playerSprite.animate(new long[] { GameConfiguation.playerAnimationSpeed }, new int[] { GameConfiguation.playerFaceRightFrame });
			} else {
				playerSprite.animate(new long[] { GameConfiguation.playerAnimationSpeed }, new int[] { GameConfiguation.playerFaceLeftFrame });
			}
			playerBody.setLinearVelocity(0f, 0f);
		} else {
			if (facing == State.RIGHT) {
				jumpmotion = GameConfiguation.playerContinueMovingWhileFallingVelocity;
				playerSprite.animate(new long[] { GameConfiguation.playerAnimationSpeed }, new int[] { GameConfiguation.playerJumpRightFrame });
			} else {
				jumpmotion = -GameConfiguation.playerContinueMovingWhileFallingVelocity;
				playerSprite.animate(new long[] { GameConfiguation.playerAnimationSpeed }, new int[] { GameConfiguation.playerJumpLeftFrame });
			}
			
			playerBody.setLinearVelocity(jumpmotion, GameConfiguation.playerStopWhileFallingVelocity);
		}
		moving = State.STOP;
	}

	/**
	 * upon contact with object we must stop any current jump as well as continue as before
	 */
	public void stopJumping() {
		if (isJumping) {
			isJumping = false;
			if (moving == State.RIGHT) {
				moveRight();
			}
			if (moving == State.LEFT) {
				moveLeft();
			}
			if (moving == State.STOP) {
				stop();
			}
		}
	}

	/**
	 * player jumps
	 */
	public void jump() {
		if (!isJumping) {
			float jumpmotion = 0;
			isJumping = true;
			if (moving == State.RIGHT) {
				jumpmotion = GameConfiguation.playerFallingVelocity;
				playerSprite.animate(new long[] { GameConfiguation.playerAnimationSpeed }, new int[] { GameConfiguation.playerJumpRightFrame });
			}
			if (moving == State.LEFT) {
				jumpmotion = -GameConfiguation.playerFallingVelocity;
				playerSprite.animate(new long[] { GameConfiguation.playerAnimationSpeed }, new int[] { GameConfiguation.playerJumpLeftFrame });
			}
			if (moving == State.STOP) {
				jumpmotion = 0;
				if (facing == State.RIGHT) {
					playerSprite.animate(new long[] { GameConfiguation.playerAnimationSpeed }, new int[] { GameConfiguation.playerJumpRightFrame });
				}
				if (facing == State.LEFT) {
					playerSprite.animate(new long[] { GameConfiguation.playerAnimationSpeed }, new int[] { GameConfiguation.playerJumpLeftFrame });
				}
			}
			final Vector2 velocity = Vector2Pool.obtain(jumpmotion, GameConfiguation.playerJumpVelocity);
			playerBody.setLinearVelocity(velocity);
		}
	}

	public void shoot(BaseScene scene) {
		if (!isShooting) {
			bullet = new Bullet(scene, playerSprite, facing);
			isShooting = true;
		}
	}

	public void shootContact() {
		bullet.hitObject();
		isShooting = false;
	}
}