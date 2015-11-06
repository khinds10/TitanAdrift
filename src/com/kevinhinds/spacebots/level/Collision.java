package com.kevinhinds.spacebots.level;

import com.kevinhinds.spacebots.ResourceManager;
import com.kevinhinds.spacebots.objects.Actor;
import com.kevinhinds.spacebots.objects.Bullet;
import com.kevinhinds.spacebots.objects.Flare;
import com.kevinhinds.spacebots.objects.Item;
import com.kevinhinds.spacebots.objects.Piece;
import com.kevinhinds.spacebots.objects.Tile;
import com.kevinhinds.spacebots.scene.GameScene;
import com.kevinhinds.spacebots.status.GameStatus;

import android.util.Log;

/**
 * handle collision between 2 game scene objects
 * 
 * @author khinds
 */
public class Collision {

	public Actor actor;
	public Tile tile;
	public Bullet bullet;
	public Flare flare;
	public String body1;
	public String body2;
	public Level level;
	public GameScene gameScene;

	/**
	 * process game scene collisions
	 * 
	 * @param body1
	 * @param body2
	 * @param level
	 * @param player
	 */
	public Collision(String body1, String body2, Level level, GameScene gameScene) {

		this.actor = null;
		this.body1 = body1;
		this.body2 = body2;
		this.level = level;
		this.gameScene = gameScene;

		/**
		 * ACTOR COLLISIONS
		 */
		if (body1.contains("Actor") || body2.contains("Actor")) {
			if (body1.contains("Actor")) {
				actor = level.getActorByName(body1);
				actorCollision(body2);
			} else {
				actor = level.getActorByName(body2);
				actorCollision(body1);
			}
		}

		/**
		 * TILE COLLISIONS
		 */
		if (body1.contains("tile") || body2.contains("tile")) {
			if (body1.contains("tile")) {
				tile = level.getTileByName(body1);
				tileCollision(body2);
			} else {
				tile = level.getTileByName(body2);
				tileCollision(body1);
			}
		}

		/**
		 * BULLET COLLISIONS
		 */
		if (body1.contains("Bullet") || body2.contains("Bullet")) {
			if (body1.contains("Bullet")) {
				bullet = level.getBulletByName(body1);
				if (!body2.contains("player")) {
					bullet.hitObject(gameScene);
				}
			} else {
				bullet = level.getBulletByName(body2);
				if (!body1.contains("player")) {
					bullet.hitObject(gameScene);
				}
			}
		}

		/**
		 * FLARE COLLISIONS
		 */
		if (body1.contains("Flare") || body2.contains("Flare")) {
			if (body1.contains("Flare")) {
				flare = level.getFlareByName(body1);
				if (!body2.contains("player")) {
					flare.hitObject(gameScene);
				}
			} else {
				flare = level.getFlareByName(body2);
				if (!body1.contains("player")) {
					flare.hitObject(gameScene);
				}
			}
		}

		/**
		 * PLAYER COLLIDES
		 */
		if (body1.contains("player") || body2.contains("player")) {
			
			String collidingBody = body1;
			if (body1.contains("player")) {
				collidingBody = body2;
			}

			/** player begins to fall when loses contact with a bounce tile (edges of platforms) */
			if (collidingBody.contains("tile") || collidingBody.contains("ground")) {
				gameScene.player.stopFalling();
				gameScene.player.stopJumping();
			}

			/** player contacts an item */
			if (collidingBody.contains("Item")) {

				/** update player controls showing the new item has been collected */
				Item itemCollected = level.getItemByName(collidingBody);
				String itemName = itemCollected.getName();
				Log.i("Collected Item: ", itemName);
				String[] itemNameDetails = itemName.split("-");
				gameScene.controls.updatePlayerAbilityButtons(Integer.parseInt(itemNameDetails[2]));
				ResourceManager.getIntance().aquireTokenSound.play();
				itemCollected.collect(gameScene);
			}

			/** player contacts an piece */
			if (collidingBody.contains("Piece")) {

				/** save player collected piece and collect it */
				Piece itemCollected = level.getPieceByName(collidingBody);
				GameStatus.collectShipPiece(itemCollected.getType());
				ResourceManager.getIntance().aquirePieceSound.play();
				itemCollected.collect(gameScene);

				/** now that we've collected a new piece, see if we've completed the level yet */
				gameScene.checkLevelComplete();
			}
		}
	}

	/**
	 * handle any events for objects colliding with tiles in the level
	 * 
	 * @param collidingBody
	 */
	public void tileCollision(String collidingBody) {

		/** bombs and flares break tiles in contact */
		if (collidingBody.contains("bomb")) {
			tile.breakTile(gameScene);
		}
		if (collidingBody.contains("Flare")) {
			tile.breakTile(gameScene);
		}
	}

	/**
	 * handle actors colliding with things
	 * 
	 * @param collidingBody
	 */
	public void actorCollision(String collidingBody) {

		/** actor touches something and changes direction */
		if (collidingBody.contains("ground") || collidingBody.contains("physical")) {
			actor.changeDirection();
		}

		/** actor hits Bullet/Bomb/Flare */
		if (collidingBody.contains("Bullet")) {
			actor.takeDamage(2, gameScene, gameScene.player);
		}

		if (collidingBody.contains("bomb")) {
			actor.takeDamage(20, gameScene, gameScene.player);
		}

		if (collidingBody.contains("Flare")) {
			actor.takeDamage(20, gameScene, gameScene.player);
		}

		/** player hits an actor */
		if (collidingBody.contains("player")) {
			gameScene.player.takeDamage(1);
		}
	}
}