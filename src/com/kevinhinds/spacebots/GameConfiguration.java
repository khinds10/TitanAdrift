package com.kevinhinds.spacebots;

import java.util.HashMap;
import java.util.Map;
import android.annotation.SuppressLint;

/**
 * all constant values for game here pretty please
 * 
 * @author khinds
 */
@SuppressLint("UseSparseArrays")
public class GameConfiguration {

	/** player animated sprite tile frames */
	public static int playerAnimationSpeed = 110;
	public static int playerStartLevelFrame = 22;

	public static int playerFaceRightFrame = 16;
	public static int playerFaceLeftFrame = 17;

	public static int playerJumpRightFrame = 20;
	public static int playerJumpLeftFrame = 21;

	/** animated frames walking right begin to end */
	public static int walkRightBeginFrame = 8;
	public static int walkRightEndFrame = 15;

	/** animated frames walking left begin to end */
	public static int walkLeftBeginFrame = 0;
	public static int walkLeftEndFrame = 7;

	/** player sprite map */
	public static int playerMapColumns = 8;
	public static int playerMapRows = 3;

	/** player sprite velocities */
	public static int playerWalkingVelocity = 4;
	public static int playerJumpVelocity = -7;
	public static int playerFallingVelocity = 4;
	public static int playerFloatingVelocity = 8;
	public static int playerFallingChangeDirectionVelocity = 1;
	public static int playerStopWhileFallingVelocity = 2;
	public static int playerContinueMovingWhileFallingVelocity = 2;
	public static int playerFallingContinueDirectionVelocity = 3;

	/** player basic weapons identifiers */
	public static enum playerWeapons {
		BLUE_BULLET, GREEN_BULLET, YELLOW_BULLET, PINK_PHASER, YELLOW_DOUBLE_PHASER, YELLOW_PHASER, RED_PHASER, ORANGE_PHASER
	}

	/** set how much energy the player has starting the level and how much each gun shot decreases that energy by default */
	public static int playerStartingEnergy = 25;
	public static int playerEnergyShotAmount = 1;

	/** set how much life the player has starting the level and how much each damage decreases that life by default */
	public static int playerStartingLife = 3;
	public static int playerLifeDamageAmount = 1;

	/** player advanced abilities mapped to token IDs */
	public static Map<Integer, String> playerAbilitiesTokensMapping = new HashMap<Integer, String>();

	static {
		playerAbilitiesTokensMapping = new HashMap<Integer, String>();
		playerAbilitiesTokensMapping.put(0, "RELOAD");
		playerAbilitiesTokensMapping.put(1, "FLOAT");
		playerAbilitiesTokensMapping.put(2, "JUMP");
		playerAbilitiesTokensMapping.put(3, "LIFE");
		playerAbilitiesTokensMapping.put(4, "FREEZE");
		playerAbilitiesTokensMapping.put(5, "POWER");
	}

	/** platform sprite map */
	public static int platformMapColumns = 22;
	public static int platformMapRows = 13;

	/** item sprite map */
	public static int itemMapColumns = 6;
	public static int itemMapRows = 1;

	/** item button sprite map */
	public static int itemButtonMapColumns = 6;
	public static int itemButtonMapRows = 1;

	/** ship pieces sprite map */
	public static int pieceMapColumns = 17;
	public static int pieceMapRows = 27;

	/** bullet sprite map */
	public static int bulletMapColumns = 3;
	public static int bulletMapRows = 3;

	/** player life and energy meter map */
	public static int lifeMapColumns = 5;
	public static int lifeMapRows = 1;

	public static int energyMapColumns = 1;
	public static int energyMapRows = 10;

	/** actor sprite map and default velocities */
	public static int actorMapColumns = 8;
	public static int actorMapRows = 30;
	public static int actorAnimationSpeed = 200;
	public static float actorMovementSpeed = 2;

	/** explosions map and default options */
	public static int explosionAnimationSpeed = 15;
	public static int explosionMapColumns = 14;
	public static int explosionMapRows = 17;
	public static int explosionDefault = 1;

	/** sprite basic state identifiers */
	public static enum State {
		UP, DOWN, LEFT, RIGHT, STOP
	}

	/** how far up and off the screen the player can jump */
	public static int offscreenJumpHeight = -150;

	/** how tall is the bottom player controls area */
	public static int buttonControlsHeight = 82;

	/** level information */
	public static int numberLevels = 30;
}