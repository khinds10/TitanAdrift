package com.kevinhinds.spacebots.player;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.kevinhinds.spacebots.GameConfiguration;
import com.kevinhinds.spacebots.GameConfiguration.playerWeapons;
import com.kevinhinds.spacebots.ResourceManager;
import com.kevinhinds.spacebots.GameConfiguration.State;
import com.kevinhinds.spacebots.objects.Bullet;
import com.kevinhinds.spacebots.scene.GameScene;

/**
 * main character for the game
 * 
 * @author khinds
 */
public class Player {

	public Body playerBody;
	public AnimatedSprite playerSprite;
	public boolean isJumping;
	public boolean isFalling;
	protected State moving;
	public State facing;
	protected PhysicsWorld physicsWorld;
	protected int bulletNumber = 0;
	protected GameScene gameScene;

	/**
	 * create player on the scene in question
	 * 
	 * @param scene
	 * @param physicsWorld
	 */
	public Player(GameScene scene, PhysicsWorld playerPhysicsworld) {
		final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(1, 0.0f, 0.0f);
		gameScene = scene;
		playerSprite = scene.createAnimatedSprite(ResourceManager.getIntance().camera.getWidth() / 2, ResourceManager.getIntance().camera.getHeight() - 150, ResourceManager.getIntance().player_region, scene.vbom);
		playerBody = PhysicsFactory.createBoxBody(scene.physicsWorld, playerSprite, BodyType.DynamicBody, playerFixtureDef);
		playerBody.setUserData("player");
		gameScene.physicsWorld.registerPhysicsConnector(new PhysicsConnector(playerSprite, playerBody, true, false));
		playerSprite.animate(new long[] { GameConfiguration.playerAnimationSpeed }, new int[] { GameConfiguration.playerStartLevelFrame });
		moving = State.STOP;
		facing = State.RIGHT;
		isJumping = false;
		physicsWorld = playerPhysicsworld;
		gameScene.attachChild(playerSprite);
	}

	/**
	 * player moves right w/animation
	 */
	public void moveRight() {
		moving = State.RIGHT;
		facing = State.RIGHT;
		if (!isJumping && !isFalling) {
			playerSprite.animate(new long[] { GameConfiguration.playerAnimationSpeed, GameConfiguration.playerAnimationSpeed, GameConfiguration.playerAnimationSpeed, GameConfiguration.playerAnimationSpeed, GameConfiguration.playerAnimationSpeed, GameConfiguration.playerAnimationSpeed, GameConfiguration.playerAnimationSpeed, GameConfiguration.playerAnimationSpeed }, GameConfiguration.walkRightBeginFrame, GameConfiguration.walkRightEndFrame, true);
			playerBody.setLinearVelocity(GameConfiguration.playerWalkingVelocity, 0.0f);
		} else {
			float minJumpVelocity = GameConfiguration.playerFallingChangeDirectionVelocity;
			if (moving == State.RIGHT) {
				minJumpVelocity = GameConfiguration.playerFallingContinueDirectionVelocity;
			}
			if (facing == State.RIGHT) {
				playerSprite.animate(new long[] { GameConfiguration.playerAnimationSpeed }, new int[] { GameConfiguration.playerJumpRightFrame });
			} else {
				playerSprite.animate(new long[] { GameConfiguration.playerAnimationSpeed }, new int[] { GameConfiguration.playerJumpLeftFrame });
			}
			playerBody.setLinearVelocity(minJumpVelocity, GameConfiguration.playerStopWhileFallingVelocity);
		}
	}

	/**
	 * player moves left w/animation
	 */
	public void moveLeft() {
		moving = State.LEFT;
		facing = State.LEFT;
		if (!isJumping && !isFalling) {
			playerSprite.animate(new long[] { GameConfiguration.playerAnimationSpeed, GameConfiguration.playerAnimationSpeed, GameConfiguration.playerAnimationSpeed, GameConfiguration.playerAnimationSpeed, GameConfiguration.playerAnimationSpeed, GameConfiguration.playerAnimationSpeed, GameConfiguration.playerAnimationSpeed, GameConfiguration.playerAnimationSpeed }, GameConfiguration.walkLeftBeginFrame, GameConfiguration.walkLeftEndFrame, true);
			playerBody.setLinearVelocity(-GameConfiguration.playerWalkingVelocity, 0.0f);
		} else {
			float minJumpVelocity = -GameConfiguration.playerFallingChangeDirectionVelocity;
			if (moving == State.LEFT) {
				minJumpVelocity = -GameConfiguration.playerFallingContinueDirectionVelocity;
			}
			if (facing == State.RIGHT) {
				playerSprite.animate(new long[] { GameConfiguration.playerAnimationSpeed }, new int[] { GameConfiguration.playerJumpRightFrame });
			} else {
				playerSprite.animate(new long[] { GameConfiguration.playerAnimationSpeed }, new int[] { GameConfiguration.playerJumpLeftFrame });
			}
			playerBody.setLinearVelocity(minJumpVelocity, GameConfiguration.playerStopWhileFallingVelocity);
		}
	}

