package com.kevinhinds.taskblaster;

/**
 * all constant values for game here pretty please
 * 
 * @author khinds
 */
public class GameConfiguation {

	/**
	 * player animated sprite tile frames
	 */
	public static int playerAnimationSpeed = 100;
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
	
	/**
	 * player sprite velocities
	 */
	public static int playerWalkingVelocity = 4;
	
	public static int playerJumpVelocity = -7;
	public static int playerFallingVelocity = 4;
	public static int playerFallingChangeDirectionVelocity = 1;
	public static int playerStopWhileFallingVelocity = 2;
	public static int playerContinueMovingWhileFallingVelocity = 2;
	public static int playerFallingContinueDirectionVelocity = 3;
	
	/**
	 * sprite basic state indentifiers
	 */
	public static enum State {
		UP, DOWN, LEFT, RIGHT, STOP
	}
}