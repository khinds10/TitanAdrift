package com.kevinhinds.taskblaster.scene;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.color.Color;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.kevinhinds.taskblaster.MaxStepPhysicsWorld;
import com.kevinhinds.taskblaster.ResourceManager;
import com.kevinhinds.taskblaster.level.LevelManager;

public class GameScene extends BaseScene {

	private HUD gameHUD;
	private PhysicsWorld physicsWorld;
	private Body playerBody;
	private Sprite playerSprite;
	private LevelManager levelManager;

	@Override
	public void createScene() {
		levelManager = new LevelManager(activity.getAssets());
		setBackground();
		createHUD();
		createControls();
		createPhysics();
		addPlayer();
		createLevel();
		//camera.setChaseEntity(playerSprite);
	}

	@Override
	public void onBackPressed() {
		System.exit(0);
	}

	@Override
	public void disposeScene() {
		ResourceManager.getIntance().unloadGameResources();
	}

	private void setBackground() {
		setBackground(new Background(Color.BLACK));
	}

	private void createHUD() {
		gameHUD = new HUD();
		createJumpControls();
		camera.setHUD(gameHUD);
	}

	private void createPhysics() {
		physicsWorld = new MaxStepPhysicsWorld(60, new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		registerUpdateHandler(physicsWorld);
	}

	private void addPlayer() {
		final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(0.5f, 0.0f, 0.75f);
		playerSprite = createSprite(50, 50, ResourceManager.getIntance().player_region, vbom);
		playerBody = PhysicsFactory.createBoxBody(physicsWorld, playerSprite, BodyType.DynamicBody, playerFixtureDef);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(playerSprite, playerBody, true, false));
		attachChild(playerSprite);
	}

	private void createLevel() {
		
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.0f, 0.0f);
		final Rectangle ground = new Rectangle(0, camera.getHeight() - 2, camera.getWidth(), 2, vbom);
		final Rectangle roof = new Rectangle(0, 0, camera.getWidth(), 2, vbom);
		final Rectangle left = new Rectangle(0, 0, 2, camera.getHeight(), vbom);
		final Rectangle right = new Rectangle(camera.getWidth() - 2, 0, 2, camera.getHeight(), vbom);		

		PhysicsFactory.createBoxBody(physicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(physicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(physicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(physicsWorld, right, BodyType.StaticBody, wallFixtureDef);
		
		attachChild(ground);
		attachChild(roof);
		attachChild(left);
		attachChild(right);
		
		levelManager.loadLevel(1, this, physicsWorld);
	}

	private void createControls() {
		final DigitalOnScreenControl control = new DigitalOnScreenControl(20, camera.getHeight() - ResourceManager.getIntance().control_base_region.getHeight() - 5, camera, ResourceManager.getIntance().control_base_region, ResourceManager.getIntance().control_knob_region, 0.1f, vbom, new IOnScreenControlListener() {
			@Override
			public void onControlChange(BaseOnScreenControl control, float x, float y) {
				if (playerBody.getLinearVelocity().x > -4 && playerBody.getLinearVelocity().x < 4) {
					if (x > 0) // right
						playerBody.setLinearVelocity(4.0f, playerBody.getLinearVelocity().y);
					else if (x < 0) // left
						playerBody.setLinearVelocity(-4.0f, playerBody.getLinearVelocity().y);
					else
						playerBody.setLinearVelocity(0f, playerBody.getLinearVelocity().y);
				}
			}
		});

		control.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		control.getControlBase().setAlpha(0.5f);
		control.getControlBase().setScaleCenter(0, 128);
		control.getControlBase().setScale(1.25f);
		control.getControlKnob().setScale(1.25f);
		control.refreshControlKnobPosition();
		setChildScene(control);

	}

	private void createJumpControls() {
		Sprite jump = new Sprite(camera.getWidth() - 100, camera.getHeight() - 75, ResourceManager.getIntance().control_jump_region, vbom) {

			@Override
			public boolean onAreaTouched(final TouchEvent event, final float x, final float y) {
				if (event.isActionUp()) {
					playerBody.applyLinearImpulse(new Vector2(0, -15), playerBody.getPosition());
				}
				return true;
			}

			@Override
			protected void preDraw(GLState glstate, Camera camera) {
				super.preDraw(glstate, camera);
				glstate.enableDither();
			}

		};
		gameHUD.registerTouchArea(jump);
		gameHUD.attachChild(jump);
	}
}
