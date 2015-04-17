package com.kevinhinds;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.opengl.GLES20;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * blast your tasks in style, with taskblaster
 * 
 * @author khinds
 */
public class TaskBlasterActivity extends SimpleBaseGameActivity {

	/**
	 * CONSTANTS
	 */
	private static final int PLAYER_SIZE = 50;

	private final int CAMERA_WIDTH = 720;
	private final int CAMERA_HEIGHT = 480;

	/**
	 * FIELDS
	 */
	private Camera camera;

	private BitmapTextureAtlas levelTexture;

	private BitmapTextureAtlas onScreenControlTexture;
	private ITextureRegion onScreenControlBaseTextureRegion;
	private ITextureRegion onScreenControlKnobTextureRegion;

	private enum PlayerDirection {
		NONE, UP, DOWN, LEFT, RIGHT
	}

	private PlayerDirection playerDirection = PlayerDirection.NONE;

	private Scene scene;

	private PhysicsWorld physicsWorld;

	private Body playerBody;
	private AnimatedSprite player;
	private BitmapTextureAtlas playerTexture;
	private TiledTextureRegion playerTextureRegion;

	@Override
	public EngineOptions onCreateEngineOptions() {
		this.camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.camera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.playerTexture = new BitmapTextureAtlas(this.getTextureManager(), 512, 256, TextureOptions.BILINEAR);
		this.playerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.playerTexture, this, "character/player.png", 0, 0, 8, 3);
		this.playerTexture.load();

		this.levelTexture = new BitmapTextureAtlas(this.getTextureManager(), 128, 256, TextureOptions.REPEATING_NEAREST);
		this.levelTexture.load();

		this.onScreenControlTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.onScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.onScreenControlTexture, this, "controls/onscreen_control_base.png", 0, 0);
		this.onScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.onScreenControlTexture, this, "controls/onscreen_control_knob.png", 128, 0);
		this.onScreenControlTexture.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.scene = new Scene();
		this.scene.setBackground(new Background(0, 0, 0));

		this.physicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0), false, 8, 1);

		this.initPlayer();
		this.initOnScreenControls();

		this.scene.registerUpdateHandler(this.physicsWorld);

		return this.scene;
	}

	@Override
	public void onGameCreated() {

	}

	/**
	 * create the screen controls with the
	 */
	private void initOnScreenControls() {

		final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(0, CAMERA_HEIGHT - this.onScreenControlBaseTextureRegion.getHeight(), this.camera, this.onScreenControlBaseTextureRegion, this.onScreenControlKnobTextureRegion, 0.1f, this.getVertexBufferObjectManager(), new IAnalogOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				
				int playerSpeed = 100;
				
				if (pValueX < 0) {
					/** Left */
					if (playerDirection != PlayerDirection.LEFT) {
						player.animate(new long[] { playerSpeed, playerSpeed, playerSpeed, playerSpeed, playerSpeed, playerSpeed, playerSpeed, playerSpeed }, 0, 7, true);
						playerDirection = PlayerDirection.LEFT;
					}
				} else if (pValueX > 0) {
					/** Right */
					if (playerDirection != PlayerDirection.RIGHT) {
						player.animate(new long[] { playerSpeed, playerSpeed, playerSpeed, playerSpeed, playerSpeed, playerSpeed, playerSpeed, playerSpeed }, 8, 15, true);
						playerDirection = PlayerDirection.RIGHT;
					}
				} else {
					if (playerDirection != PlayerDirection.NONE) {
						if (playerDirection == PlayerDirection.RIGHT) {
							player.animate(new long[] { 10000000, 10000000 }, 16, 17, true);
						} else {
							player.animate(new long[] { 10000000, 10000000 }, 17, 18, true);
						}
						playerDirection = PlayerDirection.NONE;
					}
				}

				final Body playerBody = TaskBlasterActivity.this.playerBody;
				final Vector2 velocity = Vector2Pool.obtain(pValueX * 5, 0);
				playerBody.setLinearVelocity(velocity);
				Vector2Pool.recycle(velocity);
			}

			@Override
			public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
			}
		});

		analogOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		analogOnScreenControl.getControlBase().setAlpha(0.5f);
		analogOnScreenControl.getControlBase().setScaleCenter(0, 128);
		analogOnScreenControl.refreshControlKnobPosition();

		this.scene.setChildScene(analogOnScreenControl);
	}

	/**
	 * create the player
	 */
	private void initPlayer() {

		player = new AnimatedSprite(200, 200, PLAYER_SIZE, PLAYER_SIZE, this.playerTextureRegion, this.getVertexBufferObjectManager());

		final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		playerBody = PhysicsFactory.createBoxBody(this.physicsWorld, this.player, BodyType.DynamicBody, playerFixtureDef);

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this.player, this.playerBody, true, false));

		scene.attachChild(this.player);
	}
}