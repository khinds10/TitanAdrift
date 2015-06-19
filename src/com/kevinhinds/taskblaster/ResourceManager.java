package com.kevinhinds.taskblaster;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import com.kevinhinds.taskblaster.tiles.TileManager;
import com.kevinhinds.taskblaster.villains.VillainManager;

/**
 * deal with game resources via singleton design pattern for reuseability
 * 
 * @author khinds
 */
public class ResourceManager {

	private static final ResourceManager INSTANCE = new ResourceManager();

	/** universal resources */
	public Engine engine;
	public TaskBlasterActivity activity;
	public Camera camera;
	public VertexBufferObjectManager vbom;

	/** menu regions */
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	public ITextureRegion play_button_region;
	public ITextureRegion exit_button_region;

	/** player region */
	public ITiledTextureRegion player_region;

	/** weapon region */
	public ITiledTextureRegion bullet_region;
	
	/** adversary region */
	public ITiledTextureRegion adversary_region;	

	/** controls regions */
	private BuildableBitmapTextureAtlas gameTextureAtlas;
	public ITextureRegion control_jump_region;
	public ITextureRegion control_shoot_region;
	public ITextureRegion control_left_region;
	public ITextureRegion control_right_region;

	/** level regions */
	private BuildableBitmapTextureAtlas tileTextureAtlas;
	public ITiledTextureRegion platform_region;
	public TileManager tileManager;
	public VillainManager villainManager;

	/**
	 * load resources to create the menu into memory
	 */
	public void loadMenuResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		play_button_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play_button.png");
		exit_button_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "exit_button.png");
		try {
			menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			menuTextureAtlas.load();
		} catch (Exception e) {
			Debug.e(e);
		}
	}

	/**
	 * unload the menu after you've started the game scene
	 */
	public void unloadMenuResources() {
		menuTextureAtlas.unload();
		menuTextureAtlas = null;
	}

	/**
	 * load resources for the game into memory
	 */
	public void loadGameResources() {
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.BILINEAR);
		player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "character/player.png", GameConfiguation.playerMapColumns, GameConfiguation.playerMapRows);
		adversary_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "adversary/creatures.png", GameConfiguation.villainMapColumns, GameConfiguation.villainMapRows);		
		control_jump_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "controls/jump.png");
		control_left_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "controls/left.png");
		control_right_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "controls/right.png");
		control_shoot_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "controls/shoot.png");
		bullet_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "weapons/bullet.png", 4, 1);
		try {
			gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			gameTextureAtlas.load();
		} catch (Exception e) {
			Debug.e(e);
		}
	}

	/**
	 * load game tiles into memory for usage in multiple game scenes
	 */
	public void loadTileResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/tiles/");
		tileTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		platform_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(tileTextureAtlas, activity, "platforms.png", 22, 13);
		try {
			tileTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			tileTextureAtlas.load();
		} catch (Exception e) {
			Debug.e(e);
		}
	}

	/**
	 * unload the game resource if return to main menu
	 */
	public void unloadGameResources() {
		gameTextureAtlas.unload();
		gameTextureAtlas = null;
	}

	/**
	 * load game fonts
	 */
	public void loadFonts() {

	}

	/**
	 * produce the local tile manager to render level tiles via XML based descriptions
	 */
	public void loadTileManager() {
		loadTileResources();
		tileManager = new TileManager(vbom);
		villainManager = new VillainManager(vbom);
	}

	/**
	 * prepare the manager to use via singleton in the future
	 * 
	 * @param engine
	 * @param activity
	 * @param camera
	 * @param vbom
	 */
	public static void prepareManager(Engine engine, TaskBlasterActivity activity, Camera camera, VertexBufferObjectManager vbom) {
		getIntance().engine = engine;
		getIntance().activity = activity;
		getIntance().camera = camera;
		getIntance().vbom = vbom;
	}

	/**
	 * get existing resources from the resource manager via singleton
	 * 
	 * @return
	 */
	public static ResourceManager getIntance() {
		return INSTANCE;
	}
}