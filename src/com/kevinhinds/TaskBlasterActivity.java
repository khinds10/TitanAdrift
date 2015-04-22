package com.kevinhinds;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.hardware.SensorManager;
import android.opengl.GLES20;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ContactListener;
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

	private Sprite jumpButton;
	private BitmapTextureAtlas jumpButtonTexture;
	private TextureRegion jumpButtonTextureRegion;

	private Sprite shootButton;
	private BitmapTextureAtlas shootButtonTexture;
	private TextureRegion shootButtonTextureRegion;

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
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);
		return engineOptions;
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		playerTexture = new BitmapTextureAtlas(getTextureManager(), 512, 256, TextureOptions.BILINEAR);
		playerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(playerTexture, this, "character/player.png", 0, 0, 8, 3);
		playerTexture.load();

		jumpButtonTexture = new BitmapTextureAtlas(this.getTextureManager(), 64, 64, TextureOptions.BILINEAR);
		jumpButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(jumpButtonTexture, this, "controls/onscreen_control_knob.png", 0, 0);
		jumpButtonTexture.load();

		shootButtonTexture = new BitmapTextureAtlas(this.getTextureManager(), 64, 64, TextureOptions.BILINEAR);
		shootButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shootButtonTexture, this, "controls/onscreen_control_knob.png", 0, 0);
		shootButtonTexture.load();

		levelTexture = new BitmapTextureAtlas(getTextureManager(), 128, 256, TextureOptions.REPEATING_NEAREST);
		levelTexture.load();

		onScreenControlTexture = new BitmapTextureAtlas(getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		onScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(onScreenControlTexture, this, "controls/onscreen_control_base.png", 0, 0);
		onScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(onScreenControlTexture, this, "controls/onscreen_control_knob.png", 128, 0);
		onScreenControlTexture.load();
	}

	@Override
	public Scene onCreateScene() {
		mEngine.registerUpdateHandler(new FPSLogger());
		scene = new Scene();
		scene.setBackground(new Background(0, 0, 0));
		physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		physicsWorld.setContactListener(contactListener());
		initPlayer();
		initOnScreenControls();
		
		/** create the borders of the screen */
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle left = new Rectangle(0, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);
		final Rectangle right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(physicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(physicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(physicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(physicsWorld, right, BodyType.StaticBody, wallFixtureDef);

		scene.attachChild(ground);
		scene.attachChild(roof);
		scene.attachChild(left);
		scene.attachChild(right);
		
		scene.registerUpdateHandler(physicsWorld);
		return scene;
	}

	private ContactListener contactListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onGameCreated() {

	}

	/**
	 * create the screen controls with the
	 */
	private void initOnScreenControls() {

		final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(0, CAMERA_HEIGHT - onScreenControlBaseTextureRegion.getHeight(), camera, onScreenControlBaseTextureRegion, onScreenControlKnobTextureRegion, 0.1f, getVertexBufferObjectManager(), new IAnalogOnScreenControlListener() {
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
							player.animate(new long[] { 100 }, new int[] { 16 });
						} else {
							player.animate(new long[] { 100 }, new int[] { 17 });
						}
						playerDirection = PlayerDirection.NONE;
					}
				}

				final Body playerBody = TaskBlasterActivity.this.playerBody;
				final Vector2 velocity = Vector2Pool.obtain(pValueX * 5, 0);
				playerBody.setLinearVelocity(velocity);
			}

			@Override
			public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {

			}
		});

		analogOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		analogOnScreenControl.getControlBase().setAlpha(0.5f);
		analogOnScreenControl.getControlBase().setScaleCenter(0, 128);
		analogOnScreenControl.refreshControlKnobPosition();

		/** create the jump button */
		jumpButton = new Sprite(CAMERA_WIDTH - jumpButtonTextureRegion.getWidth(), (CAMERA_HEIGHT - jumpButtonTextureRegion.getHeight() - 30), jumpButtonTextureRegion, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					final Body playerBody = TaskBlasterActivity.this.playerBody;
					final Vector2 velocity = Vector2Pool.obtain(0, -20);
					playerBody.setLinearVelocity(velocity);
					return true;
				}
				return true;
			};
		};
		scene.registerTouchArea(jumpButton);
		scene.attachChild(jumpButton);

		/** create the jump button */
		shootButton = new Sprite(CAMERA_WIDTH - shootButtonTextureRegion.getWidth() - 100, (CAMERA_HEIGHT - shootButtonTextureRegion.getHeight() - 30), shootButtonTextureRegion, getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionDown()) {
	
				}
				return true;
			};
		};
		scene.registerTouchArea(shootButton);
		scene.attachChild(shootButton);
		scene.setChildScene(analogOnScreenControl);
	}

	/**
	 * create the player
	 */
	private void initPlayer() {
		player = new AnimatedSprite(200, 200, PLAYER_SIZE, PLAYER_SIZE, playerTextureRegion, getVertexBufferObjectManager());
		final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(10, 0.5f, 0.5f);
		playerBody = PhysicsFactory.createBoxBody(physicsWorld, player, BodyType.DynamicBody, playerFixtureDef);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(player, playerBody, true, false));
		
		playerBody.setGravityScale(5f);
		
		scene.attachChild(player);
	}
}