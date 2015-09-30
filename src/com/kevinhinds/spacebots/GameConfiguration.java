package com.kevinhinds.spacebots;

/**
 * all constant values for game here pretty please
 * 
 * @author khinds
 */
public class GameConfiguration {

	/** player animated sprite tile frames */
	public static int playerAnimationSpeed = 150;
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
	public static int playerFallingChangeDirectionVelocity = 1;
	public static int playerStopWhileFallingVelocity = 2;
	public static int playerContinueMovingWhileFallingVelocity = 2;
	public static int playerFallingContinueDirectionVelocity = 3;

	/** player basic weapons identifiers */
	public static enum playerWeapons {
		BLUE_BULLET, GREEN_BULLET, YELLOW_BULLET, PINK_PHASER, YELLOW_DOUBLE_PHASER, YELLOW_PHASER, RED_PHASER, ORANGE_PHASER
	}

	/** platform sprite map */
	public static int platformMapColumns = 22;
	public static int platformMapRows = 13;

	/** item sprite map */
	public static int itemMapColumns = 5;
	public static int itemMapRows = 2;

	/** ship pieces sprite map */
	public static int pieceMapColumns = 17;
	public static int pieceMapRows = 27;
	
	/** bullet sprite map */
	public static int bulletMapColumns = 3;
	public static int bulletMapRows = 3;

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