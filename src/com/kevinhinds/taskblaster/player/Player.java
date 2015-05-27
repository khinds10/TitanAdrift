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

/**
 * main character for the game
 * 
 * @author khinds
 */
public class Player {

	public Body playerBody;
	public AnimatedSprite playerSprite;

	public enum Action {
		MOVELEFT, MOVERIGHT, STOP, SHOOTRIGHT, SHOOTLEFT;
	}

	public enum Facing {
		LEFT, RIGHT
	}

	public boolean isJumping = false;
	protected Action lastdirection;
	protected Facing lastFacingDirection;

	/**
	 * create player on the scene in question
	 * 
	 * @param scene
	 */
	public Player(BaseScene scene) {
		final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(1, 0.0f, 0.0f);
		playerSprite = scene.createAnimatedSprite(300, 100, ResourceManager.getIntance().player_region, scene.vbom);
		playerBody = PhysicsFactory.createBoxBody(scene.physicsWorld, playerSprite, BodyType.DynamicBody, playerFixtureDef);
		playerBody.setUserData("player");
		scene.physicsWorld.registerPhysicsConnector(new PhysicsConnector(playerSprite, playerBody, true, true));
		playerSprite.animate(new long[] { 100 }, new int[] { GameConstants.playerStandingTile });
		lastdirection = Action.MOVERIGHT;
		lastFacingDirection = Facing.RIGHT;
		scene.attachChild(playerSprite);
	}

	/**
	 * player moves right w/animation
	 */
	public void moveRight() {
		if (!isJumping) {
			lastdirection = Action.MOVERIGHT;
			lastFacingDirection = Facing.RIGHT;
			playerSprite.animate(new long[] { GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed }, 8, 15, true);
			playerBody.setLinearVelocity(4.0f, 0.0f);
		}
	}

	/**
	 * player moves left w/animation
	 */
	public void moveLeft() {
		if (!isJumping) {
			lastdirection = Action.MOVELEFT;
			lastFacingDirection = Facing.LEFT;
			playerSprite.animate(new long[] { GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed, GameConstants.playerSpeed }, 0, 7, true);
			playerBody.setLinearVelocity(-4.0f, 0.0f);
		}

	}

	/**
	 * player stops
	 */
	public void stop() {
		if (!isJumping) {
			if (lastFacingDirection == Facing.RIGHT) {
				playerSprite.animate(new long[] { 100 }, new int[] { 16 });
			} else {
				playerSprite.animate(new long[] { 100 }, new int[] { 17 });
			}
			lastdirection = Action.STOP;
			playerBody.setLinearVelocity(0f, 0f);
		}
	}

	/**
	 * player jumps
	 */
	public void jump() {
		if (!isJumping) {
			float jumpmotion = 0;
			isJumping = true;
			if (lastdirection == Action.MOVERIGHT) {
				jumpmotion = 4;
				playerSprite.animate(new long[] { 100 }, new int[] { 20 });
			}
			if (lastdirection == Action.MOVELEFT) {
				jumpmotion = -4;
				playerSprite.animate(new long[] { 100 }, new int[] { 21 });
			}
			final Vector2 velocity = Vector2Pool.obtain(jumpmotion, -6);
			playerBody.setLinearVelocity(velocity);
		}
	}
}