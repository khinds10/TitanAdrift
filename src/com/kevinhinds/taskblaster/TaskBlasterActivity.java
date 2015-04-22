package com.kevinhinds.taskblaster;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
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
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class TaskBlasterActivity extends SimpleBaseGameActivity {

	/**
	 * CONSTANTS
	 */
	private final int CAMERA_WIDTH = 720;
	private final int CAMERA_HEIGHT = 480;

	public enum Action {
		MOVELEFT, MOVERIGHT, STOP, SHOOTRIGHT, SHOOTLEFT;
	}

	protected Action lastdirection;

	/**
	 * FIELDS
	 */
	private Camera camera;

	private BitmapTextureAtlas playerTextureAtlas;
	private TiledTextureRegion playerTextureRegion;

	private AnimatedSprite player;
	private Body playerBody;
	private int playerSpeed = 100;

	private PhysicsWorld physicsWorld;
	private Scene scene;
	private HUD gameHUD;
	protected VertexBufferObjectManager vbom;

	private BitmapTextureAtlas jumpButtonTexture;
	private TextureRegion jumpButtonTextureRegion;

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
		playerTextureAtlas = new BitmapTextureAtlas(getTextureManager(), 512, 256, TextureOptions.BILINEAR);
		playerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(playerTextureAtlas, this, "character/player.png", 0, 0, 8, 3);
		playerTextureAtlas.load();

		jumpButtonTexture = new BitmapTextureAtlas(this.getTextureManager(), 64, 64, TextureOptions.BILINEAR);
		jumpButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(jumpButtonTexture, this, "controls/onscreen_control_knob.png", 0, 0);
		jumpButtonTexture.load();
	}

	@Override
	public Scene onCreateScene() {
		mEngine.registerUpdateHandler(new FPSLogger());

		physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);

		scene = new Scene();
		scene.setBackground(new Background(0, 0, 0));

		final VertexBufferObjectManager vertexBufferObjectManager = getVertexBufferObjectManager();
		final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle left = new Rectangle(0, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);
		final Rectangle right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.0f, 0.0f);
		PhysicsFactory.createBoxBody(physicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(physicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(physicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(physicsWorld, right, BodyType.StaticBody, wallFixtureDef);

		final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.0f, 0.0f);

		player = new AnimatedSprite(300, 100, playerTextureRegion, getVertexBufferObjectManager());
		playerBody = PhysicsFactory.createBoxBody(physicsWorld, player, BodyType.DynamicBody, objectFixtureDef);

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(player, playerBody, true, true));

		player.animate(new long[] { 100 }, new int[] { 22 });

		player.setUserData(playerBody);
		scene.attachChild(player);
		scene.attachChild(ground);
		scene.attachChild(roof);
		scene.attachChild(left);
		scene.attachChild(right);
		scene.registerUpdateHandler(physicsWorld);
		createHUD();

		return scene;
	}

	private void createHUD() {

		gameHUD = new HUD();
		gameHUD.setTouchAreaBindingOnActionDownEnabled(true);
		gameHUD.setTouchAreaBindingOnActionMoveEnabled(true);

		final Sprite jumpButton = new Sprite(CAMERA_WIDTH - 50 - jumpButtonTextureRegion.getWidth(), CAMERA_HEIGHT - jumpButtonTextureRegion.getHeight() - 30, jumpButtonTextureRegion, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					float jumpmotion = 0;
					if (lastdirection == Action.MOVERIGHT) {
						jumpmotion = 4;
						player.animate(new long[] { 100 }, new int[] { 20 });
					}
					if (lastdirection == Action.MOVELEFT) {
						jumpmotion = -4;
						player.animate(new long[] { 100 }, new int[] { 21 });
					}
					final Vector2 velocity = Vector2Pool.obtain(jumpmotion, -6);
					playerBody.setLinearVelocity(velocity);
					return true;
				}
				return true;
			};
		};

		final Sprite shootButton = new Sprite(CAMERA_WIDTH - 50 - jumpButtonTextureRegion.getWidth(), CAMERA_HEIGHT - jumpButtonTextureRegion.getHeight() - jumpButtonTextureRegion.getHeight() - 60, jumpButtonTextureRegion, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {

				}
				return true;
			};
		};

		final Sprite leftArrowButton = new Sprite(15, CAMERA_HEIGHT - jumpButtonTextureRegion.getHeight() - 30, jumpButtonTextureRegion, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					lastdirection = Action.MOVELEFT;
					player.animate(new long[] { playerSpeed, playerSpeed, playerSpeed, playerSpeed, playerSpeed, playerSpeed, playerSpeed, playerSpeed }, 0, 7, true);
					playerBody.setLinearVelocity(-4, 0);
				} else if (pSceneTouchEvent.isActionUp()) {
					lastdirection = Action.STOP;
					player.animate(new long[] { 100 }, new int[] { 17 });
					playerBody.setLinearVelocity(0, 0);
				}
				return true;
			};
		};

		final Sprite rightArrowButton = new Sprite(jumpButtonTextureRegion.getWidth() + (60 + 15), CAMERA_HEIGHT - jumpButtonTextureRegion.getHeight() - 30, jumpButtonTextureRegion, vbom) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					lastdirection = Action.MOVERIGHT;
					player.animate(new long[] { playerSpeed, playerSpeed, playerSpeed, playerSpeed, playerSpeed, playerSpeed, playerSpeed, playerSpeed }, 8, 15, true);
					playerBody.setLinearVelocity(4, 0);
				} else if (pSceneTouchEvent.isActionUp()) {
					lastdirection = Action.STOP;
					player.animate(new long[] { 100 }, new int[] { 16 });
					playerBody.setLinearVelocity(0, 0);
				}

				return true;
			};
		};
		gameHUD.attachChild(jumpButton);
		gameHUD.attachChild(shootButton);
		gameHUD.attachChild(leftArrowButton);
		gameHUD.attachChild(rightArrowButton);
		gameHUD.registerTouchArea(jumpButton);
		gameHUD.registerTouchArea(shootButton);
		gameHUD.registerTouchArea(leftArrowButton);
		gameHUD.registerTouchArea(rightArrowButton);
		camera.setHUD(gameHUD);
	}
}