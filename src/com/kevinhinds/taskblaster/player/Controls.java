package com.kevinhinds.taskblaster.player;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import com.kevinhinds.taskblaster.ResourceManager;
import com.kevinhinds.taskblaster.scene.GameScene;

/**
 * create controls for the character in the game
 * 
 * @author khinds
 */
public class Controls {

	GameScene scene;
	Player player;

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
	}

	/**
	 * create the (d-pad) style joystick
	 */
	private void createPlayerJoystick() {

		Sprite right = new Sprite(120, scene.camera.getHeight() - 75, ResourceManager.getIntance().control_right_region, scene.vbom) {

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

		Sprite left = new Sprite(15, scene.camera.getHeight() - 75, ResourceManager.getIntance().control_right_region, scene.vbom) {

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
		Sprite jump = new Sprite(scene.camera.getWidth() - 100, scene.camera.getHeight() - 75, ResourceManager.getIntance().control_jump_region, scene.vbom) {

			@Override
			public boolean onAreaTouched(final TouchEvent event, final float x, final float y) {
				if (event.isActionDown()) {
					player.jump();
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
}