	/**
	 * player stops
	 */
	public void stop() {
		float jumpmotion = 0;
		if (!isJumping) {
			if (facing == State.RIGHT) {
				playerSprite.animate(new long[] { GameConfiguration.playerAnimationSpeed }, new int[] { GameConfiguration.playerFaceRightFrame });
			} else {
				playerSprite.animate(new long[] { GameConfiguration.playerAnimationSpeed }, new int[] { GameConfiguration.playerFaceLeftFrame });
			}
			playerBody.setLinearVelocity(0f, 0f);
		} else {
			if (facing == State.RIGHT) {
				jumpmotion = GameConfiguration.playerContinueMovingWhileFallingVelocity;
				playerSprite.animate(new long[] { GameConfiguration.playerAnimationSpeed }, new int[] { GameConfiguration.playerJumpRightFrame });
			} else {
				jumpmotion = -GameConfiguration.playerContinueMovingWhileFallingVelocity;
				playerSprite.animate(new long[] { GameConfiguration.playerAnimationSpeed }, new int[] { GameConfiguration.playerJumpLeftFrame });
			}

			playerBody.setLinearVelocity(jumpmotion, GameConfiguration.playerStopWhileFallingVelocity);
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
				jumpmotion = GameConfiguration.playerFallingVelocity;
				playerSprite.animate(new long[] { GameConfiguration.playerAnimationSpeed }, new int[] { GameConfiguration.playerJumpRightFrame });
			}
			if (moving == State.LEFT) {
				jumpmotion = -GameConfiguration.playerFallingVelocity;
				playerSprite.animate(new long[] { GameConfiguration.playerAnimationSpeed }, new int[] { GameConfiguration.playerJumpLeftFrame });
			}
			if (moving == State.STOP) {
				jumpmotion = 0;
				if (facing == State.RIGHT) {
					playerSprite.animate(new long[] { GameConfiguration.playerAnimationSpeed }, new int[] { GameConfiguration.playerJumpRightFrame });
				}
				if (facing == State.LEFT) {
					playerSprite.animate(new long[] { GameConfiguration.playerAnimationSpeed }, new int[] { GameConfiguration.playerJumpLeftFrame });
				}
			}
			final Vector2 velocity = Vector2Pool.obtain(jumpmotion, GameConfiguration.playerJumpVelocity);
			playerBody.setLinearVelocity(velocity);
		}
	}

	/**
	 * player falls
	 */
	public void fall() {
		isFalling = true;
		if (facing == State.RIGHT) {
			playerSprite.animate(new long[] { GameConfiguration.playerAnimationSpeed }, new int[] { GameConfiguration.playerJumpRightFrame });
		}
		if (facing == State.LEFT) {
			playerSprite.animate(new long[] { GameConfiguration.playerAnimationSpeed }, new int[] { GameConfiguration.playerJumpLeftFrame });
		}
	}

	/**
	 * player stops falling
	 */
	public void stopFalling() {
		if (isFalling) {
			isFalling = false;
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
	 * player shoots and adds a bullet sprite to the level being played
	 * 
	 * @param scene
	 */
	public void shoot(GameScene scene) {
		bulletNumber++;
		String bulletName = "Bullet-" + Integer.toString(bulletNumber);
		Bullet b = ResourceManager.getIntance().getGameBulletById(playerWeapons.YELLOW_PHASER.ordinal());

		/** the player X,Y is the top left corner, so if facing right the bullet should start about 50px over to the right */
		int moveBulletX = 0;
		if (this.facing == State.RIGHT) {
			moveBulletX = 25;
		}
		gameScene.level.addBullet(b.getInstance(bulletName, playerSprite.getX() + moveBulletX, playerSprite.getY() + 10));
		gameScene.level.getBulletByName(bulletName).createBodyAndAttach(playerSprite, facing, scene, physicsWorld);
	}
}