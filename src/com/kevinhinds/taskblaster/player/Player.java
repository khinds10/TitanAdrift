package com.kevinhinds.taskblaster.player;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.kevinhinds.taskblaster.GameConstants;
import com.kevinhinds.taskblaster.ResourceManager;
import com.kevinhinds.taskblaster.scene.BaseScene;
import com.kevinhinds.taskblaster.weapons.Laser;

/**
 * main character for the game
 * 
 * @author khinds
 */
public class Player {

	public Body playerBody;
	public AnimatedSprite playerSprite;
	public Laser laser;
	public boolean isShooting;

	public enum State {
		LEFT, RIGHT, STOP
	}

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
		playerSprite = scene.createAnimatedSprite(ResourceManager.getIntance().camera.getWidth() / 2, ResourceManager.getIntance().camera.getHeight() - 250, ResourceManager.getIntance().player_region, scene.vbom);
		playerBody = PhysicsFactory.createBoxBody(scene.physicsWorld, playerSprite, BodyType.DynamicBody, playerFixtureDef);
		playerBody.setUserData("player");
		scene.physicsWorld.registerPhysicsConnector(new PhysicsConnector(playerSprite, playerBody, true, true));
		playerSprite.animate(new long[] { 100 }, new int[] { 22 });
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
			playerSprite.animate(new long[] { GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed }, 8, 15, true);
			playerBody.setLinearVelocity(4.0f, 0.0f);
		} else {
			float minJumpVelocity = 1.0f;
			if (moving == State.RIGHT) {
				minJumpVelocity = 3.0f;
			}
			if (facing == State.RIGHT) {
				playerSprite.animate(new long[] { 100 }, new int[] { 20 });
			} else {
				playerSprite.animate(new long[] { 100 }, new int[] { 21 });
			}
			playerBody.setLinearVelocity(minJumpVelocity, 3.0f);
		}
	}

	/**
	 * player moves left w/animation
	 */
	public void moveLeft() {
		moving = State.LEFT;
		facing = State.LEFT;
		if (!isJumping) {
			playerSprite.animate(new long[] { GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed }, 0, 7, true);
			playerBody.setLinearVelocity(-4.0f, 0.0f);
		} else {
			float minJumpVelocity = -1.0f;
			if (moving == State.LEFT) {
				minJumpVelocity = -3.0f;
			}
			if (facing == State.RIGHT) {
				playerSprite.animate(new long[] { 100 }, new int[] { 20 });
			} else {
				playerSprite.animate(new long[] { 100 }, new int[] { 21 });
			}
			playerBody.setLinearVelocity(minJumpVelocity, 3.0f);
		}
	}

	/**
	 * player stops
	 */
	public void stop() {
		float jumpmotion = 0;
		if (!isJumping) {
			if (facing == State.RIGHT) {
				playerSprite.animate(new long[] { 100 }, new int[] { 16 });
			} else {
				playerSprite.animate(new long[] { 100 }, new int[] { 17 });
			}
			playerBody.setLinearVelocity(0f, 0f);
		} else {
			if (facing == State.RIGHT) {
				jumpmotion = 2;
				playerSprite.animate(new long[] { 100 }, new int[] { 20 });
			} else {
				jumpmotion = -2;
				playerSprite.animate(new long[] { 100 }, new int[] { 21 });
			}
			playerBody.setLinearVelocity(jumpmotion, 3.0f);
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
				jumpmotion = 4;
				playerSprite.animate(new long[] { 100 }, new int[] { 20 });
			}
			if (moving == State.LEFT) {
				jumpmotion = -4;
				playerSprite.animate(new long[] { 100 }, new int[] { 21 });
			}
			if (moving == State.STOP) {
				jumpmotion = 0;
				if (facing == State.RIGHT) {
					playerSprite.animate(new long[] { 100 }, new int[] { 20 });
				}
				if (facing == State.LEFT) {
					playerSprite.animate(new long[] { 100 }, new int[] { 21 });
				}
			}
			final Vector2 velocity = Vector2Pool.obtain(jumpmotion, -6);
			playerBody.setLinearVelocity(velocity);
		}
	}

	public void shoot(BaseScene scene) {
		if (!isShooting) {
			laser = new Laser(scene, playerSprite);
			isShooting = true;
		}
	}

	public void shootContact() {
		laser.hitObject();
		isShooting = false;
	}
}