package com.kevinhinds.spacebots.player;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;

import com.kevinhinds.spacebots.GameConfiguration;
import com.kevinhinds.spacebots.ResourceManager;
import com.kevinhinds.spacebots.scene.GameScene;

/**
 * create controls for the character in the game
 * 
 * @author khinds
 */
public class Controls {

	private GameScene scene;
	private Player player;
	public TokenButton[] abilityButton;
	int abilityButtonHorizontalStart = 250;
	int abilityButtonHorizontalNext = 70;

	/**
	 * create the controls for the scene
	 * 
	 * @param scene
	 * @param player
	 */
	public Controls(GameScene scene, Player player) {
		this.scene = scene;
		this.player = player;
		createPlayerJoystick();
		createPlayerJump();
		createPlayerShoot();
		createPlayerAbilityButtons();
	}

	public void createPlayerAbilityButtons() {
		abilityButton = new TokenButton[6];
		int horizontalPosition = abilityButtonHorizontalStart;
		for (int i = 0; i < abilityButton.length; i++) {

			abilityButton[i] = new TokenButton(i, 0, i, scene.camera.getWidth() - horizontalPosition, scene.camera.getHeight() - 65, ResourceManager.getIntance().itemButtonRegion, scene.vbom) {
				@Override
				public boolean onAreaTouched(final TouchEvent event, final float x, final float y) {
					if (event.isActionDown()) {
						if (countAvailable > 0) {
							switch (this.playerAbilityID) {
							case 0: // RELOAD
								use();
								break;
							case 1: // FLOAT
								use();
								break;
							case 2: // JUMP
								/** use the pressed token ability to super jump */
								if (!player.isJumping) {
									use();
									player.tokenAbilityJump();
								}
								break;
							case 3: // LIFE
								use();
								break;
							case 4: // FREEZE
								use();
								break;
							case 5: // POWER
								use();
								break;
							default:
								break;
							}
						}
					}
					return true;
				}

				@Override
				protected void preDraw(GLState glstate, Camera camera) {
					super.preDraw(glstate, camera);
					glstate.enableDither();
				}
			};
			abilityButton[i].attachToHUD(this.scene);
			horizontalPosition += abilityButtonHorizontalNext;
		}
	}

	/**
	 * based on token id collected from game level, update the cooresponding control to show it's available
	 * @param tokenCollected
	 */
	public void updatePlayerAbilityButtons(int tokenCollected) {
		abilityButton[tokenCollected].aquire();
	}

	/**
	 * create the (d-pad) style joystick
	 */
	private void createPlayerJoystick() {

		Sprite right = new Sprite(90, scene.camera.getHeight() - 80, ResourceManager.getIntance().controlRightRegion, scene.vbom) {

			@Override
			public boolean onAreaTouched(final TouchEvent event, final float x, final float y) {
				if (event.isActionDown()) {
					player.moveRight();
				} else if (event.isActionUp()) {
					player.stop();
				}
				return true;
			}

			@Override
			protected void preDraw(GLState glstate, Camera camera) {
				super.preDraw(glstate, camera);
				glstate.enableDither();
			}
		};
		scene.gameHUD.registerTouchArea(right);
		scene.gameHUD.attachChild(right);

		Sprite left = new Sprite(10, scene.camera.getHeight() - 80, ResourceManager.getIntance().controlLeftRegion, scene.vbom) {

			@Override
			public boolean onAreaTouched(final TouchEvent event, final float x, final float y) {
				if (event.isActionDown()) {
					player.moveLeft();
				} else if (event.isActionUp()) {
					player.stop();
				}
				return true;
			}

			@Override
			protected void preDraw(GLState glstate, Camera camera) {
				super.preDraw(glstate, camera);
				glstate.enableDither();
			}
		};
		scene.gameHUD.registerTouchArea(left);
		scene.gameHUD.attachChild(left);

	}

	/**
	 * create the player jump button the game HUD
	 */
	private void createPlayerJump() {
		Sprite jump = new Sprite(scene.camera.getWidth() - 90, scene.camera.getHeight() - 80, ResourceManager.getIntance().controlJumpRegion, scene.vbom) {

			@Override
			public boolean onAreaTouched(final TouchEvent event, final float x, final float y) {
				if (event.isActionDown()) {
					player.jump(GameConfiguration.playerJumpVelocity);
				}
				return true;
			}

			@Override
			protected void preDraw(GLState glstate, Camera camera) {
				super.preDraw(glstate, camera);
				glstate.enableDither();
			}
		};
		scene.gameHUD.registerTouchArea(jump);
		scene.gameHUD.attachChild(jump);
	}

	/**
	 * create the player shoot button the game HUD
	 */
	private void createPlayerShoot() {
		Sprite shoot = new Sprite(scene.camera.getWidth() - 175, scene.camera.getHeight() - 80, ResourceManager.getIntance().controlShootRegion, scene.vbom) {

			@Override
			public boolean onAreaTouched(final TouchEvent event, final float x, final float y) {
				if (event.isActionDown()) {
					player.shoot(scene);
				}
				return true;
			}

			@Override
			protected void preDraw(GLState glstate, Camera camera) {
				super.preDraw(glstate, camera);
				glstate.enableDither();
			}
		};
		scene.gameHUD.registerTouchArea(shoot);
		scene.gameHUD.attachChild(shoot);
	}
}